<%@taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n"%>
<%@page
	import="com.aiait.eflow.housekeeping.vo.StaffVO,
	com.aiait.eflow.common.*,
	com.aiait.eflow.common.helper.*"%>
<%@page import="java.util.*,
	java.text.SimpleDateFormat,
	com.aiait.eflow.util.StringUtil,
	com.aiait.eflow.wkf.vo.*,
	com.aiait.eflow.wkf.util.DataMapUtil,
	com.aiait.eflow.common.helper.*,
	com.aiait.framework.mvc.servlet.*"%>
<%@page import="com.aiait.eflow.housekeeping.vo.FormTypeVO,
	com.aiait.eflow.common.CommonName,
	com.aiait.eflow.common.helper.FormTypeHelper,
	com.aiait.eflow.formmanage.vo.FormManageVO,
	com.aiait.eflow.housekeeping.vo.StaffVO"%>
<%@page import="com.aiait.framework.i18n.* , com.aiait.eflow.common.helper.ParamConfigHelper"%>
<%@page import="java.util.* "%>

<%@page contentType="text/html; charset=gb2312"%>
<html>
<meta http-equiv="Page-Enter" content="RevealTrans(duration=3,Transitionv=23)">
<meta http-equiv="Page-Exit" content="RevealTrans(duration=3,Transitionv=23)">
<i18n:jsmessage jsRelativePath="js" prefixName="message" />
<%
    StaffVO currentStaff = (StaffVO)request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
    String language = I18NMessageHelper.getLocale().getLanguage();
    String versionName="English";
    String targetLanguage="en";
    String languageName = "语言";
    if(language!=null){
    	language = language.toLowerCase();
    }else{
    	language = CommonName.LOCALE_LANGUAGE_EN;
    }
    if(language.equals("en")){
    	versionName="中文版";
    	languageName = "语言";
    	targetLanguage=CommonName.LOCALE_LANGUAGE_CHINESE;
    }else if(language.equals("zh")){
    	versionName="English";
    	languageName = "Language";
    	targetLanguage=CommonName.LOCALE_LANGUAGE_EN;
    }
    
    Map map = new HashMap();
	map.put(ActionContext.HTTP_REQUEST, request);
	map.put(ActionContext.HTTP_RESPONSE, response);
	ActionContext.setContext(new ActionContext(map));
	
	ParamConfigHelper paramHelper = ParamConfigHelper.getInstance();
	String languageType = paramHelper.getParamValue("language_type");
%>
<head>
<title>eFlow personal work index</title>
<link href="<%=request.getContextPath()%>/css/style.css"
	rel="stylesheet" type="text/css">
<style>
body {
	margin: 0;
	_height: 100%;
}

.Pic {
	background: #D2D3D5;
	width: 100%;
	height: 281px;
	float: right;
}

.verticalAlign {
	vertical-align: middle;
	display: inline-block;
	position: absolute;
	height: 100%;
	width: 1px;
	margin-left: -1px;
}

.divAlign {
	display: inline-block;
	vertical-align: middle;
}
</style>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/jquery.min.js"></script>
<script language="javascript"
	src="<%=request.getContextPath()%>/js/common.js"></script>
<script language="javascript">
function changeLanguage(obj){
  window.parent.location = "<%=request.getContextPath()%>/logonAction.it?method=changeLanguage&language="+obj;
}
</script>

</head>
<body style="overflow-x:hidden;">
<input id="hidCurrentStaffCode" type="hidden" value="<%=currentStaff.getStaffCode()%>"></input>
<input name="requestUrl" type="hidden" value="<%=request.getContextPath()%>">
<div class="layout-middle" id="layout-middle">
	<div id="channel-list">
		<div id="divHome" class="channel" >
			<div class="divAlign" style="Padding-left: 60px;">
				<div>
					<table border="0" cellpadding="0" cellspacing="0" style="table-layout: fixed;">
						<tr>
							<td class="tdWelcome" >
								<table style="table-layout: fixed;width:95%">
									<tr>
										<td class="divTdWelcome">
										</td>
										<td class="divTdWelcome2">
											<B><%=currentStaff.getStaffName().trim()%></B>(<%=currentStaff.getLogonId()%>)
											<br/>
												<span><i18n:message key="common.welcome"/></span>
											<br/>
											<br/>
											<a href='javascript:changeLanguage("zh_Cn")'>
												<image alt='<%=I18NMessageHelper.getMessage("common.language.chinese")%>'  border='0' src='<%=request.getContextPath()%>/images/chinese.gif'>
											</a>
											<a href='javascript:changeLanguage("en")'>
												<image alt='<%=I18NMessageHelper.getMessage("common.language.english")%>' border='0' src='<%=request.getContextPath()%>/images/english.gif'>
											</a>
										</td>
										<td class="divTdWelcome3">
											<table style="width:100%;table-layout: fixed;">
												<tr>
													<td>
														<p style="display: none;"><a class="linkStatus" href="#" onclick="clickStatus('00')"><i18n:message key="form.status_unsubmit"/></a></p>
														<span class="spanImgDraftStatus">
															<!--<span class="spanStatusNum">0</span>-->
														</span>
													</td>
													<td>
														<p style="display: none;"><a class="linkStatus" href="#" onclick="clickStatus('02')"><i18n:message key="form.status_uncomplete"/></a></p>
														<span class="spanImgInprogressStatus">
														</span>
													</td>
												</tr>
												<tr>
													<td>
														<p style="display: none;"><a class="linkStatus" href="#" onclick="clickStatus('03')"><i18n:message key="form.status_rejected"/></a></p>
														<span class="spanImgRejectStatus">
														</span>
													</td>
													<td>
														<p style="display: none;"><a class="linkStatus" href="#" onclick="clickStatus('04')"><i18n:message key="form.status_completed"/></a></p>
														<span class="spanImgCompleteStatus">
														</span>
													</td>
												</tr>
											</table>
										</td>
									</tr>
								</table>
							</td>
							<td>
							
							
							<table border="0" cellpadding="0" cellspacing="0" id="tbShortCutShow" style="table-layout:fixed; margin-left:5px">
								<tr>
									<td style="background-color: #83A8C3; width: 123px; height: 87px; border: 2px solid #fff;
											color: white; padding: 5px; text-align: center;" id="tdShortCut1"></td>
									<td style="background-color: #2EB0BD; width: 250px; height: 87px; border: 2px solid #fff;
											color: white; padding: 5px; text-align: center;" id="tdShortCut2" colspan="2"></td>
									<td style="background-color: #8ABA30; width: 123px; height: 87px; border: 2px solid #fff;
											color: white; padding: 5px; text-align: center;" id="tdShortCut3"></td>
									<td style="background-color: #EF3A5B; width: 123px; height: 87px; border: 2px solid #fff;
											color: white; padding: 5px; text-align: center;" id="tdShortCut4"></td>
								</tr>
								<tr>
									<td style="background-color: #C0A77E; width: 123px; height: 87px; border: 2px solid #fff;
											color: white; padding: 5px; text-align: center;" id="tdShortCut5"></td>
									<td style="background-color: #F2854C; width: 123px; height: 87px; border: 2px solid #fff;
											color: white; padding: 5px; text-align: center;" id="tdShortCut6"></td>
									<td style="background-color: #EBB741; width: 123px; height: 87px; border: 2px solid #fff;
											color: white; padding: 5px; text-align: center;" id="tdShortCut7"></td>
									<td style="background-color: #375D81; width: 250px; height: 87px; border: 2px solid #fff;
											color: white; padding: 5px; text-align: center;" colspan="2" id="tdShortCut8"></td>
								</tr>
							</table>
						
<!-- 原菜单代码
							<table border="0" cellpadding="0" cellspacing="0" id="tbShortCutShow" style="table-layout:fixed; margin-left:5px">
								<tr>
									<td style="background-color: rgb(163,200,227); width: 123px; height: 87px; border: 2px solid #fff;
											color: white; padding: 5px; text-align: center;" id="tdShortCut1"></td>
									<td style="background-color: rgb(109,197,221); width: 250px; height: 87px; border: 2px solid #fff;/*109,197,221*/
											color: white; padding: 5px; text-align: center;" id="tdShortCut2" colspan="2"></td>
									<td style="background-color: rgb(170,218,80); width: 123px; height: 87px; border: 2px solid #fff;
											color: white; padding: 5px; text-align: center;" id="tdShortCut3"></td>
									<td style="background-color: rgb(271,106,139); width: 123px; height: 87px; border: 2px solid #fff;
											color: white; padding: 5px; text-align: center;" id="tdShortCut4"></td>
								</tr>
								<tr>
									<td style="background-color: rgb(224,199,158); width: 123px; height: 87px; border: 2px solid #fff;
											color: white; padding: 5px; text-align: center;" id="tdShortCut5"></td>
									<td style="background-color: rgb(279,148,130); width: 123px; height: 87px; border: 2px solid #fff;
											color: white; padding: 5px; text-align: center;" id="tdShortCut6"></td>
									<td style="background-color: #FFD761; width: 123px; height: 87px; border: 2px solid #fff;
											color: white; padding: 5px; text-align: center;" id="tdShortCut7"></td>
									<td style="background-color: rgb(91,171,240); width: 250px; height: 87px; border: 2px solid #fff;
											color: white; padding: 5px; text-align: center;" colspan="2" id="tdShortCut8"></td>
								</tr>
							</table>
								-->
							</td>
						</tr>
					</table>
				</div>
					
				<div class="div-ShowSpan" id="divShowHomeSpan">		
				
				</div>
			</div>
			<div class="next"></div>
		</div>
		<div id="divNew" class="channel">
			<div class="pre"></div>
				<IFRAME frameBorder="0" scrolling="no" src="formManageAction.it?method=listAvailableForm"
					style="HEIGHT: 100%; WIDTH: 100%;Z-INDEX: 1;overflow-y:hidden;margin-left:78px;">
				</IFRAME>
			<div class="next"></div>
		</div>
		<div id="divList" class="channel" >
			<div class="pre"></div>
				<IFRAME frameBorder="0" scrolling="yes" src="wkfProcessAction.it?method=listPersonalApplyForm&comefrom=left&combin=true"
					style="HEIGHT: 100%; WIDTH: 100%;Z-INDEX: 1;overflow-y:hidden;margin-left:60px;">
				</IFRAME>
		</div>
	</div>
</div>
</body>
</html>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/NovaJS/slimscroll/jquery.slimscroll.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/NovaJS/main_jsp_common.js"></script>
