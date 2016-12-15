<%@ page contentType="text/html;charset=GBK" pageEncoding="GBK"%>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@page import="java.util.*,com.aiait.eflow.formmanage.vo.UploadFileVO,com.aiait.eflow.housekeeping.vo.StaffVO,com.aiait.eflow.common.CommonName" %>
<%@page import="com.aiait.framework.db.IDBManager,com.aiait.framework.db.DBManagerFactory,com.aiait.eflow.formmanage.dao.UploadDAO,com.aiait.eflow.util.FileUtil" %>
<script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
<% 
  String requestFormDate = (String)request.getParameter("requestFormDate");
  String requestNo = (String)request.getParameter("requestNo");
  String formSystemId = (String)request.getParameter("formSystemId");
  String sectionId = (String)request.getParameter("sectionId");
  String attachmentIdentity = (String)request.getParameter("attachmentIdentity");
  String fromMethod = (String)request.getParameter("method");
  String uploadUrl = "";
  if(requestNo==null || "".equals(requestNo)){//new form
	  requestNo = "";
	  uploadUrl = request.getContextPath()+"/uploadAction.it?method=uploadTempFile";
  }else{//modify form
	  uploadUrl = request.getContextPath()+"/uploadAction.it?method=uploadFormFile&formSystemId="+formSystemId
			      +"&sectionId="+sectionId+"&requestNo="+requestNo;
  }
%>
<html>
  <META HTTP-EQUIV="pragma" CONTENT="no-cache">
  <META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
  <META HTTP-EQUIV="expires" CONTENT="Wed, 26 Feb 1997 08:21:57 GMT">
 <link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
 <script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
 <script language="javascript" src="<%=request.getContextPath()%>/js/ajax.js"></script>
 <script language="javascript">
      function submitForm(){
        var comments = document.all['path'].value;       
        if(comments.Trim()==""){
          alert("Please select file!");
          document.all['path'].focus();
          return;
        }
        var comments = document.all['fileDescription'].value;       
        if(comments.Trim()==""){
          alert("Please fill in fileDescription!");
          document.all['fileDescription'].focus();
          return;
        }
        
        var fileDesc = document.all['fileDescription'].value.Trim();
        fileDesc = encodeURIComponent(encodeURIComponent(fileDesc));
        
        document.forms[0].action = "<%=uploadUrl%>&fileDescription=" + fileDesc
        + "&requestFormDate=<%=requestFormDate%>" + "&attachmentIdentity=<%=attachmentIdentity%>";
        document.forms[0].submit();
     }
     
     function deleteFile(){
        if(checkSelect('id')<=0){
          alert("You have not selected any records to delete!");
          return;
        }
       if(confirm("Are you sure to delete the selected records")){
         document.forms[1].action = "<%=request.getContextPath()%>/uploadAction.it?method=deleteFile&requestFormDate=<%=requestFormDate%>&requestNo=<%=requestNo%>"
         +"&attachmentIdentity=<%=attachmentIdentity%>";
         document.forms[1].submit();
       };
     }
   
   function download(fileName){
      var  xmlhttp = createXMLHttpRequest();
      var result = "";
      //fileName = formatStringAllChar(fileName);
      fileName = encodeURIComponent(encodeURIComponent(fileName));
      var url = "<%=request.getContextPath()%>/uploadAction.it?method=downLoad&fileName="+fileName;
     if(xmlhttp){
        xmlhttp.open('POST',url,false);
        xmlhttp.onreadystatechange = function()
        {
             if(xmlhttp.readyState == 4 && xmlhttp.status == 200){
                 result = xmlhttp.responseText;
             }
       }
       xmlhttp.setRequestHeader("If-Modified-Since","0");
       xmlhttp.send(null);
    }
    if(result.Trim()=="success"){
    }else{
      alert("File("+fileName+") not found !")
      return;
    }
  }
  <%if("1".equals(request.getSession().getAttribute("nonIE"))){%>
   function openFile(fileName){    
      fileName = encodeURIComponent(encodeURIComponent(fileName));
      var url = "<%=request.getContextPath()%>/upload/downloadPAD.jsp?fileName="+fileName;
      //url = url+"&nonIE=1";      
      //document.forms[0].action = url;
      //document.forms[0].submit();
      parent.document.getElementById("audit").contentWindow.document.location=url;
      parent.document.getElementById("audit").style.height = 900;
      alert("File will be opened in the page bottom");
    }
  <%} else {%>
  function openFile(fileName){
    //fileName = formatStringAllChar(fileName);
    fileName = encodeURIComponent(encodeURIComponent(fileName));
    var url = "<%=request.getContextPath()%>/upload/download.jsp?fileName="+fileName;
    window.open(url);
  }
  <%}%>
 </script>
<body style="margin:0; background:#F4F4F4">
<input type="hidden" id="attachmentId" name="attachmentIdentity" value="<%=attachmentIdentity%>">
<form enctype="multipart/form-data" method="post" action="">
<table width='100%'  border='1' cellpadding='3' cellspacing='0' bordercolor='#cdcdcd' style='border-collapse:collapse;'>
<tr>
<td align='right' class="tr3">
<i18n:message key="common.select_file"/>
</td>
<td align='left'>
<input type="file" name="path" size="60" />&nbsp;&nbsp;<input type="button"  id="upload" onclick='submitForm()' name="submitBtn" value="<i18n:message key="button.upload"/>">
</td>
</tr>
<tr>
<td align='right' class="tr3">
<i18n:message key="common.file_description"/>
</td>
<td align='left'>
<input type="text" name="fileDescription" size="60"/>
</td>
</tr>
<tr>
<td align='right' class="tr3">

</td>
<td align='left'>
( * <i18n:message key="common.reminder"/> : <i18n:message key="common.file_upload_reminder"/> )
</td>
</tr>
</table>
</form>
<form name="detailForm" method="post">
<input type="hidden" name="request">
<b><i18n:message key="common.uploaded_file"/></b>
<table id='formTable' width="100%"  border="1" cellpadding="3" cellspacing="0" bordercolor="#cdcdcd" style="border-collapse:collapse;">
   <tr class='tr2'>
      <td align='center'><input type="checkbox" name="allBtn" onclick="selectAll(this,'id')"></td>
      <td align='center'><i18n:message key="common.file_name"/></td>
      <td align='center'><i18n:message key="common.file_description"/></td>
  </tr>
  <% 
   StaffVO staff = (StaffVO) request.getSession().getAttribute(
			CommonName.CURRENT_STAFF_INFOR);
    
    IDBManager dbManager = null;  
    try{
      dbManager =  DBManagerFactory.getDBManager();
      UploadDAO dao = new UploadDAO(dbManager);
      Collection list = null;
      
      if(requestNo!=null && !"".equals(requestNo)){
			out.println("<input type='hidden' name='deleteFileType' value='modify'>");
			out.println("<input type='hidden' name='formSystemId' value='"+formSystemId+"'>");
			out.println("<input type='hidden' name='sectionId' value='"+sectionId+"'>");
			list = dao.getUploadedFileListByForm(requestNo,sectionId,Integer.parseInt(formSystemId));
      }else{
        UploadFileVO vo = new UploadFileVO();
        out.println("<input type='hidden' name='deleteFileType' value='new'>");
        vo.setStaffCode(staff.getStaffCode());
	    vo.setRequestFormDate(requestFormDate);
	    list = dao.getRequestFormAllFile(vo);
      }
      if(list!=null && list.size()>0){
        Iterator it = list.iterator();
        while(it.hasNext()){
    	  UploadFileVO temp = (UploadFileVO)it.next();	  
  %>
    <tr>
    <%if(requestNo!=null && !"".equals(requestNo)){%>
       <td align='center'><input type="checkbox" name="id" value="<%=temp.getId()%>"></td>
    <%}else{%>
       <td align='center'><input type="checkbox" name="id" value="<%=temp.getFileName()%>"></td>
    <%}%>
       <td align='left'><a href="javascript:openFile('<%=temp.getFileName()%>')" ><%=temp.getFileName()%>&nbsp;
       <%if(FileUtil.getUploadFile("/upload/requestform/"+temp.getFileName()).exists()){ %>
       <img src="<%=request.getContextPath()%>/images/expert_advised.jpg" border="0">
       <%}else{ %>
       <img src="<%=request.getContextPath()%>/images/urgent.gif" title="This file might need re-upload" border="0">
       <%} %>
       </a>&nbsp;</td>
       <td align='left'><%=temp.getFileDescription()%>&nbsp;&nbsp;</td>
    </tr>
  <%}
   }
  }catch(Exception e){
	out.println("Fail to get uploaded file list.");
    e.printStackTrace();
  }finally{
	  if(dbManager!=null) dbManager.freeConnection();
  }%>
  <tr>
   <td colspan='3'><input type="button" value="<i18n:message key="button.delete_fiel"/>" onclick="deleteFile()"></td>
  </tr>
  </table>
  </form>
</body>
</html>
