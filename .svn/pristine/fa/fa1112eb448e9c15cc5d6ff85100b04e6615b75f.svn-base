package com.aiait.eflow.housekeeping.dao;

import java.sql.*;
import java.util.*;

import com.aiait.eflow.housekeeping.vo.CityVO;
import com.aiait.framework.db.*;
import com.aiait.framework.exception.DAOException;
import com.aiait.framework.vo.BaseVO;

public class CityDAO extends BaseDAOImpl {

    public CityDAO(IDBManager dbManager) {
        super(dbManager);
    }

    public List<CityVO> getCityList() throws DAOException {
        String SQL = "select * from teflow_travel_city order by region_code, tier, code";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;
        List<CityVO> list = new ArrayList<CityVO>();

        try {
            stm = conn.prepareStatement(SQL);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                CityVO city = new CityVO();
                city.setCode(rs.getString("code"));
                city.setName(rs.getString("name"));
                city.setRegionCode(rs.getString("region_code"));
                city.setRegionName(rs.getString("region_name"));
                city.setTier(rs.getInt("tier"));
                list.add(city);
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
