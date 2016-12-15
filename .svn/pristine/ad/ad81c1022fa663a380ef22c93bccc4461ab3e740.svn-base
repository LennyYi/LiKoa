package com.aiait.eflow.housekeeping.action;

import java.io.PrintWriter;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.aiait.eflow.common.CommonName;
import com.aiait.eflow.common.helper.LeaveEntitleHelper;
import com.aiait.eflow.common.helper.MedicalEntitleHelper;
import com.aiait.eflow.common.helper.ParamConfigHelper;
import com.aiait.eflow.common.helper.StaffTeamHelper;
import com.aiait.eflow.housekeeping.dao.LeaveBalanceCommonInforDAO;
import com.aiait.eflow.housekeeping.dao.LeaveBalanceDAO;
import com.aiait.eflow.housekeeping.dao.MedicalBalanceDAO;
import com.aiait.eflow.housekeeping.vo.LeaveBalanceCommonInforVO;
import com.aiait.eflow.housekeeping.vo.LeaveBalanceVO;
import com.aiait.eflow.housekeeping.vo.MedicalBalanceVO;
import com.aiait.eflow.util.EFlowEmailUtil;
import com.aiait.eflow.util.StringUtil;
import com.aiait.framework.db.*;
import com.aiait.framework.mvc.action.*;

public class MedicalBalanceAction extends DispatchAction {
	
	public ActionLocation loadMedicalBalance(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception{
		
		String returnLabel = "loadMedicalBalance";
        String staffCode = request.getParameter("staffCode");
        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager(); 
            MedicalBalanceDAO medicalBalanceDAO = new MedicalBalanceDAO(dbManager);            
            Collection medicalBalanceList = medicalBalanceDAO.getBalanceList(staffCode);                        
            request.setAttribute("medicalBalanceList", medicalBalanceList);
            
        } catch (Exception ex) {
            request.setAttribute(CommonName.COMMON_ERROR_INFOR, ex.getMessage());
            ex.printStackTrace();
        } finally {
            if (dbManager != null) {
                dbManager.freeConnection();
            }
        }
        return mapping.findActionLocation(returnLabel);
	}
	
    public ActionLocation saveMedicalBalance(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
    	
    	String returnLabel = "leaveBalanceSetting";
        String staffCode = request.getParameter("staffCode").trim();
        String year = request.getParameter("yearMed").trim();
        
        String staffCEntitlement = request.getParameter("staffCEntitlement").trim();
        String staffCApplied = request.getParameter("staffCApplied").trim();
        String staffCBalance = request.getParameter("staffCBalance").trim();
        String staffHEntitlement = request.getParameter("staffHEntitlement").trim();
        String staffHApplied = request.getParameter("staffHApplied").trim();
        String staffHBalance = request.getParameter("staffHBalance").trim();

        String connubialName = request.getParameter("connubialName").trim();
        String connubialCEntitlement = request.getParameter("connubialCEntitlement").trim();
        String connubialCApplied = request.getParameter("connubialCApplied").trim();
        String connubialCBalance = request.getParameter("connubialCBalance").trim();
        String connubialHEntitlement = request.getParameter("connubialHEntitlement").trim();
        String connubialHApplied = request.getParameter("connubialHApplied").trim();
        String connubialHBalance = request.getParameter("connubialHBalance").trim();

        String childName = request.getParameter("childName").trim();
        String childCEntitlement = request.getParameter("childCEntitlement").trim();
        String childCApplied = request.getParameter("childCApplied").trim(); 
        String childCBalance = request.getParameter("childCBalance").trim();
        String childHEntitlement = request.getParameter("childHEntitlement").trim();
        String childHApplied = request.getParameter("childHApplied").trim();
        String childHBalance = request.getParameter("childHBalance").trim();
        
        MedicalBalanceVO medicalBalance = new MedicalBalanceVO();
        medicalBalance.setStaffCode(staffCode);
        medicalBalance.setYear(Integer.parseInt(year));
        
        medicalBalance.setStaffCEntitlement(staffCEntitlement.equals("") ? 0.0 : Double.parseDouble(staffCEntitlement));
        medicalBalance.setStaffCApplied(staffCApplied.equals("") ? 0.0 : Double.parseDouble(staffCApplied));
        medicalBalance.setStaffCBalance(staffCBalance.equals("") ? 0.0 : Double.parseDouble(staffCBalance));
        medicalBalance.setStaffHEntitlement(staffHEntitlement.equals("") ? 0.0 : Double.parseDouble(staffHEntitlement));
        medicalBalance.setStaffHApplied(staffHApplied.equals("") ? 0.0 : Double.parseDouble(staffHApplied));             
        medicalBalance.setStaffHBalance(staffHBalance.equals("") ? 0.0 : Double.parseDouble(staffHBalance));
        
        medicalBalance.setConnubialName(connubialName);
        medicalBalance.setConnubialCEntitlement(connubialCEntitlement.equals("") ? 0.0 : Double.parseDouble(connubialCEntitlement));
        medicalBalance.setConnubialCApplied(connubialCApplied.equals("") ? 0.0 : Double.parseDouble(connubialCApplied));
        medicalBalance.setConnubialCBalance(connubialCBalance.equals("") ? 0.0 : Double.parseDouble(connubialCBalance));                
        medicalBalance.setConnubialHEntitlement(connubialHEntitlement.equals("") ? 0.0 : Double.parseDouble(connubialHEntitlement));
        medicalBalance.setConnubialHApplied(connubialHApplied.equals("") ? 0.0 : Double.parseDouble(connubialHApplied));
        medicalBalance.setConnubialHBalance(connubialHBalance.equals("") ? 0.0 : Double.parseDouble(connubialHBalance));
        
        medicalBalance.setChildName(childName);
        medicalBalance.setChildCEntitlement(childCEntitlement.equals("") ? 0.0 : Double.parseDouble(childCEntitlement));
        medicalBalance.setChildCApplied(childCApplied.equals("") ? 0.0 : Double.parseDouble(childCApplied));
        medicalBalance.setChildCBalance(childCBalance.equals("") ? 0.0 : Double.parseDouble(childCBalance));
        medicalBalance.setChildHEntitlement(childHEntitlement.equals("") ? 0.0 : Double.parseDouble(childHEntitlement));
        medicalBalance.setChildHApplied(childHApplied.equals("") ? 0.0 : Double.parseDouble(childHApplied));
        medicalBalance.setChildHBalance(childHBalance.equals("") ? 0.0 : Double.parseDouble(childHBalance));
        
        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            dbManager.startTransaction();
            MedicalBalanceDAO medicalBalanceDAO = new MedicalBalanceDAO(dbManager);
            medicalBalanceDAO.saveBalance(medicalBalance);
            dbManager.commit();
        } catch (Exception ex) {
            dbManager.rollback();
            request.setAttribute(CommonName.COMMON_ERROR_INFOR, ex.getMessage());
            ex.printStackTrace();
        } finally {
            if (dbManager != null) {
                dbManager.freeConnection();
            }
        }
        return mapping.findActionLocation(returnLabel);
    }
    
    public ActionLocation deleteMedicalBalance(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
    	
    	String returnLabel = "leaveBalanceSetting";
        String staffCode = request.getParameter("staffCode").trim();
        String[] years = request.getParameterValues("medItemId");

        MedicalBalanceVO medicalBalance = new MedicalBalanceVO();
        medicalBalance.setStaffCode(staffCode);

        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            dbManager.startTransaction();
            MedicalBalanceDAO medicalBalanceDAO = new MedicalBalanceDAO(dbManager);
            for (int i = 0; i < years.length; i++) {
                String year = years[i];
                medicalBalance.setYear(Integer.parseInt(year));
                medicalBalanceDAO.deleteBalance(medicalBalance);
            }
            dbManager.commit();
        } catch (Exception ex) {
            dbManager.rollback();
            request.setAttribute(CommonName.COMMON_ERROR_INFOR, ex.getMessage());
            ex.printStackTrace();
        } finally {
            if (dbManager != null) {
                dbManager.freeConnection();
            }
        }
        return mapping.findActionLocation(returnLabel);
    } 
    
	public static Collection updateStaffAnnualEntitleForGradeChange(Collection staffCodeList, boolean manual) throws Exception {

		IDBManager dbManager = null;

		try {
			dbManager = DBManagerFactory.getDBManager();
			LeaveBalanceCommonInforDAO leaveBalanceCommonInforDAO = new LeaveBalanceCommonInforDAO(dbManager);
            Collection emailList = new Vector();
            Collection updateList = new Vector();
			Iterator it = staffCodeList.iterator();
			
			String dateFormat = "MM/dd/yyyy";
			Date today = new Date();
			String todayStr = StringUtil.getDateStr(today, dateFormat);
			int currentMonth = Integer.parseInt(todayStr.substring(0,2));
			int currentYear = Integer.parseInt(todayStr.substring(6, 10));
			
			while (it.hasNext()) {
	
				LeaveBalanceCommonInforVO leaveBalanceCommonInforVO = (LeaveBalanceCommonInforVO) it.next();
				currentYear = currentMonth == 12 ? currentYear + 1  : currentYear;
				
				// --Get the medical information of the staff of current year
				MedicalBalanceDAO medicalBalanceDAO = new MedicalBalanceDAO(dbManager);
				MedicalBalanceVO vo = new MedicalBalanceVO();
				vo.setStaffCode(leaveBalanceCommonInforVO.getStaffCode());
				vo.setYear(currentYear);
				MedicalBalanceVO medicalBalanceCurrentYear = medicalBalanceDAO.getBalance(vo);
				
				//--no record of current year, then generate a new one.
				if(medicalBalanceCurrentYear == null){
					medicalBalanceCurrentYear = new MedicalBalanceVO();
					medicalBalanceCurrentYear.setStaffCode(vo.getStaffCode());
					medicalBalanceCurrentYear.setYear(vo.getYear());
				}
				
				LeaveBalanceCommonInforVO result = new LeaveBalanceCommonInforVO();
				result.clone(leaveBalanceCommonInforVO);
				
				medicalBalanceCurrentYear = MedicalEntitleHelper.calNewGradeEntitlement(leaveBalanceCommonInforVO, medicalBalanceCurrentYear);
					
				result.setMedicalBalanceData(medicalBalanceCurrentYear);
				updateList.add(result);
				emailList.add(result);
			}
			// save all new records
			if(updateList.size()>0 ){
				saveBalanceRecord(updateList);
				
				if(!manual){
					saveGradeRecord(updateList);
				}
			}
			
			return emailList;
			
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (dbManager != null) {
				dbManager.freeConnection();
			}
		}
	}
	
   public static int updateStaffMedicalEntitlementDaily(String todayStr) {
    	
    	IDBManager dbManager = null;	
    	Collection gradeList = null;
    	Collection gradeEmailList = null; 	
		int recordCount =0;
		
		try {
			// Processing ...			
			dbManager = DBManagerFactory.getDBManager();
			LeaveBalanceCommonInforDAO leaveBalanceCommonInforDAO = new LeaveBalanceCommonInforDAO(dbManager);
			gradeList = leaveBalanceCommonInforDAO.getListByGradeEffDate(todayStr, "medical");
		
			if (gradeList.size() > 0) {		
				gradeEmailList = MedicalBalanceAction.updateStaffAnnualEntitleForGradeChange(gradeList, false);
				try {
					MedicalBalanceAction.emailSuccessUpdate(gradeEmailList);
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		
			recordCount = gradeList.size();
			
		} catch (Exception ex) {
			ex.printStackTrace();
			
			//Send email to notify the error
			try {
				MedicalBalanceAction.emailFaildUpdate(gradeList, "1", ex);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		} finally {
			if (dbManager != null) {
				dbManager.freeConnection();
			}
		}
		return recordCount;
	}
   
	public static void generateNewYearBalance(Collection staffCodeList, String currentYear) throws Exception {

		IDBManager dbManager = null;

		try {
            Collection updateList = new Vector();
			Iterator it = staffCodeList.iterator();	
			while (it.hasNext()) {
				
				LeaveBalanceCommonInforVO leaveBalanceCommonInforVO = (LeaveBalanceCommonInforVO) it.next();	
				// --Get the leave information of the staff of current year
				MedicalBalanceVO medicalBalanceCurrentYear =null;
                try {
                    dbManager = DBManagerFactory.getDBManager();
                    MedicalBalanceDAO medicalBalanceDAO = new MedicalBalanceDAO(dbManager);
                    MedicalBalanceVO vo = new MedicalBalanceVO();
    				vo.setStaffCode(leaveBalanceCommonInforVO.getStaffCode());
    				vo.setYear(Integer.parseInt(currentYear));
    				medicalBalanceCurrentYear = medicalBalanceDAO.getBalance(vo);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    throw ex;
                } finally {
                    if (dbManager != null) {
                        dbManager.freeConnection();
                    }
                }
				
				//--no record of current year, then generate a new one.
				if(medicalBalanceCurrentYear == null){
					medicalBalanceCurrentYear = new MedicalBalanceVO();
					medicalBalanceCurrentYear.setStaffCode(leaveBalanceCommonInforVO.getStaffCode());
					medicalBalanceCurrentYear.setYear(Integer.parseInt(currentYear));
					
					// --Get the Connubial / Child Name for the staff of last year
					int lastYear = Integer.parseInt(currentYear) - 1;
					MedicalBalanceVO medicalBalanceLastYear = null;
	                try {
	                    dbManager = DBManagerFactory.getDBManager();
	                    MedicalBalanceDAO medicalBalanceDAO = new MedicalBalanceDAO(dbManager);
	                    MedicalBalanceVO vo = new MedicalBalanceVO();
	    				vo.setStaffCode(leaveBalanceCommonInforVO.getStaffCode());
	    				vo.setYear(lastYear);
	    				medicalBalanceLastYear = medicalBalanceDAO.getBalance(vo);
	                } catch (Exception ex) {
	                    ex.printStackTrace();
	                    throw ex;
	                } finally {
	                    if (dbManager != null) {
	                        dbManager.freeConnection();
	                    }
	                }
	                
					if(medicalBalanceLastYear != null){
						medicalBalanceCurrentYear.setConnubialName(medicalBalanceLastYear.getConnubialName());
						medicalBalanceCurrentYear.setChildName(medicalBalanceLastYear.getChildName());
					}
					
					double CEntitlement = MedicalEntitleHelper.getBasicEntitlement(leaveBalanceCommonInforVO.getNewGrade(), "C");
					double HEntitlement = MedicalEntitleHelper.getBasicEntitlement(leaveBalanceCommonInforVO.getNewGrade(), "H");
					medicalBalanceCurrentYear.setAllEntitlement(CEntitlement, HEntitlement, CEntitlement, HEntitlement, CEntitlement, HEntitlement);																														
										
					LeaveBalanceCommonInforVO result = new LeaveBalanceCommonInforVO();
					result.clone(leaveBalanceCommonInforVO);
					result.setMedicalBalanceData(medicalBalanceCurrentYear);
					updateList.add(result);
				}
				
				//已经有更新过的记录
				else{
					continue;
				}
			}
			// save all new records
			if(updateList.size()>0 ){
				saveBalanceRecord(updateList);
			}				
			
		} catch (Exception ex) {
			throw ex;
		} finally {
			//
		}
	}
	
   public static int generateComingYearBalance(String todayStr) {
   	
   	IDBManager dbManager = null;	
   	Collection staffList = null;
   	int recordCount =0;
		try {
			// Processing ...		
			String currentYear = todayStr.substring(6, 10);
			String yearBeginStr = "12/01/" + currentYear;

			dbManager = DBManagerFactory.getDBManager();
			LeaveBalanceCommonInforDAO leaveBalanceCommonInforDAO = new LeaveBalanceCommonInforDAO(dbManager);
			staffList = leaveBalanceCommonInforDAO.getAllStaffBalanceCommonInfor(yearBeginStr);				
			
			currentYear = Integer.toString(Integer.parseInt(currentYear) + 1);
			if (staffList.size() > 0) {				
				MedicalBalanceAction.generateNewYearBalance(staffList,currentYear);
			}
			recordCount = staffList.size();
		} catch (Exception ex) {
			
			ex.printStackTrace();
			//Send email to notify the error
			try {
				MedicalBalanceAction.emailFaildUpdate(staffList, "0", ex);
			} catch (Exception e) {
				e.printStackTrace();
			}		
			
		} finally {
			if (dbManager != null) {
				dbManager.freeConnection();
			}
		}
		return recordCount;
	}
	
	/**
	 * Save all updated balance records into DB 
	 * 
	 * <li> leaveBalanceMap:  new balance record of staffList
	 * 
	 */	
	protected static void saveBalanceRecord(Collection staffCodeList) throws Exception{

		IDBManager dbManager = null;
		try {
			dbManager = DBManagerFactory.getDBManager();
			dbManager.startTransaction();
			// For medical records
			MedicalBalanceDAO medicalBalanceDAO = new MedicalBalanceDAO(dbManager);
			Iterator it = staffCodeList.iterator();
			while (it.hasNext()) {
				LeaveBalanceCommonInforVO leaveBalanceCommonInforVO = (LeaveBalanceCommonInforVO) it.next();
				medicalBalanceDAO.saveBalance(leaveBalanceCommonInforVO.getMedicalBalanceData());
			}
			dbManager.commit();

		} catch (Exception ex) {
			try {
				dbManager.rollback();
			} catch (Exception e) {
				e.printStackTrace();
			}		
			ex.printStackTrace();
			throw ex;
		} finally {
			if (dbManager != null) {
				dbManager.freeConnection();
			}
		}
	}
	
	/**
	 * Save all updated grade records as Updated_medical into DB 
	 * 
	 * <li> staffCodeList:  new balance info record of staffList
	 * 
	 */	
	protected static void saveGradeRecord(Collection staffCodeList) throws Exception{

		IDBManager dbManager = null;
		try {
			dbManager = DBManagerFactory.getDBManager();
			dbManager.startTransaction();
			// For leave records
			LeaveBalanceCommonInforDAO leaveBalanceCommonInforDAO = new LeaveBalanceCommonInforDAO(dbManager);
			Iterator it = staffCodeList.iterator();
			while (it.hasNext()) {
				LeaveBalanceCommonInforVO leaveBalanceCommonInforVO = (LeaveBalanceCommonInforVO) it.next();
				leaveBalanceCommonInforDAO.updateGradeIndicator(leaveBalanceCommonInforVO, "medical");
			}
			dbManager.commit();

		} catch (Exception ex) {
			try {
				dbManager.rollback();
			} catch (Exception e) {
				e.printStackTrace();
			}		
			ex.printStackTrace();
			throw ex;
		} finally {
			if (dbManager != null) {
				dbManager.freeConnection();
			}
		}
	}
	
	/**
	 * Send email notifying medical balance change to staff and HR if update is successful
	 * 
	 * <li> staffList:  list of staff common information
	 * 
	 */	
	
	public static void emailSuccessUpdate(Collection staffList) throws Exception {

		try {
			String emailSubject = "Medical entitlement renew";
			String currentDate = StringUtil.getCurrentDateStr("MM/dd/yyyy");
			String emailContent = "";	
			StringBuffer emailContentToHR = new StringBuffer("");
			String emailAccountHRStr = ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_LEAVE_HR_EMAIL);
			String emailAccountHR[] ={""};
			if(emailAccountHRStr!=null && !"".equals(emailAccountHRStr)){
		       emailAccountHR=emailAccountHRStr.split(";");
			}
			
			emailContentToHR.append("<br>The <font size=+1><strong>annual medical</strong></font > entitlement of below staffs were updated on "+currentDate+" due to their grades change: <br>");
			emailContentToHR.append("<TABLE BORDER=1>");
			emailContentToHR.append("<tr><TH>Staff code</TH><TH>Staff name</TH><TH>Employee C-Entitlement</TH><TH>Employee H-Entitlement</TH><TH>Connubial C-Entitlement</TH><TH>Connubial H-Entitlement</TH><TH>Child C-Entitlement</TH><TH>Child H-Entitlement</TH><TH>Old grade</TH><TH>New grade</TH></tr>");	
			Iterator it = staffList.iterator();
			while(it.hasNext()){
				LeaveBalanceCommonInforVO staff = (LeaveBalanceCommonInforVO) it.next();
				MedicalBalanceVO medicalBalance = staff.getMedicalBalanceData();
				
				emailContent = "<br>Please be informed that your annual medical entitlement of "+medicalBalance.getYear()+" was updated: "
					         + "<br>Employee Clinc Entitlement: <font color='red'>" + medicalBalance.getStaffCEntitlement() + "</font> "
					         + "<br>Employee Hospitalization Entitlement: <font color='red'>" + medicalBalance.getStaffHEntitlement() + "</font> "
					         + "<br>Connubial Clinc Entitlement: <font color='red'>" + medicalBalance.getConnubialCEntitlement() + "</font> "
					         + "<br>Connubial Hospitalization Entitlement: <font color='red'>" + medicalBalance.getConnubialHEntitlement() + "</font> "
					         + "<br>Child Clinc Entitlement: <font color='red'>" + medicalBalance.getChildCEntitlement() + "</font> "
					         + "<br>Child Hospitalization Entitlement: <font color='red'>" + medicalBalance.getChildHEntitlement() + "</font> "
					         + "<br>"
					         + "<br>The change took effect on "+staff.getGradeEffectDate()+" due to your grade was promoted from "+staff.getOldGrade()+" to "+staff.getNewGrade();
						
				String staffName = StaffTeamHelper.getInstance().getStaffByCode(staff.getStaffCode()) ==null ? staff.getStaffCode()
						:StaffTeamHelper.getInstance().getStaffByCode(staff.getStaffCode()).getStaffName();
				
				emailContentToHR.append("<tr><td>"+staff.getStaffCode()+"</td>" +
						"<td>"+staffName+"</td>" +
						"<td align='right'>"+medicalBalance.getStaffCEntitlement()+"</td>" +
						"<td align='right'>"+medicalBalance.getStaffHEntitlement()+"</td>" +
						"<td align='right'>"+medicalBalance.getConnubialCEntitlement()+"</td>" +
						"<td align='right'>"+medicalBalance.getConnubialHEntitlement()+"</td>" +
						"<td align='right'>"+medicalBalance.getChildCEntitlement()+"</td>" +
						"<td align='right'>"+medicalBalance.getChildHEntitlement()+"</td>");
				emailContentToHR.append("<td align='right'>"+staff.getOldGrade()+"</td><td align='right'>"+staff.getNewGrade()+"</td></tr>");		
				
				//Only pass probation or is exception case
				if(staff.getCompWorkDays() > 180 || "Y".equals(staff.getMedicalException())){
					EFlowEmailUtil.sendEmail(emailSubject, emailContent, new String[]{ staff.getStaffCode()});
				}
			}
			emailContentToHR.append("</TABLE>");
			if(emailAccountHRStr!=null && !"".equals(emailAccountHRStr)){
			   EFlowEmailUtil.sendEmail(emailSubject, emailContentToHR.toString(), emailAccountHR);
		    }			
		} catch (Exception ex) {
			ex.printStackTrace();
		} 
	}
	
	/**
	 * Send email notifying medical balance change to HR if update is failed
	 * 
	 * <li> staffList:  list of staff common information
	 * <li> type:  
	 * <li> 0-- send email for yearly new balances update,  
	 * <li> 1-- send email for grade change,  
	 * 
	 */	
	
	public static void emailFaildUpdate(Collection staffList, String type, Exception reason) throws Exception {

		try {
			String emailSubject = "Medical entitlement failed";
			String currentDate = StringUtil.getCurrentDateStr("MM/dd/yyyy");
			StringBuffer emailContentToHR = new StringBuffer("");
			String emailAccountHRStr = ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_LEAVE_HR_EMAIL);
			String emailAccountHR[] ={""};
			if(emailAccountHRStr!=null && !"".equals(emailAccountHRStr)){
		       emailAccountHR=emailAccountHRStr.split(";");
			}else{
				return;
			}
			
			if("1".equals(type)){
				emailContentToHR.append("<br>The annual medical entitlement of below staffs were updated on "+currentDate+"due to their grades change: <br>");
			}else if("0".equals(type)){
				emailContentToHR.append("<br>The annual medical entitlement of all staffs for their new year's record were failed to update <br>");
			}
				
			if(!("0").equals(type)){
				Iterator it = staffList.iterator();
				while(it.hasNext()){
					LeaveBalanceCommonInforVO staff = (LeaveBalanceCommonInforVO) it.next();			
					String staffName = StaffTeamHelper.getInstance().getStaffByCode(staff.getStaffCode()) ==null ? staff.getStaffCode()
							:StaffTeamHelper.getInstance().getStaffByCode(staff.getStaffCode()).getStaffName();
					emailContentToHR.append("<br>"+staff.getStaffCode()+"   "+staffName+"    ");				
				}
			}
							
			emailContentToHR.append("<br> The failure was caused by the below reason: ");
			emailContentToHR.append("<br><br><br>"+reason.toString());	
		    EFlowEmailUtil.sendEmail(emailSubject, emailContentToHR.toString(), emailAccountHR);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} 
	}
	
    public ActionLocation manualComingYearBalance(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {   	
  
        response.setContentType("text/html;charset=GB2312"); //it is very important
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");
		PrintWriter out = response.getWriter();
        try {
            String todayStr = StringUtil.getDateStr(new Date(), "MM/dd/yyyy");
            MedicalBalanceAction.generateComingYearBalance(todayStr);                    
            out.print("success");   
            
        } catch (Exception ex) {
        	out.print("failed to manual update Medical Balance!");
            ex.printStackTrace();
        } finally {
            if(out!=null) {
            	out.close();
            }
        }                
        return null;
    }
    
	public static void manualAnnualEntitle(LeaveBalanceCommonInforVO leaveBalanceCommonInfor)throws Exception{
    	try {
    		String dateFormat = "MM/dd/yyyy";
    		Date today = new Date();
    		String todayStr = StringUtil.getDateStr(today, dateFormat);
    		
    		int AIAITWorkDays = LeaveEntitleHelper.daysBetweenDateStr(leaveBalanceCommonInfor.getOnBoardDate(), todayStr, dateFormat);		
    		leaveBalanceCommonInfor.setCompWorkDays(AIAITWorkDays);
 
        	Collection staff = new Vector();
        	staff.add(leaveBalanceCommonInfor);
			Collection emailList = MedicalBalanceAction.updateStaffAnnualEntitleForGradeChange(staff, true);
		
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
