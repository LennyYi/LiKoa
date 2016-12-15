package com.aiait.eflow.housekeeping.action;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.aiait.eflow.common.CommonName;
import com.aiait.eflow.common.helper.BaseDataHelper;
import com.aiait.eflow.common.helper.FormTypeHelper;
import com.aiait.eflow.common.helper.OptionHelper;
import com.aiait.eflow.common.helper.ParamConfigHelper;
import com.aiait.eflow.common.helper.StaffTeamHelper;
import com.aiait.eflow.common.helper.SystemFieldHelper;
import com.aiait.eflow.housekeeping.dao.CompanyDAO;
import com.aiait.eflow.housekeeping.dao.StaffDAO;
import com.aiait.eflow.housekeeping.vo.StaffVO;
import com.aiait.eflow.util.BaseDataUtil;
import com.aiait.framework.db.DBManagerFactory;
import com.aiait.framework.db.IDBManager;
import com.aiait.framework.exception.DAOException;
import com.aiait.framework.mvc.action.ActionLocation;
import com.aiait.framework.mvc.action.DispatchAction;
import com.aiait.framework.mvc.action.ModuleMapping;

public class DataSynchAction extends DispatchAction {
    
	public ActionLocation beginDataSynch(
			ModuleMapping mapping,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		response.setHeader("Cache-Control", "no-cache");
		//String paramName = (String)request.getParameter("paramName");
		String result = "It is successful to refresh data";
		StaffVO currentStaff = (StaffVO)request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
		PrintWriter out = response.getWriter();
		BaseDataUtil.refreshData(currentStaff.getCurrentRoleId());
		IDBManager dbManager = null;
		try{
		  dbManager =  DBManagerFactory.getDBManager();
		  CompanyDAO companyDao = new CompanyDAO(dbManager);
		  //获取当前登录者的公司及其以下的所有公司的集合
		  currentStaff.setOwnCompanyList(companyDao.getCompanyList(currentStaff.getOrgId()));
		  //获取该公司的所有上级公司(包括自己）
		  currentStaff.setUpperCompanys( companyDao.getSuperCompanys(currentStaff.getOrgId()));
		  //获取该公司的所有下级公司（包括自己）
		  String lowerCompanyIds = "'"+currentStaff.getOrgId()+"'";
		  String temp = companyDao.getSubCompanys(currentStaff.getOrgId());
		  if(temp!=null && !"".equals(temp)){
			  lowerCompanyIds = lowerCompanyIds + ","+temp;
		  }
		  currentStaff.setLowerCompanys(lowerCompanyIds);
		  
		  request.getSession().setAttribute(CommonName.CURRENT_STAFF_INFOR,currentStaff);
		  
		}catch(DAOException e){
			e.printStackTrace();
		}finally{
			if(dbManager!=null) dbManager.freeConnection();
		}
		out.print(result);
		if(out!=null) out.close();
		return null;
	}
	
}
