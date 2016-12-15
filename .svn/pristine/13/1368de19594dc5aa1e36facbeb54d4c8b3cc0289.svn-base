package com.aiait.eflow.housekeeping.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import com.aiait.framework.common.DataType;
import com.aiait.framework.db.BaseDAOImpl;
import com.aiait.framework.db.IDBManager;
import com.aiait.framework.exception.DAOException;
import com.aiait.framework.vo.BaseVO;
import com.aiait.eflow.housekeeping.vo.HolidayVO;
import com.aiait.eflow.housekeeping.vo.ParamConfigVO;
import com.aiait.eflow.util.DataConvertUtil;
import com.aiait.eflow.util.FieldUtil;
import com.aiait.eflow.util.StringUtil;
    
	public class ParamConfigDAO extends BaseDAOImpl {
		
		public ParamConfigDAO(IDBManager dbManager){
			super(dbManager);
		}

		public int delete(BaseVO dataItem) throws DAOException {
			// TODO Auto-generated method stub
			return 0;
		}
		
		public ParamConfigVO getParamConfigVO(int ParamConfigId)throws DAOException{
			String sql = "select * from teflow_param_config where config_id='" + ParamConfigId + "'";
			Collection list = dbManager.query(sql);
			if(list==null || list.size()==0) return null;
			HashMap map = (HashMap)list.iterator().next();
			ParamConfigVO vo = new ParamConfigVO();
						
			vo.setConfigId(FieldUtil.convertSafeInt(map,"ID",-1));
			vo.setParamCode(FieldUtil.convertSafeString(map,"PARAM_CODE"));
			vo.setParamName(FieldUtil.convertSafeString(map,"PARAM_NAME"));
			vo.setParamValue(DataConvertUtil.convertISOToGBK(FieldUtil.convertSafeString(map,"PARAM_VALUE")));
			vo.setDescription(DataConvertUtil.convertISOToGBK(FieldUtil.convertSafeString(map,"DESCRIPTION")));
			vo.setOrderId(FieldUtil.convertSafeInt(map,"ORDER_ID",-1));
			
			return vo;
		}
		
		public int save(BaseVO dataItem) throws DAOException {
			ParamConfigVO model = (ParamConfigVO)dataItem;
			
			Collection paramList = new ArrayList();
			paramList.add(""+model.getConfigId());
			paramList.add(model.getParamCode());
			paramList.add(model.getParamName());
			paramList.add(model.getParamValue());
			paramList.add(model.getDescription());
			paramList.add(""+model.getOrderId());
			 
			dbManager.prepareCall("pots_param_config_save",paramList);

			// TODO Auto-generated method stub
			return 0;
		}
		
		public Collection search(String configId)throws DAOException {
			Collection result = new ArrayList();
			Collection paramList = new ArrayList();
			paramList.add(configId);
			String sql = "select * from teflow_param_config order by config_id";
			
			if(!"-1".equals(configId)){
				sql = sql  + "where config_id='" + configId + "'";
			}
			Collection list = dbManager.query(sql);
			Iterator iterator = list.iterator();
			while(iterator.hasNext()){
				ParamConfigVO model = new ParamConfigVO();
				HashMap map = (HashMap)iterator.next();
				model.setConfigId(Integer.parseInt((String)map.get("CONFIG_ID")));
				model.setParamCode((String)map.get("PARAM_CODE"));
				model.setParamName((String)map.get("PARAM_NAME"));
				model.setParamValue(DataConvertUtil.convertISOToGBK((String)map.get("PARAM_VALUE")));
				model.setDescription(DataConvertUtil.convertISOToGBK(FieldUtil.convertSafeString(map,"DESCRIPTION")));
				if(map.get("ORDER_ID")!=null){
				  model.setOrderId(Integer.parseInt((String)map.get("ORDER_ID")));
				}
				result.add(model);
			}
			list = null;
			paramList = null;
			return result;
		}

		public ParamConfigVO getParamByCode(String paramCode)throws DAOException {
			String sql = "select * from teflow_param_config where param_code='" + paramCode + "'";
			Collection list = dbManager.query(sql);
			if(list==null || list.size()==0) return null;
			HashMap map = (HashMap)list.iterator().next();
			ParamConfigVO vo = new ParamConfigVO();
						
			vo.setConfigId(FieldUtil.convertSafeInt(map,"ID",-1));
			vo.setParamCode(FieldUtil.convertSafeString(map,"PARAM_CODE"));
			vo.setParamName(FieldUtil.convertSafeString(map,"PARAM_NAME"));
			vo.setParamValue(DataConvertUtil.convertISOToGBK(FieldUtil.convertSafeString(map,"PARAM_VALUE")));
			vo.setDescription(DataConvertUtil.convertISOToGBK(FieldUtil.convertSafeString(map,"DESCRIPTION")));
			vo.setOrderId(FieldUtil.convertSafeInt(map,"ORDER_ID",-1));
			
			return vo;
		}
		
		public void updateValue(String[] configIdList,String[] valueList)throws DAOException {
		   for(int i=0;i<configIdList.length;i++){
			Collection paramList = new ArrayList();
			paramList.add(configIdList[i]);
			paramList.add(valueList[i]);
			dbManager.prepareCall("pots_param_config_upd",paramList);
		   }
		}

		public int update(BaseVO vo) throws DAOException {
			ParamConfigVO ParamConfig = (ParamConfigVO)vo;
			String updateSQL = "update teflow_param_config set param_value=?, description=? where config_id=?";
			Object[] obj = new Object[3];
			obj[0] = ParamConfig.getParamValue();
			obj[1] = ParamConfig.getDescription();
			obj[2] = ""+ParamConfig.getConfigId();
			int[] dataType = {DataType.VARCHAR,DataType.VARCHAR,DataType.INT};
			dbManager.executeUpdate(updateSQL,obj,dataType);
			return 0;
		}
		
		public int savePC(BaseVO vo) throws DAOException {
			ParamConfigVO ParamConfig = (ParamConfigVO)vo;
			String selSQL = "select * from teflow_param_config where config_id='"+ParamConfig.getConfigId()+"'";
			Collection list = dbManager.query(selSQL);
			if(list!=null && list.size()>0) return 1;
			String sql = "insert into teflow_param_config(param_code, param_name, param_value, description, order_id) values(?,?,?,?,?)";
			Object[] obj = new Object[5];
			obj[0] = ParamConfig.getParamCode();
			obj[1] = ParamConfig.getParamName();
			obj[2] = ParamConfig.getParamValue();
			obj[3] = ParamConfig.getDescription();
			obj[4] = ""+ParamConfig.getOrderId();
			int[] dataType = {DataType.VARCHAR,DataType.VARCHAR,DataType.VARCHAR,DataType.VARCHAR,DataType.INT}; 
			dbManager.executeUpdate(sql,obj,dataType);
			return 0;
		}
}
