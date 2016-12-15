<!-- Task_ID	Author	Modify_Date	Description---------------------->
<!-- IT0973		Young	 12/28/2007 For Project Management Function-->
<%@ include file="/common/head.jsp" %>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@page import="com.aiait.eflow.housekeeping.vo.ProjectVO,java.util.*,com.aiait.eflow.housekeeping.vo.StaffVO"%>
<html>
<head>
<title>Edit Project</title>
<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
 <script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/calendar.js"></script>
<SCRIPT language="javascript" src="<%=request.getContextPath()%>/js/sorttable.js"></SCRIPT>
<script language="javascript" src="<%=request.getContextPath()%>/js/ajax.js"></script>
<script language="javascript">
	
	function convertstr(str){		
		str = str.replace(/\'/g,"");
		str = str.replace(/\%/g,"");
        str = str.replace(/\&/g,"");
  		str = str.replace(/\?/g,"");
  		str = str.replace(/\#/g,"");
		return str;
	}		
	function submitForm(){						
		if(document.forms[0].beginDate.value.Trim()!=""){
        	if(isDate(document.forms[0].beginDate,"Submitted From")==false){
          		return;
        	}
      	}      	      	
		var frm = document.forms[0];
      	if(formValidate(frm)==false) return;
      	var projcode = convertstr(document.forms[0].projectCode.value.Trim());
      	if (projcode==null||projcode==""){
      		alert("project code cannot be null or you have input some Illegal character,such like # ? % '");
      		return;
      	}
	    var projectname = convertstr(document.forms[0].projectName.value.Trim());
      	var projectdesc = convertstr(document.forms[0].projectdesc.value.Trim());
      	var projleaderId = convertstr(document.forms[0].projectleader.value.Trim());      	
       	var url = "<%=request.getContextPath()%>/projectManageAction.it?method=saveProject&projectcode="+projcode
                +"&projectName="+projectname
                +"&projectdesc="+projectdesc
                +"&beginDate="+document.forms[0].beginDate.value.Trim()
                +"&projectleader="+projleaderId
                +"&type="+document.forms[0].saveType.value
                +"&status="+document.forms[0].status.value;
      	var  xmlhttp = createXMLHttpRequest();
      	var result;
      	if(xmlhttp){
        	xmlhttp.open('POST',url,false);
           	xmlhttp.onreadystatechange = function()
           	{
            	if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
                	result = xmlhttp.responseText;
             	}
           	}
           	xmlhttp.setRequestHeader("If-Modified-Since","0");
           	xmlhttp.send(null);
      	} 
      	if(result=="success"){
         	window.location = "<%=request.getContextPath()%>/projectManageAction.it?method=searchProject";
      	}else{
        	alert(result);
      	} 
	}	
</script>
</head>
<body>
<% 
	String saveType = (String)request.getParameter("type");
	Collection userList = (ArrayList)request.getAttribute("userList");
	ProjectVO pvo = null;
  	if(saveType==null){
		saveType = "new";
  	}
  	if("new".equals(saveType)){
		pvo = new ProjectVO();
  	}else{
		pvo = (ProjectVO)request.getAttribute("eflowproject");
  	}  	
%>
<form name="projectForm" action="" method="post">
  <input type="hidden" name="saveType" value="<%=saveType%>">
  <table width="100%"  border="1" cellpadding="3" cellspacing="0" bordercolor="#6595D6" style="border-collapse:collapse;" >   	 
  	 <tr class="tr1">
		<td align='center' colspan='4'><B><i18n:message key="housekeeping_project.title"/></B></td>
	</tr>
  	 <tr>
       <td align="right">
         <i18n:message key="housekeeping_project.projectcode"/> : 
       </td>
       <td>
        <input type='text' name='projectCode' size='40' maxLength='7' title="Project Code" required="true" value="<%=pvo.getPrj_code()==null?"":pvo.getPrj_code()%>" <%="edit".equals(saveType)?"disabled":""%>>
        (<font color='red'>*</font>)
       </td>
     </tr>
     <tr>
       <td align="right">
         <i18n:message key="housekeeping_project.projectname"/> :
       </td>
       <td>
        <input type='text' name='projectName' size='40' maxLength='20' title="Project Name" required="true" value="<%=pvo.getPrj_name()==null?"":pvo.getPrj_name()%>">
        (<font color='red'>*</font>)
       </td>
     </tr>
     <tr>
       <td align="right">
         <i18n:message key="housekeeping_project.des"/> : 
       </td>
       <td align="left">
         <input type='text' name='projectdesc' value="<%=pvo.getPrj_desc()==null?"":pvo.getPrj_desc()%>">
       </td>
     </tr>
     <tr>
       <td align="right">
         <i18n:message key="housekeeping_project.prjleaderid"/> : 
       </td>
       <td>
         <select name="projectleader">
           <option value="">NONE</option>
           <%
           		if(userList!=null&&userList.size()>0){
           			Iterator userIt = userList.iterator();
           			while(userIt.hasNext()){
           				StaffVO svo = (StaffVO)userIt.next();
           %>           
           <option value="<%=svo.getLogonId()%>" <%=(svo.getLogonId()!=null&&svo.getLogonId().equals(pvo.getPrj_ld_id()))?"selected":""%>><%=svo.getStaffName()%></option>
           <%
           			}
           		}
           %>           
         </select>
       </td>
     </tr>           
     <tr>
       <td align="right">
         <i18n:message key="housekeeping_project.prjstartdate"/> : 
       </td>
       <td>
			<INPUT Name="beginDate" onclick='setday(this)'  Type="text" value="<%=pvo.getPrj_start_date()==null?"":pvo.getPrj_start_date()%>" class="text2" style="WIDTH: 130px" id="beginDate">(mm/dd/yyyy)
       </td>
     </tr>     
     <tr>
       <td align="right">
         <i18n:message key="housekeeping_project.status"/> : 
       </td>
       <td>
         <select name="status">
           <option value="02" <%=(pvo.getStatus()!=null && "02".equals(pvo.getStatus())?"selected":"")%>><i18n:message key="housekeeping_project.active"/></option>
           <option value="10" <%=(pvo.getStatus()!=null && "10".equals(pvo.getStatus())?"selected":"")%>><i18n:message key="housekeeping_project.close"/></option>
         </select>
       </td>
     </tr>
     <tr>
       <td align="center" colspan="2">
         <input type="button" name="smBtn" value='<i18n:message key="button.submit"/>' onclick="submitForm()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
         <input type="button" name="returnBtn" value='<i18n:message key="button.back"/>' onclick="javascript:window.history.go(-1)" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
       </td>
     </tr>
  </table>
</form>
</body>
</html>