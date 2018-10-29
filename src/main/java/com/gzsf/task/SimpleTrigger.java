package com.gzsf.task;

public class SimpleTrigger implements Trigger {

    private final long start;
    private final long interval;
    private boolean isStart = false;

    public SimpleTrigger(long start, long interval) {
        this.start = start*1000+System.currentTimeMillis();
        this.interval = interval*1000;
    }

    @Override
    public long next() {
        long current=System.currentTimeMillis() ;
        if (isStart){
            return current+interval;
        }else {
            isStart=true;
            return this.start;
        }
    }
}
