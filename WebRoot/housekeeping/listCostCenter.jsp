<%@page import="java.util.*,com.aiait.eflow.housekeeping.vo.*, com.aiait.eflow.common.helper.*,com.aiait.eflow.common.*" %>
<%@page import="com.aiait.framework.util.*" %>
<%@ taglib uri="/WEB-INF/purview.tld" prefix="purview" %>
<%@ page contentType="text/html;charset=GBK" pageEncoding="GBK"%>
<%@ include file="/common/loading.jsp" %>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@ taglib uri="/WEB-INF/taglibs-page.tld" prefix="pageTag" %>
<% 
  Collection list = (Collection) request.getAttribute("codeList");
  StaffVO currentStaff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
%>

<html>
<head>
	<title>Cost Center</title>
	  <script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
	<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
	<script src="<%=request.getContextPath()%>/js/webtools.js" language="JavaScript"></script>
	<script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
	<SCRIPT language="javascript" src="<%=request.getContextPath()%>/js/sorttable.js"></SCRIPT>
	<script language="javascript" src="<%=request.getContextPath()%>/js/resizeCol.js"></script>
	<script language="javascript" src="<%=request.getContextPath()%>/js/ajax.js"></script>
	<script language="javascript">
	  function listCode() {
	     var url = "<%=request.getContextPath()%>/costCenterAction.it?method=listCode";
	     document.forms[0].action = url;
	     document.forms[0].submit();
	  }

      function addCode() {
          var url = "<%=request.getContextPath()%>/costCenterAction.it?method=editCode&editType=new";
          window.location = url;
      }
      	  
	  function deleteCode() {
	    if (checkSelect('codeId') <= 0) {
	      alert("You have not selected any records to delete!");
	      return;
	    }
	    if (confirm("Are you sure to delete the selected records")) {
	      document.forms[0].action = "<%=request.getContextPath()%>/costCenterAction.it?method=deleteCode";
	      document.forms[0].submit();
	    }
	  }
	  function fileUpload(){
	      var url = "<%=request.getContextPath()%>/costCenterAction.it?method=showExcelTemplateSelect";
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
 	  <strong><font color='#5980BB' family='Times New Roman'><i18n:message key="housekeeping_costcenter.location"/></font></strong>
 	 </td>
 </tr>
--></table>
   <table width="100%" border="1" cellpadding="3" cellspacing="0" bordercolor="#6595D6" style="border-collapse:collapse;">
     <tr class="tr1">
       <td align='center' colspan="4"><b><i18n:message key="housekeeping_costcenter.title"/></b></td>
     </tr>
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
            <td align='center' ><i18n:message key="housekeeping_costcenter.costCenterCode"/></td>
            <td align='center' ><i18n:message key="housekeeping_costcenter.costCenterName"/></td>
            <td align='center' >EXCO</td>
            <td align='center' ><i18n:message key="housekeeping_user.team"/></td>
          </tr>
          <%
			if (list != null) {
				int i = 1;
				Iterator it = list.iterator();
				while (it.hasNext()) {
					CostCenterVO vo = (CostCenterVO) it.next();
          %>
            <tr class="tr_change">
              <td align='center' ><input type="checkbox" name="codeId" value="<%=vo.getCc_code()%>"></td>
              <td>
                <a href='<%=request.getContextPath()%>/costCenterAction.it?method=editCode&editType=edit&codeId=<%=CommonUtil.encoderURL(vo.getCc_code())%>'><%=vo.getCc_code()%></a>
              </td>
              <td><%=vo.getCc_name()%>&nbsp;</td>
              <td><%=StaffTeamHelper.getInstance().getStaffNameByCode(vo.getExco())%>&nbsp;</td>
              <td><%=vo.getT_code()==null ? "" : StaffTeamHelper.getInstance().getTeamNameByCode(vo.getT_code())%>&nbsp;</td>
            </tr>
          <%i++;}
          }%>
        </table>
      </td>
     </tr>
   </table>
<pageTag:page action="/costCenterAction.it?method=listCode"></pageTag:page>    
</form>
</body>
</html>
   <script language="javascript">
	setResizeAble(mytable);
 </script>