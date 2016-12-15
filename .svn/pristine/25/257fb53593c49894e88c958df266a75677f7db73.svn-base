package com.aiait.eflow.common.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import com.aiait.eflow.basedata.dao.BaseDataDAO;
import com.aiait.eflow.basedata.vo.BaseDataVO;
import com.aiait.eflow.formmanage.vo.DictionaryDataVO;
import com.aiait.eflow.housekeeping.dao.ApproverGroupDAO;
import com.aiait.eflow.housekeeping.dao.SynchronizePMADAO;
import com.aiait.eflow.housekeeping.dao.SystemFieldDAO;
import com.aiait.framework.db.DBManagerFactory;
import com.aiait.framework.db.IDBManager;
import com.aiait.framework.exception.DAOException;

public class BaseDataHelper {
private static Collection masterList = new ArrayList();
   private static HashMap detailMap = new HashMap();
   private static Collection systemFieldList = new ArrayList();
   private static Collection ApproverGroupList = new ArrayList();
   
   private static BaseDataHelper helper = null;
   
   public  HashMap getDetailMap() {
	 return detailMap;
   }

   public  void setDetailMap(HashMap detailMap) {
	 BaseDataHelper.detailMap = detailMap;
   }

   private BaseDataHelper(){
	   initApproverGroup();
	   initSystemField();
	   initDetail();
   }
   
   public static BaseDataHelper getInstance(){
	   if(helper==null){
		   synchronized(BaseDataHelper.class){
		      if(helper==null){
			    helper = new BaseDataHelper();
		      }
		   }
	   }
	   return helper;
   }
	
   private  void initSystemField(){
	   IDBManager dbManager = null;
		try{
		  dbManager =  DBManagerFactory.getDBManager();
		  SystemFieldDAO dao = new SystemFieldDAO(dbManager);
		  systemFieldList = dao.getAllField();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(dbManager!=null) dbManager.freeConnection();
		}
   }
   
   private  void initApproverGroup(){
	   IDBManager dbManager = null;
	   ApproverGroupList.clear();
		try{
		  dbManager =  DBManagerFactory.getDBManager();
		  ApproverGroupDAO dao = new ApproverGroupDAO(dbManager);
		  SynchronizePMADAO synDao = new SynchronizePMADAO(dbManager);
		  synDao.synchTeamLeader(); //首先同步PMA数据中的tl记录
		  //synDao.sychNewStaff();    //再同步新来的员工，给与一个默认的角色.  //2008-09-05 Robin 由于
		  ApproverGroupList = dao.getApproverGroupList();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(dbManager!=null) dbManager.freeConnection();
		}
   }
   
   public Collection getSystemFieldList(){
	   return systemFieldList;
   }
   
   public Collection getApproverGroupList(){
	   return ApproverGroupList;
   }
   
   private  void initDetail(){
	   
	   IDBManager dbManager = null;
		try{
		  dbManager =  DBManagerFactory.getDBManager();
		  BaseDataDAO baseDataDao = new BaseDataDAO(dbManager);
		  Collection fieldBaseDataList = baseDataDao.getAllFieldBaseData();
		  if(fieldBaseDataList!=null && fieldBaseDataList.size()>0){
			  Iterator it = fieldBaseDataList.iterator();
			  while(it.hasNext()){
				  BaseDataVO masterVo = (BaseDataVO)it.next();
				  Collection tmpList = new ArrayList();
				  Collection ObsoleteList = new ArrayList();//IT1321
				  Collection detailList = baseDataDao.getDetailData(masterVo.getMasterId());
				  if(detailList!=null && detailList.size()>0){
					  Iterator detailIt = detailList.iterator();
					  while(detailIt.hasNext()){
						  BaseDataVO detailVo = (BaseDataVO)detailIt.next();
						  DictionaryDataVO tmpVo = new DictionaryDataVO();
						  tmpVo.setId(detailVo.getOptionValue());
						  tmpVo.setValue(detailVo.getOptionLabel());
						  if(! "T".equalsIgnoreCase(detailVo.getStatus())){
							  tmpList.add(tmpVo);
						  } else {
							  ObsoleteList.add(tmpVo);
						  }
					  }
					  detailMap.put(masterVo.getFormSystemId()+"&"+masterVo.getSectionId()+"&"+masterVo.getFieldId(),tmpList);
					  //IT1321
					  if(ObsoleteList.size()>0)detailMap.put(masterVo.getFormSystemId()+"&"+masterVo.getSectionId()+"&"+masterVo.getFieldId()+"&O",ObsoleteList);
				  }
			  }
		  }
		  
		  Collection list = baseDataDao.getAllMasterData();
		  if(list!=null && list.size()>0){
			  Iterator it = list.iterator();
			  while(it.hasNext()){
				  BaseDataVO masterVo = (BaseDataVO)it.next();
				  masterList.add(masterVo);
				  Collection tmpList = new ArrayList();
				  Collection detailList = baseDataDao.getDetailData(masterVo.getMasterId());
				  if(detailList!=null && detailList.size()>0){
					  Iterator detailIt = detailList.iterator();
					  while(detailIt.hasNext()){
						  BaseDataVO detailVo = (BaseDataVO)detailIt.next();
						  DictionaryDataVO tmpVo = new DictionaryDataVO();
						  tmpVo.setId(detailVo.getOptionValue());
						  tmpVo.setValue(detailVo.getOptionLabel());
						  tmpList.add(tmpVo);
					  }
					  detailMap.put(masterVo.getMasterId()+masterVo.getFieldName(),tmpList);
					  detailMap.put("" + masterVo.getMasterId(), tmpList);
				  }
			  }
		  }		  
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(dbManager!=null) dbManager.freeConnection();
		}
   }
   
   public  Collection getMasterList(){
	   return masterList;
   }
   
   public  Collection getOptionList(long masterId,String fieldName){
	   return (ArrayList)detailMap.get(masterId+fieldName);
   }
   
   public  String getLabelValue(String masterId,String fieldName,String key){
	   if(key==null)return "";
	   Collection list = (ArrayList)detailMap.get(masterId+fieldName);
	   if(list==null || list.size()==0) return "";
	   Iterator it = list.iterator();
	   while(it.hasNext()){
		   DictionaryDataVO tmpVo = (DictionaryDataVO)it.next();
		   if((key).equals(tmpVo.getId())){
			   return tmpVo.getValue();
		   }
	   }
	   return "";
   }
   
   public void refreshMasterData(long masterId,long formSystemId,String sectionId,String fieldId){
	   IDBManager dbManager = null;
		try{
		  dbManager =  DBManagerFactory.getDBManager();
		  BaseDataDAO baseDataDao = new BaseDataDAO(dbManager);
		  Collection tmpList = new ArrayList();
		  Collection ObsoleteList = new ArrayList();//IT1321
		  Collection detailList = baseDataDao.getDetailData(masterId);
		  if(detailList!=null && detailList.size()>0){
			  Iterator detailIt = detailList.iterator();
			  while(detailIt.hasNext()){
				  BaseDataVO detailVo = (BaseDataVO)detailIt.next();
				  DictionaryDataVO tmpVo = new DictionaryDataVO();
				  tmpVo.setId(detailVo.getOptionValue());
				  tmpVo.setValue(detailVo.getOptionLabel());
				  if(! "T".equalsIgnoreCase(detailVo.getStatus())){
					  tmpList.add(tmpVo);
				  } else {
					  ObsoleteList.add(tmpVo);
				  }
			  }
			  if(detailMap.containsKey(formSystemId+"&"+sectionId+"&"+fieldId)){
				detailMap.remove(formSystemId+"&"+sectionId+"&"+fieldId);
				detailMap.remove(formSystemId+"&"+sectionId+"&"+fieldId+"&O");
			  }
			  detailMap.put(formSystemId+"&"+sectionId+"&"+fieldId,tmpList);
			  if(ObsoleteList.size()>0)detailMap.put(formSystemId+"&"+sectionId+"&"+fieldId+"&O",ObsoleteList);//IT1321
		  }
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(dbManager!=null) dbManager.freeConnection();
		}
   }
   
   public  void refreshApproverGroup(){
	   initApproverGroup();
   }
   
   public  void refresh(){
	   detailMap.clear();
	   masterList.clear();
	   systemFieldList.clear();
	   ApproverGroupList.clear();
	   initApproverGroup();
	   initDetail();
	   initSystemField();
   }
   
}
