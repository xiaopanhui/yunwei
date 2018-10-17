package com.gzsf.operation.cache;

import com.gzsf.operation.dao.FileMapper;
import com.gzsf.operation.dao.FileVersionMapper;
import com.gzsf.operation.model.FileModel;
import com.gzsf.operation.model.FileVersionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class FileCache {
    @Autowired
    private FileMapper fileMapper;
    @Autowired
    private FileVersionMapper fileVersionMapper;
    /**
     * 插入或者文件信息
     * @param file 文件信息
     * @return 变更行数
     *  @CacheEvict 注解用来清理缓存
     */
    @CacheEvict(value = "file",key = "#file.fileId")
    public Long save(FileModel file){
        file.setUpdatedAt(new Date());
        if (file.getFileId()==null){
            file.setCreatedAt(new Date());
            return fileMapper.insert(file);
        }else {
            fileMapper.update(file);
            return file.getFileId();
        }
    }

    @Cacheable(value = "file",key = "#fileId")
    public FileModel getFileById(Long fileId){
        return fileMapper.getRecordById(fileId);
    }

    @CacheEvict(value = "file",key = "#fileId")
    public boolean delete(Long fileId){
        return fileMapper.delete(fileId)==1;
    }

    @Cacheable(value = "file_version",key = "#fileId+'_'+#version")
    public FileVersionModel getFileVersion(Long fileId,Integer version){
        return fileVersionMapper.getRecord(fileId,version);
    }

    @CacheEvict(value = "file_version",key = "#fileVersionModel.fileId+'_0'")
    public Long saveFileVersion(FileVersionModel fileVersionModel){
        Integer v= fileVersionMapper.getLastVersion(fileVersionModel.getFileId());
        fileVersionModel.setVersion(v==null?1:v+1);
        return fileVersionMapper.insert(fileVersionModel);
    }

}
