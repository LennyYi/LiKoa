package com.aiait.eflow._thailand.dao;

import java.sql.*;
import java.util.*;

import com.aiait.eflow._thailand.vo.ExpenseVO;
import com.aiait.framework.db.*;
import com.aiait.framework.exception.DAOException;
import com.aiait.framework.vo.BaseVO;

public class ExpenseDAO extends BaseDAOImpl {

    public ExpenseDAO(IDBManager dbManager) {
        super(dbManager);
    }

    public List<ExpenseVO> getExpenseList() throws DAOException {
        String SQL = "select * from teflow_tha_expense order by exp_type, ac_code, ac_desc";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;
        List<ExpenseVO> list = new ArrayList<ExpenseVO>();

        try {
            stm = conn.prepareStatement(SQL);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                ExpenseVO expense = new ExpenseVO();
                expense.setAcCode(rs.getString("ac_code"));
                expense.setAcSubCode(rs.getString("ac_subcode"));
                expense.setAcDesc(rs.getString("ac_desc"));
                expense.setType(rs.getString("exp_type"));
                expense.setRelateHR(rs.getString("relate_hr"));
                expense.setRelateRE(rs.getString("relate_re"));
                expense.setRelateIT(rs.getString("relate_it"));
                expense.setSpExpense(rs.getString("sp_expense"));
                expense.setFinance(rs.getString("finance"));
                expense.setFsi(rs.getString("fsi"));
                expense.setCapex(rs.getString("capex"));
                list.add(expense);
            }
            return list;
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

    public ExpenseVO getExpense(String code, String subcode) throws DAOException {
        String SQL = "select * from teflow_tha_expense where ac_code = ? and ac_subcode = ?";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(SQL);
            int i = 1;
            stm.setString(i++, code);
            stm.setString(i++, subcode);
            ResultSet rs = stm.executeQuery();
            if (!rs.next()) {
                return null;
            }
            ExpenseVO expense = new ExpenseVO();
            expense.setAcCode(rs.getString("ac_code"));
            expense.setAcSubCode(rs.getString("ac_subcode"));
            expense.setAcDesc(rs.getString("ac_desc"));
            expense.setType(rs.getString("exp_type"));
            expense.setRelateHR(rs.getString("relate_hr"));
            expense.setRelateRE(rs.getString("relate_re"));
            expense.setRelateIT(rs.getString("relate_it"));
            expense.setSpExpense(rs.getString("sp_expense"));
            expense.setFinance(rs.getString("finance"));
            expense.setFsi(rs.getString("fsi"));
            expense.setCapex(rs.getString("capex"));
            return expense;
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

    public void saveExpense(ExpenseVO expense) throws DAOException {
        this.deleteExpense(expense.getAcCode(), expense.getAcSubCode());
        this.addExpense(expense);
    }

    public void addExpense(ExpenseVO expense) throws DAOException {
        String SQL = "insert into teflow_tha_expense "
                + "(ac_code, ac_subcode, ac_desc, exp_type, relate_hr, relate_re, relate_it, sp_expense, finance, fsi, capex) "
                + "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(SQL);
            int i = 1;
            stm.setString(i++, expense.getAcCode());
            stm.setString(i++, expense.getAcSubCode());
            stm.setString(i++, expense.getAcDesc());
            stm.setString(i++, expense.getType());
            stm.setString(i++, expense.getRelateHR());
            stm.setString(i++, expense.getRelateRE());
            stm.setString(i++, expense.getRelateIT());
            stm.setString(i++, expense.getSpExpense());
            stm.setString(i++, expense.getFinance());
            stm.setString(i++, expense.getFsi());
            stm.setString(i++, expense.getCapex());
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

    public void deleteExpense(String code, String subcode) throws DAOException {
        String SQL = "delete from teflow_tha_expense where ac_code = ? and ac_subcode = ?";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(SQL);
            int i = 1;
            stm.setString(i++, code);
            stm.setString(i++, subcode);
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
