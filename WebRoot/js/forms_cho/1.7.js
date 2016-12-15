var savedCA="";   
function document.onreadystatechange(){
  //overwrite begin
try{
  onload_func();
  onchangeRequester($("request_staff_code").value);
  initFinPayBy();

//  if(document.getElementsByName("subBtn")[0]) {
//            $("subBtn").onmouseover=function(){changeAmount("02_9","04_16");}
//  }
//  if(document.getElementsByName("addBtn")[0]) {
//            $("addBtn").onmouseover=function(){changeAmount("02_9","04_16");}
//  }

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

function setCAValue(){
                       try{
   var sql="SELECT field_04_26  V1 from teflow_114_04 where request_no='"+$("request_no").value+"' ";

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
    if(tbl[i].value != ""){
      sum = sum + parseFloat(tbl[i].value.replace(/,/g,''));
    }
  }
  $("field_"+summary).value = formatNumber(sum, '#,##0.00');
  recountFinal();
}

function initFinPayBy(){
  if(  $("field_05_2")&&$("field_05_2").value==""){
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


function recountFinal(){
var i=$("field_04_22").value.replace(/,/g,'')-$("field_04_25").value.replace(/,/g,'');

$("field_04_27").value=formatNumber(i,'#,##0.00');

$("field_05_1").value=formatNumber(i,'#,##0.00');
$("field_05_17").value=formatNumber(i,'#,##0.00');
}

function checkRateCal(){
  var tbl7=document.getElementsByName("field_02_3");
  var tbl9=document.getElementsByName("field_02_8");
  var tbl10=document.getElementsByName("field_02_11");
  var sum7 = 0;
  var sum9 = 0;
  for(i=0;i<tbl7.length;i++){
    if(tbl7[i].value == ""){
      sum7 = sum7 + 0 ;
      sum9 = sum9 + parseFloat(tbl9[i].value.replace(/,/g,''));
    }else{
      sum7 = sum7 + parseFloat(tbl7[i].value.replace(/,/g,'')) * parseFloat(tbl10[i].value.replace(/,/g,'')) ;
      sum9 = sum9 + parseFloat(tbl9[i].value.replace(/,/g,''));
    }
  }
  
  if( parseInt(sum7) !=  parseInt(sum9)){
    alert("申请报销金额可能计算不正确，请检查验证!");
    return false;
  }  

  return true;
}

function onchangeRequester(staffCode) {
   sql = "select right(account_no,4) option_value1, chinese_name option_value2 from teflow_user where rtrim(staff_code)='"+staffCode+"'";
  var xmlObj = selectFromSQL(sql,2);

  $("field_01_3").value="";
  $("field_01_4").value="";
  if(xmlObj){
    var options = xmlObj.getElementsByTagName("item");
    if(options && options.length){
      $("field_01_3").value= "***********"+options[0].getElementsByTagName("OPTION_VALUE1")[0].childNodes[0].nodeValue;
      $("field_01_4").value= options[0].getElementsByTagName("OPTION_VALUE2")[0].childNodes[0].nodeValue;
      document.getElementsByName("field_01_4")[0].style.color ="#0000FF";
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
                     checkRateCal();
}

function afterDel(tbl,chk){
  if(tbl=="formTable06"){
    changeDRCR();
    recalT6();
    reBindTCEvent();
  }
  if(tbl=="formTable02"){
     changeAmount("02_8","04_22");
    checkRateCal();
  }
}

afterSetDay = function(obj){
	if(afterSetDayFlag === 1){
    changeCurrencyByDate('field_02_9','field_02_11',obj);
    rowTotal(obj);
    afterSetDayFlag = 0;
	}
}

//This function used for validation values just before approving process.
function validationUpdate(nodeId){
  if( !checkRateCal() )  return false;

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

//This function used for validation values just before submit form.
function validationSubmit(){
  if( !checkRateCal() )  return false;
  notifyEpaymentApplicant();
}

//This function used for validation values just before save form.
function validationSave(){
   if( !checkRateCal() )  return false;
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