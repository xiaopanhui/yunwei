package com.gzsf.operation.controller;


import com.gzsf.operation.ResponseUtils;
import com.gzsf.operation.model.ConfigInfo;
import com.gzsf.operation.model.User;
import com.gzsf.operation.service.ConfigInfoService;
import com.gzsf.operation.service.ConfigItemService;
import org.apache.poi.util.IOUtils;
import org.apache.poi.util.SystemOutLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.FormFieldPart;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

@RestController
public class ConfigInfoController {
    @Autowired
    private ConfigInfoService configInfoService;
    private final Logger logger= LoggerFactory.getLogger(getClass());
    @Autowired
    private ConfigItemService configItemService;

    @GetMapping("/config/{id}")
    public  Mono getByConfigInfoId(@PathVariable("id") Long id){
        return configInfoService.getByConfigInfoId(id).map(it -> ResponseUtils.success(it))
                .onErrorReturn(ResponseUtils.notFound())
                .doOnError(throwable -> logger.error("getByConfigInfoId", throwable));

    }
    @PreAuthorize("hasAnyAuthority('USER')")
    @PatchMapping("/config")
    public  Mono update(@RequestBody ConfigInfo configInfo){
        return configInfoService.update(configInfo.getConfigId(),configInfo).map(it->ResponseUtils.success(it))
                .doOnError(throwable -> logger.error("update", throwable));
    }

    @DeleteMapping("/config/{id}")
    @PreAuthorize("hasAnyAuthority('USER')")
    public  Mono deleteById(@PathVariable("id") Integer id){
        return    configInfoService.deleteById(id).map(it->ResponseUtils.success(it))
                .doOnError(throwable -> logger.error("deleteById",throwable));
    }



    @PostMapping ("/config")
    @PreAuthorize("hasAnyAuthority('USER')")
    public  Mono getConfigs(@RequestBody ConfigInfo configInfo, Authentication authentication){
        User user= (User) authentication.getPrincipal();
        configInfo.setCreatedBy(user.getUserId());
        return configInfoService.insert(configInfo).map(it->ResponseUtils.success(it));

    }
    @GetMapping("/config")
    public  Mono getConfigList(@RequestParam(value = "pageNum" ,defaultValue = "1")int pageNum,@RequestParam (value = "pageSize",defaultValue = "8") int pageSize,@RequestParam(value = "keyword",required = false) String keyword){
        return configInfoService.getConfigList(pageNum, pageSize, keyword).map(ResponseUtils::successPage);
    }

    @GetMapping("config/fields/{id}")
    @PreAuthorize("hasAnyAuthority('USER')")
    public Mono getLogFields(@PathVariable("id") Long id){
        return configItemService.getConfigFields(id).map(ResponseUtils::success);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("config/fields/{id}")
//    @PreAuthorize("hasAnyAuthority('USER')")
    public Mono updateLogFields(@PathVariable("id") Long id,@RequestBody String fields){
        System.out.println(55);
        return configItemService.updateConfigFields(fields,id).map(ResponseUtils::success);
    }

    @GetMapping("config/db/{id}")
    public Mono getDbConfig(
            @PathVariable("id") Long id,
            @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize
    ){
        return configItemService.getConfig(id,pageNum,pageSize).map(ResponseUtils::successPage);
    }

    @PostMapping("config/config/{id}")
    public Mono addCongItem(
            @PathVariable("id") Long id,
            @RequestBody Map<String,Object> body
    ){
        return this.configItemService.addCongItem(id,body).map(ResponseUtils::success);
    }
    @PostMapping("config/config/{id}/del")
    public Mono deleteCongItem(
            @PathVariable("id") Long id,
            @RequestBody Map<String,Object> body
    ){
        return this.configItemService.deleteCongItem(id,body).map(ResponseUtils::success);
    }

    @PatchMapping("config/config/{id}")
    public Mono updateCongItem(
            @PathVariable("id") Long id,
            @RequestBody Map<String,Object> body
    ){
        return this.configItemService.upateCongItem(id,body).map(ResponseUtils::success);
    }

    @PostMapping(value = "config/import")
    public Mono getFile(@RequestPart("file") FilePart file, @RequestPart("id")FormFieldPart configId) throws IOException {
        String path = "/data/import/"+file.filename();
        File f = new File(path);
        if(!f.getParentFile().exists()){
            f.getParentFile().mkdirs();
        }
        f.createNewFile();
        file.transferTo(f);
        long id =Long.parseLong(configId.value());
        FileInputStream input = new FileInputStream(f);
        MultipartFile mf = new MockMultipartFile("file", f.getName(), "application/vnd.ms-excel", IOUtils.toByteArray(input));
        return configItemService.uploadFile(id,mf).map(ResponseUtils::success);
    }

}