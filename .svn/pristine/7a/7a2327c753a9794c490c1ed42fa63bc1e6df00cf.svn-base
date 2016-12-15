package com.aiait.eflow.housekeeping.dao;

import java.sql.*;
import java.util.*;

import com.aiait.eflow.housekeeping.vo.MedicalBalanceVO;
import com.aiait.framework.db.*;
import com.aiait.framework.exception.DAOException;
import com.aiait.framework.vo.BaseVO;

public class MedicalBalanceDAO extends BaseDAOImpl {

    public MedicalBalanceDAO(IDBManager dbManager) {
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
        String SQL = "select * from teflow_medical_balance where (staff_code = ?) order by year desc";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;
        Collection balanceList = new Vector();

        try {
            stm = conn.prepareStatement(SQL);
            int i = 1;
            stm.setString(i++, staffCode);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                MedicalBalanceVO medicalBalance = new MedicalBalanceVO();
                medicalBalance.setStaffCode(rs.getString("staff_code"));
                medicalBalance.setYear(rs.getInt("year"));
                medicalBalance.setStaffCEntitlement(rs.getDouble("staff_C_entitlement"));
                medicalBalance.setStaffCApplied(rs.getDouble("staff_C_applied"));
                medicalBalance.setStaffCBalance(rs.getDouble("staff_C_balance"));
                medicalBalance.setStaffHEntitlement(rs.getDouble("staff_H_entitlement"));
                medicalBalance.setStaffHApplied(rs.getDouble("staff_H_applied"));             
                medicalBalance.setStaffHBalance(rs.getDouble("staff_H_balance"));
                
                medicalBalance.setConnubialName(rs.getString("connubial_name"));
                medicalBalance.setConnubialCEntitlement(rs.getDouble("connubial_C_entitlement"));
                medicalBalance.setConnubialCApplied(rs.getDouble("connubial_C_applied"));
                medicalBalance.setConnubialCBalance(rs.getDouble("connubial_C_balance"));                
                medicalBalance.setConnubialHEntitlement(rs.getDouble("connubial_H_entitlement"));
                medicalBalance.setConnubialHApplied(rs.getDouble("connubial_H_applied"));
                medicalBalance.setConnubialHBalance(rs.getDouble("connubial_H_balance"));
                
                medicalBalance.setChildName(rs.getString("child_name"));
                medicalBalance.setChildCEntitlement(rs.getDouble("child_C_entitlement"));
                medicalBalance.setChildCApplied(rs.getDouble("child_C_applied"));
                medicalBalance.setChildCBalance(rs.getDouble("child_C_balance"));
                medicalBalance.setChildHEntitlement(rs.getDouble("child_H_entitlement"));
                medicalBalance.setChildHApplied(rs.getDouble("child_H_applied"));
                medicalBalance.setChildHBalance(rs.getDouble("child_H_balance"));
                                                                                                                                                                                                                                                                                                                                                
                balanceList.add(medicalBalance);
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

    public MedicalBalanceVO getBalance(MedicalBalanceVO vo) throws DAOException {
        String SQL = "select * from teflow_medical_balance where (staff_code = ?) and (year = ?)";

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
            
            MedicalBalanceVO medicalBalance = new MedicalBalanceVO();
            medicalBalance.setStaffCode(rs.getString("staff_code"));
            medicalBalance.setYear(rs.getInt("year"));
            medicalBalance.setStaffCEntitlement(rs.getDouble("staff_C_entitlement"));
            medicalBalance.setStaffCApplied(rs.getDouble("staff_C_applied"));
            medicalBalance.setStaffCBalance(rs.getDouble("staff_C_balance"));
            medicalBalance.setStaffHEntitlement(rs.getDouble("staff_H_entitlement"));
            medicalBalance.setStaffHApplied(rs.getDouble("staff_H_applied"));             
            medicalBalance.setStaffHBalance(rs.getDouble("staff_H_balance"));
            
            medicalBalance.setConnubialName(rs.getString("connubial_name"));
            medicalBalance.setConnubialCEntitlement(rs.getDouble("connubial_C_entitlement"));
            medicalBalance.setConnubialCApplied(rs.getDouble("connubial_C_applied"));
            medicalBalance.setConnubialCBalance(rs.getDouble("connubial_C_balance"));                
            medicalBalance.setConnubialHEntitlement(rs.getDouble("connubial_H_entitlement"));
            medicalBalance.setConnubialHApplied(rs.getDouble("connubial_H_applied"));
            medicalBalance.setConnubialHBalance(rs.getDouble("connubial_H_balance"));
            
            medicalBalance.setChildName(rs.getString("child_name"));
            medicalBalance.setChildCEntitlement(rs.getDouble("child_C_entitlement"));
            medicalBalance.setChildCApplied(rs.getDouble("child_C_applied"));
            medicalBalance.setChildCBalance(rs.getDouble("child_C_balance"));
            medicalBalance.setChildHEntitlement(rs.getDouble("child_H_entitlement"));
            medicalBalance.setChildHApplied(rs.getDouble("child_H_applied"));
            medicalBalance.setChildHBalance(rs.getDouble("child_H_balance"));
            return medicalBalance;
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

    public void saveBalance(MedicalBalanceVO medicalBalance) throws DAOException {
        this.deleteBalance(medicalBalance);
        this.addBalance(medicalBalance);
    }

    public void addBalance(MedicalBalanceVO medicalBalance) throws DAOException {
        String SQL = "insert into teflow_medical_balance (year,staff_code,staff_c_entitlement,staff_c_applied,staff_c_balance,staff_h_entitlement,staff_h_applied,staff_h_balance," +
        			 "connubial_name,connubial_c_entitlement,connubial_c_applied,connubial_c_balance,connubial_h_entitlement,connubial_h_applied,connubial_h_balance," +
        			 "child_name,child_c_entitlement,child_c_applied,child_c_balance,child_h_entitlement,child_h_applied,child_h_balance)" +
        			 "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(SQL);
            int i = 1;
            stm.setInt(i++, medicalBalance.getYear());
            stm.setString(i++, medicalBalance.getStaffCode());  
            stm.setDouble(i++, medicalBalance.getStaffCEntitlement());
            stm.setDouble(i++, medicalBalance.getStaffCApplied());
            stm.setDouble(i++, medicalBalance.getStaffCBalance());
            stm.setDouble(i++, medicalBalance.getStaffHEntitlement());
            stm.setDouble(i++, medicalBalance.getStaffHApplied());
            stm.setDouble(i++, medicalBalance.getStaffHBalance());
            
            stm.setString(i++, medicalBalance.getConnubialName());
            stm.setDouble(i++, medicalBalance.getConnubialCEntitlement());         
            stm.setDouble(i++, medicalBalance.getConnubialCApplied());
            stm.setDouble(i++, medicalBalance.getConnubialCBalance());
            stm.setDouble(i++, medicalBalance.getConnubialHEntitlement());
            stm.setDouble(i++, medicalBalance.getConnubialHApplied());
            stm.setDouble(i++, medicalBalance.getConnubialHBalance());
            
            stm.setString(i++, medicalBalance.getChildName());
            stm.setDouble(i++, medicalBalance.getChildCEntitlement());
            stm.setDouble(i++, medicalBalance.getChildCApplied());
            stm.setDouble(i++, medicalBalance.getChildCBalance());
            stm.setDouble(i++, medicalBalance.getChildHEntitlement());
            stm.setDouble(i++, medicalBalance.getChildHApplied());         
            stm.setDouble(i++, medicalBalance.getChildHBalance());
            
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

    public void deleteBalance(MedicalBalanceVO medicalBalance) throws DAOException {
        String SQL = "delete from teflow_medical_balance where (staff_code = ?) and (year = ?)";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(SQL);
            int i = 1;
            stm.setString(i++, medicalBalance.getStaffCode());
            stm.setInt(i++, medicalBalance.getYear());
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
