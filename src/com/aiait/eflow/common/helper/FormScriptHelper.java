package com.aiait.eflow.common.helper;

import java.util.HashMap;

import com.aiait.framework.db.ConnectionException;
import com.aiait.framework.db.DBManagerFactory;
import com.aiait.framework.db.IDBManager;
import com.aiait.framework.exception.DAOException;
import com.aiait.eflow.formmanage.dao.FormManageDAO;

public class FormScriptHelper {
	private static FormScriptHelper formScript;
	private static HashMap scriptList = new HashMap();
	   
	   private FormScriptHelper(){
		   init();
	   }
	   
	   public static FormScriptHelper getInstance(){
	   	 if(formScript==null){
	   		synchronized(FormScriptHelper.class){
	   	 	    if(formScript==null){
	   			  formScript = new FormScriptHelper();
	   	 	    }
	   		}
	   	 }
	   	 return formScript;
	   }
	   
	   public void init(){
	   	 scriptList = new HashMap();
		IDBManager dbManager = null;	
		try{		
		  dbManager =  DBManagerFactory.getDBManager();
		  FormManageDAO dao = new FormManageDAO(dbManager);
		  scriptList = (HashMap) dao.getAllFormScript();

		}catch(DAOException e){
		  e.printStackTrace(); 		  
		}catch(ConnectionException ex){
		  ex.printStackTrace();	
		}
		finally{
		  dbManager.freeConnection();
		}
	  }
	  
	  public String getScript(String formSystemId){
	  	if(scriptList.get(formSystemId)!=null){
	  		String script = (String)scriptList.get(formSystemId); 
	  		if(script ==null) return "";
		  return script ;
	  	}else{
	  	  return "";
	  	}
	  }

	  
	  public void refresh(){
	  	this.init();
	  }
}
