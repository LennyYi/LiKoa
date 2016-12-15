/*******************************************************************************
 * Global Form Functions (Version: 2014-07-25)
 ******************************************************************************/

/** ***** 2014-05-04: For new form test ***** */
jQuery(function() {
    var forms = [];
    var testers = [ "ASNPGH6" ];

    var openDate = new Date(2014, 4, 20); // 2014-05-20
    var date = new Date();

    if (date < openDate && _checkTestForm(forms)) {
        var operateType = jQuery("[name='operateType']").val();
        // alert("operateType: " + operateType);
        if (operateType == "00" && !_checkFormTester(testers)) {
            alert("抱歉，此表单还未正式开放用户使用，请等待通知，谢谢！");
            jQuery("[name='initBtn']").add("[name='addBtn']").prop("disabled", true);
        }
    }
});

function _checkTestForm(forms) {
    var formId = jQuery("[name='formSystemId']").val();
    // alert("formId: " + formId);
    for (var i = 0; i < forms.length; i++) {
        if (formId == forms[i]) {
            return true;
        }
    }
    return false;
};

function _checkFormTester(testers) {
    var staffName = jQuery("[name='submit_staff_code']").parent().text().toUpperCase();
    // alert("staffName: " + staffName);
    for (var i = 0; i < testers.length; i++) {
        if (staffName.indexOf("(" + testers[i] + ")") != -1) {
            return true;
        }
    }
    return false;
};
/** ***************************************** */

// ---------- Common Field IDs ----------
var idReqDate = "request_date";
var idRequester = "request_staff_code";
var idTitle = "title_id";
var idStaffLocation = "staff_location";
var idResType = "it_res_type";

// ---------- Common Field Objects ----------

var _reqDate = null;
var _requester = null;
var _title = null;
var _staffLocation = null;
var _resType = null;

/** ********** Common Functions ********** */

jQuery(function() {
    _reqDate = jQuery("[name='" + idReqDate + "']");
    _requester = jQuery("[name='" + idRequester + "']");
    _title = jQuery("[name='" + idTitle + "']");

    _staffLocation = jQuery("[name='" + idStaffLocation + "']");
    if (_staffLocation.size() != 0) {
        _requester.change(setLocationByRequester);
        setLocationByRequester();
    }

    _resType = jQuery("[name='" + idResType + "']");
    if (_resType.size() != 0) {
        _requester.change(setResTypeByRequester);
        setResTypeByRequester();
    }
});

// ---------- Staff Location ----------
function setLocationByRequester(event) {
    var location = getStaffLocation(_requester.val());
    if (location == null) {
        location = "";
    }
    // alert(location);
    _staffLocation.val(location);
};

function getStaffLocation(staffCode) {
    var sql = "select pacii_dept_code from tpma_staffbasic where staff_code = '" + staffCode + "'";

    var xmlObj = selectFromSQL(sql, 2);
    if (!xmlObj) {
        return "ERROR: getStaffLocation > xmlObj = " + xmlObj;
    }
    var items = xmlObj.getElementsByTagName("item");
    if (!items || items.length == 0) {
        return null;
    }
    var childNodes = items[0].getElementsByTagName("PACII_DEPT_CODE")[0].childNodes;
    if (!childNodes || childNodes.length == 0) {
        return null;
    }
    return childNodes[0].nodeValue;
};

// ---------- Resource Type ----------
function setResTypeByRequester(event) {
    var resType = getResType(_requester.val());
    if (resType == null) {
        resType = "";
    }
    // alert(resType);
    _resType.val(resType);
};

function getResType(staffCode) {
    var sql = "select res_type from tpma_staffbasic where staff_code = '" + staffCode + "'";

    var xmlObj = selectFromSQL(sql, 2);
    if (!xmlObj) {
        return "ERROR: getResType > xmlObj = " + xmlObj;
    }
    var items = xmlObj.getElementsByTagName("item");
    if (!items || items.length == 0) {
        return null;
    }
    var childNodes = items[0].getElementsByTagName("RES_TYPE")[0].childNodes;
    if (!childNodes || childNodes.length == 0) {
        return null;
    }
    return childNodes[0].nodeValue;
};

/** ********** Old Functions ********** */

function ul_sel() {
    alert('Hello');
};

function servicePriseTS(obj) {
    if (obj.value == '1') {
        $("field_S5_2").required = "false";
    } else {
        $("field_S5_2").required = "true";
    }
};

function jdesRoleChange(obj) {
    var index = obj.rowIndex;
    if (index == '-1')
        index = '0';
    if (obj.value == '02') {
        document.getElementsByName("field_3_5")[index].disabled = 'true';
        document.getElementsByName("field_3_7")[index].disabled = '';
    } else {
        document.getElementsByName("field_3_5")[index].disabled = "";
        document.getElementsByName("field_3_7")[index].disabled = 'true';
    }
};

function jp_njp() {
    alert("对于没有区分日文和非日文标准的组别，成绩统一填写在此项");
};

function check_late_BPO02() {
    var late = document.getElementsByName("field_1_4")[0].value;
    if (late == 1) {
        alert("由于您存在迟到或早退记录，已经没有申请该奖金的资格，建议重新确认输入或放弃申请该奖金");
    }
};

function check_holiday_BPO02() {
    var holiday = document.getElementsByName("field_1_6")[0].value;
    if (holiday.substr(0, 1) == ".")
        document.getElementsByName("field_1_6")[0].value = "0" + holiday;
    if (holiday > 0.5) {
        alert("由于您本月的无薪假和病假天数超过0.5天，已经没有申请该奖金的资格，建议重新确认输入或放弃申请该奖金");
    }
};

function check_present_day_BPO02() {
    var present = document.getElementsByName("field_1_5")[0].value;
    var workday = document.getElementsByName("field_1_10")[0].value;
    if (present != "" && workday != "") {
        if (present < (workday * 0.8)) {
            alert("由于你的本月出勤日少于于工作日的80%，已经没有申请该奖金的资格，建议重新确认输入或放弃申请该奖金");
        }
    }
};

function check_late_BPO03() {
    var late = document.getElementsByName("field_1_12")[0].value;
    if (late == 1) {
        alert("由于您存在迟到或早退记录，已经没有申请该奖金的资格，建议重新确认输入或放弃申请该奖金");
    }
};

function check_holiday_BPO03() {
    var holiday = document.getElementsByName("field_1_14")[0].value;
    if (holiday.substr(0, 1) == ".")
        document.getElementsByName("field_1_14")[0].value = "0" + holiday;
    if (holiday > 0.5) {
        alert("由于您本月的无薪假和病假天数超过0.5天，已经没有申请该奖金的资格，建议重新确认输入或放弃申请该奖金");
    }
};

function check_present_day_BPO03() {
    var present = document.getElementsByName("field_1_13")[0].value;
    var workday = document.getElementsByName("field_1_15")[0].value;
    if (present != "" && workday != "") {
        if (present < workday * 0.8) {
            alert("由于你的本月出勤日少于于工作日的80%，已经没有申请该奖金的资格，建议重新确认输入或放弃申请该奖金");
        }
    }
};

function check_staff_validation_BPO03() {
    var validation = document.getElementsByName("field_1_6")[0].value;
    if (validation == 2) {
        alert("由于你最近三个月没有都能通过在职员工评估，已经没有申请该奖金的资格，建议重新确认输入或放弃申请该奖金");
    }
};

function check_Qmark_BPO03() {
    var qmark = document.getElementsByName("field_1_5")[0].value;
    if (qmark == 2) {
        alert("由于你过去一年没有获得Q-mark，已经没有申请该奖金的资格，建议重新确认输入或放弃申请该奖金");
    }
};

function tutor_range_BPO03() {
    var rank = document.getElementsByName("field_1_10")[0].value;
    if (rank > 2) {
        alert("你的本月组内导师效率评比排名处于前两位之外，请认真确认输入再提交表单");
    }
};

function visa_change_ADMTR02() {
    var visaVal = document.getElementsByName("field_03_2")[0].value;
    if (visaVal != '6') {// Not Required
        document.getElementsByName("field_03_9")[0].required = 'true';
    } else {
        document.getElementsByName("field_03_9")[0].required = 'false';
    }
};

function changeAmount(addend, summary) {
    var tbl = document.getElementsByName("field_" + addend);
    var sum = 0;
    for (i = 0; i < tbl.length; i++) {
        sum = sum + parseFloat(tbl[i].value.replace(/,/g, ''));
    }
    $("field_" + summary).value = formatNumber(sum, '#,##0.00');

};

afterSetDayFlag = 0;
// override calendar.js closeLayer, the calendar.js setday method will cause the
// onchange method invalid; Robert 2010-01-15
closeLayer = function() {
    document.all.meizzDateLayer.style.display = "none";
    try {
        if (afterSetDay) {
            afterSetDay(outObject);
        }
    } catch (e) {
    }
};

function showRefFormWindow3(fieldId, divId, tagId) {
    var prefix = $("requestUrl").value;
    var selectRefForms = encodeURIComponent($(tagId).value.trim());
    var url = prefix + "/formManageAction.it?method=selectRefForm&multiSelection=1&selectRefForms=" + selectRefForms;
    openRefFormWindow2(prefix, url, fieldId, divId, tagId);
};

var queryString = function(key) {
    return (window.parent.document.location.search.match(new RegExp("(?:^\\?|&)" + key + "=(.*?)(?=&|$)")) || [ '',
            null ])[1];
};

function checkBJuser() {
    if (!document.getElementById('addBtn'))
        return false;

    var staff_code = document.getElementById('submit_staff_code').value;
    var sql = "select logon_id V1 from tpma_staffbasic where staff_code='" + staff_code + "'";
    var xmlObj = selectFromSQL(sql, 2);
    if (xmlObj) {
        var options = xmlObj.getElementsByTagName("item");
        if (options && options.length) {
            var logon_id = options[0].getElementsByTagName("V1")[0].childNodes[0].nodeValue;
            if (logon_id.substring(0, 2) == 'BS' || logon_id.substring(0, 2) == 'bs') {
                document.getElementById('addBtn').disabled = true;
                document.getElementById('initBtn').disabled = true;

                alert("抱歉，这个表单暂时只供广州同事使用。");
                return true;
            }
        }
    }
    return false;
};
function receiveNames(selectNameStr) {
    document.all.selectStaffBtn.nextSibling.nextSibling.nextSibling.innerText = selectNameStr;
}

function clickPDF(fieldId) {
    var refForms = document.getElementsByName(fieldId)[0].parentNode.innerText;
    // alert(refForms );

    var paramStr = "";
    if (refForms.Trim() == "") {
        alert("No reference form");
        return;
    }
    var arr = refForms.split(",");

    for (var i = 0; i < arr.length; i++) {
        paramStr += "&requestNos=" + arr[i].Trim();
    }
    openCenterWindow2("Export_all_to_PDF", 800, 600);
    var url = "wkfProcessAction.it?method=exportPDF" + paramStr;
    document.forms[0].action = url;
    document.forms[0].target = "Export_all_to_PDF";
    document.forms[0].submit();
}
