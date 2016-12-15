<%@ include file="/common/head.jsp" %>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@page import="com.aiait.eflow.formmanage.vo.FormSectionVO" %>
<% 
  String tableId = (String)request.getParameter("tableId");
  FormSectionVO section = (FormSectionVO)request.getAttribute("section");
  String type = (String)request.getParameter("type");
  if(section==null){
	  section = new FormSectionVO();
  }
  if(type==null){
	  type = "new";
  }
%>

<html>
<head>
 <title><i18n:message key="form_design.section_edit_title"/></title>
 <link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
 <script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
 <script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
 <script language="javascript" src="<%=request.getContextPath()%>/js/ajax.js"></script>
  <script language="javascript">
 
    function submitForm(){
      var tableId = "<%=tableId%>";
      var type = "<%=type%>";

      if(formValidate(document.forms[0])==false){
        return;
      } 
      var  reg   =   /\s/g;     
      var  sectionId = document.all['sectionId'].value;
      if(sectionId.indexOf(" ")>=0){
        alert("Section Id can not include the blank");
        document.all['sectionId'].focus();
        return;
      }
      
      var patrn=/^([a-zA-Z_0-9]|[_]|[-]){0,10}$/; 
         if (!patrn.exec(sectionId.Trim())){
            alert("Section Id is invalid! It can be only the composition of letters and numbers,'_'.");
            document.all['sectionId'].focus();
            return;
         } 
      
      sectionId = sectionId.replace(reg, "");
      sectionId = sectionId.Trim();

      //alert(sectionId)
      //return;
      //var sectionName = formatStringAllChar(document.all['sectionRemark'].value.Trim());
      var sectionName = encodeURI(encodeURI(document.all['sectionRemark'].value.Trim()));
      //alert("sectionName: " + sectionName);
      
      var url = "<%=request.getContextPath()%>/formManageAction.it?method=saveFormSection&formSystemId="+document.all['formSystemId'].value
                +"&sectionId="+sectionId+"&sectionType="+document.all['sectionType'].value
                +"&sectionRemark="+sectionName+"&type="+type;
      //alert(url)
      //return;
      var  xmlhttp = createXMLHttpRequest();
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
       
       if(type=="edit"){
         if(result=="fail"){
          alert("Fail to update section!")
          return;
         }
         window.opener.location.reload();
         window.close();
       }else{
       
       if(result=="fail"){
         alert("Fail to create section!")
         return;
       }
       
       if(result.Trim()=="exist"){
         alert("Application Basic Information of the form already existed!")
         return;
       }
       if(result.Trim()=="multi-sectionid"){
         alert("Section("+sectionId+") of the form already existed!")
         return;
       }
       
       var oTable = window.opener.document.all[tableId];
     
       var oTR = oTable.insertRow(oTable.rows.length);
       var oTD = oTR.insertCell(0);
       
       oTD.innerHTML = result;
       oTR = oTable.insertRow(oTable.rows.length);
       oTD = oTR.insertCell(0);
       if(document.all['sectionType'].value=="02"){
         oTD.innerHTML = "<table id='sectionTable"+document.all['sectionId'].value+"' width='100%'  border='1' cellpadding='3' cellspacing='0' bordercolor='#6595D6' style='border-collapse:collapse;'>"
          +"<tr><td onclick='SelectRow(this)' ondblclick='editField(this,"+"\"sectionTable"+document.all['sectionId'].value+"\",\""+document.all['sectionId'].value+"\",\"-999\",\"02\")'>&nbsp;&nbsp;</td><td onclick='SelectRow(this)' ondblclick='editField(this,"+"\"sectionTable"+document.all['sectionId'].value+"\",\""+document.all['sectionId'].value+"\",\"-999\",\"02\")'>&nbsp;&nbsp;</td></tr></table>";
        }else if(document.all['sectionType'].value=="01"){
          oTD.innerHTML = "<table id='sectionTable"+document.all['sectionId'].value+"' width='100%'  border='1' cellpadding='3' cellspacing='0' bordercolor='#6595D6' style='border-collapse:collapse;'>"
          +"<tr class='tr4'><td>&nbsp;&nbsp;</td></tr><tr><td onclick='SelectRow(this)' id='-999' ondblclick='editField(this,"+"\"sectionTable"+document.all['sectionId'].value+"\",\""+document.all['sectionId'].value+"\",\"-999\",\"01\")'>&nbsp;&nbsp;</td></tr></table>";
        }else{
         //oTD.innerHTML = "<table id='sectionTable"+document.all['sectionId'].value+"' width='100%'  border='1' cellpadding='3' cellspacing='0' bordercolor='#6595D6' style='border-collapse:collapse;'>"
         // +"<tr><td onclick='SelectRow(this)'></td><td onclick='SelectRow(this)' ondblclick='editField(this,"+"\"sectionTable"+document.all['sectionId'].value+"\",\""+document.all['sectionId'].value+"\",\"-999\",\"02\")'>&nbsp;&nbsp;</td></tr></table>";          
         window.opener.location.reload();
        }
       window.close();
       }
    }
    
    function sectionTypeChange(obj){
      if(obj.value=="03"){
        document.all['sectionRemark'].readonly = "true";
        document.all['sectionRemark'].value = "Application Basic Information";
      }else if(obj.value=="00"){
        document.all['sectionRemark'].value = "Attachment";
        document.all['sectionRemark'].readonly = "false";
      }else{
        document.all['sectionRemark'].readonly = "false";
        document.all['sectionRemark'].value = "";        
      }
    }
    
 </script>
</head>
<body>
<form name='testForm'>
   <input type="hidden" name="formSystemId" value="<%=request.getParameter("formSystemId")%>">
   <table width="100%"  border="1" cellpadding="3" cellspacing="0" bordercolor="#6595D6" style="border-collapse:collapse;" >
     <tr>
       <td style="WIDTH:30%" align='right'>
         <i18n:message key="form_design.section_id"/> : 
       </td>
       <td style="WIDTH:70%">  
         <input type='text' name='sectionId' title='<i18n:message key="form_design.section_id"/>' required="true" size='10' maxlength='10' value='<%=section.getSectionId()==null?"":section.getSectionId()%>'
                       <%=section.getSectionId()==null?"":"readonly"%>>
         (<font color='red'>*</font>)
       </td>
     </tr>
     <tr>
       <td style="WIDTH:30%" align='right'>
         <i18n:message key="form_design.section_type"/> :
       </td>
       <td style="WIDTH:70%">
         <%if("new".equals(type)){%>
           <select name="sectionType" onchange="sectionTypeChange(this)">
            <option value="03" ><i18n:message key="form_design.basic_section"/></option>
            <option value="02" ><i18n:message key="form_design.common_section"/></option>
            <option value="01" ><i18n:message key="form_design.table_section"/></option>
            <option value="00" ><i18n:message key="form_design.attach_section"/></option>
          </select>
         <%}else{%>
          <select name="sectionType1" onchange="sectionTypeChange(this)" disabled>
            <option value="03" <%=(section.getSectionType()!=null && "03".equals(section.getSectionType()))?"selected":""%>><i18n:message key="form_design.basic_section"/></option>
            <option value="02" <%=(section.getSectionType()!=null && "02".equals(section.getSectionType()))?"selected":""%>><i18n:message key="form_design.common_section"/></option>
            <option value="01" <%=(section.getSectionType()!=null && "01".equals(section.getSectionType()))?"selected":""%>><i18n:message key="form_design.table_section"/></option>
            <option value="00" <%=(section.getSectionType()!=null && "00".equals(section.getSectionType()))?"selected":""%>><i18n:message key="form_design.attach_section"/></option>
          </select>
          <input type="hidden" name="sectionType" value="<%=section.getSectionType()%>">
         <%}%>
       </td>
     </tr>
     <tr>
       <td style="WIDTH:30%" align='right'>
         <i18n:message key="form_design.section_name"/> : 
       </td>
       <td style="WIDTH:70%">  
       <input type='text' name='sectionRemark' title='<i18n:message key="form_design.section_name"/>' required="true" size='40' length='40' value='<%=section.getSectionRemark()==null?"":section.getSectionRemark()%>'>(<font color='red'>*</font>)
       </td>
     </tr>
     <tr>
       <td align='center' colspan='2'>
         <input type='button' name='ok' value='<i18n:message key="button.submit"/>' onclick='submitForm()' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
         <input type='Reset' name='reset1' value='<i18n:message key="button.reset"/>' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
         <input type='button' name='close1' value='<i18n:message key="button.close"/>' onclick='window.close()' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
       </td>
     </tr>
   </table>
</form>
</body>
</html>