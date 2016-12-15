package com.aiait.eflow.service.dao;

import java.sql.*;

import com.aiait.eflow.service.vo.FormServiceVo;
import com.aiait.framework.db.*;
import com.aiait.framework.exception.DAOException;
import com.aiait.framework.vo.BaseVO;

/**
 * Form Service DAO
 * 
 * @version 2010-12-01
 */
public class FormServiceDAO extends BaseDAOImpl {

    public FormServiceDAO(IDBManager dbManager) {
        super(dbManager);
    }

    public void setFormService(FormServiceVo serviceVo) throws DAOException {
        String SQL_INS = "insert into teflow_wkf_process_service(request_no, service_id) values(?, ?)";
        String SQL_DEL = "delete from teflow_wkf_process_service where request_no = ?";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(SQL_DEL);
            int i = 1;
            stm.setString(i++, serviceVo.getRequestNo());
            stm.executeUpdate();

            if (serviceVo.getServiceId() == null) {
                return;
            }
            stm.close();
            stm = conn.prepareStatement(SQL_INS);
            i = 1;
            stm.setString(i++, serviceVo.getRequestNo());
            stm.setString(i++, serviceVo.getServiceId());
            stm.executeUpdate();

            this.setCallback(serviceVo);
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

    public void setCallback(FormServiceVo serviceVo) throws DAOException {
        String SQL_INS = "insert into teflow_wkf_process_callback(request_no, service_id, callback) values(?, ?, ?)";
        String SQL_DEL = "delete from teflow_wkf_process_callback where request_no = ? and service_id = ?";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(SQL_DEL);
            int i = 1;
            stm.setString(i++, serviceVo.getRequestNo());
            stm.setString(i++, serviceVo.getServiceId());
            stm.executeUpdate();

            if (serviceVo.getCallback() == null || serviceVo.getCallback().trim().equals("")) {
                return;
            }
            stm.close();
            stm = conn.prepareStatement(SQL_INS);
            i = 1;
            stm.setString(i++, serviceVo.getRequestNo());
            stm.setString(i++, serviceVo.getServiceId());
            stm.setString(i++, serviceVo.getCallback().trim());
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
