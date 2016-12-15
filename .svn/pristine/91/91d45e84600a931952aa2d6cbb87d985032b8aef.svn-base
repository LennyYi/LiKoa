package com.aiait.eflow.reportmanage.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import com.aiait.eflow.reportmanage.dao.ReportTypeDAO;
import com.aiait.eflow.reportmanage.vo.ReportTypeVO;
import com.aiait.framework.db.DBManagerFactory;
import com.aiait.framework.db.IDBManager;

public class ReportTypeHelper {
   private static ReportTypeHelper helper = null;
   
   private static Collection reportTypeList = new ArrayList();
   private static HashMap reportTypeMap = new HashMap();
   
   private ReportTypeHelper(){   
	   refresh();
   }
   
   public void setInstanceNull(){
	   reportTypeList.clear();
	   reportTypeMap.clear();
   	this.helper = null;
   }
   
   public static ReportTypeHelper getInstance(){
	   if(helper==null){
		   synchronized(ReportTypeHelper.class){
		      if(helper==null){
			    helper = new ReportTypeHelper();
		      }
		   }
	   }
	   return helper;
   }
   
   public Collection getReportTypeList(){
	   return reportTypeList;
   }
   
   public ReportTypeVO getReportTypeVo(String reportTypeId){
	   if(reportTypeMap.containsKey(reportTypeId)){
		   return (ReportTypeVO)reportTypeMap.get(reportTypeId);
	   }
	   return null;
   }
   
   public String getReportTypeName(String reportTypeId){
	   if(reportTypeMap.containsKey(reportTypeId)){
		   return ((ReportTypeVO)reportTypeMap.get(reportTypeId)).getReportTypeName();
	   }
	   return "";
   }
   
   public void refresh(){
	   IDBManager dbManager = null;
	   reportTypeList.clear();
	   reportTypeMap.clear();
	   try{
		  dbManager =  DBManagerFactory.getDBManager();
		  ReportTypeDAO dao = new ReportTypeDAO(dbManager);
		  reportTypeList = dao.getReportTypeList();
		  if(reportTypeList!=null && reportTypeList.size()>0){
			  Iterator it = reportTypeList.iterator();
			  while(it.hasNext()){
				  ReportTypeVO vo = (ReportTypeVO)it.next();
				  reportTypeMap.put(vo.getReportTypeId(),vo);
			  }
		  }
	   }catch(Exception e){
		  e.printStackTrace();
	   }finally{
		   if(dbManager!=null) dbManager.freeConnection();
	   }
   }
}
