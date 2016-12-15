package com.aiait.eflow.formmanage.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import com.aiait.eflow.formmanage.vo.UploadFileVO;
import com.aiait.eflow.util.DataConvertUtil;
import com.aiait.eflow.util.FieldUtil;
import com.aiait.eflow.util.StringUtil;
import com.aiait.framework.common.DataType;
import com.aiait.framework.db.BaseDAOImpl;
import com.aiait.framework.db.IDBManager;
import com.aiait.framework.exception.DAOException;
import com.aiait.framework.vo.BaseVO;

public class UploadDAO extends BaseDAOImpl {
	
	public UploadDAO(IDBManager dbManager){
		super(dbManager);
	}

	public int delete(BaseVO arg0) throws DAOException {
		UploadFileVO vo = (UploadFileVO)arg0;
		String sql = "DELETE FROM teflow_upload_file_temp WHERE staff_code=? AND file_name=?";
		//String sql = "DELETE FROM teflow_upload_file_temp WHERE attachement_id = ?";
		Object[] obj = new Object[2];
		obj[0] = vo.getStaffCode();
		obj[1] = vo.getFileName();
		//obj[0] = vo.getAttachmentIdentity();
		//System.out.println(sql);
		int[] dataType = {DataType.VARCHAR,DataType.VARCHAR}; 
		dbManager.executeUpdate(sql,obj,dataType);
		return 0;
	}

	public String getTableName(String sectionId,int formSystemId)throws DAOException {
		String getTableSql = "select table_name from teflow_form_section where section_id='"+sectionId+"' and section_type='00' and form_system_id = "+ formSystemId;
		Collection tempList = dbManager.query(getTableSql);
		if(tempList==null || tempList.size()==0) return "";
		String tableName = (String)((HashMap)tempList.iterator().next()).get("TABLE_NAME");
		return tableName;
	}
	
	public void deleteFormFile(String id,String tableName)throws DAOException {
		//String delSQL = "delete from " + tableName + " where file_name='"+fileName+"'";
		String delSQL = "delete from " + tableName + " where id="+id;
		dbManager.executeUpdate(delSQL);
	}
	
	public String getFileNameById(String id,String tableName)throws DAOException{
		String fileName = "";
		String sql = "select file_name from " + tableName + " where id="+id;
		Collection list = dbManager.query(sql);
		if(list==null || list.size()==0) return "";
		HashMap map = (HashMap)list.iterator().next();
		fileName = (String)map.get("FILE_NAME");
		return fileName;
	}
	
	public int save(BaseVO arg0) throws DAOException {
		UploadFileVO vo = (UploadFileVO)arg0;
		 //String sql = "INSERT INTO teflow_upload_file_temp(staff_code,file_name,file_description,request_form_date) ";
		 //sql = sql +" VALUES('"+vo.getStaffCode()+"','"+vo.getFileName()+"','"+vo.getFileDescription()+"','"+vo.getRequestFormDate()+"')";
         //System.out.println(sql);
		String sql = "INSERT INTO teflow_upload_file_temp(staff_code,file_name,file_description,request_form_date,attachment_id) values(?,?,?,?,?)";
		Object[] obj = new Object[5];
		obj[0] = vo.getStaffCode();
		obj[1] = vo.getFileName();
		obj[2] = vo.getFileDescription();
		obj[3] = vo.getRequestFormDate();
		obj[4] = vo.getAttachmentIdentity();
		
		int[] dataType = {DataType.VARCHAR,DataType.VARCHAR,DataType.VARCHAR,DataType.VARCHAR,DataType.VARCHAR}; 
		dbManager.executeUpdate(sql,obj,dataType);
		 return 0;
	}

	public void saveFormFile(String tableName,String requestNo,String fileName,String fileDescription)throws DAOException {
		//String sql = "insert into "+tableName + "(file_name,file_description,request_no) values('"+fileName+"','"+fileDescription+"','"+requestNo+"')";
        String sql = "insert into " + tableName + "(file_name,file_description,request_no) values(?,?,?)";
		Object[] obj = new Object[3];
		obj[0] = fileName;
		obj[1] = fileDescription;
		obj[2] = requestNo;
		int[] dataType = {DataType.VARCHAR,DataType.VARCHAR,DataType.VARCHAR}; 
		dbManager.executeUpdate(sql,obj,dataType);
	}
	
	public int update(BaseVO arg0) throws DAOException {
		// TODO Auto-generated method stub
		return 0;
	}
	

	public Collection getRequestFormAllFile(BaseVO arg0) throws DAOException {
		Collection result = new ArrayList();
		UploadFileVO vo = (UploadFileVO)arg0;
		String sql = "SELECT * FROM teflow_upload_file_temp WHERE staff_code=? AND request_form_date=?";
		Object[] obj = new Object[2];
		obj[0] = vo.getStaffCode();
		obj[1] = vo.getRequestFormDate();
		int[] dataType = {DataType.VARCHAR,DataType.VARCHAR}; 
		Collection list = dbManager.query(sql,obj,dataType);
		if(list==null || list.size()==0) return result;
		Iterator it = list.iterator();
		while(it.hasNext()){
			HashMap map = (HashMap)it.next();
			UploadFileVO temp = new UploadFileVO();
			temp.setFileName(DataConvertUtil.convertISOToGBK(FieldUtil.convertSafeString(map,"FILE_NAME")));
			temp.setStaffCode(vo.getStaffCode());
			temp.setRequestFormDate(vo.getRequestFormDate());
			temp.setFileDescription(DataConvertUtil.convertISOToGBK(FieldUtil.convertSafeString(map,"FILE_DESCRIPTION")));
			result.add(temp);
		}
		return result;
	}
	
	public Collection getUploadedFileListByForm(String requestNo,String sectionId,int formSystemId)throws DAOException {
		Collection result = new ArrayList();
		String tableName = (String)getTableName(sectionId,formSystemId);
		String sql = "select * from "+tableName + " where request_no='" + requestNo+"'";
		//System.out.println(sql);
		Collection list = dbManager.query(sql);
		if(list==null || list.size()==0) return result;
		Iterator it = list.iterator();
		while(it.hasNext()){
			HashMap map = (HashMap)it.next();
			UploadFileVO temp = new UploadFileVO();
			temp.setFileName(DataConvertUtil.convertISOToGBK(FieldUtil.convertSafeString(map,"FILE_NAME")));
			temp.setStaffCode(FieldUtil.convertSafeString(map,"STAFF_CODE"));
			temp.setFileDescription(DataConvertUtil.convertISOToGBK(FieldUtil.convertSafeString(map,"FILE_DESCRIPTION")));
			temp.setId(FieldUtil.convertSafeInt(map,"ID",0));
			result.add(temp);
		}
		return result;
	}
}
