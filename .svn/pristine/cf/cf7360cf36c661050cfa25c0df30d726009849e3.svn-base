package com.aiait.eflow.formmanage.action;

/******************************************************************/
/*Task_ID	Author	Modify_Date	Description                       */
/*IT1002	Robin	04/15/2008	DS-002 Team Leader confirm OT records*/
/*IT1002	Young	04/16/2008	DS-002 Team Leader confirm OT records*/
/*IT1002	Young	04/17/2008	DS-003 Personal ot sum*/
/******************************************************************/

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.aiait.eflow.common.CommonName;
import com.aiait.eflow.common.helper.ParamConfigHelper;
import com.aiait.eflow.common.helper.StaffTeamHelper;
import com.aiait.eflow.formmanage.dao.FormManageDAO;
import com.aiait.eflow.formmanage.vo.FormManageVO;
import com.aiait.eflow.formmanage.vo.FormSectionFieldVO;
import com.aiait.eflow.housekeeping.dao.EmailTemplateDAO;
import com.aiait.eflow.housekeeping.dao.OTRecordDAO;
import com.aiait.eflow.housekeeping.dao.TeamDAO;
import com.aiait.eflow.housekeeping.vo.CheckInoutVO;
import com.aiait.eflow.housekeeping.vo.EmailTemplateVO;
import com.aiait.eflow.housekeeping.vo.OTRecordVO;
import com.aiait.eflow.housekeeping.vo.OTSummaryCheckVO;
import com.aiait.eflow.housekeeping.vo.OTSummaryVO;
import com.aiait.eflow.housekeeping.vo.StaffVO;
import com.aiait.eflow.util.EFlowEmailUtil;
import com.aiait.eflow.util.MultipartRequest;
import com.aiait.eflow.util.StringUtil;
import com.aiait.eflow.wkf.dao.WorkFlowProcessDAO;
import com.aiait.eflow.wkf.vo.WorkFlowProcessVO;
import com.aiait.framework.db.DBManagerFactory;
import com.aiait.framework.db.IDBManager;
import com.aiait.framework.exception.DAOException;
import com.aiait.framework.mvc.action.ActionLocation;
import com.aiait.framework.mvc.action.DispatchAction;
import com.aiait.framework.mvc.action.ModuleMapping;
import com.aiait.framework.util.CommonUtil;

public class OTFormAction extends DispatchAction {
	
	/**
	 * 显示当前员工还没有加入该月份报销的可以申请报销的加班记录
	 * @param mapping
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionLocation listAddtionalOTRecord(ModuleMapping mapping,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String returnLabel = "listAdditionalOTRecord";
		String yearMonth = (String)request.getParameter("yearMonth");
		StaffVO staff = (StaffVO)request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
		IDBManager dbManager = null;
		try {
			dbManager = DBManagerFactory.getDBManager();
			OTRecordDAO dao = new OTRecordDAO(dbManager);
			Collection addionalOtRecordList = dao.getAdditionalOTRecord(yearMonth,staff.getStaffCode());
			request.setAttribute("addionalOtRecordList",addionalOtRecordList);
			Collection fieldList = dao.getOTFormTableFields();
			request.setAttribute("fieldList", fieldList);
		}catch (DAOException e) {
			e.printStackTrace();
			request.setAttribute("error", e);
			returnLabel = "fail";
		} finally {
			if (dbManager != null)
				dbManager.freeConnection();
		}
		return mapping.findActionLocation(returnLabel);
	}
	
	/**
	 * 
	 * @param mapping
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionLocation saveAdditionalRecords(ModuleMapping mapping,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String yearMonth = (String)request.getParameter("yearMonth");
		StaffVO staff = (StaffVO)request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
		String[] requestNos = request.getParameterValues("requestNo");
		response.setContentType("text/html;charset=GBK"); //it is very important
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");
		PrintWriter out = response.getWriter();
		IDBManager dbManager = null;
		try {
			dbManager = DBManagerFactory.getDBManager();
			OTRecordDAO dao = new OTRecordDAO(dbManager);
			dbManager.startTransaction();
			for(int i=0;i<requestNos.length;i++){
				dao.saveOTApplyMonthHisByRequestNo(staff.getStaffCode(),yearMonth,requestNos[i]);
			}
			dbManager.commit();
			out.print("success");
		}catch (DAOException e) {
			dbManager.rollback();
			e.printStackTrace();
			out.print("fail");
		} finally {
			if (dbManager != null)
				dbManager.freeConnection();
		}
		return null;
	}
	
	/**
	 * 显示当前用户指定日期的打卡记录(打卡记录从pma的表tpma_checkinout中获取）
	 * @param mapping
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionLocation listStaffCheckInout(ModuleMapping mapping,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String returnLabel = "showStaffCheckInout";
		String otPlanDate = (String)request.getParameter("otPlanDate");
		String staffCode = (String)request.getParameter("staffCode");
		StaffVO staff = StaffTeamHelper.getInstance().getStaffByCode(staffCode);
		if(staff==null){
			request.setAttribute("error", "It can't get the staff!");
			return mapping.findActionLocation("fail");
		}
		IDBManager dbManager = null;
		try {
			dbManager = DBManagerFactory.getDBManager();
			OTRecordDAO dao = new OTRecordDAO(dbManager);
 			HashMap map = dao.getCheckInoutRecords("'"+otPlanDate+"'",staff.getStaffCode().trim());
 			CheckInoutVO checkVo = null;
 			if(map!=null){
 				checkVo = (CheckInoutVO)map.get(otPlanDate);
 				checkVo.setStaffCode(staff.getStaffCode());
 			}
 			request.setAttribute("checkVo",checkVo);
 			request.setAttribute("staff",staff);
		}catch (DAOException e) {
			e.printStackTrace();
			request.setAttribute("error", e);
			returnLabel = "fail";
		} finally {
			if (dbManager != null)
				dbManager.freeConnection();
		}
		
		return mapping.findActionLocation(returnLabel);
	}	
	
	public ActionLocation enterWaitingStaff(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String returnLabel = "showWaitingStaff";
        String requestStaffCode = (String) request.getParameter("requestStaffCode");
        StaffVO staff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            OTRecordDAO dao = new OTRecordDAO(dbManager);
            TeamDAO teamDao = new TeamDAO(dbManager);
            String managedTeams = "";
            if (requestStaffCode == null || "".equals(requestStaffCode)) {
                managedTeams = teamDao.getManagedTeamCodesByTL(staff.getStaffCode());
            }
            // System.out.println(staff.getStaffCode() + " managed teams: " + managedTeams);
            // 包括本人作为别的TL的deputy可以approve的记录
            Collection list = dao.getWaitingConfirmBySum(staff.getStaffCode(), requestStaffCode, managedTeams);
            request.setAttribute("waitingStaffList", list);
        } catch (DAOException e) {
            e.printStackTrace();
            request.setAttribute("error", e.getMessage());
            returnLabel = "fail";
        } finally {
            if (dbManager != null)
                dbManager.freeConnection();
        }
        return mapping.findActionLocation(returnLabel);
    }

	public ActionLocation enterWaitingOTList(ModuleMapping mapping,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String returnLabel = "showWaitingOTList";

		String requestStaffCode = (String) request
				.getParameter("requestStaffCode");
		String otPlanDateBegin = (String) request
				.getParameter("otPlanDateBegin");
		String otPlanDateEnd = (String) request.getParameter("otPlanDateEnd");
		String isExceptionalCase = (String) request
				.getParameter("exceptionalCase");

		// StaffVO staff =
		// (StaffVO)request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
		OTRecordVO conditionVo = new OTRecordVO();
		conditionVo.setStaffCode(requestStaffCode);
		conditionVo.setOtPlanDateBegin(otPlanDateBegin);
		conditionVo.setOtPlanDateEnd(otPlanDateEnd);
		conditionVo.setIsExceptionalCase(isExceptionalCase);

		IDBManager dbManager = null;
		try {
			dbManager = DBManagerFactory.getDBManager();
			OTRecordDAO dao = new OTRecordDAO(dbManager);
			
			//2008-08-04:增加 当打开时，系统也update那些没有计算加班次数的加班record.
			dao.updateOTRecordFromCheckSys(requestStaffCode);
			//dao.updateOTRecordForExceptional(requestNos[i], 1);
			
			Collection list = dao.getWaitingConfirmByStaff(conditionVo);
			
			request.setAttribute("waitingOTList", list);
			Collection fieldList = dao.getOTFormTableFields();
			request.setAttribute("fieldList", fieldList);
		} catch (DAOException e) {
			e.printStackTrace();
			request.setAttribute("error", e.getMessage());
			returnLabel = "fail";
		} finally {
			if (dbManager != null)
				dbManager.freeConnection();
		}

		return mapping.findActionLocation(returnLabel);
	}

	public ActionLocation confirmOTRecords(ModuleMapping mapping,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		StaffVO staff = (StaffVO) request.getSession().getAttribute(
				CommonName.CURRENT_STAFF_INFOR);
		PrintWriter out = response.getWriter();
		String[] requestNos = request.getParameterValues("requestNo");
		IDBManager dbManager = null;
		try {
			dbManager = DBManagerFactory.getDBManager();
			dbManager.startTransaction();
			OTRecordDAO dao = new OTRecordDAO(dbManager);
			for (int i = 0; i < requestNos.length; i++) {
				//如果该form已经confirm过了，则忽略其;避免confirm多次
				if(dao.checkConfirmOTForm(requestNos[i])){
					continue;
				}
				// 如果该OT记录被requester设定为“Normal Exceptional”,则修改为weeky_num为1次
				//dao.updateOTForNormalExceptional(requestNos[i], 1);
				//如果该OT记录被requester设定为“Mid-Night Exceptional”,则修改为weeky_num为2次
				//dao.updateOTForMidNightExceptional(requestNos[i], 2);
				dao.teamLeaderConfirmOTRecord(staff.getStaffCode(),
						requestNos[i]);
			}
			dbManager.commit();
			out.print("success");
		} catch (Exception e) {
			dbManager.rollback();
			e.printStackTrace();
			out.print("fail");
		} finally {
			if (dbManager != null)
				dbManager.freeConnection();
		}
		return null;
	}
	
    public ActionLocation confirmOTRecordsByStaff(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        StaffVO staff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
        PrintWriter out = response.getWriter();
        String[] staffCodes = request.getParameterValues("staffCode");

        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            OTRecordDAO dao = new OTRecordDAO(dbManager);
            Collection requestNos = dao.getWaitingConfirmByStaffs(staffCodes);
            if (requestNos != null && !requestNos.isEmpty()) {
                dbManager.startTransaction();
                Iterator it = requestNos.iterator();
                while (it.hasNext()) {
                    HashMap map = (HashMap) it.next();
                    String requestNo = (String) map.get("REQUEST_NO");
                    // 如果该form已经confirm过了，则忽略其;避免confirm多次
                    if (dao.checkConfirmOTForm(requestNo)) {
                        continue;
                    }
                    dao.teamLeaderConfirmOTRecord(staff.getStaffCode(), requestNo);
                }
                dbManager.commit();
            }
            out.print("success");
        } catch (DAOException e) {
            dbManager.rollback();
            e.printStackTrace();
            out.print("fail");
        } finally {
            if (dbManager != null)
                dbManager.freeConnection();
        }
        return null;
    }

	/**
	 * 当员工修改本人指定日期期间内的加班信息(例如：“未能提供发票金额(Invoice Lacked)”和“报销的年月
	 * Year/Month”)时，调用该方法保存当前“年月”的ot Summary记录
	 * 
	 * @param mapping
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionLocation updateOTSummary(ModuleMapping mapping,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		StaffVO staff = (StaffVO) request.getSession().getAttribute(
				CommonName.CURRENT_STAFF_INFOR);
		PrintWriter out = response.getWriter();
		String otRecordIds = "";
		String[] ids = request.getParameterValues("id"); //本次需要申请报销的加班记录id
		for(int i=0;i<ids.length;i++){
			otRecordIds = ids[i] + "," + otRecordIds;
		}
		if(!"".equals(otRecordIds)){
			otRecordIds = otRecordIds.substring(0,otRecordIds.length()-1);
		}
		String staffNameCn = (String) request.getParameter("staffNameCn");
		String yearMonth = (String) request.getParameter("yearMonth");
		String weekyNum = (String) request.getParameter("weekyNum");
		String publicNum = (String) request.getParameter("publicNum");
		String statuaryHours = (String) request.getParameter("statuaryHours");
		String nightNum = (String) request.getParameter("nightNum");
        String dayNum = (String) request.getParameter("dayNum");
        String mealNum = (String) request.getParameter("mealNum");
		String taxiFee = (String) request.getParameter("taxiFee");
		String invoiceLackedAmount = (String) request
				.getParameter("invoiceLackedAmount");
		String totalAmount = (String) request.getParameter("totalAmount");

		staffNameCn = CommonUtil.decoderURL(staffNameCn);
		
		//检查是否在系统设定的期间内进行summary,如果不是，则不允许该操作
		String days = ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_PERSONAL_OT_SUMMARY_PERIOD);
		if(days!=null && !"".equals(days)){
			int period = 0;
			try{
				period = Integer.parseInt(days);
			}catch(Exception e){
			}
			//获取当前日期所在月份的第一天
			String firstDate = StringUtil.getMonthFirstDay(new Date(),"yyyy-MM-dd");
			//获取指定期间后的日期
			String targetDateStr = StringUtil.afterNDay(period,"yyyy-MM-dd",firstDate);
			//当前日期与目标日期进行比较，如果当前日期在目标日期后面，则已经超过了规定的期间，不允许进行summary操作
			String currentDateStr = StringUtil.getCurrentDateStr("yyyy-MM-dd");
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date targetDate = df.parse(targetDateStr);
			Date currentDate = df.parse(currentDateStr);
			if(currentDate.after(targetDate)){
				out.print("It exceeds the time limit to update Meal Allowance for this month,you can't update these information.");
				return null;
			}
		}
		
		OTSummaryVO vo = new OTSummaryVO();
		vo.setStaffCode(staff.getStaffCode());
		vo.setYearMonth(yearMonth);
		try {
			vo.setTeamCode(Integer.parseInt(staff.getTeamCode()));
		} catch (Exception e) {

		}
		vo.setStaffNameEn(StaffTeamHelper.getInstance().getStaffNameByCode(
				vo.getStaffCode()));
		vo.setStaffNameCn(staffNameCn);
		if (weekyNum != null && !"".equals(weekyNum)) {
			vo.setWeekyNum(Double.parseDouble(weekyNum));
		} else {
			vo.setWeekyNum(0);
		}
		if (publicNum != null && !"".equals(publicNum)) {
			vo.setPublicNum(Double.parseDouble(publicNum));
		} else {
			vo.setPublicNum(0);
		}
		if (statuaryHours != null && !"".equals(statuaryHours)) {
			vo.setStatutoryHours(Double.parseDouble(statuaryHours));
		} else {
			vo.setStatutoryHours(0);
		}
		if (nightNum != null && !"".equals(nightNum)) {
            vo.setMidNightNum(new BigDecimal(nightNum));
        } else {
            vo.setMidNightNum(BigDecimal.ZERO);
        }
		if (dayNum != null && !"".equals(dayNum)) {
            vo.setDayTimeNum(new BigDecimal(dayNum));
        } else {
            vo.setDayTimeNum(BigDecimal.ZERO);
        }
		if (mealNum != null && !"".equals(mealNum)) {
            vo.setMealAllowanceNum(new BigDecimal(mealNum));
        } else {
            vo.setMealAllowanceNum(BigDecimal.ZERO);
        }
		if (taxiFee != null && !"".equals(taxiFee)) {
			vo.setTaxiFeeAmount(Double.parseDouble(taxiFee));
		} else {
			vo.setTaxiFeeAmount(0);
		}
		if (invoiceLackedAmount != null && !"".equals(invoiceLackedAmount)) {
			vo.setPreTaxAmount(Double.parseDouble(invoiceLackedAmount)); // 税前报销金额（无发票）
		} else {
			vo.setPreTaxAmount(0);
		}
		if (totalAmount != null && !"".equals(totalAmount)) {
			vo.setTotalTaxAmount(Double.parseDouble(totalAmount));
		} else {
			vo.setTotalTaxAmount(0);
		}
        
		IDBManager dbManager = null;
		try {
			dbManager = DBManagerFactory.getDBManager();
			dbManager.startTransaction();
			OTRecordDAO dao = new OTRecordDAO(dbManager);
            //检查该员工本次申请报效的记录中是否存在还没有得到team leader confirm的记录，如果存在的话，则不允许进行update操作
			if(dao.checkConfirmedOTRecord(otRecordIds)==false){
				out.print("noconfirm");
				return null;
			}				
			
			// 首先删除teflow_ot_apply_mothly_his表中该用户当前年月已经申请的记录
			// dao.deleteOTApplyMonthHis(staff.getStaffCode(),yearMonth);
			dao.cancelStaffApplyOT(staff.getStaffCode(), yearMonth);
			// 然后将当前记录写入teflow_ot_apply_mothly_his表
             //dao.saveOTApplyMonthHis(staff.getStaffCode(), yearMonth);
            dao.saveOTApplyMonthHis(staff.getStaffCode(),yearMonth,otRecordIds);
			// 最后，写一条summary记录到teflow_ot_summary_monthly(方式也是，先删除已经存在的，然后再写新的）
			dao.deleteOTMonthlySummary(staff.getStaffCode(), yearMonth);
			dao.saveOTMonthlySummary(vo);

			dbManager.commit();
			out.print("success");
		} catch (DAOException e) {
			dbManager.rollback();
			e.printStackTrace();
			out.print("Fail to update the information!");
		} finally {
			if (dbManager != null)
				dbManager.freeConnection();
		}
		return null;
	}
    
	/**
	 * 
	 * @param mapping
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionLocation deleteOTSummary(ModuleMapping mapping,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String yearMonth = (String)request.getParameter("yearMonth");
		StaffVO staff = (StaffVO) request.getSession().getAttribute(
				CommonName.CURRENT_STAFF_INFOR);
		PrintWriter out = response.getWriter();
		IDBManager dbManager = null;
		try {
			dbManager = DBManagerFactory.getDBManager();
			OTRecordDAO dao = new OTRecordDAO(dbManager);
			dao.deleteStaffSummary(yearMonth,staff.getStaffCode());
			dao.deleteApplyOtMontly(yearMonth,staff.getStaffCode());
			out.print("success");
		}catch (DAOException e) {
			dbManager.rollback();
			e.printStackTrace();
			out.print("Fail to delete this month summary!");
		} finally {
			if (dbManager != null)
				dbManager.freeConnection();
		}
		return null;
	}
	
	public ActionLocation enterRejectOTRecords(ModuleMapping mapping,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String returnLabel = "rejectCommentsPage";
		return mapping.findActionLocation(returnLabel);
	}

	public ActionLocation rejectOTRecords(ModuleMapping mapping,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		StaffVO staff = (StaffVO) request.getSession().getAttribute(
				CommonName.CURRENT_STAFF_INFOR);
		PrintWriter out = response.getWriter();
		String[] requestNos = request.getParameterValues("requestNo");
		String rejectComments = (String) request.getParameter("rejectComments");
		if (rejectComments == null) {
			rejectComments = "";
		}

		String rejectToNodeId = ParamConfigHelper.getInstance().getParamValue(
				CommonName.PARAM_OT_REJECT_TO_NODE);

		IDBManager dbManager = null;
		try {
			dbManager = DBManagerFactory.getDBManager();
			dbManager.startTransaction();
			OTRecordDAO dao = new OTRecordDAO(dbManager);
			WorkFlowProcessDAO processDao = new WorkFlowProcessDAO(dbManager);

			if (requestNos != null && requestNos.length > 0) {
				EmailTemplateDAO emailTemplateDao = new EmailTemplateDAO(
						dbManager);
				EmailTemplateVO template = emailTemplateDao
						.getEmailTemplateByAction("04"); // Reject
				for (int i = 0; i < requestNos.length; i++) {
					System.out.println("RequestNo=" + requestNos[i]);
					WorkFlowProcessVO vo = processDao
							.getProcessVO(requestNos[i]);
					vo.setCurrentProcessor(vo.getSubmittedBy());
					vo.setPreviousProcessor(staff.getStaffCode());
					vo.setNodeId(rejectToNodeId);
					// 将该requestNo所对应的form拒绝到Requestor
					dao.rejectOTRecord(vo);
					// 并且记录操作轨迹
					dao.logRejectOTRecord(staff.getStaffCode(), requestNos[i],
							rejectComments);
					// 发送email给Requestor
					if (template == null)
						continue;
					String[] paramList = {
							CommonName.EMAIL_TEMPLATE_PARAM_REQUEST_NO,
							CommonName.EMAIL_TEMPLATE_PARAM_HANDLE_BY,
							CommonName.EMAIL_TEMPLATE_PARAM_RECEIVE_STAFF,
							CommonName.EMAIL_TEMPLATE_PARAM_CURRENT_DATE,
							CommonName.EMAIL_TEMPLATE_PARAM_COMMENTS,
							CommonName.EMAIL_TEMPLATE_PARAM_FORM_SYSTEM_ID,
							CommonName.EMAIL_TEMPLATE_PARAM_REQUESTED_BY };
					// get the staff to send
					String[] paramValue = {
							requestNos[i],
							staff.getStaffName(),
							StaffTeamHelper.getInstance().getStaffNameByCode(
									vo.getSubmittedBy()),
							StringUtil.getCurrentDateStr("MM/dd/yyyy HH:mm:ss"),
							rejectComments,
							"" + vo.getFormSystemId(),
							StaffTeamHelper.getInstance().getStaffNameByCode(
									vo.getRequestStaffCode()) };
					String[] toEmailStaffs = new String[] { vo
							.getRequestStaffCode() };
					EFlowEmailUtil.sendEmail(template, toEmailStaffs,
							paramList, paramValue);
				}
			}
			dbManager.commit();
			out.print("success");
		} catch (DAOException e) {
			dbManager.rollback();
			e.printStackTrace();
			out.print("fail");
		} finally {
			if (dbManager != null)
				dbManager.freeConnection();
		}
		return null;
	}

    public ActionLocation rejectOTRecordsByStaff(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        StaffVO staff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
        PrintWriter out = response.getWriter();
        String[] staffCodes = request.getParameterValues("staffCode");
        String rejectToNodeId = ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_OT_REJECT_TO_NODE);
        String rejectComments = "Batch Reject";

        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            OTRecordDAO dao = new OTRecordDAO(dbManager);
            Collection requestNos = dao.getWaitingConfirmByStaffs(staffCodes);
            if (requestNos != null && !requestNos.isEmpty()) {
                WorkFlowProcessDAO processDao = new WorkFlowProcessDAO(dbManager);
                EmailTemplateDAO emailTemplateDao = new EmailTemplateDAO(dbManager);
                EmailTemplateVO template = emailTemplateDao.getEmailTemplateByAction("04"); // Reject

                dbManager.startTransaction();
                Iterator it = requestNos.iterator();
                while (it.hasNext()) {
                    HashMap map = (HashMap) it.next();
                    String requestNo = (String) map.get("REQUEST_NO");
                    WorkFlowProcessVO vo = processDao.getProcessVO(requestNo);
                    vo.setCurrentProcessor(vo.getSubmittedBy());
                    vo.setPreviousProcessor(staff.getStaffCode());
                    vo.setNodeId(rejectToNodeId);
                    // 将该requestNo所对应的form拒绝到Requestor
                    dao.rejectOTRecord(vo);
                    // 并且记录操作轨迹
                    dao.logRejectOTRecord(staff.getStaffCode(), requestNo, rejectComments);
                    // 发送email给Requestor
                    if (template == null) {
                        continue;
                    }
                    String[] paramList = {CommonName.EMAIL_TEMPLATE_PARAM_REQUEST_NO,
                            CommonName.EMAIL_TEMPLATE_PARAM_HANDLE_BY, CommonName.EMAIL_TEMPLATE_PARAM_RECEIVE_STAFF,
                            CommonName.EMAIL_TEMPLATE_PARAM_CURRENT_DATE, CommonName.EMAIL_TEMPLATE_PARAM_COMMENTS,
                            CommonName.EMAIL_TEMPLATE_PARAM_FORM_SYSTEM_ID,
                            CommonName.EMAIL_TEMPLATE_PARAM_REQUESTED_BY};
                    // get the staff to send
                    String[] paramValue = {requestNo, staff.getStaffName(),
                            StaffTeamHelper.getInstance().getStaffNameByCode(vo.getSubmittedBy()),
                            StringUtil.getCurrentDateStr("MM/dd/yyyy HH:mm:ss"), rejectComments,
                            "" + vo.getFormSystemId(),
                            StaffTeamHelper.getInstance().getStaffNameByCode(vo.getRequestStaffCode())};
                    String[] toEmailStaffs = new String[] {vo.getRequestStaffCode()};
                    EFlowEmailUtil.sendEmail(template, toEmailStaffs, paramList, paramValue);
                }
                dbManager.commit();
            }
            out.print("success");
        } catch (DAOException e) {
            dbManager.rollback();
            e.printStackTrace();
            out.print("fail");
        } finally {
            if (dbManager != null)
                dbManager.freeConnection();
        }
        return null;
    }
	
	public ActionLocation enterPersonalOTSummary(ModuleMapping mapping,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String returnLabel = "personalOTQueryPage";
		StaffVO staff = (StaffVO) request.getSession().getAttribute(
				CommonName.CURRENT_STAFF_INFOR);
		// 获取已经存在的历史OT Summary 记录
		IDBManager dbManager = null;
		try {
			dbManager = DBManagerFactory.getDBManager();
			OTRecordDAO dao = new OTRecordDAO(dbManager);
			Collection summaryHisList = dao.getOTSummaryHistory(staff
					.getStaffCode());
			request.setAttribute("summaryHisList", summaryHisList);
		} catch (DAOException e) {
			e.printStackTrace();
			request.setAttribute("error", e.getMessage());
			returnLabel = "fail";
		} finally {
			if (dbManager != null)
				dbManager.freeConnection();
		}
		return mapping.findActionLocation(returnLabel);
	}

	public ActionLocation showPersonalOTSummary(ModuleMapping mapping,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String returnLabel = "personalOTSummaryPage";
		String formSystemId = ParamConfigHelper.getInstance().getParamValue(
				CommonName.PARAM_OT_FORM_SYSTEM_ID_CODE);
		String otPlanDateBegin = (String) request
				.getParameter("otPlanDateBegin");
		String otPlanDateEnd = (String) request.getParameter("otPlanDateEnd");

		String yearMonth = (String) request.getParameter("yearMonth");
		String printFlag = (String) request.getParameter("printFlag");

		String type = (String) request.getParameter("type"); // type=new----new
																// apply for
																// yearMonth;
																// type =
																// old------
																// history apply
																// for yearMonth
		if (type == null || "".equals(type)) {
			type = "old"; // default is old
		}

		String staffcode = (String) request.getParameter("staffcode");
		String kind = (String)request.getParameter("kind");//==
		
		IDBManager dbManager = null;
		try {
			dbManager = DBManagerFactory.getDBManager();
			OTRecordDAO dao = new OTRecordDAO(dbManager);
			// 获取OT记录所保存的表的字段记录
			Collection fieldList = dao.getOTFormTableFields();
			// 增加一个字段：teamLeader确认的字段
			FormSectionFieldVO confirmField = new FormSectionFieldVO();
			confirmField.setFieldId("tl_confirm");
			confirmField.setFieldLabel("主管确认 Supervisor's Confirmation");
			fieldList.add(confirmField);
			request.setAttribute("fieldList", fieldList);
			StaffVO staff = (StaffVO) request.getSession().getAttribute(
					CommonName.CURRENT_STAFF_INFOR);

			String instaffcode;//==
			if (staffcode==null || "".equals(staffcode)){
				instaffcode = staff.getStaffCode();
			}else{
				instaffcode = staffcode;
			}
			
			Collection validApplyOTRecordList = null;
			
			if ("new".equals(type)) {
				// 获取该人指定期间内的有效OT记录
				OTRecordVO conditionVo = new OTRecordVO();				
				conditionVo.setStaffCode(instaffcode);
				conditionVo.setOtPlanDateBegin(otPlanDateBegin);
				conditionVo.setOtPlanDateEnd(otPlanDateEnd);
			    validApplyOTRecordList = dao
						.getValidOTRecordForApplyByStaff(conditionVo);
				request.setAttribute("validApplyOTRecordList",
						validApplyOTRecordList);
				// 获取该员工的中文名字
				String staffNameCn = dao.getStaffChineseName(staff
						.getStaffCode());
				request.setAttribute("staffNameCn", staffNameCn);
			} else if ("old".equals(type)) {
				String status = (String) request.getParameter("status");
				/*Collection validApplyOTRecordList = dao
						.getStaffApplyOTMonthHis(staff.getStaffCode(),
								yearMonth, status);
				*/
				 validApplyOTRecordList = dao
				.getStaffApplyOTMonthHis(instaffcode,
						yearMonth, status);		
				
				 request.setAttribute("validApplyOTRecordList",
						validApplyOTRecordList);
			} else {
				request.setAttribute("validApplyOTRecordList", new ArrayList());
			}

			FormManageDAO formDao = new FormManageDAO(dbManager);
			FormManageVO form = formDao.getFormBaseInforBySystemId(Integer
					.parseInt(formSystemId));
			request.setAttribute("otForm", form);

			// 如果是打印，则需要获取其他信息
			// if(printFlag!=null && "true".equals(printFlag)){
			
			  //System.out.println("This staffcode is :=====>"+instaffcode);
			  OTSummaryVO summaryVo = dao.getOTSummaryVOByStaff(instaffcode,yearMonth);//==
			request.setAttribute("summaryVo", summaryVo);
			
			//如果是在弹出的新窗口显示这些内容，则需要增加两列数据“Check In / Check Out”. 单独获取该人的这些加班记录的打卡时间
            if(kind!=null && !"".equals(kind) && validApplyOTRecordList!=null){
            	Iterator it = validApplyOTRecordList.iterator();
            	String otDateStr = "";
            	java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");
            	while(it.hasNext()){
            		HashMap map = (HashMap)it.next();
            		//otDate[i] = (String)map.get("OT_PLAN_DATE");
            		java.util.Date cDate = df.parse((String) map.get("OT_PLAN_DATE"));  
            		otDateStr = otDateStr+"'"+StringUtil.getDateStr(cDate,"MM/dd/yyyy")+"',";
            	}
            	if(otDateStr!=null && !"".equals(otDateStr)){
            		otDateStr = otDateStr.substring(0,otDateStr.length()-1);
            		HashMap resultMap = dao.getCheckInoutRecords(otDateStr,staffcode);
            		request.setAttribute("resultMap",resultMap);
            	}
            	
            }
			
			//if (kind!=null || !"".equals(kind)){
				request.setAttribute("kind", kind);
			//}
			// }
               //检查是否在系统设定的期间内进行summary,如果不是，则'Update'和'Add'按钮都不可用
			  String days = ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_PERSONAL_OT_SUMMARY_PERIOD);
			  if(days!=null && !"".equals(days)){
					int period = 0;
					try{
						period = Integer.parseInt(days);
					}catch(Exception e){
					}
					//获取当前日期所在月份的第一天
					String firstDate = StringUtil.getMonthFirstDay(new Date(),"yyyy-MM-dd");
					//获取指定期间后的日期
					String targetDateStr = StringUtil.afterNDay(period,"yyyy-MM-dd",firstDate);
					//当前日期与目标日期进行比较，如果当前日期在目标日期后面，则已经超过了规定的期间，不允许进行summary操作
					String currentDateStr = StringUtil.getCurrentDateStr("yyyy-MM-dd");
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					Date targetDate = df.parse(targetDateStr);
					Date currentDate = df.parse(currentDateStr);
					if(currentDate.after(targetDate)){
					  request.setAttribute("canUpate","false");
					}else{
						request.setAttribute("canUpate","true");	
					}
				}
		} catch (DAOException e) {
			e.printStackTrace();
			request.setAttribute("error", e.getMessage());
			returnLabel = "fail";
		} finally {
			if (dbManager != null)
				dbManager.freeConnection();
		}

		return mapping.findActionLocation(returnLabel);
	}

	/**
	 * 检查指定年月，当前操作人是否已经存在申请的记录
	 * 
	 * @param mapping
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionLocation checkSummaryFor(ModuleMapping mapping,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		PrintWriter out = response.getWriter();
		StaffVO staff = (StaffVO) request.getSession().getAttribute(
				CommonName.CURRENT_STAFF_INFOR);
		String yearMonth = (String) request.getParameter("yearMonth");
		IDBManager dbManager = null;
		try {
			dbManager = DBManagerFactory.getDBManager();
			OTRecordDAO dao = new OTRecordDAO(dbManager);

			// 检查当前年月是否已经 结束
			// 报销了，status=='02'.只要表teflow_ot_summary_monthly存在一条当前年月记录的状态为“02（已结束）”，则不能再summary到该年月
			if (dao.isCloseYearMonthSummary(yearMonth)) {
				out.print("close");
				return null;
			}

			OTSummaryVO vo = dao.getOTSummaryVOByStaff(staff.getStaffCode(),
					yearMonth);
			if (vo != null) {
				out.print("yes");
			} else {
				out.print("no");
			}
		} catch (DAOException e) {
			out.print("fail");
			e.printStackTrace();
		} finally {
			if (dbManager != null)
				dbManager.freeConnection();
		}
		return null;
	}

	public ActionLocation enterTeamOTSummary(ModuleMapping mapping,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String returnLabel = "teamOTQueryPage";
		return mapping.findActionLocation(returnLabel);
	}
	
	/**
	 * 在页面使用ajax方法获取某年月某个team的OT汇总信息，请调用该方法
	 * 将获得的汇总信息，按照一个固定格式的表格方式方法。
	 * @param mapping
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionLocation ajaxTeamOTSummary(ModuleMapping mapping,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String yearMonth = (String) request.getParameter("yearMonth");
		String teamCode = (String) request.getParameter("teamCode");
		String location = (String) request.getParameter("location");
		StringBuffer str = new StringBuffer("<table id='formTable02' width='100%'  border='1' cellpadding='3' cellspacing='0' bordercolor='#6595D6' style='border-collapse:collapse;'>");
		str.append("<tr class=\"liebiao_tou\">")
		   .append("<td colspan='2' align='center'>姓名 Name</td>")
		   .append("<td rowspan=\"2\" align='center'>员工编号 Staff Code</td>")
		   .append("<td colspan='3' align='center'>误餐次数 No.of Meal Allowance</td>")
		   .append("<td rowspan='2' align='center'>法定假期(小时)Statutory Holiday</td>")
		   .append("<td colspan='3' align='center'>误餐津贴金额(元) Amount of Meal Allowance</td>")
		   .append("<td rowspan='2' align='center'>的士费用总计 Taxi Fee Total</td>")
		   .append("</tr><tr class=\"liebiao_tou\">")
		   .append("<td align='center'>中文名 Chinese Name</td>")
		   .append("<td align='center'>英文名 English Name</td>")
		   .append("<td align='center'>正常工作日 Weekday</td>")
		   .append("<td align='center'>公众假期 Public Holiday</td>")
		   .append("<td align='center'>小计 Sub-total</td>")
		   .append("<td align='center'>误餐津贴总额 Total Amount</td>")
		   .append("<td align='center'>税前报销金额(无发票) Pre-tax Amount</td>")
		   .append("<td align='center'>税后报销金额(有发票) After-tax Amount</td>")
		   .append("</tr>");//表头
		String teamarrStr = "";
		response.setContentType("text/html;charset=GBK"); //it is very important
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");
		PrintWriter out = response.getWriter();
		IDBManager dbManager = null;
		try {
			dbManager = DBManagerFactory.getDBManager();
			if (teamCode != null && !"".equals(teamCode)) {
				TeamDAO teamDao = new TeamDAO(dbManager);
				// 构造逗号分割的Team_code串
				teamarrStr = teamDao.groupSubTeam(teamCode);
				if (teamarrStr == null || "".equals(teamarrStr)) {
					teamarrStr = teamCode;
				} else {
					teamarrStr = teamarrStr.substring(0, teamarrStr
							.lastIndexOf(","));
					teamarrStr = teamCode + "," + teamarrStr;
				}
			} else {
				teamarrStr = "";
			}
			OTRecordDAO dao = new OTRecordDAO(dbManager);
			Collection summaryList = dao.getOTSummaryByTeam(yearMonth,
					teamarrStr, location);
			if(summaryList!=null && summaryList.size()>0){
		    	  double totalWeekday = 0;
		    	  double totalPublicDay = 0;
		    	  double totalStatHours = 0;
		    	  double totalSub = 0;
		    	  double totalTaxAmount = 0;
		    	  double totalPreTaxAmount = 0;
		    	  double totalAfterTaxAmount = 0;
		    	  double totalTaxiFee = 0;
		    	  Iterator it = summaryList.iterator();
		    	  while(it.hasNext()){
		    		 str.append("<tr class=\"tr_change\">");
		    		 OTSummaryVO vo = (OTSummaryVO)it.next();
		    		 str.append("<td>"+vo.getStaffNameCn()+"</td>");
		    		 str.append("<td>"+vo.getStaffNameEn()+"</td>");
		    		 str.append("<td align='center'>"+vo.getStaffCode()+"</td>");
		    		 str.append("<td align='right'>"+vo.getWeekyNum()+"</td>");
		    		 totalWeekday = totalWeekday + vo.getWeekyNum();
		    		 str.append("<td align='right'>"+vo.getPublicNum()+"</td>");
		    		 totalPublicDay = totalPublicDay + vo.getPublicNum();
		    		 str.append("<td align='right'>"+(vo.getWeekyNum()+vo.getPublicNum())+"</td>");
		    		 totalSub = totalSub + (vo.getWeekyNum()+vo.getPublicNum());
		    		 str.append("<td align='right'>"+vo.getStatutoryHours()+"</td>");
		    		 totalStatHours = totalStatHours + vo.getStatutoryHours();
		    		 str.append("<td align='right'>"+vo.getTotalTaxAmount()+"</td>");
		    		 totalTaxAmount = totalTaxAmount + vo.getTotalTaxAmount();
		    		 str.append("<td align='right'>"+vo.getPreTaxAmount()+"</td>");
		    		 totalPreTaxAmount = totalPreTaxAmount + vo.getPreTaxAmount();
		    		 str.append("<td align='right'>"+vo.getAfterTaxAmount()+"</td>");
		    		 totalAfterTaxAmount = totalAfterTaxAmount + vo.getAfterTaxAmount();
		    		 str.append("<td align='right'>"+vo.getTaxiFeeAmount()+"</td>");
		    		 totalTaxiFee = totalTaxiFee + vo.getTaxiFeeAmount();
		    		 str.append("</tr>");
		    	  }
		    	  str.append("<tr>")
		    	     .append("<td colspan='3' align='center'>总计(Total)</td>")
		    	     .append("<td align='right'><b>"+totalWeekday+"</b></td>")
		    	     .append("<td align='right'><b>"+totalPublicDay+"</b></td>")
		    	     .append("<td align='right'><b>"+totalSub+"</b></td>")
		    	     .append("<td align='right'><b>"+totalStatHours+"</b></td>")
		    	     .append("<td align='right'><b>"+totalTaxAmount+"</b></td>")
		    	     .append("<td align='right'><b>"+totalPreTaxAmount+"</b></td>")
		    	     .append("<td align='right'><b>"+totalAfterTaxAmount+"</b></td>")
		    	     .append("<td align='right'><b>"+totalTaxiFee+"</b></td>")
		    	     .append("</tr>");		    	  
			}
			str.append("</table>");

			Collection<OTSummaryCheckVO> traceList = dao.getCheckTraceByMonthTeam(yearMonth,Integer.parseInt(teamCode));
			str.append("<br><table id='confirmTraceTable' width='100%' border=1 cellpadding=3 cellspacing=0 bordercolor='#6595D6' style='border-collapse:collapse;'>");
			str.append("<tr class='liebiao_tou'><td>No.</td><td>Date</td><td>Action</td><td>Operate Staff</td><td>Remark</td></tr>");
			if(traceList!=null && traceList.size()>0){
		    	int i = 1;
		    	for(OTSummaryCheckVO temp:traceList){
				  str.append("<tr>");
				  str.append("<td>"+i+"</td>")
					   .append("<td>").append(temp.getActionDate()).append("</td>")
					   .append("<td>").append(temp.getActionCode()).append("</td>")
					   .append("<td>").append(StaffTeamHelper.getInstance().getStaffNameByCode(temp.getStaffCode())).append("</td>")
					   .append("<td>").append(temp.getRemark()).append("</td>");
				  str.append("</tr>");
				  i++;
		    	}
			}
			str.append("</table>");
			out.println(str.toString());
		} catch (DAOException e) {
			e.printStackTrace();
			request.setAttribute("error", e.getMessage());
			out.print("fail");
		} finally {
			if (dbManager != null)
				dbManager.freeConnection();
		}	
		return null;
	}

	public ActionLocation showTeamOTSummary(ModuleMapping mapping,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String returnLabel = "teamOTSummaryPage";
		String yearMonth = (String) request.getParameter("yearMonth");
		String teamCode = (String) request.getParameter("teamCode");
		String location = (String) request.getParameter("location");
		String fileType = (String) request.getParameter("fileType");
		String printFlag = (String)request.getParameter("printFlag");
		if(printFlag==null){
			printFlag = "false";
		}
		if (fileType != null && !"".equals(fileType)) {
			returnLabel = "exportOTSummaryPage";
		}
		String teamarrStr = "";
		IDBManager dbManager = null;
		try {
			dbManager = DBManagerFactory.getDBManager();
			if (teamCode != null && !"".equals(teamCode)) {
				TeamDAO teamDao = new TeamDAO(dbManager);
				// 构造逗号分割的Team_code串
				teamarrStr = teamDao.groupSubTeam(teamCode);
				if (teamarrStr == null || "".equals(teamarrStr)) {
					teamarrStr = teamCode;
				} else {
					teamarrStr = teamarrStr.substring(0, teamarrStr
							.lastIndexOf(","));
					teamarrStr = teamCode + "," + teamarrStr;
				}
			} else {
				teamarrStr = "";
			}
			OTRecordDAO dao = new OTRecordDAO(dbManager);
			Collection summaryList = dao.getOTSummaryByTeam(yearMonth,
					teamarrStr, location);
			if("false".equals(printFlag)){
			  //获取check过的历史纪录
			  Collection list = dao.getCheckTraceByMonthTeam(yearMonth,Integer.parseInt(teamCode));
			  request.setAttribute("traceList",list);
			}
			request.setAttribute("summaryList", summaryList);
		} catch (DAOException e) {
			e.printStackTrace();
			request.setAttribute("error", e.getMessage());
			returnLabel = "fail";
		} finally {
			if (dbManager != null)
				dbManager.freeConnection();
		}
		return mapping.findActionLocation(returnLabel);
	}

	/**
	 * 确认当前统计年月的team的数据ok
	 * @param mapping
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
   public ActionLocation confirmStaffOTSummary(
			ModuleMapping mapping,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		String yearMonth = (String)request.getParameter("yearMonth");
		String teamCode = (String)request.getParameter("teamCode");
		String location = (String)request.getParameter("location");
	    String locName = location == null || location.equals("") ? "" : location.equals("GZ") ? " (Guangzhou)" : " (Beijing)";
		String mailTo = (String)request.getParameter("mailTo");  //如果该参数有值，则需要发送email给这些人
		String isComplete = (String)request.getParameter("isComplete");
		String action = "Confirmed";
		
		if(teamCode==null || "".equals(teamCode)){
			teamCode = "0";
		}
		StaffVO staff = (StaffVO)request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
		OTSummaryCheckVO vo = new OTSummaryCheckVO();
		vo.setTeamCode(Integer.parseInt(teamCode));
		vo.setYearMonth(yearMonth);
		vo.setStaffCode(staff.getStaffCode());
		if("1".equals(isComplete)){
			vo.setActionCode("03");// complete
			action = "Final Confirm";
		}else{
			vo.setActionCode("01");// confirm
		}
		String teamName = StaffTeamHelper.getInstance().getTeamNameByCode(staff.getTeamCode());
		String teamName2 = StaffTeamHelper.getInstance().getTeamNameByCode(teamCode);
		if(teamName!=null && !"".equals(teamName)){
			teamName = teamName.trim();
		}
        vo.setRemark(staff.getStaffName().trim() + "(" + teamName + ") " + action.toLowerCase() + " " + teamName2
                + locName + " team report.");
		vo.setActionDate(StringUtil.getCurrentDateStr("MM/dd/yyyy HH:mm:ss"));
		PrintWriter out = response.getWriter();
		IDBManager dbManager = null;
		StringBuffer str = new StringBuffer("");
		try{
			dbManager =  DBManagerFactory.getDBManager();
			dbManager.startTransaction();
			OTRecordDAO dao = new OTRecordDAO(dbManager);
			dao.checkMonthlySummaryOT(vo);
			dbManager.commit();
			//发送mail
			if(mailTo!=null && !"".equals(mailTo)){
				String[] toStaff = StringUtil.split(mailTo,",");				
				EFlowEmailUtil.sendEmail("Meal Allowance "+action+" (" + yearMonth+" "+teamName+")",vo.getRemark(),toStaff);				
			}
			Collection list = dao.getCheckTraceByMonthTeam(yearMonth,Integer.parseInt(teamCode));
			str.append("<table id='confirmTraceTable' width='100%'  border='1' cellpadding='3' cellspacing='0' bordercolor='#6595D6' style='border-collapse:collapse;'>");
			if(list!=null && list.size()>0){
				Iterator it = list.iterator();
				int i = 1;
				str.append("<tr class='liebiao_tou'><td>No.</td><td>Date</td><td>Action</td><td>Operate Staff</td><td>Remark</td></tr>");
				while(it.hasNext()){
					OTSummaryCheckVO temp = (OTSummaryCheckVO)it.next();
					str.append("<tr>");
					str.append("<td>"+i+"</td>")
					   .append("<td>").append(temp.getActionDate()).append("</td>")
					   .append("<td>").append(temp.getActionCode()).append("</td>")
					   .append("<td>").append(StaffTeamHelper.getInstance().getStaffNameByCode(temp.getStaffCode())).append("</td>")
					   .append("<td>").append(temp.getRemark()).append("</td>");
					str.append("</tr>");
					i++;
				}
			}
			str.append("</table>");
			out.print(str.toString());
		}catch(Exception e){
			out.print("fail");
			dbManager.rollback();
			e.printStackTrace();
		}finally{
			if(dbManager!=null) dbManager.freeConnection();
		}   
		return null;
	}	/**
		 * 将指定员工的指定年月的报销申请取消掉
		 * （1.删除该员工的该年月的报销汇总记录；2.删除该员工申请该年月报销的申请记录(teflow_ot_apply_monthly_his)；3.删除Team Leader确认过的记录，需要Team Leader Reject. 
		 *     4.发送email给该员工，通知其申请的该年月报销被XX取消
		 * 
		 * @param mapping
		 * @param request
		 * @param response
		 * @return
		 * @throws Exception
		 */
	public ActionLocation cancelStaffOTSummary(ModuleMapping mapping,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		StaffVO staff = (StaffVO) request.getSession().getAttribute(
				CommonName.CURRENT_STAFF_INFOR);
		// String staffCode = (String)request.getParameter("staffCode");
		String[] staffCodes = request.getParameterValues("staffCode");
		String yearMonth = (String) request.getParameter("yearMonth");
		String teamCode = (String) request.getParameter("teamCode");
		PrintWriter out = response.getWriter();
		IDBManager dbManager = null;
		String emailSubject = "Personal Summary of Meal Allowance was cancelled ("
				+ yearMonth + ")";
		String emailContent = "Your Personal Summary of Meal Allowance for "
				+ yearMonth
				+ " has been  <font color='red'>cancelled</font> by "
				+ StaffTeamHelper.getInstance().getStaffNameByCode(
						staff.getStaffCode()) + ".";
		
		OTSummaryCheckVO vo = new OTSummaryCheckVO();
		vo.setTeamCode(Integer.parseInt(teamCode));
		vo.setYearMonth(yearMonth);
		vo.setStaffCode(staff.getStaffCode());
		vo.setActionCode("02");// canceled
		String teamName = StaffTeamHelper.getInstance().getTeamNameByCode(staff.getTeamCode());
		if(teamName!=null && !"".equals(teamName)){
			teamName = teamName.trim();
		}
		String staffName = staff.getStaffName().trim();
		
		vo.setActionDate(StringUtil.getCurrentDateStr("MM/dd/yyyy HH:mm:ss"));
		
		try {
			dbManager = DBManagerFactory.getDBManager();
			dbManager.startTransaction();
			OTRecordDAO dao = new OTRecordDAO(dbManager);
			for (int i = 0; i < staffCodes.length; i++) {
				
				dao.cancelTeamLeaderConfirm(staffCodes[i], yearMonth);//此操作必须放最前
				dao.cancelStaffOTSummary(staffCodes[i], yearMonth);
				dao.cancelStaffApplyOT(staffCodes[i], yearMonth);
				
				String cancelStaffName = StaffTeamHelper.getInstance().getStaffNameByCode(staffCodes[i]);
				if(cancelStaffName!=null){
					cancelStaffName = cancelStaffName.trim();
				}
				vo.setRemark(staffName+"("+teamName+") canceled staff("+cancelStaffName+"-"+staffCodes[i]+") on "
						+StringUtil.getCurrentDateStr("MM/dd/yyyy HH:mm:ss")+".");
				dao.checkMonthlySummaryOT(vo);
				
				EFlowEmailUtil.sendEmail(emailSubject, emailContent,
						new String[] { staffCodes[i] });
			}
			dbManager.commit();
			
			out.print("success");
		} catch (DAOException e) {
			out.print("fail");
			dbManager.rollback();
			e.printStackTrace();
		} finally {
			if (dbManager != null)
				dbManager.freeConnection();
		}
		return null;
	}

	/**
	 * 结束该年月的ot申请报销，并发送email给所有有申请报销记录的员工（通知其报销费用已到帐）
	 * 
	 * @param mapping
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionLocation completeOTSummary(ModuleMapping mapping,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		StaffVO staff = (StaffVO) request.getSession().getAttribute(
				CommonName.CURRENT_STAFF_INFOR);
		String yearMonth = (String) request.getParameter("yearMonth");
		String location = (String)request.getParameter("location");

		PrintWriter out = response.getWriter();
		IDBManager dbManager = null;
		try {
			dbManager = DBManagerFactory.getDBManager();
			dbManager.startTransaction();
			OTRecordDAO dao = new OTRecordDAO(dbManager);
			// 1.update summary status='02'
			dao.completeOTSummary(yearMonth, location);
			// 2.update all apply monthly his status='02'
			Collection requestStaffs = dao.getOTSummaryStaffList(yearMonth, location);
			if (requestStaffs != null && requestStaffs.size() > 0) {
				Iterator it = requestStaffs.iterator();
				while (it.hasNext()) {
					HashMap map = (HashMap) it.next();
					dao.completedOTRecord(staff.getStaffCode(), yearMonth,
							(String) map.get("STAFF_CODE"));
				}
			}
			dbManager.commit();
			String emailSubject = "Reimbursement";
			String currentDate = StringUtil.getCurrentDateStr("MM/dd/yyyy");
			String emailContent = "";
			// 3.send email to requester to inform them that ot had paid
			if (requestStaffs != null && requestStaffs.size() > 0) {
				Iterator it = requestStaffs.iterator();
				double amount = 0.0;
				while (it.hasNext()) {
					HashMap map = (HashMap) it.next();
					/**
					if (map.get("TOTAL_TAX_AMOUNT") != null
							&& !"".equals((String) map.get("TOTAL_TAX_AMOUNT"))) {
						amount = amount
								+ Double.parseDouble((String) map
										.get("TOTAL_TAX_AMOUNT"));
					}*/
					if (map.get("TAXI_FEE_AMOUNT") != null
							&& !"".equals((String) map.get("TAXI_FEE_AMOUNT"))) {
						amount = amount
								+ Double.parseDouble((String) map
										.get("TAXI_FEE_AMOUNT"));
					}
                    if (amount > 0) {
                        amount = amount * 100 / 100.00;
                        emailContent = "<br>Please be informed that the reimbursement for “ Taxi Fee of "
                                + yearMonth
                                + "” of <font color='red'>￥"
                                + amount
                                + "</font> was banked in your payroll account on "
                                + currentDate
                                + ".<br>"
                                + "<br>Please check it timely. In case of any queries, please contact HR or Finance, thanks!";

                        if (map != null && map.get("STAFF_CODE") != null && !"".equals((String) map.get("STAFF_CODE"))) {
                            EFlowEmailUtil.sendEmail(emailSubject, emailContent, new String[] {(String) map
                                    .get("STAFF_CODE")});
                        }
                    }
					amount = 0;
				}
			}
			out.print("success");
		} catch (DAOException e) {
			out.print("fail");
			dbManager.rollback();
			e.printStackTrace();
		} finally {
			if (dbManager != null)
				dbManager.freeConnection();
		}

		return null;
	}

	public ActionLocation showCompanyOTSummary(ModuleMapping mapping,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String returnLabel = "companyOTSummaryPage";
		String yearMonth = (String) request.getParameter("yearMonth");
		String location = (String) request.getParameter("location");
		String fileType = (String) request.getParameter("fileType");
		if (fileType != null && !"".equals(fileType)) {
			returnLabel = "exportCompanyOTSummaryPage";
		}
		IDBManager dbManager = null;
		try {
			dbManager = DBManagerFactory.getDBManager();
			OTRecordDAO dao = new OTRecordDAO(dbManager);
			Collection summaryList = dao.getOTSummaryByTeam(yearMonth, "", location);
			request.setAttribute("summaryList", summaryList);
		} catch (DAOException e) {
			e.printStackTrace();
			request.setAttribute("error", e.getMessage());
			returnLabel = "fail";
		} finally {
			if (dbManager != null)
				dbManager.freeConnection();
		}
		return mapping.findActionLocation(returnLabel);
	}

	/**
	 * 进入到导出excel文件的模板文件选择页面
	 * 
	 * @param mapping
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionLocation showExcelTemplateSelect(ModuleMapping mapping,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String returnLabel = "excelTemplateSelectPage";
		return mapping.findActionLocation(returnLabel);
	}

	public ActionLocation getOrigFile(ModuleMapping mapping,
			HttpServletRequest request0, HttpServletResponse response)
			throws Exception {
		String returnLabel = "exportCompanyOTSummaryPage";
		StaffVO currentStaff = (StaffVO) request0.getSession().getAttribute(
				CommonName.CURRENT_STAFF_INFOR);
		String userId = currentStaff.getLogonId();
		String yearMonth = (String) request0.getParameter("yearMonth");
		String location = (String) request0.getParameter("location");
		Collection newSummaryList = new ArrayList();

		if (request0.getMethod().equals("POST")
				&& MultipartRequest.isMultipart(request0)) {

			HttpServletRequest request = new MultipartRequest(request0, userId);//
			File upFile = ((MultipartRequest) request).getFile("path");
			FileReader fileread = null;
			BufferedReader bufread = null;
			String read = "";
			HashMap staffAdditionalMap = new HashMap();
			if (upFile != null) {
				fileread = new FileReader(upFile);
				bufread = new BufferedReader(fileread);
				while ((read = bufread.readLine()) != null) {
					read = read.trim();
					String[] content = StringUtil.split(read, "||"); // read内容的形式是：staffCode||中文名字||银行账号||额外报销金额
																		// （如果只有前3项，则"额外报销金额"为0）
					if (content != null || content.length >= 3) {
						if ("".equals(content[0]) || "".equals(content[1])
								|| "".equals(content[2])) {
							request
									.setAttribute("error",
											"The data format in selected file are incorrect,please check!");
							return mapping.findActionLocation("fail");
						}
						StaffVO staff = new StaffVO();
						staff.setStaffCode(content[0]);
						staff.setChineseName(content[1]);
						staff.setAccountNo(content[2]);
						if (content.length > 3 && content[3] != null
								&& !"".equals(content[3])) {
							try {
								staff.setAdditionalAmount(Double
										.parseDouble(content[3]));
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						// System.out.println(content[0]+","+content[1]+","+content[2]);
						staffAdditionalMap.put(staff.getStaffCode()
								.toUpperCase(), staff);
					} else {
						request
								.setAttribute("error",
										"The data format in selected file are incorrect,please check!");
						return mapping.findActionLocation("fail");
					}
				}
				// 得到原始数据后，开始生成报表数据

				IDBManager dbManager = null;
				try {
					dbManager = DBManagerFactory.getDBManager();
					OTRecordDAO dao = new OTRecordDAO(dbManager);
					Collection summaryList = dao.getOTSummaryByTeam(yearMonth,
							"", location);

					if (summaryList != null) {
						Iterator it = summaryList.iterator();
						while (it.hasNext()) {
							OTSummaryVO vo = (OTSummaryVO) it.next();
							if (staffAdditionalMap.containsKey(vo
									.getStaffCode().trim().toUpperCase())) {
								StaffVO staff = (StaffVO) staffAdditionalMap
										.get(vo.getStaffCode().trim()
												.toUpperCase());
								vo.setAccountNo(staff.getAccountNo());
								vo.setStaffNameCn(staff.getChineseName());
								vo.setTaxiFeeAmount((staff
										.getAdditionalAmount() + vo
										.getTaxiFeeAmount()) * 100 / 100.00);
							}
							System.out.println(":::::" + vo.getAccountNo());
							newSummaryList.add(vo);
						}
					}
				} catch (DAOException e) {
					e.printStackTrace();
					request.setAttribute("error", e.getMessage());
					returnLabel = "fail";
				} finally {
					if (dbManager != null)
						dbManager.freeConnection();
				}
				// request.getSession(true).setAttribute(CommonName.STAFF_ADDITIONAL_MAP,staffAdditionalMap);
			} else {
				request.setAttribute("error",
						"It can't get the selected file,please check!");
				return mapping.findActionLocation("fail");
			}
		}
		request0.setAttribute("summaryList", newSummaryList);
		return mapping.findActionLocation(returnLabel);
	}
}
