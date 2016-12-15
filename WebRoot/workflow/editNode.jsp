<%@ include file="/common/head.jsp" %>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@page import="java.util.*,com.aiait.eflow.util.NodePropertyPageUtil,com.aiait.eflow.formmanage.vo.*,com.aiait.eflow.wkf.vo.*,com.aiait.eflow.housekeeping.vo.*,com.aiait.eflow.util.StringUtil"%>
<%@page import="com.aiait.eflow.common.CommonName, com.aiait.eflow.common.helper.*" %>
<%
  WorkFlowItemVO node = (WorkFlowItemVO)request.getAttribute("flowNode");
  Collection approverGroupList = (ArrayList)request.getAttribute("approverGroupList");
  Collection staffList = (ArrayList)request.getAttribute("staffList");
  String nodeId = (String)request.getParameter("nodeId");
  String approverList = (String)request.getParameter("approverList");
  Collection sectionList = (ArrayList)request.getAttribute("sectionList");
  String[] approverIds = null;
  HashMap map = new HashMap();
  if(approverList!=null && !"".equals(approverList)){
	  approverIds = StringUtil.split(approverList,",");
	  for(int i=0;i<approverIds.length;i++){
		  map.put(approverIds[i],approverIds[i]);
	  }
  }
  
  Collection fieldList = (Collection) request.getAttribute("fieldList");
  if (fieldList == null) {
	  fieldList = new ArrayList();
  }
  
  StaffVO currentStaff = (StaffVO) request.getSession().getAttribute(
			CommonName.CURRENT_STAFF_INFOR);
  
%>
<HTML>
<HEAD>
  <title>Node Properties Setting</title>
  <META HTTP-EQUIV="pragma" CONTENT="no-cache">
  <META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
  <META HTTP-EQUIV="expires" CONTENT="Wed, 26 Feb 1997 08:21:57 GMT">
  <link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
  <link href="<%=request.getContextPath()%>/css/showhidden_style.css" rel="stylesheet" type="text/css">
<STYLE type=text/css>
.multipleSelectBoxControl SPAN {
	FONT-WEIGHT: bold; FONT-SIZE: 11px; FONT-FAMILY: arial
}
.multipleSelectBoxControl DIV SELECT {
	FONT-FAMILY: arial; HEIGHT: 100%
}
.multipleSelectBoxControl INPUT {
	WIDTH: 25px
}
.multipleSelectBoxControl DIV {
	FLOAT: left
}
.multipleSelectBoxDiv {
	
}
FIELDSET {
	MARGIN: 10px; WIDTH: 500px
}
</STYLE>
<STYLE>
.expanded{}
.collapsed{DISPLAY: none;}

.line {  border-color: black black #006699; font-family: "??"; font-size: 12px; color: #006699; text-decoration: none; border-style: solid; border-top-width: 0px; border-right-width: 0px; border-bottom-width: 1px; border-left-width: 0px}
.tab {  font-family: "??"; font-size: 12px; color: #ffffff; text-decoration: none}
.xingcheng {  font-family: "??"; font-size: 12px; color: #FFFFFF; text-decoration: none; padding-top: 2px; padding-left: 4px}
.xingchengt {  font-family: "??"; font-size: 12px; color: #000000; text-decoration: none; border-color: black #006699 #006699; border-style: inset; border-top-width: 0px; border-right-width: 1px; border-bottom-width: 1px; border-left-width: 1px}
.tab {  font-family: "??"; font-size: 12px; color: #000000; text-decoration: none}
</STYLE>
<script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/ajax.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/aiaitjsframe.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/selectmulti.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/showhidden_menu.js"></script>
<script language="javascript">
	<!--
			function outliner()
			{ 
				var child = document.all[event.srcElement.getAttribute("child",false)];
				if (null != child){
					if(child.className == "expanded")
					{
						child.className = "collapsed";
						return;
					}
					if(child.className == "collapsed")
					{
						child.className = "expanded";
						return;
					}
				}
			}
	//-->
</script>
<script language="javascript"><!--
	jQuery(function() {
	    jQuery("[name='nodetype']").change(selectNodeType).change();
	});
	
	function selectNodeType(event) {
	    var nodeType = jQuery(this).val();
	    jQuery("[name='delaytimeField']").prop("disabled", nodeType != "<%=CommonName.NODE_TYPE_DELAYED %>");
	};

    function submitNode(){
        var nodeType = document.all['nodetype'].value;
        
        var obj = document.getElementById('toBox[]');
        var selectIdStr = "";
		for(var no=0;no<obj.options.length;no++){
			//obj.options[no].selected = true;
			selectIdStr = selectIdStr + obj.options[no].value + ",";
		}
        if (selectIdStr == "" && nodeType != "<%=CommonName.NODE_TYPE_AUTOFLOW %>") {
            alert(flownode_select_user);
            return;
        }
        document.all['itemName'].value = formatXML2(document.all['itemName'].value);
        if((document.all['itemName'].value).Trim()==""){
          alert(flownode_node_name);
          document.all['itemName'].focus();
          return;
        }
        document.all['nodeAlias'].value = formatXML2(document.all['nodeAlias'].value);
        if((document.all['nodeAlias'].value).Trim()==""){
          alert(flownode_node_alias);
          document.all['nodeAlias'].focus();
          return;
        }
        if (nodeType == "<%=CommonName.NODE_TYPE_DELAYED %>") {
            var delaytimeField = jQuery("[name='delaytimeField']");
            if (delaytimeField.val() == "") {
                alert(flownode_select_delaytime_field);
                delaytimeField.focus();
                return;
            }
        }
        
        //get the newSectionFieldId
        var sectionFields = document.getElementsByName("newSectionFieldId");
        var selectNewSectionStr = "";
        for(var i = 0; i < sectionFields.length; i++) {  // Loop through the returned tags
          var h = sectionFields[i];
          if(h.checked==true){
            selectNewSectionStr = selectNewSectionStr + h.value + ",";
          }
        }
        //get the updateSectionFieldId
        sectionFields = document.getElementsByName("updateSectionFieldId");
        var selectUpdateSectionStr = "";
        for(var i = 0; i < sectionFields.length; i++) {  // Loop through the returned tags
          var h = sectionFields[i];
          if(h.checked==true){
            selectUpdateSectionStr = selectUpdateSectionStr + h.value + ",";
          }
        }

        sectionFields = document.getElementsByName("hiddenSectionFieldId");
        var selectHiddenSectionStr = "";
        for(var i = 0; i < sectionFields.length; i++) {  // Loop through the returned tags
          var h = sectionFields[i];
          if(h.checked==true){
        	  selectHiddenSectionStr = selectHiddenSectionStr + h.value + ",";
          }
        }
        
        //alert(selectNewSectionStr)
        //alert(selectUpdateSectionStr)
        var arr = new Array();
        arr['nodeId'] = "<%=nodeId%>";
        arr['approverList'] = selectIdStr;
        arr['nodeName'] = document.all['itemName'].value;
        arr['nodeAlias'] = document.all['nodeAlias'].value;
        if (document.all['LimitHours'].value.Trim() == "") {
            arr['LimitHours'] = "0.00";
        } else {
            arr['LimitHours'] = document.all['LimitHours'].value.Trim();
        }
        //arr['sectionIds'] = selectSectionStr;
        arr['newSectionFields'] = selectNewSectionStr;
        arr['updateSectionFields'] = selectUpdateSectionStr;
        arr['hiddenSectionFields'] = selectHiddenSectionStr;
        arr['NodeType'] = document.all['nodetype'].value;
        arr['processorField'] = document.all['processorField'].value;
        arr['companyField'] = document.all['companyField'].value;
        arr['delaytimeField'] = document.all['delaytimeField'].value;
        arr['approveHandle'] = document.all['approveHandle'].value;
        arr['rejectHandle'] = document.all['rejectHandle'].value;
        arr['approveAlias'] = document.all['approveAlias'].value;
        arr['rejectAlias'] = document.all['rejectAlias'].value;
        
        window.returnValue = arr;
        //return;
	    window.close();	
	}
	
	function showHiddenCondition(){
      if(document.getElementById('advanceCondition').style.display=='none'){
       document.all['advanceSetBtn'].value = 'Hide Advance Setting';
       document.getElementById('advanceCondition').style.display = 'block';
      }else{
       document.getElementById('advanceCondition').style.display = 'none'; 
       document.all['advanceSetBtn'].value = 'Show Advance Setting';
      }
    }

	function ie_UpdateTeamList(_orgid){
    	var url = "<%=request.getContextPath()%>/teamManageAction.it?method=getTeamList";
     	var param = "orgId="+_orgid +"&enableSearchAll=true";
     	
     	var myAjax = new Ajax.Request(
         url,
         {
            method:"get",       //
            parameters:param,   //
            asynchronous:false,  //
            setRequestHeader:{"If-Modified-Since":"0"},     //
            onComplete:function(x){    //
				    var select;
				    select = document.getElementById("teamCode");
				    clearSelect(select);
				    select.appendChild(createOptionEle("All", "", true));
				    var options = x.responseXML.getElementsByTagName("team");
				    for (var i = 0, n = options.length; i < n; i++) {
				    	select.appendChild(createElementWithValue(options[i],"code"));
//				    	if(("<=currentStaff.getTeamCode()%>" == options[i].getAttribute("code")) && (_orgid != "")){
//				    		select.appendChild(createElementWithValue(options[i],"code", true));
//					    }else{
//					    	select.appendChild(createElementWithValue(options[i],"code"));
//					    }							    
				    }

				    document.getElementById("staffName").value = "";
				    searchStaffList();				       
            },
            onError:function(x){          //
                    alert('Fail to get team list for company');
            }
         }
       ); 
  	}

  	function doSearchStaffList(staffName, teamCode, orgId){

  		document.getElementById("searchBtn").disable = true;
  		document.getElementById("resetBtn").disable = true;
  		
   		var url = "<%=request.getContextPath()%>/userManageAction.it?method=searchStaffByName";
    	var param = "staffName="+staffName+"&teamCode="+teamCode+"&orgId="+orgId;
     	
     	var myAjax = new Ajax.Request(
         url,
         {
            method:"get",       //
            parameters:param,   //
            asynchronous:false,  //
            setRequestHeader:{"If-Modified-Since":"0"},     //
            onComplete:function(x){    //
				    var select;
				    select = document.getElementById("fromBox");
				    clearSelect(select);

			        <%
			          if(approverGroupList!=null && approverGroupList.size()>0){
			        	 Iterator it = approverGroupList.iterator();
			        	 while(it.hasNext()){
			        	   ApproverGroupVO vo = (ApproverGroupVO)it.next();
			        	   if(!map.containsKey(vo.getGroupId())){
			        		   out.println("select.appendChild(createOptionEle('"+ vo.getGroupName()+"', '"+vo.getGroupId()+"'));");
			        		   // 
			        	   }
			        	 }
			         }
			         %>
			         
				    var options = x.responseXML.getElementsByTagName("staff");
				    for (var i = 0, n = options.length; i < n; i++) {
				    	if(getToBoxValues().indexOf(options[i].getAttribute("code")+",") < 0){
			        		select.appendChild(createElementWithValue(options[i],"code"));
				    	}
				    }
				    select.style.width = '200px';
			 		document.getElementById("searchBtn").disable = false;
			  		document.getElementById("resetBtn").disable = false;
            },
            onError:function(x){          //
                    alert('Fail to get staff list for the specified staff name');
             		document.getElementById("searchBtn").disable = false;
              		document.getElementById("resetBtn").disable = false;
            }
         }
       );
  	}		

	function searchStaffList(){
	
		var staffName = document.getElementById("staffName").value;
		var teamCode = document.getElementById("teamCode").value;
		var orgId = document.getElementById("orgId").value;

		doSearchStaffList(staffName, teamCode,orgId);
  	}	

  	function getToBoxValues(){
  	  	var toboxValues = "";
  	  	var tmpToBox = document.getElementById("toBox[]");
		for(var no=0;no<tmpToBox.options.length;no++){
			toboxValues += tmpToBox.options[no].value+",";
		}
		return  toboxValues;
	}  	

	function init(){
		document.getElementById("orgId").value = "<%=currentStaff.getOrgId()%>";
		ie_UpdateTeamList("<%=currentStaff.getOrgId()%>");
		//todo to delay to 
		document.getElementById("staffName").value = "";
		searchStaffList();
  	}  	
  		
	window.onload=function(){
		resize(500);
		init();
	}

	var orderDoc; 
    var Root=document.documentElement; 
   
 </script>   
</HEAD>
<BODY>

<form id="Form1" method="post">
<table width="100%"  border="1" cellpadding="3" cellspacing="0" bordercolor="#6595D6" style="border-collapse:collapse;" >
   <TR>
       <TD style="width:20%" align='right' class="tr3"><i18n:message key="common.company"/></TD>
       <TD align='left'><select id="orgId" onChange="ie_UpdateTeamList(this.value)">
           <%
           out.print("<option value=''>All</option>");
            //Collection companyList = currentStaff.getOwnCompanyList(); 
           Collection companyList = CompanyHelper.getInstance().getCompanyList();
            if(companyList!=null && companyList.size()>0){
            	Iterator it = companyList.iterator();
            	while(it.hasNext()){
            	   CompanyVO vo = (CompanyVO)it.next();
            	   if(vo.getOrgId() != null && vo.getOrgId().equals(currentStaff.getOrgId())){
	           	     out.print("<option selected value='"+vo.getOrgId()+"'>" + vo.getOrgName()+"</option>");
            	   }else{
            		 out.print("<option value='"+vo.getOrgId()+"'>" + vo.getOrgName()+"</option>");
            	   }
                }
            }
           %>
         </select>
      </TD>
   </TR>
   <TR>
    <TD style="width:20%" align='right' class="tr3"><i18n:message key="housekeeping_user.team"/></TD>
    <TD align='left'><select id="teamCode" onchange="document.getElementById('staffName').value = '';searchStaffList();">
         </select>    
	</TD>
   </TR>    
   <TR>
    <TD style="width:20%" align='right' class="tr3"><i18n:message key="housekeeping_user.staffname"/></TD>
    <TD align='left'><input type='text' id='staffName' size='30' maxLength='50' value='' />
    </TD>
   </TR> 
   <TR>
    <TD align='center' colspan="2">
    <input type="button" id="searchBtn" value='<i18n:message key="button.search"/>' onclick="searchStaffList();" class="btn3_mouseout" onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           		onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
	&nbsp;&nbsp;
	<input type="button" id="resetBtn" value='<i18n:message key="button.reset"/>' onclick="init();" class="btn3_mouseout" onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           		onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
    </TD>
   </TR>    
</table>
<br> 
<table width="100%"  border="1" cellpadding="3" cellspacing="0" bordercolor="#6595D6" style="border-collapse:collapse;" >
   <TR>
	 <td style="width: 480px; height: 21px" nowrap colspan="4">
		<SELECT id=fromBox multiple name=fromBox>
        <%
          if(approverGroupList!=null && approverGroupList.size()>0){
        	 Iterator it = approverGroupList.iterator();
        	 while(it.hasNext()){
        	   ApproverGroupVO vo = (ApproverGroupVO)it.next();
        	   if(!map.containsKey(vo.getGroupId())){
        %>
             <option value="<%=vo.getGroupId()%>"><%=vo.getGroupName()%></option>
        <%
        	   }
        	 }
         }
         if(staffList!=null && staffList.size()>0){
           Iterator staffIt = staffList.iterator();
           while(staffIt.hasNext()){
        	  StaffVO staff = (StaffVO)staffIt.next();
        	  if(!map.containsKey(staff.getStaffCode())){
        %>
          <option value="<%=staff.getStaffCode()%>"><%=staff.getStaffName()%></option>
        <%
        	  }
        	}
         }
        %>
	   </SELECT>
		<SELECT id=toBox[] name=toBox[] multiple>
         <%
          if(approverGroupList!=null && approverGroupList.size()>0){
        	 Iterator it = approverGroupList.iterator();
        	 while(it.hasNext()){
        	   ApproverGroupVO vo = (ApproverGroupVO)it.next();
        	   if(map.containsKey(vo.getGroupId())){
        %>
             <option value="<%=vo.getGroupId()%>"><%=vo.getGroupName()%></option>
        <%
        	   }
        	 }
         }
         if(staffList!=null && staffList.size()>0){
           Iterator staffIt = staffList.iterator();
           while(staffIt.hasNext()){
        	  StaffVO staff = (StaffVO)staffIt.next();
        	  if(map.containsKey(staff.getStaffCode())){
        %>
          <option value="<%=staff.getStaffCode()%>"><%=staff.getStaffName()%></option>
        <%
        	  }
        	}
         }
        %>
		</SELECT>
		<SCRIPT type=text/javascript>
           createMovableOptions("fromBox","toBox[]",480,300,house_user_userlist,house_user_selectedlist);
       </SCRIPT>
	 </td>
   </TR>
   <TR>
       <td style="width:20%" align='right' class="tr3">
         <i18n:message key="flow_node.nodename"/> (<font color='red'>*</font>):
       </td>
       <td style="width:30%">
         <INPUT id="itemName" type="text" value="<%=node==null?"":node.getName()%>">
	   </td>
	   <td style="width:20%" align='right' class="tr3">
         <i18n:message key="flow_node.nodealias"/> (<font color='red'>*</font>):
       </td>
       <td style="width:30%">
         <INPUT id="nodeAlias" type="text" value="<%=node==null?"":node.getNodeAlias()%>">
       </td>
   </TR>
   <TR>
       <td style="width:20%" align='right' class="tr3">
         <i18n:message key="flow_node.nodetype"/> :
       </td>
       <td style="width:30%">
   	     <select name="nodetype">
   	 	            <option value="<%=CommonName.NODE_TYPE_APPROVAL %>" <%=(node !=null && (CommonName.NODE_TYPE_APPROVAL.equals(node.getItemType())))?"selected":"" %> ><i18n:message key="flow_node.nodetype_approval"/></option>
                    <option value="<%=CommonName.NODE_TYPE_PROCESS %>" <%=(node !=null && (CommonName.NODE_TYPE_PROCESS.equals(node.getItemType())))?"selected":"" %> ><i18n:message key="flow_node.nodetype_process"/></option>
	 	 			<option value="<%=CommonName.NODE_TYPE_AUTOFLOW %>" <%=(node !=null && (CommonName.NODE_TYPE_AUTOFLOW.equals(node.getItemType())))?"selected":"" %> ><i18n:message key="flow_node.nodetype_autoflow"/></option>
                    <option value="<%=CommonName.NODE_TYPE_WAITING %>" <%=(node != null && (CommonName.NODE_TYPE_WAITING.equals(node.getItemType()))) ? "selected" : ""%>><i18n:message key="flow_node.nodetype_waiting"/></option>
                    <option value="<%=CommonName.NODE_TYPE_MULTIAPPROVER %>" <%=(node != null && (CommonName.NODE_TYPE_MULTIAPPROVER.equals(node.getItemType()))) ? "selected" : ""%>><i18n:message key="flow_node.nodetype_multi"/></option>
                    <option value="<%=CommonName.NODE_TYPE_DELAYED %>" <%=(node != null && (CommonName.NODE_TYPE_DELAYED.equals(node.getItemType()))) ? "selected" : ""%>><i18n:message key="flow_node.nodetype_delayed"/></option>
        <%
        if (CompanyHelper.getInstance().getEFlowCompany().equals(
				CompanyHelper.EFlow_AIA_CHINA)) {
        %>
        <option value="<%=CommonName.NODE_TYPE_SELECTAPPROVER %>" <%=(node != null && (CommonName.NODE_TYPE_SELECTAPPROVER.equals(node.getItemType()))) ? "selected" : ""%>><i18n:message key="flow_node.nodetype_selectapprover"/></option>
        <option value="<%=CommonName.NODE_TYPE_OPTIONAL %>" <%=(node != null && (CommonName.NODE_TYPE_OPTIONAL.equals(node.getItemType()))) ? "selected" : ""%>><i18n:message key="flow_node.nodetype_optional"/></option>        
        <option value="<%=CommonName.NODE_TYPE_PAYMENT %>" <%=(node != null && (CommonName.NODE_TYPE_PAYMENT.equals(node.getItemType()))) ? "selected" : ""%>><i18n:message key="flow_node.nodetype_payment"/></option>
        <%
        }
        %>
         </select>
   	   </td>
   	   <td style="width:20%" align='right' class="tr3">
         <i18n:message key="flow_node.exptime"/> :
       </td>
       <td style="width:30%">
         <INPUT id="LimitHours" type="text" size="10" value="<%=node==null?"0.0":node.getLimiteDate()%>"  style="ime-mode:disabled" onkeydown="if(event.keyCode==13)event.keyCode=9" onKeyPress="if ((event.keyCode<48 || event.keyCode>57)) event.returnValue=false"> (Hours)
       </td>
   </TR>
   <tr>
       <td align='right' class="tr3">
         <i18n:message key="flow_node.processor_field"/> :
       </td>
       <td colspan="3">
         <select name="processorField">
           <option value=""></option>
           <%
           String processorField = node == null ? null : node.getProcessorField();
           String sectionId = "";
           String fieldId = "";
           if (processorField != null && !processorField.equals("")) {
               String[] items = processorField.split("\\.");
               sectionId = items[0];
               fieldId = items[1];
           }
           Iterator it = fieldList.iterator();
           String fieldOptions = "";
           while (it.hasNext()) {
               FormSectionFieldVO field = (FormSectionFieldVO) it.next();
               if (processorField != null && 
                       field.getSectionId().equals(sectionId) &&
                       field.getFieldId().equals(fieldId)) {
                   fieldOptions += "<option value='" + field.getSectionId() + "." + field.getFieldId() + "' selected>";
               } else {
                   fieldOptions += "<option value='" + field.getSectionId() + "." + field.getFieldId() + "'>";
               }
               fieldOptions += field.getSectionId() + "." + StringUtil.htmlEncoder(field.getFieldLabel());
               fieldOptions += "</option>\n";
           }
           %>
           <%=fieldOptions %>
         </select>
       </td>
   </tr>
   <tr>
       <td align='right' class="tr3">
         <i18n:message key="flow_node.company_field"/> :
       </td>
       <td colspan="3">
         <select name="companyField">
           <option value=""></option>
           <%
           String companyField = node == null ? null : node.getCompanyField();
           sectionId = "";
           fieldId = "";
           if (companyField != null && !companyField.equals("")) {
               String[] items = companyField.split("\\.");
               sectionId = items[0];
               fieldId = items[1];
           }
           it = fieldList.iterator();
           fieldOptions = "";
           while (it.hasNext()) {
               FormSectionFieldVO field = (FormSectionFieldVO) it.next();
               if (companyField != null && 
                       field.getSectionId().equals(sectionId) &&
                       field.getFieldId().equals(fieldId)) {
                   fieldOptions += "<option value='" + field.getSectionId() + "." + field.getFieldId() + "' selected>";
               } else {
                   fieldOptions += "<option value='" + field.getSectionId() + "." + field.getFieldId() + "'>";
               }
               fieldOptions += field.getSectionId() + "." + StringUtil.htmlEncoder(field.getFieldLabel());
               fieldOptions += "</option>\n";
           }
           %>
           <%=fieldOptions %>
         </select>
       </td>
   </tr>
   <tr>
       <td align='right' class="tr3">
         <i18n:message key="flow_node.delaytime_field"/> :
       </td>
       <td colspan="3">
         <select name="delaytimeField">
           <option value=""></option>
           <%
           String delaytimeField = node == null ? null : node.getDelaytimeField();
           sectionId = "";
           fieldId = "";
           if (delaytimeField != null && !delaytimeField.equals("")) {
               String[] items = delaytimeField.split("\\.");
               sectionId = items[0];
               fieldId = items[1];
           }
           it = fieldList.iterator();
           fieldOptions = "";
           while (it.hasNext()) {
               FormSectionFieldVO field = (FormSectionFieldVO) it.next();
               if (field.getFieldType() != 3) {
                   // 3 - Date.
                   continue;
               }
               if (delaytimeField != null && 
                       field.getSectionId().equals(sectionId) &&
                       field.getFieldId().equals(fieldId)) {
                   fieldOptions += "<option value='" + field.getSectionId() + "." + field.getFieldId() + "' selected>";
               } else {
                   fieldOptions += "<option value='" + field.getSectionId() + "." + field.getFieldId() + "'>";
               }
               fieldOptions += field.getSectionId() + "." + StringUtil.htmlEncoder(field.getFieldLabel());
               fieldOptions += "</option>\n";
           }
           %>
           <%=fieldOptions %>
         </select>
       </td>
   </tr>
   <tr>
       <td style="width:20%" align='right' class="tr3">
         <i18n:message key="flow_node.approve_handle"/> :
       </td>
       <td style="width:30%">
         <input id="approveHandle" type="text" value="<%=node == null ? "" : node.getApproveHandle()%>">
       </td>
       <td style="width:20%" align='right' class="tr3">
         <i18n:message key="flow_node.approve_alias"/> :
       </td>
       <td style="width:30%">
         <input id="approveAlias" type="text" value="<%=node == null ? "" : node.getApproveAlias()%>">
       </td>
   </tr>
   <tr>
       <td style="width:20%" align='right' class="tr3">
         <i18n:message key="flow_node.reject_handle"/> :
       </td>
       <td style="width:30%">
         <input id="rejectHandle" type="text" value="<%=node == null ? "" : node.getRejectHandle()%>">
       </td>
       <td style="width:20%" align='right' class="tr3">
         <i18n:message key="flow_node.reject_alias"/> :
       </td>
       <td style="width:30%">
         <input id="rejectAlias" type="text" value="<%=node == null ? "" : node.getRejectAlias()%>">
       </td>
   </tr>
</table>

<table bgcolor=#3333CC border=1 bordercolor=#000000 bordercolordark=#ffffff cellpadding=0 cellspacing=0 width="100%" height="12" align="center" >
  <tr>
    <td bgcolor=#3399CC width=22 align="center"><b><font color="#FFFFFF" size="1">>>></font></b><spacer type="block" width="5"></td>
    <td  class=xingcheng height=12 onMouseOver="this.bgColor='#3366cc';" onMouseOut="this.bgColor='#3333CC';" width="503" >
       <table width="100%" border="0" cellspacing="0" cellpadding="0" class=xingcheng >
         <tr>
           <td style="cursor: hand" onClick="outliner()" child="1ALL" ><i18n:message key="flow_node.comment1"/></td>
         </tr>
       </table>
    </td>
  </tr>
</table>
<div   class="collapsed"   id="1ALL" align="center"> 

          <div child="1ALL2" onClick="outliner()" align="center"> 

  <table class=xingchengt width="100%" border="0" cellpadding="2" cellspacing="2">
 <tr> 
     <td> 
  		<% 
		  out.println(NodePropertyPageUtil.printFieldList(sectionList,node,CommonName.NODE_FIELD_CHANGE_TYPE_NEW));
		%>
		</td>
	  </tr>
	 </table>
</div>
</div>

<table bgcolor=#3333CC border=1 bordercolor=#000000 bordercolordark=#ffffff cellpadding=0 cellspacing=0 width="100%" height="12" align="center" >
  <tr>
    <td bgcolor=#3399CC width=22 align="center"><b><font color="#FFFFFF" size="1">>>></font></b><spacer type="block" width="5"></td>
    <td  class=xingcheng height=12 onMouseOver="this.bgColor='#3366cc';" onMouseOut="this.bgColor='#3333CC';" width="503" >
       <table width="100%" border="0" cellspacing="0" cellpadding="0" class=xingcheng >
         <tr>
           <td style="cursor: hand" onClick="outliner()" child="3ALL" ><i18n:message key="flow_node.comment2"/></td>
         </tr>
       </table>
    </td>
  </tr>
</table>
<div   class="collapsed"   id="3ALL" align="center"> 
  <div child="3ALL2" onClick="outliner()" align="center"> 
    <table class=xingchengt width="100%" border="0" cellpadding="2" cellspacing="2">
     <tr>
       <td> 
  		<% 
  		 out.println(NodePropertyPageUtil.printFieldList(sectionList,node,CommonName.NODE_FIELD_CHANGE_TYPE_UPDATE));
		%>
		</td>
	  </tr>
	 </table>
</div>
</div>

<table bgcolor=#3333CC border=1 bordercolor=#000000 bordercolordark=#ffffff cellpadding=0 cellspacing=0 width="100%" height="12" align="center" >
  <tr>
    <td bgcolor=#3399CC width=22 align="center"><b><font color="#FFFFFF" size="1">>>></font></b><spacer type="block" width="5"></td>
    <td  class=xingcheng height=12 onMouseOver="this.bgColor='#3366cc';" onMouseOut="this.bgColor='#3333CC';" width="503" >
       <table width="100%" border="0" cellspacing="0" cellpadding="0" class=xingcheng >
         <tr>
           <td style="cursor: hand" onClick="outliner()" child="2ALL" ><i18n:message key="flow_node.comment3"/></td>
         </tr>
       </table>
    </td>
  </tr>
</table>
<div   class="collapsed"   id="2ALL" align="center"> 
  <div child="2ALL2" onClick="outliner()" align="center"> 
    <table class=xingchengt width="100%" border="0" cellpadding="2" cellspacing="2">
     <tr>
       <td> 
  		<% 
  		 out.println(NodePropertyPageUtil.printFieldList(sectionList,node,"Hidden"));
		%>
		</td>
	  </tr>
	 </table>
</div>
</div>

<table width="100%"  border="1" cellpadding="3" cellspacing="0" bordercolor="#6595D6" style="border-collapse:collapse;" >
  <TR>
    <TD align='center'>
        <input id="btnSelect"  type="button" value='<i18n:message key="button.submit"/>' onclick="javascript:submitNode()" class="btn3_mouseout" onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">&nbsp;&nbsp;
		<input name="btnClose" type="button" onclick="javascript:window.close();" value='<i18n:message key="button.close"/>' class="btn3_mouseout" onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
	</TD>
  </TR>
</table>
</form>
</BODY>
</HTML>
