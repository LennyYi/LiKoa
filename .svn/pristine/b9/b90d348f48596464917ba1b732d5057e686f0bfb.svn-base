package com.aiait.eflow.job;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.*;

import com.aiait.eflow.common.CommonName;
import com.aiait.eflow.common.helper.CompanyHelper;
import com.aiait.eflow.common.helper.ParamConfigHelper;
import com.aiait.eflow.common.helper.StaffTeamHelper;
import com.aiait.eflow.housekeeping.dao.ApproverGroupMemberDAO;
import com.aiait.eflow.housekeeping.dao.StaffDAO;
import com.aiait.eflow.housekeeping.vo.EflowStaffVO;
import com.aiait.eflow.util.EFlowEmailUtil;
import com.aiait.eflow.util.FieldUtil;
import com.aiait.framework.db.*;

import com.aiait.eflow.util.EncryptUtil;
import com.aiait.eflow.wkf.dao.WorkFlowProcessDAO;
import com.aiait.eflow.wkf.vo.WorkFlowProcessVO;

/**
 * HR DB Synchronize Job
 * 
 * @version 2012-05-22
 */
public class HrSyncJob extends BaseJob {

    public static final String JOB_ID = "hrsync_job";
    
    public HrSyncJob(int hour, int minute, int second) {
        super(hour, minute, second);
    }

    public String getJobId() {
        return JOB_ID;
    }

    public SchedulerCtrlTask createTask() {
        return new SchedulerCtrlTask(JOB_ID) {
        	protected void process(){
                IDBManager dbManager = null;
                String server, db, login, pwd;
                
                try {
                	Calendar cal=Calendar.getInstance(); cal.setTime(new Date());
                	if(cal.get(Calendar.DATE)==1 || cal.get(Calendar.DATE)==16){
                		//每月只跑2次
	                    dbManager = DBManagerFactory.getDBManager();
	                    Collection c = dbManager.query("select * from teflow_HRDB_setting");
	                    Iterator it = c.iterator();        
	                    EncryptUtil encrypt = new EncryptUtil();            
	                    if(it.hasNext()){
	                    	HashMap map = (HashMap)it.next();
	                    	server = FieldUtil.convertSafeString(map,"SERVER");
	                    	db = FieldUtil.convertSafeString(map,"DB");
	                    	login = FieldUtil.convertSafeString(map,"LOGIN");
	                    	pwd = FieldUtil.convertSafeString(map,"PASSWORD");     
	                    	
	                    	server = encrypt.UnryptDES(server);
	                    	db = encrypt.UnryptDES(db);
	                    	login = encrypt.UnryptDES(login);
	                    	pwd = encrypt.UnryptDES(pwd);
                    	
	                    	getTerminatedStaffList(server, db, login, pwd);
	                    	
	                    }
                	}
                	
                	terminateTempStaffs();
                	
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (dbManager != null)
                        dbManager.freeConnection();
                }
            }
        };
    }

    static public void terminateTempStaffs() {
    	IDBManager dbManager = null;
    	try{
	    	dbManager = DBManagerFactory.getDBManager();
	    	
	    	Collection rsEFLOW = dbManager.query("Select staff_code from tpma_staffbasic where user_type='1' " +
	    			"and CONVERT(datetime,to_date) < getdate() and status='A'");
	    	Iterator itEFLOW =  rsEFLOW.iterator(); 
			StaffDAO sdao = new StaffDAO(dbManager);
			WorkFlowProcessDAO wkfdao = new WorkFlowProcessDAO(dbManager);
           	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	    	while(itEFLOW.hasNext()){
	    		HashMap map = (HashMap)itEFLOW.next();
	    		String staffCode = (String)map.get("STAFF_CODE");
	    		EflowStaffVO staff = sdao.getStaffByStaffCode(staffCode);                		
				boolean isleader = sdao.IsTorPLeader(staff.getLogonId());
	
				if (isleader) {
					continue;
				} else 
					if (!wkfdao.isStaffProcessCompleted(staff.getStaffCode())) {					
					continue;
				} else 
					if (wkfdao.getForDealFormListByStaff(staff.getStaffCode(),new WorkFlowProcessVO()).size()>0) {
					continue;
				}
				sdao.saveHistory(staff, df.format(new Date()), "HRDB");
				sdao.deleteStaffByStaffcode(staffCode);
				sdao.updateduputytable(staffCode);
				new ApproverGroupMemberDAO(dbManager).deleteStaffApproverGroup(staffCode);				        				
	    	}
    	} catch (Exception e) {
            e.printStackTrace();
        } finally {
        	if (dbManager != null)dbManager.freeConnection();
        }
	}

	static public void getTerminatedStaffList(String server, String db, String login, String pwd){
    	IDBManager dbManager = null;
    	String[] hrdb = ParamConfigHelper.getInstance().getParamValue("hrdb_maintain").split(";");
    	String[] keyArr = {"Z07001","Z07002","Z07003","Z07004","Z07005","Z07009","Z07011","Z07012","Z07016","Z07017","Z07018","Z09003"};
       	HashMap aggregateMap = new HashMap();
    	int global_cnt = 0;
    	
        try {
        	Connection conn;
        	ResultSet rs;
        	Statement st;
        	//变量声明结束
        	//Class.forName("com.microsoft.jdbc.sqlserver.SQLServerDriver");//jdbc驱动 UT only! 
        	Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");//jdbc驱动 TODO e.g. CIGD3R8CSQL01
        	//数据库连接字符串
        	//String url="jdbc:microsoft:sqlserver://"+server+":1433;DatabaseName="+db; //sqlsvr2000 only! 
        	String url="jdbc:sqlserver://"+server+":1433;DatabaseName="+db;             //sqlsvr2000 and 2005
        	conn= DriverManager.getConnection(url,login,pwd); //设置conn
        	//设置st
        	st = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
        	rs = st.executeQuery("select * from "+hrdb[1]+ " order by "+ hrdb[2]);//sql查询语句
        	rs.beforeFirst();

           	dbManager = DBManagerFactory.getDBManager();
           	StringBuffer str = new StringBuffer("");
           	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
           	String minStr = "";
           	String maxStr = "";
           	Collection rsEFLOW = null;
           	Iterator itEFLOW = null;           	
        	int cnt = 0;
           	
           	for(int i=0;i<keyArr.length;i++)
           		aggregateMap.put(keyArr[i],new SyncDetailVO()); 
           	
			dbManager.startTransaction();
        	while(true){
        		global_cnt++;
        		if(rs.next() && cnt<100){
        			if(cnt==0){
        				minStr = maxStr;
        				str = new StringBuffer("");    
        				str.append("'"+ minStr +"'");
        			}
        			str.append(",'"+rs.getString(hrdb[2]).trim()+"'");
        			maxStr = rs.getString(hrdb[2]).trim();
            		cnt++;
        		}else{
        			String sql = "select staff_code from tpma_staffbasic where staff_code between '"+ minStr + "' and '" + 
                		maxStr + "' and staff_code not in (" + str.toString() + ") and status='A' and user_type='0' and len(staff_code)<=10";
                	if(cnt<100){
                		sql = "select staff_code from tpma_staffbasic where staff_code >= '"+
        				minStr + "' and staff_code not in (" + str.toString() + ") and status='A' and user_type='0' and len(staff_code)<=10";
                	}
                	System.out.println(global_cnt+" ... "+sql);     
                	rsEFLOW = dbManager.query(sql);
                	itEFLOW = rsEFLOW.iterator(); 
        			StaffDAO sdao = new StaffDAO(dbManager);
        			WorkFlowProcessDAO wkfdao = new WorkFlowProcessDAO(dbManager);
                	while(itEFLOW.hasNext()){
                		HashMap map = (HashMap)itEFLOW.next();
                        String staffCode = (String)map.get("STAFF_CODE");
                        EflowStaffVO staff = sdao.getStaffByStaffCode(staffCode);
                        boolean isleader = sdao.IsTorPLeader(staff.getLogonId());
                        SyncDetailVO syncDetail = (SyncDetailVO)aggregateMap.get(StaffTeamHelper.getInstance().getStaffByCode(staffCode).getOrgId());
                        StringBuffer ngReport = syncDetail.getNgReport();
                        StringBuffer okReport = syncDetail.getOkReport();
                        if(isleader)
                        {
                            syncDetail.incrementNgCount();
                            ngReport.append((new StringBuilder(String.valueOf(staff.getStaffName()))).append("(").append(staff.getStaffCode()).append(") is a team or project leader, cannot be terminated!<br>").toString());
        					continue;
                        } else
                        if(!wkfdao.isStaffProcessCompleted(staff.getStaffCode()))
                        {
                            syncDetail.incrementNgCount();
                            ngReport.append((new StringBuilder(String.valueOf(staff.getStaffName()))).append("(").append(staff.getStaffCode()).append(") has flows in processing, cannot be terminated!<br>").toString());
        					continue;
                        } else 
        				if (wkfdao.getForDealFormListByStaff(staff.getStaffCode(),new WorkFlowProcessVO()).size()>0) {
        					syncDetail.incrementNgCount();
                            ngReport.append("Staff "+staff.getStaffName()+"(" + staff.getStaffCode()
        							+ ") has forms to handle, it's status cannot be Terminated!");
        					continue;
        				}
        				sdao.saveHistory(staff, df.format(new Date()), "HRDB");
        				sdao.deleteStaffByStaffcode(staffCode);
        				sdao.updateduputytable(staffCode);
        				new ApproverGroupMemberDAO(dbManager).deleteStaffApproverGroup(staffCode);
        				syncDetail.incrementOkCount();
        				okReport.append("Staff "+staff.getStaffName()+"(" + staff.getStaffCode() + ") terminated.<br>-----------------<br>");        				
                	}     	
            		if(cnt==100){
                    	cnt = 0;
                		global_cnt--;
                    	rs.previous();//Important!
            		} else {
            			break;
            		}
        		}
        	}
			dbManager.commit();
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            WorkFlowProcessVO process = new WorkFlowProcessVO();
            process.setFormType("12");
            String adminEmailAddress = ParamConfigHelper.getInstance().getParamValue(
                    CommonName.PARAM_ADMIN_EMAIL);
            if(global_cnt != 0){
	            try{
	            	StringBuffer reportAll = new StringBuffer();
	               	for(int i=0;i<keyArr.length;i++){
	               		SyncDetailVO syncDetailOut = (SyncDetailVO)aggregateMap.get(keyArr[i]);
	               		int totalHeadCnt = syncDetailOut.getOkCount() + syncDetailOut.getNgCount();
	                    if(totalHeadCnt != 0)
	                    {
	                        reportAll.append("<br><br><font size=2 face=Arial color=navy><b>");
	                        reportAll.append((new StringBuilder(String.valueOf(CompanyHelper.getInstance().getCompany(keyArr[i]).getOrgName()))).append(": </b></font><br>").toString());
	                        reportAll.append((new StringBuilder("<font size=2 face=Arial color=red>共自动处理")).append(totalHeadCnt).append("个员工账户，")
	                        		.append(syncDetailOut.getOkCount()).append("个成功终止，").append(syncDetailOut.getNgCount()).append("个终止失败，请管理员核对。").toString());
	                        reportAll.append("</font><br><font size=2 face=Arial color=navy><b>");
	                        reportAll.append(syncDetailOut.getNgReport());
	                        reportAll.append("</b>");
	                        reportAll.append(syncDetailOut.getOkReport());
	                    }
	               	}
	               	EFlowEmailUtil.sendEmail("E-Flow HRDB sync result", reportAll.toString(), adminEmailAddress, process);
	            }catch(Exception ex){
	            	ex.printStackTrace();
	            	System.out.println(ex.getMessage());
	            }
            }
            if (dbManager != null)
                dbManager.freeConnection();
        }
    }
}

class SyncDetailVO
{

    SyncDetailVO()
    {
        okReport = new StringBuffer();
        ngReport = new StringBuffer();
    }

    public int getOkCount()
    {
        return okCount;
    }

    public void incrementOkCount()
    {
        okCount++;
    }

    public int getNgCount()
    {
        return ngCount;
    }

    public void incrementNgCount()
    {
        ngCount++;
    }

    public StringBuffer getOkReport()
    {
        return okReport;
    }

    public void setOkReport(StringBuffer okReport)
    {
        this.okReport = okReport;
    }

    public StringBuffer getNgReport()
    {
        return ngReport;
    }

    public void setNgReport(StringBuffer ngReport)
    {
        this.ngReport = ngReport;
    }

    private int okCount;
    private int ngCount;
    private StringBuffer okReport;
    private StringBuffer ngReport;
}
