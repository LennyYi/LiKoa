package com.aiait.eflow.housekeeping.dao;

import java.sql.*;
import java.util.*;

import com.aiait.eflow.housekeeping.vo.FinanceCodeVO;
import com.aiait.eflow.housekeeping.vo.TeamVO;
import com.aiait.framework.db.*;
import com.aiait.framework.exception.DAOException;
import com.aiait.framework.page.PageVO;
import com.aiait.framework.vo.BaseVO;

public class T2T9DAO extends BaseDAOImpl {

	private String tableName;
	private String orgIdClause;
	private static String CONST_SUNAC = "SUNAC";	
	private static String CONST_T2 = "T2";		
	private static String CONST_T6 = "T6";
	
    public T2T9DAO(IDBManager dbManager) {
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

	public int getTotalRecordsNum(String orgId, PageVO page) throws DAOException {
        String SQL = "select count(*) from teflow_payment_" + tableName + "_code";

        if (tableName.equalsIgnoreCase(CONST_T2)) {
            SQL = "select count(*) from teflow_payment_" + tableName + "_code where org_id = '" + orgId + "'";
        }

        int i = dbManager.getRecordCount(SQL.toString());
        return i;
    }
	
    public Collection search(String orgId,PageVO page) throws DAOException {
        String SQL = "select * from teflow_payment_" +tableName+ "_code order by code";
        
        if (tableName.equalsIgnoreCase(CONST_T2)) {
            SQL = "select * from teflow_payment_" + tableName + "_code where org_id='" + orgId + "' order by code";
        }
        
        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;
        Collection rateList = new Vector();

        try {
            /*stm = conn.prepareStatement(SQL);
            
            if(!tableName.equalsIgnoreCase(CONST_SUNAC))stm.setString(1, orgId);
            
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                FinanceCodeVO t2t9VO = new FinanceCodeVO();
                
                t2t9VO.setOrgId(orgId);
                t2t9VO.setCode(rs.getString("code"));
                t2t9VO.setName(rs.getString("name"));
                rateList.add(t2t9VO);
            }*/
    		Collection list = dbManager.query(SQL.toString(),page.getPageSize(),page.getCurrentPage());
    		
    		if(list==null || list.size()==0) {			
    			return null;
    		}
    		Collection result = new ArrayList();
    		Iterator it = list.iterator();		
    		while(it.hasNext()){
    			HashMap map = (HashMap)it.next();
                FinanceCodeVO t2t9VO = new FinanceCodeVO();
                
                t2t9VO.setOrgId(orgId);
                t2t9VO.setCode((String)map.get("CODE"));
                t2t9VO.setName((String)map.get("NAME"));
                rateList.add(t2t9VO);
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

    public FinanceCodeVO getCode(FinanceCodeVO t2t9VO) throws DAOException {
        String SQL = "select * from teflow_payment_" +tableName+ "_code where code = ? " + orgIdClause;

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(SQL);
            int i = 1;
            stm.setString(i++, t2t9VO.getCode());
            if (false == tableName.equalsIgnoreCase(CONST_SUNAC)) {
                stm.setString(i++, t2t9VO.getOrgId());
            }
            ResultSet rs = stm.executeQuery();
            if (!rs.next()) {
                return null;
            }
            FinanceCodeVO resultVO = new FinanceCodeVO();
            if(tableName.equalsIgnoreCase(CONST_SUNAC)){
            	resultVO.setT0(rs.getInt("T0"));
            	resultVO.setT1(rs.getInt("T1"));
            	resultVO.setT2(rs.getInt("T2"));
            	resultVO.setT3(rs.getInt("T3"));
            	resultVO.setT4(rs.getInt("T4"));
            	resultVO.setT5(rs.getInt("T5"));
            	resultVO.setT6(rs.getInt("T6"));
            } else {
            	resultVO.setOrgId(rs.getString("org_id"));
            }
            resultVO.setCode(rs.getString("code"));
            resultVO.setName(rs.getString("name"));
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
    
    public void saveCode(FinanceCodeVO t2t9VO) throws DAOException {
        this.deleteCode(t2t9VO);
        this.addCode(t2t9VO);
    }

    public void addCode(FinanceCodeVO t2t9VO) throws DAOException {
        String SQL = "insert into teflow_payment_" +tableName+ "_code(org_id, code, name) values(?, ?, ?)";


        if(tableName.equalsIgnoreCase(CONST_SUNAC)){
        	SQL = "insert into teflow_payment_" +tableName+ "_code(t0, t1, t2, t3, t4, t5, t6, code, name) values(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        }
        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(SQL);
            int i = 1;
            if(tableName.equalsIgnoreCase(CONST_SUNAC)){
            	stm.setInt(i++, t2t9VO.getT0());
            	stm.setInt(i++, t2t9VO.getT1());
            	stm.setInt(i++, t2t9VO.getT2());
            	stm.setInt(i++, t2t9VO.getT3());
            	stm.setInt(i++, t2t9VO.getT4());
            	stm.setInt(i++, t2t9VO.getT5());
            	stm.setInt(i++, t2t9VO.getT6());
            } else {
            	stm.setString(i++, t2t9VO.getOrgId());
            }
            stm.setString(i++, t2t9VO.getCode());
            stm.setString(i++, t2t9VO.getName());
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

    public void deleteCode(FinanceCodeVO t2t9VO) throws DAOException {
        String SQL = "delete from teflow_payment_" + tableName + "_code where code = ?";

        if (tableName.equalsIgnoreCase(CONST_T2)) {
            // T2 code can have multi-records with the same code.
            if (t2t9VO.getName() != null && !"".equals(t2t9VO.getName())) {
                SQL = "delete from teflow_payment_" + tableName + "_code where code = ? and org_id = ? and name = ?";
            } else {
                SQL = "delete from teflow_payment_" + tableName + "_code where code = ? and org_id = ?";
            }
        }

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(SQL);
            int i = 1;
            stm.setString(i++, t2t9VO.getCode());
            if (tableName.equalsIgnoreCase(CONST_T2)) {
                stm.setString(i++, t2t9VO.getOrgId());
                if (t2t9VO.getName() != null && !"".equals(t2t9VO.getName())) {
                    stm.setString(i++, t2t9VO.getName());
                }
            }
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

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String flgT2T9) {
		this.tableName = flgT2T9;
		if (false==tableName.equalsIgnoreCase(CONST_SUNAC)){
			this.orgIdClause = " and org_id = ? ";
		} else {
			this.orgIdClause = "";					
		}
	}


}
