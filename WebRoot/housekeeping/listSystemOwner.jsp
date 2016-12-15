<%@ include file="/common/head.jsp" %>
<%@ include file="/common/loading.jsp" %>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@page import="java.util.*,com.aiait.eflow.housekeeping.vo.SystemOwnerVO,com.aiait.eflow.common.helper.*,com.aiait.eflow.common.*" %>
<%@ taglib uri="/WEB-INF/purview.tld" prefix="purview" %>
<html>
<head>
<title>Manage System Owner</title>
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
     var url = "<%=request.getContextPath()%>/systemOwnerAction.it?method=editSystemOwner&saveType=new";
     window.location = url;
  }
  
  function deleteForm(){
    if(checkSelect('systemId')<=0){
      alert("You have not selected any records to delete!");
      return;
    }
    if(confirm("Are you sure to delete the selected records")){
      document.forms[0].action = "<%=request.getContextPath()%>/systemOwnerAction.it?method=deleteSystemOwner";
      document.forms[0].submit();
    }
  }
</script>
</head>
<% 
  Collection list = (ArrayList)request.getAttribute("resultList");
%>
<body>
<form name="myForm" method="post">
<table border="0" width="100%" cellspacing="0" cellpadding="0">
 <tr>
 	<td height="10"></td>
 </tr>
 <!--<tr>
 	<td>
 	  <strong><font color='#5980BB' family='Times New Roman'><i18n:message key="housekeeping_systemowner.location"/></font></strong>
 	 </td>
 </tr>
--></table>
   <table width="100%"  border="0">
     <tr class="tr1">
       <td align='center'><B><i18n:message key="housekeeping_systemowner.title"/></B></td>
     </tr>
     <tr>
       <td align='left'>
        <purview:purview moduleId='<%=ModuleOperateName.MODULE_SYSTEM_OWNER%>' operateId='<%=ModuleOperateName.OPER_SYSTEM_OWNER_ADD%>' isButton='true' labelValue='Add'>
         <input type="button" name="addBtn" value='<i18n:message key="button.add"/>' onclick="addForm()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
        </purview:purview>
        &nbsp;
       <purview:purview moduleId='<%=ModuleOperateName.MODULE_SYSTEM_OWNER%>' operateId='<%=ModuleOperateName.OPER_SYSTEM_OWNER_DELETE%>' isButton='true' labelValue='Delete'>
        <input type="button" name="deleteBtn" value='<i18n:message key="button.delete"/>' onclick="deleteForm()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
       </purview:purview>
       </td>
     </tr>
     <tr>
      <td>
        <table width="100%"  border="0" cellpadding="0" cellspacing="1" class=sortable id=mytable style="border:1px #8899cc solid;">
          <tr class="liebiao_tou">
            <td align='center' ><input type="checkbox" name="allBtn" onclick="selectAll(this,'systemId')"></td>
            <td align='center' ><i18n:message key="housekeeping_systemowner.systemid"/></td>
            <td align='center' ><i18n:message key="housekeeping_systemowner.systemname"/></td>
            <td align='center' ><i18n:message key="housekeeping_systemowner.systemowner"/></td>
          </tr>
          <%
           if(list!=null){
        	int i = 1;
            Iterator listIt = list.iterator();
            while(listIt.hasNext()){
            	SystemOwnerVO vo = (SystemOwnerVO)listIt.next();
          %>
            <tr class="tr_change">
              <td align='center' ><input type="checkbox" name="systemId" value="<%=vo.getSystemId()%>"></td>
              <td >
              <purview:purview moduleId='<%=ModuleOperateName.MODULE_SYSTEM_OWNER%>' operateId='<%=ModuleOperateName.OPER_SYSTEM_OWNER_UPDATE%>' isButton='false' labelValue='<%=vo.getSystemId()%>'>
                <a href='<%=request.getContextPath()%>/systemOwnerAction.it?method=editSystemOwner&saveType=edit&systemId=<%=vo.getSystemId()%>'><%=vo.getSystemId()%></a>
              </purview:purview>
              &nbsp;&nbsp;</td>
              <td ><%=vo.getSystemName()%>&nbsp;&nbsp;</td>
              <td ><%=StaffTeamHelper.getInstance().getStaffNameByCode(vo.getSystemStaffCode())%>&nbsp;&nbsp;</td>
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