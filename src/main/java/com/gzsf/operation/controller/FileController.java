package com.gzsf.operation.controller;

import com.gzsf.operation.ResponseUtils;
import com.gzsf.operation.bean.PageResponse;
import com.gzsf.operation.bean.Response;
import com.gzsf.operation.model.FileModel;
import com.gzsf.operation.model.User;
import com.gzsf.operation.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.function.Function;
import java.util.function.Predicate;

@RestController
public class FileController {
    @Autowired
    private FileService fileService;
    private final Logger logger= LoggerFactory.getLogger(getClass());
    @GetMapping("file")
    public Mono getFileList(
            @RequestParam("limit") Integer limit,
            @RequestParam("offset") Integer offset,
            @RequestParam(value = "keyword",required = false) String keyword
    ){
        return fileService.getFileList(limit, offset, keyword)
                .map(ResponseUtils::successPage)
                .doOnError(it->logger.error("getFileList",it));
    }

    @PostMapping("file")
    @PreAuthorize("hasAnyAuthority('USER')")
    public Mono create(@RequestBody FileModel fileModel, Authentication authentication){
        User user= (User) authentication.getPrincipal();
        fileModel.setCreatedBy(user.getUserId());
        return fileService.saveFile(fileModel)
                .map(ResponseUtils::success)
                .doOnError(it-> logger.error("create File",it))
                .onErrorReturn(ResponseUtils.recordExists());
    }
    @PatchMapping("file")
    @PreAuthorize("hasAnyAuthority('USER')")
    public Mono update(@RequestBody FileModel fileModel){
        if (fileModel.getFileId()==null)return Mono.just(ResponseUtils.paramError());
        return fileService.saveFile(fileModel)
                .map(ResponseUtils::success)
                .doOnError(it-> logger.error("create File",it))
                .onErrorReturn(ResponseUtils.recordExists());
    }

    @DeleteMapping("file/{fileId}")
    @PreAuthorize("hasAnyAuthority('USER')")
    public Mono delete(@PathVariable("fileId") Long fileId){

        return fileService.delete(fileId)
                .map(it->{
                    if (it){
                      return   ResponseUtils.success(null);
                    }else {
                     return    ResponseUtils.systemError();
                    }
        })
                .doOnError(it-> logger.error("create File",it));
    }
}
