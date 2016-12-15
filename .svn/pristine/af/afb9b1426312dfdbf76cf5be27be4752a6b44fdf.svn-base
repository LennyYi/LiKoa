package com.aiait.eflow.job;

import java.util.*;

import javax.servlet.*;

import com.aiait.eflow.common.CommonName;
import com.aiait.eflow.common.helper.*;
import com.aiait.eflow.util.BaseDataUtil;
import com.aiait.framework.mvc.util.Global;

/**
 * TimeJobListener
 * 
 * @version 2014-06-05
 */
public class TimeJobListener implements ServletContextListener {

    public static final String KEY_JOBLIST = "job_list";

    public void contextInitialized(ServletContextEvent contextEvent) {
        // Initialize Global data ...
        Global.init(contextEvent.getServletContext());

        // Initialize base data ...
        BaseDataUtil.initData();

        // Starting jobs ...
        List<IBaseJob> jobList = new ArrayList<IBaseJob>();
        contextEvent.getServletContext().setAttribute(KEY_JOBLIST, jobList);
        System.out.println("----- Begin - Starting jobs ...");

        BaseDataUtil.startOTJob(jobList);
        // BaseDataUtil.startACEJob(jobList);

        BaseDataUtil.startWaitingNodeJob(jobList);

        BaseDataUtil.startDelayedNodeJob(jobList);

        BaseDataUtil.startHandleHoursJob(jobList);

        if (CompanyHelper.getInstance().getEFlowCompany().equals(CompanyHelper.EFlow_AIAIT)) {
            if ("Yes".equalsIgnoreCase(ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_LEAVE_BALANCE))) {
                BaseDataUtil.startLeaveDailyJob(jobList);
            }
            if ("Yes".equalsIgnoreCase(ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_MEDICAL_CLAIM))) {
                BaseDataUtil.startMedicalDailyJob(jobList);
            }
        }

        if (CompanyHelper.getInstance().getEFlowCompany().equals(CompanyHelper.EFlow_AIA_CHINA)) {
            BaseDataUtil.startAdviserRemindJob(jobList);

            BaseDataUtil.startHRDBSyncJob(jobList);
        }

        System.out.println("----- Starting Revoke Deputy Job ...");
        RevokeDeputyJob revokeDeputyJob = new RevokeDeputyJob(23, 45, 0);
        revokeDeputyJob.start();
        jobList.add(revokeDeputyJob);

        System.out.println("----- End - Starting jobs");
    }

    public void contextDestroyed(ServletContextEvent contextEvent) {
        List<IBaseJob> jobList = (List<IBaseJob>) contextEvent.getServletContext().getAttribute(KEY_JOBLIST);
        Iterator<IBaseJob> it = jobList.iterator();
        int i = 0;
        while (it.hasNext()) {
            IBaseJob job = it.next();
            job.cancel();
            i++;
        }
        System.out.println("----- All of " + i + " scheduled jobs stopped -----");
    }

}
