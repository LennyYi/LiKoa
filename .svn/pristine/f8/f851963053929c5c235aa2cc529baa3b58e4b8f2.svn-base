package com.aiait.eflow.housekeeping.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import com.aiait.eflow.common.helper.ParamConfigHelper;
import com.aiait.eflow.housekeeping.vo.FormTypeVO;
import com.aiait.eflow.util.FieldUtil;
import com.aiait.framework.db.BaseDAOImpl;
import com.aiait.framework.db.IDBManager;
import com.aiait.framework.exception.DAOException;
import com.aiait.framework.vo.BaseVO;

public class FormTypeDAO extends BaseDAOImpl {
	
	public FormTypeDAO(IDBManager dbManager){
		super(dbManager);
	}

	public int delete(BaseVO arg0) throws DAOException {
		// TODO Auto-generated method stub
		return 0;
	}

	public int save(BaseVO arg0) throws DAOException {
		// TODO Auto-generated method stub
		FormTypeVO vo = (FormTypeVO)arg0;
		String sql1 = "insert into teflow_form_type(form_type_id,form_type_name,description) values('"
			        +vo.getFormTypeId()+"','"+vo.getFormTypeName()+"','"+vo.getDescription()+"') ";
		dbManager.executeUpdate(sql1);
		
		/******************** IT1177 New form type automation enhancement 2010/09 Begin ***********************/
		ParamConfigHelper param = ParamConfigHelper.getInstance();
		String[] formTypeModule = param.getParamValue("form_type_related_modules").split(";");
		
		for(int i=0;i<formTypeModule.length;i++){
			
			String sql2 = "select max(operate_id) V1 from teflow_module_operate where module_id = "+formTypeModule[i];
			Collection tempList = dbManager.query(sql2);
			
			Iterator it = tempList.iterator();
			int maxOprID = 0;
			if(it.hasNext()){
				HashMap map = (HashMap)it.next();
				maxOprID = Integer.parseInt((String) map.get("V1")) + 1;
			} else {
				continue;
			}
			
			String sql3 = "insert into teflow_module_operate(operate_id, module_id, operate_name, remark, form_type_id) " +
				"values("+maxOprID+", "+formTypeModule[i]+", '"+vo.getFormTypeName()+"', null, '"+vo.getFormTypeId()+"')";
			dbManager.executeUpdate(sql3);

			String sql4 = "insert into teflow_role_module_operate(role_id,module_id,operate_id) values(" +
					"'01',"+formTypeModule[i]+","+maxOprID+")";//grant authority to Admin first
			dbManager.executeUpdate(sql4);			
		}
		return 0;
	}

	public int update(BaseVO arg0) throws DAOException {
		// TODO Auto-generated method stub
		FormTypeVO vo = (FormTypeVO)arg0;
		String sql = "update teflow_form_type set form_type_name='" + vo.getFormTypeName()+"',description='"
		             +vo.getDescription()+"' where form_type_id='" + vo.getFormTypeId()+"' ";
		dbManager.executeUpdate(sql);
		return 0;
	}
	
	public FormTypeVO getFormType(String typeCode)throws DAOException{
		String sql = "select * from teflow_form_type where form_type_id = '" + typeCode + "' ";
		Collection tempList = dbManager.query(sql);
		if(tempList==null || tempList.size()==0) return null;
		HashMap map = (HashMap)tempList.iterator().next();
		return convertMapToVO(map);
	}
	
	public Collection getFormTypeList()throws DAOException{
		Collection list = new ArrayList();
		String sql = "select * from teflow_form_type where 1=1 order by form_type_id";
		Collection tempList = dbManager.query(sql);
		if(tempList==null || tempList.size()==0) return list;
		Iterator it = tempList.iterator();
		while(it.hasNext()){
			HashMap map = (HashMap)it.next();
			FormTypeVO vo = convertMapToVO(map);
			list.add(vo);
		}
		return list;
	}
	
	private FormTypeVO convertMapToVO(HashMap map){
		FormTypeVO vo = new FormTypeVO();
		vo.setFormTypeId(FieldUtil.convertSafeString(map,"FORM_TYPE_ID"));
		vo.setFormTypeName(FieldUtil.convertSafeString(map,"FORM_TYPE_NAME"));
		vo.setDescription(FieldUtil.convertSafeString(map,"DESCRIPTION"));
		return vo;
	}

}
