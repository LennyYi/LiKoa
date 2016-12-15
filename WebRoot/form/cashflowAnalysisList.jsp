<%@ page contentType="text/html;charset=GBK" pageEncoding="GBK"%>
<%@page import="com.aiait.eflow.common.CommonName,com.aiait.eflow.common.helper.ParamConfigHelper" %>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@ include file="/common/loading.jsp" %>
<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
<script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/ajax.js"></script>
<style type="text/css">
.input1 {color: rgb(255,0,0); border: medium none;background:transparent;text-align:right;width:100px}
.input2 {color: rgb(255,0,0); border: medium none;background:transparent;text-align:right;font-weight:bolder;width:100px}
.input3 {color: #0000FF; border: medium none;background:transparent;text-align:right;font-weight:bolder;width:100px}
</style>

<body>
  <table width="100%" border="0" cellpadding="0" cellspacing="1"  id=mytable style="border:1px #8899cc solid;">  
          <tr class="liebiao_tou">
            <td align='center' width="20%">Year</td>
            <td align='center' >Yr0</td>
            <td align='center' >Yr1</td>
            <td align='center' >Yr2</td>
            <td align='center' >Yr3</td>
            <td align='center' >Yr4</td>
            <td align='center' >Yr5</td>
            <td align='center' >Total</td>
          </tr>
          <tr class="tr_change">
            <td colspan='8'> <b>(A)Leasehold/ rental</b> </td>
          </tr> 
          <tr class="tr_change">
            <td align='left'>&nbsp;&nbsp;&nbsp;&nbsp;-Rental </td>
            <td align='right'>-</td>
            <td align='right'><input type="text" value="" name="c23" readonly class='input1'></td>
            <td align='right'><input type="text" value="" name="d23" readonly class='input1'></td>
            <td align='right'><input type="text" value="" name="e23" readonly class='input1'></td>
            <td align='right'><input type="text" value="" name="f23" readonly class='input1'></td>
            <td align='right'><input type="text" value="" name="g23" readonly class='input1'></td>
            <td align='right'><input type="text" value="" name="h23" readonly class='input1'></td>
          </tr> 
          <tr class="tr_change">
            <td align='left'>&nbsp;&nbsp;&nbsp;&nbsp;-Automobile Expenses </td>
            <td align='right'>-</td>
            <td align='right'><input type="text" value="" name="c24" readonly class='input1'></td>
            <td align='right'><input type="text" value="" name="d24" readonly class='input1'></td>
            <td align='right'><input type="text" value="" name="e24" readonly class='input1'></td>
            <td align='right'><input type="text" value="" name="f24" readonly class='input1'></td>
            <td align='right'><input type="text" value="" name="g24" readonly class='input1'></td>
            <td align='right'><input type="text" value="" name="h24" readonly class='input1'></td>
          </tr> 
          <tr class="tr_change">
            <td align='left'>&nbsp;&nbsp;&nbsp;&nbsp;-Salary for driver </td>
            <td align='right'>-</td>
            <td align='right'><input type="text" value="" name="c25" readonly class='input1'></td>
            <td align='right'><input type="text" value="" name="d25" readonly class='input1'></td>
            <td align='right'><input type="text" value="" name="e25" readonly class='input1'></td>
            <td align='right'><input type="text" value="" name="f25" readonly class='input1'></td>
            <td align='right'><input type="text" value="" name="g25" readonly class='input1'></td>
            <td align='right'><input type="text" value="" name="h25" readonly class='input1'></td>
          </tr>  
          <tr class="tr_change">
            <td align='left'>&nbsp;&nbsp;&nbsp;&nbsp;<b>Total Cash Outlay (Nominal)</b></td>
            <td align='right'>-</td>
            <td align='right'><input type="text" value="" name="c26" readonly class='input2'></td>
            <td align='right'><input type="text" value="" name="d26" readonly class='input2'></td>
            <td align='right'><input type="text" value="" name="e26" readonly class='input2'></td>
            <td align='right'><input type="text" value="" name="f26" readonly class='input2'></td>
            <td align='right'><input type="text" value="" name="g26" readonly class='input2'></td>
            <td align='right'><input type="text" value="" name="h26_1" readonly class='input2'>
            <input type="hidden" name='h26'>
            </td>
          </tr>   
          <tr class="tr_change">
            <td align='left'>&nbsp;&nbsp;&nbsp;&nbsp;<b>Total Cash Outlay (NPV)</b></td>
            <td align='right'>-</td>
            <td align='right'><input type="text" value="" name="c27" readonly class='input2'></td>
            <td align='right'><input type="text" value="" name="d27" readonly class='input2'></td>
            <td align='right'><input type="text" value="" name="e27" readonly class='input2'></td>
            <td align='right'><input type="text" value="" name="f27" readonly class='input2'></td>
            <td align='right'><input type="text" value="" name="g27" readonly class='input2'></td>
            <td align='right'><input type="text" value="" name="h27_1" readonly class='input2'><input type="hidden" name='h27'></td>
          </tr>    
          <tr class="tr_change">
            <td colspan='8'>&nbsp;&nbsp;</td>
          </tr> 
          <tr class="tr_change">
            <td colspan='8'> <b>(B) Purchase</b> </td>
          </tr>    
          <tr class="tr_change">
            <td align='left'>&nbsp;&nbsp;&nbsp;&nbsp;-Automobile Cost </td>
            <td align='right'><input type="text" value="" name="b30" readonly class='input1'></td>
            <td align='right'>-</td>
            <td align='right'>-</td>
            <td align='right'>-</td>
            <td align='right'>-</td>
            <td align='right'>-</td>
            <td align='right'><input type="text" value="" name="h30" readonly class='input1'></td>
          </tr> 
          <tr class="tr_change">
            <td align='left'>&nbsp;&nbsp;&nbsp;&nbsp;-Automobile Expenses </td>
            <td align='right'>-</td>
            <td align='right'><input type="text" value="" name="c31" readonly class='input1'></td>
            <td align='right'><input type="text" value="" name="d31" readonly class='input1'></td>
            <td align='right'><input type="text" value="" name="e31" readonly class='input1'></td>
            <td align='right'><input type="text" value="" name="f31" readonly class='input1'></td>
            <td align='right'><input type="text" value="" name="g31" readonly class='input1'></td>
            <td align='right'><input type="text" value="" name="h31" readonly class='input1'></td>
          </tr> 
          <tr class="tr_change">
            <td align='left'>&nbsp;&nbsp;&nbsp;&nbsp;-Automobile Maintenance </td>
            <td align='right'>-</td>
            <td align='right'><input type="text" value="" name="c32" readonly class='input1'></td>
            <td align='right'><input type="text" value="" name="d32" readonly class='input1'></td>
            <td align='right'><input type="text" value="" name="e32" readonly class='input1'></td>
            <td align='right'><input type="text" value="" name="f32" readonly class='input1'></td>
            <td align='right'><input type="text" value="" name="g32" readonly class='input1'></td>
            <td align='right'><input type="text" value="" name="h32" readonly class='input1'></td>
          </tr>  
          <tr class="tr_change">
            <td align='left'>&nbsp;&nbsp;&nbsp;&nbsp;-Insurance </td>
            <td align='right'>-</td>
            <td align='right'><input type="text" value="" name="c33" readonly class='input1'></td>
            <td align='right'><input type="text" value="" name="d33" readonly class='input1'></td>
            <td align='right'><input type="text" value="" name="e33" readonly class='input1'></td>
            <td align='right'><input type="text" value="" name="f33" readonly class='input1'></td>
            <td align='right'><input type="text" value="" name="g33" readonly class='input1'></td>
            <td align='right'><input type="text" value="" name="h33" readonly class='input1'></td>
          </tr> 
          <tr class="tr_change">
            <td align='left'>&nbsp;&nbsp;&nbsp;&nbsp;-Salary for driver </td>
            <td align='right'>-</td>
            <td align='right'><input type="text" value="" name="c34" readonly class='input1'></td>
            <td align='right'><input type="text" value="" name="d34" readonly class='input1'></td>
            <td align='right'><input type="text" value="" name="e34" readonly class='input1'></td>
            <td align='right'><input type="text" value="" name="f34" readonly class='input1'></td>
            <td align='right'><input type="text" value="" name="g34" readonly class='input1'></td>
            <td align='right'><input type="text" value="" name="h34" readonly class='input1'></td>
          </tr>                      
          <tr class="tr_change">
            <td align='left'>&nbsp;&nbsp;&nbsp;&nbsp;<b>Total Cash Outlay (Nominal)</b></td>
            <td align='right'><input type="text" value="" name="b35" readonly class='input2'></td>
            <td align='right'><input type="text" value="" name="c35" readonly class='input2'></td>
            <td align='right'><input type="text" value="" name="d35" readonly class='input2'></td>
            <td align='right'><input type="text" value="" name="e35" readonly class='input2'></td>
            <td align='right'><input type="text" value="" name="f35" readonly class='input2'></td>
            <td align='right'><input type="text" value="" name="g35" readonly class='input2'></td>
            <td align='right'><input type="text" value="" name="h35_1" readonly class='input2'><input type="hidden" name='h35'></td>
          </tr>   
          <tr class="tr_change">
            <td align='left'>&nbsp;&nbsp;&nbsp;&nbsp;<b>Total Cash Outlay (NPV)</b></td>
            <td align='right'><input type="text" value="" name="b36" readonly class='input2'></td>
            <td align='right'><input type="text" value="" name="c36" readonly class='input2'></td>
            <td align='right'><input type="text" value="" name="d36" readonly class='input2'></td>
            <td align='right'><input type="text" value="" name="e36" readonly class='input2'></td>
            <td align='right'><input type="text" value="" name="f36" readonly class='input2'></td>
            <td align='right'><input type="text" value="" name="g36" readonly class='input2'></td>
            <td align='right'><input type="text" value="" name="h36_1" readonly class='input2'><input type="hidden" name='h36'></td>
          </tr>                                                       
    </table>
    <table border="0" width="100%" cellspacing="0" cellpadding="0">
    <tr>
 	  <td colspan='2'><b>Comparison of rental vs purchase:</b></td>
    </tr>
    <tr>
 	  <td><b>Net Cash Flow (Nominal)</b></td>
 	  <td><input type="text" value="" name="b39" readonly class='input3'></td>
    </tr>   
    <tr>
 	  <td><b>Net Cash Flow (NPV)</b></td>
 	  <td><input type="text" value="" name="b40" readonly class='input3'>&nbsp;&nbsp;&nbsp;&nbsp;
 	  <span id="resultValue"></span>
 	  </td>
    </tr>       
 </table>
</body>

<script language="javascript">
  initialData();
  function initialData(){
    summaryForAutomobile();
    summaryAutomobilePurchase();
  }
  
function summaryForAutomobile(){
  // var pObj =  parent. ;
   var B14 = parent.window.document.all["<%=ParamConfigHelper.getInstance().getParamValue(CommonName.AUTOMOBILE_LEASING_FEE)%>"].value;
   if(B14=="") B14 = 0
     else B14 = parseFloat(B14)
  
    var F14 = parent.window.document.all["<%=ParamConfigHelper.getInstance().getParamValue(CommonName.AUTOMOBILE_COST)%>"].value;
    if(F14=="") F14 = 0
     else F14 = parseFloat(F14)

    var B15 = parent.window.document.all["<%=ParamConfigHelper.getInstance().getParamValue(CommonName.AUTOMOBILE_EXPENSES_RENTAL)%>"].value
     if(B15 =="") B15 = 0
     else B15 = parseFloat(B15 )

       var F15 = parent.window.document.all["<%=ParamConfigHelper.getInstance().getParamValue(CommonName.AUTOMOBILE_EXPENSES_PURCHASE)%>"].value;
      if(F15 =="") F15 = 0
       else F15 = parseFloat(F15 )

       var B16 = parent.window.document.all["<%=ParamConfigHelper.getInstance().getParamValue(CommonName.AUTOMOBILE_SALARY_RENTAL)%>"].value;
      if(B16 =="") B16 = 0
       else B16 = parseFloat(B16 ) 

       var F16 = parent.window.document.all["<%=ParamConfigHelper.getInstance().getParamValue(CommonName.AUTOMOBILE_SALARY_PURCHASE)%>"].value;
      if(F16 =="") F16 = 0
       else F16 = parseFloat(F16 ) 
 
       var F17 = parent.window.document.all["<%=ParamConfigHelper.getInstance().getParamValue(CommonName.AUTOMOBILE_INSURANCE)%>"].value;
      if(F17 =="") F17 = 0
       else F17 = parseFloat(F17) 

       var C20 = parent.window.document.all["<%=ParamConfigHelper.getInstance().getParamValue(CommonName.AUTOMOBILE_C20)%>"].value
        if(C20 =="") C20 = 0
       else C20 = parseFloat(C20/100) 
       
      var C21 = parent.window.document.all["<%=ParamConfigHelper.getInstance().getParamValue(CommonName.AUTOMOBILE_C21)%>"].value //Salary Increase(%) 
      if(C21=="") C21 = 0
       else C21 = parseFloat(C21/100)
      
      var c23 = B14*12;
       document.all['c23'].value = formatNumber(c23,'#,###')
      var d23 = c23
       document.all['d23'].value =  document.all['c23'].value
      var e23 = B14*12*(1+C20);
       document.all['e23'].value = formatNumber(e23,'#,###')
      var f23 = e23
       document.all['f23'].value =  document.all['e23'].value
      var g23 = B14*12*(1+C20)*(1+C20)
       document.all['g23'].value = formatNumber(g23,'#,###')
      var h23 = parseFloat(c23) + parseFloat(d23)+parseFloat(e23) + parseFloat(f23) + parseFloat(g23);
       document.all['h23'].value = formatNumber(h23,'#,###');

      var c24 = B15*12;
       document.all['c24'].value = formatNumber(c24,'#,###')
      var d24 = c24
       document.all['d24'].value =  document.all['c24'].value
      var e24 = B15*12*(1+C20)
       document.all['e24'].value = formatNumber(e24,'#,###')
      var f24 = e24
       document.all['f24'].value =  document.all['e24'].value
      var g24 = B15*12*(1+C20)*(1+C20);
       document.all['g24'].value = formatNumber(g24,'#,###')
      var h24 = parseFloat(c24) + parseFloat(d24)+parseFloat(e24) + parseFloat(f24) + parseFloat(g24)
       document.all['h24'].value = formatNumber(h24,'#,###');
      
      var c25 = B16*12;
       document.all['c25'].value = formatNumber(c25,'#,###')
      var d25 = parseFloat(c25)*(1+C21)
       document.all['d25'].value = formatNumber(d25,'#,###')
      var e25 = parseFloat(d25)*(1+C21)
       document.all['e25'].value = formatNumber(e25,'#,###')
      var f25 = parseFloat(e25)*(1+C21)
       document.all['f25'].value = formatNumber(f25,'#,###')
      var g25 = parseFloat(f25)*(1+C21)
       document.all['g25'].value = formatNumber(g25,'#,###')
      var h25 = c25+d25+e25+f25+g25
       document.all['h25'].value = formatNumber(h25,'#,###');      
      
      var c26 = c23 + c24 + c25
       document.all['c26'].value = formatNumber(c26,'#,###')
      var d26 = d23 + d24 + d25
       document.all['d26'].value = formatNumber(d26,'#,###')
      var e26 = e23 + e24 + e25
       document.all['e26'].value = formatNumber(e26,'#,###')
      var f26 = f23 + f24 + f25
       document.all['f26'].value = formatNumber(f26,'#,###')
      var g26 = g23 + g24 + g25
       document.all['g26'].value = formatNumber(g26,'#,###')
      var h26 = h23 + h24 + h25
       document.all['h26_1'].value = formatNumber(h26,'#,###')
       document.all['h26'].value = h26
      
      var c27 = c26/(1+C20);
       document.all['c27'].value = formatNumber(c27,'#,###')
      var d27 = d26/((1+C20)*(1+C20));
       document.all['d27'].value = formatNumber(d27,'#,###')
      var e27 = e26/((1+C20)*(1+C20)*(1+C20))
       document.all['e27'].value = formatNumber(e27,'#,###')
      var f27 = f26/((1+C20)*(1+C20)*(1+C20)*(1+C20))
       document.all['f27'].value = formatNumber(f27,'#,###')
      var g27 = g26/((1+C20)*(1+C20)*(1+C20)*(1+C20)*(1+C20))
       document.all['g27'].value = formatNumber(g27,'#,###')
      var h27 = c27+d27+e27+f27+g27;
       document.all['h27_1'].value = formatNumber(h27,'#,###')
       document.all['h27'].value = h27

      var h35 =  document.all['h35'].value
      var h36 =  document.all['h36'].value

      if(h35=="") h35=0
       else h35 = parseFloat(h35)       

      if(h36=="") h36=0
       else h36 = parseFloat(h36)
                    
       document.all['b39'].value = formatNumber(h26 - h35,'#,###')
       document.all['b40'].value = formatNumber(h27 - h36,'#,###')
      
      if((h27-h36)>0)  document.all['resultValue'].innerHTML = "<b>Analysis indicates car purchase generates lesser cash outlay<b>"
       else if ((h27-h36)<=0)  document.all['resultValue'].innerHTML = "<b>Analysis indicates leasehold/rental generates lesser cash outlay<b>"
      else  document.all['resultValue'].innerHTML = ""
 }

function summaryAutomobilePurchase(){
     //var iObj =  document.frames['iframeList'] ;
 
     var F14 = parent.window.document.all["<%=ParamConfigHelper.getInstance().getParamValue(CommonName.AUTOMOBILE_COST)%>"].value;
     if(F14=="") F14 = 0
     else F14 = parseFloat(F14)

       var F15 = parent.window.document.all["<%=ParamConfigHelper.getInstance().getParamValue(CommonName.AUTOMOBILE_EXPENSES_PURCHASE)%>"].value;
      if(F15 =="") F15 = 0
       else F15 = parseFloat(F15 )

       var F16 = parent.window.document.all["<%=ParamConfigHelper.getInstance().getParamValue(CommonName.AUTOMOBILE_INSURANCE)%>"].value;
      if(F16 =="") F16 = 0
       else F16 = parseFloat(F16 ) 
 
       var F17 = parent.window.document.all["<%=ParamConfigHelper.getInstance().getParamValue(CommonName.AUTOMOBILE_SALARY_PURCHASE)%>"].value;
      if(F17 =="") F17 = 0
       else F17 = parseFloat(F17) 

       var C20 = parent.window.document.all["<%=ParamConfigHelper.getInstance().getParamValue(CommonName.AUTOMOBILE_C20)%>"].value //PV @ (%)
        if(C20 =="") C20 = 0
       else C20 = parseFloat(C20/100) 
      
      var C21 = parent.window.document.all["<%=ParamConfigHelper.getInstance().getParamValue(CommonName.AUTOMOBILE_C21)%>"].value //Salary Increase(%) 
      if(C21=="") C21 = 0
       else C21 = parseFloat(C21/100)
       
      var C22 = parent.window.document.all["<%=ParamConfigHelper.getInstance().getParamValue(CommonName.AUTOMOBILE_C22)%>"].value //Maintenance Increase(%) 
      if(C22=="") C22 = 0
       else C22 = parseFloat(C22/100)
       
      var b30 = F14
       document.all['b30'].value = formatNumber(b30,'#,###')
      var h30 = F14
       document.all['h30'].value = formatNumber(h30,'#,###')
      
      var c31 =  F15*12
       document.all['c31'].value = formatNumber(c31,'#,###')
      var d31 = c31
       document.all['d31'].value =  document.all['c31'].value
      var e31 =  F15*12*(1+C20)
       document.all['e31'].value = formatNumber(e31,'#,###')
      var f31 = e31
       document.all['f31'].value =  document.all['e31'].value
      var g31 =  F15*12*(1+C20)*(1+C20)
       document.all['g31'].value = formatNumber(g31,'#,###')
      var h31 = c31 + d31 + e31 + f31 + g31
       document.all['h31'].value = formatNumber(h31,'#,###');

      var c32 =  F14*(0.01+C22*1)
       document.all['c32'].value = formatNumber(c32,'#,###')
      var d32 =  F14*(0.01+C22*2)
       document.all['d32'].value = formatNumber(d32,'#,###')
      var e32 =  F14*(0.01+C22*3)
       document.all['e32'].value = formatNumber(e32,'#,###')
      var f32 = F14*(0.01+C22*4)
       document.all['f32'].value = formatNumber(f32,'#,###')
      var g32 =  F14*(0.01+C22*5)
       document.all['g32'].value = formatNumber(g32,'#,###')
      var h32 = c32 + d32 + e32 + f32 + g32
       document.all['h32'].value = formatNumber(h32,'#,###');
      
      var c33 = F16
       document.all['c33'].value = formatNumber(c33,'#,###')
      var d33 = c33
       document.all['d33'].value =  document.all['c33'].value
      var e33 = d33
       document.all['e33'].value =  document.all['d33'].value
      var f33 = e33
       document.all['f33'].value =  document.all['e33'].value
      var g33 = f33
       document.all['g33'].value =  document.all['f33'].value
      var h33 = c33+d33+e33+f33+g33
       document.all['h33'].value = formatNumber(h33,'#,###');      
      
      var c34 = F17*12
       document.all['c34'].value = formatNumber( c34,'#,###')
      var d34 = c34*(1+C21)
       document.all['d34'].value = formatNumber(d34,'#,###')
      var e34 = d34*(1+C21)
       document.all['e34'].value = formatNumber(e34,'#,###')
      var f34 = e34 * (1+C21)
       document.all['f34'].value = formatNumber(f34,'#,###')
      var g34 = f34*(1+C21)
       document.all['g34'].value = formatNumber(g34,'#,###')
      var h34 = c34+d34+e34+f34+g34
       document.all['h34'].value = formatNumber(h34,'#,###');      
      
      var b35 = F14
       document.all['b35'].value = formatNumber(b35,'#,###')
      var c35 = c31 + c32 + c33 + c34
       document.all['c35'].value = formatNumber(c35,'#,###');
      var d35 = d31 + d32 + d33 + d34
       document.all['d35'].value = formatNumber(d35,'#,###');
      var e35 = e31 + e32 + e33 + e34 
       document.all['e35'].value = formatNumber(e35,'#,###');      
      var f35 = f31 + f32 + f33 + f34 
       document.all['f35'].value = formatNumber(f35,'#,###');      
      var g35 = g31 + g32 + g33 + g34
       document.all['g35'].value = formatNumber(g35,'#,###');      
      var h35 = h30 + h31 + h32 + h33 + h34
       document.all['h35_1'].value = formatNumber(h35,'#,###');      
       document.all['h35'].value = h35
      
      var b36 = b35
       document.all['b36'].value = formatNumber(b36,'#,###')
      var c36 = c35/(1+C20)
       document.all['c36'].value = formatNumber(c36,'#,###');
      var d36 = d35/((1+C20)*(1+C20))
       document.all['d36'].value = formatNumber(d36,'#,###');
      var e36 = e35/((1+C20)*(1+C20)*(1+C20))
       document.all['e36'].value = formatNumber(e36,'#,###');
      var f36 = f35/((1+C20)*(1+C20)*(1+C20)*(1+C20))
       document.all['f36'].value = formatNumber(f36,'#,###');
      var g36 = g35/((1+C20)*(1+C20)*(1+C20)*(1+C20)*(1+C20))
       document.all['g36'].value = formatNumber(g36,'#,###');
      var h36 = b36 + c36 + d36 + e36 + f36 + g36
       document.all['h36_1'].value = formatNumber(h36,'#,###');            
       document.all['h36'].value = h36
      
      //
      var h26 =  document.all['h26'].value
      var h35 =  document.all['h35'].value
      var h27 =  document.all['h27'].value
      var h36 =  document.all['h36'].value
      
      if(h26=="") h26=0
       else h26 = parseFloat(h26)
      if(h35=="") h35=0
       else h35 = parseFloat(h35)       
      if(h27=="") h27=0
       else h27 = parseFloat(h27)
      if(h36=="") h36=0
       else h36 = parseFloat(h36)
                    
       document.all['b39'].value = formatNumber( h26 - h35,'#,###')
       document.all['b40'].value = formatNumber( h27 - h36,'#,###')
      
      if((h27 - h36)>0)  document.all['resultValue'].innerHTML = "<b>Analysis indicates car purchase generates lesser cash outlay<b>"
       else if((h27-h36)<=0)  document.all['resultValue'].innerHTML = "<b>Analysis indicates leasehold/rental generates lesser cash outlay<b>"
      else  document.all['resultValue'].innerHTML = ""
 }

</script>

