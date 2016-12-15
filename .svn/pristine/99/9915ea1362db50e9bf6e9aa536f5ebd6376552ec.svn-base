package com.aiait.eflow.housekeeping.dao;

import java.sql.*;
import java.util.*;

import com.aiait.eflow.housekeeping.vo.BuildingVO;
import com.aiait.framework.db.*;
import com.aiait.framework.exception.DAOException;
import com.aiait.framework.vo.BaseVO;

public class BuildingDAO extends BaseDAOImpl {

    public BuildingDAO(IDBManager dbManager) {
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

    public Collection search(BuildingVO vo) throws DAOException {
        String SQL = "select * from teflow_building where 1=1 and org_id = ? order by code";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;
        Collection rateList = new Vector();

        try {
            stm = conn.prepareStatement(SQL);
            stm.setString(1, vo.getOrg_id());
            
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
            	BuildingVO resultVO = new BuildingVO();
                
                resultVO.setCode(rs.getString("code"));
                resultVO.setName(rs.getString("name"));
                resultVO.setRenter(rs.getString("renter"));
                resultVO.setPeriod(rs.getString("period"));
                resultVO.setMonth_rent_fee(rs.getDouble("month_rent_fee"));
                resultVO.setMonth_mang_fee(rs.getDouble("month_mang_fee"));
                rateList.add(resultVO);
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

    public BuildingVO getBuilding(BuildingVO vo) throws DAOException {
        String SQL = "select * from teflow_building where 1=1 and code = ? and org_id = ?";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(SQL);
            int i = 1;
            stm.setString(i++, vo.getCode());
            stm.setString(i++, vo.getOrg_id());
            ResultSet rs = stm.executeQuery();
            if (!rs.next()) {
                return null;
            }
            BuildingVO resultVO = new BuildingVO();
            resultVO.setCode(rs.getString("code"));
            resultVO.setName(rs.getString("name"));
            resultVO.setProvince(rs.getString("province"));
            resultVO.setCity(rs.getString("city"));
            resultVO.setRenter(rs.getString("renter"));
            resultVO.setPeriod(rs.getString("period"));
            resultVO.setDuration(rs.getString("duration")); 
            resultVO.setAcc_name(rs.getString("acc_name")); 
            resultVO.setAcc_bank(rs.getString("acc_bank")); 
            resultVO.setAcc_no(rs.getString("acc_no")); 
            resultVO.setArea(rs.getDouble("area")); 
            resultVO.setFree_month(rs.getInt("free_month")); 
            resultVO.setFree_period(rs.getString("free_period")); 
            resultVO.setMonth_rent_fee(rs.getDouble("month_rent_fee")); 
            resultVO.setMonth_rent_curr(rs.getString("month_rent_curr")); 
            resultVO.setMonth_mang_fee(rs.getDouble("month_mang_fee")); 
            resultVO.setMonth_mang_curr(rs.getString("month_mang_curr")); 
            resultVO.setDepo_month(rs.getInt("depo_month")); 
            resultVO.setDepo_fee_rent(rs.getDouble("depo_fee_rent")); 
            resultVO.setDepo_fee_prop(rs.getDouble("depo_fee_prop")); 
            resultVO.setTot_amount(rs.getDouble("tot_amount")); 
            resultVO.setOrg_id(rs.getString("org_id"));
            resultVO.setContract_no(rs.getString("contract_no"));
            return resultVO;
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
    
    public void saveBuilding(BuildingVO vo) throws DAOException {
        this.deleteBuilding(vo);
        this.addBuilding(vo);
    }

    public void addBuilding(BuildingVO vo) throws DAOException {
        String SQL = "insert into teflow_building(code, name, province, city, renter, period, duration, " +
        		"acc_name, acc_bank, acc_no, area, free_month, free_period, month_rent_fee, month_rent_curr, " +
        		"month_mang_fee, month_mang_curr, depo_month, depo_fee_rent, depo_fee_prop, tot_amount, org_id, contract_no )" +
        		" values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(SQL);
            int i = 1;            
            stm.setString(i++,vo.getCode());
            stm.setString(i++,vo.getName());
            stm.setString(i++,vo.getProvince());
            stm.setString(i++,vo.getCity());
            stm.setString(i++,vo.getRenter());
            stm.setString(i++,vo.getPeriod());
            stm.setString(i++,vo.getDuration()); 
            stm.setString(i++,vo.getAcc_name()); 
            stm.setString(i++,vo.getAcc_bank()); 
            stm.setString(i++,vo.getAcc_no()); 
            stm.setDouble(i++,vo.getArea()); 
            stm.setDouble(i++,vo.getFree_month()); 
            stm.setString(i++,vo.getFree_period()); 
            stm.setDouble(i++,vo.getMonth_rent_fee()); 
            stm.setString(i++,vo.getMonth_rent_curr()); 
            stm.setDouble(i++,vo.getMonth_mang_fee()); 
            stm.setString(i++,vo.getMonth_mang_curr()); 
            stm.setDouble(i++,vo.getDepo_month()); 
            stm.setDouble(i++,vo.getDepo_fee_rent()); 
            stm.setDouble(i++,vo.getDepo_fee_prop()); 
            stm.setDouble(i++,vo.getTot_amount());
            stm.setString(i++,vo.getOrg_id());
            stm.setString(i++,vo.getContract_no());
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

    public void deleteBuilding(BuildingVO vo) throws DAOException {
        String SQL = "delete from teflow_building where 1=1 and code = ? and org_id = ?";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(SQL);
            int i = 1;
            stm.setString(i++, vo.getCode());
            stm.setString(i++, vo.getOrg_id());
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
