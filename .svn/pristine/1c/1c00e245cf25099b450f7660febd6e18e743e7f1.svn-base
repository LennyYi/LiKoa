<%@ include file="/common/head.jsp" %>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@page import="com.aiait.eflow.formmanage.vo.FormSectionVO,java.util.*" %>
<%@page import="com.aiait.framework.i18n.*"%>
<%
   String formSystemId = (String)request.getParameter("formSystemId");
   Collection sectionList = (ArrayList)request.getAttribute("sectionList");
%>
<html>
<head>
 <title><i18n:message key="form_design.adjust_order"/></title>
 <link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
    <script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
 <script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
 <script language="javascript" src="<%=request.getContextPath()%>/js/aiaitjsframe.js"></script>
 <script language="javascript">
    function saveSectionOrder(){
        var param = $.Form("myForm").serialize({ regTime:new Date().toGMTString()}); //get all param value
      	var url = "<%=request.getContextPath()%>/formManageAction.it?method=saveAdjustSectionOrder&formSystemId=<%=formSystemId%>";
      	param = encodeURI(encodeURI(param)); //
        var myAjax = new Ajax.Request(
         url,
        {
            method:"post",      //
            parameters:param,   //
            setRequestHeader:{"If-Modified-Since":"0"},     //
            onComplete:function(x){    //
                var result = x.responseText;
                 if(result.Trim()=="success"){
                    alert("It is successful to set the order id.");
                    window.opener.location.reload();
                    window.close();
                 }else{
                    alert("Fail to set the order id");
                 }
            },
            onError:function(x){          //
                    alert('Fail to set the order id');
            } 
        } 
       ); 
    }
 window.onload=function(){resize(600);}
 </script>
 </head>
 <body>
 <form name="myForm" method="post">
  <table width="100%"  border="1" cellpadding="3" cellspacing="0" bordercolor="#6595D6" style="border-collapse:collapse;" >
    <%
      if(sectionList==null || sectionList.size()==0){
    %>
    <tr>
      <td><b><i18n:message key="form_design.form_has_no_sections"/></b></td>
    </tr>
    <%}else{ 
    	Iterator it = sectionList.iterator();
    	while(it.hasNext()){
    		FormSectionVO section = (FormSectionVO)it.next();
    		out.println("<tr>");
    		out.println("<td align='left'>"+I18NMessageHelper.getMessage("form_design.section_name")+":<b>"+section.getSectionRemark()+"</b></td><input type='hidden' name='sectionId_"+section.getSectionId()+"' value='"
    				    +section.getSectionId()+"'>");
    		out.print("<td>"+I18NMessageHelper.getMessage("form_design.order_id")+" :<input type='text' name='orderId_"+section.getSectionId()+"' ");
    		if(section.getOrderId()!=null){
    			out.println(" value='" + section.getOrderId() + "'>");
    		}else{
    			out.println(" value=''>");
    		}
    		out.println("</tr>");
    	}
    }
    %>
   <tr>
       <td align='center' colspan='2'>
         <input type='button' name='ok' value='<i18n:message key="button.submit"/>' onclick='saveSectionOrder()' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
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