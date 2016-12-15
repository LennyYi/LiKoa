package com.aiait.eflow.housekeeping.dao;

import java.sql.*;
import java.util.*;

import com.aiait.eflow.housekeeping.vo.CostCenterVO;
import com.aiait.eflow.util.FieldUtil;
import com.aiait.framework.db.*;
import com.aiait.framework.exception.DAOException;
import com.aiait.framework.page.PageVO;
import com.aiait.framework.vo.BaseVO;

public class CostCenterDAO extends BaseDAOImpl {
	
    public CostCenterDAO(IDBManager dbManager) {
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

	public int getTotalRecordsNum(PageVO page) throws DAOException {
        String SQL = "select count(*) from teflow_cost_center";

        int i = dbManager.getRecordCount(SQL);
        return i;
    }
	
    public Collection search(PageVO page) throws DAOException {
        String SQL = "select * from teflow_cost_center order by cc_code";
        
        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;
        Collection rateList = new Vector();

        try {

    		Collection list = dbManager.query(SQL, page.getPageSize(),page.getCurrentPage());
    		
    		if(list==null || list.size()==0) {			
    			return null;
    		}

    		Iterator it = list.iterator();		
    		while(it.hasNext()){
    			HashMap map = (HashMap)it.next();
    			CostCenterVO costCenterVO = new CostCenterVO();
                
    			costCenterVO.setCc_code((String)map.get("CC_CODE"));
    			costCenterVO.setCc_name((String)map.get("CC_NAME"));
    			costCenterVO.setExco((String)map.get("EXCO"));
    			costCenterVO.setT_code((String)map.get("TEAM_CODE"));
                rateList.add(costCenterVO);
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

    public CostCenterVO getCode(CostCenterVO costCenterVO) throws DAOException {
        String SQL = "select * from teflow_cost_center where cc_code = ? ";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(SQL);
            int i = 1;
            stm.setString(i++, costCenterVO.getCc_code());
            ResultSet rs = stm.executeQuery();
            if (!rs.next()) {
                return null;
            }
            CostCenterVO resultVO = new CostCenterVO();
            resultVO.setCc_code(rs.getString("cc_code"));
            resultVO.setCc_name(rs.getString("cc_name"));
            resultVO.setExco(rs.getString("exco"));
            resultVO.setT_code(rs.getString("Team_code"));
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
    
    public void saveCode(CostCenterVO costCenterVO) throws DAOException {
        this.deleteCode(costCenterVO);
        this.addCode(costCenterVO);
    }

    public void addCode(CostCenterVO costCenterVO) throws DAOException {
        String SQL = "insert into teflow_cost_center(cc_code, cc_name, exco, Team_code) values(?, ?, ?, ?)";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(SQL);
            int i = 1;
            stm.setString(i++, costCenterVO.getCc_code());
            stm.setString(i++, costCenterVO.getCc_name());
            stm.setString(i++, costCenterVO.getExco());
            stm.setString(i++, costCenterVO.getT_code());
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

    public void deleteCode(CostCenterVO costCenterVO) throws DAOException {
        String SQL = "delete from teflow_cost_center where cc_code = ?";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(SQL);
            int i = 1;
            stm.setString(i++, costCenterVO.getCc_code());
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
