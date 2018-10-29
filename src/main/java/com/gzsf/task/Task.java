package com.gzsf.task;

public class Task<T> {
    private long priority;
    private final TaskRunnable runnable;
    private final T event;
    private final String name;
    private final Trigger trigger;

    public Task(String name,Trigger trigger, TaskRunnable runnable, T event) {
        this.runnable = runnable;
        this.event = event;
        this.name=name;
        this.trigger=trigger;
        priority=this.trigger.next();
    }

    public void update(){
        this.priority=trigger.next();
    }

    public TaskRunnable getRunnable() {
        return runnable;
    }

    public T getEvent() {
        return event;
    }

    public long getPriority() {
        return priority;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Task){
           return  ((Task) obj).name.equals(name);
        }
        if (obj instanceof String){
            return obj.equals(name);
        }
        return false;
    }
}
