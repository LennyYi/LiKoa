package com.aiait.eflow.housekeeping.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import com.aiait.eflow.common.CommonName;
import com.aiait.eflow.housekeeping.vo.ModuleOperateVO;
import com.aiait.eflow.housekeeping.vo.ModuleVO;
import com.aiait.eflow.util.FieldUtil;
import com.aiait.framework.db.BaseDAOImpl;
import com.aiait.framework.db.IDBManager;
import com.aiait.framework.exception.DAOException;
import com.aiait.framework.vo.BaseVO;


public class ModuleDAO extends BaseDAOImpl {
	
	public Collection getLowerList(int moduleId,String roleId)throws DAOException{
		return query("lower",""+moduleId,roleId);
	}
	
	public ModuleVO getModule(int moduleId)throws DAOException{
	  Collection list = query("self",""+moduleId,"");
	  if(list.size()>0) 
	    return (ModuleVO)list.iterator().next();
	  else
	    return null;
    }
    
	public Collection query(String queryType,String moduleId,String roleId)throws DAOException{
		roleId = "'"+roleId.replaceAll(CommonName.MODULE_ROLE_SPLIT_SIGN, "','")+"'";
		String sql = "select * from teflow_module where 1=1 ";
		if("lower".equals(queryType)){
		  if(roleId!=null && !"".equals(roleId)){
			//sql = sql + "  and parent_id = " + moduleId + " and module_id in (select module_id  from teflow_role_module_operate where role_id='" + roleId + "' "
			//		+" union select parent_id from teflow_module where module_id in (select distinct module_id from teflow_role_module_operate where role_id='"+roleId+"')"
			sql = sql + "  and parent_id = " + moduleId + " and module_id in (select module_id  from teflow_role_module_operate where role_id in (" + roleId + ") "
			      +" union select parent_id from teflow_module where module_id in (select distinct module_id from teflow_role_module_operate where role_id in("+roleId+"))"
			      +") order by order_id";  
		  }else{
			sql = sql + " and parent_id = " + moduleId +"  order by order_id";
		  }
		}else if("self".equals(queryType)){
			sql = sql + " and module_id = "+moduleId+" order by order_id";
		}else{
			sql = sql + " order by parent_id,module_id,order_id";
		}
		
		Collection result = new ArrayList();
	
		Collection list = dbManager.query(sql);
		Iterator iterator = list.iterator();
		while(iterator.hasNext()){
			ModuleVO module = new ModuleVO();
			HashMap map = (HashMap)iterator.next();
			module.setModuleId(Integer.parseInt((String)map.get("MODULE_ID")));
			module.setModuleName((String)map.get("MODULE_NAME"));
			module.setParentId(Integer.parseInt((String)map.get("PARENT_ID")));
			module.setTargetUrl((String)map.get("TARGET_URL"));
			module.setRemark((String)map.get("REMARK"));
			module.setImageFileName((String)map.get("IMAGE_FILE_NAME"));
			if(map.get("ORDER_ID")!=null){
			  module.setOrderId(Integer.parseInt((String)map.get("ORDER_ID")));
		    }
			result.add(module);
	    }
	  return result;
	}
	
	public Collection getModuleAndOperateList()throws DAOException{
		Collection moduleList = query("",null,"");
		Collection result = new ArrayList();
		Iterator it = moduleList.iterator();
		while(it.hasNext()){
			ModuleVO module = (ModuleVO)it.next();
			Collection operateList = dbManager.query("select * from teflow_module_operate where module_id = " + module.getModuleId());
			if(operateList.size()>0){
				Collection list = new ArrayList();
				Iterator i = operateList.iterator();
				while(i.hasNext()){
					HashMap map = (HashMap)i.next();
					ModuleOperateVO model = new ModuleOperateVO();
					model.setModuleId(module.getModuleId());
					model.setOperateId(Integer.parseInt((String)map.get("OPERATE_ID")));
					model.setOperateName((String)map.get("OPERATE_NAME"));
					model.setRemark((String)map.get("REMARK"));
					model.setFormTypeId(FieldUtil.convertSafeString(map,"FORM_TYPE_ID"));
					list.add(model);
				}
				module.setOperateList(list);
			}
			result.add(module);
			
		}
		it = null;
		moduleList = null;
		return result;
	}
	
	
    public ModuleDAO(IDBManager dbManager){
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

}
