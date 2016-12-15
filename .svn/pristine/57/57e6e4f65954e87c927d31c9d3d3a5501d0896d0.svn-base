package com.aiait.eflow.formmanage.action;
/**
 * IT0977   Robin Hou       03/06/2008
 * IT1002   Robin Hou       04/16/2008 动态生成一行指定的section的空白记录
 */
import java.io.PrintWriter;	
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.aiait.eflow.common.CommonName;
//import com.aiait.eflow.common.helper.FieldControlHelper;
import com.aiait.eflow.common.helper.FormFieldHelper;
import com.aiait.eflow.common.helper.ParamConfigHelper;
import com.aiait.eflow.formmanage.dao.FormManageDAO;
//import com.aiait.eflow.formmanage.vo.FormManageVO;
import com.aiait.eflow.formmanage.vo.FormSectionFieldVO;
import com.aiait.eflow.housekeeping.vo.StaffVO;
import com.aiait.eflow.util.StringUtil;
import com.aiait.framework.db.DBManagerFactory;
import com.aiait.framework.db.IDBManager;
import com.aiait.framework.exception.DAOException;
import com.aiait.framework.mvc.action.ActionLocation;
import com.aiait.framework.mvc.action.DispatchAction;
import com.aiait.framework.mvc.action.ModuleMapping;
import com.aiait.framework.util.CommonUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class FormSectionAction extends DispatchAction {
    
	/**
	 * 动态生成一行指定的section的记录
	 * @param mapping
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionLocation getTableSectionField(
			ModuleMapping mapping,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		String formSystemId = (String)request.getParameter("formSystemId");
		String sectionId = (String)request.getParameter("sectionId");
		String sectionType = (String)request.getParameter("sectionType");
		String index = (String)request.getParameter("index");
		String nodeId = (String)request.getParameter("nodeId"); // nodeId=0表示在开始节点
		if(nodeId==null){
			nodeId = "0";
		}
		StaffVO staff = (StaffVO)request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);	
		if(index==null){
			index = "0";
		}
		if(sectionType==null || "".equals(sectionType)){
			sectionType = "01"; //table type
		}
		//response.setContentType("text/html;charset=GB2312"); //it is very important
		response.setContentType("text/html;charset=GBK"); //it is very important
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");
		PrintWriter out = response.getWriter();
		StringBuffer result = new StringBuffer("<input type='checkbox' name='chkid_"+sectionId+"' value=''><input type='hidden' name='"+sectionId+"_ID' value=''><td>");
		
		int rowIndex = Integer.parseInt(index);
		
		IDBManager dbManager = null;
		try{
		  dbManager =  DBManagerFactory.getDBManager();
		  
		  FormManageDAO formDao = new FormManageDAO(dbManager);
		  
		  String dateFormat = ParamConfigHelper.getInstance().getParamValue("dateFormat", "MM/dd/yyyy").toLowerCase().replace("mm", "MM");
		  
		  //FormManageVO form = formDao.getFormBySystemId(Integer.parseInt(formSystemId));
		  
		  HashMap onlyFillSectionFieldMap = formDao.getOnlyFillSectionFieldMap(Integer.parseInt(formSystemId));
		  
		  HashMap updateSectionFieldMap = formDao.getCanUpdateSectionFieldMap(Integer.parseInt(formSystemId));
		  
		  //if(onlyFillSectionFieldMap==null){
			//  onlyFillSectionFieldMap = new HashMap();
		  //}
		  //onlyFillSectionFieldMap.putAll(updateSectionFieldMap);
		  //System.out.println(onlyFillSectionFieldMap.size());
		  
		  Collection fieldList = formDao.getSectionFieldListByForm(Integer.parseInt(formSystemId),sectionId,sectionType);
		  if(fieldList==null){
			  out.print("fail");
			  return null;
		  }
		  
		  Iterator it = fieldList.iterator();
		  while(it.hasNext()){
			  FormSectionFieldVO field = (FormSectionFieldVO)it.next();
			  if(field.getFieldType()==CommonName.FIELD_TYPE_IDENTITY){
        		  continue;
        	  }
			  
			  if("0".equals(nodeId)){
			    if(onlyFillSectionFieldMap.containsKey((sectionId+"."+field.getFieldId()).toUpperCase())){
				  //result.append(FieldControlHelper.ShowDisabledField(field));
				  result.append(FormFieldHelper.ShowDisabledField(field));
			    }else{
				  //result.append(FieldControlHelper.createControl(field,sectionType,rowIndex));  
				  result.append(FormFieldHelper.showEditField(field,null,staff,null,rowIndex,dateFormat,Integer.parseInt(formSystemId),true));
			    }
			  }else{ //流转中的中间节点时
				 if(updateSectionFieldMap.containsKey((sectionId+"."+field.getFieldId()).toUpperCase()) || onlyFillSectionFieldMap.containsKey((sectionId+"."+field.getFieldId()).toUpperCase())){
					 result.append(FormFieldHelper.showEditField(field,null,staff,null,rowIndex,dateFormat,Integer.parseInt(formSystemId),true));
				 }else{
					 result.append(FormFieldHelper.ShowDisabledField(field));
				 }
			  }
			  result.append("<td>");
		  }
		  out.print(result.toString());
		}catch(DAOException e){
			e.printStackTrace();
			out.print("fail");
			return null;
		}finally{
			if(dbManager!=null) dbManager.freeConnection();
		}
		return null;
	}
	
	/**
	 * 公共获取xml格式的数据(调用传入的sql语句，返回其查询结果，该方法假设只有一条数据库查询记录返回，返回到客户端的是该记录的各个查询字段的值组成的xml格式数据）
	 * @param mapping
	 * @param request
	 * @param response
	 * @return 返回xml格式数据（按照查询sql中查询字段的先后顺序组成返回的xml数据）：
	 ex: select money1,money2 from table1 where col1=@staffCode and col2='test'
	   <resultList>
	     <result value='200'>200</result>         ---value of column 'money1'
	     <result value='300'>300</result>         ---value of column 'money2'
	   </resultList>
	 * @throws Exception
	 */
	public ActionLocation getXmlTextDataBySQL(
			ModuleMapping mapping,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		String sql = (String)request.getParameter("dataSQL");
		StaffVO staff = (StaffVO)request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);	
		//response.setContentType("text/xml;charset=GB2312"); //it is very important
		response.setContentType("text/xml;charset=GBK"); //it is very important
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");
		PrintWriter out = response.getWriter();
		IDBManager dbManager = null;
		//将sql中含有的系统参数变量，采用实际的值替换掉
        String[] paramName = new String[]{"@staffCode","@teamCode","@orgId"}; 
        String[] paramValue = new String[]{staff.getStaffCode(),staff.getTeamCode(),staff.getOrgId()};
        for(int i=0;i<paramName.length;i++){
        	sql = StringUtil.replace(sql,paramName[i],paramValue[i]);
        }
        sql = CommonUtil.decoderURL(sql);
		//StringBuffer resultXml = new StringBuffer("<?xml version=\"1.0\" encoding=\"GB2312\"?>");
        StringBuffer resultXml = new StringBuffer("<?xml version=\"1.0\" encoding=\"GBK\"?>");
		resultXml.append("<resultList>");
        try{
		  dbManager =  DBManagerFactory.getDBManager();
		  //运行需要查询数据的sql
		  Collection list = dbManager.query(sql); 
		  if(list!=null && list.size()>0){
			 Iterator it = list.iterator();
			 HashMap map = (HashMap)it.next();
		     if(map!=null){
				 //Object[] obj = map.values().toArray();
				 Object[] key = map.keySet().toArray();
				 if(key!=null && key.length>0){
		    	   for(int i=0;i<key.length;i++){
		    		 if(map.get(key[i])==null){
		    			 resultXml.append("<result value='"+key[i]+"'></result>"); 
		    		 }else{
		    		   if(map.get(key[i])!=null && !"".equals(map.get(key[i]))){
					     resultXml.append("<result value='").append(key[i]).append("'>")
				             .append(StringUtil.formatDouble(Double.parseDouble((String)map.get(key[i])))).append("</result>");
		    		   }else{
						     resultXml.append("<result value='").append(key[i]).append("'></result>");
		    		   }
					   //System.out.println(map.get(key[i]));
					   //System.out.println(StringUtil.formatDouble(Double.parseDouble((String)map.get(key[i]))));
		    		 }
		    	   }
				 }
				    
		     }
		  }
		  resultXml.append("</resultList>");
		  out.print(resultXml.toString());
		}catch(DAOException e){
			e.printStackTrace();
			out.print("fail");
			return null;
		}finally{
			if(out!=null) out.close();
			if(dbManager!=null) dbManager.freeConnection();
		}
		return null;
	}
	
    public ActionLocation getXmlDataListBySQL(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String sql = (String) request.getParameter("dataSQL");
        StaffVO staff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
        //response.setContentType("text/xml;charset=GB2312"); // it is very important
        response.setContentType("text/xml;charset=GBK"); // it is very important
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        PrintWriter out = response.getWriter();
        IDBManager dbManager = null;
        // 将sql中含有的系统参数变量，采用实际的值替换掉
        String[] paramName = new String[] { "@staffCode", "@teamCode", "@orgId" };
        String[] paramValue = new String[] { staff.getStaffCode(), staff.getTeamCode(), staff.getOrgId() };
        for (int i = 0; i < paramName.length; i++) {
            sql = StringUtil.replace(sql, paramName[i], paramValue[i]);
        }
        sql = CommonUtil.decoderURL(sql);
        //StringBuffer resultXml = new StringBuffer("<?xml version=\"1.0\" encoding=\"GB2312\"?>");
        StringBuffer resultXml = new StringBuffer("<?xml version=\"1.0\" encoding=\"GBK\"?>");
        resultXml.append("<resultlist>");
        try {
            dbManager = DBManagerFactory.getDBManager();
            // 运行需要查询数据的sql
            System.out.println(sql);            
            Collection list = dbManager.query(sql);
            if (list != null && list.size() > 0) {
                Iterator it = list.iterator();
                while (it.hasNext()) {
                    HashMap map = (HashMap) it.next();
                    if (map != null) {
                        Object[] key = map.keySet().toArray();
                        if (key != null && key.length > 0) {
                            resultXml.append("<item>");
                            for (int i = 0; i < key.length; i++) {
                                if (map.get(key[i]) == null || "".equals(map.get(key[i]))) {
                                    resultXml.append("<" + key[i] + "></" + key[i] + ">");
                                } else {
                                    resultXml.append("<" + key[i] + ">" + StringUtil.formatXML((String)map.get(key[i])) + "</" + key[i] + ">");
                                }
                            }
                            resultXml.append("</item>");
                        }
                    }
                }
            }
            resultXml.append("</resultlist>");
            //System.out.println("resultXml: " + resultXml);
            out.print(resultXml.toString());
        } catch (DAOException e) {
            e.printStackTrace();
            out.print("fail");
            return null;
        } finally {
            if (out != null)
                out.close();
            if (dbManager != null)
                dbManager.freeConnection();
        }
        return null;
    }
    
    public ActionLocation getJSONListBySQL(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response) throws Exception {
        
    	response.setContentType("application/json;charset=GBK"); //it is very important
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");
		
		String sql = (String) request.getParameter("dataSQL");
		String connectName = (String) request.getParameter("dataConnectName");
	    StaffVO staff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
		
	    IDBManager dbManager = null;
        // 将sql中含有的系统参数变量，采用实际的值替换掉
        String[] paramName = new String[] { "@staffCode", "@teamCode", "@orgId" };
        String[] paramValue = new String[] { staff.getStaffCode(), staff.getTeamCode(), staff.getOrgId() };
        
        PrintWriter pw = response.getWriter();
        
        for (int i = 0; i < paramName.length; i++) {
            sql = StringUtil.replace(sql, paramName[i], paramValue[i]);
        }
        
        connectName = CommonUtil.decoderURL(connectName);
        sql = CommonUtil.decoderURL(sql);
	    
	    try{  
	    	if(connectName=="")
	    		dbManager = DBManagerFactory.getDBManager();
	    	else
	    		dbManager = DBManagerFactory.getDBManager(connectName);
	    	
	        // 运行需要查询数据的sql
            System.out.println(sql);            
            Collection list = dbManager.query(sql);
            
	        JSONObject json = new JSONObject();    
	            
	        JSONArray array = new JSONArray();    

	        array.addAll(list);
	            
	        //json.put("account", account);    
	        json.put("jsonArray", array);    
	        
	        pw.print(array.toString());    
	        
	    }catch(Exception e){  
	          e.printStackTrace();   
	    } finally {
	        if (pw != null)
	        	pw.close();
	        if (dbManager != null)
	            dbManager.freeConnection();
	    }
	    return null;
    }
    
    public ActionLocation getJSONListBySP(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response) throws Exception {
        
    	response.setContentType("application/json;charset=GBK"); //it is very important
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");
		
		String connectName = (String) request.getParameter("dataConnectName");
		String spName = (String) request.getParameter("SPName");
		String spParameter = (String) request.getParameter("SPParameter");
		
		//StaffVO currentStaff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
		
		connectName = CommonUtil.decoderURL(connectName);
		spName = CommonUtil.decoderURL(spName);
		spParameter= CommonUtil.decoderURL(spParameter);
		
	    IDBManager dbManager = null;
	    String[] paramValue = null;
	    if(spParameter!=null)
	    {
	    	paramValue = spParameter.split(",");
	    }
	    
        PrintWriter pw = response.getWriter();
    	
    	try{     
    		if(connectName=="")
    			dbManager = DBManagerFactory.getDBManager();
    		else
    			dbManager = DBManagerFactory.getDBManager(connectName);
	        
	        Collection paramList = new ArrayList();
	        
	        if(paramValue!= null && paramValue.length>0)
	        {
	        	 for (int i = 0; i < paramValue.length; i++) {
	        		 paramList.add(paramValue[i]);
	             }
	        }
	        
            Collection list = dbManager.prepareCall2(spName, paramList);
            
	        JSONObject json = new JSONObject();    
	            
	        JSONArray array = new JSONArray();    

	        array.addAll(list);
	        
	        pw.print(array.toString());    
	        
	    }catch(Exception e){  
	          e.printStackTrace();  
	    } finally {
	        if (pw != null)
	        	pw.close();
	        if (dbManager != null)
	            dbManager.freeConnection();
	    }  
    	return null;
    }
	
	public static void main(String[] args){
		Collection list = new ArrayList();
		HashMap map = new HashMap();
		map.put("name","123");
		//map.put("t","231");
		list.add(map);
		HashMap map2 = new HashMap();
		map2.put("name","454");
		list.add(map2);
		Iterator it = list.iterator();
		while(it.hasNext()){
			HashMap temp = (HashMap)it.next();
			System.out.println(temp.values().toArray()[0]);
		}
	}

}
