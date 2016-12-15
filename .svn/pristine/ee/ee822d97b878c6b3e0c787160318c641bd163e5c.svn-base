<%@page import="java.util.*,com.aiait.eflow.housekeeping.vo.*, com.aiait.eflow.common.helper.*,com.aiait.eflow.common.*" %>
<%@page import="com.aiait.framework.util.*" %>
<%@ taglib uri="/WEB-INF/purview.tld" prefix="purview" %>
<%@ page contentType="text/html;charset=GBK" pageEncoding="GBK"%>
<%@ include file="/common/loading.jsp" %>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@ taglib uri="/WEB-INF/taglibs-page.tld" prefix="pageTag" %>
<% 
  String orgId = (String) request.getAttribute("orgId");
  Collection list = (Collection) request.getAttribute("codeList");
  StaffVO currentStaff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
  boolean isAdmin = false;
  AuthorityHelper authority = AuthorityHelper.getInstance();
  if(authority.checkAuthority(currentStaff.getCurrentRoleId(),ModuleOperateName.MODULE_AUTHORITY_MANAGE,ModuleOperateName.OPER_AUTHORITY_MANAGE_ADD)){
      isAdmin = true;
  }
%>

<html>
<head>
	<title>Finance Code</title>
	  <script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
	<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
	<script src="<%=request.getContextPath()%>/js/webtools.js" language="JavaScript"></script>
	<script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
	<SCRIPT language="javascript" src="<%=request.getContextPath()%>/js/sorttable.js"></SCRIPT>
	<script language="javascript" src="<%=request.getContextPath()%>/js/resizeCol.js"></script>
	<script language="javascript" src="<%=request.getContextPath()%>/js/ajax.js"></script>
	<script language="javascript">
	  function listCode() {
	     var url = "<%=request.getContextPath()%>/financeCodeAction.it?method=listCode&flg=<%=(String)request.getAttribute("flg")%>";
	     document.forms[0].action = url;
	     document.forms[0].submit();
	  }

      function addCode() {
          var url = "<%=request.getContextPath()%>/financeCodeAction.it?method=editCode&editType=new&flg=<%=(String)request.getAttribute("flg")%>" + "&orgId=<%=orgId%>";
          window.location = url;
      }
      	  
	  function deleteCode() {
	    if (checkSelect('codeId') <= 0) {
	      alert("You have not selected any records to delete!");
	      return;
	    }
	    if (confirm("Are you sure to delete the selected records")) {
	      document.forms[0].action = "<%=request.getContextPath()%>/financeCodeAction.it?method=deleteCode&flg=<%=(String)request.getAttribute("flg")%>";
	      document.forms[0].submit();
	    }
	  }
	  function fileUpload(){
	      var url = "<%=request.getContextPath()%>/financeCodeAction.it?method=showExcelTemplateSelect&flg=<%=(String)request.getAttribute("flg")%>" + "&orgId=<%=orgId%>";
	      openCenterWindow(url,450,100);
	  }
	</script>
</head>

<body>
<form name="myForm" method="post">
<table border="0" width="100%" cellspacing="0" cellpadding="0">
 <tr>
 	<td height="10"></td>
 </tr>
 <!--<tr>
 	<td>
 	  <strong><font color='#5980BB' family='Times New Roman'><i18n:message key="housekeeping_financecode.location"/></font></strong>
 	 </td>
 </tr>
--></table>
   <table width="100%" border="1" cellpadding="3" cellspacing="0" bordercolor="#6595D6" style="border-collapse:collapse;">
     <tr class="tr1">
       <td align='center' colspan="4"><b><i18n:message key="housekeeping_financecode.title"/> 
       <%if("T6".equals((String)request.getAttribute("flg").toString().toUpperCase()))
			out.write("DB CODE");
		else 
    	   out.write((String)request.getAttribute("flg").toString().toUpperCase());
       %></b></td>
     </tr>
     <%if ("T2".equals(((String)request.getAttribute("flg")).toUpperCase())) {
     %>
     <tr> 
        <td width=15% height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="common.company"/></span></div></td>
        <td width=35% height="20" > 
         <select id="orgId" name="orgId" onchange="listCode()" <%= isAdmin ? "" : "disabled" %>>
           <%
            Collection companyList = currentStaff.getOwnCompanyList();
            if (companyList != null && companyList.size() > 0) {
                Iterator it = companyList.iterator();
                while (it.hasNext()) {
                    CompanyVO vo = (CompanyVO) it.next();
                    if (vo.getOrgId().equals(orgId)) {
                        out.print("<option value='" + vo.getOrgId() + "' selected>" + vo.getOrgName() + "</option>");   
                    } else {
                        out.print("<option value='" + vo.getOrgId() + "'>" + vo.getOrgName() + "</option>");
                    }
                }
            }
           %>
         </select>
        </td>
        <td width=15% height="20" class="tr3"><div align="right"><span class="style1"></span></div></td>
        <td width=35% height="20"></td>
    </tr>
    <%}%>
     <tr>
       <td align='center' colspan="4">
		<input type="button" name="addBtn" value='<i18n:message key="button.add"/>' onclick="addCode()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'"/>
		&nbsp;
		<input type="button" name="deleteBtn" value='<i18n:message key="button.delete"/>' onclick="deleteCode()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'"/>
		&nbsp;
		<input type="button" name="uploadBtn1" value='<i18n:message key="button.selectFile"/>' onclick="fileUpload()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'"/>
        &nbsp;
        <input type="button" name="refreshBtn" value='<i18n:message key="button.refresh"/>' onclick="listCode()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'"/>  
       </td>
     </tr>
     <tr>
      <td colspan="4">
        <table width="100%"  border="0" cellpadding="0" cellspacing="1" class=sortable id=mytable style="border:1px #8899cc solid;">
          <tr class="liebiao_tou">
            <td align='center' width="30"><input type="checkbox" name="allBtn" onclick="selectAll(this,'codeId')"></td>
            <td align='center' ><i18n:message key="housekeeping_financecode.code"/></td>
            <td align='center' ><i18n:message key="housekeeping_financecode.name"/></td>
          </tr>
          <%
			if (list != null) {
				int i = 1;
				Iterator it = list.iterator();
				while (it.hasNext()) {
				    FinanceCodeVO vo = (FinanceCodeVO) it.next();
          %>
            <tr class="tr_change">
              <td align='center' ><input type="checkbox" name="codeId" value="<%=vo.getOrgId() + "_" + vo.getCode()%>"></td>
              <td>
                <a href='<%=request.getContextPath()%>/financeCodeAction.it?method=editCode&editType=edit&flg=<%=(String)request.getAttribute("flg")%>&codeId=<%=CommonUtil.encoderURL(vo.getOrgId() + "_" + vo.getCode())%>'><%=vo.getCode()%></a>
              </td>
              <td><%=vo.getName()%>&nbsp;</td>
            </tr>
          <%i++;}
          }%>
        </table>
      </td>
     </tr>
   </table>
<pageTag:page action="/financeCodeAction.it?method=listCode"></pageTag:page>    
</form>
</body>
</html>
   <script language="javascript">
	setResizeAble(mytable);
 </script>