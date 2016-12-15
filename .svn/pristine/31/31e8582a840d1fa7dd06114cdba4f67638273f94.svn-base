package com.aiait.eflow.housekeeping.dao;

import java.sql.*;
import java.util.*;

import com.aiait.eflow.housekeeping.vo.LeaveBalanceCommonInforVO;
import com.aiait.eflow.housekeeping.vo.LeaveBalanceVO;
import com.aiait.framework.db.*;
import com.aiait.framework.exception.DAOException;
import com.aiait.framework.vo.BaseVO;

public class LeaveBalanceDAO extends BaseDAOImpl {

    public LeaveBalanceDAO(IDBManager dbManager) {
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

    public Collection getBalanceList(String staffCode) throws DAOException {
        String SQL = "select * from teflow_leave_balance where (staff_code = ?) order by year desc";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;
        Collection balanceList = new Vector();

        try {
            stm = conn.prepareStatement(SQL);
            int i = 1;
            stm.setString(i++, staffCode);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                LeaveBalanceVO leaveBalance = new LeaveBalanceVO();
                leaveBalance.setStaffCode(rs.getString("staff_code"));
                leaveBalance.setYear(rs.getInt("year"));
                leaveBalance.setAnnualTotalEntitleDays(rs.getDouble("annual_total_entitle_days"));
                leaveBalance.setAnnualCarryForwardDays(rs.getDouble("annual_carry_forward_days"));
                leaveBalance.setAnnualAppliedDays(rs.getDouble("annual_applied_days"));
                
                leaveBalance.setAnnualStatutoryEntitleDays(rs.getDouble("annual_statutory_entitle_days"));
                leaveBalance.setAnnualCompanyEntitleDays(rs.getDouble("annual_company_entitle_days"));
                leaveBalance.setAnnualForfeitDays(rs.getDouble("annual_forfeit_days"));
                
                leaveBalance.setSickTotalEntitleDays(rs.getDouble("sick_total_entitle_days"));
                leaveBalance.setSickAppliedDays(rs.getDouble("sick_applied_days"));
                balanceList.add(leaveBalance);
            }
            return balanceList;
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

    public LeaveBalanceVO getBalance(LeaveBalanceVO vo) throws DAOException {
        String SQL = "select * from teflow_leave_balance where (staff_code = ?) and (year = ?)";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(SQL);
            int i = 1;
            stm.setString(i++, vo.getStaffCode());
            stm.setInt(i++, vo.getYear());
            ResultSet rs = stm.executeQuery();
            if (!rs.next()) {
                return null;
            }
            
            LeaveBalanceVO leaveBalance = new LeaveBalanceVO();
            leaveBalance.setStaffCode(rs.getString("staff_code"));
            leaveBalance.setYear(rs.getInt("year"));
            leaveBalance.setAnnualTotalEntitleDays(rs.getDouble("annual_total_entitle_days"));
            leaveBalance.setAnnualCarryForwardDays(rs.getDouble("annual_carry_forward_days"));
            leaveBalance.setAnnualAppliedDays(rs.getDouble("annual_applied_days"));
            
            leaveBalance.setAnnualStatutoryEntitleDays(rs.getDouble("annual_statutory_entitle_days"));
            leaveBalance.setAnnualCompanyEntitleDays(rs.getDouble("annual_company_entitle_days"));
            leaveBalance.setAnnualForfeitDays(rs.getDouble("annual_forfeit_days"));
            
            leaveBalance.setSickTotalEntitleDays(rs.getDouble("sick_total_entitle_days"));
            leaveBalance.setSickAppliedDays(rs.getDouble("sick_applied_days"));
            return leaveBalance;
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

    public void saveBalance(LeaveBalanceVO leaveBalance) throws DAOException {
        this.deleteBalance(leaveBalance);
        this.addBalance(leaveBalance);
    }

    public void addBalance(LeaveBalanceVO leaveBalance) throws DAOException {
        String SQL = "insert into teflow_leave_balance(staff_code, year, annual_statutory_entitle_days, annual_company_entitle_days, annual_total_entitle_days, " +
        		"annual_carry_forward_days, annual_forfeit_days, annual_applied_days, annual_balance_days, " +
        		"sick_total_entitle_days, sick_applied_days, sick_balance_days ) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(SQL);
            int i = 1;
            stm.setString(i++, leaveBalance.getStaffCode());
            stm.setInt(i++, leaveBalance.getYear());
            stm.setDouble(i++, leaveBalance.getAnnualStatutoryEntitleDays());
            stm.setDouble(i++, leaveBalance.getAnnualCompanyEntitleDays());
            stm.setDouble(i++, leaveBalance.getAnnualTotalEntitleDays());
            stm.setDouble(i++, leaveBalance.getAnnualCarryForwardDays());
            stm.setDouble(i++, leaveBalance.getAnnualForfeitDays());
            stm.setDouble(i++, leaveBalance.getAnnualAppliedDays());
            stm.setDouble(i++, leaveBalance.getAnnualBalanceDays());
            
            stm.setDouble(i++, leaveBalance.getSickTotalEntitleDays());
            stm.setDouble(i++, leaveBalance.getSickAppliedDays());
            stm.setDouble(i++, leaveBalance.getSickBalanceDays());
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

    public void deleteBalance(LeaveBalanceVO leaveBalance) throws DAOException {
        String SQL = "delete from teflow_leave_balance where (staff_code = ?) and (year = ?)";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(SQL);
            int i = 1;
            stm.setString(i++, leaveBalance.getStaffCode());
            stm.setInt(i++, leaveBalance.getYear());
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
