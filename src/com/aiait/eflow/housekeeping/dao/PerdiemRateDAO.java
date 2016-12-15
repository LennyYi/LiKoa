package com.aiait.eflow.housekeeping.dao;

import java.sql.*;
import java.util.*;

import com.aiait.eflow.housekeeping.vo.PerdiemRateVO;
import com.aiait.framework.db.*;
import com.aiait.framework.exception.DAOException;
import com.aiait.framework.vo.BaseVO;

public class PerdiemRateDAO extends BaseDAOImpl {

    public PerdiemRateDAO(IDBManager dbManager) {
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
        String SQL = "select * from teflow_perdiem_rate order by location, title_group";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;
        Collection rateList = new Vector();

        try {
            stm = conn.prepareStatement(SQL);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                PerdiemRateVO perdiemRate = new PerdiemRateVO();
                perdiemRate.setRegion(rs.getString("location"));
                perdiemRate.setTitleGroup(rs.getString("title_group"));
                perdiemRate.setRate(rs.getInt("perdiem_rate"));
                rateList.add(perdiemRate);
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

    public PerdiemRateVO getRate(PerdiemRateVO perdiemRate) throws DAOException {
        String SQL = "select * from teflow_perdiem_rate where (location = ?) and (title_group = ?)";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(SQL);
            int i = 1;
            stm.setString(i++, perdiemRate.getRegion());
            stm.setString(i++, perdiemRate.getTitleGroup());
            ResultSet rs = stm.executeQuery();
            if (!rs.next()) {
                return null;
            }
            perdiemRate.setRegion(rs.getString("location"));
            perdiemRate.setTitleGroup(rs.getString("title_group"));
            perdiemRate.setRate(rs.getInt("perdiem_rate"));
            return perdiemRate;
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

    public void saveRate(PerdiemRateVO perdiemRate) throws DAOException {
        this.deleteRate(perdiemRate);
        this.addRate(perdiemRate);
    }

    public void addRate(PerdiemRateVO perdiemRate) throws DAOException {
        String SQL = "insert into teflow_perdiem_rate(location, title_group, perdiem_rate) values(?, ?, ?)";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(SQL);
            int i = 1;
            stm.setString(i++, perdiemRate.getRegion());
            stm.setString(i++, perdiemRate.getTitleGroup());
            stm.setInt(i++, perdiemRate.getRate());
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

    public void deleteRate(PerdiemRateVO perdiemRate) throws DAOException {
        String SQL = "delete from teflow_perdiem_rate where (location = ?) and (title_group = ?)";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(SQL);
            int i = 1;
            stm.setString(i++, perdiemRate.getRegion());
            stm.setString(i++, perdiemRate.getTitleGroup());
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
