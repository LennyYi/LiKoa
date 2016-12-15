package com.aiait.eflow.report.action;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.*;

import com.aiait.eflow.common.CommonName;
import com.aiait.eflow.common.ModuleOperateName;
import com.aiait.eflow.common.helper.AuthorityHelper;
import com.aiait.eflow.common.helper.BaseDataHelper;
import com.aiait.eflow.common.helper.LeaveEntitleHelper;
import com.aiait.eflow.common.helper.ParamConfigHelper;
import com.aiait.eflow.common.helper.StaffTeamHelper;
import com.aiait.eflow.formmanage.dao.FormManageDAO;
import com.aiait.eflow.formmanage.vo.FormManageVO;
import com.aiait.eflow.housekeeping.dao.StaffDAO;
import com.aiait.eflow.housekeeping.dao.TeamDAO;
import com.aiait.eflow.housekeeping.vo.StaffVO;
import com.aiait.eflow.housekeeping.vo.TeamVO;
import com.aiait.eflow.report.dao.LeaveReportDAO;
import com.aiait.eflow.report.dao.ReportDAO;
import com.aiait.eflow.report.vo.*;
import com.aiait.eflow.util.StringUtil;
import com.aiait.eflow.wkf.dao.WorkFlowDefineDAO;
import com.aiait.eflow.wkf.vo.WorkFlowItemVO;
import com.aiait.framework.common.GlobalCommonName;
import com.aiait.framework.db.*;
import com.aiait.framework.exception.DAOException;
import com.aiait.framework.mvc.action.*;
import com.aiait.framework.page.*;

public class LeaveReportAction extends DispatchAction {

    /**
     * enter personal deal summary report page
     * 
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionLocation leaveDetailReport(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String returnLabel = "leaveDetailReport";
        String requestStaffCode = (String) request.getParameter("requestStaffCode");
        StaffVO staff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
        String leaveDateBegin = (String) request.getParameter("leaveDateBegin");;
		String leaveDateEnd = (String) request.getParameter("leaveDateEnd");
		String teamCode = (String) request.getParameter("teamCode");
		String formSystemId = ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_LEAVE_FORM_SYSTEM_ID);
		String currentStaffCode = staff.getStaffCode();
		String orgId = staff.getOrgId();
		String isExport = request.getParameter("isExport");
        String comeFrom = (String) request.getParameter("comefrom");
        		
		String selectType = request.getParameter("selectType");
		request.setAttribute("formSystemId", formSystemId);
		if (comeFrom != null && "left".equals(comeFrom)) {
			ParamConfigHelper paramHelper = ParamConfigHelper.getInstance();
			String defaultDays = paramHelper.getParamValue("list_data_days");
			int days = 0;
			if (defaultDays == null || "".equals(defaultDays)) {
				days = -7;
			} else {
				days = -Integer.parseInt(defaultDays);
			}
			
			SimpleDateFormat bartDateFormat = new SimpleDateFormat("MM/dd/yyyy");
			Date now = new Date();
			leaveDateEnd = bartDateFormat.format(now);
			leaveDateBegin = StringUtil.afterNDay(days, "MM/dd/yyyy");
			request.setAttribute("leaveDateBegin", leaveDateBegin);
			request.setAttribute("leaveDateEnd", leaveDateEnd);
		}
		
        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            TeamDAO teamDao = new TeamDAO(dbManager);          
            Collection teamList = null;
            String managedTeams = "";
            
    		String currentRole = staff.getCurrentRoleId();
            AuthorityHelper authority = AuthorityHelper.getInstance();
            
            //Staff who can see all teams
            if(authority.checkAuthority(currentRole,ModuleOperateName.MODULE_LEAVE_DETAIL_REPORT,ModuleOperateName.OPER_LEAVE_DETAIL_ALL)){
            	teamList = teamDao.getTeamListByCompany(orgId);
            	currentStaffCode ="";
            }
            //Staff who can see managed team(s) only
            else if(authority.checkAuthority(currentRole,ModuleOperateName.MODULE_LEAVE_DETAIL_REPORT,ModuleOperateName.OPER_LEAVE_DETAIL_TL)){
                managedTeams = teamDao.getManagedTeamCodesByTL(currentStaffCode);
                
                if(!("").equals(managedTeams)){
                	teamList = new Vector();
                	String managedTeamsList[]=managedTeams.split(",");
                	for(int i=0; i< managedTeamsList.length; i++){
                		TeamVO team = new TeamVO();
                		team.setTeamCode(managedTeamsList[i]);
                		teamList.add(team);
                	}
                }
            }
            
/*            //HR can see all teams
            if(("65").equals(staff.getTeamCode())){
            	teamList = teamDao.getTeamListByCompany(orgId);
            	currentStaffCode ="";
            }
            //Other TL can see his/her own team
            else{
                managedTeams = teamDao.getManagedTeamCodesByTL(currentStaffCode);
             
                if(!("").equals(managedTeams)){
                	teamList = new Vector();
                	String managedTeamsList[]=managedTeams.split(",");
                	for(int i=0; i< managedTeamsList.length; i++){
                		TeamVO team = new TeamVO();
                		team.setTeamCode(managedTeamsList[i]);
                		teamList.add(team);
                	}
                }
            } */           
            request.setAttribute("teamList", teamList);
                                    
            if (comeFrom == null || (!"left".equals(comeFrom))) {
               String teamListSQL ="";
               
               if (teamCode != null && !("").equals(teamCode)) {
					teamListSQL = teamDao.groupSubTeam(teamCode);
					if (teamListSQL == null || "".equals(teamListSQL)) {
						teamListSQL = teamCode;
					} else {
						teamListSQL = teamListSQL.substring(0, teamListSQL.lastIndexOf(","));
						teamListSQL = teamCode + "," + teamListSQL;
					}
				} else {
					teamListSQL = managedTeams;
				}
            	           	
               LeaveReportDAO leaveReportDAO = new LeaveReportDAO(dbManager);  
               Collection leaveDetailList = null;
               leaveDetailList = leaveReportDAO.getLeaveDetailList(formSystemId, leaveDateBegin, leaveDateEnd, teamListSQL, currentStaffCode,selectType);
               request.setAttribute("leaveDetailList", leaveDetailList);  
            }
            
        } catch (Exception ex) {
        	request.setAttribute(CommonName.COMMON_ERROR_INFOR, ex.getMessage());
        	ex.printStackTrace();
        } finally {
        	if (dbManager != null) {
                dbManager.freeConnection();
            }
        }
        
		if ("1".equals(isExport)) {
			return mapping.findActionLocation("xlsLeaveDetailReport");
		}else{
		    return mapping.findActionLocation(returnLabel);
		}
        
    }
    
    /**
     * leave summary report page
     * 
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionLocation leaveSummaryReport(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String returnLabel = "leaveSummaryReport";
        String requestStaffCode = (String) request.getParameter("requestStaffCode");
        StaffVO staff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
        String yearMonth = (String) request.getParameter("yearMonth");
		String teamCode = (String) request.getParameter("teamCode");
		String formSystemId = ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_LEAVE_FORM_SYSTEM_ID);
		String currentStaffCode = staff.getStaffCode();
		String orgId = staff.getOrgId();
		String isExport = request.getParameter("isExport");
		String type = request.getParameter("type");	
        String comeFrom = (String) request.getParameter("comefrom");
        
        String selectType = request.getParameter("selectType");
		request.setAttribute("formSystemId", formSystemId);
		if (comeFrom != null && "left".equals(comeFrom)) {
			
			//Calendar calendar = Calendar.getInstance();			
			//year = Integer.toString(calendar.get(Calendar.YEAR));
			//month = Integer.toString(calendar.get(Calendar.MONTH)+1);
			//request.setAttribute("year", year);
			//request.setAttribute("month", month);
		}
		
		
        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            TeamDAO teamDao = new TeamDAO(dbManager);          
            Collection teamList = null;
            String managedTeams = "";
            
    		String currentRole = staff.getCurrentRoleId();
            AuthorityHelper authority = AuthorityHelper.getInstance();
            
            //Staff who can see all teams
            if(authority.checkAuthority(currentRole,ModuleOperateName.MODULE_LEAVE_SUMMARY_REPORT,ModuleOperateName.OPER_LEAVE_SUMMARY_ALL)){
            	teamList = teamDao.getTeamListByCompany(orgId);
            	currentStaffCode ="";
            }
            //Staff who can see managed team(s) only
            else if(authority.checkAuthority(currentRole,ModuleOperateName.MODULE_LEAVE_SUMMARY_REPORT,ModuleOperateName.OPER_LEAVE_SUMMARY_TL)){
                managedTeams = teamDao.getManagedTeamCodesByTL(currentStaffCode);
                
                if(!("").equals(managedTeams)){
                	teamList = new Vector();
                	String managedTeamsList[]=managedTeams.split(",");
                	for(int i=0; i< managedTeamsList.length; i++){
                		TeamVO team = new TeamVO();
                		team.setTeamCode(managedTeamsList[i]);
                		teamList.add(team);
                	}
                }
            }
            
/*            //HR can see all teams
            if(("65").equals(staff.getTeamCode())){
            	teamList = teamDao.getTeamListByCompany(orgId);
            	currentStaffCode ="";
            }
            //Other TL can see his/her own team
            else{       	 
                managedTeams = teamDao.getManagedTeamCodesByTL(currentStaffCode);
                
                if(!("").equals(managedTeams)){
                	teamList = new Vector();
                	String managedTeamsList[]=managedTeams.split(",");
                	for(int i=0; i< managedTeamsList.length; i++){
                		TeamVO team = new TeamVO();
                		team.setTeamCode(managedTeamsList[i]);
                		teamList.add(team);
                	}
                }
            } */           
            request.setAttribute("teamList", teamList);
            
            if (comeFrom == null || (!"left".equals(comeFrom))) {
            	
                String teamListSQL ="";
                
                if (teamCode != null && !("").equals(teamCode)) {
 					teamListSQL = teamDao.groupSubTeam(teamCode);
 					if (teamListSQL == null || "".equals(teamListSQL)) {
 						teamListSQL = teamCode;
 					} else {
 						teamListSQL = teamListSQL.substring(0, teamListSQL.lastIndexOf(","));
 						teamListSQL = teamCode + "," + teamListSQL;
 					}
 				} else {
 					teamListSQL = managedTeams;
 				}

				LeaveReportDAO leaveReportDAO = new LeaveReportDAO(dbManager);
				HashMap leaveSummaryMap = null;
				ArrayList leaveSummaryList =null;
				leaveSummaryMap = new HashMap();
				leaveSummaryList = leaveReportDAO.getLeaveSummaryList(Integer.parseInt(yearMonth.substring(0,4)), teamListSQL, currentStaffCode, leaveSummaryMap);
				
				//leaveSummaryMap = leaveReportDAO.getLeaveSummaryList(Integer.parseInt(yearMonth.substring(0,4)), teamCode, teamListSQL.toString(), staffCodeList,currentStaffCode);
				String yearBegin = "01/01/"+yearMonth.substring(0,4);
				String monthBegin = yearMonth.substring(5,7) + "/" + "01/" + yearMonth.substring(0,4);
				String monthEnd = LeaveEntitleHelper.monthEndDate(monthBegin);

				leaveReportDAO.CalYearMonthTaken(leaveSummaryList,leaveSummaryMap, formSystemId, yearBegin, monthBegin, monthEnd, teamListSQL,currentStaffCode,selectType);


				request.setAttribute("leaveSummaryList", leaveSummaryList);
			}
            
        } catch (Exception ex) {
        	request.setAttribute(CommonName.COMMON_ERROR_INFOR, ex.getMessage());
        	ex.printStackTrace();
        } finally {
        	if (dbManager != null) {
                dbManager.freeConnection();
            }
        }
        
		if ("1".equals(isExport)) {
			return mapping.findActionLocation("xlsLeaveSummaryReport");
		}else{
		    return mapping.findActionLocation(returnLabel);
		}
        
    }   
}
