/*******************************************************************************
 * IT-03 (TRF Form)
 ******************************************************************************/

document.onreadystatechange = function() {

    try {

        if (document.readyState == "complete") {
            InitProcess();
            if (process == 1 || process == 2) {
                InitGlobalPmt();
            }
            delNode("loading");
        }

    } catch (e) {
        alert("Fail to load page!");
    }
};

function InitGlobalPmt() {
    hideSection("4");
    hideSection("5");
    hideSection("6");
    hideSection("7");
    hideSection("8");
};

function validationAttachUponSelection() {

    if (!validationUploadedAttach("iAttachementList06", "iAttachementList3")) {

        var msg = "Please upload TRF excel in  <3.TRF Attachment> section!";
        alert(msg);
        // jQuery("#div2 table tr:eq(1) td:eq(1)").focus();
        return false;
    }

    return true;
};

function validationSave() {

};

function validationSubmit() {

    if (document.getElementsByName("ba_list_it").length < 1) {
        alert("Please input the Assigned BA.");
        return false;
    }

    if (!validationAttachUponSelection()) {
        return false;
    }

    return true;
};