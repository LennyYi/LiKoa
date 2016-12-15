package com.aiait.eflow.housekeeping.dao;

import java.sql.*;
import java.util.*;

import com.aiait.eflow.housekeeping.vo.BankVO;
import com.aiait.framework.db.*;
import com.aiait.framework.exception.DAOException;
import com.aiait.framework.vo.BaseVO;

public class BankDAO extends BaseDAOImpl {

    public BankDAO(IDBManager dbManager) {
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

    public Collection search(String orgId) throws DAOException {
        String SQL = "select * from teflow_bank where 1=1 and org_id='" + orgId +"'";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;
        Collection bankList = new Vector();

        try {
            stm = conn.prepareStatement(SQL);
            
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
            	BankVO vo = new BankVO();
                
                vo.setBankCode(rs.getString("bank_code"));
                vo.setBankName(rs.getString("bank_name"));
                vo.setAccountCode(rs.getString("account_code"));
                vo.setAccountName(rs.getString("account_name"));
                vo.setOrgId(rs.getString("org_id"));
                vo.setIsDefault(rs.getInt("is_default"));
                bankList.add(vo);
            }
            return bankList;
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

    public BankVO getBank(BankVO vo) throws DAOException {
        String SQL = "select * from teflow_bank where 1=1 and bank_code = ? and org_id = ?";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(SQL);
            int i = 1;
            stm.setString(i++, vo.getBankCode());
            stm.setString(i++, vo.getOrgId());
            ResultSet rs = stm.executeQuery();
            if (!rs.next()) {
                return null;
            }
            BankVO resultVO = new BankVO();
            resultVO.setBankCode(rs.getString("bank_code"));
            resultVO.setBankName(rs.getString("bank_name"));
            resultVO.setAccountCode(rs.getString("account_code"));
            resultVO.setAccountName(rs.getString("account_name"));
            resultVO.setType(rs.getString("type"));
            resultVO.setOrgId(rs.getString("org_id"));
            resultVO.setCity(rs.getString("city"));
            resultVO.setSunCode(rs.getString("sun_code"));
            resultVO.setIsDefault(rs.getInt("is_default"));
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
    
    public void saveBank(BankVO vo) throws DAOException {
        this.deleteBank(vo);
        this.addBank(vo);
        if(vo.getIsDefault()==1){
        	this.setDefault(vo);
        }
    }

    public void addBank(BankVO vo) throws DAOException {
        String SQL = "insert into teflow_bank(bank_code, bank_name, account_code, account_name, org_id, city, sun_code, type, is_default)" +
        		" values(?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(SQL);
            int i = 1;
            stm.setString(i++, vo.getBankCode());
            stm.setString(i++, vo.getBankName());
            stm.setString(i++, vo.getAccountCode());
            stm.setString(i++, vo.getAccountName());
            stm.setString(i++, vo.getOrgId());
            stm.setString(i++, vo.getCity());
            stm.setString(i++, vo.getSunCode());
            stm.setString(i++, vo.getType());
            stm.setInt(i++, vo.getIsDefault());
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

    public void deleteBank(BankVO vo) throws DAOException {
        String SQL = "delete from teflow_bank where 1=1 and bank_code = ? and org_id = ?";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(SQL);
            int i = 1;
            stm.setString(i++, vo.getBankCode());
            stm.setString(i++, vo.getOrgId());
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

	public void setDefault(BankVO vo) throws DAOException {
        String SQL1 = "update teflow_bank set is_default=0 where org_id = ?";
        String SQL2 = "update teflow_bank set is_default=1 where bank_code = ? and org_id = ?";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(SQL1);
            stm.setString(1, vo.getOrgId());
            stm.executeUpdate();

            stm = conn.prepareStatement(SQL2);
            stm.setString(1, vo.getBankCode());
            stm.setString(2, vo.getOrgId());
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
