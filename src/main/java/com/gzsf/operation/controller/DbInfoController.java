package com.gzsf.operation.controller;

import com.gzsf.operation.ResponseUtils;
import com.gzsf.operation.model.DbInfo;
import com.gzsf.operation.model.User;
import com.gzsf.operation.service.impl.DbInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
public class DbInfoController {
    @Autowired
    private DbInfoService dbInfoService;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final DefaultDataBufferFactory factory = new DefaultDataBufferFactory();

    @GetMapping("dbInfo")
    public Mono getDbInfoList(
            @RequestParam("limit") Integer limit,
            @RequestParam("offset") Integer offset,
            @RequestParam(value = "keyword",required = false) String keyword
    ) {
        return dbInfoService.getDbIfoList(limit,offset,keyword)
                .map(ResponseUtils::successPage)
                .doOnError(it->logger.error("getDbInfoList",it));
    }

    @PostMapping("db")
    @PreAuthorize("hasAnyAuthority('USER')")
    public Mono create(@RequestBody DbInfo dbInfo, Authentication authentication){
        User user= (User) authentication.getPrincipal();
        dbInfo.setCreatedBy(user.getUserId());
        return dbInfoService.saveDbInfo(dbInfo)
                .map(ResponseUtils::success)
                .doOnError(it-> logger.error("create DbInfo",it))
                .onErrorReturn(ResponseUtils.recordExists());
    }

    @PatchMapping("db/{dbId}")
    @PreAuthorize("hasAnyAuthority('USER')")
    public Mono update(@RequestBody DbInfo dbInfo) {
        if (dbInfo.getDbId() == null) {
            return Mono.just(ResponseUtils.paramError());
        }
        return dbInfoService.saveDbInfo(dbInfo)
                .map(ResponseUtils::success)
                .doOnError(it->logger.error("create DbInfo",it))
                .onErrorReturn(ResponseUtils.recordExists());
    }

    @DeleteMapping("db/{dbId}")
    @PreAuthorize("hasAnyAuthority('USER')")
    public Mono delete(@PathVariable("dbId") Long dbId) {
        return dbInfoService.delete(dbId)
                .map(it->{
                    if (it) {
                        return ResponseUtils.success(null);
                    } else {
                        return ResponseUtils.systemError();
                    }
                }).doOnError(it->logger.error("create DbInfo",it));
    }
}

