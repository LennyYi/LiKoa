package com.aiait.eflow.housekeeping.action;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.http.*;

import org.apache.poi.hssf.usermodel.HSSFRow;

import com.aiait.eflow.common.CommonName;
import com.aiait.eflow.common.helper.ParamConfigHelper;
import com.aiait.eflow.housekeeping.dao.CostCenterDAO;
import com.aiait.eflow.housekeeping.vo.CostCenterVO;
import com.aiait.eflow.housekeeping.vo.StaffVO;
import com.aiait.eflow.util.ExcelUtil;
import com.aiait.eflow.util.FileUtil;
import com.aiait.eflow.util.MultipartRequest;
import com.aiait.framework.common.GlobalCommonName;
import com.aiait.framework.db.*;
import com.aiait.framework.mvc.action.*;
import com.aiait.framework.page.PageUtil;
import com.aiait.framework.page.PageVO;
import com.aiait.framework.util.CommonUtil;

public class CostCenterAction extends DispatchAction {

    public ActionLocation listCode(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String returnLabel = "listCode";
        
        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            CostCenterDAO dao = new CostCenterDAO(dbManager);

            // 分页查询begin
            String pagenum = (String) request.getParameter(GlobalCommonName.PAGE_CURRENT_NUM);
            if (pagenum == null) {
                pagenum = "1";
            }
            PageVO page = new PageVO(Integer.parseInt(ParamConfigHelper.getInstance().getParamValue(
                    CommonName.EVERY_PAGE_RECORDS_NUM, CommonName.DEFAULT_RECORDS_NUM)));
            
            page.setCurrentPage(Integer.parseInt(pagenum));
            int totalRecordsNum = dao.getTotalRecordsNum(page);
            page = PageUtil.createPage(page, totalRecordsNum);
            Collection list = dao.search(page);
            request.setAttribute(GlobalCommonName.PAGE_INFORMATION_NAME, page);
            // 分页查询end

            request.setAttribute("codeList", list);
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

    public ActionLocation editCode(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String returnLabel = "editCode";
        String editType = request.getParameter("editType");
        String codeId = request.getParameter("codeId");
        if (editType != null && "new".equals(editType)) {
            return mapping.findActionLocation(returnLabel);
        }

        codeId = CommonUtil.decoderURL(codeId);
        CostCenterVO costCenterVO = new CostCenterVO();
        costCenterVO.setCc_code(codeId);

        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            CostCenterDAO dao = new CostCenterDAO(dbManager);
            costCenterVO = dao.getCode(costCenterVO);
            request.setAttribute("costCenter", costCenterVO);
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

    public ActionLocation saveCode(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
    	
        String returnLabel = "";
        String cc_code = request.getParameter("cc_code");
        String cc_name = request.getParameter("cc_name");
        String exco = request.getParameter("exco");
        String t_code = request.getParameter("t_code");
        
        CostCenterVO costCenterVO = new CostCenterVO();
        costCenterVO.setCc_code(cc_code);
        costCenterVO.setCc_name(cc_name);
        costCenterVO.setExco(exco);
        costCenterVO.setT_code(t_code);

        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            CostCenterDAO dao = new CostCenterDAO(dbManager);
            dao.saveCode(costCenterVO);
            return this.listCode(mapping, request, response);
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

    public ActionLocation deleteCode(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String returnLabel = "";
        String[] codeIds = request.getParameterValues("codeId");

        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            CostCenterDAO dao = new CostCenterDAO(dbManager);
            CostCenterVO costCenterVO = new CostCenterVO();
            for (int i = 0; i < codeIds.length; i++) {
                String codeId = codeIds[i];
                costCenterVO.setCc_code(codeId);
                dao.deleteCode(costCenterVO);
            }
            return this.listCode(mapping, request, response);
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
		String returnLabel = "selectUploadFile";
        request.setAttribute("action", "costCenterAction.it?method=importExcelFile");
        request.setAttribute("title", "Upload Cost Center");
		return mapping.findActionLocation(returnLabel);
	}
	
	public ActionLocation importExcelFile(
			ModuleMapping mapping,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		String returnLabel = "tempsuccess";
		StaffVO currentStaff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
		String userId = currentStaff.getLogonId();
		String fileName = "";
		SimpleDateFormat bartDateFormat = new SimpleDateFormat(
		"yyyyMMddHHmmdd");
        Date date = new Date();
        String dateName = bartDateFormat.format(date);
        HttpServletRequest multiRequest = null;
        try {
		  if (request.getMethod().equals("POST")
				&& MultipartRequest.isMultipart(request)) {
		    multiRequest = new MultipartRequest(request, userId);//
			File upFile = ((MultipartRequest) multiRequest).getFile("path");
			if (upFile != null) {
				fileName = dateName + "_"
						+ ((MultipartRequest) multiRequest).getFileName(upFile);
				fileName = fileName.replaceAll("'","");

				
			   FileUtil.saveAs(upFile, ParamConfigHelper.getInstance().getParamValue("upload_file_dir") + "/"
								+ fileName);		
			}

			ExcelUtil excelUtil = new ExcelUtil(ParamConfigHelper.getInstance().getParamValue("upload_file_dir") + "/"
					+ fileName);
			
			Collection dataList = this.parseExcel(excelUtil);
			
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
			CostCenterDAO dao = new CostCenterDAO(dbManager);
			dbManager.startTransaction();
			Iterator it = dataList.iterator();
			while (it.hasNext()) {
				CostCenterVO costCenterVO = (CostCenterVO) it.next();
				dao.saveCode(costCenterVO);
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
	
    protected Collection parseExcel(ExcelUtil excelUtil) throws Exception {
        int startLine = 2;
        int colNum = 4;
        int sheetIndex = 1;

        Collection list = new ArrayList();
        excelUtil.setSheet(sheetIndex);
        int rows = excelUtil.getSheet().getPhysicalNumberOfRows();
        if (rows >= startLine) {
            // Check format
            HSSFRow rowline = excelUtil.getSheet().getRow(1);
            if (rowline.getLastCellNum() < colNum) {
                throw new Exception("Excel file format error, Cost center sheet should have " + colNum + " data columns.");
            }

            // Process
            for (int r = startLine; r <= rows; r++) {
            	CostCenterVO costCenterVO = new CostCenterVO();
                int i = 1;

                // Cost center code
                String cc_code = excelUtil.getCellValue(sheetIndex, r, i++);
                cc_code = cc_code == null ? "" : cc_code.trim();
                if ("".equals(cc_code) || cc_code.length() > 10) {
                    throw new Exception("Row/Column: " + r + "/" + --i + ": Staff Code cannot be null and max length is 10!");
                }
                costCenterVO.setCc_code(cc_code);

                // Cost center name
                String cc_name = excelUtil.getCellValue(sheetIndex, r, i++);
                cc_name = cc_name == null ? "" : cc_name.trim();
                if ("".equals(cc_name)) {
                     throw new Exception("Row/Column: " + r + "/" + --i + ": Cost center name cannot be null!");
                }
                costCenterVO.setCc_name(cc_name);
               
                // EXCO
                String exco = excelUtil.getCellValue(sheetIndex, r, i++);
                exco = exco == null ? "" : exco.trim();
                if ("".equals(exco) || exco.length() > 10) {
                     throw new Exception("Row/Column: " + r + "/" + --i + ": EXCO cannot be null and max length is 10!");
                }
                costCenterVO.setExco(exco);
                
                // Team code
                String t_code = excelUtil.getCellValue(sheetIndex, r, i++);
                t_code = t_code == null ? "" : t_code.trim();
                if (t_code.length() > 4) {
                    throw new Exception("Row/Column: " + r + "/" + --i + ": Team code max length is 4!");
               }
                costCenterVO.setT_code(t_code);
                
                list.add(costCenterVO);
            }
        }

        return list;
    }
}
