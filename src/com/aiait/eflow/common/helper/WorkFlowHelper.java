package com.aiait.eflow.common.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import com.aiait.eflow.common.CommonName;
import com.aiait.eflow.wkf.dao.WorkFlowDefineDAO;
import com.aiait.eflow.wkf.dao.WorkFlowNodeDAO;
import com.aiait.eflow.wkf.vo.WorkFlowVO;
import com.aiait.framework.db.DBManagerFactory;
import com.aiait.framework.db.IDBManager;

public class WorkFlowHelper {
  private static WorkFlowHelper helper = null;
  //private static Collection workFlowList = new ArrayList();
  private static HashMap workFlowMap = new HashMap();
  
  private WorkFlowHelper(){
	  initData();
  }
  
  public void refresh(){
	  initData();
  }
  
  public static WorkFlowHelper getInstance(){
	  if(helper==null){
		  synchronized(WorkFlowHelper.class){
			  if(helper==null){
			    helper = new WorkFlowHelper();
			  }
		  }
	  }
	  return helper;
  }
  
  public WorkFlowVO getFlow(int formSystemId){
	 // if(workFlowMap.containsKey(flowId+CommonName.STRING_JOIN_FLAG+formSystemId)){
	  //保证一个form只能有一个流程
	  if(workFlowMap.containsKey(""+formSystemId)){
	    return (WorkFlowVO)workFlowMap.get(""+formSystemId);
	  }
	  return null;
  }
  
  private void initData(){
	  workFlowMap.clear();
	  IDBManager dbManager = null;
		try{
		    dbManager =  DBManagerFactory.getDBManager();
		    WorkFlowDefineDAO workDao = new WorkFlowDefineDAO(dbManager);
		    WorkFlowNodeDAO nodeDao = new WorkFlowNodeDAO(dbManager);
		    Collection list = workDao.getAllFlow();
		    if(list!=null && list.size()>0){
		    	Iterator it = list.iterator();
		    	while(it.hasNext()){
		    		WorkFlowVO flow = (WorkFlowVO)it.next();
		    		Collection nodeList = nodeDao.getNodeListByFlow(flow.getFlowBaseId());
		    		flow.setItemList(nodeList);
		    		workFlowMap.put(""+flow.getFormSystemId(),flow);
		    	}
		    }
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(dbManager!=null) dbManager.freeConnection();
		}
  }
  
  
	
}
