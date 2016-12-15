package com.aiait.eflow.job;

import java.sql.*;
import java.util.Calendar;

import com.aiait.framework.db.*;
import com.aiait.framework.job.SchedulerTask;

/**
 * SchedulerCtrlTask
 * 
 * @version 2013-11-26
 */
public abstract class SchedulerCtrlTask extends SchedulerTask {

    public static final String STATUS_RUNNING = "running";
    public static final String STATUS_IDLE = "idle";

    protected String jobId;

    public SchedulerCtrlTask(String jobId) {
        if (jobId == null || jobId.trim().equals("")) {
            throw new IllegalArgumentException("job id cannot be null");
        }
        this.jobId = jobId;
    }

    @Override
    public void run() {
        System.out.println("Prepare to run job (" + this.jobId + "), checking status ...");
        if (!this.lockTask()) {
            System.out.println("Job (" + this.jobId + ") status: Job is running on other server ...");
            return;
        }
        System.out.println("Job (" + this.jobId + ") status: Start running ...");
        this.process();
        this.unlockTask();
        System.out.println("Job (" + this.jobId + ") status: Running closed");
    }

    protected boolean lockTask() {
        String SQL_SEL = "select * from teflow_job_control where job_id = ?";

        String SQL_UPD = "update teflow_job_control set status = ?, last_runtime = ? "
                + "where job_id = ? and isnull(status, 'free') <> ?";

        String SQL_INS = "insert into teflow_job_control (job_id, status, last_runtime) values (?, ?, ?)";

        IDBManager dbManager = null;
        PreparedStatement stm = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            dbManager.startTransaction();
            Connection conn = dbManager.getJDBCConnection();
            stm = conn.prepareStatement(SQL_UPD);
            int i = 1;
            stm.setString(i++, STATUS_RUNNING);
            stm.setTimestamp(i++, new Timestamp(System.currentTimeMillis()));
            stm.setString(i++, this.jobId);
            stm.setString(i++, STATUS_RUNNING);
            int rowCount = stm.executeUpdate();
            stm.close();
            if (rowCount == 0) {
                // Check if it's the 1st time to run the job
                stm = conn.prepareStatement(SQL_SEL);
                i = 1;
                stm.setString(i++, this.jobId);
                ResultSet rs = stm.executeQuery();
                if (rs.next()) {
                    Timestamp lastRuntime = rs.getTimestamp("last_runtime");
                    Calendar calendar = Calendar.getInstance();
                    calendar.add(Calendar.DAY_OF_MONTH, -7);
                    if (calendar.getTime().after(lastRuntime)) {
                        System.out.println("It seems the Job stopped abnormally before, try to restart running ...");
                        dbManager.rollback();
                        this.unlockTask();
                        return this.lockTask();
                    } else {
                        // Job is running ...
                        return false;
                    }
                }
                rs.close();
                stm.close();
                // It's the 1st time
                stm = conn.prepareStatement(SQL_INS);
                i = 1;
                stm.setString(i++, this.jobId);
                stm.setString(i++, STATUS_RUNNING);
                stm.setTimestamp(i++, new Timestamp(System.currentTimeMillis()));
                stm.executeUpdate();
            }
            dbManager.commit();
            return true;
        } catch (Exception ex) {
            try {
                dbManager.rollback();
            } catch (Exception e) {
                e.printStackTrace();
            }
            ex.printStackTrace();
            return false;
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            if (dbManager != null) {
                dbManager.freeConnection();
            }
        }
    }

    protected void unlockTask() {
        String SQL_UPD = "update teflow_job_control set status = ? where job_id = ?";

        IDBManager dbManager = null;
        PreparedStatement stm = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            dbManager.startTransaction();
            Connection conn = dbManager.getJDBCConnection();
            stm = conn.prepareStatement(SQL_UPD);
            int i = 1;
            stm.setString(i++, STATUS_IDLE);
            stm.setString(i++, this.jobId);
            stm.executeUpdate();
            dbManager.commit();
        } catch (Exception ex) {
            try {
                dbManager.rollback();
            } catch (Exception e) {
                e.printStackTrace();
            }
            ex.printStackTrace();
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            if (dbManager != null) {
                dbManager.freeConnection();
            }
        }
    }

    protected abstract void process();

}
