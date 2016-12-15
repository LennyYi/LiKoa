package com.aiait.eflow.formmanage.action;

import java.io.File;
import java.net.URLDecoder;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.aiait.eflow.common.CommonName;
import com.aiait.eflow.common.helper.ParamConfigHelper;
import com.aiait.eflow.formmanage.dao.UploadDAO;
import com.aiait.eflow.formmanage.vo.UploadFileVO;
import com.aiait.eflow.housekeeping.vo.StaffVO;
import com.aiait.eflow.util.DataConvertUtil;
import com.aiait.eflow.util.FileUtil;
import com.aiait.eflow.util.MultipartRequest;
import com.aiait.framework.db.DBManagerFactory;
import com.aiait.framework.db.IDBManager;
import com.aiait.framework.mvc.action.ActionLocation;
import com.aiait.framework.mvc.action.DispatchAction;
import com.aiait.framework.mvc.action.ModuleMapping;
import com.aiait.framework.mvc.util.Global;
import com.aiait.framework.util.CommonUtil;

public class UploadAction extends DispatchAction {

	public ActionLocation enter(ModuleMapping mapping,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String returnLabel = "showSelectFilePage";
		return mapping.findActionLocation(returnLabel);
	}

	public ActionLocation deleteFile(ModuleMapping mapping,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String returnLabel = "showSelectFilePage";
		String[] fileNames = request.getParameterValues("id");
		StaffVO staff = (StaffVO) request.getSession().getAttribute(
				CommonName.CURRENT_STAFF_INFOR);
		IDBManager dbManager = null;
		try {
			dbManager = DBManagerFactory.getDBManager();
			UploadDAO dao = new UploadDAO(dbManager);
			dbManager.startTransaction();
			
			String deleteFileType = (String)request.getParameter("deleteFileType");
			String fileFoldName = "";
			if(deleteFileType!=null && !"".equals(deleteFileType) && "modify".equals(deleteFileType)){//该表单已经存在
				String sectionId = (String)request.getParameter("sectionId");
				String formSystemId = (String)request.getParameter("formSystemId");
				String tableName = dao.getTableName(sectionId,Integer.parseInt(formSystemId));
				String tempFileName = "";
				for (int i = 0; i < fileNames.length; i++) {
					tempFileName = dao.getFileNameById(fileNames[i],tableName);
					dao.deleteFormFile(fileNames[i],tableName);
					fileNames[i] = tempFileName;
				}
				fileFoldName = "requestform";
			}else{//该表单还没有保存
			   UploadFileVO vo = new UploadFileVO();
			   vo.setStaffCode(staff.getStaffCode());
			   for (int i = 0; i < fileNames.length; i++) {
				 vo.setFileName(fileNames[i]);
				 dao.delete(vo);
			   }
			   fileFoldName = "requestform";
			}
			dbManager.commit();
			for (int i = 0; i < fileNames.length; i++) {
                fileNames[i] = DataConvertUtil.convertISOToGBK(fileNames[i]);
                if (!FileUtil.deleteUploadFile("/upload/" + fileFoldName + "/" + fileNames[i])) {

                    System.out.println("---Fail to delete attached file(" + fileNames[i] + ")!----");
                }
            }
		} catch (Exception e) {
			dbManager.rollback();
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
	 * 当表单已经存在（即进行附件修改时，再上传新文件，调用该方法）
	 * @param mapping
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionLocation uploadFormFile(ModuleMapping mapping,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String returnLabel = "showSelectFilePage";
		StaffVO staff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
        String userId = staff.getLogonId();
        String filePathName = "";
        String fileDescription = (String) request.getParameter("fileDescription");
        fileDescription = URLDecoder.decode(fileDescription, "UTF-8");
        String requestFormDate = (String) request.getParameter("requestFormDate");
        String attachmentIdentity = (String) request.getParameter("attachmentIdentity");
        String staffCode = staff.getStaffCode();
        String formSystemId = (String) request.getParameter("formSystemId");
        String requestNo = (String) request.getParameter("requestNo");
        String sectionId = (String) request.getParameter("sectionId");
        Date date = new Date();
        String fileName = "";
        IDBManager dbManager = null;
		try {
			if (request.getMethod().equals("POST")
					&& MultipartRequest.isMultipart(request)) {
				request = new MultipartRequest(request, userId);//
				File upFile = ((MultipartRequest) request).getFile("path");
				if (upFile != null) {
					fileName = date.getTime() + "-"
							+ ((MultipartRequest) request).getFileName(upFile);
					fileName = fileName.replaceAll("'","");
					filePathName = "upload/requestform/" + fileName;
					FileUtil.saveAs(upFile, ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_UPLOAD_DIR) + "/"
							+ filePathName);
				}
			}
			
			dbManager = DBManagerFactory.getDBManager();
			UploadDAO dao = new UploadDAO(dbManager);
			String tableName = dao.getTableName(sectionId,Integer.parseInt(formSystemId));
			fileName = DataConvertUtil.convertGBKToISO(fileName);


			UploadFileVO vo = new UploadFileVO();
			vo.setFileName(fileName);
			vo.setFileDescription(fileDescription);
			vo.setStaffCode(staffCode);
			vo.setRequestFormDate(requestFormDate);
			
			//dao.save(vo);
			
			//System.out.println("fileName="+fileName);
			//System.out.println("fileDescription="+fileDescription);
			dao.saveFormFile(tableName,requestNo,fileName,fileDescription);
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("error", e.getMessage());
			returnLabel = "fail";
		} finally {
			if (dbManager != null)
				dbManager.freeConnection();
			if (request instanceof MultipartRequest) {
				// delete the temp file
				((MultipartRequest) request).deleteTemporaryFile();
			}
		}
		return mapping.findActionLocation(returnLabel);
	}
    
	/**
	 * 当表单还没有保存时，上传文件调用该方法，先保存到临时表teflow_upload_file_temp中
	 * @param mapping
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionLocation uploadTempFile(ModuleMapping mapping,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String returnLabel = "showSelectFilePage";

        StaffVO staff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
        String userId = staff.getLogonId();
        String filePathName = "";
        String fileDescription = (String) request.getParameter("fileDescription");
        fileDescription = URLDecoder.decode(fileDescription, "UTF-8");
        // System.out.println("fileDescription: " + fileDescription);
        String requestFormDate = (String) request.getParameter("requestFormDate");
        String attachmentIdentity = (String) request.getParameter("attachmentIdentity");

        String staffCode = staff.getStaffCode();
        Date date = new Date();
        String fileName = "";
        IDBManager dbManager = null;
		try {
			if (request.getMethod().equals("POST")
					&& MultipartRequest.isMultipart(request)) {
				request = new MultipartRequest(request, userId);//
				File upFile = ((MultipartRequest) request).getFile("path");
				if (upFile != null) {
					fileName = date.getTime() + "-"
							+ ((MultipartRequest) request).getFileName(upFile);
					fileName = fileName.replaceAll("'","");
					filePathName = "upload/requestform/" + fileName;
					FileUtil.saveAs(upFile,ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_UPLOAD_DIR)+"/"
							+filePathName);
				}
			}
			System.out.println("fileName: " + fileName);
			//fileName = DataConvertUtil.convertGBKToISO(fileName);
			//fileName = fileName.replaceAll("'","");
			dbManager = DBManagerFactory.getDBManager();
			UploadDAO dao = new UploadDAO(dbManager);
			UploadFileVO vo = new UploadFileVO();
			vo.setFileName(fileName);
			vo.setFileDescription(fileDescription);
			vo.setStaffCode(staffCode);
			vo.setRequestFormDate(requestFormDate);
			vo.setAttachmentIdentity(attachmentIdentity);
			dao.save(vo);
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("error", e.getMessage());
			returnLabel = "fail";
		} finally {
			if (dbManager != null)
				dbManager.freeConnection();
			if (request instanceof MultipartRequest) {
				// delete the temp file
				((MultipartRequest) request).deleteTemporaryFile();
			}
		}
		return mapping.findActionLocation(returnLabel);
	}
	/**
	 * 中间节点修改附件时，上传新文件。从文件系统上覆盖旧文件(!)
	 * @param mapping
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionLocation overwriteFormFile(ModuleMapping mapping,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String returnLabel = "showSelectFilePage";
		StaffVO staff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
        String userId = staff.getLogonId();
        String filePathName = "";
        String fileDescription = (String) request.getParameter("fileDescription");
        fileDescription = URLDecoder.decode(fileDescription, "UTF-8");
        String requestFormDate = (String) request.getParameter("requestFormDate");
        String attachmentIdentity = (String) request.getParameter("attachmentIdentity");
        String staffCode = staff.getStaffCode();
        String formSystemId = (String) request.getParameter("formSystemId");
        String requestNo = (String) request.getParameter("requestNo");
        String sectionId = (String) request.getParameter("sectionId");
        Date date = new Date();
        String fileName = "";
        IDBManager dbManager = null;
		try {
			if (request.getMethod().equals("POST")
					&& MultipartRequest.isMultipart(request)) {
				request = new MultipartRequest(request, userId);//
				File upFile = ((MultipartRequest) request).getFile("path");
				if (upFile != null) {
					/*fileName = date.getTime() + "-"
							+ ((MultipartRequest) request).getFileName(upFile);
					fileName = fileName.replaceAll("'","");*/
					
					fileName = (String) request.getParameter("targetFileName");
					filePathName = "upload/requestform/" + fileName;
					FileUtil.saveAs(upFile, ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_UPLOAD_DIR) + "/"
							+ filePathName);
				}
			}
			
			dbManager = DBManagerFactory.getDBManager();
			UploadDAO dao = new UploadDAO(dbManager);
			String tableName = dao.getTableName(sectionId,Integer.parseInt(formSystemId));
			fileName = DataConvertUtil.convertGBKToISO(fileName);


//			UploadFileVO vo = new UploadFileVO();
//			vo.setFileName(fileName);
//			vo.setFileDescription(fileDescription);
//			vo.setStaffCode(staffCode);
//			vo.setRequestFormDate(requestFormDate);
			
			//dao.save(vo);
			
			//System.out.println("fileName="+fileName);
			//System.out.println("fileDescription="+fileDescription);
//			dao.saveFormFile(tableName,requestNo,fileName,fileDescription);
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("error", e.getMessage());
			returnLabel = "fail";
		} finally {
			if (dbManager != null)
				dbManager.freeConnection();
			if (request instanceof MultipartRequest) {
				// delete the temp file
				((MultipartRequest) request).deleteTemporaryFile();
			}
		}
		return mapping.findActionLocation(returnLabel);
	}
}
