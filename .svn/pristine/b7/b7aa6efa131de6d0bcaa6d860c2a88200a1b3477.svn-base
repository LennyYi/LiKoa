package com.aiait.eflow.housekeeping.action;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.http.*;

import com.aiait.eflow.common.CommonName;
import com.aiait.eflow.common.helper.ParamConfigHelper;
import com.aiait.eflow.housekeeping.dao.StaffDAO;
import com.aiait.eflow.housekeeping.dao.SupplierDAO;
import com.aiait.eflow.housekeeping.dao.TeamDAO;
import com.aiait.eflow.housekeeping.vo.SupplierVO;
import com.aiait.eflow.housekeeping.vo.StaffVO;
import com.aiait.eflow.util.ExcelFileUtil;
import com.aiait.eflow.util.FileUtil;
import com.aiait.eflow.util.MultipartRequest;
import com.aiait.framework.common.GlobalCommonName;
import com.aiait.framework.db.*;
import com.aiait.framework.exception.DAOException;
import com.aiait.framework.mvc.action.*;
import com.aiait.framework.mvc.form.WebForm;
import com.aiait.framework.page.PageUtil;
import com.aiait.framework.page.PageVO;
import com.aiait.framework.util.CommonUtil;

public class SupplierAction extends DispatchAction {

    public ActionLocation listSupplier(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String returnLabel = "listSupplier";
        IDBManager dbManager = null;
        if(request.getParameter("editType")==null) return mapping.findActionLocation(returnLabel);
        
        try {
            dbManager = DBManagerFactory.getDBManager();
            SupplierDAO dao = new SupplierDAO(dbManager);
            SupplierVO vo = new SupplierVO();
            
            if(request.getParameter("editType").equals("search")){//新查询
	    		//StaffVO currentStaff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
	            //vo.setOrgId(request.getParameter("orgId"));
            	vo.setCertClass(request.getParameter("class"));
	            vo.setType(request.getParameter("type"));
	            vo.setNameC(request.getParameter("name_c"));
	            vo.setProduct(request.getParameter("product"));
	            try{
	            	vo.setTeamCode(Integer.parseInt(request.getParameter("team_code")));
	            }catch(Exception e){
	            	vo.setTeamCode(0);
	            }
	            vo.setStatus(request.getParameter("status"));
	            request.getSession().setAttribute("searchvo", vo);
	            
            }else if(request.getParameter("editType").equals("navi")){//翻页
            	vo = (SupplierVO) request.getSession().getAttribute("searchvo");             	
            }
            
            // 分页查询begin
			String pagenum = (String) request.getParameter(GlobalCommonName.PAGE_CURRENT_NUM);
			if (pagenum == null) {
				pagenum = "1";
			}
			PageVO page = new PageVO(Integer.parseInt(ParamConfigHelper.getInstance().getParamValue(
					CommonName.EVERY_PAGE_RECORDS_NUM, CommonName.DEFAULT_RECORDS_NUM)));
			//page.setParamMap(paramMap);
			page.setCurrentPage(Integer.parseInt(pagenum));
			int totalRecordsNum = dao.getTotalRecordsNum(vo, page);
			page = PageUtil.createPage(page, totalRecordsNum);
			Collection staffList = dao.getSupplierList(vo, page);
			request.setAttribute("supplierList", staffList);
			request.setAttribute(GlobalCommonName.PAGE_INFORMATION_NAME, page);
			// 分页查询end
			
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", e.getMessage());
            returnLabel = "fail";
        } finally {
            if (dbManager != null)
                dbManager.freeConnection();
        }
        return mapping.findActionLocation(returnLabel);
    }

    public ActionLocation editSupplier(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String returnLabel = "editSupplier";
        String editType = request.getParameter("editType");
        String orgId = request.getParameter("orgId");
        String code = request.getParameter("code");
        
        if (editType != null && "new".equals(editType)) {
            return mapping.findActionLocation(returnLabel);
        }

        code = CommonUtil.decoderURL(code);
        
        SupplierVO vo = new SupplierVO();
        vo.setCode(code);
		vo.setOrgId(orgId);
       
        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            SupplierDAO dao = new SupplierDAO(dbManager);
            vo = dao.getSupplier(vo);
            request.setAttribute("vo", vo);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", e.getMessage());
            returnLabel = "fail";
        } finally {
            if (dbManager != null)
                dbManager.freeConnection();
        }
        return mapping.findActionLocation(returnLabel);
    }

    public ActionLocation saveSupplier(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String returnLabel = "";
        SupplierVO vo = new SupplierVO();
        
        vo.setCertClass       (request.getParameter("class"));
        vo.setCode            (request.getParameter("code"));
        vo.setType            (request.getParameter("type"));
        vo.setNameC           (request.getParameter("name_c"));            
        vo.setNameE           (request.getParameter("name_e"));            
        vo.setProduct         (request.getParameter("product"));     
        vo.setEffDate         (request.getParameter("eff_date"));
        vo.setContacter       (request.getParameter("contacter"));        
        vo.setTel             (request.getParameter("tel"));              
        vo.setFax             (request.getParameter("fax"));        
        vo.setAddressC        (request.getParameter("address_c"));         
        vo.setAddressE        (request.getParameter("address_e"));         
        vo.setEvaluateComments(request.getParameter("evaluate_comments")); 
        vo.setBank            (request.getParameter("bank"));             
        vo.setBankAccount     (request.getParameter("bank_account"));      
        vo.setProvince        (request.getParameter("province"));         
        vo.setCity            (request.getParameter("city"));    
		StaffVO currentStaff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
        //vo.setOrgId(currentStaff.getOrgId());   
		vo.setOrgId("Z07002"); //AIA SH
        vo.setTeamName        (request.getParameter("team_name"));        
        vo.setTeamContacter   (request.getParameter("team_contacter"));
        vo.setStatus          (request.getParameter("status"));
        vo.setTermDate        (request.getParameter("term_date"));
        
        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            SupplierDAO dao = new SupplierDAO(dbManager);
            dao.saveSupplier(vo);
            return this.listSupplier(mapping, request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", e.getMessage());
            returnLabel = "fail";
        } finally {
            if (dbManager != null)
                dbManager.freeConnection();
        }
        return mapping.findActionLocation(returnLabel);
    }

    public ActionLocation deleteSupplier(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String returnLabel = "";
        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            SupplierDAO dao = new SupplierDAO(dbManager);
            SupplierVO vo = new SupplierVO();
            for (int i = 0; i < request.getParameterValues("code").length; i++) {
                vo.setCode(request.getParameterValues("code")[i]);
        		StaffVO currentStaff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
                vo.setOrgId(currentStaff.getOrgId());
                dao.deleteSupplier(vo);
            }
            return this.listSupplier(mapping, request, response);
        } catch (Exception e) {
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
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		String returnLabel = "excelFileSelectPage";
		return mapping.findActionLocation(returnLabel);
	}
	
    public ActionLocation importExcelFile(
			ModuleMapping mapping,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		String returnLabel = "tempsuccess";
		StaffVO currentStaff = (StaffVO) request.getSession().getAttribute(
				CommonName.CURRENT_STAFF_INFOR);
		String userId = currentStaff.getLogonId();
		String fileName = "";
		SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyyMMddHHmmdd");
        Date date = new Date();
        String dateName = bartDateFormat.format(date);
        HttpServletRequest multiRequest = null;
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
			dataList = excelUtil.parseSupplier(currentStaff.getOrgId());
			
			if(dataList!=null){
				saveExcelData(dataList);
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
	
	private void saveExcelData(Collection dataList)
			throws Exception {
		IDBManager dbManager = null;
		int r = 1;
		try {
			dbManager = DBManagerFactory.getDBManager();
			SupplierDAO dao = new SupplierDAO(dbManager);
			dbManager.startTransaction();
			Iterator it = dataList.iterator();
			while (it.hasNext()) {
				SupplierVO vo = (SupplierVO) it.next();
				if (dao.getSupplier(vo) == null) { // 如果不存在，则insert数据库
					dao.addSupplier(vo);
				} else { // 否则采用update数据库
					dao.saveSupplier(vo);
				}
				r++;
			}
			dbManager.commit();
		} catch (Exception e) {
			if (dbManager != null)
				dbManager.rollback();
			e.printStackTrace();
			throw new Exception("第 "+(r+1)+" 行，Fail to save excel data:" + e.toString());
		} finally {
			if (dbManager != null)
				dbManager.freeConnection();
		}
	}
	
	// Export inquiry supplier data
	public ActionLocation exportInquirySupplier(
			ModuleMapping mapping,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String returnLabel = "exportInquirySupplier";

    	SupplierVO vo = (SupplierVO) request.getSession().getAttribute("searchvo");      

		//HashMap paramMap = new HashMap();

		IDBManager dbManager = null;
		try {
			dbManager = DBManagerFactory.getDBManager();
			SupplierDAO supplierDao = new SupplierDAO(dbManager);

			// List all records
			PageVO page = new PageVO();
			page.setPageSize(0);
			page.setCurrentPage(1);

			//page.setParamMap(paramMap);
			Collection supplierList = supplierDao.getSupplierList(vo, page);
			request.setAttribute("supplierList", supplierList);
		} catch (DAOException e) {
			e.printStackTrace();
			request.setAttribute(CommonName.COMMON_ERROR_INFOR, e.toString());
			returnLabel = "fail";
		} finally {
			if (dbManager != null) {
				dbManager.freeConnection();
			}
		}
		return mapping.findActionLocation(returnLabel);
	}

}
