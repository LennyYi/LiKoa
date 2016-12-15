package com.aiait.eflow.housekeeping.dao;

import java.sql.Driver;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import com.aiait.eflow.basedata.vo.BaseDataVO;
import com.aiait.eflow.common.CommonName;
import com.aiait.eflow.formmanage.vo.DictionaryDataVO;
import com.aiait.eflow.housekeeping.vo.StaffVO;
import com.aiait.eflow.housekeeping.vo.SystemFieldVO;
import com.aiait.eflow.util.FieldUtil;
import com.aiait.eflow.util.StringUtil;
import com.aiait.eflow.util.XMLUtil;
import com.aiait.eflow.wkf.util.WorkFlowXmlUtil;
import com.aiait.framework.db.BaseDAOImpl;
import com.aiait.framework.db.ConnManagerFactory;
import com.aiait.framework.db.ConnectionException;
import com.aiait.framework.db.DBManagerFactory;
import com.aiait.framework.db.IBaseDAO;
import com.aiait.framework.db.IConnectionManager;
import com.aiait.framework.db.IDBManager;
import com.aiait.framework.exception.DAOException;
import com.aiait.framework.vo.BaseVO;

import net.sf.json.*;
import net.sf.json.xml.XMLSerializer;

public class SystemFieldDAO extends BaseDAOImpl {
	
	
	public static void main(String[] args) throws DAOException {
		IDBManager dbManager = null;
		try {
			dbManager =  DBManagerFactory.getDBManager();
			SystemFieldDAO sd = new SystemFieldDAO(dbManager);
			System.out.println(sd.getAllField());
		} catch (ConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace(); 
		}
	}
	
	
	public SystemFieldDAO(IDBManager dbManager){
		super(dbManager);
	}
	
	public Collection getOneField(String fieldId)throws DAOException{
		Collection list = new ArrayList();
		String sql = "select data_sql from teflow_system_field where "+fieldId+" order by field_label";
		Collection rList = dbManager.query(sql);
		if(rList!=null && rList.size()>0){
			list = convertData(rList);
		}
		return list;
	}
	
	
	public Collection getAllField()throws DAOException{
		Collection list = new ArrayList();
		String sql = "select * from teflow_system_field where 1=1 order by field_label";
		Collection rList = dbManager.query(sql);
		if(rList!=null && rList.size()>0){
			list = convertData(rList);
		}
		return list;
	}
	
	public SystemFieldVO getField(String fieldId)throws DAOException{
		return getField(fieldId, null, null);
	}
	public SystemFieldVO getField(String fieldId,String staffCode, String formId)throws DAOException{
		String sql = "select * from teflow_system_field where field_id='" + fieldId + "'";
		Collection list = dbManager.query(sql);
		if(list==null)return null;
		Collection rList=null;
		if (staffCode==null){
			rList = convertData(list);
		}else{
			String[] paramNames = {"@staffcode","@formid"};
			//20150423 Justin Bin Added
			
		
			String[] paramValues = {"'"+staffCode+"'","'"+formId+"'"};
			rList = convertData(list,paramNames, paramValues);
		}
		return (SystemFieldVO)rList.iterator().next();
	}
	private Collection convertData(Collection rList)throws DAOException{		
		return convertData(rList, null, null);
	}
	
	//20150423 Justin Bin Added
	private Collection getParamValues(String paramString)throws DAOException{
		Collection list=new ArrayList();
			 try {
				 
				 String [] arrayStr=paramString.split(",");
				 String aa="";
				 for(int i=0;i<arrayStr.length;i++){
					 aa+=",'"+arrayStr[i]+"'";
				 }
				 aa=aa.substring(1);
				 String paramSql="select param_code,param_value from teflow_param_config where param_code in ("+aa+")";
				 list = dbManager.query(paramSql);
				 if(list!=null){
					 list.toString();
					 return list;
				 }
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
			 return list;
		
	}
	
	/**
	 * 
	 * @param rList
	 * @param paramNames   传入的动态参数名的数组
	 * @param paramValues  传入的动态参数的值的数组 (paramNames paramValues的大小必须要保证一致，该函数不提供进行检查的功能
	 * @return
	 * @throws DAOException
	 */
	private Collection convertData(Collection rList,String[] paramNames, String[] paramValues)throws DAOException{
		Collection list = new ArrayList();
		Iterator it = rList.iterator();
		while(it.hasNext()){
			HashMap map = (HashMap)it.next();
			SystemFieldVO vo = new SystemFieldVO();
			
			vo.setFieldId(FieldUtil.convertSafeString(map,"FIELD_ID"));
			vo.setFieldLabel(FieldUtil.convertSafeString(map,"FIELD_LABEL"));
			vo.setFieldType(FieldUtil.convertSafeString(map,"FIELD_TYPE"));
			vo.setColumnType(FieldUtil.convertSafeInt(map,"COLUMN_TYPE",1));
			vo.setColumnLength(FieldUtil.convertSafeInt(map,"COLUMN_LENGTH",10));
			vo.setSrcSQL(FieldUtil.convertSafeString(map,"DATA_SQL"));
			//20150424
			vo.setParams(FieldUtil.convertSafeString(map,"PARAM_LIST"));
			
			String sql =  vo.getSrcSQL();
			
			if(!"".equals(sql)){
				Collection opList =null;
				
				if(vo.getParams()!=""){		//20150427 Justin Bin Added
					//getParamValues(vo.getParams());
					//XMLUtil.parserXml(vo.getParams());
					Collection pList= getParamValues(vo.getParams());
					Iterator it2=pList.iterator();
					while(it2.hasNext()){
						HashMap map2 = (HashMap)it2.next();
						sql=StringUtil.replace(sql,"@"+map2.get("PARAM_CODE").toString(),map2.get("PARAM_VALUE").toString());
						vo.setSrcSQL(sql);
					}
					
				}
				
				if("02".equals(vo.getFieldType())){ //fieldType='02',select list ,并且sql中无动态参数
					opList = dbManager.query(sql);	
				}else if("03".equals(vo.getFieldType())){ 
					if(paramNames!=null){
                      //fieldType='03',Reference Form（并且sql中存在动态参数）
					  //String sql =  vo.getSrcSQL();		
						//sql =  vo.getSrcSQL();	
					  for(int i=0;i<paramNames.length;i++){
				    	  sql = StringUtil.replace(sql,paramNames[i],paramValues[i]);
				       }
					  opList = dbManager.query(sql);
					}
				}
				/*else{
					opList=dbManager.query(sql);		//20150427 Justin Bin Added, 
				}*/
				/**commented by Robin on 03/07/2008
				if (vo.getColumnType()==1&&staffCode==null&&formId==null){
					opList = dbManager.query(FieldUtil.convertSafeString(map,"DATA_SQL"));					
				}else if (vo.getColumnType()==2&&staffCode!=null&&formId!=null){
					//SystemField ColumnType=2 represents having parameter to handle					
					StringBuffer tmpSql =  new StringBuffer(FieldUtil.convertSafeString(map,"DATA_SQL"));				
					String substring1="scode";
					String substring2="fid";
					int position1,position2;
					position1 = tmpSql.lastIndexOf(substring1);					
					tmpSql.replace(position1, position1+substring1.length(), "'"+staffCode+"'");
					position2 = tmpSql.lastIndexOf(substring2);
					tmpSql.replace(position2, position2+substring2.length(), "'"+formId+"'");		
					opList = dbManager.query(tmpSql.toString());
				}
				**/
				
				if(opList!=null && opList.size()>0){
					Iterator opIt = opList.iterator();
					Collection optionList = new ArrayList();
					while(opIt.hasNext()){
						HashMap tmpMap = (HashMap)opIt.next();
						DictionaryDataVO bVo = new DictionaryDataVO();
						bVo.setId((String)tmpMap.get("OPTION_VALUE"));
						bVo.setValue((String)tmpMap.get("OPTION_LABEL"));
						bVo.setMisc((String)tmpMap.get("OPTION_MISC"));
						optionList.add(bVo);
					}
					vo.setOptionList(optionList);
				}
			}
			list.add(vo);
		}
		return list;
	}
	public String getSelectedReqList(String request_no, String formsystemId, String sectionId)throws DAOException{
		String sql = "select reference_form from teflow_"+formsystemId+"_"+sectionId+" where request_no='"+request_no+"'";
		Collection list = dbManager.query(sql);
		if(list==null)return null;
		String retStr = "";
		retStr = convertreform(list);
		System.out.println(retStr);
		return retStr;
	}
	private String convertreform(Collection rList)throws DAOException {
		String retStr="";
		Iterator it = rList.iterator();
		while(it.hasNext()){
			HashMap map = (HashMap)it.next();
			retStr = FieldUtil.convertSafeString(map,"REFERENCE_FORM");
		}
		return retStr;
	}
	public int delete(BaseVO v) throws DAOException {
		  SystemFieldVO vo = (SystemFieldVO)v;
          String sql = "delete from teflow_system_field where field_id='" + vo.getFieldId()+"'";
          dbManager.executeUpdate(sql);
          return 0;
	}
    
	public void saveOrUpate(BaseVO v,String savaType) throws DAOException {
		if("create".equals(savaType)){
			save(v);
		}else{
			update(v);
		}
	}
	
	public int save(BaseVO v) throws DAOException {
		SystemFieldVO vo = (SystemFieldVO)v;
		StringBuffer sql = new StringBuffer("insert into teflow_system_field(field_id,field_label,field_type,data_sql,column_type,column_length) ");
		sql.append(" values('").append(vo.getFieldId()).append("','").append(vo.getFieldLabel()).append("'")
		   .append(",'").append(vo.getFieldType()).append("','").append(vo.getSrcSQL()).append("'")
		   .append(",'").append(vo.getColumnType()).append(",").append(vo.getColumnLength()).append(")");
		dbManager.executeUpdate(sql.toString());
		return 0;
	}

	public int update(BaseVO v) throws DAOException {
		SystemFieldVO vo = (SystemFieldVO)v;
		StringBuffer sql = new StringBuffer("update teflow_system_field ");
		sql.append(" set field_label='").append(vo.getFieldLabel()).append("'")
		   .append(",field_type='").append(vo.getFieldType()).append("',src_table_name='").append(vo.getSrcSQL()).append("'")
		   .append(",column_type=").append(vo.getColumnType()).append(",column_length=").append(vo.getColumnLength())
		   .append(" where field_id='").append(vo.getFieldId()).append("'");
		dbManager.executeUpdate(sql.toString());
		return 0;
	}

}
