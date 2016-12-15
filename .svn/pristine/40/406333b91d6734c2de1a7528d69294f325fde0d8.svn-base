/******************************************************************************************/
/*Task_ID	Author	Modify_Date	Description                       						  */
/*IT1029	Young	06/30/2008	DS-002 Team filter values vary depending on company filter*/
/******************************************************************************************/
package com.aiait.eflow.common.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import com.aiait.eflow.housekeeping.dao.StaffDAO;
import com.aiait.eflow.housekeeping.dao.TeamDAO;
import com.aiait.eflow.housekeeping.vo.StaffVO;
import com.aiait.eflow.housekeeping.vo.TeamVO;
import com.aiait.eflow.util.StringUtil;
import com.aiait.framework.db.DBManagerFactory;
import com.aiait.framework.db.IDBManager;

public class StaffTeamHelper {
	private static StaffTeamHelper instance = null;
	
	private static HashMap staffMap = new HashMap();
	private static HashMap teamMap = new HashMap();
	private static Collection staffList = new ArrayList();
	private static Collection teamList = new ArrayList();
	
	private static Collection atstaffList = new ArrayList();
	private static HashMap atstaffMap = new HashMap();
	
	
	private StaffTeamHelper(){
		refresh();
		//init();
	}
	
    public static StaffTeamHelper getInstance(){
    	if(instance==null){
    		synchronized(StaffTeamHelper.class){
    		   if(instance==null){
    			 instance = new StaffTeamHelper();
    		   }
    		}
    	}
    	return instance;
    }
    
    public void setInstanceNull(){
    	clear();
    	this.instance = null;
    }
    
    public StaffVO getStaffByCode(String staffCode){
    	if(staffMap.containsKey(staffCode.trim())){
    		return (StaffVO)staffMap.get(staffCode.trim());
    	}
    	return null;
    }
    
    public TeamVO getTeamByCode(String teamCode){
    	if(teamMap.containsKey(teamCode.trim())){
    		return (TeamVO)teamMap.get(teamCode.trim());
    	}
    	return null;
    }
    
    public String getStaffNameByCode(String staffCode){
    	//如果是多个staffCode连在一起，则分割符号是“,”
    	String[] staffCodeList = StringUtil.split(staffCode,",");
    	String staffName = "";
    	for(int i=0;i<staffCodeList.length;i++){
    	  if(atstaffMap.containsKey(staffCodeList[i].trim())){
    		  StaffVO staff = (StaffVO)atstaffMap.get(staffCodeList[i].trim());
    		  staffName = staffName + staff.getStaffName() + ("T".equalsIgnoreCase(staff.getStatus()) ? "(T)":"")+", ";
    		//return ((StaffVO)staffMap.get(staffCode.trim())).getStaffName();
    	  }
    	}
    	if(!"".equals(staffName)){
    	  staffName = staffName.substring(0,staffName.length()-2);
    	  return staffName;	
    	}
    	return staffCode;
    }
    
    public String getTeamNameByCode(String teamCode){
    	if(teamMap.containsKey(teamCode.trim())){
    		return ((TeamVO)teamMap.get(teamCode.trim())).getTeamName();
    	}
    	return teamCode;
    }
    
    public Collection getStaffList(){
    	return StaffTeamHelper.staffList;
    }
    
    public Collection getATStaffList(){
    	return StaffTeamHelper.atstaffList;
    }    
    
    public Collection getStaffListByTeam(String teamCode){
    	Collection staffListByTeam = new ArrayList();
    	IDBManager dbManager = null;
    	try{
  		  dbManager =  DBManagerFactory.getDBManager();
  		  StaffDAO staffDao = new StaffDAO(dbManager);
  		  staffListByTeam = staffDao.getStaffListByTeam(teamCode);
  		  return staffListByTeam;
    	}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(dbManager!=null) dbManager.freeConnection();
		}
    	return staffListByTeam;
    }
    
    public Collection getStaffListByCompany(String orgId){
    	Collection staffListByCompany = new ArrayList();
    	IDBManager dbManager = null;
    	try{
  		  dbManager =  DBManagerFactory.getDBManager();
  		  StaffDAO staffDao = new StaffDAO(dbManager);
  		  staffListByCompany = staffDao.getStaffListByCompany(orgId);
  		  return staffListByCompany;
    	}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(dbManager!=null) dbManager.freeConnection();
		}
    	
    	return staffListByCompany;
    }
    
    public Collection getTeamList(){
    	return StaffTeamHelper.teamList;
    }
    
    private void init(){
		IDBManager dbManager = null;
		try{
		  dbManager =  DBManagerFactory.getDBManager();
		  StaffDAO staffDao = new StaffDAO(dbManager);
		  Collection staffListTemp = staffDao.getAllStaff();
		  if(staffListTemp!=null){
			staffList.clear();
			staffMap.clear();
			Iterator staffIt = staffListTemp.iterator();
			while(staffIt.hasNext()){
				StaffVO staff = (StaffVO)staffIt.next();
				StaffTeamHelper.staffList.add(staff);
				staffMap.put(staff.getStaffCode().trim(),staff);
			}
		  }
		  
		  Collection atstaffListTemp = staffDao.getAllATStaff();
		  if(atstaffListTemp!=null){
			atstaffList.clear();
			atstaffMap.clear();
			Iterator staffIt = atstaffListTemp.iterator();
			while(staffIt.hasNext()){
				StaffVO staff = (StaffVO)staffIt.next();
				StaffTeamHelper.atstaffList.add(staff);
				atstaffMap.put(staff.getStaffCode().trim(),staff);
			}
		  }
		  
		  TeamDAO teamDao = new TeamDAO(dbManager);
		  Collection teamListTemp = teamDao.getTeamList("");
		  if(teamListTemp!=null){
			  Iterator teamIt = teamListTemp.iterator();
			  while(teamIt.hasNext()){
				  TeamVO team = (TeamVO)teamIt.next();
				  teamList.add(team);
				  teamMap.put(team.getTeamCode().trim(),team);
			  }
		  }
		  
		  if(instance!=null)teamDao.syncDeptFromPMA();
		  
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(dbManager!=null) dbManager.freeConnection();
		}
    }

    public void addStaff(String staffCode) {
        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            StaffDAO staffDao = new StaffDAO(dbManager);
            StaffVO staff = staffDao.getStaffByStaffCode(staffCode);
            if (staff != null) {
                staffList.add(staff);
                staffMap.put(staff.getStaffCode(), staff);
                atstaffList.add(staff);
                atstaffMap.put(staff.getStaffCode(), staff);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (dbManager != null) {
                dbManager.freeConnection();
            }
        }
    }

	public void clear(){
    	staffMap.clear();
    	teamMap.clear();
    	staffList.clear();
    	teamList.clear();
    	atstaffList.clear();
    	atstaffMap.clear();
	}
    
    public  void refresh(){
    	clear();
    	init();
    }
}
