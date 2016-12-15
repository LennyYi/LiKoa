package com.aiait.eflow.common.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import com.aiait.eflow.housekeeping.dao.FormTypeDAO;
import com.aiait.eflow.housekeeping.vo.FormTypeVO;
import com.aiait.framework.db.DBManagerFactory;
import com.aiait.framework.db.IDBManager;

public class FormTypeHelper {
   private static FormTypeHelper helper = null;
   
   private static Collection formTypeList = new ArrayList();
   private static HashMap formTypeMap = new HashMap();
   
   private FormTypeHelper(){   
	   refresh();
   }
   
   public void setInstanceNull(){
	   formTypeList.clear();
	   formTypeMap.clear();
   	this.helper = null;
   }
   
   public static FormTypeHelper getInstance(){
	   if(helper==null){
		   synchronized(FormTypeHelper.class){
		      if(helper==null){
			    helper = new FormTypeHelper();
		      }
		   }
	   }
	   return helper;
   }
   
   public Collection getFormTypeList(){
	   return formTypeList;
   }
   
   public FormTypeVO getFormTypeVo(String formTypeId){
	   if(formTypeMap.containsKey(formTypeId)){
		   return (FormTypeVO)formTypeMap.get(formTypeId);
	   }
	   return null;
   }
   
   public String getFormTypeName(String formTypeId){
	   if(formTypeMap.containsKey(formTypeId)){
		   return ((FormTypeVO)formTypeMap.get(formTypeId)).getFormTypeName();
	   }
	   return "";
   }
   
   public void refresh(){
	   IDBManager dbManager = null;
	   formTypeList.clear();
	   formTypeMap.clear();
	   try{
		  dbManager =  DBManagerFactory.getDBManager();
		  FormTypeDAO dao = new FormTypeDAO(dbManager);
		  formTypeList = dao.getFormTypeList();
		  if(formTypeList!=null && formTypeList.size()>0){
			  Iterator it = formTypeList.iterator();
			  while(it.hasNext()){
				  FormTypeVO vo = (FormTypeVO)it.next();
				  formTypeMap.put(vo.getFormTypeId(),vo);
			  }
		  }
	   }catch(Exception e){
		  e.printStackTrace();
	   }finally{
		   if(dbManager!=null) dbManager.freeConnection();
	   }
   }
}
