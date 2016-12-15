package com.aiait.eflow.reportmanage.helper;

import java.util.HashMap;

import com.aiait.eflow.reportmanage.dao.ReportManageDAO;
import com.aiait.framework.db.ConnectionException;
import com.aiait.framework.db.DBManagerFactory;
import com.aiait.framework.db.IDBManager;
import com.aiait.framework.exception.DAOException;

public class ReportHtmlHelper {
	private static ReportHtmlHelper reportHtml;
	private static HashMap htmlList = new HashMap();
	   
	   private ReportHtmlHelper(){
		   init();
	   }
	   
	   public static ReportHtmlHelper getInstance(){
	   	 if(reportHtml==null){
	   		synchronized(ReportHtmlHelper.class){
	   	 	    if(reportHtml==null){
	   			  reportHtml = new ReportHtmlHelper();
	   	 	    }
	   		}
	   	 }
	   	 return reportHtml;
	   }
	   
	   public void init(){
	   	 htmlList = new HashMap();
		IDBManager dbManager = null;	
		try{		
		  dbManager =  DBManagerFactory.getDBManager();
		  ReportManageDAO dao = new ReportManageDAO(dbManager);
		  htmlList = (HashMap) dao.getAllReportHtml();

		}catch(DAOException e){
		  e.printStackTrace(); 		  
		}catch(ConnectionException ex){
		  ex.printStackTrace();	
		}
		finally{
		  dbManager.freeConnection();
		}
	  }
	  
	  public String getHtml(String reportSystemId){
	  	if(htmlList.get(reportSystemId)!=null){
	  		String html = (String)htmlList.get(reportSystemId); 
	  		if(html ==null) return "";
		  return html ;
	  	}else{
	  	  return "";
	  	}
	  }

	  
	  public void refresh(){
	  	this.init();
	  }
}
