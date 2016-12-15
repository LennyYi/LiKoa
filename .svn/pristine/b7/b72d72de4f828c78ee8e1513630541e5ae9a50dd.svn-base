package com.aiait.eflow.service.action;

import java.io.*;
import java.util.*;

import javax.servlet.http.*;

import org.w3c.dom.*;

import com.aiait.eflow.common.CommonName;
import com.aiait.eflow.common.helper.*;
import com.aiait.eflow.formmanage.dao.FormManageDAO;
import com.aiait.eflow.formmanage.vo.*;
import com.aiait.eflow.housekeeping.dao.StaffDAO;
import com.aiait.eflow.housekeeping.vo.*;
import com.aiait.eflow.service.dao.FormServiceDAO;
import com.aiait.eflow.service.vo.FormServiceVo;
import com.aiait.eflow.util.*;
import com.aiait.eflow.wkf.dao.WorkFlowProcessDAO;
import com.aiait.eflow.wkf.util.DataMapUtil;
import com.aiait.eflow.wkf.vo.*;
import com.aiait.framework.db.*;
import com.aiait.framework.mvc.action.*;

/**
 * Form Service Action
 * 
 * @version 2011-06-09
 */
public class FormServiceAction extends ServiceAction {

    public ActionLocation newForm(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        response.setContentType("text/html;charset=GBK");
        PrintWriter out = response.getWriter();
        if (!authenticate(request, response)) {
            out.print("Error: " + ServiceAction.AUTHEN_ERROR);
            return null;
        }
        FormServiceVo serviceVo = new FormServiceVo();
        serviceVo.setFormCode(StringUtil.decoderURL(request.getParameter("formId")));
        serviceVo.setRequester(StringUtil.decoderURL(request.getParameter("requester")));
        serviceVo.setSubmitBy(StringUtil.decoderURL(request.getParameter("submitby")));
        serviceVo.setNTID("yes".equalsIgnoreCase(StringUtil.decoderURL(request.getParameter("isntid"))));
        serviceVo.setCallback(StringUtil.decoderURL(request.getParameter("callback")));
        serviceVo.setServiceId(StringUtil.decoderURL(request.getParameter("serviceId")));

        String requestNo = null;
        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            FormManageDAO formDao = new FormManageDAO(dbManager);
            int formSystemId = formDao.getFormSystemId(serviceVo.getFormCode());
            if (formSystemId == -1) {
                out.print("Error: Invalid form id: " + serviceVo.getFormCode());
                return null;
            }
            StaffDAO staffDao = new StaffDAO(dbManager);
            if (serviceVo.isNTID()) {
                String logonId = serviceVo.getRequester();
                StaffVO staff = staffDao.getStaffByDefaultRoel(logonId);
                if (staff == null) {
                    out.print("Error: Cannot find requester for the logon id: " + logonId);
                    return null;
                }
                serviceVo.setRequester(staff.getStaffCode());
                logonId = serviceVo.getSubmitBy();
                staff = staffDao.getStaffByDefaultRoel(logonId);
                if (staff == null) {
                    out.print("Error: Cannot find submitter for the logon id: " + logonId);
                    return null;
                }
                serviceVo.setSubmitBy(staff.getStaffCode());
            } else {
                EflowStaffVO staff = staffDao.getActiveStaffByStaffCode(serviceVo.getRequester());
                if (staff == null) {
                    out.print("Error: Cannot find requester for the staff code: " + serviceVo.getRequester());
                    return null;
                }
                serviceVo.setRequester(staff.getStaffCode());
                staff = staffDao.getActiveStaffByStaffCode(serviceVo.getSubmitBy());
                if (staff == null) {
                    out.print("Error: Cannot find submitter for the staff code: " + serviceVo.getSubmitBy());
                    return null;
                }
                serviceVo.setSubmitBy(staff.getStaffCode());
            }
            FormManageVO form = formDao.getFormBySystemId(formSystemId);
            Iterator sectionIt = form.getSectionList().iterator();
            dbManager.startTransaction();

            // Save initial form data ...
            while (sectionIt.hasNext()) {
                FormSectionVO section = (FormSectionVO) sectionIt.next();
                if (requestNo == null) {
                    // Generate request no.
                    requestNo = FieldUtil.getRequestNo(formSystemId, form.getFormId(), section.getSectionId(),
                            dbManager);
                    serviceVo.setRequestNo(requestNo);
                }
                Iterator fieldIt = section.getFieldList().iterator();
                HashMap fieldMap = new HashMap();
                while (fieldIt.hasNext()) {
                    FormSectionFieldVO field = (FormSectionFieldVO) fieldIt.next();
                    fieldMap.put(field.getFieldId().toLowerCase(), field);
                }

                StringBuffer fieldStr = new StringBuffer();
                StringBuffer valueStr = new StringBuffer();

                if (CommonName.FORM_SECTION_TYPE_BASIC.equals(section.getSectionType())
                        || CommonName.FORM_SECTION_TYPE_COMMON.equals(section.getSectionType())) {
                    String[] columnNames = dbManager.getTableColumnNames(section.getTableName());
                    for (int i = 0; i < columnNames.length; i++) {
                        if (!CommonName.SYSTEM_ID_REQUEST_NO.equals(columnNames[i])) {
                            String value = StringUtil.decoderURL(request.getParameter(columnNames[i]));
                            if (value != null && !"".equals(value)) {
                                FormSectionFieldVO field = (FormSectionFieldVO) fieldMap.get(columnNames[i]
                                        .toLowerCase());
                                fieldStr.append(columnNames[i] + ",");
                                value = getSpelledValues(columnNames[i], request);
                                value = StringUtil.replace(value, "&#39;", "'");
                                value = StringUtil.replace(value, "'", "''");
                                if (field.getFieldType() == CommonName.FIELD_TYPE_NUMBER) {
                                    value = StringUtil.replace(value, ",", "");
                                }
                                valueStr.append("'" + value + "',");
                            }
                        } else {
                            fieldStr.append(columnNames[i] + ",");
                            valueStr.append("'" + requestNo + "',");
                        }
                    }
                    if (valueStr != null && !"".equals(valueStr)) {
                        String insertSQL = "insert into " + section.getTableName() + "("
                                + fieldStr.substring(0, fieldStr.lastIndexOf(",")) + ") values("
                                + valueStr.substring(0, valueStr.lastIndexOf(",")) + ")";
                        // System.out.println("----- insertSQL: " + insertSQL);
                        dbManager.executeUpdate(insertSQL);
                    }
                } else if (CommonName.FORM_SECTION_TYPE_TABLE.equals(section.getSectionType())) {
                    // ...
                }
            }

            // Create form process ...
            WorkFlowProcessTraceVO traceVo = new WorkFlowProcessTraceVO();
            traceVo.setFormSystemId(formSystemId);
            traceVo.setRequestNo(requestNo);
            traceVo.setHandleStaffCode(serviceVo.getSubmitBy());
            traceVo.setCurrentNodeId("0");
            traceVo.setHandleType(CommonName.HANDLE_TYPE_DRAFT);
            traceVo.setHandleComments("Requester drafted the form");
            String isUrgent = request.getParameter("urgent_level");
            if (!"2".equals(isUrgent)) {
                // Default: "1" - Not Urgent
                isUrgent = "1";
            }
            traceVo.setIsUrgent(isUrgent);
            WorkFlowProcessDAO processDao = new WorkFlowProcessDAO(dbManager);
            processDao.drawOutForm(traceVo, serviceVo.getRequester());
            FormServiceDAO formServiceDao = new FormServiceDAO(dbManager);
            formServiceDao.setFormService(serviceVo);
            dbManager.commit();

            out.print(requestNo);
        } catch (Exception ex) {
            dbManager.rollback();
            ex.printStackTrace();
            out.print("Error: " + ex.getMessage());
            return null;
        } finally {
            if (out != null) {
                out.close();
            }
            if (dbManager != null) {
                dbManager.freeConnection();
            }
        }
        return null;
    }

    public ActionLocation deleteForm(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        response.setContentType("text/html;charset=GBK");
        PrintWriter out = response.getWriter();
        if (!authenticate(request, response)) {
            out.print("Error: " + ServiceAction.AUTHEN_ERROR);
            return null;
        }
        String requestNo = StringUtil.decoderURL(request.getParameter("requestNo"));
        if (requestNo == null || requestNo.equals("")) {
            out.print("Failed");
            return null;
        }

        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            WorkFlowProcessDAO processDao = new WorkFlowProcessDAO(dbManager);
            WorkFlowProcessVO process = processDao.getProcessVO(requestNo);
            if (process == null || !process.getStatus().equals(CommonName.STATUS_DRAFT)) {
                out.print("Failed");
                return null;
            }
            FormManageDAO formDao = new FormManageDAO(dbManager);
            dbManager.startTransaction();
            formDao.deleteRequestedFormContent(requestNo);
            formDao.deleteProcess(requestNo);
            formDao.deleteProcessTrace(requestNo);
            dbManager.commit();
            out.print("OK");
        } catch (Exception ex) {
            dbManager.rollback();
            ex.printStackTrace();
            out.print("Failed");
            return null;
        } finally {
            if (out != null) {
                out.close();
            }
            if (dbManager != null) {
                dbManager.freeConnection();
            }
        }
        return null;
    }

    public ActionLocation queryFormField(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        response.setContentType("text/html;charset=GBK");
        PrintWriter out = response.getWriter();
        if (!authenticate(request, response)) {
            out.print("Error: " + ServiceAction.AUTHEN_ERROR);
            return null;
        }
        String requestNo = StringUtil.decoderURL(request.getParameter("requestNo"));
        if (requestNo == null || requestNo.equals("")) {
            out.print("Error: Request No. cannot be null");
            return null;
        }
        String fieldId = StringUtil.decoderURL(request.getParameter("fieldId"));
        if (fieldId == null || fieldId.equals("")) {
            out.print("Error: Field Id cannot be null");
            return null;
        }
        int rowId = 1;
        int rowIndex = fieldId.lastIndexOf("-");
        if (rowIndex != -1) {
            try {
                rowId = Integer.parseInt(fieldId.substring(rowIndex + 1));
            } catch (Exception ex) {
                // Not a number - ignore
            }
            fieldId = fieldId.substring(0, rowIndex);
        }

        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            WorkFlowProcessDAO processDao = new WorkFlowProcessDAO(dbManager);
            WorkFlowProcessVO process = processDao.getProcessVO(requestNo);
            if (process == null) {
                out.print("Error: Cannot find the form - " + requestNo);
                return null;
            }
            if (fieldId.equalsIgnoreCase("form_status")) {
                out.print(DataMapUtil.covertNodeStatus(process.getStatus()));
                return null;
            }
            FormManageDAO formDao = new FormManageDAO(dbManager);
            Iterator sectionIt = formDao.getFormSectionListByFlowId(process.getFlowId()).iterator();
            while (sectionIt.hasNext()) {
                FormSectionVO section = (FormSectionVO) sectionIt.next();
                Iterator fieldIt = section.getFieldList().iterator();
                while (fieldIt.hasNext()) {
                    FormSectionFieldVO field = (FormSectionFieldVO) fieldIt.next();
                    if (field.getFieldId().equalsIgnoreCase(fieldId)) {
                        Object fieldValue = processDao.getFormFieldValue(requestNo, section, field.getFieldId(), rowId);
                        if (fieldValue != null) {
                            out.print(FormFieldHelper.showTextField(field, fieldValue));
                        } else {
                            out.print("");
                        }
                        return null;
                    }
                }
            }
            out.print("Error: Cannot find the field id - " + fieldId);
        } catch (Exception ex) {
            ex.printStackTrace();
            out.print("Error: " + ex.getMessage());
            return null;
        } finally {
            if (out != null) {
                out.close();
            }
            if (dbManager != null) {
                dbManager.freeConnection();
            }
        }
        return null;
    }

    public ActionLocation queryForm(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        response.setContentType("text/xml;charset=GBK");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        OutputStream os = response.getOutputStream();
        PrintWriter out = new PrintWriter(os, true);
        if (!authenticate(request, response)) {
            out.println("Error: " + ServiceAction.AUTHEN_ERROR);
            return null;
        }
        String requestNo = StringUtil.decoderURL(request.getParameter("requestNo"));
        if (requestNo == null || requestNo.equals("")) {
            out.println("Error: Request No. cannot be null");
            return null;
        }

        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            WorkFlowProcessDAO processDao = new WorkFlowProcessDAO(dbManager);
            WorkFlowProcessVO process = processDao.getProcessVO(requestNo);
            if (process == null) {
                out.println("Error: Cannot find the form - " + requestNo);
                return null;
            }
            Document document = XMLUtil.newDocument("form-data");
            FormManageDAO formDao = new FormManageDAO(dbManager);
            int formSystemId = formDao.getFormSystemIdByFlowId(process.getFlowId());
            FormManageVO form = formDao.getFormBySystemId(formSystemId);
            if (form == null) {
                out.println("Error: Cannot find the form design (formSystemId: " + formSystemId + ")");
                return null;
            }
            Element element = document.createElement("form");
            element.setAttribute("systemid", Integer.toString(form.getFormSystemId()));
            element.setAttribute("code", form.getFormId());
            element.setAttribute("name", form.getFormName());
            document.getDocumentElement().appendChild(element);

            element = document.createElement("request-no");
            element.appendChild(document.createTextNode(process.getRequestNo()));
            document.getDocumentElement().appendChild(element);

            element = document.createElement("submitted-by");
            element.setAttribute("code", process.getSubmittedBy());
            element.setAttribute("name", StaffTeamHelper.getInstance().getStaffNameByCode(process.getSubmittedBy()));
            document.getDocumentElement().appendChild(element);

            StaffVO requester = StaffTeamHelper.getInstance().getStaffByCode(process.getRequestStaffCode());
            element = document.createElement("requester");
            element.setAttribute("code", requester.getStaffCode());
            element.setAttribute("name", requester.getStaffName());
            document.getDocumentElement().appendChild(element);

            element = document.createElement("company");
            element.setAttribute("code", requester.getOrgId());
            element.setAttribute("name", CompanyHelper.getInstance().getOrgName(requester.getOrgId()));
            document.getDocumentElement().appendChild(element);

            element = document.createElement("team");
            element.setAttribute("code", requester.getTeamCode());
            element.setAttribute("name", StaffTeamHelper.getInstance().getTeamNameByCode(requester.getTeamCode()));
            document.getDocumentElement().appendChild(element);

            element = document.createElement("request-date");
            element.appendChild(document.createTextNode(this.datetimeFormat.format(process.getSubmissionDate())));
            document.getDocumentElement().appendChild(element);

            element = document.createElement("current-node");
            element.setAttribute("id", process.getNodeId());
            element.setAttribute("name", process.getNodeName());
            document.getDocumentElement().appendChild(element);

            element = document.createElement("current-processor");
            element.setAttribute("code", process.getCurrentProcessor());
            element.setAttribute("name", StaffTeamHelper.getInstance()
                    .getStaffNameByCode(process.getCurrentProcessor()));
            document.getDocumentElement().appendChild(element);

            element = document.createElement("status");
            element.appendChild(document.createTextNode(DataMapUtil.covertNodeStatus(process.getStatus())));
            document.getDocumentElement().appendChild(element);

            element = document.createElement("query-date");
            element.appendChild(document.createTextNode(this.datetimeFormat.format(new Date())));
            document.getDocumentElement().appendChild(element);

            Element sectionsElement = document.createElement("sections");
            document.getDocumentElement().appendChild(sectionsElement);

            Iterator sectionIt = form.getSectionList().iterator();
            while (sectionIt.hasNext()) {
                FormSectionVO section = (FormSectionVO) sectionIt.next();
                Element sectionElement = document.createElement("section");
                sectionElement.setAttribute("id", section.getSectionId());
                sectionElement.setAttribute("name", section.getSectionRemark());
                sectionElement.setAttribute("type", DataMapUtil.covertSectionType(section.getSectionType()));
                sectionsElement.appendChild(sectionElement);

                if (CommonName.FORM_SECTION_TYPE_BASIC.equals(section.getSectionType())
                        || CommonName.FORM_SECTION_TYPE_COMMON.equals(section.getSectionType())) {
                    List<Map<String, Object>> valuelist = processDao.getFormSectionValue(requestNo, section);
                    Iterator fieldIt = section.getFieldList().iterator();
                    while (fieldIt.hasNext()) {
                        FormSectionFieldVO field = (FormSectionFieldVO) fieldIt.next();
                        Element fieldElement = document.createElement("field");
                        fieldElement.setAttribute("id", field.getFieldId());
                        fieldElement.setAttribute("name", field.getFieldLabel());
                        if (!valuelist.isEmpty()) {
                            Object fieldValue = valuelist.get(0).get(field.getFieldId());
                            String fieldValueString = FormFieldHelper.showTextField(field, fieldValue);
                            // System.out.println("fieldValueString: " + fieldValueString);
                            fieldElement.setAttribute("value", fieldValueString);
                        } else {
                            fieldElement.setAttribute("value", "");
                        }
                        sectionElement.appendChild(fieldElement);
                    }
                } else if (CommonName.FORM_SECTION_TYPE_TABLE.equals(section.getSectionType())
                        || CommonName.FORM_SECTION_TYPE_ATTACHMENT.equals(section.getSectionType())) {
                    List<Map<String, Object>> valuelist = processDao.getFormSectionValue(requestNo, section);
                    Iterator fieldIt = section.getFieldList().iterator();
                    while (fieldIt.hasNext()) {
                        FormSectionFieldVO field = (FormSectionFieldVO) fieldIt.next();
                        if (field.getFieldId().equalsIgnoreCase("id")) {
                            continue;
                        }
                        Element columnElement = document.createElement("column");
                        columnElement.setAttribute("id", field.getFieldId());
                        columnElement.setAttribute("name", field.getFieldLabel());
                        sectionElement.appendChild(columnElement);
                    }
                    if (!valuelist.isEmpty()) {
                        Element rowsElement = document.createElement("rows");
                        sectionElement.appendChild(rowsElement);
                        for (int i = 0; i < valuelist.size(); i++) {
                            Map<String, Object> values = valuelist.get(i);
                            int row = i + 1;
                            Element rowElement = document.createElement("row");
                            rowElement.setAttribute("id", section.getSectionId() + "-" + row);
                            rowsElement.appendChild(rowElement);
                            fieldIt = section.getFieldList().iterator();
                            while (fieldIt.hasNext()) {
                                FormSectionFieldVO field = (FormSectionFieldVO) fieldIt.next();
                                if (field.getFieldId().equalsIgnoreCase("id")) {
                                    continue;
                                }
                                Element fieldElement = document.createElement("field");
                                fieldElement.setAttribute("id", field.getFieldId() + "-" + row);
                                Object fieldValue = values.get(field.getFieldId());
                                String fieldValueString = FormFieldHelper.showTextField(field, fieldValue);
                                // System.out.println("fieldValueString: " + fieldValueString);
                                fieldElement.setAttribute("value", fieldValueString);
                                rowElement.appendChild(fieldElement);
                            }
                        }
                    }
                }
            }
            XMLUtil.writeXML(document, os, "GBK");
        } catch (Exception ex) {
            ex.printStackTrace();
            out.println("Error: " + ex.getMessage());
            return null;
        } finally {
            if (out != null) {
                out.close();
            }
            if (dbManager != null) {
                dbManager.freeConnection();
            }
        }
        return null;
    }

    public ActionLocation queryFlow(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        response.setContentType("text/xml;charset=GBK");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        OutputStream os = response.getOutputStream();
        PrintWriter out = new PrintWriter(os, true);
        if (!authenticate(request, response)) {
            out.println("Error: " + ServiceAction.AUTHEN_ERROR);
            return null;
        }
        String requestNo = StringUtil.decoderURL(request.getParameter("requestNo"));
        if (requestNo == null || requestNo.equals("")) {
            out.println("Error: Request No. cannot be null");
            return null;
        }

        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            WorkFlowProcessDAO processDao = new WorkFlowProcessDAO(dbManager);
            WorkFlowProcessVO process = processDao.getProcessVO(requestNo);
            if (process == null) {
                out.println("Error: Cannot find the form - " + requestNo);
                return null;
            }
            Document document = XMLUtil.newDocument("flow-data");
            FormManageDAO formDao = new FormManageDAO(dbManager);
            int formSystemId = formDao.getFormSystemIdByFlowId(process.getFlowId());
            FormManageVO form = formDao.getFormBySystemId(formSystemId);
            if (form == null) {
                out.println("Error: Cannot find the form design (formSystemId: " + formSystemId + ")");
                return null;
            }
            Element element = document.createElement("form");
            element.setAttribute("systemid", Integer.toString(form.getFormSystemId()));
            element.setAttribute("code", form.getFormId());
            element.setAttribute("name", form.getFormName());
            document.getDocumentElement().appendChild(element);

            element = document.createElement("request-no");
            element.appendChild(document.createTextNode(process.getRequestNo()));
            document.getDocumentElement().appendChild(element);

            element = document.createElement("submitted-by");
            element.setAttribute("code", process.getSubmittedBy());
            element.setAttribute("name", StaffTeamHelper.getInstance().getStaffNameByCode(process.getSubmittedBy()));
            document.getDocumentElement().appendChild(element);

            StaffVO requester = StaffTeamHelper.getInstance().getStaffByCode(process.getRequestStaffCode());
            element = document.createElement("requester");
            element.setAttribute("code", requester.getStaffCode());
            element.setAttribute("name", requester.getStaffName());
            document.getDocumentElement().appendChild(element);

            element = document.createElement("company");
            element.setAttribute("code", requester.getOrgId());
            element.setAttribute("name", CompanyHelper.getInstance().getOrgName(requester.getOrgId()));
            document.getDocumentElement().appendChild(element);

            element = document.createElement("team");
            element.setAttribute("code", requester.getTeamCode());
            element.setAttribute("name", StaffTeamHelper.getInstance().getTeamNameByCode(requester.getTeamCode()));
            document.getDocumentElement().appendChild(element);

            element = document.createElement("request-date");
            element.appendChild(document.createTextNode(this.datetimeFormat.format(process.getSubmissionDate())));
            document.getDocumentElement().appendChild(element);

            element = document.createElement("current-node");
            element.setAttribute("id", process.getNodeId());
            element.setAttribute("name", process.getNodeName());
            document.getDocumentElement().appendChild(element);

            element = document.createElement("current-processor");
            element.setAttribute("code", process.getCurrentProcessor());
            element.setAttribute("name", StaffTeamHelper.getInstance()
                    .getStaffNameByCode(process.getCurrentProcessor()));
            document.getDocumentElement().appendChild(element);

            element = document.createElement("status");
            element.appendChild(document.createTextNode(DataMapUtil.covertNodeStatus(process.getStatus())));
            document.getDocumentElement().appendChild(element);

            element = document.createElement("query-date");
            element.appendChild(document.createTextNode(this.datetimeFormat.format(new Date())));
            document.getDocumentElement().appendChild(element);

            Element processingTrailElement = document.createElement("processing-trail");
            document.getDocumentElement().appendChild(processingTrailElement);

            Collection traceList = processDao.getProcessTraceList(requestNo, true);
            Iterator traceIt = traceList.iterator();
            while (traceIt.hasNext()) {
                WorkFlowProcessTraceVO traceVo = (WorkFlowProcessTraceVO) traceIt.next();
                Element processElement = document.createElement("process");
                processElement.setAttribute("node", traceVo.getNodeName());
                processElement.setAttribute("processor", StaffTeamHelper.getInstance().getStaffNameByCode(
                        traceVo.getHandleStaffCode()));
                processElement.setAttribute("date", StringUtil.getDateStr(StringUtil.stringToDate(traceVo
                        .getHandleDateStr(), "yyyy-MM-dd HH:mm:ss"), "MM/dd/yyyy HH:mm:ss"));
                processElement.setAttribute("type", DataMapUtil.convertHandleType(traceVo.getHandleType()));
                processElement.setAttribute("remark", traceVo.getHandleComments());
                processingTrailElement.appendChild(processElement);
            }
            XMLUtil.writeXML(document, os, "GBK");
        } catch (Exception ex) {
            ex.printStackTrace();
            out.println("Error: " + ex.getMessage());
            return null;
        } finally {
            if (out != null) {
                out.close();
            }
            if (dbManager != null) {
                dbManager.freeConnection();
            }
        }
        return null;
    }

    protected String getSpelledValues(String paramName, HttpServletRequest request) {
        String[] valueList = request.getParameterValues(paramName);
        if (valueList != null && valueList.length >= 1) {
            String tempValue = "";
            for (int i = 0; i < valueList.length; i++) {
                tempValue = tempValue + valueList[i] + ",";
            }
            tempValue = tempValue.substring(0, tempValue.length() - 1);
            tempValue = tempValue.trim();
            return tempValue;
        } else {
            return "";
        }
    }

}
