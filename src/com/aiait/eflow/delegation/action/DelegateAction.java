package com.aiait.eflow.delegation.action;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.aiait.eflow.common.CommonName;
import com.aiait.eflow.common.ModuleOperateName;
import com.aiait.eflow.common.helper.AuthorityHelper;
import com.aiait.eflow.common.helper.FormTypeHelper;
import com.aiait.eflow.common.helper.ParamConfigHelper;
import com.aiait.eflow.common.helper.StaffTeamHelper;
import com.aiait.eflow.delegation.dao.DelegateDAO;
import com.aiait.eflow.delegation.vo.DelegationVO;
import com.aiait.eflow.formmanage.dao.FormManageDAO;
import com.aiait.eflow.housekeeping.dao.EmailTemplateDAO;
import com.aiait.eflow.housekeeping.dao.StaffDAO;
import com.aiait.eflow.housekeeping.dao.TeamDAO;
import com.aiait.eflow.housekeeping.vo.EmailTemplateVO;
import com.aiait.eflow.housekeeping.vo.FormTypeVO;
import com.aiait.eflow.housekeeping.vo.StaffVO;
import com.aiait.eflow.util.EmailUtil;
import com.aiait.eflow.util.OverDueUtil;
import com.aiait.eflow.util.StringUtil;
import com.aiait.eflow.wkf.dao.WorkFlowProcessDAO;
import com.aiait.eflow.wkf.vo.AdjustProcessorLogVO;
import com.aiait.eflow.wkf.vo.WorkFlowProcessVO;
import com.aiait.framework.db.DBManagerFactory;
import com.aiait.framework.db.IDBManager;
import com.aiait.framework.exception.DAOException;
import com.aiait.framework.mvc.action.ActionLocation;
import com.aiait.framework.mvc.action.DispatchAction;
import com.aiait.framework.mvc.action.ModuleMapping;

public class DelegateAction extends DispatchAction {
		
	public class OverlapException extends Exception{
		private String _subject;
		private OverlapException(String subject){
			super();			
			_subject = subject;
		}
	}
	
	/**
	 * 显示当前处理人下的所有等待其处理的form列表
	 * @param mapping
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionLocation enterReassign(
			ModuleMapping mapping,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		String returnLabel = "listReassignFormPage";
		String staffCode = (String)request.getParameter("staffCode");
	    //String deputyCode = (String)request.getParameter("deputyCode");
	    WorkFlowProcessVO vo = new WorkFlowProcessVO();
	    vo.setCurrentProcessor(staffCode);	
		IDBManager dbManager = null;
		try {
			dbManager = DBManagerFactory.getDBManager();
			WorkFlowProcessDAO dao = new WorkFlowProcessDAO(dbManager);
			Collection list = dao.getWaitingForManualFormList(vo);
			request.setAttribute("inquiryFormList", list);
		}catch (DAOException e) {
			returnLabel = "fail";
			request.setAttribute("error","Error ocurred during searched form list. Error:"+e.getMessage());
			e.printStackTrace();
		} finally {
			dbManager.freeConnection();
		}
		return mapping.findActionLocation(returnLabel);
	}
	
	public ActionLocation reassignForm(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        PrintWriter out = response.getWriter();
        IDBManager dbManager = null;
        response.setContentType("text/xml");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        String deputyCode = (String) request.getParameter("deputyCode");
        String[] requestNos = request.getParameterValues("requestNo");
        String staffCode = (String) request.getParameter("staffCode"); // origin staff

        String[] toEmailStaffs = { deputyCode };

        StaffVO currentStaff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
        String handleType = "03"; // approve
        if (requestNos != null && requestNos.length > 0) {
            try {
                dbManager = DBManagerFactory.getDBManager();
                WorkFlowProcessDAO dao = new WorkFlowProcessDAO(dbManager);
                EmailTemplateDAO emailTemplateDao = new EmailTemplateDAO(dbManager);
                // dbManager.startTransaction();
                EmailTemplateVO template = emailTemplateDao.getEmailTemplateByAction(handleType);
                AdjustProcessorLogVO vo = new AdjustProcessorLogVO();
                vo.setOperateStaffCode(currentStaff.getStaffCode());
                for (int i = 0; i < requestNos.length; i++) {
                    WorkFlowProcessVO processVo = dao.getProcessVO(requestNos[i]);
                    //
                    String tempProcessStaffs = processVo.getCurrentProcessor();
                    tempProcessStaffs = StringUtil.replace(tempProcessStaffs, staffCode, deputyCode);
                    vo.setRequestNo(requestNos[i]);
                    vo.setAdjustToProcessor(deputyCode);
                    try {
                        dbManager.startTransaction();
                        dao.adjustProcessor(requestNos[i], tempProcessStaffs);
                        dao.adjustProcessorLog(vo);
                        dbManager.commit();
                    } catch (Exception ex) {
                        dbManager.rollback();
                        ex.printStackTrace();
                        out.print("fail");
                        return null;
                    }
                    sendEmail(template, processVo.getRequestStaffCode(), requestNos[i], "", "", toEmailStaffs, ""
                            + processVo.getFormSystemId(), processVo);
                }
                // dbManager.commit();
                out.print("success");
            } catch (Exception e) {
                // dbManager.rollback();
                out.print("fail");
                e.printStackTrace();
            } finally {
                if (dbManager != null)
                    dbManager.freeConnection();
                if (out != null)
                    out.close();
            }
        }
        return null;
    }
	
	 /**
     * list all deputy handle request form
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
	public ActionLocation listDeputyHandle(
			ModuleMapping mapping,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		String returnLabel = "listDeputyHandlePage";
		DelegationVO queryVo = new DelegationVO();
		String approver = (String)request.getParameter("approver");
		String deputy = (String)request.getParameter("deputy");
		String handleBeginDate = (String)request.getParameter("handleBeginDate");
		String handleEndDate = (String)request.getParameter("handleEndDate");
		String isSearching = (String)request.getParameter("isSearching");
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		Calendar cl = Calendar.getInstance();
		cl.add(Calendar.MONTH, -1); //1 months before
		
		if(approver!=null && !"".equals(approver)){
			queryVo.setAuthorityApprover(approver);
		}
		if(deputy!=null && !"".equals(deputy)){
			queryVo.setAuthorityDeputy(deputy);
		}
		if(handleBeginDate!=null && !"".equals(handleBeginDate)){
			queryVo.setHandledBeginDate(handleBeginDate);
		} else {
			if(isSearching==null){	//If is not searching from page, set default value
				queryVo.setHandledBeginDate(sdf.format(cl.getTime()));
			}
		}
		if(handleEndDate!=null && !"".equals(handleEndDate)){
			queryVo.setHandledEndDate(handleEndDate);
		}
		
		StaffVO currentStaff = (StaffVO)request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
		String currentRole = currentStaff.getCurrentRoleId();
        AuthorityHelper authority = AuthorityHelper.getInstance();
        if(!authority.checkAuthority(currentRole,ModuleOperateName.MODULE_DEPUTY_HANDLE,ModuleOperateName.OPER_DEPUTY_HANDLE_QUERY_ALL)){
        	queryVo.setAuthorityApprover(currentStaff.getStaffCode());
        	request.setAttribute("type","self");
        }else{
        	request.setAttribute("type","all");
        }
        request.setAttribute("queryVo",queryVo);
		IDBManager dbManager = null;
		try{
		  dbManager =  DBManagerFactory.getDBManager();
		  DelegateDAO dao = new DelegateDAO(dbManager);
		  StaffDAO sdao = new StaffDAO(dbManager);
		  Collection list = dao.getAllDeputyHandle(queryVo, request.getParameter("orgid"));
		  request.setAttribute("resultList",list);
			 Collection staffList = sdao.getStaffListByCompanyAndSubCompany(currentStaff.getLowerCompanys());			
			  request.setAttribute("staffList", staffList);
		}catch(Exception e){
		  returnLabel = "fail";
		  e.printStackTrace();	
		}finally{
			if(dbManager!=null) dbManager.freeConnection();
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
	public ActionLocation listAvailable(
			ModuleMapping mapping,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		String returnLabel = "listPage";
		IDBManager dbManager = null;
		try{
		  dbManager =  DBManagerFactory.getDBManager();
		  DelegateDAO dao = new DelegateDAO(dbManager);
		  //Collection list = dao.getList("01");
		  DelegationVO vo = new DelegationVO();
		  vo.setStatus("01");
		  if(request.getParameter("begin_date")!=null)vo.setHandledBeginDate(request.getParameter("begin_date"));
		  if(request.getParameter("end_date")!=null)vo.setHandledEndDate(request.getParameter("end_date"));
		  		  
		  Collection list = dao.getList(vo, request.getParameter("orgid"));
		  request.setAttribute("resultList",list);
		}catch(Exception e){
		  returnLabel = "fail";
		  e.printStackTrace();	
		}finally{
			if(dbManager!=null) dbManager.freeConnection();
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
	public ActionLocation listHistory(
			ModuleMapping mapping,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		String returnLabel = "listHistoryPage";
		IDBManager dbManager = null;
		try{
		  dbManager =  DBManagerFactory.getDBManager();
		  DelegateDAO dao = new DelegateDAO(dbManager);
		  //Collection list = dao.getList("02");
		  DelegationVO vo = new DelegationVO();
		  vo.setStatus("02");
		  if(request.getParameter("begin_date")!=null)vo.setHandledBeginDate(request.getParameter("begin_date"));
		  if(request.getParameter("end_date")!=null)vo.setHandledEndDate(request.getParameter("end_date"));
		  		  
		  Collection list = dao.getList(vo, request.getParameter("orgid"));
		  request.setAttribute("resultList",list);
		}catch(Exception e){
		  returnLabel = "fail";
		  e.printStackTrace();	
		}finally{
			if(dbManager!=null) dbManager.freeConnection();
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
	public ActionLocation revokeDelegation(
			ModuleMapping mapping,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		String returnLabel = "saveSuccess";
		String[] ids = request.getParameterValues("id");
		IDBManager dbManager = null;
		StaffVO currentStaff = (StaffVO)request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
		try{
			  dbManager =  DBManagerFactory.getDBManager();
			  DelegateDAO dao = new DelegateDAO(dbManager);
			  dbManager.startTransaction();
			  if(ids!=null){
				  DelegationVO vo = new DelegationVO();
				  for(int i=0;i<ids.length;i++){
					  vo.setId(Integer.parseInt(ids[i]));
					  vo.setStatus("02");
					  vo.setActivedBy(currentStaff.getStaffCode());
					  dao.update(vo);
				  }
			  }
			  dbManager.commit();
			}catch(Exception e){
			  dbManager.rollback();
			  returnLabel = "fail";
			  e.printStackTrace();	
			}finally{
				if(dbManager!=null) dbManager.freeConnection();
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
    public ActionLocation enternEditPage(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String returnLabel = "editPage";
        IDBManager dbManager = null;
        StaffVO currentStaff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
        request.setAttribute("companyList", currentStaff.getOwnCompanyList());
        try {
            dbManager = DBManagerFactory.getDBManager();
            TeamDAO dao = new TeamDAO(dbManager);
            StaffDAO sdao = new StaffDAO(dbManager);
            FormManageDAO fdao = new FormManageDAO(dbManager);

            Collection teamList = dao.getTeamListByCompany(currentStaff.getOrgId());
            request.setAttribute("teamList", teamList);

            Collection staffList = sdao.getStaffListByCompany(currentStaff.getOrgId());
            request.setAttribute("staffList", staffList);
            
            Collection formTypeList = FormTypeHelper.getInstance().getFormTypeList();
            request.setAttribute("formTypeList", formTypeList);
            
            Iterator it = formTypeList.iterator();
        	HashMap formMap = new HashMap();
            while(it.hasNext()){
            	String typeId = ((FormTypeVO)it.next()).getFormTypeId();
	            Collection formList = fdao.getFormList("0", typeId);
	            if(!formList.isEmpty())
	            	formMap.put(typeId, formList);
            }
            request.setAttribute("formMap", formMap);
            
        } catch (Exception e) {
            returnLabel = "fail";
            e.printStackTrace();
        } finally {
            if (dbManager != null)
                dbManager.freeConnection();
        }
        return mapping.findActionLocation(returnLabel);
    }
	
	/**
	 * save the delegation information
	 * @param mapping
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionLocation saveDelegation(
			ModuleMapping mapping,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		String[] forms = request.getParameterValues("checkbox");
		String[] formTypes = request.getParameterValues("checktype");
		String[] all = request.getParameterValues("checkall");
		if (forms == null){
			request.setAttribute("fatalerror",
				"You have not select any form!");
			return mapping.findActionLocation("fail");
		}
		String returnLabel = "saveSuccess";
		StaffVO currentStaff = (StaffVO)request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
		DelegationVO vo = new DelegationVO();
		vo.setAuthorityApprover((String)request.getParameter("authorityApprover"));
		vo.setAuthorityDeputy((String)request.getParameter("authorityDeputy"));
		vo.setApproverTeamCode(StaffTeamHelper.getInstance().getStaffByCode(vo.getAuthorityApprover()).getTeamCode());
		vo.setDeputyTeamCode(StaffTeamHelper.getInstance().getStaffByCode(vo.getAuthorityDeputy()).getTeamCode());
		vo.setDelegateFromStr((String)request.getParameter("delegateFrom"));
		vo.setDelegateToStr((String)request.getParameter("delegateTo"));
		vo.setActivedBy(currentStaff.getStaffCode());
		StringBuffer applyOrgId= new StringBuffer();
		if(request.getParameterValues("applyOrgId")!=null){
			for(String tmp:request.getParameterValues("applyOrgId")){
				applyOrgId.append(","+tmp);
			}
		}
		if(applyOrgId.length()>1)
			vo.setApplyOrgId(applyOrgId.toString().substring(1));
		else
			vo.setApplyOrgId("");
		vo.setStatus("01");
		if(request.getParameter("maxDay")!=null){//CHO only
	        SimpleDateFormat df1 = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
	        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			int maxDay = Integer.parseInt((String)request.getParameter("maxDay"));
			if(OverDueUtil.computeInvertalDays(df2.format(df1.parse(vo.getDelegateFromStr()+" 00:00:00")), 
											   df2.format(df1.parse(vo.getDelegateToStr()+" 23:30:00")))>maxDay){
				//超出当前权限
				request.setAttribute("error","若授权时间段超过"+maxDay+"个工作日，需获得授权人上级主管的批准并通知财务部管理员，由管理员协助完成代理人设定");
				return mapping.findActionLocation("fail");				
			}
		}
		IDBManager dbManager = null;
		try{
		  dbManager =  DBManagerFactory.getDBManager();
		  DelegateDAO dao = new DelegateDAO(dbManager);
		  String wholeTypeToAdd = ",";
		  String singleFormToAdd = ",";
		  //检查是否已经存在时间的重复（开始日期已经落于设置好的记录中）
		  if(all != null){
			  if(dao.checkBeginDate(vo.getDelegateFromStr(),vo.getAuthorityApprover(),"1",null,null,vo.getApplyOrgId())>0)
				  throw new OverlapException("All forms");
			  
			  vo.setDelegateLevel("1");
			  
		  }else if(formTypes != null){
			  
			  
			  for(int i=0;i<formTypes.length;i++){
				  String[] tmp1 = formTypes[i].split("_");
				  if(dao.checkBeginDate(vo.getDelegateFromStr(),vo.getAuthorityApprover(),"2",tmp1[0],null,vo.getApplyOrgId())>0)
					  throw new OverlapException(tmp1.length<2?"":tmp1[1]);
				  wholeTypeToAdd += tmp1[0]+",";
			  }
			  vo.setDelegateLevel("2");
			  vo.setFormTypeId(wholeTypeToAdd);
		  }
		  if(forms != null){
			  for(int i=0;i<forms.length;i++){
				  String[] tmp2 = forms[i].split("_");
				  //剔除已经以表单类型为范围设置的
				  if(wholeTypeToAdd.indexOf(","+tmp2[0]+",")>=0)
					  continue;
				  
				  if(dao.checkBeginDate(vo.getDelegateFromStr(),vo.getAuthorityApprover(),"3",null,tmp2[1],vo.getApplyOrgId())>0)
					  throw new OverlapException(tmp2.length<3?"":tmp2[2]);
				  
				  singleFormToAdd += tmp2[1]+",";
			  }
			  if(vo.getDelegateLevel()!=null && vo.getDelegateLevel().length()>0)
				  vo.setDelegateLevel(vo.getDelegateLevel() + ",3");
			  else
				  vo.setDelegateLevel("3");
			  vo.setFormSystemId(singleFormToAdd);
		  }
		  
		  //写入DB
		  dao.save(vo);
		  
		}catch(OverlapException e){
			//显示是哪个form/formType重复
			request.setAttribute("error","During this period, "+StaffTeamHelper.getInstance().getStaffNameByCode(vo.getAuthorityApprover())
					+" had setted his/her deputy for: "+e._subject);
			return mapping.findActionLocation("fail");
		}catch(Exception e){
			returnLabel = "fail";
			e.printStackTrace();	
		}finally{
			if(dbManager!=null) dbManager.freeConnection();
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
	public ActionLocation getStaffList(
			ModuleMapping mapping,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		String teamCode = (String)request.getParameter("teamCode");
		String staffType = (String)request.getParameter("staffType");
		if(staffType==null){
			staffType = "to";
		}
		PrintWriter out = response.getWriter();
	    IDBManager dbManager = null;
	    response.setContentType("text/xml");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");
	   try{
		  StringBuffer responseXML = new StringBuffer("<staffs>");
		  dbManager =  DBManagerFactory.getDBManager();
		  DelegateDAO dao = new DelegateDAO(dbManager);
		  Collection staffList = null;
		  if("to".equals(staffType)){
			  staffList = dao.getToStaffList(teamCode);
		  }else{
			  staffList = dao.getFromStaffList(teamCode);
		  }
		  if(staffList!=null && staffList.size()>0){
			   Iterator it = staffList.iterator();
			   while(it.hasNext()){
				   StaffVO staff = (StaffVO)it.next();
				   responseXML.append("<staff code='" + staff.getStaffCode() + "'>")
		             .append(staff.getStaffName())
		             .append("</staff>");
			   }
		  }
		  responseXML.append("</staffs>");
		  out.write(responseXML.toString());
		}catch(Exception e){
		  e.printStackTrace();	
		}finally{
			if(dbManager!=null) dbManager.freeConnection();
		}
		if(out!=null) out.close();
		return null;
	}
	
	private void sendEmail(EmailTemplateVO template, String formRequesterStaffCode, String requestNo,
            String handleStaffCode, String handleComments, String[] toEmailStaffs, String formSystemId,
            WorkFlowProcessVO processVo) throws Exception {
        String emailTo = "";
        String staffName = "";
        for (int i = 0; i < toEmailStaffs.length; i++) {
            if (toEmailStaffs[i] != null
                    && StaffTeamHelper.getInstance().getStaffByCode(toEmailStaffs[i]) != null) {
                emailTo = emailTo + StaffTeamHelper.getInstance().getStaffByCode(toEmailStaffs[i]).getEmail()
                        + ",";
            }
        }
        emailTo = emailTo.substring(0, emailTo.length() - 1);

        for (int i = 0; i < toEmailStaffs.length; i++) {
            if (toEmailStaffs[i] != null
                    && StaffTeamHelper.getInstance().getStaffByCode(toEmailStaffs[i]) != null) {
                staffName = staffName
                        + StaffTeamHelper.getInstance().getStaffByCode(toEmailStaffs[i]).getStaffName() + ",";
            }
        }
        staffName = staffName.substring(0, staffName.length() - 1);

        // if(template!=null){
        ParamConfigHelper paramHelper = ParamConfigHelper.getInstance();
        String emailHost = paramHelper.getParamValue("email_host_ip");
        String emailUserName = paramHelper.getParamValue("email_user_name");
        String emailAccount = paramHelper.getParamValue("email_from_account");
        EmailUtil sendmail = new EmailUtil();
        sendmail.setHost(emailHost);
        sendmail.setUserName(emailUserName);

        sendmail.setTo(emailTo);

        // sendmail.setCopyTo(teamLeaderEmails);
        sendmail.setFrom(emailAccount);
        sendmail.setSubject(template.getEmailSubject());
        String emailContent = template.getEmailContent();
        
        // String emailSuffix = ResourceHelper.getInstance().getEmailSuffix();
        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            EmailTemplateDAO emailTemplateDao = new EmailTemplateDAO(dbManager);
            EmailTemplateVO templateSuf = emailTemplateDao.getEmailTemplateByAction(CommonName.HANDLE_SUFFIX);
            String emailSuffix = templateSuf.getEmailContent();
            if (emailSuffix != null) {
                emailContent = emailContent + emailSuffix;
            }
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (dbManager != null) {
                dbManager.freeConnection();
            }
        }
        
        // Process for Deputy
        String deputyFor = "";
        String deputyMarkStart = " <!-- ";
        String deputyMarkEnd = " --> ";
        if (processVo.isDealByDeputy()) {
            deputyFor = StaffTeamHelper.getInstance().getStaffNameByCode(
                    processVo.getOriginProcessor()).trim();
            deputyMarkStart = " ";
            deputyMarkEnd = " ";
        }
        
        String[] paramList = { CommonName.EMAIL_TEMPLATE_PARAM_REQUEST_NO,
                CommonName.EMAIL_TEMPLATE_PARAM_HANDLE_BY,
                CommonName.EMAIL_TEMPLATE_PARAM_RECEIVE_STAFF,
                CommonName.EMAIL_TEMPLATE_PARAM_CURRENT_DATE,
                CommonName.EMAIL_TEMPLATE_PARAM_COMMENTS,
                CommonName.EMAIL_TEMPLATE_PARAM_FORM_SYSTEM_ID,
                CommonName.EMAIL_TEMPLATE_PARAM_REQUESTED_BY,
                CommonName.EMAIL_TEMPLATE_PARAM_DEPUTY_MARK_START,
                CommonName.EMAIL_TEMPLATE_PARAM_DEPUTY_FOR,
                CommonName.EMAIL_TEMPLATE_PARAM_DEPUTY_MARK_END };
        
        String[] paramValue = {
                requestNo,
                StaffTeamHelper.getInstance().getStaffNameByCode(
                        handleStaffCode),
                staffName,
                StringUtil.getCurrentDateStr("MM/dd/yyyy HH:mm:ss"),
                handleComments,
                formSystemId,
                StaffTeamHelper.getInstance().getStaffNameByCode(
                        formRequesterStaffCode), 
                deputyMarkStart, 
                deputyFor,
                deputyMarkEnd };

        for (int i = 0; i < paramList.length; i++) {
            emailContent = StringUtil.replace(emailContent, paramList[i], paramValue[i]);
        }
        
        //String[] paramList = { "@request_no", "@handle_by", "@receive_staff", "@current_date", "@comments",
        //        "@form_system_id" };
        //String[] paramValue = { requestNo, StaffTeamHelper.getInstance().getStaffNameByCode(handleStaffCode),
        //        staffName, StringUtil.getCurrentDateStr("MM/dd/yyyy HH:mm:ss"), handleComments, formSystemId };

        //for (int i = 0; i < paramList.length; i++) {
        //    if (emailContent.indexOf(paramList[i]) > -1) {
        //        emailContent = emailContent.replaceAll(paramList[i], paramValue[i]);
        //    }
        //}
        sendmail.setContent(emailContent);

        sendmail.sendMail();
        // }
    }
	
}
