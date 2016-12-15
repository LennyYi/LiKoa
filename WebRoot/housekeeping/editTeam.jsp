<!-- Task_ID	Author	Modify_Date	Description------------------->
<!-- IT0973		Young	 12/28/2007 For Team Management Function-->
<%@ include file="/common/head.jsp" %>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@page import="com.aiait.eflow.housekeeping.vo.EflowStaffVO,java.util.*,com.aiait.eflow.housekeeping.vo.TeamVO,com.aiait.eflow.housekeeping.vo.ApproverGroupMemberVO"%>
<%@page import="com.aiait.eflow.common.helper.StaffTeamHelper,com.aiait.eflow.housekeeping.vo.StaffVO,com.aiait.eflow.common.CommonName" %>
<%@page import="com.aiait.eflow.common.helper.CompanyHelper,com.aiait.eflow.housekeeping.vo.CompanyVO" %>
<%@page import="com.aiait.eflow.housekeeping.vo.ViewTeamTypeVO,com.aiait.eflow.common.helper.ScriptBuildTreeHelper" %>

<% 
    StaffVO currentStaff = (StaffVO)request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
	String saveType = (String)request.getParameter("type");
	Collection teamList = (ArrayList)request.getAttribute("teamList");
	StaffTeamHelper staffHelper = StaffTeamHelper.getInstance();
	TeamVO vo = null;
	EflowStaffVO eflowvo = null;
	boolean newTeam = false;

	if(saveType==null){saveType = "new";}
	if("new".equals(saveType)){
	    newTeam = true;
		vo = new TeamVO();
		eflowvo = new EflowStaffVO();
	}else{
		vo = (TeamVO)request.getAttribute("eflowteam");
		//System.out.println("teamCode="+vo.getOrgId());
	}
	Collection efusrlist=null;
	if (request.getAttribute("efusrlist")!=null){
		efusrlist = (ArrayList)request.getAttribute("efusrlist");  		
	}
	
	//Collection teamLeaderList = null;
	//if (request.getAttribute("teamLeaderList")!=null){
	//	teamLeaderList = (ArrayList)request.getAttribute("teamLeaderList");  		
	//}
	
	//Collection ViewTeamList = null;
	//if (request.getAttribute("ViewTeamList")!=null){
	//	ViewTeamList = (ArrayList)request.getAttribute("ViewTeamList");  		
	//}
	
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Team Edit</title>
<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
 <script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/ajax.js"></script>
 <script language="javascript" src="<%=request.getContextPath()%>/js/aiaitjsframe.js"></script>
 <script src="<%=request.getContextPath()%>/js/xtree/TreeNormal.js"></script>
 <script src="<%=request.getContextPath()%>/js/xtree/NodeNormal.js"></script>
 <script src="<%=request.getContextPath()%>/js/xtree/DivTreeNormal.js"></script> 
<script language="javascript">

	function convertstr(str){		
		str = str.replace(/\'/g,"");
		str = str.replace(/\%/g,"");
        str = str.replace(/\&/g,"");
  		str = str.replace(/\?/g,"");
  		str = str.replace(/\#/g,"");
		return str;
	}

	function submitForm(){
		var frm = document.forms[0];
      	if(formValidate(frm)==false) {
      		document.all['smBtn'].disabled = "";
      		return;
      	}
	    var teamName = document.forms[0].teamName.value.Trim();
	    var orgChart = "N";
	    if(document.forms[0].orgChart.checked){
	      orgChart = "Y";
	    }
	    var department = "N";
	    if(document.forms[0].department.checked){
	    	department = "Y";
	    }	    
       	var url = "<%=request.getContextPath()%>/teamManageAction.it?method=saveTeam";       	
       	var param = "&teamname="+ encodeURIComponent(teamName)
                +"&superiorteamcode="+document.forms[0].superiorteamcode.value
                +"&status="+document.forms[0].status.value
                +"&tlid="+document.forms[0].tlid.value
                +"&type="+document.forms[0].saveType.value
                +"&orgChart="+orgChart
                +"&department="+department                  
                +"&teamcode="+document.forms[0].Inputteamcode.value
                +"&orgId="+document.forms[0].orgId.value
                +"&t2Code="+document.forms[0].t2Code.value;
        
      	param = encodeURI(encodeURI(param)); //
        var myAjax = new Ajax.Request(
         url,
        {
            method:"post",       //
            parameters:param,   //
            setRequestHeader:{"If-Modified-Since":"0"},     //
            onComplete:function(x){    //
                 alert(x.responseText);
                 //window.opener.document.forms[0].submit();                 
                 //window.close();
                 window.location = "<%=request.getContextPath()%>/teamManageAction.it?method=searchTeam";
            },
            onError:function(x){          //
                 alert(save_unsuccess + " " + x.responseText);
                 document.all['smBtn'].disabled = "";
            } 
        } 
       ); 
      	
      	//var  xmlhttp = createXMLHttpRequest();
      	//var result;
      	//if(xmlhttp){
        //	xmlhttp.open('POST',url,false);
        //   	xmlhttp.onreadystatechange = function()
        //   	{
        //    	if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
        //        	result = xmlhttp.responseText;
        //     	}
        //   	}
        //   	xmlhttp.setRequestHeader("If-Modified-Since","0");
        //   	xmlhttp.send(null);
      	//} 
      	//if(result=="success"){
        // 	window.location = "<%=request.getContextPath()%>/teamManageAction.it?method=searchTeam";
      	//}else{
        //	alert(result);
      	//} 
	}
</script>

	<script language="javascript">
	var treeTeam = new xyTree.DivTreeNormal('Sub-Teams Tree');
	<% 
	if(!"new".equals(saveType)){
		//TeamVO to ViewTeamTypeVO;
		List curteamList = new ArrayList();
		ViewTeamTypeVO vto = new ViewTeamTypeVO();
		vto.setTeamCode(vo.getTeamCode());
		vto.setTeam_type(null);
		vto.setTeamName(vo.getTeamName());
		vto.setSuperiorsCode("-1");//as the root node
		curteamList.add(vto);
		
		out.println(ScriptBuildTreeHelper.buildtree(request,curteamList,null));
	}
	%>  
	
	var curnode = null;
	function selectNode(node){
		//alert(node.id);
		//do nothing		
	}

	function init(){
	  document.getElementById('teamTreeId').appendChild(treeTeam.div);
	  treeTeam.init(selectNode);
	}
	
	window.onload = init;
	</script>
</head>
<body>
<form name="teamForm" action="" method="post">
  <input type="hidden" name="Inputteamcode" value="<%="edit".equals(saveType)?vo.getTeamCode():"" %>">
  <input type="hidden" name="saveType" value="<%=saveType%>">
  <table width="100%"  border="1" cellpadding="3" cellspacing="0" bordercolor="#6595D6" style="border-collapse:collapse;" >
   	 <tr class="tr1">
       <td align='center' colspan='2'>
          <b><i18n:message key="housekeeping_team.title"/>
       </td>
     </tr>  
     <tr>
       <td align="right">
         <i18n:message key="housekeeping_team.teamname"/> : 
       </td>
       <td>
        <input type='text' name='teamName' size='40' maxLength='100' title="Team Name" required="true" value="<%=vo.getTeamName()==null?"":vo.getTeamName()%>">
        (<font color='red'>*</font>)
       </td>
     </tr>
     
     <tr>
       <td align="right">
         <i18n:message key="housekeeping_team.superiorteam"/> : 
       </td>
       <td>
         <select name="superiorteamcode">
           <option value=""></option>        
           <%   
           		if(teamList!=null&&teamList.size()>0){
           			Iterator teamIt = teamList.iterator();
           			while(teamIt.hasNext()){
           				TeamVO tvo = (TeamVO)teamIt.next();     
           				if(vo!=null && vo.getTeamCode()!=null && vo.getTeamCode().equals(tvo.getTeamCode())){
           					continue;
           				}
           %>
			           <option value="<%=tvo.getTeamCode()%>" <%=(vo.getSuperiorsCode()!=null&&tvo.getTeamCode().equals(vo.getSuperiorsCode()))?"selected":""%>><%=tvo.getTeamName()%></option>
           <%           			
           			}
           		}           
				
           %>           
         </select>
       </td>
     </tr>
     <tr>
       <td align="right">
         <i18n:message key="housekeeping_team.company"/> : 
       </td>
       <td>
         <select name="orgId" required="true" title="Company">
          <option value=''></option>
            <%
            Collection companyList = currentStaff.getOwnCompanyList();//CompanyHelper.getInstance().getOwnCompanyList(currentStaff.getOrgId());
            if(companyList!=null && companyList.size()>0){
            	Iterator it = companyList.iterator();
            	while(it.hasNext()){
            	   CompanyVO company = (CompanyVO)it.next();
            	   if(vo!=null && vo.getOrgId()!=null && company.getOrgId().equals(vo.getOrgId())){
            		   out.print("<option value='"+company.getOrgId()+"' selected>" + company.getOrgName()+"</option>");   
            	   }else{
            	     out.print("<option value='"+company.getOrgId()+"'>" + company.getOrgName()+"</option>");
            	   }
            	}
            }
           %>
         </select>(<font color='red'>*</font>)
       </td>
     </tr>
     <tr>
       <td align="right">
         <i18n:message key="housekeeping_team.isorgteam"/> : 
       </td>
       <td>
        <input type="checkbox" name='orgChart' value='Y' <%=(newTeam || (vo.getOrgChart()!=null && "Y".equals(vo.getOrgChart())))?"checked":""%>>
       </td>
     </tr>
     <tr>
       <td align="right">
         <i18n:message key="housekeeping_team.teamleader"/> : 
       </td>
       <td>
         <select name="tlid">
           <option value=""></option>
           <%boolean isTLFound = false;
           		if (efusrlist!=null&&efusrlist.size()>0){
           			Iterator usrIt = efusrlist.iterator();
           			while(usrIt.hasNext()){
           				StaffVO svo = (StaffVO)usrIt.next();           				
           %>
		   <option value="<%=svo.getLogonId()%>" 
		   <%if(vo.getTlid()!=null&&svo.getLogonId().equals(vo.getTlid())){
			   out.print("selected");
			   isTLFound = true;
		   }%> ><%=svo.getStaffName()%></option>		   
           <% 
           			}
           		}
           %>
         </select>
         <%if(!isTLFound){%><a><b>&nbsp;&nbsp;&nbsp;<%=vo.getTLeaderName()%></b></a><%}%>
       </td>
     </tr>
     <tr>
       <td align="right">
         <i18n:message key="housekeeping_team.isdepteam"/> : 
       </td>
       <td>
        <input type="checkbox" name='department' value='Y' <%=(newTeam || (vo.getDepartment()!=null && "Y".equals(vo.getDepartment())))?"checked":""%>>
       </td>
     </tr>
     <tr>
       <td align="right">T2 Code : 
       </td>
       <td>
        <input type="text" name='t2Code' value="<%=vo.getT2Code()==null?"":vo.getT2Code()%>">
       </td>
     </tr>         
     <tr>
       <td align="right">
         <i18n:message key="housekeeping_team.status"/> : 
       </td>
       <td>
         <select name="status">
           <option value="A" <%=(vo.getStatus()!=null && "A".equals(vo.getStatus())?"selected":"")%>>Active</option>
           <option value="T" <%=(vo.getStatus()!=null && "T".equals(vo.getStatus())?"selected":"")%>>Terminated</option>
         </select>
       </td>
     </tr>     
     <tr>
       <td align="center" colspan="2">
         <input type="button" name="smBtn" value='<i18n:message key="button.submit"/>' onclick="submitForm()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
         <input type="button" name="returnBtn" value='<i18n:message key="button.back"/>' onclick="javascript:window.history.go(-1)" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
       </td>
     </tr>
     <tr>
       <td align="left" colspan="2">
       	<div id="teamTreeId"></div>
       </td>
     </tr>     
  </table>
</form>
</body>
</html>