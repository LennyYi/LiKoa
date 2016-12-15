package com.aiait.eflow.wkf.util;

import java.util.ArrayList;
import java.util.Collection;


import com.aiait.eflow.common.CommonName;
import com.aiait.eflow.housekeeping.dao.EmailTemplateDAO;
import com.aiait.eflow.housekeeping.vo.EmailTemplateVO;
import com.aiait.eflow.housekeeping.vo.StaffVO;
import com.aiait.eflow.util.EFlowEmailUtil;
import com.aiait.eflow.util.StringUtil;
import com.aiait.eflow.wkf.dao.WorkFlowProcessDAO;
import com.aiait.eflow.wkf.vo.WorkFlowProcessTraceVO;
import com.aiait.eflow.wkf.vo.WorkFlowProcessVO;
import com.aiait.framework.db.DBManagerFactory;
import com.aiait.framework.db.IDBManager;

public class WKAfterProccessTread implements Runnable {

	private String requestNo;

	private StaffVO currentStaff;

	private String returnLabel;

	private String formSystemId;

	private String functionLabel;
	
	private String requestStaffCode;
	
	private String ccToStaffs;

	public WKAfterProccessTread(String requestNo, StaffVO currentStaff,
			String returnLabel, String formSystemId, String functionLabel, String requestStaffCode,String ccToStaffs) {
		
		this.requestNo = requestNo;
		
		this.currentStaff = currentStaff;
		
		this.returnLabel = returnLabel;
		
		this.formSystemId = formSystemId;
		
		this.functionLabel = functionLabel;
		
		this.requestStaffCode = requestStaffCode;
		
		this.ccToStaffs = ccToStaffs;
	}

	@Override
	public void run() {
		IDBManager dbManager = null;
		try {
			dbManager = DBManagerFactory.getDBManager();
			Collection paramList = new ArrayList();
			paramList.add(requestNo);
			paramList.add(currentStaff.getStaffCode());
			System.out.println("Call Stored Procedure: " + functionLabel);
			dbManager.prepareCall(functionLabel, paramList);
			sendingMail(dbManager);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (dbManager != null)
				dbManager.freeConnection();
		}

	}
	
	private void sendingMail(IDBManager dbManager) throws Exception{
		String handleType = "03";
		WorkFlowProcessDAO processDao = new WorkFlowProcessDAO(dbManager);
        WorkFlowProcessVO process = processDao.getProcessVO(requestNo);
        EmailTemplateDAO emailTemplateDao = new EmailTemplateDAO(dbManager);
        EmailTemplateVO template = emailTemplateDao.getEmailTemplateByAction(handleType);
     // For delayed node
        if (CommonName.NODE_TYPE_DELAYED.equals(process.getNodeType()) && "1".equals(process.getInProcess())
                && "SYS".equals(process.getInProcessStaffCode())) {
            template = null;
        }

        if (template != null) {
            // to get the staff emails who will need to handle
            String handleStaffList = process.getCurrentProcessor();
            String[] staffs = StringUtil.split(handleStaffList, ",");
            WorkFlowProcessTraceVO  traceVo = new WorkFlowProcessTraceVO();
            traceVo.setFormSystemId(Integer.parseInt(formSystemId));
            traceVo.setRequestNo(requestNo);
            traceVo.setIsUrgent("1");
            traceVo.setHandleStaffCode(currentStaff.getStaffCode());
            traceVo.setCurrentNodeId("0");// beginNode

            // 获取选择需要发邮件抄送的员工
            traceVo.setCcStaffs(ccToStaffs);
            if (staffs != null && staffs.length > 0) {
                String handleComments = traceVo.getHandleComments();
                String[] ccStaff = null;
                if (traceVo.getCcStaffs() != null && !"".equals(traceVo.getCcStaffs())) {
                    ccStaff = StringUtil.split(traceVo.getCcStaffs(), ",");
                }
                EFlowEmailUtil.sendEmail(template, requestStaffCode, requestNo, currentStaff.getStaffCode(), handleComments, staffs,
                        formSystemId, ccStaff, process);
            }else {
            	//send email to requester
            	EmailTemplateVO template1 = emailTemplateDao.getEmailTemplateByAction("05");
                staffs = StringUtil.split(process.getRequestStaffCode(), ",");
                String handleComments = traceVo.getHandleComments();
                String[] ccStaff = null;
                if (traceVo.getCcStaffs() != null && !"".equals(traceVo.getCcStaffs())) {
                    ccStaff = StringUtil.split(traceVo.getCcStaffs(), ",");
                }
                EFlowEmailUtil.sendEmail(template1, requestStaffCode, requestNo, currentStaff.getStaffCode(), handleComments, staffs,
                        formSystemId, ccStaff, process);
            }
        }
            
	}
}
