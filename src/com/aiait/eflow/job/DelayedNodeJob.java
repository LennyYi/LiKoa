package com.aiait.eflow.job;

import java.sql.Timestamp;
import java.util.*;

import com.aiait.eflow.common.CommonName;
import com.aiait.eflow.formmanage.dao.FormManageDAO;
import com.aiait.eflow.formmanage.vo.FormSectionVO;
import com.aiait.eflow.housekeeping.dao.EmailTemplateDAO;
import com.aiait.eflow.housekeeping.vo.EmailTemplateVO;
import com.aiait.eflow.util.StringUtil;
import com.aiait.eflow.wkf.action.WorkFlowProcessAction;
import com.aiait.eflow.wkf.dao.*;
import com.aiait.eflow.wkf.vo.*;
import com.aiait.framework.db.*;

/**
 * Delayed Node Job
 * 
 * @version 2014-04-16
 */
public class DelayedNodeJob extends BaseJob {

    public static final String JOB_ID = "delayed_node_job";

    public DelayedNodeJob(int hour, int minute, int second) {
        super(hour, minute, second);
    }

    public String getJobId() {
        return JOB_ID;
    }

    public SchedulerCtrlTask createTask() {
        return new SchedulerCtrlTask(JOB_ID) {

            protected void process() {
                System.out.println("Begin - Processing forms in delayed node ... (" + Thread.currentThread().getId()
                        + ")");
                long startTime = System.currentTimeMillis();

                // Processing ...
                List<WorkFlowProcessVO> forms = null;
                Date now = new Date();
                HashMap<String, WorkFlowItemVO> nodeMap = new HashMap<String, WorkFlowItemVO>();
                HashMap<String, FormSectionVO> sectionMap = new HashMap<String, FormSectionVO>();
                HashMap<String, EmailTemplateVO> emailTemplateMap = new HashMap<String, EmailTemplateVO>();
                IDBManager dbManager = null;
                try {
                    dbManager = DBManagerFactory.getDBManager();
                    WorkFlowProcessDAO processDao = new WorkFlowProcessDAO(dbManager);
                    WorkFlowNodeDAO nodeDAO = new WorkFlowNodeDAO(dbManager);
                    FormManageDAO formDAO = new FormManageDAO(dbManager);
                    EmailTemplateDAO emailTemplateDao = new EmailTemplateDAO(dbManager);

                    forms = processDao.getDelayedNodeForms();
                    for (int i = 0; i < forms.size(); i++) {
                        WorkFlowProcessVO process = forms.get(i);
                        String nodeKey = process.getFlowId() + "_" + process.getNodeId();
                        WorkFlowItemVO node = nodeMap.get(nodeKey);
                        if (node == null) {
                            node = nodeDAO.getNodeById(process.getFlowId(), process.getNodeId());
                            nodeMap.put(nodeKey, node);
                        }
                        String delaytimeField = node.getDelaytimeField();
                        if (delaytimeField != null && !delaytimeField.equals("")) {
                            String[] _delaytimeField = delaytimeField.split("\\.");
                            String sectionId = _delaytimeField[0];
                            String fieldId = _delaytimeField[1];
                            String sectionKey = process.getFormSystemId() + "_" + sectionId;
                            FormSectionVO section = sectionMap.get(sectionKey);
                            if (section == null) {
                                section = formDAO.getFormSectionInfor(process.getFormSystemId(), sectionId);
                                sectionMap.put(sectionKey, section);
                            }
                            Object fieldValue = processDao.getFormFieldValue(process.getRequestNo(), section, fieldId,
                                    1);
                            if (fieldValue != null) {
                                boolean delay = false;
                                if (fieldValue instanceof Timestamp) {
                                    Timestamp time = (Timestamp) fieldValue;
                                    if (now.before(time)) {
                                        delay = true;
                                    }
                                } else {
                                    // For other field type
                                }
                                if (!delay) {
                                    // Unlock form
                                    processDao.lockForm(process.getRequestNo(), "0", "");
                                    System.out.println("Unlock Form (Delay Time): " + process.getRequestNo() + " ("
                                            + fieldValue + ")");

                                    // Send email ...
                                    EmailTemplateVO emailTemplate = emailTemplateMap.get(process.getFormType());
                                    if (emailTemplate == null) {
                                        emailTemplate = emailTemplateDao.getEmailTemplateByAction(
                                                CommonName.HANDLE_TYPE_APPROVE, process.getFormType());
                                        emailTemplateMap.put(process.getFormType(), emailTemplate);
                                    }

                                    WorkFlowProcessTraceVO traceVo = new WorkFlowProcessTraceVO();
                                    traceVo.setHandleStaffCode("System");
                                    traceVo.setHandleComments("On the due time (" + fieldValue + ")");

                                    String[] approverStaffList = null;
                                    String approverStaff = process.getCurrentProcessor();
                                    if (approverStaff != null && !"".equals(approverStaff)) {
                                        approverStaffList = StringUtil.split(approverStaff, ",");
                                    }
                                    String dedup = ",";
                                    if (approverStaffList != null && approverStaffList.length > 0) {
                                        for (int j = 0; j < approverStaffList.length; j++) {
                                            if (dedup.indexOf("," + approverStaffList[j] + ",") != -1) {
                                                continue;
                                            } else {
                                                dedup += approverStaffList[j] + ",";
                                            }
                                        }
                                    }
                                    String[] toStaff = dedup.substring(1).split(",");
                                    try {
                                        WorkFlowProcessAction.sendEmail(emailTemplate, process.getRequestStaffCode(),
                                                process.getRequestNo(), traceVo.getHandleStaffCode(),
                                                traceVo.getHandleComments(), toStaff,
                                                Integer.toString(process.getFormSystemId()), null);
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                }
                            }
                        }
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
                System.out.println("End - Processing forms in delayed node (" + forms.size() + " records / " + costTime
                        + " ms)");
            }
        };
    }

}
