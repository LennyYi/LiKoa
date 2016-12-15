package com.aiait.eflow.housekeeping.dao;
/**
/******************************************************************/
/*Task_ID	Author	Modify_Date	Description                       */
/*          Robin   2008-05-21   年假标准管理数据库操作类
/******************************************************************/
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import com.aiait.eflow.housekeeping.vo.AnnualLeaveStVO;
import com.aiait.eflow.util.FieldUtil;
import com.aiait.framework.db.BaseDAOImpl;
import com.aiait.framework.db.IDBManager;
import com.aiait.framework.exception.DAOException;
import com.aiait.framework.vo.BaseVO;

public class AnnualLeaveStDAO extends BaseDAOImpl {
	
	public AnnualLeaveStDAO(IDBManager dbManager){
		super(dbManager);
	}
	
	/**
	 * 获取某个公司所指定服务年限已经设过年假得职级集合，多个职级之间以","间隔
	 * @param orgId
	 * @param serviceYearLevel
	 * @return
	 * @throws DAOException
	 */
	public String getGradeIdsByOrgServiceYear(String orgId,int serviceYearLevel)throws DAOException{
	   String sql = "select grade_id from teflow_annual_leave_standard where org_id= '"
			           +orgId+"'and service_year_level="
			           +serviceYearLevel;	
	   Collection list = dbManager.query(sql);
	   if(list==null || list.size()==0) return null;
	   String gradeIds = "";
	   Iterator it = list.iterator();
	   while(it.hasNext()){
		   HashMap map = (HashMap)it.next();
		   gradeIds = gradeIds + map.get("GRADE_ID")+",";
	   }
	   if(!"".equals(gradeIds) && gradeIds.indexOf(",")>-1){
		   gradeIds = gradeIds.substring(0,gradeIds.length()-1);
	   }
	   return gradeIds;
	}
	
	public AnnualLeaveStVO getAnnualLeaveStVO(String orgId,String gradeId,int serviceYearLevel)throws DAOException{
		String sql = "select * from teflow_annual_leave_standard where org_id= '"
			           +orgId+"' and grade_id='"+gradeId+"' and service_year_level="
			           +serviceYearLevel;
		Collection list = dbManager.query(sql);
		if(list==null || list.size()==0) return null;
		HashMap map = (HashMap)list.iterator().next();
		return convertMapToVO(map);
	}
	
	public AnnualLeaveStVO getAnnualLeaveStVO(int id)throws DAOException{
		String sql = "select * from teflow_annual_leave_standard where id = "
			           +id;
		Collection list = dbManager.query(sql);
		if(list==null || list.size()==0) return null;
		HashMap map = (HashMap)list.iterator().next();
		return convertMapToVO(map);
	}
	
	public Collection getList(AnnualLeaveStVO conditionVo)throws DAOException{
		String sql = "select * from teflow_annual_leave_standard where 1=1 ";
		if(conditionVo!=null && conditionVo.getOrgId()!=null && !"".equals(conditionVo.getOrgId())){
			sql = sql + " and org_id='" + conditionVo.getOrgId()+"' ";
		}
		if(conditionVo!=null && conditionVo.getServiceYearLevel()>0){
			sql = sql + " and service_year_level=" + conditionVo.getServiceYearLevel()+" ";
		}		
		sql = sql + " order by org_id,grade_id,service_year_level";
		Collection list = dbManager.query(sql);
		if(list==null || list.size()==0) return null;
		Collection result = new ArrayList();
		Iterator it = list.iterator();
		while(it.hasNext()){
			HashMap map = (HashMap)it.next();
			AnnualLeaveStVO vo = convertMapToVO(map);
			result.add(vo); 
		}
		return result;
	}

	public int delete(BaseVO arg0) throws DAOException {
		AnnualLeaveStVO vo = (AnnualLeaveStVO)arg0;
		String sql = "delete from teflow_annual_leave_standard where id="+vo.getId();
		dbManager.executeUpdate(sql);
		return 0;
	}
	

	public int save(BaseVO arg0) throws DAOException {
		// TODO Auto-generated method stub
		AnnualLeaveStVO vo = (AnnualLeaveStVO)arg0;
		String sql = "insert into teflow_annual_leave_standard(org_id,service_year_level,grade_id,annual_leave_days,remark,limit_transfer_days) "
			          +" values('" +vo.getOrgId()+"',"+vo.getServiceYearLevel()+",'"+vo.getGradeId()+"',"
			          +vo.getAnnualLeaveDays()+",'"+vo.getRemark()+"',"+vo.getLimitTransferDays()+")";
		dbManager.executeUpdate(sql);
		return 0;
	}

	public int update(BaseVO arg0) throws DAOException {
		// TODO Auto-generated method stub
		AnnualLeaveStVO vo = (AnnualLeaveStVO)arg0;
		String sql = "update teflow_annual_leave_standard set annual_leave_days=" + vo.getAnnualLeaveDays()
		             +",grade_id='" + vo.getGradeId()+"',remark='" + vo.getRemark()+"',limit_transfer_days="+vo.getLimitTransferDays()+" where id="
		             +vo.getId();
		dbManager.executeUpdate(sql);
		return 0;
	}
	
	private AnnualLeaveStVO convertMapToVO(HashMap map){
		AnnualLeaveStVO vo = new AnnualLeaveStVO();
		vo.setId(FieldUtil.convertSafeInt(map,"ID",0));
		vo.setOrgId(FieldUtil.convertSafeString(map,"ORG_ID"));
		vo.setGradeId(FieldUtil.convertSafeString(map,"GRADE_ID"));
		vo.setServiceYearLevel(FieldUtil.convertSafeInt(map,"SERVICE_YEAR_LEVEL",0));
		vo.setAnnualLeaveDays(FieldUtil.convertSafeFloat(map,"ANNUAL_LEAVE_DAYS",0));
		vo.setLimitTransferDays(FieldUtil.convertSafeFloat(map,"LIMIT_TRANSFER_DAYS",0));
		vo.setRemark(FieldUtil.convertSafeString(map,"REMARK"));
		return vo;
	}

}
