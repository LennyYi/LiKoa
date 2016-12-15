package com.aiait.eflow.housekeeping.action;

import java.io.PrintWriter;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.aiait.eflow.common.CommonName;
import com.aiait.eflow.common.helper.ParamConfigHelper;
import com.aiait.eflow.housekeeping.dao.ParamConfigDAO;
import com.aiait.eflow.housekeeping.vo.ParamConfigVO;
import com.aiait.framework.db.DBManagerFactory;
import com.aiait.framework.db.IDBManager;
import com.aiait.framework.mvc.action.ActionLocation;
import com.aiait.framework.mvc.action.DispatchAction;
import com.aiait.framework.mvc.action.ModuleMapping;

public class ParamConfigAction extends DispatchAction{
	
	public ActionLocation listParamConfig(
			ModuleMapping mapping,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		String returnLabel = "listResult";
		IDBManager dbManager = null;
		try{
		  dbManager =  DBManagerFactory.getDBManager();
		  ParamConfigDAO dao = new ParamConfigDAO(dbManager);
		  Collection list = dao.search("-1");
		  request.setAttribute("resultList",list);
		}catch(Exception e){
			e.printStackTrace();
			returnLabel = "fail";
		}finally{
			if(dbManager!=null) dbManager.freeConnection();
		}
		return mapping.findActionLocation(returnLabel);
	}
	
	/*
	public ActionLocation deleteParamConfig()throws Exception{
	}*/
	
	public ActionLocation editParamConfig(
			ModuleMapping mapping,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		String returnLabel = "editPage";
		String saveType = (String)request.getParameter("saveType");
		if(saveType==null){
			saveType = "new";
		}
		/*
		if(!"".equals(saveType)){
			String ConfigId = (String)request.getParameter("configId");
			IDBManager dbManager = null;
			try{
			  dbManager =  DBManagerFactory.getDBManager();
			  ParamConfigDAO dao = new ParamConfigDAO(dbManager);
			  ParamConfigVO vo = dao.getParamConfigVO(ConfigId);
			  request.setAttribute("vo",vo);
			}catch(Exception e){
				e.printStackTrace();
				returnLabel = "fail";
			}finally{
				if(dbManager!=null) dbManager.freeConnection();
			}
		}*/
		return mapping.findActionLocation(returnLabel);
	}
	
	public ActionLocation saveParamConfig(
			ModuleMapping mapping,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		String returnLabel = "saveSuccess";
		String saveType = (String)request.getParameter("saveType");
		if(saveType==null){
			saveType = "new";
		}
		ParamConfigVO vo = new ParamConfigVO();
		
		IDBManager dbManager = null;
		try{
		  dbManager =  DBManagerFactory.getDBManager();
		  ParamConfigDAO dao = new ParamConfigDAO(dbManager);
		  if("new".equals(saveType)){
			  vo.setParamName((String)request.getParameter("paramName"));
		      vo.setParamValue((String)request.getParameter("paramValue"));
		      vo.setDescription((String)request.getParameter("description"));
			  vo.setParamCode((String)request.getParameter("paramCode"));
			  dao.savePC(vo);
		  }else{
			  String[] ConfigIds = request.getParameterValues("configId");
			  String[] ParamValue = request.getParameterValues("paramValue");
			  String[] Description = request.getParameterValues("description");
			  //String[] paramCode = request.getParameterValues("paramCode");
			  for(int i=0; i<ConfigIds.length; i++){
				  vo.setConfigId(Integer.parseInt(ConfigIds[i]));
				  vo.setParamValue(ParamValue[i]);
				  vo.setDescription(Description[i]);
				  //vo.setParamCode(paramCode[i]);
				  dao.update(vo);
			  }
		  }
		}catch(Exception e){
			e.printStackTrace();
			returnLabel = "fail";
		}finally{
			if(dbManager!=null) dbManager.freeConnection();
		}
		ParamConfigHelper.getInstance().refresh();
		return mapping.findActionLocation(returnLabel);
	}
	
	
	//************************
	//Brian Qiu 
	//Get Param for ajax
	public ActionLocation getParamByCodeforAJAX(ModuleMapping mapping,HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		String paramCode = (String) request.getParameter("paramCode");
		
		IDBManager dbManager = null;
		try {
			
			PrintWriter out = response.getWriter();
			if("dateFormat".equals(paramCode)){		
				out.write(ParamConfigHelper.getInstance().getParamValue(paramCode).toLowerCase().replace("mm", "MM"));
			}else{
				out.write(ParamConfigHelper.getInstance().getParamValue(paramCode));
			}
		    
			out.close();
		} catch (Exception e) {
			// TODO: handle exception
			request.setAttribute(CommonName.COMMON_ERROR_INFOR, e.getMessage());
			e.printStackTrace();
		} finally {
			if (dbManager != null)
				dbManager.freeConnection();
		}	
		
		return null;
	}
}

