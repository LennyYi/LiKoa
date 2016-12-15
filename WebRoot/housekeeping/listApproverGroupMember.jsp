<%@ include file="/common/head.jsp" %>
<%@ include file="/common/loading.jsp" %>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@page import="java.util.*,com.aiait.eflow.housekeeping.vo.ApproverGroupMemberVO" %>
<html>
<% 
  String groupName = (String)request.getParameter("groupName");
  String groupId = (String)request.getParameter("groupId");
  String groupType = (String)request.getParameter("groupType");
  Collection list = (ArrayList)request.getAttribute("memberList");
%>
<head>
<title>Manage Approver Group Member</title>
<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
  <script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
<script src="<%=request.getContextPath()%>/js/webtools.js" language="JavaScript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/calendar.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
<SCRIPT language="javascript" src="<%=request.getContextPath()%>/js/sorttable.js"></SCRIPT>
 <script language="javascript" src="<%=request.getContextPath()%>/js/ajax.js"></script>
<script language="javascript">
  function addForm(){
     //window.open('<%=request.getContextPath()%>/editForm.jsp?type=new','Base Form'height=300,width=385,toolbar=no,menubar=no,scrollbars=no,resizable=no,location=no,status=no); 
     var url = "<%=request.getContextPath()%>/approverGroupAction.it?method=enterMemberPage&groupId=<%=groupId%>&groupType=<%=groupType%>";
     var recdata=false;
     recdata = showModalDialog(url,window,'dialogWidth:500px; dialogHeight:400px;help:0;status:0;resizeable:1;');
     if(recdata==true){
       window.location.reload();
     }
  }
</script>
</head>

<body>
<form name="myForm" method="post">
   <table width="100%"  border="0">
     <tr class="tr1">
       <td align='center'><B><i18n:message key="housekeeping_approvergroup.listtitle"/> - <%=groupName%></B></td>
     </tr>
     <tr>
       <td align='left'>
       <%if(!"01".equals(groupId) && !"07".equals(groupId) && !"08".equals(groupId) && !"02".equals(groupId) && !"15".equals(groupId) && !"34".equals(groupId)){%>
        <input type="button" name="addBtn" value='<i18n:message key="housekeeping_approvergroup.editmember"/>' onclick="addForm()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">&nbsp;
        <%}%>
        <input type="button" name="backBtn" value='<i18n:message key="button.back"/>' onclick="javascript:window.history.go(-1)" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
       </td>
     </tr>
     <tr>
      <td>
        <table width="100%"  border="0" cellpadding="0" cellspacing="1" class=sortable id=mytable style="border:1px #8899cc solid;">
          <tr class="liebiao_tou">
           <%if("07".equals(groupId)){%>
             <td align='center'>System Name</td>
           <%}else if("08".equals(groupId)){%>
             <td align='center'>DB Name</td>
           <%}else{%>
            <td align='center'><i18n:message key="housekeeping_approvergroup.staffcode"/></td>
           <%}%>
            <td align='center'><i18n:message key="housekeeping_approvergroup.staffname"/></td>
          </tr>
          <%
           if(list!=null){
        	int i=1;
            Iterator listIt = list.iterator();
            while(listIt.hasNext()){
            	ApproverGroupMemberVO vo = (ApproverGroupMemberVO)listIt.next();
          %>
            <tr class="tr_change">
              <td><%=vo.getStaffCode()%>&nbsp;&nbsp;</td>
              <td><%=vo.getStaffName()%>&nbsp;&nbsp;</td>
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