<%@ include file="/common/head.jsp" %>
<%@ include file="/common/loading.jsp" %>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@ page import="java.util.*,com.aiait.eflow.housekeeping.vo.*,com.aiait.eflow.wkf.util.DataMapUtil,com.aiait.eflow.common.*" %>
<%@ taglib uri="/WEB-INF/purview.tld" prefix="purview" %>

<html>
<head>
<title>Manage Approver Group</title>
<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
  <script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
<script src="<%=request.getContextPath()%>/js/webtools.js" language="JavaScript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/calendar.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
<SCRIPT language="javascript" src="<%=request.getContextPath()%>/js/sorttable.js"></SCRIPT>
<script language="javascript" src="<%=request.getContextPath()%>/js/resizeCol.js"></script>
 <script language="javascript" src="<%=request.getContextPath()%>/js/ajax.js"></script>
<script language="javascript">
  function addForm(){
     //window.open('<%=request.getContextPath()%>/editForm.jsp?type=new','Base Form'height=300,width=385,toolbar=no,menubar=no,scrollbars=no,resizable=no,location=no,status=no); 
     var url = "<%=request.getContextPath()%>/approverGroupAction.it?method=enterEditPage&saveType=new";
     window.location = url;
  }
  
  function deleteForm(){
    if(checkSelect('groupId')<=0){
      alert("You have not selected any records to delete!");
      return;
    }
    if(confirm("Are you sure to delete the selected records")){
      document.forms[0].action = "<%=request.getContextPath()%>/approverGroupAction.it?method=deleteApproverGroup";
      document.forms[0].submit();
    }
  }
</script>
</head>
<% 
  Collection list = (ArrayList)request.getAttribute("resultList");
  StaffVO currentStaff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
%>
<body>
<form name="myForm" method="post">
<table border="0" width="100%" cellspacing="0" cellpadding="0">
 <tr>
 	<td height="10"></td>
 </tr>
 <!--<tr>
 	<td>
 	  <strong><font color='#5980BB' family='Times New Roman'><i18n:message key="housekeeping_approvergroup.location"/></font></strong>
 	 </td>
 </tr>-->
</table>
   <table width="100%"  border="0">
     <tr class="tr1">
       <td align='center'><B><i18n:message key="housekeeping_approvergroup.title"/></B></td>
     </tr>
     <tr>
       <td align='left'>
       <purview:purview moduleId='<%=ModuleOperateName.MODULE_AUTHORIZED_GROUP%>' operateId='<%=ModuleOperateName.OPER_AUTHORIZED_GROUP_ADD%>' isButton='true' labelValue='Add'>
        <input type="button" name="addBtn" value='<i18n:message key="button.add"/>' onclick="addForm()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
       </purview:purview>  
        &nbsp;
       <purview:purview moduleId='<%=ModuleOperateName.MODULE_AUTHORIZED_GROUP%>' operateId='<%=ModuleOperateName.OPER_AUTHORIZED_GROUP_DELETE%>' isButton='true' labelValue='Delete'>
        <input type="button" name="deleteBtn" value='<i18n:message key="button.delete"/>' onclick="deleteForm()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
       </purview:purview>
       </td>
     </tr>
     <tr>
      <td>
        <table width="100%"  border="0" cellpadding="0" cellspacing="1" class=sortable id=mytable style="border:1px #8899cc solid;">
          <tr class="liebiao_tou">
            <td align='center' ><input type="checkbox" name="allBtn" onclick="selectAll(this,'groupId')"></td>
            <td align='center' ><i18n:message key="housekeeping_approvergroup.groupid"/></td>
            <td align='center' ><i18n:message key="housekeeping_approvergroup.groupname"/></td>
            <td align='center' ><i18n:message key="housekeeping_approvergroup.grouptype"/></td>
            <td align='center' ><i18n:message key="housekeeping_approvergroup.des"/></td>
            <td align='center' ><i18n:message key="housekeeping_approvergroup.groupmember"/></td>
          </tr>
          <%
           //01?11?????????????????
           if(list!=null){
        	int i = 1;
            Iterator listIt = list.iterator();
            boolean canDel = true;
            while(listIt.hasNext()){
            	ApproverGroupVO vo = (ApproverGroupVO)listIt.next();
            	if ("00".equals(vo.getGroupId()) || "01".equals(vo.getGroupId()) || "02".equals(vo.getGroupId())
            		|| "04".equals(vo.getGroupId()) || "06".equals(vo.getGroupId())
            		|| "07".equals(vo.getGroupId()) || "08".equals(vo.getGroupId())
            		|| "12".equals(vo.getGroupId())
            		|| "15".equals(vo.getGroupId())
            		|| "17".equals(vo.getGroupId()) || "18".equals(vo.getGroupId())
            		|| "0A".equals(vo.getGroupId())) {
            		canDel = false;
            	} else if (!"01".equals(currentStaff.getCurrentRoleId()) && 
                        ("03".equals(vo.getGroupId()) || "05".equals(vo.getGroupId()) || "09".equals(vo.getGroupId())
                        || "10".equals(vo.getGroupId()) || "11".equals(vo.getGroupId()) || "13".equals(vo.getGroupId())
                        || "14".equals(vo.getGroupId()) || "16".equals(vo.getGroupId()))) {
            	    canDel = false;
                } else {
            	    canDel = true;
            	}
          %>
            <tr class="tr_change">
              <td align='center' ><input type="checkbox" <%=canDel?"":"disabled"%> name="groupId" value="<%=vo.getGroupId()%>"></td>
              <td >
              <purview:purview moduleId='<%=ModuleOperateName.MODULE_AUTHORIZED_GROUP%>' operateId='<%=ModuleOperateName.OPER_AUTHORIZED_GROUP_UPDATE%>' isButton='false' labelValue='<%=vo.getGroupId()%>'>
                <a href='<%=request.getContextPath()%>/approverGroupAction.it?method=enterEditPage&saveType=edit&groupId=<%=vo.getGroupId()%>'><%=vo.getGroupId()%></a>
              </purview:purview>
              &nbsp;&nbsp;</td>
              <td ><%=vo.getGroupName()%>&nbsp;&nbsp;</td>
              <td ><%=vo.getGroupType()==null?"":DataMapUtil.convertApproverGroupName(vo.getGroupType())%>&nbsp;&nbsp;</td>
              <td ><%=vo.getDescription()%>&nbsp;&nbsp;</td>
              <td >
              <%if("00".equals(vo.getGroupId())){ %>
              Requestor
              <%}else if("0A".equals(vo.getGroupId())){ %>
              Submitter
              <%}else{ %>
              <purview:purview moduleId='<%=ModuleOperateName.MODULE_AUTHORIZED_GROUP%>' operateId='<%=ModuleOperateName.OPER_AUTHORIZED_GROUP_MEMBER%>' isButton='false' labelValue='list member'>
              <a href="<%=request.getContextPath()%>/approverGroupAction.it?method=listApproverGroupMember&groupId=<%=vo.getGroupId()%>&groupName=<%=vo.getGroupName()%>&groupType=<%=vo.getGroupType()%>"><i18n:message key="housekeeping_approvergroup.listmember"/></a>
              </purview:purview>
              <%} %>
              </td>
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