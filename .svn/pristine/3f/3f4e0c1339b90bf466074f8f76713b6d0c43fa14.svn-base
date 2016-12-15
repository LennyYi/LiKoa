<!-- Task_ID	Author	Modify_Date	Description------------------->
<!-- IT0973		Young	 12/28/2007 For User Management Function-->
<!-- IT1029	    Young	 06/30/2008	Team filter values vary depending on company filter-->
<%@ include file="/common/head.jsp" %>
<%@ include file="/common/loading.jsp" %>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@page import="java.util.*,com.aiait.eflow.housekeeping.vo.EflowStaffVO,com.aiait.eflow.housekeeping.vo.TeamVO,com.aiait.eflow.housekeeping.vo.CompanyVO,com.aiait.eflow.housekeeping.vo.StaffVO,com.aiait.eflow.util.StringUtil,com.aiait.eflow.common.helper.*,com.aiait.eflow.common.*" %>
<%@ taglib uri="/WEB-INF/purview.tld" prefix="purview" %>
<%@ taglib uri="/WEB-INF/taglibs-page.tld" prefix="pageTag" %>
<html>
<head>
<title>User Management</title>
<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
<script language="javascript" src="<%=request.getContextPath()%>/js/calendar.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
<SCRIPT language="javascript" src="<%=request.getContextPath()%>/js/sorttable.js"></SCRIPT>
<script language="javascript" src="<%=request.getContextPath()%>/js/ajax.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/aiaitjsframe.js"></script>
<script language="javascript">

	function getOptionList(orgId){
    	var url = "<%=request.getContextPath()%>/teamManageAction.it?method=getTeamList";
     	var param = "orgId="+orgId;
     	
     	var myAjax = new Ajax.Request(
         url,
         {
            method:"post",       //
            parameters:param,   //
            setRequestHeader:{"If-Modified-Since":"0"},     //
            onComplete:function(x){    //
                   updateListTeam(x.responseXML);
            },
            onError:function(x){          //
                    alert('Fail to get team list for company');
            }
         }
       ); 
  	}

    function changeUserType(obj){      
      var userType = obj.value;
      if(userType=="1"){ //temp
        document.all['beginDate'].disabled = "";
        document.all['endDate'].disabled = "";
      }else{
        document.all['beginDate'].disabled = "true";
        document.all['endDate'].disabled = "true";
      }
    }
    
 	function searchUser(){
		document.forms[0].action = "<%=request.getContextPath()%>/userManageAction.it?method=searchUser"
		                           +"&recursionteam="+document.forms[0].recursionteam.checked;
		document.forms[0].submit();
	}

 	function exportUser(){
		document.forms[0].action = "<%=request.getContextPath()%>/userManageAction.it?method=exportInquiryUser"
		                           +"&recursionteam="+document.forms[0].recursionteam.checked;
		document.forms[0].target = "_blank";
		document.forms[0].submit();
		document.forms[0].target = "";
	}
	 
	function addUser(){
 		var url = "<%=request.getContextPath()%>/userManageAction.it?method=enterEditUser&type=new&moduleId=<%=ModuleOperateName.MODULE_USER_MANAGE%>&operateId=<%=ModuleOperateName.OPER_USER_ADD%>";
 		window.location = url;
	}	
  
	function deleteUser(){
		if(checkSelect('staffcodeId')<=0){
      		alert("You have not selected any users to delete!");
      		return;
    	}
    	if(confirm("Are you sure to delete the selected users")==false){
       		return;
    	}
      var url = "<%=request.getContextPath()%>/userManageAction.it?method=deleteUsers&moduleId=<%=ModuleOperateName.MODULE_USER_MANAGE%>&operateId=<%=ModuleOperateName.OPER_USER_DELETE%>";
      document.forms['deleteForm'].action = url;
      document.forms['deleteForm'].submit();
	}

    function uploadLeaveBalance() {
        var url = "<%=request.getContextPath()%>/leaveBalanceAction.it?method=selectUploadFile";
	    openCenterWindow(url, 450, 100);
    }
  
</script>
</head>
<%
StaffVO currentStaff = (StaffVO)request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
	String staffcode = (String)request.getParameter("staffCode");
    String logonId = (String)request.getParameter("logonId");
    String teamId = (String)request.getParameter("team_code");
    String staffname = (String)request.getParameter("staffname");
    String status = (String)request.getParameter("status");
    String email = (String)request.getParameter("email");
    String recursionteam = (String)request.getParameter("recursionteam");
    String usertype = (String)request.getParameter("usertype");
    String beginDate = (String)request.getParameter("beginDate");
    String endDate = (String)request.getParameter("endDate");
    String orgId = (String)request.getParameter("orgId");
    String chinesename = (String)request.getParameter("chinesename");
    
    //if (orgId==null || "".equals(orgId)){
    //	orgId = currentStaff.getOrgId();
   // }
    
	Iterator userIt=null;
	Iterator teamIt=null;
    if (request.getAttribute("eflowuserList")!=null){
		Collection userList = (ArrayList)request.getAttribute("eflowuserList");
		userIt = userList.iterator();
    }
    //if (request.getAttribute("teamList")!=null){
	//	Collection teamList = (ArrayList)request.getAttribute("teamList");
	//	teamIt = teamList.iterator();
   // }
   Collection teamList = StaffTeamHelper.getInstance().getTeamList();
   if(teamList!=null){
	   teamIt = teamList.iterator();
   }
   
   Collection companyList = (ArrayList)request.getAttribute("companyList");
	
%>
<body>
<table border="0" width="100%" cellspacing="0" cellpadding="0">
	<tr>
 		<td height="10"></td>
	</tr>
 	<!--<tr>
 		<td>
 	  		<strong><font color='#5980BB' family='Times New Roman'><i18n:message key="housekeeping_user.location"/></font></strong>
	 	</td>
	</tr>
--></table>

<form name="myForm" method="post">
<input type="hidden" name="vstaffCode" value="">
<TABLE WIDTH=100% bordercolor="#6595D6" style="border-collapse:collapse;"  BORDER=1 CELLPADDING=3 CELLSPACING=0 class="tr0" >
	<tr class="tr1">
		<td align='center' colspan='4'><B><i18n:message key="housekeeping_user.toptitle"/></B></td>
	</tr>
	
	<tr> 
		<TD width=15% height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="housekeeping_user.staffcode"/></span></div></TD>
		<TD width=35% height="20" > 
			<input type="text"  value="<%=staffcode==null?"":staffcode%>" name="staffCode" >
		</TD>
      	<TD width=15% height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="housekeeping_user.logonid"/></span></div></TD>
      	<TD width=30% height="20">
         	<input type="text"  value="<%=logonId==null?"":logonId%>" name="logonId">
		</TD>
    </tr>
    
    <tr>
    	<TD width=15% height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="common.company"/></span></div></TD>
      	<TD width=30% height="20">
         <select name="orgId" required="true" title="Company" onchange="getOptionList(this.value)">
          <option value=''></option>
            <%
            
            if(companyList!=null && companyList.size()>0){
            	Iterator it = companyList.iterator();
            	while(it.hasNext()){
            	   CompanyVO company = (CompanyVO)it.next();
            	   if(company.getOrgId().equals(orgId)){
            		   out.print("<option value='"+company.getOrgId()+"' selected>" + company.getOrgName()+"</option>");   
            	   }else{
            	     out.print("<option value='"+company.getOrgId()+"'>" + company.getOrgName()+"</option>");
            	   }
            	}
            }
           %>
         </select>
		</TD>
		<TD width=15% height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="housekeeping_user.team"/></span></div></TD>
		<TD width=35% height="20"> 
         <select name="team_code">
           <option value=""></option>
           <%
           		if(teamIt!=null){           			
           			while(teamIt.hasNext()){
           				TeamVO tvo = (TeamVO)teamIt.next();
           				if(orgId!=null && !"".equals(orgId) && !orgId.equals(tvo.getOrgId())){
           					continue;
           				}
           %>
           <option value="<%=tvo.getTeamCode()%>" <%=(teamId!=null&&teamId.equals(tvo.getTeamCode()))?"selected":""%>><%=tvo.getTeamName()%></option>
           <%
           			}
           		}
           %>
         </select>
         <input type="checkbox" name="recursionteam" <%="true".equals(recursionteam)?"checked":""%> > <i18n:message key="housekeeping_user.recursion"/>
		</TD>
      	
    </tr>
    
    <tr>
    	<TD width=15% height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="housekeeping_user.staffname"/></span></div></TD>
      	<TD width=35% height="20" nowrap>
        	<input type="text"  value="<%=staffname==null?"":staffname%>" name="staffname">
        <%
        if (CompanyHelper.getInstance().getEFlowCompany().equals(
                CompanyHelper.EFlow_AIA_CHINA)) {
        %><i18n:message key="housekeeping_user.acnt_name"/>
        	<input type="text"  value="<%=chinesename==null?"":chinesename%>" name="chinesename">
        <%} %>
      	</TD> 
      	<TD width=15% height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="housekeeping_user.status"/></span></div></TD>
      	<TD width=35% height="20" > 
         <select name="status">    
           <option value="" <%="".equals(status)?"selected":""%>></option>
           <option value="A" <%="A".equals(status)?"selected":""%>>Active</option>
           <option value="T" <%="T".equals(status)?"selected":""%>>Terminated</option>
         </select>         
      	</TD>
      	
    </tr>
    
    <tr> 
    	<TD width=15% height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="housekeeping_user.email"/></span></div></TD>
      	<TD width=30% height="20">
         	<input type="text"  value="<%=email==null?"":email%>" name="email">
		</TD>
      	<TD width=15% height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="housekeeping_user.usertype"/></span></div></TD>
      	<TD width=35% height="20" > 
         <select name="usertype" onchange="changeUserType(this)">
           <option value="-1"></option>
           <option value="0" <%="0".equals(usertype)?"selected":""%>>Permanent</option>
           <option value="1" <%="1".equals(usertype)?"selected":""%>>Temporary</option>
         </select>         
      	</TD>      	      	           	
    </tr>
    
    <tr> 
		<TD width=15% height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="housekeeping_user.tempfrom"/></span></div></TD>
		<TD width=35% height="20" > 
			<input type="text"  name="beginDate" onclick='setday(this)' value="<%=beginDate==null?"":beginDate%>">(mm/dd/yyyy)
		</TD>
      	<TD width=15% height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="housekeeping_user.tempto"/></span></div></TD>
      	<TD width=30% height="20">
         	<input type="text"  name="endDate" onclick='setday(this)' value="<%=endDate==null?"":endDate%>">(mm/dd/yyyy)
		</TD>
    </tr>
    <tr>
		<td align='center' colspan='4'>
         	<input type="button" name="searchBtn" value='<i18n:message key="button.search"/>' onclick="searchUser()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           		onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">&nbsp;&nbsp;
           	<input type="button" name="exportBtn" value='<i18n:message key="button.export_excel"/>' onclick="exportUser()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           		onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">&nbsp;&nbsp;
			<input type="Reset" name="resetBtn" value='<i18n:message key="button.reset"/>' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           		onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">&nbsp;&nbsp;        
			<purview:purview moduleId='<%=ModuleOperateName.MODULE_USER_MANAGE%>' operateId='<%=ModuleOperateName.OPER_USER_ADD%>' isButton='true' labelValue='Add'>
			  <input type="button" name="addBtn" value='<i18n:message key="button.add"/>' onclick="addUser()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           		onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
            </purview:purview>
           &nbsp;&nbsp;        
		   <purview:purview moduleId='<%=ModuleOperateName.MODULE_USER_MANAGE%>' operateId='<%=ModuleOperateName.OPER_USER_DELETE%>' isButton='true' labelValue='Delete'>	
			<input type="button" name="deleteBtn" value='<i18n:message key="button.delete"/>' onclick="deleteUser()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           		onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
           </purview:purview>		
           &nbsp;&nbsp;
           <%
             if ("Yes".equalsIgnoreCase(ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_LEAVE_BALANCE))) {
           %>
            <purview:purview moduleId='<%=ModuleOperateName.MODULE_USER_MANAGE%>' operateId='<%=ModuleOperateName.OPER_USER_LEAVE_MAINTAIN%>' isButton='true' labelValue='Upload Leave Balance'>	
			<input type="button" name="deleteBtn" value='<i18n:message key="housekeeping_leavebalance.upload_leave_balance"/>' onclick="uploadLeaveBalance()" class="btn3_mouseout" onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           		onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
            </purview:purview>
           <%}%>
       	</td>
	</tr>
</TABLE>
</form>


<form name="deleteForm" method="post">
<table width="100%"  border="0" cellpadding="0" cellspacing="1" class=sortable id=mytable  style="border:1px #8899cc solid;">
	<tr class="liebiao_tou">
		<td align='center' ><input type="checkbox" name="allBtn" onclick="selectAll(this,'staffcodeId')"></td>
		<td align='center' ><i18n:message key="housekeeping_user.status"/></td>
		<td align='center' ><i18n:message key="common.company"/></td>
        <td align='center' ><i18n:message key="housekeeping_user.staffcode"/></td>
        <td align='center' ><i18n:message key="housekeeping_user.staffname"/></td>
        <td align='center' ><i18n:message key="housekeeping_user.logonid"/></td>
        <td align='center' ><i18n:message key="housekeeping_user.team"/></td>
        <td align='center' ><i18n:message key="housekeeping_user.title"/></td>
        <td align='center' ><i18n:message key="housekeeping_user.usertype"/></td>
        <td align='center' ><i18n:message key="housekeeping_user.setting"/></td>
	</tr>		
	<%
		if(userIt!=null){
			while(userIt.hasNext()){
				EflowStaffVO staffvo = (EflowStaffVO)userIt.next();				
	%>
			<tr class="tr_change">
			<td align='center' ><input type="checkbox" <%=("T".equals(staffvo.getStatus())?"disabled":"")%> name="staffcodeId" value="<%=staffvo.getStaffCode()%>"></td>
			<td><%=staffvo.getStatus()%>&nbsp;&nbsp;</td>
			<td><%=staffvo.getOrgName()%>&nbsp;&nbsp;</td>
			<td>
			<%if("1".equals(staffvo.getDisplayType())){ //pma user%>
			   <%=staffvo.getStaffCode()%>&nbsp;&nbsp;
			<%}else{%>
				<purview:purview moduleId='<%=ModuleOperateName.MODULE_USER_MANAGE%>' operateId='<%=ModuleOperateName.OPER_USER_EDIT%>' isButton='false' labelValue='<%=staffvo.getStaffCode()%>'>
                	<a href='<%=request.getContextPath()%>/userManageAction.it?method=enterEditUser&type=edit&staffcode=<%=staffvo.getStaffCode()%>&displayType=<%=staffvo.getDisplayType()%>'><%=staffvo.getStaffCode()%></a>
            	</purview:purview>
            <%}%>
			</td>
			<td><%=staffvo.getStaffName()==null?"":staffvo.getStaffName()%>&nbsp;&nbsp;</td>
			<td><%=staffvo.getLogonId()==null?"":staffvo.getLogonId()%>&nbsp;&nbsp;</td>
			<td><%=staffvo.getTeamName()==null?"":staffvo.getTeamName()%>&nbsp;&nbsp;</td>
			<td><%=staffvo.getTitleName()==null?"":staffvo.getTitleName()%>&nbsp;&nbsp;</td>
			<td><%="0".equals(staffvo.getUsertype())?"Permanent":"Temporary"%>&nbsp;&nbsp;</td>
			<td><a href="javascript:openCenterWindow('<%=request.getContextPath()%>/userManageAction.it?method=enterApproverGroupSetting&staffCode=<%=staffvo.getStaffCode()%>',450,400)"><i18n:message key="housekeeping_user.group"/></a>
            	&nbsp;/&nbsp;
            	<a href="javascript:openCenterWindow('<%=request.getContextPath()%>/userManageAction.it?method=enterRoleSetting&staffCode=<%=staffvo.getStaffCode()%>',480,400)"><i18n:message key="housekeeping_user.role"/></a>
                <%
                  if ("Yes".equalsIgnoreCase(ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_LEAVE_BALANCE))
                   || "Yes".equalsIgnoreCase(ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_MEDICAL_CLAIM))) {
                %>
                <purview:purview moduleId='<%=ModuleOperateName.MODULE_USER_MANAGE%>' operateId='<%=ModuleOperateName.OPER_USER_LEAVE_MAINTAIN%>' isButton='false' labelValue=''>
                &nbsp;/&nbsp;
            	<a href="javascript:openCenterWindow('<%=request.getContextPath()%>/leaveBalanceAction.it?method=leaveBalanceSetting&staffCode=<%=staffvo.getStaffCode()%>',850,500)"><i18n:message key="housekeeping_leavebalance.leave_balance"/></a>
                </purview:purview>
                <%}%>
			</td>
	<% 		}
		}%>
</table>   
<pageTag:page action="/userManageAction.it?method=searchUser"></pageTag:page>    
</form>   
</body>
</html>
<script language="javascript">
  changeUserType(document.all['usertype']);
</script>