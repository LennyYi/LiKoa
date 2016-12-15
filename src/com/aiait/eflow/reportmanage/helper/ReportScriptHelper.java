package com.aiait.eflow.reportmanage.helper;

import java.util.HashMap;

import com.aiait.eflow.reportmanage.dao.ReportManageDAO;
import com.aiait.framework.db.ConnectionException;
import com.aiait.framework.db.DBManagerFactory;
import com.aiait.framework.db.IDBManager;
import com.aiait.framework.exception.DAOException;

public class ReportScriptHelper {
	private static ReportScriptHelper reportScript;
	private static HashMap scriptList = new HashMap();
	   
	   private ReportScriptHelper(){
		   init();
	   }
	   
	   public static ReportScriptHelper getInstance(){
	   	 if(reportScript==null){
	   		synchronized(ReportScriptHelper.class){
	   	 	    if(reportScript==null){
	   			  reportScript = new ReportScriptHelper();
	   	 	    }
	   		}
	   	 }
	   	 return reportScript;
	   }
	   
	   public void init(){
	   	 scriptList = new HashMap();
		IDBManager dbManager = null;	
		try{		
		  dbManager =  DBManagerFactory.getDBManager();
		  ReportManageDAO dao = new ReportManageDAO(dbManager);
		  scriptList = (HashMap) dao.getAllReportScript();

		}catch(DAOException e){
		  e.printStackTrace(); 		  
		}catch(ConnectionException ex){
		  ex.printStackTrace();	
		}
		finally{
		  dbManager.freeConnection();
		}
	  }
	  
	  public String getScript(String reportSystemId){
	  	if(scriptList.get(reportSystemId)!=null){
	  		String script = (String)scriptList.get(reportSystemId); 
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
