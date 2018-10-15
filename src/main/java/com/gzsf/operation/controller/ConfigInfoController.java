package com.gzsf.operation.controller;


import com.gzsf.operation.ResponseUtils;
import com.gzsf.operation.model.ConfigInfo;
import com.gzsf.operation.service.ConfigInfoService;
import org.apache.ibatis.annotations.Delete;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @GetMapping("/config/{id}")
    public  Mono getByConfigInfoId(@PathVariable("id") Integer id){
        return configInfoService.getByConfigInfoId(id).map(it -> ResponseUtils.success(it))
                .onErrorReturn(ResponseUtils.notFound())
                .doOnError(throwable -> logger.error("getByConfigInfoId", throwable));

    }
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PatchMapping("/config/{id}")
    public  Mono update(@PathVariable("id") Integer id,@RequestBody ConfigInfo configInfo){
          return   configInfoService.update(id,configInfo).map(it->ResponseUtils.success(it))
                    .doOnError(throwable -> logger.error("update", throwable));
    }

   @DeleteMapping("/config/{id}")
   @PreAuthorize("hasAnyAuthority('ADMIN')")
    public  Mono deleteById(@PathVariable("id") Integer id){
            return    configInfoService.deleteById(id).map(it->ResponseUtils.success(it))
                        .doOnError(throwable -> logger.error("deleteById",throwable));
   }



    @PostMapping ("/configs")
    public  Mono getConfigs(@RequestBody ConfigInfo configInfo,@RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                            @RequestParam(value = "pagesize",defaultValue ="8")int pagesize){
        return configInfoService.getListConfig(configInfo,pageNum, pagesize).map(it->ResponseUtils.success(it));

    }
    @GetMapping("/config")
    public  Mono getConfigList(@RequestParam(value = "pageNum" ,defaultValue = "1")int pageNum,@RequestParam (value = "pageSize",defaultValue = "8") int pageSize,@RequestParam("keyword") String keyword){
        return configInfoService.getConfigList(pageNum, pageSize, keyword);
    }
}