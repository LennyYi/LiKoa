<%-- 
NO  Task_ID    Author     Modify_Date    Description
1.  IT0958     Robin.Hou   11/01/2007     Invite expert during processing form
2.  IT0958     Robin.Hou   11/01/2007     Expert can give the advice during processing form
3.  IT1002     Robin.Hou   04/16/2008    ���ֶ���ʾ�����У�������ֶ� ��������ĳ���ڵ��޸����ͱ����� �� �����޸ġ���
                                               �򲻹����ڿ�ʼ�ڵ㻹�Ǹ��м�ڵ㣬���ֶζ������޸ģ�������м�ڵ��б����óɡ���¼�롱�����ڵ�һ���ڵ㣨��ʼ�ڵ㣩���ֶ��ǲ���¼��ģ����м�ýڵ��ǿ���¼��ġ�
--%>
<%@ page contentType="text/html;charset=GBK" pageEncoding="GBK"%>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@ include file="/common/loading.jsp" %>
<%@page import="java.util.*,com.aiait.eflow.formmanage.vo.*,com.aiait.eflow.common.helper.*,com.aiait.eflow.util.FieldUtil,com.aiait.eflow.wkf.util.DataMapUtil" %>
<%@page import="com.aiait.framework.db.*,com.aiait.eflow.wkf.vo.WorkFlowProcessTraceVO,com.aiait.eflow.housekeeping.vo.StaffVO,com.aiait.eflow.common.*" %>
<%@page import="com.aiait.eflow.wkf.vo.WorkFlowProcessVO,com.aiait.eflow.util.StringUtil,com.aiait.eflow.util.DisplayFormPageUtil" %>
<%
  DisplayFormPageUtil pageUtil;
  if("1".equals(request.getSession().getAttribute("nonIE"))){
      pageUtil = new DisplayFormPageUtil(true);
  }else{
	  pageUtil = new DisplayFormPageUtil();
  }
  StaffVO currentStaff = (StaffVO)request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
  FieldControlHelper.setServerUrl(request.getContextPath());
  FieldControlHelper.setCurrentStaffCode(currentStaff.getStaffCode());
  FormFieldHelper.setServerUrl(request.getContextPath());

  FormManageVO form = (FormManageVO)request.getAttribute("listForm");
  HashMap sectionFieldMap = (HashMap)request.getAttribute("sectionFieldMap");
  HashMap onlyFillSectionFieldMap= (HashMap)request.getAttribute("onlyFillSectionFieldMap"); //�����ǰ�ڵ��ǡ�Begin���ڵ㣬����Ҫ���ÿ��section�Ƿ���������ת�п����޸ģ�������ڣ����ڡ�Begin���ڵ㲻���޸�
  String requestFormDate = "";
  
  String showCClist = ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_CONFIG_SHOW_CC_LIST_SUBMIT);
  if(showCClist==null || "".equals(showCClist)){
	  showCClist = "0"; // 0---Not need to show the CC Staff selecting window; 1---Need to show the CC Staff selecting window.
  }
  
  //��ǰ�����޸ĵ�sectionFields,���sectionFieldId֮���Է��š�,�����,ÿ��field����ʽΪ��sectionId.fieldId��
  String updateSections = (String)request.getAttribute("updateSectionFields");
  if(updateSections!=null && !"".equals(updateSections)){
	  updateSections = ","+updateSections; //example: ",02.field_02_01,03.field_03_01"
  }else{
	  updateSections = "";
  }
  //��ǰ�ڵ�ſ��������sectionFields,���sectionFieldId֮���Է��š�,�����,ÿ��field����ʽΪ��sectionId.fieldId��
  String newSectionFields = (String)request.getAttribute("newSectionFields");
  if(newSectionFields!=null && !"".equals(newSectionFields)){
	  newSectionFields = ","+newSectionFields; //example: ",02.field_02_01,03.field_03_01"
  }else{
	  newSectionFields = "";
  }
  
  //SYSTEM FIELD: is_exceptional_case �Ƿ����ϵͳ�ֶΡ�is_exceptional_case���ı�־��Ĭ�ϲ�����;������ڣ������form����������isExceptionalCase��Ϊtrue,����Ϊfalse
  boolean existIsExceptionalCase = false;
  boolean isExceptionalCase = false;
  
  String operateType= (String)request.getParameter("operateType");
  if (operateType == null) {
      operateType = (String) request.getAttribute("operateType");
      if (operateType == null) {
          operateType = "view";
      }
  }
  
  String viewFlag= (String)request.getParameter("viewFlag"); //������������ǲ鿴���ϵ�ʱ��ֻ�ǲ鿴����Ҫ��ʾ�����İ�ť�������ֵ�Ļ���
  String pop = (String) request.getParameter("pop");
  //status: 00 ------ draft
  //        01 ------ submitted
  //        02 ------ in progress
  //        03 ------ rejected
  //        04 ------ completed
  String ccStaffView = (String)request.getAttribute("ccStaffView"); //CC����STAFF�򿪸�ҳ����в鿴
  if(ccStaffView!=null && "true".equals(ccStaffView)){
	  viewFlag = "false";
  }
    
  String status = (String)request.getParameter("status");
  String requestNo = (String)request.getParameter("requestNo");
  
  //
  if("reject".equals(operateType)){
	  String flag = (String)request.getAttribute("noUpdate");
	  operateType = "view";
	  if(!"true".equals(flag)){
	    status = "03";
	  }
  }
  
  WorkFlowProcessVO process = (WorkFlowProcessVO)request.getAttribute("processVo");
  if(status==null || "".equals(status)){
	  status = process.getStatus();
  }
  String inProcess = "";
  String lockStaffCode = null;//���inProcess=1�������������ڴ��������������ˣ�����Ϊnull
  String currentNodeId = "";
  String requestStaffCode = "";
  String lastNode = "";//�Ƿ������һ�������ڵ�ı�־��false���ǣ�true�����һ�������ڵ�
  lastNode = (String)request.getAttribute("lastNode");
  if(lastNode==null){
	  lastNode = "false";
  }
  //���е�ǰ�����˵��������ı�־������ò������ڣ����ʾ�ǡ���Ҫ���е����Ĳ�����Ӧ����ʾ������ť��
  String adjustFlag = (String)request.getParameter("adjust");
  
  String printFlag = (String)request.getParameter("print");
  
  if(process!=null){
	  inProcess = process.getInProcess();
	  lockStaffCode = process.getInProcessStaffCode();
	  currentNodeId = process.getNodeId();
	  FieldControlHelper.setCurrentNodeId(currentNodeId);
	  requestStaffCode = process.getRequestStaffCode();
  }
  
  Collection traceList = (ArrayList)request.getAttribute("traceList");
 
  Collection sectionList = form.getSectionList();
  int sectionNum = sectionList.size();
  
  pageUtil.setCurrentNodeId(currentNodeId);
  pageUtil.setForm(form);
  pageUtil.setIsExceptionalCase(false);
  pageUtil.setNewSectionFields(newSectionFields);
  pageUtil.setOnlyFillSectionFieldMap(onlyFillSectionFieldMap);
  pageUtil.setPrintFlag(printFlag);
  pageUtil.setProcess(process);
  pageUtil.setRequestNo(requestNo);
  pageUtil.setSectionFieldMap(sectionFieldMap);
  pageUtil.setStaff(currentStaff);
  pageUtil.setStatus(status);
  pageUtil.setUpdateSections(updateSections);
  pageUtil.setLockedStaffCode(lockStaffCode);
  
  boolean canReassign = false;
  AuthorityHelper authority = AuthorityHelper.getInstance();
  if (authority.checkAuthority(currentStaff.getCurrentRoleId(), ModuleOperateName.MODULE_FORM_INQUIRY, ModuleOperateName.OPER_FORM_INQUIRY_REASSIGN)) {
      canReassign = true;
  }
  
  long lonDateNow = new Date().getTime(); 
  String strDateNow = Long.toString(lonDateNow);
%>

<head>
<title>E-Flow</title>
<script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
<link rel="stylesheet"	href="<%=request.getContextPath()%>/css/smart_wizard.css" media="screen" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/form-field-tooltip.css" media="screen" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/jquery-ui.css" media="screen" type="text/css">
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/rounded-corners.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/form-field-tooltip.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/calendar.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/formdesign.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/table.js"></script>

<script language="javascript" src="<%=request.getContextPath()%>/js/ajax.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/windowPrompt.js"></script>
<%if(CompanyHelper.EFlow_AIA_CHINA.equals(CompanyHelper.getInstance().getEFlowCompany())){%>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/displayform_cho.js"></script>
<%}else{%>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/displayform.js"></script>
<%}%>

<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-ui.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/aiaitjsframe.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/dealform.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/selectmulti.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/formManageAction.it?method=getFormScript&formSystemId=-1"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/formManageAction.it?method=getFormScript&formSystemId=<%=form.getFormSystemId()%>&t=<%=strDateNow%>"></script> 
<script type="text/javascript" charset="gb2312" src="<%=request.getContextPath()%>/js/NovaJS/base.js"></script>
<script language="javascript">



  var checkSubmitFlg = false; //������ʾ��ֹ�ظ��ύ���

  function lockForm(){
    var labelValue = document.all.lockBtn.value;
    var method = "";
    if(labelValue==lock_form){
       method = "lockForm";
    }else{
       method = "unLockForm";
    }
    var message = confirm_lock_form;
    if(labelValue==unlock_form){
      message = confirm_unlock_form;
    }
    if(confirm(message)){
         var xmlhttp = createXMLHttpRequest();
         var url = "<%=request.getContextPath()%>/wkfProcessAction.it?method="+method+"&requestNo=<%=requestNo%>";
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
        //alert(result.Trim())
        
        if(result.Trim()=="success"){
          if(labelValue==lock_form){
        	document.all.approveBtn.disabled = "";
            if(document.all.holdBtn) document.all.holdBtn.disabled = "";
            document.all.rejectBtn.disabled = "";
            document.all.adviseBtn.disabled = "";
            //document.all['saveSectionBtn'].disabled = "";
            if(document.all.updateBtn) document.all.updateBtn.disabled = "";
            document.all.lockBtn.value = unlock_form;
            document.getElementById("message").innerHTML = "(<font color='red'><%=currentStaff.getStaffName().trim()%> locked the form</font>)";
          }else{
            document.all.approveBtn.disabled = "true";
            if(document.all.holdBtn) document.all.holdBtn.disabled = "true";
            document.all.rejectBtn.disabled = "true";
            document.all.adviseBtn.disabled = "true";
            //document.all['saveSectionBtn'].disabled = "true";
            if(document.all.updateBtn) document.all.updateBtn.disabled = "true";
            document.all.lockBtn.value = lock_form;
            document.getElementById("message").innerText = "";
          }
        }else if(result.Trim()=="lock"){
          alert("Someone had locked the form!");
        }else{
          alert("Operation fails!");
        }
    }
  }
 
  function changeCheckSelect(origObjName,targetObjName){  
      var temp = "";
      var headings = document.getElementsByName(origObjName);   
      for(var i = 0; i < headings.length; i++) {  // Loop through the returned tags
        var h = headings[i];
        if(h.checked==true){
           temp = temp+","+h.value
        }
      }
      if(temp!=""){
       temp = temp.substring(1,temp.length);
      }
      document.all[targetObjName].value = temp;
      alert(document.all[targetObjName].value)
  }
   var xmlhttp = createXMLHttpRequest();
   
  /**
  * to get staff List
  **/
  function getOptionList(teamCode){
    var url = "";
    url = "<%=request.getContextPath()%>/staffAction.it?method=getStaffList&teamCode="+teamCode;
    xmlhttp.open("POST", url, false);
    xmlhttp.onreadystatechange=handleStateChangeStaff;
    xmlhttp.setRequestHeader("If-Modified-Since","0");
    xmlhttp.send(null);

    if (typeof(onchangeTeam) == "function") {
    	onchangeTeam(teamCode);
    }
  }

  function changeRequester(staffCode) {
      if (typeof(onchangeRequester) == "function") {
          onchangeRequester(staffCode);
      }
  }
  
   function mergeColumn(tblId){   
   	var fieldtable = document.getElementById(tblId);
   	if (fieldtable != null){
   		var RowCount = fieldtable.rows.length;
		var CurrentRow = null;  	
  		for(var i=0;i<RowCount;i++){
  			CurrentRow = fieldtable.rows(i);
			if (CurrentRow.cells(2)==null){
			    if (CurrentRow.cells(0).isSingle=='Yes'){
					CurrentRow.cells(1).colSpan = 5;
				}else if((i!=RowCount-1)){
		  		   	CurrentRow.cells(1).colSpan = 5;
		  		}				
  			}
  		}  	   
     }
 }
   function showRefFormWindow2(fieldId, divId, tagId) {
       var selectRefForms = encodeURIComponent($(tagId).value.trim());
       //alert(fieldId + ": " + selectRefForms);
       var url = "<%=request.getContextPath()%>/formManageAction.it?method=selectRefForm&selectRefForms=" + selectRefForms;
       openRefFormWindow2('<%=request.getContextPath()%>', url, fieldId, divId, tagId);
   }

   function selectRefContract(fieldId, divId, tagId) {
       var selectedContracts = encodeURIComponent($(tagId).value.trim());
       //alert(fieldId + ": " + selectedContracts);
       var url = "<%=request.getContextPath()%>/contractAction.it?method=selectContract&selectedContracts=" + selectedContracts;
       openRefContractWindow('<%=request.getContextPath()%>', url, fieldId, divId, tagId);
   }
   function holdForm(){
	    document.all['holdBtn'].disabled = "true";
	    
	    if(confirm("Are you sure hold the form?")){
	         var xmlhttp = createXMLHttpRequest();
	         var url = "<%=request.getContextPath()%>/wkfProcessAction.it?method=holdForm&requestNo=<%=requestNo%>";
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
	        if(result.Trim()!="success")
	            alert("Operation fails!");
	    }	    
   }
   <%if("1".equals(request.getSession().getAttribute("nonIE"))){%>
   //overwrite the functions in dealform.js
   function updateForm(currentNodeId,needValid){
	    var frm = document.forms[0];
	    if (needValid == true) {
		    if (validateForm(frm) == false) {
	            return false;
	        }
	        if (typeof(validationUpdate) == "function") {
	            if (validationUpdate(currentNodeId) == false) {
	                return false;
	            }
	        }
	    }
	    //check the length of every fields
	    for(var i=0;i<frm.length;i++)   
	    {   
	      if((frm[i].tagName.toUpperCase()=="INPUT" &&frm[i].type.toUpperCase()=="TEXT") || frm[i].tagName.toUpperCase()=="TEXTAREA"){
	       //��ʱ��objΪ�ı���   
	       //var value = frm[i].value.Trim();
	       var value = "";
	       if(frm[i].isNumber=="true")
	    	   value = frm[i].value.Trim().replace(/,/g,"");
	       else
	    	   value = frm[i].value.Trim();
	       var length = value.len();
	       if(length>frm[i].maxLength){
	          alert(field_length_long+":"+frm[i].title);
	          frm[i].focus();
	          return false;
	        }
	      }
	    }
	    //if(confirm(str)){
	    //enable all input
	    for(var i=0;i<frm.length;i++) frm[i].disabled = false;
	    //***********************
	    var url = document.all['requestUrl'].value+"/formManageAction.it?method=updateFormFill&currentNodeId="+currentNodeId;
	    var args = getFormStr(frm);
	    args = encodeURI(encodeURI(args)); //Very Important!
	    var myAjax = new Ajax.Request(
	    	url,
	       {
	           method:"post",       //
	           parameters:args,   //
	           asynchronous:false,
	           setRequestHeader:{"If-Modified-Since":"0"},     //
	           onComplete:function(x){},
	           onError:function(x){          //
	               alert('Fail to update form');
	               return false;
	           }
	       }
	    ); 
	    //}
	    return true;
   }
   //����ͨ����form
   function approveForm(requestNo,currentNodeId,requestStaffCode,updateSections,newSectionFields){
      //�����form��exceptional case,����Ҫ������ʾ
      if(document.all['isExceptionalCase'].value=="true"){
        if(confirm(confirm_exceptional)==false){
           return;
        }
      }
      var handleType = document.all['approveRejectType'].value ;
      //url,width,height,moveX,moveY
      var url = document.all['requestUrl'].value+"/wkfProcessAction.it?method=enterAuditForm&requestNo="+requestNo
                +"&formSystemId="+document.all['formSystemId'].value+"&handleType="+handleType+"&currentNodeId="+currentNodeId
                +"&requestStaffCode="+requestStaffCode+"&tempDate="+Math.random()*100000+"&updateSectionFields="+updateSections+"&newSectionFields="+newSectionFields;
       	url = url+"&nonIE=1";
     	//parent.document.getElementById("auditTR").style.display="";
     	//document.getElementById("audit").style.display = "";
     	document.getElementById("audit").contentWindow.document.location=url;
    }
    function rejectForm(formReturn) {
	    if (formReturn == null) {
	        if (!confirm(confirm_reject)) {
	            return;
	        }
	    } else if (!confirm(confirm_return)) {
	        return;
	    }
	    var url = document.all['requestUrl'].value + "/wkfProcessAction.it?method=enterAuditForm&requestNo="
	            + document.all['requestNo'].value + "&formSystemId=" + document.all['formSystemId'].value
	            + "&handleType=04&currentNodeId=" + document.all['currentNodeId'].value + "&requestStaffCode="
	            + document.all['requestStaffCode'].value + "&tempDate=" + Math.random() * 100000
	            + "&updateSectionFields=&newSectionFields=&formReturn=" + formReturn;
       	url = url+"&nonIE=1";
     	document.getElementById("audit").contentWindow.document.location=url;
	}
    function inviteExpert(){
       var url = document.all['requestUrl'].value+"/wkfProcessAction.it?method=enterInviteExpert&requestNo="+document.all['requestNo'].value;
       url = url+"&nonIE=1";
       document.getElementById("audit").contentWindow.document.location=url;
    }
    function adviseForm(){
       var url = document.all['requestUrl'].value+"/wkfProcessAction.it?method=enterExpertAdvise&requestNo="+document.all['requestNo'].value;
       url = url+"&nonIE=1";
   	   document.getElementById("audit").contentWindow.document.location=url;
    }
   <%}%>
</script>
<style media=print> 
.Noprint{display:none;} 
</style> 
</head>
<body onkeydown="checkesc(event)">
 <input type="hidden" name="textareaLimitLength">
 <%if(printFlag!=null && !"".equals(printFlag)){%>
  <OBJECT id=WebBrowser classid=CLSID:8856F961-340A-11D0-A96B-00C04FD705A2 height=0 width=0> 
  </OBJECT> 
 <%}%>
 <input   name="theHistoryRecord"   type=hidden   value="">   
 <form name="myForm" method="post" >
  <input type='hidden' name='requestUrl' value='<%=request.getContextPath()%>'>
  <input type='hidden' name='formSystemId' value='<%=form.getFormSystemId()%>'>
  <input type="hidden" name="curformId" value="<%=form.getFormId()%>">
  <input type="hidden" name="updateSections" value='<%=updateSections==null?"":updateSections%>'>
  <input type="hidden" name="newSectionFields" value='<%=newSectionFields==null?"":newSectionFields%>'>
  <input type="hidden" name="requestNo" value="<%=requestNo%>">
  <input type="hidden" name="operateType" value="<%=operateType%>">
  <input type="hidden" name="currentNodeId" value="<%=currentNodeId%>">
  <input type="hidden" name="ccStaffCode">
  <input type="hidden" name="showCCFlag" value="<%=showCClist%>">
  <input type="hidden" name="requestStaffCode" value="<%=requestStaffCode%>">
  <input type="hidden" name="updateFieldsVal" value="">
  <input type="hidden" name="newFieldsVal" value="">
  <% 
    if("00".equals(status) || "03".equals(status)){
  %>
    <input type="hidden" name="saveType" value="update">
  <%
   }
  %>
 <div   width=100%   id=thedetailtableDIV style='text-align:center;margin-top:10px'>
  <!-- table width="100%"  border="0" cellpadding="3" cellspacing="0" bordercolor="#6595D6" style="border-collapse:collapse;" > -->
  <table width="96%" style="border: 1px #D4D4D5 solid; background-color:#F0F0F0;">
  <%=pageUtil.displayFormWithContent(request)%> 
  <tr>
    <td colspan='2'>&nbsp;&nbsp;</td>
  </tr>
 
 <%if(pageUtil.getIsExceptionalCase()==true){%>
  <tr>
   <td colspan='6' align='center'>
     <input type="hidden" name="isExceptionalCase" value="true">
     <center class="Noprint" > 
     <img src="<%=request.getContextPath()%>/images/reminder_03.gif">&nbsp;&nbsp;<font color='red'><b>Please note: This form has the exceptional item!</b></font>
     </center>
   </td>
  </tr>
  <%}else{%>
   <input type="hidden" name="isExceptionalCase" value="false">
  <%}%>
  
  <tr>
      <td colspan='6' align='center'>
    <%if(printFlag!=null && !"".equals(printFlag)){%>
    <center class="Noprint" > 
 <input type="button" name="setupBtn" value='<i18n:message key="button.page_setup"/>' onclick='document.all.WebBrowser.ExecWB(8,1)' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'"
onmouseout="this.className='btn3_mouseout'"
onmousedown="this.className='btn3_mousedown'"
onmouseup="this.className='btn3_mouseup'">
 <input type="button" name="previewBtn" value='<i18n:message key="button.print_preview"/>' onclick='document.all.WebBrowser.ExecWB(7,1)' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'"
onmouseout="this.className='btn3_mouseout'"
onmousedown="this.className='btn3_mousedown'"
onmouseup="this.className='btn3_mouseup'">
 <input type="button" name="previewBtn" value='<i18n:message key="button.print"/>' onclick='document.all.WebBrowser.ExecWB(6,1)' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'"
onmouseout="this.className='btn3_mouseout'"
onmousedown="this.className='btn3_mousedown'"
onmouseup="this.className='btn3_mouseup'">
 <input type="button" name="closeBtn" value='<i18n:message key="button.close"/>' onclick='javascript:window.close()' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'"
onmouseout="this.className='btn3_mouseout'"
onmousedown="this.className='btn3_mousedown'"
onmouseup="this.className='btn3_mouseup'">
   </center>
    <%}else{%>
       <% 
         if("false".equals(viewFlag)){
       %>
   

		<%
		  if (CompanyHelper.getInstance().getEFlowCompany().equals(CompanyHelper.EFlow_AIAIT)) {
		%>
        <input type="button" name="printBtn" value='<i18n:message key="button.print"/>' onclick='javascript:printForm("<%=status%>")' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'"
onmouseout="this.className='btn3_mouseout'"
onmousedown="this.className='btn3_mousedown'"
onmouseup="this.className='btn3_mouseup'">
		<%}%>

        <%
          if (!CompanyHelper.getInstance().getEFlowCompany().equals(CompanyHelper.EFlow_AIA_CHINA) || !CommonName.STATUS_DRAFT.equals(status)) {
        %>
        <input type="button" name="exportBtn" value='<i18n:message key="button.export_pdf"/>' onclick='javascript:exportPDF()' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'"
onmouseout="this.className='btn3_mouseout'"
onmousedown="this.className='btn3_mousedown'"
onmouseup="this.className='btn3_mouseup'">
        <%}%>
         
        <%
            if ("true".equals(pop)) {
        %>
        <input type="button" name="closeBtn" value='<i18n:message key="button.close"/>' onclick='javascript:window.close();' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'"
        onmouseout="this.className='btn3_mouseout'"
        onmousedown="this.className='btn3_mousedown'"
        onmouseup="this.className='btn3_mouseup'">
        <%
            } else {
                if (canReassign && "Yes".equalsIgnoreCase(ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_FORM_RETURN))
                        && "04".equals(status)) {
        %>
        <input type="button" name="rejectBtn" value='<i18n:message key="button.return_form"/>' onclick="rejectForm('formReturn')" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'"
        onmouseout="this.className='btn3_mouseout'"
        onmousedown="this.className='btn3_mousedown'"
        onmouseup="this.className='btn3_mouseup'">
        <%
                }
        %>
        <input type="button" name="backBtn" value='<i18n:message key="button.back"/>' onclick='javascript:history.go(-1)' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'"
        onmouseout="this.className='btn3_mouseout'"
        onmousedown="this.className='btn3_mousedown'"
        onmouseup="this.className='btn3_mouseup'">
        <%
            }
        } else {
          if ("view".equals(operateType) && ("00".equals(status) || "03".equals(status))) {
        %>
          <input type="button" name="saveBtn" value='<i18n:message key="button.save_form"/>' onclick="saveForm('<%=request.getContextPath()%>/formManageAction.it?method=saveFormFill&submitType=00','00','<%=form.getActionType()%>','<%=form.getActionMessage()%>','<%=(form.getPre_validation_url()==null?"":form.getPre_validation_url())%>')" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'"
onmouseout="this.className='btn3_mouseout'"
onmousedown="this.className='btn3_mousedown'"
onmouseup="this.className='btn3_mouseup'">
          <input type="button" name="subBtn" value='<i18n:message key="button.submit_form"/>' onclick="saveForm('<%=request.getContextPath()%>/formManageAction.it?method=saveFormFill&submitType=01','01','<%=form.getActionType()%>','<%=form.getActionMessage()%>','<%=(form.getPre_validation_url()==null?"":form.getPre_validation_url())%>')" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'"
onmouseout="this.className='btn3_mouseout'"
onmousedown="this.className='btn3_mousedown'"
onmouseup="this.className='btn3_mouseup'">
         <%
          }else if("view".equals(operateType) && "01".equals(status)){
        %>
          <input type="button" name="withDrawBtn" value='<i18n:message key="button.withdraw"/>' onclick='withDrawForm("<%=requestNo%>")' <%="1".equals(inProcess)?"disabled":""%> class=btn3_mouseout onmouseover="this.className='btn3_mouseover'"
onmouseout="this.className='btn3_mouseout'"
onmousedown="this.className='btn3_mousedown'"
onmouseup="this.className='btn3_mouseup'">
        <%}else if("advise".equals(operateType)){ //ר�Ҹ�����%> 
         <input type="button" name="adviseBtn" value='<i18n:message key="button.advise"/>' onclick="adviseForm()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'"
onmouseout="this.className='btn3_mouseout'"
onmousedown="this.className='btn3_mousedown'"
onmouseup="this.className='btn3_mouseup'">
        <%}else if("deal".equals(operateType)){
        	String disabled = "";
        	String hold_disabled = "";
        	String approveBtnValue = "button.approve";
        	String rejectBtnValue = "button.reject";
        	String approveRejectType = "03";
            if("true".equals(lastNode)){
            	approveBtnValue = "button.complete";
            	approveRejectType = "05";
            }
            if (process.getApproveAlias() != null && !process.getApproveAlias().equals("")) {
                approveBtnValue = process.getApproveAlias();
            }
            if (process.getRejectAlias() != null && !process.getRejectAlias().equals("")) {
                rejectBtnValue = process.getRejectAlias();
            }
System.out.println((newSectionFields+updateSections).length());

			if ("1".equals(inProcess) && !lockStaffCode.equals(currentStaff.getStaffCode())) {
			    disabled = "disabled";
			}
			
            //�����ǰ����ֻ��һ�������ˣ��Ͳ�Ҫ��ʾ��Lock����ť
            if(process.getCurrentProcessor()!=null && process.getCurrentProcessor().indexOf(",")>-1 
            	&& !(CommonName.NODE_TYPE_MULTIAPPROVER.equals(process.getNodeType()) && (newSectionFields+updateSections).length()==0) ){
          	 if("1".equals(inProcess)){//����Ǳ����� 
          		 
          		 if(lockStaffCode.equals(currentStaff.getStaffCode()) || "m".equals(process.getNodeType())){//��������˾��ǵ�ǰ�û�, ����multiapprover����  2014-7-18
          			disabled = "";
          %>
                     <input type="button" name="lockBtn" value='<i18n:message key="button.unlock_form"/>' onclick="lockForm()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'"
onmouseout="this.className='btn3_mouseout'"
onmousedown="this.className='btn3_mousedown'"
onmouseup="this.className='btn3_mouseup'">
             
          <%    }else{ disabled = "disabled";%>
                   <input type="button" name="lockBtn" value='<i18n:message key="button.lock_form"/>' onclick="lockForm()" disabled class=btn3_mouseout>
          <%    }
            }else{ disabled = "disabled";//������û�б�������%> 
             <input type="button" name="lockBtn" value="<i18n:message key="button.lock_form"/>" onclick="lockForm()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'"
onmouseout="this.className='btn3_mouseout'"
onmousedown="this.className='btn3_mousedown'"
onmouseup="this.className='btn3_mouseup'">
          <%}
          }//end if �����ǰ����ֻ��һ�������ˣ��Ͳ�Ҫ��ʾ��Lock����ť
          
          if(process.getNodeName().toLowerCase().matches("(aiait help|aiait security)(.*)")){
        	  if("1".equals(inProcess) && !lockStaffCode.equals(currentStaff.getStaffCode()))//��������� not ��ǰ�û�
           		hold_disabled = "disabled";
          %>
          <input type="button" name="holdBtn" value='Hold' onclick='holdForm()' <%=hold_disabled%> class=btn3_mouseout onmouseover="this.className='btn3_mouseover'"
onmouseout="this.className='btn3_mouseout'"
onmousedown="this.className='btn3_mousedown'"
onmouseup="this.className='btn3_mouseup'">
		  <%}%>
          <input type="button" name="approveBtn" value='<i18n:message key="<%=approveBtnValue%>"/>' onclick='approveForm("<%=requestNo%>","<%=currentNodeId%>","<%=requestStaffCode%>","<%=updateSections%>","<%=newSectionFields%>")' <%=disabled%> class=btn3_mouseout onmouseover="this.className='btn3_mouseover'"
onmouseout="this.className='btn3_mouseout'"
onmousedown="this.className='btn3_mousedown'"
onmouseup="this.className='btn3_mouseup'">
          <input type="button" name="adviseBtn" value='<i18n:message key="button.invite"/>' onclick="inviteExpert()" <%=disabled%> class=btn3_mouseout onmouseover="this.className='btn3_mouseover'"
onmouseout="this.className='btn3_mouseout'"
onmousedown="this.className='btn3_mousedown'"
onmouseup="this.className='btn3_mouseup'"> 
         <%if(!"".equals(updateSections) || !"".equals(newSectionFields)){%>
          <input type="button" name="updateBtn" value='<i18n:message key="button.save"/>' onclick='updateForm("<%=currentNodeId%>",false)' <%=disabled%> class=btn3_mouseout onmouseover="this.className='btn3_mouseover'"
onmouseout="this.className='btn3_mouseout'"
onmousedown="this.className='btn3_mousedown'"
onmouseup="this.className='btn3_mouseup'">
         <%}%>   	
          <input type="hidden" name="approveRejectType" value="<%=approveRejectType%>">
          
        <%}%>

		<%
		  if (CompanyHelper.getInstance().getEFlowCompany().equals(CompanyHelper.EFlow_AIAIT)) {
		%>
        <input type="button" name="printBtn" value="<i18n:message key="button.print"/>" onclick='javascript:printForm("<%=status%>")' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'"
onmouseout="this.className='btn3_mouseout'"
onmousedown="this.className='btn3_mousedown'"
onmouseup="this.className='btn3_mouseup'">
		<%}%>

        <%
          if (!CompanyHelper.getInstance().getEFlowCompany().equals(CompanyHelper.EFlow_AIA_CHINA) || !CommonName.STATUS_DRAFT.equals(status)) {
        %>
        <input type="button" name="exportBtn" value='<i18n:message key="button.export_pdf"/>' onclick='javascript:exportPDF()' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'"
onmouseout="this.className='btn3_mouseout'"
onmousedown="this.className='btn3_mousedown'"
onmouseup="this.className='btn3_mouseup'">
        <%}%>
          
        <input type="button" name="backBtn" value='<i18n:message key="button.back"/>' onclick='javascript:history.go(-1)' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'"
onmouseout="this.className='btn3_mouseout'"
onmousedown="this.className='btn3_mousedown'"
onmouseup="this.className='btn3_mouseup'">
<%}%>
<%}%>

      </td>
     </tr>
         <% if("1".equals(request.getSession().getAttribute("nonIE"))){ %>
    <tr id="auditTR" >
         <td width="96%"><IFRAME frameBorder="0" name="audit" id="audit" scrolling="yes" src=""
            style="display:hidden  HEIGHT: 100px; VISIBILITY: inherit; WIDTH: 100%; Z-INDEX: 2"></IFRAME></td>
    </tr>
    <%} %>
   </table> 
    </div>
    <script type="text/javascript"  language="javascript" src="<%=request.getContextPath()%>/js/NovaJS/combox.js"></script>
	
    <script language="javascript">
	//mergeColumn();
	var tooltipObj = new DHTMLgoodies_formTooltip();
	tooltipObj.setTooltipPosition('right');
	tooltipObj.setPageBgColor('#FBFBFC');
	tooltipObj.setTooltipCornerSize(15);
	tooltipObj.initFormFieldTooltip();

	document.all.updateFieldsVal.value = getFieldVals(document, document.all.updateSections);
	document.all.newFieldsVal.value = getFieldVals(document, document.all.newSectionFields);
	jQuery(document).ready(function(){
		  var reqNo = jQuery('input[name="requestNo"]').val();
		  var formId = jQuery('input[name="formSystemId"]').val();
		  //wizard bar
		  var wizardData = selectFromSPJson("poef_wkf_getFlowStatus",reqNo+','+formId); //���������Ÿ���;
		  var wizardStr = '<tr><td><div class="wizard-steps-parent"><div class="wizard-steps">';
          if(wizardData.length>0){
		     jQuery.each(wizardData, function(i,item){ 
		        var curNode = item.CURNODE;
		        var stepClass = 'active-step';
		        if(item.CURNODE!=''&&item.CURNODE!=null){stepClass = 'completed-step'; }
		        wizardStr += '<div class="'+stepClass+'"><a href="#">'+item.NDNAME+'</a></div>'
		     });
          }
	   	  wizardStr += '</div></div></td></tr>';
	   	  //warning msg
	   	  var warnMsg = '<tr><td><div class="wizard-steps-parent2"><div align="center" class="wizard-steps2">';
	   	  var warningData = selectFromSPJson("poef_wkf_getFlowMsg",reqNo);
	   	  if(warningData.length>0){
		     jQuery.each(warningData, function(i,item){
				 var msgClass = 'warningMsg';
			     if(item.MSGCODE=='E')
			    	 msgClass = 'errorMsg';
		    	 warnMsg += '<span class="'+msgClass+'">'+item.MSGCODE+'-'+item.MSGTEXT+'</span>';
			 });
	   	  }
	   	  warnMsg += '</div></div></td></tr>';
          jQuery('table:eq(0)').find('tr:eq(0)').after(wizardStr+warnMsg);

    });

	jQuery(window).resize(function(){
		var width1 = jQuery('div.wizard-steps-parent').outerWidth();
		var width2 = jQuery('div.wizard-steps').outerWidth();
		var width = (width1 - width2)/2;
        jQuery('div.wizard-steps').css("left",  width + "px");

        width1 = jQuery('div.wizard-steps-parent2').outerWidth();
		width2 = jQuery('div.wizard-steps2').outerWidth();
		width = (width1 - width2)/2;
        jQuery('div.wizard-steps2').css("left",  width + "px");
	});
</script>

 <% 
  String openType = (String)request.getParameter("openType");
  if(openType!=null && "sub".equals(openType)){
	  out.println("<script language=\"javascript\">");
	  out.println("parent.document.all(\"iframeName\").style.height = document.body.scrollHeight;");
	  out.println("</script>");
  } 
%>
</form>
</body>
</html>