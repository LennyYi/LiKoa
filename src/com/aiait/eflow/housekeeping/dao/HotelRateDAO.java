package com.aiait.eflow.housekeeping.dao;

import java.sql.*;
import java.util.*;

import com.aiait.eflow.housekeeping.vo.HotelRateVO;
import com.aiait.framework.db.*;
import com.aiait.framework.exception.DAOException;
import com.aiait.framework.vo.BaseVO;

public class HotelRateDAO extends BaseDAOImpl {

    public HotelRateDAO(IDBManager dbManager) {
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
        String SQL = "select * from teflow_hotel_rate order by city, title_group";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;
        Collection rateList = new Vector();

        try {
            stm = conn.prepareStatement(SQL);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                HotelRateVO hotelRate = new HotelRateVO();
                hotelRate.setCity(rs.getString("city"));
                hotelRate.setTitleGroup(rs.getString("title_group"));
                hotelRate.setRate(rs.getInt("hotel_rate"));
                rateList.add(hotelRate);
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

    public HotelRateVO getRate(HotelRateVO hotelRate) throws DAOException {
        String SQL = "select * from teflow_hotel_rate where (city = ?) and (title_group = ?)";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(SQL);
            int i = 1;
            stm.setString(i++, hotelRate.getCity());
            stm.setString(i++, hotelRate.getTitleGroup());
            ResultSet rs = stm.executeQuery();
            if (!rs.next()) {
                return null;
            }
            hotelRate.setCity(rs.getString("city"));
            hotelRate.setTitleGroup(rs.getString("title_group"));
            hotelRate.setRate(rs.getInt("hotel_rate"));
            return hotelRate;
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

    public void saveRate(HotelRateVO hotelRate) throws DAOException {
        this.deleteRate(hotelRate);
        this.addRate(hotelRate);
    }

    public void addRate(HotelRateVO hotelRate) throws DAOException {
        String SQL = "insert into teflow_hotel_rate(city, title_group, hotel_rate) values(?, ?, ?)";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(SQL);
            int i = 1;
            stm.setString(i++, hotelRate.getCity());
            stm.setString(i++, hotelRate.getTitleGroup());
            stm.setInt(i++, hotelRate.getRate());
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

    public void deleteRate(HotelRateVO hotelRate) throws DAOException {
        String SQL = "delete from teflow_hotel_rate where (city = ?) and (title_group = ?)";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(SQL);
            int i = 1;
            stm.setString(i++, hotelRate.getCity());
            stm.setString(i++, hotelRate.getTitleGroup());
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
