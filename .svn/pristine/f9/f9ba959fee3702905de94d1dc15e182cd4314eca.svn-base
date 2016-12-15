package com.aiait.eflow.report.action;

import java.util.*;

import javax.servlet.http.*;

import com.aiait.eflow.common.CommonName;
import com.aiait.eflow.common.helper.MedicalEntitleHelper;
import com.aiait.eflow.common.helper.ParamConfigHelper;
import com.aiait.eflow.common.helper.StaffTeamHelper;
import com.aiait.eflow.housekeeping.dao.TeamDAO;
import com.aiait.eflow.report.dao.MedicalReportDAO;
import com.aiait.framework.db.*;
import com.aiait.framework.mvc.action.*;

public class MedicalReportAction extends DispatchAction {

    /**
     * medical summary report page
     * 
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionLocation medicalSummaryReport(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
    	
        String returnLabel = "medicalSummaryReport";
        String yearMonth = (String) request.getParameter("yearMonth");
		String teamCode = (String) request.getParameter("teamCode");
		String formSystemId = ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_MEDICAL_FORM_SYSTEM_ID);
		String yearlyJobDay = ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_MEDICAL_YEARLY_JOB_DAY);
		String isExport = request.getParameter("isExport");
        String comeFrom = (String) request.getParameter("comefrom");
        
        Collection teamList	= StaffTeamHelper.getInstance().getTeamList();
        request.setAttribute("teamList", teamList);
        
        IDBManager dbManager = null;
        
        try {
            dbManager = DBManagerFactory.getDBManager();
            
            if (comeFrom == null || (!"left".equals(comeFrom))) {
            	
                String teamListSQL ="";
                TeamDAO teamDao = new TeamDAO(dbManager);  
                if (teamCode != null && !("").equals(teamCode)) {
 					teamListSQL = teamDao.groupSubTeam(teamCode);
 					if (teamListSQL == null || "".equals(teamListSQL)) {
 						teamListSQL = teamCode;
 					} else {
 						teamListSQL = teamListSQL.substring(0, teamListSQL.lastIndexOf(","));
 						teamListSQL = teamCode + "," + teamListSQL;
 					}
 				} 
                MedicalReportDAO medicalReportDAO = new MedicalReportDAO(dbManager);
                Collection medicalSummaryList = null;
                int year = Integer.parseInt(yearMonth.substring(0,4));
                String monthStr = yearMonth.substring(5,7);
                
                //String finYeaStr = "12".equals(monthStr) ? Integer.toString(year+1) : Integer.toString(year);
                //String monthBeginStr = monthStr + "/" + "01/" + Integer.toString(year);          
                //String dateEndStr = MedicalEntitleHelper.monthEndDate(monthBeginStr);           
                //String dateBeginStr = "12".equals(monthStr) ? monthBeginStr : "12/01/" + Integer.toString(year-1);
                
                //month period between last month 5th to this month 4th
                String finYeaStr = Integer.toString(year);
                String lastYearMonth = MedicalEntitleHelper.getPreviousYearMonth(yearMonth);
                String monthBeginStr =  lastYearMonth.substring(5,7)+ "/"+yearlyJobDay.substring(3,5) +"/"+lastYearMonth.substring(0,4) + " 00:00:00";                
                String dateEndStr = MedicalEntitleHelper.getPreviousDay(monthStr + "/"+ yearlyJobDay.substring(3,5) + "/" +finYeaStr) + " 23:59:59";       
                String dateBeginStr = yearlyJobDay + "/" + Integer.toString(year-1) + " 00:00:00";   
                
                medicalSummaryList = medicalReportDAO.getMedicalSummaryList(formSystemId, dateBeginStr, dateEndStr, monthBeginStr, finYeaStr, teamListSQL);
				request.setAttribute("medicalSummaryList", medicalSummaryList);
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
			return mapping.findActionLocation("xlsMedicalSummaryReport");
		}else{
		    return mapping.findActionLocation(returnLabel);
		}
        
    }   
}
