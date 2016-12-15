package com.aiait.eflow.common.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import com.aiait.eflow.housekeeping.dao.CompanyDAO;
import com.aiait.eflow.housekeeping.vo.CompanyVO;
import com.aiait.framework.db.DBManagerFactory;
import com.aiait.framework.db.IDBManager;

public class CompanyHelper {
	private static CompanyHelper helper = null;
	private static Collection companyList = new ArrayList();
	private static Collection ownCompanyList = new ArrayList();
	private static HashMap companyMap = new HashMap();

	private static String upperCompanyIds = "";
	private static String lowerCompanyIds = "";

	public static final String EFlow_AIAIT = "AIAIT";
	public static final String EFlow_AIA_CHINA = "AIA_CHINA";
	public static final String EFlow_AIA = "AIA";

	private CompanyHelper() {
		init();
	}

	public void setInstanceNull() {
		clear();
		this.helper = null;
	}

	private void init() {
		IDBManager dbManager = null;
		try {
			dbManager = DBManagerFactory.getDBManager();
			CompanyDAO dao = new CompanyDAO(dbManager);
			companyList.clear();
			companyMap.clear();
			companyList = dao.getCompanyList("");
			if (companyList != null) {
				Iterator it = companyList.iterator();
				while (it.hasNext()) {
					CompanyVO vo = (CompanyVO) it.next();
					companyMap.put(vo.getOrgId(), vo);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (dbManager != null)
				dbManager.freeConnection();
		}
	}

	public static CompanyHelper getInstance() {
		if (helper == null) {
			synchronized (CompanyHelper.class) {
				if (helper == null) {
					helper = new CompanyHelper();
				}
			}
		}
		return helper;
	}

	public Collection getCompanyList() {
		return companyList;
	}

	/**
	 * public Collection getOwnCompanyList(String orgId){
	 * if(ownCompanyList!=null && ownCompanyList.size()>0){ return
	 * ownCompanyList; } IDBManager dbManager = null; try{ dbManager =
	 * DBManagerFactory.getDBManager(); CompanyDAO dao = new
	 * CompanyDAO(dbManager); ownCompanyList.clear(); ownCompanyList =
	 * dao.getCompanyList(orgId); }catch(Exception e){ e.printStackTrace();
	 * }finally{ if(dbManager!=null) dbManager.freeConnection(); } return
	 * ownCompanyList; }
	 */

	/**
	 * 获取该公司的所有上级公司(包括自己）
	 * 
	 * @param orgId
	 * @return 所有上级公司的字符串，形如：'0001','0002'
	 * 
	 * public String getUpperCompany(String orgId){ if(upperCompanyIds!=null &&
	 * upperCompanyIds.length()>0){ return upperCompanyIds; } IDBManager
	 * dbManager = null; try{ dbManager = DBManagerFactory.getDBManager();
	 * CompanyDAO dao = new CompanyDAO(dbManager); upperCompanyIds = "";
	 * upperCompanyIds = dao.getSuperCompanys(orgId); }catch(Exception e){
	 * e.printStackTrace(); }finally{ if(dbManager!=null)
	 * dbManager.freeConnection(); } return upperCompanyIds; }
	 */

	/**
	 * 获取该公司的所有下级公司（包括自己）
	 * 
	 * @param orgId
	 * @return 所有下级公司的字符串，形如：'0001','0002'
	 * 
	 * public String getLowerCompany(String orgId){ if(lowerCompanyIds!=null &&
	 * lowerCompanyIds.length()>0){ return lowerCompanyIds; } IDBManager
	 * dbManager = null; try{ dbManager = DBManagerFactory.getDBManager();
	 * CompanyDAO dao = new CompanyDAO(dbManager); lowerCompanyIds =
	 * "'"+orgId+"'"; String temp = dao.getSubCompanys(orgId); if(temp!=null &&
	 * !"".equals(temp)){ lowerCompanyIds = lowerCompanyIds + ","+temp; }
	 * }catch(Exception e){ e.printStackTrace(); }finally{ if(dbManager!=null)
	 * dbManager.freeConnection(); } return lowerCompanyIds; }
	 */

	public void clear() {
		companyList.clear();
		companyMap.clear();
		ownCompanyList.clear();
		upperCompanyIds = "";
		lowerCompanyIds = "";
	}

	public String getOrgName(String orgId) {
		if (companyMap != null && companyMap.containsKey(orgId)) {
			return ((CompanyVO) companyMap.get(orgId)).getOrgName();
		}
		return orgId;
	}

	public CompanyVO getCompany(String orgId) {
		if (companyMap != null && companyMap.containsKey(orgId)) {
			return (CompanyVO) companyMap.get(orgId);
		}
		return null;
	}

	public void refresh() {
		clear();
		init();
	}

	public String getEFlowCompany() {
		// This is a temporary solution.
		String paramCompanyName = ParamConfigHelper.getInstance()
				.getParamValue("company_name").toUpperCase();

		if (paramCompanyName.indexOf("友邦资讯") != -1
				|| paramCompanyName.indexOf("AIAIT") != -1
				|| paramCompanyName.indexOf("AIA INFORMATION TECHNOLOGY") != -1) {
			// System.out.println(EFlow_AIAIT);
			return EFlow_AIAIT;
		}

		if (paramCompanyName.indexOf("友邦保险") != -1
				|| paramCompanyName.indexOf("AIA CHINA") != -1) {
			// System.out.println(EFlow_AIA_CHINA);
			return EFlow_AIA_CHINA;
		}

		return EFlow_AIA;
	}

}
