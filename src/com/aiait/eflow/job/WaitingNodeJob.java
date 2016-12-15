package com.aiait.eflow.job;

import java.text.SimpleDateFormat;
import java.util.*;

import com.aiait.eflow.common.CommonName;
import com.aiait.eflow.common.helper.ParamConfigHelper;
import com.aiait.eflow.housekeeping.dao.EmailTemplateDAO;
import com.aiait.eflow.housekeeping.vo.EmailTemplateVO;
import com.aiait.eflow.util.OverDueUtil;
import com.aiait.eflow.wkf.action.WorkFlowProcessAction;
import com.aiait.eflow.wkf.dao.WorkFlowProcessDAO;
import com.aiait.eflow.wkf.vo.*;
import com.aiait.framework.db.*;

/**
 * Waiting Node Job
 * 
 * @version 2014-07-25
 */
public class WaitingNodeJob extends BaseJob {

    public static final String JOB_ID = "waiting_node_job";

    protected SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

    public WaitingNodeJob(int hour, int minute, int second) {
        super(hour, minute, second);
    }

    public String getJobId() {
        return JOB_ID;
    }

    public SchedulerCtrlTask createTask() {
        return new SchedulerCtrlTask(JOB_ID) {

            protected void process() {
                try {
                    System.out.println("Begin - Processing forms in waiting node ... ("
                            + Thread.currentThread().getId() + ")");
                    long startTime = System.currentTimeMillis();

                    // Limited Hours for Waiting Node
                    double limitedHours = CommonName.DEFAULT_WAITING_NODE_LIMITED_HOURS; // Default
                    String paramLimitedHours = ParamConfigHelper.getInstance().getParamValue(
                            CommonName.PARAM_WAITING_NODE_LIMITED_HOURS);
                    if (paramLimitedHours != null && !"".equals(paramLimitedHours)) {
                        try {
                            limitedHours = Double.parseDouble(paramLimitedHours);
                            System.out.println("Common Limited Hours: " + limitedHours);
                        } catch (Exception ex) {
                            System.out.println("Common Limited Hours: Not defined correctly, use " + limitedHours
                                    + " by default");
                        }
                    } else {
                        System.out.println("Common Limited Hours: Not defined, use " + limitedHours + " by default");
                    }

                    // Limited Hours for DH Node
                    double limitedHoursDH = 0;
                    String paramLimitedHoursDH = ParamConfigHelper.getInstance().getParamValue("DH_limited_hours");
                    if (paramLimitedHoursDH != null && !"".equals(paramLimitedHoursDH)) {
                        try {
                            limitedHoursDH = Double.parseDouble(paramLimitedHoursDH);
                            System.out.println("DH Limited Hours: " + limitedHoursDH);
                        } catch (Exception ex) {
                            System.out.println("DH Limited Hours: Not defined correctly, will be ignored.");
                        }
                    } else {
                        System.out.println("DH Limited Hours: Not defined, will be ignored.");
                    }

                    // Reminder Hours
                    double[] reminderHours = {15.00, 30.00}; // Default
                    String paramReminderHours = ParamConfigHelper.getInstance().getParamValue(
                            CommonName.PARAM_WAITING_NODE_REMINDER_HOURS);
                    if (paramReminderHours != null && !"".equals(paramReminderHours)) {
                        try {
                            String[] aryReminderHours = paramReminderHours.split(";");
                            double[] reminderHours2 = new double[aryReminderHours.length];
                            for (int i = 0; i < aryReminderHours.length; i++) {
                                reminderHours2[i] = Double.parseDouble(aryReminderHours[i]);
                            }
                            reminderHours = reminderHours2;
                            System.out.println("Common Reminder Hours: " + getHoursString(reminderHours));
                        } catch (Exception ex) {
                            System.out.println("Common Reminder Hours: Not defined correctly, use "
                                    + getHoursString(reminderHours) + " by default");
                        }
                    } else {
                        System.out.println("Common Reminder Hours: Not defined, use " + getHoursString(reminderHours)
                                + " by default");
                    }

                    // Processing ...
                    IDBManager dbManager = null;
                    List forms = null;
                    HashMap templateRejectMap = new HashMap();
                    HashMap templateRejectDHMap = new HashMap();
                    HashMap templateReminderMap = new HashMap();
                    EmailTemplateVO templateReject = null;
                    EmailTemplateVO templateRejectDH = null;
                    EmailTemplateVO templateReminder = null;
                    try {
                        dbManager = DBManagerFactory.getDBManager();
                        WorkFlowProcessDAO processDao = new WorkFlowProcessDAO(dbManager);
                        forms = processDao.getWaitingNodeForms(limitedHoursDH != 0);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        return;
                    } finally {
                        if (dbManager != null) {
                            dbManager.freeConnection();
                        }
                    }

                    Date processDate = new Date();
                    String currentDate = dateFormat.format(processDate);
                    for (int i = 0; i < forms.size(); i++) {
                        WorkFlowProcessVO vo = (WorkFlowProcessVO) forms.get(i);
                        String receivingDate = dateFormat.format(vo.getReceivingDate());
                        String lastProcessDate = vo.getWaitNodeProcessDate() == null ? null : dateFormat.format(vo
                                .getWaitNodeProcessDate());
                        double _limitedHours = vo.getLimitedHours();
                        if (_limitedHours == 0.0) {
                            if ("a".equals(vo.getNodeType())) {
                                _limitedHours = limitedHours;
                            } else {
                                _limitedHours = limitedHoursDH;
                            }
                        }
                        double remainHours = OverDueUtil.computeOverdueHours(receivingDate, currentDate, _limitedHours);
                        String processType = null;
                        if (remainHours <= 0.0) {
                            // Reject ...
                            processType = "Reject";
                            vo.setWaitNodeProcessType(processType);
                            vo.setWaitNodeProcessDate(processDate);
                            vo.setLimitedHours(_limitedHours);
                            if ("a".equals(vo.getNodeType())) {
                                templateReject = (EmailTemplateVO) templateRejectMap.get(vo.getFormType());
                                if (templateReject == null) {
                                    try {
                                        dbManager = DBManagerFactory.getDBManager();
                                        EmailTemplateDAO emailTemplateDao = new EmailTemplateDAO(dbManager);
                                        templateReject = emailTemplateDao.getEmailTemplateByAction(
                                                CommonName.HANDLE_TYPE_WAITING_REJECT, vo.getFormType());
                                        templateRejectMap.put(vo.getFormType(), templateReject);
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                        return;
                                    } finally {
                                        if (dbManager != null) {
                                            dbManager.freeConnection();
                                        }
                                    }
                                }
                                processReject(vo, templateReject);
                            } else {
                                templateRejectDH = (EmailTemplateVO) templateRejectDHMap.get(vo.getFormType());
                                if (templateRejectDH == null) {
                                    try {
                                        dbManager = DBManagerFactory.getDBManager();
                                        EmailTemplateDAO emailTemplateDao = new EmailTemplateDAO(dbManager);
                                        templateRejectDH = emailTemplateDao.getEmailTemplateByAction(
                                                CommonName.HANDLE_TYPE_DH_REJECT, vo.getFormType());
                                        templateRejectDHMap.put(vo.getFormType(), templateRejectDH);
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                        return;
                                    } finally {
                                        if (dbManager != null) {
                                            dbManager.freeConnection();
                                        }
                                    }
                                }
                                processReject(vo, templateRejectDH);
                            }
                        } else if ("a".equals(vo.getNodeType())) {
                            for (int j = 0; j < reminderHours.length; j++) {
                                double _reminderHours = reminderHours[j];
                                double checkHours = OverDueUtil.computeOverdueHours(receivingDate, currentDate,
                                        _reminderHours);
                                if (checkHours <= 0.0) {
                                    if (lastProcessDate != null) {
                                        double checkHours2 = OverDueUtil.computeOverdueHours(receivingDate,
                                                lastProcessDate, _reminderHours);
                                        if (checkHours2 <= 0.0) {
                                            continue;
                                        }
                                    }
                                    // Reminder ...
                                    processType = "Reminder";
                                    vo.setWaitNodeProcessType(processType);
                                    vo.setWaitNodeProcessDate(processDate);
                                    vo.setRemainTime("" + remainHours);
                                    templateReminder = (EmailTemplateVO) templateReminderMap.get(vo.getFormType());
                                    if (templateReminder == null) {
                                        try {
                                            dbManager = DBManagerFactory.getDBManager();
                                            EmailTemplateDAO emailTemplateDao = new EmailTemplateDAO(dbManager);
                                            templateReminder = emailTemplateDao.getEmailTemplateByAction(
                                                    CommonName.HANDLE_TYPE_WAITING_REMINDER, vo.getFormType());
                                            templateReminderMap.put(vo.getFormType(), templateReminder);
                                        } catch (Exception ex) {
                                            ex.printStackTrace();
                                            return;
                                        } finally {
                                            if (dbManager != null) {
                                                dbManager.freeConnection();
                                            }
                                        }
                                    }
                                    processReminder(vo, templateReminder);
                                    break;
                                } else {
                                    // Passed
                                    processType = "Passed";
                                    break;
                                }
                            }
                        }
                        System.out.println("Req.No: " + vo.getRequestNo() + "/R.Date: " + receivingDate + "/LM.Hours: "
                                + _limitedHours + "/LP.Date: " + lastProcessDate + "/RM.Hours: " + remainHours
                                + "/P.Type: " + processType);
                    }

                    long costTime = System.currentTimeMillis() - startTime;
                    System.out.println("End - Processing forms in waiting node (" + forms.size() + " records / "
                            + costTime + " ms)");
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    //
                }
            }
        };
    }

    protected void processReject(WorkFlowProcessVO vo, EmailTemplateVO templateReject) {
        IDBManager dbManager = null;
        WorkFlowProcessTraceVO traceVo = new WorkFlowProcessTraceVO();
        traceVo.setRequestNo(vo.getRequestNo());
        traceVo.setHandleType(CommonName.HANDLE_TYPE_REJECT);
        traceVo.setCurrentNodeId(vo.getNodeId());
        traceVo.setFormSystemId(vo.getFormSystemId());
        traceVo.setHandleStaffCode("System");
        traceVo.setHandleComments("Over the waiting time (" + vo.getLimitedHours() + " hours)");
        traceVo.setRejectToNodeId("0");
        try {
            dbManager = DBManagerFactory.getDBManager();
            WorkFlowProcessDAO processDao = new WorkFlowProcessDAO(dbManager);
            dbManager.startTransaction();
            processDao.nodeProcess(traceVo, vo.getRequestStaffCode());
            processDao.lockForm(vo.getRequestNo(), "0", "");
            processDao.saveWaitingNodeProcess(vo);
            dbManager.commit();
        } catch (Exception ex) {
            try {
                dbManager.rollback();
            } catch (Exception e) {
                e.printStackTrace();
            }
            ex.printStackTrace();
            return;
        } finally {
            if (dbManager != null) {
                dbManager.freeConnection();
            }
        }

        // Send email ...
        if (templateReject != null) {
            String[] toStaff = null;
            if (vo.getSubmittedBy().equals(vo.getRequestStaffCode())) {
                toStaff = new String[] {vo.getRequestStaffCode()};
            } else {
                toStaff = new String[] {vo.getRequestStaffCode(), vo.getSubmittedBy()};
            }
            try {
                WorkFlowProcessAction.sendEmail(templateReject, vo.getRequestStaffCode(), vo.getRequestNo(),
                        traceVo.getHandleStaffCode(), "" + vo.getLimitedHours(), toStaff,
                        Integer.toString(vo.getFormSystemId()), null);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    protected void processReminder(WorkFlowProcessVO vo, EmailTemplateVO templateReminder) {
        IDBManager dbManager = null;
        WorkFlowProcessTraceVO traceVo = new WorkFlowProcessTraceVO();
        traceVo.setHandleStaffCode("System");
        traceVo.setHandleComments(vo.getRemainTime());
        try {
            dbManager = DBManagerFactory.getDBManager();
            WorkFlowProcessDAO processDao = new WorkFlowProcessDAO(dbManager);
            dbManager.startTransaction();
            processDao.saveWaitingNodeProcess(vo);
            dbManager.commit();
        } catch (Exception ex) {
            try {
                dbManager.rollback();
            } catch (Exception e) {
                e.printStackTrace();
            }
            ex.printStackTrace();
            return;
        } finally {
            if (dbManager != null) {
                dbManager.freeConnection();
            }
        }

        // Send email ...
        if (templateReminder != null) {
            String[] toStaff = null;
            if (vo.getSubmittedBy().equals(vo.getRequestStaffCode())) {
                toStaff = new String[] {vo.getRequestStaffCode()};
            } else {
                toStaff = new String[] {vo.getRequestStaffCode(), vo.getSubmittedBy()};
            }
            try {
                WorkFlowProcessAction.sendEmail(templateReminder, vo.getRequestStaffCode(), vo.getRequestNo(),
                        traceVo.getHandleStaffCode(), traceVo.getHandleComments(), toStaff,
                        Integer.toString(vo.getFormSystemId()), null);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    protected String getHoursString(double[] hours) {
        String string = "";
        String p = "";
        for (int i = 0; i < hours.length; i++) {
            string += p + hours[i];
            p = ", ";
        }
        return string;
    }

}
