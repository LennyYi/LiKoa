package com.aiait.eflow.common.helper;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import com.aiait.eflow.housekeeping.dao.HolidayDAO;
import com.aiait.eflow.housekeeping.vo.HolidayVO;
import com.aiait.eflow.util.StringUtil;
import com.aiait.framework.db.DBManagerFactory;
import com.aiait.framework.db.IDBManager;

public class HolidayHelper {
    private static HolidayHelper helper = null;
    private static int holidayYear = 0;
    private static HashMap holidayMap = new HashMap();
	
	private HolidayHelper(){
		init();
	}
	
	public HashMap getHolidayMap(){
		return holidayMap;
	}
	
	public static HolidayHelper getInstance(){
		if(helper==null){
			helper = new HolidayHelper();
		}else{
			Calendar time = Calendar.getInstance(); 
			int currentYear = time.get(Calendar.YEAR);
			if(currentYear!=helper.getHolidayYear()){
			  holidayYear = currentYear;
			  helper = new HolidayHelper();
			}
		}
		return helper;
	}
	
	private static void init(){
		Calendar time = Calendar.getInstance(); 
		int currentYear = time.get(Calendar.YEAR);
		int lastYear = currentYear-1;
		IDBManager dbManager = null;	
		try{		
			dbManager =  DBManagerFactory.getDBManager();
			HolidayDAO dao = new HolidayDAO(dbManager);
			Collection list = dao.getHolidayList(""+currentYear);
			list.addAll(dao.getHolidayList(""+lastYear));//为解决跨年问题，需加入去年的假期列表
			if(list!=null){
			  Iterator it = list.iterator();
			  while(it.hasNext()){
				  HolidayVO vo = (HolidayVO)it.next();
				  String fromDate = vo.getHolidayFromDate();
				  String toDate = vo.getHolidayToDate();
				  if(fromDate.trim().equals(toDate.trim())){
					  holidayMap.put(fromDate.trim(),""+vo.getHolidayStatus());
				  }else{
					//否则则是跨越多天的时间段，则需要将每一天都转换成一个key
					//String preDate = StringUtil.afterNDay(-1,"MM/dd/yyyy",toDate); //2012/02/06逻辑错误，正确写法见下行
					String preDate = toDate;
					holidayMap.put(preDate,""+vo.getHolidayStatus());
					while(!preDate.equals(fromDate)){
						preDate = StringUtil.afterNDay(-1,"MM/dd/yyyy",preDate);
						holidayMap.put(preDate,""+vo.getHolidayStatus());
					}
				  }
			  }
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(dbManager!=null) dbManager.freeConnection();
		}
	}
	
	public int getHolidayYear(){
		return holidayYear;
	}
	
	public static void refresh(){
		holidayMap.clear();
		init();
	}
	
	public static void main(String[] args){
		System.out.println(HolidayHelper.getInstance().getHolidayMap().size());
	}
}
