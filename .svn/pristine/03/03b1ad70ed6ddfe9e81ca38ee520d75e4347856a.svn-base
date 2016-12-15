package com.aiait.eflow.basedata.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import com.aiait.eflow.basedata.vo.BaseDataVO;
import com.aiait.eflow.util.DataConvertUtil;
import com.aiait.eflow.util.FieldUtil;
import com.aiait.framework.common.DataType;
import com.aiait.framework.db.BaseDAOImpl;
import com.aiait.framework.db.IDBManager;
import com.aiait.framework.exception.DAOException;
import com.aiait.framework.vo.BaseVO;

public class BaseDataDAO extends BaseDAOImpl {
	
	public BaseDataDAO(IDBManager dbManager){
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
	
	
	
	/**
	 * 
	 * @param vo
	 * @throws DAOException
	 */
	public void updateMasterData(BaseDataVO vo)throws DAOException{
		String sql = "update teflow_base_data_master set field_name='"+vo.getFieldName()+"'";
		 sql = sql + " where master_id in (select master_id from teflow_field_basedata where form_system_id=" + vo.getFormSystemId()
		           + " and section_id='"+vo.getSectionId()+"' and field_id='"+vo.getFieldId()+"')";
	   dbManager.executeUpdate(sql);
	}
	
	/**
	 * 检查master下是否已经存在optionValue的值
	 * @param vo
	 * @return false --- 已经存在； true----不存在
	 * @throws DAOException
	 */
	public boolean checkOptionValue(BaseDataVO vo)throws DAOException{
		String sql = "select count(*) from teflow_base_data_detail where master_id="
			         +vo.getMasterId()+" and option_value='" + vo.getOptionValue()+"' ";
		int count = dbManager.getRecordCount(sql);
		if(count>0){
			return false;
		}
		return true;
	}
	
	public void saveDetailData(BaseDataVO vo)throws DAOException{
		String sql = "INSERT INTO teflow_base_data_detail(master_id,option_value,option_label) values("
			       +vo.getMasterId()+",'" + vo.getOptionValue()+"','" + vo.getOptionLabel()+"')";
		dbManager.executeUpdate(sql);
	}
	
	public void deleteDetailData(long masterId,String optionValue)throws DAOException{
		String sql = "delete from teflow_base_data_detail where master_id=" + masterId + " and option_value='" + optionValue + "'";
		dbManager.executeUpdate(sql);
	}
	
	public void updateOptionLabel(BaseDataVO vo)throws DAOException{
		String sql= "update teflow_base_data_detail set option_label='"+vo.getOptionLabel()+"' where master_id = "+vo.getMasterId()+" and option_value='"
		            +vo.getOptionValue()+"'";
		dbManager.executeUpdate(sql);
	}
	
	
	public BaseDataVO getMasterData(int formSystemId,String sectionId,String fieldId)throws DAOException{
		String sql = "select * from teflow_base_data_master where master_id in (select master_id from teflow_field_basedata "
			         + " where form_system_id=" + formSystemId + " and section_id='"+sectionId+"' and field_id='" + fieldId + "')";
		
		Collection list = dbManager.query(sql);
		if(list==null || list.size()==0) return null;
		HashMap map = (HashMap)list.iterator().next();
		BaseDataVO vo = new BaseDataVO();
		vo.setMasterId(Long.parseLong((String)map.get("MASTER_ID")));
		vo.setFieldName(DataConvertUtil.convertISOToGBK((String)map.get("FIELD_NAME")));
		return vo;
	}
	
	public Collection getMasterDataByType(String typeCode)throws DAOException{
		return getMasterDataByType(-1,typeCode);
	}
	
	public Collection getMasterDataByType(long masterId,String typeCode)throws DAOException{
		String sql = "select * from teflow_base_data_master where 1=1 ";
		if(masterId>-1){
			sql = sql + " and master_id = " + masterId;
		}
		if(typeCode!=null && !"".equals(typeCode)){
			sql = sql + " and type_code='" + typeCode + "' order by field_name";
		}else{
			
			sql = sql + " order by type_code,field_name";
		}
        Collection list = dbManager.query(sql);
        Collection<BaseDataVO> result = new ArrayList<BaseDataVO>();
        if(list==null || list.size()<1) return result;
		Iterator it = list.iterator();
		while(it.hasNext()){
			HashMap map = (HashMap)it.next();
			BaseDataVO vo = new BaseDataVO();
			vo.setMasterId(Long.parseLong((String)map.get("MASTER_ID")));
			vo.setFieldName(DataConvertUtil.convertISOToGBK((String)map.get("FIELD_NAME")));
			result.add(vo);
		}
		return result;
	}
	
	public Collection getAllMasterData()throws DAOException{
		 return getMasterDataByType(null);
	}
	
	/**
	 * 获取所有form字段关联基础数据关系数据
	 * @return
	 * @throws DAOException
	 */
	public Collection getAllFieldBaseData()throws DAOException{
		String sql = "select * from teflow_field_basedata where 1=1 ";
		Collection<BaseDataVO> result = new ArrayList<BaseDataVO>();
		Collection list = dbManager.query(sql);
		if(list==null || list.size()<1) return result;
		Iterator it = list.iterator();
		while(it.hasNext()){
			HashMap map = (HashMap)it.next();
			BaseDataVO vo = new BaseDataVO();
			vo.setMasterId(Long.parseLong((String)map.get("MASTER_ID")));
			vo.setFormSystemId(Integer.parseInt((String)map.get("FORM_SYSTEM_ID")));
			vo.setSectionId((String)map.get("SECTION_ID"));
			vo.setFieldId((String)map.get("FIELD_ID"));
			result.add(vo);
		}	
		return result;
	}
	
	public Collection getDetailData(long masterId)throws DAOException{
		String sql = "select * from teflow_base_data_detail where master_id="+masterId + " order by option_value";
		Collection list = dbManager.query(sql);
		Collection<BaseDataVO> result = new ArrayList<BaseDataVO>();
		if(list==null || list.size()<1) return result;
		Iterator it = list.iterator();
		while(it.hasNext()){
			HashMap map = (HashMap)it.next();
			BaseDataVO vo = new BaseDataVO();
			vo.setMasterId(Long.parseLong((String)map.get("MASTER_ID")));
			vo.setOptionValue((String)map.get("OPTION_VALUE"));
			vo.setOptionLabel(DataConvertUtil.convertISOToGBK((String)map.get("OPTION_LABEL")));			
			vo.setStatus(DataConvertUtil.convertISOToGBK((String)map.get("STATUS")));		
			result.add(vo);
		}	
		return result;
	}
	
	public BaseDataVO getBaseDataVO(long masterId,String optionValue)throws DAOException{
		String sql = "select * from teflow_base_data_detail where master_id=" + masterId + " and option_value='"
		          +optionValue+"'";
		Collection list = dbManager.query(sql);
		if(list==null || list.size()==0) return null;
		BaseDataVO vo = new BaseDataVO();
		HashMap map = (HashMap)list.iterator().next();
		vo.setMasterId(FieldUtil.convertSafeInt(map,"MASTER_ID",0));
		vo.setOptionValue(FieldUtil.convertSafeString(map,"OPTION_VALUE"));
		vo.setOptionLabel(FieldUtil.convertSafeString(map,"OPTION_LABEL"));
		vo.setSectionId(FieldUtil.convertSafeString(map, "STATUS"));//IT1321
		return vo;
	}

   /**
    * 添加字段与字典数据之间的关联
    * @param masterId
    * @param formSystemId
    * @param sectionId
    * @param fieldId
    * @throws DAOException
    */
   public void addFieldBaseDataLink(int masterId,int formSystemId,String sectionId,String fieldId)throws DAOException{
	   
	   deleteFieldBaseDataLink(formSystemId,sectionId,fieldId);
	   
	   String sql = "insert into teflow_field_basedata(master_id,form_system_id,section_id,field_id) values(?,?,?,?)";
	   Object[] obj = new Object[4];
	   obj[0] = "" + masterId;
	   obj[1] = "" + formSystemId;
	   obj[2] = sectionId;
	   obj[3] = fieldId;
	   int[] dataType = {DataType.INT,DataType.INT,DataType.VARCHAR,DataType.VARCHAR};
	   dbManager.executeUpdate(sql,obj,dataType);
   }
	
   /**
    * 检查某个master字段是否已经被form使用，如果是，则返回true,否则返回false
    * @param masterId
    * @return
    * @throws DAOException
    */
   public boolean checkMasterIdInForm(long masterId)throws DAOException{
	   String sql = "select count(*) from teflow_field_basedata where master_id=" + masterId;
	   int count = dbManager.getRecordCount(sql);
	   if(count>0){
		   return true;
	   }else{
		   return false;
	   }
   }
   
	/**
	 * 删除字段与字典数据之间的关系
	 * @param formSystemId
	 * @param sectionId
	 * @param fieldId
	 * @throws DAOException
	 */
	public void deleteFieldBaseDataLink(int formSystemId,String sectionId,String fieldId)throws DAOException{
		String sql = "delete from teflow_field_basedata where  form_system_id=? and section_id=? and field_id=?";
		Object[] obj = new Object[3];
	    obj[0] = ""+formSystemId;
	    obj[1] = sectionId;
	    obj[2] = fieldId;
	    int[] dataType = {DataType.INT,DataType.VARCHAR,DataType.VARCHAR};
	    dbManager.executeUpdate(sql,obj,dataType);	
	}
	
	public void deleteDetailData(int formSystemId,String sectionId,String fieldId)throws DAOException{
		String sql = "delete from teflow_base_data_detail where master_id in (select master_id from teflow_field_basedata where form_system_id=? and section_id=? and field_id=?)";
		Object[] obj = new Object[3];
	    obj[0] = ""+formSystemId;
	    obj[1] = sectionId;
	    obj[2] = fieldId;
	    int[] dataType = {DataType.INT,DataType.VARCHAR,DataType.VARCHAR};
	    dbManager.executeUpdate(sql,obj,dataType);
	    
	    //sql = "delete from teflow_base_data_master where form_system_id=? and section_id=? and field_id=?";
	    //dbManager.executeUpdate(sql,obj,dataType);
	}

	public int saveDetail(BaseVO baseVo) throws DAOException {
		BaseDataVO vo = (BaseDataVO)baseVo;
		String sql = "INSERT INTO teflow_base_data_detail(master_id,option_value,option_label) select master_id,?,? from teflow_field_basedata where form_system_id=? and section_id=? and field_id=?";
		Object[] obj = new Object[5];
	    obj[0] = vo.getOptionValue();
	    obj[1] = vo.getOptionLabel();	    
		obj[2] = ""+vo.getFormSystemId();
	    obj[3] = vo.getSectionId();
	    obj[4] = vo.getFieldId();
	    int[] dataType1 = {DataType.VARCHAR,DataType.VARCHAR,DataType.INT,DataType.VARCHAR,DataType.VARCHAR};
		dbManager.executeUpdate(sql,obj,dataType1);        
		// TODO Auto-generated method stub
		return 0;
	}
	
	public boolean saveMaster(BaseVO baseVo)  throws DAOException {
		BaseDataVO vo = (BaseDataVO)baseVo;
		////master的名称不能重复！
		//String querySql = "select * from teflow_base_data_master where field_name='"+vo.getFieldName()+"'";
		//Collection list = dbManager.query(querySql);
		//if(list!=null && list.size()>0) return false;
		
		String sql = "INSERT INTO teflow_base_data_master(field_name,type_code) values(?,?)";
		Object[] obj = new Object[2];
		obj[0] = "" + vo.getFieldName();
		obj[1] = vo.getFieldType();
		int[] dataType = {DataType.VARCHAR,DataType.VARCHAR};
		dbManager.executeUpdate(sql,obj,dataType);
		return true;
	}
	
	public long getNewMasterId()throws DAOException{
		String sql = "select IDENT_CURRENT('teflow_base_data_master') as current_master_id";
		Collection list = dbManager.query(sql);
		if(list==null || list.size()==0) return -1;
		HashMap map = (HashMap)list.iterator().next();
		return FieldUtil.convertSafeInt(map,"CURRENT_MASTER_ID",-1);
	}
	
	public void updateMaster(long masterId,String fieldName,String fieldType)throws DAOException{
		String sql = "update teflow_base_data_master set field_name='" + fieldName + "',type_code='" + fieldType+"' where master_id="
		            +masterId;
		dbManager.executeUpdate(sql);
	}
	
	public int saveFieldBaseData(BaseVO baseVo) throws DAOException {
		BaseDataVO vo = (BaseDataVO)baseVo;
		String sql = "INSERT INTO teflow_field_basedata(master_id,form_system_id,section_id,field_id) select master_id,?,?,? from teflow_base_data_master where field_name = ?";
		Object[] obj = new Object[4];
	    obj[0] = ""+vo.getFormSystemId();
	    obj[1] = vo.getSectionId();
	    obj[2] = vo.getFieldId();
	    obj[3] = vo.getFieldName();
	    //obj[3] = vo.getOptionValue();
	    //obj[4] = vo.getOptionLabel();
		int[] dataType = {DataType.INT,DataType.VARCHAR,DataType.VARCHAR,DataType.VARCHAR};
		dbManager.executeUpdate(sql,obj,dataType);
		return 0;
	}

	public int update(BaseVO arg0) throws DAOException {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * IT1321
	 * @param parseLong
	 * @param optionValues
	 */
	public void setOptionsToObsolete(long masterId, String[] optionValues) throws DAOException {
		
		StringBuffer sql = new StringBuffer("update teflow_base_data_detail set status='T'" +
			" where master_id ='" + masterId +"' and option_value in (");
		for(String tmp:optionValues){
			sql.append("'"+tmp+"', ");
		}
		sql.append("null )");
		dbManager.executeUpdate(sql.toString());

	}
	/**
	 * IT1321
	 * @param parseLong
	 * @param optionValues
	 */
	public void reviveObsoleteOptions(long masterId, String[] optionValues) throws DAOException {
		
		StringBuffer sql = new StringBuffer("update teflow_base_data_detail set status='A'" +
			" where master_id ='" + masterId +"' and option_value in (");
		for(String tmp:optionValues){
			sql.append("'"+tmp+"', ");
		}
		sql.append("null )");
		dbManager.executeUpdate(sql.toString());

	}
}
