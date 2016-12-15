package com.aiait.eflow.common.helper;
/******************************************************************/
/*Task_ID	Author	Modify_Date	Description                       */
/*IT0973	Young	12/28/2007	For the function of reference form*/
/******************************************************************/
import com.aiait.eflow.formmanage.vo.FormSectionFieldVO;
import com.aiait.eflow.housekeeping.vo.SystemFieldVO;
import com.aiait.eflow.housekeeping.dao.SystemFieldDAO;
import com.aiait.framework.db.DBManagerFactory;
import com.aiait.framework.db.IDBManager;
import com.aiait.eflow.common.CommonName;

public class DynamicSystemFieldHelper {
	
	private	SystemFieldVO	resultField;
	
	private void setDynamicSystemField(String fieldId, String staffCode, String formId){	
		IDBManager dbManager = null;
		try{
			  dbManager =  DBManagerFactory.getDBManager();
			  SystemFieldDAO dao = new SystemFieldDAO(dbManager);
			  this.resultField = dao.getField(fieldId, staffCode, formId);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(dbManager!=null) dbManager.freeConnection();
		}
		
	}
    public 	SystemFieldVO getDynamicSystemField(String fieldId, String staffCode, String formId){
    	setDynamicSystemField(fieldId, staffCode, formId);
    	return this.resultField;
    }
    public FormSectionFieldVO getSysFieldOfFormSectionFieldVO(String fieldId, String staffCode, String formsystemId){
    	FormSectionFieldVO sysfield=new FormSectionFieldVO();    	
    	setDynamicSystemField(fieldId,staffCode,formsystemId);
    	sysfield.setFieldId(this.resultField.getFieldId());
    	sysfield.setFieldLabel(this.resultField.getFieldLabel());
    	sysfield.setFieldType(CommonName.FIELD_TYPE_SYSTEM);
    	sysfield.setIsRequired(true);
    	sysfield.setFieldLength(this.resultField.getColumnLength());
    	sysfield.setDataType(1);
    	sysfield.setOrderId(9);
    	sysfield.setOptionList(this.resultField.getOptionList());
    	return sysfield;
    }
    public String getSelectedReqList(String request_no, String formsystemId, String sectionId){
    	String retStr="";
    	IDBManager dbManager = null;
		try{
			  dbManager =  DBManagerFactory.getDBManager();
			  SystemFieldDAO dao = new SystemFieldDAO(dbManager);
			  retStr = dao.getSelectedReqList(request_no, formsystemId, sectionId);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(dbManager!=null) dbManager.freeConnection();
		}    	    
    	return retStr;
    }
}
