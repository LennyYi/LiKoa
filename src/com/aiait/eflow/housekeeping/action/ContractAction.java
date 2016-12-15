package com.aiait.eflow.housekeeping.action;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.http.*;

import com.aiait.eflow.common.CommonName;
import com.aiait.eflow.common.helper.ParamConfigHelper;
import com.aiait.eflow.housekeeping.dao.ContractDAO;
import com.aiait.eflow.housekeeping.vo.ContractVO;
import com.aiait.eflow.housekeeping.vo.StaffVO;
import com.aiait.eflow.util.ExcelFileUtil;
import com.aiait.eflow.util.FileUtil;
import com.aiait.eflow.util.MultipartRequest;
import com.aiait.eflow.wkf.dao.ListInquiryFormDAO;
import com.aiait.eflow.wkf.vo.WorkFlowProcessVO;
import com.aiait.framework.db.*;
import com.aiait.framework.exception.DAOException;
import com.aiait.framework.mvc.action.*;
import com.aiait.framework.util.CommonUtil;

public class ContractAction extends DispatchAction {

    public ActionLocation listCntr(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String returnLabel = "listCntr";
        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            ContractDAO dao = new ContractDAO(dbManager);
            Collection list = dao.getCntrList();
            request.setAttribute("cntrList", list);
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

    public ActionLocation editCntr(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String returnLabel = "editCntr";
        String editType = request.getParameter("editType");
        String contractNo = request.getParameter("contractNo");
        
        if (editType != null && "new".equals(editType)) {
            return mapping.findActionLocation(returnLabel);
        }

        contractNo = CommonUtil.decoderURL(contractNo);
        
        ContractVO vo = new ContractVO();
        vo.setContractNo(Integer.parseInt(contractNo));
        
        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            ContractDAO dao = new ContractDAO(dbManager);
            vo = dao.getCntr(vo);
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

    public ActionLocation saveCntr(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String returnLabel = "";
        ContractVO vo = new ContractVO();
        
        vo.setContractNo    (Integer.parseInt(request.getParameter("contractNo")));
        vo.setCity		   (request.getParameter("city"));
        vo.setReceiveDate   (request.getParameter("receiveDate"));
        vo.setRespStaff (request.getParameter("respStaff"));
        vo.setRespDept  (request.getParameter("respDept"));
        vo.setContactTel    (request.getParameter("contactTel"));
        vo.setContractName  (request.getParameter("contractName"));
        vo.setSign1         (request.getParameter("sign1"));    
        vo.setSign2         (request.getParameter("sign2"));    
        vo.setSign3         (request.getParameter("sign3"));    
        vo.setContent       (request.getParameter("content"));
        vo.setAmount        (Double.parseDouble(request.getParameter("amount")));  
        vo.setSignDate      (request.getParameter("signDate"));
        vo.setEffPeriod   (request.getParameter("effFromDate"));
        vo.setIssueDate     (request.getParameter("issueDate"));
        vo.setSignDoc       (request.getParameter("signDoc"));
        vo.setOrgName       (request.getParameter("orgName"));  
        vo.setRemark        (request.getParameter("remark"));
        
        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            ContractDAO dao = new ContractDAO(dbManager);
            dao.saveCntr(vo);
            return this.listCntr(mapping, request, response);
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

    public ActionLocation deleteCntr(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String returnLabel = "";
        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            ContractDAO dao = new ContractDAO(dbManager);
            ContractVO vo = new ContractVO();
            for (int i = 0; i < request.getParameterValues("contractNo").length; i++) {
                vo.setContractNo(Integer.parseInt(request.getParameterValues("contractNo")[i]));
                dao.deleteCntr(vo);
            }
            return this.listCntr(mapping, request, response);
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
			dataList = excelUtil.parseContract(currentStaff.getOrgId());
			
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
			ContractDAO dao = new ContractDAO(dbManager);
			dbManager.startTransaction();
			Iterator it = dataList.iterator();
			while (it.hasNext()) {
				ContractVO vo = (ContractVO) it.next();
				if (dao.getCntr(vo) == null) { // 如果不存在，则insert数据库
					dao.addCntr(vo);
				} else { // 否则采用update数据库
					dao.saveCntr(vo);
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
	
	/**
     * Select Reference Contract for common fields.
     * 
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionLocation selectContract(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String returnUrl = "selectContract";

        String selectedContracts = (String) request.getParameter("selectedContracts");
        String[] selectedContractList = selectedContracts == null || selectedContracts.trim().equals("") ? null
                : selectedContracts.trim().split(",");
        request.setAttribute("selectedContracts", selectedContractList);

        ContractVO queryVo = new ContractVO();
        String qryContractNo = (String) request.getParameter("qryContractNo");
        // System.out.println("qryContractNo: " + qryContractNo);
        try {
            queryVo.setContractNo(Integer.parseInt(qryContractNo));
        } catch (Exception ex) {
            queryVo.setContractNo(-1);
        }

        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            ContractDAO contractDAO = new ContractDAO(dbManager);
            Collection queryContracts = contractDAO.queryContracts(queryVo);
            request.setAttribute("queryContracts", queryContracts);
        } catch (DAOException e) {
            e.printStackTrace();
            request.setAttribute(CommonName.COMMON_ERROR_INFOR, e.getMessage());
            returnUrl = "fail";
        } finally {
            if (dbManager != null)
                dbManager.freeConnection();
        }

        return mapping.findActionLocation(returnUrl);
    }

}
