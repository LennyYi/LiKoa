/** ***** 2014-03-20: For new form test ***** */
jQuery(function() {
    var forms = [ "116", "117", "118", "119", "120", "121", "122", "123", "124", "125", "126", "127", "128", "129" ];
    var testers = [ "ASNPGH6", "NCAR159", "NCAR154", "NCAR151", "NCAR068", "NCAR044", "NCAR153", "NCACR002",
            "NCACR012", "BACR016", "BACRY37", "BACRY25", "NACR055", "NACR057", "NACR041", "SACR017", "SLIF048",
            "SACR014", "FACR058", "FACR026", "FACR031", "EACR004", "EACR123", "EACR009", "EACR043", "EACR141" ];

    var openDate = new Date(2014, 2, 24);
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

////////////////////////////////////////////////GD Purchasing Form Functions////////////////////////////////////////////////////////////////////////////
function getAmount(unitPrice, quantity) {
    var Amount;

    Amount = unitPrice * quantity;

    return (Amount);
}

function sumAmount() {
    var Amount, PL;
    var sumAmount = 0, sumPL = 0;
    var tbl=document.getElementsByName("field_03_4");
    if (tbl != null) {
        for (i = 1; i <= tbl.length; i++) {
            Amount = PL = 0;
            if (document.getElementsByName("field_03_4")[i - 1].value != "")
                Amount = parseFloat(document.getElementsByName("field_03_4")[i - 1].value.replace(/,/g,''));
            if (document.getElementsByName("field_03_5")[i - 1].value != "")
                PL = parseFloat(document.getElementsByName("field_03_5")[i - 1].value.replace(/,/g,''));

            //alert(document.getElementsByName("field_03_4")[i - 1].value);            
            //alert(Amount);

            sumAmount += Amount;
            sumPL += PL;
        }
    }

    $("field_04_1").value = Math.round(sumAmount);
    $("field_04_2").value = Math.round(sumPL);
}

function sumForPLAmount(obj) {
    var unitPrice, quantity;
    var index = obj.rowIndex;
    var amount, PL;
    var totalAmount, totalPL;
    if (index == '-1')
        index = '0';
    var objname = obj.name.substring(0, 8);

    if (document.getElementsByName(objname + "_2")[index].value == "") {
        unitPrice = 0;
    } else {
        unitPrice = parseFloat(document.getElementsByName(objname + "_2")[index].value.replace(/,/g,''));
    }

    if (document.getElementsByName(objname + "_3")[index].value == "") {
        quantity = 0;
    } else {
        quantity = parseFloat(document.getElementsByName(objname + "_3")[index].value.replace(/,/g,''));
    }

    amount = getAmount(unitPrice, quantity);
    PL = getAmount(unitPrice, quantity);

    document.getElementsByName(objname + "_4")[index].value = Math.round(amount);
    document.getElementsByName(objname + "_5")[index].value = Math.round(PL);
    sumAmount();
}
// //////////////////////////////////////////////GD Purchasing Form
// Functions////////////////////////////////////////////////////////////////////////////


function sumForTravelTotal() {
    var g1 = 0, g2 = 0, g3 = 0, g4 = 0, g5 = 0, g6 = 0;

    if ($("field_05_1").value == "")
        g1 = 0;
    else
        g1 = parseFloat($("field_05_1").value.replace(/,/g,''));

    if ($("field_05_3").value == "")
        g2 = 0;
    else
        g2 = parseFloat($("field_05_3").value.replace(/,/g,''));

    if ($("field_05_5").value == "")
        g3 = 0;
    else
        g3 = parseFloat($("field_05_5").value.replace(/,/g,''));

    if ($("field_05_7").value == "")
        g4 = 0;
    else
        g4 = parseFloat($("field_05_7").value.replace(/,/g,''));

    if ($("field_05_9").value == "")
        g5 = 0;
    else
        g5 = parseFloat($("field_05_9").value.replace(/,/g,''));

    g6 = g1 + g2 + g3 + g4 + g5;

    $("field_05_11").value = getNum(g6, 2);
}
function selectRole(obj) {
    var index = obj.rowIndex;
    if (index == '-1')
        index = '0';
    if (obj.value == '01') {
        document.getElementsByName("field_03_6")[index].disabled = 'true';
        document.getElementsByName("field_03_7")[index].disabled = 'true';
        document.getElementsByName("field_03_4")[index].disabled = '';
    } else if (obj.value == '02') {
        document.getElementsByName("field_03_4")[index].disabled = 'true';
        document.getElementsByName("field_03_6")[index].disabled = '';
        document.getElementsByName("field_03_7")[index].disabled = '';
    } else {
        document.getElementsByName("field_03_4")[index].disabled = '';
        document.getElementsByName("field_03_6")[index].disabled = '';
        document.getElementsByName("field_03_7")[index].disabled = '';
    }
}

function getSelectVaue(obj) {
    alert(obj.value)
}

//
function getSystemPlatform(obj) {
    if (obj.value == '1') {
        document.all['field_02_5'].required = 'true';
    } else {
        document.all['field_02_5'].required = '';

    }
}

function sel_category_bak(objValue, sectionId) {
    var teamCode = $('team_code').value;
    var subCategoryId = objValue.substring(0, objValue.indexOf("-"));
    // alert("subCategoryId: " + subCategoryId);
    var sql = "select full_year_budget, adjust_full_year_budget, ytd_budget, ytd_actual_expense, balance from teflow_finance_budget where sub_cat_id = '"
            + subCategoryId
            + "' and org_id = '@orgId' and department_id = '"
            + teamCode
            + "' and category_id = '"
            + formCategoryId + "'";
    var xmlObj = selectFromSQL(sql, 1);
    if (xmlObj) {
        var options = xmlObj.getElementsByTagName("result");
        // alert(options[0].getAttribute("value")+","+options[1].getAttribute("value")+","+options[2].getAttribute("value"))
        var value = "";
        if (options && options.length) {
            for ( var i = 0; i < options.length; i++) {
                if (options[i].text == "" || options[i].text == "null") {
                    value = "0";
                } else {
                    value = options[i].text;
                }
                if (options[i].getAttribute("value") == "FULL_YEAR_BUDGET") {
                    $('field_' + sectionId + '_1').value = value;
                } else if (options[i].getAttribute("value") == "ADJUST_FULL_YEAR_BUDGET") {
                    $('field_' + sectionId + '_2').value = value;
                } else if (options[i].getAttribute("value") == "YTD_BUDGET") {
                    $('field_' + sectionId + '_3').value = value;
                } else if (options[i].getAttribute("value") == "YTD_ACTUAL_EXPENSE") {
                    $('field_' + sectionId + '_4').value = value;
                } else if (options[i].getAttribute("value") == "BALANCE") {
                    $('field_' + sectionId + '_5').value = value;
                }
            }

        } else {
            $('field_' + sectionId + '_1').value = "0";
            $('field_' + sectionId + '_2').value = "0";
            $('field_' + sectionId + '_3').value = "0";
            $('field_' + sectionId + '_4').value = "0";
            $('field_' + sectionId + '_5').value = "0";
        }
    }
}

function sel_category(objValue, sectionId) {
    var teamCode = $('team_code').value;
    var subCategoryId = objValue.substring(objValue.indexOf("_") + 1, objValue.indexOf("-"));
    // alert("subCategoryId: " + subCategoryId);
    var sql = "select full_year_budget, adjust_full_year_budget, ytd_budget, ytd_actual_expense, balance from teflow_finance_budget where sub_cat_id = '"
            + subCategoryId
            + "' and org_id = '"+$('company_id').value+"' and department_id = '"
            + teamCode
            + "' and category_id = '"
            + formCategoryId + "'";
    var xmlObj = selectFromSQL(sql, 1);
    if (xmlObj) {
        var options = xmlObj.getElementsByTagName("result");
        // alert(options[0].getAttribute("value")+","+options[1].getAttribute("value")+","+options[2].getAttribute("value"))
        var value = "";
        if (options && options.length) {
            for ( var i = 0; i < options.length; i++) {
                if (options[i].text == "" || options[i].text == "null") {
                    value = "0";
                } else {
                    value = options[i].text;
                }
                if (options[i].getAttribute("value") == "FULL_YEAR_BUDGET") {
                    $('field_' + sectionId + '_1').value = value;
                } else if (options[i].getAttribute("value") == "ADJUST_FULL_YEAR_BUDGET") {
                    $('field_' + sectionId + '_2').value = value;
                } else if (options[i].getAttribute("value") == "YTD_BUDGET") {
                    $('field_' + sectionId + '_3').value = value;
                } else if (options[i].getAttribute("value") == "YTD_ACTUAL_EXPENSE") {
                    $('field_' + sectionId + '_4').value = value;
                } else if (options[i].getAttribute("value") == "BALANCE") {
                    $('field_' + sectionId + '_5').value = value;
                }
            }

        } else {
            $('field_' + sectionId + '_1').value = "0";
            $('field_' + sectionId + '_2').value = "0";
            $('field_' + sectionId + '_3').value = "0";
            $('field_' + sectionId + '_4').value = "0";
            $('field_' + sectionId + '_5').value = "0";
        }
    }
}

function sumForCashTravel() {
    // $('field_05_11').value = parseFloat($("field_05_1").value)+
    // parseFloat($("field_05_3").value)
    $('field_05_11').value = addition("field_05_1", "field_05_3", "field_05_5", "field_05_7", "field_05_9")
}

function computeForREForm() {
    var G9 = 0;
    var G10 = 0;
    var E17 = 0;
    var E18 = 0;
    var E19 = 0;
    var C10 = 0;

    if ($("field_02_5").value == "")
        G9 = 0;
    else
        G9 = parseFloat($("field_02_5").value.replace(/,/g,''));

    if ($("field_02_7").value == "")
        G10 = 0;
    else
        G10 = parseFloat($("field_02_7").value.replace(/,/g,''));

    if ($("field_03_1").value == "")
        E17 = 0;
    else
        E17 = parseFloat($("field_03_1").value.replace(/,/g,''));

    if ($("field_02_6").value == "")
        C10 = 0;
    else
        C10 = parseFloat($("field_02_6").value.replace(/,/g,''));

    if (G9 == 0) {
        alert("Leasing Period must be > 0");
        $("field_02_5").focus();
        return;
        $("field_03_3").value = "0";
        E19 = 0;
    } else {
        $("field_03_3").value = E17 * (G9 * 12 - G10) / (G9 * 12);
        $("field_03_3").value = getNum($("field_03_3").value.replace(/,/g,''), 2);
        E19 = parseFloat($('field_03_3').value);
    }

    if ($('field_03_2').value == "")
        E18 = 0;
    else
        E18 = parseFloat($('field_03_2').value.replace(/,/g,''));

    $("field_03_4").value = getNum((E18 + E19) * 12 * G9, 2)

    var G19 = (E18 + E19) * (12 - getMonth($("field_02_8").value.replace(/,/g,'')))
    $('field_03_5').value = getNum(G19, 2);

    $('field_03_6').value = getNum((E18 + E19) * 12, 2);

    var F21 = $("field_04_1").value.replace(/,/g,'');
    if (F21 == "")
        F21 = 0;
    else
        F21 = parseFloat(F21);

    $("field_04_2").value = getNum(F21 * C10 / (G9 * 12), 2);
    $("field_04_3").value = getNum(F21 / G9, 2);

    var F22 = $("field_05_1").value.replace(/,/g,'');
    if (F22 == "")
        F22 = 0;
    else
        F22 = parseFloat(F22);

    $("field_05_2").value = getNum(F22 * C10 / (G9 * 12), 2);
    $("field_05_3").value = getNum(F22 / G9, 2);

    var F23 = $("field_06_1").value.replace(/,/g,'');
    if (F23 == "")
        F23 = 0;
    else
        F23 = parseFloat(F23);

    $("field_06_2").value = getNum(F23 * 0.95 * (C10 - 1) / (5 * 12), 2);
    $("field_06_3").value = getNum(F23 * 0.95 / 5, 2);

    $("field_07_1").value = getNum(addition("field_03_4", "field_04_1", "field_05_1", "field_06_1", "field_10_1"), 2);
    $("field_07_2").value = getNum(addition("field_03_5", "field_04_2", "field_05_2", "field_06_2", "field_10_1"), 2);
    $("field_07_3").value = getNum(addition("field_03_6", "field_04_3", "field_05_3", "field_06_3", "field_10_1"), 2);
}

function summaryForAutomobile() {
    var iObj = document.frames['iframeList'];
    var B14 = $("field_04_3").value.replace(/,/g,'');
    if (B14 == "")
        B14 = 0;
    else
        B14 = parseFloat(B14);

    var F14 = $("field_04_4").value.replace(/,/g,'');
    if (F14 == "")
        F14 = 0;
    else
        F14 = parseFloat(F14);

    var B15 = $("field_04_5").value.replace(/,/g,'');
    if (B15 == "")
        B15 = 0;
    else
        B15 = parseFloat(B15);

    var F15 = $("field_04_6").value.replace(/,/g,'');
    if (F15 == "")
        F15 = 0;
    else
        F15 = parseFloat(F15);

    var B16 = $("field_04_7").value.replace(/,/g,'');
    if (B16 == "")
        B16 = 0;
    else
        B16 = parseFloat(B16);

    var F16 = $("field_04_8").value.replace(/,/g,'');
    if (F16 == "")
        F16 = 0
    else
        F16 = parseFloat(F16);

    var F17 = $("field_04_10").value.replace(/,/g,'');
    if (F17 == "")
        F17 = 0;
    else
        F17 = parseFloat(F17);

    var C20 = $("field_04_11").value.replace(/,/g,'');
    if (C20 == "")
        C20 = 0;
    else
        C20 = parseFloat(C20 / 100);

    var C21 = $("field_04_12").value.replace(/,/g,''); // Salary Increase(%)
    if (C21 == "")
        C21 = 0;
    else
        C21 = parseFloat(C21 / 100);

    var c23 = B14 * 12;
    iObj.document.all['c23'].value = formatNumber(c23, '#,###');
    var d23 = c23;
    iObj.document.all['d23'].value = iObj.document.all['c23'].value.replace(/,/g,'');
    var e23 = B14 * 12 * (1 + C20);
    iObj.document.all['e23'].value = formatNumber(e23, '#,###');
    var f23 = e23;
    iObj.document.all['f23'].value = iObj.document.all['e23'].value.replace(/,/g,'');
    var g23 = B14 * 12 * (1 + C20) * (1 + C20);
    iObj.document.all['g23'].value = formatNumber(g23, '#,###');
    var h23 = parseFloat(c23) + parseFloat(d23) + parseFloat(e23) + parseFloat(f23) + parseFloat(g23);
    iObj.document.all['h23'].value = formatNumber(h23, '#,###');

    var c24 = B15 * 12;
    iObj.document.all['c24'].value = formatNumber(c24, '#,###');
    var d24 = c24;
    iObj.document.all['d24'].value = iObj.document.all['c24'].value;
    var e24 = B15 * 12 * (1 + C20);
    iObj.document.all['e24'].value = formatNumber(e24, '#,###');
    var f24 = e24;
    iObj.document.all['f24'].value = iObj.document.all['e24'].value;
    var g24 = B15 * 12 * (1 + C20) * (1 + C20);
    iObj.document.all['g24'].value = formatNumber(g24, '#,###');
    var h24 = parseFloat(c24) + parseFloat(d24) + parseFloat(e24) + parseFloat(f24) + parseFloat(g24);
    iObj.document.all['h24'].value = formatNumber(h24, '#,###');

    var c25 = B16 * 12;
    iObj.document.all['c25'].value = formatNumber(c25, '#,###');
    var d25 = parseFloat(c25) * (1 + C21);
    iObj.document.all['d25'].value = formatNumber(d25, '#,###');
    var e25 = parseFloat(d25) * (1 + C21);
    iObj.document.all['e25'].value = formatNumber(e25, '#,###');
    var f25 = parseFloat(e25) * (1 + C21);
    iObj.document.all['f25'].value = formatNumber(f25, '#,###');
    var g25 = parseFloat(f25) * (1 + C21);
    iObj.document.all['g25'].value = formatNumber(g25, '#,###');
    var h25 = c25 + d25 + e25 + f25 + g25;
    iObj.document.all['h25'].value = formatNumber(h25, '#,###');

    var c26 = c23 + c24 + c25;
    iObj.document.all['c26'].value = formatNumber(c26, '#,###');
    var d26 = d23 + d24 + d25;
    iObj.document.all['d26'].value = formatNumber(d26, '#,###');
    var e26 = e23 + e24 + e25;
    iObj.document.all['e26'].value = formatNumber(e26, '#,###');
    var f26 = f23 + f24 + f25;
    iObj.document.all['f26'].value = formatNumber(f26, '#,###');
    var g26 = g23 + g24 + g25;
    iObj.document.all['g26'].value = formatNumber(g26, '#,###');
    var h26 = h23 + h24 + h25;
    iObj.document.all['h26_1'].value = formatNumber(h26, '#,###');
    iObj.document.all['h26'].value = h26;

    var c27 = c26 / (1 + C20);
    iObj.document.all['c27'].value = formatNumber(c27, '#,###');
    var d27 = d26 / ((1 + C20) * (1 + C20));
    iObj.document.all['d27'].value = formatNumber(d27, '#,###');
    var e27 = e26 / ((1 + C20) * (1 + C20) * (1 + C20));
    iObj.document.all['e27'].value = formatNumber(e27, '#,###');
    var f27 = f26 / ((1 + C20) * (1 + C20) * (1 + C20) * (1 + C20));
    iObj.document.all['f27'].value = formatNumber(f27, '#,###');
    var g27 = g26 / ((1 + C20) * (1 + C20) * (1 + C20) * (1 + C20) * (1 + C20));
    iObj.document.all['g27'].value = formatNumber(g27, '#,###');
    var h27 = c27 + d27 + e27 + f27 + g27;
    iObj.document.all['h27_1'].value = formatNumber(h27, '#,###');
    iObj.document.all['h27'].value = h27;

    var h35 = iObj.document.all['h35'].value.replace(/,/g,'');
    var h36 = iObj.document.all['h36'].value.replace(/,/g,'');

    if (h35 == "")
        h35 = 0;
    else
        h35 = parseFloat(h35);

    if (h36 == "")
        h36 = 0;
    else
        h36 = parseFloat(h36);

    iObj.document.all['b39'].value = formatNumber(h26 - h35, '#,###');
    iObj.document.all['b40'].value = formatNumber(h27 - h36, '#,###');

    if ((h27 - h36) > 0)
        iObj.document.all['resultValue'].innerHTML = "<b>Analysis indicates car purchase generates lesser cash outlay<b>";
    else if ((h27 - h36) <= 0)
        iObj.document.all['resultValue'].innerHTML = "<b>Analysis indicates leasehold/rental generates lesser cash outlay<b>";
    else
        iObj.document.all['resultValue'].innerHTML = "";
}

function summaryAutomobilePurchase() {
    var iObj = document.frames['iframeList'];

    var F14 = $("field_04_4").value.replace(/,/g,'');
    if (F14 == "")
        F14 = 0;
    else
        F14 = parseFloat(F14);

    var F15 = $("field_04_6").value.replace(/,/g,'');
    if (F15 == "")
        F15 = 0;
    else
        F15 = parseFloat(F15);

    var F16 = $("field_04_10").value.replace(/,/g,'');
    if (F16 == "")
        F16 = 0;
    else
        F16 = parseFloat(F16);

    var F17 = $("field_04_8").value.replace(/,/g,'');
    if (F17 == "")
        F17 = 0;
    else
        F17 = parseFloat(F17);

    var C20 = $("field_04_11").value.replace(/,/g,''); // PV @ (%)
    if (C20 == "")
        C20 = 0;
    else
        C20 = parseFloat(C20 / 100);

    var C21 = $("field_04_12").value.replace(/,/g,''); // Salary Increase(%)
    if (C21 == "")
        C21 = 0;
    else
        C21 = parseFloat(C21 / 100);

    var C22 = $("field_04_13").value.replace(/,/g,''); // Salary Increase(%)
    if (C22 == "")
        C22 = 0;
    else
        C22 = parseFloat(C22 / 100);

    var b30 = F14;
    iObj.document.all['b30'].value = formatNumber(b30, '#,###');
    var h30 = F14;
    iObj.document.all['h30'].value = formatNumber(h30, '#,###');

    var c31 = F15 * 12;
    iObj.document.all['c31'].value = formatNumber(c31, '#,###');
    var d31 = c31;
    iObj.document.all['d31'].value = iObj.document.all['c31'].value.replace(/,/g,'');
    var e31 = F15 * 12 * (1 + C20);
    iObj.document.all['e31'].value = formatNumber(e31, '#,###');
    var f31 = e31;
    iObj.document.all['f31'].value = iObj.document.all['e31'].value.replace(/,/g,'');
    var g31 = F15 * 12 * (1 + C20) * (1 + C20);
    iObj.document.all['g31'].value = formatNumber(g31, '#,###');
    var h31 = c31 + d31 + e31 + f31 + g31;
    iObj.document.all['h31'].value = formatNumber(h31, '#,###');

    var c32 = F14 * (C22 * 2);
    iObj.document.all['c32'].value = formatNumber(c32, '#,###');
    var d32 = F14 * (C22 * 3);
    iObj.document.all['d32'].value = formatNumber(d32, '#,###');
    var e32 = F14 * C22 * 4;
    iObj.document.all['e32'].value = formatNumber(e32, '#,###');
    var f32 = F14 * C22 * 5;
    iObj.document.all['f32'].value = formatNumber(f32, '#,###');
    var g32 = F14 * C22 * 6;
    iObj.document.all['g32'].value = formatNumber(g32, '#,###');
    var h32 = c32 + d32 + e32 + f32 + g32;
    iObj.document.all['h32'].value = formatNumber(h32, '#,###');

    var c33 = F16;
    iObj.document.all['c33'].value = formatNumber(c33, '#,###');
    var d33 = c33;
    iObj.document.all['d33'].value = iObj.document.all['c33'].value.replace(/,/g,'');
    var e33 = d33;
    iObj.document.all['e33'].value = iObj.document.all['d33'].value.replace(/,/g,'');
    var f33 = e33;
    iObj.document.all['f33'].value = iObj.document.all['e33'].value.replace(/,/g,'');
    var g33 = f33;
    iObj.document.all['g33'].value = iObj.document.all['f33'].value.replace(/,/g,'');
    var h33 = c33 + d33 + e33 + f33 + g33;
    iObj.document.all['h33'].value = formatNumber(h33, '#,###');

    var c34 = F17 * 12;
    iObj.document.all['c34'].value = formatNumber(c34, '#,###');
    var d34 = c34 * (1 + C21);
    iObj.document.all['d34'].value = formatNumber(d34, '#,###');
    var e34 = d34 * (1 + C21);
    iObj.document.all['e34'].value = formatNumber(e34, '#,###');
    var f34 = e34 * (1 + C21);
    iObj.document.all['f34'].value = formatNumber(f34, '#,###');
    var g34 = f34 * (1 + C21);
    iObj.document.all['g34'].value = formatNumber(g34, '#,###');
    var h34 = c34 + d34 + e34 + f34 + g34;
    iObj.document.all['h34'].value = formatNumber(h34, '#,###');

    var b35 = F14;
    iObj.document.all['b35'].value = formatNumber(b35, '#,###')
    var c35 = c31 + c32 + c33 + c34;
    iObj.document.all['c35'].value = formatNumber(c35, '#,###');
    var d35 = d31 + d32 + d33 + d34;
    iObj.document.all['d35'].value = formatNumber(d35, '#,###');
    var e35 = e31 + e32 + e33 + e34;
    iObj.document.all['e35'].value = formatNumber(e35, '#,###');
    var f35 = f31 + f32 + f33 + f34;
    iObj.document.all['f35'].value = formatNumber(f35, '#,###');
    var g35 = g31 + g32 + g33 + g34;
    iObj.document.all['g35'].value = formatNumber(g35, '#,###');
    var h35 = h30 + h31 + h32 + h33 + h34;
    iObj.document.all['h35_1'].value = formatNumber(h35, '#,###');
    iObj.document.all['h35'].value = h35;

    var b36 = b35;
    iObj.document.all['b36'].value = formatNumber(b36, '#,###');
    var c36 = c35 / (1 + C20);
    iObj.document.all['c36'].value = formatNumber(c36, '#,###');
    var d36 = d35 / ((1 + C20) * (1 + C20));
    iObj.document.all['d36'].value = formatNumber(d36, '#,###');
    var e36 = e35 / ((1 + C20) * (1 + C20) * (1 + C20));
    iObj.document.all['e36'].value = formatNumber(e36, '#,###');
    var f36 = f35 / ((1 + C20) * (1 + C20) * (1 + C20) * (1 + C20));
    iObj.document.all['f36'].value = formatNumber(f36, '#,###');
    var g36 = g35 / ((1 + C20) * (1 + C20) * (1 + C20) * (1 + C20) * (1 + C20));
    iObj.document.all['g36'].value = formatNumber(g36, '#,###');
    var h36 = b36 + c36 + d36 + e36 + f36 + g36;
    iObj.document.all['h36_1'].value = formatNumber(h36, '#,###');
    iObj.document.all['h36'].value = h36;

    //
    var h26 = iObj.document.all['h26'].value.replace(/,/g,'');
    var h35 = iObj.document.all['h35'].value.replace(/,/g,'');
    var h27 = iObj.document.all['h27'].value.replace(/,/g,'');
    var h36 = iObj.document.all['h36'].value.replace(/,/g,'');

    if (h26 == "")
        h26 = 0;
    else
        h26 = parseFloat(h26);
    if (h35 == "")
        h35 = 0;
    else
        h35 = parseFloat(h35);
    if (h27 == "")
        h27 = 0;
    else
        h27 = parseFloat(h27);
    if (h36 == "")
        h36 = 0;
    else
        h36 = parseFloat(h36);

    iObj.document.all['b39'].value = formatNumber(h26 - h35, '#,###');
    iObj.document.all['b40'].value = formatNumber(h27 - h36, '#,###');

    if ((h27 - h36) > 0)
        iObj.document.all['resultValue'].innerHTML = "<b>Analysis indicates car purchase generates lesser cash outlay<b>";
    else if ((h27 - h36) <= 0)
        iObj.document.all['resultValue'].innerHTML = "<b>Analysis indicates leasehold/rental generates lesser cash outlay<b>";
    else
        iObj.document.all['resultValue'].innerHTML = "";
}

function changePVRatio() {
    summaryForAutomobile();
    summaryAutomobilePurchase();
}

function onchangeTeam(teamCode) {
    if ($('account_dc')) {
        // alert("account_dc");
        changeAccountDC(teamCode);
    }
}

function changeAccountDC(teamCode) {
    var sql = "select distinct account_dc option_label, account_dc option_value from teflow_finance_budget where org_id = '"+$('company_id').value+"' and department_id = '"
            + teamCode + "'";
    var xmlObj = selectFromSQL(sql, 2);
    if (xmlObj) {
        var select = $("account_dc");
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
}

var formCategoryId = null;

function getSubCategory_bak(categoryId) {
    var sqlCategory = null;
    if ($('account_dc').value.trim() == "") {
        return;
    }
    formCategoryId = categoryId;
    if(categoryId=="06"){//others
        sqlCategory = " not in ('01','02','03','04','05','06')";
    }else{
        sqlCategory = " ='"+categoryId+"'";
    }
    var teamCode = $('team_code').value;
    var sql = "select sub_cat_name option_label, sub_cat_id %2B '-' %2B sub_cat_name option_value from teflow_finance_budget where org_id = '@orgId' and department_id = '"
            + teamCode + "' and category_id " + sqlCategory;
    var xmlObj = selectFromSQL(sql, 2);
    if (xmlObj) {
        var select = $("sub_category");
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
}

function getSubCategory(categoryId) {
    if ($('account_dc').value.trim() == "") {
        return;
    }
    formCategoryId = categoryId;
    var teamCode = $('team_code').value;
    var sql = "select sub_cat_name option_label, category_id %2B '_' %2B sub_cat_id %2B '-' %2B sub_cat_name option_value from teflow_finance_budget where org_id = '"+$('company_id').value+"' and department_id = '"
            + teamCode + "' and category_id = '" + categoryId + "'";
    var xmlObj = selectFromSQL(sql, 2);
    if (xmlObj) {
        var select = $("sub_category");
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
}

window.onload = init;

function init() {
    try{
    initCategory();
    onchangeTeam($("team_code").value);
    }catch(e){}
}

function initCategory() {
    var select = $("sub_category");
    if (select == null || select[0] == null) {
        return;
    }
    var value = select[0].value;
    //alert("sub_category: " + value);
    formCategoryId = value.substring(0, value.indexOf("_"));
}
////////////////////////ePayment functions////////////////////////////////////////////
var oneClick=false;
function onload_func(){
try{
    var str = "<input type='hidden' value='a' name='holdBtn'>";
    var abc = document.createElement(str);
    var container = document.getElementById("thedetailtableDIV");
    container.appendChild(abc);
}catch(e){}
try{
  if(document.getElementsByName("addRowBtn")[0]){
    var btn;
    len = document.getElementsByName("addRowBtn").length;//最后一个table
    btn=document.getElementsByName("addRowBtn")[len-1];
    
    btn.onmouseup=function(){oneClick=true;}
    btn.onmouseout=bindEvent;
        
    var tbl = document.getElementsByName("field_06_11");
    if(tbl.length) {
      for (i = 0; i < tbl.length; i++) {
        bindEvent(i);
        changeSunac(tbl[i]);
      }
    }
  }
}catch(e){}
 try{
  var tmpBank =  $("field_05_28").value;
  var tmpAcc =  $("field_05_23").value;

  sql = "select org_name V1,org_id V2 from teflow_company ";
  xmlObj = selectFromSQL(sql,2);    
  var select = $("field_05_26");
  if(xmlObj){
    if (select==null||select.type != "select-one")  return;
    clearSelect(select);
    select.appendChild(createEmptyElement(""));
    var items = xmlObj.getElementsByTagName("item");

    for (var i = 0; i < items.length; i++) {
      select.appendChild(createOptionElement(items[i], "V2", "V1"));
    }
  }

  setTimeout(function(){ 
  if($("curformId").value.substring(0,2)=="1."){
    select.value = $("company_id").value;
  } else if($("curformId").value.substring(0,2)=="2."){
    select.value = $("flow_org_id").value;    
  }
   if(select.value=='Z07003') select.value='Z07002';
   selBankComp(select.value);
   if(tmpBank!=""){
     $("field_05_22").value = "";
     $("field_05_28").value = tmpBank;
     $("field_05_23").value = tmpAcc;
  }
  }, 2000);  

  }catch(e){}
}





//-----------------------------------------------------------
function sel_category_2(objValue, sectionId) {
  var teamCode = $('team_code').value;
  var subCategoryId = objValue.substring(0, objValue.indexOf("-"));
  // alert("subCategoryId: " + subCategoryId);
  var sql = "select full_year_budget, adjust_full_year_budget, ytd_budget, ytd_actual_expense, balance from teflow_finance_budget where sub_cat_id = '"
      + subCategoryId
      + "' and org_id = '"+$('company_id').value+"' and department_id = '"
      + teamCode
      + "' and category_id = '"
      + formCategoryId + "'";
  var xmlObj = selectFromSQL(sql, 1);
  if (xmlObj) {
    var options = xmlObj.getElementsByTagName("result");
    // alert(options[0].getAttribute("value")+","+options[1].getAttribute("value")+","+options[2].getAttribute("value"))
    var value = "";
    if (options && options.length) {
      for ( var i = 0; i < options.length; i++) {
        if (options[i].text == "" || options[i].text == "null") {
          value = "0";
        } else {
          value = options[i].text;
        }
        if (options[i].getAttribute("value") == "BALANCE") {
          $('field_' + sectionId + '_1').value = formatNumber(value,"#,##0.00");
        }
      }

    } else {
      $('field_' + sectionId + '_1').value = "0.00";
    }
  }
}
// ----------------------------------


function selBankComp(orgid){
  sql = "SELECT bank_name V1,bank_code %2B '_' %2B account_code  %2B '_' %2B sun_code %2B '  ' %2B ISNULL(b.name ,'') %2B '_' %2B  ISNULL(c.code ,'') %2B '  ' %2B ISNULL(c.name ,'') V2 FROM teflow_bank a LEFT JOIN teflow_payment_sunac_code b ON (a.sun_code = b.code ) LEFT JOIN teflow_payment_t6_code c ON (a.city = c.name) WHERE a.org_id='"+orgid+"' ORDER BY is_default desc";
  xmlObj = selectFromSQL(sql,2);
  if(xmlObj){
    var select = $("field_05_22");
    clearSelect(select);
    //select.appendChild(createEmptyElement(""));
    var items = xmlObj.getElementsByTagName("item");
    if (items == null)  return;

    for (var i = 0; i < items.length; i++) {
      select.appendChild(createOptionElement(items[i], "V2", "V1"));
    }
  }
//setTimeout(function(){ selBank(select); }, 300);
selBank(select);
}

function selBank(select){
  if(select.value !=  ""){
    $("field_05_23").value=select[select.selectedIndex].value.split('_')[1];  
    $("field_05_28").value=select[select.selectedIndex].text;  
  } else {
    $("field_05_23").value=""
    $("field_05_28").value="";  
  }
}

function replacenull(options,val,repl){
    var tmp=null;
    try{
      tmp=options[0].getElementsByTagName(val)[0].childNodes[0].nodeValue;
      return tmp;
    }catch(e){
      tmp=repl;
    }
    return tmp;
}

function changeCurrency(ob,to,date){
  var i = ob.rowIndex;
  var curr = ob[ob.selectedIndex].text;
  var strDate = null;
  if(date != null){
    strDate="'"+document.getElementsByName(date)[i].value+"'";
  } else {
    strDate="GETDATE()";
  }
  
  if(curr=='RMB'){
         document.getElementsByName(to)[i].value = '1';
         return;
  }
    sql = "select ex_rate*100000 from teflow_exchange_rate where rtrim(currency_code)='"+curr +"' and ex_month=month("+strDate+") and ex_year=year("+strDate+")";
  xmlObj = selectFromSQL(sql, 1);

  if(xmlObj){
    var options = xmlObj.getElementsByTagName("result");
    if(options && options.length){
         document.getElementsByName(to)[i].value = formatNumber(parseFloat(options[0].text)/100000,"#,##0.000000");
          }else{
             alert("当前汇率未设定");
             document.getElementsByName(to)[i].value = 1;
      }
  }
}

function  changeShare(ob){
  var tbl = document.getElementsByName("field_06_12");
  var select = $("field_05_22");
  if(ob.value=='02'){
    createTableSectionRow("formTable06","06","14");
    var i = tbl.length-1;
    document.getElementsByName("field_06_8")[i].value=$("field_05_1").value;
    document.getElementsByName("field_06_1")[i].value='2';
    document.getElementsByName("field_06_11")[i].value=select[select.selectedIndex].value.split('_')[2];
    changeSunac(document.getElementsByName("field_06_11")[i]);
   
    tbl[i].value=select[select.selectedIndex].value.split('_')[3];  
    createTableSectionRow("formTable06","06","14");
    var i = tbl.length-1;
    document.getElementsByName("field_06_8")[i].value=$("field_05_1").value;
    document.getElementsByName("field_06_1")[i].value='1';    
    document.getElementsByName("field_06_11")[i].value=$("field_08_4").tooltipText.split(',')[1];
    changeSunac(document.getElementsByName("field_06_11")[i]);
    tbl[i].value=tbl[i-1].value;
    bindEvent(i-1);
    bindEvent(i);
    changeDRCR();    
  }
}
function checkSuncode(ob){
 var idx=ob.rowIndex;
 var re = new RegExp("^" + document.getElementsByName("field_06_11")[idx].value + "", "i");
 for(var i=0; i<jsAutoInstance._msg.length; i++) {
  if(re.test(jsAutoInstance._msg[i]))   return;
 }
 alert("Sun code not exist!");
 document.getElementsByName("field_06_11")[idx].focus();
}

function checkT2code(ob){
return;
 var idx=ob.rowIndex;
 var re = new RegExp("^" + document.getElementsByName("field_06_3")[idx].value + "", "i");
 for(var i=0; i<jsAutoInstance._msg.length; i++) {
  if(re.test(jsAutoInstance._msg[i]))   return;
 }
 alert("T2 code not exist!");
 document.getElementsByName("field_06_3")[idx].focus();
}

function checkDBcode(ob){
return;
 var idx=ob.rowIndex;
  var sql = "select * from teflow_payment_t6_code where code='"+document.getElementsByName("field_06_12")[idx].value .split(' ')[0] +"'";
  xmlObj = selectFromSQL(sql, 1);
  if(xmlObj){
    return;
  }
 alert("DB code not exist!");
 document.getElementsByName("field_06_12")[idx].focus();
}


function checkTcode(){
  for(i=0;i<document.getElementsByName("field_06_11").length;i++){
     var suncode=document.getElementsByName("field_06_11")[i].value .split(' ')[0] ; 
     if (suncode!=="") {
         var sql = "select code option_label, name option_value from teflow_payment_sunac_code where code='"+suncode+"'";
         xmlObj = selectFromSQL(sql, 2);
         if(xmlObj){
                var items = xmlObj.getElementsByTagName("item");
                if (items.length==0)  {
                     alert("错误的会计科目!");
                     document.getElementsByName("field_06_11")[i].focus();
                     return;
                } 
          }
     }

     var t2code=document.getElementsByName("field_06_3")[i].value .split(' ')[0] ; 
     if (t2code!=="") {
         var sql = "select code option_label, name option_value from teflow_payment_t2_code where code='"+t2code+"'";
         xmlObj = selectFromSQL(sql, 2);
         if(xmlObj){
                var items = xmlObj.getElementsByTagName("item");
                if (items.length==0)  {
                     alert("错误的T2 Code!");
                     document.getElementsByName("field_06_3")[i].focus();
                     return;
                } 
          }
     }

     var t6code=document.getElementsByName("field_06_12")[i].value .split(' ')[0] ; 
     if (t6code!=="") {
         var sql = "select code option_label, name option_value from teflow_payment_t6_code where code='"+t6code+"'";
         xmlObj = selectFromSQL(sql, 2);
         if(xmlObj){
                var items = xmlObj.getElementsByTagName("item");
                if (items.length==0)  {
                     alert("错误的DB Code!");
                     document.getElementsByName("field_06_12")[i].focus();
                     return;
                } 
          }
     }

  }
}


function reBindTCEvent(){
 var tbl = document.getElementsByName("field_06_11");
 if(tbl.length) {
   for (i = 0; i < tbl.length; i++) {
     bindEvent(i);
   }
 }
}


function changeCurrencyByDate(ob,to,obj){
      var i = obj.rowIndex;
                      var curr = document.getElementsByName(ob)[i][document.getElementsByName(ob)[i].selectedIndex].text;
      var strDate = null;
      if(obj!= null){
        strDate="'"+obj.value+"'";
      } else {
        strDate="GETDATE()";
      }
      
      if(curr=='RMB'){
             document.getElementsByName(to)[i].value = '1';
             return;
      }
      sql = "select ex_rate*100000 from teflow_exchange_rate where rtrim(currency_code)='"+curr +"' and ex_month=month("+strDate+") and ex_year=year("+strDate+")";
      xmlObj = selectFromSQL(sql, 1);

      if(xmlObj){
        var options = xmlObj.getElementsByTagName("result");
        if(options && options.length){
             document.getElementsByName(to)[i].value = formatNumber(parseFloat(options[0].text)/100000,"#,##0.000000");
              }else{
             alert("当前汇率未设定");
             document.getElementsByName(to)[i].value = 1;
      }
     }
}


afterSetDayFlag = 0;
//override calendar.js closeLayer, the calendar.js setday method will cause the onchange method invalid; Robert 2010-01-15 
closeLayer = function(){
  document.all.meizzDateLayer.style.display="none";
try{  if(afterSetDay){
    afterSetDay(outObject);
  }
}catch(e){}
}

function notifyEpaymentApplicant(){
  alert("当您的申请获得部门主管的批准后，\n请尽早将报销单据（打印的系统报销申请及其附件）传至财务部，\n财务部将于收到您送达的单据之日起开始审批。");
}
function notifyEvendorApplicant(){
  alert("Please check if approved budget has been attached.\nPlease check if vendor’s quotation documents have been attached.\nPlease check if approved budget, tender documents have been attached.\nPlease check if TOF, TEF and vendor’s quotation documents have been attached.\nPlease check if approved budget, memo have been attached.");
}
function alignCNName(cnName){
  if(cnName == null || cnName == '') return '';
  var i = cnName.length;
  for (j=0; j<4-i; j++){
    cnName += " ";
  }
  return cnName ;
}

function checkCAForm(obj){
 if(obj.selectedIndex==0)   return true;

 var strLabel = obj[obj.selectedIndex].text.split("|");
 if( strLabel[3].trim() == '04'){
    return true;
  }else{
    alert("借款Form[" + strLabel[0].trim() + "] 还没有审批完成!");
    return false;
  }
}

function forceSingle(ob){
  var pos;
  if(ob.innerHTML!="" && ob.innerText.indexOf(",")>0 ){
    pos=ob.innerHTML.indexOf("<\/A>");
    ob.innerHTML= ob.innerHTML.substring(0, pos+4);
    alert("此处只能选取一个表单，现为您保留第一个");
  }
}

cn_name_color = "rgb(0,0,255)";

function changeflow_company(){
   if ($("flow_org_id").value!==$("company_id").value){
              alert(" 注意：此申请单的适用流程将改变，将改走其他公司流程。请确认真的要改变吗？");
   }
}

//////////////////////////////////////////////////

//*-----------
   //邀请专家 
var win;
function inviteExpert(){
     var url = document.all['requestUrl'].value+"/wkfProcessAction.it?method=enterInviteExpert&requestNo="+document.all['requestNo'].value;
     openCenterWindow(url,490,560,'Test');
     win=window.open("",'Test');

     setInterval(function(){checkAndClose();},500); 
 
}

function checkAndClose( ){
if(!win.closed&& win.document.body.innerText=='Success'){
win.close(); 
alert("Success");
location.reload();
}
}