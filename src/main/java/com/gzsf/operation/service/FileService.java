package com.gzsf.operation.service;

import com.github.pagehelper.Page;
import com.gzsf.operation.cache.FileCache;
import com.gzsf.operation.dao.FileMapper;
import com.gzsf.operation.model.FileModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class FileService extends MonoService {
    @Autowired
    private FileMapper fileMapper;
    @Autowired
    private FileCache fileCache;
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
}
