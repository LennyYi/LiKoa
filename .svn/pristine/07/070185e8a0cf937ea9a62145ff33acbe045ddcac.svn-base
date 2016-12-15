<%-- 
New Page: IT0958    Robin    11/02/2007    DS-013 Form can export to excel file 
--%>
<%@ page contentType="application/vnd.ms-excel;charset=GBK"%>
<%@ page
    import="java.util.*,com.aiait.eflow.util.*,com.aiait.eflow.wkf.vo.*,com.aiait.eflow.formmanage.vo.*,com.aiait.eflow.common.helper.*"%>
<%@ page import="java.text.*"%>
<%
    Collection resultList = (ArrayList) request.getAttribute("resultList");
    TableDataListVO tableDataList = (TableDataListVO) request.getAttribute("tableDataList");
    FormManageVO form = (FormManageVO) request.getAttribute("form");
    String[] fieldArray = (String[]) request.getAttribute("fieldArray");
    HashMap fieldMap = (HashMap) request.getAttribute("formFieldMap");
%>

<%@page import="java.sql.Date"%>
<%@page import="java.sql.Timestamp"%><html>
<head>
<title>Export form list to excel</title>
<meta http-equiv="Content-Type" content="application/vnd.ms-excel;charset=GBK">
<meta Content-Disposition="attachment; filename=ExportFormList.xls">
</head>
<body>
<table width="100%" border="1" cellspacing="0" cellpading="0">
    <tr height="50">
        <td align="center" colspan="<%=fieldArray.length%>"><strong><font color="blue"><%=form.getFormName()%></font></strong></td>
    </tr>
    <tr>
        <%
            for (int i = 0; i < fieldArray.length; i++) {
        %>
        <td align="center"><strong> <%
     HashMap field = (HashMap) fieldMap.get(fieldArray[i].toUpperCase());
         if (field != null) {
             out.print(DataConvertUtil.convertISOToGBK((String) field.get("FIELD_LABEL")));
             //System.out.println(DataConvertUtil.convertISOToGBK((String)field.get("FIELD_LABEL")));
         } else {
             out.print(fieldArray[i]);
         }
 %> </strong></td>
        <%
            }
        %>
    </tr>
    <%
        //
        if (resultList != null && resultList.size() > 0) {
            Iterator it = resultList.iterator();
            while (it.hasNext()) {
                HashMap map = (HashMap) it.next();
                String requestNo = (String) map.get("REQUEST_NO");
                int line = 1;
                if (tableDataList != null) {
                    line = tableDataList.getMaxLine(requestNo);
                    line = line == 0 ? 1 : line;
                }
                for (int l = 1; l <= line; l++) {
                    out.print("<tr>");
                    for (int i = 0; i < fieldArray.length; i++) {
                        String fieldId = fieldArray[i];
                        String fieldName = fieldId.toUpperCase();
                        HashMap field = (HashMap) fieldMap.get(fieldName);
                        if (map.containsKey(fieldName)) {
                            if (l != 1) {
                                continue;
                            }
                            // System.out.println("Common fieldId: " + fieldId);
                            out.print("<td align=\"left\" rowspan=\"" + line + "\">");
                            if ((String) map.get(fieldName) != null && !"".equals((String) map.get(fieldName))) {
                                if ("REQUEST_STAFF_CODE".equals(fieldName) || "PRJ_LD_ID".equals(fieldName)
                                        || "SUBMIT_STAFF_CODE".equals(fieldName)|| "STAFF_CODE".equals(fieldName)) {
                                    out.print(DataConvertUtil.convertISOToGBK(StaffTeamHelper.getInstance()
                                            .getStaffNameByCode((String) map.get(fieldName))));
                                } else if ("COMPANY_ID".equals(fieldName)) {
                                    out.print(CompanyHelper.getInstance().getOrgName((String) map.get(fieldName)));
                                } else if ("TEAM_CODE".equals(fieldName)) {
                                    out.print(StaffTeamHelper.getInstance().getTeamNameByCode(
                                            (String) map.get(fieldName)));
                                } else if ("REQUEST_DATE".equals(fieldName)) {
                                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    java.util.Date cDate = df.parse((String) map.get(fieldName));
                                    out.print(StringUtil.getDateStr(cDate, "MM/dd/yyyy HH:mm:ss"));
                                } else {
                                    String fieldType = (String) field.get("FIELD_TYPE");
                                    String fieldValue = (String) map.get(fieldName);
                                    if (fieldValue == null) {
                                        fieldValue = "";
                                    }
                                    if ("7".equals(fieldType)) { // systemField
                                        FormSectionFieldVO vo = SystemFieldHelper.getInstance().getSystemFieldById(
                                                fieldArray[i]);
                                        Collection sysOpList = vo.getOptionList();
                                        if (sysOpList != null && sysOpList.size() > 0) {
                                            Iterator it1 = sysOpList.iterator();
                                            while (it1.hasNext()) {
                                                DictionaryDataVO op = (DictionaryDataVO) it1.next();
                                                if (fieldValue.equals(op.getId())) {
                                                    out.print(DataConvertUtil.convertISOToGBK(op.getValue()));
                                                    break;
                                                }
                                            }
                                        } else {
                                            out.print(fieldValue);
                                        }
                                    } else if ("6".equals(fieldType)) { //checkbox
                                        BaseDataHelper dataHelper = BaseDataHelper.getInstance();
                                        Collection optionList = (ArrayList) dataHelper.getDetailMap().get(
                                                (String) field.get("FORM_SYSTEM_ID") + "&"
                                                        + (String) field.get("SECTION_ID") + "&" + fieldArray[i]);
                                        StringBuffer str = new StringBuffer("");
                                        if (optionList != null && optionList.size() > 0) {
                                            Iterator opIt = optionList.iterator();
                                            while (opIt.hasNext()) {
                                                DictionaryDataVO vo = (DictionaryDataVO) opIt.next();
                                                if (fieldValue.indexOf(vo.getId()) > -1) {
                                                    str.append(vo.getValue()).append("  ");
                                                }
                                            }
                                        } else {
                                            str.append(fieldValue);
                                        }
                                        out.print(str.toString());
                                    } else if ("4".equals(fieldType)) { //select
                                        BaseDataHelper dataHelper1 = BaseDataHelper.getInstance();
                                        Collection selectOptionList = (ArrayList) dataHelper1.getDetailMap().get(
                                                (String) field.get("FORM_SYSTEM_ID") + "&"
                                                        + (String) field.get("SECTION_ID") + "&" + fieldArray[i]);
                                        if (selectOptionList != null && selectOptionList.size() > 0) {
                                            Iterator opIt = selectOptionList.iterator();
                                            while (opIt.hasNext()) {
                                                DictionaryDataVO vo = (DictionaryDataVO) opIt.next();
                                                if (fieldValue.equals(vo.getId())) {
                                                    out.print(vo.getValue());
                                                    break;
                                                }
                                            }
                                        } else {
                                            out.print(fieldValue);
                                        }
                                    } else if ("3".equals(fieldType)) { //Date
                                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        java.util.Date cDate = df.parse((String) map.get(fieldName));
                                        out.print(StringUtil.getDateStr(cDate, "MM/dd/yyyy"));
                                    } else {
                                        out.print(DataConvertUtil.convertISOToGBK((String) map.get(fieldName)));
                                        //System.out.println(DataConvertUtil.convertISOToGBK((String)map.get(fieldName)));
                                    }
                                }
                            }
                            out.print("</td>");
                        } else {
                            // System.out.println("Table fieldId: " + fieldId);
                            out.print("<td align=\"left\">");
                            if (tableDataList != null) {
                                Object fieldValue = tableDataList.getFieldValue(requestNo, fieldId, l);
                                String text = FormFieldHelper.showTextField(field, fieldValue);
                                out.print(text);
                                // System.out.println("Value: " + text);
                            }
                            out.print("</td>");
                        }
                    }
                    out.print("</tr>");
                }
            }
        }
    %>
</table>
</body>
</html>
