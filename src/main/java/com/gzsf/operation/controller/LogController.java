package com.gzsf.operation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gzsf.operation.ResponseUtils;
import com.gzsf.operation.bean.DbLogQuery;
import com.gzsf.operation.bean.Db_table;
import com.gzsf.operation.model.LogModel;
import com.gzsf.operation.model.User;
import com.gzsf.operation.service.DbLogService;
import com.gzsf.operation.service.LogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;

@RestController
@RequestMapping("log")
public class LogController {
    @Autowired
    private LogService logService;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private DbLogService dbLogService;
    private final Logger logger= LoggerFactory.getLogger(this.getClass());

    @PostMapping()
    @PreAuthorize("hasAnyAuthority('USER')")
    public Mono insert(@RequestBody @Validated LogModel logModel, Authentication authentication){
        User user= (User) authentication.getPrincipal();
        logModel.setCreatedBy(user.getUserId());
        return logService.save(logModel).map(ResponseUtils::success);
    }
    @GetMapping("{id}")
    public Mono getItem(@PathVariable("id")Long id){
        return logService.getItem(id).map(ResponseUtils::success);
    }
    @GetMapping()
    public Mono getList(
            @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize,
            @RequestParam(value = "keyword",required = false) String keyword
                        ){
        return logService.getList(pageNum, pageSize, keyword).map(ResponseUtils::successPage);
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

    @GetMapping("fields/{id}")
    @PreAuthorize("hasAnyAuthority('USER')")
    public Mono getLogFields(@PathVariable("id") Long id){
        return logService.getLogFields(id).map(ResponseUtils::success);
    }

    @PostMapping("fields/{id}")
    @PreAuthorize("hasAnyAuthority('USER')")
    public Mono updateLogFields(@PathVariable("id") Long id,@RequestBody String fields){
        return logService.updateLogFields(fields,id).map(ResponseUtils::success);
    }
    @GetMapping("db/{id}")
    public Mono getDbLog(
            @PathVariable("id") Long id,
            @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize,
            @RequestParam(value = "startTime",required = false) String startTime,
            @RequestParam(value = "endTime",required = false) String endTime,
            @RequestParam(value = "keyword",required = false) String keyword){
        DbLogQuery query=new DbLogQuery();
        query.setEndTime(endTime);
        query.setStartTime(startTime);
        query.setPageNum(pageNum);
        query.setPageSize(pageSize);
        query.setKeyword(keyword);
        return dbLogService.getLogs(id,query).map(ResponseUtils::successPage)
                .doOnError(throwable -> logger.error("getDbLog",throwable));
    }
    @PostMapping("/logdel")
    public  Mono logdel(@RequestBody Db_table db_table){
        return logService.log_del(db_table.getLog_table()).doOnError(throwable -> logger.error("deleteDbLog",throwable)).map(ResponseUtils::success);
    }
}
