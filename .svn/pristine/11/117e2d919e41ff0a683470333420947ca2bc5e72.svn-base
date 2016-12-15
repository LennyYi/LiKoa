package com.aiait.eflow.housekeeping.dao;

import java.sql.*;
import java.util.*;

import com.aiait.eflow.housekeeping.vo.HotelRateVO2;
import com.aiait.framework.db.*;
import com.aiait.framework.exception.DAOException;
import com.aiait.framework.vo.BaseVO;

public class HotelRateDAO2 extends BaseDAOImpl {

    public HotelRateDAO2(IDBManager dbManager) {
        super(dbManager);
    }

    public List<HotelRateVO2> getRateList() throws DAOException {
        String SQL = "select a.*, b.name cityname, c.name currname "
                + "from teflow_hotel_rate a, teflow_travel_city b, teflow_currency c "
                + "where (a.city = b.code) and (a.currency = c.code) "
                + "order by a.effdate desc, b.region_code, b.tier, b.code, a.grade desc";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;
        List<HotelRateVO2> list = new ArrayList<HotelRateVO2>();

        try {
            stm = conn.prepareStatement(SQL);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                HotelRateVO2 rate = new HotelRateVO2();
                rate.setEffDate(rs.getDate("effdate"));
                rate.setCityCode(rs.getString("city"));
                rate.setCityName(rs.getString("cityname"));
                rate.setGrade(rs.getInt("grade"));
                rate.setRate(rs.getBigDecimal("rate"));
                rate.setCurrCode(rs.getString("currency"));
                rate.setCurrName(rs.getString("currname"));
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

    public HotelRateVO2 getRate(HotelRateVO2 rate) throws DAOException {
        String SQL = "select a.*, b.name cityname, c.name currname "
                + "from teflow_hotel_rate a, teflow_travel_city b, teflow_currency c "
                + "where (a.city = b.code) and (a.currency = c.code) "
                + "and (a.effdate = ?) and (a.city = ?) and (a.grade = ?)";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(SQL);
            int i = 1;
            stm.setDate(i++, rate.getEffDate());
            stm.setString(i++, rate.getCityCode());
            stm.setInt(i++, rate.getGrade());
            ResultSet rs = stm.executeQuery();
            if (!rs.next()) {
                return null;
            }
            rate.setEffDate(rs.getDate("effdate"));
            rate.setCityCode(rs.getString("city"));
            rate.setCityName(rs.getString("cityname"));
            rate.setGrade(rs.getInt("grade"));
            rate.setRate(rs.getBigDecimal("rate"));
            rate.setCurrCode(rs.getString("currency"));
            rate.setCurrName(rs.getString("currname"));
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

    public void saveRate(HotelRateVO2 rate) throws DAOException {
        this.deleteRate(rate);
        this.addRate(rate);
    }

    public void addRate(HotelRateVO2 rate) throws DAOException {
        String SQL = "insert into teflow_hotel_rate (effdate, city, grade, rate, currency) values (?, ?, ?, ?, ?)";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(SQL);
            int i = 1;
            stm.setDate(i++, rate.getEffDate());
            stm.setString(i++, rate.getCityCode());
            stm.setInt(i++, rate.getGrade());
            stm.setBigDecimal(i++, rate.getRate());
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

    public void deleteRate(HotelRateVO2 rate) throws DAOException {
        String SQL = "delete from teflow_hotel_rate where (effdate = ?) and (city = ?) and (grade = ?)";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(SQL);
            int i = 1;
            stm.setDate(i++, rate.getEffDate());
            stm.setString(i++, rate.getCityCode());
            stm.setInt(i++, rate.getGrade());
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
