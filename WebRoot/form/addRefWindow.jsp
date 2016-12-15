<%@ include file="/common/head.jsp" %>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@page import="java.util.*,com.aiait.eflow.housekeeping.vo.SystemFieldVO,com.aiait.eflow.formmanage.vo.DictionaryDataVO,com.aiait.eflow.util.StringUtil"%>
<%
	SystemFieldVO sysfieldvo = (SystemFieldVO)request.getAttribute("RefFormField");
    String	requestnoList = (String)request.getAttribute("requestnoList");    
    String[] requestnoarr = null;
    HashMap map = new HashMap();
    if(requestnoList!=null && !"".equals(requestnoList)){
    	requestnoarr = StringUtil.split(requestnoList,",");
  	  for(int i=0;i<requestnoarr.length;i++){
  		System.out.println("==>"+i+requestnoarr[i]);
  		  map.put(requestnoarr[i],requestnoarr[i]);
  	  }
    }   
%>
<html>
<head>
<title>Add Reference Form</title>
</head>
  <META HTTP-EQUIV="pragma" CONTENT="no-cache">
  <META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
  <META HTTP-EQUIV="expires" CONTENT="Wed, 26 Feb 1997 08:21:57 GMT">
<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
  <script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
<STYLE type=text/css>
.multipleSelectBoxControl SPAN {
	FONT-WEIGHT: bold; FONT-SIZE: 11px; FONT-FAMILY: arial
}
.multipleSelectBoxControl DIV SELECT {
	FONT-FAMILY: arial; HEIGHT: 100%
}
.multipleSelectBoxControl INPUT {
	WIDTH: 25px
}
.multipleSelectBoxControl DIV {
	FLOAT: left
}
.multipleSelectBoxDiv {
	
}
FIELDSET {
	MARGIN: 10px; WIDTH: 500px
}
</STYLE>
<script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/selectmulti.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
<script language="javascript">	
	function  trim(str)
	{
    	for(var i=0;  i<str.length&&str.charAt(i)==" ";i++);
    	for(var j=str.length;j>0&&str.charAt(j-1)==" ";j--);
    	if(i>j)  return  "";  
    	return  str.substring(i,j);  
	}
	function dividestr(str){
		var retstr;
		retstr = str.substring(0,str.lastIndexOf(","));
		return trim(retstr);
	}
	function submitForm(){
		var obj = document.getElementById('toBox[]');		
		var selectRefFormStr = "";
		for(var no=0;no<obj.options.length;no++){		
			selectRefFormStr = selectRefFormStr + obj.options[no].value+",";
		}		
		window.returnValue = dividestr(selectRefFormStr);
	    window.close();
	}
	
</script>
<body>
<form id="Form1" method="post">
<table width="100%"  border="1" cellpadding="3" cellspacing="0" bordercolor="#6595D6" style="border-collapse:collapse;" >
	<TR>
		<TD style="WIDTH: 323px; HEIGHT: 21px" noWrap>
			<SELECT id=fromBox multiple name=fromBox>
        	<%        	
        		if (sysfieldvo.getOptionList()!=null&&sysfieldvo.getOptionList().size()>0){
        			Iterator it = sysfieldvo.getOptionList().iterator();
        			while(it.hasNext()){
        				DictionaryDataVO op = (DictionaryDataVO)it.next();        				
        				if (!map.containsKey(op.getId())){
        					//System.out.println("==>"+op.getId());	
        	%>
        		<option value="<%=op.getId()%>"><%=op.getValue()%></option>
        	<%
        				}
        			}
        		}
        	%>
	   		</SELECT>
			<SELECT id='toBox[]' multiple name='toBox[]' multiple>
      		<%
      			if (requestnoarr!=null){
      				for(int i=0;i<requestnoarr.length;i++){      			
      		%>
      			<option value="<%=requestnoarr[i]%>"><%=requestnoarr[i]%></option>
      		<%
      				}
      			}
      		%>
			</SELECT>
			<script language="javascript">
           		createMovableOptions("fromBox","toBox[]",450,300,'<i18n:message key="reference_form.leftbox"/>','<i18n:message key="reference_form.rightbox"/>');
       		</script>
		</TD>
	</TR>
	<TR>
    	<TD  align='center'>
        	<input id="btnSelect"  type="button" value='<i18n:message key="button.submit"/>' onclick="javascript:submitForm()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           		onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">&nbsp;&nbsp;
			<input name="btnClose" type="button" onclick="javascript:window.close();" value='<i18n:message key="button.close"/>' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           		onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
		</TD>
  	</TR>
</table>
</form>
</body>
</html>