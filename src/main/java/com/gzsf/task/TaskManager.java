package com.gzsf.task;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskManager {
    private final ExecutorService executorService;
    private final TaskRunner taskRunner;
    private final Thread loop;
    private final Map<String,Task> taskMap=new HashMap<>();

    public TaskManager(int threadSize){
        executorService= Executors.newFixedThreadPool(threadSize);
        taskRunner=new TaskRunner(executorService);
        loop=new Thread(taskRunner);
    }

    public void start(){
        loop.start();
    }
    public <T> void  addTask(String name,T data,Trigger trigger,TaskRunnable<T> runnable){
        if (taskMap.containsKey(name)){
            removeTask(name);
        }
        Task task=new Task(name,trigger,runnable,data);
        taskMap.put(name,task);
        taskRunner.addTask(task);
    }

    public void removeTask(String name){
        if (taskMap.containsKey(name)){
            taskRunner.removeTask(taskMap.get(name));
            taskMap.remove(name);
        }
    }

}
