package com.gzsf.operation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

/**
 * 可异步服务
 */
public class MonoService {
    private final Scheduler scheduler;

    public MonoService() {
        scheduler= Schedulers.fromExecutor(Executors.newFixedThreadPool(16));
    }
//创建一个延迟发射器，该发射器可与基于回调的api一起使用，以最多发送一个值，一个完整或错误信号。
    //使用提供的Callable创建产生其值的Mono。
    //使用提供的Callable创建产生其值的Mono。如果可调用函数解析为null，则产生的Mono将为空。

    public <T> Mono<T> async(Callable<T> callable)  {

        return Mono.fromCallable(callable).publishOn(scheduler);
    }
}
