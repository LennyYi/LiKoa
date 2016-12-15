package com.aiait.eflow.housekeeping.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import com.aiait.eflow.housekeeping.vo.CompanyVO;
import com.aiait.eflow.util.FieldUtil;
import com.aiait.framework.db.BaseDAOImpl;
import com.aiait.framework.db.IDBManager;
import com.aiait.framework.exception.DAOException;
import com.aiait.framework.vo.BaseVO;

public class CompanyDAO extends BaseDAOImpl {
	
	public CompanyDAO(IDBManager dbManager){
		super(dbManager);
	}
	
	/**
	 * 获取指定公司及其以下的所有公司的集合
	 * @param orgId
	 * @return
	 * @throws DAOException
	 */
	public Collection getCompanyList(String orgId)throws DAOException{
		String sql = "select * from teflow_company where 1=1 ";
		if(orgId!=null && !"".equals(orgId)){
			sql = sql + " and org_id in ('" + orgId + "'";
			String subOrgs = getSubCompanys(orgId);
			if(subOrgs!=null && subOrgs.length()>0){
				sql = sql + "," + subOrgs;
			}
//Hardcode for AIA SH to access CHO
			if(orgId.equals("Z07002")){
				sql = sql + ",'Z07003'";
			}
			
			sql = sql + ")";
		}
		Collection list = dbManager.query(sql);
		Collection result = new ArrayList(); 
		if(list!=null && list.size()>0){
			Iterator it = list.iterator();
			while(it.hasNext()){
				HashMap map = (HashMap)it.next();
				CompanyVO vo = convertMapTOVO(map);
				result.add(vo);
			}
		}
		return result;
	}
	
	private CompanyVO convertMapTOVO(HashMap map){
		CompanyVO vo = new CompanyVO();
		vo.setOrgId(FieldUtil.convertSafeString(map,"ORG_ID"));
		vo.setOrgName(FieldUtil.convertSafeString(map,"ORG_NAME"));
		vo.setCompanyCode(FieldUtil.convertSafeString(map,"COMPANY_CODE"));
		vo.setParentOrgId(FieldUtil.convertSafeString(map,"PARENT_ORG_ID"));
		vo.setCityCode(FieldUtil.convertSafeString(map,"CITY_CODE"));
		return vo;
	}

	
	public String getSubCompanys(String orgId) throws DAOException {
		String str ="";		
		String[] arr = getSubCompany(orgId);		
		if (arr==null || arr.length==0){			
			return str;
		}
		String temp = "";
		for(int i=0;i<arr.length;i++){
			str = str +",'"+ arr[i]+"'";
			temp = getSubCompanys(arr[i]);
			if(temp!=null && !"".equals(temp)){
				str = str + "," + temp;
			}
		}			
		if(str.length()>0){
			str = str.substring(1,str.length());
			if(str.substring(str.length()).equals(",")){
				str = str.substring(0,str.length()-1);
			}
		}

		return str;
	}
	
	private String[] getSubCompany(String orgId)throws DAOException{
		String sql = "select * from teflow_company where parent_org_id='" + orgId + "' ";
		Collection list = dbManager.query(sql);
		if(list==null || list.size()==0) return null;
		String[] orgIds = new String[list.size()];
		Iterator it = list.iterator();
		int index = 0;
		while(it.hasNext()){
			HashMap map = (HashMap)it.next();
			orgIds[index] = (String)map.get("ORG_ID");
			index++;
		}
		return orgIds;
	}
	
	/**
	 * 获取某个公司的所有上级公司（包括其自己）
	 * @param orgId
	 * @return 
	 * @throws DAOException
	 */
	public String getSuperCompanys(String orgId)throws DAOException{
		String sql = "select parent_org_id,org_id from teflow_company where org_id='" + orgId + "' ";
		Collection list = dbManager.query(sql);
		if(list==null || list.size()==0) return "";
		String result = "";
		String parentOrgId = "";
		HashMap map = (HashMap)list.iterator().next();
		parentOrgId = (String)map.get("PARENT_ORG_ID");
		result = "'"+(String)map.get("ORG_ID")+"'";
		if(parentOrgId!=null && !"root".equals(parentOrgId)){
			String temp = getSuperCompanys(parentOrgId);
			if(temp!=null && !"".equals(temp)){
			  result = result +"," + temp;
			}
		}
		//System.out.println(result);
		return result;
	}
	
	public int delete(BaseVO arg0) throws DAOException {
		// TODO Auto-generated method stub
		return 0;
	}

	public int save(BaseVO arg0) throws DAOException {
		// TODO Auto-generated method stub
		return 0;
	}

	public int update(BaseVO arg0) throws DAOException {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public static void main(String[] args){
		String[] s = new String[]{"Z007","Z008","Z009"};
		String l = "";
		for(int i=0;i<s.length;i++){
			l = l + ",'" + s[i] + "'";
		}
		if(l.length()>0){
			l = l.substring(1,l.length());
		}
		System.out.println(l);
	}

}
