package com.gzsf.operation.bean;

import com.gzsf.operation.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;

/**
 * 对日志文件的读取
 */
public class LogFile {
    private Long logId;
    private File file;
    private FileInputStream inputStream;
    private LogMessage message;
    private ByteBuffer byteBuffer=ByteBuffer.allocate(1024);

    public LogFile(Long logId,String logPath){
        message=new LogMessage(logId,logPath,byteBuffer);
        this.logId=logId;
        this.file=new File(logPath);
    }

    public Long getLogId() {
        return logId;
    }

    public String getLogPath(){
        return file.getAbsolutePath();
    }
    public void update(String logPath){
        if (!file.getAbsolutePath().equals(logPath)){
            Utils.close(inputStream);
            inputStream=null;
            file=new File(logPath);
        }
    }

    public boolean hasData() {
        try {
            if (inputStream == null) {
                if (this.file.exists()){
                    inputStream=new FileInputStream(this.file);
                    //*跳过并丢弃<代码>n</代码>字节的数据*跳过并丢弃<代码>n</代码>字节的数据
                    inputStream.skip(inputStream.available());
                    return false;
                }else {
                    return false;
                }
            }
            {
                int size=   inputStream.available() ;
                return size>0;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public int read(){
        try {
            if (inputStream!=null){
              int size=   inputStream.read(byteBuffer.array());
              if(size<1)return size;
                byteBuffer.clear();
                byteBuffer.position(0);
              byteBuffer.limit(size);
              return size;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    public LogMessage getMessage() {
        return message;
    }

    public void close(){
        Utils.close(inputStream);
    }
}
