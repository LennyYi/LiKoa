<%@ page contentType="text/html;charset=GBK" pageEncoding="GBK"%>
<%@ include file="/common/loading.jsp" %>
<%@ page import="java.util.*,java.io.*,com.aiait.eflow.util.FileUtil" %>
<html>
<head>
  <base target="_self">
 <title>Edit Event Function</title>
 <link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
 <script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
 <script language="javascript">
  function saveFunction(){
    if(confirm("Are you sure to save")){
       document.forms[0].action = "<%=request.getContextPath()%>/form/saveUserDefineFunc.jsp";
       document.forms[0].submit();
     }
  }
  
  window.onload=function(){resize(500);}
 </script>
</head>
<%
     StringBuffer str = new StringBuffer("");
     String path=request.getRealPath("");//取得当前目录的路径 
     path = path + "\\js\\userdefine.js";     
     String s = FileUtil.readfile(path,true);
     
%> 
<body>
<form name="functionForm" method='post'>
 <table width="100%"  border="1" cellpadding="3" cellspacing="0" bordercolor="#6595D6" style="border-collapse:collapse;" >
     <tr class="liebiao_tou">
       <td align='center'>Edit Event Function</td>
     </tr>
     <tr>
      <td>
      <textarea name='userDefineFunction' rows='23' cols='120'><%=s%></textarea>
      </td>
     </tr>
     <tr>
       <td colspan='2' align='center'>
         <input type='button' name='submitBtn' value='Submit' onclick='saveFunction()' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
         <input type='button' name='closeBtn' value='Close' onclick='javascript:window.close()' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
       </td>
     </tr>
  </table>
  </form>
</body>

</html>