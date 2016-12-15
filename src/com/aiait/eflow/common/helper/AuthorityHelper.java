/*IT1280	Mario	02/10/2011	Multiple roles for a user*/
package com.aiait.eflow.common.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import com.aiait.eflow.common.CommonName;
import com.aiait.eflow.housekeeping.dao.RoleDAO;
import com.aiait.eflow.housekeeping.vo.RoleVO;
import com.aiait.framework.db.ConnectionException;
import com.aiait.framework.db.DBManagerFactory;
import com.aiait.framework.db.IDBManager;
import com.aiait.framework.exception.DAOException;


public class AuthorityHelper {
	private static AuthorityHelper instance = null;

	private static HashMap funcMap = new HashMap();  //key--roleId, value---functionList
	private static HashMap formTypeFuncMap = new HashMap(); //key--roleId, value---formTypeFunctionList
	private static HashMap reportTypeFuncMap = new HashMap();

	private AuthorityHelper() {
		init();
	}

    public void setInstanceNull(){
    	funcMap.clear();
    	formTypeFuncMap.clear();
    	this.instance = null;
    }
    
    /**
     * 初始化所有权限数据
     *
     */
    private void init(){
    	IDBManager dbManager = null;
    	funcMap.clear();
    	formTypeFuncMap.clear();
		try {
			dbManager = DBManagerFactory.getDBManager();
			RoleDAO dao = new RoleDAO(dbManager);
			Collection roleList = dao.getAllRole();
			if(roleList==null || roleList.size()==0) return;
			Iterator roleIt = roleList.iterator();
			while(roleIt.hasNext()){
				RoleVO role = (RoleVO)roleIt.next();
				Collection roleFunctions = dao.getFunctionsByRole(role.getRoleId());
				Collection roleFormTypeFunctions = dao.getFormTypeFunctionsByRole(role.getRoleId());
				funcMap.put(role.getRoleId(),roleFunctions);
				formTypeFuncMap.put(role.getRoleId(),roleFormTypeFunctions);
			}
		} catch (DAOException e) {
			e.printStackTrace();
		} catch (ConnectionException ex) {
			ex.printStackTrace();
		} finally {
			dbManager.freeConnection();
		}
    }

	public static AuthorityHelper getInstance(){
		if (instance == null) {
			synchronized(AuthorityHelper.class){
			  if(instance == null){
			    instance = new AuthorityHelper();
			  }
			}
		} 
		return instance;
	}
	
	public boolean checkAuthority(String roleId,int moduleId,int operateId){
		if(roleId==null) return false;
		String[] roleMatrix = roleId.split(CommonName.MODULE_ROLE_SPLIT_SIGN);
		
		for(int i=0;i<roleMatrix.length;i++){
			if(funcMap.containsKey(roleMatrix[i])){
				Collection functionList = (ArrayList)funcMap.get(roleMatrix[i]);
				if(functionList.contains(moduleId+CommonName.MODULE_ROLE_SPLIT_SIGN+operateId)){
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean checkAuthorityByFormType(String roleId,int moduleId,String formType){
		if(roleId==null) return false;
		String[] roleMatrix = roleId.split(CommonName.MODULE_ROLE_SPLIT_SIGN);

		for(int i=0;i<roleMatrix.length;i++){
			if(formTypeFuncMap.containsKey(roleMatrix[i])){
				Collection formTypeFunctionList = (ArrayList)formTypeFuncMap.get(roleMatrix[i]);
				if(formTypeFunctionList.contains(moduleId+CommonName.MODULE_ROLE_SPLIT_SIGN+formType)){
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean checkAuthorityByReportType(String roleId,int moduleId,String reportType){
		if(roleId==null) return false;
		String[] roleMatrix = roleId.split(CommonName.MODULE_ROLE_SPLIT_SIGN);

		for(int i=0;i<roleMatrix.length;i++){
			if(reportTypeFuncMap.containsKey(roleMatrix[i])){
				Collection reportTypeFunctionList = (ArrayList)reportTypeFuncMap.get(roleMatrix[i]);
				if(reportTypeFunctionList.contains(moduleId+CommonName.MODULE_ROLE_SPLIT_SIGN+reportType)){
					return true;
				}
			}
		}
		return false;
	}
	
	public void refresh(){
		funcMap.clear();
		formTypeFuncMap.clear();
		init();
	}
}
