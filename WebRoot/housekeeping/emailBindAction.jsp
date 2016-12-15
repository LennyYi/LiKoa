<%@ page contentType="text/html; charset=gb2312"%>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@page import="java.util.*,com.aiait.eflow.housekeeping.vo.FlowActionVO"%>
<%
  Collection fromList = (ArrayList)request.getAttribute("fromList");
  Collection toList = (ArrayList)request.getAttribute("toList");
%>
<HTML>
  <HEAD>
		<title>Bind Email Template Action</title>
  </HEAD>
  <META HTTP-EQUIV="pragma" CONTENT="no-cache">
  <META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
  <META HTTP-EQUIV="expires" CONTENT="Wed, 26 Feb 1997 08:21:57 GMT">
   <link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
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
<script language="javascript" src="<%=request.getContextPath()%>/js/ajax.js"></script>
<script language="javascript">
    function submitNode(){
        var obj = document.getElementById('toBox[]');
        var selectIdStr = "";
		for(var no=0;no<obj.options.length;no++){
			//obj.options[no].selected = true;
			selectIdStr = selectIdStr + obj.options[no].value + ",";
		}
		if(selectIdStr!=""){
		  selectIdStr = selectIdStr.substring(0,selectIdStr.length-1);
		}
		//alert(selectIdStr)
		window.returnValue = selectIdStr;
	    window.close();	
	}
  </script>
<BODY>
 <form id="Form1" method="post">
<table width="100%"  border="1" cellpadding="3" cellspacing="0" bordercolor="#6595D6" style="border-collapse:collapse;" >
   <TR>
	 <TD style="WIDTH: 323px; HEIGHT: 21px" noWrap>
		<SELECT id=fromBox multiple name=fromBox>
        <%
         if(fromList!=null && fromList.size()>0){
           Iterator staffIt = fromList.iterator();
           while(staffIt.hasNext()){
        	   FlowActionVO from = (FlowActionVO)staffIt.next();
        %>
          <option value="<%=from.getActionId()%>"><%=from.getActionName()%></option>
        <%
        	}
         }
        %>
	   </SELECT>
		<SELECT id=toBox[] multiple name=toBox[] multiple>
         <%
          if(toList!=null && toList.size()>0){
        	 Iterator it = toList.iterator();
        	 while(it.hasNext()){
        		 FlowActionVO to = (FlowActionVO)it.next();
        %>
             <option value="<%=to.getActionId()%>"><%=to.getActionName()%></option>
        <%
        	 }
         }
        %>
		</SELECT>
		<SCRIPT type=text/javascript>
           createMovableOptions("fromBox","toBox[]",450,300,'<i18n:message key="emailbindaction.from"/>','<i18n:message key="emailbindaction.to"/>');
       </SCRIPT>
	 </TD>
   </TR>
  <TR>
    <TD  align='center'>
        <input id="btnSelect"  type="button" value='<i18n:message key="button.submit"/>' onclick="javascript:submitNode()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">&nbsp;&nbsp;
		<input name="btnClose" type="button" onclick="javascript:window.close();" value='<i18n:message key="button.close"/>' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
	</TD>
  </TR>
 </TABLE>

</form>
</BODY>
</HTML>
