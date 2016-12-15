package com.aiait.eflow.common.helper;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import com.aiait.framework.db.ConnectionException;
import com.aiait.framework.db.DBManagerFactory;
import com.aiait.framework.db.IDBManager;
import com.aiait.framework.exception.DAOException;
import com.aiait.eflow.housekeeping.dao.ParamConfigDAO;
import com.aiait.eflow.housekeeping.vo.ParamConfigVO;

public class ParamConfigHelper {
	private static ParamConfigHelper paramConfig;
	private static HashMap paramList = new HashMap();
	   
	   private ParamConfigHelper(){
		   init();
	   }
	   
	   public static ParamConfigHelper getInstance(){
	   	 if(paramConfig==null){
	   		synchronized(ParamConfigHelper.class){
	   	 	    if(paramConfig==null){
	   			  paramConfig = new ParamConfigHelper();
	   	 	    }
	   		}
	   	 }
	   	 return paramConfig;
	   }
	   
	   public void init(){
	   	 //if(paramList.size()>0){
	   	 //	paramList.clear();
	   	 //}
	   	 paramList = new HashMap();
		IDBManager dbManager = null;	
		try{		
		  dbManager =  DBManagerFactory.getDBManager();
		  ParamConfigDAO dao = new ParamConfigDAO(dbManager);
		  Collection list = dao.search("-1");
		  Iterator iterator = list.iterator();
		  while(iterator.hasNext()){
			  ParamConfigVO model = (ParamConfigVO)iterator.next();
			  paramList.put(model.getParamCode(),model);
		  }
		}catch(DAOException e){
		  e.printStackTrace(); 		  
		}catch(ConnectionException ex){
		  ex.printStackTrace();	
		}
		finally{
		  dbManager.freeConnection();
		}
	  }
	  
	  public String getParamValue(String paramCode){
	  	if(paramList.get(paramCode)!=null){
	  		ParamConfigVO model = (ParamConfigVO)paramList.get(paramCode); 
	  		if(model.getParamValue()==null) return "";
		  return model.getParamValue();
	  	}else{
	  	  return "";
	  	}
	  }
	  
	  public String getParamValue(String paramCode,String defaultValue){
		  	if(paramList.get(paramCode)!=null){
		  	  ParamConfigVO model = (ParamConfigVO)paramList.get(paramCode); 
			  return model.getParamValue();
		  	}else{
		  	  return defaultValue;
		  	}
	  }
	  
	  public void refresh(){
	  	this.init();
	  }
}
