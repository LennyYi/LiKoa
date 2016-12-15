<%@ page contentType="text/html; charset=gb2312"%>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@page import="java.util.*,com.aiait.eflow.housekeeping.vo.*,com.aiait.eflow.common.helper.*,com.aiait.framework.i18n.*" %>
<%@page import="com.aiait.eflow.housekeeping.vo.StaffVO,com.aiait.eflow.common.*,com.aiait.eflow.formmanage.vo.*"%>

<html>
<head>
<title>Add delegation</title>
  <script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
<link href="<%=request.getContextPath()%>/css/TreeView.css" rel ="stylesheet" type="text/css">
<script language="JavaScript" src="<%=request.getContextPath()%>/js/tv.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/ajax.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/aiaitjsframe.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/calendar.js"></script>
<script language="javascript">
demo = new TreeView();
demo.setImagePath("images/purview/");
demo.add(9999,0,"All Forms","javascript:void(0)",null,"checkall",null,true);
<%
ArrayList formTypeList = (ArrayList) request.getAttribute("formTypeList");
HashMap formMap = (HashMap) request.getAttribute("formMap");
boolean isRoot = true;
int parentId = 9999;

int treeSize = formTypeList.size();
int value = -10000;

Iterator formIter = null;

for (int i = 0; i < treeSize; i++) {
	FormTypeVO formType = (FormTypeVO)formTypeList.get(i);

	if(formMap.get(formType.getFormTypeId())!=null){	
%>			
		demo.addcheck(<%=formType.getFormTypeId()%>,<%=parentId%>,"<%=formType.getFormTypeName()%>","javascript:void(0)",null,"checktype","<%=formType.getFormTypeId()%>_<%=formType.getFormTypeName()%>",true);
<% 	}
	if(formMap.get(formType.getFormTypeId())!=null){

		formIter = ((Collection)formMap.get(formType.getFormTypeId())).iterator();
	 	while(formIter.hasNext()){
	 		FormManageVO form = (FormManageVO)formIter.next();
	    	String valueStr = ""+ formType.getFormTypeId() + "_" + form.getFormSystemId() + "_" + form.getFormName();
%>
			demo.addcheck(<%=value++%>,<%=formType.getFormTypeId()%>,"<%=form.getFormId()+" "+form.getFormName().trim()%>","javascript:void(0)",null,"checkbox","<%=valueStr%>",true);
<%
    	}//end while
   	}
  }//end for 
  
  StaffVO currentStaff = (StaffVO)request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);	
  String role = currentStaff.getCurrentRoleId();
  String maxDay = null;
  if(!"".equals(ParamConfigHelper.getInstance().getParamValue("role_delegate_day"))){
  	String[] roleDelegateDay = ParamConfigHelper.getInstance().getParamValue("role_delegate_day").split(";");
  	for(int i=0;i<roleDelegateDay.length;i++){
  		String[] tmp = roleDelegateDay[i].split("=");
  		if(role.equals(tmp[0]) && tmp.length>1){
  			maxDay = tmp[1];
  			break;
  		}
  	}
  }
  if(maxDay==null)maxDay = "5";
%>
  function selectCompany(type) {
      var url = "<%=request.getContextPath()%>/teamManageAction.it?method=getTeamList";

      var companyCode;
      if (type == "from") {
          companyCode = document.getElementById("approverCompany").value;
      } else {
          companyCode = document.getElementById("deputyCompany").value;
      }
  	  var param = "orgId=" + companyCode + "&enableSearchAll=true";
      
   	  var myAjax = new Ajax.Request(
          url,
          {
          method:"get",
          parameters:param,
          asynchronous:false,
          setRequestHeader:{"If-Modified-Since":"0"},
          onComplete:function(x) {
              var select;
              if (type == "from") {
                  select = document.getElementById("approverTeam");
              } else {
                  select = document.getElementById("deputyTeam");
              }
              clearSelect(select);
              select.appendChild(createOptionEle("All", "", true));
              var options = x.responseXML.getElementsByTagName("team");
              for (var i = 0; i < options.length; i++) {
                  select.appendChild(createElementWithValue(options[i], "code"));
              }
              selectTeam(type);
          },
          onError:function(x) {
                  alert('Failed to get the team list!');
          }
          }
      );
  }

  function selectTeam(type) {
      var url = "<%=request.getContextPath()%>/userManageAction.it?method=searchStaffByName";

      var companyCode;
      var teamCode;
      if (type == "from") {
          companyCode = document.getElementById("approverCompany").value;
          teamCode = document.getElementById("approverTeam").value;
      } else {
          companyCode = document.getElementById("deputyCompany").value;
          teamCode = document.getElementById("deputyTeam").value;
      }
      var param = "orgId=" + companyCode + "&teamCode=" + teamCode;

   	  var myAjax = new Ajax.Request(
          url,
          {
          method:"get",
          parameters:param,
          asynchronous:false,
          setRequestHeader:{"If-Modified-Since":"0"},
          onComplete:function(x) {
              var select;
              if (type == "from") {
                  select = document.getElementById("authorityApprover");
              } else {
                  select = document.getElementById("authorityDeputy");
              }
              clearSelect(select);
              var options = x.responseXML.getElementsByTagName("staff");
              for (var i = 0; i < options.length; i++) {
                  select.appendChild(createElementWithValue(options[i], "code"));
              }
          },
          onError:function(x) {
                  alert('Failed to get the staff list!');
          }
          }
      );
  }

  function submitForm() {
      if (document.all.authorityApprover.value == "") {
          alert("Please select the 'Staff Name'");
          document.all.authorityApprover.focus();
          return;
      }
      if (document.all.authorityDeputy.value == "") {
          alert("Please select the 'Deputy Name'");
          document.all.authorityDeputy.focus();
          return;
      }
      if (document.all.delegateFrom.value == "") {
          alert("Please select the 'Delegate From' date");
          document.all.delegateFrom.focus();
          return;
      }
      if (document.all.delegateTo.value == "") {
          alert("Please select the 'Delegate To' date");
          document.all.delegateTo.focus();
          return;
      }
      if (!compareDate(document.all.delegateFrom.value, document.all.delegateTo.value)) {
          alert("'Delegate From' date must be earlier or equal to 'Delegate To' date");
          document.all.delegateFrom.focus();
          return;
      }
	  document.forms[0].submit();	
  }

</script>
<body>
<%
  Collection companyList = CompanyHelper.getInstance().getCompanyList();
	  // (Collection) request.getAttribute("companyList");
  Collection teamList = (Collection) request.getAttribute("teamList");
  Collection staffList = (Collection) request.getAttribute("staffList");
  String type = request.getParameter("type");
  if (type == null) {
	  type = "self";
  }
  //StaffVO currentStaff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
%>
<FORM nane=myForm method="post" action="<%=request.getContextPath()%>/delegateAction.it?method=saveDelegation"> 
<%if(CompanyHelper.EFlow_AIA_CHINA.equals(CompanyHelper.getInstance().getEFlowCompany())){%>
<input type=hidden name='maxDay' value="<%=maxDay%>">
<%}%>
<table border="0" width="100%" cellspacing="0" cellpadding="0">
 <tr>
 	<td height="10"></td>
 </tr>
 <!--<tr>
 	<td>
 	  <strong><font color='#5980BB' family='Times New Roman'><i18n:message key="deletate_add.location"/></font></strong>
 	 </td>
 </tr>-->
</table>
<TABLE WIDTH="100%" bordercolor="#6595D6" style="border-collapse:collapse;"  BORDER=1 CELLPADDING=3 CELLSPACING=0 class="tr0" >
   <TR align="center" >
      <TD height="25" colspan="4" class="tr1"><i18n:message key="deletate_add.title"/></TD>
   </TR>
   <TR>
      <TD width="15%" height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="deletate_add.company"/></span></div></TD>
      <TD width="35%" height="20">
      <%if("self".equals(type)){%>
        <select name="approverCompany1" disabled>
          <option value=""><%=currentStaff.getOrgName()%></option>
        </select>
        <input type="hidden" name="approverCompany" value="<%=currentStaff.getOrgId()%>">
      <%}else{%>
        <select id="approverCompany" name="approverCompany" onchange="selectCompany('from')">
         <%
           if (companyList != null) {
               Iterator it = companyList.iterator();
               while (it.hasNext()) {
                   CompanyVO company = (CompanyVO) it.next();
                   String selected = currentStaff.getOrgId().equals(company.getOrgId()) ? "selected" : "";
                   out.print("<option value='" + company.getOrgId() + "' " + selected + ">" + company.getOrgName() + "</>");
               }
           }
         %>
        </select>
       <%}%>
      </TD>
      <TD width="15%" height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="deletate_add.deputycompany"/></span></div></TD>
      <TD width="35%" height="20">
        <select id="deputyCompany" name="deputyCompany" onchange="selectCompany('to')">
         <%
           if (companyList != null) {
               Iterator it = companyList.iterator();
               while (it.hasNext()) {
                   CompanyVO company = (CompanyVO) it.next();
                   String selected = currentStaff.getOrgId().equals(company.getOrgId()) ? "selected" : "";
                   out.print("<option value='" + company.getOrgId() + "' " + selected + ">" + company.getOrgName() + "</>");
               }
           }
         %>
        </select>
      </TD>
   </TR>
   <TR> 
      <TD width="15%" height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="deletate_add.team"/></span></div></TD>
      <TD width="35%" height="20" > 
      <%if("self".equals(type)){%>
        <select name="approverTeam1" disabled>
          <option value=""><%=currentStaff.getTeamName()%></option>
        </select>
        <input type="hidden" name="approverTeam" value="<%=currentStaff.getTeamCode()%>">
      <%}else{%>
        <select id="approverTeam" name="approverTeam" onchange="selectTeam('from')">
         <option value="">All</option>
         <%
           if (teamList != null) {
        	   Iterator teamIt = teamList.iterator();
        	   while (teamIt.hasNext()) {
        		   TeamVO team = (TeamVO) teamIt.next();
        		   out.print("<option value='" + team.getTeamCode() + "'>" + team.getTeamName().trim() + "</>");
        	   }
           }
         %>
        </select>
       <%}%>
      </TD>
      <TD width="15%" height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="deletate_add.deputyteam"/></span></div></TD>
      <TD width="35%" height="20">
        <select id="deputyTeam" name="deputyTeam" onchange="selectTeam('to')">
         <option value="">All</option>
         <%
           if (teamList != null) {
               Iterator teamIt = teamList.iterator();
               while (teamIt.hasNext()) {
                   TeamVO team = (TeamVO) teamIt.next();
                   out.print("<option value='" + team.getTeamCode() + "'>" + team.getTeamName().trim() + "</>");
               }
           }
         %>
        </select>
      </TD>
    </TR>
   <TR> 
      <TD width="15%" height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="deletate_add.staffname"/></span></div></TD>
      <TD width="35%" height="20" >
      <%if("self".equals(type)){%>
        <select name="authorityApprover1" disabled>
          <option value=""><%=currentStaff.getStaffName()%></option>
        </select>
        <input type="hidden" name="authorityApprover" value="<%=currentStaff.getStaffCode()%>">
      <%}else{%>
       <select id="authorityApprover" name="authorityApprover">
         <%
           if (staffList != null) {
               Iterator staffIt = staffList.iterator();
               while (staffIt.hasNext()) {
                   StaffVO staff = (StaffVO) staffIt.next();
                   out.print("<option value='" + staff.getStaffCode() + "'>" + staff.getStaffName().trim() + "</>");
               }
           }
         %>
         </select>
       <%}%>
       (<font color='red'>*</font>)
      </TD>
      <TD width="15%" height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="deletate_add.deputyname"/></span></div></TD>
      <TD width="35%" height="20" > 
        <select name="authorityDeputy" id="authorityDeputy">
         <%
           if (staffList != null) {
        	   Iterator staffIt = staffList.iterator();
        	   while (staffIt.hasNext()) {
        		   StaffVO staff = (StaffVO) staffIt.next();
        		   out.print("<option value='" + staff.getStaffCode() + "'>" + staff.getStaffName().trim() + "</>");
        	   }
           }
         %>
        </select>(<font color='red'>*</font>)
      </TD>
    </TR>
    <TR> 
      <TD width="15%" height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="deletate_add.delegatefrom"/></span></div></TD>
      <TD width="35%" height="20" > 
        <input type="text" readonly id="delegateFrom" name="delegateFrom" onclick='setday(this)'>(MM/DD/YYYY)(<font color='red'>*</font>)
      </TD>
      <TD width="15%" height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="deletate_add.delegateto"/></span></div></TD>
      <TD width="35%" height="20" >
        <input type="text" readonly id="delegateTo" name="delegateTo" onclick='setday(this)' >(MM/DD/YYYY)(<font color='red'>*</font>)
      </TD>
    </TR>
    <tr>
		<td  class="tr3" height="20" width="12%"><div align="right"> 
	          <i18n:message key="housekeeping_role.pleaseselect"/></div>
	         </td>
		<td height=20  class="tbtext" colspan=3>	      
	     <script language="JavaScript">
	                        <!--
	                           demo.setCheckbox(true);
	                           demo.setName = "gns";
	                           document.write(demo);
	                           //-->
	     </script>	
		</td>
  	</tr>
<%if(CompanyHelper.EFlow_AIA_CHINA.equals(CompanyHelper.getInstance().getEFlowCompany())){%>
  	<tr>
  		<td  class="tr3" height="20" width="12%"><div align="right"> 
	          	请选择适用分公司表单（全不选则适用于所有表单）</div>
	    </td>
		<td height=20  class="tbtext" colspan=3>
         <%
           if (companyList != null) {
               Iterator it = companyList.iterator();
               while (it.hasNext()) {
                   CompanyVO company = (CompanyVO) it.next();
                   out.print("<input type='checkbox' id='applyOrgId' name='applyOrgId' value='" 
                		   + company.getOrgId() + "'>" + company.getOrgName() + "<br>");
               }
           }
         %> 
		</td>
  	</tr>
<%}%>
    <tr>
      <td colspan='4' align='center'>
         <input type="button" name="submitBtn" value='<i18n:message key="button.submit"/>' onclick="submitForm()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">&nbsp;&nbsp;
         <input type="button" name="btBtn" value='<i18n:message key="button.back"/>' onclick="javascript:window.history.go(-1)" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
      </td>
    </tr>	
 </TABLE>
</FORM>
</body>
</html>
<script language="JavaScript">
   demo.checkoriginal(0);
   demo.closeAll(0);
</script>