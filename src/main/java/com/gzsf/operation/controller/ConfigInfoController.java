package com.gzsf.operation.controller;


import com.gzsf.operation.ResponseUtils;
import com.gzsf.operation.model.ConfigInfo;
import com.gzsf.operation.model.User;
import com.gzsf.operation.service.ConfigInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
public class ConfigInfoController {
    @Autowired
    private ConfigInfoService configInfoService;
    private final Logger logger= LoggerFactory.getLogger(getClass());
    private final DefaultDataBufferFactory factory=new DefaultDataBufferFactory();
    @PostMapping("/config")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public Mono add(@RequestBody ConfigInfo configInfo){
        return  configInfoService.insert(configInfo).map(it -> ResponseUtils.success(it))
                .onErrorReturn(ResponseUtils.nameExists())
                .doOnError(throwable -> logger.error("add",throwable));
    }

    /**
     * 查询不到不返回
     * @param id
     * @return
     */
    @GetMapping("/config/{id}")
    public  Mono getByConfigInfoId(@PathVariable("id") Integer id){
        return configInfoService.getByConfigInfoId(id).map(it -> ResponseUtils.success(it))
                .onErrorReturn(ResponseUtils.notFound())
                .doOnError(throwable -> logger.error("getByConfigInfoId", throwable));

    }
    @PatchMapping("/config/{id}")
    public  Mono update(@PathVariable("id") Integer id,@RequestBody ConfigInfo configInfo, Authentication authentication){
        User user= (User)authentication.getPrincipal();
        if( user.getUserId()==(long)id||user.getRole().equals(User.Role.ADMIN)){
          return   configInfoService.update(configInfo).map(it->ResponseUtils.success(it));
        }
    return  Mono.just(ResponseUtils.accessDenied());
    }



}