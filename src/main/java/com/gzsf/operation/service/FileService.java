package com.gzsf.operation.service;

import com.github.pagehelper.Page;
import com.gzsf.operation.bean.FileContent;
import com.gzsf.operation.cache.FileCache;
import com.gzsf.operation.dao.FileMapper;
import com.gzsf.operation.dao.FileVersionMapper;
import com.gzsf.operation.model.FileModel;
import com.gzsf.operation.model.FileVersionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

@Service
public class FileService extends MonoService {
    @Autowired
    private FileMapper fileMapper;
    @Autowired
    private FileVersionMapper fileVersionMapper;
    @Autowired
    private FileCache fileCache;
    private ExecutorService fileThreads= Executors.newFixedThreadPool(10);
    public Mono<Page> getFileList(Integer limit,
                                             Integer offset,
                                             String keyword){
       return async(()-> fileMapper.getList(offset,limit,keyword));
    }

    public Mono<FileModel> saveFile(FileModel fileModel){
        fileModel.setCreatedBy(null);
        return async(()->fileCache.save(fileModel))
                .map(fileCache::getFileById);
    }

    public Mono<Boolean> delete(Long fielId){
        return async(()-> fileCache.delete(fielId));
    }

    public Mono<Page> getVersionList(Long fileId,
                               Integer limit,
                              Integer offset,
                             String keyword){
        return  async(()-> fileVersionMapper.getList(offset,limit,fileId,keyword));
    }

    public Mono saveFile(FilePart filePart,String updateLog,Long fileId,Long userId){
        return async(()->{
            Integer v= fileVersionMapper.getLastVersion(fileId);
            return v==null?1:v+1;
                }
        ).map(it ->{

            FileVersionModel fileVersionModel=new FileVersionModel();
            fileVersionModel.setCreatedAt(new Date());
            fileVersionModel.setVersion(it);
            fileVersionModel.setFileId(fileId);
            fileVersionModel.setUpdateLog(updateLog);
            fileVersionModel.setUpdatedBy(userId);
            fileVersionModel.setFileName(filePart.filename());
            Long id= fileVersionMapper.insert(fileVersionModel);
            fileVersionModel.setFileVersionId(id);
            return  fileVersionModel;
        }).doOnSuccess(fileVersionModel -> fileThreads.submit(()->{
            File file=new File("/data/"+fileId+"/"+fileVersionModel.getVersion());
            createdFile(file);
            filePart.transferTo(file).block();
        }));
    }

    private void createdFile(File file){
        if (!file.exists()){
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Mono<FileContent> getFile(Long fileId,Integer version){
        return async(()-> fileVersionMapper.getRecord(fileId,version)).map(it ->{
            FileContent fileContent=new FileContent();
            fileContent.setName(it.getFileName());
           try {
               File file=new File("/data/"+fileId+"/"+version);
               FileInputStream stream=new FileInputStream(file);
               int size= stream.available();
               byte[] bytes=new byte[size];
               stream.read(bytes);
               fileContent.setContent(bytes);
               return fileContent;
           }catch (Exception e){
               return fileContent;
           }
        });
    }

}
