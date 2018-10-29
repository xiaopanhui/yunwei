package com.gzsf.task;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ExecutorService;

public class TaskRunner implements Runnable {
    private final ExecutorService executorService;
    private final Object lock =new Object();
    private final PriorityQueue<Task> taskQueue=new PriorityQueue<Task>((o1, o2) -> (int) (o1.getPriority()-o2.getPriority()));
    private boolean isStop=false;
     TaskRunner(ExecutorService executorService) {
        this.executorService = executorService;
    }

    private long checkLoop() {
        Task task = taskQueue.poll();
        long currentTime=System.currentTimeMillis();
        if (task==null){
            return 0;
        }
        while (task != null && (task.getPriority() - currentTime < 100)) {
            task.update();
            taskQueue.add(task);
            runTask(task);
            task=taskQueue.poll();
            currentTime=System.currentTimeMillis();
        }
        if (task!=null){
            taskQueue.add(task);
        }
        return task==null?0: (int) (task.getPriority() - currentTime);
    }

    private void runTask(final Task task){
        this.executorService.submit(()->{
            try {
                if(!task.getRunnable().run(task.getEvent())){
                    taskQueue.remove(task);
                }
            } catch (Throwable throwable) {
                task.getRunnable().error(task.getEvent(),throwable);
            }
        });
    }

    public void run() {
        synchronized (lock){
            while (!isStop){
                try {
                    lock.wait(checkLoop());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

     void addTask(Task task){
        synchronized (lock){
            taskQueue.add(task);
            lock.notify();
        }
    }

     void removeTask(Task task) {
         synchronized (lock){
             taskQueue.remove(task);
             lock.notify();
         }
    }
}
