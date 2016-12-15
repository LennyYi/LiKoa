package com.aiait.eflow.housekeeping.action;

import java.io.File;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.*;

import org.apache.poi.hssf.usermodel.HSSFRow;

import com.aiait.eflow.common.CommonName;
import com.aiait.eflow.common.helper.HolidayHelper;
import com.aiait.eflow.common.helper.LeaveEntitleHelper;
import com.aiait.eflow.common.helper.ParamConfigHelper;
import com.aiait.eflow.common.helper.StaffTeamHelper;
import com.aiait.eflow.housekeeping.dao.EmailTemplateDAO;
import com.aiait.eflow.housekeeping.dao.LeaveBalanceCommonInforDAO;
import com.aiait.eflow.housekeeping.dao.LeaveBalanceDAO;
import com.aiait.eflow.housekeeping.vo.*;
import com.aiait.eflow.job.LeaveDailyJob;
import com.aiait.eflow.util.*;
import com.aiait.framework.db.*;
import com.aiait.framework.exception.DAOException;
import com.aiait.framework.i18n.I18NMessageHelper;
import com.aiait.framework.mvc.action.*;

public class LeaveBalanceAction extends DispatchAction {

    public ActionLocation leaveBalanceSetting(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String returnLabel = "leaveBalanceSetting";
        String staffCode = request.getParameter("staffCode");
        IDBManager dbManager = null;
        try {
        	
            dbManager = DBManagerFactory.getDBManager(); 
            //--get the common information of staff
            LeaveBalanceCommonInforDAO leaveBalanceCommonInforDAO = new LeaveBalanceCommonInforDAO(dbManager);
            LeaveBalanceCommonInforVO leaveBalanceCommonInfor = leaveBalanceCommonInforDAO.getBalanceCommonInfor(staffCode);
            request.setAttribute("leaveBalanceCommonInfor", leaveBalanceCommonInfor);
            
            //--get the grade information of staff         
            Collection gradeHistoryList = leaveBalanceCommonInforDAO.getStaffGradeHistoryList(staffCode);                        
            request.setAttribute("gradeHistoryList", gradeHistoryList);
            
            //--get the leave balance information of staff
            Collection leaveBalanceList =null;
            if("Yes".equalsIgnoreCase(ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_LEAVE_BALANCE)) ){
	            LeaveBalanceDAO leaveBalanceDAO = new LeaveBalanceDAO(dbManager);            
	            leaveBalanceList = leaveBalanceDAO.getBalanceList(staffCode);                        
            }
            request.setAttribute("leaveBalanceList", leaveBalanceList);
            
            //--get the medical balance information of staff
            if("Yes".equalsIgnoreCase(ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_MEDICAL_CLAIM)) ){
            	returnLabel = "loadMedicalBalance";
            }
                                      
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

    public ActionLocation saveLeaveBalance(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String staffCode = request.getParameter("staffCode").trim();
        String year = request.getParameter("year").trim();
        String entitle = request.getParameter("entitle").trim();
        String carryForward = request.getParameter("carry_forward").trim();
        String applied = request.getParameter("applied").trim();
        
        String statutoryEntitle = request.getParameter("statutory_entitle").trim();
        String companyEntitle = request.getParameter("company_entitle").trim();
        String forfeit = request.getParameter("forfeit").trim();
        
        String sickTotalEntitle = request.getParameter("sick_total_entitle").trim();
        String sickApplied = request.getParameter("sick_applied").trim();

        LeaveBalanceVO leaveBalance = new LeaveBalanceVO();
        leaveBalance.setStaffCode(staffCode);
        leaveBalance.setYear(Integer.parseInt(year));
        leaveBalance.setAnnualTotalEntitleDays(entitle.equals("") ? 0.0 : Double.parseDouble(entitle));
        leaveBalance.setAnnualCarryForwardDays(carryForward.equals("") ? 0.0 : Double.parseDouble(carryForward));
        leaveBalance.setAnnualAppliedDays(applied.equals("") ? 0.0 : Double.parseDouble(applied));
        
        leaveBalance.setAnnualStatutoryEntitleDays(statutoryEntitle.equals("") ? 0.0 : Double.parseDouble(statutoryEntitle));
        leaveBalance.setAnnualCompanyEntitleDays(companyEntitle.equals("") ? 0.0 : Double.parseDouble(companyEntitle));
        leaveBalance.setAnnualForfeitDays(forfeit.equals("") ? 0.0 : Double.parseDouble(forfeit));
        
        leaveBalance.setSickTotalEntitleDays(sickTotalEntitle.equals("") ? 0.0 : Double.parseDouble(sickTotalEntitle));
        leaveBalance.setSickAppliedDays(sickApplied.equals("") ? 0.0 : Double.parseDouble(sickApplied));

        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            dbManager.startTransaction();
            LeaveBalanceDAO leaveBalanceDAO = new LeaveBalanceDAO(dbManager);
            leaveBalanceDAO.saveBalance(leaveBalance);
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
        return this.leaveBalanceSetting(mapping, request, response);
    }
    
    public ActionLocation saveLeaveBalanceCommonInfor(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
          
    	String currentStaffCode = request.getParameter("currentStaffCode").trim();
        String staffCode = request.getParameter("staffCode").trim();
        String staffType = request.getParameter("staff_type").trim();
        String onBoardDate = request.getParameter("onboard_date").trim();
        String preWorkingExperience = request.getParameter("pre_working_experience").trim();
        String grade = request.getParameter("grade").trim();
        String old_grade = request.getParameter("old_grade").trim();
        String effectiveDate = request.getParameter("effective_date").trim();
        String updateEntitlement = request.getParameter("updateEntitlement").trim();
        //String sendEmail = request.getParameter("sendEmail");
        String medicalException = request.getParameter("medicalException").trim();
        String updatedLeave = request.getParameter("updatedLeave").trim();
        String updatedMedical = request.getParameter("updatedMedical").trim();
        String updateMedicalEntitlement = request.getParameter("updateMedicalEntitlement").trim();
        
        LeaveBalanceCommonInforVO leaveBalanceCommonInfor = new LeaveBalanceCommonInforVO();
        leaveBalanceCommonInfor.setModifiedStaff(currentStaffCode);
        leaveBalanceCommonInfor.setStaffCode(staffCode);
        leaveBalanceCommonInfor.setStaffType(staffType);
        leaveBalanceCommonInfor.setOnBoardDate(onBoardDate);
        leaveBalanceCommonInfor.setPreWorkExperience(Double.parseDouble(preWorkingExperience));
        leaveBalanceCommonInfor.setNewGrade(Double.parseDouble(grade));
        leaveBalanceCommonInfor.setOldGrade(Double.parseDouble(old_grade));
        leaveBalanceCommonInfor.setGradeEffectDate(effectiveDate);
        leaveBalanceCommonInfor.setMedicalException(medicalException);
        leaveBalanceCommonInfor.setUpdatedLeave(updatedLeave);
        leaveBalanceCommonInfor.setUpdatedMedical(updatedMedical);
        
        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            dbManager.startTransaction();
            LeaveBalanceCommonInforDAO leaveBalanceCommonInforDAO = new LeaveBalanceCommonInforDAO(dbManager);
            leaveBalanceCommonInforDAO.saveBalanceCommonInfor(leaveBalanceCommonInfor);
            dbManager.commit();
            
            //手动计算更新年休假
            if(CommonName.LEAVE_BALANCE_STAFF_PERMANENT.equals(staffType) && "true".equals(updateEntitlement)){
            	this.manualAnnualEntitle(leaveBalanceCommonInfor);
            }
            
            //手动计算更新医疗年额
            if(CommonName.LEAVE_BALANCE_STAFF_PERMANENT.equals(staffType) && "true".equals(updateMedicalEntitlement)){
            	MedicalBalanceAction.manualAnnualEntitle(leaveBalanceCommonInfor);
            }
            
        } catch (Exception ex) {
            dbManager.rollback();
            request.setAttribute(CommonName.COMMON_ERROR_INFOR, ex.getMessage());
            ex.printStackTrace();
        } finally {
            if (dbManager != null) {
                dbManager.freeConnection();
            }
        }        
        return this.leaveBalanceSetting(mapping, request, response);
    }
    
    public ActionLocation deleteGradeRecord(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String staffCode = request.getParameter("staffCode").trim();
        String[] gradeEffectDates = request.getParameterValues("gradeItemId");

        LeaveBalanceCommonInforVO leaveBalanceCommonInfor = new LeaveBalanceCommonInforVO();
        leaveBalanceCommonInfor.setStaffCode(staffCode);

        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            dbManager.startTransaction();
            LeaveBalanceCommonInforDAO leaveBalanceCommonInforDAO = new LeaveBalanceCommonInforDAO(dbManager);
            for (int i = 0; i < gradeEffectDates.length; i++) {
                String gradeEffectDate = gradeEffectDates[i];
                leaveBalanceCommonInfor.setGradeEffectDate(gradeEffectDate);
                leaveBalanceCommonInforDAO.deleteGrade(leaveBalanceCommonInfor);
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
        return this.leaveBalanceSetting(mapping, request, response);
    }    
        
    public ActionLocation deleteLeaveBalance(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String staffCode = request.getParameter("staffCode").trim();
        String[] years = request.getParameterValues("itemId");

        LeaveBalanceVO leaveBalance = new LeaveBalanceVO();
        leaveBalance.setStaffCode(staffCode);

        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            dbManager.startTransaction();
            LeaveBalanceDAO leaveBalanceDAO = new LeaveBalanceDAO(dbManager);
            for (int i = 0; i < years.length; i++) {
                String year = years[i];
                leaveBalance.setYear(Integer.parseInt(year));
                leaveBalanceDAO.deleteBalance(leaveBalance);
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
        return this.leaveBalanceSetting(mapping, request, response);
    }

    public ActionLocation selectUploadFile(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String returnLabel = "selectUploadFile";
        request.setAttribute("action", "leaveBalanceAction.it?method=importLeaveBalance");
        request.setAttribute("title", "Upload Leave Balance");
        return mapping.findActionLocation(returnLabel);
    }

    public ActionLocation importLeaveBalance(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String returnLabel = "tempsuccess";
        StaffVO currentStaff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
        String userId = currentStaff.getLogonId();
        String fileName = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmdd");
        String dateName = dateFormat.format(new Date());
        HttpServletRequest multiRequest = null;
        try {
            if (request.getMethod().equals("POST") && MultipartRequest.isMultipart(request)) {
                multiRequest = new MultipartRequest(request, userId);
                File file = ((MultipartRequest) multiRequest).getFile("path");
                if (file == null) {
                    request.setAttribute(CommonName.COMMON_ERROR_INFOR, "File path error");
                    return mapping.findActionLocation("fail");
                }
                fileName = dateName + "_" + ((MultipartRequest) multiRequest).getFileName(file);
                fileName = fileName.replaceAll("'", "");
                String uploadPath = ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_UPLOAD_DIR);
                if (uploadPath == null) {
                    uploadPath = "D:\\E-Flow_Upload";
                }
                fileName = uploadPath + "/" + fileName;
                FileUtil.saveAs(file, fileName);
                ExcelUtil excelUtil = new ExcelUtil(fileName);

                Collection dataList = this.parseExcel(excelUtil);
                Collection workExperienceList = this.parseWorkExperience(excelUtil);
                Collection gradeList = this.parseGrade(excelUtil);
                this.saveExcelData(dataList, workExperienceList, gradeList);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            request.setAttribute(CommonName.COMMON_ERROR_INFOR, ex.toString());
            returnLabel = "fail";
        } finally {
            if (multiRequest != null && multiRequest instanceof MultipartRequest) {
                // Delete the temp file
                ((MultipartRequest) multiRequest).deleteTemporaryFile();
            }
            if (fileName != null && !"".equals(fileName)) {
                FileUtil.deleteFile(fileName);
            }
        }
        return mapping.findActionLocation(returnLabel);
    }

    private void saveExcelData(Collection dataList, Collection workExperienceList, Collection gradeList) throws Exception {
        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            dbManager.startTransaction();
            //For leave records
            LeaveBalanceDAO leaveBalanceDAO = new LeaveBalanceDAO(dbManager);
            Iterator it = dataList.iterator();
            while (it.hasNext()) {
                LeaveBalanceVO leaveBalance = (LeaveBalanceVO) it.next();
                leaveBalanceDAO.saveBalance(leaveBalance);
            }
            dbManager.commit();
            
            //For work experience
            LeaveBalanceCommonInforDAO leaveBalanceCommonInforDAO = new LeaveBalanceCommonInforDAO(dbManager);          
            Iterator wke_it = workExperienceList.iterator();
            while (wke_it.hasNext()) {
            	LeaveBalanceCommonInforVO leaveBalanceWorkExperience = (LeaveBalanceCommonInforVO) wke_it.next();
            	leaveBalanceCommonInforDAO.saveWorkExperience(leaveBalanceWorkExperience);
            }
            dbManager.commit();
            
            //For grade
            Iterator grade_it = gradeList.iterator();
            while (grade_it.hasNext()) {
            	LeaveBalanceCommonInforVO leaveBalanceGrade = (LeaveBalanceCommonInforVO) grade_it.next();
            	leaveBalanceCommonInforDAO.saveGrade(leaveBalanceGrade);
            }
            dbManager.commit();            
            
            
            
        } catch (Exception ex) {
            dbManager.rollback();
            ex.printStackTrace();
        } finally {
            if (dbManager != null) {
                dbManager.freeConnection();
            }
        }
    }

    protected Collection parseWorkExperience(ExcelUtil excelUtil) throws Exception {
        int startLine = 2;
        int colNum = 4;
        int sheetIndex = 2;

        Collection list = new ArrayList();
        excelUtil.setSheet(sheetIndex);
        int rows = excelUtil.getSheet().getPhysicalNumberOfRows();
        if (rows >= startLine) {
            // Check format
            HSSFRow rowline = excelUtil.getSheet().getRow(1);
            if (rowline.getLastCellNum() < colNum) {
                throw new Exception("Excel file format error, work experience sheet should have " + colNum + " data columns.");
            }

            // Process
            for (int r = startLine; r <= rows; r++) {
            	LeaveBalanceCommonInforVO leaveBalanceCommonInfor = new LeaveBalanceCommonInforVO();
                int i = 1;

                // Staff Code
                String staffCode = excelUtil.getCellValue(sheetIndex, r, i++);
                staffCode = staffCode == null ? "" : staffCode.trim();
                if ("".equals(staffCode)) {
                    continue;
                    // throw new Exception("Row/Column: " + r + "/" + --i + ": Staff Code cannot be null!");
                }
                leaveBalanceCommonInfor.setStaffCode(staffCode);

                // Staff Type
                String staffType = excelUtil.getCellValue(sheetIndex, r, i++);
                staffType = staffType == null ? "Permanent" : staffType.trim();
                if ("".equals(staffType)||"Permanent".equals(staffType)) {
                	staffType = CommonName.LEAVE_BALANCE_STAFF_PERMANENT;
                    // throw new Exception("Row/Column: " + r + "/" + --i + ": Staff Type cannot be null!");
                }else if ("Intern".equals(staffType)){
                	staffType = CommonName.LEAVE_BALANCE_STAFF_INTERN;
                }else if("EC".equals(staffType)){
                	staffType = CommonName.LEAVE_BALANCE_STAFF_EC;
                }else {
                	staffType = CommonName.LEAVE_BALANCE_STAFF_PERMANENT;
                }
                
                try {
                	leaveBalanceCommonInfor.setStaffType(staffType);
                } catch (Exception ex) {
                    throw new Exception("Row/Column: " + r + "/" + --i + ": Invalid Staff Type");
                }
                
                // OnBoard Date
                String onBoardDate = excelUtil.getCellValue(sheetIndex, r, i++);
                onBoardDate = onBoardDate == null ? "" : onBoardDate.trim();
                if ("".equals(onBoardDate)) {
                	continue;
                    // throw new Exception("Row/Column: " + r + "/" + --i + ": OnBoard Date cannot be null!");
                }
                try {
                	leaveBalanceCommonInfor.setOnBoardDate(onBoardDate);
                } catch (Exception ex) {
                    throw new Exception("Row/Column: " + r + "/" + --i + ": Invalid OnBoard Date");
                }
                
                //PreWorking Experience
                String preWorkExperience = excelUtil.getCellValue(sheetIndex, r, i++);
                preWorkExperience = preWorkExperience == null ? "" : preWorkExperience.trim();
                if ("".equals(preWorkExperience)) {
                	preWorkExperience = "0.00";
                    // throw new Exception("Row/Column: " + r + "/" + --i + ": PreWorking Experience cannot be null!");
                }
                try {
                	leaveBalanceCommonInfor.setPreWorkExperience(Double.parseDouble(preWorkExperience));
                } catch (Exception ex) {
                    throw new Exception("Row/Column: " + r + "/" + --i + ": Invalid PreWorking Experience");
                }
                
                list.add(leaveBalanceCommonInfor);
            }
        }

        return list;
    } 
    
    protected Collection parseGrade(ExcelUtil excelUtil) throws Exception {
        int startLine = 2;
        int colNum = 5;
        int sheetIndex = 3;

        Collection list = new ArrayList();
        excelUtil.setSheet(sheetIndex);
        int rows = excelUtil.getSheet().getPhysicalNumberOfRows();
        if (rows >= startLine) {
            // Check format
            HSSFRow rowline = excelUtil.getSheet().getRow(1);
            if (rowline.getLastCellNum() < colNum) {
                throw new Exception("Excel file format error, grade sheet should have " + colNum + " data columns.");
            }

            // Process
            for (int r = startLine; r <= rows; r++) {
            	LeaveBalanceCommonInforVO leaveBalanceCommonInfor = new LeaveBalanceCommonInforVO();
                int i = 1;

                // Staff Code
                String staffCode = excelUtil.getCellValue(sheetIndex, r, i++);
                staffCode = staffCode == null ? "" : staffCode.trim();
                if ("".equals(staffCode)) {
                    continue;
                    // throw new Exception("Row/Column: " + r + "/" + --i + ": Staff Code cannot be null!");
                }
                leaveBalanceCommonInfor.setStaffCode(staffCode);

                // Staff Grade
                String grade = excelUtil.getCellValue(sheetIndex, r, i++);
                grade = grade == null ? "" : grade.trim();
                if ("".equals(grade)) {
                	continue;
                    // throw new Exception("Row/Column: " + r + "/" + --i + ": Staff New Grade cannot be null!");
                }
                try {
                	leaveBalanceCommonInfor.setNewGrade(Double.parseDouble(grade));
                } catch (Exception ex) {
                    throw new Exception("Row/Column: " + r + "/" + --i + ": Invalid Staff New Grade");
                }
                
                // Staff Old Grade
                String oldGrade = excelUtil.getCellValue(sheetIndex, r, i++);
                oldGrade = oldGrade == null ? "" : oldGrade.trim();
                if ("".equals(oldGrade)) {
                	continue;
                    // throw new Exception("Row/Column: " + r + "/" + --i + ": Staff Old Grade cannot be null!");
                }
                try {
                	leaveBalanceCommonInfor.setOldGrade(Double.parseDouble(oldGrade));
                } catch (Exception ex) {
                    throw new Exception("Row/Column: " + r + "/" + --i + ": Invalid Staff Old Grade");
                }
                
                //Grade Effect Date
                String gradeEffectDate = excelUtil.getCellValue(sheetIndex, r, i++);
                gradeEffectDate = gradeEffectDate == null ? "" : gradeEffectDate.trim();
                if ("".equals(gradeEffectDate)) {
                	continue;
                    // throw new Exception("Row/Column: " + r + "/" + --i + ": Grade Effect Date cannot be null!");
                }
                try {
                	leaveBalanceCommonInfor.setGradeEffectDate(gradeEffectDate);
                } catch (Exception ex) {
                    throw new Exception("Row/Column: " + r + "/" + --i + ": Invalid Grade Effect Date");
                }
                
                //Modified Staff
                String modifiedStaff = excelUtil.getCellValue(sheetIndex, r, i++);
                modifiedStaff = modifiedStaff == null ? "" : modifiedStaff.trim();
                if ("".equals(modifiedStaff)) {
                	modifiedStaff = "";
                    // throw new Exception("Row/Column: " + r + "/" + --i + ": Modified Staff cannot be null!");
                }
                try {
                	leaveBalanceCommonInfor.setModifiedStaff(modifiedStaff);
                } catch (Exception ex) {
                    throw new Exception("Row/Column: " + r + "/" + --i + ": Invalid Modified Staff");
                }
                
                list.add(leaveBalanceCommonInfor);
            }
        }

        return list;
    }    
    
    protected Collection parseExcel(ExcelUtil excelUtil) throws Exception {
        int startLine = 2;
        int colNum = 12;

        Collection list = new ArrayList();
        excelUtil.setSheet(1);
        int rows = excelUtil.getSheet().getPhysicalNumberOfRows();
        if (rows >= startLine) {
            // Check format
            HSSFRow rowline = excelUtil.getSheet().getRow(1);
            if (rowline.getLastCellNum() < colNum) {
                throw new Exception("Excel file format error, leave balance sheet should have " + colNum + " data columns.");
            }

            // Process
            for (int r = startLine; r <= rows; r++) {
                LeaveBalanceVO leaveBalance = new LeaveBalanceVO();
                int i = 1;

                // Staff Code
                String staffCode = excelUtil.getCellValue(1, r, i++);
                staffCode = staffCode == null ? "" : staffCode.trim();
                if ("".equals(staffCode)) {
                    continue;
                    // throw new Exception("Row/Column: " + r + "/" + --i + ": Staff Code cannot be null!");
                }
                leaveBalance.setStaffCode(staffCode);

                // Year
                String year = excelUtil.getCellValue(1, r, i++);
                year = year == null ? "" : year.trim();
                if ("".equals(year)) {
                    continue;
                    // throw new Exception("Row/Column: " + r + "/" + --i + ": Year cannot be null!");
                }
                try {
                    leaveBalance.setYear(Integer.parseInt(year));
                } catch (Exception ex) {
                    throw new Exception("Row/Column: " + r + "/" + --i + ": Invalid Year");
                }
                
                // Annual Carry Forward
                String annualCarryForward = excelUtil.getCellValue(1, r, i++);
                annualCarryForward = annualCarryForward == null ? "" : annualCarryForward.trim();
                if ("".equals(annualCarryForward)) {
                	annualCarryForward = "0.0";
                    // throw new Exception("Row/Column: " + r + "/" + --i + ": Annual Carry Forward cannot be null!");
                }
                try {
                    leaveBalance.setAnnualCarryForwardDays(Double.parseDouble(annualCarryForward));
                } catch (Exception ex) {
                    throw new Exception("Row/Column: " + r + "/" + --i + ": Invalid Annual Carry Forward Days");
                }
                
                // Annual Statutory Entitlement
                String annualStatutoryEntitlement = excelUtil.getCellValue(1, r, i++);
                annualStatutoryEntitlement = annualStatutoryEntitlement == null ? "" : annualStatutoryEntitlement.trim();
                if ("".equals(annualStatutoryEntitlement)) {
                	annualStatutoryEntitlement = "0.0";
                    // throw new Exception("Row/Column: " + r + "/" + --i + ": Annual Statutory Entitlement cannot be null!");
                }
                try {
                    leaveBalance.setAnnualStatutoryEntitleDays(Double.parseDouble(annualStatutoryEntitlement));
                } catch (Exception ex) {
                    throw new Exception("Row/Column: " + r + "/" + --i + ": Invalid Annual Statutory Entitlement Days");
                }
                
                // Annual Company Entitlement
                String annualCompanyEntitlement = excelUtil.getCellValue(1, r, i++);
                annualCompanyEntitlement = annualCompanyEntitlement == null ? "" : annualCompanyEntitlement.trim();
                if ("".equals(annualCompanyEntitlement)) {
                	annualCompanyEntitlement = "0.0";
                    // throw new Exception("Row/Column: " + r + "/" + --i + ": Annual Company Entitlement cannot be null!");
                }
                try {
                    leaveBalance.setAnnualCompanyEntitleDays(Double.parseDouble(annualCompanyEntitlement));
                } catch (Exception ex) {
                    throw new Exception("Row/Column: " + r + "/" + --i + ": Invalid Annual Company Entitlement Days");
                }

                // Annual Total Entitlement
                String entitle = excelUtil.getCellValue(1, r, i++);
                entitle = entitle == null ? "" : entitle.trim();
                if ("".equals(entitle)) {
                    entitle = "0.0";
                    // throw new Exception("Row/Column: " + r + "/" + --i + ": Annual Total Entitlement cannot be null!");
                }
                Double totalEntitle = Double.parseDouble(annualStatutoryEntitlement) + Double.parseDouble(annualCompanyEntitlement);
                if(Double.parseDouble(entitle) != totalEntitle){
                	throw new Exception("Row/Column: " + r + "/" + --i + ": Annual Total Entitlement value is not correct,it should be the sum value of Statutory Entitlement and Company Entitlement!");
                }
                
                try {
                    leaveBalance.setAnnualTotalEntitleDays(Double.parseDouble(entitle));
                } catch (Exception ex) {
                    throw new Exception("Row/Column: " + r + "/" + --i + ": Invalid Annual Total Entitlement Days");
                }
                

                // Annual Applied
                String applied = excelUtil.getCellValue(1, r, i++);
                applied = applied == null ? "" : applied.trim();
                if ("".equals(applied)) {
                	applied = "0.0";
                    // throw new Exception("Row/Column: " + r + "/" + --i + ": Annual Applied cannot be null!");
                }
               
                    try {
                        leaveBalance.setAnnualAppliedDays(Double.parseDouble(applied));
                    } catch (Exception ex) {
                        throw new Exception("Row/Column: " + r + "/" + --i + ": Invalid Annual Applied Days");
                    }

                
                // Annual Forfeit
                String annualForfeit = excelUtil.getCellValue(1, r, i++);
                annualForfeit = annualForfeit == null ? "" : annualForfeit.trim();
                if ("".equals(annualForfeit)) {
                	annualForfeit = "0.0";
                    // throw new Exception("Row/Column: " + r + "/" + --i + ": Annual Forfeit cannot be null!");
                }
                try {
                    leaveBalance.setAnnualForfeitDays(Double.parseDouble(annualForfeit));
                } catch (Exception ex) {
                    throw new Exception("Row/Column: " + r + "/" + --i + ": Invalid Annual Forfeit Days");
                }
                
                // Annual Balance
                String  annualBalance = excelUtil.getCellValue(1, r, i++);
                double annualBalanceResult = leaveBalance.getAnnualBalanceDays();
                if(annualBalanceResult < 0){
                	throw new Exception("Row/Column: " + r + "/" + --i + ": The annual balance shouldn't less than 0,please check!");
                }
                
                //Sick Total Entitlement
                String sickTotalEntitlement = excelUtil.getCellValue(1, r, i++);
                sickTotalEntitlement = sickTotalEntitlement == null ? "" : sickTotalEntitlement.trim();
                if ("".equals(sickTotalEntitlement)) {
                	sickTotalEntitlement = "0.0";
                    // throw new Exception("Row/Column: " + r + "/" + --i + ": Sick Total Entitlement cannot be null!");
                }
                try {
                    leaveBalance.setSickTotalEntitleDays(Double.parseDouble(sickTotalEntitlement));
                } catch (Exception ex) {
                    throw new Exception("Row/Column: " + r + "/" + --i + ": Invalid Sick Total Entitlement Days");
                }
                
                //Sick Applied Days
                String sickAppliedDays = excelUtil.getCellValue(1, r, i++);
                sickAppliedDays = sickAppliedDays == null ? "" : sickAppliedDays.trim();
                if ("".equals(sickAppliedDays)) {
                	sickAppliedDays = "0.0";
                    // throw new Exception("Row/Column: " + r + "/" + --i + ": Sick Applied Days cannot be null!");
                }
                try {
                    leaveBalance.setSickAppliedDays(Double.parseDouble(sickAppliedDays));
                } catch (Exception ex) {
                    throw new Exception("Row/Column: " + r + "/" + --i + ": Invalid Sick Applied Days");
                }
                
                // Sick Balance
                String  sickBalance = excelUtil.getCellValue(1, r, i++);
                double sickBalanceResult = leaveBalance.getSickBalanceDays();
                if(sickBalanceResult < 0){
                	throw new Exception("Row/Column: " + r + "/" + --i + ": The sick balance shouldn't less than 0,please check!");
                }

                list.add(leaveBalance);
            }
        }

        return list;
    }
    
	public static Collection UpdateStaffAnnualEntitleForExperienceChange(Collection staffCodeList) throws Exception {

		IDBManager dbManager = null;

		try {
			dbManager = DBManagerFactory.getDBManager();
			LeaveBalanceCommonInforDAO leaveBalanceCommonInforDAO = new LeaveBalanceCommonInforDAO(dbManager);
            Collection emailList = new Vector();
            Collection updateList = new Vector();
			Iterator it = staffCodeList.iterator();
			while (it.hasNext()) {
				LeaveBalanceCommonInforVO leaveBalanceCommonInforVO = (LeaveBalanceCommonInforVO) it.next();

				String experienceChangeDateStr = "";
				String dateFormat = "MM/dd/yyyy";

				Date today = new Date();
				String todayStr = StringUtil.getDateStr(today, dateFormat);
				String currentYear = todayStr.substring(6, 10);

				int AIAITWorkDays = leaveBalanceCommonInforVO.getCompWorkDays();
				int totalWorkDays = leaveBalanceCommonInforVO.getTotalWorkDays();

				// Grade change history of current year
				Collection gradeHistoryList = leaveBalanceCommonInforDAO.getStaffGradeHistoryList(leaveBalanceCommonInforVO.getStaffCode(), todayStr, "2");
				String lastYear = "" + (Integer.parseInt(currentYear) - 1);
				String beginDateStr = "12/31/" + lastYear;
				String endDateStr = "12/31/" + currentYear;
				Double totalEntitleDays = 0.0;

				//There is grade change record in this year
				if (gradeHistoryList.size() > 0) {
					Iterator historyIt = gradeHistoryList.iterator();

					while (historyIt.hasNext()) {

						LeaveBalanceCommonInforVO gradeHistory = (LeaveBalanceCommonInforVO) historyIt.next();

						String oneDateBeforeGC = StringUtil.afterNDay(-1, dateFormat, gradeHistory.getGradeEffectDate());

						/*
						 * Here to work out the leave entitlement between last effective date / year begin  ~  this effective date 
						 */
						double actualBeforeGC = LeaveEntitleHelper.calActualAnnualLeaveDays(beginDateStr, oneDateBeforeGC, leaveBalanceCommonInforVO.getOnBoardDate(), dateFormat, gradeHistory.getOldGrade());
						totalEntitleDays += actualBeforeGC;

						if (historyIt.hasNext()) {
							beginDateStr = oneDateBeforeGC;
						} else {
							/*
							 * Here to work out the leave entitlement between this grade effective  ~ today
							 */
							String oneDateBeforeWEC = StringUtil.afterNDay(-1, dateFormat, todayStr);
							double actualBeforeWEC = LeaveEntitleHelper.calActualAnnualLeaveDays(oneDateBeforeGC, oneDateBeforeWEC, leaveBalanceCommonInforVO.getOnBoardDate(), dateFormat, gradeHistory.getNewGrade());
							totalEntitleDays += actualBeforeWEC;

							/*
							 * Here to work out the leave entitlement between today ~ year end
							 */					
							double actualAfterWEC = LeaveEntitleHelper.calActualAnnualLeaveDays(oneDateBeforeWEC, endDateStr, leaveBalanceCommonInforVO.getOnBoardDate(), dateFormat, gradeHistory.getNewGrade());
							totalEntitleDays += actualAfterWEC;
						}
					}
				} 
				
				//There is not grade change record in this year
				else {	
					LeaveBalanceCommonInforVO gradeHistory = leaveBalanceCommonInforDAO.getClosestGrade(leaveBalanceCommonInforVO.getStaffCode(), todayStr);
					
					/*
					 * Here to work out the leave entitlement between year begin  ~ today
					 */
					String oneDateBeforeWEC = StringUtil.afterNDay(-1, dateFormat, todayStr);
					double actualBeforeWEC = LeaveEntitleHelper.calActualAnnualLeaveDays(beginDateStr, oneDateBeforeWEC, leaveBalanceCommonInforVO.getOnBoardDate(), dateFormat, gradeHistory.getNewGrade());
					totalEntitleDays += actualBeforeWEC;

					/*
					 * Here to work out the leave entitlement between today ~ year end
					 */
					double actualAfterWEC = LeaveEntitleHelper.calActualAnnualLeaveDays(oneDateBeforeWEC, endDateStr, leaveBalanceCommonInforVO.getOnBoardDate(), dateFormat, gradeHistory.getNewGrade());
					totalEntitleDays += actualAfterWEC;
				}

				totalEntitleDays = LeaveEntitleHelper.RoundDownDays(totalEntitleDays);

				// --get the leave information of the staff of current year
				LeaveBalanceDAO leaveBalanceDAO = new LeaveBalanceDAO(dbManager);
				LeaveBalanceVO vo = new LeaveBalanceVO();
				vo.setStaffCode(leaveBalanceCommonInforVO.getStaffCode());
				vo.setYear(Integer.parseInt(currentYear));
				LeaveBalanceVO leaveBalanceCurrentYear = leaveBalanceDAO.getBalance(vo);
				
				//--no record of current year, then generate a new one.
				if(leaveBalanceCurrentYear == null){
					//以新一年最后一天为总工作天数计算法定假
					int daysBetween = LeaveEntitleHelper.daysBetweenDateStr(todayStr, endDateStr, "MM/dd/yyyy");	
					double annualStatutoryEntitle = LeaveEntitleHelper.getStatutoryAnnualEntitle(totalWorkDays + daysBetween);
					
					leaveBalanceCurrentYear = LeaveEntitleHelper.generateLeaveBalanceRecord(leaveBalanceCommonInforVO.getStaffCode(),leaveBalanceCommonInforVO.getOnBoardDate(), todayStr, dateFormat,totalEntitleDays, annualStatutoryEntitle);
				}//-- update the current one
				else {
					leaveBalanceCurrentYear = LeaveEntitleHelper.updateLeaveBalanceRecord(leaveBalanceCurrentYear, totalEntitleDays);
				}

				LeaveBalanceCommonInforVO result = new LeaveBalanceCommonInforVO();
				result.clone(leaveBalanceCommonInforVO);
				result.setLeaveBasicData(leaveBalanceCurrentYear);
				//如果当年法定年休假大于总年休假, 不更新.
				if(leaveBalanceCurrentYear.getAnnualStatutoryEntitleDays() > leaveBalanceCurrentYear.getAnnualTotalEntitleDays()){
					result.setIndct1("0");
				}else{
					result.setIndct1("1");
					updateList.add(result);
				}
				
				emailList.add(result);
			}
			
			// save all new records
			if(updateList.size()>0 ){
				saveBalanceRecord(updateList);
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
	
	public static Collection UpdateStaffAnnualEntitleForGradeChange(Collection staffCodeList, boolean manual) throws Exception {

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
			String currentYear = todayStr.substring(6, 10);
			
			while (it.hasNext()) {
				LeaveBalanceCommonInforVO leaveBalanceCommonInforVO = (LeaveBalanceCommonInforVO) it.next();

				String experienceChangeDateStr = "";

				int AIAITWorkDays = leaveBalanceCommonInforVO.getCompWorkDays();
				int totalWorkDays = leaveBalanceCommonInforVO.getTotalWorkDays();

				// Get the date of the work experience was just happen to over 5, 10 or 15 years
				if (AIAITWorkDays >= LeaveEntitleHelper.LEAVE_BALANCE_15YEARS) {
					experienceChangeDateStr = StringUtil.afterNDay(LeaveEntitleHelper.LEAVE_BALANCE_15YEARS, dateFormat, leaveBalanceCommonInforVO.getOnBoardDate());
				} else if (AIAITWorkDays >= LeaveEntitleHelper.LEAVE_BALANCE_10YEARS) {
					experienceChangeDateStr = StringUtil.afterNDay(LeaveEntitleHelper.LEAVE_BALANCE_10YEARS, dateFormat, leaveBalanceCommonInforVO.getOnBoardDate());
				} else if (AIAITWorkDays >= LeaveEntitleHelper.LEAVE_BALANCE_5YEARS) {
					experienceChangeDateStr = StringUtil.afterNDay(LeaveEntitleHelper.LEAVE_BALANCE_5YEARS, dateFormat, leaveBalanceCommonInforVO.getOnBoardDate());
				}

				// Check if the experience change was happen in this year and before today
				boolean experienceWasChanged = false;
				if ((!experienceChangeDateStr.equals("")) && (experienceChangeDateStr.substring(6, 10).equals(currentYear)) && (LeaveEntitleHelper.compareDateStr(experienceChangeDateStr, todayStr, dateFormat) != 2)) {
					experienceWasChanged = true;
				}

				// Grade change history of current year
				Collection gradeHistoryList = leaveBalanceCommonInforDAO.getStaffGradeHistoryList(leaveBalanceCommonInforVO.getStaffCode(), todayStr, "1");

				// int totalEntitleDays =0;
				String lastYear = "" + (Integer.parseInt(currentYear) - 1);
				String beginDateStr = "12/31/" + lastYear;
				String endDateStr = "12/31/" + currentYear;
				Double totalEntitleDays = 0.0;

				// There is grade change record in this year
				if (gradeHistoryList.size() > 0) {

					Iterator historyIt = gradeHistoryList.iterator();
					while (historyIt.hasNext()) {

						LeaveBalanceCommonInforVO gradeHistory = (LeaveBalanceCommonInforVO) historyIt.next();
						String oneDateBeforeGC = StringUtil.afterNDay(-1, dateFormat, gradeHistory.getGradeEffectDate());

						/*
						 * Here to work out the leave entitlement between last effective date / year begin  ~  this effective date 
						 */

						// The work experience wasn't changed
						if (experienceWasChanged == false) {
							// The actual annual entitlement between last effective date / year begin  ~  this effective date 
							double actualBeforeGC = LeaveEntitleHelper.calActualAnnualLeaveDays(beginDateStr, oneDateBeforeGC, leaveBalanceCommonInforVO.getOnBoardDate(), dateFormat, gradeHistory.getOldGrade());
							totalEntitleDays += actualBeforeGC;
						}

						// The work experience was changed
						else {
							int changedStatus = LeaveEntitleHelper.compareDateStr(experienceChangeDateStr, gradeHistory.getGradeEffectDate(), dateFormat);

							// It is occurred before this grade change date, then split into two parts
							if (changedStatus == 1) {
								// The actual annual entitlement between last effective date / year begin  ~  the experience change date
								String oneDateBeforeWEC = StringUtil.afterNDay(-1, dateFormat, experienceChangeDateStr);
								double actualBeforeWEC = LeaveEntitleHelper.calActualAnnualLeaveDays(beginDateStr, oneDateBeforeWEC, leaveBalanceCommonInforVO.getOnBoardDate(), dateFormat, gradeHistory.getOldGrade());
								totalEntitleDays += actualBeforeWEC;

								// The actual annual entitlement between the experience change date ~ this effective date.
								double actualBeforeGC = LeaveEntitleHelper.calActualAnnualLeaveDays(oneDateBeforeWEC, oneDateBeforeGC, leaveBalanceCommonInforVO.getOnBoardDate(), dateFormat, gradeHistory.getOldGrade());
								totalEntitleDays += actualBeforeGC;
								experienceWasChanged = false;
							}
							// It is occurred on or after the date of this grade change
							else {								
								// The actual annual entitlement between last effective date / year begin  ~  this effective date .
								double actualBeforeGC = LeaveEntitleHelper.calActualAnnualLeaveDays(beginDateStr, oneDateBeforeGC, leaveBalanceCommonInforVO.getOnBoardDate(), dateFormat, gradeHistory.getOldGrade());
								totalEntitleDays += actualBeforeGC;
							}
						}

						if (historyIt.hasNext()) {
							beginDateStr = oneDateBeforeGC;
						}
						/*
						 * Here to work out the leave entitlement between the last grade change date  ~  year end
						 */
						else {
							if (experienceWasChanged == false) {
								double actualAfterGC = LeaveEntitleHelper.calActualAnnualLeaveDays(oneDateBeforeGC, endDateStr, leaveBalanceCommonInforVO.getOnBoardDate(), dateFormat, gradeHistory.getNewGrade());
								totalEntitleDays += actualAfterGC;
							} 
							
							// The work experience was changed after the last grade changed date
							else {
								// The actual annual entitlement between last effective date / year begin ~ the experience change date
								String oneDateBeforeWEC = StringUtil.afterNDay(-1, dateFormat, experienceChangeDateStr);
								double actualBeforeWEC = LeaveEntitleHelper.calActualAnnualLeaveDays(oneDateBeforeGC, oneDateBeforeWEC, leaveBalanceCommonInforVO.getOnBoardDate(), dateFormat, gradeHistory.getNewGrade());
								totalEntitleDays += actualBeforeWEC;

								// The actual annual entitlement between the experience change date ~ this effective date.
								double actualBeforeGC = LeaveEntitleHelper.calActualAnnualLeaveDays(oneDateBeforeWEC, endDateStr, leaveBalanceCommonInforVO.getOnBoardDate(), dateFormat, gradeHistory.getNewGrade());
								totalEntitleDays += actualBeforeGC;
							}
						}
					}
				} 
				//There is not grade change record in this year
				else {
					LeaveBalanceCommonInforVO gradeHistory = leaveBalanceCommonInforDAO.getClosestGrade(leaveBalanceCommonInforVO.getStaffCode(), todayStr);
					
					// The work experience wasn't changed
					if (experienceWasChanged == false) {
						// The actual annual entitlement between  year begin  ~  year end 
						double actualBeforeGC = LeaveEntitleHelper.calActualAnnualLeaveDays(beginDateStr, endDateStr, leaveBalanceCommonInforVO.getOnBoardDate(), dateFormat, gradeHistory.getNewGrade());
						totalEntitleDays += actualBeforeGC;
					}
					// The work experience was changed
					else{
						
						/*
						 * Here to work out the leave entitlement between year begin  ~ today
						 */
						String oneDateBeforeWEC = StringUtil.afterNDay(-1, dateFormat, experienceChangeDateStr);
						double actualBeforeWEC = LeaveEntitleHelper.calActualAnnualLeaveDays(beginDateStr, oneDateBeforeWEC, leaveBalanceCommonInforVO.getOnBoardDate(), dateFormat, gradeHistory.getNewGrade());
						totalEntitleDays += actualBeforeWEC;

						/*
						 * Here to work out the leave entitlement between today ~ year end
						 */
						double actualAfterWEC = LeaveEntitleHelper.calActualAnnualLeaveDays(oneDateBeforeWEC, endDateStr, leaveBalanceCommonInforVO.getOnBoardDate(), dateFormat, gradeHistory.getNewGrade());
						totalEntitleDays += actualAfterWEC;						
					}
				}

				totalEntitleDays = LeaveEntitleHelper.RoundDownDays(totalEntitleDays);
		
				// --Get the leave information of the staff of current year
				LeaveBalanceDAO leaveBalanceDAO = new LeaveBalanceDAO(dbManager);
				LeaveBalanceVO vo = new LeaveBalanceVO();
				vo.setStaffCode(leaveBalanceCommonInforVO.getStaffCode());
				vo.setYear(Integer.parseInt(currentYear));
				LeaveBalanceVO leaveBalanceCurrentYear = leaveBalanceDAO.getBalance(vo);
				
				//--no record of current year, then generate a new one.
				if(leaveBalanceCurrentYear == null){
					//以新一年最后一天为总工作天数计算法定假
					int daysBetween = LeaveEntitleHelper.daysBetweenDateStr(todayStr, endDateStr, "MM/dd/yyyy");					
					double annualStatutoryEntitle = LeaveEntitleHelper.getStatutoryAnnualEntitle(totalWorkDays + daysBetween);
					
					leaveBalanceCurrentYear = LeaveEntitleHelper.generateLeaveBalanceRecord(leaveBalanceCommonInforVO.getStaffCode(), leaveBalanceCommonInforVO.getOnBoardDate(),todayStr, dateFormat, totalEntitleDays, annualStatutoryEntitle);
				}//-- update the current one
				else {
					leaveBalanceCurrentYear = LeaveEntitleHelper.updateLeaveBalanceRecord(leaveBalanceCurrentYear, totalEntitleDays);
				}
				
				LeaveBalanceCommonInforVO result = new LeaveBalanceCommonInforVO();
				result.clone(leaveBalanceCommonInforVO);
				result.setLeaveBasicData(leaveBalanceCurrentYear);
				
				//如果当年法定年休假大于总年休假, 不更新.
				//手动升级时仍然更新计算结果
				if(leaveBalanceCurrentYear.getAnnualStatutoryEntitleDays() > leaveBalanceCurrentYear.getAnnualTotalEntitleDays()
				&& !manual){
					result.setIndct1("0");
				}else{
					result.setIndct1("1");
					updateList.add(result);
				}
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

	public static Collection UpdateStaffAnnualStatutory(Collection staffCodeList, String todayStr) throws Exception {
		IDBManager dbManager = null;
		try {
            Collection emailList = new Vector();
            Collection updateList = new Vector();
			Iterator it = staffCodeList.iterator();	
			while (it.hasNext()) {
				
				LeaveBalanceCommonInforVO leaveBalanceCommonInforVO = (LeaveBalanceCommonInforVO) it.next();
				String currentYear = todayStr.substring(6, 10);
				
				// --Get the leave information of the staff of current year
				LeaveBalanceVO leaveBalanceCurrentYear =null;
                try {
                    dbManager = DBManagerFactory.getDBManager();
                    LeaveBalanceDAO leaveBalanceDAO = new LeaveBalanceDAO(dbManager);
                    LeaveBalanceVO vo = new LeaveBalanceVO();
    				vo.setStaffCode(leaveBalanceCommonInforVO.getStaffCode());
    				vo.setYear(Integer.parseInt(currentYear));
    				leaveBalanceCurrentYear = leaveBalanceDAO.getBalance(vo);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return null;
                } finally {
                    if (dbManager != null) {
                        dbManager.freeConnection();
                    }
                }				
				//--no record of current year
				if(leaveBalanceCurrentYear == null){
	                continue;
				}else{
					//以新一年最后一天为总工作天数计算法定假
					String endDateStr = "12/31/" + currentYear;
					int daysBetween = LeaveEntitleHelper.daysBetweenDateStr(todayStr, endDateStr, "MM/dd/yyyy");
					int totalWorkDays = leaveBalanceCommonInforVO.getTotalWorkDays()+daysBetween;	
					
					double annualStatutoryEntitle = LeaveEntitleHelper.getStatutoryAnnualEntitle(totalWorkDays);
					
					if(annualStatutoryEntitle != leaveBalanceCurrentYear.getAnnualStatutoryEntitleDays()){
						//更新法定年休假
						leaveBalanceCurrentYear.setAnnualStatutoryEntitleDays(annualStatutoryEntitle);
						LeaveBalanceCommonInforVO result = new LeaveBalanceCommonInforVO();
						result.clone(leaveBalanceCommonInforVO);
						result.setLeaveBasicData(leaveBalanceCurrentYear);	
								
						//如果当年法定年休假大于总年休假, 不更新.
						if(leaveBalanceCurrentYear.getAnnualStatutoryEntitleDays() > leaveBalanceCurrentYear.getAnnualTotalEntitleDays()){
							result.setIndct1("0");							
						}					
						else{
							//更新公司年休假
							result.getLeaveBasicData().setAnnualCompanyEntitleDays(leaveBalanceCurrentYear.getAnnualTotalEntitleDays()
									-leaveBalanceCurrentYear.getAnnualStatutoryEntitleDays());
							result.setIndct1("1");
							updateList.add(result);
						}				
						emailList.add(result);															
					}
				}
			}
			// save all new records
			if(updateList.size()>0 ){
				saveBalanceRecord(updateList);
			}	
			
			return emailList;
			
		} catch (Exception ex) {
			throw ex;
		} finally {
			//
		}
	}
	
	public static void generateNewYearBalance(Collection staffCodeList, String todayStr) throws Exception {

		IDBManager dbManager = null;

		try {
            Collection updateList = new Vector();
			Iterator it = staffCodeList.iterator();	
			while (it.hasNext()) {
				
				LeaveBalanceCommonInforVO leaveBalanceCommonInforVO = (LeaveBalanceCommonInforVO) it.next();
				String dateFormat = "MM/dd/yyyy";
				String currentYear = todayStr.substring(6, 10);
				
				// --Get the leave information of the staff of current year
				LeaveBalanceVO leaveBalanceCurrentYear =null;
                try {
                    dbManager = DBManagerFactory.getDBManager();
                    LeaveBalanceDAO leaveBalanceDAO = new LeaveBalanceDAO(dbManager);
                    LeaveBalanceVO vo = new LeaveBalanceVO();
    				vo.setStaffCode(leaveBalanceCommonInforVO.getStaffCode());
    				vo.setYear(Integer.parseInt(currentYear));
    				leaveBalanceCurrentYear = leaveBalanceDAO.getBalance(vo);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    throw ex;
                } finally {
                    if (dbManager != null) {
                        dbManager.freeConnection();
                    }
                }
				
				//--no record of current year, then generate a new one.
				if(leaveBalanceCurrentYear == null){
					String yearBeginStr = "01/01/" + currentYear;
					int AIAITWorkDays = leaveBalanceCommonInforVO.getCompWorkDays();
					int totalWorkDays = leaveBalanceCommonInforVO.getTotalWorkDays();
					double totalAnnualEntitle =(double)LeaveEntitleHelper.getBasicTotalAnnualEntitle(AIAITWorkDays, leaveBalanceCommonInforVO.getNewGrade());
					
					leaveBalanceCurrentYear = LeaveEntitleHelper.generateLeaveBalanceRecord(leaveBalanceCommonInforVO.getStaffCode(), 
							leaveBalanceCommonInforVO.getOnBoardDate(),yearBeginStr, dateFormat, totalAnnualEntitle, 0);

					String lastYear = "" + (Integer.parseInt(currentYear) - 1);
																	
					// --Get the leave information of the staff of last year
					LeaveBalanceVO leaveBalanceLastYear = null;
	                try {
	                    dbManager = DBManagerFactory.getDBManager();
	                    LeaveBalanceDAO leaveBalanceDAO = new LeaveBalanceDAO(dbManager);
	                    LeaveBalanceVO vo = new LeaveBalanceVO();
	    				vo.setStaffCode(leaveBalanceCommonInforVO.getStaffCode());
	    				vo.setYear(Integer.parseInt(lastYear));
	    				leaveBalanceLastYear = leaveBalanceDAO.getBalance(vo);
	                } catch (Exception ex) {
	                    ex.printStackTrace();
	                    throw ex;
	                } finally {
	                    if (dbManager != null) {
	                        dbManager.freeConnection();
	                    }
	                }					
					
					if(leaveBalanceLastYear != null){
						//--update the current year carry forward days as the balance left from last year
						double annualCarryForwardDays = leaveBalanceLastYear.getAnnualBalanceDays();
						leaveBalanceCurrentYear.setAnnualCarryForwardDays(annualCarryForwardDays);
						
						double annualStatutoryEntitle = leaveBalanceLastYear.getAnnualStatutoryEntitleDays();
						if(annualStatutoryEntitle > leaveBalanceCurrentYear.getAnnualTotalEntitleDays()){
							annualStatutoryEntitle = leaveBalanceCurrentYear.getAnnualTotalEntitleDays();
						}
						leaveBalanceCurrentYear.setAnnualStatutoryEntitleDays(annualStatutoryEntitle);
						
						//更新公司年休假
						double annualCompanyEntitle = leaveBalanceCurrentYear.getAnnualTotalEntitleDays()-leaveBalanceCurrentYear.getAnnualStatutoryEntitleDays();							
						leaveBalanceCurrentYear.setAnnualCompanyEntitleDays(annualCompanyEntitle);
					}
										
					
					LeaveBalanceCommonInforVO result = new LeaveBalanceCommonInforVO();
					result.clone(leaveBalanceCommonInforVO);
					result.setLeaveBasicData(leaveBalanceCurrentYear);
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
	
	/**
	 * Save all updated balance records into DB 
	 * 
	 * <li> staffCodeList:  new balance info record of staffList
	 * 
	 */	
	
	protected static void saveBalanceRecord(Collection staffCodeList)throws Exception{

		IDBManager dbManager = null;
		try {
			dbManager = DBManagerFactory.getDBManager();
			dbManager.startTransaction();
			// For leave records
			LeaveBalanceDAO leaveBalanceDAO = new LeaveBalanceDAO(dbManager);
			Iterator it = staffCodeList.iterator();
			while (it.hasNext()) {
				LeaveBalanceCommonInforVO leaveBalanceCommonInforVO = (LeaveBalanceCommonInforVO) it.next();
				leaveBalanceDAO.saveBalance(leaveBalanceCommonInforVO.getLeaveBasicData());
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
				leaveBalanceCommonInforDAO.updateGradeIndicator(leaveBalanceCommonInforVO, "leave");
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
	 * Send email notifying leave balance change to staff and HR if update is successful
	 * 
	 * <li> staffList:  list of staff common information
	 * <li> leaveBalanceMap:  new balance record of staffList
	 * <li> type:  
	 * <li> 0-- not send email,  
	 * <li> 1-- send email for grade change,  
	 * <li> 2-- send email for experience change 
	 * <li> 3-- send email for statutory entitlement change
	 * 
	 */	
	
	public static void emailSuccessUpdate(Collection staffList, String type) throws Exception {
		if(type == "0"){
			return;
		}
		try {
			String emailSubject = "Leave entitlement renew";
			String currentDate = StringUtil.getCurrentDateStr("MM/dd/yyyy");
			String emailContent = "";	
			StringBuffer emailContentToHR = new StringBuffer("");
			String emailAccountHRStr = ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_LEAVE_HR_EMAIL);
			String emailAccountHR[] ={""};
			if(emailAccountHRStr!=null && !"".equals(emailAccountHRStr)){
		       emailAccountHR=emailAccountHRStr.split(";");
			}
			
			if("1".equals(type)){
				emailContentToHR.append("<br>The <font size=+1><strong>annual leave</strong></font > entitlement of below staffs were updated on "+currentDate+" due to their grades change: <br>");
				emailContentToHR.append("<TABLE BORDER=1>");
				emailContentToHR.append("<tr><TH>Staff code</TH><TH>Staff name</TH><TH>Total entitlement</TH><TH>Statutory entitlement</TH><TH>Company entitlement</TH><TH>Old grade</TH><TH>New grade</TH>");
			}else if("2".equals(type)){
				emailContentToHR.append("<br>The <font size=+1><strong>annual leave</strong></font > entitlement of below staffs were updated on "+currentDate+" due to their AIAIT work experience change: <br>");
				emailContentToHR.append("<TABLE BORDER=1>");
				emailContentToHR.append("<tr><TH>Staff code</TH><TH>Staff name</TH><TH>Total entitlement</TH><TH>Statutory entitlement</TH><TH>Company entitlement</TH><TH>AIAIT Work experience(year)</TH>");
			}else if("3".equals(type)){
				emailContentToHR.append("<br>The <font size=+1><strong>statutory leave</strong></font > entitlement of below staffs were updated on "+currentDate+" due to their total work experience change: <br>");
				emailContentToHR.append("<TABLE BORDER=1>");
				emailContentToHR.append("<tr><TH>Staff code</TH><TH>Staff name</TH><TH>Total entitlement</TH><TH>Statutory entitlement</TH><TH>Company entitlement</TH><TH>AIAIT Work experience(year)</TH><TH>Total Work experience(year)</TH>");
			}
			
			emailContentToHR.append("<TH>Updated</TH></tr>");
			
			Iterator it = staffList.iterator();
			while(it.hasNext()){
				LeaveBalanceCommonInforVO staff = (LeaveBalanceCommonInforVO) it.next();
				LeaveBalanceVO leaveBalance = staff.getLeaveBasicData();
				
				emailContent = "<br>Please be informed that your annual leave entitlement of "+leaveBalance.getYear()+" was updated: "
					         + "<br>Total annual entitlement: <font color='red'>" + leaveBalance.getAnnualTotalEntitleDays() + "</font> "
					         + "<br>Statutory annual entitlement: <font color='red'>" + leaveBalance.getAnnualStatutoryEntitleDays() + "</font> "
					         + "<br>Company annual entitlement: <font color='red'>" + leaveBalance.getAnnualCompanyEntitleDays() + "</font> "
					         + "<br>"
					         + "<br>The change took effect on "+staff.getGradeEffectDate()+" due to your ";
				
				//String staffName = StaffTeamHelper.getInstance().getStaffByCode(staff.getStaffCode()).getStaffName();
				
				String staffName = StaffTeamHelper.getInstance().getStaffByCode(staff.getStaffCode()) ==null ? staff.getStaffCode()
						:StaffTeamHelper.getInstance().getStaffByCode(staff.getStaffCode()).getStaffName();
				
				emailContentToHR.append("<tr><td>"+staff.getStaffCode()+"</td>" +
						"<td>"+staffName+"</td>" +
						"<td align='right'>"+leaveBalance.getAnnualTotalEntitleDays()+"</td>" +
						"<td align='right'>"+leaveBalance.getAnnualStatutoryEntitleDays()+"</td>" +
						"<td align='right'>"+leaveBalance.getAnnualCompanyEntitleDays()+"</td>");
				
				if("1".equals(type)){
					emailContent = emailContent + "grade was promoted from "+staff.getOldGrade()+" to "+staff.getNewGrade();
					emailContentToHR.append("<td align='right'>"+staff.getOldGrade()+"</td><td align='right'>"+staff.getNewGrade()+"</td>");
				}else if("2".equals(type)){
                    int compYear = (int)staff.getCompWorkDays()/365;
					emailContent = emailContent + "total work experience in AIAIT is over " +compYear+ " years";					
					emailContentToHR.append("<td align='right'>"+compYear+"</td>");
				}else if("3".equals(type)){
                    int compYear = (int)staff.getCompWorkDays()/365;
					emailContent = emailContent + "total work experience in AIAIT is over " +compYear+ " years, ";					
					emailContentToHR.append("<td align='right'>"+compYear+"</td>");
					
                    int totalYear = (int)staff.getTotalWorkDays()/365;
					emailContent = emailContent + "total work experience is over " +totalYear+ " years";					
					emailContentToHR.append("<td align='right'>"+totalYear+"</td>");
				}
				
				
				if(staff.getIndct1()!=null && !"0".equals(staff.getIndct1())){
					emailContentToHR.append("<td>Yes</td></tr>");
					
					//只有总年休假更新时发email通知员工
					if("1".equals(type) || "2".equals(type)){
						emailContent += "<br>"
					                 +"<br>Please contact us if any query, thanks!";			
				        EFlowEmailUtil.sendEmail(emailSubject, emailContent, new String[]{ staff.getStaffCode()});
					}
				}
				//如果当年法定年休假大于总年休假, 发email让HR手动升级
				else{
					emailContentToHR.append("<td><font color='red'>No, please update it manual</font></td></tr>");
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
	 * Send email notifying leave balance change to HR if update is failed
	 * 
	 * <li> staffList:  list of staff common information
	 * <li> leaveBalanceMap:  new balance record of staffList
	 * <li> type:  
	 * <li> 0-- send email for yearly new balances update,  
	 * <li> 1-- send email for grade change,  
	 * <li> 2-- send email for experience change 
	 * <li> 3-- send email for statutory entitlement change
	 * 
	 */	
	
	public static void emailFaildUpdate(Collection staffList, String type, Exception reason) throws Exception {

		try {
			String emailSubject = "Leave entitlement failed";
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
				emailContentToHR.append("<br>The annual leave entitlement of below staffs were updated on "+currentDate+"due to their grades change: <br>");
			}else if("2".equals(type)){
				emailContentToHR.append("<br>The annual leave entitlement of below staffs were updated on "+currentDate+"due to their AIAIT work experience change: <br>");
			}else if("3".equals(type)){
				emailContentToHR.append("<br>The statutory leave entitlement of below staffs were updated on "+currentDate+"due to their total work experience change: <br>");
			}else if("0".equals(type)){
				emailContentToHR.append("<br>The annual leave entitlement of all staffs for their new year's record were failed to update <br>");
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
        IDBManager dbManager = null;
        response.setContentType("text/html;charset=GB2312"); //it is very important
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");
		PrintWriter out = response.getWriter();
        try {
            dbManager = DBManagerFactory.getDBManager();
            LeaveBalanceCommonInforDAO leaveBalanceCommonInforDAO = new LeaveBalanceCommonInforDAO(dbManager);
            String todayStr = StringUtil.getDateStr(new Date(), "MM/dd/yyyy");
            String currentYear = todayStr.substring(6, 10);
			String yearBeginStr = "01/01/" + currentYear;
            Collection staffList = leaveBalanceCommonInforDAO.getAllStaffBalanceCommonInfor(yearBeginStr);	
            Collection staffEmailList = null;
            
            if (staffList.size() > 0) {			
            	//Update the new year balance first.
				LeaveBalanceAction.generateNewYearBalance(staffList,yearBeginStr);
				//Then update the statutory
				staffEmailList = LeaveBalanceAction.UpdateStaffAnnualStatutory(staffList, yearBeginStr);
			}
            
            out.print("success");
            
            
        } catch (Exception ex) {
        	out.print("failed to manual update");
            ex.printStackTrace();
        } finally {
            if (dbManager != null) {
                dbManager.freeConnection();
            }
            if(out!=null) {
            	out.close();
            }
        }                
        return null;
    }
    
    public ActionLocation manualStatutoryEntitle(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {   	
        IDBManager dbManager = null;
        response.setContentType("text/html;charset=GB2312"); //it is very important
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");
		PrintWriter out = response.getWriter();
        try {
            dbManager = DBManagerFactory.getDBManager();
            LeaveBalanceCommonInforDAO leaveBalanceCommonInforDAO = new LeaveBalanceCommonInforDAO(dbManager);
            String todayStr = StringUtil.getDateStr(new Date(), "MM/dd/yyyy");
            String currentYear = todayStr.substring(6, 10);
			String yearBeginStr = "01/01/" + currentYear;
            Collection staffList = leaveBalanceCommonInforDAO.getAllStaffBalanceCommonInfor(yearBeginStr);	
            
            if (staffList.size() > 0) {													
            	Collection emailList = LeaveBalanceAction.UpdateStaffAnnualStatutory(staffList,yearBeginStr);          	
				try {
					LeaveBalanceAction.emailSuccessUpdate(emailList, "3");
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
                     
            out.print("success");
            
            
        } catch (Exception ex) {
        	out.print("failed to manual update");
            ex.printStackTrace();
        } finally {
            if (dbManager != null) {
                dbManager.freeConnection();
            }
            if(out!=null) {
            	out.close();
            }
        }                
        return null;
    }
	
	private void manualAnnualEntitle(LeaveBalanceCommonInforVO leaveBalanceCommonInfor)throws Exception{
    	try {
    		String dateFormat = "MM/dd/yyyy";
    		Date today = new Date();
    		String todayStr = StringUtil.getDateStr(today, dateFormat);

    		int preWorkDays = (int) (leaveBalanceCommonInfor.getPreWorkExperience() * 365);
    		int AIAITWorkDays = LeaveEntitleHelper.daysBetweenDateStr(leaveBalanceCommonInfor.getOnBoardDate(), todayStr, dateFormat);
    		int totalWorkDays = preWorkDays + AIAITWorkDays;   		
    		leaveBalanceCommonInfor.setCompWorkDays(AIAITWorkDays);
    		leaveBalanceCommonInfor.setTotalWorkDays(totalWorkDays);
 
        	Collection staff = new Vector();
        	staff.add(leaveBalanceCommonInfor);
			Collection emailList = UpdateStaffAnnualEntitleForGradeChange(staff, true);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
