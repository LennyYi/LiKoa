<%-- 
New Page: IT0973    Robin    12/26/2007 
--%>
<%@ include file="/common/head.jsp" %>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@ page import="java.util.*,com.aiait.eflow.util.*,com.aiait.eflow.util.StringUtil,com.aiait.eflow.formmanage.vo.*,com.aiait.eflow.common.helper.*"%>
<%@ page import="java.text.*" %>
<%
  Collection resultList = (ArrayList)request.getAttribute("resultList");
  FormManageVO form = (FormManageVO)request.getAttribute("form");
  String[] fieldArray = (String[])request.getAttribute("fieldArray");
  HashMap fieldMap = (HashMap)request.getAttribute("formFieldMap");
%>
<html>   
  <head>   
    <title>Export form list to excel</title>   
    <link href="<%=request.getContextPath()%>/css/style.css"
	rel="stylesheet" type="text/css">
	  <script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
	<SCRIPT language="javascript"
	src="<%=request.getContextPath()%>/js/sorttable.js"></SCRIPT>
<script language="javascript"
	src="<%=request.getContextPath()%>/js/resizeCol.js"></script>
	<script language="javascript"
	src="<%=request.getContextPath()%>/js/common.js"></script>
  </head>   
  <body>   
  <%if(fieldArray!=null && fieldArray.length>0){%>
    <table width="100%" border="0" cellpadding="0" cellspacing="1" 
	class=sortable id=mytable style="border:1px #8899cc solid;">  
      <tr class="liebiao_tou">   
       <%
          for(int i=0;i<fieldArray.length;i++)
         {
       %>
          <td align="center">
          <strong>
            <%
              HashMap field = (HashMap)fieldMap.get(fieldArray[i].toUpperCase());
              if(field!=null){
            	  out.print(DataConvertUtil.convertISOToGBK((String)field.get("FIELD_LABEL")));
            	  //System.out.println(DataConvertUtil.convertISOToGBK((String)field.get("FIELD_LABEL")));
              }else{
            	  out.print(fieldArray[i]);
              }
            %>
            </strong>
          </td>   
      <%}%>
      </tr>   
      <%
        if(resultList!=null && resultList.size()>0){
        	Iterator it = resultList.iterator();
        	int rowIndex = 1;
        	while(it.hasNext()){
        	  HashMap map = (HashMap)it.next();
        	  out.print("<tr class=\"tr_change\">");
        	  for(int i=0;i<fieldArray.length;i++){
      %>
        <td align="left">
          <%
            HashMap field = (HashMap)fieldMap.get(fieldArray[i].toUpperCase());
            if(i==0){
            	out.print("<a href='"+request.getContextPath()+"/formManageAction.it?method=displayFormContent&operateType=view&viewFlag=false&status=02&requestNo="+(String)map.get("REQUEST_NO")+"&formSystemId="+form.getFormSystemId()+"&openType=sub' >");
            }
            if((String)map.get(fieldArray[i].toUpperCase())!=null && !"".equals((String)map.get(fieldArray[i].toUpperCase()))){
            	if("REQUEST_STAFF_CODE".equals(fieldArray[i].toUpperCase()) || "PRJ_LD_ID".equals(fieldArray[i].toUpperCase()) || "SUBMIT_STAFF_CODE".equals(fieldArray[i].toUpperCase())){
            		out.print(DataConvertUtil.convertISOToGBK(StaffTeamHelper.getInstance().getStaffNameByCode((String)map.get(fieldArray[i].toUpperCase()))));
            	}else if("TEAM_CODE".equals(fieldArray[i].toUpperCase())){
            		out.print(StaffTeamHelper.getInstance().getTeamNameByCode((String)map.get(fieldArray[i].toUpperCase())));
            	}else if("REQUEST_DATE".equals(fieldArray[i].toUpperCase())){
      	    		SimpleDateFormat   df   =new   SimpleDateFormat("yyyy-MM-dd HH:mm:ss");   
                    java.util.Date   cDate   =   df.parse((String)map.get(fieldArray[i].toUpperCase()));  
                    out.print(StringUtil.getDateStr(cDate,"MM/dd/yyyy HH:mm:ss"));
            	}
            	else{
            	    String fieldType = (String)field.get("FIELD_TYPE");
            	    String fieldValue = (String)map.get(fieldArray[i].toUpperCase());
            	    if(fieldValue==null){
            	    	fieldValue = "";
            	    }
            	    if("7".equals(fieldType)){ // systemField
            	    	 FormSectionFieldVO vo = SystemFieldHelper.getInstance().getSystemFieldById(fieldArray[i]);
            	   		 Collection sysOpList = vo.getOptionList();
            	   		 if(sysOpList!=null && sysOpList.size()>0){
                			  Iterator it1 = sysOpList.iterator();
                			  while(it1.hasNext()){
                				DictionaryDataVO op = (DictionaryDataVO)it1.next();
                				if(fieldValue.equals(op.getId())){
             		               out.print(DataConvertUtil.convertISOToGBK(op.getValue()));
             		               break;
                				}
                			  }
               		 }else{
               			 out.print(fieldValue);
               		 }
            	   }else if("6".equals(fieldType)){ //checkbox
            		   BaseDataHelper dataHelper = BaseDataHelper.getInstance();
            		   Collection optionList = (ArrayList)dataHelper.getDetailMap().get((String)field.get("FORM_SYSTEM_ID")+"&"+(String)field.get("SECTION_ID")+"&"+fieldArray[i]);
            		   StringBuffer str = new StringBuffer("");
            		   if (optionList!=null && optionList.size()>0){
                  		 Iterator opIt = optionList.iterator();
                  		 while(opIt.hasNext()){
                      		DictionaryDataVO vo = (DictionaryDataVO)opIt.next();
                      		if(fieldValue.indexOf(vo.getId())>-1){
                      			str.append(vo.getValue()).append("  ");
                      		}
                  		 }
                  	  }else{
                  		 str.append(fieldValue);
                  	  }
            		  out.print(str.toString());
            	   }else if("4".equals(fieldType)){ //select
            		   BaseDataHelper dataHelper1 = BaseDataHelper.getInstance();
                       Collection selectOptionList = (ArrayList)dataHelper1.getDetailMap().get((String)field.get("FORM_SYSTEM_ID")+"&"+(String)field.get("SECTION_ID")+"&"+fieldArray[i]);
                       if (selectOptionList!=null && selectOptionList.size()>0){
                	    	Iterator opIt = selectOptionList.iterator();
                      	    while(opIt.hasNext()){
                      		 DictionaryDataVO vo = (DictionaryDataVO)opIt.next();
                      		 if(fieldValue.equals(vo.getId())){
                      			out.print(vo.getValue());
                      			break;
                      		 }
                      	    }
                	     }else{
                	    	 out.print(fieldValue);
                	     }
            	   }else if("3".equals(fieldType)){ //Date
            		   SimpleDateFormat   df   =new   SimpleDateFormat("yyyy-MM-dd HH:mm:ss");   
                       java.util.Date   cDate   =   df.parse((String)map.get(fieldArray[i].toUpperCase()));  
                       out.print(StringUtil.getDateStr(cDate,"MM/dd/yyyy HH:mm:ss"));
            	   }
            	    else{
            	      out.print(DataConvertUtil.convertISOToGBK((String)map.get(fieldArray[i].toUpperCase())));
            	      //System.out.println(DataConvertUtil.convertISOToGBK((String)map.get(fieldArray[i].toUpperCase())));
            	   }
            	}
            	
            }
          %>
        <%
        if(i==0){
        	out.print("</a>");
        }
        %>
        </td>   
     <%
        }
         out.print("</tr>");
         }
       }
     %>
    </table>   
    <%}%>
  </body>   
</html>   
