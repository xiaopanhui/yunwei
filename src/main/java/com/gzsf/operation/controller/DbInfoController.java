package com.gzsf.operation.controller;

import com.gzsf.operation.ResponseUtils;
import com.gzsf.operation.model.DbInfo;
import com.gzsf.operation.model.User;
import com.gzsf.operation.security.UserAuthentication;
import com.gzsf.operation.service.DbInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
public class DbInfoController {
    @Autowired
    private DbInfoService dbInfoService;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final DefaultDataBufferFactory factory = new DefaultDataBufferFactory();

    @GetMapping("db")
    public Mono getList( @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
                         @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize,
                        @RequestParam(value = "keyword",required = false) String keyword
                        ){
        return dbInfoService.getList(pageNum, pageSize, keyword).map(ResponseUtils::successPage);
    }

    /**
     *添加
     * @param dbInfo
     * @return
     */
    @PostMapping("/db")
    @PreAuthorize("hasAnyAuthority('USER')")
    public Mono addDbInfo(@RequestBody DbInfo dbInfo, UserAuthentication userAuthentication) {
        User user= (User) userAuthentication.getPrincipal();
        dbInfo.setCreatedBy(user.getUserId());
        return dbInfoService.add(dbInfo).map(ResponseUtils::success);
//                .onErrorReturn(ResponseUtils.nameExists())
//                .doOnError(throwable -> logger.error("addDbInfo",throwable));
    }

    @GetMapping("/db/{id}")
    public Mono getByDbInfoId(@PathVariable("id") Long id) {
        return dbInfoService.getByDbInfoId(id).map(it -> ResponseUtils.success(it))
                .onErrorReturn(ResponseUtils.notFound())
                .doOnError(throwable -> logger.error("getByConfigInfoId", throwable));
    }

    /**
     * 修改
     * @param
     * @return
     */
    @PatchMapping("/db")
    @PreAuthorize("hasAnyAuthority('USER')")
    public  Mono update(@RequestBody DbInfo dbInfo){
        return dbInfoService.update(dbInfo.getDbId(),dbInfo).map(it->ResponseUtils.success(it));
//                .onErrorReturn(ResponseUtils.accessDenied())
//                .doOnError(throwable -> logger.error("update", throwable));
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @DeleteMapping("/db/{id}")
    @PreAuthorize("hasAnyAuthority('USER')")
    public Mono delete(@PathVariable("id") Long id) {
        return dbInfoService.delete(id)
                .map(it ->ResponseUtils.success(null))
                .onErrorResume(e ->{
                    logger.error("dbInfo delete ",e);
                    return Mono.just(ResponseUtils.systemError());
                });
    }
}