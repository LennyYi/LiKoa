package com.aiait.eflow.job;

import java.text.SimpleDateFormat;
import java.util.*;

import com.aiait.eflow.common.CommonName;
import com.aiait.eflow.housekeeping.dao.EmailTemplateDAO;
import com.aiait.eflow.housekeeping.vo.EmailTemplateVO;
import com.aiait.eflow.util.OverDueUtil;
import com.aiait.eflow.wkf.action.WorkFlowProcessAction;
import com.aiait.eflow.wkf.dao.WorkFlowProcessDAO;
import com.aiait.eflow.wkf.vo.*;
import com.aiait.framework.db.*;

/**
 * Adviser Remind Job 
 * 
 * @version 2011-1-31
 */
public class AdviserRemindJob extends BaseJob {

    public static final String JOB_ID = "adviser_remind_job";

    protected SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

    public AdviserRemindJob(int hour, int minute, int second) {
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
                    System.out.println("Begin - Processing forms in adviser remind ... ("
                            + Thread.currentThread().getId() + ")");
                    long startTime = System.currentTimeMillis();

                    // Processing ...
                    List forms = null;
                    HashMap templateReminderMap = new HashMap();
                    EmailTemplateVO templateReminder = null;
                    WorkFlowProcessDAO processDao = null;
                    try {
                        dbManager = DBManagerFactory.getDBManager();
                        processDao = new WorkFlowProcessDAO(dbManager);
                        forms = processDao.getAdviserRemindForms();
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
                        String lastInvitedDate = processDao.getLastInvitedDate(vo.getRequestNo());
                        //看上次邀请是否在一天以前！
                                               
                        String processType = null;
                        
                        double _reminderHours = 8.0;
                        double checkHours = OverDueUtil.computeOverdueHours(lastInvitedDate, currentDate,
                                _reminderHours);
                        if (checkHours <= 0.0) {
                            // Reminder ...
                            processType = "Reminder";
                            vo.setWaitNodeProcessType(processType);
                            vo.setWaitNodeProcessDate(processDate);
                            
                            templateReminder = (EmailTemplateVO) templateReminderMap.get(vo.getFormType());
                            if (templateReminder == null) {
                                try {
                                    dbManager = DBManagerFactory.getDBManager();
                                    EmailTemplateDAO emailTemplateDao = new EmailTemplateDAO(dbManager);
                                    templateReminder = emailTemplateDao.getEmailTemplateByAction(
                                            CommonName.HANDLE_TYPE_INVITED_EXPERT, vo.getFormType());
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
                        } else {
                            // Passed
                            processType = "Passed";
                        }                        
                    
                        System.out.println("Req.No: " + vo.getRequestNo() + "/LI.Date: " + lastInvitedDate +  "/P.Type: " + processType);
                
                    }
                    long costTime = System.currentTimeMillis() - startTime;
                    System.out.println("End - Processing forms in waiting advisor (" + forms.size() + " records / "
                            + costTime + " ms)");
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                	//
                }
            }
        };
    }

    protected void processReminder(WorkFlowProcessVO vo, EmailTemplateVO templateReminder) {
        IDBManager dbManager = null;
        WorkFlowProcessTraceVO traceVo = new WorkFlowProcessTraceVO();
        traceVo.setHandleStaffCode("System");
        traceVo.setHandleComments(vo.getRemainTime());

        // Send email ...
        if (templateReminder != null) {
        	
            String toStaffStr = "";
            
            String[] advisers_all = vo.getInvitedExpert().split(",");
            
            for(int i=0;i<advisers_all.length;i++){
            	String adviser = advisers_all[i];
            	if((","+vo.getCcStaffs()+",").indexOf(","+adviser+",") < 0)            		
            		//已发过提醒信的不需要            	
            		toStaffStr += adviser+",";
            }
            String[] toStaff = toStaffStr.split(",");           
            
            try {
                WorkFlowProcessAction.sendEmail(templateReminder, vo.getRequestStaffCode(), vo.getRequestNo(), traceVo.getHandleStaffCode(), 
                		"This is a reminder.", 
                        toStaff, Integer.toString(vo.getFormSystemId()), null);

                dbManager = DBManagerFactory.getDBManager();
                WorkFlowProcessDAO processDao = new WorkFlowProcessDAO(dbManager);                
                processDao.updateRemindedExperts(vo);
                
            } catch (Exception ex) {
                ex.printStackTrace();
            }finally {
                if (dbManager != null) {
                    dbManager.freeConnection();
                }
            }
        }
    }
}
