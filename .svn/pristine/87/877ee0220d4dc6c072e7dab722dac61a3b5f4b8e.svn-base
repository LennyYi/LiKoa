package com.aiait.eflow.service.dao;

import java.sql.*;

import com.aiait.eflow.service.vo.ServiceUserVo;
import com.aiait.framework.db.*;
import com.aiait.framework.exception.DAOException;
import com.aiait.framework.vo.BaseVO;

/**
 * Security Service DAO
 * 
 * @version 2011-01-13
 */
public class SecurityServiceDAO extends BaseDAOImpl {

    public SecurityServiceDAO(IDBManager dbManager) {
        super(dbManager);
    }

    public ServiceUserVo getServiceUser(String serviceId) throws DAOException {
        String SQL = "select * from teflow_service_user where service_id = ?";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(SQL);
            int i = 1;
            stm.setString(i++, serviceId);
            ResultSet rs = stm.executeQuery();
            if (!rs.next()) {
                return null;
            }
            ServiceUserVo serviceUser = new ServiceUserVo();
            serviceUser.setServiceId(rs.getString("service_id"));
            serviceUser.setServiceName(rs.getString("service_name"));
            serviceUser.setPassword(rs.getString("password"));
            serviceUser.setNewPassword(rs.getString("new_password"));
            return serviceUser;
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

    public void updatePassword(ServiceUserVo serviceUser) throws DAOException {
        String SQL = "update teflow_service_user set password = ?, new_password = ? where service_id = ?";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(SQL);
            int i = 1;
            stm.setString(i++, serviceUser.getPassword());
            stm.setString(i++, serviceUser.getNewPassword());
            stm.setString(i++, serviceUser.getServiceId());
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

    public int delete(BaseVO dataItem) throws DAOException {
        return 0;
    }

    public int save(BaseVO dataItem) throws DAOException {
        return 0;
    }

    public int update(BaseVO dataItem) throws DAOException {
        return 0;
    }

}
