package com.aiait.eflow.job;

import com.aiait.framework.job.*;

/**
 * BaseJob
 * 
 * @version 2010-11-19
 */
public abstract class BaseJob implements IBaseJob {

    protected Scheduler scheduler = new Scheduler();
    protected int hour, minute, second;

    public BaseJob(int hour, int minute, int second) {
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }

    public abstract String getJobId();

    public void start() {
        this.scheduler.schedule(this.createTask(), new DailyIterator(this.hour, this.minute, this.second));
        System.out.println("Job started - " + this.getJobId() + "(" + this.hour + ":" + this.minute + ":" + this.second
                + ")");
    }

    public abstract SchedulerCtrlTask createTask();

    public void cancel() {
        this.scheduler.cancel();
        System.out.println("Job stopped - " + this.getJobId() + "(" + this.hour + ":" + this.minute + ":" + this.second
                + ")");
    }

}
