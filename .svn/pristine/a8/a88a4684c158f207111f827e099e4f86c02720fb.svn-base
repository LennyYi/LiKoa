package com.aiait.eflow.common.helper;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import com.aiait.eflow.housekeeping.dao.OptionDAO;
import com.aiait.eflow.housekeeping.vo.OptionVO;
import com.aiait.framework.db.ConnectionException;
import com.aiait.framework.db.DBManagerFactory;
import com.aiait.framework.db.IDBManager;
import com.aiait.framework.exception.DAOException;

public class OptionHelper {
   private static OptionHelper helper = null;
   private static HashMap helperMap = new HashMap();
   
   private OptionHelper(){
	   init();
   }
   
   public static OptionHelper getInstance(){
	   if(helper==null){
		   synchronized(OptionHelper.class){
		      if(helper==null){
			    helper = new OptionHelper();
		      }
		   }
	   }
	   return helper;
   }
   
   public void init(){
	   this.helperMap.clear();
	   IDBManager dbManager = null;
		try {
			dbManager = DBManagerFactory.getDBManager();
			OptionDAO dao = new OptionDAO(dbManager);
			Collection list = dao.getOptionList();
			if(list!=null){
				Iterator it = list.iterator();
				while(it.hasNext()){
					OptionVO vo = (OptionVO)it.next();
					if(!this.helperMap.containsKey(vo.getFieldId().toUpperCase())){
						HashMap optionMap = new HashMap();
						optionMap.put(vo.getOptionValue(),vo.getOptionLabel());
						this.helperMap.put(vo.getFieldId().toUpperCase(),optionMap);
					}else{
						HashMap optionMap = (HashMap)this.helperMap.get(vo.getFieldId().toUpperCase());
						optionMap.put(vo.getOptionValue(),vo.getOptionLabel());
					}
				}
			}
		}catch (DAOException e) {
			e.printStackTrace();
		} catch (ConnectionException ex) {
			ex.printStackTrace();
		} finally {
			dbManager.freeConnection();
		}
   }
   
   public HashMap getFieldMap(String fieldId){
	   if(this.helperMap!=null && this.helperMap.containsKey(fieldId.toUpperCase())){
		   return (HashMap)this.helperMap.get(fieldId.toUpperCase());
	   }
	   return null;
   }
   
   public String getOptionLabel(String fieldId,String optionValue){
	   if(this.helperMap!=null && this.helperMap.containsKey(fieldId.toUpperCase())){
		   HashMap optionMap = (HashMap)this.helperMap.get(fieldId.toUpperCase());
		   if(optionMap!=null && optionMap.containsKey(optionValue)){
			   return (String)optionMap.get(optionValue);
		   }else{
			   return "";
		   }
	   }
	   return "";
   }
   
   public void refresh(){
	   init();
   }
}
