<%@ include file="/common/head.jsp"%>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n"%>
<%@page
    import="com.aiait.eflow.housekeeping.vo.EflowStaffVO,java.util.*,com.aiait.eflow.housekeeping.vo.TeamVO,com.aiait.eflow.housekeeping.vo.TitleVO"%>
<%@page
    import="com.aiait.eflow.common.helper.*,com.aiait.eflow.housekeeping.vo.CompanyVO,com.aiait.eflow.common.CommonName,com.aiait.eflow.housekeeping.vo.StaffVO"%>
<%
    //
            String logonId = (String) request.getAttribute("logonId");
            String saveType = (String) request.getParameter("type");
            String platformName = ParamConfigHelper.getInstance().getParamValue("platform_name","E-Flow");
            if (saveType == null) {
                saveType = "new";
            }
            EflowStaffVO vo = null;
            if ("new".equals(saveType)) {
                vo = new EflowStaffVO();
            } else {
                vo = (EflowStaffVO) request.getAttribute("eflowuser");
            }
            
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>User Registration</title>
<style type="text/css">
.out {
    position: absolute;
    background: #bbb;
    margin: 10px auto;
    width: 500px;
    height: 100px;
    top: 20%;
    left: 30%;
    bottom: 30%;
    text-align: left;
}

.in {
    background: #fff;
    border: 1px solid #555;
    padding: 10px 5px;
    position: relative;
    top: -5px;
    left: -5px;
    vertical-align: middle;
}

</style>
<i18n:jsmessage jsRelativePath="js" prefixName="message" />
<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
<script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/calendar.js"></script>
<SCRIPT language="javascript" src="<%=request.getContextPath()%>/js/sorttable.js"></SCRIPT>
<script language="javascript" src="<%=request.getContextPath()%>/js/ajax.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/aiaitjsframe.js"></script>
<script language="javascript">
   function getTeamList(orgId) {
     var url = "<%=request.getContextPath()%>/teamManageAction.it?method=getTeamList&orgId=" + orgId;
     var param = "";
     var myAjax = new Ajax.Request(
         url,
         {
            method:"get",
            parameters:param,
            setRequestHeader:{"If-Modified-Since":"0"},
            onComplete:function(x) {
                // alert(x.responseText);
                updateListTeam(x.responseXML);
            },
            onError:function(x) {
                    alert('Failed to get team list for company');
            }
         }
     );
  }
  
	function submitForm() {
		var frm = document.forms[0];
      	if (formValidate(frm) == false) return;
      	if (document.all['email'].value!="" && checkEmail(document.all['email'],"Email") == false) {
  	        return;
  	    }
      	
      	var param = $.Form("userForm").serialize({regTime:new Date().toGMTString()});
      	// alert(param);
        param = encodeURI(param);
      	var url = "<%=request.getContextPath()%>/userManageAction.it?method=userRegisterSvc&" + param;
      	param = "";
        
        var myAjax = new Ajax.Request(
         url,
         {
            method:"get",
            parameters:param,
            setRequestHeader:{"If-Modified-Since":"0"},
            onComplete:function(x){
                alert(x.responseText);
                if ("Register success!" == x.responseText) {
                    window.location = "<%=request.getContextPath()%>/logonAction.it?method=logOn&logonId=<%=logonId%>";
                }
            },
            onError:function(x){
                alert("Failed to register user: " + x.responseText);
            }
         }
       );
	}
</script>
</head>
<body>
<div class="out">
<div class="in">
<form name="userForm" action="" method="post">
<input type="hidden" name="saveType" value="<%=saveType%>">
<table width="100%" border="1" cellpadding="3" cellspacing="0" bordercolor="#6595D6" style="border-collapse: collapse;">
    <tr class="tr1">
        <td align='center' colspan='2'><b><%=platformName%> - <i18n:message key="housekeeping_user.userregister" /></b></td>
    </tr>
    <tr>
        <td align="right" width="27%"><i18n:message key="housekeeping_user.logonid" /> :</td>
        <td align="left"><b><%=logonId%></b><input type="hidden" name="logonId" value="<%=logonId%>"></td>
    </tr>
    <tr>
        <td align="right"><i18n:message key="housekeeping_user.staffcode" /> :</td>
        <td><input type='text' name='staffCode' size='20' maxLength='10' title="Staff Code" required="true"
            value="<%=vo.getStaffCode() == null ? "" : vo.getStaffCode()%>"
            <%=!"new".equals(saveType) ? "readonly" : ""%>> (<font color='red'>*</font> Global Employee ID)</td>
    </tr>
    <tr>
        <td align="right"><i18n:message key="housekeeping_user.staffname" /> :</td>
        <td><input type='text' name='staffName' size='40' maxLength='40' title="Staff Name" required="true"
            value="<%=vo.getStaffName() == null ? "" : vo.getStaffName()
							.trim()%>"> (<font color='red'>*</font>)</td>
    </tr>
    <tr>
        <td align="right"><i18n:message key="housekeeping_user.chinesename" /> :</td>
        <td><input type='text' name='chineseName' size='40' maxLength='40'
            value="<%=vo.getChineseName() == null ? "" : vo.getChineseName()
					.trim()%>"></td>
    </tr>
    <tr>
        <td align="right"><i18n:message key="common.company" /> :</td>
        <td><select name="orgId" required="true" title="Company" onchange="getTeamList(this.value)">
            <option value=""></option>
            <%
                //
            			Collection companyList = CompanyHelper.getInstance().getCompanyList();
            			if (companyList != null && companyList.size() > 0) {
            				Iterator it = companyList.iterator();
            				while (it.hasNext()) {
            					CompanyVO company = (CompanyVO) it.next();
            					if (vo != null && vo.getOrgId() != null
            							&& company.getOrgId().equals(vo.getOrgId())) {
            						out.print("<option value='" + company.getOrgId()
            								+ "' selected>" + company.getOrgName()
            								+ "</option>");
            					} else {
            						out.print("<option value='" + company.getOrgId() + "'>"
            								+ company.getOrgName() + "</option>");
            					}
            				}
            			}
            %>
        </select> (<font color='red'>*</font>)</td>
    </tr>
    <tr>
        <td align="right"><i18n:message key="housekeeping_user.team" /> :</td>
        <td><select name="team_code" title='Team / Department' required='true'>
            <option value="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
        </select> (<font color='red'>*</font>)</td>
    </tr>
    <tr>
        <td align="right"><i18n:message key="housekeeping_user.email" /> :</td>
        <td><input type='text' name='email' title="Email" required="true" size="40" maxLength='40'
            value="<%=(vo.getEmail() == null ? "" : vo.getEmail().trim())%>"> (<font color='red'>*</font>)</td>
    </tr>
    <tr>
        <td align="center" colspan="2"><input type="button" name="submitBtn"
            value='<i18n:message key="button.submit"/>' onclick="submitForm()" class=btn3_mouseout
            onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
            onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">&nbsp;&nbsp;&nbsp;
            <input type="reset" name="resetBtn" value='<i18n:message key="button.reset"/>'
            class=btn3_mouseout onmouseover="this.className='btn3_mouseover'"
            onmouseout="this.className='btn3_mouseout'" onmousedown="this.className='btn3_mousedown'"
            onmouseup="this.className='btn3_mouseup'"></td>
    </tr>
</table>
</form>
</div>
</div>
</body>
</html>
