<%@ include file="/common/head.jsp" %>
<%@page import="com.aiait.eflow.housekeeping.vo.StaffVO,com.aiait.eflow.common.CommonName"%>
<html>

<%
//只有administrator才有权限使用该功能
 StaffVO staff = (StaffVO)request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
 if(!"01".equals(staff.getCurrentRoleId())){
	 out.println("<font color='red'><b>You haven't authority for this function!</b></font>");
	 return;
 }
%>

<head>
<title>AIAIT E-Flow</title>
<link href="style.css" rel="stylesheet" type="text/css">
<script language="javascript" src="<%=request.getContextPath()%>/js/ajax.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
<script language="javascript">
   var xmlhttp = createXMLHttpRequest();
   function submitForm(type){
     if(document.forms[0].oldPassword.value.Trim()==""){
       alert("Original value can't be null");
       document.forms[0].oldPassword.focus();
       return;
     }
     var url = "<%=request.getContextPath()%>/encrypt.jsp?oldPassword="+document.forms[0].oldPassword.value+"&type="+type;
     xmlhttp.open("GET", url, true);
     xmlhttp.onreadystatechange=handleStateChange1;
     xmlhttp.setRequestHeader("If-Modified-Since","0");
     xmlhttp.send(null);	
   }
   
   
  function handleStateChange1(){
     if(xmlhttp.readyState == 4) {
        if(xmlhttp.status == 200) {
          var newValue = xmlhttp.responseText.Trim();
          if(newValue=="fail"){
             alert("Failure")
          }else{
             document.forms[0].newPassword.value = newValue;
          }
        }
     }
  }
</script>
<body>
  <form name="myForm" method='post' action="">
  <table>
    <tr>
     <td colspan='2' align='center'>Encrypt/Decrypt Password </td>
    </tr>
    <tr>
     <td>Original value :</td>
     <td><input type="password" name="oldPassword" length='20'></td>
    </tr>
    <tr>
     <td>New value :</td>
     <td><input type="text" name="newPassword" size="40"></td>
    </tr>
    <tr>
    <td colspan='2'>
    <input type="button" name="searchBtn" value="Encrypt" onclick="submitForm('01')" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">&nbsp;&nbsp;
    <input type="button" name="searchBtn" value="Decrypt" onclick="submitForm('02')" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">&nbsp;&nbsp;
    <input type="Reset" name="resetBtn" value="Reset" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
           </td>
    </tr>
  </table>
  </form>
</body>
</html>