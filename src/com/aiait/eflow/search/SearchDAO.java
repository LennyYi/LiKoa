package com.aiait.eflow.search;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.aiait.eflow.common.CommonName;
import com.aiait.eflow.common.helper.ParamConfigHelper;
import com.aiait.eflow.formmanage.vo.FormSectionFieldVO;
import com.aiait.eflow.util.FieldUtil;
import com.aiait.eflow.util.StringUtil;
import com.aiait.framework.db.BaseDAOImpl;
import com.aiait.framework.db.ConnectionException;
import com.aiait.framework.db.DBManagerFactory;
import com.aiait.framework.db.IDBManager;
import com.aiait.framework.exception.DAOException;
import com.aiait.framework.vo.BaseVO;

public class SearchDAO extends BaseDAOImpl {
	private static final String PROCEDURENAME="TEFLOW_FORM_PROCE";
    public SearchDAO(IDBManager dbManager) {
        super(dbManager);
    }
    
    public void executeSQL(String sql) throws DAOException {
        dbManager.executeUpdate(sql);
    }
    
    private String getProName(String sessionID){
    	 Map proNameMap=null;
    	 IDBManager dbManager =null;
    	try {
    		
    		dbManager= DBManagerFactory.getDBManager();
			String sql = "select teflow_form_proce from teflow_form_section  where  section_id='" +sessionID + "'";
			 Collection col = dbManager.query(sql);
		    for(Iterator it = col.iterator();it.hasNext();){
		    	proNameMap= (HashMap<String,String>)it.next();
		    }
		    if(proNameMap.size()>1){
		    	throw new Exception("section_id not unique");
		    }
		} catch (ConnectionException e) {
			e.printStackTrace();
		} catch (DAOException e) {
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
    	finally{
			if (dbManager != null)
	            dbManager.freeConnection();
		}
    	return (String)proNameMap.get(PROCEDURENAME);
    }
    
    private Map<String,String> getProNames(){
   	Map proNameMap=new HashMap();
 	IDBManager dbManager=null;
 	Iterator it =null;
 	Map proMaps = new HashMap();
   	try {
   		dbManager = DBManagerFactory.getDBManager();
			String sql = "select section_id,teflow_form_proce from teflow_form_section where  form_system_id in ('20272','20273','20274','20275');";
			Collection col= dbManager.query(sql);
		    for(it= col.iterator();it.hasNext();){
		    	proNameMap = (HashMap)it.next();
		    	Object sidKey =null;
		    	Object proceVal=null;
		    	for (Object key : proNameMap.keySet()) {
	    			  String keyStr = key.toString();
	    			  if("section_id".equalsIgnoreCase(keyStr)){
	    				  sidKey = proNameMap.get(keyStr);
	    			  }else if("teflow_form_proce".equalsIgnoreCase(keyStr)){
	    				  proceVal = proNameMap.get(keyStr);
	    			  }
	    		}
					proMaps.put((String) sidKey, (String) proceVal);
		    }
		} catch (ConnectionException e) {
			e.printStackTrace();
		} catch (DAOException e) {
			e.printStackTrace();
		}finally{
			if (dbManager != null)
	            dbManager.freeConnection();
		}
   	  return proMaps;
   }
    
    public Map prepareSearchDetailData(Map<String,String[]> paramMaps) throws DAOException{
    	String searchType = paramMaps.get("searchType")[0];
    	String policyNumber = paramMaps.get("policyNumber")[0];
    	Map<String,Collection> detailData = new HashMap();
			if(SearchConstant.SEARCH_TYPE_POLICY.equalsIgnoreCase(searchType)){
	    		List<String> paramList = new ArrayList<String>();
	    		paramList.add(SearchConstant.SESSION_ID_POLICY_BASE);
	    		paramList.add(policyNumber);
	    		detailData.put(SearchConstant.SESSION_ID_POLICY_BASE, dbManager.prepareCall2(getProName(SearchConstant.SESSION_ID_POLICY_BASE), paramList));
	    	}else if(SearchConstant.SEARCH_TYPE_MEMBER.equalsIgnoreCase(searchType)){
	        	String clientCode = paramMaps.get("clientCode")[0];
	        	String deptCode = paramMaps.get("deptCode")[0];
	        	String certNo = paramMaps.get("certNo")[0];
	        	List<String> paramList = new ArrayList<String>();
	        	paramList.add(SearchConstant.SESSION_ID_MEMB_POLI_BASE);
	    		paramList.add(policyNumber);
	        	paramList.add(certNo);
	        	paramList.add(clientCode);
	        	paramList.add(deptCode);
	    		detailData.put(SearchConstant.SESSION_ID_MEMB_POLI_BASE, dbManager.prepareCall2(getProName(SearchConstant.SESSION_ID_MEMB_POLI_BASE), paramList));
	    		paramList.set(0, SearchConstant.SESSION_ID_MEMB_BASE);
	    		detailData.put(SearchConstant.SESSION_ID_MEMB_BASE, dbManager.prepareCall2(getProName(SearchConstant.SESSION_ID_MEMB_BASE), paramList));
	    	}
	    	List<String> paramList = new ArrayList<String>();
			paramList.add(SearchConstant.SESSION_ID_CLAIM_BASE);
			paramList.add(policyNumber);
	    	detailData.put(SearchConstant.SESSION_ID_CLAIM_BASE, dbManager.prepareCall2(getProName(SearchConstant.SESSION_ID_CLAIM_BASE), paramList));
       	return detailData;
    }

    public Collection getSearchDetailDataByPaging(Map<String,String[]> paramMaps) throws DAOException{
   
    	String sectionId = paramMaps.get("sectionId")[0];
    	String policyNumber = paramMaps.get("policyNumber")[0];
    	List<String> paramList = new ArrayList<String>();
		paramList.add(sectionId);
		paramList.add(policyNumber);
		
    	Collection list = null;
    	if(SearchConstant.SESSION_ID_BILL_PAY.equals(sectionId) || SearchConstant.SESSION_ID_BILL_LIST.equals(sectionId)){
    		list = dbManager.prepareCall2(getProName(SearchConstant.SESSION_ID_BILL_PAY), paramList);
    	}else if(SearchConstant.SESSION_ID_POLI_PROD.equals(sectionId) || SearchConstant.SESSION_ID_POLI_MEMB.equals(sectionId)){
    		list = dbManager.prepareCall2(getProName(SearchConstant.SESSION_ID_POLI_PROD), paramList);
    	}else if(SearchConstant.SESSION_ID_CLAIM_LIST.equals(sectionId) || SearchConstant.SESSION_ID_CLAIM_MEMB.equals(sectionId)){
    		list = dbManager.prepareCall2(getProName(SearchConstant.SESSION_ID_CLAIM_LIST), paramList);
    	}else if(SearchConstant.SESSION_ID_COVE_LIST.equals(sectionId)){
        	String clientCode = paramMaps.get("clientCode")[0];
        	String certNo = paramMaps.get("certNo")[0];
        	paramList.add(certNo);
        	paramList.add(clientCode);
        	paramList.add("0");
    		list = dbManager.prepareCall2(getProName(SearchConstant.SESSION_ID_COVE_LIST), paramList);
    	}
    	if(list!=null){
    		for(Iterator it = list.iterator();it.hasNext();){
        		Map element = (Map)it.next();
        		for (Object key : element.keySet()) {
        			  String keyStr = key.toString();
        			  Object val = element.get(keyStr);
        			  if(null==val||"null".trim()==val){
        				element.put(keyStr, "");
        			  }
        			  if(keyStr.endsWith("DAY")||keyStr.endsWith("DATE")){
        				  try {
        					String dateFormat = ParamConfigHelper.getInstance().getParamValue("dateFormat", "MM/dd/yyyy");
        					SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        					SimpleDateFormat df2 = new SimpleDateFormat("yyyy/MM/dd");
        					SimpleDateFormat newDf = new SimpleDateFormat(dateFormat);
    	    					if(val.toString().length()==10){
    	    						Date date1 = df2.parse(val.toString());
    	    						val= newDf.format(date1);
    	    						element.put(keyStr, val);
    	    					}else{
    	    						Date date2 = df1.parse(val.toString());
    	    						val =  newDf.format(date2);
    	    						element.put(keyStr, val);
    	    					}
    						} catch (ParseException e) {
    			            	e.printStackTrace();
    					  }
        			  }
        		}
        	}
    	}
    	
    	return list;
    }
    
	@Override
	public int delete(BaseVO arg0) throws DAOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int save(BaseVO arg0) throws DAOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(BaseVO arg0) throws DAOException {
		// TODO Auto-generated method stub
		return 0;
	}
    
}
