package com.aiait.eflow.job;

import com.aiait.eflow.common.helper.StaffTeamHelper;
import com.aiait.eflow.delegation.dao.DelegateDAO;
import com.aiait.framework.db.*;

/**
 * IT0958 DS-005 Delegation can automatically revoke after the available period
 * 
 * @version 2010-11-19
 */
public class RevokeDeputyJob extends BaseJob {

    public static final String JOB_ID = "revoke_deputy_job";

    public RevokeDeputyJob(int hour, int minute, int second) {
        super(hour, minute, second);
    }

    public String getJobId() {
        return JOB_ID;
    }

    public SchedulerCtrlTask createTask() {
        return new SchedulerCtrlTask(JOB_ID) {

            protected void process() {
                // Synchinzed staff data with PMA
                StaffTeamHelper.getInstance().refresh();

                IDBManager dbManager = null;
                try {
                    dbManager = DBManagerFactory.getDBManager();
                    DelegateDAO dao = new DelegateDAO(dbManager);
                    dao.revokeDeputyByTask();
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
