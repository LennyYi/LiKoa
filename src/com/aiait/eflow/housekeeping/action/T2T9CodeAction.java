package com.aiait.eflow.housekeeping.action;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.http.*;

import com.aiait.eflow.common.CommonName;
import com.aiait.eflow.common.helper.ParamConfigHelper;
import com.aiait.eflow.housekeeping.dao.T2T9DAO;
import com.aiait.eflow.housekeeping.vo.StaffVO;
import com.aiait.eflow.housekeeping.vo.FinanceCodeVO;
import com.aiait.eflow.util.ExcelFileUtil;
import com.aiait.eflow.util.FileUtil;
import com.aiait.eflow.util.MultipartRequest;
import com.aiait.framework.common.GlobalCommonName;
import com.aiait.framework.db.*;
import com.aiait.framework.mvc.action.*;
import com.aiait.framework.page.PageUtil;
import com.aiait.framework.page.PageVO;
import com.aiait.framework.util.CommonUtil;

public class T2T9CodeAction extends DispatchAction {

    public ActionLocation listCode(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String returnLabel = "listCode";
        
        IDBManager dbManager = null;
        try {
            String orgId = request.getParameter("orgId");
            StaffVO currentStaff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
            if (orgId == null || "".equals(orgId)) {
                orgId = currentStaff.getOrgId();
            }
            dbManager = DBManagerFactory.getDBManager();
            T2T9DAO dao = new T2T9DAO(dbManager);
            dao.setTableName(request.getParameter("flg"));

            // 分页查询begin
            String pagenum = (String) request.getParameter(GlobalCommonName.PAGE_CURRENT_NUM);
            if (pagenum == null) {
                pagenum = "1";
            }
            PageVO page = new PageVO(Integer.parseInt(ParamConfigHelper.getInstance().getParamValue(
                    CommonName.EVERY_PAGE_RECORDS_NUM, CommonName.DEFAULT_RECORDS_NUM)));
            // page.setParamMap(paramMap);
            page.setCurrentPage(Integer.parseInt(pagenum));
            int totalRecordsNum = dao.getTotalRecordsNum(orgId, page);
            page = PageUtil.createPage(page, totalRecordsNum);
            Collection list = dao.search(orgId, page);
            request.setAttribute(GlobalCommonName.PAGE_INFORMATION_NAME, page);
            // 分页查询end

            request.setAttribute("codeList", list);
            request.setAttribute("flg", request.getParameter("flg"));
            request.setAttribute("orgId", orgId);
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
        request.setAttribute("flg", request.getParameter("flg"));
        if (editType != null && "new".equals(editType)) {
            return mapping.findActionLocation(returnLabel);
        }

        codeId = CommonUtil.decoderURL(codeId);
        int split = codeId.indexOf('_');
        String orgId = codeId.substring(0, split);
        String code = codeId.substring(split + 1);
        FinanceCodeVO t2t9VO = new FinanceCodeVO();
        t2t9VO.setOrgId(orgId);
        t2t9VO.setCode(code);

        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            T2T9DAO dao = new T2T9DAO(dbManager);
            dao.setTableName(request.getParameter("flg"));
            t2t9VO = dao.getCode(t2t9VO);
            request.setAttribute("t2t9", t2t9VO);
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
        String orgId = request.getParameter("orgId");
        StaffVO currentStaff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
        if (orgId == null || "".equals(orgId)) {
            orgId = currentStaff.getOrgId();
        }
        String returnLabel = "";
        String code = request.getParameter("code");
        String name = request.getParameter("name");
        String t0 = request.getParameter("t0");
        String t1 = request.getParameter("t1");
        String t2 = request.getParameter("t2");
        String t3 = request.getParameter("t3");
        String t4 = request.getParameter("t4");
        String t5 = request.getParameter("t5");
        String t6 = request.getParameter("t6");

        FinanceCodeVO t2t9VO = new FinanceCodeVO();
        t2t9VO.setOrgId(orgId);
        t2t9VO.setCode(code);
        t2t9VO.setName(name);
        t2t9VO.setT0("1".equals(t0) ? 1 : 0);
        t2t9VO.setT1("1".equals(t1) ? 1 : 0);
        t2t9VO.setT2("1".equals(t2) ? 1 : 0);
        t2t9VO.setT3("1".equals(t3) ? 1 : 0);
        t2t9VO.setT4("1".equals(t4) ? 1 : 0);
        t2t9VO.setT5("1".equals(t5) ? 1 : 0);
        t2t9VO.setT6("1".equals(t6) ? 1 : 0);

        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            T2T9DAO dao = new T2T9DAO(dbManager);
            dao.setTableName(request.getParameter("flg"));
            dao.saveCode(t2t9VO);
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
            T2T9DAO dao = new T2T9DAO(dbManager);
            dao.setTableName(request.getParameter("flg"));
            FinanceCodeVO t2t9VO = new FinanceCodeVO();
            for (int i = 0; i < codeIds.length; i++) {
                String codeId = codeIds[i];
                int split = codeId.indexOf('_');
                String orgId = codeId.substring(0, split);
                String code = codeId.substring(split + 1);
                t2t9VO.setOrgId(orgId);
                t2t9VO.setCode(code);
                dao.deleteCode(t2t9VO);
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
		String returnLabel = "excelFileSelectPage";
		request.setAttribute("flg", request.getParameter("flg"));
		return mapping.findActionLocation(returnLabel);
	}
	
	public ActionLocation importExcelFile(
			ModuleMapping mapping,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		String returnLabel = "tempsuccess";
		String flg = (String)request.getParameter("flg"); // T2,T9,SunAC...
		if(flg==null){
			flg="01";
		}
		String orgId = (String) request.getParameter("orgId");
		// System.out.println("orgId: " + orgId);
		StaffVO currentStaff = (StaffVO) request.getSession().getAttribute(
				CommonName.CURRENT_STAFF_INFOR);
		if (orgId == null || "".equals(orgId)) {
            orgId = currentStaff.getOrgId();
        }
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
			dataList = excelUtil.parseFinanceCode(orgId, flg);
			
			if(dataList!=null){
				saveExcelData(dataList,flg);
			}
			
			String url = "financeCodeAction.it?method=listCode&flg=" + flg + "&orgId=" + orgId;
			// request.setAttribute("url", url);
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
	
	private void saveExcelData(Collection dataList, String flg)
			throws Exception {
		IDBManager dbManager = null;
		try {
			dbManager = DBManagerFactory.getDBManager();
			T2T9DAO dao = new T2T9DAO(dbManager);
			dao.setTableName(flg);
			dbManager.startTransaction();
			Iterator it = dataList.iterator();
			while (it.hasNext()) {
				FinanceCodeVO vo = (FinanceCodeVO) it.next();
				dao.saveCode(vo);
				//if (dao.getCode(vo) == null) { // 如果不存在，则insert数据库
				//	dao.addCode(vo);
				//} else { // 否则采用update数据库
				//	dao.saveCode(vo);
				//}
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
