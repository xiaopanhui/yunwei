package com.gzsf.operation.controller;

import com.gzsf.operation.ResponseUtils;
import com.gzsf.operation.model.ServiceModel;
import com.gzsf.operation.model.User;
import com.gzsf.operation.service.HuiProcessSer;
import com.gzsf.operation.service.ProcessService;
import com.gzsf.operation.service.ServiceService;
import com.gzsf.operation.service.impl.SerH;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("service")
public class ServiceController {
    @Autowired
    private ServiceService serviceService;

    @Autowired
    private ProcessService processService;
    @Autowired
    private HuiProcessSer huiProcessSer;
    @Autowired
    private SerH serH;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping("")
    public Mono list(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "keyword", required = false) String keyword
    ) {
        return serviceService
                .getList(pageNum, pageSize, keyword)
                .map(ResponseUtils::successPage)
                .onErrorResume(e -> {
                    logger.error("server list", e);
                    return Mono.just(ResponseUtils.systemError());
                });
    }

    @PostMapping("")
//    @PreAuthorize("hasAnyAuthority('USER')")
    public Mono add(@RequestBody ServiceModel serviceModel, Authentication authentication) {
        serviceModel.setServiceId(null);
        User user = (User) authentication.getPrincipal();
        serviceModel.setCreatedBy(user.getUserId());
        return serviceService
                .save(serviceModel)
                .map(ResponseUtils::success)
                .onErrorResume(e -> {
                    logger.error("service add ", e);
                    return Mono.just(ResponseUtils.recordExists());
                });
    }

    @PatchMapping("")
    @PreAuthorize("hasAnyAuthority('USER')")
    public Mono update(@RequestBody ServiceModel serviceModel) {
        return serviceService
                .save(serviceModel)
                .map(ResponseUtils::success)
                .onErrorResume(e -> {
                    logger.error("service add ", e);
                    return Mono.just(ResponseUtils.systemError());
                });
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyAuthority('USER')")
    public Mono delete(@PathVariable("id") Long id) {
        return serviceService.delete(id)
                .map(ResponseUtils::success)
                .onErrorResume(e -> {
                    logger.error("service add ", e);
                    return Mono.just(ResponseUtils.systemError());
                });
    }

//    @GetMapping("start/{id}")
//    @PreAuthorize("hasAnyAuthority('USER')")
//    public Mono start(@PathVariable("id") Long id){
//        return processService.startService(id).map(ResponseUtils::success);
//    }

    //    @GetMapping("start/{id}")
//    @PreAuthorize("hasAnyAuthority('USER')")
//    public Mono start(@PathVariable("id") Long id) {
//
//            return serH.runtimeExec(id).map(ResponseUtils::success);
//
//    }
    @GetMapping("start/{id}")
    @PreAuthorize("hasAnyAuthority('USER')")
    public Mono start(@PathVariable("id") Long id) {

        return serH.process(id).map(ResponseUtils::success);

    }

    @GetMapping("stop/{id}")
    @PreAuthorize("hasAnyAuthority('USER')")
    public Mono stop(@PathVariable("id") Long id) {
        return processService.stopService(id).map(ResponseUtils::success);
    }
}
