package com.gzsf.task;

public interface TaskRunnable<T> {
    /**
     *
     * @param data
     * @return
     */
    boolean run(T data) throws Throwable;

    void error(T data,Throwable throwable);
}
