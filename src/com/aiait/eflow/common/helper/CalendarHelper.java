package com.aiait.eflow.common.helper;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;
import com.aiait.eflow.housekeeping.dao.HolidayDAO;
import com.aiait.framework.db.DBManagerFactory;
import com.aiait.framework.db.IDBManager;
import com.aiait.framework.exception.DAOException;


public class CalendarHelper {
    public static String getholiday(String strYear, String strMonth){
    	String result=",";
    	
    	IDBManager dbManager = null;
		try{
		  dbManager =  DBManagerFactory.getDBManager();
		  HolidayDAO dao = new HolidayDAO(dbManager);
		  Collection arrday = (ArrayList)dao.getHolidayList(strYear, strMonth);
		  if (arrday!=null){
			  Iterator listIt = arrday.iterator();
	          while(listIt.hasNext()){
	        	  result += (String)listIt.next()+",";
	          }
		  }
		  
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(dbManager!=null) dbManager.freeConnection();
		}    	
    	return result;
    }
}
