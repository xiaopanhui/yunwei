package com.gzsf.operation.controller;

import com.gzsf.operation.ResponseUtils;
import com.gzsf.operation.bean.LogMessage;
import com.gzsf.operation.model.LogModel;
import com.gzsf.operation.model.User;
import com.gzsf.operation.service.LogFileService;
import com.gzsf.operation.service.LogService;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("log")
public class LogController {
    @Autowired
    private LogService logService;

    @Autowired
    private LogFileService logFileService;



    @PostMapping()
    @PreAuthorize("hasAnyAuthority('USER')")
    public Mono insert(@RequestBody @Validated LogModel logModel, Authentication authentication){
        User user= (User) authentication.getPrincipal();
        logModel.setCreatedBy(user.getUserId());
        return logService.save(logModel).map(ResponseUtils::success);
    }

    @GetMapping()
    public Mono getList(@RequestParam(value = "limit",defaultValue = "10") Integer limit,
                        @RequestParam(value = "offset",defaultValue = "1") Integer offset,
                        @RequestParam(value = "keyword",required = false) String keyword
                        ){
        return logService.getList(limit, offset, keyword).map(ResponseUtils::successPage);
    }

    @PatchMapping()
    @PreAuthorize("hasAnyAuthority('USER')")
    public Mono update(@RequestBody @Validated LogModel logModel){
        return logService.save(logModel).map(ResponseUtils::success);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyAuthority('USER')")
    public Mono delete(@PathVariable("id") Long id){
        return logService.delete(id).map(ResponseUtils::success);
    }

    @MessageMapping("/websocket/log/{id}")
    @SendTo("/log")
    public Object sw(@DestinationVariable Long id) throws Exception {
        logFileService.addLogFile(id);
        return ResponseUtils.success(null);
    }
}