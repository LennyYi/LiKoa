<!-- Task_ID	Author	Modify_Date	Description------------------->
<!-- IT0973		Young	 12/28/2007 For Team Management Function-->
<!-- IT1297		Mario	 06/27/2012 For Dept. flag              -->
<%@ include file="/common/head.jsp" %>
<%@ include file="/common/loading.jsp" %>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@page import="java.util.*,com.aiait.eflow.housekeeping.vo.TeamVO,com.aiait.eflow.housekeeping.vo.StaffVO,com.aiait.eflow.util.StringUtil,com.aiait.eflow.common.helper.*,com.aiait.eflow.common.*" %>
<%@page import="com.aiait.eflow.common.helper.CompanyHelper,com.aiait.eflow.housekeeping.vo.CompanyVO" %>
<%@ taglib uri="/WEB-INF/purview.tld" prefix="purview" %>
<%@ taglib uri="/WEB-INF/taglibs-page.tld" prefix="pageTag" %>
<html>
<head>
<title>Team Management</title>
  <script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
<script src="<%=request.getContextPath()%>/js/webtools.js" language="JavaScript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/calendar.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
<SCRIPT language="javascript" src="<%=request.getContextPath()%>/js/sorttable.js"></SCRIPT>
<script language="javascript" src="<%=request.getContextPath()%>/js/resizeCol.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/ajax.js"></script>
<script language="javascript">
	
	function searchTeam(){
		document.forms[0].action = "<%=request.getContextPath()%>/teamManageAction.it?method=searchTeam";
		document.forms[0].submit();
	}

	function exportTeam(){
		document.forms[0].action = "<%=request.getContextPath()%>/teamManageAction.it?method=exportInquiryTeam";
		document.forms[0].target = "_blank";
		document.forms[0].submit();
		document.forms[0].target = "";
	}
	 
	function addTeam(){
 		var url = "<%=request.getContextPath()%>/teamManageAction.it?method=enterEditTeam&type=new&moduleId=<%=ModuleOperateName.MODULE_TEAM_MANAGE%>&operateId=<%=ModuleOperateName.OPER_TEAM_ADD%>";
 		window.location = url;
 		//var newWindow = window.open("","Add_Team","height=1, width=1, top=0, left=0,toolbar =no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no");
		//if(document.all){
		//	newWindow.moveTo(300,200);  
		//	newWindow.resizeTo(385,300);
     	//}
		//newWindow.location=url;
	}
		
	function deleteTeam(){
		if(checkSelect('teamcodeId')<=0){
      		alert("You have not selected any Teams to delete!");
      		return;
    	}
    	if(confirm("Are you sure to delete the selected Teams")==false){
       		return;
    	}
      var url = "<%=request.getContextPath()%>/teamManageAction.it?method=deleteTeams&&moduleId=<%=ModuleOperateName.MODULE_TEAM_MANAGE%>&operateId=<%=ModuleOperateName.OPER_TEAM_DELETE%>";
      document.forms['deleteForm'].action = url;
      document.forms['deleteForm'].submit();
	}
  
	function configTeam(){
		var url = "<%=request.getContextPath()%>/teamManageAction.it?method=ListConfPage&type=new&moduleId=<%=ModuleOperateName.MODULE_TEAM_MANAGE%>&operateId=<%=ModuleOperateName.OPER_TEAM_CONFIG%>";
 		window.location = url;		
	}
	
	function toggleDept(ob){
		var updateTo = "N";
		if(ob.checked==true){
			updateTo = "Y";
		}else{
			updateTo = "N";
		}
		var url = "<%=request.getContextPath()%>/teamManageAction.it?method=updateTeamConfig&teamCode="+ob.value+"&teamType="+updateTo+
				"&moduleId=<%=ModuleOperateName.MODULE_TEAM_MANAGE%>&operateId=<%=ModuleOperateName.OPER_TEAM_CONFIG%>";			
 		window.location = url;		
	}
</script>
</head>
<%
    StaffVO currentStaff = (StaffVO)request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
	String teamcode = (String)request.getParameter("teamCode");
    String teamname = (String)request.getParameter("teamName");
//    String status = (String)request.getParameter("status");
    String status = (String)request.getParameter("department");
    String teamleader = (String)request.getParameter("teamleader");
    String orgId = (String)request.getParameter("orgId");
    
    Iterator eflowteamIt=null;
	Iterator teamIt=null;	
    if (request.getAttribute("eflowteamList")!=null){
		Collection eflowteamList = (ArrayList)request.getAttribute("eflowteamList");		
		eflowteamIt = eflowteamList.iterator();
    }
    if (request.getAttribute("teamList")!=null){
		Collection teamList = (ArrayList)request.getAttribute("teamList");
		teamIt = teamList.iterator();	
    }
%>
<body>
<table border="0" width="100%" cellspacing="0" cellpadding="0">
	<tr>
 		<td height="10"></td>
	</tr>
 	<!--<tr>
 		<td>
 	  		<strong><font color='#5980BB' family='Times New Roman'><i18n:message key="housekeeping_team.location"/></font></strong>
	 	</td>
	</tr>
--></table>
<form name="myForm" method="post">
<TABLE WIDTH=100% bordercolor="#6595D6" style="border-collapse:collapse;"  BORDER=1 CELLPADDING=3 CELLSPACING=0 class="tr0" >
	<tr class="tr1">
		<td align='center' colspan='4'><B><i18n:message key="housekeeping_team.title"/></B></td>
	</tr>
	<tr> 
		<TD width=15% height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="housekeeping_team.teamcode"/></span></div></TD>
		<TD width=35% height="20" > 
			<input type="text"  value="<%=teamcode==null?"":teamcode%>" name="teamCode">
		</TD>
      	<TD width=15% height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="housekeeping_team.teamname"/></span></div></TD>
      	<TD width=30% height="20">
         	<input type="text"  value="<%=teamname==null?"":teamname%>" name="teamName">
		</TD>
    </tr>    
    <tr> 
      	<TD width=15% height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="housekeeping_team.company"/></span></div></TD>
      	<TD width=35% height="20" > 
         <select name="orgId">
           <option value=''></option>
           <%
            Collection companyList = currentStaff.getOwnCompanyList(); //CompanyHelper.getInstance().getOwnCompanyList(currentStaff.getOrgId());
            if(companyList!=null && companyList.size()>0){
            	Iterator it = companyList.iterator();
            	while(it.hasNext()){
            	   CompanyVO vo = (CompanyVO)it.next();
            	   if(orgId!=null && vo.getOrgId().equals(orgId)){
            		   out.print("<option value='"+vo.getOrgId()+"' selected>" + vo.getOrgName()+"</option>");   
            	   }else{
            	     out.print("<option value='"+vo.getOrgId()+"'>" + vo.getOrgName()+"</option>");
            	   }
            	}
            }
           %>
         </select>     
      	</TD>
       <TD width=15% height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="housekeeping_team.status"/></span></div></TD>
      	<TD width=35% height="20" > 
         <select name="status">
	       <option value="" <%="".equals(status)?"selected":""%>></option>	    
           <option value="A" <%="A".equals(status)?"selected":""%>><i18n:message key="housekeeping_team.active"/></option>
           <option value="T" <%="T".equals(status)?"selected":""%>><i18n:message key="housekeeping_team.terminated"/></option>
         </select>         
      	</TD>
    </tr>
    <tr> 
      	<TD width=15% height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="housekeeping_team.teamleader"/></span></div></TD>
      	<TD width=35% height="20">
      		<input type="text"  value="<%=teamleader==null?"":teamleader%>" name="teamleader">                
      	</TD>
      	<TD width=15% height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="housekeeping_team.isdepteam"/></span></div></TD>
      	<TD width=35% height="20">
      		<select name="department">
	       <option value="" <%="".equals(status)?"selected":""%>></option>	    
           <option value="Y" <%="Y".equals(status)?"selected":""%>>Yes-Dept.</option>
           <option value="N" <%="N".equals(status)?"selected":""%>>No-Section</option>
         </select>                     
      	</TD>      	
    </tr>    
    <tr>
		<td align='center' colspan='4'>
         	<input type="button" name="searchBtn" value='<i18n:message key="button.search"/>' onclick="searchTeam()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           		onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">&nbsp;&nbsp;
           	<input type="button" name="searchBtn" value='<i18n:message key="button.export_excel"/>' onclick="exportTeam()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           		onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">&nbsp;&nbsp;
			<input type="Reset" name="resetBtn" value='<i18n:message key="button.reset"/>' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           		onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">&nbsp;&nbsp;        
			<purview:purview moduleId='<%=ModuleOperateName.MODULE_TEAM_MANAGE%>' operateId='<%=ModuleOperateName.OPER_TEAM_ADD%>' isButton='true' labelValue='Add'>
			  <input type="button" name="addBtn" value='<i18n:message key="button.add"/>' onclick="addTeam()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           		onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
            </purview:purview>&nbsp;&nbsp; 
            <purview:purview moduleId='<%=ModuleOperateName.MODULE_TEAM_MANAGE%>' operateId='<%=ModuleOperateName.OPER_TEAM_DELETE%>' isButton='true' labelValue='Delete'>
  			  <input type="button" name="deleteBtn" value='<i18n:message key="button.delete"/>' onclick="deleteTeam()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           		onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
            </purview:purview>&nbsp;&nbsp;           
            <purview:purview moduleId='<%=ModuleOperateName.MODULE_TEAM_MANAGE%>' operateId='<%=ModuleOperateName.OPER_TEAM_CONFIG%>' isButton='true' labelValue='Config Team'>
			 <!-- <input type="button" name="configBtn" value='<i18n:message key="housekeeping_team.configteam"/>' onclick="configTeam()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           		onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">-->
            </purview:purview>&nbsp;&nbsp;
       	</td>
	</tr>			
</TABLE>
</form>
<form name="deleteForm" method="post">
<table width="100%"  border="0" cellpadding="0" cellspacing="1" class=sortable id=mytable  style="border:1px #8899cc solid;">
	<tr class="liebiao_tou">
		<td align='center' ><input type="checkbox" name="allBtn" onclick="selectAll(this,'teamcodeId')"></td>
		<td align='center' ><i18n:message key="housekeeping_team.status"/></td>
        <td align='center' ><i18n:message key="housekeeping_team.teamcode"/></td>
        <td align='center' ><i18n:message key="housekeeping_team.teamname"/></td>
        <td align='center' ><i18n:message key="housekeeping_team.superiorteam"/></td>
        <td align='center' ><i18n:message key="housekeeping_team.teamleader"/></td>
        <td align='center' ><i18n:message key="housekeeping_team.isdepteam"/></td>
        <td align='center' ><i18n:message key="housekeeping_team.company"/></td>
	</tr>
	<%
		if(eflowteamIt!=null){			
			while(eflowteamIt.hasNext()){
				TeamVO eflowteamvo = (TeamVO)eflowteamIt.next();
	%>
			<tr class="tr_change">
			<td align='center' ><input type="checkbox" <%=("T".equals(eflowteamvo.getStatus())?"disabled":"")%> name="teamcodeId" value="<%=eflowteamvo.getTeamCode()%>"></td>
			<td><%=eflowteamvo.getStatus()%>&nbsp;&nbsp;</td>
			<td>
            <%if (!eflowteamvo.isReadOnly()) {%>
				<purview:purview moduleId='<%=ModuleOperateName.MODULE_TEAM_MANAGE%>' operateId='<%=ModuleOperateName.OPER_TEAM_DELETE%>' isButton='false' labelValue='<%=eflowteamvo.getTeamCode()%>'>
                	<a href='<%=request.getContextPath()%>/teamManageAction.it?method=enterEditTeam&type=edit&teamcode=<%=eflowteamvo.getTeamCode()%>'><%=eflowteamvo.getTeamCode()%></a>
            	</purview:purview>
            <%} else {%>
                <%=eflowteamvo.getTeamCode()%>
            <%}%>
			</td>
			<td><%=eflowteamvo.getTeamName()%>&nbsp;&nbsp;</td>
			<td><%=StaffTeamHelper.getInstance().getTeamNameByCode(eflowteamvo.getSuperiorsCode())%>&nbsp;&nbsp;</td>
			<td><%=eflowteamvo.getTLeaderName()%>&nbsp;&nbsp;</td>
			<td>
			<!-- <input type=checkbox  name="isDept"  value="<%=eflowteamvo.getTeamCode()%>" onclick="toggleDept(this)" <%="Y".equalsIgnoreCase(eflowteamvo.getDepartment())? "checked":""%>> -->
			<%="Y".equalsIgnoreCase(eflowteamvo.getDepartment())? "Dept.":""%>&nbsp;&nbsp;</td>	
			<td><%=CompanyHelper.getInstance().getCompany(eflowteamvo.getOrgId())==null?"":CompanyHelper.getInstance().getCompany(eflowteamvo.getOrgId()).getOrgName()%>&nbsp;&nbsp;</td>		
	<% 		}
		}%>

</table>
<pageTag:page action="/teamManageAction.it?method=searchTeam"></pageTag:page>    
</form>
</body>
</html>