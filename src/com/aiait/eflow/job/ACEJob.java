package com.aiait.eflow.job;

import java.util.*;

import com.aiait.framework.db.*;

/**
 * ACEJob
 * 
 * @version 2010-11-19
 */
public class ACEJob extends BaseJob {

    public static final String JOB_ID = "ace_job";

    public ACEJob(int hour, int minute, int second) {
        super(hour, minute, second);
    }

    public String getJobId() {
        return JOB_ID;
    }

    public SchedulerCtrlTask createTask() {
        return new SchedulerCtrlTask(JOB_ID) {

            protected void process() {
                IDBManager dbManager = null;
                try {
                    dbManager = DBManagerFactory.getDBManager();
                    Collection paramList = new ArrayList();
                    System.out.println("Call Stored Procedure: poef_build_ace_interface ...");
                    dbManager.prepareCall("poef_build_ace_interface", paramList);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (dbManager != null)
                        dbManager.freeConnection();
                }
            }
        };
    }

}
