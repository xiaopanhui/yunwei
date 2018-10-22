package com.gzsf.operation.service;

import com.gzsf.operation.FileUtils;
import com.gzsf.operation.Utils;
import com.gzsf.operation.cache.FileCache;
import com.gzsf.operation.cache.ServiceCache;
import com.gzsf.operation.exception.*;
import com.gzsf.operation.model.FileVersionModel;
import com.gzsf.operation.model.ServiceModel;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class ProcessService extends MonoService {
    private final Map<Long,Long> pidMap;
    private final ExecutorService executorService= Executors.newSingleThreadExecutor();
    private final FileUtils fileUtils;
    private ProcessLibrary processLibrary;
    @Autowired
    private ServiceCache serviceCache;
    @Autowired
    private FileCache fileCache;
    public interface ProcessLibrary extends Library {
        long run_cmd(String path,String cmd,String cwd);
        int kill_proc(long pid, int code);
        int proc_status(long pid);
    }



    @Autowired
    public ProcessService(FileUtils fileUtils){
        this.fileUtils=fileUtils;
        pidMap=new HashMap<>();
        executorService.submit(this::update);
        init();
    }

    private void init(){
        String arc;
        String ext;
        switch (Platform.getOSType()){
            case Platform.MAC: {
                arc = "mac";
                ext = "dylib";
                break;
            }
            case Platform.WINDOWS:{
                arc="win";
                ext="dll";
                break;
            }
            default: {
                arc = "linux";
                ext = "so";
                break;
            }
        }
        String path=String.format("/clibs/%s_%d/libprocess.%s",arc, Platform.is64Bit()?64:32,ext);
        processLibrary =  Native.loadLibrary(path,ProcessLibrary.class);
    }
    private void save(Long serviceId,Long pid){
        if (pid==0)return;
        pidMap.put(serviceId,pid);
        this.executorService.submit(()->{
            File file= fileUtils.getPidFile(serviceId);
            if (file==null)return;
            FileOutputStream outputStream= null;
            try {
                outputStream = new FileOutputStream(file);
                outputStream.write(pid.toString().getBytes(Charset.forName("utf8")));
            } catch (Exception e) {

            }finally {
                Utils.close(outputStream);
            }

        });
    }
    public Mono startService(final Long serviceId) {
        return async(() -> {
            ServiceModel serviceModel = serviceCache.getRecord(serviceId);
            if (serviceModel == null) {
                throw new NoServiceFoundException();
            }
            FileVersionModel fileVersionModel = fileCache.getFileVersion(serviceModel.getFileId(), serviceModel.getFileVersion());
            if (fileVersionModel == null) {
                throw new NoFileFoundException();
            }
            if (pidMap.containsKey(serviceId)){
                throw new ServiceRunningException();
            }
            String cmd= getStartCmd(serviceModel,fileVersionModel.getVersion());
            Long pid= this.runCmd(cmd,fileUtils.getWordDirPath(serviceId));
            save(serviceId,pid);
            return pid;
        });
    }

    public Mono<Boolean> stopService(final Long serviceId) {
        return async(() -> {
            ServiceModel serviceModel = serviceCache.getRecord(serviceId);
            if (serviceModel == null) {
                throw new NoServiceFoundException();
            }
            FileVersionModel fileVersionModel = fileCache.getFileVersion(serviceModel.getFileId(), serviceModel.getFileVersion());
            if (fileVersionModel == null) {
                throw new NoFileFoundException();
            }
            if (pidMap.containsKey(serviceId)){
                throw new ServiceRunningException();
            }
            String cmd= serviceModel.getStopCmd();
            if (Utils.isEmpty(cmd)){
                if(!pidMap.containsKey(serviceId)){
                    throw  new ServiceNotRunningException();
                }
                return this.stopProcess(pidMap.get(serviceId));
            }else {
                return Runtime.getRuntime().exec(cmd).exitValue()==0;
            }
        });
    }
    public void update(){
        byte[] bytes=new byte[128];
        File file=new File(this.fileUtils.getBaseWorkDir());
        if (file.exists()){
            File[] files= file.listFiles();
            if (files==null)return;
            for (File temp:files){
                File pid=new File(temp,"pid");
                if (pid.exists()){
                    try {
                        FileInputStream inputStream=new FileInputStream(pid);
                        int size= inputStream.read(bytes);
                        inputStream.close();
                        if (size==0)continue;
                        long p=Long.valueOf(new String(bytes,0,size));
                        if (p==0)continue;
                        pidMap.put(Long.valueOf(temp.getName()),p);
                    }catch (Exception e){

                    }
                }
            }
        }
    }


    private String getStartCmd(ServiceModel serviceModel, int realVersion) throws Exception{
        String cmd=serviceModel.getStartCmd();
        String filePath= fileUtils.getFilePath(serviceModel.getFileId(),realVersion);

        if (Utils.isEmpty(cmd)){
            switch (serviceModel.getType()){
                case PY2:{
                    cmd="python2 "+filePath;
                    break;
                }
                case PY3:{
                    cmd="python3 "+filePath;
                    break;
                }
                case EXE:{
                    cmd="";
                    break;
                }
                case JAR:{
                    cmd="java -jar "+filePath;
                    break;
                }
                case SHELL:{
                    cmd="sh "+filePath;
                    break;
                }
                case OTHER:{
                    throw new NoCmdToRunException();
                }
            }
        }else {
            Map<String, Object> params = new HashMap<>();
            String executeDir= fileUtils.getWordDirPath(serviceModel.getServiceId());
            params.put("execute_dir", executeDir);
            params.put("file_path", filePath);
            cmd = Utils.parseVTL(cmd, params);
        }
        return cmd;
    }

    public long getPid(long serviceId){
        return pidMap.getOrDefault(serviceId,0L);
    }

    @Scheduled( fixedDelay = 30000)
    public void refreshProcessService(){
        for (Map.Entry<Long,Long> entry:pidMap.entrySet()){
            if(isRunning(entry.getValue())!=1){
                if(fileUtils.getPidFile(entry.getKey()).delete()){
                    pidMap.remove(entry.getKey());
                }
            }
        }
    }

    /**
     *  运行 外部 命令
     * @param cmd 命令
     * @param cwd 运行目录
     * @return pid
     */
    private long runCmd(String cmd,String cwd){
        String[] strs= cmd.split(" ");
        return this.processLibrary.run_cmd(strs[0],cmd,cwd);
    }

    /**
     * 停止进程
     * @param pid 进程id
     * @return 是否成功
     */
    private boolean stopProcess(long pid){
        return this.processLibrary.kill_proc(pid,0)==0?false:true;
    }
    /**
     * 检查进程 存活
     * @param pid 进程id
     * @return 状态码
     */
    private int isRunning(long pid){
        return this.processLibrary.proc_status(pid);
    }

}
