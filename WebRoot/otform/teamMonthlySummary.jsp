<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@ page contentType="text/html;charset=GBK" pageEncoding="GBK"%>
<%@ include file="/common/loading.jsp" %>
<%@page import="java.util.*,com.aiait.eflow.wkf.vo.WorkFlowItemVO"%>

<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
  <script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/ajax.js"></script>

<body style="margin:0">
 <div id='otRecordsListDiv'>
  <table id='formTable02' width="100%"  border="1" cellpadding="3" cellspacing="0" bordercolor="#6595D6" style="border-collapse:collapse;">
   <tr class="liebiao_tou">
     <td align='center' rowspan="2" ><input type="checkbox" name="allBtn" onclick="selectAll(this,'staffCode')"></td>
     <td colspan='2' align='center'>���� Name</td>
     <td rowspan="2" align='center'>Ա����� Staff Code</td>
     <td colspan='3' align='center'>��ʹ��� No.of Meal Allowance</td>
     <td rowspan="2" align='center'>��������(Сʱ)Statutory Holiday</td>
     <td colspan='3' align='center'>��ͽ������(Ԫ) Amount of Meal Allowance</td>
     <td rowspan="2" align='center'>��ʿ�����ܼ� Taxi Fee Total</td>
   </tr>
   <tr class="liebiao_tou">
     <td align='center'>������ Chinese Name</td>
     <td align='center'>Ӣ���� English Name</td>
     <td align='center'>���������� Weekday</td>
     <td align='center'>���ڼ��� Public Holiday</td>
     <td align='center'>С�� Sub-total</td>
     <td align='center'>��ͽ����ܶ� Total Amount</td>
     <td align='center'>˰ǰ�������(�޷�Ʊ) Pre-tax Amount</td>
     <td align='center'>˰�������(�з�Ʊ) After-tax Amount</td>
   </tr>
  </table>
</div>
</body>
<script language="javascript">
  var queryString=function(key){
   	return (window.parent.document.location.search.match(new RegExp("(?:^\\?|&)"+key+"=(.*?)(?=&|$)"))||['',null])[1];
  };
  var yearMonth = window.parent.document.all['field_02_1'].value;
  var teamCode = window.parent.document.all['sys_team_code'].value;
  if(yearMonth=="" && teamCode==""){
	  window.parent.document.all['field_02_1'].value = queryString('yearMonth');
	  window.parent.document.all['sys_team_code'].value = queryString('teamCode');
	  yearMonth = window.parent.document.all['field_02_1'].value;
	  teamCode = window.parent.document.all['sys_team_code'].value;
  }
  if(yearMonth!="" && teamCode!=""){
    var xmlhttp = createXMLHttpRequest();
    var url = "<%=request.getContextPath()%>/otFormAction.it?method=ajaxTeamOTSummary&teamCode="
              +teamCode+"&yearMonth="+yearMonth;
    var result;
          if(xmlhttp){
            xmlhttp.open('POST',url,false);
            xmlhttp.onreadystatechange = function()
            {
              if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
                 result = xmlhttp.responseText;
                 if(result.Trim()=="fail"){
                   alert("Fail to get the Meal Allowance Records!");
                 }else{
                   document.all["otRecordsListDiv"].innerHTML = result;
                 }
              }
           }
           xmlhttp.setRequestHeader("If-Modified-Since","0");
           xmlhttp.send(null);
        }
  }
</script>


<script language="javascript">
//    parent.document.all("otsmummary").style.height = document.body.scrollHeight;
</script>