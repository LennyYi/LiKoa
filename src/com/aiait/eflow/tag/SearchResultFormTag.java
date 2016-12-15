package com.aiait.eflow.tag;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.aiait.eflow.common.helper.ParamConfigHelper;
import com.aiait.eflow.formmanage.dao.FormManageDAO;
import com.aiait.eflow.formmanage.vo.FormManageVO;
import com.aiait.eflow.formmanage.vo.FormSectionFieldVO;
import com.aiait.eflow.formmanage.vo.FormSectionVO;
import com.aiait.eflow.housekeeping.dao.ParamConfigDAO;
import com.aiait.eflow.util.StringUtil;
import com.aiait.framework.db.DBManagerFactory;
import com.aiait.framework.db.IDBManager;
import com.aiait.framework.i18n.I18NMessageHelper;

public class SearchResultFormTag extends TagSupport {
	private String formId; // refer to the teflow_form table;
	private String sectionId; // refer to the teflow_form_section table;
	private Map<String,Collection> data;
	private int columnNum = 3;
	private FormManageVO formFormat;//整个form的格式对象
	private int formSystemId;
	private String name;
	private String id;
	private String dateFormat;
	private String formatterFlag;
	
	/**
	 * 设置 formFormat（FormManageVO）对象，如果为空，则到数据库获取并放在ServletContext中
	 */
	private void setFromFormat(){
        IDBManager dbManager = null;
        try {
        	ServletContext application = this.pageContext.getServletContext();
        	formFormat = (FormManageVO)application.getAttribute(formId+"_formFormat");
        	dbManager = DBManagerFactory.getDBManager();
        	ParamConfigDAO paramConfigDao = new ParamConfigDAO(dbManager);
        	formatterFlag = paramConfigDao.getParamByCode("formatter_flag").getParamValue();
        	if(null == formFormat||formatterFlag.trim().toUpperCase()=="Y"){
        		dbManager = DBManagerFactory.getDBManager();
        		FormManageDAO formDao = new FormManageDAO(dbManager);
        		formSystemId = formDao.getFormSystemId(formId);
        		formFormat = formDao.getFormBySystemId(formSystemId);
        		application.setAttribute(formId+"_formFormat", formFormat);
        	}
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (dbManager != null)
                dbManager.freeConnection();
        }
	}
	/**
	 *  type1 ： 列表数据型式的表格；用调用jqgrid去初始化数据
	 * @param session
	 * @return
	 */
	private StringBuffer generateType1Table(FormSectionVO session){
		StringBuffer str = new StringBuffer("");
		String i18nStr =I18NMessageHelper.getMessage("common.export");
		String tableId = session.getSectionId()+"_table";
		String pagerId = session.getSectionId()+"_pager";
		String url = session.getSectionUrl();
		//数据库先插入国际化的key
		String caption = session.getSectionRemark();
		/*这里做好标题的国际化
		 * String i18nCaption =I18NMessageHelper.getMessage(cation);
		 * */
		if("Y".equalsIgnoreCase(session.getExport())){
			caption = caption + "<input type=button class=exportButton value="+i18nStr+"  onclick=export2Execl(\""+tableId+"\",\""+session.getSectionRemark()+"\")></input>";
		}
		str.append("<div class=inside_grid>");
		str.append("<table id='"+tableId+"'></table>");
		str.append("<div id='"+pagerId+"'></div>");
		str.append("<script language='javascript'>");
		
		
		Collection<FormSectionFieldVO> fieldList = session.getFieldList();
		List<String> columnNames = new ArrayList();
		String columnModel = "";
		List<ColumnModelVO> columnModelList = new ArrayList();
		JSONArray defaultFilterArray = new JSONArray();  
		
		int i = 0;
		for( FormSectionFieldVO field: fieldList){
			columnNames.add(field.getFieldLabel());
			ColumnModelVO columnModelVO = new ColumnModelVO();
			if(null == field.getFieldLabel() || "".equals(field.getFieldLabel())){
				columnModelVO.setHidden(true);
			}
			columnModelVO.setName(field.getFieldId().toUpperCase());
			columnModelVO.setIndex(field.getFieldId().toUpperCase());
			columnModelVO.setWidth(field.getColumnWidth());
			columnModelList.add(columnModelVO);
			/*if(!StringUtil.isEmptyString(field.getDefaultValue())){
				Map<String, String> defaultFilterValueMap = new HashMap<String,String>();
				defaultFilterValueMap.put("id",field.getFieldId().toUpperCase());
				defaultFilterValueMap.put("value",field.getDefaultValue());
				//defaultFilterList.add(defaultFilterValueMap);
				defaultFilterArray.element(defaultFilterValueMap);
			}*/
			if(!StringUtil.isEmptyString(field.getDefaultValue())){
				columnModelVO.setSearchoptions("{sopt:['cn','nc','eq','bw','bn','ew','en'],defaultValue:'"+field.getDefaultValue()+"'}");
			}else{
				columnModelVO.setSearchoptions("{sopt:['cn','nc','eq','bw','bn','ew','en']}");
			}
		}
		JSONArray modelArray = new JSONArray();  
		JSONArray nameArray = new JSONArray();   
		modelArray.addAll(columnModelList);   
		nameArray.addAll(columnNames); 
		
		
		
		Map<String, String[]> parasObjectMap = this.pageContext.getRequest().getParameterMap();
		Map<String, String> parasStringMap = new HashMap<String,String>();
		for(Map.Entry<String, String[]> entry : parasObjectMap.entrySet()){
			if(null != entry.getValue()){				
				parasStringMap.put(entry.getKey(),entry.getValue()[0]);
			}
		}
		JSONObject parametersJson = JSONObject.fromObject(parasStringMap);  

		  
		
		str.append("$(function(){ initJqGrid('"+url+"','"+tableId+"','"+pagerId+"',"+nameArray.toString()+","+modelArray.toString()+",'"+caption+"',"+parametersJson.toString()+","+defaultFilterArray.toString()+") })");
		str.append("</script></div>");
		return str;
	}

	/**
	 *  type3 ：标题竖放的只读型式表格
	 * @param session
	 * @return
	 */
	private StringBuffer generateType3Table(FormSectionVO session,Collection data){
		StringBuffer str = new StringBuffer("");
		str.append("<div class='baseInfo' >");
		str.append("<div>"+session.getSectionRemark()+"</div>");
		str.append("<table class=interval_color id='"+session.getSectionId()+"_table' width='98%'  border='1' cellpadding='3' cellspacing='0' bordercolor='#CDCDCD' style='border-collapse:collapse;margin:20px;'>");
		Collection<FormSectionFieldVO> fieldList = session.getFieldList();
		
		Iterator dataIt = data.iterator();
		Map map = new HashMap();
		if(dataIt.hasNext()){
			map = (Map)dataIt.next();
		}
		
		int i = 0;
		for( FormSectionFieldVO field: fieldList){
			i++;
			
			if(i%columnNum == 1 || field.getIsSingleRow() == 1){				
				str.append("<tr>");
			}
			str.append("<td class='tableLabel' width=13%>"+field.getFieldLabel()+"</td><td width=20%>"+getDisplayValue(field,map)+"</td>");
			if(i%columnNum == 0 || field.getIsSingleRow() == 1){				
				str.append("</tr>");
			}
			
		}
		str.append("</table></div>");
		return str;
	}
	
	private String getDisplayValue(FormSectionFieldVO field,Map valueMap){
		String value = "";
		if(2==field.getDataType()){ // date type
            try{
            	
            	dateFormat = ParamConfigHelper.getInstance().getParamValue("dateFormat", "MM/dd/yyyy");
            	SimpleDateFormat df = new SimpleDateFormat(dateFormat);
            	if(null != valueMap.get(field.getFieldId().toUpperCase()) && !"".equals(valueMap.get(field.getFieldId().toUpperCase()))){
            		Date date = df.parse(valueMap.get(field.getFieldId().toUpperCase()).toString());
            	}
            }catch(Exception e){
            	value = filterNull(valueMap.get(field.getFieldId().toUpperCase()));
            	e.printStackTrace();
            }
		}else{
			value = filterNull(valueMap.get(field.getFieldId().toUpperCase()));
		}
		return value;
	}
	
	private String filterNull(Object o){
		if(null == o || "null".equals(o.toString()))
			return "";
		else
			return o.toString();
	}
	
	public int doStartTag(){
		
		int processBodyOrNot = EVAL_BODY_INCLUDE;
		setFromFormat();
		Collection<FormSectionVO> sessionList = formFormat.getSectionList();
		FormSectionVO currentSession = null;
		StringBuffer strbuffer = new StringBuffer("");
		String i18nKey = formFormat.getFormName();
		String i18nStr  = I18NMessageHelper.getMessage(i18nKey);
		strbuffer.append("<div id='"+formFormat.getFormId()+"' class='rounded'><div class='levelBar rounded'>"+i18nStr+"</div>");
		for(FormSectionVO session : sessionList){
			if("01".equals(session.getSectionType())){
				strbuffer.append(generateType1Table(session));
			}else if("03".equals(session.getSectionType())){
				strbuffer.append(generateType3Table(session,data.get(session.getSectionId())));
			}
		}
		strbuffer.append("</div>");
		JspWriter out = pageContext.getOut();
		try {
			out.println(strbuffer.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return processBodyOrNot;
	}

	

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public void setName(String name) {
		this.name = name;
	}


	public void setId(String id) {
		this.id = id;
	}


	public void setFormId(String formId) {
		this.formId = formId;
	}


	public void setSectionId(String sectionId) {
		this.sectionId = sectionId;
	}


	public void setData(Map data) {
		this.data = data;
	}


	public void setColumnNum(int columnNum) {
		this.columnNum = columnNum;
	}
}
