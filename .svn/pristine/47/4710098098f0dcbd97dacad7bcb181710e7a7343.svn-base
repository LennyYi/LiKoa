////////////////////////////////////////////////GD Purchasing Form Functions////////////////////////////////////////////////////////////////////////////
function getAmount(unitPrice, quantity) {
    var Amount;

    Amount = unitPrice * quantity;

    return (Amount);
}

function sumAmount() {
    var Amount, PL
    var sumAmount = 0, sumPL = 0;
    var tbl=document.getElementsByName("field_03_4");
    if (tbl != null) {
        for (i = 1; i <= tbl.length; i++) {
            Amount = PL = 0;
            if (document.getElementsByName("field_03_4")[i - 1].value != "")
                Amount = parseFloat(document.getElementsByName("field_03_4")[i - 1].value);
            if (document.getElementsByName("field_03_5")[i - 1].value != "")
                PL = parseFloat(document.getElementsByName("field_03_5")[i - 1].value);
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
        unitPrice = parseFloat(document.getElementsByName(objname + "_2")[index].value);
    }

    if (document.getElementsByName(objname + "_3")[index].value == "") {
        quantity = 0;
    } else {
        quantity = parseFloat(document.getElementsByName(objname + "_3")[index].value);
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
        g1 = 0
    else
        g1 = parseFloat($("field_05_1").value);

    if ($("field_05_3").value == "")
        g2 = 0
    else
        g2 = parseFloat($("field_05_3").value);

    if ($("field_05_5").value == "")
        g3 = 0
    else
        g3 = parseFloat($("field_05_5").value);

    if ($("field_05_7").value == "")
        g4 = 0
    else
        g4 = parseFloat($("field_05_7").value);

    if ($("field_05_9").value == "")
        g5 = 0
    else
        g5 = parseFloat($("field_05_9").value);

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
        G9 = 0
    else
        G9 = parseFloat($("field_02_5").value);

    if ($("field_02_7").value == "")
        G10 = 0
    else
        G10 = parseFloat($("field_02_7").value)

    if ($("field_03_1").value == "")
        E17 = 0
    else
        E17 = parseFloat($("field_03_1").value)

    if ($("field_02_6").value == "")
        C10 = 0
    else
        C10 = parseFloat($("field_02_6").value)

    if (G9 == 0) {
        $("field_03_3").value = "0";
        E19 = 0;
    } else {
        $("field_03_3").value = E17 * (G9 * 12 - G10) / (G9 * 12)
        $("field_03_3").value = getNum($("field_03_3").value, 2)
        E19 = parseFloat($('field_03_3').value);
    }

    if ($('field_03_2').value == "")
        E18 = 0
    else
        E18 = parseFloat($('field_03_2').value);

    $("field_03_4").value = getNum((E18 + E19) * 12 * G9, 2)

    var G19 = (E18 + E19) * (12 - getMonth($("field_02_8").value))
    $('field_03_5').value = getNum(G19, 2);

    $('field_03_6').value = getNum((E18 + E19) * 12, 2);

    var F21 = $("field_04_1").value
    if (F21 == "")
        F21 = 0
    else
        F21 = parseFloat(F21)

    $("field_04_2").value = getNum(F21 * C10 / (G9 * 12), 2)
    $("field_04_3").value = getNum(F21 / G9, 2)

    var F22 = $("field_05_1").value
    if (F22 == "")
        F22 = 0
    else
        F22 = parseFloat(F22)

    $("field_05_2").value = getNum(F22 * C10 / (G9 * 12), 2)
    $("field_05_3").value = getNum(F22 / G9, 2)

    var F23 = $("field_06_1").value
    if (F23 == "")
        F23 = 0
    else
        F23 = parseFloat(F23)

    $("field_06_2").value = getNum(F23 * 0.95 * (C10 - 1) / (5 * 12), 2)
    $("field_06_3").value = getNum(F23 * 0.95 / 5, 2)

    $("field_07_1").value = getNum(addition("field_03_4", "field_04_1", "field_05_1", "field_06_1", "field_10_1"), 2)
    $("field_07_2").value = getNum(addition("field_03_5", "field_04_2", "field_05_2", "field_06_2", "field_10_1"), 2)
    $("field_07_3").value = getNum(addition("field_03_6", "field_04_3", "field_05_3", "field_06_3", "field_10_1"), 2)
}

function summaryForAutomobile() {
    var iObj = document.frames['iframeList'];
    var B14 = $("field_04_3").value;
    if (B14 == "")
        B14 = 0
    else
        B14 = parseFloat(B14)

    var F14 = $("field_04_4").value;
    if (F14 == "")
        F14 = 0
    else
        F14 = parseFloat(F14)

    var B15 = $("field_04_5").value
    if (B15 == "")
        B15 = 0
    else
        B15 = parseFloat(B15)

    var F15 = $("field_04_6").value;
    if (F15 == "")
        F15 = 0
    else
        F15 = parseFloat(F15)

    var B16 = $("field_04_7").value;
    if (B16 == "")
        B16 = 0
    else
        B16 = parseFloat(B16)

    var F16 = $("field_04_8").value;
    if (F16 == "")
        F16 = 0
    else
        F16 = parseFloat(F16)

    var F17 = $("field_04_10").value;
    if (F17 == "")
        F17 = 0
    else
        F17 = parseFloat(F17)

    var C20 = $("field_04_11").value
    if (C20 == "")
        C20 = 0
    else
        C20 = parseFloat(C20 / 100)

    var C21 = $("field_04_12").value // Salary Increase(%)
    if (C21 == "")
        C21 = 0
    else
        C21 = parseFloat(C21 / 100)

    var c23 = B14 * 12;
    iObj.document.all['c23'].value = formatNumber(c23, '#,###')
    var d23 = c23
    iObj.document.all['d23'].value = iObj.document.all['c23'].value
    var e23 = B14 * 12 * (1 + C20);
    iObj.document.all['e23'].value = formatNumber(e23, '#,###')
    var f23 = e23
    iObj.document.all['f23'].value = iObj.document.all['e23'].value
    var g23 = B14 * 12 * (1 + C20) * (1 + C20)
    iObj.document.all['g23'].value = formatNumber(g23, '#,###')
    var h23 = parseFloat(c23) + parseFloat(d23) + parseFloat(e23) + parseFloat(f23) + parseFloat(g23);
    iObj.document.all['h23'].value = formatNumber(h23, '#,###');

    var c24 = B15 * 12;
    iObj.document.all['c24'].value = formatNumber(c24, '#,###')
    var d24 = c24
    iObj.document.all['d24'].value = iObj.document.all['c24'].value
    var e24 = B15 * 12 * (1 + C20)
    iObj.document.all['e24'].value = formatNumber(e24, '#,###')
    var f24 = e24
    iObj.document.all['f24'].value = iObj.document.all['e24'].value
    var g24 = B15 * 12 * (1 + C20) * (1 + C20);
    iObj.document.all['g24'].value = formatNumber(g24, '#,###')
    var h24 = parseFloat(c24) + parseFloat(d24) + parseFloat(e24) + parseFloat(f24) + parseFloat(g24)
    iObj.document.all['h24'].value = formatNumber(h24, '#,###');

    var c25 = B16 * 12;
    iObj.document.all['c25'].value = formatNumber(c25, '#,###')
    var d25 = parseFloat(c25) * (1 + C21)
    iObj.document.all['d25'].value = formatNumber(d25, '#,###')
    var e25 = parseFloat(d25) * (1 + C21)
    iObj.document.all['e25'].value = formatNumber(e25, '#,###')
    var f25 = parseFloat(e25) * (1 + C21)
    iObj.document.all['f25'].value = formatNumber(f25, '#,###')
    var g25 = parseFloat(f25) * (1 + C21)
    iObj.document.all['g25'].value = formatNumber(g25, '#,###')
    var h25 = c25 + d25 + e25 + f25 + g25
    iObj.document.all['h25'].value = formatNumber(h25, '#,###');

    var c26 = c23 + c24 + c25
    iObj.document.all['c26'].value = formatNumber(c26, '#,###')
    var d26 = d23 + d24 + d25
    iObj.document.all['d26'].value = formatNumber(d26, '#,###')
    var e26 = e23 + e24 + e25
    iObj.document.all['e26'].value = formatNumber(e26, '#,###')
    var f26 = f23 + f24 + f25
    iObj.document.all['f26'].value = formatNumber(f26, '#,###')
    var g26 = g23 + g24 + g25
    iObj.document.all['g26'].value = formatNumber(g26, '#,###')
    var h26 = h23 + h24 + h25
    iObj.document.all['h26_1'].value = formatNumber(h26, '#,###')
    iObj.document.all['h26'].value = h26

    var c27 = c26 / (1 + C20);
    iObj.document.all['c27'].value = formatNumber(c27, '#,###')
    var d27 = d26 / ((1 + C20) * (1 + C20));
    iObj.document.all['d27'].value = formatNumber(d27, '#,###')
    var e27 = e26 / ((1 + C20) * (1 + C20) * (1 + C20))
    iObj.document.all['e27'].value = formatNumber(e27, '#,###')
    var f27 = f26 / ((1 + C20) * (1 + C20) * (1 + C20) * (1 + C20))
    iObj.document.all['f27'].value = formatNumber(f27, '#,###')
    var g27 = g26 / ((1 + C20) * (1 + C20) * (1 + C20) * (1 + C20) * (1 + C20))
    iObj.document.all['g27'].value = formatNumber(g27, '#,###')
    var h27 = c27 + d27 + e27 + f27 + g27;
    iObj.document.all['h27_1'].value = formatNumber(h27, '#,###')
    iObj.document.all['h27'].value = h27

    var h35 = iObj.document.all['h35'].value
    var h36 = iObj.document.all['h36'].value

    if (h35 == "")
        h35 = 0
    else
        h35 = parseFloat(h35)

    if (h36 == "")
        h36 = 0
    else
        h36 = parseFloat(h36)

    iObj.document.all['b39'].value = formatNumber(h26 - h35, '#,###')
    iObj.document.all['b40'].value = formatNumber(h27 - h36, '#,###')

    if ((h27 - h36) > 0)
        iObj.document.all['resultValue'].innerHTML = "<b>Analysis indicates car purchase generates lesser cash outlay<b>"
    else if ((h27 - h36) <= 0)
        iObj.document.all['resultValue'].innerHTML = "<b>Analysis indicates leasehold/rental generates lesser cash outlay<b>"
    else
        iObj.document.all['resultValue'].innerHTML = ""
}

function summaryAutomobilePurchase() {
    var iObj = document.frames['iframeList'];

    var F14 = $("field_04_4").value;
    if (F14 == "")
        F14 = 0
    else
        F14 = parseFloat(F14)

    var F15 = $("field_04_6").value;
    if (F15 == "")
        F15 = 0
    else
        F15 = parseFloat(F15)

    var F16 = $("field_04_10").value;
    if (F16 == "")
        F16 = 0
    else
        F16 = parseFloat(F16)

    var F17 = $("field_04_8").value;
    if (F17 == "")
        F17 = 0
    else
        F17 = parseFloat(F17)

    var C20 = $("field_04_11").value // PV @ (%)
    if (C20 == "")
        C20 = 0
    else
        C20 = parseFloat(C20 / 100)

    var C21 = $("field_04_12").value // Salary Increase(%)
    if (C21 == "")
        C21 = 0
    else
        C21 = parseFloat(C21 / 100)

    var C22 = $("field_04_13").value // Salary Increase(%)
    if (C22 == "")
        C22 = 0
    else
        C22 = parseFloat(C22 / 100)

    var b30 = F14
    iObj.document.all['b30'].value = formatNumber(b30, '#,###')
    var h30 = F14
    iObj.document.all['h30'].value = formatNumber(h30, '#,###')

    var c31 = F15 * 12
    iObj.document.all['c31'].value = formatNumber(c31, '#,###')
    var d31 = c31
    iObj.document.all['d31'].value = iObj.document.all['c31'].value
    var e31 = F15 * 12 * (1 + C20)
    iObj.document.all['e31'].value = formatNumber(e31, '#,###')
    var f31 = e31
    iObj.document.all['f31'].value = iObj.document.all['e31'].value
    var g31 = F15 * 12 * (1 + C20) * (1 + C20)
    iObj.document.all['g31'].value = formatNumber(g31, '#,###')
    var h31 = c31 + d31 + e31 + f31 + g31
    iObj.document.all['h31'].value = formatNumber(h31, '#,###');

    var c32 = F14 * (C22 * 2)
    iObj.document.all['c32'].value = formatNumber(c32, '#,###')
    var d32 = F14 * (C22 * 3)
    iObj.document.all['d32'].value = formatNumber(d32, '#,###')
    var e32 = F14 * C22 * 4
    iObj.document.all['e32'].value = formatNumber(e32, '#,###')
    var f32 = F14 * C22 * 5
    iObj.document.all['f32'].value = formatNumber(f32, '#,###')
    var g32 = F14 * C22 * 6
    iObj.document.all['g32'].value = formatNumber(g32, '#,###')
    var h32 = c32 + d32 + e32 + f32 + g32
    iObj.document.all['h32'].value = formatNumber(h32, '#,###');

    var c33 = F16
    iObj.document.all['c33'].value = formatNumber(c33, '#,###')
    var d33 = c33
    iObj.document.all['d33'].value = iObj.document.all['c33'].value
    var e33 = d33
    iObj.document.all['e33'].value = iObj.document.all['d33'].value
    var f33 = e33
    iObj.document.all['f33'].value = iObj.document.all['e33'].value
    var g33 = f33
    iObj.document.all['g33'].value = iObj.document.all['f33'].value
    var h33 = c33 + d33 + e33 + f33 + g33
    iObj.document.all['h33'].value = formatNumber(h33, '#,###');

    var c34 = F17 * 12
    iObj.document.all['c34'].value = formatNumber(c34, '#,###')
    var d34 = c34 * (1 + C21)
    iObj.document.all['d34'].value = formatNumber(d34, '#,###')
    var e34 = d34 * (1 + C21)
    iObj.document.all['e34'].value = formatNumber(e34, '#,###')
    var f34 = e34 * (1 + C21)
    iObj.document.all['f34'].value = formatNumber(f34, '#,###')
    var g34 = f34 * (1 + C21)
    iObj.document.all['g34'].value = formatNumber(g34, '#,###')
    var h34 = c34 + d34 + e34 + f34 + g34
    iObj.document.all['h34'].value = formatNumber(h34, '#,###');

    var b35 = F14
    iObj.document.all['b35'].value = formatNumber(b35, '#,###')
    var c35 = c31 + c32 + c33 + c34
    iObj.document.all['c35'].value = formatNumber(c35, '#,###');
    var d35 = d31 + d32 + d33 + d34
    iObj.document.all['d35'].value = formatNumber(d35, '#,###');
    var e35 = e31 + e32 + e33 + e34
    iObj.document.all['e35'].value = formatNumber(e35, '#,###');
    var f35 = f31 + f32 + f33 + f34
    iObj.document.all['f35'].value = formatNumber(f35, '#,###');
    var g35 = g31 + g32 + g33 + g34
    iObj.document.all['g35'].value = formatNumber(g35, '#,###');
    var h35 = h30 + h31 + h32 + h33 + h34
    iObj.document.all['h35_1'].value = formatNumber(h35, '#,###');
    iObj.document.all['h35'].value = h35

    var b36 = b35
    iObj.document.all['b36'].value = formatNumber(b36, '#,###')
    var c36 = c35 / (1 + C20)
    iObj.document.all['c36'].value = formatNumber(c36, '#,###');
    var d36 = d35 / ((1 + C20) * (1 + C20))
    iObj.document.all['d36'].value = formatNumber(d36, '#,###');
    var e36 = e35 / ((1 + C20) * (1 + C20) * (1 + C20))
    iObj.document.all['e36'].value = formatNumber(e36, '#,###');
    var f36 = f35 / ((1 + C20) * (1 + C20) * (1 + C20) * (1 + C20))
    iObj.document.all['f36'].value = formatNumber(f36, '#,###');
    var g36 = g35 / ((1 + C20) * (1 + C20) * (1 + C20) * (1 + C20) * (1 + C20))
    iObj.document.all['g36'].value = formatNumber(g36, '#,###');
    var h36 = b36 + c36 + d36 + e36 + f36 + g36
    iObj.document.all['h36_1'].value = formatNumber(h36, '#,###');
    iObj.document.all['h36'].value = h36

    //
    var h26 = iObj.document.all['h26'].value
    var h35 = iObj.document.all['h35'].value
    var h27 = iObj.document.all['h27'].value
    var h36 = iObj.document.all['h36'].value

    if (h26 == "")
        h26 = 0
    else
        h26 = parseFloat(h26)
    if (h35 == "")
        h35 = 0
    else
        h35 = parseFloat(h35)
    if (h27 == "")
        h27 = 0
    else
        h27 = parseFloat(h27)
    if (h36 == "")
        h36 = 0
    else
        h36 = parseFloat(h36)

    iObj.document.all['b39'].value = formatNumber(h26 - h35, '#,###')
    iObj.document.all['b40'].value = formatNumber(h27 - h36, '#,###')

    if ((h27 - h36) > 0)
        iObj.document.all['resultValue'].innerHTML = "<b>Analysis indicates car purchase generates lesser cash outlay<b>"
    else if ((h27 - h36) <= 0)
        iObj.document.all['resultValue'].innerHTML = "<b>Analysis indicates leasehold/rental generates lesser cash outlay<b>"
    else
        iObj.document.all['resultValue'].innerHTML = ""
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
    var sql = "select distinct account_dc option_label, account_dc option_value from teflow_finance_budget where org_id = '@orgId' and department_id = '"
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
    var sql = "select sub_cat_name option_label, category_id %2B '_' %2B sub_cat_id %2B '-' %2B sub_cat_name option_value from teflow_finance_budget where org_id = '@orgId' and department_id = '"
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
    initCategory();
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
    var sql = "select code option_label,code %2B'  '%2B name option_value from teflow_payment_sunac_code";
    var xmlObj = selectFromSQL(sql, 2);
    if (xmlObj) {
      var items = xmlObj.getElementsByTagName("item");
      if(items){
      for(var j=0; j<items.length;j++){
        jsAutoInstance.item(items[j].getElementsByTagName("OPTION_VALUE")[0].childNodes[0].nodeValue);
      }}
    }
    sql = "select code option_label,code %2B'  '%2B name option_value from teflow_payment_t2_code ";
    xmlObj = selectFromSQL(sql, 2);
    if (xmlObj) {
      var items = xmlObj.getElementsByTagName("item");
      if(items){
      for(var j=0; j<items.length;j++){
        jsAutoInstanceT2.item(items[j].getElementsByTagName("OPTION_VALUE")[0].childNodes[0].nodeValue);
      }}
    }   
    sql = "select code option_label,code %2B'  '%2B name option_value from teflow_payment_t6_code";
    xmlObj = selectFromSQL(sql, 2);
    if (xmlObj) {
      var items = xmlObj.getElementsByTagName("item");
      if(items){
      for(var j=0; j<items.length;j++){
        jsAutoInstanceT6.item(items[j].getElementsByTagName("OPTION_VALUE")[0].childNodes[0].nodeValue);
      }}
    }    
  }
}catch(e){}
    
  sql = "select org_name V1,org_id V2 from teflow_company ";
  xmlObj = selectFromSQL(sql,2);
  if(xmlObj){
    var select = $("field_05_26");
    if (select==null||select.type != "select-one")  return;
    clearSelect(select);
    select.appendChild(createEmptyElement(""));
    var items = xmlObj.getElementsByTagName("item");

    for (var i = 0; i < items.length; i++) {
      select.appendChild(createOptionElement(items[i], "V2", "V1"));
    }
  }  
}

function bindEvent(row) {
  var tbl = document.getElementsByName("field_06_11");
  var tblt2 = document.getElementsByName("field_06_3");
  var tblt6 = document.getElementsByName("field_06_12");
  var select = $("field_05_22");

  if ($("approveBtn")) {
      $("approveBtn").onmouseover=function(){checkTcode();changeDRCR();recalT6();$("field_05_22").value="";}
  }

// $("approveBtn").onmouseover=function(){changeDRCR();recalT6();$("field_05_22").value="";}


  if(typeof row=="undefined"){
    if (tbl.length&&oneClick==true) {
      row = tbl.length-1;
      oneClick = false;
    }else{
    return;}
  } else {
    tbl[row].onkeyup=function(){jsAutoInstance.handleEvent(tbl[row],event)};
    tblt2[row].onkeyup=function(){jsAutoInstanceT2.handleEvent(tblt2[row],event)};
    tblt6[row].onkeyup=function(){jsAutoInstanceT6.handleEvent(tblt6[row],event)};
    return;
  }

  tbl[row].onkeyup=function(){jsAutoInstance.handleEvent(tbl[row],event)};
  tblt2[row].onkeyup=function(){jsAutoInstanceT2.handleEvent(tblt2[row],event)};
  tblt6[row].onkeyup=function(){jsAutoInstanceT6.handleEvent(tblt6[row],event)};
  if(row%2==0){
    document.getElementsByName("field_06_1")[row].value='2';
    document.getElementsByName("field_06_7")[row].value=$("field_08_4").tooltipText.split(',')[0];
    if($("field_05_26").selectedIndex>0){
      tbl[row].value=select[select.selectedIndex].value.split('_')[2];  
      tblt6[row].value=select[select.selectedIndex].value.split('_')[3];  
    }
  }
  if(row%2==1){
    document.getElementsByName("field_06_1")[row].value='1';
    document.getElementsByName("field_06_7")[row].value=document.getElementsByName("field_06_7")[row-1].value;
    tbl[row].value=$("field_08_4").tooltipText.split(',')[1];  
    if(tblt6[row].value=="")tblt6[row].value=tblt6[row-1].value;  
  }
  document.getElementsByName("field_06_8")[row].value=$("field_05_1").value;
 
  changeSunac(tbl[row]);
  changeDRCR();
  recalT6();
}

function jsAuto(instanceName,objID)
{
 this._msg = [];
 this._x = null;
 this._o = document.getElementById( objID );
 if (!this._o) return;
 this._f = null;
 this._i = instanceName;
 this._r = null;
 this._c = 0;
 this._s = false;
 this._v = null;
 this._o.style.visibility = "hidden";
 this._o.style.position = "absolute";
 this._o.style.zIndex = "9999";
this._o.style.overflow = "auto";
this._o.style.height = "200";
 return this;
};
jsAuto.prototype.directionKey=function() { with (this)
{
 var e = _e.keyCode ? _e.keyCode : _e.which;
 var l = _o.childNodes.length;
 (_c>l-1 || _c<0) ? _s=false : "";
 if( e==40 && _s )
 {
  _o.childNodes[_c].style.backgroundColor="#ffffff";
  (_c >= l-1) ? _c=0 : _c ++;
  _o.childNodes[_c].style.backgroundColor="#cccccc";
 }
 if( e==38 && _s )
 {
  _o.childNodes[_c].style.backgroundColor="#ffffff";
  _c--<=0 ? _c = _o.childNodes.length-1 : "";
  _o.childNodes[_c].style.backgroundColor="#cccccc";
 }
 if( e==13 )
 {
  if(_o.childNodes[_c] && _o.style.visibility=="visible")
  {
   _r.value = _x[_c];
   _o.style.visibility = "hidden";
  }
 }
 if( !_s )
 {
  _c = 0;
  _o.childNodes[_c].style.backgroundColor="#bbbbbb";
  _s = true;
 }
}};
// mouseEvent.
jsAuto.prototype.domouseover=function(obj) { with (this)
{
 _o.childNodes[_c].style.backgroundColor = "#ffffff";
 _c = 0;
 obj.tagName=="DIV" ? obj.style.backgroundColor="#cccccc" : obj.parentElement.style.backgroundColor="#cccccc";
}};
jsAuto.prototype.domouseout=function(obj)
{
 obj.tagName=="DIV" ? obj.style.backgroundColor="#ffffff" : obj.parentElement.style.backgroundColor="#ffffff";
};
jsAuto.prototype.doclick=function(msg) { with (this)
{
 if(_r)
 {
  _r.value = msg;
  _o.style.visibility = "hidden";
 }
 else
 {
  alert("javascript autocomplete ERROR :nn can not get return object.");
  return;
 }
}};
// object method;
jsAuto.prototype.item=function(msg)
{
 if( msg.indexOf(",")>0 )
 {
  var arrMsg=msg.split(",");
  for(var i=0; i<arrMsg.length; i++)
  {
   arrMsg[i] ? this._msg.push(arrMsg[i]) : "";
  }
 }
 else
 {
  this._msg.push(msg);
 }
 this._msg.sort();
};
jsAuto.prototype.append=function(msg) { with (this)
{
 _i ? "" : _i = eval(_i);
 _x.push(msg);
 var div = document.createElement("DIV");
 //bind event to object.
 div.onmouseover = function(){_i.domouseover(this)};
 div.onmouseout = function(){_i.domouseout(this)};
 div.onclick = function(){_i.doclick(msg)};
 var re  = new RegExp("(" + _v + ")","i");
 div.style.lineHeight="140%";
 div.style.backgroundColor = "#ffffff";
 if (_v) div.innerHTML = msg.replace(re , "<strong>$1</strong>");
 div.style.fontFamily = "verdana";
 _o.appendChild(div);
}};
jsAuto.prototype.display=function() { with(this)
{
 if(_f&&_v!="")
 {
  var x=y=0;
  var p=_r; 
  while(p){
  x+=p.offsetLeft;
  y+=p.offsetTop;
  p=p.offsetParent;
  };
  _o.style.left = x;
  _o.style.width = _r.offsetWidth*2;
  _o.style.top = y + _r.offsetHeight
  _o.style.visibility = "visible";
 }
 else
 {
  _o.style.visibility="hidden";
 }
}};
jsAuto.prototype.handleEvent=function(o,event) { with (this)
{
 var re;
 _e = event;
 var e = _e.keyCode ? _e.keyCode : _e.which;
 _x = [];
 _f = false;
 _r = o;//document.getElementById( fID );
 _v = o.value;//fValue;
 _i = eval(_i);
 re = new RegExp("^" + _v + "", "i");
 _o.innerHTML="";
 for(var i=0; i<_msg.length; i++)
 {
  if(re.test(_msg[i]))
  {
   _i.append(_msg[i]);
   _f = true;
  }
 }
 _i ? _i.display() : alert("can not get instance");
 if(_f)
 {
  if((e==38 || e==40 || e==13))
  {
   _i.directionKey();
  }
  else
  {
   _c=0;
   _o.childNodes[_c].style.backgroundColor = "#cccccc";
   _s=true;
  }
 }
}};
window.onerror=new Function("return true;");
//container div---------------------------------------
var div=document.createElement("div");
div.id="divc";
div.style.border="black 1px solid";
document.body.appendChild(div);
var divt2=document.createElement("div");
divt2.id="divct2";
divt2.style.border="black 1px solid";
document.body.appendChild(divt2);
var divt6=document.createElement("div");
divt6.id="divct6";
divt6.style.border="black 1px solid";
document.body.appendChild(divt6);
//----------------------------------------------------------

var jsAutoInstance = new jsAuto("jsAutoInstance","divc");
var jsAutoInstanceT2 = new jsAuto("jsAutoInstanceT2","divct2");
var jsAutoInstanceT6 = new jsAuto("jsAutoInstanceT6","divct6");

//----------------------------------------------------------

function changeDRCR(){
  var tbl=document.getElementsByName("field_06_8");
  var sign=document.getElementsByName("field_06_1");//DR=1 CR=2

  var sumD=sumC=0;
  for(i=0;i<tbl.length;i++){
    if(sign[i].value=='')return;
    sumD = sumD + parseFloat(tbl[i].value.replace(/,/g,''))*(2-sign[i].value);
    sumC = sumC + parseFloat(tbl[i].value.replace(/,/g,''))*(sign[i].value-1);
  }
  $("field_08_1").value = formatNumber(sumD ,"#,##0.00");
  $("field_08_2").value = formatNumber(sumC ,"#,##0.00");
}

//-----------------------------------------------------------
function sel_category_2(objValue, sectionId) {
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

function recalT6(){
var strMatrix="";       //存?形式?"code_123,code_22.5,...."
for(i=0;i<document.getElementsByName("field_06_8").length;i++){
  var amount=document.getElementsByName("field_06_8")[i].value.replace(/,/g,'');
  var sign=document.getElementsByName("field_06_1")[i].value;//DR=1 CR=2
  var t6=document.getElementsByName("field_06_12")[i].value.split(' ')[0]+"_";

  var newAmount = parseFloat(amount)*(sign*2-3);
  var start = strMatrix.indexOf(t6);

  if(start == -1){
    strMatrix = strMatrix + t6 + newAmount + ",";
  }  else  {
    var oldToken = strMatrix.substring(start, start+20).split(',')[0];
    var oldAmount = parseFloat(oldToken.split('_')[1]);
    var result = parseFloat((oldAmount+ newAmount).toFixed(2));
//alert(oldAmount+" +  "+newAmount);
    if(result == 0|| Math.abs(result)<0.01){
      strMatrix = strMatrix.replace(new RegExp(oldToken + ",",'g') , "");
      //如果?零，??去?个token
    } else {
      strMatrix = strMatrix.replace(new RegExp(oldToken, 'g'), t6 + result );
    }
  }
//alert(strMatrix);
}
if(strMatrix==""){
  $("field_08_3").value="Yes";
} else {
  $("field_08_3").value="";
}
autoItem();
}
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
setTimeout(function(){ selBank(select); }, 500);
}

function selBank(select){
  if(select.length>0){
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
    }
  }
}

function changeSunac(obj) {
    // checkSuncode(obj);
    var elmt = new Array("field_06_9", "field_06_13", "field_06_3", "field_06_5", "field_06_4", "field_06_14", "field_06_12");
    var idx = obj.rowIndex;
    var sql = "select * from teflow_payment_sunac_code where code='" + obj.value.split(' ')[0] + "'";
    var xmlObj = selectFromSQL(sql, 2);
    var items = xmlObj.getElementsByTagName("item");
    for ( var i = 0; i < 7; i++) {
        document.getElementsByName(elmt[i])[idx].disabled = false;
        document.getElementsByName(elmt[i])[idx].required = "true";
    }
    try {
        var codemap = items[0].getElementsByTagName("T0")[0].childNodes[0].nodeValue
                + items[0].getElementsByTagName("T1")[0].childNodes[0].nodeValue
                + items[0].getElementsByTagName("T2")[0].childNodes[0].nodeValue
                + items[0].getElementsByTagName("T3")[0].childNodes[0].nodeValue
                + items[0].getElementsByTagName("T4")[0].childNodes[0].nodeValue
                + items[0].getElementsByTagName("T5")[0].childNodes[0].nodeValue
                + items[0].getElementsByTagName("T6")[0].childNodes[0].nodeValue;

        for ( var i = 0; i < codemap.length; i++) {
            if (codemap.charAt(i) == '0') {
                document.getElementsByName(elmt[i])[idx].disabled = true;
                document.getElementsByName(elmt[i])[idx].required = "false";
                document.getElementsByName(elmt[i])[idx].value = "";
            } else {
                document.getElementsByName(elmt[i])[idx].disabled = false;
                document.getElementsByName(elmt[i])[idx].required = "true";
            }
        }
    } catch (e) {
    }
}

function autoItem(){
  var tbl=document.getElementsByName("field_06_12");
  if(tbl.length==2 && tbl[0].value!=tbl[1].value&&tbl[0].value!=""&&tbl[1].value!=""){
    if(confirm("Add inter-company record ?")){
      createTableSectionRow("formTable06","06","14");
      var i= tbl.length-1;
      document.getElementsByName("field_06_8")[i].value=document.getElementsByName("field_06_8")[i-1].value;
      document.getElementsByName("field_06_1")[i].value='2';
      tbl[i].value=tbl[i-1].value;
      createTableSectionRow("formTable06","06","14");
      i= tbl.length-1;
      document.getElementsByName("field_06_8")[i].value=document.getElementsByName("field_06_8")[i-2].value;
      document.getElementsByName("field_06_1")[i].value='1';
      tbl[i].value=tbl[i-3].value;
      bindEvent(i-1);
      bindEvent(i);
      changeDRCR();
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