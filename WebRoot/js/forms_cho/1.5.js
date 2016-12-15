function document.onreadystatechange(){
	//overwrite begin
try{
  onload_func();
}catch(e){}
try{
  onchangeRequester($("request_staff_code").value);
}catch(e){}
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

function getLoanHis(){
    sql = "select a.request_no %2B '     |' %2B c.field_03_1 %2B '|' %2B str(c.field_03_3,10,2) V1,a.request_no V2"+
  " from teflow_wkf_process as a,teflow_94_03 as c "+
  " where a.request_staff_code='"+$("request_staff_code").value+"'"+
  " and a.request_no = c.request_no"+
  " and a..status in ('01','02','04') "+
  " order by a.submission_date ";
    
  xmlObj = selectFromSQL(sql,2);

  if(xmlObj){
    var select = $("field_03_7");
    if (select == null)  return;
    clearSelect(select);
    select.appendChild(createEmptyElement(""));
    var items = xmlObj.getElementsByTagName("item");
    for (var i = 0; i < items.length; i++) {
      select.appendChild(createOptionElement(items[i], "V2", "V1"));
    }
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
function rowTotal(ob){
	var i=ob.rowIndex;
	var v7=0;
	var exrate = 1.000;
	var total;

	v7=0+$("field_03_3").value;
	exrate=$("field_03_5").value;
	total=parseFloat(v7)*exrate;
	$("field_05_1").value=formatNumber(total,'#,##0.00');
	$("field_05_17").value=formatNumber(total,'#,##0.00');
}

function recountFinal(){
$("field_04_7").value=$("field_04_2").value-$("field_04_5").value;
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
      document.getElementsByName("field_01_4")[0].style.color =cn_name_color;
    }
  }

  getLoanHis();
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