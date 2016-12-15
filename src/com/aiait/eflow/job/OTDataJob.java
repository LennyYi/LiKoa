package com.aiait.eflow.job;

import com.aiait.eflow.housekeeping.dao.OTRecordDAO;
import com.aiait.framework.db.*;

/**
 * IT1002 DS-001 ��ʱ�Զ���ȡcheck in/out�����ݣ�Ȼ�󱣴浽OT Form��OT Record�ı���
 * 
 * @version 2010-11-19
 */
public class OTDataJob extends BaseJob {

    public static final String JOB_ID = "ot_job";

    public OTDataJob(int hour, int minute, int second) {
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
                    OTRecordDAO dao = new OTRecordDAO(dbManager);
                    dao.updateOTRecordFromCheckSys();
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
