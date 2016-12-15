<%@page
	import="com.aiait.eflow.common.CommonName,com.aiait.eflow.common.helper.ParamConfigHelper,com.aiait.eflow.common.helper.CompanyHelper"%>
<%@page 
	import="com.aiait.framework.db.*,com.aiait.eflow.wkf.vo.WorkFlowProcessTraceVO,com.aiait.eflow.housekeeping.vo.StaffVO,com.aiait.eflow.common.*" %>
<%@page contentType="text/html; charset=gb2312"%>
<%@ include file="/common/loading.jsp"%>
<%
	String newForm = (String) request.getParameter("newForm");
	String menuItemId = (String) request.getParameter("menuItemId");
	String formSystemId = (String) request.getParameter("formSystemId");
	String requestNo = (String) request.getParameter("requestNo");
	String operateType = (String) request.getParameter("operateType");
	String platformName = ParamConfigHelper.getInstance()
			.getParamValue(CommonName.PLATFORM_NAME).trim();
	if ("".equals(platformName)) {
		platformName = "E-Flow";
	}
	String companyName = ParamConfigHelper.getInstance().getParamValue(
			CommonName.COMPANY_NAME).trim();
	String contact = ParamConfigHelper.getInstance().getParamValue(
			CommonName.PARAM_CONTACT_EMAIL).trim();
	String homepage = ParamConfigHelper.getInstance().getParamValue(
			CommonName.PARAM_HOMEPAGE).trim();
	StaffVO currentStaff = (StaffVO)request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
	String currentStaffCode = currentStaff.getStaffCode();

%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title><%=platformName%></title>
<script language="javascript"
	src="<%=request.getContextPath()%>/js/ajax.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/openDivWindow.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/jquery.min.js"></script>
<link href="<%=request.getContextPath()%>/css/style.css"
	rel="stylesheet" type="text/css">
<style type="text/css">
<!--
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
}

.style1 {
	background-image: images/ table_bg.gif;
	color: #FFFFFF;
}
-->
</style>
<script language="javascript">
	function showAbout() {
	    var companyName = "<%=companyName%>";
	    var instance = "";
	    if (companyName.indexOf("友邦资讯") != -1 || companyName.indexOf("AIAIT") != -1) {
	        instance = "AIAIT (3.11 / 2014072501)";
	    } else if (companyName.toUpperCase().indexOf("CHINA") != -1) {
	        instance = "AIA China (2.11 / 2014070101)";
	    } else if (companyName.toUpperCase().indexOf("THAILAND") != -1) {
	        instance = "AIA Thailand (1.10 / 2014032501)";
	    } else if (companyName.toUpperCase().indexOf("PHILAM") != -1) {
	        instance = "AIA Philam Life (1.0 / 2014032101)";
	    }
	    var message = "<table border=0 width=100% cellspacing=0 cellpadding=0>"
				   + "<tr><td width='20%'><img src='<%=request.getContextPath()%>/images/aia_logo.gif'></td>"
	               + "<td style='font-size:16px;color:#AA231B'><b><%=platformName%> System</b><br>"
	               + "<span style='font-size:12px'><b>The Enterprise Workflow Platform</b></span></td></tr>"
		           + "<tr><td></td><td><table>"
	               + "<tr style='font-size:12px'><td width='18%'>Version: </td><td><b>7.4.0</b></td></tr>"
				   + "<tr style='font-size:12px'><td>Release: </td><td><b>2014072501</b></td></tr>"
				   + "<tr style='font-size:12px'><td>Instance: </td><td><b>" + instance + "</b></td></tr>"
				   + "<tr><td>&nbsp;</td></tr>"
				   + "<tr style='font-size:12px'><td colspan='2'>Copyright &#169; <b><a href='https://wave.aia.com/groups/technology-shared-services-aiait' target='_blank'>AIA Technology Shared Services (AIAIT)</a></b></td></tr>"
	               + "<tr style='font-size:12px'><td colspan='2'>Welcome to <b><a href='mailto:<%=contact%>'>Contact Us</a></b></td></tr>"
	               + "</table></td></tr>"
				   + "<tr><td>&nbsp;</td></tr>"
				   + "<tr style='font-size:12px'><td align='center' colspan='2'>"
	               + "<input type='button' value='    OK    ' onclick='closeDivWindow(1);' class='btn3_mouseout'>"
	               + "</td></tr></table>";
	
	    var a = new DivWindow("1", "About <%=platformName%>", 500, 290, message);
	    a.setPopupTopBgColor("#BE0F34");
	    a.showTop = false;
	    a.open();
	};
		
		if (top.location !== self.location) {
		     top.location=self.location; 
		} 
		
		
	function selectSearchType(){
		if (document.getElementById("searchContext").value=="P"){
			document.getElementById("searchContext").value="M";
			document.getElementById("searchType").style.backgroundImage="url(./images/MEM.png)";
		}
		else{
			document.getElementById("searchContext").value="P";
			document.getElementById("searchType").style.backgroundImage="url(./images/POL.png)";
		}
		document.getElementById("searchKey").select();
		document.getElementById("searchKey").focus();
	}

/* 	//ctrl key sln
	$(document).keydown(function(event){
		if ($(document)[0].activeElement.id=="searchKey"){
			if (event.ctrlKey){
				document.getElementById("searchType").click();
			} 
		}
	}); */

	//ctrl+left/right sln
	$(document).keydown(function(event){
		if ($(document)[0].activeElement.id=="searchKey"){
			if (event.ctrlKey && event.keyCode==37){
				if (document.getElementById("searchContext").value!="P"){
					selectSearchType();	
				}
				event.keyCode=0;
				event.returnValue=false;
				return false;
			} 
			else if (event.ctrlKey && event.keyCode==39){
				if (document.getElementById("searchContext").value!="M"){
					selectSearchType();	
				}
				event.keyCode=0;
				event.returnValue=false;
				return false;
			} 
		}
	});
	
	</script>
</head>
<body scroll="no">
<input type='hidden' name='requestUrl' value='<%=request.getContextPath()%>'></input>
<input type='hidden' name='currentStaffCode' value='<%=currentStaffCode%>'></input>
<table width="100%" height="100%" border="0" cellpadding="0"
	cellspacing="0">
	<tr width="100%">
		<td height="68" colspan="3">
		<table width="100%" height="100%" border="0" cellpadding="0"
			cellspacing="0" class="top-bg1">
			<tr>
				<td style="width: 12%; padding-left: 40px;"><img
					src="images/Logo.png" style="width: 166px;"></td>
				<td width="40%" class="top-bg-center">
				<table style="margin-top: 2px;margin-left: 420px;" border="0" cellpadding="0"
					cellspacing="0">
					<tr>
						<td><span id="spTimeH"
							style="font: 35px Helvetica nenu, Arial, clean, sans-serif; color: white;">
							</span>
						</td>
						<td><span id="spTimeD"
							style="font: 15px Helvetica nenu, Arial, clean, sans-serif; color: white;"></span>
						<br />
						<span id="spTimeW"
							style="font: 15px Helvetica nenu, Arial, clean, sans-serif; color: white;"></span>
						</td>
					</tr>
				</table>
				</td>
				
				<td width="20%" class="top-bg-center">
					<form action="index.jsp" target=_blank style="display:none;" id=searchForm onSubmit="document.getElementById('encodeSearchKey').value=encodeURI(encodeURI(document.getElementById('searchKey').value));return true;">
						<input type=hidden value="search/searchMain.jsp" name=FramePage>
						<input type=hidden value="P" name=searchContext id=searchContext>
						<input type=hidden name=encodeSearchKey id=encodeSearchKey>
						<div id=searchType style="cursor:pointer;margin-top:2px;background-repeat:no-repeat;background-image:url(./images/POL.png);width:61px;height:30px;float:left; display:inline;margin-right:10px;" onclick="selectSearchType();"></div>
						<div id=doSearch style="cursor:pointer;margin-top:1px;background-repeat:no-repeat;background-image:url(./images/searchIcoLeft.png);width:29px;height:32px;float:left; display:inline;" onclick="document.getElementById('encodeSearchKey').value=encodeURI(encodeURI(document.getElementById('searchKey').value));searchForm.submit();document.getElementById('searchForm').style.display='none';"></div>
						<div style="float:left; display:inline;height:32px;">
							<input name=searchKey id=searchKey style="color:#666;line-height:32px;height:32px;border=0px;width=200px;" type="text" onblur="if (document.activeElement.id!='searchType' && document.activeElement.id!='doSearch') document.getElementById('searchForm').style.display='none';">
						</div>
						<div style="margin-top:1px;background-repeat:no-repeat;background-image:url(./images/searchIcoRight.png);width:4px;height:32px;float:left; display:inline;"></div>
					</form>
				</td>
				
				<td width="10%" class="top-bg-right">
				<table border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td><span class="top-bg-right-search" onclick="gotoSearch()"></span>
						</td>
						<td><span class="top-bg-right-home" onclick="gotoHomePage()"></span>
						</td>
						<td><span class="top-bg-right-new" onclick="gotoNewPage()"></span>
						</td>
						<td><span class="top-bg-right-list" onclick="gotoListPage()"></span>
						</td>
						<!--<td><a class="top-link" href="#">主题</a></td>
						<td><a class="top-link" href="#">帮助</a></td>
						<td><a class="top-link" href="#">关于</a></td>-->
					</tr>
				</table>
				</td>
			</tr>
		</table>
		<!--<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td width="5"></td>
				<%if (platformName.equals("E-Flow")) {%>
				<td width="86" height="50" align="center" valign="middle"
					class="eflow_title">E-Flow</td>
				<td width="5"></td>
				<td width="84" bgcolor="white" align="center" valign="middle"><img
					src="images/<%=ParamConfigHelper.getInstance().getParamValue(
								CommonName.COMPANY_LOGO_FILE)%>.gif"></td>
				<%} else if (platformName.length() > 10) {%>
				<td width="179" height="50" align="center" valign="middle"
					class="eflow_title"><%=platformName%></td>
				<td width="5"></td>
				<td width="74" bgcolor="white" align="center" valign="middle"><img
					src="images/<%=ParamConfigHelper.getInstance().getParamValue(
								CommonName.COMPANY_LOGO_FILE)%>.gif"></td>
				<%} else {%>
				<td width="86" height="50" align="center" valign="middle"
					class="eflow_title"><%=platformName%></td>
				<td width="5"></td>
				<td width="84" bgcolor="white" align="center" valign="middle"><img
					src="images/<%=ParamConfigHelper.getInstance().getParamValue(
								CommonName.COMPANY_LOGO_FILE)%>.gif"></td>
				<%}%>
				<td width="20"></td>
				<td height="50" align="left" valign="middle" class="eflow_subtitle"><%=ParamConfigHelper.getInstance().getParamValue(
							CommonName.SYSTEM_SUB_TITLE)%></td>
				<td align="right">
				<%if ("E-Flow".equals(platformName)) {%> <img
					src="images/topright.gif" height="100%" border="0" usemap="#Map">
				<%} else {%> <img src="images/topright_<%=platformName%>.gif"
					height="100%" border="0" usemap="#Map"> <%}%>
				</td>
			</tr>
		</table>--></td>
	</tr>
	<tr>
		<td align="center" noWrap id="frmTitle">
		<%
			String menuSrc = "menu.jsp";
			if (menuItemId != null && !"".equals(menuItemId)) {
				menuSrc = menuSrc + "?menuItemId=" + menuItemId;
			}
			
			String rightSrc = "main.jsp";
			if (request.getParameter("FramePage")!=null){
				String FramePage = (String) request.getParameter("FramePage");
				if (FramePage!="") {
					rightSrc = FramePage+"?searchContext=" + request.getParameter("searchContext") + "&searchKey=" + request.getParameter("encodeSearchKey");
				}
			}
			
			if ("personalWorkIndex".equalsIgnoreCase(homepage)) {
				rightSrc = request.getContextPath()
						+ "/wkfProcessAction.it?method=personalWorkIndex";
			}

			//1.--Apply new request form
			if (newForm != null && "Y".equals(newForm)) {
				if (formSystemId != null && !"".equals(formSystemId)) {
					rightSrc = request.getContextPath()
							+ "/formManageAction.it?method=displayFormFill&formSystemId="
							+ formSystemId;
				}
			}
			//2.--Deal with requested form
			else {
				if (formSystemId != null && !"".equals(formSystemId)) {
					if (operateType == null || "".equals(operateType)) {
						operateType = "deal";
					}
					rightSrc = request.getContextPath()
							+ "/formManageAction.it?method=displayFormContent&operateType="
							+ operateType + "&requestNo=" + requestNo
							+ "&formSystemId=" + formSystemId;
				}
			}
		%> <IFRAME src="<%=menuSrc%>" name="menu" id="menu" frameBorder="0"
			style="HEIGHT: 100%; VISIBILITY: inherit; WIDTH: 180px; Z-INDEX: 2;"
			target="right"> </IFRAME></td>
		<td>
		<table border="0" cellspacing="0" cellpadding="0" height="100%">
			<tr id="tdSwichBar" >
				<td
					style="background: url(images/Switch1.jpg) no-repeat center middle; width: 9px; height: 113px; background-color: #D31145;"><font
					id="switchPoint" title="collapse"
					style="COLOR: #FFFFFF; CURSOR: hand; FONT-FAMILY: Webdings; FONT-SIZE: 7pt;">3</font></td>
			</tr>
		</table>
		</td>
		<td width="100%">
			<span class="frameBox">
				<IFRAME frameBorder="0" name="main" id="main" scrolling="yes" src="<%=rightSrc%>" style="HEIGHT: 100%; VISIBILITY: inherit; WIDTH: 100%; Z-INDEX: 1">
				</IFRAME>
			</span>
		</td>
	</tr>
</table>
<map name="Map">
	<%
		if (CompanyHelper.EFlow_AIA_CHINA.equals(CompanyHelper
				.getInstance().getEFlowCompany())) {
	%>
	<area shape="rect" coords="45,11,80,42" title='About E-Flow'
		href="javascript:showAbout()">
	<area shape="rect" coords="141,11,176,48" title='Help'
		href="<%=ParamConfigHelper.getInstance().getParamValue(
								"help_link")%>"
		target="_blank">
	<%
		} else {
	%>
	<area shape="rect" coords="105,11,145,42" title='Homepage'
		href="<%=request.getContextPath()%>/welcome.jsp">
	<area shape="rect" coords="150,11,182,42" title='User Manual'
		href="<%=request.getContextPath()%>/manual/index.htm" target="_blank">
	<area shape="rect" coords="190,11,230,42" title='About E-Flow'
		href="javascript:showAbout()">
	<%
		}
	%>
</map>
</body>
<script type="text/javascript" charset="gb2312"
	src="<%=request.getContextPath()%>/js/common.js"></script>
<script type="text/javascript" charset="gb2312"
	src="<%=request.getContextPath()%>/js/NovaJS/index_jsp_common.js"></script>
</html>
