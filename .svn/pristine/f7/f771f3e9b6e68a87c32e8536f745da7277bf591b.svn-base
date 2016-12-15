<%-- 
Task_ID   Author   Modify_Date   Description
IT0958    Robin    11/02/2007    DS-006 When reject a form, current processor can select the reject to node 
--%>
<%@ include file="/common/head.jsp" %>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@ page contentType="text/html;charset=GBK" pageEncoding="GBK"%>
<%@page import="com.aiait.eflow.wkf.util.DataMapUtil,java.util.*,com.aiait.eflow.wkf.vo.WorkFlowItemVO,com.aiait.eflow.common.CommonName"%>
<%@page import="com.aiait.eflow.common.*,com.aiait.eflow.common.helper.*,com.aiait.eflow.housekeeping.vo.StaffVO,com.aiait.eflow.formmanage.vo.DictionaryDataVO"%>
<%@page import="java.util.*,com.aiait.eflow.common.helper.FormTypeHelper,com.aiait.eflow.housekeeping.vo.FormTypeVO,com.aiait.eflow.basedata.vo.BaseDataVO,com.aiait.eflow.common.helper.BaseDataHelper" %>
<%@page import="com.aiait.eflow.basedata.vo.BaseDataVO,com.aiait.eflow.wkf.vo.WorkFlowProcessVO" %> 
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
  
  /************************************************************************/
  
  Collection formTypeList = BaseDataHelper.getInstance().getMasterList();
  int size = 1;
  if(formTypeList!=null && formTypeList.size()>0){
	   size = formTypeList.size() + 1 ;
  }
  DictionaryDataVO[] typeVos = new DictionaryDataVO[size];;
  typeVos[0] = new DictionaryDataVO();
  
  Collection list = BaseDataHelper.getInstance().getOptionList(10205, "");
 /************************************************************************/
 
  
%>

<html>
<head>
  <title>Other Information Form</title>
  <link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
  <script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
  <script language="javascript" src="<%=request.getContextPath()%>/js/aiaitjsframe.js"></script>
  <script language="javascript" src="<%=request.getContextPath()%>/js/dealform.js"></script>
  <script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
  <script language="javascript" src="<%=request.getContextPath()%>/js/ajax.js"></script>
  <script language="javascript" src="<%=request.getContextPath()%>/js/addOptions.js""></script>
  <script language="javascript"><!--

  var _integrityType, _integritySQL, _integrityField;
  function submitForm() {
      var updateSectionFields = "<%=updateSectionFields%>";
      var newSectionFields = "<%=newSectionFields%>";
      
      <%if(CompanyHelper.EFlow_AIA_CHINA.equals(CompanyHelper.getInstance().getEFlowCompany())){%>
	  	  try{
	  		  _integrityType = window.opener.checkIntegrityType();
		      _integritySQL = window.opener.checkIntegritySQL("<%=requestNo%>");
		      _integrityField = window.opener.checkIntegrityField();
	  	  }catch(e){}
      <%}%>
      
      //如果存在可以修改的sectionField或者只在该节点才可以输入的sectionField,则调用父窗口的函数进行保存
      if (updateSectionFields != "" || newSectionFields != "" ) {
          if (confirm("确认后,系统将提交你的申请!")) {
              var result = window.opener.updateForm("<%=currentNodeId%>", true);
              if (result == false) {
                  window.close();
                  return;
              }
		      setTimeout(countTime, 500);
          }
      } else {
          doSubmit();
      }
  };

  var x = 0;

  function countTime() {
      x++;
      if (window.opener.document.readyState != "complete" && x <= 20) {
          setTimeout(countTime, 500);
      } else if (x > 20) {
    	  return;//超时，为保证正确性停止所有操作。
      } else {
          if (window.opener.document.title == "Error Display") {
              alert("Operate Error");
              window.close();
              return;
          }
          // alert("WaitTime: " + x);
          var handleType = "<%=handleType%>";
          if (handleType == "03" || handleType == "05") {
              // Refresh whether it's the Last Node after save the form data.
              handleType = window.opener.document.all.approveRejectType.value;
              document.all.handleType.value = handleType;
          }
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
                
        <%if(CompanyHelper.EFlow_AIA_CHINA.equals(CompanyHelper.getInstance().getEFlowCompany())){%>
        	try{
	        	for(i=0;i<_integrityType.length;i++){
		        	if (_integrityType[i] == handleType && _integrityField[i] == false) { //editable for current processor
		   			    var sql = _integritySQL[i];
					    var xmlObj = selectFromSQL(sql, 2);
		   			    if (xmlObj) {
		   			      var items = xmlObj.getElementsByTagName("item");
		   			      if(items&&items.length>0){
		            		if(!confirm(checkIntegrityFailed)){
		              			window.close();
		   		               	return;
		   	        		}
		   			      }
		   			    }
		   			}
	        	}
        	}catch(e){}
        <%}%>
        
        if (handleType=="03"&&"<%=request.getAttribute("showSelectProcessor")%>"=="2" &&
        	document.all.<%=CommonName.NEXT_APPROVER_STAFF_CODE%>.value=="") {
          alert("Please select next approver!");
          location.reload(true);
          return;
        }
        
        //if (!confirm("Are you sure to <%=DataMapUtil.convertHandleType(handleType).toLowerCase()%> the form?")) {
        //  document.getElementsByName('searchBtn')[0].disabled = "";
        //  return;
        //}
        // comments = formatStringAllChar(comments);
        comments = encodeURIComponent(encodeURIComponent(comments));
         var url = "<%=request.getContextPath()%>/wkfProcessAction.it?method=auditForm&requestNo=<%=requestNo%>"
                  +"&formSystemId=<%=formSystemId%>&currentNodeId=<%=currentNodeId%>&requestStaffCode=<%=requestStaffCode%>"
                  +"&handleComments="+comments
                  +"&handleType="+handleType+"&rejectToNode="+rejectToNode
                  +"&nextApproverStaffCode="+document.getElementsByName('nextApproverStaffCode')[0].value+"&contextPath=<%=contextPath%>"
                  + "&formReturn=<%=formReturn%>";
        //alert(url)
        //return;
        document.forms[0].action = url;
        document.forms[0].submit();
  };
  
   	window.onload=function(){resize(550);}
   	document.oncontextmenu = function() { return false;} 
   	
   	
   	//下拉框
   	
   
			function messageReveal(id) {
				var messageindex = id.selectedIndex;
				var val = id.options[messageindex].value;
				document.myForm.handleComments.value = val;

			}

			-->
		</script>
</head>

<body>
<form enctype="multipart/form-data" name="myForm" method="post" >
   <input type='hidden' name='requestUrl' value='<%=request.getContextPath()%>'>
   <input type="hidden" name="requestNo" value="<%=requestNo%>">
   <input type="hidden" name="formSystemId" value="<%=formSystemId%>">
   <input type="hidden" name="currentNodeId" value="<%=currentNodeId%>">
   <input type="hidden" name="requestStaffCode" value="<%=requestStaffCode%>">
   <input type="hidden" name="handleType" value="<%=handleType%>">
   <input type="hidden" name="textareaLimitLength">
   <TABLE WIDTH=100% bordercolor="#6595D6" style="border-collapse:collapse;"  BORDER=1 CELLPADDING=3 CELLSPACING=0 class="tr0" >
   <TR align="center" >
      <TD colspan="2" class="tr1">Other Information- <%=DataMapUtil.convertHandleType(handleType)%></TD>
   </TR>
   <TR> 
      <TD width=15% class="tr3"><div align="right"><span class="style1"><i18n:message key="flow_auditpro.requestno"/></span></div></TD>
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
      <TD width=15% class="tr3"><div align="right"><span class="style1"><i18n:message key="flow_auditpro.rejecttonode"/></span></div></TD>
      <TD width=30%>
        <select name="rejectToNode">
          <option value=""></option>
          <%
            if(processedNodeList!=null && processedNodeList.size()>0){
            	Iterator it = processedNodeList.iterator();
            	while(it.hasNext()){
            		WorkFlowItemVO node = (WorkFlowItemVO)it.next();
            		WorkFlowProcessVO processVo = new WorkFlowProcessVO(); 
            		if(currentNodeId.equals(node.getItemId())){
            		     String[] toStaff = null;
            			 toStaff = new String[]{processVo.getSubmittedBy()};
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
    
     <TR>
      <TD width=15% class="tr3"><div align="right"><span class="style1"><i18n:message key="flow_auditpro.rejectreason"/></span></div></TD>
      <TD width=30%>
       <select name="messagePick" OnChange="messageReveal(this)">
       
         <!--  <option value="">请选择</option> -->
          <!-- <option value="aaa">aaa</option>
          <option value="bbb">bbb</option>
          <option value="ccc">ccc</option>
          <option value="ddd">ddd</option>
          <option value="eee">eee</option>
          <option value="fff">fff</option> -->
          
              <option value=""><i18n:message key="flow_auditpro.reject_choices"/></option>
            <%
            if(list!=null && list.size()>0){
         	   Iterator it = list.iterator();
         	   while(it.hasNext()){
         		  DictionaryDataVO typeVo = (DictionaryDataVO)it.next();
         		   out.println("<option value='" + typeVo.getId()+"'>"+typeVo.getValue()+"</option>");
         	   }
            }
            %>
          
   
        </select>
       <!--  <input type="hidden" name="nextApproverStaffCode"> --> 
      </TD>
    </TR>
    <%}else if("1".equals(ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_CONFIG_SELECT_APPROVER_TYPE))){%>
    <TR>
      <TD width=15% class="tr3"><div align="right"><span class="style1"><i18n:message key="flow_auditpro.optionalapprover"/></span></div></TD>
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
    <TR>
      <TD width=15% class="tr3"><div align="right"><span class="style1"><i18n:message key="flow_auditpro.attachment"/></span></div></TD>
      <TD width=30% ><font size="2"> 
        <input type="file" name="path" size='20'>
        <div style='display: inline' id='div_attachment'></div>
      </TD>      
    </TR>
    <TR>
      <TD width=15% class="tr3"><div align="right"><span class="style1"><i18n:message key="flow_auditpro.remark"/></span></div></TD>
      <TD width=30% ><font size="2"> 
        <textarea  cols="40" rows="6" name="handleComments" onKeyDown="javascript:textCounter(this,document.getElementById('textareaLimitLength'),'500')"></textarea>
      </TD>
    </TR>
    <tr>
      <td colspan='4' align='center'>
         <input type="button" name="searchBtn" value='<i18n:message key="button.submit"/>' onclick='submitForm()'>&nbsp;&nbsp;
         <input type="Reset" name="resetBtn" value="<i18n:message key="button.reset"/>">
         <input type="button" name="closeBtn" value='<i18n:message key="button.close"/>' onclick="window.close()">
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