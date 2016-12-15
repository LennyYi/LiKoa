var staffType="1";


document.onreadystatechange = function() {
    try {
        if (document.readyState == "complete") {
            $("field_02_2").onkeyup = validateDays;
            load_balance();
            load_type();   
            $("cancel_leave_form").disabled = true;
            if( $("operateType").value == "view"  && $("saveType") && $("saveType").value =="update" ){
            	changeApplyType();
            }
            
            delNode("loading");
        }
    } catch (e) {
        alert("Fail to load page!");
    }
    //changeCancelLeaveForm($("request_staff_code").value);

};

function changeApplyType(){
	var applyType = $("field_02_10").value;
	var select = $("cancel_leave_form");
	
	if(applyType =="2"){
		
		if(select.disabled == true){
			select.disabled = false; 
		}
		if(select.required == "false"){
			select.required = "true"; 
		}
		changeCancelLeaveForm($("request_staff_code").value);	
		
	}else{
		
		if(select.disabled == false){
			select.disabled = true; 
			clearSelect(select);
			clearField();
		  }	
		
		if(select.required == "true"){
			select.required = "false"; 
		}	
	}
}
function load_type() {

    sql = "select Staff_type T from teflow_user_work_experience where Staff_code = '" + $("request_staff_code").value + "'";

    var xmlObj = selectFromSQL(sql, 2);

    if (xmlObj) {
        var options = xmlObj.getElementsByTagName("item");
        if (options && options.length) {
            staffType = options[0].getElementsByTagName("T")[0].childNodes[0].nodeValue;
        } else {
        	staffType = "1"
        }
        
    }
};

function checkType() {	
                    
	if(staffType == "1"){
		return true;
	}
	
	var leaveType = $("field_02_1").value;
	if(leaveType == "51" || leaveType == "91"  || leaveType == "92"){
		return true;
	}	
	
	alert("For staff of Intern/External Consultant, can only apply \"No pay leave\" or \"Others leave\"! ");
        $("field_02_1").focus();
	return false;
};

function load_balance() {
    var date = new Date();

    sql = "select annual_total_entitle_days V4_1, annual_carry_forward_days V4_3, annual_applied_days V4_5, annual_balance_days V4_6, "
    	    +"annual_statutory_entitle_days V4_7, annual_company_entitle_days V4_8, annual_forfeit_days V4_9, "
    	    +"sick_total_entitle_days V5_1, sick_applied_days V5_2, sick_balance_days V5_3 "
            + "from teflow_leave_balance where (staff_code = '" + $("request_staff_code").value + "') and (year = "
            + date.getFullYear() + ")";

    var xmlObj = selectFromSQL(sql, 2);

    if (xmlObj) {
        var options = xmlObj.getElementsByTagName("item");
        if (options && options.length) {
        	var totalEntitle = parseFloat(options[0].getElementsByTagName("V4_1")[0].childNodes[0].nodeValue) + parseFloat(options[0].getElementsByTagName("V4_3")[0].childNodes[0].nodeValue);
            $("field_04_1").value = formatNumber(totalEntitle,'#,##0.0');
            $("field_04_3").value = options[0].getElementsByTagName("V4_3")[0].childNodes[0].nodeValue;
            $("field_04_5").value = options[0].getElementsByTagName("V4_5")[0].childNodes[0].nodeValue;
            $("field_04_6").value = options[0].getElementsByTagName("V4_6")[0].childNodes[0].nodeValue;
            
            $("field_04_7").value = options[0].getElementsByTagName("V4_7")[0].childNodes[0].nodeValue;
            $("field_04_8").value = options[0].getElementsByTagName("V4_8")[0].childNodes[0].nodeValue;
            $("field_04_9").value = options[0].getElementsByTagName("V4_9")[0].childNodes[0].nodeValue;
 
            $("field_05_1").value = options[0].getElementsByTagName("V5_1")[0].childNodes[0].nodeValue;
            $("field_05_2").value = options[0].getElementsByTagName("V5_2")[0].childNodes[0].nodeValue;
            $("field_05_3").value = options[0].getElementsByTagName("V5_3")[0].childNodes[0].nodeValue;
        } else {
            $("field_04_1").value = "";
            $("field_04_3").value = "";
            $("field_04_5").value = "";
            $("field_04_6").value = "";
            
            $("field_04_7").value = "";
            $("field_04_8").value = "";
            $("field_04_9").value = "";
            
            $("field_05_1").value = "";
            $("field_05_2").value = "";
            $("field_05_3").value = "";
        }
    }
};

function reqChange(obj) {
	var cancel = $("cancel_leave_form").value;
    if( cancel!=""){
    	return;
    }
    
    var select = $("field_02_1");
    $("field_02_8").value = select[select.selectedIndex].text + "  " + $("field_02_2").value + " Days: From  "
            + $("field_02_3").value + "  To  " + $("field_02_5").value;
};

function checkAnnualBalance() {
    if ($("field_02_1").value != '01') {
        return true;
    }

    var balance = parseFloat($("field_04_6").value);
    if (isNaN(balance)) {
        alert("There is no leave balance record, please contact administrator.");
        return false;
    }

    var applied = parseFloat($("field_04_5").value);

    var applyObj = $("field_02_2");
    var apply = parseFloat(applyObj.value);
    if (isNaN(apply) || apply == 0) {
        alert("Please input valid days.");
        applyObj.focus();
        return false;
    }

    if (balance < apply) {
        alert("Your remaining annual leave days (" + balance + ") is not enough for this request.");
        applyObj.focus();
        return false;
    }

    if (apply < 0 && applied < Math.abs(apply)) {
        alert("Your applied annual leave days (" + applied + ") is not proper for this request.");
        applyObj.focus();
        return false;
    }

    return true;
};

function checkSickBalance() {
    if ($("field_02_1").value != '11') {
        return true;
    }

    var balance = parseFloat($("field_05_3").value);
    if (isNaN(balance)) {
        alert("There is no sick leave balance record, please contact administrator.");
        return false;
    }

    var applied = parseFloat($("field_05_2").value);

    var applyObj = $("field_02_2");
    var apply = parseFloat(applyObj.value);
    if (isNaN(apply) || apply == 0) {
        alert("Please input valid days.");
        applyObj.focus();
        return false;
    }

    if (balance < apply) {
        alert("Your remaining sick leave days (" + balance + ") is not enough for this request.");
        applyObj.focus();
        return false;
    }

    if (apply < 0 && applied < Math.abs(apply)) {
        alert("Your applied sick leave days (" + applied + ") is not proper for this request.");
        applyObj.focus();
        return false;
    }

    return true;
};

function checkDate() {
    var staff_code = $("request_staff_code").value;
    var type = $("field_02_1").value;
    var apply = parseFloat($("field_02_2").value);
    var fromDate = $("field_02_3").value;
    var fromTime = $("field_02_4").value;
    var toDate = $("field_02_5").value;
    var toTime = $("field_02_6").value;
    var fromDateTime = fromTime == "2" ? fromDate + " 12:00" : fromDate + " 00:00";
    var toDateTime = toTime == "1" ? toDate + " 00:00" : toDate + " 12:00";
    if (Date.parse(fromDateTime) > Date.parse(toDateTime)) {
        alert("Invalid Date Range: From Date (" + fromDate + ") > To Date (" + toDate + ")");
        $("field_02_5").focus();
        return false;
    }
    
    var applyType = $("field_02_10").value;
    
    // new leave application form
    if(applyType != "2"){
    	if(apply <0){
    		alert ("The days can not be negative number when apply new leave");
    		$("field_02_2").focus()
    		return false;
    	}

        var sql = "select * from (" + "select distinct "
                + "from_date = case field_02_4 when 2 then dateadd(hour, 12, field_02_3) else field_02_3 end, "
                + "to_date = case field_02_6 when 1 then field_02_5 else dateadd(hour, 12, field_02_5) end, "
                + "days from (select field_02_3, field_02_4, field_02_5, field_02_6, sum(field_02_2) as days "
                + "from teflow_"+$("formSystemId").value+"_02 where request_no in ("
                + "select request_no from teflow_wkf_process where (request_staff_code = '" + staff_code
                + "') and (status = '04')) group by field_02_3, field_02_4, field_02_5, field_02_6 "
                + "having sum(field_02_2) <> 0 union "
                + "select field_02_3, field_02_4, field_02_5, field_02_6, field_02_2 as days "
                + "from teflow_"+$("formSystemId").value+"_02 where request_no in ("
                + "select request_no from teflow_wkf_process where (request_staff_code = '" + staff_code
                + "') and (status not in ('00', '03', '04')))) a) a where (a.from_date <= '" + toDateTime
                + "') and (a.to_date >= '" + fromDateTime + "')";
        var xmlObj = selectFromSQL(sql, 2);
        if (xmlObj) {
            var items = xmlObj.getElementsByTagName("item");
            if (items == null || items.length == 0) {
                return true;
            }
            alert("Overlapped date range against to existing application: From " + fromDate + " To " + toDate + "");
            $("field_02_3").focus();
            return false;
        }
    }else{
        var sql = "select * from ("
				+ "select field_02_1 as type, field_02_3 as from_date, field_02_4 as from_time, field_02_5 as to_date, field_02_6 as to_time, sum(field_02_2) as days "
				+ "from teflow_"+ $("formSystemId").value+ "_02 where request_no in ("
				+ "select request_no from teflow_wkf_process where (request_staff_code = '"+ staff_code
				+ "') and (status = '04')) group by field_02_1, field_02_3, field_02_4, field_02_5, field_02_6 "
				+ "having sum(field_02_2) <> 0) a where (type = '" + type+ "') and (from_date = '" + fromDate + "') and (from_time = '"
				+ fromTime + "') and (to_date = '" + toDate+ "') and (to_time = '" + toTime + "') and (days = "+ (-1 * apply) + ")";
		var xmlObj = selectFromSQL(sql, 2);
		if (xmlObj) {
			var items = xmlObj.getElementsByTagName("item");
			if (items == null || items.length == 0) {
				alert("No such leave application form");
				$("cancel_leave_form").focus();
				return false;
			}
			 return true;
		}
    }
    
    return true;
};

function calculateWorkDayAndCalendarDay(){
	
    var fromDate = $("field_02_3").value;
    var fromTime = $("field_02_4").value;
    var toDate = $("field_02_5").value;
    var toTime = $("field_02_6").value;
    var cancel = $("cancel_leave_form").value;
    
    if(fromDate=="" || toDate=="" || fromTime =="" || toTime =="" || cancel!=""){
    	return;
    }
    
    var fromDateTime = fromTime == "2" ? fromDate + " 12:00" : fromDate + " 00:00";
    var toDateTime = toTime == "1" ? toDate + " 00:00" : toDate + " 12:00";
 
    var millFromDateTime = Date.parse(fromDateTime);
    var millToDateTime = Date.parse(toDateTime);   
    if(millFromDateTime > millToDateTime) {
        alert("Invalid Date Range: From Date (" + fromDate + ") > To Date (" + toDate + ")");
        return false;
    }
 
 //Calculate the total leave days
    var calendarDay = (millToDateTime - millFromDateTime)/3600000/24+0.5;   
    $("field_02_9").value = calendarDay;   

//Get total holidays of selected date range
    sql = "select sum (days) as total_holidays from(select from_date, afrom_date, to_date, ato_date, "
          +"days = datediff(hour, afrom_date, dateadd(hour, 12, ato_date)) from "
          +"(select from_date, afrom_date = case when '"+fromDateTime+"' > from_date then '"+fromDateTime+"' else from_date end, to_date, " 
          +"ato_date = case when '"+toDateTime+"' < to_date then '"+toDateTime+"' else to_date end, status from " 
          +"(select from_date = case status when 2 then dateadd(hour, 12, from_date) else from_date end," 
          +"to_date = case status when 3 then to_date else  dateadd(hour, 12, to_date) end, status from teflow_holiday_define " 
          +"where set_year >= year('"+fromDateTime+"') and set_year <=  year('"+toDateTime+"')) a "
          +"where  (from_date >= '"+fromDateTime+"' and from_date <= '"+toDateTime+"') or (from_date <= '"+fromDateTime+"' and  to_date >= '"+fromDateTime+"')) b )c"

    var xmlObj = selectFromSQL(sql, 2);

//Calculate the total working days 
    if (xmlObj) {
        var options = xmlObj.getElementsByTagName("item");
        if (options == null || options.length == 0 || options[0].text=="") {
        	$("field_02_2").value = calendarDay;
        } else {
            $("field_02_2").value =calendarDay-parseFloat(options[0].text)/24;           
        }
    }
    
    
     
};

closeLayer = function(){
   if(document.all.meizzDateLayer.style.display=="none"){
	return;
   }
   document.all.meizzDateLayer.style.display="none";
   
   if(afterSetDay){
   afterSetDay(outObject);
  }
};

afterSetDay = function(obj){
	 calculateWorkDayAndCalendarDay();
	 reqChange(obj);
};


function validationSubmit() {
	if (!checkLeaveDays()){
		return false;
	}
	
    if (!checkAnnualBalance()) {
        return false;
    }
    
    if (!checkSickBalance()) {
        return false;
    }
     
    if (!checkDate()) {
        return false;
    }
    if (!checkType()) {
        return false;
    }
    
   // fieldDisabled(false);
    
    return true;
};

function validationSave() {
    
    fieldDisabled(false);
    
    return true;
};

function validateDays(event) {
    if (!event) {
        event = window.event;
    }
    var target = (event.target) ? event.target : event.srcElement;
    var re = /^((-)|(-?[0-9]+\.?[0-9]?))$/;
    var days = target.value;
    if (days != "" && !re.test(days)) {
        target.value = days.substring(0, days.length - 1);
        return;
    }
};

function onchangeRequester(staffCode) {
    load_balance();
    changeCancelLeaveForm(staffCode);   
};

function changeCancelLeaveForm(staffCode) { 	
	var select = $("cancel_leave_form");

	if (select == null) {
        return;
    }
	
    var sql = "select w.request_no %2B ' (' %2B v.field_02_8 %2B ')' option_label, w.request_no option_value " 
    	     +"from teflow_wkf_process w,teflow_wkf_define d,  teflow_form f, teflow_"+$("formSystemId").value+"_02 v " 
    	     +"where (w.flow_id = d.flow_id) and (d.form_system_id=f.form_system_id) and (v.request_no = w.request_no) and (w.status = '04')"
    	     +" and (request_staff_code= '"+staffCode+"') and (f.form_id='HR-07') and (w.Submission_date >= dateadd(month, -12, getdate()) ) and (v.field_02_10 <> '2')" 
    	     +" and (w.request_no not in (select cancel_leave_form from teflow_"+$("formSystemId").value+"_02 a, teflow_wkf_process b where (a.field_02_10 ='2') and (a.request_no =b.request_no) and ( (b.status = '01') or (b.status = '02') or (b.status = '04') ) ) )"
    	     +" order by v.field_02_3 desc";

    var xmlObj = selectFromSQL(sql, 2);
    if (xmlObj) {
        clearSelect(select);
        select.appendChild(createEmptyElement(""));
        var items = xmlObj.getElementsByTagName("item");
        if (items == null) {
            return;
        }
        for ( var i = 0; i < items.length; i++) {
            select.appendChild(createOptionElement(items[i], "OPTION_VALUE", "OPTION_LABEL"));
        }
    }
};

function getApprovedFormInfor(rq) {
	
	if(rq==""){
		clearField();
		return;
	}
	
    sql ="select field_02_1, field_02_2, field_02_9, CONVERT(varchar(12) , field_02_3, 101 ) field_02_3, field_02_4, CONVERT(varchar(12) , field_02_5, 101 ) field_02_5, field_02_6, field_02_7, field_02_8 " 
    	+"from teflow_"+$("formSystemId").value+"_02 where request_no='"+rq+"'"

    var xmlObj = selectFromSQL(sql, 2);

    if (xmlObj) {
        var options = xmlObj.getElementsByTagName("item");
        if (options && options.length) {
        	        	
            $("field_02_1").value = options[0].getElementsByTagName("FIELD_02_1")[0].childNodes[0].nodeValue;
            $("field_02_2").value = -options[0].getElementsByTagName("FIELD_02_2")[0].childNodes[0].nodeValue;           
            $("field_02_9").value = -options[0].getElementsByTagName("FIELD_02_9")[0].childNodes[0].nodeValue;         
            $("field_02_3").value = options[0].getElementsByTagName("FIELD_02_3")[0].childNodes[0].nodeValue;      
            $("field_02_4").value = options[0].getElementsByTagName("FIELD_02_4")[0].childNodes[0].nodeValue;
            $("field_02_5").value = options[0].getElementsByTagName("FIELD_02_5")[0].childNodes[0].nodeValue;
            $("field_02_6").value = options[0].getElementsByTagName("FIELD_02_6")[0].childNodes[0].nodeValue;
            
            $("field_02_7").value = options[0].getElementsByTagName("FIELD_02_7")[0].childNodes[0] ? options[0].getElementsByTagName("FIELD_02_7")[0].childNodes[0].nodeValue : "";              
            $("field_02_8").value = "Cancel the leave: " + options[0].getElementsByTagName("FIELD_02_8")[0].childNodes[0].nodeValue;
    
        } else {
        	clearField();
        }
    }
};

function clearField(){
    $("field_02_1").value = "";
    $("field_02_2").value = "";
    $("field_02_9").value = "";
    $("field_02_3").value = "";
    $("field_02_4").value = "";
    $("field_02_5").value = "";
    $("field_02_6").value = "";
    $("field_02_7").value = "";
    $("field_02_8").value = "";
};

function fieldDisabled(val){

    $("field_02_1").disabled=val;
    $("field_02_3").disabled=val;
    $("field_02_4").disabled=val;
    $("field_02_5").disabled=val;
    $("field_02_6").disabled=val;
};

function checkLeaveDays() {
	
	var days = $("field_02_9").value;
		
    if ($("field_02_1").value == '21') {
        if(days > 13){
        	alert("Marriage Leave can not be longer than 13 calendar days.");
        	return false;
        }
    }
    
    if ($("field_02_1").value == '31') {
        if(days > 170){
        	alert("Maternity Leave can not be longer than 170 calendar days.");
        	return false;
        }
    }
    
    if ($("field_02_1").value == '32') {
        if(days > 10){
        	alert("Paternity Leave can not be longer than 10 calendar days.");
        	return false;
        }
    }
    return true;
};