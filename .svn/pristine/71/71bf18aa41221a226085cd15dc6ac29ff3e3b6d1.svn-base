function document.onreadystatechange(){
	//overwrite begin
try{
initFinPayBy();
onload_func();
onchangeRequester($("request_staff_code").value);  

  if(document.getElementsByName("subBtn")[0]) {
        $("subBtn").onmouseover=function(){changeAmount("02_8","04_22");}
  }
  if(document.getElementsByName("addBtn")[0]) {
        $("addBtn").onmouseover=function(){changeAmount("02_8","04_22");}
  }
}catch(e){}  

setCAValue(); 

	//overwrite end
	try
	{
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
  if($("field_05_2")&&$("field_05_2").value==""){
     $("field_05_2").value = $("field_01_2").value;
  }
}

function checkPayBy(){
   var payby1 = $("field_01_2").value;
   var payby2 = $("field_05_2").value;

  if(payby1 != payby2){
      alert("选择的付款/收款方式与申请人填写的不一样");
  }
}

function setCAValue(){
                       try{
   var sql="SELECT field_04_26  V1 from teflow_87_04 where request_no='"+$("request_no").value+"' ";
   var savedCA="";   
    var xmlObj = selectFromSQL(sql,2);
      savedCA=xmlObj.getElementsByTagName("item")[0].getElementsByTagName("V1")[0].childNodes[0].nodeValue;
            }catch(e){
              //alert(e);
              return;
            };
 if(savedCA!="") {
    alert("请注意：本申请已关联借款单 : " +savedCA);  
  $("field_04_26").value = savedCA;
    changeCAForm($("field_04_26"));
 }
}

function getLoanHis(){
 sql = "select a.request_no %2B '     |' %2B c.field_03_1 %2B '|'  %2B str(d.field_05_1,10,2) V1,a.request_no V2 from teflow_94_05 d, teflow_wkf_process a, teflow_94_03 c where  a.request_no = d.request_no and a.request_no = c.request_no  and a.request_staff_code = '"+$("request_staff_code").value+"'"+
  " and d.field_05_1 is not null and a.status in ('01','02','04') and a.request_no not in (select request_no from teflow_ebank_exp_history where type=2)  order by a.submission_date";
    
  xmlObj = selectFromSQL(sql,2);

  if(xmlObj){
    var select = $("field_04_26");
    clearSelect(select);
    select.appendChild(createEmptyElement(""));
    var items = xmlObj.getElementsByTagName("item");
    if (items == null)  return;

    for (var i = 0; i < items.length; i++) {
      select.appendChild(createOptionElement(items[i], "V2", "V1"));
    }
   select.value=savedCA;
  }
}

function changeAmount(addend,summary){
  var tbl=document.getElementsByName("field_"+addend);
  var sum=0;
  for(i=0;i<tbl.length;i++){
    sum = sum + parseFloat(tbl[i].value.replace(/,/g,''));
  }
  $("field_"+summary).value = formatNumber(sum, '#,##0.00');
  recountFinal();
}

function changeCAForm(obj){
if(obj.selectedIndex==0){
$("field_04_25").value="0";
$("field_04_12").value="";
$("field_04_29").value="";
}else {
var strLabel = obj[obj.selectedIndex].text.split("|");
$("field_04_25").value=formatNumber(strLabel[2].trim(),'#,##0.00');
$("field_04_12").value=strLabel[1].trim();
$("field_04_29").value=strLabel[0].trim();
}
recountFinal();
}

function recountFinal(){
var i=$("field_04_22").value.replace(/,/g,'')-$("field_04_25").value.replace(/,/g,'');

$("field_04_27").value=formatNumber(i,'#,##0.00');

$("field_05_1").value=formatNumber(i,'#,##0.00');
$("field_05_17").value=formatNumber(i,'#,##0.00');
}


function onchangeRequester(staffCode) {
   sql = "select right(account_no,4) option_value1, chinese_name option_value2 from teflow_user where rtrim(staff_code)='"+staffCode+"'";
  var xmlObj = selectFromSQL(sql,2);

  $("field_01_3").value="";
  $("field_01_5").value="";
  if(xmlObj){
    var options = xmlObj.getElementsByTagName("item");
    if(options && options.length){
      $("field_01_3").value= "***********"+options[0].getElementsByTagName("OPTION_VALUE1")[0].childNodes[0].nodeValue;
      $("field_01_5").value= options[0].getElementsByTagName("OPTION_VALUE2")[0].childNodes[0].nodeValue;
      document.getElementsByName("field_01_5")[0].style.color =cn_name_color;
    }
  }

  getLoanHis();
  changeCAForm($("field_04_26"));
}

function rowTotal(ob){
	var i=ob.rowIndex;
	var v7=0;
	var exrate = 1.000;
	var total;
	
	v7=0+document.getElementsByName("field_02_3")[i].value;
	exrate=document.getElementsByName("field_02_11")[i].value;

	total=parseFloat(v7)*exrate;
	document.getElementsByName("field_02_8")[i].value=formatNumber(total,'#,##0.00');
	changeAmount("02_8","04_22");
}

function searchEform(ob){
var eFormid = $("div_"+ob.id).innerText;
eFormid = "'"+eFormid.replace(/, /g,"','")+"'";
  var sql=	"select str(c.form_system_id) %2B '_' %2B c.section_id V1,c.field_id V2"+
  " from teflow_wkf_process as a,teflow_wkf_define as b,teflow_form_special_field as c "+
  " where  a.request_no in  ("+eFormid+") and c.field_type = 1"+
  " and a.flow_id=b.flow_id and b.form_system_id = c.form_system_id";
  var table = "";
  var field = "";
  var textlines = "";
  var xmlObj = selectFromSQL(sql, 2);
  var balance = 0;

  if(xmlObj){
   var options = xmlObj.getElementsByTagName("item");
   var sum= 0;
  for(var i=0;i<options.length;i++){
     	table = "teflow_"+options[i].getElementsByTagName("V1")[0].childNodes[0].nodeValue.trim();
     	field = options[i].getElementsByTagName("V2")[0].childNodes[0].nodeValue;
       	sql = "select "+field+" from "+table+" where request_no in ("+eFormid+")";
  xmlObj = selectFromSQL(sql, 1);

    if(xmlObj){
          //balance = parseFloat(options[0].text);
      sum= sum+parseFloat(xmlObj.getElementsByTagName("result")[0].text);
    }
   }
    document.getElementsByName("field_02_4")[ob.rowIndex].value = formatNumber(sum,'#,##0.00');//Amount
  }

  /*sql = "select b.request_no V1,b.field_02_8 V2"+
  " from teflow_"+$("formSystemId").value+"_02 b,teflow_wkf_process c "+
  " where b.request_no=c.request_no AND c.status <> '00'"+
  " and b.field_02_2 like '"+eFormid+"%25' order by b.request_no";

  xmlObj = selectFromSQL(sql, 2);

  if(xmlObj){
   var options = xmlObj.getElementsByTagName("item");
    if(options && options.length){
    for(var i=0;i < options.length;i++){
      balance = balance - options[i].getElementsByTagName("V2")[0].childNodes[0].nodeValue;
    }
    }
  } 
  document.getElementsByName("field_02_5")[ob.rowIndex].value= formatNumber(balance,'#,##0.00');*///balance;
}

function changePaymode(obj){
checkPayBy();
   if(obj.value=='11' && parseFloat($("field_05_1").value)>=0){
        alert("仅还款时可选");
        obj.value='';
   }
}

function afterDel(tbl,chk){
  if(tbl=="formTable06"){
    changeDRCR();
    recalT6();
    reBindTCEvent();
  }
  if(tbl=="formTable02"){
    changeAmount("02_8","04_22");
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