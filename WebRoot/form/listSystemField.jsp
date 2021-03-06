<%@ page contentType="text/html;charset=GBK" pageEncoding="GBK"%>
<%@ include file="/common/loading.jsp" %>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@page import="java.util.*,com.aiait.eflow.formmanage.vo.*,com.aiait.eflow.common.helper.*,com.aiait.eflow.basedata.vo.*,com.aiait.eflow.housekeeping.vo.*" %>
<%@page import="com.aiait.eflow.basedata.vo.BaseDataVO" %>
<html>
<head>
 <title><i18n:message key="form_design.list_system_field"/></title>
 <link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
 <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/loading.css" />
 <script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
 <script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
 <script language="javascript" src="<%=request.getContextPath()%>/js/ajax.js"></script>
 <script language="javascript">
    function changeSelectOption(fieldId,fieldLabel,columnType,fieldLenth){
       document.forms[0].fieldId.value = fieldId;
       document.forms[0].fieldLabel.value = fieldLabel;
    }
    function selectSystemField(){
       if(document.all['fieldId'].value==""){
         alert(basedatamain_not_sel_masterf);
         return;
      }
      window.opener.document.getElementById("showSystemFieldLabel").innerHTML = document.all['fieldLabel'].value;
      window.opener.document.all['systemFieldId'].value = document.forms[0].fieldId.value;
      window.close();
    }
 </script>
 <body>
<form name="listForm">
 <input type="hidden" name="fieldId">
 <input type="hidden" name="fieldLabel">
 <input type="hidden" name="columnType">
 <input type="hidden" name="fieldLenth">
 <table width="100%"  border="0">
     <tr class="tr1">
       <td align='Center'>
         <input type="button" name="selBtn" value='<i18n:message key="button.submit"/>' onclick="selectSystemField()">
         <input type="button" name="closeBtn" value='<i18n:message key="button.close"/>' onclick="javascript:window.close()">
       </td>
     </tr>
</table>
 <table width="100%" border="1" cellpadding="3" cellspacing="0" bordercolor="#6595D6" style="border-collapse:collapse;" >
        <tr>
          <td align='center'>
          <b><i18n:message key="form_design.list_system_field"/></b>
          </td>
       </tr>
       <% 
       /* [5/13/2015 10:21 AM] Wu, Sting-SS: 
    	   就是要改成进入到某一个field，根据field Id去数据库读取
    	   而不是从list里边读取就可以了
    	   问题详述:
    		   当页面加载的时候
    	   表里有field_id data_sql两个字段，通过field_id来查找到data_sql的数据库查询语句，然后传给Dao类（第一次数据库查询），
    	   取出了查询语句，让其进行数据库操作，（第二次数据库查询），
    	   将查询结果rs取代list，
 		*/
         Collection systemFieldlist = SystemFieldHelper.getInstance().getSystemFieldList();
         //System.out.println("============================systemfieldlist.size()="+systemFieldlist.size());
         if(systemFieldlist!=null && systemFieldlist.size()>0){
        	 Iterator sysIt = systemFieldlist.iterator();
        	 while(sysIt.hasNext()){
        		 SystemFieldVO sysVo = (SystemFieldVO)sysIt.next();
       %>
        <tr>
         <td colspan='2'>
          <input type="radio" name="systemFieldId"  value="<%=sysVo.getFieldId()%>" onclick="changeSelectOption('<%=sysVo.getFieldId()%>','<%=sysVo.getFieldLabel().replace("'","")%>','<%=sysVo.getColumnType()%>','<%=sysVo.getColumnLength()%>')"> 
          	<%=sysVo.getFieldLabel()%> - (<%=sysVo.getFieldId()%>)            
          <input type="hidden" name="label_<%=sysVo.getFieldId()%>" value="<%=sysVo.getFieldLabel()%>">
         </td>
       </tr>
       <%
        	 }
         }else{
       %>
        <tr>
         <td colspan='2'>
           Have no data
         </td>
        </tr> 
       <%
         }
       %>
       </table>
   </form>
  </body>
  </html>