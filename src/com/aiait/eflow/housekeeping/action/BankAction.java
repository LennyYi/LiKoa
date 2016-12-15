package com.aiait.eflow.housekeeping.action;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.http.*;

import com.aiait.eflow.common.CommonName;
import com.aiait.eflow.common.helper.ParamConfigHelper;
import com.aiait.eflow.housekeeping.dao.BankDAO;
import com.aiait.eflow.housekeeping.vo.StaffVO;
import com.aiait.eflow.housekeeping.vo.BankVO;
import com.aiait.eflow.util.ExcelFileUtil;
import com.aiait.eflow.util.FileUtil;
import com.aiait.eflow.util.MultipartRequest;
import com.aiait.framework.db.*;
import com.aiait.framework.mvc.action.*;
import com.aiait.framework.util.CommonUtil;

public class BankAction extends DispatchAction {

    public ActionLocation listBank(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String returnLabel = "listBank";     
        IDBManager dbManager = null;
        try {
        	dbManager = DBManagerFactory.getDBManager();
        	StaffVO currentStaff = (StaffVO)request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
            BankDAO dao = new BankDAO(dbManager);
            Collection list = dao.search(currentStaff.getOrgId());
            request.setAttribute("BankList", list);
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

    public ActionLocation editBank(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String returnLabel = "editBank";
        String editType = request.getParameter("editType");
        String BankId = request.getParameter("code");
        if (editType != null && "new".equals(editType)) {
            return mapping.findActionLocation(returnLabel);
        }

        String code = CommonUtil.decoderURL(BankId);
        StaffVO currentStaff = (StaffVO)request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
        BankVO vo = new BankVO();
        vo.setBankCode(code);
        vo.setOrgId(currentStaff.getOrgId());

        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            BankDAO dao = new BankDAO(dbManager);
            vo = dao.getBank(vo);
            request.setAttribute("Bank", vo);
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

    public ActionLocation saveBank(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
    	StaffVO currentStaff = (StaffVO)request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
        String returnLabel = "";
        String orgId = currentStaff.getOrgId();

        BankVO vo = new BankVO();
        vo.setBankCode(request.getParameter("bankcode"));
        vo.setBankName(request.getParameter("bankname"));
        vo.setAccountCode(request.getParameter("accountcode"));
        vo.setAccountName(request.getParameter("accountname"));
        vo.setOrgId(orgId);
        vo.setType(request.getParameter("type"));
        vo.setCity(request.getParameter("city"));
        vo.setSunCode(request.getParameter("sun_code"));
        vo.setIsDefault(("on").equals(request.getParameter("is_default"))?1:0);

        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            BankDAO dao = new BankDAO(dbManager);
            dao.saveBank(vo);
            return this.listBank(mapping, request, response);
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

    public ActionLocation deleteBank(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
    	StaffVO currentStaff = (StaffVO)request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
        String returnLabel = "";
        String[] BankIds = request.getParameterValues("code");

        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            BankDAO dao = new BankDAO(dbManager);
            BankVO vo = new BankVO();
            for (int i = 0; i < BankIds.length; i++) {
            	String code = BankIds[i];
                vo.setBankCode(code);
                vo.setOrgId(currentStaff.getOrgId());
                dao.deleteBank(vo);
            }
            return this.listBank(mapping, request, response);
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
	    
    public ActionLocation setDefault(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
    throws Exception {
		String returnLabel = "";
		String code = request.getParameter("code");
		String orgId = request.getParameter("orgid");
		
		IDBManager dbManager = null;
		try {
		    dbManager = DBManagerFactory.getDBManager();
		    BankDAO dao = new BankDAO(dbManager);
		    BankVO vo = new BankVO();
		    vo.setBankCode(code);
		    vo.setOrgId(orgId);
		    dao.setDefault(vo);
		    return this.listBank(mapping, request, response);
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
			dataList = excelUtil.parseBank(currentStaff.getOrgId());
			
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
			BankDAO dao = new BankDAO(dbManager);
			dbManager.startTransaction();
			Iterator it = dataList.iterator();
			while (it.hasNext()) {
				BankVO vo = (BankVO) it.next();
				if (dao.getBank(vo) == null) { // 如果不存在，则insert数据库
					dao.addBank(vo);
				} else { // 否则采用update数据库
					dao.saveBank(vo);
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