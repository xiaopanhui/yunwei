package com.gzsf.task;

/**
 * 定时触发器
 */
public interface Trigger {
    /**
     * 计算下次执行时间
     * @return 下一次执行时间
     */
    long next();
}
