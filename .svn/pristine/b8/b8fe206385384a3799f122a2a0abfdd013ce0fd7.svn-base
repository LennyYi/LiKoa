function document.onreadystatechange(){
	//overwrite begin
try{
initFinPayBy();
onload_func();
onchangeRequester($("request_staff_code").value);

  if(document.getElementsByName("subBtn")[0]) {
            $("subBtn").onmouseover=function(){changeAmount("02_12","04_2");}
  }
  if(document.getElementsByName("addBtn")[0]) {
            $("addBtn").onmouseover=function(){changeAmount("02_12","04_2");}
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
  if($("field_05_2") &&$("field_05_2").value==""){
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
   var sql="SELECT field_04_6  V1 from teflow_95_04 where request_no='"+$("request_no").value+"' ";
   var savedCA="";   
    var xmlObj = selectFromSQL(sql,2);
      savedCA=xmlObj.getElementsByTagName("item")[0].getElementsByTagName("V1")[0].childNodes[0].nodeValue;
            }catch(e){
              //alert(e);
              return;
            };

 if(savedCA!="") {
  alert("请注意：本申请已关联借款单 : " +savedCA);  
  $("field_04_6").value = savedCA;
    changeCAForm($("field_04_6"));
 }
}

function getLoanHis(){

 sql = "select a.request_no %2B '     |' %2B c.field_03_1 %2B '|'  %2B str(d.field_05_1,10,2) V1,a.request_no V2 from teflow_94_05 d, teflow_wkf_process a, teflow_94_03 c where  a.request_no = d.request_no and a.request_no = c.request_no  and a.request_staff_code = '"+$("request_staff_code").value+"'"+
  " and d.field_05_1 is not null and a.status in ('01','02','04') and a.request_no not in (select request_no from teflow_ebank_exp_history where type=2)  order by a.submission_date";

  xmlObj = selectFromSQL(sql,2);

  if(xmlObj){
    var select = $("field_04_6");
    if (select == null)  return;
    clearSelect(select);
    select.appendChild(createEmptyElement(""));
    var items = xmlObj.getElementsByTagName("item");

    for (var i = 0; i < items.length; i++) {
      select.appendChild(createOptionElement(items[i], "V2", "V1"));
    }
  }
}

function rowTotal(ob){
	var i=ob.rowIndex;
	var v7=v8=v9=v10=v11=0;
	var exrate = 1.000;
	var total;
	
	v7=0+document.getElementsByName("field_02_7")[i].value;
	v8=0+document.getElementsByName("field_02_8")[i].value;
	v9=0+document.getElementsByName("field_02_9")[i].value;
	v10=0+document.getElementsByName("field_02_10")[i].value;
	v11=0+document.getElementsByName("field_02_11")[i].value;
	exrate=document.getElementsByName("field_02_15")[i].value;

	total=(parseFloat(v7)+parseFloat(v8)+parseFloat(v9)+parseFloat(v10)+parseFloat(v11))*exrate;
	document.getElementsByName("field_02_12")[i].value=formatNumber(total,'#,##0.00');
	changeAmount("02_12","04_2");
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
$("field_04_5").value="0";
$("field_04_12").value="";
$("field_04_15").value="";
} else {
var strLabel = obj[obj.selectedIndex].text.split("|");
$("field_04_5").value=formatNumber(strLabel[2].trim(),'#,##0.00');
$("field_04_12").value=strLabel[1].trim();
$("field_04_15").value=strLabel[0].trim();
}
recountFinal();
}

function recountFinal(){
var i=$("field_04_14").value.replace(/,/g,'')-$("field_04_2").value.replace(/,/g,'');

//$("field_04_14").value=formatNumber(i,'#,##0.00');

i=$("field_04_2").value.replace(/,/g,'')-$("field_04_5").value.replace(/,/g,'');

$("field_04_7").value=formatNumber(i,'#,##0.00');
$("field_05_1").value=formatNumber(i,'#,##0.00');
$("field_05_17").value=formatNumber(i,'#,##0.00');
}

function searchEform(eFormid){        
  $("field_04_1").value = "";
  $("field_04_16").value = "";
  $("field_04_14").value = "";

  var sql=	"select str(c.form_system_id) %2B '_' %2B c.section_id V1,c.field_id V2,a.flow_id V3 "+
  " from teflow_wkf_process as a,teflow_wkf_define as b,teflow_form_special_field as c "+
  " where  a.request_no = '"+eFormid+"' and c.field_type=1"+
  " and a.flow_id=b.flow_id and b.form_system_id = c.form_system_id order by submission_date desc";

  var table = "";
  var section = "";
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
        $("field_04_1").value = formatNumber(options[0].text,'#,##0.00');//eForm Amount
    }
  }
  
//---------history query------------

  sql = "SELECT a.form_system_id V1,a.section_id V2,a.field_id V3,a1.section_id V4,a1.field_id V5  FROM teflow_form_section_field a,teflow_form_section_field a1,teflow_form b" +
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
      
      sql2 = "select distinct b.request_no V1,b."+field1+" V2"+
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

  $("field_04_16").value = textlines;
  $("field_04_14").value = formatNumber(balance,'#,##0.00');//balance;
  
recountFinal();
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
document.getElementsByName("field_01_5")[0].style.color = cn_name_color;
    }
  }

  getLoanHis();
  changeCAForm($("field_04_6"));
}


function changePayMode(obj){  
   checkPayBy();
   if(obj.value=='02'){
        document.getElementsByName('field_05_22')[0].disabled = false;
        document.getElementsByName('field_05_26')[0].disabled = false;
        document.getElementsByName('field_05_23')[0].required = "true";
    }
   else{
        document.getElementsByName('field_05_22')[0].disabled = true;
        document.getElementsByName('field_05_26')[0].disabled = true;
        document.getElementsByName('field_05_23')[0].required = "false";
   }
   if(obj.value=='11' && parseFloat($("field_05_1").value)>=0){
        alert("仅还款时可选");
        obj.value='';
   }
//      document.getElementsByName(elmt[i])[idx].disabled = false;
//     document.getElementsByName(elmt[i])[idx].required = "true";
}

function afterDel(tbl,chk){
  if(tbl=="formTable06"){
    changeDRCR();
    recalT6();
    reBindTCEvent();
  }
  if(tbl=="formTable02"){
    changeAmount("02_12","04_2");
  }
}

afterSetDay = function(obj){
	if(afterSetDayFlag === 1){
    changeCurrencyByDate('field_02_14','field_02_15',obj);
    rowTotal(obj);
    afterSetDayFlag = 0;
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

function validationSubmit(){
  notifyEpaymentApplicant();
}