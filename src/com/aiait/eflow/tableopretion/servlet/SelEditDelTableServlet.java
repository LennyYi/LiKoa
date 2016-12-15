package com.aiait.eflow.tableopretion.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.aiait.eflow.tableopretion.dao.DatabaseDao;
import com.aiait.framework.db.ConnectionException;
import com.aiait.framework.db.DBManagerFactory;
import com.aiait.framework.db.IDBManager;

/**
 * Servlet implementation class SelEditDelTableServlet
 */
public class SelEditDelTableServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("null")
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String method = request.getParameter("method");
		 IDBManager dbManager = null;
		if ("col".equals(method)){
			try {
				dbManager = DBManagerFactory.getDBManager();
			DatabaseDao db = new DatabaseDao(dbManager);
			
			JSONObject jsonObject = new JSONObject();
			JSONArray jsonArray = new JSONArray();
			
			String sel = request.getParameter("selected");
			List<String> colname = db.getColumnsName(sel);
			int k = db.getColCount(sel);
			for (int i = 0; i < k; i++) {
				jsonObject.put("name", colname.get(i));
				jsonObject.put("index", colname.get(i));
				jsonObject.put("width", 120);// width: 1200,
				jsonObject.put("editable", true);
				jsonObject.put("edittype", "text");
				jsonObject.put("editoptions", "{size:20,maxlength:25}");
				jsonObject.put("editrules", "{required:true}");
				jsonObject.put("formoptions", "{elmprefix:'(*)'}");
				jsonArray.add(jsonObject);
			}
			// 自控制台打印输出，以检验json对象生成是否正确
			System.out.println("构造的colModel对象：\n" + jsonArray.toString());
			// 设置字符编码
			response.setCharacterEncoding("UTF-8");
			// 返回json对象（通过PrintWriter输出）
			response.getWriter().print(jsonArray);
		
			} catch (ConnectionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if("table".equals(method)){
			try {
				dbManager = DBManagerFactory.getDBManager();
			DatabaseDao db = new DatabaseDao(dbManager);
			
			String sel = request.getParameter("select");
			List<String> colname = db.getColumnsName(sel);
			String[] arr = (String[]) db.getTableName();
			request.setAttribute("datelist", 2);
			request.setAttribute("tablename", arr);
		
			request.getRequestDispatcher("/selEditDelTable.jsp").forward(request, response);
			} catch (ConnectionException e) {
				e.printStackTrace();
			}
			}
		else if("context".equals(method)){
			try {
			DatabaseDao db = new DatabaseDao(dbManager);
			
			JSONArray rows = new JSONArray();
			JSONArray rowss = new JSONArray();
			JSONObject jsonObj = new JSONObject();
			
			String page = request.getParameter("page");
			String rownum = request.getParameter("rows");
			String sele = "";
			sele = request.getParameter("select11");
			int records = db.getRecords(sele);
			int rowI =Integer.parseInt(rownum);
			int pageI = Integer.parseInt(page);
			int total = records%rowI != 0 ? records/rowI+1 : records/rowI;
			jsonObj.put("page",pageI);                // 当�?页
			jsonObj.put("total", total);        // 总页数
			jsonObj.put("records", records);   // 总记录数
			rows = (JSONArray) db.getDataTable(sele);
					
					
			int index = (pageI-1) * rowI;//开始记录数
			System.out.println("start count:"+index);
			int pageSize = Integer.parseInt(rownum);
			int rst = pageSize + index;
			for(int j = index ;j<rst && j < records;j++ ){
				rowss.add(rows.get(j));
			}
			jsonObj.put("rows", rowss);
	        response.setCharacterEncoding("UTF-8");
	        response.getWriter().print(jsonObj);
	        dbManager = DBManagerFactory.getDBManager();
			} catch (ConnectionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//start edit
		else if("edit".equals(method)){
			try {
				//取得下拉框中选中表名
				Map map = request.getParameterMap();
				Set key = map.keySet();
				int len = key.size();
				Set entrySet = map.entrySet();
				Iterator it2 = entrySet.iterator();  
				Map dataMap = new HashMap();
				List<String> list = new ArrayList<String>();
				StringBuffer sb=new StringBuffer();
				while(it2.hasNext()){  
					Map.Entry me = (Entry) it2.next();  
					Object k = me.getKey();
					//k.equals("id") == false 
					if(k.equals("id") == false && k.equals("table") == false && k.equals("oper") == false && k.equals("method") == false){
						String[] v = (String[])me.getValue();  
						dataMap.put(k, v);
						String str = k+" = '"+v[0]+"'";
						list.add(str);
					}
					int lenght = dataMap.size();
				}
				int i = 1;
				for(String item:list){
				   	sb.append(item);
				   	if(i<dataMap.size()){
				       	i++;
				      	sb.append(",");
		        	}
		        }
		        String updateStr = sb.toString();
		        String sql = "update users set "+sb.toString()+" where uid = '3'";
		        String str = String.valueOf(dataMap);
		        
				dbManager = DBManagerFactory.getDBManager();
				DatabaseDao db = new DatabaseDao(dbManager);
				
				String table = request.getParameter("table");
				db.updata(table, dataMap, updateStr);
			} catch (ConnectionException e) {
				e.printStackTrace();
			}
		}
		//edit end
		//add start
		else if("add".equals(method)){
			try {
				Map map = request.getParameterMap();
				Map dataMap = new HashMap<String, String>();
				List<String> keylist = new ArrayList<String>();
				List<String> vallist = new ArrayList<String>();
				StringBuffer ksb=new StringBuffer();
				StringBuffer vsb=new StringBuffer();
				int lenght = 0;
				Set entrySet = map.entrySet();
				Iterator it2 = entrySet.iterator();  
				while(it2.hasNext()){  
				    Map.Entry me = (Entry) it2.next();  
				    Object k = me.getKey();
				    if(k.equals("id") == false && k.equals("table") == false && k.equals("oper") == false && k.equals("method") == false){
				    	String[] v = (String[])me.getValue();  
				    	dataMap.put(k, v);
				    	String keystr = (String) k;
				    	String valstr = v[0];
				    	String str = k+" = '"+v[0]+"'";
				    	keylist.add(keystr);
				    	vallist.add(valstr);
				    }
				    lenght = dataMap.size();
				}
				int i = 1;
				for(String kitem:keylist){
				   	ksb.append(kitem);
				   	if(i<dataMap.size()){
				       	i++;
				       	ksb.append(",");
					}
				}
				int j =1;
				for(String vitem:vallist){
					vsb.append(vitem);
					if(j<dataMap.size()){
					   	j++;
					   	vsb.append(",");
					}
				}
				String addStr1 = ksb.toString();
				String addStr2 = vsb.toString();
				String table = request.getParameter("table");
				
				dbManager = DBManagerFactory.getDBManager();
				DatabaseDao db = new DatabaseDao(dbManager);
				db.add(table, addStr1, addStr2);
			} catch (ConnectionException e) {
				e.printStackTrace();
			}
		}
		//add end
		else if("del".equals(method)){
			try {
				List<String> list = new ArrayList<String>();
				Map map = request.getParameterMap();
				String table = request.getParameter("table");
				Set entrySet = map.entrySet();
				Iterator it2 = entrySet.iterator();
				String prinID = null ;
				while(it2.hasNext()){  
					Map.Entry me = (Entry) it2.next();  
					Object k = me.getKey();
					if(k.equals("prinKey")){
						String[] v = (String[]) me.getValue();
						prinID = v[0];
					}
					else if(k.equals("id") == false && k.equals("table") == false && k.equals("oper") == false && k.equals("method") == false)
					{
					String[] v = (String[])me.getValue();  
					String str = v[0];
					list.add(str);
					}
				}
				//String sql= "DELETE FROM policy WHERE pid IN "+(list.toString().replace("[", "(").replace("]", ")"));
				dbManager = DBManagerFactory.getDBManager();
				DatabaseDao db = new DatabaseDao(dbManager);
				db.delById(table,prinID,list);
			} catch (ConnectionException e) {
				e.printStackTrace();
			}
		}
		else if("key".equals(method)){
			try {
				dbManager = DBManagerFactory.getDBManager();
				DatabaseDao db = new DatabaseDao(dbManager);
				String table = request.getParameter("table");
				String key = db.getPrinKey(table);
				response.getWriter().print(key);
			} catch (ConnectionException e) {
				dbManager.freeConnection();
	            e.printStackTrace();
			}
		}
	}
}


