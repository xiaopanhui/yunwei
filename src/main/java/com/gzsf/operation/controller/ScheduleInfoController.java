package com.gzsf.operation.controller;

import com.gzsf.operation.ResponseUtils;
import com.gzsf.operation.model.ScheduleInfo;
import com.gzsf.operation.model.User;
import com.gzsf.operation.service.ScheduleInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Date;

@RestController
@RequestMapping("schedule")
public class ScheduleInfoController {
    @Autowired
    private ScheduleInfoService scheduleInfoService;

    @PreAuthorize("hasAnyAuthority('USER')")
    @PostMapping("")
    public Mono insert(@RequestBody  ScheduleInfo scheduleInfo, Authentication authentication){
        User user= (User) authentication.getPrincipal();
        scheduleInfo.setScheduleId(null);
        scheduleInfo.setCreatedBy(user.getUserId());
        return scheduleInfoService.save(scheduleInfo).map(ResponseUtils::success);
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @PatchMapping("")
    public Mono update(@RequestBody ScheduleInfo scheduleInfo){
        return scheduleInfoService.save(scheduleInfo).map(ResponseUtils::success);
    }

    @GetMapping("")
    public Mono getList(
            @RequestParam(value = "keyword",required = false) String keyword,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum
    ){
        return scheduleInfoService.getList(pageNum, pageSize, keyword).map(ResponseUtils::successPage);
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @DeleteMapping("{id}")
    public Mono delete(
            @PathVariable("id") Long id
    ){
        return scheduleInfoService.delete(id).map(ResponseUtils::success);
    }

    @GetMapping("{id}")
    public Mono geLogList(
            @PathVariable("id") Long id
    ){
        return scheduleInfoService.getitemById(id).map(ResponseUtils::success);
    }
    @GetMapping("{id}/log")
    public Mono geLogList(
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(value = "startTime", required = false)@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date startTime,
            @RequestParam(value = "endTime", required = false)@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date endTime,
            @PathVariable("id") Long id
    ){
        return scheduleInfoService.getLogList(id,pageNum, pageSize,startTime,endTime).map(ResponseUtils::successPage);
    }
}
