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
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.FormFieldPart;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;



@RestController
public class FileController {
    @Autowired
    private FileService fileService;
    private final Logger logger= LoggerFactory.getLogger(getClass());
    private final DefaultDataBufferFactory factory=new DefaultDataBufferFactory();

    @GetMapping("file")
    public Mono getFileList(
            @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize,
            @RequestParam(value = "keyword",required = false) String keyword
    ){
        return fileService.getFileList(pageNum, pageSize, keyword)
                .map(ResponseUtils::successPage)
                .doOnError(it->logger.error("getFileList",it));
    }

    @PostMapping("file")
    @PreAuthorize("hasAnyAuthority('USER')")
    public Mono create(@RequestBody FileModel fileModel, Authentication authentication){
        User user= (User) authentication.getPrincipal();
        fileModel.setCreatedBy(user.getUserId());
        fileModel.setFileId(null);
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

    @GetMapping("/file/{id}/version")
    public Mono getVersionList(
            @PathVariable("id") Long fileId,
            @RequestParam("limit") Integer limit,
            @RequestParam("offset") Integer offset,
            @RequestParam(value = "keyword",required = false) String keyword
            ){
        return fileService.getVersionList(fileId,limit, offset, keyword)
                .map(ResponseUtils::successPage)
                .doOnError(it->logger.error("getVersionList",it));
    }
    @PostMapping("/file/{id}/version")
    public Mono updateFile(
            @PathVariable("id") Long fileId,
            Authentication authentication,
            @RequestPart("file") FilePart filePart,
            @RequestPart(value = "updateLog" ,required = false)FormFieldPart updateLog
            )
    {
        User user= (User) authentication.getPrincipal();
        String updateInfo=updateLog==null?null:updateLog.value();
        return fileService.saveFile(filePart,updateInfo,fileId,user.getUserId());
    }

    @GetMapping("file/{id}/version/{version}/file")
//    @PreAuthorize("hasAnyAuthority('USER')")
    public Mono<Void> download(
            @PathVariable("id") Long fileId,
            @PathVariable("version") Integer version,
            ServerHttpResponse serverHttpResponse
    )
    {
        serverHttpResponse.getHeaders().setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return serverHttpResponse.writeWith(this.fileService.getFile(fileId,version).map(it ->{
            serverHttpResponse.getHeaders().set("Content-Disposition", "attachment; filename="+it.getName());
            serverHttpResponse.getHeaders().set("fileName",it.getName());
            serverHttpResponse.getHeaders().set("fileSize",it.getContent().length+"");
            return factory.wrap(it.getContent());
        }));
    }

}
