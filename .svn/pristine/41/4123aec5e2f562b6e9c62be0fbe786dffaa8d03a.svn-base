package com.aiait.eflow.housekeeping.dao;

import java.sql.*;
import java.util.*;

import com.aiait.eflow.housekeeping.vo.ExchangeRateVO2;
import com.aiait.framework.db.*;
import com.aiait.framework.exception.DAOException;
import com.aiait.framework.vo.BaseVO;

public class ExchangeRateDAO2 extends BaseDAOImpl {

    public ExchangeRateDAO2(IDBManager dbManager) {
        super(dbManager);
    }

    public List<ExchangeRateVO2> getRateList() throws DAOException {
        String SQL = "select a.*, b.name currname from teflow_exchange_rate a, teflow_currency b "
                + "where (a.currency = b.code) order by a.effyear desc, a.effmonth desc, b.ord";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;
        List<ExchangeRateVO2> list = new ArrayList<ExchangeRateVO2>();

        try {
            stm = conn.prepareStatement(SQL);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                ExchangeRateVO2 rate = new ExchangeRateVO2();
                rate.setEffyear(rs.getInt("effyear"));
                rate.setEffmonth(rs.getInt("effmonth"));
                rate.setCurrCode(rs.getString("currency"));
                rate.setCurrName(rs.getString("currname"));
                rate.setRate(rs.getBigDecimal("rate"));
                list.add(rate);
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

    public List<ExchangeRateVO2> getRatesByMonth(int year, int month) throws DAOException {
        String SQL = "select a.*, b.name currname from teflow_exchange_rate a, teflow_currency b "
                + "where (a.currency = b.code) and (a.effyear = ?) and (a.effmonth = ?) order by b.ord";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;
        List<ExchangeRateVO2> list = new ArrayList<ExchangeRateVO2>();

        try {
            stm = conn.prepareStatement(SQL);
            int i = 1;
            stm.setInt(i++, year);
            stm.setInt(i++, month);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                ExchangeRateVO2 rate = new ExchangeRateVO2();
                rate.setEffyear(rs.getInt("effyear"));
                rate.setEffmonth(rs.getInt("effmonth"));
                rate.setCurrCode(rs.getString("currency"));
                rate.setCurrName(rs.getString("currname"));
                rate.setRate(rs.getBigDecimal("rate"));
                list.add(rate);
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

    public ExchangeRateVO2 getRate(ExchangeRateVO2 rate) throws DAOException {
        String SQL = "select a.*, b.name currname from teflow_exchange_rate a, teflow_currency b "
                + "where (a.currency = b.code) and (a.effyear = ?) and (a.effmonth = ?) and (a.currency = ?)";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(SQL);
            int i = 1;
            stm.setInt(i++, rate.getEffyear());
            stm.setInt(i++, rate.getEffmonth());
            stm.setString(i++, rate.getCurrCode());
            ResultSet rs = stm.executeQuery();
            if (!rs.next()) {
                return null;
            }
            rate.setEffyear(rs.getInt("effyear"));
            rate.setEffmonth(rs.getInt("effmonth"));
            rate.setCurrCode(rs.getString("currency"));
            rate.setCurrName(rs.getString("currname"));
            rate.setRate(rs.getBigDecimal("rate"));
            return rate;
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

    public void saveRate(ExchangeRateVO2 rate) throws DAOException {
        this.deleteRate(rate);
        this.addRate(rate);
    }

    public void addRate(ExchangeRateVO2 rate) throws DAOException {
        String SQL = "insert into teflow_exchange_rate (effyear, effmonth, currency, rate) values (?, ?, ?, ?)";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(SQL);
            int i = 1;
            stm.setInt(i++, rate.getEffyear());
            stm.setInt(i++, rate.getEffmonth());
            stm.setString(i++, rate.getCurrCode());
            stm.setBigDecimal(i++, rate.getRate());
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

    public void deleteRate(ExchangeRateVO2 rate) throws DAOException {
        String SQL = "delete from teflow_exchange_rate where (effyear = ?) and (effmonth = ?) and (currency = ?)";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(SQL);
            int i = 1;
            stm.setInt(i++, rate.getEffyear());
            stm.setInt(i++, rate.getEffmonth());
            stm.setString(i++, rate.getCurrCode());
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

    public void deleteRatesByMonth(int year, int month) throws DAOException {
        String SQL = "delete from teflow_exchange_rate where (effyear = ?) and (effmonth = ?)";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(SQL);
            int i = 1;
            stm.setInt(i++, year);
            stm.setInt(i++, month);
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
