/*******************************************************************************
 * Global Functions
 ******************************************************************************/

// 1-new, 2-update, 3-deal, 4-view, 5-view_reject, 6-print, 7-advise, 0-unknown
var process = 1;

function test() {
    alert("test func");
};

function InitProcess() {
    var operateType = jQuery("[name='operateType']");
    var previewBtn = jQuery("[name='previewBtn']");
    var saveType = jQuery("[name='saveType']");
    var saveBtn = jQuery("[name='saveBtn']");
    var viewFlowBtn = jQuery("[name='viewFlowBtn']");

    if (operateType.val() == "00") {
        process = 1;
    } else if (previewBtn.length > 0) {
        process = 6;
    } else if (operateType.val() == "view" && saveType && saveType.val() == "update") {
        if (saveBtn.length > 0) {
            process = 2;
        } else if (viewFlowBtn.length > 0) {
            process = 5;
        }
    } else if (operateType.val() == "advise") {
        process = 7;
    } else if (operateType.val() == "deal") {
        process = 3;
    } else {
        process = 4;
    }
};

function setSpecialFieldRequired(fieldName, fieldLabal, fieldType) {

    var obj = jQuery("[name='" + fieldName + "']");

    function validateSpecialField() {

        if (obj.prop("required") == "true" && obj.val() == "") {
            alert(please_input + fieldLabal);
            var objFocus = obj;
            if (fieldType == "RefForm") {
                objFocus = obj.prev();
            }
            objFocus.focus();
            return false;
        }
        return true;
    }

    validateForm = (function() {
        var cached_function = validateForm;

        return function() {

            if (!validateSpecialField()) {
                return false;
            }

            return cached_function.apply(this, arguments);
        };
    }());
};

function setDefaultTest(fieldName, textStr) {

    var obj = jQuery("[name='" + fieldName + "']");
    var text = textStr;

    function removeText() {

        if (obj.val() == text) {
            obj.css({
                'color' : '#333333',
                'font-style' : 'normal'
            });
            obj.val("");
        }
    }
    ;

    function addText() {

        if (obj.val() == "") {
            obj.css({
                'color' : '#a1a1a1',
                'font-style' : 'italic'
            });
            obj.val(text);
        }
    }
    ;

    obj.focus(function() {
        removeText();
    });

    obj.blur(function() {
        addText();
    });

    saveForm = (function() {
        var cached_function = saveForm;

        return function() {
            removeText();
            cached_function.apply(this, arguments);
        };
    }());

    addText();
};

function setCheckBoxSingleSelect(fieldName) {

    var obj = jQuery("[name='" + fieldName + "']");

    obj.change(function() {
        var group = ":checkbox[name='" + jQuery(this).attr("name") + "']";
        if (jQuery(this).is(':checked')) {
            jQuery(group).not(jQuery(this)).attr("checked", false);
        }
    });
};

function fieldDisable(obj, setRequired, resetVal) {

    obj.prop("disabled", true);
    if (setRequired) {
        obj.prop("required", "false");
    }
    if (resetVal) {
        obj.val("");
    }
};

function fieldEnable(obj, setRequired) {

    obj.prop("disabled", false);
    if (setRequired) {
        obj.prop("required", "true");
    }
};

function checkBoxDisable(obj, setRequired) {

    for (i = 0; i < obj.size(); i++) {
        obj.eq(i).prop("checked", false);
    }
    obj.prop("disabled", true);
    if (setRequired) {
        obj.prop("required", "false");
    }
};

function setEnableBaseOnSelect(enableFieldName, triggerFieldName, selectIndexArray, setRequired) {

    var tObj = jQuery("[name='" + triggerFieldName + "']");
    var tType = tObj.attr("type");
    var eObj = jQuery("[name='" + enableFieldName + "']");
    var eType = eObj.attr("type");

    var funcIsSelect;
    var funcEnable;
    var funcDisable;

    if (tType == "checkbox") { // The trigger is checkbox

        funcIsSelect = function() {

            for (i = 0; i < selectIndexArray.length; i++) {
                var selectIndex = selectIndexArray[i];
                if (tObj.eq(selectIndex).prop("checked")) {
                    return true;
                }
            }
            return false;
        };
    } else {

        funcIsSelect = function() { // The trigger is selection

            for (i = 0; i < selectIndexArray.length; i++) {
                var selectIndex = selectIndexArray[i];
                if (tObj.val() == selectIndex) {
                    return true;
                }
            }
            return false;
        };
    }

    funcEnable = function() {
        fieldEnable(eObj, setRequired);
    };

    if (eType == "checkbox") { // Need enable field is check box

        funcDisable = function() {
            checkBoxDisable(eObj, setRequired);
        };
    } else { // Need enable field is text, textarea, or selection

        funcDisable = function() {
            fieldDisable(eObj, setRequired, true);
        };
    }

    function check() {
        if (funcIsSelect()) {
            funcEnable();
        } else {
            funcDisable();
        }
    }
    ;

    tObj.change(function() {
        check();
    });

    check();
};

function setContentBaseOnSelect(contentFieldName, triggerFieldName, selectIndexArray, contentArray, contentDefault) {

    var tObj = jQuery("[name='" + triggerFieldName + "']");
    var tType = tObj.attr("type");
    var cObj = jQuery("[name='" + contentFieldName + "']");

    var funcIsSelect;

    if (tType == "checkbox") { // The trigger is checkbox

        funcIsSelect = function() {

            for (i = 0; i < selectIndexArray.length; i++) {
                var selectIndex = selectIndexArray[i];
                if (tObj.eq(selectIndex).prop("checked")) {
                    return i;
                }
            }
            return -1;
        };
    } else {

        funcIsSelect = function() { // The trigger is selection

            if (tObj.val().length == 0) {
                return -2;
            }

            for (i = 0; i < selectIndexArray.length; i++) {
                var selectIndex = selectIndexArray[i];
                if (tObj.val() == selectIndex) {
                    return i;
                }
            }
            return -1;
        };
    }

    function check() {

        var selectIndex = funcIsSelect();

        if (selectIndex == -2) {
            cObj.html("");
        } else if (selectIndex == -1) {
            if (contentDefault != "") {
                cObj.html(contentDefault);
            }
        } else {
            cObj.html(contentArray[selectIndex]);
        }
    }
    ;

    tObj.change(function() {
        check();
    });

    check();
};

function getSelectionList(sql) {

    var xmlObj;
    try {
        xmlObj = selectFromSQL(sql, 2);
    } catch (e) {
        alert("Failed to get selection list due to DB connection error, please try again!");
        throw e;
    }

    return xmlObj;
};

function setSelectionList(fieldObj, xmlObj) {

    var field = fieldObj;

    if (xmlObj) {
        clearSelect(field);
        field.appendChild(createEmptyElement(""));

        var items = xmlObj.getElementsByTagName("item");
        if (items == null || items.length == 0) {
            return;
        }

        for (var i = 0; i < items.length; i++) {
            field.appendChild(createOptionElement(items[i], "OPTION_VALUE", "OPTION_LABEL"));
        }
    }
};

function disableTableFields(tableID) {
    jQuery("#" + tableID + " td select").prop("disabled", true);
    jQuery("#" + tableID + " td input").prop("disabled", true);
    jQuery("#" + tableID + " td textarea").prop("disabled", true);
};

function disableTableButton(tableID) {

    var nodeObj = jQuery("#" + tableID).parent();
    var BtnAdd = nodeObj.children("[name='addRowBtn']");
    var BtnDel = nodeObj.children("[name='deleteRowBtn']");

    BtnAdd.prop("disabled", true);
    BtnAdd.hide();
    BtnDel.prop("disabled", true);
    BtnDel.hide();
};

function genRowValueStr(nameID) {

    var fieldObj = jQuery("#" + nameID);

    if (fieldObj.length == 0) {
        fieldObj = jQuery("[name='" + nameID + "']");
    }

    var result = "";

    for (var i = 0; i < fieldObj.length; i++) {
        var value = jQuery.trim(fieldObj.eq(i).val());
        value = value.toUpperCase();
        if (value != "" && result.indexOf(value) < 0) {
            result += value + ",";
        }
    }

    if (result.charAt(result.length - 1) == ",") {
        result = result.slice(0, result.length - 1);
    }

    return result;
};

function validationUploadedAttach(nameNew, nameUpd) {

    var iframeAttach = jQuery("[name='" + nameNew + "']");

    if (iframeAttach.length == 0) {
        iframeAttach = jQuery("[name='" + nameUpd + "']");
    }

    var attachID = iframeAttach.contents().find("[name='id']");

    if (attachID.length == 0) {
        return false;
    }

    return true;
};

function hideSection(sectionNo) {

    var sectionObj = jQuery("#imgcon" + sectionNo);

    if (sectionObj.length == 0) {
        sectionObj = jQuery("[name=imgcon'" + sectionNo + "']");
    }

    var hideObj = sectionObj.parent().parent();
    hideObj.hide();
    hideObj.next().hide();
    hideObj.next().next().hide();
};

function printHtml() {
    var htmlStr = jQuery("html").html();
    w = window.open("", "_blank", "k");
    w.document.write(htmlStr);
    window.print();
    w.close();
};