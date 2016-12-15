<%@page import="com.aiait.eflow.util.*, com.aiait.eflow.common.*, com.aiait.eflow.common.helper.*"%>
<%@page contentType="text/html;charset=GBK"%>
<%@taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n"%>
<%
    String url = request.getContextPath() + "/logonAction.it?method=logOn";
    String loginPage = request.getContextPath() + "/login.jsp?";

    boolean loginPageFlag = true;
    
    //Indicator to create new request form
    String newForm = (String) request.getParameter("newForm");
    if (newForm != null) {
        url = url + "&newForm=" + newForm;
        loginPage += "&newForm=" + newForm;
        loginPageFlag = false;
    }

    //default to open the menu item id
    String menuItemId = (String) request.getParameter("menuItemId");
    if (menuItemId != null) {
        url = url + "&menuItemId=" + menuItemId;
        loginPage += "&menuItemId=" + menuItemId;
        loginPageFlag = false;
    }
    //default to show the funtion page at the right
    String formSystemId = (String) request.getParameter("formSystemId");
    //DS-014 Begin
    String operateType = (String) request.getParameter("operateType");
    if (operateType != null) {
        url = url + "&operateType=" + operateType;
        loginPage += "&operateType=" + operateType;
        loginPageFlag = false;
    }
    //end DS-014
    if (formSystemId != null) {
        url = url + "&formSystemId=" + formSystemId;
        loginPage += "&formSystemId=" + formSystemId;
        loginPageFlag = false;
    }
    String requestNo = (String) request.getParameter("requestNo");
    if (requestNo != null) {
        url = url + "&requestNo=" + requestNo;
        loginPage += "&requestNo=" + requestNo;
        loginPageFlag = false;
    }
    
    if (loginPage.endsWith("?")) {
        loginPage = loginPage.substring(0, loginPage.length() - 1);
    }
    
    url = url.replaceAll("<","[");
    url = url.replaceAll(">","]");
    loginPage = loginPage.replaceAll("<","[");
    loginPage = loginPage.replaceAll(">","]");
    
    String byNTLM = "NTLM".equalsIgnoreCase(ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_SSO_TYPE)) ? "true" : "false";
%>
<html>
<head>
<script language="javascript" src="<%=request.getContextPath()%>/js/jquery.min.js"></script>
<script type="text/javascript">
    $(function() {
        if (navigator.appName.indexOf("Microsoft") != -1) {
            testSSO();
        } else {
            window.location = "<%=loginPage%>";
        }
    });

    function testSSO() {
        var url = "testSSO.jsp";
        if (_ntlm) {
            url += "?auth=ntlm";
        }
        $.ajax( {
            url : url,
            type : "POST",
            async : false,
            success : function(response, status, xhr) {
                var loginkey = xhr.getResponseHeader("loginkey");
                if (loginkey != null && $.browser.msie) {
                    document.execCommand("ClearAuthenticationCache", false);
                    window.location = "<%=url%>&loginkey=" + loginkey;
                } else {
                    window.location = "<%=url%>";
                }
            },
            error : function(xhr, status) {
                if (!_ntlm) {
                    _ntlm = true;
                    testSSO();
                } else {
                    window.location = "<%=loginPage%>";
                }
            }
        });
    };

    var _ntlm = <%=byNTLM%>;

    <%if ("1".equals(request.getParameter("IS_TIMEOUT"))) {%>
        alert("Session Time Out / 本次操作已超时");
    <%}%>
</script>
</head>
<body>
</body>
</html>
