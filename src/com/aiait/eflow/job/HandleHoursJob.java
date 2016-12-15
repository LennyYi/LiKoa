package com.aiait.eflow.job;

import java.util.List;

import com.aiait.eflow.util.*;
import com.aiait.eflow.wkf.dao.WorkFlowProcessDAO;
import com.aiait.eflow.wkf.vo.WorkFlowProcessTraceVO;
import com.aiait.framework.db.*;

/**
 * Handle Hours Job (for processing history data)
 * 
 * @version 2014-06-06
 */
public class HandleHoursJob extends BaseJob {

    public static final String JOB_ID = "handle_hours_job";

    public HandleHoursJob(int hour, int minute, int second) {
        super(hour, minute, second);
    }

    public String getJobId() {
        return JOB_ID;
    }

    public SchedulerCtrlTask createTask() {
        return new SchedulerCtrlTask(JOB_ID) {

            protected void process() {
                System.out.println("Begin - Processing history handle hours ... (" + Thread.currentThread().getId()
                        + ")");
                long startTime = System.currentTimeMillis();

                // Processing ...
                List<WorkFlowProcessTraceVO> list = null;
                IDBManager dbManager = null;
                String dateFormat = "yyyy-MM-dd HH:mm:ss";
                try {
                    dbManager = DBManagerFactory.getDBManager();
                    WorkFlowProcessDAO processDao = new WorkFlowProcessDAO(dbManager);
                    list = processDao.getNullHandleHoursProcessTraces();
                    String requestNo = null;
                    for (int i = 0; i < list.size(); i++) {
                        WorkFlowProcessTraceVO traceVO = list.get(i);
                        if (traceVO.getRequestNo().equals(requestNo)) {
                            String receiveDate = StringUtil.getDateStr(list.get(i - 1).getHandleDate(), dateFormat);
                            String handleDate = StringUtil.getDateStr(traceVO.getHandleDate(), dateFormat);
                            double handleHours = OverDueUtil.computeInvertalHours(receiveDate, handleDate);
                            traceVO.setHandleHours(handleHours >= 0 ? handleHours : 0);
                        } else {
                            requestNo = traceVO.getRequestNo();
                            traceVO.setHandleHours(0);
                        }
                        processDao.updateHandleHours(traceVO);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return;
                } finally {
                    if (dbManager != null) {
                        dbManager.freeConnection();
                    }
                }

                long costTime = System.currentTimeMillis() - startTime;
                System.out.println("End - Processing history handle hours (" + list.size() + " records / " + costTime
                        + " ms)");
            }
        };
    }

}
