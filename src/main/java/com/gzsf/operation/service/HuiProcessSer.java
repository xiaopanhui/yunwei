package com.gzsf.operation.service;

import com.gzsf.operation.FileUtils;
import com.gzsf.operation.Utils;
import com.gzsf.operation.cache.FileCache;
import com.gzsf.operation.cache.ServiceCache;
import com.gzsf.operation.exception.NoCmdToRunException;
import com.gzsf.operation.exception.NoFileFoundException;
import com.gzsf.operation.exception.NoServiceFoundException;
import com.gzsf.operation.model.FileVersionModel;
import com.gzsf.operation.model.ServiceModel;
import com.sun.jna.Platform;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Service
public class HuiProcessSer extends MonoService {
    private String command;
    @Autowired
    private FileUtils fileUtils;
    @Autowired
    private ServiceCache serviceCache;
    @Autowired
    private FileCache fileCache;
    private Map<Long, Long> pidMap;



    private String init(){
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
        System.out.println("path:"+path);
        return path;

    }


    //执行成功弹出笔记本页面
    public Mono process(Long serviceId) throws Exception {//ok
        return async(()->{
            //        command = "QQLive.exe";
            ServiceModel serviceModel = serviceCache.getRecord(serviceId);
            FileVersionModel fileVersionModel = fileCache.getFileVersion(serviceModel.getFileId(), serviceModel.getFileVersion());

            if (serviceModel == null) {
                throw new NoServiceFoundException();
            }
            if (fileVersionModel == null) {
                throw new NoFileFoundException();
            }
//
            String cmd = getStartCmd(serviceModel, fileVersionModel.getVersion());

            try {
                //获取path
                File file = new File(init());
                Process process = Runtime.getRuntime().exec(cmd, null, file);
                BufferedInputStream bis = new BufferedInputStream(
                        process.getInputStream());
                BufferedReader br = new BufferedReader(new InputStreamReader(bis));
                String line;
                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                }

                process.waitFor();
                if (process.exitValue() != 0) {
                    System.out.println("error!");
                }

                bis.close();
                br.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        });
    }


    //获取系统 启动的命令
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

}








