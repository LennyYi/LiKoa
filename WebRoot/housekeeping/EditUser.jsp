<!-- Task_ID	Author	Modify_Date	Description------------------->
<!-- IT0973		Young	 12/28/2007 For User Management Function-->
<%@ include file="/common/head.jsp" %>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@page import="com.aiait.eflow.housekeeping.vo.EflowStaffVO,java.util.*,com.aiait.eflow.housekeeping.vo.TeamVO,com.aiait.eflow.housekeeping.vo.TitleVO"%>
<%@page import="com.aiait.eflow.common.helper.CompanyHelper,com.aiait.eflow.housekeeping.vo.CompanyVO,com.aiait.eflow.common.CommonName,com.aiait.eflow.housekeeping.vo.StaffVO" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>Edit User</title>
<script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
<script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/calendar.js"></script>
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
    
	function submitForm(){
	    if(document.all['email'].value!="" && checkEmail(document.all['email'],"Email")==false){
	      return;
	    }
		if(document.forms[0].usertype.value=="1"){
			if(document.forms[0].beginDate.value.Trim()==""||document.forms[0].endDate.value.Trim()==""){
				alert("Temporary user must be set with valid Date!");
				return
			}
			if(document.forms[0].beginDate.value.Trim()!=""){
        		if(isDate(document.forms[0].beginDate,"Submitted From")==false){
          			return;
        		}
      		}
      		if(document.forms[0].endDate.value.Trim()!=""){
        		if(isDate(document.forms[0].endDate,"Submitted To")==false){
          			return;
        		}
      		}
      		if(document.forms[0].beginDate.value.Trim()!="" && document.forms[0].endDate.value.Trim()!=""){
        		if(compareDate(document.forms[0].beginDate.value.Trim(),document.forms[0].endDate.value.Trim())==false){
            		alert("From Date must be earlier or equal to To Date");
            		document.forms[0].beginDate.focus();
            		return;
         		}
      		}
		}
		var frm = document.forms[0];
      	if(formValidate(frm)==false) return;    
      	
      	var param = $.Form("userForm").serialize({ regTime:new Date().toGMTString()}); //???????????
      	var url = '<%=request.getContextPath()%>/userManageAction.it?method=saveUser';
      	param = encodeURI(encodeURI(param)); //
        var myAjax = new Ajax.Request(
         url,
        {
            method:"post",       //
            parameters:param,   //
            setRequestHeader:{"If-Modified-Since":"0"},     //
            onComplete:function(x){    //
                alert(x.responseText);
                if("success" == x.responseText){
                	history.back();
                	//window.location = "<%=request.getContextPath()%>/userManageAction.it?method=searchUser";
                }
                    
            },
            onError:function(x){
                    alert('Fail to save user: '+x.responseText);
            } 
        } 
       ); 
	}	
</script>
</head>
<body>
<% 
StaffVO currentStaff = (StaffVO)request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
    String beginDate = (String)request.getParameter("beginDate");
	String endDate = (String)request.getParameter("endDate");
	String saveType = (String)request.getParameter("type");
	String displayType = (String)request.getParameter("dispalyType");
	Collection teamList = (ArrayList)request.getAttribute("teamList");
	Collection titleList = (ArrayList)request.getAttribute("titleList");
	EflowStaffVO vo = null;
  	if(saveType==null){
		saveType = "new";
  	}
  	if("new".equals(saveType)){
		vo = new EflowStaffVO();
  	}else{
		vo = (EflowStaffVO)request.getAttribute("eflowuser");
  	}	
%>
<form name="userForm" action="" method="post">
  <input type="hidden" name="have">
  <input type="hidden" name="saveType" value="<%=saveType%>">
  <table width="100%"  border="1" cellpadding="3" cellspacing="0" bordercolor="#6595D6" style="border-collapse:collapse;" >
   	 <tr class="tr1">
       <td align='center' colspan='2'>
          <b>
             <i18n:message key="housekeeping_user.toptitle"/>
          </b>
       </td>
     </tr>
     <tr>
       <td align="right" width="25%">
         <i18n:message key="housekeeping_user.staffcode"/> : 
       </td>
       <td>
        <input type='text' name='staffCode' size='20' maxLength='10' title="Staff Code" required="true" value="<%=vo.getStaffCode()==null?"":vo.getStaffCode()%>"
               <%=!"new".equals(saveType)?"readonly":""%>>(<font color='red'>*</font>)
         (<i18n:message key="housekeeping_approvergroup.remark"/>)
       </td>
     </tr>
     <tr>
       <td align="right">
         <i18n:message key="housekeeping_user.staffname"/> : 
       </td>
       <td>
        <input type='text' name='staffName' size='40' maxLength='40' title="Staff Name" required="true" value="<%=vo.getStaffName()==null?"":vo.getStaffName().trim()%>">
        (<font color='red'>*</font>)
       </td>
     </tr>
     <tr>
       <td align="right">
         <i18n:message key="housekeeping_user.chinesename"/> : 
       </td>
       <td>
        <input type='text' name='chineseName' size='40' maxLength='40' value="<%=vo.getChineseName()==null?"":vo.getChineseName().trim()%>">
       </td>
     </tr>
     <tr>
       <td align="right">
         <i18n:message key="housekeeping_user.bank_detail"/> : 
       </td>
       <td>
        <input type='text' name='bankDetail' size='40' maxLength='40' value="<%=vo.getBankDetail()==null?"":vo.getBankDetail().trim()%>">
       </td>
     </tr>
     <tr>
       <td align="right">
         <i18n:message key="housekeeping_user.accountno"/> : 
       </td>
       <td>
        <input type='text' name='accountNo' size='40' maxLength='40' value="<%=vo.getAccountNo()==null?"":vo.getAccountNo().trim()%>">
       </td>
     </tr>
     <tr>
       <td align="right">
         <i18n:message key="housekeeping_user.logonid"/> : 
       </td>
       <td align="left">
         <input type='text' name='logonId' size='20' maxLength='20' title="Logon Id" required="true" value="<%=vo.getLogonId()==null?"":vo.getLogonId().trim()%>">(<font color='red'>*</font>)
       </td>
     </tr>
     <tr>
       <td align="right">
          <i18n:message key="housekeeping_user.title"/> : 
       </td>
       <td align="left">
         <select name="titleId">
            <%if(titleList!=null){
               Iterator titleIt = titleList.iterator();
               while(titleIt.hasNext()){
            	   TitleVO title = (TitleVO)titleIt.next();
            	   if(vo.getTitle()!=null && (title.getTitleId()+"").equals(vo.getTitle())){
            	      out.println("<option value='"+title.getTitleId()+"' selected>"+title.getAbrev()+" (" + title.getDescription()+") </option>");
            	   }else{
            		   out.println("<option value='"+title.getTitleId()+"'>"+title.getAbrev()+" (" + title.getDescription()+") </option>"); 
            	   }
               }
            }
            %>
         </select>
       </td>
     </tr>
     <tr>
       <td align="right">
         <i18n:message key="common.company"/> : 
       </td>
       <td>
         <select name="orgId" required="true" title="Company" onchange="getOptionList(this.value)">
          <option value=''></option>
            <%
            Collection companyList = currentStaff.getOwnCompanyList(); //CompanyHelper.getInstance().getOwnCompanyList(currentStaff.getOrgId());
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
         <i18n:message key="housekeeping_user.team"/> : 
       </td>
       <td>
         <select name="team_code" title='Team' required='true'>
           <option value=""></option>
           <%
           		if(teamList!=null&&teamList.size()>0){
           			Iterator teamIt = teamList.iterator();
           			while(teamIt.hasNext()){
           				TeamVO tvo = (TeamVO)teamIt.next();
           %>           
           <option value="<%=tvo.getTeamCode()%>" <%=(vo.getTeamCode()!=null&&tvo.getTeamCode().equals(vo.getTeamCode()))?"selected":""%>><%=tvo.getTeamName()%></option>
           <%
           			}
           		}
           %>               
         </select>(<font color='red'>*</font>)
       </td>
     </tr>
     <tr>
       <td align="right">
         <i18n:message key="housekeeping_user.status"/> : 
       </td>
       <td>
         <select name="status">
           <option value="A" <%=(vo.getStatus()!=null && "A".equals(vo.getStatus())?"selected":"")%>>Active</option>
           <option value="T" <%=(vo.getStatus()!=null && "T".equals(vo.getStatus())?"selected":"")%>>Terminated</option>
         </select>
       </td>
     </tr>
     <tr>
       <td align="right">
         <i18n:message key="housekeeping_user.email"/> : 
       </td>
       <td>
           <input type='text' name='email' title="Email" required="true" size="40" maxLength='40' value="<%=(vo.getEmail()==null?"":vo.getEmail().trim())%>">
           (<font color='red'>*</font>)
       </td>
     </tr>
     <tr>
       <td align="right">
         <i18n:message key="housekeeping_user.usertype"/> : 
       </td>
       <td>
         <select name="usertype"  onchange="changeUserType(this)">
           <option value="0" <%=(vo.getUsertype()!=null && "0".equals(vo.getUsertype())?"selected":"")%>>Permanent</option>
           <option value="1" <%=(vo.getUsertype()!=null && "1".equals(vo.getUsertype())?"selected":"")%>>Temporary</option>
         </select>
       </td>
     </tr>
     <tr>
       <td align="right">
         <i18n:message key="housekeeping_user.tempfrom"/> : 
       </td>
       <td>
			<INPUT Name="beginDate" onclick='setday(this)'  Type="text" value="<%=vo.getFromdate()==null?"":vo.getFromdate()%>" class="text2" style="WIDTH: 130px" id="beginDate">(mm/dd/yyyy)
       </td>
     </tr>
     <tr>
       <td align="right">
         <i18n:message key="housekeeping_user.tempto"/> : 
       </td>
       <td>
			<INPUT Name="endDate" onclick='setday(this)'  Type="text" value="<%=vo.getTodate()==null?"":vo.getTodate()%>" class="text2" style="WIDTH: 130px" id="endDate">(mm/dd/yyyy) 
       </td>
     </tr>
     <tr>
       <td align="center" colspan="2">
         <input type="button" name="smBtn" value='<i18n:message key="button.submit"/>' onclick="submitForm()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">&nbsp;&nbsp;
         <input type="button" name="returnBtn" value='<i18n:message key="button.back"/>' onclick="javascript:window.history.go(-1)" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
       </td>
     </tr>
  </table>
</form>
</body>
</html>
<script language="javascript">
  changeUserType(document.all['usertype']);
</script>