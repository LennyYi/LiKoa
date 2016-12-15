package com.aiait.eflow.housekeeping.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import com.aiait.eflow.housekeeping.vo.SupplierVO;
import com.aiait.framework.db.BaseDAOImpl;
import com.aiait.framework.db.IDBManager;
import com.aiait.framework.exception.DAOException;
import com.aiait.framework.page.PageVO;
import com.aiait.framework.vo.BaseVO;

public class SupplierDAO extends BaseDAOImpl {

	SimpleDateFormat bartDateFormat = new SimpleDateFormat("MM/dd/yyyy");
	
	public SupplierDAO(IDBManager dbManager){
		super(dbManager);
	}
	public int getTotalRecordsNum(SupplierVO vo, PageVO page)throws DAOException{
		
		try {
			String sql = "select count(*) cnt from teflow_supplier where 1=1";

			sql +=  vo.getOrgId() == null || vo.getOrgId().length() == 0 ? "" : " and org_id = '"+vo.getOrgId()+"'";
			sql +=  vo.getCertClass() == null || vo.getCertClass().length() == 0 ? "" : " and class = '"+vo.getCertClass()+"'";
			sql +=  vo.getType() == null || vo.getType().length() == 0 ? "" : " and type like '%" + vo.getType() + "%'";
			sql +=  vo.getNameC() == null ? "" : " and name_c like '%" + vo.getNameC()	+ "%'";
			sql +=  vo.getProduct() == null ? "" : " and product like '%" + vo.getProduct() + "%'";
			sql +=  vo.getTeamCode() == 0 ? "" : " and team_code = " + vo.getTeamCode();
			sql +=  vo.getStatus() == null || vo.getStatus().length() == 0 ? "" : "and status='"+vo.getStatus()+"'";
			
			//System.out.println(staffSQL.toString());
			ArrayList rs = (ArrayList) dbManager.query(sql);
			HashMap resultmap = (HashMap) (rs.iterator().next());
			return Integer.parseInt((String) resultmap.get("CNT"));
		} catch (Exception e) {
            e.printStackTrace();
            throw new DAOException(e);
		}
	}
	public Collection getSupplierList(SupplierVO vo, PageVO page)throws DAOException{
		String sql = "select * from teflow_supplier where 1=1 ";
		
		Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;
        Collection cntrList = new Vector();

        try {
			sql +=  vo.getOrgId() == null || vo.getOrgId().length() == 0 ? "" : " and org_id = '"+vo.getOrgId()+"'";
			sql +=  vo.getCertClass() == null || vo.getCertClass().length() == 0 ? "" : " and class = '"+vo.getCertClass()+"'";
			sql +=  vo.getType() == null || vo.getType().length() == 0 ? "" : " and type like '%" + vo.getType() + "%'";
			sql +=  vo.getNameC() == null ? "" : " and name_c like '%" + vo.getNameC()	+ "%'";
			sql +=  vo.getProduct() == null ? "" : " and product like '%" + vo.getProduct() + "%'";
			sql +=  vo.getTeamCode() == 0 ? "" : " and team_code = " + vo.getTeamCode();
			sql +=  vo.getStatus() == null || vo.getStatus().length() == 0 ? "" : "and status='"+vo.getStatus()+"'";

			ArrayList rs = (ArrayList) dbManager.query(sql, page.getPageSize(), page.getCurrentPage());
            Iterator it = rs.iterator();
            while (it.hasNext()) {
            	SupplierVO resultVO = new SupplierVO();
            	HashMap resultmap = (HashMap) it.next();
            	resultVO.setCertClass       ((String)resultmap.get("CLASS"));
            	resultVO.setCode            ((String)resultmap.get("CODE"));
            	resultVO.setType            ((String)resultmap.get("TYPE"));
            	resultVO.setNameC           ((String)resultmap.get("NAME_C"));           
            	resultVO.setNameE           ((String)resultmap.get("NAME_E"));           
            	resultVO.setProduct         ((String)resultmap.get("PRODUCT"));
            	resultVO.setContacter       ((String)resultmap.get("CONTACTER"));       
            	resultVO.setTel             ((String)resultmap.get("TEL"));             
            	resultVO.setFax             ((String)resultmap.get("FAX"));
            	resultVO.setEmail           ((String)resultmap.get("EMAIL"));
            	resultVO.setRemark          ((String)resultmap.get("REMARK"));
            	resultVO.setAddressC        ((String)resultmap.get("ADDRESS_C"));        
            	resultVO.setAddressE        ((String)resultmap.get("ADDRESS_E"));        
            	resultVO.setEvaluateComments((String)resultmap.get("EVALUATE_COMMENTS"));
            	resultVO.setBank            ((String)resultmap.get("BANK"));            
            	resultVO.setBankAccount     ((String)resultmap.get("BANK_ACCOUNT"));     
            	resultVO.setProvince        ((String)resultmap.get("PROVINCE"));        
            	resultVO.setCity            ((String)resultmap.get("CITY"));            
            	resultVO.setOrgId           ((String)resultmap.get("ORG_ID"));        
            	resultVO.setTeamName        ((String)resultmap.get("TEAM_NAME"));        
            	resultVO.setTeamContacter   ((String)resultmap.get("TEAM_CONTACTER"));    
            	resultVO.setStatus          ((String)resultmap.get("STATUS"));      
            	resultVO.setEffDate         ((String)resultmap.get("EFF_DATE"));
            	resultVO.setTermDate        ((String)resultmap.get("TERM_DATE"));
            	resultVO.setContacter2      ((String)resultmap.get("CONTACTER2"));       
            	resultVO.setTel2            ((String)resultmap.get("TEL2"));             
            	resultVO.setFax2            ((String)resultmap.get("FAX2"));
            	resultVO.setEmail2          ((String)resultmap.get("EMAIL2"));
            	resultVO.setRemark2         ((String)resultmap.get("REMARK2"));
            	resultVO.setTeamCode        (Integer.parseInt((String)resultmap.get("TEAM_CODE")));

                cntrList.add(resultVO);
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
	
    public SupplierVO getSupplier(SupplierVO vo) throws DAOException {
        //String SQL = "select * from teflow_supplier where (code = ? and org_id = ?)";
    	String SQL = "select * from teflow_supplier where (code = ? )";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;
        
        try {
            stm = conn.prepareStatement(SQL);
            stm.setString(1, vo.getCode());
            //stm.setString(2, vo.getOrgId());
            ResultSet rs = stm.executeQuery();
            if (!rs.next()) {
            	
                return null;
            }                

            SupplierVO resultVo = new SupplierVO();
            resultVo.setCertClass       (rs.getString("class"));
        	resultVo.setCode            (rs.getString("code"));       
        	resultVo.setType            (rs.getString("type"));     
        	resultVo.setNameC           (rs.getString("name_c"));           
        	resultVo.setNameE           (rs.getString("name_e"));           
        	resultVo.setProduct         (rs.getString("product"));          
        	resultVo.setEffDate         (rs.getDate("eff_date")==null?"":bartDateFormat.format(rs.getDate("eff_date")));         
        	resultVo.setContacter       (rs.getString("contacter"));       
        	resultVo.setTel             (rs.getString("tel"));          
        	resultVo.setFax             (rs.getString("fax"));                 
        	resultVo.setAddressC        (rs.getString("address_c"));        
        	resultVo.setAddressE        (rs.getString("address_e"));        
        	resultVo.setEvaluateComments(rs.getString("evaluate_comments"));
        	resultVo.setBank            (rs.getString("bank"));            
        	resultVo.setBankAccount     (rs.getString("bank_account"));     
        	resultVo.setProvince        (rs.getString("province"));        
        	resultVo.setCity            (rs.getString("city"));                
        	resultVo.setOrgId           (rs.getString("org_id"));        
        	resultVo.setTeamName        (rs.getString("team_name"));        
        	resultVo.setTeamContacter   (rs.getString("team_contacter"));     
        	resultVo.setStatus          (rs.getString("status"));  
        	resultVo.setTermDate        (rs.getDate("term_date")==null?"":bartDateFormat.format(rs.getDate("term_date")));  
          
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
    
    public void saveSupplier(SupplierVO vo) throws DAOException {
        this.deleteSupplier(vo);
        this.addSupplier(vo);
    }

    public void addSupplier(SupplierVO vo) throws DAOException {
        String SQL = "INSERT INTO teflow_supplier(class, code, type, name_c, name_e, product, eff_date, contacter, tel, fax, address_c, address_e," +
        		" evaluate_comments, bank, bank_account, province, city, org_id, team_name, team_contacter, status, term_date)" +
        		" values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(SQL);
            int i = 1;    
            stm.setString(i++, vo.getCertClass());
            stm.setString(i++, vo.getCode());
            stm.setString(i++, vo.getType());
            stm.setString(i++, vo.getNameC());            
            stm.setString(i++, vo.getNameE());            
            stm.setString(i++, vo.getProduct());
            stm.setString(i++, vo.getEffDate());
            stm.setString(i++, vo.getContacter()); 
            stm.setString(i++, vo.getTel());
            stm.setString(i++, vo.getFax());
            stm.setString(i++, vo.getAddressC()); 
            stm.setString(i++, vo.getAddressE());         
            stm.setString(i++, vo.getEvaluateComments()); 
            stm.setString(i++, vo.getBank()); 
            stm.setString(i++, vo.getBankAccount());
            stm.setString(i++, vo.getProvince());
            stm.setString(i++, vo.getCity()); 
            stm.setString(i++, vo.getOrgId()); 
            stm.setString(i++, vo.getTeamName()); 
            stm.setString(i++, vo.getTeamContacter()); 
            stm.setString(i++, vo.getStatus()); 
            stm.setString(i++, vo.getTermDate());
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
    
    public void deleteSupplier(SupplierVO vo) throws DAOException {
        //String SQL = "delete from teflow_supplier where (code = ? and org_id = ?)";
    	String SQL = "delete from teflow_supplier where (code = ?)";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(SQL);
            stm.setString(1, vo.getCode());
            //stm.setString(2, vo.getOrgId());
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

}
