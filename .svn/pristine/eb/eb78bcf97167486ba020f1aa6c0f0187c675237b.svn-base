<%@ include file="/common/head.jsp" %>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@page import="com.aiait.eflow.reportmanage.vo.ReportSectionFieldVO,java.util.*" %>
<%@page import="com.aiait.framework.i18n.*"%>
<%
   String reportSystemId = (String)request.getParameter("reportSystemId");
   String sectionId = (String)request.getParameter("sectionId");
   Collection sectionFieldList = (ArrayList)request.getAttribute("sectionFieldList");
%>
<html>
<head>
 <title><i18n:message key="report_design.adjust_col_width"/></title>
 <link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
    <script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
 <script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
 <script language="javascript" src="<%=request.getContextPath()%>/js/ajax.js"></script>
 <script language="javascript">
    function saveSectionFieldOrder(){
       var formElements=document.forms["myForm"].elements;
       var returnString = "";
       for(var i=formElements.length-1;i>=0; --i ){
         returnString+="&" +escape(formElements[i].name)+"=" +escape(formElements[i].value);
       }  
       var url = "<%=request.getContextPath()%>/reportManageAction.it?method=saveAdjustColumnWidth&reportSystemId=<%=reportSystemId%>&sectionId=<%=sectionId%>"
                 +returnString;
       var  xmlhttp = createXMLHttpRequest();
       var result;
       if(xmlhttp){
           xmlhttp.open('POST',url,false);
           xmlhttp.onreadystatechange = function()
           {
             if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
                 result = xmlhttp.responseText;
                 if(result.Trim()=="success"){
                    alert("It is successful to set the columnes width.");
                    window.opener.location.reload();
                    window.close();
                 }else{
                    alert("Fail to set the columnes width");
                 }
             }
           }
           xmlhttp.setRequestHeader("If-Modified-Since","0");
           xmlhttp.send(null);
       } 
    }
 window.onload=function(){resize(600);}
 </script>
 </head>
 <body>
 <form name="myForm" method="post">
  <table width="100%"  border="1" cellpadding="3" cellspacing="0" bordercolor="#6595D6" style="border-collapse:collapse;" >
    <%
      if(sectionFieldList==null || sectionFieldList.size()==0){
    %>
    <tr>
      <td><b><i18n:message key="report_design.section_has_no_fields"/></b></td>
    </tr>
    <%}else{ 
    	Iterator it = sectionFieldList.iterator();
    	while(it.hasNext()){
    		ReportSectionFieldVO field = (ReportSectionFieldVO)it.next();
    		if(field.getFieldLabel()==null || "ID".equals(field.getFieldId().toUpperCase())){
    			continue;
    		}
    		out.println("<tr>");
    		out.println("<td align='left'>"+I18NMessageHelper.getMessage("report_design.column_label")+":<b>"+field.getFieldLabel()+"</b></td>");
    		out.print("<td>"+I18NMessageHelper.getMessage("report_design.column_width")+":<input type='text' name='columnWidth||"+field.getFieldId()+"' ");
    		out.println(" value='" + field.getColumnWidth() + "'>(%)");
    		out.println("</tr>");
    	}
    }
    %>
   <tr>
       <td align='center' colspan='2'>
         <input type='button' name='ok' value='<i18n:message key="button.submit"/>' onclick='saveSectionFieldOrder()' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
         <input type='Reset' name='reset1' value='<i18n:message key="button.reset"/>' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
         <input type='button' name='close1' value='<i18n:message key="button.close"/>' onclick='window.close()' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
       </td>
     </tr>
  </table>
  </form>
 </body>
</html>