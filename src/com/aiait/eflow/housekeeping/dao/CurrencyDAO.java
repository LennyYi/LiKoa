package com.aiait.eflow.housekeeping.dao;

import java.sql.*;
import java.util.*;

import com.aiait.eflow.housekeeping.vo.CurrencyVO;
import com.aiait.framework.db.*;
import com.aiait.framework.exception.DAOException;
import com.aiait.framework.vo.BaseVO;

public class CurrencyDAO extends BaseDAOImpl {

    public CurrencyDAO(IDBManager dbManager) {
        super(dbManager);
    }

    public List<CurrencyVO> getCurrencyList() throws DAOException {
        String SQL = "select * from teflow_currency order by ord";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;
        List<CurrencyVO> list = new ArrayList<CurrencyVO>();

        try {
            stm = conn.prepareStatement(SQL);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                CurrencyVO currency = new CurrencyVO();
                currency.setCode(rs.getString("code"));
                currency.setName(rs.getString("name"));
                currency.setRequired("Y".equalsIgnoreCase(rs.getString("required")));
                currency.setSymbol(rs.getString("symbol"));
                currency.setOrd(rs.getString("ord"));
                list.add(currency);
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

    public List<CurrencyVO> getCurrencyList2() throws DAOException {
        List<CurrencyVO> list = this.getCurrencyList();
        for (CurrencyVO cur : list) {
            if (cur.getCode().equalsIgnoreCase("RMB")) {
                list.remove(cur);
                break;
            }
        }
        return list;
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
