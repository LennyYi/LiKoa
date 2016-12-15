<%@page import="java.util.*,com.aiait.eflow.housekeeping.vo.*, com.aiait.eflow.common.helper.*,com.aiait.eflow.common.*" %>
<%@page import="com.aiait.framework.util.*" %>
<%@ taglib uri="/WEB-INF/purview.tld" prefix="purview" %>
<%@ page contentType="text/html;charset=GBK" pageEncoding="GBK"%>
<%@ include file="/common/loading.jsp" %>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@ taglib uri="/WEB-INF/taglibs-page.tld" prefix="pageTag" %>
<html>
<head>
	<title>Supplier</title>
	<meta http-equiv="Content-Type" content="text/html; charset=GBK">
	<script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
	<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
	<script src="<%=request.getContextPath()%>/js/webtools.js" language="JavaScript"></script>
	<script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
	<SCRIPT language="javascript" src="<%=request.getContextPath()%>/js/sorttable.js"></SCRIPT>
	<script language="javascript" src="<%=request.getContextPath()%>/js/resizeCol.js"></script>
	<script language="javascript" src="<%=request.getContextPath()%>/js/ajax.js"></script>
	<script language="javascript" src="<%=request.getContextPath()%>/js/aiaitjsframe.js"></script>
	<script language="javascript">	
	  function addCode() {
	     var url = "<%=request.getContextPath()%>/supplierAction.it?method=editSupplier&editType=new";
	     window.location = url;
	  }
	  function search() {
	     //var url = "<%=request.getContextPath()%>/supplierAction.it?method=editSupplier&editType=new";
	     document.forms[0].action = "<%=request.getContextPath()%>/supplierAction.it?method=listSupplier&editType=search";
	     document.forms[0].submit();
	  }
	  
	  function deleteCode() {
	    if (checkSelect('code') <= 0) {
	      alert("You have not selected any records to delete!");
	      return;
	    }
	    if (confirm("Are you sure to delete the selected records")) {
	      document.forms[0].action = "<%=request.getContextPath()%>/supplierAction.it?method=deleteSupplier";
	      document.forms[0].submit();
	    }
	  }
	  function fileUpload(){
	      var url = "<%=request.getContextPath()%>/supplierAction.it?method=showExcelTemplateSelect";
	      openCenterWindow(url,450,100);
	  }
	 	function exportSupplier(){
			document.forms[0].action = "<%=request.getContextPath()%>/supplierAction.it?method=exportInquirySupplier";
			document.forms[0].target = "_blank";
			document.forms[0].submit();
			document.forms[0].target = "";
		}
	</script>
</head>

<% 
	StaffVO currentStaff = (StaffVO)request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
	SupplierVO searchvo = (SupplierVO)request.getSession().getAttribute("searchvo");
	if(searchvo==null)searchvo = new SupplierVO();
	Collection list = (Collection) request.getAttribute("supplierList");
	Collection teamList = StaffTeamHelper.getInstance().getTeamList();	
    String name_c = searchvo.getNameC();
    String status = searchvo.getStatus();
    String teamId = ""+searchvo.getTeamCode();
    String product = searchvo.getProduct();
    String orgId = searchvo.getOrgId();
    Iterator teamIt = null;
	if(teamList!=null){
		teamIt = teamList.iterator();
	}
	
	Collection companyList = (ArrayList)currentStaff.getOwnCompanyList();
%>
<body>
<form name="myForm" method="post">
<table border="0" width="100%" cellspacing="0" cellpadding="0">
 <tr>
 	<td height="10"></td>
 </tr>
 <!--<tr>
 	<td>
 	  <strong><font color='#5980BB' family='Times New Roman'><i18n:message key="housekeeping_supplier.location"/></font></strong>
 	 </td>
 </tr>
--></table>
   <table width="100%" bordercolor="#6595D6" style="border-collapse:collapse;"  BORDER=1 CELLPADDING=3 CELLSPACING=0 class="tr0" >
     <tr class="tr1">
       <td align='center' colspan=4><B><i18n:message key="housekeeping_supplier.title"/></B></td>
     </tr>
    
    <tr>
    	<TD width=15% height="20" class="tr3"><div align="right"><span class="style1">认证分类</span></div></TD>
      	<TD width=30% height="20">
         <!--select name="orgId" required="true" title="<i18n:message key="common.company"/>" onchange="getOptionList(this.value)"-->
         <select name="class" required="true" title="认证分类">
          	<option value=''></option>
            <%            
            /*if(companyList!=null && companyList.size()>0){
            	Iterator it = companyList.iterator();
            	while(it.hasNext()){
            	   CompanyVO company = (CompanyVO)it.next();
            	   if(!company.getOrgId().matches("Z07001|Z07002|Z07004|Z07005|Z07009"))continue;
            	   if(company.getOrgId().equals(orgId)){
            		   out.print("<option value='"+company.getOrgId()+"' selected>" + company.getOrgName()+"</option>");   
            	   }else{
            	     out.print("<option value='"+company.getOrgId()+"'>" + company.getOrgName()+"</option>");
            	   }
            	}
            }*/            
           %>
           <option value='1'>签约供应商</option>
           <option value='2'>预审供应商</option>
           <option value='3'>优选供应商</option>
           <option value='3'>采购管理除外项 </option>
         </select>
		</TD>
		<TD width=15% height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="housekeeping_supplier.type"/></span></div></TD>
		<TD width=35% height="20" > 
         <select name="type">
           <option value=""></option>
         </select>
		</TD>
    </tr>
    
    <tr>
    	<TD width=15% height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="housekeeping_supplier.name_c"/></span></div></TD>
      	<TD width=35% height="20" >
        	<input type="text"  value="<%=name_c==null?"":name_c%>" name="name_c">
      	</TD> 
    	<TD width=15% height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="housekeeping_supplier.product"/></span></div></TD>
      	<TD width=35% height="20" >
        	<input type="text"  value="<%=product==null?"":product%>" name="product">
      	</TD> 
	</tr>
	<tr>
		<!-- <TD width=15% height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="housekeeping_user.team"/></span></div></TD>
		<TD width=35% height="20" > 
         <select name="team_code">
           <option value="0"></option>
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
		</TD> -->
      	<TD width=15% height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="common.status"/></span></div></TD>
      	<TD width=35% height="20" > 
         <select name="status">    
           <option value="" ></option>
           <option value="A" <%=!"T".equals(status)?"selected":""%>>Active</option>
           <option value="T" <%="T".equals(status)?"selected":""%>>Terminated</option>
         </select>         
      	</TD>
      	<td class="tr3"></td><td></td>
    </tr>
     <tr >
       <td align='center' colspan=4>
        <purview:purview moduleId='56' operateId='2' isButton='true' labelValue='Add'>
		<input type="button" name="addBtn" value='<i18n:message key="button.add"/>' onclick="addCode()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'"/>
        </purview:purview>  
		&nbsp;
		<input type="button" name="SearchBtn" value='<i18n:message key="button.search"/>' onclick="search()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'"/>
		&nbsp;
		<input type="reset" name="resetBtn" value='<i18n:message key="button.reset"/>' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'"/>
		&nbsp;
		<input type="button" name="exportBtn1" value='<i18n:message key="button.export_excel"/>' onclick="exportSupplier()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'"/>
        &nbsp;  
        <purview:purview moduleId='56' operateId='2' isButton='true' labelValue='Upload'>
		<input type="button" name="uploadBtn1" value='<i18n:message key="button.selectFile"/>' onclick="fileUpload()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'"/>
        </purview:purview>
       </td>
     </tr>
     <tr>
      <td colspan=4>
        <table width="100%"  border="0" cellpadding="0" cellspacing="1" class=sortable id=mytable style="border:1px #8899cc solid;">
          <tr class="liebiao_tou">
            <td align='center' ><i18n:message key="common.company"/></td>            
            <td align='center' ><i18n:message key="housekeeping_supplier.type"/></td>
            <td align='center' ><i18n:message key="housekeeping_supplier.name_c"/></td>
            <td align='center' ><i18n:message key="common.status"/></td>
            <td align='center' ><i18n:message key="housekeeping_supplier.product"/></td>
            <td align='center' ><i18n:message key="housekeeping_supplier.contacter"/></td>
            <td align='center' ><i18n:message key="housekeeping_supplier.tel"/></td>
            <td align='center' ><i18n:message key="housekeeping_user.email"/></td>
          </tr>
          <%
			if (list != null) {
				int i = 1;
				Iterator it = list.iterator();
				while (it.hasNext()) {
				    SupplierVO vo = (SupplierVO) it.next();
          %>
            <tr class="tr_change">
              <td><%=CompanyHelper.getInstance().getOrgName(vo.getOrgId())%>&nbsp;</td> 
              <td><%=vo.getType()%>&nbsp;</td>
              <td>
		        <purview:purview moduleId='56' operateId='2' isButton='true' labelValue='Edit'>
		        <a href='<%=request.getContextPath()%>/supplierAction.it?method=editSupplier&editType=edit&code=<%=CommonUtil.encoderURL(vo.getCode().trim())%>&orgId=<%=vo.getOrgId()%>'>
		        </purview:purview>        
              <%=vo.getNameC()%></a>&nbsp;</td>
              <td><%=vo.getStatus()%>&nbsp;</td>
              <td><%=vo.getProduct()%>&nbsp;</td>
              <td><%=vo.getContacter()%>&nbsp;</td>
              <td><%=vo.getTel()%>&nbsp;</td>
              <td><%=vo.getEmail()==null?"":vo.getEmail()%>&nbsp;</td>
            </tr>
          <%i++;}
          }%>
        </table>
      </td>
     </tr>
   </table>
<pageTag:page action="/supplierAction.it?method=listSupplier&editType=navi"></pageTag:page>   
</form>
</body>
</html>
   <script language="javascript">
	setResizeAble(mytable);
 </script>