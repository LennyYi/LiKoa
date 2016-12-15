<!-- 
 Task_ID	Author	Modify_Date	Description
 IT1002     Robin   04/12/2008 DS-006 在字段显示过程中，如果在中间节点中被设置成“新录入”，该字段是不能录入的.否则都可以录入
-->

<%@ page contentType="text/html;charset=GBK" pageEncoding="GBK"%>
<%@ include file="/common/loading.jsp"%>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n"%>
<%@page
	import="java.util.*,com.aiait.eflow.reportmanage.vo.*,com.aiait.eflow.common.*,com.aiait.eflow.common.helper.*,com.aiait.eflow.reportmanage.helper.*,com.aiait.eflow.util.FieldUtil"%>
<%@page
	import="com.aiait.framework.db.*,com.aiait.eflow.util.StringUtil,com.aiait.eflow.util.DataConvertUtil"%>
<%@page
	import="com.aiait.eflow.housekeeping.vo.StaffVO,com.aiait.eflow.reportmanage.util.DisplayReportPageUtil"%>
<html style="overflow-x: hidden; overflow-y: auto; ">
<%
	ReportManageVO report = (ReportManageVO) request.getAttribute("listReport");
	String type = (String) request.getParameter("type");
	String initRequestNo = "";
	String showCClist = ParamConfigHelper.getInstance().getParamValue(
			CommonName.PARAM_CONFIG_SHOW_CC_LIST_SUBMIT);
	if (showCClist == null || "".equals(showCClist)) {
		showCClist = "0"; // 0---Not need to show the CC Staff selecting window; 1---Need to show the CC Staff selecting window.
	}
	DisplayReportPageUtil pageUtil = new DisplayReportPageUtil();
	ReportFieldHelper.setServerUrl(request.getContextPath());
	long lonDateNow = new Date().getTime();
	String strDateNow = Long.toString(lonDateNow);
%>
<head>
	<title>eForm - Report Display</title>
	<META HTTP-EQUIV="pragma" CONTENT="no-cache">
	<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
	<META HTTP-EQUIV="expires" CONTENT="Wed, 26 Feb 1997 08:21:57 GMT">
	<i18n:jsmessage jsRelativePath="js" prefixName="message" />
	<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/form-field-tooltip.css" media="screen" type="text/css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/jquery-ui.css" media="screen" type="text/css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smart_wizard.css" media="screen" type="text/css">
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/rounded-corners.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/form-field-tooltip.js"></script>
	<script language="javascript" src="<%=request.getContextPath()%>/js/calendar.js"></script>
	<script language="javascript" src="<%=request.getContextPath()%>/js/formdesign.js"></script>
	<script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
	<script language="javascript" src="<%=request.getContextPath()%>/js/table.js"></script>
	<script language="javascript" src="<%=request.getContextPath()%>/js/ajax.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/windowPrompt.js"></script>
	<%
		if (CompanyHelper.EFlow_AIA_CHINA.equals(CompanyHelper
				.getInstance().getEFlowCompany())) {
	%>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/displayform_cho.js"></script>
	<% } else {%>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/displayform.js"></script>
	<% } %>
	
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-ui.js"></script>
	<link href="<%=request.getContextPath()%>/js/NovaJS/qtip/jquery.qtip.css" rel="stylesheet" type="text/css">
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/NovaJS/qtip/jquery.qtip.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/aiaitjsframe.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/selectmulti.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/reportManageAction.it?method=getReportScript&reportSystemId=-1"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/reportManageAction.it?method=getReportScript&reportSystemId=<%=report.getReportSystemId()%>&t=<%=strDateNow%>"></script>
	<script language="javascript">
	  var xmlhttp = createXMLHttpRequest();
	  var bFlag = true;
	  var checkSubmitFlg = false; //用来标示防止重复提交多次
	  function mergeColumn(){   
	   var fieldid = '<%=CommonName.MergedTableId%>';
	   var fieldtables = document.all[fieldid];
	   if (fieldtables!=null){
	       for(var j=0;j<fieldtables.length;j++){
	            var fieldtable = fieldtables[j];
	  			var RowCount = fieldtable.rows.length;
			  	var CurrentRow = null;  	
	  			for(var i=0;i<RowCount;i++){
	  				CurrentRow = fieldtable.rows(i);
		  			if (CurrentRow.cells(2)==null){
		  			   if (CurrentRow.cells(0).isSingle=='Yes'){				    
			  		   		CurrentRow.cells(1).colSpan = 3;
			  		   }else if((i!=RowCount-1)){		  	
			  		   	    CurrentRow.cells(1).colSpan = 3;
			  		   }
	  				}
	  			}
	  	   }
	  	}
	  }
	  
	  /**
	  * to get staff List
	  **/
	  function getOptionList(teamCode) {
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
	    
	  /**
	  *To trigger the Selecting Reference Report Window
	  **/
	  
	  function openWindow(url){
	    url = "" + url;
	    window.open(url)
	  }
	  
	  function	showRefReportWindow(){
	  	var url = "<%=request.getContextPath()%>/reportManageAction.it?method=enterRefReportWindow&sfilId=reference_report&reportId="+document.forms[0].curreportId.value; 		
	  	openRefReportWindow('<%=request.getContextPath()%>',url,document.forms[0].reportSystemId.value);
	  }
	  
	  function showRefReportWindow2(fieldId, divId, tagId) {
	      var selectRefReports = encodeURIComponent($(tagId).value.trim());
	      //alert(fieldId + ": " + selectRefReports);
	      var url = "<%=request.getContextPath()%>/reportManageAction.it?method=selectRefReport&selectRefReports=" + selectRefReports;
	      openRefReportWindow2('<%=request.getContextPath()%>', url, fieldId, divId, tagId);
	  }
	
	  function selectRefContract(fieldId, divId, tagId) {
	      var selectedContracts = encodeURIComponent($(tagId).value.trim());
	      //alert(fieldId + ": " + selectedContracts);
	      var url = "<%=request.getContextPath()%>/contractAction.it?method=selectContract&selectedContracts=" + selectedContracts;
	      openRefContractWindow('<%=request.getContextPath()%>', url, fieldId, divId, tagId);
	  }
	  
	  /**
	  *动态生成指定的section table中的一空白行
	  **/
	  function createTableSectionRow(tableId,sectionId,nodeId){
	     createReportSectionRow('<%=request.getContextPath()%>',tableId,sectionId,'<%=report.getReportSystemId()%>',nodeId);
	  }
	</script>
	<script type="text/javascript" charset="gb2312"
		src="<%=request.getContextPath()%>/js/NovaJS/base.js"></script>
</head>
<body onload="javascript:document.all['addBtn'].disabled=false;"
	onkeydown="checkesc(event)" id="bdDisplay">
<input name="theHistoryRecord" type=hidden value="">
<form name="<%=report.getReportId()%>" method="post"
	action="<%=request.getContextPath()%>/reportManageAction.it?method=saveReportFill">
<input type="hidden" name="textareaLimitLength"><!-- Only used in limiting the length of textarea value -->
<input type="hidden" name="operateType" value="00"><!-- 默认为“初始化initial00”操作，"01"为“提交submited”操作-->
<input type="hidden" name="saveType" value="new"> <input
	type='hidden' name='reportSystemId' value='<%=report.getReportSystemId()%>'>
<input type="hidden" name="curreportId" value="<%=report.getReportId()%>">
<input type="hidden" name="ccStaffCode">
<input type="hidden" name="showCCFlag" value="<%=showCClist%>">
<input type='hidden' name='requestUrl' value='<%=request.getContextPath()%>'>
<div id=thedetailtableDIV style='text-align:center;margin-top:10px'>
<%
	pageUtil.setCurrentNodeId("0");
    out.println(pageUtil.displayBlankReport(request));
%>
<table width="96%" border="1" cellpadding="3" cellspacing="0"
	bordercolor="#D4D4D5" height="40px"
	style="border-collapse: collapse; background: #D31145;">
	<tr>
		<td colspan='6' align='center'>
		<%
			if (!"preview".equals(type)) {
		%> <input type="button" name="initBtn"
			onclick="saveReport('<%=request.getContextPath()%>/reportManageAction.it?method=saveReportFill&submitType=00','00','<%=report.getActionType()%>','<%=report.getActionMessage()%>','<%=(report.getPre_validation_url() == null ? "" : report
								.getPre_validation_url())%>')"
			value="<i18n:message key="button.save_report"/>" class=btn3_mouseout
			onmouseover="this.className='btn3_mouseover'"
			onmouseout="this.className='btn3_mouseout'"
			onmousedown="this.className='btn3_mousedown'"
			onmouseup="this.className='btn3_mouseup'"> <input
			type="button" name="addBtn"
			onclick="saveReport('<%=request.getContextPath()%>/reportManageAction.it?method=saveReportFill&submitType=01','01','<%=report.getActionType()%>','<%=report.getActionMessage()%>','<%=(report.getPre_validation_url() == null ? "" : report
								.getPre_validation_url())%>')"
			value="<i18n:message key="button.submit_report"/>" class=btn3_mouseout
			onmouseover="this.className='btn3_mouseover'"
			onmouseout="this.className='btn3_mouseout'"
			onmousedown="this.className='btn3_mousedown'"
			onmouseup="this.className='btn3_mouseup'"> <input
			type="reset" name="reset1" value="<i18n:message key="button.reset"/>"
			class=btn3_mouseout onmouseover="this.className='btn3_mouseover'"
			onmouseout="this.className='btn3_mouseout'"
			onmousedown="this.className='btn3_mousedown'"
			onmouseup="this.className='btn3_mouseup'"> <input
			type="button" name="back" value="<i18n:message key="button.back"/>"
			onclick='javascript:history.go(-1)' class=btn3_mouseout
			onmouseover="this.className='btn3_mouseover'"
			onmouseout="this.className='btn3_mouseout'"
			onmousedown="this.className='btn3_mousedown'"
			onmouseup="this.className='btn3_mouseup'"> <%
 	} else {
 %> <input type="button" name="closeBtn" onclick="window.close()"
			value="<i18n:message key="button.close"/>" class=btn3_mouseout
			onmouseover="this.className='btn3_mouseover'"
			onmouseout="this.className='btn3_mouseout'"
			onmousedown="this.className='btn3_mousedown'"
			onmouseup="this.className='btn3_mouseup'"> <%
 	}
 %>
		</td>
	</tr>
</table>
</div>
<div style="position: absolute; bottom: 50px; right: 50px; text-align:right;" id="divTop">
	<div class="sideCatalogBg" >
		<div id="sideCatalog-catalog">
		</div>
	</div>
	<div>
		<a class="sideCatalogBtn activeBar" href="javascript:void(0);" ></a>
		<a class="sideToolbar-up" title="Back to TOP" href="#" id="likTop" ></a>
		<a class="sideToolbar-down" title="Back to DOWN" href="javascript:void(0);" id="likDown" ></a>
	</div>
</div>
<input type=hidden name='pending_request_no' value=''></form>
</body>
</html>
<script type="text/javascript">
mergeColumn();
var tooltipObj = new DHTMLgoodies_formTooltip();
tooltipObj.setTooltipPosition('right');
tooltipObj.setPageBgColor('#FBFBFC');
tooltipObj.setTooltipCornerSize(15);
tooltipObj.initReportFieldTooltip();

<%if (CompanyHelper.EFlow_AIA_CHINA.equals(CompanyHelper
					.getInstance().getEFlowCompany())) {%>
  document.all['pending_request_no'].value=document.all['request_no'].value;
  document.all['request_no'].value='';
<%}%>
</script>

