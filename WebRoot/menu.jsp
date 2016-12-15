<%@ page contentType="text/html; charset=gb2312"%>
<%@page import="com.aiait.eflow.housekeeping.vo.StaffVO,com.aiait.eflow.common.CommonName,com.aiait.eflow.util.LeftMenuTreeUtil"%>
<%@page import="com.aiait.framework.i18n.*, com.aiait.eflow.common.helper.ParamConfigHelper"%>
<%@page import="java.util.*, com.aiait.framework.mvc.servlet.*"%>
<html>
<%
    StaffVO currentStaff = (StaffVO)request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
    String language = I18NMessageHelper.getLocale().getLanguage();
    String versionName="English";
    String targetLanguage="en";
    String languageName = "ÓïÑÔ";
    if(language!=null){
    	language = language.toLowerCase();
    }else{
    	language = CommonName.LOCALE_LANGUAGE_EN;
    }
    if(language.equals("en")){
    	versionName="ÖÐÎÄ°æ";
    	languageName = "ÓïÑÔ";
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
<style type=text/css>
body  {
background:#799AE1;
margin:0px;
font:normal 12px ËÎÌå; 
/*SCROLLBAR-FACE-COLOR: #1553C2; 
SCROLLBAR-HIGHLIGHT-COLOR: #1553C2; 
SCROLLBAR-SHADOW-COLOR: #1553C2; 
SCROLLBAR-DARKSHADOW-COLOR: #799AE1; 
SCROLLBAR-3DLIGHT-COLOR: #799AE1; 
SCROLLBAR-ARROW-COLOR: #FFFFFF;
SCROLLBAR-TRACK-COLOR: #AABFEC;*/
}
</style>
<script src="js/TaskMenu.js"></script>
<script src="js/jquery.min.js"></script>
<script language="javascript">
function relogon(){
  if(confirm("Are you sure to refresh your information")){
    window.parent.location = "<%=request.getContextPath()%>/logonAction.it?method=logOut";
  }
}

function changeLanguage(obj){
  window.parent.location = "<%=request.getContextPath()%>/logonAction.it?method=changeLanguage&language="+obj;
}

var taskMenu1;
var taskMenu2;
var taskMenu3;
var taskMenu4;

var item1;
var item2;
var item3;
var item4;
var item5;
var item6;
var item7;
var item8;
TaskMenu.setStyle("css/AIAStyle.css"); 

var contentLanguage;

jQuery().ready(function()
{
	TaskMenu.setHeadMenuSpecial(false);
	TaskMenu.setScrollbarEnabled(true);
	TaskMenu.setAutoBehavior(false);
	////////////////////////////////////////////////
	var cotent = "<div class=\"MyPage\"><B><%=currentStaff.getStaffName().trim()%></B>(<%=currentStaff.getLogonId()%>)<br/><br/>";

	//var cotent = "<div class=\"MyPage\">";
    var languageType ="<%=languageType%>";  

	//languageType = "<%=language%>";
    
    if(languageType =='' || languageType =='zh_Cn'){
		cotent = cotent+"&nbsp;&nbsp;&nbsp;&nbsp;<a href='javascript:changeLanguage(\"zh_Cn\")'><image alt='<%=I18NMessageHelper.getMessage("common.language.chinese")%>'  border='0' src='<%=request.getContextPath()%>/images/chinese.gif'></a>";
    }
    
	cotent = cotent+"&nbsp;&nbsp;<a href='javascript:changeLanguage(\"en\")'><image alt='<%=I18NMessageHelper.getMessage("common.language.english")%>' border='0' src='<%=request.getContextPath()%>/images/english.gif'></a></div>";
	item1 = new TaskMenuItem(cotent);

	//contentLanguage = content;
    //contentLanguage = content;
	////////////////////////////////////////////////
	
	taskMenu1 = new TaskMenu("<%=I18NMessageHelper.getMessage("menu.user_information")%>",0,"images/Home.png");
	taskMenu1.add(item1);
	
	//taskMenu1.add(item2,1);
	//taskMenu1.setBackground("Image/bg.gif");
	
	taskMenu1.init();
	
	//taskMenu2 = new TaskMenu("<%=I18NMessageHelper.getMessage("menu.user_information")%>");
	//taskMenu2.init();
	
    <%=LeftMenuTreeUtil.buildTree2(request)%>
    
    //jQuery(window.parent.document).find("#tdSwichBar").trigger('hover');
    if(jQuery(window.parent.document).find("#switchPoint").text()==3){
	   jQuery(window.parent.document).find("#switchPoint").text(4);
	   jQuery(window.parent.document).find("#frmTitle").css('display','none');
	   jQuery(window.parent.document).find("#switchPoint").attr('title','expand');
    }
	//document.getElementById("frmTitle").style.display="none";
});

</script>
</head>
</html>