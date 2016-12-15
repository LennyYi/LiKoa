package com.aiait.eflow.housekeeping.dao;

import java.sql.*;
import java.sql.Date;
import java.util.*;

import com.aiait.eflow.common.helper.LeaveEntitleHelper;
import com.aiait.eflow.housekeeping.vo.LeaveBalanceCommonInforVO;
import com.aiait.eflow.housekeeping.vo.LeaveBalanceVO;
import com.aiait.eflow.util.StringUtil;
import com.aiait.framework.db.*;
import com.aiait.framework.exception.DAOException;
import com.aiait.framework.vo.BaseVO;

public class LeaveBalanceCommonInforDAO extends BaseDAOImpl {

    public LeaveBalanceCommonInforDAO(IDBManager dbManager) {
        super(dbManager);
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

	/**
	 * get staff grade history list of the specific date
	 * 
	 * <li> type == 1:  before or equal the dateStr
	 * <li> type == 2:  just before the dateStr
	 * 
	 */
    
    public Collection getStaffGradeHistoryList(String staffCode, String dateStr, String type)throws DAOException {
        String SQL = "select New_grade, Old_grade, Effective_date from teflow_user_grade_history where (Staff_code = ? ) "
        	       + " and (year(Effective_date) = year ('"+dateStr+"') ) and (Effective_date ";
        
        
        if("1".equals(type)){
        	SQL = SQL + " <= ";
        }else if (type =="2"){
        	SQL = SQL + " < ";
        };
        
        SQL = SQL + " '"+dateStr+"') order by Effective_date";
        
        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;
        Collection gradeList = new Vector();

        try {
            stm = conn.prepareStatement(SQL);
            int i = 1;
            stm.setString(i++, staffCode);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                LeaveBalanceCommonInforVO leaveBalanceCommonInfor = new LeaveBalanceCommonInforVO();
                leaveBalanceCommonInfor.setNewGrade(rs.getDouble("New_grade"));
                leaveBalanceCommonInfor.setOldGrade(rs.getDouble("Old_grade"));
                leaveBalanceCommonInfor.setGradeEffectDate(StringUtil.getDateStr(rs.getDate("Effective_date"),"MM/dd/yyyy"));
                gradeList.add(leaveBalanceCommonInfor);
            }
            return gradeList;
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
    
	/**
	 * get staff grade history list
	 * 
	 * <li> type == 1:  before or equal the dateStr
	 * <li> type == 2:  just before the dateStr
	 * 
	 */
    
    public Collection getStaffGradeHistoryList(String staffCode)throws DAOException {
        String SQL = "select New_grade, Old_grade, Effective_date, Updated_leave, Updated_medical from teflow_user_grade_history where (Staff_code = ? ) "+
                     "order by Effective_date desc ";
                             
        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;
        Collection gradeList = new Vector();

        try {
            stm = conn.prepareStatement(SQL);
            int i = 1;
            stm.setString(i++, staffCode);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                LeaveBalanceCommonInforVO leaveBalanceCommonInfor = new LeaveBalanceCommonInforVO();
                leaveBalanceCommonInfor.setNewGrade(rs.getDouble("New_grade"));
                leaveBalanceCommonInfor.setOldGrade(rs.getDouble("Old_grade"));
                leaveBalanceCommonInfor.setGradeEffectDate(StringUtil.getDateStr(rs.getDate("Effective_date"),"MM/dd/yyyy"));
                leaveBalanceCommonInfor.setUpdatedLeave(rs.getString("Updated_leave"));
                leaveBalanceCommonInfor.setUpdatedMedical(rs.getString("Updated_medical"));
                gradeList.add(leaveBalanceCommonInfor);
            }
            return gradeList;
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
        
	/**
	 * get all permanent staff's which the grade were changed.
	 * 
	 * <li> dateStr: date of the grade effect
	 * <li> type: leave or medical
	 * 
	 */
    
    public Collection getListByGradeEffDate(String dateStr, String type)throws DAOException {
        String SQL = "select e.Staff_code, e.Onboard_date, e.Preworking_experience,g.New_grade, g.Old_grade, g.Effective_date, " +
			         "Total_wdays = convert (int, e.Preworking_experience * 365) + datediff(day, e.Onboard_date, g.Effective_date), " + 
			    	 "AIAIT_wdays = datediff(day, e.Onboard_date, g.Effective_date) "+
        	         "from tpma_staff s, teflow_user_work_experience e, teflow_user_grade_history g "+ 
        	         "where (s.staff_code = e.Staff_code) and (s.status='A') " +
        	         "and (Effective_date= (select MAX(g2.Effective_date) from teflow_user_grade_history g2 " +
        	                              "where (e.Staff_code = g2.Staff_code) and (year(g2.Effective_date) = year (?) ) " +
        	                              "and (g2.Effective_date <= ?) and (g2.<indicator> = 'N') )) "+       	                              
        	         "and (e.Staff_code = g.Staff_code) and (e.Staff_type ='1')";

        if (type == "medical"){
        	SQL += " and ((g.New_grade >7 and g.Old_grade <=7) or (g.Old_grade =0 ))";
        }
        
        String indicator = type == "leave" ? "Updated_leave" : "Updated_medical";
        SQL = SQL.replaceAll("<indicator>", indicator);
        
        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;
        Collection gradeList = new Vector();

        try {
            stm = conn.prepareStatement(SQL);
            int i = 1;
            stm.setString(i++, dateStr);
            stm.setString(i++, dateStr);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                LeaveBalanceCommonInforVO leaveBalanceCommonInfor = new LeaveBalanceCommonInforVO();
                
                leaveBalanceCommonInfor.setStaffCode(rs.getString("Staff_code"));
                leaveBalanceCommonInfor.setOnBoardDate(StringUtil.getDateStr(rs.getDate("Onboard_date"),"MM/dd/yyyy"));
                leaveBalanceCommonInfor.setPreWorkExperience(rs.getDouble("Preworking_experience"));
                leaveBalanceCommonInfor.setNewGrade(rs.getDouble("New_grade"));
                leaveBalanceCommonInfor.setOldGrade(rs.getDouble("Old_grade"));
                leaveBalanceCommonInfor.setGradeEffectDate(StringUtil.getDateStr(rs.getDate("Effective_date"),"MM/dd/yyyy"));
                leaveBalanceCommonInfor.setCompWorkDays(rs.getInt("AIAIT_wdays"));
                leaveBalanceCommonInfor.setTotalWorkDays(rs.getInt("Total_wdays"));
                gradeList.add(leaveBalanceCommonInfor);
            }
            return gradeList;
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
    
	/**
	 * get all permanent staff's which the AIAIT working experience were changed.
	 * 
	 * <li> dateStr: the experience change date is basic on the calculation of staff's experience and the specific date
	 * 
	 */
    
    public Collection getListByExperienceEffDate(String dateStr)throws DAOException {
        String SQL = "select * from ( select e.Staff_code, e.Onboard_date, e.Preworking_experience, " +  
                "Total_wdays = convert (int, e.Preworking_experience * 365) + datediff(day, e.Onboard_date, ?), " + 
        		"AIAIT_wdays = datediff(day, e.Onboard_date, ?) "+
        		"from teflow_user_work_experience e where (e.Staff_type ='1') and (? <> (select max(Effective_date) from teflow_user_grade_history where (e.Staff_code = Staff_code) ))) a " +
        		"where  ((AIAIT_wdays = ?) or (AIAIT_wdays = ?) or (AIAIT_wdays = ?)) ";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;
        Collection gradeList = new Vector();

        try {
            stm = conn.prepareStatement(SQL);
            int i = 1;
            stm.setString(i++, dateStr);
            stm.setString(i++, dateStr);     
            stm.setString(i++, dateStr); 
            
            stm.setInt(i++, LeaveEntitleHelper.LEAVE_BALANCE_5YEARS);
            stm.setInt(i++, LeaveEntitleHelper.LEAVE_BALANCE_10YEARS);
            stm.setInt(i++, LeaveEntitleHelper.LEAVE_BALANCE_15YEARS);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                LeaveBalanceCommonInforVO leaveBalanceCommonInfor = new LeaveBalanceCommonInforVO();
                
                leaveBalanceCommonInfor.setStaffCode(rs.getString("Staff_code"));
                leaveBalanceCommonInfor.setOnBoardDate(StringUtil.getDateStr(rs.getDate("Onboard_date"),"MM/dd/yyyy"));
                leaveBalanceCommonInfor.setPreWorkExperience(rs.getDouble("Preworking_experience"));
                leaveBalanceCommonInfor.setCompWorkDays(rs.getInt("AIAIT_wdays"));
                leaveBalanceCommonInfor.setTotalWorkDays(rs.getInt("Total_wdays"));
                gradeList.add(leaveBalanceCommonInfor);
            }
            return gradeList;
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
    
	/**
	 * get all permanent staff's which the total working experience were changed.
	 * 
	 * <li> dateStr: the experience change date is basic on the calculation of staff's pre-working experience and the specific date
	 * 
	 */
    
    public Collection getListByTotalExperienceEffDate(String dateStr)throws DAOException {
        String SQL = "select * from ( select e.Staff_code, e.Onboard_date, e.Preworking_experience, " +
        		"Total_wdays = convert (int, e.Preworking_experience * 365) + datediff(day, e.Onboard_date, ?), " +  
        		"AIAIT_wdays = datediff(day, e.Onboard_date, ?) "+
        		"from teflow_user_work_experience e where (e.Staff_type ='1') and (? <> (select max(Effective_date) from teflow_user_grade_history where (e.Staff_code = Staff_code) ))) a " +
        		"where ( (a.Total_wdays = ?) or (a.Total_wdays = ?) or (a.Total_wdays = ?) ) " +
        		"and ((a.AIAIT_wdays <> ?) and (a.AIAIT_wdays <> ?) and (a.AIAIT_wdays <> ?)) ";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;
        Collection gradeList = new Vector();

        try {
            stm = conn.prepareStatement(SQL);
            int i = 1;
            stm.setString(i++, dateStr);
            stm.setString(i++, dateStr);      
            stm.setString(i++, dateStr); 
            
            stm.setInt(i++, LeaveEntitleHelper.LEAVE_BALANCE_1YEAR);
            stm.setInt(i++, LeaveEntitleHelper.LEAVE_BALANCE_10YEARS);
            stm.setInt(i++, LeaveEntitleHelper.LEAVE_BALANCE_20YEARS);
            
            stm.setInt(i++, LeaveEntitleHelper.LEAVE_BALANCE_5YEARS);
            stm.setInt(i++, LeaveEntitleHelper.LEAVE_BALANCE_10YEARS);
            stm.setInt(i++, LeaveEntitleHelper.LEAVE_BALANCE_15YEARS);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                LeaveBalanceCommonInforVO leaveBalanceCommonInfor = new LeaveBalanceCommonInforVO();
                
                leaveBalanceCommonInfor.setStaffCode(rs.getString("Staff_code"));
                leaveBalanceCommonInfor.setOnBoardDate(StringUtil.getDateStr(rs.getDate("Onboard_date"),"MM/dd/yyyy"));
                leaveBalanceCommonInfor.setPreWorkExperience(rs.getDouble("Preworking_experience"));
                leaveBalanceCommonInfor.setCompWorkDays(rs.getInt("AIAIT_wdays"));
                leaveBalanceCommonInfor.setTotalWorkDays(rs.getInt("Total_wdays"));
                gradeList.add(leaveBalanceCommonInfor);
            }
            return gradeList;
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
    
	/**
	 * get the closest grade history  of the specific date
	 * 
	 * <li> staffCode : staff code
	 * <li> dateStr: the closest grade record before the dateStr
	 * 
	 */
    
    public LeaveBalanceCommonInforVO getClosestGrade(String staffCode, String dateStr) throws DAOException {
    	String SQL = "select New_grade, Old_grade, Effective_date from teflow_user_grade_history where  (Staff_code = ? )and" 
    		        +" Effective_date in (select max(Effective_date) from teflow_user_grade_history where (Effective_date < ? ) and (Staff_code = ? ))";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(SQL);
            int i = 1;
            stm.setString(i++, staffCode);
            stm.setString(i++, dateStr);
            stm.setString(i++, staffCode);
            ResultSet rs = stm.executeQuery();
            if (!rs.next()) {
                return null;
            }
            LeaveBalanceCommonInforVO leaveBalanceCommonInfor = new LeaveBalanceCommonInforVO();
            
            leaveBalanceCommonInfor.setNewGrade(rs.getDouble("New_grade"));
            leaveBalanceCommonInfor.setOldGrade(rs.getDouble("Old_grade"));
            leaveBalanceCommonInfor.setGradeEffectDate(StringUtil.getDateStr(rs.getDate("Effective_date"),"MM/dd/yyyy"));
  
            
            return leaveBalanceCommonInfor;
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
    
	/**
	 * get all permanent staff's common information.
	 * 
	 * <li> dateStr: the closest grade record before or equal the dateStr
	 * 
	 */
    
    public Collection getAllStaffBalanceCommonInfor(String dateStr)throws DAOException {
        String SQL = "select e.Staff_code, e.Staff_type, e.Onboard_date, e.Preworking_experience, g.New_grade, g.Old_grade, g.Effective_date," +
        		" Total_wdays = convert (int, e.Preworking_experience * 365) + datediff(day, e.Onboard_date, ?)," +
        		" AIAIT_wdays = datediff(day, e.Onboard_date, ?)" +
        		" from tpma_staff s, teflow_user_work_experience e, teflow_user_grade_history g" +
        		" where (s.staff_code = e.Staff_code) and (s.status='A') " +
        		" and (e.Staff_code = g.Staff_code) and (e.Staff_type ='1') and" +
        		" (g.Effective_date in (select max(Effective_date) from teflow_user_grade_history" +
        		" where (Staff_code = e.Staff_code) and (Effective_date <= ? ) ) ) ";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;
        Collection gradeList = new Vector();

        try {
            stm = conn.prepareStatement(SQL);
            int i = 1;
            stm.setString(i++, dateStr);
            stm.setString(i++, dateStr);
            stm.setString(i++, dateStr);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                LeaveBalanceCommonInforVO leaveBalanceCommonInfor = new LeaveBalanceCommonInforVO();
                
                leaveBalanceCommonInfor.setStaffCode(rs.getString("Staff_code"));
                leaveBalanceCommonInfor.setOnBoardDate(StringUtil.getDateStr(rs.getDate("Onboard_date"),"MM/dd/yyyy"));
                leaveBalanceCommonInfor.setPreWorkExperience(rs.getDouble("Preworking_experience"));
                leaveBalanceCommonInfor.setNewGrade(rs.getDouble("New_grade"));
                leaveBalanceCommonInfor.setOldGrade(rs.getDouble("Old_grade"));
                leaveBalanceCommonInfor.setGradeEffectDate(StringUtil.getDateStr(rs.getDate("Effective_date"),"MM/dd/yyyy"));
                leaveBalanceCommonInfor.setCompWorkDays(rs.getInt("AIAIT_wdays"));
                leaveBalanceCommonInfor.setTotalWorkDays(rs.getInt("Total_wdays"));
                gradeList.add(leaveBalanceCommonInfor);
            }
            return gradeList;
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
    
	/**
	 * get individual staff's common information, return the grade record which is the max effective date
	 * 
	 * <li> staffCode: staff code
	 * 
	 */

    public LeaveBalanceCommonInforVO getBalanceCommonInfor(String staffCode) throws DAOException {
        String SQL = "select e.Staff_code, e.Staff_type, e.Onboard_date, e.Preworking_experience, e.Medical_exception,"
        			+" g.New_grade, g.Old_grade, g.Effective_date, g.Updated_leave, g.Updated_medical" 
        	        +" from teflow_user_work_experience e left join teflow_user_grade_history g on (e.Staff_code = g.Staff_code)" 
        	        +" and (g.Effective_date in (select max(Effective_date) from teflow_user_grade_history where Staff_code = ? ) )"
        	        +" where (e.Staff_code = ? ) ";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(SQL);
            int i = 1;
            stm.setString(i++, staffCode);
            stm.setString(i++, staffCode);
            ResultSet rs = stm.executeQuery();
            if (!rs.next()) {
                return null;
            }
            LeaveBalanceCommonInforVO leaveBalanceCommonInfor = new LeaveBalanceCommonInforVO();
            
            leaveBalanceCommonInfor.setStaffCode(staffCode);
            leaveBalanceCommonInfor.setStaffType(rs.getString("Staff_type"));
            if(rs.getDate("Onboard_date")!= null){
                leaveBalanceCommonInfor.setOnBoardDate(StringUtil.getDateStr(rs.getDate("Onboard_date"),"MM/dd/yyyy"));
            }else{
            	leaveBalanceCommonInfor.setOnBoardDate("");
            }
            leaveBalanceCommonInfor.setPreWorkExperience(rs.getDouble("Preworking_experience"));
            leaveBalanceCommonInfor.setNewGrade(rs.getDouble("New_grade"));
            leaveBalanceCommonInfor.setOldGrade(rs.getDouble("Old_grade"));
            if(rs.getDate("Effective_date")!= null){
            	leaveBalanceCommonInfor.setGradeEffectDate(StringUtil.getDateStr(rs.getDate("Effective_date"),"MM/dd/yyyy"));
            }else{
            	leaveBalanceCommonInfor.setGradeEffectDate("");
            }
            
            leaveBalanceCommonInfor.setMedicalException(rs.getString("Medical_exception"));
            leaveBalanceCommonInfor.setUpdatedLeave(rs.getString("Updated_leave"));
            leaveBalanceCommonInfor.setUpdatedMedical(rs.getString("Updated_medical"));
            
            return leaveBalanceCommonInfor;
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
    


    public void saveBalanceCommonInfor(LeaveBalanceCommonInforVO leaveBalanceCommonInfor) throws DAOException {
        this.saveWorkExperience(leaveBalanceCommonInfor);
        this.saveGrade(leaveBalanceCommonInfor);
    }
    
    public void saveWorkExperience(LeaveBalanceCommonInforVO leaveBalanceCommonInfor) throws DAOException {
        this.deleteWorkExperience(leaveBalanceCommonInfor);   
        this.addWorkExperience(leaveBalanceCommonInfor);
    }
    
    public void saveGrade(LeaveBalanceCommonInforVO leaveBalanceCommonInfor) throws DAOException {
        this.deleteGrade(leaveBalanceCommonInfor);  
        if(leaveBalanceCommonInfor.getOldGrade()==-1 ){
          this.getOldGrade(leaveBalanceCommonInfor);
        }
        this.addWorkGrade(leaveBalanceCommonInfor);
    }

    public void addWorkExperience(LeaveBalanceCommonInforVO leaveBalanceCommonInfor) throws DAOException {
        String SQL = "insert into teflow_user_work_experience(Staff_code, Staff_type, Onboard_date, Preworking_experience, Medical_exception) " 
        	        +"values (?,?,?,?,?)";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(SQL);
            int i = 1;
            stm.setString(i++, leaveBalanceCommonInfor.getStaffCode());
            stm.setString(i++, leaveBalanceCommonInfor.getStaffType());
            stm.setDate(i++, StringUtil.stringToSqlDate(leaveBalanceCommonInfor.getOnBoardDate(), "MM/dd/yyyy"));
            stm.setDouble(i++, leaveBalanceCommonInfor.getPreWorkExperience());
            stm.setString(i++, leaveBalanceCommonInfor.getMedicalException());
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

    public void addWorkGrade(LeaveBalanceCommonInforVO leaveBalanceCommonInfor) throws DAOException {
        String SQL = "insert into teflow_user_grade_history(Staff_code, New_grade, Old_grade, Modified_Staff, Effective_date, Updated_leave, Updated_medical) " 
        	        +"values (?,?,?,?,?,?,?)";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(SQL);
            int i = 1;
            stm.setString(i++, leaveBalanceCommonInfor.getStaffCode());
            stm.setDouble(i++, leaveBalanceCommonInfor.getNewGrade());
            stm.setDouble(i++, leaveBalanceCommonInfor.getOldGrade());
            stm.setString(i++, leaveBalanceCommonInfor.getModifiedStaff());
            stm.setDate(i++, StringUtil.stringToSqlDate(leaveBalanceCommonInfor.getGradeEffectDate(), "MM/dd/yyyy"));
            stm.setString(i++, leaveBalanceCommonInfor.getUpdatedLeave());
            stm.setString(i++, leaveBalanceCommonInfor.getUpdatedMedical());
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
    public void deleteWorkExperience(LeaveBalanceCommonInforVO leaveBalanceCommonInfor) throws DAOException {
        String SQL = "delete from teflow_user_work_experience where Staff_code = ?";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(SQL);
            int i = 1;
            stm.setString(i++, leaveBalanceCommonInfor.getStaffCode());
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
    
   
    public void deleteGrade(LeaveBalanceCommonInforVO leaveBalanceCommonInfor) throws DAOException {
        String SQL = "delete from teflow_user_grade_history where ( Staff_code = ? ) and ( Effective_date = ? )";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(SQL);
            int i = 1;
            stm.setString(i++, leaveBalanceCommonInfor.getStaffCode());
            stm.setDate(i++, StringUtil.stringToSqlDate(leaveBalanceCommonInfor.getGradeEffectDate(), "MM/dd/yyyy"));
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
    
    public void getOldGrade(LeaveBalanceCommonInforVO leaveBalanceCommonInfor)throws DAOException {
        String SQL = "select New_grade from teflow_user_grade_history" 
        	       +" where Effective_date in (select max(Effective_date) from teflow_user_grade_history where (Staff_code = ?) and (Effective_date < ? ) )" 
        	       +" and Staff_code = ? ";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;
        double currentGrade = 0.0;

        try {
            stm = conn.prepareStatement(SQL);
            int i = 1;
            stm.setString(i++, leaveBalanceCommonInfor.getStaffCode());
            stm.setString(i++, leaveBalanceCommonInfor.getGradeEffectDate());
            stm.setString(i++, leaveBalanceCommonInfor.getStaffCode());
            ResultSet rs = stm.executeQuery();
            while (rs.next()) { 	
            	currentGrade = rs.getDouble("New_grade");
            }
            
            leaveBalanceCommonInfor.setOldGrade(currentGrade);
           
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
    
	/**
	 * Update indicator to indicate the grade change has updated the relative balance
	 * 
	 * <li> leaveBalanceCommonInfor: 
	 * <li> type: leave or medical
	 * 
	 */
    public void updateGradeIndicator(LeaveBalanceCommonInforVO leaveBalanceCommonInfor, String type) throws DAOException {
        String SQL = "update teflow_user_grade_history set <indicator> = 'Y' "+   
        			 "where ( Staff_code = ? ) and (year(Effective_date) = year (?) ) " +
        			 "and ( Effective_date <= ? ) and (<indicator> = 'N') ";  

        String indicator = type == "leave" ? "Updated_leave" : "Updated_medical";
        SQL = SQL.replaceAll("<indicator>", indicator);
        
        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(SQL);
            int i = 1;
            stm.setString(i++, leaveBalanceCommonInfor.getStaffCode());
            stm.setDate(i++, StringUtil.stringToSqlDate(leaveBalanceCommonInfor.getGradeEffectDate(), "MM/dd/yyyy"));
            stm.setDate(i++, StringUtil.stringToSqlDate(leaveBalanceCommonInfor.getGradeEffectDate(), "MM/dd/yyyy"));
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

}
