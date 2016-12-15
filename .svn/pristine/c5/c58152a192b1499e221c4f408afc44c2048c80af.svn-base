package com.aiait.eflow.housekeeping.action;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.aiait.eflow.common.CommonName;
import com.aiait.eflow.common.helper.CompanyHelper;
import com.aiait.eflow.common.helper.ParamConfigHelper;
import com.aiait.eflow.housekeeping.dao.FinanceBudgetDAO;
import com.aiait.eflow.housekeeping.dao.TeamDAO;
import com.aiait.eflow.housekeeping.vo.FinanceBudgetVO;
import com.aiait.eflow.housekeeping.vo.StaffVO;
import com.aiait.eflow.util.DataConvertUtil;
import com.aiait.eflow.util.ExcelFileUtil;
import com.aiait.eflow.util.FileUtil;
import com.aiait.eflow.util.MultipartRequest;
import com.aiait.eflow.util.StringUtil;
import com.aiait.framework.common.GlobalCommonName;
import com.aiait.framework.db.DBManagerFactory;
import com.aiait.framework.db.IDBManager;
import com.aiait.framework.mvc.action.ActionLocation;
import com.aiait.framework.mvc.action.DispatchAction;
import com.aiait.framework.mvc.action.ModuleMapping;
import com.aiait.framework.mvc.form.WebForm;
import com.aiait.framework.page.PageUtil;
import com.aiait.framework.page.PageVO;

public class FinanceBudgetAction extends DispatchAction {
	
	/**
	 * 进入到选择上传excel文件的页面
	 * 
	 * @param mapping
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionLocation showExcelTemplateSelect(
			ModuleMapping mapping,
			WebForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		String returnLabel = "excelFileSelectPage";
		return mapping.findActionLocation(returnLabel);
	}
	
	public ActionLocation importExcelBudget(
			ModuleMapping mapping,
			WebForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		String returnLabel = "tempsuccess";
		String byType = (String)request.getParameter("byType"); // 01 ---按年； 02 --- 按月
		if(byType==null){
			byType="01";
		}
		StaffVO currentStaff = (StaffVO) request.getSession().getAttribute(
				CommonName.CURRENT_STAFF_INFOR);
		String userId = currentStaff.getLogonId();
		String fileName = "";
		SimpleDateFormat bartDateFormat = new SimpleDateFormat(
		"yyyyMMddHHmmdd");
        Date date = new Date();
        String dateName = bartDateFormat.format(date);
        HttpServletRequest multiRequest = null;
        IDBManager dbManager = null;
        try {
		  if (request.getMethod().equals("POST")
				&& MultipartRequest.isMultipart(request)) {
		    multiRequest = new MultipartRequest(request, userId);//
			File upFile = ((MultipartRequest) multiRequest).getFile("path");
		    // java.io.InputStream stream = upFile.get();//把文件读入
			if (upFile != null) {
				fileName = dateName + "_"
						+ ((MultipartRequest) multiRequest).getFileName(upFile);
				fileName = fileName.replaceAll("'","");
				//FileUtil.saveAs(upFile, Global.WEB_ROOT_PATH + "/"
						//+ filePathName);
			   FileUtil.saveAs(upFile, ParamConfigHelper.getInstance().getParamValue("upload_file_dir") + "/"
								+ fileName);		
			}
			//fileName = DataConvertUtil.convertGBKToISO(fileName);
			//开始解析excel文件的数据
			ExcelFileUtil excelUtil = new ExcelFileUtil(ParamConfigHelper.getInstance().getParamValue("upload_file_dir") + "/"
					+ fileName);
			
			Collection dataList = null;
			if("01".equals(byType)){//annually
			   dataList = excelUtil.parseAnnuallyBudget();
			}else if("02".equals(byType)){//monthly
			   dataList = excelUtil.parseMonthlyBudget();
			}
			if(dataList!=null){
				saveExcelData(dataList,byType);
			}
		  }
        }catch(Exception e){
			e.printStackTrace();
			request.setAttribute(CommonName.COMMON_ERROR_INFOR,e.toString());
			returnLabel = "fail" ;
		}finally{
        	if (multiRequest!=null && multiRequest instanceof MultipartRequest) {
				// delete the temp file
				((MultipartRequest) multiRequest).deleteTemporaryFile();
			}
        	if(fileName!=null && !"".equals(fileName)){
        		FileUtil.deleteFile(ParamConfigHelper.getInstance().getParamValue("upload_file_dir") + "/"
    					+fileName);
        	}
        	
        }
        return mapping.findActionLocation(returnLabel);
	}
	
	private void saveExcelData(Collection dataList, String byType)
			throws Exception {
		IDBManager dbManager = null;
		try {
			dbManager = DBManagerFactory.getDBManager();
			FinanceBudgetDAO dao = new FinanceBudgetDAO(dbManager);
			dbManager.startTransaction();
			Iterator it = dataList.iterator();
			while (it.hasNext()) {
				FinanceBudgetVO vo = (FinanceBudgetVO) it.next();
				if ("01".equals(byType)) {// annually
					if (dao.checkRecordExist(vo) == false) { // 如果不存在，则insert数据库
						dao.annualSave(vo);
					} else { // 否则采用update数据库
						dao.annualUpdate(vo);
					}
				} else if ("02".equals(byType)) {// monthly
					double fullYearBudget = 0;
					fullYearBudget = vo.getAdjustFullYearBudget();
					/**
					if (fullYearBudget == 0) {
						FinanceBudgetVO oldVo = dao.getVO(vo);
						if (oldVo != null) {
							fullYearBudget = oldVo.getAdjustFullYearBudget();
							if (fullYearBudget == 0) {
								fullYearBudget = oldVo.getFullYearBudget();
							}
						}
					}*/
					//if (fullYearBudget != 0) {
						vo.setBanlance(fullYearBudget - vo.getYtnActualExpense());
						dao.monthlyUpdate(vo);
					//}
				}
			}
			dbManager.commit();
		} catch (Exception e) {
			if (dbManager != null)
				dbManager.rollback();
			e.printStackTrace();
			throw new Exception("Fail to sava excel data:" + e.toString());
		} finally {
			if (dbManager != null)
				dbManager.freeConnection();
		}
	}
   
	public ActionLocation search(
			ModuleMapping mapping,
			WebForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		String returnLabel = "listPage";
		HashMap paramMap = new HashMap();
		if(form.get("orgId")!=null && !"".equals("orgId")){
			paramMap.put("orgId",form.get("orgId"));
		}
		if(form.get("team_code")!=null && !"".equals("team_code")){
			paramMap.put("departmentId",form.get("team_code"));
		}		
		if(form.get("categoryId")!=null && !"".equals("categoryId")){
			paramMap.put("categoryId",form.get("categoryId"));
		}		
		
		StaffVO currentStaff = (StaffVO)request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
		//获取其所有下级公司（包括本公司）
		//String teamId = (String)request.getParameter("team_code");
		String orgIdfrmpage = (String)request.getParameter("orgId");
		String lowerOrgIds ="";
		if (orgIdfrmpage==null || "".equals(orgIdfrmpage)){
			lowerOrgIds = currentStaff.getLowerCompanys(); //CompanyHelper.getInstance().getLowerCompany(currentStaff.getOrgId());
		}else{			
			orgIdfrmpage = "'"+orgIdfrmpage+"'";
			lowerOrgIds = orgIdfrmpage;
		}
		
        //分页查询
		String pagenum = (String)request.getParameter(GlobalCommonName.PAGE_CURRENT_NUM);
		if(pagenum==null){
			pagenum = "1";
		}
		PageVO page = new PageVO(Integer.parseInt(ParamConfigHelper.getInstance().getParamValue(CommonName.EVERY_PAGE_RECORDS_NUM,CommonName.DEFAULT_RECORDS_NUM)));
		page.setParamMap(paramMap);
		page.setCurrentPage(Integer.parseInt(pagenum));
		
		IDBManager dbManager = null;
		try{
			dbManager =  DBManagerFactory.getDBManager();
			FinanceBudgetDAO dao = new FinanceBudgetDAO(dbManager);
			int totalRecordsNum = dao.getTotalRecordNum(page);
			page = PageUtil.createPage(page,totalRecordsNum);
			Collection list = dao.search(page);
			request.setAttribute("resultList",list);
			request.setAttribute(GlobalCommonName.PAGE_INFORMATION_NAME,page);
            //获取所有的Team列表
			Collection TeamList = null;
			TeamDAO tdao = new TeamDAO(dbManager);
			//TeamList = tdao.getMergedTeamList();
			TeamList = tdao.getTeamList(lowerOrgIds);
			request.setAttribute("teamList",TeamList);
		}catch(Exception e){
			e.printStackTrace();
			request.setAttribute(CommonName.COMMON_ERROR_INFOR,e.toString());
			returnLabel = "fail" ;
		}finally{
			if(dbManager!=null) dbManager.freeConnection();
		}
		return mapping.findActionLocation(returnLabel);
	}
	
	public ActionLocation viewPage(
			ModuleMapping mapping,
			WebForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		String returnLabel = "editPage";
         
		IDBManager dbManager = null;
		try{
			dbManager =  DBManagerFactory.getDBManager();
			FinanceBudgetDAO dao = new FinanceBudgetDAO(dbManager);
			FinanceBudgetVO conditionVo = new FinanceBudgetVO();
			conditionVo.setOrgId((String)form.get("orgId"));
			conditionVo.setDepartmentId((String)form.get("departmentId"));
			conditionVo.setCategoryId((String)form.get("categoryId"));
			conditionVo.setSubCategoryId((String)form.get("subCategoryId"));
			FinanceBudgetVO vo = dao.getVO(conditionVo);
			request.setAttribute("vo",vo);
		}catch(Exception e){
			e.printStackTrace();
			request.setAttribute(CommonName.COMMON_ERROR_INFOR,e.toString());
			returnLabel = "fail" ;
		}finally{
			if(dbManager!=null) dbManager.freeConnection();
		}
		return mapping.findActionLocation(returnLabel);
	}
	
	public ActionLocation save(
			ModuleMapping mapping,
			WebForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		String returnLabel = "saveSuccess";
         
		IDBManager dbManager = null;
		try{
			dbManager =  DBManagerFactory.getDBManager();
			FinanceBudgetDAO dao = new FinanceBudgetDAO(dbManager);
			FinanceBudgetVO saveVo = new FinanceBudgetVO();
			saveVo.setCurrentMonth((String)form.get("currentMonth"));
			saveVo.setYtnBudget(Double.parseDouble(""+form.get("ytdBudget")));
			saveVo.setAdjustFullYearBudget(Double.parseDouble(""+form.get("adjustfullYearBudget")));
			saveVo.setYtnActualExpense(Double.parseDouble(""+form.get("actualExpense")));
			saveVo.setBanlance(Double.parseDouble(""+form.get("balance")));
			saveVo.setOrgId((String)form.get("orgId"));
			saveVo.setDepartmentId((String)form.get("departmentId"));
			saveVo.setCategoryId((String)form.get("categoryId"));
			saveVo.setSubCategoryId((String)form.get("subCategoryId"));
			saveVo.setBudgetYear((String)form.get("budgetYear"));
			dao.monthlyUpdate(saveVo);
		    dbManager.commit();
		}catch(Exception e){
			e.printStackTrace();
			request.setAttribute(CommonName.COMMON_ERROR_INFOR,e.toString());
			returnLabel = "fail" ;
		}finally{
			if(dbManager!=null) dbManager.freeConnection();
		}
		return mapping.findActionLocation(returnLabel);
	}
	
	public ActionLocation delete(
			ModuleMapping mapping,
			WebForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		String returnLabel = "saveSuccess";
		String[] compoundValue = request.getParameterValues("compoundValue");
		if(compoundValue==null) return mapping.findActionLocation(returnLabel);
		IDBManager dbManager = null;
		try{
			dbManager =  DBManagerFactory.getDBManager();
			FinanceBudgetDAO dao = new FinanceBudgetDAO(dbManager);
			FinanceBudgetVO vo = new FinanceBudgetVO();
			dbManager.startTransaction();
		    for(int i=0;i<compoundValue.length;i++){
			  String[] temp = StringUtil.split(compoundValue[i],"||"); //orgId||departmentId||categoryId||subCategoryId
			   vo.setOrgId(temp[0]);
			   vo.setDepartmentId(temp[1]);
			   vo.setCategoryId(temp[2]);
			   vo.setSubCategoryId(temp[3]);
			   dao.delete(vo);
		    }
		    dbManager.commit();
		}catch(Exception e){
			dbManager.rollback();
			e.printStackTrace();
			request.setAttribute(CommonName.COMMON_ERROR_INFOR,e.toString());
			returnLabel = "fail" ;
		}finally{
			if(dbManager!=null) dbManager.freeConnection();
		}    
		return mapping.findActionLocation(returnLabel);
	}
}
