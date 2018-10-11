package com.gzsf.operation;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;
import org.springframework.stereotype.Service;

@Service
public class ProcessManager {
    private final ProcessLibrary processLibrary;
    public interface ProcessLibrary extends Library {
        long run_cmd(String path,String cmd,String cwd);
        int kill_proc(long pid, int code);
        int proc_status(long pid);
    }

    public ProcessManager(){
        String arc;
        String ext;
         switch (Platform.getOSType()){
             case Platform.MAC: {
                 arc = "mac_64";
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
        String path=String.format("/clibs/%s_%d/libprocess.%s",arc,Platform.is64Bit()?64:32,ext);
        processLibrary =  Native.loadLibrary(path,ProcessLibrary.class);
    }

    /**
     *  运行 外部 命令
     * @param cmd 命令
     * @param cwd 运行目录
     * @return pid
     */
    public long runCmd(String cmd,String cwd){
        String[] strs= cmd.split("");
        return this.processLibrary.run_cmd(strs[0],cmd,cwd);
    }

    /**
     * 停止进程
     * @param pid 进程id
     * @return 是否成功
     */
    public boolean stopProcess(long pid){
        return this.processLibrary.kill_proc(pid,0)==0?false:true;
    }
    /**
     * 检查进程 存活
     * @param pid 进程id
     * @return 状态码
     */
    public int isRunning(long pid){
        return this.processLibrary.proc_status(pid);
    }

}
