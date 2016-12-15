function document.onreadystatechange(){
	//overwrite begin
  try{
  if ($("flow_org_id").value=="") 
   {
              $("flow_org_id").value=$("company_id").value;
   }

    populateField02_1();
  } catch(e){}
initFinPayBy();
  onload_func();

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

function populateField02_1(){

  var select = $("field_02_1");
  if(select!=null&&select.type=='select-one'){
  var sql = "select name option_label, code option_value from teflow_building where org_id='" +  $("company_id").value+ "'  order by name";

     var xmlObj = selectFromSQL(sql, 2);
     if (xmlObj) {
        items = xmlObj.getElementsByTagName("item");
     }
 
     if (items == null) {
         return;
      }

     clearSelect(select);
     select.appendChild(createEmptyElement(""));

     for ( var i = 0; i < items.length; i++) {
         select.appendChild(createOptionElement(items[i], "OPTION_VALUE", "OPTION_LABEL"));
     }

     var isql = "select field_02_1 from teflow_92_02 where request_no = '"+$('request_no').value+"'";
     xmlObj = selectDataListFromSQL(isql);
  if(xmlObj){
    options = xmlObj.getElementsByTagName("item");
    if(options && options.length){
      var curvalue = options[0].getElementsByTagName("FIELD_02_1")[0].childNodes[0].nodeValue;
       $("field_02_1").value = curvalue;
    }
  }
 }
}

function changeAmount(addend,summary){
	var tbl=document.getElementsByName("field_"+addend);
	var sum=0;
	for(i=0;i<tbl.length;i++){
		sum = sum + parseFloat(tbl[i].value.replace(/,/g,""));
	}
	$("field_"+summary).value = formatNumber(sum,"#,##0.00");
	recountFinal();
}

function recountFinal(){
//$("field_04_7").value=$("field_04_2").value-$("field_04_5").value;
}

function searchCntr(bldname,bldcode){
  var table = "";
  var field = "";
  var sql = "";
  var textlines = "";
  var xmlObj = null;
  var balance = 0;
  
  sql = "select tot_amount*100 from teflow_building where code='"+bldcode+"'";
  xmlObj = selectFromSQL(sql, 1);

  if(xmlObj){
    var options = xmlObj.getElementsByTagName("result");
    if(options && options.length){
      balance = parseFloat(options[0].text)/100;
      $("field_09_3").value = formatNumber(balance,'#,##0.00');//contract amount
    }
  }
  
  sql = "select str(c.form_system_id) %2B '_' %2B c.section_id V1,c.field_id V2"+
  " from teflow_form_special_field c "+
  " where c.field_type=2 and  c.form_system_id="+$("formSystemId").value;
  
  xmlObj = selectFromSQL(sql, 2);
  
  if(xmlObj){
   var options = xmlObj.getElementsByTagName("item");
    if(options && options.length){
     	table = "teflow_"+options[0].getElementsByTagName("V1")[0].childNodes[0].nodeValue.trim();
     	field = options[0].getElementsByTagName("V2")[0].childNodes[0].nodeValue;
     }
  }
  
  sql = "select b.request_no V1,b."+field+" V2, a.field_02_3 PERIOD_F,a.field_02_4 PERIOD_T "+
  " from teflow_"+$("formSystemId").value+"_02 a,"+table+" b, teflow_wkf_process w  "+
  " where a.request_no = b.request_no and a.request_no=w.request_no "+
  " and (a.field_02_1=substring('"+bldname+"',1,len(a.field_02_1)) or a.field_02_1='"+bldcode+"') and w.status in ('01','02','04') order by b.request_no ";
  xmlObj = selectFromSQL(sql, 2);
 
  if(xmlObj){
   var options = xmlObj.getElementsByTagName("item");
    if(options && options.length){
    for(var i=0;i < options.length;i++){
       textlines = textlines+options[i].getElementsByTagName("V1")[0].childNodes[0].nodeValue;
       textlines = textlines+"  (  ";
       textlines = textlines+options[i].getElementsByTagName("PERIOD_F")[0].childNodes[0].nodeValue.substring(0,10);
       textlines = textlines+" ~ ";
       textlines = textlines+options[i].getElementsByTagName("PERIOD_T")[0].childNodes[0].nodeValue.substring(0,10);;
       textlines = textlines+" ) :  ";
       textlines = textlines+options[i].getElementsByTagName("V2")[0].childNodes[0].nodeValue;       
       textlines = textlines+"\n";
       balance = balance - options[i].getElementsByTagName("V2")[0].childNodes[0].nodeValue;
    }
    }
  }
  
  $("field_09_4").value = textlines;
  $("field_09_5").value = formatNumber(balance,'#,##0.00');//balance;
  
}  


function selBuilding(obj) {
  var sql = "select * from teflow_building where code='" + obj.value + "'";

  var xmlObj = selectFromSQL(sql, 2);
  if (xmlObj) {
    var options = xmlObj.getElementsByTagName("item");
    if (options && options.length) {
      $("field_02_18").value = obj[obj.selectedIndex].text;
      $("field_02_2").value = replacenull(options,"PERIOD",' ');
      $("field_02_11").value = replacenull(options,"AREA",' ');
      $("field_02_8").value = replacenull(options,"RENTER",' ');
      $("field_02_10").value = replacenull(options,"ACC_BANK",' ');
      $("field_02_9").value = replacenull(options,"ACC_NO",' ');      
      $("field_02_5").value = replacenull(options,"CONTRACT_NO",' ');
      $("field_02_7").value = replacenull(options,"TOT_AMOUNT",' ');
      $("field_02_12").value = replacenull(options,"FREE_PERIOD",' ');
      $("field_02_13").value = replacenull(options,"PROVINCE",' ')+"_"+replacenull(options,"CITY",' ');
      $("field_02_14").value = replacenull(options,"MONTH_RENT_FEE",' ');
      $("field_02_15").value = replacenull(options,"MONTH_MANG_FEE",' ');
      $("field_02_16").value = replacenull(options,"DEPO_MONTH",' ');
      $("field_02_17").value = replacenull(options,"DEPO_FEE_RENT",' ') + " / " + replacenull(options,"DEPO_FEE_PROP",' ');
      searchCntr(obj[obj.selectedIndex].text,obj.value);
    }
  }
}
function rowTotal(ob){
	var i=ob.rowIndex;
	var v7=0;
	var exrate = 1.000;
	var total;
	
	v7=0+document.getElementsByName("field_03_2")[i].value;
	exrate=document.getElementsByName("field_03_9")[i].value;

	total=parseFloat(v7)*exrate;
	document.getElementsByName("field_03_4")[i].value=formatNumber(total,'#,##0.00');
	changeAmount("03_4","05_1");
	changeAmount("03_4","05_17");
}

function afterDel(tbl,chk){
  if(tbl=="formTable06"){
    changeDRCR();
    recalT6();
    reBindTCEvent();
  }
  if(tbl=="formTable03"){
     changeAmount("03_4","05_1");
     changeAmount("03_4","05_17");
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

function changeflow_company(){
   if ($("flow_org_id").value!==$("company_id").value){
              alert(" 注意：此申请单的适用流程将改变，将改走其他公司流程。请确认真的要改变吗？");
   }
}