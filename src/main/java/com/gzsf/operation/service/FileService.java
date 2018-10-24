package com.gzsf.operation.service;

import com.github.pagehelper.Page;
import com.gzsf.operation.FileUtils;
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
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class FileService extends MonoService {
    @Autowired
    private FileMapper fileMapper;
    @Autowired
    private FileVersionMapper fileVersionMapper;
    @Autowired
    private FileCache fileCache;
    @Autowired
    private FileUtils fileUtils;
    private ExecutorService fileThreads= Executors.newFixedThreadPool(10);
    public Mono<Page> getFileList(
            Integer pageNum,
            Integer pageSize,
            String keyword) {
        return async(() -> fileMapper.getList(pageNum, pageSize, keyword));
    }

    public Mono<FileModel> saveFile(FileModel fileModel){
//        fileModel.setCreatedBy(null);
        return async(()->fileCache.save(fileModel))
                .map(fileCache::getFileById);
    }

    public Mono<Boolean> delete(Long fielId){
        return async(()-> fileCache.delete(fielId));
    }

    public Mono<Page> getVersionList(Long fileId,
                                     Integer pageNum,
                                      Integer pageSize,
                             String keyword){
        return  async(()-> fileVersionMapper.getList(pageNum,pageSize,fileId,keyword));
    }

    public Mono saveFile(FilePart filePart,String updateLog,Long fileId,Long userId){
        return async(()->{

            FileVersionModel fileVersionModel=new FileVersionModel();
            fileVersionModel.setCreatedAt(new Date());
            fileVersionModel.setFileId(fileId);
            fileVersionModel.setUpdateLog(updateLog);
            fileVersionModel.setUpdatedBy(userId);
            fileVersionModel.setFileName(filePart.filename());
            Long id= fileCache.saveFileVersion(fileVersionModel);
            fileVersionModel.setFileVersionId(id);
            return  fileVersionModel;
        }).doOnSuccess(fileVersionModel -> fileThreads.submit(()->{
            File file = fileUtils.getNewFile(fileId,fileVersionModel.getVersion());
            filePart.transferTo(file).block();
        }));
    }


    public Mono<FileContent> getFile(Long fileId,Integer version){
        return async(()-> fileCache.getFileVersion(fileId,version)).map(it ->{
            FileContent fileContent=new FileContent();
            fileContent.setName(it.getFileName());
           try {
               File file=new File(fileUtils.getFilePath(fileId,it.getVersion()));
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
