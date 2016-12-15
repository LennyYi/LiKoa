<!-- Task_ID	Author	Modify_Date	Description------------------------------------------->
<!-- IT0958		Young	 10/18/2007 DS011 Form Design can authorized for every form type-->
<%@ include file="/common/head.jsp" %>
<%@ include file="/common/loading.jsp" %>
<%@page import="java.util.*,com.aiait.eflow.housekeeping.vo.StaffVO,com.aiait.eflow.housekeeping.vo.FormTypeVO,java.text.SimpleDateFormat,com.aiait.eflow.util.StringUtil,com.aiait.eflow.formmanage.vo.*,com.aiait.eflow.common.helper.*,com.aiait.eflow.common.*" %>
<%@page import="com.aiait.eflow.common.helper.CompanyHelper,com.aiait.eflow.housekeeping.vo.CompanyVO" %>
<%@ taglib uri="/WEB-INF/purview.tld" prefix="purview" %>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<html>
<head>
<title>Manage Form</title>
<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
<script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
<script src="<%=request.getContextPath()%>/js/webtools.js" language="JavaScript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/calendar.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
<SCRIPT language="javascript" src="<%=request.getContextPath()%>/js/sorttable.js"></SCRIPT>
<script language="javascript" src="<%=request.getContextPath()%>/js/resizeCol.js"></script>
 <script language="javascript" src="<%=request.getContextPath()%>/js/ajax.js"></script>
<script language="javascript">

  function searchForm(){
    document.forms[0].action = "<%=request.getContextPath()%>/formManageAction.it?method=manageForm";
    document.forms[0].submit();
  }

  function addForm(){
     var url = "<%=request.getContextPath()%>/formManageAction.it?method=enterEditForm&type=new";
     openCenterWindow(url,460,300);
  }
  function editForm(formSystemId){
    //var str = getTableSelectRecordStr("formSystemId","formSystemId");
    //var formSystemId = getObjectValue("formSystemId");
    if(formSystemId==""){
      return;
    }
    var url = "<%=request.getContextPath()%>/formManageAction.it?method=enterEditForm&type=edit&formSystemId=" + formSystemId;
    openCenterWindow(url,460,300);
  }
  
  function publishForm(action){
    var message = "";
    var confirmMsg = "";
    if(action=="publish"){
      message = have_no_select;
      confirmMsg = confirm_publish_form;
    }else{
      message = have_no_select;
      confirmMsg = confirm_disable_form;
    }

    if(checkSelect('formSystemId')<=0){
      alert(message);
      return;
    }

    if(confirm(confirmMsg)){
       //var str = getTableSelectRecordStr("formSystemId","formSystemId");
       var formSystemIdStr = getTableSelectRecordStr("formSystemId","formSystemId");
       var url = "<%=request.getContextPath()%>/formManageAction.it?";
       if(action=="publish"){
           url = url + "method=publishForm&" + formSystemIdStr;
       }else if(action=="disable"){
           url = url + "method=disableForm&" + formSystemIdStr;
       }
       var  xmlhttp = createXMLHttpRequest();
       var result;
       if(xmlhttp){
           xmlhttp.open('POST',url,false);
           xmlhttp.onreadystatechange = function()
           {
             if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
                 result = xmlhttp.responseText;
                 alert(result)
                 document.location.reload();
             }
           }
           xmlhttp.setRequestHeader("If-Modified-Since","0");
           xmlhttp.send(null);
       }
    }
  }
  
  function revokeForm(){
    var formSystemId = getObjectValue("formSystemId");
    if(formSystemId==""){
      return;
    }
    var url = "<%=request.getContextPath()%>/formManageAction.it?method=revokeForm&formSystemId="+formSystemId;
    var xmlhttp = createXMLHttpRequest();
    var result;
     if(xmlhttp){
         xmlhttp.open('POST',url,false);
         xmlhttp.onreadystatechange = function()
         {
             if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
                 result = xmlhttp.responseText;
              }
         }
         xmlhttp.setRequestHeader("If-Modified-Since","0");
         xmlhttp.send(null);
     }   
     if(result.Trim()=="success"){
       window.location.reload();
     }else{
       alert("Fail to Revoke form");
     }
  }
  
  function copyForm(formSystemId){
    /**
     if(checkSelect('formSystemId')!=1){
      alert("You have not selected any form to copy!");
      return;
    }
    **/
    //var formSystemId = getObjectValue("formSystemId");
    if(formSystemId==""){
      return;
    }
    if(confirm(confirm_copy_form)==false){
      return;
    }
    //var str = getTableSelectRecordStr("formSystemId","formSystemId");
    var url = "<%=request.getContextPath()%>/formManageAction.it?method=copyForm&formSystemId="+formSystemId;
    var xmlhttp = createXMLHttpRequest();
     var result;
     if(xmlhttp){
         xmlhttp.open('POST',url,false);
         xmlhttp.onreadystatechange = function()
         {
             if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
                 result = xmlhttp.responseText;
              }
         }
         xmlhttp.setRequestHeader("If-Modified-Since","0");
         xmlhttp.send(null);
     }   
     if(result.Trim()=="success"){
       window.location.reload();
     }else{
       alert("Fail to Copy Form");
     }
  }
  
  function deleteForm(){
    if(checkSelect('formSystemId')<=0){
      alert(have_no_select);
      return;
    }
    //var formSystemId = getObjectValue("formSystemId");
    //if(formSystemId==""){
    //  return;
   // }
    if(confirm(confirm_delete_form)==false){
       return;
    }
    var formSystemIdStr = getTableSelectRecordStr("formSystemId","formSystemId");
    var url = "<%=request.getContextPath()%>/formManageAction.it?method=deleteForm&"+formSystemIdStr;
    var xmlhttp = createXMLHttpRequest();
     var result;
     if(xmlhttp){
         xmlhttp.open('POST',url,false);
         xmlhttp.onreadystatechange = function()
         {
             if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
                 result = xmlhttp.responseText;
              }
         }
         xmlhttp.setRequestHeader("If-Modified-Since","0");
         xmlhttp.send(null);
     }   
     if(result.Trim()=="success"){
       window.location.reload();
     }else{
       alert(result);
     }
  }
 function selectRowChange(obj)   
 {       
  var flag = "false";
  for(var i=1;i<obj.rows.length;i++)   
  {   
    if(i==event.srcElement.parentElement.rowIndex)   
    {   
      obj.rows(i).className="liebiao_nr1"; 
      changeObjValue("formSystemId",document.getElementById("formSystemId"+i).value);
      flag = "true";
    }else{   
      if((i-1)%2==0){
        obj.rows(i).className="liebiao_nr2";   
      }else{
        obj.rows(i).className="liebiao_nr3";   
      }
    }
  }   
  if(flag=="false"){
    changeObjValue("formSystemId","");
  }
 }   
</script>

</head>
<%
  String orgId = (String)request.getParameter("orgId");
  String formId = (String)request.getParameter("formId");
  String formType = (String)request.getParameter("formType");
  String status = (String)request.getParameter("status");
  Collection formList = (ArrayList)request.getAttribute("formList");
  Iterator formIt = formList.iterator();
  StaffVO currentStaff = (StaffVO)request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
  AuthorityHelper authority = AuthorityHelper.getInstance();
  if(orgId==null || "".equals(orgId)){
	  orgId = currentStaff.getOrgId();
  }
  
%>
<body>
<table border="0" width="100%" cellspacing="0" cellpadding="0">
 <tr>
 	<td height="10"></td>
 </tr>
 <!--<tr>
 	<td>
 	  <strong><font color='#5980BB' family='Times New Roman'><i18n:message key="form_design.navigate"/></font></strong>
 	 </td>
 </tr>-->
</table>
<form name="myForm" method="post">
<input type="hidden" name="formSystemId" value="">
<TABLE WIDTH=100% bordercolor="#6595D6" style="border-collapse:collapse;"  BORDER=1 CELLPADDING=3 CELLSPACING=0 class="tr0" >
     <tr class="tr1">
       <td align='center' colspan='4'><B><i18n:message key="menu.design.form_design"/></B></td>
     </tr>
     <TR> 
      <TD width=15% height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="form_design.form_id"/></span></div></TD>
      <TD width=35% height="20" > 
        <input type="text"  value="<%=formId==null?"":formId%>" name="formId">
      </TD>
      <TD width=15% height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="common.form_type"/></span></div></TD>
      <TD width=30% height="20">
         <select name="formType">
          <option value="">All</option>
          <% 
            Collection typeList = FormTypeHelper.getInstance().getFormTypeList();
            if(typeList!=null && typeList.size()>0){
  		      Iterator it = typeList.iterator();
  		      while(it.hasNext()){
  			    FormTypeVO typeVo = (FormTypeVO)it.next();
  			    if(authority.checkAuthorityByFormType(currentStaff.getCurrentRoleId(),ModuleOperateName.MODULE_FORM_MANAGE,typeVo.getFormTypeId())){
  			    %>
  			    <option value="<%=typeVo.getFormTypeId()%>" <%=(formType!=null && (typeVo.getFormTypeId()).equals(formType))?"selected" : ""%>><%=typeVo.getFormTypeName()%></option>
  			    <% 	
  			    }
              }
          }
          %>
        </select>
      </TD>
    </TR>
    <tr>
      <TD width=15% height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="common.company"/></span></div></TD>
      <TD width=35% height="20" > 
         <select name="orgId">
           <%
            Collection companyList = currentStaff.getOwnCompanyList(); //CompanyHelper.getInstance().getOwnCompanyList(currentStaff.getOrgId());
            if(companyList!=null && companyList.size()>0){
            	Iterator it = companyList.iterator();
            	while(it.hasNext()){
            	   CompanyVO vo = (CompanyVO)it.next();
            	   if(vo.getOrgId().equals(orgId)){
            		   out.print("<option value='"+vo.getOrgId()+"' selected>" + vo.getOrgName()+"</option>");   
            	   }else{
            	     out.print("<option value='"+vo.getOrgId()+"'>" + vo.getOrgName()+"</option>");
            	   }
            	}
            }
           %>
         </select>
      </TD>
      <TD width=15% height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="common.status"/></span></div></TD>
      <TD width=35% height="20"> 
         <select name="status">
           <option value=""></option>
           <option value="0" <%="0".equals(status)?"selected":""%>>Published</option>
           <option value="1" <%="1".equals(status)?"selected":""%>>Design</option>
           <option value="2" <%="2".equals(status)?"selected":""%>>Disabled</option>
         </select>
      </TD>
    </tr>
     <tr>
       <td align='center' colspan='4'>
                <input type="button" name="searchBtn" value='<i18n:message key="button.search"/>' onclick="searchForm()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">&nbsp;&nbsp;
         <input type="Reset" name="resetBtn" value="<i18n:message key="button.reset"/>" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">&nbsp;&nbsp;
        <purview:purview moduleId='<%=ModuleOperateName.MODULE_FORM_MANAGE%>' operateId='<%=ModuleOperateName.OPER_FORM_ADD%>' isButton='true' labelValue='Add'>
        <input type="button" name="addBtn" value='<i18n:message key="button.add"/>' onclick="addForm()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">&nbsp;&nbsp;
        </purview:purview>
        &nbsp;
        <purview:purview moduleId='<%=ModuleOperateName.MODULE_FORM_MANAGE%>' operateId='<%=ModuleOperateName.OPER_FORM_DELETE%>' isButton='true' labelValue='Delete'>
        <input type="button" name="deleteBtn" value='<i18n:message key="button.delete"/>' onclick="deleteForm()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">&nbsp;&nbsp;
        </purview:purview>
        &nbsp;
        <purview:purview moduleId='<%=ModuleOperateName.MODULE_FORM_MANAGE%>' operateId='<%=ModuleOperateName.OPER_FORM_PUBLISH%>' isButton='true' labelValue='Publish'>
          <input type="button" name="publishBtn" value='<i18n:message key="button.publish"/>' onclick="publishForm('publish')" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">&nbsp;
        </purview:purview>
        <purview:purview moduleId='<%=ModuleOperateName.MODULE_FORM_MANAGE%>' operateId='<%=ModuleOperateName.OPER_FORM_PUBLISH%>' isButton='true' labelValue='Disable'>
         <input type="button" name="disabledBtn" value='<i18n:message key="button.disable"/>' onclick="publishForm('disable')" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
        </purview:purview>   
       </td>
     </tr>
     </table>
   </form>
        <table width="100%"  border="0" cellpadding="0" cellspacing="1" class=sortable id=mytable  style="border:1px #8899cc solid;">
          <tr class="liebiao_tou">
            <td align='center' ><input type="checkbox" name="allBtn" onclick="selectAll(this,'formSystemId')"></td>
            <td align='center' ><i18n:message key="form_design.form_id"/></td>
            <td align='center' ><i18n:message key="common.request_form"/></td>
            <td align='center' ><i18n:message key="common.form_type"/></td>
            <!--<td align='center'>Description</td>  -->
            <td align='center' ><i18n:message key="common.status"/></td>
            <td align='center' ><i18n:message key="form_design.create_date"/></td>
            <td align='center' ><i18n:message key="form_design.action"/></td>
          </tr>
          <%
            int i = 1;
            SimpleDateFormat   df  =null;
            if(formIt!=null){
               df   =new   SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            }
            while(formIt.hasNext()){
            	FormManageVO form = (FormManageVO)formIt.next();
            	Date cDate = null;
            	if(form.getCreateDateStr()!=null && !"".equals(form.getCreateDateStr())){
            	   cDate = df.parse(form.getCreateDateStr());
            	}
          %>
            <tr class="tr_change">
             <!--   <input type="hidden" name="formSystemId<%=i%>" value="<%=form.getFormSystemId()%>"> -->
              <td align='center' ><input type="checkbox" name="formSystemId" value="<%=form.getFormSystemId()%>"></td>
              <td >
               <%=form.getFormId()%>&nbsp;&nbsp;
              </td>
              <td ><%=form.getFormName()%>&nbsp;&nbsp;</td>
              <td ><%=FormTypeHelper.getInstance().getFormTypeName(form.getFormType())%>&nbsp;&nbsp;</td>
              <!--<td><%=form.getDescription()%>&nbsp;&nbsp;</td>-->
              <td >
              <%
               if("0".equals(form.getStatus())){
            	  out.print("Published");
               }else if("1".equals(form.getStatus())){
            	   out.print("Design");
               }else if("2".equals(form.getStatus())){
            	   out.print("Disabled");
               }
              %>
              &nbsp;&nbsp;
              </td>
              <td ><%=(form.getCreateDateStr()!=null && !"".equals(form.getCreateDateStr()))?StringUtil.getDateStr(cDate,"MM/dd/yyyy HH:mm:ss"):""%> &nbsp;&nbsp;</td>
              <td >
               <purview:purview moduleId='<%=ModuleOperateName.MODULE_FORM_MANAGE%>' operateId='<%=ModuleOperateName.OPER_FORM_DESIGN%>' isButton='false' labelValue='<i18n:message key="button.design"/>'>
               <a href='<%=request.getContextPath()%>/formManageAction.it?method=enterEditForm&type=editWholeForm&formSystemId=<%=form.getFormSystemId()%>'><i18n:message key="button.design"/></a>
               </purview:purview>
               &nbsp;/&nbsp;
               <purview:purview moduleId='<%=ModuleOperateName.MODULE_FORM_MANAGE%>' operateId='<%=ModuleOperateName.OPER_FORM_UPDATE%>' isButton='false' labelValue='<i18n:message key="button.edit"/>'>
               <a href="javascript:editForm('<%=form.getFormSystemId()%>')"><i18n:message key="button.edit"/></a>
               </purview:purview>
               &nbsp;/&nbsp;
               <purview:purview moduleId='<%=ModuleOperateName.MODULE_FORM_MANAGE%>' operateId='<%=ModuleOperateName.OPER_FORM_COPY%>' isButton='false' labelValue='<i18n:message key="button.copy"/>'>
               <a href="javascript:copyForm('<%=form.getFormSystemId()%>')"><i18n:message key="button.copy"/></a>
               </purview:purview>
               </td>
            </tr>
          <%i++;}%>
        </table>
   <!-- 
     <tr>
     <td>
         <div class="left_mianbody_fy"> Result Number:10, Go to
    	 <select name="select" onchange="window.navigate(this.value)" class="inpud"> 
            <option value='' selected>1
<option value=''>2
<option value=''>3
<option value=''>4
<option value=''>5
<option value=''>6
</select>&nbsp;&nbsp; Pages:&nbsp;&nbsp; 
          First&nbsp; Previous&nbsp;<a href="">Next</a>&nbsp;<a href=""> Last</a> 
        </div>
     </td>
     </tr>  -->
</body>
</html>
   <script language="javascript">
	//setResizeAble(mytable);
 </script>