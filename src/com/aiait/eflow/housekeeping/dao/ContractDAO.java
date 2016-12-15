package com.aiait.eflow.housekeeping.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Vector;

import com.aiait.eflow.common.CommonName;
import com.aiait.eflow.common.helper.ParamConfigHelper;
import com.aiait.eflow.housekeeping.vo.ContractVO;
import com.aiait.framework.db.BaseDAOImpl;
import com.aiait.framework.db.IDBManager;
import com.aiait.framework.exception.DAOException;
import com.aiait.framework.vo.BaseVO;

public class ContractDAO extends BaseDAOImpl {

	SimpleDateFormat bartDateFormat = new SimpleDateFormat("MM/dd/yyyy");
	
	public ContractDAO(IDBManager dbManager){
		super(dbManager);
	}
	
	public Collection getCntrList()throws DAOException{
		String sql = "select * from teflow_contract order by contract_no ";        
		Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;
        Collection cntrList = new Vector();

        try {
            stm = conn.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                ContractVO vo = new ContractVO();
                vo.setContractNo(rs.getInt("contract_no"));
                vo.setReceiveDate(rs.getDate("receive_date")==null?"":bartDateFormat.format(rs.getDate("receive_date")));
                vo.setRespStaff(rs.getString("resp_staff"));
                vo.setRespDept(rs.getString("resp_department"));
                vo.setContactTel(rs.getString("contact_tel"));
                vo.setContractName(rs.getString("contract_name"));
                vo.setSign1(rs.getString("sign_1"));
                vo.setSign2(rs.getString("sign_2"));	
                vo.setSign3(rs.getString("sign_3"));
                vo.setContent(rs.getString("content"));
                vo.setAmount(rs.getDouble("amount"));
                vo.setSignDate(rs.getDate("sign_date")==null?"":bartDateFormat.format(rs.getDate("sign_date")));
                vo.setEffPeriod(rs.getString("eff_period"));
                vo.setIssueDate(rs.getDate("issue_date")==null?"":bartDateFormat.format(rs.getDate("issue_date")));
                vo.setSignDoc(rs.getString("sign_doc"));
                vo.setOrgName(rs.getString("org_name"));
                vo.setRemark(rs.getString("remark"));
                
                cntrList.add(vo);
            }
            return cntrList;
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
	
    public ContractVO getCntr(ContractVO vo) throws DAOException {
        String SQL = "select * from teflow_contract where (contract_no = ?)";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;
        
        try {
            stm = conn.prepareStatement(SQL);
            stm.setInt(1, vo.getContractNo());
            ResultSet rs = stm.executeQuery();
            if (!rs.next()) {
            	
                return null;
            }                

            ContractVO resultVo = new ContractVO();
            resultVo.setContractNo(rs.getInt("contract_no"));
            resultVo.setCity(rs.getString("city"));
            resultVo.setReceiveDate(rs.getDate("receive_date")==null?"":bartDateFormat.format(rs.getDate("receive_date")));
            resultVo.setRespStaff(rs.getString("resp_staff"));
            resultVo.setRespDept(rs.getString("resp_department"));
            resultVo.setContactTel(rs.getString("contact_tel"));
            resultVo.setContractName(rs.getString("contract_name"));
            resultVo.setSign1(rs.getString("sign_1"));
            resultVo.setSign2(rs.getString("sign_2"));	
            resultVo.setSign3(rs.getString("sign_3"));
            resultVo.setContent(rs.getString("content"));
            resultVo.setAmount(rs.getDouble("amount"));
            resultVo.setSignDate(rs.getDate("sign_date")==null?"":bartDateFormat.format(rs.getDate("sign_date")));
            resultVo.setEffPeriod(rs.getString("eff_period"));
            resultVo.setIssueDate(rs.getDate("issue_date")==null?"":bartDateFormat.format(rs.getDate("issue_date")));
            resultVo.setSignDoc(rs.getString("sign_doc"));
            resultVo.setOrgName(rs.getString("org_name"));
            resultVo.setRemark(rs.getString("remark"));
          
            return resultVo;
            
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
    
    public void saveCntr(ContractVO vo) throws DAOException {
        this.deleteCntr(vo);
        this.addCntr(vo);
    }

    public void addCntr(ContractVO vo) throws DAOException {
        String SQL = "insert into teflow_contract(contract_no,city,receive_date,resp_staff,resp_department,contact_tel,contract_name," +
        		"sign_1,sign_2,sign_3,content,amount,sign_date,eff_period,issue_date,sign_doc,org_name,remark) " +
        		"values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(SQL);
            int i = 1;
            stm.setInt(i++, vo.getContractNo());
            stm.setString(i++, vo.getCity());
            stm.setString(i++, vo.getReceiveDate());
            stm.setString(i++, vo.getRespStaff());
            stm.setString(i++, vo.getRespDept());
            stm.setString(i++, vo.getContactTel());
            stm.setString(i++, vo.getContractName());
            stm.setString(i++, vo.getSign1());
            stm.setString(i++, vo.getSign2());
            stm.setString(i++, vo.getSign3());
            stm.setString(i++, vo.getContent());
            stm.setDouble(i++, vo.getAmount());
            stm.setString(i++, vo.getSignDate());
            stm.setString(i++, vo.getEffPeriod());
            stm.setString(i++, vo.getIssueDate());
            stm.setString(i++, vo.getSignDoc());
            stm.setString(i++, vo.getOrgName());
            stm.setString(i++, vo.getRemark());
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
    
    public void deleteCntr(ContractVO vo) throws DAOException {
        String SQL = "delete from teflow_contract where (contract_no = ?)";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(SQL);
            stm.setInt(1, vo.getContractNo());
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
	
	public Collection queryContracts(ContractVO queryVo) throws DAOException {
        String SQL = "select * from teflow_contract where contract_no like ? order by contract_no";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(SQL);
            int i = 1;
            if ("Yes".equalsIgnoreCase(ParamConfigHelper.getInstance().getParamValue(
                    CommonName.PARAM_CONTRACT_SELECT_LIKE))) {
                stm.setString(i++, "%" + queryVo.getContractNo() + "%");
            } else {
                stm.setString(i++, "" + queryVo.getContractNo());
            }
            ResultSet rs = stm.executeQuery();
            Collection contracts = new Vector();
            while (rs.next()) {
                ContractVO vo = new ContractVO();
                vo.setContractNo(rs.getInt("contract_no"));
                contracts.add(vo);
            }
            return contracts;
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
