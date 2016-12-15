<%@ include file="/common/head.jsp" %>
<%@ include file="/common/loading.jsp" %>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@page import="java.util.*,com.aiait.eflow.housekeeping.vo.ApproverGroupVO" %>

<%
   Collection hasApproverGroupList = (ArrayList)request.getAttribute("hasApproverGroupList") ;
   Collection noApproverGroupList = (ArrayList)request.getAttribute("noApproverGroupList") ; 
   String staffCode = (String)request.getParameter("staffCode");
%>
<html>
  <head>
    <title>Setting Staff's Approver Group</title>
      <script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
    <link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
    <link rel="stylesheet" type="text/css"
			href="<%=request.getContextPath()%>/css/loading.css" />
    <script language="javascript" src="<%=request.getContextPath()%>/js/ajax.js"></script>
    <script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
    
     <script language="javascript">
      function submitForm(){
         var optionValueStr = getTableSelectRecordStr("groupId","groupId");
         if(confirm("Are you sure to save the setting?")){
            var url = "<%=request.getContextPath()%>/userManageAction.it?method=saveApproverGroupSetting&staffCode=<%=staffCode%>";
            if(optionValueStr!=""){
              url = url + "&"+optionValueStr;
            }
             var  xmlhttp = createXMLHttpRequest();
             var result;
             if(xmlhttp){
               xmlhttp.open('GET',url,true);
               xmlhttp.onreadystatechange = function()
              {
                if(xmlHttp.readyState!=4){
             	   document.getElementById("loading").style.display="block";
                 }
                if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
                   result = xmlhttp.responseText;
                   document.getElementById("loading").style.display="none";
                   if(result.Trim()=="success"){
                      alert("Successfully saved the setting.")
                      window.close();
                   }else if(result.Trim()=="fail"){
                      alert("Fail to save the setting!");
                   }
                }
              }
               xmlhttp.setRequestHeader("If-Modified-Since","0");
               xmlhttp.send(null);
            }
         }
      }
      window.onload=function(){resize(450);}
     </script>
  </head>
  
  <body>
   <form name="optionForm" action="" method="post">
     <table width="100%"  border="1" cellpadding="3" cellspacing="0" bordercolor="#6595D6" style="border-collapse:collapse;" >
       <tr class="tr1">
         <td align='center' colspan='2'>
           <b><i18n:message key="housekeeping_approvergroup.mtitle"/></b>
         </td>
       </tr>
     <div id="loading" style="display:none;top:250px; left:40px;">
	   <div class="loading-indicator">
		 <i18n:message key="housekeeping_approvergroup.processing"/>...
	   </div>
     </div>
       <tr>
         <td>
           <% 
             int i = 1;
             if(hasApproverGroupList!=null && hasApproverGroupList.size()>0){
            	 Iterator it = hasApproverGroupList.iterator();
            	 while(it.hasNext()){
            		 ApproverGroupVO group = (ApproverGroupVO)it.next();
            		 out.println("<input type='checkbox' name='groupId' checked value='" + group.getGroupId()+"'>"+group.getGroupName()+"&nbsp;&nbsp;");
            		 if(i%2==0){
            			 out.println("<br>");
            		 }
            		 i++;
            	 }
             }
             if(noApproverGroupList!=null && noApproverGroupList.size()>0){
            	 Iterator it = noApproverGroupList.iterator();
            	 while(it.hasNext()){
            		 ApproverGroupVO group = (ApproverGroupVO)it.next();
            		 out.println("<input type='checkbox' name='groupId' value='" + group.getGroupId()+"'>"+group.getGroupName()+"&nbsp;&nbsp;");
            		 if(i%2==0){
            			 out.println("<br>");
            		 }
            		 i++;
            	 }
             }
           %>
         </td>
       </tr>
       <tr>
       <td align="center" colspan="2">
         <input type="button" name="smBtn" value='<i18n:message key="button.submit"/>' onclick="submitForm()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
         <input type="button" name="returnBtn" value='<i18n:message key="button.close"/>' onclick="javascript:window.close()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
       </td>
     </tr>
     </table>
   </form>
 
  </body>