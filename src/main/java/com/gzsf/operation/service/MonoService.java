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

    <T> Mono<T> async(Callable<T> callable)  {
        return Mono.fromCallable(callable).publishOn(scheduler);
    }
}
