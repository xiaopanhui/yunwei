package com.gzsf.operation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class FileUtils {
    private final String format="/data/%d/%d";
    private final Logger logger= LoggerFactory.getLogger(this.getClass());
    public String getFilePath(long fileId,int version) {
        return String.format(format,fileId,version);
    }

    public File getNewFile(long fileId,int version){
        return getNewFile(getFilePath(fileId,version));
    }
    public File getNewFile(String path){
        File file;
        try {
            file=new File(path);
            if (!file.getParentFile().exists()){
                file.getParentFile().mkdirs();
            }
            file.createNewFile();
            return file;
        }catch (Exception e){
            logger.error("create file",e);
        }
        return null;
    }
}
