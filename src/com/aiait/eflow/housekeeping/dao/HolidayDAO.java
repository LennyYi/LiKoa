package com.aiait.eflow.housekeeping.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import com.aiait.eflow.formmanage.vo.FormSectionVO;
import com.aiait.eflow.housekeeping.vo.HolidayVO;
import com.aiait.eflow.util.FieldUtil;
import com.aiait.eflow.util.StringUtil;
import com.aiait.framework.common.DataType;
import com.aiait.framework.db.BaseDAOImpl;
import com.aiait.framework.db.IDBManager;
import com.aiait.framework.exception.DAOException;
import com.aiait.framework.vo.BaseVO;

public class HolidayDAO extends BaseDAOImpl{
	public HolidayDAO(IDBManager dbManager){
		super(dbManager);
	}
	
	public Collection getHolidayYear()throws DAOException{
		//IT1206 2011-02
		//String sql = "select distinct set_year,count(id) holidays from teflow_holiday_define group by set_year order by set_year";
		String sql = "select distinct set_year,sum(datediff(day,from_date,to_date)+1) holidays from teflow_holiday_define group by set_year order by set_year";
		Collection list = dbManager.query(sql);
		return list;
	}
	
	public Collection getHolidayList(String strYear, String strMonth) throws DAOException{
		String sql = "select day(from_date) as holiday_day from teflow_holiday_define where set_year='"+strYear+"' and month(from_date)='"+strMonth+"'";				
		Collection list = dbManager.query(sql);
		if(list==null || list.size()==0) return null;
		Iterator it = list.iterator();
		Collection result = new ArrayList();
		while(it.hasNext()){
			HashMap map = (HashMap)it.next();			
			result.add(FieldUtil.convertSafeString(map,"HOLIDAY_DAY"));
		}
		return  result;
	}
	
	public Collection getHolidayList()throws DAOException{
		return getHolidayList(null);
	}
	
	public Collection getHolidayList(String year)throws DAOException{
		String sql = "select * from teflow_holiday_define where 1=1 ";
		if(year!=null && !"".equals(year)){
			sql = sql + " and set_year='"+year+"' ";
		}
		sql = sql + " order by set_year desc,from_date desc";
		Collection list = dbManager.query(sql);
		if(list==null || list.size()==0) return new ArrayList();
		Iterator it = list.iterator();
		Collection result = new ArrayList();
		SimpleDateFormat   df   =new   SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		while(it.hasNext()){
			HolidayVO vo = new HolidayVO();
			HashMap map = (HashMap)it.next();

			vo.setHolidayId(FieldUtil.convertSafeInt(map,"ID",-1));
			vo.setHolidayYear(FieldUtil.convertSafeString(map,"SET_YEAR"));
			try{
				  Date cDate = df.parse((String)map.get("FROM_DATE"));
				  vo.setHolidayFromDate(StringUtil.getDateStr(cDate,"MM/dd/yyyy"));
				}catch(Exception e){
				  vo.setHolidayFromDate((String)map.get("FROM_DATE"));
				}
			try{
				  Date cDate = df.parse((String)map.get("TO_DATE"));
				  vo.setHolidayToDate(StringUtil.getDateStr(cDate,"MM/dd/yyyy"));
				}catch(Exception e){
				  vo.setHolidayToDate((String)map.get("TO_DATE"));
				}
			vo.setHolidayDescription(FieldUtil.convertSafeString(map,"DESCRIPTION"));
			vo.setHolidayStatus(FieldUtil.convertSafeInt(map,"STATUS",-1));
			vo.setHolidayType(FieldUtil.convertSafeString(map,"HOLIDAY_TYPE"));
			result.add(vo);
		}
		return result;
	}
	
	public HolidayVO getHolidayVO(int HolidayId)throws DAOException{
		String sql = "select * from teflow_holiday_define where id='" + HolidayId + "'";
		Collection list = dbManager.query(sql);
		if(list==null || list.size()==0) return null;
		HashMap map = (HashMap)list.iterator().next();
		HolidayVO vo = new HolidayVO();
		
		SimpleDateFormat   df   =new   SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		vo.setHolidayId(FieldUtil.convertSafeInt(map,"ID",-1));
		vo.setHolidayYear(FieldUtil.convertSafeString(map,"SET_YEAR"));
		try{
			  Date cDate = df.parse((String)map.get("FROM_DATE"));
			  vo.setHolidayFromDate(StringUtil.getDateStr(cDate,"MM/dd/yyyy"));
			}catch(Exception e){
			  vo.setHolidayFromDate((String)map.get("FROM_DATE"));
			}
		try{
			  Date cDate = df.parse((String)map.get("TO_DATE"));
			  vo.setHolidayToDate(StringUtil.getDateStr(cDate,"MM/dd/yyyy"));
			}catch(Exception e){
			  vo.setHolidayToDate((String)map.get("TO_DATE"));
			}
		vo.setHolidayDescription(FieldUtil.convertSafeString(map,"DESCRIPTION"));
		vo.setHolidayStatus(FieldUtil.convertSafeInt(map,"STATUS",-1));
		
		vo.setHolidayType(FieldUtil.convertSafeString(map,"HOLIDAY_TYPE"));
		return vo;
	}
	
	public String getHolidayIdByDate(String adate)throws DAOException{
		String sql = "select id from teflow_holiday_define where from_date = '"+adate+"'";		
		Collection list = dbManager.query(sql);
		if(list==null || list.size()==0) return "";
		HashMap map = (HashMap)list.iterator().next();
		String retStr = (String)map.get("ID");
		return retStr;
	}
	
	public String getWeekdayofDate(String adate) throws DAOException{
		String sql = "select  case when datepart(weekday,'"+adate+"')=1 then 'sunday' when datepart(weekday,'"+adate+"')=7 then 'saturday' else ''  end as DESCRIPTION";
		Collection list = dbManager.query(sql);
		if(list==null || list.size()==0) {
			return "";
		}
		HashMap map = (HashMap)list.iterator().next();
		String retStr = (String)map.get("DESCRIPTION");	
		return retStr;
	}
	
	public int delete(BaseVO vo) throws DAOException {
		HolidayVO Holiday = (HolidayVO)vo;
		String delSQL = "delete from teflow_holiday_define where id='"+Holiday.getHolidayId()+"'";
		dbManager.executeUpdate(delSQL);
		return 0;
	}
	
	public int saveBatch(BaseVO vo) throws DAOException {
		HolidayVO Holiday = (HolidayVO)vo;
		int size;
		size = (Integer.parseInt((String)Holiday.getHolidayBatchToYear()) - Integer.parseInt((String)Holiday.getHolidayBatchFromYear()));
		
		//should be size+1
		for(int i = 1; i <= size+1; i++)
		{
			String selSQL = "select * from teflow_holiday_define where id='"+Holiday.getHolidayId()+"'";
			Collection list = dbManager.query(selSQL);
			if(list!=null && list.size()>0) return 1;
			String sql = "insert into teflow_holiday_define(set_year, from_date, to_date, description, status, Holiday_type) values(?,?,?,?,?,?)";
			
			int newYear;
			newYear = Integer.parseInt((String)Holiday.getHolidayYear()) + i;
			
			String newFromDate = Holiday.getHolidayFromDate();
			newFromDate = newFromDate.substring(0,newFromDate.lastIndexOf("/")+1) + "" + newYear;
			
			String newToDate = Holiday.getHolidayToDate();
			newToDate = newToDate.substring(0,newToDate.lastIndexOf("/")+1) + "" + newYear;
			
			Object[] obj = new Object[6];
			obj[0] = ""+newYear;
			obj[1] = newFromDate;
			obj[2] = newToDate;
			obj[3] = Holiday.getHolidayDescription();
			obj[4] = ""+Holiday.getHolidayStatus();
			obj[5] = Holiday.getHolidayType();
			int[] dataType = {DataType.CHAR,DataType.DATETIME,DataType.DATETIME,DataType.VARCHAR,DataType.INT,DataType.VARCHAR}; 
			dbManager.executeUpdate(sql,obj,dataType);
		}
		return 0;
	}
	
	public int save(BaseVO vo) throws DAOException {
		HolidayVO Holiday = (HolidayVO)vo;
		String selSQL = "select * from teflow_holiday_define where id='"+Holiday.getHolidayId()+"'";
		Collection list = dbManager.query(selSQL);
		if(list!=null && list.size()>0) return 1;
		String sql = "insert into teflow_holiday_define(set_year, from_date, to_date, description, status, holiday_type) values(?,?,?,?,?,?)";
		Object[] obj = new Object[6];
		obj[0] = Holiday.getHolidayYear();
		obj[1] = Holiday.getHolidayFromDate();
		obj[2] = Holiday.getHolidayToDate();
		obj[3] = Holiday.getHolidayDescription();
		obj[4] = ""+Holiday.getHolidayStatus();
		obj[5] = Holiday.getHolidayType();
		int[] dataType = {DataType.CHAR,DataType.DATETIME,DataType.DATETIME,DataType.VARCHAR,DataType.INT,DataType.VARCHAR}; 
		dbManager.executeUpdate(sql,obj,dataType);
		return 0;
	}
	
	public int update(BaseVO vo) throws DAOException {
		HolidayVO Holiday = (HolidayVO)vo;
		String updateSQL = "update teflow_holiday_define set set_year=?, from_date=?, to_date=?, description=?, status=?, holiday_type=? where id=?";
		Object[] obj = new Object[7];
		obj[0] = Holiday.getHolidayYear();
		obj[1] = Holiday.getHolidayFromDate();
		obj[2] = Holiday.getHolidayToDate();
		obj[3] = Holiday.getHolidayDescription();
		obj[4] = ""+Holiday.getHolidayStatus();
		obj[5] = Holiday.getHolidayType();
		obj[6] = ""+Holiday.getHolidayId();		
		int[] dataType = {DataType.CHAR,DataType.DATETIME,DataType.DATETIME,DataType.VARCHAR,DataType.INT,DataType.VARCHAR,DataType.INT};
		dbManager.executeUpdate(updateSQL,obj,dataType);
		return 0;
	}
	/**
	* result[0]=added entry count; [1]=removed entry count
	*/ 
	public int[] importPMAOffDays(BaseVO vo) throws DAOException {
		HolidayVO holiday = (HolidayVO)vo;
		int[] result=new int[]{0,0}; 
		String insSQL = "insert into teflow_holiday_define(set_year, from_date, to_date, description, status, holiday_type) " +
				" select '"+holiday.getHolidayYear()+"', holiday_date, holiday_date,"+  
				" case datepart(dw,holiday_date) when 1 then 'Sunday' when 7 then 'Saturday' else '' end, 5, 1 from tpma_holiday a " +
				" where YEAR(holiday_date) = '"+holiday.getHolidayYear()+"' " +
				" and not exists (select 1 from teflow_holiday_define b where a.holiday_date between from_date and to_date)";
		result[0] = dbManager.executeUpdate(insSQL);
		
		String delSQL = "delete teflow_holiday_define "+
		" where YEAR(from_date) = '"+holiday.getHolidayYear()+"' " +
		" and      from_date not in (select holiday_date from tpma_holiday)";
		result[1] = dbManager.executeUpdate(delSQL);
		return result;
	}
}
