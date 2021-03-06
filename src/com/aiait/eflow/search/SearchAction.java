package com.aiait.eflow.search;

//import java.io.PrintWriter;	
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import com.aiait.eflow.util.StringUtil;
import com.aiait.eflow.util.excel.ExcelWriterUtil;
import com.aiait.framework.db.DBManagerFactory;
import com.aiait.framework.db.IDBManager;
import com.aiait.framework.mvc.action.ActionLocation;
import com.aiait.framework.mvc.action.DispatchAction;
import com.aiait.framework.mvc.action.ModuleMapping;
import com.aiait.framework.util.CommonUtil;



public class SearchAction extends DispatchAction {
	
	

    public ActionLocation search(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response) throws Exception {

    	
		
    	String searchType = (String)request.getParameter("searchType");
    	String searchKey = (String)request.getParameter("searchKey");
    	searchKey = CommonUtil.decoderURL(searchKey);
    	
    	if(StringUtil.isEmptyString(searchType) || StringUtil.isEmptyString(searchKey)){
    		return null;
    	}
    	IDBManager dbManager = null;
    	try{     
	        dbManager = DBManagerFactory.getDBManager("compnwJNDI");
	        List<String> paramList = new ArrayList<String>();
	        paramList.add(searchType);
	        paramList.add(searchKey);
            Collection list = dbManager.prepareCall2("search_main", paramList);
            changeDisplayColor(list,searchKey);
	        JSONArray array = new JSONArray();    
	        array.addAll(list);  
	        printJson(response,array);
	    }catch(Exception e){  
	          e.printStackTrace();  
	    } finally {
	        if (dbManager != null)
	            dbManager.freeConnection();
	    }  
    	return null;
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	private boolean changeDisplayColor(Collection<Map> list,String searchKey){
    	boolean result = false;
    	if(null != list & !list.isEmpty()){
    		for(Map map :list){
    			String display = (String)map.get("DISPLAYCOLUMN");
    			if(null != display && !"".equals(display)){
    				display=replaceIgnoreCase(searchKey,display);
    				map.put("DISPLAYCOLUMN", display);
    			}
    		}
    	}
    	return result;
    }
     private String replaceIgnoreCase(String searchKey,String searchResult){
    	 String isearchKey = searchKey.toLowerCase();
    	 String isearchResult = searchResult.toLowerCase();
    	if(searchResult.contains(searchKey.toLowerCase())){
    		return searchResult.replaceFirst(searchKey.toLowerCase(), "<span style='color: red;'>"+searchKey.toLowerCase()+"</span>");
    	}else if(searchResult.contains(searchKey.toUpperCase())){
    		return searchResult.replaceFirst(searchKey.toUpperCase(), "<span style='color: red;'>"+searchKey.toUpperCase()+"</span>");
    	}else if(isearchResult.contains(isearchKey)){
    		return searchResult.replaceFirst(searchKey, "<span style='color: red;'>"+searchKey+"</span>");
    	}else{
    		return searchResult;
    	}
    }

    public ActionLocation getSearchDetailData(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response) throws Exception {

		Map<String,String[]> paramMap =  request.getParameterMap();
    	IDBManager dbManager = null;
    	try{     
	        dbManager = DBManagerFactory.getDBManager("compnwJNDI");
	        SearchDAO searchDAO = new SearchDAO(dbManager);
	        Map detailData = searchDAO.prepareSearchDetailData(paramMap);
	        request.setAttribute("searchDetailData", detailData);
	    }catch(Exception e){  
	        e.printStackTrace();  
	    } finally {
	        if (dbManager != null)
	            dbManager.freeConnection();
	    }  
    
    	return mapping.findActionLocation("searchResultDetail");
    }
    
    public ActionLocation getSearchDetailDataByPaging(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	IDBManager dbManager = null;
    	try{     
    		Map<String,String[]> paramMap =  request.getParameterMap();
	        dbManager = DBManagerFactory.getDBManager("compnwJNDI");
	        SearchDAO searchDAO = new SearchDAO(dbManager);
	        Collection list = searchDAO.getSearchDetailDataByPaging(paramMap);
	        JSONArray array = new JSONArray();    
	        array.addAll(list);  
	        printJson(response,array);
	       
	    }catch(Exception e){  
	          e.printStackTrace();  
	    } finally {
	        if (dbManager != null)
	            dbManager.freeConnection();
	    }  
    	return null;
    }

    private void printJson(HttpServletResponse response,JSONArray jsnoArray){
    	PrintWriter pw = null;
    	try {
	    	response.setContentType("application/json;charset=GBK"); //it is very important
			response.setHeader("Cache-Control", "no-cache");
			response.setHeader("Pragma", "no-cache");
	    	pw = response.getWriter();
	    	String jsonStr = jsnoArray.toString();
	    	pw.print(jsonStr);   
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
	        if (pw != null)
	        	pw.close();
		}
    	
    }
    
    public ActionLocation export2Execl(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	try{     
    		String rows = (String)request.getParameter("rows");
    		String colModels = (String)request.getParameter("colModels");
    		String colNames = (String)request.getParameter("colNames");
    		String fileName = (String)request.getParameter("fileName");
    		Map data = new HashMap();
    		data.put("rows", rows);
    		data.put("colModels", colModels);
    		data.put("colNames", colNames);
    		
    		ExcelWriterUtil excelWriterProxy = new ExcelWriterUtil(fileName);
    		File destFile = excelWriterProxy.setExcelData(data);
    		InputStream inputStream = new FileInputStream(destFile);

         
	        response.setContentType("multipart/form-data;charset=GBK");  //1.设置文件ContentType类型，这样设置，会自动判断下载文件类型 
	       // response.setContentType("application/json;charset=GBK"); 
	        response.setHeader("Content-Disposition", "attachment;charset=GBK;fileName="+destFile.getName());   //2.设置文件头：最后一个参数是设置下载文件名  
    		ServletOutputStream out = response.getOutputStream();
    		int b = 0;  
            byte[] buffer = new byte[1024];  
            while (true){  
                b = inputStream.read(buffer);  //4.写到输出流(out)中  
                if(b != -1){
                	out.write(buffer,0,b);
                }else{
                	break;
                }
            }  
            inputStream.close();  
            out.close();  
            out.flush();  

	    }catch(Exception e){  
	          e.printStackTrace();  
	    }
    	return null;
    }
    
    /*加一个快速响应的查询，只查询基本信息*/
    //跳转？ajax？
//   public ActionLocation getBaseSearch(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response){
//   	Map<String,String[]> paramMap =  request.getParameterMap();
//   	IDBManager dbManager = null;
//   	try{     
//	        dbManager = DBManagerFactory.getDBManager("compnwJNDI");
//	        SearchDAO searchDAO = new SearchDAO(dbManager);
//	        Map detailData = searchDAO.prepareSearchDetailData(paramMap);
//	        request.setAttribute("searchBaseData", detailData);
//	    }catch(Exception e){  
//	        e.printStackTrace();  
//	    } finally {
//	        if (dbManager != null)
//	            dbManager.freeConnection();
//	    }  
//   	return mapping.findActionLocation("searchMain");
//   }
    
}
