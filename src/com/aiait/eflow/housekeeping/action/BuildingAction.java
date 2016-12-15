package com.aiait.eflow.housekeeping.action;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.http.*;

import com.aiait.eflow.common.CommonName;
import com.aiait.eflow.common.helper.ParamConfigHelper;
import com.aiait.eflow.housekeeping.dao.BuildingDAO;
import com.aiait.eflow.housekeeping.vo.StaffVO;
import com.aiait.eflow.housekeeping.vo.BuildingVO;
import com.aiait.eflow.util.ExcelFileUtil;
import com.aiait.eflow.util.FileUtil;
import com.aiait.eflow.util.MultipartRequest;
import com.aiait.framework.db.*;
import com.aiait.framework.mvc.action.*;
import com.aiait.framework.util.CommonUtil;

public class BuildingAction extends DispatchAction {

    public ActionLocation listBuilding(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String returnLabel = "listBuilding";     
        IDBManager dbManager = null;
        try {
        	dbManager = DBManagerFactory.getDBManager();
            BuildingDAO dao = new BuildingDAO(dbManager);
            BuildingVO vo = new BuildingVO();
            StaffVO currentStaff = (StaffVO)request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
            vo.setOrg_id(currentStaff.getOrgId());
            Collection list = dao.search(vo);
            request.setAttribute("buildingList", list);
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

    public ActionLocation editBuilding(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String returnLabel = "editBuilding";
        String editType = request.getParameter("editType");
        String buildingId = request.getParameter("buildingId");
        if (editType != null && "new".equals(editType)) {
            return mapping.findActionLocation(returnLabel);
        }

        String code = CommonUtil.decoderURL(buildingId);
        BuildingVO vo = new BuildingVO();
        vo.setCode(code);
        StaffVO currentStaff = (StaffVO)request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
        vo.setOrg_id(currentStaff.getOrgId());

        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            BuildingDAO dao = new BuildingDAO(dbManager);
            vo = dao.getBuilding(vo);
            request.setAttribute("building", vo);
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

    public ActionLocation saveBuilding(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String returnLabel = "";

        BuildingVO vo = new BuildingVO();
        vo.setCode(request.getParameter("code"));
        vo.setName(request.getParameter("name"));
        vo.setProvince(request.getParameter("province"));
        vo.setCity(request.getParameter("city"));
        vo.setRenter(request.getParameter("renter"));
        vo.setPeriod(request.getParameter("period"));
        vo.setDuration(request.getParameter("duration")); 
        vo.setAcc_name(request.getParameter("acc_name")); 
        vo.setAcc_bank(request.getParameter("acc_bank")); 
        vo.setAcc_no(request.getParameter("acc_no")); 
        vo.setArea(Double.parseDouble(request.getParameter("area"))); 
        vo.setFree_month(Double.parseDouble(request.getParameter("free_month"))); 
        vo.setFree_period(request.getParameter("free_period")); 
        vo.setMonth_rent_fee(Double.parseDouble(request.getParameter("month_rent_fee"))); 
        vo.setMonth_rent_curr(request.getParameter("month_rent_curr")); 
        vo.setMonth_mang_fee(Double.parseDouble(request.getParameter("month_mang_fee"))); 
        vo.setMonth_mang_curr(request.getParameter("month_mang_curr")); 
        vo.setDepo_month(Double.parseDouble(request.getParameter("depo_month"))); 
        vo.setDepo_fee_rent(Double.parseDouble(request.getParameter("depo_fee_rent")));
        vo.setDepo_fee_prop(Double.parseDouble(request.getParameter("depo_fee_prop"))); 
        vo.setTot_amount(Double.parseDouble(request.getParameter("tot_amount"))); 
        StaffVO currentStaff = (StaffVO)request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
        vo.setOrg_id(currentStaff.getOrgId());
        vo.setContract_no(request.getParameter("contract_no")); 

        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            BuildingDAO dao = new BuildingDAO(dbManager);
            dao.saveBuilding(vo);
            return this.listBuilding(mapping, request, response);
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

    public ActionLocation deleteBuilding(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String returnLabel = "";
        String[] buildingIds = request.getParameterValues("buildingId");

        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            BuildingDAO dao = new BuildingDAO(dbManager);
            BuildingVO vo = new BuildingVO();
            for (int i = 0; i < buildingIds.length; i++) {
            	String code = buildingIds[i];
                vo.setCode(code);
                StaffVO currentStaff = (StaffVO)request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
                vo.setOrg_id(currentStaff.getOrgId());
                dao.deleteBuilding(vo);
            }
            return this.listBuilding(mapping, request, response);
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
			dataList = excelUtil.parseBuilding(currentStaff.getOrgId());
			
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
		try {
			dbManager = DBManagerFactory.getDBManager();
			BuildingDAO dao = new BuildingDAO(dbManager);
			dbManager.startTransaction();
			Iterator it = dataList.iterator();
			while (it.hasNext()) {
				BuildingVO vo = (BuildingVO) it.next();
				if (dao.getBuilding(vo) == null) { // 如果不存在，则insert数据库
					dao.addBuilding(vo);
				} else { // 否则采用update数据库
					dao.saveBuilding(vo);
				}
			}
			dbManager.commit();
		} catch (Exception e) {
			if (dbManager != null)
				dbManager.rollback();
			e.printStackTrace();
			throw new Exception("Fail to save excel data:" + e.toString());
		} finally {
			if (dbManager != null)
				dbManager.freeConnection();
		}
	}
}