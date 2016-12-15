<%@ include file="/common/head.jsp" %>
<%@ include file="/common/loading.jsp" %>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@page import="java.util.*,com.aiait.eflow.housekeeping.vo.RoleVO,com.aiait.eflow.common.*" %>
<%@ taglib uri="/WEB-INF/purview.tld" prefix="purview" %>
<html>
<head>
<title>Manage Role</title>
  <script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
<script src="<%=request.getContextPath()%>/js/webtools.js" language="JavaScript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/calendar.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
<SCRIPT language="javascript" src="<%=request.getContextPath()%>/js/sorttable.js"></SCRIPT>
<script language="javascript" src="<%=request.getContextPath()%>/js/resizeCol.js"></script>
 <script language="javascript" src="<%=request.getContextPath()%>/js/ajax.js"></script>
<script language="javascript">
  function addForm(){
     //window.open('<%=request.getContextPath()%>/editForm.jsp?type=new','Base Form'height=300,width=385,toolbar=no,menubar=no,scrollbars=no,resizable=no,location=no,status=no); 
     var url = "<%=request.getContextPath()%>/roleAction.it?method=editRole&saveType=new";
     window.location = url;
  }
  
   function addRoleMember(roleId){
     var url = "<%=request.getContextPath()%>/roleAction.it?method=editRoleMember&roleId="+roleId;
     var recdata=false;
     recdata = showModalDialog(url,window,'dialogWidth:500px; dialogHeight:400px;help:0;status:0;resizeable:1;');
     if(recdata==true){
       //window.location.reload();
     }
  }
  
  function deleteForm(){
    if(checkSelect('roleId')<=0){
      alert("You have not selected any records to delete!");
      return;
    }
    if(confirm("Are you sure to delete the selected records")){
      document.forms[0].action = "<%=request.getContextPath()%>/roleAction.it?method=deleteRole";
      document.forms[0].submit();
    }
  }
</script>
</head>
<% 
  Collection list = (ArrayList)request.getAttribute("roleList");
%>
<body>
<form name="myForm" method="post">
<table border="0" width="100%" cellspacing="0" cellpadding="0">
 <tr>
 	<td height="10"></td>
 </tr><!--
 <tr>
 	<td>
 	  <strong><font color='#5980BB' family='Times New Roman'><i18n:message key="housekeeping_role.location"/></font></strong>
 	 </td>
 </tr>
--></table>
   <table width="100%"  border="0">
     <tr class="tr1">
       <td align='center'><B><i18n:message key="housekeeping_role.title"/></B></td>
     </tr>
     <tr>
       <td align='left'>
       <purview:purview moduleId='<%=ModuleOperateName.MODULE_AUTHORITY_MANAGE%>' operateId='<%=ModuleOperateName.OPER_AUTHORITY_MANAGE_ADD%>' isButton='true' labelValue='Add'>
        <input type="button" name="addBtn" value='<i18n:message key="button.add"/>' onclick="addForm()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
       </purview:purview>
       &nbsp;
      <purview:purview moduleId='<%=ModuleOperateName.MODULE_AUTHORITY_MANAGE%>' operateId='<%=ModuleOperateName.OPER_AUTHORITY_MANAGE_DELETE%>' isButton='true' labelValue='Delete'>
        <input type="button" name="deleteBtn" value='<i18n:message key="button.delete"/>' onclick="deleteForm()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
       </purview:purview>
       </td>
     </tr>
     <tr>
      <td>
        <table width="100%"  border="0" cellpadding="0" cellspacing="1" class=sortable id=mytable style="border:1px #8899cc solid;">
          <tr class="liebiao_tou">
            <td align='center' ><input type="checkbox" name="allBtn" onclick="selectAll(this,'roleId')"></td>
            <td align='center' ><i18n:message key="housekeeping_role.roleid"/></td>
            <td align='center' ><i18n:message key="housekeeping_role.rolename"/></td>
            <td align='center' ><i18n:message key="housekeeping_role.remark"/></td>
            <td align='center' ><i18n:message key="housekeeping_role.ausetting"/></td>
            <td align='center' ><i18n:message key="housekeeping_role.membersetting"/></td>
          </tr>
          <%
           if(list!=null){
        	int i=1;
            Iterator listIt = list.iterator();
            while(listIt.hasNext()){
            	RoleVO vo = (RoleVO)listIt.next();
          %>
            <tr class="tr_change">
              <td align='center' >
               <input type="checkbox" name="roleId" value="<%=vo.getRoleId()%>" <%=("00".equals(vo.getRoleId()))?"disabled":""%>>
              </td>
              <td >
              <%if("00".equals(vo.getRoleId())){%>
                <%=vo.getRoleId()%>
              <%}else{%>
              <purview:purview moduleId='<%=ModuleOperateName.MODULE_AUTHORITY_MANAGE%>' operateId='<%=ModuleOperateName.OPER_AUTHORITY_MANAGE_UPDATE%>' isButton='false' labelValue='<%=vo.getRoleId()%>'>
               <a href='<%=request.getContextPath()%>/roleAction.it?method=editRole&saveType=edit&roleId=<%=vo.getRoleId()%>'><%=vo.getRoleId()%></a>
              </purview:purview>
              <%}%>
              &nbsp;&nbsp;</td>
              <td ><%=vo.getRoleName()%>&nbsp;&nbsp;</td>
              <td ><%=vo.getRemark()%>&nbsp;&nbsp;</td>
              <td >
              <purview:purview moduleId='<%=ModuleOperateName.MODULE_AUTHORITY_MANAGE%>' operateId='<%=ModuleOperateName.OPER_AUTHORITY_MANAGE_PURVIEW%>' isButton='false' labelValue='Authority'>
               <a href="<%=request.getContextPath()%>/roleAction.it?method=preModifyRolePurview&roleId=<%=vo.getRoleId()%>&roleName=<%=vo.getRoleName()%>"><i18n:message key="housekeeping_role.authority"/></A>
              </purview:purview>
              </td>
              <td >
              <%if("00".equals(vo.getRoleId())){%>
                <i18n:message key="housekeeping_role.member"/>
              <%}else{%>
                <purview:purview moduleId='<%=ModuleOperateName.MODULE_AUTHORITY_MANAGE%>' operateId='<%=ModuleOperateName.OPER_AUTHORITY_MANAGE_MEMBER%>' isButton='false' labelValue='member'>
                 <a href="javascript:addRoleMember('<%=vo.getRoleId()%>')"><i18n:message key="housekeeping_role.member"/></a>
                </purview:purview>
              <%}%>
              &nbsp;&nbsp;</td>
            </tr>
          <%i++;}
          }%>
        </table>
      </td>
     </tr>
   </table>
</form>
</body>
</html>
   <script language="javascript">
	setResizeAble(mytable);
 </script>