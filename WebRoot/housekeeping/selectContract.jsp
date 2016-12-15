<%@ include file="/common/head.jsp"%>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n"%>
<%@page
    import="java.util.*,com.aiait.eflow.housekeeping.vo.SystemFieldVO,com.aiait.eflow.formmanage.vo.DictionaryDataVO,com.aiait.eflow.util.StringUtil"%>
<%@page import="com.aiait.eflow.housekeeping.vo.*"%>

<%
    //-----
			String qryContractNo = (String) request
					.getParameter("qryContractNo");
			Collection queryContracts = (Collection) request
					.getAttribute("queryContracts");
			String[] selectedContracts = (String[]) request
					.getAttribute("selectedContracts");
			HashMap map = new HashMap();
			if (selectedContracts != null) {
				for (int i = 0; i < selectedContracts.length; i++) {
					//System.out.println("==> " + selectRefForms[i]);
					map.put(selectedContracts[i], selectedContracts[i]);
				}
			}
%>
<html>
<head>
<title>Select Contract</title>
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
    
	function getSelectedContracts() {
		var obj = document.getElementById('toBox');
		var selectedContracts = "";
		for (var i = 0; i < obj.options.length; i++) {
		    selectedContracts = selectedContracts + obj.options[i].value + ",";
		}
		selectedContracts = dividestr(selectedContracts);
	    return selectedContracts;
	}

	function submitForm() {
		window.returnValue = getSelectedContracts();
	    window.close();
	}

	function search() {
	    var selectedContracts = encodeURIComponent(getSelectedContracts());
        //alert("selectedContracts: " + selectedContracts);return;
        
        var qryContractNo = document.getElementById('qryContractNo').value.Trim();
        if (qryContractNo == "") {
            alert('<i18n:message key="reference_contract.check_contract_no" />');
            document.getElementById('qryContractNo').focus();
            return;
        }
        var qryContractNo = encodeURIComponent(qryContractNo);
        
        var url = "<%=request.getContextPath()%>/contractAction.it?method=selectContract&selectedContracts=" + selectedContracts
            + "&qryContractNo=" + qryContractNo;
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
        <div align="right"><span class="style1"><i18n:message key="housekeeping_contract.contract_no" /></span></div>
        </td>
        <td height="20"><font size="2"> <input name="qryContractNo" type="text"
            value="<%=qryContractNo == null ? "" : qryContractNo%>" class="text2" style="WIDTH: 130px"
            id="qryContractNo"> </font>&nbsp;&nbsp;<input type="button" name="searchBtn"
            value='<i18n:message key="button.search"/>' class=btn3_mouseout
            onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
            onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'" onclick="search()"></td>
    </tr>
</table>
<table width="100%" border="1" cellpadding="3" cellspacing="0" bordercolor="#6595D6" style="border-collapse: collapse;">
    <tr>
        <td style="WIDTH: 323px; HEIGHT: 21px" noWrap><select id='fromBox' multiple name='fromBox'>
            <%
                //
            			if (queryContracts != null) {
            				Iterator it = queryContracts.iterator();
            				while (it.hasNext()) {
            				    ContractVO vo = (ContractVO) it.next();
            %>
            <option value="<%=vo.getContractNo()%>"><%=vo.getContractNo()%></option>
            <%
                //
            				}
            			}
            %>
        </select> <select id='toBox' multiple name='toBox' multiple>
            <%
                //
            			if (selectedContracts != null) {
            				for (int i = 0; i < selectedContracts.length; i++) {
            %>
            <option value="<%=selectedContracts[i]%>"><%=selectedContracts[i]%></option>
            <%
                //
            				}
            			}
            %>
        </select> <script language="javascript">
           		createMovableOptions("fromBox","toBox",450,300,'<i18n:message key="reference_contract.leftbox"/>','<i18n:message key="reference_contract.rightbox"/>');
       		</script></td>
    </tr>
    <tr>
        <td align='center'><input id="btnSelect" type="button" value='<i18n:message key="button.confirm"/>'
            onclick="javascript:submitForm()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'"
            onmouseout="this.className='btn3_mouseout'" onmousedown="this.className='btn3_mousedown'"
            onmouseup="this.className='btn3_mouseup'">&nbsp;&nbsp; <input name="btnClose" type="button"
            onclick="javascript:window.close();" value='<i18n:message key="button.cancel"/>' class=btn3_mouseout
            onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
            onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'"></td>
    </tr>
</table>
</form>
</body>
</html>