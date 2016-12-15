<%@ include file="/common/head.jsp"%>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n"%>
<%@page
    import="java.util.*,com.aiait.eflow.housekeeping.vo.SystemFieldVO,com.aiait.eflow.formmanage.vo.DictionaryDataVO,com.aiait.eflow.util.StringUtil"%>
<%@page import="com.aiait.eflow.wkf.vo.*,com.aiait.eflow.common.helper.CompanyHelper"%>

<%
    //-----
			String qryRequestNo = (String) request.getParameter("qryRequestNo");
			String singleForm = (String) request.getParameter("singleForm");
			Collection queryRefForms = (Collection) request
					.getAttribute("queryRefForms");
			String[] selectRefForms = (String[]) request
					.getAttribute("selectRefForms");
			HashMap map = new HashMap();
			if (selectRefForms != null) {
				for (int i = 0; i < selectRefForms.length; i++) {
					//System.out.println("==> " + selectRefForms[i]);
					map.put(selectRefForms[i], selectRefForms[i]);
				}
			}
%>
<html>
<head>
<title>Select Reference Form</title>
</head>
<META HTTP-EQUIV="pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
<META HTTP-EQUIV="expires" CONTENT="Wed, 26 Feb 1997 08:21:57 GMT">
<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
<i18n:jsmessage jsRelativePath="js" prefixName="message" />
<style type=text/css>
.multipleSelectBoxControl SPAN {
    FONT-WEIGHT: bold;
    FONT-SIZE: 11px;
    FONT-FAMILY: arial
}

.multipleSelectBoxControl DIV SELECT {
    FONT-FAMILY: arial;
    HEIGHT: 100%
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
    MARGIN: 10px;
    WIDTH: 500px
}
</style>
<i18n:jsmessage jsRelativePath="js" prefixName="message" />
<script language="javascript" src="<%=request.getContextPath()%>/js/selectmulti.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
<script language="javascript">	
	function trim(str) {
    	for (var i = 0; i < str.length&&str.charAt(i)==" "; i++);
    	for(var j = str.length; j > 0&&str.charAt(j-1)==" "; j--);
    	if (i > j) return "";
    	return str.substring(i, j);
	}
    
	function dividestr(str) {
		var retstr;
		retstr = str.substring(0, str.lastIndexOf(","));
		return trim(retstr);
	}
    
	function getSelectRefForms() {
		var obj = document.getElementById('toBox');
		var selectRefForms = "";
		for (var i = 0; i < obj.options.length; i++) {
		    selectRefForms = selectRefForms + obj.options[i].value + ",";
		}
		selectRefForms = dividestr(selectRefForms);
	    return selectRefForms;
	}

	function submitForm() {
<%if(!"1".equals(request.getAttribute("multiSelection"))){%>
        if(document.getElementById('toBox').options.length>1){
          alert(single_eform);
          return;
        }
<%}%>   
		window.returnValue = getSelectRefForms();
	    window.close();
	}

	function submitNA() {
		window.returnValue = dividestr("N/A.,");
	    window.close();
	}
	function searchForm() {
	    var selectRefForms = encodeURIComponent(getSelectRefForms());
        //alert("selectRefForms: " + selectRefForms);return;
        
        var qryRequestNo = document.getElementById('qryRequestNo').value.Trim();
        if (qryRequestNo == "") {
            alert('<i18n:message key="reference_form.check_request_no" />');
            document.getElementById('qryRequestNo').focus();
            return;
        }
        var qryRequestNo = encodeURIComponent(qryRequestNo);
        
        var url = "<%=request.getContextPath()%>/formManageAction.it?method=selectRefForm&selectRefForms=" + selectRefForms
            + "&qryRequestNo=" + qryRequestNo + "&multiSelection=<%=request.getAttribute("multiSelection")%>";
        window.returnValue = url;
	    window.close();
	}
	
</script>
<body>
<form id="Form1" method="post">
<table WIDTH=100% bordercolor="#6595D6" style="border-collapse: collapse;" BORDER=1 CELLPADDING=3 CELLSPACING=0
    class="tr0">
    <tr>
        <td width=20% height="20" class="tr3">
        <div align="right"><span class="style1"><i18n:message key="common.request_no" /></span></div>
        </td>
        <td height="20"><font size="2"> <input name="qryRequestNo" type="text"
            value="<%=qryRequestNo == null ? "" : qryRequestNo%>" class="text2" style="WIDTH: 130px" id="qryRequestNo">
        </font>&nbsp;&nbsp;<input type="button" name="searchBtn" value='<i18n:message key="button.search"/>'
            class=btn3_mouseout onmouseover="this.className='btn3_mouseover'"
            onmouseout="this.className='btn3_mouseout'" onmousedown="this.className='btn3_mousedown'"
            onmouseup="this.className='btn3_mouseup'" onclick="searchForm()"></td>
    </tr>
</table>
<table width="100%" border="1" cellpadding="3" cellspacing="0" bordercolor="#6595D6" style="border-collapse: collapse;">
    <tr>
        <td style="WIDTH: 323px; HEIGHT: 21px" noWrap><select id='fromBox' multiple name='fromBox'>
            <%
                //
            			if (queryRefForms != null) {
            				Iterator it = queryRefForms.iterator();
            				while (it.hasNext()) {
            					WorkFlowProcessVO vo = (WorkFlowProcessVO) it.next();
            					String value = vo.getRequestNo() + "."
            							+ vo.getFormSystemId();
            %>
            <option value="<%=value%>"><%=vo.getRequestNo()%></option>
            <%
                //
            				}
            			}
            %>
        </select> <select id='toBox' multiple name='toBox' multiple>
            <%
                //
            			if (selectRefForms != null) {
            				for (int i = 0; i < selectRefForms.length; i++) {
            					String requestNo = selectRefForms[i].substring(0,
            							selectRefForms[i].lastIndexOf("."));
            %>
            <option value="<%=selectRefForms[i]%>"><%=requestNo%></option>
            <%
                //
            				}
            			}
            %>
        </select> <script language="javascript">
           		createMovableOptions("fromBox","toBox",450,300,'<i18n:message key="reference_form.leftbox"/>','<i18n:message key="reference_form.rightbox"/>');
       		</script></td>
    </tr>
    <tr>
        <td align='center'><input id="btnSelect" type="button" value='<i18n:message key="button.confirm"/>'
            onclick="javascript:submitForm()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'"
            onmouseout="this.className='btn3_mouseout'" onmousedown="this.className='btn3_mousedown'"
            onmouseup="this.className='btn3_mouseup'">&nbsp;&nbsp; 
            
<%if(CompanyHelper.EFlow_AIA_CHINA.equals(CompanyHelper.getInstance().getEFlowCompany())){%>
			<input name="btnNA" type="button"
            onclick="javascript:submitNA();" value='<i18n:message key="button.ref_form_na"/>' class=btn3_mouseout
            onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
            onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
<%}else{%>
            <input name="btnClose" type="button"
            onclick="javascript:window.close();" value='<i18n:message key="button.cancel"/>' class=btn3_mouseout
            onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
            onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
<%}%>
        </td>
    </tr>
</table>
</form>
</body>
</html>