<%@ page contentType="text/html;charset=GBK" pageEncoding="GBK"%>
<%@ page import="com.aiait.eflow.util.FileUtil,java.io.IOException" %>
<html>
<head>
 <title>Edit Event Function</title>
 <link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
 <script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
</head>
<body>
<table width="100%"  border="1" cellpadding="3" cellspacing="0" bordercolor="#6595D6" style="border-collapse:collapse;" >
  <tr class="liebiao_tou">
       <td align='center'>User Define Function Save</td>
  </tr>
  <tr>
   <td align='center'>
  <%
    String str = request.getParameter("userDefineFunction");
    String path=request.getRealPath("");//取得当前目录的路径 
    path = path + "\\js\\userdefine.js";    
    try{
      FileUtil.saveAs(path,str);
      out.print("<b><font color='green'>Successfully</font></b>");
    }catch(IOException e){
      e.printStackTrace();
      out.print("<font color='red'>Fail to save! Error is:<br><b>" + e+"</b></font>");
    }
  %>
   </td>
  </tr>
  <tr>
       <td colspan='2' align='center'>
         <input type='button' name='submitBtn' value='Close' onclick='javascript:window.close()' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
         <input type='button' name='closeBtn' value='Back' onclick='javascript:window.history.go(-1)' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
       </td>
     </tr>
  </table>
  </body>
</html>