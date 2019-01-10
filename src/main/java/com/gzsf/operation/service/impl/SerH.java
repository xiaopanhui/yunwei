package com.gzsf.operation.service.impl;

import com.gzsf.operation.FileUtils;
import com.gzsf.operation.Utils;
import com.gzsf.operation.cache.FileCache;
import com.gzsf.operation.cache.ServiceCache;
import com.gzsf.operation.exception.NoCmdToRunException;
import com.gzsf.operation.exception.NoFileFoundException;
import com.gzsf.operation.exception.NoServiceFoundException;
import com.gzsf.operation.model.FileVersionModel;
import com.gzsf.operation.model.ServiceModel;
import com.gzsf.operation.service.MonoService;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class SerH extends MonoService {

    private Map<Long, Long> pidMap;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    @Autowired
    private FileUtils fileUtils;
    @Autowired
    private FileCache fileCache;
    @Autowired
    private ServiceCache serviceCache;


    public Mono runtimeExec(final Long serviceId) {
        return async(() -> {
            ServiceModel serviceModel = serviceCache.getRecord(serviceId);
            if (serviceModel == null) {
                throw new NoServiceFoundException();
            }
            FileVersionModel fileVersionModel = fileCache.getFileVersion(serviceModel.getFileId(), serviceModel.getFileVersion());
            if (fileVersionModel == null) {
                throw new NoFileFoundException();
            }

            String cmd = getStartCmd(serviceModel, fileVersionModel.getVersion());


            Process proc;
            try {
                StringBuilder builder = new StringBuilder();
                builder.append("cmd /c ");//java程序启动不起
//            builder.append(file);
//            builder.append(File.separator);
//            builder.append(cmd);
                Runtime rt = Runtime.getRuntime();
                proc = rt.exec(cmd);
                InputStream stderr = proc.getErrorStream();
                InputStreamReader isr = new InputStreamReader(stderr);
                BufferedReader br = new BufferedReader(isr);
                String line = null;
                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                }
                int exitVal = proc.waitFor();
                return exitVal == 0;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }

        });
    }






    //可以成功，但是去掉流的写入java程序启动错误
    public Mono process(final Long serviceId) {
        return async(() -> {
            boolean flage = false;
            ServiceModel serviceModel = serviceCache.getRecord(serviceId);
            if (serviceModel == null) {
                throw new NoServiceFoundException();
            }
            FileVersionModel fileVersionModel = fileCache.getFileVersion(serviceModel.getFileId(), serviceModel.getFileVersion());
            if (fileVersionModel == null) {
                throw new NoFileFoundException();
            }

            String cmd = getStartCmd(serviceModel, fileVersionModel.getVersion());

            Process process;
            try {
                 process = Runtime.getRuntime().exec(cmd);
                DefaultExecutor exec = new DefaultExecutor();

//                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//
//                ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
//                int pid = exec.execute(commandline);
//                System.out.print("pid------------------------------------------------------"+pid);

                 //读取程序
                BufferedInputStream bis = new BufferedInputStream(process.getInputStream());
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


            return true;



        });
    }



//
//    //2可以成功，但是去掉流的写入java程序启动错误
//    public Mono process(final Long serviceId) {
//        return async(() -> {
//            ServiceModel serviceModel = serviceCache.getRecord(serviceId);
//            if (serviceModel == null) {
//                throw new NoServiceFoundException();
//            }
//            FileVersionModel fileVersionModel = fileCache.getFileVersion(serviceModel.getFileId(), serviceModel.getFileVersion());
//            if (fileVersionModel == null) {
//                throw new NoFileFoundException();
//            }
//
//            String cmd = getStartCmd(serviceModel, fileVersionModel.getVersion());
//
//            try {
//                String command = cmd;
//
//                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//
//                ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
//                CommandLine commandline = CommandLine.parse(command);
//
//
//                DefaultExecutor exec = new DefaultExecutor();
//
//                exec.setExitValues(null);
//
//                PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream,errorStream);
//
//                exec.setStreamHandler(streamHandler);
//
//                int pid = exec.execute(commandline);
//                System.out.print(pid);
//
//                String out = outputStream.toString("gbk");
//
//                String error = errorStream.toString("gbk");
//
//                return out+error;
//
//            } catch (Exception e) {
//
//                return e.toString();
//
//            }
//
//        });
//    }
//





    public static void kill(String name) {//ok
        Runtime runtime = Runtime.getRuntime();
        List tasklist = new ArrayList();
        try {
            Process process = runtime.exec("tasklist");
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String s = "";
            while ((s = br.readLine()) != null) {
                if ("".equals(s)) {
                    continue;
                }
                tasklist.add(s + " ");
            }
            System.out.println(s);
            System.out.println(br);

            String maxRow = tasklist.get(1) + "";//行
            // 获取每列最长的长度
            String[] maxCol = maxRow.split(" ");
            System.out.println(maxCol);

            // 定义映像名称数组
            String[] taskName = new String[tasklist.size()];
            // 定义 PID数组
            String[] taskPid = new String[tasklist.size()];
            // 会话名数组
            String[] taskSessionName = new String[tasklist.size()];
            // 会话#数组
            String[] taskSession = new String[tasklist.size()];
            // 内存使用 数组
            String[] taskNec = new String[tasklist.size()];
            for (int i = 0; i < tasklist.size(); i++) {
                String data = tasklist.get(i) + "";
                System.out.println(data);
                for (int j = 0; j < maxCol.length; j++) {
                    switch (j) {
                        case 0:
                            taskName[i] = data.substring(0, maxCol[j].length() + 1);
                            System.out.println(taskName);
                            data = data.substring(maxCol[j].length() + 1);

                            break;
                        case 1:
                            taskPid[i] = data.substring(0, maxCol[j].length() + 1);
                            System.out.println(taskPid);
                            data = data.substring(maxCol[j].length() + 1);
                            break;
                        case 2:
                            taskSessionName[i] = data.substring(0, maxCol[j].length() + 1);
                            data = data.substring(maxCol[j].length() + 1);
                            break;
                        case 3:
                            taskSession[i] = data.substring(0, maxCol[j].length() + 1);
                            data = data.substring(maxCol[j].length() + 1);
                            break;
                        case 4:
                            taskNec[i] = data;
                            break;
                    }
                }
            }

            int count = 0;
            for (int i = 0; i < taskNec.length; i++) {
                //打印进程列表
                //System.out.println(taskName[i]+" "+taskPid[i]+" "+taskSessionName[i]+" "+taskSession[i]+" "+taskNec[i]);
                //在此处我是根据获取进程名称来杀掉360se.exe这个进程的，你们可以按照你们得需求来写，按照进程名称或进程id来杀进程
                if (taskName[i].contains("360se.exe")) {//notepad++.exe
                    count++;            //用于进程计数
                }
            }
//            //当进程数大于等于2个以上时，要求杀掉
////            if(count>=2) {
////                Process p = null;


            //当进程数大于等于2个以上时，要求杀掉
            Process p = null;
            try {
                //此处需要填写你要杀掉的进程名称
                runtime.exec("cmd.exe /C start wmic process where name='notepad++.exe' call terminate");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //获取系统 启动的命令
    private String getStartCmd(ServiceModel serviceModel, int realVersion) throws Exception {
        String cmd = serviceModel.getStartCmd();
        System.out.println("cmd:"+cmd);
        String filePath = fileUtils.getFilePath(serviceModel.getFileId(), realVersion);

        if (Utils.isEmpty(cmd)) {
            switch (serviceModel.getType()) {
                case PY2: {
                    cmd = "python2 " + filePath;
                    break;
                }
                case PY3: {
                    cmd = "python3 " + filePath;
                    break;
                }
                case EXE: {
                    cmd = "";
                    break;
                }
                case JAR: {
                    cmd = "java -jar " + filePath;
                    break;
                }
                case SHELL: {
                    cmd = "sh " + filePath;
                    break;
                }
                case OTHER: {
                    throw new NoCmdToRunException();
                }
            }
        } else {
            Map<String, Object> params = new HashMap<>();
            String executeDir = fileUtils.getWordDirPath(serviceModel.getServiceId());
            params.put("execute_dir", executeDir);
            params.put("file_path", filePath);
            cmd = Utils.parseVTL(cmd, params);
        }
        return cmd;
    }

}
