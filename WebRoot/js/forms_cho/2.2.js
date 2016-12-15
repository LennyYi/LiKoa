function document.onreadystatechange(){
	//overwrite begin
try{ 
  if ($("flow_org_id").value=="") 
   {
              $("flow_org_id").value=$("company_id").value;
   }
     
  $("field_03_10").onkeyup=function(){ if(event.keyCode==32)lookup();}
  document.getElementsByName("addRowBtn")[1].onclick=function(){if(document.getElementsByName("field_11_1").length<1)createTableSectionRow("formTable11","11","0");}
  sql = "select org_name V1,org_id V2 from teflow_company ";
  xmlObj = selectFromSQL(sql,2);

  if(xmlObj){
    var select = $("field_03_19");
    clearSelect(select);
    select.appendChild(createEmptyElement(""));
    var items = xmlObj.getElementsByTagName("item");
    if (items == null)  return;

    for (var i = 0; i < items.length; i++) {
      select.appendChild(createOptionElement(items[i], "V2", "V1"));
    }
  }
}catch(e){}
try{
  sql = "select rtrim(bank_name %2B '_' %2B account_code) V1,bank_code V2 from teflow_bank where org_id='"+$("company_id").value+"' order by is_default desc";
    
  xmlObj = selectFromSQL(sql,2);

  if(xmlObj){
    var select = $("field_05_22");
    clearSelect(select);
    var items = xmlObj.getElementsByTagName("item");
    if (items == null || select ==undefined)  return;

    for (var i = 0; i < items.length; i++) {
      select.appendChild(createOptionElement(items[i], "V2", "V1"));
    }
  }
}catch(e){}
try{
    sql = "select a.request_no %2B '     |' %2B c.field_03_1 %2B '|' %2B str(c.field_03_3,10,2) V1,a.request_no V2"+
  " from teflow_wkf_process as a,teflow_94_03 as c "+
  " where (a.request_staff_code='"+$("request_staff_code").value+
  "' or a.submit_staff_code='"+$("request_staff_code").value+"' )"+
  " and a.request_no = c.request_no"+
  " and a.status in ('01','02','04') "+
  " and a.request_no not in (select request_no from teflow_ebank_exp_history where type=2) "+
  " order by a.submission_date ";
    
  xmlObj = selectFromSQL(sql,2);

  if(xmlObj){
    var select = $("field_12_26");
    clearSelect(select);
    select.appendChild(createEmptyElement(""));
    var items = xmlObj.getElementsByTagName("item");
    if (items == null)  return;

    for (var i = 0; i < items.length; i++) {
      select.appendChild(createOptionElement(items[i], "V2", "V1"));
    }
  }
}catch(e){}  
initFinPayBy();
   onload_func();

  if(document.getElementsByName("subBtn")[0]) {
            $("subBtn").onmouseover=function(){changeAmount("04_5","12_28"); changeAmount("04_5","05_1");}
  }
  if(document.getElementsByName("addBtn")[0]) {
            $("addBtn").onmouseover=function(){changeAmount("04_5","12_28"); changeAmount("04_5","05_1");}
  } 
  try{ if(document.getElementsByName("addBtn")[1]&&$("field_03_3").value=="02")changeUncert("02");}catch(e){}
  
   //overwrite end
    try {
	if (document.readyState == "complete") 
	{
     		delNode("loading");  

    	}
    }
    catch(e)
    {
    	alert("Fail to load page!");
    }
}
function initFinPayBy(){
  if($("field_05_2")&&$("field_01_3")&&$("field_05_2").value==""){
     $("field_05_2").value = $("field_01_3").value;
  }
}

function checkPayBy(){
   var payby1 = $("field_01_3").value;
   var payby2 = $("field_05_2").value;

  if($("field_01_3")&&payby1 != payby2){
      alert("选择的付款/收款方式与申请人填写的不一样");
  }
}
function changeAmount(addend,summary){
	var tbl=document.getElementsByName("field_"+addend);
	var sum=0;
	for(i=0;i<tbl.length;i++){
		sum = sum + parseFloat(tbl[i].value.replace(/,/g,""));
	}
	$("field_"+summary).value = formatNumber(sum,"#,##0.00");
	$("field_05_17").value=formatNumber(sum,"#,##0.00");
	recountFinal();
}

function changeCAForm(obj){
if(obj.selectedIndex==0){
$("field_12_25").value="0";
$("field_12_12").value="";
} else {
var strLabel = obj[obj.selectedIndex].text.split("|");
$("field_12_25").value=formatNumber(strLabel[2].trim(),'#,##0.00');
$("field_12_12").value=strLabel[1].trim();
}
recountFinal();
}

function recountFinal(){
var i=$("field_12_28").value.replace(/,/g,'')-$("field_12_34").value.replace(/,/g,'');

$("field_12_27").value=formatNumber(i,'#,##0.00');

$("field_05_1").value=formatNumber(i,'#,##0.00');
$("field_05_17").value=formatNumber(i,'#,##0.00');
}

function changeBranch(ob,nm){
 
    var tmp="";
    if(nm!=null&&nm.trim()!=""){tmp=" and name_c like '%25"+nm+"%25' ";}
    sql = "select name_c V1,org_id %2B '_' %2B code V2 from teflow_supplier where status='A'  and org_id='"+ob.value+"'"+tmp+"  order by name_c";
    
  xmlObj = selectFromSQL(sql,2);

  if(xmlObj){
    var select = $("field_03_10");
    clearSelect(select);
    select.appendChild(createEmptyElement(""));
    var items = xmlObj.getElementsByTagName("item");
    if (items == null)  return;

    for (var i = 0; i < items.length; i++) {
      select.appendChild(createOptionElement(items[i], "V2", "V1"));
    }
  }
}

function rowTotal(ob){
	var i=ob.rowIndex;
	var v7=0;
	var exrate = 1.000;
	var total;
	
	v7=0+document.getElementsByName("field_04_2")[i].value;
	exrate=document.getElementsByName("field_04_6")[i].value;

	total=parseFloat(v7)*exrate;
	document.getElementsByName("field_04_5")[i].value=formatNumber(total,'#,##0.00');
	changeAmount("04_5","12_28");
	changeAmount("04_5","05_1");
}


function changeSupplier(ob){
   if (ob.selectedIndex==0) {
      $("field_03_25").value="";  
      $("field_03_12").value="";  
      $("field_03_13").value="";  
      $("field_03_14").value="";    
      $("field_03_15").value="";     
      $("field_03_16").value="";         
      $("field_03_17").value="";         
      $("field_03_18").value=""; 
      $("field_03_20").value="";         
      $("field_03_21").value="";     
      $("field_03_23").value="";         
      $("field_03_24").value="";         
      return;
  }
  sql = "select * from teflow_supplier where org_id %2B '_' %2B code ='"+ob.value+"'";
    
  xmlObj = selectFromSQL(sql,2);

  if(xmlObj){
      var items = xmlObj.getElementsByTagName("item");
      if (items == null) return;
      $("field_03_25").value=replacenull(items,"NAME_C","");  
      $("field_03_12").value=replacenull(items,"TYPE","");  
      $("field_03_13").value=replacenull(items,"PRODUCT","");  
      $("field_03_14").value=replacenull(items,"CONTACTER","");    
      $("field_03_15").value=replacenull(items,"PROVINCE","");     
      $("field_03_16").value=replacenull(items,"CITY","");         
      $("field_03_17").value=replacenull(items,"BANK","");         
      $("field_03_18").value=replacenull(items,"BANK_ACCOUNT",""); 
      $("field_03_20").value=replacenull(items,"TEAM_NAME","");         
      $("field_03_21").value=replacenull(items,"TEAM_CONTACTER","");     
      $("field_03_23").value=replacenull(items,"TEL","");         
      $("field_03_24").value=replacenull(items,"FAX","");  
   }
}

function searchEform(eFormid){
    
  $("field_02_2").value = "";
  $("field_02_4").value = "";
  $("field_02_5").value = "";

  var sql=	"select str(c.form_system_id) %2B '_' %2B c.section_id V1,c.field_id V2"+
  " from teflow_wkf_process as a,teflow_wkf_define as b,teflow_form_special_field as c "+
  " where  a.request_no = '"+eFormid+"' and c.field_type=1"+
  " and a.flow_id=b.flow_id and b.form_system_id = c.form_system_id order by submission_date desc";

  var table = "";
  var field = "";
  var textlines = "";
  var xmlObj = selectFromSQL(sql, 2);
  var balance = 0;

  if(xmlObj){
   var options = xmlObj.getElementsByTagName("item");

    if(options && options.length){
      table = "teflow_"+options[0].getElementsByTagName("V1")[0].childNodes[0].nodeValue.trim();
      field = options[0].getElementsByTagName("V2")[0].childNodes[0].nodeValue;
    }
    else return;
  }

  sql = "select "+field+" from "+table+" where request_no='"+eFormid+"'";
  xmlObj = selectFromSQL(sql, 1);

  if(xmlObj){
    var options = xmlObj.getElementsByTagName("result");
    if(options && options.length){
      balance = parseFloat(options[0].text);
      $("field_02_2").value = formatNumber(options[0].text,"#,##0.00");//eForm Amount
    }
  }
//---------history query------------

  sql = "SELECT distinct a.form_system_id V1,a.section_id V2,a.field_id V3,a1.section_id V4,a1.field_id V5  FROM teflow_form_section_field a,teflow_form_section_field a1,teflow_form b" +
" Where a.form_system_id=b.form_system_id and a1.form_system_id=b.form_system_id and b.status=0 and a.event_onchange like 'searchEform%25' and a1.field_label like '申请报销金额%25'";
  xmlObj = selectFromSQL(sql, 2);

  if(xmlObj){
   var options = xmlObj.getElementsByTagName("item");
    if(options && options.length){
    for(var i=0;i < options.length;i++){
      table = options[i].getElementsByTagName("V1")[0].childNodes[0].nodeValue.trim();
      section = options[i].getElementsByTagName("V2")[0].childNodes[0].nodeValue.trim();
      field = options[i].getElementsByTagName("V3")[0].childNodes[0].nodeValue;
      section1 = options[i].getElementsByTagName("V4")[0].childNodes[0].nodeValue.trim();
      field1 = options[i].getElementsByTagName("V5")[0].childNodes[0].nodeValue;
      
      if(table=="91"){
        section1 = "05";
        field1 = "field_05_1";
      }     
      sql2 = "select distinct  b.request_no V1,b."+field1+" V2"+
      " from teflow_"+table+"_"+section+" a,teflow_"+table+"_"+section1+" b,teflow_wkf_process w "+
      " where a.request_no = b.request_no and a.request_no=w.request_no and w.status in ('01','02','04') "+
      " and a."+field+" like '"+eFormid+"%25' and b."+field1+" is not null order by b.request_no";
    
      xmlObj2 = selectFromSQL(sql2, 2);
    
      if(xmlObj2){
       var options2 = xmlObj2.getElementsByTagName("item");
        if(options2 && options2.length){
        for(var j=0;j < options2.length;j++){
           textlines = textlines+options2[j].getElementsByTagName("V1")[0].childNodes[0].nodeValue;
           textlines = textlines+"  :  ";
           textlines = textlines+options2[j].getElementsByTagName("V2")[0].childNodes[0].nodeValue;
           textlines = textlines+"\n";
           balance = balance - options2[j].getElementsByTagName("V2")[0].childNodes[0].nodeValue;
        }
        }
      }
    }
    }
  }
  //-------------

  
  $("field_02_4").value = textlines;
  $("field_02_5").value = formatNumber(balance,'#,##0.00');//balance;
  
recountFinal();
}

function changeUncert(val){
  var bool ;
  if(val=="02"){
    if(document.getElementsByName("field_11_1").length<1){
      createTableSectionRow("formTable11","11","0");
    }
    bool = true;
  } else {
    bool = false;
  }

    for(var i=2;i<26;i++){
      if(i==3||i==22)continue;
      if($("field_03_"+i))
        $("field_03_"+i).disabled = bool;
    }

}

function lookup(){
  var keywd = prompt("Input keyword, blank to show all","");
  changeBranch($("field_03_19"),keywd);
}

function afterDel(tbl,chk){
  if(tbl=="formTable06"){
    changeDRCR();
    recalT6();
    reBindTCEvent();
  }
  if(tbl=="formTable04"){
    changeAmount("04_5","12_28");
    changeAmount("04_5","05_1");
  }
}

//This function used for validation values just before approving process.
function validationUpdate(nodeId){
	if($("field_08_3").required == undefined){
                      	return true;
	}

	var dcEqual = $("field_08_3").value;
	if(dcEqual === 'Yes'){
		return true;
	}else{
		alert("相同DB Code下,借贷不相等.");
		return false;
	}
}

//This function used for validation values just before submitting form.
function validationSubmit(){
  notifyEpaymentApplicant();
}
function searchVenderPayHis(formids) {
  if(formids == "" || formids == null){
  $('field_12_34').value ="0.00";
  $('field_12_31').value = "";
  recountFinal();
  return;
}
  
  var hispaytotal = 0;
  var payhis = "";
  var xmlObj;
  
  var ids = formids.split(',');
  var completeds = 0;

  for(var i=0; i< ids.length; i++){
    //获取报销内容以及报销金额
    var sql = "select a.field_04_1 V1, a.field_04_5 V2 from teflow_91_04 a, teflow_wkf_process b where a.request_no =b.request_no and b.status = '04' and a.request_no='"+ ids[i].trim() + "';";
    xmlObj = selectFromSQL(sql, 2);
    if(xmlObj){
        var options = xmlObj.getElementsByTagName("item");
        if(options){
             for(var j=0; j<options.length; j++){
                     var field1 = options[j].getElementsByTagName("V1")[0].childNodes[0].nodeValue;             
                     var field2 = options[j].getElementsByTagName("V2")[0].childNodes[0].nodeValue;
                     //alert(hispaytotal );
                     //alert(field2);
                     if(field2 || field1){
                             if (j== 0){ 
                                hispaytotal =  parseFloat(hispaytotal) + parseFloat(field2);
                                payhis =  payhis + ids[i]+":"+field1+":"+field2;
                             }else{
                                hispaytotal = parseFloat(hispaytotal) + parseFloat(field2);
                                payhis =  payhis + ",  "+field1+":"+field2;
                             }
                     }
            }
        }
    payhis =  payhis + "\n";
    }
  }  
  $('field_12_34').value = formatNumber(hispaytotal,'#,##0.00');
  $('field_12_31').value = payhis;
  recountFinal();
  alert("最终报销金额仅供参考！");
}

  function openRefFormWindow2(preUrl, targetUrl, fieldId, divId, tagId) {
      returnValue = showModalDialog(targetUrl, window, "dialogWidth:500px;dialogHeight:430px;help:0;status:0;resizeable:1;");
      while (true) {
          // alert("returnValue: " + returnValue);
          if (returnValue == undefined) {
              // Cancel
              return;
          }
          if (returnValue.indexOf("?method") == -1) {
              // Confirm
              break;
          }
          // Search
          returnValue = showModalDialog(returnValue, window, "dialogWidth:500px;dialogHeight:430px;help:0;status:0;resizeable:1;");
      }
      
      var linkStr = "";
      if (returnValue.length > 1) {
          var arr;
          arr = returnValue.split(",");
          var forduplicate = "";
          for (var i = 0; i < arr.length; i++) {
              if (forduplicate.indexOf(arr[i])  > 0)   {
                   continue;
              }
              forduplicate = forduplicate + " " + arr[i];

              var dot = arr[i].lastIndexOf(".");
              var requestNo = arr[i].substring(0, dot);
              var formSysId = arr[i].substring(dot + 1);
              linkStr += "<a href=\"javascript:openCenterWindow('" + preUrl
                  + "/formManageAction.it?method=displayFormContent&operateType=view&viewFlag=false&pop=true&requestNo="
                  + requestNo + "&formSystemId=" + formSysId + "',800,600)\">" + requestNo + "</a>, ";
          }
          if (arr.length > 0) {
              linkStr = linkStr.substring(0, linkStr.length - 2);
          }
      }
      // alert(linkStr);
      var obj = document.getElementById(divId);
      obj.innerHTML = linkStr;
      
      document.getElementById(tagId).value = returnValue;
      document.getElementById(tagId).fireEvent("onchange");
  }


function changeflow_company(){
   if ($("flow_org_id").value!==$("company_id").value){
              alert(" 注意：此申请单的适用流程将改变，将改走其他公司流程。请确认真的要改变吗？");
   }
}

function bindEventPrivate(ob){
ob.onkeypress=function(){if (event.keyCode!=46 && event.keyCode!=45 && (event.keyCode<48 || event.keyCode>57)) event.returnValue=false;}
}
function checkIntegrityType(){return new Array('03');//approve
}

function checkIntegrityField(){return new Array($('field_05_2').type==hidden?true:$('field_05_2').disabled);
}

function checkIntegritySQL(rq){return new Array("select * from teflow_"+$("formSystemId").value+"_05 where request_no='"+rq+"' and (field_05_2 is null or field_05_2='02' and field_05_28 is null)");
}