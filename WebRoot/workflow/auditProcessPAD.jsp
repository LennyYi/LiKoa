<%-- 
Task_ID   Author   Modify_Date   Description
IT0958    Robin    11/02/2007    DS-006 When reject a form, current processor can select the reject to node 
--%>
<%@ include file="/common/head.jsp" %>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@ page contentType="text/html;charset=GBK" pageEncoding="GBK"%>
<%@page import="com.aiait.eflow.wkf.util.DataMapUtil,java.util.*,com.aiait.eflow.wkf.vo.WorkFlowItemVO,com.aiait.eflow.common.CommonName"%>
<%@page import="com.aiait.eflow.common.*,com.aiait.eflow.common.helper.*,com.aiait.eflow.housekeeping.vo.StaffVO"%>
<%
  String handleType = (String)request.getParameter("handleType");
  String requestNo = (String)request.getParameter("requestNo");
  String formSystemId = (String)request.getParameter("formSystemId");
  String currentNodeId = (String)request.getParameter("currentNodeId");
  String requestStaffCode = (String)request.getParameter("requestStaffCode");
  String updateSectionFields = (String)request.getParameter("updateSectionFields");
  String newSectionFields = (String)request.getParameter("newSectionFields");
  String contextPath = request.getContextPath();
  
  Collection processedNodeList = (ArrayList)request.getAttribute("processedNodeList");
  
  String formReturn = request.getParameter("formReturn");
  formReturn = formReturn == null ? "" : formReturn;
  //System.out.println("formReturn: " + formReturn);
%>
<html>
<head>
  <title>eFlow - AuditForm</title>
  <link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
  <script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
  <script language="javascript" src="<%=request.getContextPath()%>/js/aiaitjsframe.js"></script>
  <script language="javascript" src="<%=request.getContextPath()%>/js/dealform.js"></script>
  <script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
  <script language="javascript" src="<%=request.getContextPath()%>/js/ajax.js"></script>
  <script language="javascript">

  function submitForm() {
      var updateSectionFields = "<%=updateSectionFields%>";
      var newSectionFields = "<%=newSectionFields%>";
      
      //如果存在可以修改的sectionField或者只在该节点才可以输入的sectionField,则调用父窗口的函数进行保存
      if (updateSectionFields != "" || newSectionFields != "" ) {
          var result = parent.updateForm("<%=currentNodeId%>", true);
          if (result == true) {
              var handleType = "<%=handleType%>";
              if (handleType == "03" || handleType == "05") {
                  // Refresh whether it's the Last Node after save the form data.
                  handleType = parent.document.all.approveRejectType.value;
                  document.all.handleType.value = handleType;
              }
              doSubmit();
          }
      } else {
          doSubmit();
      }
  };
 
  function doSubmit() {
        document.all['searchBtn'].disabled = "true";
        var handleType = document.all.handleType.value;
        // alert("handleType: " + handleType);
        var comments = document.getElementsByName('handleComments')[0].value;       
        var rejectToNode = "";
        if(handleType=="04"){
          if(comments.Trim()==""){
            alert("Please fill in Remark!");
            document.getElementsByName('handleComments')[0].focus();
            document.getElementsByName('searchBtn')[0].disabled = "";
            return;
          }
          //IT0958 DS-006 Begin
          rejectToNode = document.getElementsByName('rejectToNode')[0].value;
          //IT0958 DS-006 End
          if(rejectToNode.Trim()==""){
            alert("Please choose a node you want to reject to!");
            document.getElementsByName('rejectToNode')[0].focus();
            document.getElementsByName('searchBtn')[0].disabled = "";
            return;
          }
        }
       
        if (handleType=="03"&&"<%=request.getAttribute("showSelectProcessor")%>"=="2" &&
        	document.all.<%=CommonName.NEXT_APPROVER_STAFF_CODE%>.value=="") {
          alert("Please select next approver!");
          location.reload(true);
          return;
        }
        
        if (!confirm("Are you sure to <%=DataMapUtil.convertHandleType(handleType).toLowerCase()%> the form?")) {
          document.getElementsByName('searchBtn')[0].disabled = "";
          return;
        }
        // comments = formatStringAllChar(comments);
        comments = encodeURIComponent(encodeURIComponent(comments));
        var url = "<%=request.getContextPath()%>/wkfProcessAction.it?method=auditForm&requestNo=<%=requestNo%>"
                  +"&formSystemId=<%=formSystemId%>&currentNodeId=<%=currentNodeId%>&requestStaffCode=<%=requestStaffCode%>"
                  +"&handleComments="+comments
                  +"&handleType="+handleType+"&rejectToNode="+rejectToNode
                  +"&nextApproverStaffCode="+document.getElementsByName('nextApproverStaffCode')[0].value+"&contextPath=<%=contextPath%>"
                  +"&formReturn=<%=formReturn%>";

        url = url+"&nonIE=1";
        document.forms[0].target = '_parent';
        document.forms[0].action = url;
        document.forms[0].submit();        
  };
  
   	window.onload=function(){resize(550);}
   	document.oncontextmenu = function() { return false;} 
  </script>
</head>

<body>
<form name="myForm" method="post" >
   <input type='hidden' name='requestUrl' value='<%=request.getContextPath()%>'>
   <input type="hidden" name="requestNo" value="<%=requestNo%>">
   <input type="hidden" name="formSystemId" value="<%=formSystemId%>">
   <input type="hidden" name="currentNodeId" value="<%=currentNodeId%>">
   <input type="hidden" name="requestStaffCode" value="<%=requestStaffCode%>">
   <input type="hidden" name="handleType" value="<%=handleType%>">
   <input type="hidden" name="textareaLimitLength">
   <TABLE WIDTH=100% bordercolor="#6595D6" style="border-collapse:collapse;"  BORDER=1 CELLPADDING=3 CELLSPACING=0 class="tr0" >
   <TR align="center" >
      <TD colspan="2" class="tr1">Audit Form - <%=DataMapUtil.convertHandleType(handleType)%></TD>
   </TR>
   <TR> 
      <TD width=15% class="tr3" align="right">Request No &nbsp;</TD>
      <TD width=35%> 
      <font size="2"> 
       <%=requestNo%>
      </font>
      </TD>
    </TR>
    <%
      //如果是“Reject(04)”操作，则可以选择需要Reject到曾经经过的哪个节点;如果没有选择，则默认Reject到第一个节点（Draft）
      if("04".equals(handleType)){
    %>
    <TR>
      <TD width=15% class="tr3" align="right">Reject To &nbsp; </TD>
      <TD width=30%>
        <select name="rejectToNode">
          <option value=""></option>
          <%
            if(processedNodeList!=null && processedNodeList.size()>0){
            	Iterator it = processedNodeList.iterator();
            	while(it.hasNext()){
            		WorkFlowItemVO node = (WorkFlowItemVO)it.next();
            		
            		if(currentNodeId.equals(node.getItemId())){
            			continue;		
            		}
            		if(node.getNodeAlias()!=null && !"".equals(node.getNodeAlias())){
            		  out.println("<option value='"+node.getItemId()+"'>"+node.getName()+"("+node.getNodeAlias()+")</option>");
            		}else{
            			out.println("<option value='"+node.getItemId()+"'>"+node.getName()+"("+node.getName()+")</option>");
            		}
            	}
            }
          %>
        </select>
        <input type="hidden" name="nextApproverStaffCode">
      </TD>
    </TR>
    <%}else if("1".equals(ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_CONFIG_SELECT_APPROVER_TYPE))){%>
    <TR>
      <TD width=15% class="tr3"> <i18n:message key="flow_auditpro.optionalapprover"/> </TD>
      <TD width=30% ><font size="2"> 
        <select name="nextApproverStaffCode">
         <option value=""></option>
         <%
            Collection staffList = StaffTeamHelper.getInstance().getStaffList();
            if(staffList!=null && staffList.size()>0){
            	Iterator it = staffList.iterator();
            	while(it.hasNext()){
            		StaffVO staff = (StaffVO)it.next();
            		out.print("<option value='" + staff.getStaffCode()+"'>" + staff.getStaffName()+"</option>");
            	}
            }
         %>
        </select>
      </TD>
    </TR>
    <%}else{%>
      <!-- <input type="hidden" name="nextApproverStaffCode"> -->
    <%}%>
<!--     <TR>
      <TD width=15% class="tr3"><div align="right"><span class="style1"><i18n:message key="flow_auditpro.attachment"/></span></div></TD>
      <TD width=30% ><font size="2"> 
        <input type="file" name="path" size='20'>
        <div style='display: inline' id='div_attachment'></div>
      </TD>      
    </TR> -->
    <TR>
      <TD width=15% class="tr3"><div align="right"><span class="style1">Remark &nbsp;</span></div></TD>
      <TD width=30% ><font size="2"> 
        <textarea  cols="40" rows="6" name="handleComments" onKeyDown="javascript:textCounter(this,document.getElementById('textareaLimitLength'),'500')"></textarea>
      </TD>
    </TR>
    <tr>
      <td colspan='4' align='center'>
         <input type="button" name="searchBtn" value='Submit' onclick='submitForm()'>&nbsp;&nbsp;
         <input type="Reset" name="resetBtn" value="Reset">
         <!-- input type="button" name="closeBtn" value='<i18n:message key="button.back"/>' onclick="parent.document.getElementById('audit').style.height=10px"> -->
      </td>
    </tr>
 </TABLE>
 <input type=hidden name="<%=CommonName.NEXT_APPROVER_STAFF_CODE%>" value="">
 </form>
</body>
</html>
<%
String selectFlag = (String)request.getAttribute("showSelectProcessor"); 
if("1".equals(selectFlag)||"2".equals(selectFlag)){ %>
<script type="text/javascript">
<!--
var retVal = showModalDialog('<%=request.getContextPath()%>/userManageAction.it?method=enterSelectStaff&pageTitle=Select Next Approver&showTips=form.select_approver_<%=selectFlag%>',
	 window,'dialogWidth:500px; dialogHeight:550px;help:0;status:0;resizeable:1;')
if(retVal!=undefined&&retVal!="undefined"){
	document.all.<%=CommonName.NEXT_APPROVER_STAFF_CODE%>.value = retVal;
}
//-->
</script>
<%}%>