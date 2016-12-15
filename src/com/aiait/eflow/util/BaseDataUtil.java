package com.aiait.eflow.util;

import java.text.SimpleDateFormat;
import java.util.*;

import com.aiait.eflow.common.CommonName;
import com.aiait.eflow.common.helper.*;
import com.aiait.eflow.job.*;

/**
 * 基础数据的工具类 1.实现统一的数据更新方法的调用 2.实现统一的实例置空方法的调用
 * 
 * @version 2014-06-06
 */

public class BaseDataUtil {

    public static void refreshData(String currentRoleId) {

        BaseDataHelper.getInstance().refresh();
        //SystemFieldHelper.getInstance().refresh();
        StaffTeamHelper.getInstance().refresh();
        ParamConfigHelper.getInstance().refresh();
        FormTypeHelper.getInstance().refresh();
        OptionHelper.getInstance().refresh();
        AuthorityHelper.getInstance().refresh();
        CompanyHelper.getInstance().refresh();
        HolidayHelper.getInstance().refresh();
    }

    public static void refreshData() {
        BaseDataHelper.getInstance().refresh();
        //SystemFieldHelper.getInstance().refresh();
        StaffTeamHelper.getInstance().refresh();
        ParamConfigHelper.getInstance().refresh();
        FormTypeHelper.getInstance().refresh();
        OptionHelper.getInstance().refresh();
        CompanyHelper.getInstance().refresh();
        HolidayHelper.getInstance().refresh();
    }

    /**
     * 容器启动时，配置调用该方法进行部分基础数据的初始化
     * 
     */
    public static void initData() {
        //System.out.println("----- Begin to initialize Base Data -----");

        AuthorityHelper.getInstance();
        BaseDataHelper.getInstance();
        CompanyHelper.getInstance();
        FormTypeHelper.getInstance();
        OptionHelper.getInstance();
        ParamConfigHelper.getInstance();
        StaffTeamHelper.getInstance();
        //SystemFieldHelper.getInstance();
        WorkFlowHelper.getInstance();

        //System.out.println("----- End to initialize Base Data -----");
    }

    public static void startOTJob(List<IBaseJob> jobList) {
        if (CompanyHelper.getInstance().getEFlowCompany().equals(CompanyHelper.EFlow_AIAIT)) {
            System.out.println("----- Starting OT Job ...");

            // It will run job(update OT Record) on every day 7:30
            OTDataJob otJob = new OTDataJob(7, 30, 0);
            otJob.start();

            // Run second
            OTDataJob otJob2 = new OTDataJob(12, 30, 0);
            otJob2.start();

            // Run third
            OTDataJob otJob3 = new OTDataJob(15, 55, 0);
            otJob3.start();

            jobList.add(otJob);
            jobList.add(otJob2);
            jobList.add(otJob3);
        }
    }

    public static void startACEJob(List<IBaseJob> jobList) {
        if (CompanyHelper.getInstance().getEFlowCompany().equals(CompanyHelper.EFlow_AIA_CHINA)) {
            // System.out.println("----- Starting ACE job ...");

            int hour = 22;
            int minute = 0;
            String jobTime = ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_ACE_JOB_TIME);
            if (jobTime != null && !jobTime.trim().equals("")) {
                jobTime = jobTime.trim();
                try {
                    String[] time = jobTime.split(":");
                    hour = Integer.parseInt(time[0]);
                    if (time.length > 1) {
                        minute = Integer.parseInt(time[1]);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                // Do not run ...
                return;
            }
            System.out.println("ACE Job Time: " + hour + ":" + minute);

            // It will run job on every day 22:00 by default.
            ACEJob aceJob = new ACEJob(hour, minute, 0);
            aceJob.start();
            jobList.add(aceJob);
        }
    }

    public static void startWaitingNodeJob(List<IBaseJob> jobList) {
        System.out.println("----- Starting Waiting Node Job ...");
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.US);
        dateFormat.setLenient(false);
        String[] jobTimes = {"12:30", "17:30"}; // Default
        String paramJobTimes = ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_WAITING_NODE_JOB_TIME);
        if (paramJobTimes != null && !paramJobTimes.trim().equals("")) {
            try {
                String[] jobTimes2 = paramJobTimes.split(";");
                for (int i = 0; i < jobTimes2.length; i++) {
                    dateFormat.parse(jobTimes2[i]); // Format validation
                }
                jobTimes = jobTimes2;
                System.out.println("Waiting Node Job Time: " + getArrayString(jobTimes));
            } catch (Exception ex) {
                System.out.println("Waiting Node Job Time: Not defined correctly, use " + getArrayString(jobTimes)
                        + " by default");
            }
        } else {
            System.out.println("Waiting Node Job Time: Not defined, use " + getArrayString(jobTimes) + " by default");
        }

        Calendar calendar = Calendar.getInstance();
        for (int i = 0; i < jobTimes.length; i++) {
            try {
                Date time = dateFormat.parse(jobTimes[i]);
                calendar.setTime(time);
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                WaitingNodeJob job = new WaitingNodeJob(hour, minute, 0);
                job.start();
                jobList.add(job);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void startDelayedNodeJob(List<IBaseJob> jobList) {
        System.out.println("----- Starting Delayed Node Job ...");
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.US);
        dateFormat.setLenient(false);
        String[] jobTimes = {"00:30", "12:30"}; // Default
        String paramJobTimes = ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_DELAYED_NODE_JOB_TIME);
        if (paramJobTimes != null && !paramJobTimes.trim().equals("")) {
            try {
                String[] jobTimes2 = paramJobTimes.split(";");
                for (int i = 0; i < jobTimes2.length; i++) {
                    dateFormat.parse(jobTimes2[i]); // Format validation
                }
                jobTimes = jobTimes2;
                System.out.println("Delayed Node Job Time: " + getArrayString(jobTimes));
            } catch (Exception ex) {
                System.out.println("Delayed Node Job Time: Not defined correctly, use " + getArrayString(jobTimes)
                        + " by default");
            }
        } else {
            System.out.println("Delayed Node Job Time: Not defined, use " + getArrayString(jobTimes) + " by default");
        }

        Calendar calendar = Calendar.getInstance();
        for (int i = 0; i < jobTimes.length; i++) {
            try {
                Date time = dateFormat.parse(jobTimes[i]);
                calendar.setTime(time);
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                DelayedNodeJob job = new DelayedNodeJob(hour, minute, 0);
                job.start();
                jobList.add(job);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void startAdviserRemindJob(List<IBaseJob> jobList) {
        System.out.println("----- Starting Adviser Remind Job ...");
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.US);
        dateFormat.setLenient(false);
        String[] jobTimes = {"12:30", "17:30"}; // Default
        String paramJobTimes = ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_WAITING_NODE_JOB_TIME);
        if (paramJobTimes != null && !paramJobTimes.trim().equals("")) {
            try {
                String[] jobTimes2 = paramJobTimes.split(";");
                for (int i = 0; i < jobTimes2.length; i++) {
                    dateFormat.parse(jobTimes2[i]); // Format validation
                }
                jobTimes = jobTimes2;
                System.out.println("Adviser Remind Job Time: " + getArrayString(jobTimes));
            } catch (Exception ex) {
                System.out.println("Adviser Remind Job Time: Not defined correctly, use " + getArrayString(jobTimes)
                        + " by default");
            }
        } else {
            System.out.println("Adviser Remind Job Time: Not defined, use " + getArrayString(jobTimes) + " by default");
        }

        Calendar calendar = Calendar.getInstance();
        for (int i = 0; i < jobTimes.length; i++) {
            try {
                Date time = dateFormat.parse(jobTimes[i]);
                calendar.setTime(time);
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                AdviserRemindJob job = new AdviserRemindJob(hour, minute, 0);
                job.start();
                jobList.add(job);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void startLeaveDailyJob(List<IBaseJob> jobList) {
        System.out.println("----- Starting Leave Daily Job ...");
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.US);
        dateFormat.setLenient(false);
        String jobTimes = "01:30"; // Default
        String paramJobTimes = ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_LEAVE_DAILY_JOB_TIME);
        if (paramJobTimes != null && !paramJobTimes.trim().equals("")) {
            try {
                String jobTimes2 = paramJobTimes;
                dateFormat.parse(jobTimes2); // Format validation

                jobTimes = jobTimes2;
                System.out.println("Leave Daily Job Time: " + jobTimes);
            } catch (Exception ex) {
                System.out.println("Leave Daily Job Time: Not defined correctly, use " + jobTimes + " by default");
            }
        } else {
            System.out.println("Leave Daily Job Time: Not defined, use " + jobTimes + " by default");
        }

        Calendar calendar = Calendar.getInstance();
        try {
            Date time = dateFormat.parse(jobTimes);
            calendar.setTime(time);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            LeaveDailyJob job = new LeaveDailyJob(hour, minute, 0);
            // WaitingNodeJob job = new WaitingNodeJob(hour, minute, 0);
            job.start();
            jobList.add(job);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public static void startHRDBSyncJob(List<IBaseJob> jobList) {
        System.out.println("----- Starting HR DB Sync Job ...");
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.US);
        dateFormat.setLenient(false);
        String[] jobTimes = {"01:30"}; // TODO Default
        /*
         * String paramJobTimes = ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_WAITING_NODE_JOB_TIME);
         * if (paramJobTimes != null && !paramJobTimes.trim().equals("")) { try { String[] jobTimes2 =
         * paramJobTimes.split(";"); for (int i = 0; i < jobTimes2.length; i++) { dateFormat.parse(jobTimes2[i]); //
         * Format validation } jobTimes = jobTimes2; System.out.println("HR DB Sync Job Time: " +
         * getArrayString(jobTimes)); } catch (Exception ex) {
         * System.out.println("HR DB Sync Job Time: Not defined correctly, use " + getArrayString(jobTimes) +
         * " by default"); } } else { System.out.println("HR DB Sync Job Time: Not defined, use " +
         * getArrayString(jobTimes) + " by default"); }
         */

        Calendar calendar = Calendar.getInstance();
        for (int i = 0; i < jobTimes.length; i++) {
            try {
                Date time = dateFormat.parse(jobTimes[i]);
                calendar.setTime(time);
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                HrSyncJob job = new HrSyncJob(hour, minute, 0);
                job.start();
                jobList.add(job);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void startMedicalDailyJob(List<IBaseJob> jobList) {
        System.out.println("----- Starting Medical Daily Job ...");
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.US);
        dateFormat.setLenient(false);
        String jobTimes = "03:30"; // Default
        String paramJobTimes = ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_MEDICAL_DAILY_JOB_TIME);
        if (paramJobTimes != null && !paramJobTimes.trim().equals("")) {
            try {
                String jobTimes2 = paramJobTimes;
                dateFormat.parse(jobTimes2); // Format validation

                jobTimes = jobTimes2;
                System.out.println("Medical Daily Job Time: " + jobTimes);
            } catch (Exception ex) {
                System.out.println("Medical Daily Job Time: Not defined correctly, use " + jobTimes + " by default");
            }
        } else {
            System.out.println("Medical Daily Job Time: Not defined, use " + jobTimes + " by default");
        }

        Calendar calendar = Calendar.getInstance();
        try {
            Date time = dateFormat.parse(jobTimes);
            calendar.setTime(time);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            MedicalDailyJob job = new MedicalDailyJob(hour, minute, 0);
            job.start();
            jobList.add(job);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void startHandleHoursJob(List<IBaseJob> jobList) {
        System.out.println("----- Starting Handle Hours Job ...");
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.US);
        dateFormat.setLenient(false);
        String[] jobTimes = {"01:00"}; // Default
        String paramJobTimes = ParamConfigHelper.getInstance().getParamValue("handle_hours_job_time");
        if (paramJobTimes != null && !paramJobTimes.trim().equals("")) {
            try {
                String[] jobTimes2 = paramJobTimes.split(";");
                for (int i = 0; i < jobTimes2.length; i++) {
                    dateFormat.parse(jobTimes2[i]); // Format validation
                }
                jobTimes = jobTimes2;
                System.out.println("Handle Hours Job Time: " + getArrayString(jobTimes));
            } catch (Exception ex) {
                System.out.println("Handle Hours Job Time: Not defined correctly, use " + getArrayString(jobTimes)
                        + " by default");
            }
        } else {
            System.out.println("Handle Hours Job Time: Not defined, use " + getArrayString(jobTimes) + " by default");
        }

        Calendar calendar = Calendar.getInstance();
        for (int i = 0; i < jobTimes.length; i++) {
            try {
                Date time = dateFormat.parse(jobTimes[i]);
                calendar.setTime(time);
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                HandleHoursJob job = new HandleHoursJob(hour, minute, 0);
                job.start();
                jobList.add(job);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void setInstanceNull(String currentRoleId) {
        StaffTeamHelper.getInstance().setInstanceNull();
        AuthorityHelper.getInstance().setInstanceNull();
        CompanyHelper.getInstance().setInstanceNull();
        FormTypeHelper.getInstance().setInstanceNull();
    }

    protected static String getArrayString(Object[] array) {
        String string = "";
        String p = "";
        for (int i = 0; i < array.length; i++) {
            string += p + array[i];
            p = ", ";
        }
        return string;
    }

}
