package com.aiait.eflow.housekeeping.dao;
/******************************************************************/
/*Task_ID	Author	Modify_Date	Description                       */
/*IT0973	Young	12/26/2007	For Eflow Project Management      */
/******************************************************************/
import javax.servlet.http.HttpServletRequest;

import com.aiait.eflow.common.CommonName;
import com.aiait.eflow.housekeeping.vo.ProjectVO;
import com.aiait.eflow.housekeeping.vo.StaffVO;
import com.aiait.eflow.util.FieldUtil;
import com.aiait.eflow.util.StringUtil;

import com.aiait.framework.db.BaseDAOImpl;
import com.aiait.framework.db.IDBManager;
import com.aiait.framework.exception.DAOException;
import com.aiait.framework.vo.BaseVO;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;


public class ProjectDAO extends BaseDAOImpl{
	
	public ProjectDAO(IDBManager dbManager){
		super(dbManager);
	}
	
	public Collection getProjectList() throws DAOException {		
		String sql = "select Prj_code,Prj_type,Prj_name,Prj_desc,Prj_ld_id,pln_date,status from teflow_projects";
		Collection list = dbManager.query(sql);
		if(list==null || list.size()==0) return null;
		Collection result = new ArrayList();
		Iterator it = list.iterator();
		while(it.hasNext()){
			HashMap map = (HashMap)it.next();
			ProjectVO team = ConvertRecord(map);
			result.add(team);
		}
		return result;
	}
	private  ProjectVO ConvertRecord(HashMap map){
		ProjectVO pvo = new ProjectVO();
		pvo.setPrj_code(FieldUtil.convertSafeString(map,"PRJ_CODE"));
		pvo.setPrj_type(FieldUtil.convertSafeString(map,"PRJ_TYPE"));
		pvo.setPrj_name(FieldUtil.convertSafeString(map,"PRJ_NAME"));//original name is prj_name
		pvo.setPrj_desc(FieldUtil.convertSafeString(map,"PRJ_DESC"));
		pvo.setPrj_start_date(FieldUtil.convertSafeString(map,"PRJ_START_DATE"));
		pvo.setPrj_ld_id(FieldUtil.convertSafeString(map,"PRJ_LD_ID"));
		pvo.setPrj_ld_name(FieldUtil.convertSafeString(map,"STAFF_NAME"));
		pvo.setStatus(FieldUtil.convertSafeString(map,"STATUS"));
		return pvo;
	}
	public ProjectVO getProjectByProjectCode(String projectCode) throws DAOException {
		StringBuffer sql = new StringBuffer("select o.Prj_code,o.Prj_type,o.Prj_name,o.Prj_desc,o.Prj_ld_id,convert(varchar,o.Prj_start_date,101) Prj_start_date,o.status,s.staff_name from ");
        sql.append("(select * from teflow_projects) o left join tpma_staffbasic s on o.Prj_ld_id = s.logon_id ")
           .append("where o.Prj_code='").append(StringUtil.FormatSQL(projectCode)).append("'");
		Collection list = dbManager.query(sql.toString());
		if(list==null || list.size()==0) {return null;}		
		Iterator it = list.iterator();		
		HashMap map = (HashMap)it.next();
		ProjectVO projectvo = ConvertRecord(map);		
		return projectvo;
	}
	public Collection getSearchProject(ProjectVO vo) throws DAOException {
		StringBuffer sql = new StringBuffer("select o.Prj_code,o.Prj_type,o.Prj_name,o.Prj_desc,o.Prj_ld_id,convert(varchar,o.Prj_start_date,101) Prj_start_date,o.status,s.staff_name from ");
		sql.append("(select * from teflow_projects) o left join (select * from tpma_staffbasic where status='A') s on o.Prj_ld_id = s.logon_id ")
		   .append("where 1=1 ");
		if (vo.getPrj_code()!=null&&!"".equals(vo.getPrj_code())){
			sql.append(" and o.Prj_code like '%").append(StringUtil.FormatSQL(vo.getPrj_code())).append("%' ");
		}
		if (vo.getPrj_name()!=null&&!"".equals(vo.getPrj_name())){
			sql.append(" and o.Prj_name like '%").append(StringUtil.FormatSQL(vo.getPrj_name())).append("%' ");
		}
		if (vo.getPrj_desc()!=null&&!"".equals(vo.getPrj_desc())){
			sql.append(" and o.Prj_desc like '%").append(StringUtil.FormatSQL(vo.getPrj_desc())).append("%' ");
		}
		if (vo.getPrj_ld_id()!=null&&!"".equals(vo.getPrj_ld_id())){
			sql.append(" and o.Prj_ld_id like '%").append(vo.getPrj_ld_id()).append("%' ");
		}
		if (vo.getPrj_ld_name()!=null&&!"".equals(vo.getPrj_ld_name())){
			sql.append(" and s.staff_name like '%").append(vo.getPrj_ld_name()).append("%' ");
		}
		if (vo.getPrj_start_date()!=null&&!"".equals(vo.getPrj_start_date())){
			sql.append(" and o.Prj_start_date ='").append(vo.getPrj_start_date()).append("' ");
		}
		if (vo.getStatus()!=null&&!"".equals(vo.getStatus())){
			sql.append(" and o.status='").append(vo.getStatus()).append("' ");
		}		
		//System.out.println(sql.toString());
		Collection list = dbManager.query(sql.toString());
		if(list==null || list.size()==0){			
			return null;
		}
		Collection resultList = new ArrayList();
		Iterator it = list.iterator();
		while(it.hasNext()){
			   HashMap map = (HashMap)it.next();		   
			   ProjectVO project = ConvertRecord(map);
			   resultList.add(project);
			}
		return resultList;
	}
	public void deleteProject(String projectCode)throws DAOException{	
		String sql = "update teflow_projects set status='10' where Prj_code='" + projectCode+"'";
		dbManager.executeUpdate(sql);
	}
	public int delete(ArrayList projectList,HttpServletRequest request) throws DAOException {
		
		return 0;
	}

	public int save(BaseVO vo,HttpServletRequest request) throws DAOException {		
		ProjectVO pvo = (ProjectVO)vo;
		String requestFormDate = "";
		requestFormDate = StringUtil.getDateStr(new java.util.Date(),"MM/dd/yyyy HH:mm:ss");
		StaffVO staff = (StaffVO)request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
		StringBuffer insertSQL = new StringBuffer("insert into teflow_projects (Prj_code,Prj_type,Prj_name,Prj_desc,Prj_ld_id,Prj_start_date,Prj_update_date,Prj_update_user,status) ");
		insertSQL.append("values('").append(StringUtil.FormatSQL(pvo.getPrj_code())).append("',");
		if (pvo.getPrj_type()==null || "".equals(pvo.getPrj_type())){
			insertSQL.append("'',");
		}else{
			insertSQL.append("'").append(pvo.getPrj_type()).append("',");
		}
		if (pvo.getPrj_name()==null || "".equals(pvo.getPrj_name())){
			insertSQL.append("'',");
		}else{
			insertSQL.append("'").append(StringUtil.FormatSQL(pvo.getPrj_name())).append("',");
		}
		if (pvo.getPrj_desc()==null || "".equals(pvo.getPrj_desc())){
			insertSQL.append("'',");
		}else{
			insertSQL.append("'").append(StringUtil.FormatSQL(pvo.getPrj_desc())).append("',");
		}
		if (pvo.getPrj_ld_id()==null || "".equals(pvo.getPrj_ld_id())){
			insertSQL.append("'',");
		}else{
			insertSQL.append("'").append(pvo.getPrj_ld_id()).append("',");
		}
		if (pvo.getPrj_start_date()==null || "".equals(pvo.getPrj_start_date())){
			insertSQL.append("'',");
		}else{
			insertSQL.append("'").append(pvo.getPrj_start_date()).append("',");
		}
		insertSQL.append("'").append(requestFormDate).append("','")
				 .append(staff.getStaffCode()).append("',")
				 .append("'").append(pvo.getStatus()).append("')");
		//System.out.println(insertSQL.toString());
		dbManager.executeUpdate(insertSQL.toString());
		return 0;
	}

	public int update(BaseVO vo, HttpServletRequest request) throws DAOException {
		ProjectVO pvo = (ProjectVO)vo;
		String requestFormDate = "";
		requestFormDate = StringUtil.getDateStr(new java.util.Date(),"MM/dd/yyyy HH:mm:ss");
		StaffVO staff = (StaffVO)request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
		StringBuffer insertSQL = new StringBuffer("update teflow_projects ");
		if (pvo.getPrj_type()==null||"".equals(pvo.getPrj_type())){
			insertSQL.append(" set Prj_type='',");
		}else{
			insertSQL.append(" set Prj_type='").append(pvo.getPrj_type()).append("',");
		}
		if (pvo.getPrj_name()==null||"".equals(pvo.getPrj_name())){
			insertSQL.append(" Prj_name='',");
		}else{
			insertSQL.append(" Prj_name='").append(StringUtil.FormatSQL(pvo.getPrj_name())).append("',");
		}
		if (pvo.getPrj_desc()==null||"".equals(pvo.getPrj_desc())){
			insertSQL.append(" Prj_desc='',");
		}else{
			insertSQL.append(" Prj_desc='").append(StringUtil.FormatSQL(pvo.getPrj_desc())).append("',");
		}
		if (pvo.getPrj_ld_id()==null||"".equals(pvo.getPrj_ld_id())){
			insertSQL.append(" Prj_ld_id='',");
		}else{
			insertSQL.append(" Prj_ld_id='").append(pvo.getPrj_ld_id()).append("',");
		}		
		
		insertSQL.append(" Prj_start_date='").append(pvo.getPrj_start_date()).append("', ");
		insertSQL.append(" status='").append(pvo.getStatus()).append("' ");
		insertSQL.append(" where Prj_code='").append(StringUtil.FormatSQL(pvo.getPrj_code())).append("'");
		
		//System.out.println(insertSQL.toString());
		
		dbManager.executeUpdate(insertSQL.toString());
		return 0;
	}
	public int saveHistory(BaseVO vo, HttpServletRequest request) throws DAOException {
		ProjectVO pvo = (ProjectVO)vo;
		String requestFormDate = "";
		requestFormDate = StringUtil.getDateStr(new java.util.Date(),"MM/dd/yyyy HH:mm:ss");
		StaffVO staff = (StaffVO)request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
		StringBuffer insertSQL = new StringBuffer("insert into teflow_projects_history (Prj_code,Prj_type,Prj_name,Prj_desc,Prj_ld_id,Prj_start_date,Prj_update_date,Prj_update_user,status) ");
		insertSQL.append("values('").append(StringUtil.FormatSQL(pvo.getPrj_code())).append("',");
		if (pvo.getPrj_type()==null || "".equals(pvo.getPrj_type())){
			insertSQL.append("'',");
		}else{
			insertSQL.append("'").append(pvo.getPrj_type()).append("',");
		}
		if (pvo.getPrj_name()==null || "".equals(pvo.getPrj_name())){
			insertSQL.append("'',");
		}else{
			insertSQL.append("'").append(StringUtil.FormatSQL(pvo.getPrj_name())).append("',");
		}
		if (pvo.getPrj_desc()==null || "".equals(pvo.getPrj_desc())){
			insertSQL.append("'',");
		}else{
			insertSQL.append("'").append(StringUtil.FormatSQL(pvo.getPrj_desc())).append("',");
		}
		if (pvo.getPrj_ld_id()==null || "".equals(pvo.getPrj_ld_id())){
			insertSQL.append("'',");
		}else{
			insertSQL.append("'").append(pvo.getPrj_ld_id()).append("',");
		}
		if (pvo.getPrj_start_date()==null || "".equals(pvo.getPrj_start_date())){
			insertSQL.append("'',");
		}else{
			insertSQL.append("'").append(pvo.getPrj_start_date()).append("',");
		}
		insertSQL.append("'").append(requestFormDate).append("','")
				 .append(staff.getStaffCode()).append("',");
		if (pvo.getStatus()==null || "".equals(pvo.getStatus())){
			insertSQL.append("'')");
		}else{
			insertSQL.append("'").append(pvo.getStatus()).append("')");
		}
		//System.out.println(insertSQL.toString());
		dbManager.executeUpdate(insertSQL.toString());
		return 0;
	}
	public int delete(BaseVO arg0) throws DAOException {

		return 0;
	}

	public int save(BaseVO arg0) throws DAOException {

		return 0;
	}

	public int update(BaseVO arg0) throws DAOException {

		return 0;
	}
}
