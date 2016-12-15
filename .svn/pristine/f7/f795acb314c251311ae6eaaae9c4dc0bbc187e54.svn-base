/*******************************************************************************
 * HR-04 (Meal Allowance Form) Version: 2013-12-02
 ******************************************************************************/

var formSysId = "79";
var tableRecords = "teflow_" + formSysId + "_02";
var tableAttachment = "teflow_" + formSysId + "_03";

var idReqNo = "request_no";
var idReqDate = "request_date";
var idRequester = "request_staff_code";
var idHasExCase = "field_01_2";

var idDateObj = "ot_plan_date";
var idExCaseObj = "is_exceptional_case";
var idExReasonObj = "field_02_2";
var idWeekyNumObj = "weeky_num";
var idPubHolidayNumObj = "pub_holiday_num";
var idMidNightNumObj = "ot_mid_night_num";
var idDayTimeNumObj = "ot_day_time_num";
var idMealAllowanceNumObj = "ot_meal_allowance_num";
var idTaxiObj = "taxi_fee_amount";
var idTaxiRemarkObj = "field_02_3";

var EX_YES = "01";
var EX_NO = "02";
var EX_MIDNIGHT = "03";
var EX_DAYTIME = "04";

var MIDNIGHT_RATE = "2.4";
var DAYTIME_RATE = "1";

var TAXI_TIME = "21:00:00";
var ERROR = "Error";

// ==================================================

var aryWeekyNum = new Array();
var aryPubHolidayNum = new Array();

window.onload = function() {
    var caseObjs = document.getElementsByName(idExCaseObj);
    var weekyNumObjs = document.getElementsByName(idWeekyNumObj);
    var pubHolidayNumObjs = document.getElementsByName(idPubHolidayNumObj);
    var midNightNumObjs = document.getElementsByName(idMidNightNumObj);
    var dayTimeNumObjs = document.getElementsByName(idDayTimeNumObj);
    var mealAllowanceNumObjs = document.getElementsByName(idMealAllowanceNumObj);

    for ( var i = 0; i < caseObjs.length; i++) {
        caseObjs[i].onchange = selectCase;

        weekyNumObjs[i].readOnly = true;
        weekyNumObjs[i].onkeypress = validateNumberKey;
        weekyNumObjs[i].onchange = setMealAllowanceNum;
        if (weekyNumObjs[i].value != "") {
            weekyNumObjs[i].value = Number(weekyNumObjs[i].value).toFixed(0);
        }

        pubHolidayNumObjs[i].readOnly = true;
        pubHolidayNumObjs[i].onkeypress = validateNumberKey;
        pubHolidayNumObjs[i].onchange = setMealAllowanceNum;
        if (pubHolidayNumObjs[i].value != "") {
            pubHolidayNumObjs[i].value = Number(pubHolidayNumObjs[i].value).toFixed(0);
        }

        aryWeekyNum[i] = "";
        aryPubHolidayNum[i] = "";
        if (caseObjs[i].value == EX_YES) {
            weekyNumObjs[i].readOnly = false;
            pubHolidayNumObjs[i].readOnly = false;
            // weekyNumObjs[i].required = 'true';
            // pubHolidayNumObjs[i].required = 'true';
        } else if (caseObjs[i].value == EX_NO || caseObjs[i].value == "") {
            aryWeekyNum[i] = weekyNumObjs[i].value;
            aryPubHolidayNum[i] = pubHolidayNumObjs[i].value;
        }

        midNightNumObjs[i].onkeypress = validateNumberKey;
        midNightNumObjs[i].onchange = setMealAllowanceNum;
        if (midNightNumObjs[i].value != "") {
            midNightNumObjs[i].value = Number(midNightNumObjs[i].value).toFixed(0);
        }

        dayTimeNumObjs[i].onkeypress = validateNumberKey;
        dayTimeNumObjs[i].onchange = setMealAllowanceNum;
        if (dayTimeNumObjs[i].value != "") {
            dayTimeNumObjs[i].value = Number(dayTimeNumObjs[i].value).toFixed(0);
        }

        var mealAllowanceNum = mealAllowanceNumObjs[i].value;
        mealAllowanceNum = mealAllowanceNum == "" ? 0 : Number(mealAllowanceNum);
        if (caseObjs[i].value != "" && mealAllowanceNum == 0) {
            setMealAllowanceNum(i);
        }
    }

    var nodeId = $("currentNodeId");
    var addBtn = $("addRowBtn");
    if (addBtn && nodeId && nodeId.value >= '1') {
        addBtn.onclick = function() {
            alert('Cannot add row now.');
        };
    }
};

function selectCase() {
    var weekyNumObjs = document.getElementsByName(idWeekyNumObj);
    var pubHolidayNumObjs = document.getElementsByName(idPubHolidayNumObj);

    var index = this.parentNode.parentNode.rowIndex - 1;
    weekyNumObjs[index].readOnly = true;
    pubHolidayNumObjs[index].readOnly = true;
    // alert("Case Category [" + index + "]: " + this.value);
    if (this.value == EX_YES) {
        weekyNumObjs[index].readOnly = false;
        pubHolidayNumObjs[index].readOnly = false;
        // weekyNumObjs[index].required = 'true';
        // pubHolidayNumObjs[index].required = 'true';
    } else {
        weekyNumObjs[index].value = aryWeekyNum[index];
        pubHolidayNumObjs[index].value = aryPubHolidayNum[index];
        // weekyNumObjs[index].required = 'false';
        // pubHolidayNumObjs[index].required = 'false';
    }

    setMealAllowanceNum(index);
};

function setMealAllowanceNum(_index) {
    var weekyNumObjs = document.getElementsByName(idWeekyNumObj);
    var pubHolidayNumObjs = document.getElementsByName(idPubHolidayNumObj);
    var midNightNumObjs = document.getElementsByName(idMidNightNumObj);
    var dayTimeNumObjs = document.getElementsByName(idDayTimeNumObj);
    var mealAllowanceNumObjs = document.getElementsByName(idMealAllowanceNumObj);

    var index = _index;
    if (index == null) {
        index = this.parentNode.parentNode.rowIndex - 1;
    }
    var weekyNum = weekyNumObjs[index].value;
    weekyNum = weekyNum == "" ? 0 : Number(weekyNum);
    if (isNaN(weekyNum)) {
        weekyNum = 0;
        weekyNumObjs[index].value = "0";
    }
    var pubHolidayNum = pubHolidayNumObjs[index].value;
    pubHolidayNum = pubHolidayNum == "" ? 0 : Number(pubHolidayNum);
    if (isNaN(pubHolidayNum)) {
        pubHolidayNum = 0;
        pubHolidayNumObjs[index].value = "0";
    }
    var midNightNum = midNightNumObjs[index].value;
    midNightNum = midNightNum == "" ? 0 : Number(midNightNum);
    if (isNaN(midNightNum)) {
        midNightNum = 0;
        midNightNumObjs[index].value = "0";
    }
    var dayTimeNum = dayTimeNumObjs[index].value;
    dayTimeNum = dayTimeNum == "" ? 0 : Number(dayTimeNum);
    if (isNaN(dayTimeNum)) {
        dayTimeNum = 0;
        dayTimeNumObjs[index].value = "0";
    }

    var mealAllowanceNum = weekyNum + pubHolidayNum + midNightNum * Number(MIDNIGHT_RATE) + dayTimeNum
            * Number(DAYTIME_RATE);
    mealAllowanceNumObjs[index].value = mealAllowanceNum.toFixed(2);

    setExCase();
};

function setExCase() {
    var hasExCase = document.getElementsByName(idHasExCase);
    var caseObjs = document.getElementsByName(idExCaseObj);
    var midNightNumObjs = document.getElementsByName(idMidNightNumObj);
    var dayTimeNumObjs = document.getElementsByName(idDayTimeNumObj);

    for ( var i = 0; i < caseObjs.length; i++) {
        var midNightNum = midNightNumObjs[i].value;
        midNightNum = midNightNum == "" ? 0 : Number(midNightNum);
        var dayTimeNum = dayTimeNumObjs[i].value;
        dayTimeNum = dayTimeNum == "" ? 0 : Number(dayTimeNum);
        if (caseObjs[i].value == EX_YES || midNightNum != 0 || dayTimeNum != 0) {
            hasExCase[0].value = "Yes";
            return;
        }
    }
    hasExCase[0].value = "No";
};

function validateNumberKey(event) {
    if (!event) {
        event = window.event;
    }
    // alert(event.keyCode);
    if (event.keyCode < 48 || event.keyCode > 57) {
        event.returnValue = false;
    }
};

function validateNumber(obj, type) {
    // alert(obj.value);
    if (obj.value == "") {
        return true;
    }
    var number = parseInt(obj.value);
    if (number == 0) {
        obj.value = "";
    } else {
        obj.value = String(number);
    }
    return true;
};

function validateHours(obj) {
    // alert(obj.value);
    if (obj.value == "") {
        return true;
    }
    if (isNaN(obj.value)) {
        alert("Not a valid number!");
        obj.focus();
        return false;
    }
    var hours = parseFloat(obj.value);
    if (hours > 24) {
        alert("You can not input more than 24 hours in a day!");
        obj.focus();
        return false;
    }
    if (hours == 0) {
        obj.value = "";
    } else {
        obj.value = String(hours);
    }
    return true;
};

function validationSubmit() {
    var requestNo = document.getElementsByName(idReqNo)[0].value;
    var reqDate = document.getElementsByName(idReqDate)[0].value;
    var requester = document.getElementsByName(idRequester)[0].value;
    var dateObjs = document.getElementsByName(idDateObj);

    reqDate = getJSDate(reqDate);
    var oldDates = 0;
    var dates = "";
    for ( var i = 0; i < dateObjs.length; i++) {
        var si = i == 0 ? "" : ",";
        dates += si + "'" + dateObjs[i].value + "'";
        if (getJSDate(dateObjs[i].value) < reqDate) {
            oldDates++;
            if (oldDates > 3) {
                alert("Only 3 previous dates could be applied in one form!");
                return false;
            }
        }
    }
    if (dates == "") {
        alert("You have not applied any Meal Allowance record!");
        return false;
    }
    var sql = "select convert(varchar(10), b.ot_plan_date, 101) as ot_plan_date " + "from teflow_wkf_process a, "
            + tableRecords + " b " + "where a.request_no = b.request_no and a.request_staff_code = '" + requester
            + "' " + "and a.request_no <> '" + requestNo + "' and b.ot_plan_date in (" + dates + ")";
    // alert("sql: " + sql);
    var xmlObj = selectFromSQL(sql, 2);
    if (!xmlObj) {
        alert("System Error: Validation failed!");
        return false;
    }
    dates = "";
    var options = xmlObj.getElementsByTagName("item");
    for ( var j = 0; j < options.length; j++) {
        var sj = j == 0 ? "" : ",";
        dates += sj + options[j].getElementsByTagName("OT_PLAN_DATE")[0].childNodes[0].nodeValue;
    }
    if (dates != "") {
        alert("You have already applied the day(s): " + dates);
        return false;
    }
    return true;
};

function validationSave() {
    return validationSubmit();
};

function validationUpdate(nodeId) {
    var requestNo = document.getElementsByName(idReqNo)[0].value;
    var requester = document.getElementsByName(idRequester)[0].value;
    var dateObjs = document.getElementsByName(idDateObj);
    var caseObjs = document.getElementsByName(idExCaseObj);
    var exptReason = document.getElementsByName(idExReasonObj);
    var weekyNumObjs = document.getElementsByName(idWeekyNumObj);
    var pubHolidayNumObjs = document.getElementsByName(idPubHolidayNumObj);
    var midNightNumObjs = document.getElementsByName(idMidNightNumObj);
    var dayTimeNumObjs = document.getElementsByName(idDayTimeNumObj);
    var mealAllowanceNumObjs = document.getElementsByName(idMealAllowanceNumObj);
    var taxiObjs = document.getElementsByName(idTaxiObj);
    var taxiRemarks = document.getElementsByName(idTaxiRemarkObj);
    var hasExCase = document.getElementsByName(idHasExCase);

    for ( var i = 0; i < weekyNumObjs.length; i++) {
        if (caseObjs[i].value == EX_YES && exptReason[i].value == "") {
            alert("Please input the 'Exceptional Reason'");
            exptReason[i].focus();
            return false;
        }

        var weekyNum = weekyNumObjs[i].value;
        weekyNum = weekyNum == "" ? 0 : Number(weekyNum);
        var pubHolidayNum = pubHolidayNumObjs[i].value;
        pubHolidayNum = pubHolidayNum == "" ? 0 : Number(pubHolidayNum);
        //if (caseObjs[i].value == EX_YES && weekyNum == 0 && pubHolidayNum == 0) {
        //    alert("Please input the 'Num of Working Day' or 'Num of Public Holiday'");
        //    weekyNumObjs[i].focus();
        //    return false;
        //}
        if (weekyNum != 0 && pubHolidayNum != 0) {
            alert("Cannot input both 'Num of Working Day' and 'Num of Public Holiday' in one day");
            weekyNumObjs[i].focus();
            return false;
        }
        if (caseObjs[i].value == EX_YES && weekyNum > 2) {
            alert("'Num of Working Day' cannot be more than 2");
            weekyNumObjs[i].focus();
            return false;
        }
        if (caseObjs[i].value == EX_YES && pubHolidayNum > 2) {
            alert("'Num of Public Holiday' cannot be more than 2");
            pubHolidayNumObjs[i].focus();
            return false;
        }

        var mealAllowanceNum = mealAllowanceNumObjs[i].value;
        mealAllowanceNum = mealAllowanceNum == "" ? 0 : Number(mealAllowanceNum);
        if (caseObjs[i].value != "" && mealAllowanceNum == 0) {
            alert("The number of Meal Allowance cannot be zero!");
            weekyNumObjs[i].focus();
            return false;
        }

        var taxiFee = Number(taxiObjs[i].value);
        // alert(taxiFee);
        if (!isNaN(taxiFee) && taxiFee > 0) {
            if (taxiRemarks[i].value == "") {
                alert("Please input the 'Taxi Route'");
                taxiRemarks[i].focus();
                return false;
            }
            if (caseObjs[i].value == EX_NO) {
                var leave = getCheckOutTime(requester, toUSDateString(dateObjs[i].value.substring(0, 10)));
                // alert(leave);
                if (leave != null && leave < TAXI_TIME) {
                    alert("Your checkout time is before " + TAXI_TIME
                            + ", please select the 'Exceptional' case to apply the Taxi fee.");
                    caseObjs[i].focus();
                    return false;
                }
            }
        }
    }

    if (hasExCase[0].value == "Yes" && !hasAttachment(requestNo)) {
        alert("Please upload supporting for 'Exceptional Case, Mid-night or Day-time Support'");
        return false;
    }

    return true;
};

function getCheckOutTime(staff, date) {
    var sql = "select * from tpma_checkinout where staffcode = '" + staff + "' and workdate_c = '" + date + "'";

    var xmlObj = selectFromSQL(sql, 2);
    if (!xmlObj) {
        alert(ERROR + ": getCheckOutTime > xmlObj = " + xmlObj);
        return null;
    }
    var items = xmlObj.getElementsByTagName("item");
    if (!items || items.length == 0) {
        return null;
    }
    var leave = items[0].getElementsByTagName("LEAVE")[0].childNodes[0].nodeValue;
    return leave;
};

function hasAttachment(requestNo) {
    var sql = "select * from " + tableAttachment + " where request_no = '" + requestNo + "'";

    var xmlObj = selectFromSQL(sql, 2);
    if (!xmlObj) {
        alert(ERROR + ": hasAttachment > xmlObj = " + xmlObj);
        return false;
    }
    var items = xmlObj.getElementsByTagName("item");
    return items != null && items.length != 0;
};

function toUSDateString(date) {
    var month = date.substring(5, 7);
    var day = date.substring(8, 10);
    var year = date.substring(0, 4);
    return month + "/" + day + "/" + year;
};

function getJSDate(strDate) {
    if (strDate != "") {
        strDate = strDate.substring(0, 10).split("/");
        return new Date(strDate[2], strDate[0] - 1, strDate[1]);
    } else {
        return new Date();
    }
};

function require(ob, target) {
    if (ob.value != '') {
        $(target).required = "true";
    } else {
        $(target).required = "false";
    }
};

// ========== For New Form ==========
var newDate = new Date(2013, 6, 1);

jQuery(function() {
    jQuery("[name='" + idDateObj + "']").live("change", onChangeDate);
});

function onChangeDate(event) {
    var otDate = null;
    var _otDate = jQuery(this);
    if (_otDate.val() != "") {
        otDate = getJSDate(_otDate.val());
    }

    if (otDate == null) {
        _otDate.val("");
    } else if (otDate < newDate) {
        alert("由于误餐津贴政策的更新，系统已无法接受7月1日以前的误餐申请。如需协助请联系当地HR。谢谢。");
        _otDate.val("");
    }
};
