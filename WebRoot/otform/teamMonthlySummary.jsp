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
     <td colspan='2' align='center'>姓名 Name</td>
     <td rowspan="2" align='center'>员工编号 Staff Code</td>
     <td colspan='3' align='center'>误餐次数 No.of Meal Allowance</td>
     <td rowspan="2" align='center'>法定假期(小时)Statutory Holiday</td>
     <td colspan='3' align='center'>误餐津贴金额(元) Amount of Meal Allowance</td>
     <td rowspan="2" align='center'>的士费用总计 Taxi Fee Total</td>
   </tr>
   <tr class="liebiao_tou">
     <td align='center'>中文名 Chinese Name</td>
     <td align='center'>英文名 English Name</td>
     <td align='center'>正常工作日 Weekday</td>
     <td align='center'>公众假期 Public Holiday</td>
     <td align='center'>小计 Sub-total</td>
     <td align='center'>误餐津贴总额 Total Amount</td>
     <td align='center'>税前报销金额(无发票) Pre-tax Amount</td>
     <td align='center'>税后报销金额(有发票) After-tax Amount</td>
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