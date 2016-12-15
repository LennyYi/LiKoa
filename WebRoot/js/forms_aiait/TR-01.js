/*******************************************************************************
 * CO-TR-01 (Travel Request Form) Version: 2014-07-24
 ******************************************************************************/

var idReqDate = "request_date";
var idRequester = "request_staff_code";
var idTitle = "title_id";
var idLocation = "field_01_18";
var idCostParty = "field_01_19";
var idProjectCode = "field_01_2";
var idOvernight = "-";
var idUrgent = "urgent_level";

var idTotalFee = "field_01_6";
var idTotalFee2 = "field_BB_13";

var idTable = "formTable07";
var idStartDate = "field_07_3";
var idEndDate = "field_07_4";
var idDuration = "field_07_7";
var idRegionCity = "travel_city";
var idOtherCity = "field_07_1";

var idStdHotel = "field_BB_1";
var idStdHotelDesc = "field_BB_2";
var idExpHotel = "field_BB_3";
var idExpHotelDesc = "field_BB_4";

var idStdPerdiem = "field_BB_5";
var idStdPerdiemDesc = "field_BB_6";
var idExpPerdiem = "-";
var idExpPerdiemDesc = "-";

var idTrans = "field_BB_7";
var idTransDesc = "field_BB_8";
var idMisc = "field_BB_9";
var idMiscDesc = "field_BB_10";
var idOthers = "field_BB_11";
var idOthersDesc = "field_BB_12";

// ==================================================

var _reqDate = null;
var _requester = null;
var _title = null;
var _location = null;
var _costParty = null;
var _projectCode = null;
var _overnight = null;
var _urgent = null;

var _totalFee = null;

var _stdHotel = null;
var _stdHotelDesc = null;
var _expHotel = null;
var _expHotelDesc = null;

var _stdPerdiem = null;
var _stdPerdiemDesc = null;
var _expPerdiem = null;
var _expPerdiemDesc = null;

var _trans = null;
var _transDesc = null;
var _misc = null;
var _miscDesc = null;
var _others = null;
var _othersDesc = null;

var MLCHINA = "A01";
var HK = "A02";
var OVERSEA = "Z99";
var MISC_MLCHINA = "500.00";
var MISC_OVERSEA = "1000.00";
var DISC_SAMEDAY = "0.5";
var DISC_OVERMONTH = "0.85";
var ERROR = "Error";
var SEPARATOR = ";";
var RMB = "RMB";
var L_BJ = "01";

var date = new Date();
var grade = null;
var resType = null;

jQuery(function() {
    date = date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate();

    _reqDate = jQuery("[name='" + idReqDate + "']");
    _requester = jQuery("[name='" + idRequester + "']");
    _title = jQuery("[name='" + idTitle + "']");
    _location = jQuery("[name='" + idLocation + "']");
    _location.change(onChangeLocation);

    _totalFee = jQuery("[name='" + idTotalFee + "']").add("[name='" + idTotalFee2 + "']");

    _stdHotel = jQuery("[name='" + idStdHotel + "']");
    _stdHotelDesc = jQuery("[name='" + idStdHotelDesc + "']");
    _expHotel = jQuery("[name='" + idExpHotel + "']");
    _expHotel.change(onChangeExpHotel);
    _expHotelDesc = jQuery("[name='" + idExpHotelDesc + "']");

    _stdPerdiem = jQuery("[name='" + idStdPerdiem + "']");
    _stdPerdiemDesc = jQuery("[name='" + idStdPerdiemDesc + "']");
    _expPerdiem = jQuery("[name='" + idExpPerdiem + "']");
    _expPerdiem.change(onChangeExpPerdiem);
    _expPerdiemDesc = jQuery("[name='" + idExpPerdiemDesc + "']");

    _trans = jQuery("[name='" + idTrans + "']");
    _trans.change(onChangeTrans);
    _transDesc = jQuery("[name='" + idTransDesc + "']");
    _misc = jQuery("[name='" + idMisc + "']");
    _misc.change(onChangeMisc);
    _miscDesc = jQuery("[name='" + idMiscDesc + "']");
    _others = jQuery("[name='" + idOthers + "']");
    _others.change(onChangeOthers);
    _othersDesc = jQuery("[name='" + idOthersDesc + "']");

    _overnight = jQuery("[name='" + idOvernight + "']");
    _urgent = jQuery("[name='" + idUrgent + "']");

    _costParty = jQuery("[name='" + idCostParty + "']").change(onChangeCostParty);
    _projectCode = jQuery("[name='" + idProjectCode + "']");

    if (_requester.attr("type") != "hidden") {
        onchangeRequester(_requester.val());
    }
});

function onchangeRequester(staffCode) {
    // Get Staff Title and Resource Type
    var sql = "select title_id, res_type from tpma_staffbasic where rtrim(staff_code) = '" + staffCode + "'";

    var xmlObj = selectFromSQL(sql, 2);
    if (!xmlObj) {
        alert("System Error, Please retry ...");
        return;
    }
    var items = xmlObj.getElementsByTagName("item");
    if (!items || items.length == 0) {
        return;
    }
    var titleId = items[0].getElementsByTagName("TITLE_ID")[0].childNodes[0].nodeValue;
    _title.val(titleId);
    resType = items[0].getElementsByTagName("RES_TYPE")[0].childNodes[0].nodeValue;

    // Get Staff Grade
    if (resType == '08') {
        // For staff as EC, the band is 1 by default.
        grade = '1';
    } else {
        sql = "select * from teflow_user_grade_history where staff_code = '" + staffCode
                + "' order by effective_date desc";

        var xmlObj = selectFromSQL(sql, 2);
        if (!xmlObj) {
            alert("System Error, Please retry ...");
            return;
        }
        var items = xmlObj.getElementsByTagName("item");
        if (!items || items.length == 0) {
            alert("No Grade record, Please contact administrator ...");
            return;
        }
        grade = items[0].getElementsByTagName("NEW_GRADE")[0].childNodes[0].nodeValue;
        grade = Number(grade).toString();
    }

    calStdFee();
};

function onChangeLocation(event) {
    setMisc();
    calTotalFee();
};

function onChangeDate(obj) {
    var i = obj.rowIndex;
    var _duration = jQuery(jQuery("[name='" + idDuration + "']").get(i));

    var fromDate = null;
    var _fromDates = jQuery("[name='" + idStartDate + "']");
    var _fromDate = jQuery(_fromDates.get(i));
    if (_fromDate.val() != "") {
        fromDate = _fromDate.val().split("/");
        fromDate = new Date(fromDate[2], fromDate[0] - 1, fromDate[1]);
    }

    var toDate = null;
    var _toDates = jQuery("[name='" + idEndDate + "']");
    var _toDate = jQuery(_toDates.get(i));
    if (_toDate.val() != "") {
        toDate = _toDate.val().split("/");
        toDate = new Date(toDate[2], toDate[0] - 1, toDate[1]);
    }

    if (fromDate == null && toDate == null) {
        _duration.val("");
        calStdFee();
        return;
    }

    // Check for date overlapped
    for (var j = 0; j < _fromDates.size(); j++) {
        if (j == i) {
            continue;
        }
        var fromDate2 = null;
        var _fromDate2 = jQuery(_fromDates.get(j));
        if (_fromDate2.val() != "") {
            fromDate2 = _fromDate2.val().split("/");
            fromDate2 = new Date(fromDate2[2], fromDate2[0] - 1, fromDate2[1]);
        }

        var toDate2 = null;
        var _toDate2 = jQuery(_toDates.get(j));
        if (_toDate2.val() != "") {
            toDate2 = _toDate2.val().split("/");
            toDate2 = new Date(toDate2[2], toDate2[0] - 1, toDate2[1]);
        }

        if (fromDate2 == null || toDate2 == null) {
            continue;
        }

        if (fromDate != null && fromDate2 <= fromDate && fromDate < toDate2) {
            alert('Start Date overlapped with others.');
            _fromDate.focus();
            _fromDate.val("");
            _duration.val("");
            calStdFee();
            return;
        }

        if (toDate != null && fromDate2 <= toDate && toDate < toDate2) {
            alert('End Date overlapped with others.');
            _toDate.focus();
            _toDate.val("");
            _duration.val("");
            calStdFee();
            return;
        }

        if (fromDate != null && toDate != null && fromDate < fromDate2 && toDate2 < toDate) {
            alert('Date Range overlapped with others.');
            _toDate.focus();
            _toDate.val("");
            _duration.val("");
            calStdFee();
            return;
        }
    }

    if (fromDate == null || toDate == null) {
        _duration.val("");
    } else {
        var diff = (toDate - fromDate) / (24 * 60 * 60 * 1000);
        if (diff < 0) {
            alert('End Date cannot be early than Start Date.');
            _toDate.focus();
            _toDate.val("");
            _duration.val("");
        } else {
            _duration.val(diff);
        }
    }
    calStdFee();
};

function onChangeRegionCity(obj) {
    var _dest = jQuery(obj);
    var _city = _dest.find("option:selected");
    if (_city.val() != "") {
        var reg = _city.attr("misc").split(";")[0];
        var _otherCity = _dest.parent().parent().find("[name='" + idOtherCity + "']");
        if (_city.val() == reg) {
            _otherCity.prop("readonly", false);
        } else {
            _otherCity.prop("readonly", true).val("");
        }
    }
    calStdFee();
};

function onChangeExpHotel(event) {
    var hotelFee = Number(_expHotel.val());
    if (hotelFee <= 0 || isNaN(hotelFee)) {
        _expHotel.val("");
    } else {
        _expHotel.val(hotelFee.toFixed(2));
    }
    calTotalFee();
};

function onChangeExpPerdiem(event) {
    var perdiemFee = Number(_expPerdiem.val());
    if (perdiemFee <= 0 || isNaN(perdiemFee)) {
        _expPerdiem.val("");
    } else {
        _expPerdiem.val(perdiemFee.toFixed(2));
    }
    calTotalFee();
};

function onChangeTrans(event) {
    var transFee = Number(_trans.val());
    if (transFee <= 0 || isNaN(transFee)) {
        _trans.val("");
    } else {
        _trans.val(transFee.toFixed(2));
    }
    calTotalFee();
};

function onChangeMisc(event) {
    var miscFee = Number(_misc.val());
    if (miscFee <= 0 || isNaN(miscFee)) {
        _misc.val("");
    } else {
        _misc.val(miscFee.toFixed(2));
    }
    calTotalFee();
};

function onChangeOthers(event) {
    var othersFee = Number(_others.val());
    if (othersFee <= 0 || isNaN(othersFee)) {
        _others.val("");
    } else {
        _others.val(othersFee.toFixed(2));
    }
    calTotalFee();
};

function calTotalFee() {
    var hotelFee = _expHotel.val() != "" ? _expHotel.val() : _stdHotel.val();
    hotelFee = hotelFee != null && hotelFee != "" ? Number(hotelFee.replace(",", "")) : 0;

    var perdiemFee = _expPerdiem.val() != null && _expPerdiem.val() != "" ? _expPerdiem.val() : _stdPerdiem.val();
    perdiemFee = perdiemFee != null && perdiemFee != "" ? Number(perdiemFee.replace(",", "")) : 0;

    var transFee = _trans.val();
    transFee = transFee != null && transFee != "" ? Number(transFee.replace(",", "")) : 0;

    var miscFee = _misc.val();
    miscFee = miscFee != null && miscFee != "" ? Number(miscFee.replace(",", "")) : 0;

    var othersFee = _others.val();
    othersFee = othersFee != null && othersFee != "" ? Number(othersFee.replace(",", "")) : 0;

    var totalFee = hotelFee + perdiemFee + transFee + miscFee + othersFee;
    _totalFee.val(totalFee == 0 ? "" : totalFee.toFixed(2));
};

function calStdFee() {
    getStdFee(_stdHotel, _stdHotelDesc, getHotelRate);
    getStdFee(_stdPerdiem, _stdPerdiemDesc, getPerdiemRate);
    setMisc();
    calTotalFee();

    // Other Settings
    setOvernight();
    setUrgent();
};

function getStdFee(_stdFee, _stdDesc, _getStdRate) {
    var _durations = jQuery("[name='" + idDuration + "']");
    var _cities = jQuery("[name='" + idRegionCity + "']");
    var stdFee = 0;
    var stdDesc = "";
    var abnormal = false;
    var _duration = 0;

    for (var i = 0; i < _durations.size(); i++) {
        stdDesc += (i == 0 ? "" : "\n") + "(" + (i + 1) + ")";

        var duration = jQuery(_durations.get(i)).val();
        duration = duration == "" ? -1 : Number(duration);
        if (duration == -1 || isNaN(duration)) {
            abnormal = true;
            continue;
        }

        var _city = jQuery(_cities.get(i)).find("option:selected");
        var city = _city.val();
        if (city == "") {
            abnormal = true;
            continue;
        }
        var reg = _city.attr("misc").split(";")[0];
        // alert("City/Region: " + city + "/" + reg);

        var rateCity = city;
        var _rate = _getStdRate.call(this, date, city, grade);
        if (_rate == null && city != reg) {
            rateCity = reg;
            _rate = _getStdRate.call(this, date, reg, grade);
        }
        if (_rate == null && rateCity != MLCHINA && rateCity != OVERSEA) {
            rateCity = OVERSEA;
            _rate = _getStdRate.call(this, date, OVERSEA, grade);
        }
        if (_rate == null) {
            stdDesc += " * No Standard Rate used for the Region/City";
            abnormal = true;
            continue;
        }
        if (_rate.indexOf(ERROR) != -1) {
            stdDesc += " " + _rate;
            abnormal = true;
            continue;
        }
        // alert("Standard Rate: " + rateCity + "/" + _rate);

        _rate = _rate.split(SEPARATOR);
        var rate = _rate[0];
        var curr = _rate[1];
        var itemDesc = rate + " (" + curr + ")";
        if (curr != RMB) {
            var exRate = getExchangeRate(date, curr);
            rate = Number((Number(rate) * Number(exRate)).toFixed(2));
            itemDesc += " * " + exRate + " (Ex. Rate)";
        }

        var dur = duration;
        var durDesc = duration;
        if (_stdFee == _stdPerdiem) {
            // For perdiem adjustment according to policy
            if (duration == 0 && _durations.size() == 1) {
                dur = Number(DISC_SAMEDAY);
                durDesc = duration;
            } else if (_duration >= 30) {
                dur = duration * Number(DISC_OVERMONTH);
                durDesc = "(" + duration + " * " + DISC_OVERMONTH + ")";
            } else if (_duration + duration > 30) {
                var dur1 = 30 - _duration;
                var dur2 = duration - dur1;
                dur = dur1 + dur2 * Number(DISC_OVERMONTH);
                durDesc = "(" + dur1 + " + " + dur2 + " * " + DISC_OVERMONTH + ")";
            }
        }
        _duration += duration;

        var itemFee = Number((dur * rate).toFixed(2));
        stdFee += itemFee;
        stdDesc += " " + itemFee + " = " + itemDesc + " * " + durDesc + " (Nights)";
    }

    stdFee = stdFee.toFixed(2);
    if (_durations.size() == 0 || abnormal) {
        _stdFee.val("");
    } else {
        _stdFee.val(stdFee);
    }
    _stdDesc.val(stdDesc);
    return _stdFee.val();
};

function getHotelRate(date, city, grade) {
    var sql = "select * from teflow_hotel_rate where (effdate <= '" + date + "') and (city = '" + city
            + "') and (grade <= " + grade + ") order by effdate desc, grade desc";

    var xmlObj = selectFromSQL(sql, 2);
    if (!xmlObj) {
        return ERROR + ": getHotelRate > xmlObj = " + xmlObj;
    }
    var items = xmlObj.getElementsByTagName("item");
    if (!items || items.length == 0) {
        return null;
    }
    var rate = items[0].getElementsByTagName("RATE")[0].childNodes[0].nodeValue;
    var curr = items[0].getElementsByTagName("CURRENCY")[0].childNodes[0].nodeValue;
    return rate + SEPARATOR + curr;
};

function getPerdiemRate(date, city, grade) {
    var sql = "select * from teflow_perdiem_rate where (effdate <= '" + date + "') and (city = '" + city
            + "') and (grade <= " + grade + ") order by effdate desc, grade desc";

    var xmlObj = selectFromSQL(sql, 2);
    if (!xmlObj) {
        return ERROR + ": getPerdiemRate > xmlObj = " + xmlObj;
    }
    var items = xmlObj.getElementsByTagName("item");
    if (!items || items.length == 0) {
        return null;
    }
    var rate = items[0].getElementsByTagName("RATE")[0].childNodes[0].nodeValue;
    var curr = items[0].getElementsByTagName("CURRENCY")[0].childNodes[0].nodeValue;
    return rate + SEPARATOR + curr;
};

function getExchangeRate(date, currency) {
    var sql = "select * from teflow_exchange_rate where (effyear <= year('" + date + "')) and (effmonth <= month('"
            + date + "')) and (currency = '" + currency + "') order by effyear desc, effmonth desc";

    var xmlObj = selectFromSQL(sql, 2);
    if (!xmlObj) {
        return ERROR + ": getExchangeRate > xmlObj = " + xmlObj;
    }
    var items = xmlObj.getElementsByTagName("item");
    if (!items || items.length == 0) {
        return null;
    }
    var rate = items[0].getElementsByTagName("RATE")[0].childNodes[0].nodeValue;
    return rate;
};

function setMisc() {
    var misc = "";
    var _cities = jQuery("[name='" + idRegionCity + "']");
    for (var i = 0; i < _cities.size(); i++) {
        var _city = jQuery(_cities.get(i)).find("option:selected");
        var city = _city.val();
        if (city == "") {
            continue;
        }
        var reg = _city.attr("misc").split(";")[0];
        if (reg == MLCHINA || (reg == HK && _location.val() != L_BJ)) {
            misc = MISC_MLCHINA;
        } else {
            misc = MISC_OVERSEA;
            break;
        }
    }
    _misc.val(misc);
};

function postDeleteRows(tableId, checkBoxId) {
    if (tableId == idTable) {
        calStdFee();
    }
};

function validationSubmit() {
    var _cities = jQuery("[name='" + idRegionCity + "']");
    var _otherCities = jQuery("[name='" + idOtherCity + "']");
    for (var i = 0; i < _cities.size(); i++) {
        var _city = jQuery(_cities.get(i)).find("option:selected");
        var city = _city.val();
        if (city == "") {
            continue;
        }
        var reg = _city.attr("misc").split(";")[0];
        var _otherCity = jQuery(_otherCities.get(i));
        _otherCity.val(jQuery.trim(_otherCity.val()));
        if (city == reg && _otherCity.val() == "") {
            alert("Please input the '" + _otherCity.attr("title") + "'");
            _otherCity.focus();
            return false;
        }
    }

    _expHotelDesc.val(jQuery.trim(_expHotelDesc.val()));
    if (_expHotel.val() != "" && _expHotelDesc.val() == "") {
        alert("Please input the '" + _expHotelDesc.attr("title") + "'");
        _expHotelDesc.focus();
        return false;
    }

    _expPerdiemDesc.val(jQuery.trim(_expPerdiemDesc.val()));
    if (_expPerdiem.val() != "" && _expPerdiemDesc.val() == "") {
        alert("Please input the '" + _expPerdiemDesc.attr("title") + "'");
        _expPerdiemDesc.focus();
        return false;
    }

    _transDesc.val(jQuery.trim(_transDesc.val()));
    if (_trans.val() != "" && _transDesc.val() == "") {
        alert("Please input the '" + _transDesc.attr("title") + "'");
        _transDesc.focus();
        return false;
    }

    _miscDesc.val(jQuery.trim(_miscDesc.val()));
    if (_misc.val() != "" && _miscDesc.val() == "") {
        alert("Please input the '" + _miscDesc.attr("title") + "'");
        _miscDesc.focus();
        return false;
    }

    _othersDesc.val(jQuery.trim(_othersDesc.val()));
    if (_others.val() != "" && _othersDesc.val() == "") {
        alert("Please input the '" + _othersDesc.attr("title") + "'");
        _othersDesc.focus();
        return false;
    }
};

function setOvernight() {
    _overnight.val("2");
    var _durations = jQuery("[name='" + idDuration + "']");
    var _cities = jQuery("[name='" + idRegionCity + "']");

    for (var i = 0; i < _durations.size(); i++) {
        var duration = jQuery(_durations.get(i)).val();
        duration = duration == "" ? -1 : Number(duration);
        if (duration <= 0 || isNaN(duration)) {
            continue;
        }

        var _city = jQuery(_cities.get(i)).find("option:selected");
        var city = _city.val();
        if (city == "") {
            continue;
        }
        var reg = _city.attr("misc").split(";")[0];
        if (reg != MLCHINA) {
            _overnight.val("1");
            break;
        }
    }
};

function setUrgent() {
    _urgent.val("1");
    var reqDate = _reqDate.val().substring(0, 10).split("/");
    reqDate = new Date(reqDate[2], reqDate[0] - 1, reqDate[1]);
    var _fromDates = jQuery("[name='" + idStartDate + "']");

    for (var i = 0; i < _fromDates.size(); i++) {
        var fromDate = jQuery(_fromDates.get(i)).val().split("/");
        fromDate = new Date(fromDate[2], fromDate[0] - 1, fromDate[1]);
        if (fromDate - reqDate < 24 * 60 * 60 * 1000 * 7) {
            _urgent.val("2");
            break;
        }
    }
};

function onChangeCostParty(event) {
    if (_costParty.val() == "01") {
        _projectCode.val("N/A").prop("readonly", true);
    } else {
        _projectCode.val("").prop("readonly", false);
    }
};
