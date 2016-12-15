<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<html>
<head>
 <title>Setting Control Property</title>
 <link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
  <script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
</head>
<script language="javascript">
   var obj = window.dialogArguments;
   alert(obj.type)
   
   function closeWindow(){
     var returnObj = new Object();
     returnObj.fieldId = document.all['fieldId'].value;
     returnObj.required = document.all['required'].value;
     if(obj.type=='button' || obj.type=='label'){
       returnObj.labelValue = document.all['labelValue'].value;
     }
     window.returnValue = returnObj;
     window.close();
   }
</script>
<body>

  <table width="100%"  border="1" cellpadding="3" cellspacing="0" bordercolor="#6595D6" style="border-collapse:collapse;" >
   <tr>
     <td>
       Control Id(Field Id) : <input type='text' name="fieldId">
     </td>
   </tr>
   <tr>  
     <td>
       Is requried : <select name="required">
                      <option value='true'>Yes</option>
                      <option value='false'>No</option>
     </td>
     </tr>
     <% 
       String type = (String)request.getParameter("type");
       if("button".equals(type) || "label".equals(type)){
     %>
     <tr>
       <td>
          Label Value : <input type='text' name='labelValue'>
       </td>
     </tr>
     <%}%>
   <tr>
    <td colspan='2' align='center'>
      <input type='button' name='ok' value='Submit' onclick='closeWindow()'>
      <input type='reset' name='reset1' value='Reset'>
      <input type='button' name='close1' value='Close' onclick='window.close()'>
    </td>
   </tr>
  </table>

</body>
</html>