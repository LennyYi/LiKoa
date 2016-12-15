/*******************************************************************************
 * IT-01 (ITPAR Form)
 ******************************************************************************/

var formSystemId;
var team_code;
var tl_id;
var currentNodeId;
var field_04_5;

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

            hideSection("8");
            delNode("loading");
        }

    } catch (e) {
        alert("Fail to load page!");
    }
};

function InitGlobalPmt() {
    formSystemId = jQuery("[name='formSystemId']");
    team_code = jQuery("[name='team_code']");
    tl_id = jQuery("[name='tl_id']");
    setEnableBaseOnSelect("field_01_3", "field_01_2", [ 01 ], true);
    setSpecialFieldRequired("field_01_3", "ITPAR reference", "RefForm");

    DynamicField();

    hideSection("4");
    hideSection("5");
    hideSection("6");
    hideSection("7");
};

function DynamicField() {
    var suffix = "<br>.<br>(Reminder:  For attachments, use the <b><3. Attachment></b> section)";
    var dataExtract = "Data Extraction -> (<a href='upload/download.jsp?fileName=DownloadForm_data_extract.doc'>Request form</a>)"
            + suffix;
    var dataPatch = "Data Patching -> (<a href='upload/download.jsp?fileName=DownloadForm_data_patching.doc'>Request form</a>)"
            + suffix;
    var securityUWID = "Security (UWID) -> (<a href='upload/download.jsp?fileName=DownloadForm_security(UWID).doc'>Request form</a>)"
            + suffix;
    var sdac = "Systems Development or Application Change -> (<a href='upload/download.jsp?fileName=DownloadForm_systemdev_appchange.doc'>Request form</a>)"
            + suffix;
    var tableFileUpdate = "Table/File Update -> (<a href='upload/download.jsp?fileName=DownloadForm_tablefile_update.doc'>Request form</a>)"
            + suffix;

    setContentBaseOnSelect("field_02_3", "field_02_1", [ 01, 04, 07, 11, 14 ], [ dataExtract, dataPatch, securityUWID,
            sdac, tableFileUpdate ]);
    var field_02_3 = jQuery("[name='field_02_3']");
    field_02_3.css("color", "black");
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

function InitGlobalPmtDeal() {
    jQuery("[name='field_02_2']").next().hide();
    currentNodeId = jQuery("[name='currentNodeId']");
    field_04_5 = jQuery("[name='field_04_5']");
    InitAMTable();
    getBAList();
};

function InitGlobalPmtAdvice() {
    jQuery("[name='field_02_2']").next().hide();
    DisableEditFields();
};

function InitAMTable() {

    if (currentNodeId.val() == "1" || currentNodeId.val() == "16" || currentNodeId.val() == "17"
            || currentNodeId.val() == "18") { // AM initial

        if (field_04_5.length == 0) {
            createTableSectionRow("formTable04", "04", "1");
            field_04_5 = jQuery("[name='field_04_5']");
            field_04_5.val("Initiated by AM");
        }
        disableTableButton("formTable04");
        disableTableFields("formTable04 tr:not(:eq(1))");
    }

    // else if (currentNodeId.val() == "10"){ // BA update
    //    
    // afterAddRow = function(tableId, rowIndex, sectionId, formSystemId,
    // nodeId){
    //        
    // if(tableId != 'formTable04'){
    // return;
    // }
    // field_04_5=jQuery("[name='field_04_5']");
    // var aIndex = rowIndex -1;
    // fieldDisable(jQuery("[name='field_04_4']").eq(aIndex), true);
    // };
    // fieldDisable(jQuery("[name='chkid_04']").eq(0), false);
    // }

    else if (currentNodeId.val() == "4" || currentNodeId.val() == "10") { // BA
        // update
        disableTableFields("formTable04 tr:eq(1)");
    }
};

function DisableEditFields() {
    disableTableFields("sectionTable03");
    disableTableFields("formTable011");
    disableTableButton("formTable011");
    disableTableFields("formTable012");
    disableTableButton("formTable012");
    disableTableFields("formTable04");
    disableTableButton("formTable04");
};

function InitSelectionList() {

};

function validationAttachUponSelection() {

    var field_02_1 = jQuery("[name='field_02_1']");

    // if(field_02_1.val() == "06" || field_02_1.val() == "08" ){

    if (!validationUploadedAttach("iAttachementList3", "iAttachementList06")) {

        var msg = "Please download and fill in the '" + field_02_1.find(":selected").text()
                + "'doc, then upload it in  <3.Attachment> section!";
        alert(msg);
        jQuery("#div2 table tr:eq(1) td:eq(1)").focus();
        return false;
    }
    // return true;
    // }
    return true;
};

function validationAMSelection() {

    if (currentNodeId.val() == "1" || currentNodeId.val() == "16" || currentNodeId.val() == "17"
            || currentNodeId.val() == "18") {
        var system_id = jQuery("[name='system_id']");
        if (system_id.length == 0) {
            alert("Please select system owner!");
            return false;
        }

        var sdsa_list_it = jQuery("[name='sdsa_list_it']");
        if (sdsa_list_it.length == 0) {
            alert("Please select SD/SA/ITSD!");
            return false;
        }

        return true;
    }
    return true;
};

function validationSave() {

};

function validationSubmit() {

    DynamicField();

    if (!validationAttachUponSelection()) {
        return false;
    }

    getBUGroup();

    return true;
};

function validationUpdate(currentNodeId) {

    if (!validationAMSelection()) {
        return false;
    }

    return true;
};

function getBUGroup() {
    var field_01_1 = jQuery("[name='field_01_1']");
    var field_99_2 = jQuery("[name='field_99_2']");
    var valueStr = field_01_1.val();
    valueStr = valueStr.slice(0, valueStr.indexOf("_"));
    field_99_2.val(valueStr);
};

function getBAList() {

    if (currentNodeId.val() != "1" && currentNodeId.val() != "16" && currentNodeId.val() != "17"
            && currentNodeId.val() != "18") { // Not AM initial
        return;
    }

    var ba_list_it = document.all.ba_list_it;
    if (ba_list_it == null) {
        return;
    }

    var tempBAValue = ba_list_it.value;

    var field_99_2 = jQuery("[name='field_99_2']");
    var groupBA = getBApproveGroup(field_99_2.val());
    if (groupBA == "") {
        return;
    }

    var sql = "select distinct staff_code option_value,a.staff_name option_label from tpma_staffbasic a where a.status='A' and a.staff_code in "
            + " (select distinct staff_code from teflow_approver_group_member where approver_group_id = '"
            + groupBA
            + "') order by a.staff_name ";

    var xmlObj;
    try {
        xmlObj = selectFromSQL(sql, 2);
    } catch (e) {
        alert("Failed to get BA list due to DB connection error, please try again!");
        throw e;
    }

    if (xmlObj) {
        clearSelect(ba_list_it);
        ba_list_it.appendChild(createEmptyElement(""));

        var items = xmlObj.getElementsByTagName("item");
        if (items == null || items.length == 0) {
            return;
        }

        for (var i = 0; i < items.length; i++) {
            ba_list_it.appendChild(createOptionElement(items[i], "OPTION_VALUE", "OPTION_LABEL"));
        }
    }
    ba_list_it.value = tempBAValue;
};

function getBApproveGroup(BU) {

    var approveGroup = "";

    if (BU == "1") { // Operations
        approveGroup = "TJ"
    } else if (BU == "2") { // Finance
        approveGroup = "TK"
    } else if (BU == "3") { // Distribution and Office Support
        approveGroup = "TL"
    } else if (BU == "4") { // Affiliates
        approveGroup = "TM"
    }

    return approveGroup;
};

function getSystemOwnerList() {

    var valueStr = genRowValueStr("system_id");
    var field_99_1 = jQuery("[name='field_99_1']");
    field_99_1.val(valueStr);
};

function test() {

    // alert(jQuery('#fieldform tr:last td:eq(1)').html());
    // alert(jQuery("[name='field_01_3']").val());
    alert(jQuery("#formTable04 tr").length);

};