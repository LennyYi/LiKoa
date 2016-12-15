package com.aiait.eflow.housekeeping.dao;

import java.sql.*;
import java.util.*;

import com.aiait.eflow.housekeeping.vo.CurrencyExVO;
import com.aiait.framework.db.*;
import com.aiait.framework.exception.DAOException;
import com.aiait.framework.vo.BaseVO;

public class CurrencyExDAO extends BaseDAOImpl {

    public CurrencyExDAO(IDBManager dbManager) {
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

    public Collection getRateList() throws DAOException {
        String SQL = "select * from teflow_exchange_rate order by currency_code, ex_year, ex_month ";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;
        Collection rateList = new Vector();

        try {
            stm = conn.prepareStatement(SQL);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                CurrencyExVO currency = new CurrencyExVO();
                currency.setCurrencyCode(rs.getString("currency_code"));
                currency.setCurrencyName(rs.getString("currency_name"));
                currency.setExchangeRate(rs.getDouble("ex_rate"));
                currency.setYear(rs.getInt("ex_year"));
                currency.setMonth(rs.getInt("ex_month"));
                rateList.add(currency);
            }
            return rateList;
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

    public CurrencyExVO getRate(CurrencyExVO currency) throws DAOException {
        String SQL = "select * from teflow_exchange_rate where currency_code = ? and ex_year = ? and ex_month = ?";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(SQL);
            stm.setString(1, currency.getCurrencyCode());
            stm.setInt(2, currency.getYear());
            stm.setInt(3, currency.getMonth());
            ResultSet rs = stm.executeQuery();
            if (!rs.next()) {
                return null;
            }
            currency.setCurrencyCode(rs.getString("currency_code"));
            currency.setCurrencyName(rs.getString("currency_name"));
            currency.setExchangeRate(rs.getDouble("ex_rate"));
            currency.setMonth(rs.getInt("ex_year"));
            currency.setMonth(rs.getInt("ex_month"));
            return currency;
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

    public void saveRate(CurrencyExVO currency) throws DAOException {
        this.deleteRate(currency);
        this.addRate(currency);
    }

    public void addRate(CurrencyExVO currency) throws DAOException {
        String SQL = "insert into teflow_exchange_rate(currency_code, currency_name, ex_rate, ex_year, ex_month) " +
        		"values(?, ?, ?, ?, ?)";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(SQL);
            int i = 1;
            stm.setString(i++, currency.getCurrencyCode());
            stm.setString(i++, currency.getCurrencyName());
            stm.setDouble(i++, currency.getExchangeRate());
            stm.setInt(i++, currency.getYear());
            stm.setInt(i++, currency.getMonth());
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

    public void deleteRate(CurrencyExVO currency) throws DAOException {
        String SQL = "delete from teflow_exchange_rate where currency_code = ? and ex_year = ? and ex_month = ?";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(SQL);
            stm.setString(1, currency.getCurrencyCode());
            stm.setInt(2, currency.getYear());
            stm.setInt(3, currency.getMonth());
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
