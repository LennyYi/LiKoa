/*******************************************************************************
 * IT-02 (ITSRF Form)
 ******************************************************************************/

var field_02_1;
var field_02_8;
var team_code;
var tl_id;
var currentNodeId;

document.onreadystatechange = function() {

    try {

        if (document.readyState == "complete") {
            InitProcess();
            if (process == 1 || process == 2) {
                InitGlobalPmt();
                if (process == 1) {
                    GetDH();
                }
            } else if (process == 7) {
                InitGlobalPmtAdvice();
            } else if (process == 3) {
                InitGlobalPmtDeal();
            }
            delNode("loading");
        }

    } catch (e) {
        alert("Fail to load page!");
    }
};

function InitGlobalPmt() {
    field_02_1 = jQuery("[name='field_02_1']");
    field_02_8 = jQuery("[name='field_02_8']");
    team_code = jQuery("[name='team_code']");
    tl_id = jQuery("[name='tl_id']");
    hideSection("4");
    hideSection("5");
    hideSection("6");
    hideSection("7");
};

function InitGlobalPmtDeal() {

    jQuery("[name='field_02_3']").next().hide();
    currentNodeId = jQuery("[name='currentNodeId']");

    if (currentNodeId.val() == "1") {
        hideSection("4");
        hideSection("5");
        hideSection("6");
        hideSection("7");
    } else if (currentNodeId.val() == "10") {
        hideSection("6");
        hideSection("7");
    }
};

function InitGlobalPmtAdvice() {
    jQuery("[name='field_02_3']").next().hide();
};

function validationAttachUponSelection() {

    var field_02_1 = jQuery("[name='field_02_1']");

    if (!validationUploadedAttach("iAttachementList3", "iAttachementList06")) {

        var msg = "Please download and fill in the Request form doc, then upload it in  <3.Attachment> section!";
        alert(msg);
        // jQuery("#div2 table tr:eq(1) td:eq(1)").focus();
        return false;
    }

    return true;
};

function validationBASelection() {

    if (currentNodeId.val() == "10") {
        var system_id = jQuery("[name='system_id']");
        if (system_id.length == 0) {
            alert("Please select system owner!");
            return false;
        }
        return true;
    }
    return true;
};

function validationSave() {

};

function validationSubmit() {

    if (!validationAttachUponSelection()) {
        return false;
    }

    return true;
};

function validationUpdate(currentNodeId) {

    if (!validationBASelection()) {
        return false;
    }

    return true;
};

function getServiceCost() {
    var valueStr = field_02_1.val();
    valueStr = valueStr.slice(0, valueStr.indexOf("_"));
    field_02_8.val(valueStr);
};

function GetDH() {

    sql = "select distinct u.staff_name NAME, u.staff_code CODE from tpma_team t, tpma_staff u "
            + "where t.team_code ='" + team_code.val() + "' and t.tl_id = u.logon_id and u.status='A' ";

    var xmlObj;
    try {
        xmlObj = selectFromSQL(sql, 2);
    } catch (e) {
        alert("Failed to get Department Head due to DB connection error, please try again!");
        throw e;
    }

    if (xmlObj) {
        var item = jQuery(xmlObj).find("item:first");
        if (item && item.length) {
            tl_id.val(item.find("CODE:first").text());
        }
    }
};