/******************************************************************************************/
/*Task_ID	Author	Modify_Date	Description                       						  */
/*IT0973	Young	11/29/2007	DS-001 Eflow User Management      						  */
/*IT1029	Young	06/30/2008	DS-002 Team filter values vary depending on company filter*/
/*IT1303	Mario	07/24/2012	FS-2.5 Add getATStaffListByCompany                        */
/******************************************************************************************/
package com.aiait.eflow.housekeeping.dao;

import java.sql.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import com.aiait.eflow.common.CommonName;
import com.aiait.eflow.housekeeping.vo.EflowStaffVO;
import com.aiait.eflow.housekeeping.vo.RoleVO;
import com.aiait.eflow.housekeeping.vo.StaffVO;
import com.aiait.eflow.util.FieldUtil;
import com.aiait.eflow.util.StringUtil;
import com.aiait.framework.common.DataType;
import com.aiait.framework.db.BaseDAOImpl;
import com.aiait.framework.db.IDBManager;
import com.aiait.framework.exception.DAOException;
import com.aiait.framework.page.PageVO;
import com.aiait.framework.vo.BaseVO;



public class StaffDAO extends BaseDAOImpl {
			
	
	
	public Collection getStaffListByCompanyAndSubCompany(String orgId)throws DAOException{
		StringBuffer sql = new StringBuffer("select s.user_no,s.email,s.logon_id,s.staff_code,s.staff_name,s.status from tpma_staffbasic s where s.status='A' ");
		if(orgId!=null && !"".equals(orgId)){
			sql.append(" and org_id in ("+orgId+") ");
		}
		sql.append(" order by s.staff_name");

		//System.out.println(sql.toString());
		
		Collection list = dbManager.query(sql.toString());
		if(list==null || list.size()==0) return null;                                       
		Collection result = new ArrayList();
		Iterator it = list.iterator();
		while(it.hasNext()){
		   HashMap map = (HashMap)it.next();
		   StaffVO staff = convertRecord(map);
		   result.add(staff);
		}
		return result;
	}
	
	public Collection getStaffListByCompany(String orgId)throws DAOException{
		StringBuffer sql = new StringBuffer("select s.user_no,s.email,s.logon_id,s.staff_code,s.staff_name,s.status from tpma_staffbasic s where s.status='A' ");
		if(orgId!=null && !"".equals(orgId)){
			sql.append(" and org_id='"+orgId+"' ");
		}
		sql.append(" order by s.staff_name");
		Collection list = dbManager.query(sql.toString());
		if(list==null || list.size()==0) return null;                                       
		Collection result = new ArrayList();
		Iterator it = list.iterator();
		while(it.hasNext()){
		   HashMap map = (HashMap)it.next();
		   StaffVO staff = convertRecord(map);
		   result.add(staff);
		}
		return result;
	}
	
	/**
	 * Get active and terminated staffs
	 * */
	public Collection getATStaffListByCompany(String orgId)throws DAOException{
		StringBuffer sql = new StringBuffer("select s.user_no,s.email,s.logon_id,s.staff_code,rtrim(s.staff_name)+replace(' ('+status+')','(A)','') as staff_name, s.status from tpma_staffbasic s where 1=1 ");
		if(orgId!=null && !"".equals(orgId)){
			sql.append(" and org_id='"+orgId+"' ");
		}
		sql.append(" order by status, staff_name ");
		Collection list = dbManager.query(sql.toString());
		if(list==null || list.size()==0) return null;                                       
		Collection result = new ArrayList();
		Iterator it = list.iterator();
		while(it.hasNext()){
		   HashMap map = (HashMap)it.next();
		   StaffVO staff = convertRecord(map);
		   result.add(staff);
		}
		return result;
	}
	
	public Collection getStaffListByTeam(String teamCode)throws DAOException{
		return getStaffListByTeam(teamCode, true);
	}
	
	public Collection getStaffListByTeam(String teamCode, boolean onlyActive)throws DAOException{
		StringBuffer sql = new StringBuffer("select s.user_no,s.email,s.logon_id,s.staff_code,s.staff_name,s.status from tpma_staffbasic s where 1=1 ");
		if(teamCode!=null && !"".equals(teamCode)){
			sql.append(" and team_code='"+teamCode+"' ");
		}
		if(onlyActive){
			sql.append(" and s.status='A' ");
		}
		
		sql.append(" order by s.staff_name");
		Collection list = dbManager.query(sql.toString());
		if(list==null || list.size()==0) return null;                                       
		Collection result = new ArrayList();
		Iterator it = list.iterator();
		while(it.hasNext()){
		   HashMap map = (HashMap)it.next();
		   EflowStaffVO staff = convertERecord(map);
		   result.add(staff);
		}
		return result;
	}	
	
	public Collection getStaffListByStaffName(String staffName, String teamCode, String orgId)throws DAOException{
		StringBuffer sql = new StringBuffer("select s.user_no,s.email,s.logon_id,s.staff_code,s.staff_name,s.status from tpma_staffbasic s where s.status='A' ");
		if(staffName!=null && !"".equals(staffName)){
			sql.append(" and s.staff_name like '%"+staffName+"%' ");
		}
		
		if(teamCode!=null && !"".equals(teamCode)){
			sql.append(" and s.team_code in ("+teamCode+") ");
		}

		if(orgId!=null && !"".equals(orgId)){
			sql.append(" and s.org_id='"+orgId+"' ");
		}
		
		sql.append(" order by s.staff_name");
		//System.out.println(sql.toString());
		Collection list = dbManager.query(sql.toString());
		if(list==null || list.size()==0) return null; 
		Collection result = new ArrayList();
		Iterator it = list.iterator();
		while(it.hasNext()){
		   HashMap map = (HashMap)it.next();
		   StaffVO staff = convertRecord(map);
		   result.add(staff);
		}
		return result;
	}
	
	/**
	 * @return all active staff
	 * @throws DAOException
	 */
	public Collection getAllStaff()throws DAOException{
		StringBuffer sql = new StringBuffer("select s.user_no,s.email,s.logon_id,s.staff_code,s.staff_name, s.chinese_name, s.status, s.team_code, s.org_id from tpma_staffbasic s where s.status='A' order by s.staff_name");
		Collection list = dbManager.query(sql.toString());
		if(list==null || list.size()==0) return null;
		Collection result = new ArrayList();
		Iterator it = list.iterator();
		while(it.hasNext()){
		   HashMap map = (HashMap)it.next();
		   StaffVO staff = convertRecord(map);
		   result.add(staff);
		}
		return result;
	}
	
	/**
	 * @return all Active and Terminated staff.
	 * @throws DAOException
	 */
	public Collection getAllATStaff()throws DAOException{
		StringBuffer sql = new StringBuffer("select s.user_no,s.email,s.logon_id,s.staff_code,s.staff_name, s.chinese_name, s.status, s.team_code, s.org_id from tpma_staffbasic s order by s.staff_name");
		Collection list = dbManager.query(sql.toString());
		if(list==null || list.size()==0) return null;
		Collection result = new ArrayList();
		Iterator it = list.iterator();
		while(it.hasNext()){
		   HashMap map = (HashMap)it.next();
		   StaffVO staff = convertRecord(map);
		   result.add(staff);
		}
		return result;
	}
	

	private StaffVO convertRecord(HashMap map){
		StaffVO staff = new StaffVO();		
		staff.setStaffCode(FieldUtil.convertSafeString(map,"STAFF_CODE"));
		staff.setStaffName(FieldUtil.convertSafeString(map,"STAFF_NAME"));
		staff.setLogonId(FieldUtil.convertSafeString(map,"LOGON_ID"));
		//if(map.get("team_code")!=null){
		staff.setTeamCode(FieldUtil.convertSafeString(map,"TEAM_CODE"));
		//}
		//if(map.get("team_name")!=null){
			staff.setTeamName(FieldUtil.convertSafeString(map,"TEAM_NAME"));
		//}
	    staff.setEmail(FieldUtil.convertSafeString(map,"EMAIL"));
	    staff.setCurrentRoleId(FieldUtil.convertSafeString(map,"ROLE_ID"));
	    //staff.set
	    
	    staff.setUserType(FieldUtil.convertSafeString(map,"USER_TYPE"));
	    
	    staff.setOrgId(FieldUtil.convertSafeString(map,"ORG_ID"));
	    staff.setChineseName(FieldUtil.convertSafeString(map,"CHINESE_NAME"));
	    staff.setStatus(FieldUtil.convertSafeString(map,"STATUS"));
	    
		return staff;
	}
	
	public StaffVO getStaffByLogonId(String logonId)throws DAOException{
		Pattern p = Pattern.compile("(\\w)*");
		Matcher m = p.matcher(logonId);
		if(!m.matches())throw new DAOException("Logon ID should only contains letters and numbers");
		
		//StringBuffer sql = new StringBuffer("select r.role_id,s.user_no,s.logon_id,s.staff_code,s.staff_name,s.status,s.email,s.team_code team_code,s.org_id org_id,s.user_type,t.team_name team_name from tpma_staffbasic s,tpma_team t,teflow_role_member r  where s.logon_id='"+logonId+"' and s.status='A' and s.team_code=t.team_code and s.staff_code=r.staff_code");
		StringBuffer sql = new StringBuffer("select s.user_no,s.logon_id,s.staff_code,s.staff_name,s.status,s.email,s.team_code team_code,s.org_id org_id,s.user_type,t.team_name team_name from tpma_staffbasic s,tpma_team t where s.logon_id='"+logonId+"' and s.status='A' and s.team_code=t.team_code ");
		Collection list = dbManager.query(sql.toString());
		if(list==null || list.size()==0) return null;
		HashMap map = (HashMap)list.iterator().next();
		StaffVO staff = convertRecord(map);
		
		RoleDAO roleDAO = new RoleDAO(dbManager);
		ArrayList roleList = (ArrayList)roleDAO.getRoleByStaff(getStaffCodeByLogonId(logonId));
		if(roleList==null || roleList.size()==0) return null;
		String roles = ""; 
		Iterator it = roleList.iterator();
		while(it.hasNext()){
			RoleVO vo = (RoleVO)it.next();
			roles = roles + "_" + vo.getRoleId();			
		}
		if(roles.startsWith("_"))roles = roles.substring(1);
		staff.setCurrentRoleId(roles);
			
		return staff;
	}
	
	public String getStaffCodeByLogonId(String logonId) throws DAOException{
		StringBuffer sql = new StringBuffer("select staff_code from tpma_staffbasic where logon_id = '"+logonId+"' order by status");
		Collection list = dbManager.query(sql.toString());
		if(list==null || list.size()==0) return null;
		HashMap map = (HashMap)list.iterator().next();
		return FieldUtil.convertSafeString(map,"STAFF_CODE");
	}
	
	public StaffVO getStaffByDefaultRoel(String logonId)throws DAOException{
		StringBuffer sql = new StringBuffer("select r.role_id,s.user_no,s.logon_id,s.staff_code,s.staff_name,s.status,s.email,s.team_code team_code,s.org_id org_id,s.user_type,t.team_name team_name from tpma_staffbasic s,tpma_team t,teflow_role r  where s.logon_id='"+logonId+"' and s.status='A' and s.team_code=t.team_code and r.role_id='00' ");
		Collection list = dbManager.query(sql.toString());
		if(list==null || list.size()==0) return null;
		HashMap map = (HashMap)list.iterator().next();
		StaffVO staff = convertRecord(map);
		return staff;
	}
	
	private EflowStaffVO convertERecord(HashMap map){	
		EflowStaffVO staff = new EflowStaffVO();
		staff.setStaffCode(FieldUtil.convertSafeString(map,"STAFF_CODE"));
		staff.setStaffName(FieldUtil.convertSafeString(map,"STAFF_NAME"));
		staff.setLogonId(FieldUtil.convertSafeString(map,"LOGON_ID"));
		//if(map.get("team_code")!=null){
		staff.setTeamCode(FieldUtil.convertSafeString(map,"TEAM_CODE"));
		//}
		//if(map.get("team_name")!=null){
			staff.setTeamName(FieldUtil.convertSafeString(map,"TEAM_NAME"));
		//}
		//staff.setVirtualTeamCode(FieldUtil.convertSafeString(map,"VIRTUAL_TEAM_CODE"));
		//staff.setVirtualTeamName(FieldUtil.convertSafeString(map,"VIRTUAL_TEAM_NAME"));
		staff.setEmail(FieldUtil.convertSafeString(map,"EMAIL"));
		staff.setCurrentRoleId(FieldUtil.convertSafeString(map,"ROLE_ID"));	
		staff.setStatus(FieldUtil.convertSafeString(map,"STATUS"));
		staff.setUsertype(FieldUtil.convertSafeString(map,"USER_TYPE"));
		staff.setFromdate(FieldUtil.convertSafeString(map,"FROM_DATE"));
		staff.setTodate(FieldUtil.convertSafeString(map,"TO_DATE"));
		staff.setTitle(""+FieldUtil.convertSafeInt(map,"TITLE_ID",0));
		staff.setTitleName(FieldUtil.convertSafeString(map,"ABREV"));
		staff.setChineseName(FieldUtil.convertSafeString(map,"CHINESE_NAME"));
		staff.setBankDetail(FieldUtil.convertSafeString(map,"BANK_DETAIL"));
		staff.setAccountNo(FieldUtil.convertSafeString(map,"ACCOUNT_NO"));
		staff.setDisplayType(FieldUtil.convertSafeString(map,"DISPLAY_TYPE"));
		staff.setOrgId(FieldUtil.convertSafeString(map,"ORG_ID"));
		staff.setOrgName(FieldUtil.convertSafeString(map,"ORG_NAME"));
		return staff;
	}
	
	public Collection getEflowAllStaff()throws DAOException{		
		//("select a.*,b.team_name as virtual_team_name ");
		StringBuffer sql = new StringBuffer("select s.staff_code,s.staff_name,s.Logonid logon_id,s.status,s.email,");
		sql.append("s.team_code,t.team_name,s.user_type,convert(varchar,s.from_date,101) from_date,convert(varchar,s.to_date,101) to_date,s.chinese_name,s.account_no ")
		   .append("from teflow_user s left join teflow_team t on s.team_code=t.team_code ")
		   .append("where s.status='A'")
		   .append("order by s.staff_name");		
		//System.out.println(sql.toString());
		Collection list = dbManager.query(sql.toString());
		if(list==null || list.size()==0){			
			return null;
		}
		Collection result = new ArrayList();
		Iterator it = list.iterator();
		while(it.hasNext()){
		   HashMap map = (HashMap)it.next();		   
		   EflowStaffVO staff = convertERecord(map);
		   result.add(staff);
		}		
		return result;
	}
	
	public int getTotalRecordsNum(PageVO page)throws DAOException{
		HashMap paramMap = page.getParamMap();
		StringBuffer staffSQL = new StringBuffer("select count(a.staff_code) ");
		//staffSQL.append("(select a.*,b.team_name as virtual_team_name ");
		staffSQL.append("from (select s.title_id,s.staff_code,s.staff_name,s.logon_id logon_id,s.status,s.email,s.team_code,t.team_name,s.virtual_team_code,s.user_type,convert(varchar,s.from_date,101) from_date, convert(varchar,s.to_date,101) to_date,s.chinese_name,s.account_no,s.display_type,s.org_id,c.org_name ")
		        .append("from tpma_staffbasic s left join tpma_team t on s.team_code=t.team_code ")
		        .append("                       left join teflow_company c on s.org_id=c.org_id  ")
		        .append("where 1=1 ");                           
		
		staffSQL.append(makeQueryCondition(paramMap));
		
		staffSQL.append(" ) a left join tpma_title l on a.title_id=l.title_id ");
		//System.out.println(staffSQL.toString());
		int i = dbManager.getRecordCount(staffSQL.toString());
		return i;
	}
	
	public Collection searchStaff(PageVO page)throws DAOException{
		HashMap paramMap = page.getParamMap();
		StringBuffer staffSQL = new StringBuffer("select a.*,l.abrev ");
		//staffSQL.append("(select a.*,b.team_name as virtual_team_name ");
		staffSQL.append("from (select s.title_id,s.staff_code,s.staff_name,s.logon_id logon_id,s.status,s.email,s.team_code,t.team_name,s.virtual_team_code,s.user_type,convert(varchar,s.from_date,101) from_date, convert(varchar,s.to_date,101) to_date,s.chinese_name,s.bank_detail,s.account_no,s.display_type,s.org_id,c.org_name ")
		        .append("from tpma_staffbasic s left join tpma_team t on s.team_code=t.team_code ")
		        .append("                       left join teflow_company c on s.org_id=c.org_id  ")
		        .append("where 1=1 ");                           
		
		staffSQL.append(makeQueryCondition(paramMap));
		
		staffSQL.append(" ) a left join tpma_title l on a.title_id=l.title_id ")
        .append("order by a.staff_name");
		
		//System.out.println(staffSQL.toString());
		
		Collection list = dbManager.query(staffSQL.toString(),page.getPageSize(),page.getCurrentPage());
		
		if(list==null || list.size()==0){			
			return null;
		}
		Collection resultList = new ArrayList();
		Iterator it = list.iterator();
		while(it.hasNext()){
			   HashMap map = (HashMap)it.next();		   
			   EflowStaffVO staff = convertERecord(map);
			   resultList.add(staff);
			}
		return resultList;
	}
	
	// Get staff with role and approver groups.
    public Collection searchStaff2(PageVO page) throws DAOException {
        Collection list = this.searchStaff(page);
        if (list.isEmpty()) {
            return list;
        }

        RoleDAO roleDAO = new RoleDAO(this.dbManager);
        ApproverGroupDAO approverGroupDAO = new ApproverGroupDAO(this.dbManager);

        Iterator it = list.iterator();
        while (it.hasNext()) {
            EflowStaffVO staff = (EflowStaffVO) it.next();
            staff.setRoles(roleDAO.getRoleByStaff(staff.getStaffCode()));
            Collection groups = approverGroupDAO.getApproverGroupByStaff(staff.getStaffCode());
            staff.setApproverGroups(groups == null ? new ArrayList() : groups);
        }
        return list;
    }
	
	public String makeQueryCondition(HashMap paramMap){
		StringBuffer staffSQL = new StringBuffer("");
		if (paramMap!=null && paramMap.get("staffCode")!=null && !"".equals(paramMap.get("staffCode"))){
			staffSQL.append(" and s.STAFF_CODE like '%").append(paramMap.get("staffCode")).append("%'");
		}
		if (paramMap!=null && paramMap.get("logonId")!=null && !"".equals(paramMap.get("logonId"))){
			staffSQL.append(" and s.LOGON_ID like '%").append(paramMap.get("logonId")).append("%'");
		}
		if (paramMap!=null && paramMap.get("staffName")!=null && !"".equals(paramMap.get("staffName"))){
			staffSQL.append(" and s.STAFF_NAME like '%").append(paramMap.get("staffName")).append("%'");
		}
		if (paramMap!=null && paramMap.get("chinesename")!=null && !"".equals(paramMap.get("chinesename"))){
			staffSQL.append(" and s.CHINESE_NAME like '%").append(paramMap.get("chinesename")).append("%'");
		}
		if (paramMap!=null && paramMap.get("status")!=null && !"".equals(paramMap.get("status"))){
			staffSQL.append(" and s.STATUS like '%").append(paramMap.get("status")).append("%'");
		}
		if (paramMap!=null && paramMap.get("email")!=null && !"".equals(paramMap.get("email"))){
			staffSQL.append(" and s.EMAIL like '%").append(paramMap.get("email")).append("%'");
		}
		if (paramMap!=null && paramMap.get("teamCode")!=null && !"".equals(paramMap.get("teamCode"))){
			staffSQL.append(" and s.TEAM_CODE in (").append(paramMap.get("teamCode")).append(") ");
		}
		if (paramMap!=null && paramMap.get("orgId")!=null && !"".equals(paramMap.get("orgId"))){
			staffSQL.append(" and s.org_id in (").append(paramMap.get("orgId")).append(") ");
		}
		if (paramMap!=null && paramMap.get("userType")!=null && !"".equals(paramMap.get("userType"))){
			staffSQL.append(" and s.USER_TYPE ='").append(paramMap.get("userType")).append("'");
		}
		if (paramMap!=null && paramMap.get("userType")!=null && "1".equals(paramMap.get("userType"))){
			if (paramMap.get("beginDate")!=null && paramMap.get("endDate")!=null && !"".equals((String)paramMap.get("beginDate"))&&!"".equals(paramMap.get("endDate"))){
				staffSQL.append(" and s.FROM_DATE>='").append(paramMap.get("beginDate")).append("' and s.TO_DATE<='").append(paramMap.get("endDate")).append("' ");
			}else if (!"".equals(paramMap.get("beginDate")) && "".equals(paramMap.get("endDate"))){
				staffSQL.append(" and s.FROM_DATE>='").append(paramMap.get("beginDate")).append("' ");
			}else if ("".equals(paramMap.get("beginDate"))&&!"".equals(paramMap.get("endDate"))){
				staffSQL.append(" and s.TO_DATE<='").append(paramMap.get("endDate")).append("'");
			}
		}
		
		return staffSQL.toString();
	}
	
	public Collection getSearchStaff(EflowStaffVO estaff)throws DAOException{
		StringBuffer staffSQL = new StringBuffer("select a.*,l.abrev ");
		//staffSQL.append("(select a.*,b.team_name as virtual_team_name ");
		staffSQL.append("from (select s.title_id,s.staff_code,s.staff_name,s.logon_id logon_id,s.status,s.email,s.team_code,t.team_name,s.virtual_team_code,s.user_type,convert(varchar,s.from_date,101) from_date, convert(varchar,s.to_date,101) to_date,s.chinese_name,s.bank_detail,s.account_no,s.display_type,s.org_id,c.org_name ")
		        .append("from tpma_staffbasic s left join tpma_team t on s.team_code=t.team_code ")
		        .append("                       left join teflow_company c on s.org_id=c.org_id  ")
		        .append("where 1=1 ");                           
		if (estaff.getStaffCode()!=null && !"".equals(estaff.getStaffCode())){
			staffSQL.append(" and s.STAFF_CODE like '%").append(estaff.getStaffCode()).append("%'");
		}
		if (estaff.getLogonId()!=null && !"".equals(estaff.getLogonId())){
			staffSQL.append(" and s.LOGON_ID like '%").append(estaff.getLogonId()).append("%'");
		}
		if (estaff.getStaffName()!=null && !"".equals(estaff.getStaffName())){
			staffSQL.append(" and s.STAFF_NAME like '%").append(estaff.getStaffName()).append("%'");
		}
		if (estaff.getStatus()!=null && !"".equals(estaff.getStatus())){
			staffSQL.append(" and s.STATUS like '%").append(estaff.getStatus()).append("%'");
		}
		if (estaff.getEmail()!=null && !"".equals(estaff.getEmail())){
			staffSQL.append(" and s.EMAIL like '%").append(estaff.getEmail()).append("%'");
		}
		if (estaff.getTeamCode()!=null && !"".equals(estaff.getTeamCode())){
			staffSQL.append(" and s.TEAM_CODE in (").append(estaff.getTeamCode()).append(") ");
		}else if(estaff.getOrgId()!=null && !"".equals(estaff.getOrgId())){
			staffSQL.append(" and s.org_id in (").append(estaff.getOrgId()).append(") ");
		}
			
		if (estaff.getUsertype()!=null && !"".equals(estaff.getUsertype())){
			staffSQL.append(" and s.USER_TYPE ='").append(estaff.getUsertype()).append("'");
		}
		if ("1".equals(estaff.getUsertype())){
			if (!"".equals(estaff.getFromdate())&&!"".equals(estaff.getTodate())){
				staffSQL.append(" and s.FROM_DATE>='").append(estaff.getFromdate()).append("' and s.TO_DATE<='").append(estaff.getTodate()).append("' ");
			}else if (!"".equals(estaff.getFromdate())&&"".equals(estaff.getTodate())){
				staffSQL.append(" and s.FROM_DATE>='").append(estaff.getFromdate()).append("' ");
			}else if ("".equals(estaff.getFromdate())&&!"".equals(estaff.getTodate())){
				staffSQL.append(" and s.TO_DATE<='").append(estaff.getFromdate()).append("'");
			}
		}	
		staffSQL.append(" ) a left join tpma_title l on a.title_id=l.title_id ")
		        .append("order by a.staff_name");
		//System.out.println(staffSQL.toString());
		Collection list = dbManager.query(staffSQL.toString());
		if(list==null || list.size()==0){			
			return null;
		}
		Collection resultList = new ArrayList();
		Iterator it = list.iterator();
		while(it.hasNext()){
			   HashMap map = (HashMap)it.next();		   
			   EflowStaffVO staff = convertERecord(map);
			   resultList.add(staff);
			}
		return resultList;
	}
	
	public void deleteStaffByStaffcode(String scode)throws DAOException{
	  String sql = "update teflow_user set status='T' where staff_code='" + scode + "'";
	  dbManager.executeUpdate(sql);
	}
	public void updateduputytable(String scode) throws DAOException{
		String sql = "update teflow_deputy_manage set status='02' where authority_approver='"+scode+"' or authority_deputy='"+scode+"'";
		dbManager.executeUpdate(sql);
	}
	
	public Collection getActiveEflowStaffByTeam(String teamCode)throws DAOException{
		return getEflowStaffByTeam(teamCode,"A");
	}
	
	public boolean IsTorPLeader(String logon_Id) throws DAOException{
		String sql = "select tl_id from teflow_team where status='A' and Tl_id='"+logon_Id+"' union all select prj_ld_id from teflow_projects where status='01' and prj_ld_id='"+logon_Id+"'";		
		Collection list = dbManager.query(sql);
		//int count = 0;
		if(list==null || list.size()==0) return false;
		else return true;
	}
	
	
	/**
	 * 获取eflow user表中所属team的所有员工
	 * @param teamCode
	 * @param status
	 * @return
	 * @throws DAOException
	 */
	public Collection getEflowStaffByTeam(String teamCode,String status)throws DAOException{
		String sql = "select * from teflow_user where 1=1 ";
		if(teamCode!=null && !"".equals(teamCode)){
			sql = sql + " and team_code="+teamCode;
		}
		if(status!=null && !"".equals(status)){
			sql = sql + " and status='" + status + "'";
		}
		Collection list = dbManager.query(sql);
		if(list==null || list.size()==0) return new ArrayList();
		Iterator it = list.iterator();
		Collection resultList = new ArrayList();
		while(it.hasNext()){
			HashMap map = (HashMap)it.next();
			EflowStaffVO staff = convertERecord(map);
			staff.setLogonId(FieldUtil.convertSafeString(map,"LOGONID"));
			resultList.add(staff);
		}
		return resultList;
	}
	
	public EflowStaffVO getStaffByStaffCode(String staffcode)throws DAOException{		
		String sql = "select Staff_code,Staff_name,logon_id as Logon_id,Team_code,Virtual_team_code,Status,Email,Title_id,Modified_date,Modified_Staff,user_type,convert(varchar,from_date,101) from_date,convert(varchar,to_date,101) to_date,chinese_name,bank_detail,account_no,org_id from tpma_staffbasic where staff_code='"+staffcode+"'";
		//System.out.println(sql);
		Collection list = dbManager.query(sql);
		if(list==null || list.size()==0) return null;
		HashMap map = (HashMap)list.iterator().next();
		EflowStaffVO staff = convertERecord(map);
		return staff;
	}
	
	public EflowStaffVO getActiveStaffByStaffCode(String staffcode)throws DAOException{		
		String sql = "select * from tpma_staffbasic where staff_code='"+staffcode+"' and status='A'";
		Collection list = dbManager.query(sql);
		if(list==null || list.size()==0) return null;
		HashMap map = (HashMap)list.iterator().next();
		EflowStaffVO staff = convertERecord(map);
		return staff;
	}
	
	
	public StaffDAO(IDBManager dbManager){
		super(dbManager);
	}

	public int delete(BaseVO arg0) throws DAOException {
		// TODO Auto-generated method stub
		return 0;
	}
	public int delete(ArrayList userList, HttpServletRequest request) throws DAOException {
		return 0;
	}
	
	public int save(BaseVO vo, HttpServletRequest request) throws DAOException {
		EflowStaffVO estaffvo = (EflowStaffVO)vo;
		String requestFormDate = "";
		requestFormDate = StringUtil.getDateStr(new java.util.Date(),"MM/dd/yyyy HH:mm:ss");
		StaffVO staff = (StaffVO)request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
		StringBuffer insertSQL = new StringBuffer("insert into teflow_user ");
		insertSQL.append("(Staff_code,Staff_name,Logonid,Team_code,Status,Email,Title_id,Modified_date,Modified_Staff,user_type,from_date,to_date,chinese_name,bank_detail,account_no,org_id) values('").append(estaffvo.getStaffCode().trim()).append("','").append(estaffvo.getStaffName().trim()).append("','")
		         .append(estaffvo.getLogonId().trim()).append("',");
		if(estaffvo.getTeamCode()==null||"".equals(estaffvo.getTeamCode())){
			insertSQL.append("-1,");
		}else{
			insertSQL.append(estaffvo.getTeamCode()).append(",");
		}
		insertSQL.append("'").append(estaffvo.getStatus()).append("','")
		         .append(estaffvo.getEmail().trim()).append("',").append(estaffvo.getTitle()).append(",'").append(requestFormDate).append("','")
		         .append(staff.getStaffCode()).append("','").append(estaffvo.getUsertype()).append("',");
		if (estaffvo.getFromdate()==null||"".equals(estaffvo.getFromdate())){
			insertSQL.append("null,");
		}else{
			insertSQL.append("'").append(estaffvo.getFromdate()).append("',");
		}
		if (estaffvo.getTodate()==null||"".equals(estaffvo.getTodate())){
			insertSQL.append("null,");
		}else{
			insertSQL.append("'").append(estaffvo.getTodate()).append("',");
		}     
		if (estaffvo.getChineseName()==null||"".equals(estaffvo.getChineseName())){
			insertSQL.append("null,");
		}else{
			insertSQL.append("'").append(estaffvo.getChineseName()).append("',");
		}
		if (estaffvo.getBankDetail()==null||"".equals(estaffvo.getBankDetail())){
			insertSQL.append("null,");
		}else{
			insertSQL.append("'").append(estaffvo.getBankDetail()).append("',");
		}
		if (estaffvo.getAccountNo()==null||"".equals(estaffvo.getAccountNo())){
			insertSQL.append("null,");
		}else{
			insertSQL.append("'").append(estaffvo.getAccountNo()).append("',");
		}
		if (estaffvo.getOrgId()==null||"".equals(estaffvo.getOrgId())){
			insertSQL.append("null");
		}else{
			insertSQL.append("'").append(estaffvo.getOrgId()).append("'");
		}
		insertSQL.append(")");
		//System.out.println(insertSQL.toString());
		
		dbManager.executeUpdate(insertSQL.toString());
		return 0;
	}
	
	public void save(HashMap map)throws DAOException{
		String requestFormDate = "";
		requestFormDate = StringUtil.getDateStr(new java.util.Date(),"MM/dd/yyyy HH:mm:ss");
		StringBuffer insertSQL = new StringBuffer("insert into teflow_user ");
		insertSQL.append("(Staff_code,Staff_name,Logonid,Team_code,Status,Email,Title_id,Modified_date,Modified_Staff,user_type,from_date,to_date,chinese_name,bank_detail,account_no,org_id) values(")
		         .append("?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		String[] mapKeyName = {""};
		
		Object[] obj = new Object[16];
		
		int[] dataType = {DataType.VARCHAR,DataType.VARCHAR,DataType.VARCHAR,DataType.VARCHAR,DataType.VARCHAR,DataType.VARCHAR,DataType.VARCHAR,DataType.VARCHAR,DataType.VARCHAR,DataType.VARCHAR};
		dbManager.executeUpdate(insertSQL.toString(),obj,dataType);
	}

	public int update(BaseVO vo,StaffVO staff) throws DAOException {
		EflowStaffVO estaffvo = (EflowStaffVO)vo;
		String requestFormDate = "";
		requestFormDate = StringUtil.getDateStr(new java.util.Date(),"MM/dd/yyyy HH:mm:ss");
		StringBuffer insertSQL = new StringBuffer("update teflow_user ");
		insertSQL.append(" set Staff_name='").append(estaffvo.getStaffName().trim())
				 .append("',Logonid='").append(estaffvo.getLogonId().trim()).append("',Team_code=");
		if(estaffvo.getTeamCode()==null||"".equals(estaffvo.getTeamCode())){
			insertSQL.append("-1");
		}else{
			insertSQL.append(estaffvo.getTeamCode());
		}
		
		insertSQL.append(",Status='").append(estaffvo.getStatus())
				 .append("',email='").append(estaffvo.getEmail().trim()).append("',Title_id=").append(estaffvo.getTitle()).append(",Modified_date='").append(requestFormDate)
				 .append("',Modified_Staff='").append(staff.getStaffCode()).append("',User_Type='").append(estaffvo.getUsertype());
		if ("".equals(estaffvo.getFromdate())||estaffvo.getFromdate()==null){
			insertSQL.append("',From_date=").append("null,");
		}else{
			insertSQL.append("',From_date='").append(estaffvo.getFromdate()).append("',");
		}
		if ("".equals(estaffvo.getTodate())||estaffvo.getTodate()==null){
			insertSQL.append("To_date=").append("null, ");
		}else{
			insertSQL.append("To_date='").append(estaffvo.getTodate()).append("', ");
		}
		if ("".equals(estaffvo.getChineseName())||estaffvo.getChineseName()==null){
			insertSQL.append("chinese_name=").append("null, ");
		}else{
			insertSQL.append("chinese_name='").append(estaffvo.getChineseName()).append("', ");
		}
		if ("".equals(estaffvo.getBankDetail())||estaffvo.getBankDetail()==null){
			insertSQL.append("bank_detail=").append("null,");
		}else{
			insertSQL.append("bank_detail='").append(estaffvo.getBankDetail()).append("',");
		}
		if ("".equals(estaffvo.getAccountNo())||estaffvo.getAccountNo()==null){
			insertSQL.append("account_no=").append("null,");
		}else{
			insertSQL.append("account_no='").append(estaffvo.getAccountNo()).append("',");
		}
		if ("".equals(estaffvo.getOrgId())||estaffvo.getOrgId()==null){
			insertSQL.append("org_id=").append("null");
		}else{
			insertSQL.append("org_id='").append(estaffvo.getOrgId()).append("'");
		}
		//System.out.println(insertSQL.toString());
		insertSQL.append(" where Staff_code='").append(estaffvo.getStaffCode()).append("'");	
		
		dbManager.executeUpdate(insertSQL.toString());
		saveHistory(estaffvo, requestFormDate, staff.getStaffCode());
		
		return 0;
	}
	
	
	/**
	 * 
	 * @param estaffvo
	 * @param modifyDate
	 * @param staffcode
	 * @return
	 * @throws DAOException
	 * 
	 * @modify at 2011-10 有些人名和email中带有单引号，需要转义成连续两个单引号
	 */
	public int saveHistory(EflowStaffVO estaffvo, String modifyDate, String staffcode) throws DAOException {			
		StringBuffer insertSQL = new StringBuffer("insert into teflow_user_history(Staff_code,Staff_name,Logonid,Team_code,Status,email,Title_id,Modified_date,Modified_Staff,User_type,From_date,To_date,chinese_name,account_no,org_id) ");
		insertSQL.append(" values('").append(estaffvo.getStaffCode().trim()).append("','").append(estaffvo.getStaffName().replaceAll("'", "''").trim()).append("','")
		         .append(estaffvo.getLogonId().trim()).append("',");
		if(estaffvo.getTeamCode()==null||"".equals(estaffvo.getTeamCode())){
			insertSQL.append("-1,");
		}else{
			insertSQL.append(estaffvo.getTeamCode()).append(",");
		}	
		insertSQL.append("'").append(estaffvo.getStatus()).append("','")
		         .append(estaffvo.getEmail().replaceAll("'", "''").trim()).append("',").append(estaffvo.getTitle()).append(",'").append(modifyDate).append("','")
		         .append(staffcode).append("','").append(estaffvo.getUsertype()).append("',");
		if (estaffvo.getFromdate()==null || "".equals(estaffvo.getFromdate())){
			insertSQL.append("null,");
		}else{
			insertSQL.append("'").append(estaffvo.getFromdate()).append("',");
		}
		if (estaffvo.getTodate()==null || "".equals(estaffvo.getTodate())){
			insertSQL.append("null,");
		}else{
			insertSQL.append("'").append(estaffvo.getTodate()).append("', ");
		}
		if (estaffvo.getChineseName()==null||"".equals(estaffvo.getChineseName())){
			insertSQL.append("null,");
		}else{
			insertSQL.append("'").append(estaffvo.getChineseName()).append("',");
		}
		if (estaffvo.getAccountNo()==null||"".equals(estaffvo.getAccountNo())){
			insertSQL.append("null,");
		}else{
			insertSQL.append("'").append(estaffvo.getAccountNo()).append("',");
		}
		if (estaffvo.getOrgId()==null||"".equals(estaffvo.getOrgId())){
			insertSQL.append("null");
		}else{
			insertSQL.append("'").append(estaffvo.getOrgId()).append("'");
		}
		insertSQL.append(")");

		dbManager.executeUpdate(insertSQL.toString());
		return 0;
	}
	
	public void updateStaffCompany(String teamcode, String orgid, String opeStaffCode)throws DAOException{
		
		if(StringUtil.isEmptyString(teamcode)) return;
		if(StringUtil.isEmptyString(orgid)) return;
		
		String modifyDate = StringUtil.getDateStr(new java.util.Date(),"MM/dd/yyyy HH:mm:ss");
		
		Collection staffs = getStaffListByTeam(teamcode, false);
		if(staffs != null && staffs.size() > 0){
			//save history
			Iterator ite = staffs.iterator();
			while(ite.hasNext()){
				EflowStaffVO staff = (EflowStaffVO)ite.next();
				saveHistory(staff, modifyDate, opeStaffCode);
			}
			
			//do update 
			String sql = "UPDATE teflow_user SET org_id = '"+orgid+"' WHERE Team_code = '"+teamcode+"'";
			dbManager.executeUpdate(sql);
		}
	}
	
	public int update(BaseVO arg0) throws DAOException {
		// TODO Auto-generated method stub
		return 0;
	}

	public int save(BaseVO arg0) throws DAOException {
		// TODO Auto-generated method stub
		return 0;
    }

    public void userRegister(EflowStaffVO staff) throws DAOException {
        String SQL = "insert into teflow_user (staff_code, staff_name, chinese_name, logonid, org_id, team_code, status, email, "
                + "modified_date, user_type) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;
        try {
            stm = conn.prepareStatement(SQL);
            int i = 1;
            stm.setString(i++, staff.getStaffCode());
            stm.setString(i++, staff.getStaffName());
            stm.setString(i++, staff.getChineseName());
            stm.setString(i++, staff.getLogonId());
            stm.setString(i++, staff.getOrgId());
            stm.setString(i++, staff.getTeamCode());
            stm.setString(i++, "A");
            stm.setString(i++, staff.getEmail());
            stm.setTimestamp(i++, new java.sql.Timestamp(System.currentTimeMillis()));
            stm.setString(i++, "0");
            stm.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new DAOException(ex);
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public EflowStaffVO getStaffByIDM(String staffCode) throws DAOException {
        String SQL = "select * from veflow_idm_hremp where employee_id = ?";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;
        try {
            stm = conn.prepareStatement(SQL);
            int i = 1;
            stm.setString(i++, staffCode);
            ResultSet rs = stm.executeQuery();
            if (!rs.next()) {
                return null;
            }
            EflowStaffVO staff = new EflowStaffVO();
            staff.setStaffCode(rs.getString("employee_id").trim());
            String staffName = rs.getString("full_name");
            staff.setStaffName(staffName == null ? "" : staffName.trim());
            String companyCode = rs.getString("company_cd");
            staff.setOrgId(companyCode == null ? "" : companyCode.trim());
            String companyName = rs.getString("company");
            staff.setOrgName(companyName == null ? "" : companyName.trim());
            String deptCode = rs.getString("dept_cd");
            staff.setTeamCode(deptCode == null ? "" : deptCode.trim());
            String deptName = rs.getString("dept");
            staff.setTeamName(deptName == null ? "" : deptName.trim());
            String jobCode = rs.getString("job_cd");
            staff.setTitle(jobCode == null ? "" : jobCode.trim());
            String jobTitle = rs.getString("job_title");
            staff.setTitleName(jobTitle == null ? "" : jobTitle.trim());
            String email = rs.getString("email_address");
            staff.setEmail(email == null ? "" : email.trim());
            return staff;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new DAOException(ex);
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

}
