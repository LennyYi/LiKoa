package com.aiait.eflow.formmanage.action;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.aiait.eflow.basedata.dao.BaseDataDAO;
import com.aiait.eflow.basedata.vo.BaseDataVO;
import com.aiait.eflow.common.CommonName;
import com.aiait.eflow.common.ModuleOperateName;
import com.aiait.eflow.common.helper.AuthorityHelper;
import com.aiait.eflow.common.helper.BaseDataHelper;
import com.aiait.eflow.common.helper.CompanyHelper;
import com.aiait.eflow.common.helper.DynamicSystemFieldHelper;
import com.aiait.eflow.common.helper.FieldControlHelper;
import com.aiait.eflow.common.helper.FormScriptHelper;
import com.aiait.eflow.common.helper.FormTypeHelper;
import com.aiait.eflow.common.helper.ParamConfigHelper;
import com.aiait.eflow.common.helper.StaffTeamHelper;
import com.aiait.eflow.common.helper.SystemFieldHelper;
import com.aiait.eflow.delegation.dao.DelegateDAO;
import com.aiait.eflow.formmanage.dao.FormManageDAO;
import com.aiait.eflow.formmanage.vo.DictionaryDataVO;
import com.aiait.eflow.formmanage.vo.FormAttachedFileVO;
import com.aiait.eflow.formmanage.vo.FormManageVO;
import com.aiait.eflow.formmanage.vo.FormSectionFieldVO;
import com.aiait.eflow.formmanage.vo.FormSectionVO;
import com.aiait.eflow.formmanage.vo.SpecialFieldVo;
import com.aiait.eflow.housekeeping.dao.CompanyDAO;
import com.aiait.eflow.housekeeping.dao.EmailTemplateDAO;
import com.aiait.eflow.housekeeping.dao.OTRecordDAO;
import com.aiait.eflow.housekeeping.dao.StaffDAO;
import com.aiait.eflow.housekeeping.dao.TeamDAO;
import com.aiait.eflow.housekeeping.vo.CompanyVO;
import com.aiait.eflow.housekeeping.vo.EflowStaffVO;
import com.aiait.eflow.housekeeping.vo.EmailTemplateVO;
import com.aiait.eflow.housekeeping.vo.FormTypeVO;
import com.aiait.eflow.housekeeping.vo.StaffVO;
import com.aiait.eflow.housekeeping.vo.SystemFieldVO;
import com.aiait.eflow.housekeeping.vo.TeamVO;
import com.aiait.eflow.report.dao.ReportDAO;
import com.aiait.eflow.service.action.ServiceAction;
import com.aiait.eflow.util.EFlowEmailUtil;
import com.aiait.eflow.util.ExportFileUtil;
import com.aiait.eflow.util.FieldUtil;
import com.aiait.eflow.util.FileUtil;
import com.aiait.eflow.util.LogonFilter;
import com.aiait.eflow.util.StringUtil;
import com.aiait.eflow.wkf.dao.ListInquiryFormDAO;
import com.aiait.eflow.wkf.dao.WorkFlowNodeDAO;
import com.aiait.eflow.wkf.dao.WorkFlowProcessDAO;
import com.aiait.eflow.wkf.vo.WorkFlowItemVO;
import com.aiait.eflow.wkf.vo.WorkFlowProcessTraceVO;
import com.aiait.eflow.wkf.vo.WorkFlowProcessVO;
import com.aiait.framework.db.DBManagerFactory;
import com.aiait.framework.db.IDBManager;
import com.aiait.framework.exception.DAOException;
import com.aiait.framework.i18n.I18NMessageHelper;
import com.aiait.framework.mvc.action.ActionLocation;
import com.aiait.framework.mvc.action.DispatchAction;
import com.aiait.framework.mvc.action.ModuleMapping;
import com.aiait.framework.util.CommonUtil;
import com.lowagie.text.DocumentException;

public class FormManageAction extends DispatchAction {

    /**
     * ����ѡ��������Form��ҳ��
     * 
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionLocation enterRefFormWindow(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        // I18NMessageHelper.getMessage("");
        String returnUrl = "RefWindowPage";
        String SystemFieldId = (String) request.getParameter("sfilId");
        String formId = (String) request.getParameter("formId");
        String requestNo = (String) request.getParameter("requestNo");
        StaffVO staff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);

        DynamicSystemFieldHelper dsfhelper = new DynamicSystemFieldHelper();
        SystemFieldVO dynamicSysField = dsfhelper.getDynamicSystemField(SystemFieldId, staff.getStaffCode(), formId);
        if (dynamicSysField != null) {
            request.setAttribute("RefFormField", dynamicSysField);
        }

        // ��ȡForm_system_id, section
        String formsystemId = (String) request.getParameter("formsystemId");
        String sectionId = (String) request.getParameter("sectionId");
        if (formsystemId != null && !"".equals(formsystemId) && sectionId != null && !"".equals(sectionId)) {
            // System.out.println("===>formsystemId"+formsystemId+"======>sectionId"+sectionId+"=====>requestNo"+requestNo);
            String requestnoList = dsfhelper.getSelectedReqList(requestNo, formsystemId, sectionId);
            request.setAttribute("requestnoList", requestnoList);
        }
        return mapping.findActionLocation(returnUrl);
    }

    /**
     * Select Reference Form for common fields.
     * 
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionLocation selectRefForm(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String returnUrl = "selectRefForm";

        String selectRefForms = (String) request.getParameter("selectRefForms");
        String[] selectRefFormList = selectRefForms == null || selectRefForms.trim().equals("") ? null : selectRefForms
                .trim().split(",");
        request.setAttribute("selectRefForms", selectRefFormList);

        String multiSelection = (String) request.getParameter("multiSelection");
        if (multiSelection != null) {
            request.setAttribute("multiSelection", multiSelection);
        }

        WorkFlowProcessVO queryVo = new WorkFlowProcessVO();
        String qryRequestNo = (String) request.getParameter("qryRequestNo");
        // System.out.println("qryRequestNo: " + qryRequestNo);
        queryVo.setRequestNo(qryRequestNo);

        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            ListInquiryFormDAO inquiryFormDao = new ListInquiryFormDAO(dbManager);
            Collection queryRefForms = inquiryFormDao.queryForms(queryVo);
            request.setAttribute("queryRefForms", queryRefForms);
        } catch (DAOException e) {
            e.printStackTrace();
            request.setAttribute(CommonName.COMMON_ERROR_INFOR, e.getMessage());
            returnUrl = "fail";
        } finally {
            if (dbManager != null)
                dbManager.freeConnection();
        }

        return mapping.findActionLocation(returnUrl);
    }

    /**
     * ����form��section��ʾ˳�����ҳ��
     * 
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionLocation preAdjustSectionOrder(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String formSystemId = (String) request.getParameter("formSystemId");
        String returnUrl = "adjustSectionOrderPage";
        // ��ȡ��form������section
        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            FormManageDAO formDao = new FormManageDAO(dbManager);
            Collection sectionList = formDao.getSectionListByForm(Integer.parseInt(formSystemId));
            request.setAttribute("sectionList", sectionList);
        } catch (DAOException e) {
            e.printStackTrace();
            request.setAttribute(CommonName.COMMON_ERROR_INFOR, e.getMessage());
            returnUrl = "fail";
        } finally {
            if (dbManager != null)
                dbManager.freeConnection();
        }
        return mapping.findActionLocation(returnUrl);
    }

    /**
     * ����section��field��ʾ˳�����ҳ��
     * 
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionLocation preAdjustFieldOrder(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String formSystemId = (String) request.getParameter("formSystemId");
        String sectionId = (String) request.getParameter("sectionId");
        String sectionType = (String) request.getParameter("sectionType");
        String returnUrl = "adjustFieldOrderPage";
        // ��ȡ��form������section
        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            FormManageDAO formDao = new FormManageDAO(dbManager);
            Collection sectionFieldList = formDao.getSectionFieldListByForm(Integer.parseInt(formSystemId), sectionId,
                    sectionType);
            request.setAttribute("sectionFieldList", sectionFieldList);
        } catch (DAOException e) {
            e.printStackTrace();
            request.setAttribute(CommonName.COMMON_ERROR_INFOR, e.getMessage());
            returnUrl = "fail";
        } finally {
            if (dbManager != null)
                dbManager.freeConnection();
        }
        return mapping.findActionLocation(returnUrl);
    }

    /**
     * �������õ�sectionField��ʾ˳��
     * 
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionLocation saveAdjustFieldOrder(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String formSystemId = (String) request.getParameter("formSystemId");
        String sectionId = (String) request.getParameter("sectionId");
        // String sectionType = (String)request.getParameter("sectionType");
        System.out.println("formSystemId=" + formSystemId + ",sectionId=" + sectionId);
        // ��ȡ��section������fields
        IDBManager dbManager = null;
        PrintWriter out = response.getWriter();
        try {
            dbManager = DBManagerFactory.getDBManager();
            dbManager.startTransaction();
            FormManageDAO formDao = new FormManageDAO(dbManager);
            Enumeration paramNames = request.getParameterNames();
            String name = "";
            String[] paramValues;
            String orderId = "";
            while (paramNames.hasMoreElements()) {
                name = (String) paramNames.nextElement();
                orderId = (String) request.getParameter(name);
                name = CommonUtil.decoderURL(name);
                if (name != null && name.indexOf("orderId||") > -1) { // ����������"|"����
                    paramValues = StringUtil.split(name, "||"); // paramValues[0]='orderId',paramValues[1]=fieldId
                    if (paramValues != null && paramValues.length == 2) {
                        formDao.updateFieldOrder(Integer.parseInt(formSystemId), sectionId, paramValues[1], orderId);
                    }
                }
            }
            dbManager.commit();
            out.print("success");
        } catch (DAOException e) {
            dbManager.rollback();
            e.printStackTrace();
            out.print("fail");
            return null;
        } finally {
            if (dbManager != null)
                dbManager.freeConnection();
            if (out != null)
                out.close();
        }
        return null;
    }

    /**
     * �������õ�section��ʾ˳��
     * 
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionLocation saveAdjustSectionOrder(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String formSystemId = (String) request.getParameter("formSystemId");
        // ��ȡ��form������section
        IDBManager dbManager = null;
        PrintWriter out = response.getWriter();
        try {
            dbManager = DBManagerFactory.getDBManager();
            dbManager.startTransaction();
            FormManageDAO formDao = new FormManageDAO(dbManager);
            Collection sectionList = formDao.getSectionListByForm(Integer.parseInt(formSystemId));
            if (sectionList != null) {
                Iterator it = sectionList.iterator();
                while (it.hasNext()) {
                    FormSectionVO section = (FormSectionVO) it.next();
                    String sectionId = (String) request.getParameter("sectionId_" + section.getSectionId());
                    String orderId = (String) request.getParameter("orderId_" + section.getSectionId());
                    formDao.updateSectionOrder(Integer.parseInt(formSystemId), sectionId, orderId);
                }
            }
            dbManager.commit();
            out.print("success");
        } catch (DAOException e) {
            dbManager.rollback();
            e.printStackTrace();
            out.print("fail");
            return null;
        } finally {
            if (dbManager != null)
                dbManager.freeConnection();
            if (out != null)
                out.close();
        }
        return null;
    }

    /**
     * �������ø�section��ÿ���еĿ��ȵ���ҳ��
     * 
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionLocation preAdjustColumnWidth(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String formSystemId = (String) request.getParameter("formSystemId");
        String sectionId = (String) request.getParameter("sectionId");
        String sectionType = (String) request.getParameter("sectionType");
        String returnUrl = "adjustColumnWidthPage";
        // ��ȡ��form������section
        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            FormManageDAO formDao = new FormManageDAO(dbManager);
            Collection sectionFieldList = formDao.getSectionFieldListByForm(Integer.parseInt(formSystemId), sectionId,
                    sectionType);
            request.setAttribute("sectionFieldList", sectionFieldList);
        } catch (DAOException e) {
            e.printStackTrace();
            request.setAttribute(CommonName.COMMON_ERROR_INFOR, e.getMessage());
            returnUrl = "fail";
        } finally {
            if (dbManager != null)
                dbManager.freeConnection();
        }
        return mapping.findActionLocation(returnUrl);
    }

    /**
     * �������ø�section��ÿ���еĿ���
     * 
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionLocation saveAdjustColumnWidth(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String formSystemId = (String) request.getParameter("formSystemId");
        String sectionId = (String) request.getParameter("sectionId");
        // String sectionType = (String)request.getParameter("sectionType");
        // ��ȡ��section������fields
        IDBManager dbManager = null;
        PrintWriter out = response.getWriter();
        try {
            dbManager = DBManagerFactory.getDBManager();
            dbManager.startTransaction();
            FormManageDAO formDao = new FormManageDAO(dbManager);
            Enumeration paramNames = request.getParameterNames();
            String name = "";
            String[] paramValues;
            int columnWidth = 10; // default is 10%
            while (paramNames.hasMoreElements()) {
                name = (String) paramNames.nextElement();
                if (name != null && name.indexOf("columnWidth||") > -1) { // ����������"|"����
                    if ((String) request.getParameter(name) != null && !"".equals((String) request.getParameter(name))) {
                        try {
                            columnWidth = Integer.parseInt((String) request.getParameter(name));
                        } catch (Exception e) {
                        }
                    }
                    // System.out.println("orderId="+orderId);
                    paramValues = StringUtil.split(name, "||"); // paramValues[0]='orderId',paramValues[1]=fieldId
                    if (paramValues != null && paramValues.length == 2) {
                        formDao.updateSectionColumnWidth(Integer.parseInt(formSystemId), sectionId, paramValues[1],
                                columnWidth);
                    }
                }
            }
            dbManager.commit();
            out.print("success");
        } catch (DAOException e) {
            dbManager.rollback();
            e.printStackTrace();
            out.print("fail");
            return null;
        } finally {
            if (dbManager != null)
                dbManager.freeConnection();
            if (out != null)
                out.close();
        }
        return null;
    }

    /**
     * get form list by formType, the result is used to create the option list of select
     * 
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionLocation getFormList(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String formType = (String) request.getParameter("formType");
        String moduleIdStr = (String) request.getParameter("moduleId");
        String formTypeList = "";
        if (formType == null || "".equals(formType)) {
            StaffVO currentStaff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
            AuthorityHelper authority = AuthorityHelper.getInstance();
            if (moduleIdStr != null && !"".equals(moduleIdStr)) {
                int moduleId = Integer.parseInt(moduleIdStr);

                Collection typeList = FormTypeHelper.getInstance().getFormTypeList();
                if (typeList != null && typeList.size() > 0) {
                    Iterator it = typeList.iterator();
                    while (it.hasNext()) {
                        FormTypeVO vo = (FormTypeVO) it.next();
                        if (authority.checkAuthorityByFormType(currentStaff.getCurrentRoleId(), moduleId,
                                vo.getFormTypeId())) {
                            formTypeList = formTypeList + "'" + vo.getFormTypeId() + "',";
                        }
                    }
                }
                /**
                 * if(authority.checkAuthority(moduleId,1)){ //Query TS Form formTypeList = formTypeList + "'01',"; }
                 * if(authority.checkAuthority(moduleId,2)){ //Query Account Form formTypeList = formTypeList + "'03',";
                 * } if(authority.checkAuthority(moduleId,3)){ //Query Admin Form formTypeList = formTypeList + "'02',";
                 * } if(authority.checkAuthority(moduleId,4)){ //Query HR Form formTypeList = formTypeList + "'04',"; }
                 * if(authority.checkAuthority(moduleId,5)){ //Query E-Flow Form formTypeList = formTypeList + "'05',";
                 * }
                 **/
                if (!"".equals(formTypeList)) {
                    formTypeList = formTypeList.substring(0, formTypeList.length() - 1);
                }
            }
        }
        StaffVO currentStaff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
        String supperOrgs = currentStaff.getUpperCompanys(); // CompanyHelper.getInstance().getUpperCompany(currentStaff.getOrgId());

        IDBManager dbManager = null;
        // PrintWriter out = response.getWriter();
        // response.setContentType("text/xml");
        response.setContentType("text/xml;charset=GB2312"); // it is very important
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        PrintWriter out = response.getWriter();
        out.println("<?xml version=\"1.0\" encoding=\"GB2312\"?>");
        try {
            StringBuffer responseXML = new StringBuffer("<options>");
            dbManager = DBManagerFactory.getDBManager();
            FormManageDAO formDao = new FormManageDAO(dbManager);
            // get all form list

            Collection formList = null;
            if (formType == null || "".equals(formType)) {
                formList = formDao.getFormListByTypeList(formTypeList, supperOrgs, null);
            } else {
                formList = formDao.getNewFormListByType(formType, supperOrgs);
            }

            if (formList != null && formList.size() > 0) {
                Iterator it = formList.iterator();
                while (it.hasNext()) {
                    FormManageVO form = (FormManageVO) it.next();
                    responseXML
                            .append("<option id='" + form.getFormSystemId() + "'>")
                            .append(StringUtil.formatXML(form.getFormId()) + " - "
                                    + StringUtil.formatXML(form.getFormName())).append("</option>");
                }
            }
            responseXML.append("</options>");
            // System.out.println(responseXML.toString());
            out.write(responseXML.toString());

        } catch (DAOException e) {
            e.printStackTrace();
            out.print("fail");
        } finally {
            if (dbManager != null)
                dbManager.freeConnection();
            if (out != null)
                out.close();
        }
        return null;
    }

    public ActionLocation getFormFieldValue(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String formType = (String) request.getParameter("formType");
        String formSystemId = (String) request.getParameter("formSystemId");
        String fieldSectionId = (String) request.getParameter("fieldId");
        String sectionId = null;
        String fieldId = null;

        boolean isFormTypeField = false;
        if (formSystemId != null && !"".equals(formSystemId.trim()) && fieldSectionId.indexOf("||") > -1) {
            String[] temp = StringUtil.split(fieldSectionId, "||");
            sectionId = temp[0];
            fieldId = temp[1];
        } else {
            fieldId = fieldSectionId;
            isFormTypeField = true;
        }

        IDBManager dbManager = null;
        // PrintWriter out = response.getWriter();
        // response.setContentType("text/xml");
        response.setContentType("text/xml;charset=GB2312"); // it is very important
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        PrintWriter out = response.getWriter();
        out.println("<?xml version=\"1.0\" encoding=\"GB2312\"?>");
        try {
            dbManager = DBManagerFactory.getDBManager();
            FormManageDAO formDao = new FormManageDAO(dbManager);
            FormSectionFieldVO fieldVo = null;
            if (isFormTypeField) {
                fieldVo = formDao.getFormTypeField(formType, fieldId);
            } else {
                fieldVo = formDao.getField(Integer.parseInt(formSystemId), sectionId, fieldId);
            }
            StringBuffer outStr = new StringBuffer("");
            StaffVO tmpstaff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);

            if (fieldVo != null) {
                int fieldType = fieldVo.getFieldType();
                switch (fieldType) {
                case CommonName.FIELD_TYPE_TEXT:// ���ı���
                    outStr.append("<input type=\"text\" name='fieldsValue' size='20'>");
                    break;
                case CommonName.FIELD_TYPE_TEXTAREA:// 2���ı���
                    outStr.append("<input type=\"text\" name='fieldsValue' size='20'>");
                    break;
                case CommonName.FIELD_TYPE_DATE:// 3����ѡ���
                    outStr.append("<input type=\"text\" size='12' readonly name='fieldsValue' title='"
                            + fieldVo.getFieldLabel() + "' onclick='setday(this)'>(MM/DD/YYYY)");
                    break;
                case CommonName.FIELD_TYPE_SELECT:// 4����ѡ���
                    outStr.append("<select name='fieldsValue'>");
                    BaseDataHelper dataHelper1 = BaseDataHelper.getInstance();
                    Collection selectOptionList = null;
                    if (isFormTypeField) {
                        selectOptionList = (ArrayList) dataHelper1.getDetailMap().get(fieldVo.getFieldComments()); // Master_Id
                    } else {
                        selectOptionList = (ArrayList) dataHelper1.getDetailMap().get(
                                fieldVo.getFormSystemId() + "&" + fieldVo.getSectionId() + "&" + fieldVo.getFieldId());
                    }
                    if (selectOptionList != null && selectOptionList.size() > 0) {
                        Iterator opIt = selectOptionList.iterator();
                        while (opIt.hasNext()) {
                            DictionaryDataVO vo = (DictionaryDataVO) opIt.next();
                            outStr.append("<option value='" + vo.getId() + "'>" + vo.getValue() + "</option>");
                        }
                    } else {
                        outStr.append("<option value=''>     </option>");
                    }
                    outStr.append("</select>");
                    break;
                case CommonName.FIELD_TYPE_NUMBER:// 5 Number
                    outStr.append("<input type=\"text\" name='fieldsValue' title='"
                            + fieldVo.getFieldLabel()
                            + "'"
                            + " onKeyPress='if (event.keyCode!=46 && event.keyCode!=45 && (event.keyCode<48 || event.keyCode>57)) event.returnValue=false'>");
                    break;
                case CommonName.FIELD_TYPE_CHECKBOX:// 6 checkbox
                    BaseDataHelper dataHelper = BaseDataHelper.getInstance();
                    outStr.append("<select name='fieldsValue'>");
                    // ������ѡ������ʽ����
                    Collection optionList = null;
                    if (isFormTypeField) {
                        optionList = (ArrayList) dataHelper.getDetailMap().get(fieldVo.getFieldComments());
                    } else {
                        optionList = (ArrayList) dataHelper.getDetailMap().get(
                                fieldVo.getFormSystemId() + "&" + fieldVo.getSectionId() + "&" + fieldVo.getFieldId());
                    }
                    /**
                     * if (optionList!=null && optionList.size()>0){ Iterator opIt = optionList.iterator();
                     * while(opIt.hasNext()){ DictionaryDataVO vo = (DictionaryDataVO)opIt.next();
                     * outStr.append("<input type='checkbox' name='fieldsValue' title='"
                     * +fieldVo.getFieldLabel()+"' value='"+vo.getId()+"'>"+vo.getValue()+"&nbsp;&nbsp;"); } }
                     **/
                    if (optionList != null && optionList.size() > 0) {
                        Iterator opIt = optionList.iterator();
                        while (opIt.hasNext()) {
                            DictionaryDataVO vo = (DictionaryDataVO) opIt.next();
                            outStr.append("<option value='" + vo.getId() + "'>" + vo.getValue() + "</option>");
                        }
                    } else {
                        outStr.append("<option value=''>     </option>");
                    }
                    outStr.append("</select>");
                    break;
                case CommonName.FIELD_TYPE_SYSTEM: // 7 SystemField
                    FormSectionFieldVO vo = SystemFieldHelper.getInstance().getSystemFieldById(fieldVo.getFieldId());
                    Collection sysOpList = vo.getOptionList();
                    if (sysOpList != null && sysOpList.size() > 0) {
                        outStr.append("<select name='fieldsValue'>");
                        Iterator it = sysOpList.iterator();
                        while (it.hasNext()) {
                            DictionaryDataVO op = (DictionaryDataVO) it.next();
                            outStr.append("<option value='" + op.getId() + "'>" + op.getValue() + "</option>");
                        }
                        outStr.append("</select>");
                    } else if (vo.getFieldId().toLowerCase().indexOf("date") > -1) { // IT1321 begin
                        outStr.append("<input type=\"text\" size='12' readonly name='fieldsValue' title='"
                                + fieldVo.getFieldLabel() + "' onclick='setday(this)'>(MM/DD/YYYY)");
                    } else {
                        outStr.append("<input type=\"text\" name='fieldsValue' size='20'>");
                    } // IT1321 end
                    break;
                case CommonName.FIELD_TYPE_BASIC:
                    // �����ϵͳ�ֶ��е�request_staff_code,team_code,company_Idʱ������Ҫ��ʾ����ѡ���б�
                    FormSectionFieldVO requestStaffVo = SystemFieldHelper.getInstance().getBasicFieldById(
                            CommonName.SYSTEM_ID_REQUEST_STAFF_CODE);
                    FormSectionFieldVO teamVo = SystemFieldHelper.getInstance().getBasicFieldById(
                            CommonName.SYSTEM_ID_REQUESTOR_TEAM_CODE);
                    FormSectionFieldVO companyvo = SystemFieldHelper.getInstance().getBasicFieldById(
                            CommonName.SYSTEM_ID_COMPANY_ID);
                    if (fieldVo.getFieldId().equals(requestStaffVo.getFieldId())
                            || fieldVo.getFieldId().equals(
                                    SystemFieldHelper.getInstance()
                                            .getBasicFieldById(CommonName.SYSTEM_ID_SUBMIT_STAFF_CODE).getFieldId())) {
                        outStr.append("<select name='fieldsValue'>");
                        Collection staffList = null;
                        if ("Y".equals(ParamConfigHelper.getInstance().getParamValue("show_terminated_staffs"))) {
                            staffList = StaffTeamHelper.getInstance().getATStaffList();
                        } else {
                            staffList = StaffTeamHelper.getInstance().getStaffList();
                        }
                        if (staffList != null) {
                            Iterator staffIt = staffList.iterator();
                            while (staffIt.hasNext()) {
                                StaffVO staff = (StaffVO) staffIt.next();
                                outStr.append("<option value='" + staff.getStaffCode() + "'>")
                                        .append(staff.getStaffName()).append("</option>");
                            }
                        } else {
                            outStr.append("<option value=''>     </option>");
                        }
                        outStr.append("</select>");
                    } else if (fieldVo.getFieldId().equals(teamVo.getFieldId())) {
                        outStr.append("<select name='fieldsValue'>");
                        Collection teamList = StaffTeamHelper.getInstance().getTeamList();
                        if (teamList != null) {
                            Iterator teamIt = teamList.iterator();
                            while (teamIt.hasNext()) {
                                TeamVO team = (TeamVO) teamIt.next();
                                outStr.append("<option value='" + team.getTeamCode() + "'>").append(team.getTeamName())
                                        .append("</option>");
                            }
                        } else {
                            outStr.append("<option value=''>     </option>");
                        }
                        outStr.append("</select>");
                    } else if (fieldVo.getFieldId().equals(companyvo.getFieldId())) {
                        CompanyDAO companydao = new CompanyDAO(dbManager);
                        outStr.append("<select name='fieldsValue'>");
                        Collection companyList = companydao.getCompanyList(tmpstaff.getOrgId());
                        if (companyList != null) {
                            Iterator companyIt = companyList.iterator();
                            while (companyIt.hasNext()) {
                                CompanyVO company = (CompanyVO) companyIt.next();
                                outStr.append("<option value='" + company.getOrgId() + "'>")
                                        .append(company.getOrgName()).append("</option>");
                            }
                        } else {
                            outStr.append("<option value=''>     </option>");
                        }
                        outStr.append("</select>");

                    } else if (fieldVo.getFieldId().equals(
                            SystemFieldHelper.getInstance().getBasicFieldById(CommonName.SYSTEM_ID_REQUEST_DATE)
                                    .getFieldId())) {
                        outStr.append("<input type=\"text\" size='12' readonly name='fieldsValue' title='"
                                + fieldVo.getFieldLabel() + "' onclick='setday(this)'>(MM/DD/YYYY)");
                    } else {
                        outStr.append("<input type=\"text\" name='fieldsValue' size='20'>");
                    }
                    break;
                default:
                    outStr.append("<input type=\"text\" name='fieldsValue' size='20'>");
                }
                out.print(outStr.toString());
            } else {
                // out.print("fail");
                out.print("<input type=\"text\" name='fieldsValue' size='20'>");
            }
        } catch (DAOException e) {
            e.printStackTrace();
            out.print("fail");
        } finally {
            if (dbManager != null)
                dbManager.freeConnection();
            if (out != null)
                out.close();
        }
        return null;
    }

    public ActionLocation getFormFieldList(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String formSystemId = (String) request.getParameter("formSystemId");
        String ignoreTableSection = (String) request.getParameter("ignoreTableSection"); // �Ƿ����table��ʽ��section��Ĭ��Ϊfalse"������"��trueΪ�����ԡ�
        boolean ingore = false;
        if (ignoreTableSection != null) {
            if ("true".equals(ignoreTableSection)) {
                ingore = true;
            } else {
                ingore = false;
            }
        }
        IDBManager dbManager = null;
        // PrintWriter out = response.getWriter();
        // response.setContentType("text/xml");
        response.setContentType("text/xml;charset=GB2312"); // it is very important
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        PrintWriter out = response.getWriter();
        out.println("<?xml version=\"1.0\" encoding=\"GB2312\"?>");
        try {
            StringBuffer responseXML = new StringBuffer("<options>");
            dbManager = DBManagerFactory.getDBManager();
            FormManageDAO formDao = new FormManageDAO(dbManager);
            // get all form list
            Collection formList = formDao.getFieldListByForm(Integer.parseInt(formSystemId), ingore);
            if (formList != null && formList.size() > 0) {
                Iterator it = formList.iterator();
                while (it.hasNext()) {
                    FormSectionFieldVO field = (FormSectionFieldVO) it.next();
                    responseXML.append("<option id='" + field.getSectionId() + "||" + field.getFieldId() + "'>")
                            .append(field.getSectionId() + "." + StringUtil.formatXML(field.getFieldLabel()))
                            .append("</option>");
                }
            }
            responseXML.append("</options>");
            // System.out.println(responseXML.toString());
            out.write(responseXML.toString());

        } catch (DAOException e) {
            e.printStackTrace();
            out.print("fail");
        } finally {
            if (dbManager != null)
                dbManager.freeConnection();
            if (out != null)
                out.close();
        }
        return null;
    }

    public ActionLocation getFormTypeFieldList(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String formType = (String) request.getParameter("formType");
        IDBManager dbManager = null;
        response.setContentType("text/xml;charset=GB2312");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        PrintWriter out = response.getWriter();
        out.println("<?xml version=\"1.0\" encoding=\"GB2312\"?>");
        try {
            StringBuffer responseXML = new StringBuffer("<options>");
            dbManager = DBManagerFactory.getDBManager();
            FormManageDAO formDao = new FormManageDAO(dbManager);
            List fieldList = formDao.getFieldListByFormType(formType);
            Iterator it = fieldList.iterator();
            while (it.hasNext()) {
                FormSectionFieldVO field = (FormSectionFieldVO) it.next();
                responseXML.append("<option id='" + field.getFieldId() + "'>")
                        .append(StringUtil.formatXML(field.getFieldLabel())).append("</option>");
            }
            responseXML.append("</options>");
            // System.out.println(responseXML.toString());
            out.write(responseXML.toString());
        } catch (DAOException e) {
            e.printStackTrace();
            out.print("fail");
        } finally {
            if (dbManager != null)
                dbManager.freeConnection();
            if (out != null)
                out.close();
        }
        return null;
    }

    public ActionLocation listAvailableForm(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String returnLabel = "availableFormList";
        StaffVO currentStaff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
        String supperOrgs = currentStaff.getUpperCompanys();// CompanyHelper.getInstance().getUpperCompany(currentStaff.getOrgId());
        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            FormManageDAO formDao = new FormManageDAO(dbManager);
            // get all forms which had linked flow
            Collection typeList = FormTypeHelper.getInstance().getFormTypeList();
            if (typeList != null && typeList.size() > 0) {
                Iterator it = typeList.iterator();
                while (it.hasNext()) {
                    FormTypeVO typeVo = (FormTypeVO) it.next();
                    // Collection formList = formDao.getAvailableForm();
                    // ֻ��ȡ�Ѿ�����'publish'״̬��form
                    Collection formList = formDao.getNewFormListByType(typeVo.getFormTypeId(), supperOrgs, "0");
                    request.setAttribute(typeVo.getFormTypeName(), formList);
                }
            }
        } catch (DAOException e) {
            e.printStackTrace();
            returnLabel = "fail";
        } finally {
            dbManager.freeConnection();
        }
        return mapping.findActionLocation(returnLabel);
    }

    /**
     * ����FORM�б���ʾҳ�棨��ʾ�����Ѿ����ڵ�FORM,����û��section��form)
     * 
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionLocation manageForm(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String returnLabel = "showManageForm";
        String formId = (String) request.getParameter("formId");
        String formType = (String) request.getParameter("formType");
        String status = (String) request.getParameter("status");
        String orgId = (String) request.getParameter("orgId");

        String superOrgIds = "";
        StaffVO currentStaff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
        // modify by Robin Hou 2008-06-19
        // ���orgIdΪ�գ���Ĭ�ϲ�ѯ�����ڹ�˾�Լ��������¼���˾������form
        if (orgId == null || "".equals(orgId)) {
            orgId = currentStaff.getOrgId();
            superOrgIds = currentStaff.getLowerCompanys(); // CompanyHelper.getInstance().getLowerCompany(orgId);
        } else {
            superOrgIds = "'" + orgId + "'";
        }
        // end modify

        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            FormManageDAO formDao = new FormManageDAO(dbManager);
            FormManageVO queryVo = new FormManageVO();
            if (formId != null) {
                queryVo.setFormId(formId.trim());
            }
            // IT0958-DS011 Begin comment out
            /*
             * if(formType!=null){ queryVo.setFormType(formType); }
             */
            // IT0958-DS011 End comment out

            // IT0958-DS011 Begin
            if (formType != null && !"".equals(formType)) {
                queryVo.setFormType("'" + formType + "'");
            } else {
                String formTypes = "";

                AuthorityHelper authority = AuthorityHelper.getInstance();

                Collection typeList = FormTypeHelper.getInstance().getFormTypeList();
                if (typeList != null && typeList.size() > 0) {
                    Iterator it = typeList.iterator();
                    while (it.hasNext()) {
                        FormTypeVO typeVo = (FormTypeVO) it.next();
                        if (authority.checkAuthorityByFormType(currentStaff.getCurrentRoleId(),
                                ModuleOperateName.MODULE_FORM_MANAGE, typeVo.getFormTypeId())) {
                            formTypes = formTypes + "'" + typeVo.getFormTypeId() + "',";
                        }
                    }
                }
                /**
                 * if(authority.checkAuthority(ModuleOperateName.MODULE_FORM_MANAGE,8)){ formTypes = formTypes +
                 * "'01',"; } if(authority.checkAuthority(ModuleOperateName.MODULE_FORM_MANAGE,9)){ formTypes =
                 * formTypes + "'02',"; } if(authority.checkAuthority(ModuleOperateName.MODULE_FORM_MANAGE,10)){
                 * formTypes = formTypes + "'03',"; }
                 * if(authority.checkAuthority(ModuleOperateName.MODULE_FORM_MANAGE,11)){ formTypes = formTypes +
                 * "'04',"; } if(authority.checkAuthority(ModuleOperateName.MODULE_FORM_MANAGE,12)){ formTypes =
                 * formTypes + "'05',"; }
                 **/
                if (!"".equals(formTypes)) {
                    formTypes = formTypes.substring(0, formTypes.length() - 1);
                }
                queryVo.setFormType(formTypes);
            }
            queryVo.setOrgId(superOrgIds);
            // IT0958-DS011 End
            if (status != null) {
                queryVo.setStatus(status);
            }
            // ��ȡ���е�form
            Collection formList = formDao.getFormList(queryVo);
            request.setAttribute("formList", formList);
        } catch (DAOException e) {
            e.printStackTrace();
            returnLabel = "fail";
            request.setAttribute(CommonName.COMMON_ERROR_INFOR, I18NMessageHelper.getMessage("common.databaseerror")
                    + ":<br>" + e.toString());
        } finally {
            dbManager.freeConnection();
        }
        return mapping.findActionLocation(returnLabel);
    }

    /**
     * ����form��Ϣ�޸�ҳ��
     * 
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionLocation enterEditForm(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String returnLabel = "newFormPage";
        String type = (String) request.getParameter("type");
        if (type == null) {
            type = "new";
        }
        if ("edit".equals(type) || "editWholeForm".equals(type)) {
            String formSystemId = (String) request.getParameter("formSystemId");
            IDBManager dbManager = null;
            try {
                dbManager = DBManagerFactory.getDBManager();
                FormManageDAO formDao = new FormManageDAO(dbManager);
                FormManageVO form = formDao.getFormBySystemId(Integer.parseInt(formSystemId));
                request.setAttribute("wholeForm", form);

                // For field of tip and highlight
                Collection fieldList = formDao.getFieldListByForm(Integer.parseInt(formSystemId));
                request.setAttribute("fieldList", fieldList);
                // Tip field
                SpecialFieldVo tipField = new SpecialFieldVo();
                tipField.setFormSystemId(Integer.parseInt(formSystemId));
                tipField.setFieldType(SpecialFieldVo.TYPE_TIP);
                tipField = formDao.getSpecialField(tipField);
                request.setAttribute("tipField", tipField);

                // Highlight fields
                SpecialFieldVo highlightField = new SpecialFieldVo();
                highlightField.setFormSystemId(Integer.parseInt(formSystemId));
                highlightField.setFieldType(SpecialFieldVo.TYPE_HIGHLIGHT_1);
                highlightField = formDao.getSpecialField(highlightField);
                request.setAttribute("highlightField", highlightField);

                SpecialFieldVo highlightField2 = new SpecialFieldVo();
                highlightField2.setFormSystemId(Integer.parseInt(formSystemId));
                highlightField2.setFieldType(SpecialFieldVo.TYPE_HIGHLIGHT_2);
                highlightField2 = formDao.getSpecialField(highlightField2);
                request.setAttribute("highlightField2", highlightField2);

                SpecialFieldVo highlightField3 = new SpecialFieldVo();
                highlightField3.setFormSystemId(Integer.parseInt(formSystemId));
                highlightField3.setFieldType(SpecialFieldVo.TYPE_HIGHLIGHT_3);
                highlightField3 = formDao.getSpecialField(highlightField3);
                request.setAttribute("highlightField3", highlightField3);

            } catch (DAOException e) {
                e.printStackTrace();
                returnLabel = "fail";
            } finally {
                dbManager.freeConnection();
            }
        }
        if ("editWholeForm".equals(type)) {
            returnLabel = "editWholeFormPage";
        }

        return mapping.findActionLocation(returnLabel);
    }

    /**
     * ����form section��Ϣ�޸�ҳ��
     * 
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionLocation enterEditFormSection(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String returnLabel = "sectionEditPage";
        String type = (String) request.getParameter("type");
        if ("edit".equals(type)) {
            IDBManager dbManager = null;
            String formSystemId = (String) request.getParameter("formSystemId");
            String sectionId = (String) request.getParameter("sectionId");
            try {
                dbManager = DBManagerFactory.getDBManager();
                FormManageDAO formDao = new FormManageDAO(dbManager);
                FormSectionVO section = formDao.getFormSectionInfor(Integer.parseInt(formSystemId), sectionId);
                if (section == null) {
                    request.setAttribute(CommonName.COMMON_TIP_INFO, I18NMessageHelper.getMessage("common.happenerror"));
                    mapping.findActionLocation("fail");
                }
                request.setAttribute("section", section);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                dbManager.freeConnection();
            }
        }
        return mapping.findActionLocation(returnLabel);
    }

    /**
     * Requester copy one requested form
     * 
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionLocation copyRequetedForm(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String requestNo = (String) request.getParameter("requestNo");
        StaffVO currentStaff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
        IDBManager dbManager = null;
        response.setContentType("text/html;charset=GB2312"); // it is very important
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        PrintWriter out = response.getWriter();
        try {
            dbManager = DBManagerFactory.getDBManager();
            FormManageDAO formDao = new FormManageDAO(dbManager);
            // validate the status of the copied form's template is "published"
            dbManager.startTransaction();
            FormManageVO form = formDao.getFormManageVObyRequestNo(requestNo);
            if (form != null) {
                if (!"0".equals(form.getStatus())) { // �����form��״̬���ǡ�published��,���ܿ���
                    out.print(I18NMessageHelper.getMessage("common.cannotcopy"));
                    return null;
                }
                Collection sectionList = formDao.getSectionListByForm(form.getFormSystemId());
                FormSectionVO sectionVo = (FormSectionVO) sectionList.iterator().next();
                // ��ʼ����ѡ����form������һ���µ�form

                // 1.�õ�һ���µ�requestNo
                String newRequestNo = FieldUtil.getRequestNo(form.getFormSystemId(), form.getFormId(),
                        sectionVo.getSectionId());

                // 2.�������ɸ�form�ľ�������
                Iterator sectionIt = sectionList.iterator();
                while (sectionIt.hasNext()) {
                    FormSectionVO tempSection = (FormSectionVO) sectionIt.next();
                    if (!tempSection.getSectionType().equals("00")) {
                        // sectionType='04'�Ĳ���Ҫ��������Ϊ�����͵�section�����ݿ����ǲ�����table�ģ�û�б������������ݿ��У�
                        if (tempSection.getSectionType().equals("04")) {
                            continue;
                        }
                        formDao.copySectionRecord(requestNo, newRequestNo, tempSection);
                    } else {
                        ArrayList SourceAttachedfile = (ArrayList) formDao.getAttachedfiles(tempSection, requestNo);
                        if (SourceAttachedfile != null) {
                            for (int i = 0; i < SourceAttachedfile.size(); i++) {
                                FormAttachedFileVO attachedfilevo = (FormAttachedFileVO) SourceAttachedfile.get(i);
                                String Destfilename = FileUtil.generateDestfile(attachedfilevo.getFilename());
                                String DestAttachedfile = ParamConfigHelper.getInstance().getParamValue(
                                        CommonName.PARAM_UPLOAD_DIR)
                                        + "/upload/requestform/" + Destfilename;
                                // File sourcefile = new
                                // File(ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_UPLOAD_DIR)
                                // + "/upload/requestform/" + attachedfilevo.getFilename());
                                File sourcefile = FileUtil.getUploadFile("/upload/requestform/"
                                        + attachedfilevo.getFilename());
                                if (sourcefile.exists()) {
                                    FileUtil.saveAs(sourcefile, DestAttachedfile);
                                    formDao.copyfileName(newRequestNo, tempSection, Destfilename, attachedfilevo);
                                }
                            }
                        }
                    }
                }

                // 3.���ɳ�ʼ����process��¼
                WorkFlowProcessDAO processDao = new WorkFlowProcessDAO(dbManager);
                // ��ȡ�ɵļ�¼
                WorkFlowProcessVO oldProcessVo = processDao.getProcessVO(requestNo);

                WorkFlowProcessTraceVO traceVo = new WorkFlowProcessTraceVO();
                traceVo.setCurrentNodeId("0");
                if (oldProcessVo.getIsUrgent() != null) {
                    traceVo.setIsUrgent(oldProcessVo.getIsUrgent());
                } else {
                    traceVo.setIsUrgent("");
                }
                traceVo.setHandleType("00");
                traceVo.setHandleComments("Requester drafted the form");
                traceVo.setHandleStaffCode(currentStaff.getStaffCode());
                traceVo.setFormSystemId(form.getFormSystemId());
                traceVo.setRequestNo(newRequestNo);
                processDao.drawOutForm(traceVo, currentStaff.getStaffCode());
                // copy end
                out.print("success");
            } else {
                out.print(I18NMessageHelper.getMessage("common.formnotexist"));
            }
            dbManager.commit();
        } catch (Exception e) {
            dbManager.rollback();
            out.print("fail");
            e.printStackTrace();
        } finally {
            if (dbManager != null)
                dbManager.freeConnection();
            if (out != null)
                out.close();
        }
        return null;
    }

    /**
     * copy form (Include copy form base information and form section and form field information
     * 
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionLocation copyForm(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String formSystemId = (String) request.getParameter("formSystemId");
        IDBManager dbManager = null;
        PrintWriter out = response.getWriter();
        try {
            dbManager = DBManagerFactory.getDBManager();
            FormManageDAO formDao = new FormManageDAO(dbManager);
            dbManager.startTransaction();
            formDao.copyForm(Integer.parseInt(formSystemId));
            dbManager.commit();
            // begin to refresh base data
            // have a test first
            // StaffVO staff = (StaffVO)request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
            BaseDataHelper.getInstance().refresh();
            SystemFieldHelper.getInstance().refresh();
            out.print("success");
        } catch (Exception e) {
            dbManager.rollback();
            out.print("fail");
            e.printStackTrace();
        } finally {
            if (dbManager != null)
                dbManager.freeConnection();
            if (out != null)
                out.close();
        }
        return null;
    }

    /**
     * delete form (Include delete form base information and delete section and delete field information
     * 
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionLocation deleteForm(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String formSystemId = (String) request.getParameter("formSystemId");
        IDBManager dbManager = null;
        response.setContentType("text/html;charset=GB2312"); // it is very important
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        PrintWriter out = response.getWriter();
        try {
            dbManager = DBManagerFactory.getDBManager();
            // 1.if the form's status is "Published",it can't be deleted!
            // 2.if the form is linked by one workflow,it can't be deleted!
            FormManageDAO formDao = new FormManageDAO(dbManager);
            dbManager.startTransaction();
            FormManageVO form = formDao.getFormBySystemId(Integer.parseInt(formSystemId));
            if ("0".equals(form.getStatus())) {
                out.print(I18NMessageHelper.getMessage("common.formpublished"));
                return null;
            }
            if ("2".equals(form.getStatus())) {
                out.print(I18NMessageHelper.getMessage("common.formdisabled"));
                return null;
            }
            // int num = formDao.getFlowNumLinkedForm(Integer.parseInt(formSystemId));
            // if(num>0){
            // }
            formDao.deleteFormBase(Integer.parseInt(formSystemId));
            formDao.deleteFormAllSections(Integer.parseInt(formSystemId));
            formDao.deleteFormAllFields(Integer.parseInt(formSystemId));
            dbManager.commit();
            out.print("success");
        } catch (Exception e) {
            dbManager.rollback();
            out.print("fail");
            e.printStackTrace();
        } finally {
            if (dbManager != null)
                dbManager.freeConnection();
            if (out != null)
                out.close();
        }
        return null;
    }

    /**
     * revoke form
     * 
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionLocation revokeForm(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String formSystemId = (String) request.getParameter("formSystemId");
        IDBManager dbManager = null;
        PrintWriter out = response.getWriter();
        try {
            dbManager = DBManagerFactory.getDBManager();
            FormManageDAO formDao = new FormManageDAO(dbManager);
            formDao.revokeForm(Integer.parseInt(formSystemId));
            out.print("success");
        } catch (Exception e) {
            out.print("fail");
            e.printStackTrace();
        } finally {
            if (dbManager != null)
                dbManager.freeConnection();
            if (out != null)
                out.close();
        }
        return null;
    }

    /**
     * 
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionLocation saveFormSection(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String formSystemId = (String) request.getParameter("formSystemId");
        String sectionId = (String) request.getParameter("sectionId");
        String sectionType = (String) request.getParameter("sectionType");
        // String sectionRemark = (String)request.getParameter("sectionRemark");
        String sectionRemark = CommonUtil.decoderURL((String) request.getParameter("sectionRemark"));
        // System.out.println("sectionRemark: " + sectionRemark);
        String type = (String) request.getParameter("type");
        if (type == null) {
            type = "new";
        }

        FormSectionVO section = new FormSectionVO();
        FormSectionFieldVO field = new FormSectionFieldVO();
        field.setFormSystemId(Integer.parseInt(formSystemId));
        field.setSectionId(sectionId);

        section.setFormSystemId(Integer.parseInt(formSystemId));
        section.setSectionId(sectionId);
        section.setSectionType(sectionType);
        section.setSectionRemark(sectionRemark);
        if ("new".equals(type)) {
            section.setTableName(FieldUtil.getSectionDataTableName(field));
        }
        IDBManager dbManager = null;
        PrintWriter out = response.getWriter();
        StringBuffer result = new StringBuffer("");
        response.setHeader("Cache-Control", "no-cache");
        try {
            dbManager = DBManagerFactory.getDBManager();
            FormManageDAO formDao = new FormManageDAO(dbManager);
            dbManager.startTransaction();
            if ("new".equals(type)) {// it is new
                // ÿ��form��section_id������Ψһ�ģ����ܴ����ظ�
                if (formDao.checkFormSectionId(Integer.parseInt(formSystemId), section.getSectionId()) == true) {
                    out.print("multi-sectionid");
                    return null;
                }
                // ÿ��formֻ�ܴ���һ��sectionType==03��section��¼
                if ("03".equals(sectionType)) {
                    if (formDao.checkFormBySectionType(Integer.parseInt(formSystemId), "03")) {
                        out.println("exist");
                        return null;
                    }
                }
                // save form
                formDao.saveSection(section);
                if ("03".equals(sectionType)) {// ����Ĭ�ϵ�section application_basic_information
                    FormSectionFieldVO requestNoVo = SystemFieldHelper.getInstance().getBasicFieldById(
                            CommonName.SYSTEM_ID_REQUEST_NO);
                    FormSectionFieldVO staffVo = SystemFieldHelper.getInstance().getBasicFieldById(
                            CommonName.SYSTEM_ID_REQUEST_STAFF_CODE);
                    FormSectionFieldVO dateVo = SystemFieldHelper.getInstance().getBasicFieldById(
                            CommonName.SYSTEM_ID_REQUEST_DATE);
                    FormSectionFieldVO teamVo = SystemFieldHelper.getInstance().getBasicFieldById(
                            CommonName.SYSTEM_ID_REQUESTOR_TEAM_CODE);
                    FormSectionFieldVO submitByVo = SystemFieldHelper.getInstance().getBasicFieldById(
                            CommonName.SYSTEM_ID_SUBMIT_STAFF_CODE);
                    FormSectionFieldVO companyVo = SystemFieldHelper.getInstance().getBasicFieldById(
                            CommonName.SYSTEM_ID_COMPANY_ID);

                    requestNoVo.setFormSystemId(Integer.parseInt(formSystemId));
                    requestNoVo.setSectionId(sectionId);
                    staffVo.setFormSystemId(Integer.parseInt(formSystemId));
                    staffVo.setSectionId(sectionId);
                    dateVo.setFormSystemId(Integer.parseInt(formSystemId));
                    dateVo.setSectionId(sectionId);
                    teamVo.setFormSystemId(Integer.parseInt(formSystemId));
                    teamVo.setSectionId(sectionId);
                    submitByVo.setFormSystemId(Integer.parseInt(formSystemId));
                    submitByVo.setSectionId(sectionId);
                    companyVo.setFormSystemId(Integer.parseInt(formSystemId));
                    companyVo.setSectionId(sectionId);

                    // IT1321
                    section.getFieldList().add(requestNoVo);
                    section.getFieldList().add(staffVo);
                    section.getFieldList().add(dateVo);
                    section.getFieldList().add(teamVo);
                    section.getFieldList().add(submitByVo);
                    section.getFieldList().add(companyVo);

                    formDao.saveFormSecitonField(requestNoVo);
                    formDao.saveFormSecitonField(staffVo);
                    formDao.saveFormSecitonField(dateVo);
                    formDao.saveFormSecitonField(teamVo);
                    formDao.saveFormSecitonField(submitByVo);
                    formDao.saveFormSecitonField(companyVo);
                } else if ("00".equals(sectionType)) { // attached file section
                    FormSectionFieldVO fileNameVo = SystemFieldHelper.getInstance().getBasicFieldById(
                            CommonName.SYSTEM_ID_FILE_NAME);
                    FormSectionFieldVO fileDescriptionVo = SystemFieldHelper.getInstance().getBasicFieldById(
                            CommonName.SYSTEM_ID_FILE_DESCRIPTION);

                    fileNameVo.setFormSystemId(Integer.parseInt(formSystemId));
                    fileNameVo.setSectionId(sectionId);
                    fileDescriptionVo.setFormSystemId(Integer.parseInt(formSystemId));
                    fileDescriptionVo.setSectionId(sectionId);

                    formDao.saveFormSecitonField(fileNameVo);
                    formDao.saveFormSecitonField(fileDescriptionVo);
                }

                /**
                 * ��������published form��ʱ���Զ��������ֶ� //����Ǳ�����ʽ��section�����Զ�����ϵͳ�����ֶΡ�id�� if("01".equals(sectionType)){
                 * field.setFormSystemId(Integer.parseInt(formSystemId)); field.setSectionId(sectionId);
                 * field.setFieldId("id"); field.setFieldType(CommonName.FIELD_TYPE_IDENTITY); }
                 **/

                if ("01".equals(sectionType)) {
                    result.append(
                            "<div id=\"div"
                                    + section.getSectionId()
                                    + "\"><b>"
                                    + section.getSectionRemark()
                                    + "</b>:<input type='button' name='addBtn"
                                    + section.getSectionId()
                                    + "' value='Add Column' onclick='addCol("
                                    + "\""
                                    + "sectionTable"
                                    + section.getSectionId()
                                    + "\",\""
                                    + section.getSectionId()
                                    + "\")' class=btn3_mouseout onmouseover=\"this.className='btn3_mouseover'\" onmouseout=\"this.className='btn3_mouseout'\" onmousedown=\"this.className='btn3_mousedown'\" onmouseup=\"this.className='btn3_mouseup'\" >")
                            .append("&nbsp;<input type='button' name='delBtn"
                                    + section.getSectionId()
                                    + "' value='Delete Column' onclick='deleteCol(\"sectionTable"
                                    + section.getSectionId()
                                    + "\")' class=btn3_mouseout onmouseover=\"this.className='btn3_mouseover'\" onmouseout=\"this.className='btn3_mouseout'\" onmousedown=\"this.className='btn3_mousedown'\" onmouseup=\"this.className='btn3_mouseup'\" >")
                            .append("&nbsp;<input type='button' name='addColField"
                                    + section.getSectionId()
                                    + "' value='Add Field' onclick='addColField(\"sectionTable"
                                    + section.getSectionId()
                                    + "\",\""
                                    + section.getSectionId()
                                    + "\")' class=btn3_mouseout onmouseover=\"this.className='btn3_mouseover'\" onmouseout=\"this.className='btn3_mouseout'\" onmousedown=\"this.className='btn3_mousedown'\" onmouseup=\"this.className='btn3_mouseup'\" >")
                            .append("&nbsp;<input type='button' name='delField"
                                    + section.getSectionId()
                                    + "' value='Delete Field' onclick='deleteField(\"sectionTable"
                                    + section.getSectionId()
                                    + "\",\""
                                    + section.getSectionId()
                                    + "\",\"01\")' class=btn3_mouseout onmouseover=\"this.className='btn3_mouseover'\" onmouseout=\"this.className='btn3_mouseout'\" onmousedown=\"this.className='btn3_mousedown'\" onmouseup=\"this.className='btn3_mouseup'\" >")
                            .append("&nbsp;<input type='button' name='delSection"
                                    + section.getSectionId()
                                    + "' value='Delete Section' onclick='deleteSection(\"sectionTable"
                                    + section.getSectionId()
                                    + "\",\"div"
                                    + section.getSectionId()
                                    + "\",\""
                                    + section.getSectionType()
                                    + "\",\""
                                    + section.getSectionId()
                                    + "\")' class=btn3_mouseout onmouseover=\"this.className='btn3_mouseover'\" onmouseout=\"this.className='btn3_mouseout'\" onmousedown=\"this.className='btn3_mousedown'\" onmouseup=\"this.className='btn3_mouseup'\" >")
                            .append("&nbsp;<input type='button' name='adjustField"
                                    + section.getSectionId()
                                    + "' value='Adjust Fields Order' onclick='adjustFieldOrder(\""
                                    + section.getSectionId()
                                    + "\",\""
                                    + section.getSectionType()
                                    + "\")' class=btn3_mouseout onmouseover=\"this.className='btn3_mouseover'\" onmouseout=\"this.className='btn3_mouseout'\" onmousedown=\"this.className='btn3_mousedown'\" onmouseup=\"this.className='btn3_mouseup'\" >")
                            .append("&nbsp;<input type='button' name='adjustColumn"
                                    + section.getSectionId()
                                    + "' value='Adjust Column Width' onclick='adjustColumnWidth(\""
                                    + section.getSectionId()
                                    + "\",\""
                                    + section.getSectionType()
                                    + "\")' class=btn3_mouseout onmouseover=\"this.className='btn3_mouseover'\" onmouseout=\"this.className='btn3_mouseout'\" onmousedown=\"this.className='btn3_mousedown'\" onmouseup=\"this.className='btn3_mouseup'\" >")
                            .append("</div>");
                } else if ("02".equals(sectionType)) {
                    result.append(
                            "<div id=\"div"
                                    + section.getSectionId()
                                    + "\"><b>"
                                    + section.getSectionRemark()
                                    + ":</b><input type='button' name='addBtn"
                                    + section.getSectionId()
                                    + "' value='Add Row' onclick='addRow("
                                    + "\""
                                    + "sectionTable"
                                    + section.getSectionId()
                                    + "\",\""
                                    + section.getSectionId()
                                    + "\")' class=btn3_mouseout onmouseover=\"this.className='btn3_mouseover'\" onmouseout=\"this.className='btn3_mouseout'\" onmousedown=\"this.className='btn3_mousedown'\" onmouseup=\"this.className='btn3_mouseup'\" >")
                            .append("&nbsp;<input type='button' name='delBtn"
                                    + section.getSectionId()
                                    + "' value='Delete Row' onclick='deleteRow("
                                    + "\""
                                    + "sectionTable"
                                    + section.getSectionId()
                                    + "\""
                                    + ")' class=btn3_mouseout onmouseover=\"this.className='btn3_mouseover'\" onmouseout=\"this.className='btn3_mouseout'\" onmousedown=\"this.className='btn3_mousedown'\" onmouseup=\"this.className='btn3_mouseup'\" >")
                            .append("&nbsp;<input type='button' name='addField"
                                    + section.getSectionId()
                                    + "' value='Add Field' onclick='addField("
                                    + "\""
                                    + "sectionTable"
                                    + section.getSectionId()
                                    + "\""
                                    + ",\""
                                    + section.getSectionId()
                                    + "\",\""
                                    + section.getSectionType()
                                    + "\")' class=btn3_mouseout onmouseover=\"this.className='btn3_mouseover'\" onmouseout=\"this.className='btn3_mouseout'\" onmousedown=\"this.className='btn3_mousedown'\" onmouseup=\"this.className='btn3_mouseup'\" >")
                            .append("&nbsp;<input type='button' name='delField"
                                    + section.getSectionId()
                                    + "' value='Delete Field' onclick='deleteField(\"sectionTable"
                                    + section.getSectionId()
                                    + "\",\""
                                    + section.getSectionId()
                                    + "\",\"02\")' class=btn3_mouseout onmouseover=\"this.className='btn3_mouseover'\" onmouseout=\"this.className='btn3_mouseout'\" onmousedown=\"this.className='btn3_mousedown'\" onmouseup=\"this.className='btn3_mouseup'\" >")
                            .append("&nbsp;<input type='button' name='delSection"
                                    + section.getSectionId()
                                    + "' value='Delete Section' onclick='deleteSection(\"sectionTable"
                                    + section.getSectionId()
                                    + "\",\"div"
                                    + section.getSectionId()
                                    + "\",\""
                                    + section.getSectionType()
                                    + "\",\""
                                    + section.getSectionId()
                                    + "\")' class=btn3_mouseout onmouseover=\"this.className='btn3_mouseover'\" onmouseout=\"this.className='btn3_mouseout'\" onmousedown=\"this.className='btn3_mousedown'\" onmouseup=\"this.className='btn3_mouseup'\"> ")
                            .append("&nbsp;<input type='button' name='adjustField"
                                    + section.getSectionId()
                                    + "' value='Adjust Fields Order' onclick='adjustFieldOrder(\""
                                    + section.getSectionId()
                                    + "\",\""
                                    + section.getSectionType()
                                    + "\")' class=btn3_mouseout onmouseover=\"this.className='btn3_mouseover'\" onmouseout=\"this.className='btn3_mouseout'\" onmousedown=\"this.className='btn3_mousedown'\" onmouseup=\"this.className='btn3_mouseup'\" >")
                            .append("</div>");
                } else if ("03".equals(sectionType)) {
                    result.append("<b>" + section.getSectionRemark() + ":</b>");
                }
                // IT1321
                formDao.createSectionTable(section);
            } else { // it is update
                formDao.updateFormSectionInfor(section);
                out.print("success");
            }
            dbManager.commit();
            out.println(result.toString());
        } catch (DAOException e) {
            out.println("fail");
            dbManager.rollback();
            e.printStackTrace();
        } finally {
            if (out != null)
                out.close();
            dbManager.freeConnection();
        }
        return null;
    }

    /**
     * ����form field��Ϣ�޸�ҳ��
     * 
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionLocation enterEditFormField(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String returnLabel = "fieldEditPage";
        String type = (String) request.getParameter("type");
        String formSystemId = (String) request.getParameter("formSystemId");
        String sectionId = (String) request.getParameter("sectionId");
        if ("edit".equals(type)) {
            String fieldId = (String) request.getParameter("fieldId");
            IDBManager dbManager = null;
            try {
                dbManager = DBManagerFactory.getDBManager();
                FormManageDAO formDao = new FormManageDAO(dbManager);
                // get formField
                FormSectionFieldVO field = formDao.getField(Integer.parseInt(formSystemId), sectionId, fieldId);
                request.setAttribute("field", field);
                // if field type is '4' or '6', it needs to get the masterId
                if (field.getFieldType() == 4 || field.getFieldType() == 6) {
                    BaseDataDAO baseDataDao = new BaseDataDAO(dbManager);
                    BaseDataVO masterVo = baseDataDao.getMasterData(Integer.parseInt(formSystemId), sectionId, fieldId);
                    request.setAttribute("masterVo", masterVo);
                }

                ReportDAO reportDAO = new ReportDAO(dbManager);
                Collection publishReports = reportDAO.getAllPublishedReport();
                request.setAttribute("publishReports", publishReports);
            } catch (DAOException e) {
                returnLabel = "fail";
                e.printStackTrace();
            } finally {
                dbManager.freeConnection();
            }
        } else {
            // initial field_id
            IDBManager dbManager = null;
            try {
                dbManager = DBManagerFactory.getDBManager();
                FormManageDAO formDao = new FormManageDAO(dbManager);
                int maxFieldNo = formDao.getMaxFieldNo(Integer.parseInt(formSystemId), sectionId);
                String fieldId = "field_" + sectionId + "_" + maxFieldNo;
                FormSectionFieldVO field = new FormSectionFieldVO();
                field.setFieldId(fieldId);
                request.setAttribute("field", field);
                
                ReportDAO reportDAO = new ReportDAO(dbManager);
                Collection publishReports = reportDAO.getAllPublishedReport();
                request.setAttribute("publishReports", publishReports);
                
            } catch (DAOException e) {
                returnLabel = "fail";
                e.printStackTrace();
            } finally {
                dbManager.freeConnection();
            }
        }
        return mapping.findActionLocation(returnLabel);
    }

    /**
     * ����System Field��Ϣ
     * 
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionLocation saveSystemFormField(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String result = "success";
        String formSystemId = (String) request.getParameter("formSystemId");
        String sectionId = (String) request.getParameter("sectionId");
        String fieldId = (String) request.getParameter("systemFieldId");
        String saveType = (String) request.getParameter("saveType");
        String formStatus = (String) request.getParameter("formStatus");

        FormSectionFieldVO field = SystemFieldHelper.getInstance().getSystemFieldById(fieldId);
        if (field == null) {
            DynamicSystemFieldHelper dsfhelper = new DynamicSystemFieldHelper();
            StaffVO staff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
            field = dsfhelper.getSysFieldOfFormSectionFieldVO(fieldId, staff.getStaffCode(), formSystemId);
        } else {
            // ������е�sql����
            field.setFieldDataSrSQL("");
        }
        field.setFormSystemId(Integer.parseInt(formSystemId));
        field.setSectionId(sectionId);

        // add by robin hou on 2008/07/24
        String clickEvent = (String) request.getParameter("clickEvent");
        String dbclickEvent = (String) request.getParameter("dbclickEvent");
        String onfocusEvent = (String) request.getParameter("onfocusEvent");
        String lostfocusEvent = (String) request.getParameter("lostfocusEvent");
        String changeEvent = (String) request.getParameter("changeEvent");
        String defaultValue = (String) request.getParameter("defaultValue");
        String highLevel = (String) request.getParameter("highLevel"); // -1----No,1-----Yes
        String fieldComments = CommonUtil.decoderURL((String) request.getParameter("fieldComments"));
        String orderId = (String) request.getParameter("orderId");
        String issinglrow = (String) request.getParameter("isSingleRow"); // -1----not single,1----single
        String isRequired = (String) request.getParameter("isRequired");
        if (issinglrow == null || "".equals(issinglrow)) {
            issinglrow = "-1";
        }
        String isReadonly = (String) request.getParameter("isReadonly"); // -1 ��ֻ����1 ֻ����Ĭ��Ϊ-1
        if (isReadonly == null || "".equals(isReadonly)) {
            isReadonly = "-1";
        }
        String isDisabled = (String) request.getParameter("isDisabled"); 
        if (isDisabled == null || "".equals(isDisabled)) {
        	isDisabled = "-1";
        }
        field.setIsReadonly(Integer.parseInt(isReadonly));
        field.setIsDisabled(Integer.parseInt(isDisabled));
        field.setClickEvent(clickEvent);
        field.setDbclickEvent(dbclickEvent);
        field.setOnfocusEvent(onfocusEvent);
        field.setLostfocusEvent(lostfocusEvent);
        field.setChangeEvent(changeEvent);
        field.setDefaultValue(defaultValue);
        field.setFieldComments(fieldComments);
        field.setIsSingleRow(Integer.parseInt(issinglrow));
        if (orderId != null && !"".equals(orderId)) {
            field.setOrderId(Integer.parseInt(orderId));
        }
        if (highLevel == null || "".equals(highLevel)) {
            highLevel = "-1";
        }
        field.setHighLevel(Integer.parseInt(highLevel));
        // end add 2008/07/24

        if ("true".equals(isRequired)) {// add 2013/07/22
            field.setIsRequired(true);
        } else {
            field.setIsRequired(false);
        }

        PrintWriter out = response.getWriter();
        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            dbManager.startTransaction();
            FormManageDAO formDao = new FormManageDAO(dbManager);
            if ("new".equals(saveType)) {
                // save formField
                formDao.saveFormSecitonField(field);
                // �����form��״̬�ǡ�published(0)���ģ�����Ҫmodify section table������һ�У�
                if ("0".equals(formStatus)) {
                    field.setIsRequired(false); // ����Ѿ��б����ڣ����ܻ������ݴ��ڣ���Ĭ������Ϊ��
                    formDao.addSectionTableColumn(field);
                }
            } else {
                formDao.updateFormSectionField(field);
            }
            dbManager.commit();
            out.print(result);
        } catch (DAOException e) {
            dbManager.rollback();
            e.printStackTrace();
            out.print("fail");
        } finally {
            if (out != null)
                out.close();
            if (dbManager != null)
                dbManager.freeConnection();
        }
        return null;
    }

    /**
     * ����field��Ϣ
     * 
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionLocation saveFormField(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String result = "success";
        FormSectionFieldVO field = new FormSectionFieldVO();
        String formSystemId = (String) request.getParameter("formSystemId");
        String sectionId = (String) request.getParameter("sectionId");
        String fieldId = (String) request.getParameter("fieldId");
        String fieldLabel = CommonUtil.decoderURL((String) request.getParameter("fieldLabel"));
        String isRequired = (String) request.getParameter("isRequired");
        String length = (String) request.getParameter("length");
        String fieldType = (String) request.getParameter("fieldType");
        String dataType = (String) request.getParameter("dataType");
        String saveType = (String) request.getParameter("saveType");
        String formStatus = (String) request.getParameter("formStatus");// 0---published 1---design
        String highLevel = (String) request.getParameter("highLevel"); // -1----No,1-----Yes
        // IT0958 begin
        String ismoney = (String) request.getParameter("isMoney"); // -1----not money,1-----is money
        String issinglrow = (String) request.getParameter("isSingleRow"); // -1----not single,1----single
        String defaultValue = (String) request.getParameter("defaultValue");
        // IT0958 end
        String isReadonly = (String) request.getParameter("isReadonly"); // -1 ��ֻ����1 ֻ����Ĭ��Ϊ-1
        if (isReadonly == null || "".equals(isReadonly)) {
            isReadonly = "-1";
        }
        String isDisabled = (String) request.getParameter("isDisabled");
        if (isDisabled == null || "".equals(isDisabled)) {
        	isDisabled = "-1";
        }
        // IT1003 begin:setting event function
        String clickEvent = (String) request.getParameter("clickEvent");
        String dbclickEvent = (String) request.getParameter("dbclickEvent");
        String onfocusEvent = (String) request.getParameter("onfocusEvent");
        String lostfocusEvent = (String) request.getParameter("lostfocusEvent");
        String changeEvent = (String) request.getParameter("changeEvent");
        String fieldDataSrSQL = (String) request.getParameter("sourceSql");
        // IT1003 end

        if (highLevel == null || "".equals(highLevel)) {
            highLevel = "-1";
        }
        if (ismoney == null || "".equals(ismoney)) {
            ismoney = "-1";
        }
        if (saveType == null) {
            saveType = "new"; // default is 'new'
        }

        field.setFormSystemId(Integer.parseInt(formSystemId));
        field.setSectionId(sectionId);
        field.setFieldId(fieldId);
        field.setFieldLabel(fieldLabel);
        field.setHighLevel(Integer.parseInt(highLevel));
        // IT0958 begin
        field.setIsMoney(Integer.parseInt(ismoney));
        field.setIsSingleRow(Integer.parseInt(issinglrow));
        // IT0958 end
        field.setDefaultValue(defaultValue);
        field.setClickEvent(clickEvent);
        field.setDbclickEvent(dbclickEvent);
        field.setOnfocusEvent(onfocusEvent);
        field.setLostfocusEvent(lostfocusEvent);
        field.setChangeEvent(changeEvent);
        field.setIsReadonly(Integer.parseInt(isReadonly));
        field.setIsDisabled(Integer.parseInt(isDisabled));
        field.setFieldDataSrSQL(fieldDataSrSQL);

        if (fieldType != null) {
            field.setFieldType(Integer.parseInt(fieldType));
        } else {
            field.setFieldType(1);
        }
        if (dataType != null) {
            field.setDataType(Integer.parseInt(dataType));
        } else {
            field.setFieldType(1);
        }

        if ("true".equals(isRequired)) {
            field.setIsRequired(true);
        } else {
            field.setIsRequired(false);
        }

        // ����� document.all['controlType'].value=='1'�����ı���document.all['controlType'].value=='2'�����ı���
        // document.all['controlType'].value=='5'�����֣� document.all['controlType'].value=='3'�����ڣ���������ÿؼ��Ŀ���
        if (field.getFieldType() == 1 || field.getFieldType() == 2 || field.getFieldType() == 3
                || field.getFieldType() == 5) {
            String controlsWidth = (String) request.getParameter("controlsWidth");
            if (controlsWidth != null && !"".equals(controlsWidth)) {
                field.setControlsWidth(Integer.parseInt(controlsWidth));
            } else {
                field.setControlsWidth(0);
            }
        } else {
            field.setControlsWidth(0);
        }

        // �����document.all['controlType'].value=='2'�����ı��������������߶�
        if (field.getFieldType() == 2) {
            String controlsHeight = (String) request.getParameter("controlsHeight");
            if (controlsHeight != null && !"".equals(controlsHeight)) {
                field.setControlsHeight(Integer.parseInt(controlsHeight));
            } else {
                field.setControlsHeight(0);
            }
        } else {
            field.setControlsHeight(0);
        }
        // �����
        // document.all['controlType'].value=='1'�����ı���document.all['controlType'].value=='2'�����ı���document.all['controlType'].value=='5'�����֣�
        if (field.getFieldType() == 1 || field.getFieldType() == 2 || field.getFieldType() == 3
                || field.getFieldType() == 5 || field.getFieldType() == 4 || field.getFieldType() == 6
                || field.getFieldType() == 12) {
            String fieldComments = CommonUtil.decoderURL((String) request.getParameter("fieldComments"));
            if (fieldComments != null && !"".equals(fieldComments)) {
                field.setFieldComments(fieldComments);
            } else {
                field.setFieldComments("");
            }
        } else {
            field.setFieldComments("");
        }
        // �����document.all['controlType'].value=='9'��Comments)
        // IT0958 begin
        if (field.getFieldType() == 9) {
            String strcommentContent = CommonUtil.decoderURL((String) request.getParameter("commentContent"));
            if (strcommentContent != null && !"".equals(strcommentContent)) {
                field.setCommentContent(strcommentContent);
            } else {
                field.setCommentContent("");
            }
        } else {
            field.setCommentContent("");
        }
        // IT0958 end

        if (length != null) {
            field.setFieldLength(Integer.parseInt(length));
        } else {
            field.setFieldLength(10); // default is 10
        }

        // String[] optionValues = null;
        // String[] optionLabels = null;

        // if(field.getFieldType()==4 || field.getFieldType()==6){ // select checkbox
        // optionValues = request.getParameterValues("optionValue");
        // optionLabels = request.getParameterValues("optionLabel");
        if (field.getFieldType() == 5) { // number
            if (request.getParameter("decimalDigits") != null) {
                field.setDecimalDigits(Integer.parseInt((String) request.getParameter("decimalDigits")));
            }
        }

        if (field.getFieldType() == 15) { // number
        	field.setReportType((String)request.getParameter("reportType"));
            if ("O".equalsIgnoreCase((String)request.getParameter("reportType"))) {
            	field.setReportSystemId(Integer.parseInt(request.getParameter("reportSystemId")));
            }
        }
        if (request.getParameter("orderId") != null) {
            field.setOrderId(Integer.parseInt((String) request.getParameter("orderId")));
        }
        PrintWriter out = response.getWriter();
        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            dbManager.startTransaction();

            FormManageDAO formDao = new FormManageDAO(dbManager);
            BaseDataDAO baseDataDao = new BaseDataDAO(dbManager);

            if ("new".equals(saveType)) {
                // ��������������ǡ�checkbox����select��������Ҫ���
                // save formField
                formDao.saveFormSecitonField(field);
                // �����form��״̬�ǡ�published(0)���ģ�����Ҫmodify section table������һ�У�
                if ("0".equals(formStatus)) {
                    formDao.addSectionTableColumn(field);
                }
            } else {
                // �����form��״̬�ǡ�published(0)���ģ�����Ҫmodify section table���޸ĸ��е����ԣ�������ʱֻ���޸��䳤�ȣ����ҳ���ֻ�ܱ䳤�����ܼ�С����
                if ("0".equals(formStatus)) {
                    // ���ı��򣬶��ı�������,��ѡһ����ѡ�࣬��Ҫ���
                    if ((field.getFieldType() == 1 || field.getFieldType() == 2 || field.getFieldType() == 5
                            || field.getFieldType() == 4 || field.getFieldType() == 6)) {
                        // ��ȡ���ֶ�֮ǰ�ĳ���
                        FormSectionFieldVO oldField = formDao.getField(field.getFormSystemId(), field.getSectionId(),
                                field.getFieldId());
                        if (oldField.getFieldLength() > field.getFieldLength()) {
                            out.println("length");
                            out.close();
                            return null;
                        }
                    }
                    formDao.alertSectionTableColumn(field);
                }
                formDao.updateFormSectionField(field);
            }
            if (field.getFieldType() == 6 || field.getFieldType() == 4) { // ���������ѡ���checkboxѡ��
                String masterId = (String) request.getParameter("masterId");
                // 1.����ɾ���������������������޸ģ�
                baseDataDao.deleteFieldBaseDataLink(Integer.parseInt(formSystemId), sectionId, fieldId);
                // 2.Ȼ�󱣴��µĹ���
                baseDataDao.addFieldBaseDataLink(Integer.parseInt(masterId), Integer.parseInt(formSystemId), sectionId,
                        fieldId);
            }
            dbManager.commit();
        } catch (Exception e) {
            dbManager.rollback();
            result = "Save fail";
            out.print(result);
            e.printStackTrace();
        } finally {
            dbManager.freeConnection();
        }
        // /=====================have a test first======================================
        if ((field.getFieldType() == 6 || field.getFieldType() == 4)) {
            // refresh the dictionry data
            String masterId = (String) request.getParameter("masterId");
            BaseDataHelper.getInstance().refreshMasterData(Integer.parseInt(masterId), Integer.parseInt(formSystemId),
                    sectionId, fieldId);
        }
        out.print(result);
        if (out != null)
            out.close();
        return null;
    }

    /**
     * ɾ��ָ����section
     * 
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionLocation deleteFormSection(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String formSystemId = (String) request.getParameter("formSystemId");
        String sectionId = (String) request.getParameter("sectionId");
        IDBManager dbManager = null;
        response.setContentType("text/html;charset=GB2312"); // it is very important
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        PrintWriter out = response.getWriter();
        try {
            dbManager = DBManagerFactory.getDBManager();
            dbManager.startTransaction();
            FormManageDAO formDao = new FormManageDAO(dbManager);
            formDao.deleteSection(Integer.parseInt(formSystemId), sectionId);
            dbManager.commit();
            out.print("success");
        } catch (DAOException e) {
            out.println(I18NMessageHelper.getMessage("common.delsecfail"));
            dbManager.rollback();
        } finally {
            if (out != null)
                out.close();
            if (dbManager != null)
                dbManager.freeConnection();
        }
        return null;

    }

    /**
     * ɾ��ָ����field
     * 
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionLocation deleteFormField(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String formSystemId = (String) request.getParameter("formSystemId");
        String sectionId = (String) request.getParameter("sectionId");
        String fieldId = (String) request.getParameter("fieldId");
        IDBManager dbManager = null;
        response.setContentType("text/html;charset=GB2312"); // it is very important
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        PrintWriter out = response.getWriter();
        try {
            dbManager = DBManagerFactory.getDBManager();
            FormManageDAO formDao = new FormManageDAO(dbManager);
            BaseDataDAO baseDao = new BaseDataDAO(dbManager);
            // �����form��״̬�ǡ�published��0����������Ҫ�޸�����Ӧ�ı��ṹ
            String formStatus = (String) request.getParameter("formStatus");// 0---published 1---design
            if ("0".equals(formStatus)) {
                FormSectionFieldVO field = new FormSectionFieldVO();
                field.setFormSystemId(Integer.parseInt(formSystemId));
                field.setSectionId(sectionId);
                field.setFieldId(fieldId);
                formDao.delSectionTableColumn(field);
            }
            // ɾ�����ֶεĶ����¼
            formDao.deleteFormSectionField(Integer.parseInt(formSystemId), sectionId, fieldId);
            // ɾ�����ֶζ�Ӧ����baseData�Ĺ��������ܲ�����ڹ�������ҲҪִ�У�
            baseDao.deleteFieldBaseDataLink(Integer.parseInt(formSystemId), sectionId, fieldId);
            out.print("success");
        } catch (DAOException e) {
            out.print(I18NMessageHelper.getMessage("common.delfieldfail"));
            e.printStackTrace();
        } finally {
            if (out != null)
                out.close();
            dbManager.freeConnection();
        }
        return null;
    }

    /**
     * form������Ϣ����
     * 
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionLocation saveBaseForm(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        FormManageVO form = new FormManageVO();
        String formId = CommonUtil.decoderURL((String) request.getParameter("formId"));
        String formName = CommonUtil.decoderURL((String) request.getParameter("formName"));
        String formDescription = (String) request.getParameter("formDescription");
        String actionType = (String) request.getParameter("actionType");
        String actionMessage = CommonUtil.decoderURL((String) request.getParameter("actionMessage"));
        String orgId = (String) request.getParameter("orgId");
        String formType = (String) request.getParameter("formType");
        String saveType = (String) request.getParameter("saveType");
        // String validation = (String)request.getParameter("validation");
        // String afterSaveUrl = (String)request.getParameter("afterSaveUrl");
        String validation = CommonUtil.decoderURL((String) request.getParameter("validation"));
        String afterSaveUrl = CommonUtil.decoderURL((String) request.getParameter("afterSaveUrl"));
        String submitAlias = CommonUtil.decoderURL((String)request.getParameter("submitAlias"));
        String saveAlias = CommonUtil.decoderURL((String)request.getParameter("saveAlias"));
        formDescription = CommonUtil.decoderURL(formDescription);
        form.setFormId(formId);
        form.setFormName(formName);
        form.setFormType(formType);
        form.setDescription(formDescription);
        form.setActionType(actionType);
        form.setActionMessage(actionMessage);
        form.setOrgId(orgId);
        form.setPre_validation_url(validation);
        form.setAfterSaveUrl(afterSaveUrl);
        form.setSaveAlias(saveAlias);
        form.setSubmitAlias(submitAlias);
        // For fields of tip and highlight
        SpecialFieldVo tipField = null;
        SpecialFieldVo highlightField = null;
        SpecialFieldVo highlightField2 = null;
        SpecialFieldVo highlightField3 = null;

        if ("new".equals(saveType)) {
            form.setStatus("1");
        } else {
            form.setFormSystemId(Integer.parseInt((String) request.getParameter("formSystemId")));
            form.setStatus(request.getParameter("status"));
            // For tip field
            tipField = new SpecialFieldVo();
            tipField.setFormSystemId(Integer.valueOf(form.getFormSystemId()));
            tipField.setFieldType(SpecialFieldVo.TYPE_TIP);
            String tipFieldStr = (String) request.getParameter("tipField");
            if (tipFieldStr != null && !tipFieldStr.equals("")) {
                String[] temp = StringUtil.split(tipFieldStr, "$#*");
                String tipSectionId = temp[0];
                String tipFieldId = temp[1];
                tipField.setSectionId(tipSectionId);
                tipField.setFieldId(tipFieldId);
            }

            // For highlight fields
            highlightField = new SpecialFieldVo();
            highlightField.setFormSystemId(Integer.valueOf(form.getFormSystemId()));
            highlightField.setFieldType(SpecialFieldVo.TYPE_HIGHLIGHT_1);
            String highlightFieldStr = (String) request.getParameter("highlightField");
            if (highlightFieldStr != null && !highlightFieldStr.equals("")) {
                String[] temp = StringUtil.split(highlightFieldStr, "$#*");
                String highlightSectionId = temp[0];
                String highlightFieldId = temp[1];
                highlightField.setSectionId(highlightSectionId);
                highlightField.setFieldId(highlightFieldId);
            }

            highlightField2 = new SpecialFieldVo();
            highlightField2.setFormSystemId(Integer.valueOf(form.getFormSystemId()));
            highlightField2.setFieldType(SpecialFieldVo.TYPE_HIGHLIGHT_2);
            String highlightFieldStr2 = (String) request.getParameter("highlightField2");
            if (highlightFieldStr2 != null && !highlightFieldStr2.equals("")) {
                String[] temp = StringUtil.split(highlightFieldStr2, "$#*");
                String highlightSectionId = temp[0];
                String highlightFieldId = temp[1];
                highlightField2.setSectionId(highlightSectionId);
                highlightField2.setFieldId(highlightFieldId);
            }

            highlightField3 = new SpecialFieldVo();
            highlightField3.setFormSystemId(Integer.valueOf(form.getFormSystemId()));
            highlightField3.setFieldType(SpecialFieldVo.TYPE_HIGHLIGHT_3);
            String highlightFieldStr3 = (String) request.getParameter("highlightField3");
            if (highlightFieldStr3 != null && !highlightFieldStr3.equals("")) {
                String[] temp = StringUtil.split(highlightFieldStr3, "$#*");
                String highlightSectionId = temp[0];
                String highlightFieldId = temp[1];
                highlightField3.setSectionId(highlightSectionId);
                highlightField3.setFieldId(highlightFieldId);
            }
        }

        PrintWriter out = response.getWriter();
        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            FormManageDAO formDao = new FormManageDAO(dbManager);
            if ("new".equals(saveType)) {
                formDao.saveBaseForm(form);
            } else {
                formDao.updateBaseForm(form);
            }

            // For tip field
            if (tipField != null) {
                formDao.saveSpecialField(tipField);
            }
            // For highlight fields
            if (highlightField != null) {
                formDao.saveSpecialField(highlightField);
            }
            if (highlightField2 != null) {
                formDao.saveSpecialField(highlightField2);
            }
            if (highlightField3 != null) {
                formDao.saveSpecialField(highlightField3);
            }

            out.println("successfully");
        } catch (DAOException e) {
            out.println("unsuccessfully");
            e.printStackTrace();
        } finally {
            if (out != null)
                out.close();
            dbManager.freeConnection();
        }
        return null;
    }

    /**
     * ����ĳ�������ϵ�form
     * 
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionLocation publishForm(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String returnLabel = "Publish form successfully!";
        String[] formIds = request.getParameterValues("formSystemId");
        IDBManager dbManager = null;
        response.setContentType("text/html;charset=GB2312"); // it is very important
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        PrintWriter out = response.getWriter();
        try {
            dbManager = DBManagerFactory.getDBManager();
            FormManageDAO formDao = new FormManageDAO(dbManager);
            FormManageVO form = null;
            for (int i = 0; i < formIds.length; i++) {
                form = formDao.getFormBySystemId(Integer.parseInt(formIds[i]));
                if (formDao.checkForm(Integer.parseInt(formIds[i])) == false) {
                    out.print("(" + form.getFormId() + ")-" + I18NMessageHelper.getMessage("common.formpublishtip"));
                    return null;
                }
                // if the form already have been published, it can't be published again

                if ("0".equals(form.getStatus())) {
                    out.print("(" + form.getFormId() + ")-" + I18NMessageHelper.getMessage("common.formpubed"));
                    return null;
                }
            }
            //
            dbManager.startTransaction();
            for (int i = 0; i < formIds.length; i++) {
                formDao.publishForm(Integer.parseInt(formIds[i]));
            }
            out.print(returnLabel);
            dbManager.commit();
        } catch (DAOException e) {
            dbManager.rollback();
            e.printStackTrace();
            returnLabel = "Fail to publish form!";
            out.print(returnLabel);
        } finally {
            if (out != null)
                out.close();
            dbManager.freeConnection();
        }
        return null;
    }

    /**
     * ����ָ����form
     * 
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionLocation disableForm(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String returnLabel = "Disable form successfully!";
        String[] formIds = request.getParameterValues("formSystemId");
        IDBManager dbManager = null;
        response.setContentType("text/html;charset=GB2312"); // it is very important
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        PrintWriter out = response.getWriter();
        try {
            dbManager = DBManagerFactory.getDBManager();
            FormManageDAO formDao = new FormManageDAO(dbManager);
            dbManager.startTransaction();
            for (int i = 0; i < formIds.length; i++) {
                formDao.disableForm(Integer.parseInt(formIds[i]));
            }
            out.print(returnLabel);
            dbManager.commit();
        } catch (DAOException e) {
            dbManager.rollback();
            e.printStackTrace();
            returnLabel = I18NMessageHelper.getMessage("common.faildisableform");
            out.print(returnLabel);
        } finally {
            if (out != null)
                out.close();
            if (dbManager != null)
                dbManager.freeConnection();
        }
        return null;
    }

    /**
     * Get form/global java script. 2013-10-21 Change to memory cache
     */
    public ActionLocation getFormScript(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String returnLabel = "getFormScript";
        String formSystemId = request.getParameter("formSystemId");
        String script = "";

        IDBManager dbManager = null;
        try {
            /*
             * dbManager = DBManagerFactory.getDBManager(); FormManageDAO formDao = new FormManageDAO(dbManager); script
             * = formDao.getFormScript(Integer.parseInt(formSystemId));
             */
            script = FormScriptHelper.getInstance().getScript(formSystemId);
            request.setAttribute("script", script);
            /*
             * } catch (DAOException ex) { ex.printStackTrace(); returnLabel = "fail";
             */
        } finally {
            if (dbManager != null) {
                dbManager.freeConnection();
            }
        }
        return mapping.findActionLocation(returnLabel);
    }

    /**
     * Edit form/global java script.
     */
    public ActionLocation editFormScript(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String returnLabel = "editFormScript";
        String formSystemId = request.getParameter("formSystemId");
        String script = "";

        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            FormManageDAO formDao = new FormManageDAO(dbManager);
            script = formDao.getFormScript(Integer.parseInt(formSystemId));
            request.setAttribute("script", script);
        } catch (DAOException ex) {
            ex.printStackTrace();
            returnLabel = "fail";
        } finally {
            if (dbManager != null) {
                dbManager.freeConnection();
            }
        }
        return mapping.findActionLocation(returnLabel);
    }

    /**
     * Save form/global java script. 2013-10-21 Add refreshing memory cache
     */
    public ActionLocation saveFormScript(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String returnLabel = "saveFormScript";
        String formSystemId = request.getParameter("formSystemId");
        String script = request.getParameter("script");
        script = script == null ? "" : script.trim();
        System.out.println("script: " + script.length());

        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            dbManager.startTransaction();
            FormManageDAO formDao = new FormManageDAO(dbManager);
            formDao.saveFormScript(Integer.parseInt(formSystemId), script);
            dbManager.commit();
            request.setAttribute("script", script);
            FormScriptHelper.getInstance().refresh();
        } catch (DAOException ex) {
            dbManager.rollback();
            ex.printStackTrace();
            returnLabel = "fail";
        } finally {
            if (dbManager != null) {
                dbManager.freeConnection();
            }
        }
        return mapping.findActionLocation(returnLabel);
    }

    /**
     * ��ת��ָ��form������дҳ��(ֻ����ʾ��form���ֶι��ɣ�׼��������дform����)
     * 
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionLocation displayFormFill(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String returnLabel = "success";
        // ��ȡָ��form�ı�ʶ
        String formSystemId = request.getParameter("formSystemId");

        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            String dateFormat = ParamConfigHelper.getInstance().getParamValue("dateFormat", "MM/dd/yyyy");
            request.setAttribute("dateFormat", dateFormat.toLowerCase().replace("mm", "MM"));
            FormManageDAO formDao = new FormManageDAO(dbManager);
            FormManageVO form = formDao.getFormBySystemId(Integer.parseInt(formSystemId));
            // ��ȡ��form����ת�����п����޸ĵ�section
            // HashMap updateSectionFieldMap = formDao.getCanUpdateSectionFieldMap(Integer.parseInt(formSystemId));
            // request.setAttribute("updateSectionFieldMap",updateSectionFieldMap);
            HashMap onlyFillSectionFieldMap = formDao.getOnlyFillSectionFieldMap(Integer.parseInt(formSystemId));           
            request.setAttribute("onlyFillSectionFieldMap", onlyFillSectionFieldMap);
            
            HashMap hiddenSectionFieldMap = formDao.getHiddenSectionFieldMap(Integer.parseInt(formSystemId),0);           
            request.setAttribute("hiddenSectionFieldMap", hiddenSectionFieldMap);
                               
            request.setAttribute("listForm", form);

            // 1.���Ҹ�form���ڼ���section
            Collection sectionList = form.getSectionList();
            Iterator sectionIt = sectionList.iterator();
            Iterator tempIt = sectionList.iterator();
            FormSectionVO tempSection = (FormSectionVO) tempIt.next();

        } catch (DAOException e) {
            e.printStackTrace();
            returnLabel = "fail";
        } finally {
            if (dbManager != null)
                dbManager.freeConnection();
        }

        return mapping.findActionLocation(returnLabel);
    }

    /**
     * ��ȡ����form�и�ʽΪtable��section��һ��
     * 
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionLocation getRowForFormTableSection(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String formSystemId = (String) request.getParameter("formSystemId");
        String sectionId = (String) request.getParameter("sectionId");
        String sectionType = "01"; // table
        IDBManager dbManager = null;
        response.setContentType("text/html;charset=GB2312"); // it is very important
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        PrintWriter out = response.getWriter();
        try {
            dbManager = DBManagerFactory.getDBManager();
            FormManageDAO formDao = new FormManageDAO(dbManager);
            Collection sectionFieldList = formDao.getSectionFieldListByForm(Integer.parseInt(formSystemId), sectionId,
                    sectionType);
            out.print("<tbody id='tbDetailPrepare" + sectionId + "'>");
            if (sectionFieldList != null && sectionFieldList.size() > 0) {
                Iterator it1 = sectionFieldList.iterator();
                out.print("<tr>");
                int iFlag = 0;
                int rowIndex = 0;
                double columnWidth = 100 / sectionFieldList.size();
                while (it1.hasNext()) {
                    FormSectionFieldVO field = (FormSectionFieldVO) it1.next();
                    if (field.getFieldType() == CommonName.FIELD_TYPE_IDENTITY) {
                        continue;
                    }
                    if (iFlag == 0) {
                        out.print("<td align='center'><input type='checkbox' name='chkid' value='" + field.getFieldId()
                                + "'></td>");
                    }
                    // out.print("<td align='left'><input type='text' name='"+field.getFieldId()+"'></td>");
                    out.print("<td align='left' width='" + columnWidth
                            + "%' style='word-wrap:break-word;table-layout:fixed;'>");
                    // �����section����Ҫ����ת�����޸ĵģ�����Ҫrequestor������ʱ��д
                    // if(updateSectionMap!=null && updateSectionMap.containsKey(section.getSectionId())){
                    // out.println(FieldControlHelper.ShowDisabledField(field));
                    // }else{

                    out.print(FieldControlHelper.createControl(field, sectionType, rowIndex));
                    // }
                    out.print("</td>");
                    iFlag++;
                }
                out.print("</tr>");
            }
            out.print("</tbody>");
        } catch (DAOException e) {
            out.print("fail");
            e.printStackTrace();
        } finally {
            if (out != null)
                out.close();
            if (dbManager != null)
                dbManager.freeConnection();
        }
        return null;
    }

    /**
     * ��ת��ָ��form������ʾҳ�棨���ñ������ֶ�����ֶ��Ѿ�����ֵ��ʾ������
     * 
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionLocation displayFormContent(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String returnLabel = "listFormContent";
        // ��ȡָ��form���͵ı�ʶ
        String formSystemId = request.getParameter("formSystemId");
        if (formSystemId == null) {
            formSystemId = (String) request.getAttribute("formSystemId");
        }
        // ��ȡ�ñ���ʵ���ı�ʶ
        String requestNo = (String) request.getParameter("requestNo");
        String operateType = (String) request.getParameter("operateType");

        if (operateType == null) {
            operateType = "view";
            request.setAttribute("operateType", operateType);
        }

        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            FormManageDAO formDao = new FormManageDAO(dbManager);
            String dateFormat = ParamConfigHelper.getInstance().getParamValue("dateFormat", "MM/dd/yyyy");
            request.setAttribute("dateFormat", dateFormat.toLowerCase().replace("mm", "MM"));
            HashMap sectionFieldMap = formDao.getFieldContentByForm(requestNo);
            request.setAttribute("sectionFieldMap", sectionFieldMap);
            WorkFlowProcessDAO processDao = new WorkFlowProcessDAO(dbManager);
            // ��ȡ���еĴ����켣��¼
            Collection traceList = processDao.getProcessTraceList(requestNo, true);

            // ��ȡ��ǰ�Ĵ���ʵ����¼
            WorkFlowProcessVO processVo = processDao.getProcessVO(requestNo);
            StaffVO staff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);

            if (processVo == null) {
                request.setAttribute("error", "This form (" + requestNo + ") doesn't exist.");
                return mapping.findActionLocation("fail");
            }

            if (formSystemId == null || "".equals(formSystemId)) {
                formSystemId = "" + formDao.getFormManageVObyRequestNo(requestNo).getFormSystemId();
            }
            System.out.println(formSystemId);
            FormManageVO form = formDao.getFormBySystemId(Integer.parseInt(formSystemId));
            request.setAttribute("listForm", form);
            
            HashMap hiddenSectionFieldMap = formDao.getHiddenSectionFieldMap(Integer.parseInt(formSystemId),Integer.parseInt(processVo.getNodeId()));           
            request.setAttribute("hiddenSectionFieldMap", hiddenSectionFieldMap);

            // �����ǰ�ڵ��ǵ�һ���ڵ㣬����Ҫ
            if ("0".equals(processVo.getNodeId())) {
                // HashMap updateSectionFieldMap = formDao.getCanUpdateSectionFieldMap(Integer.parseInt(formSystemId));
                // request.setAttribute("updateSectionFieldMap",updateSectionFieldMap);
                // ��ȡ��form����ת�����вſ��������sectionField
                HashMap onlyFillSectionFieldMap = formDao.getOnlyFillSectionFieldMap(Integer.parseInt(formSystemId));
                request.setAttribute("onlyFillSectionFieldMap", onlyFillSectionFieldMap);
            }

            // ���operateType=reject��ʾ��ͨ��reject�������͵�email�е�link�򿪸�form����ʱ�����������requester������requester��submitted
            // by����ͬһ���ˣ�����
            // ��formֻ����view
            if ("reject".equals(operateType)) {
                if (!processVo.getSubmittedBy().equals((processVo.getRequestStaffCode()))
                        && staff.getStaffCode().equals(processVo.getRequestStaffCode())) {
                    request.setAttribute("noUpdate", "true");
                }
            }

            // ����ǽ��зǲ鿴�Ĳ���ʱ����Ҫ֪����һ���ڵ��ǲ��ǽ����ڵ㣨����ǣ���ǰ�ڵ������һ�������ڵ㣩
            if (!"view".equals(operateType) && !"reject".equals(operateType)) {
                // �����ר�Ҹ����飬����Ҫ��鵱ǰ�ڵ������ר���Ƿ��Ѿ���������Ƿ񻹰�����ǰ�����ߣ���������������������ʾ��ҳ�棻���򣬲���ʾ��ҳ��
                if ("advise".equals(operateType)) {
                    if (processVo.getInvitedExpert() == null || "".equals(processVo.getInvitedExpert())
                            || processVo.getInvitedExpert().indexOf(staff.getStaffCode()) < 0) {
                        request.setAttribute(CommonName.COMMON_TIP_INFO,
                                I18NMessageHelper.getMessage("common.notinvited"));
                        return mapping.findActionLocation("fail");
                    } else {// �����뽨����Ҳ���Կ�����Ҫ�޸ĵ�field
                        WorkFlowNodeDAO nodeDao = new WorkFlowNodeDAO(dbManager);
                        WorkFlowItemVO currentNode = nodeDao.getNodeByFormId(Integer.parseInt(formSystemId),
                                processVo.getNodeId());
                        // ��ȡ�ýڵ�����޸ĵ�field
                        // System.out.println("currentNode="+currentNode.getItemId());
                        request.setAttribute("updateSectionFields", currentNode.getUpdateSections());
                        // ��ȡֻ�иýڵ�ſ��������field
                        request.setAttribute("newSectionFields", currentNode.getFillSectionFields());
                    }
                } else {
                    if (processVo.getCurrentProcessor().indexOf(staff.getStaffCode()) > -1) {
                        WorkFlowNodeDAO nodeDao = new WorkFlowNodeDAO(dbManager);
                        WorkFlowItemVO nextNode = nodeDao.getNextNodeById(Integer.parseInt(formSystemId),
                                processVo.getNodeId(), requestNo, staff.getStaffCode());
                        if ("-1".equals(nextNode.getItemId())) {
                            request.setAttribute("lastNode", "true");
                        } else {
                            request.setAttribute("lastNode", "false");
                        }
                        if (!"1".equals(processVo.getOpenFlag())) {
                            // update the open_flag to 1
                            nodeDao.updateNodeOpenFlag(requestNo, "1");
                        }

                        WorkFlowItemVO currentNode = nodeDao.getNodeByFormId(Integer.parseInt(formSystemId),
                                processVo.getNodeId());
                        // ��ȡ�ýڵ�����޸ĵ�field
                        // System.out.println("currentNode="+currentNode.getItemId());
                        request.setAttribute("updateSectionFields", currentNode.getUpdateSections());
                        // ��ȡֻ�иýڵ�ſ��������field
                        request.setAttribute("newSectionFields", currentNode.getFillSectionFields());
                    } else {
                        // �����ǰ����״̬Ϊ��00�������ʾ��form�Ѿ��������ߡ�withdraw����
                        if ("00".equals(processVo.getStatus())) {
                            request.setAttribute(CommonName.COMMON_TIP_INFO,
                                    "(" + requestNo + ")-" + I18NMessageHelper.getMessage("common.formwithrawn"));
                            return mapping.findActionLocation("fail");
                        } else if (processVo.getCcStaffs() != null
                                && processVo.getCcStaffs().indexOf(staff.getStaffCode()) > -1) {
                            request.setAttribute("lastNode", "false");
                            request.setAttribute("ccStaffView", "true");
                        } else {
                            // request.setAttribute("error","You have no authority to process the form ("+requestNo+").");
                            // request.setAttribute(CommonName.COMMON_TIP_INFO,"("+requestNo+")-"+I18NMessageHelper.getMessage("common.formnotinnode")
                            // );
                            request.setAttribute(CommonName.COMMON_TIP_INFO,
                                    I18NMessageHelper.getMessage("common.formnotinnode", new String[] {requestNo}));
                            request.setAttribute("returnType", "close");
                            return mapping.findActionLocation("fail");
                        }
                    }
                }
            }
            request.setAttribute("processVo", processVo);
            request.setAttribute("traceList", traceList);
        } catch (DAOException e) {
            e.printStackTrace();
            returnLabel = "fail";
        } finally {
            dbManager.freeConnection();
        }

        return mapping.findActionLocation(returnLabel);
    }

    /**
     * Edit the form created by service for 3rd party system.
     */
    public ActionLocation editForm(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        if (!ServiceAction.authenticate(request, response)) {
            request.setAttribute("error", ServiceAction.AUTHEN_ERROR);
            return mapping.findActionLocation("fail");
        }
        String requestNo = StringUtil.decoderURL(request.getParameter("requestNo"));
        if (requestNo == null || requestNo.equals("")) {
            request.setAttribute("error", "Request No. cannot be null.");
            return mapping.findActionLocation("fail");
        }

        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            WorkFlowProcessDAO processDao = new WorkFlowProcessDAO(dbManager);
            WorkFlowProcessVO process = processDao.getProcessVO(requestNo);
            if (process == null) {
                request.setAttribute("error", "This form (" + requestNo + ") doesn't exist.");
                return mapping.findActionLocation("fail");
            }
            if (!process.getStatus().equals(CommonName.STATUS_DRAFT)) {
                request.setAttribute("error", "This form (" + requestNo + ") has been submitted.");
                return mapping.findActionLocation("fail");
            }
            request.setAttribute("formSystemId", Integer.toString(process.getFormSystemId()));
            StaffVO staff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
            if (staff == null) {
                StaffDAO staffDao = new StaffDAO(dbManager);
                EflowStaffVO eStaff = staffDao.getActiveStaffByStaffCode(process.getSubmittedBy());
                if (eStaff == null) {
                    request.setAttribute("error",
                            "Cannot find submitter for the staff code: " + process.getSubmittedBy());
                    return mapping.findActionLocation("fail");
                }
                staff = staffDao.getStaffByLogonId(eStaff.getLogonId());
                if (staff == null) {
                    staff = staffDao.getStaffByDefaultRoel(eStaff.getLogonId());
                }
                CompanyDAO companyDao = new CompanyDAO(dbManager);
                staff.setOwnCompanyList(companyDao.getCompanyList(staff.getOrgId()));
                staff.setUpperCompanys(companyDao.getSuperCompanys(staff.getOrgId()));
                String lowerCompanyIds = "'" + staff.getOrgId() + "'";
                String temp = companyDao.getSubCompanys(staff.getOrgId());
                if (temp != null && !"".equals(temp)) {
                    lowerCompanyIds = lowerCompanyIds + "," + temp;
                }
                staff.setLowerCompanys(lowerCompanyIds);
                TeamDAO teamDao = new TeamDAO(dbManager);
                Collection teamList = teamDao.getTeamListByCompany(staff.getOrgId());
                staff.setTeamList(teamList);
                request.getSession().setAttribute(CommonName.CURRENT_STAFF_INFOR, staff);
                request.getSession().setAttribute(LogonFilter.NONE_SSO, "Y");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return mapping.findActionLocation("fail");
        } finally {
            if (dbManager != null) {
                dbManager.freeConnection();
            }
        }
        return this.displayFormContent(mapping, request, response);
    }

    /**
     * 
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionLocation exportPDF(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // ��ȡָ��form���͵ı�ʶ
        String formSystemId = request.getParameter("formSystemId");
        // ��ȡ�ñ���ʵ���ı�ʶ
        String requestNo = (String) request.getParameter("requestNo");
        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            FormManageDAO formDao = new FormManageDAO(dbManager);
            FormManageVO form = formDao.getFormBySystemId(Integer.parseInt(formSystemId));
            HashMap sectionFieldMap = formDao.getFieldContentByForm(requestNo);
            // request.setAttribute("listForm",form);
            // request.setAttribute("sectionFieldMap",sectionFieldMap);
            WorkFlowProcessDAO processDao = new WorkFlowProcessDAO(dbManager);
            // ��ȡ���еĴ����켣��¼
            Collection traceList = processDao.getProcessTraceList(requestNo, true);
            // ��ȡ��ǰ�Ĵ���ʵ����¼
            WorkFlowProcessVO processVo = processDao.getProcessVO(requestNo);

            DocumentException ex = null;
            ByteArrayOutputStream baosPDF = null;
            try {
                baosPDF = ExportFileUtil.exportPDF(form, sectionFieldMap, traceList);

                StringBuffer sbFilename = new StringBuffer();
                // sbFilename.append("filename_");
                // sbFilename.append(System.currentTimeMillis());
                sbFilename.append(requestNo);
                sbFilename.append(".pdf");

                response.setHeader("Cache-Control", "max-age=30");
                response.setContentType("application/pdf");

                StringBuffer sbContentDispValue = new StringBuffer();
                if (!CompanyHelper.getInstance().getEFlowCompany().equals(CompanyHelper.EFlow_AIA)) {
                    sbContentDispValue.append("inline");
                } else {
                    sbContentDispValue.append("attachment");
                }
                sbContentDispValue.append("; filename=");
                sbContentDispValue.append(sbFilename);

                response.setHeader("Content-disposition", sbContentDispValue.toString());

                response.setContentLength(baosPDF.size());
                ServletOutputStream sos;
                sos = response.getOutputStream();

                baosPDF.writeTo(sos);
                sos.flush();

            } catch (DocumentException dex) {
                response.setContentType("text/html");
                PrintWriter writer = response.getWriter();
                writer.println(this.getClass().getName() + " caught an exception: " + dex.getClass().getName() + "<br>");
                writer.println("<pre>");
                dex.printStackTrace(writer);
                writer.println("</pre>");
            } finally {
                if (baosPDF != null) {
                    baosPDF.reset();
                }
            }

        } catch (DAOException e) {
            e.printStackTrace();
        } finally {
            dbManager.freeConnection();
        }
        return null;
    }

    /**
     * �ڴ��������У���Ȩ�޵������������޸���form���ݺ󣬽��еı��������ֻ��Ҫ������Ȩ���޸ĵ�sectionField��
     * 
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionLocation updateFormFill(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        // response.setHeader("Cache-Control", "no-cache");
        // response.setHeader("Pragma", "no-cache");
        // PrintWriter out = response.getWriter();

        String formSystemId = (String) request.getParameter("formSystemId");
        // ��ȡ�ڸýڵ�����޸ĵ�sectionFields
        String updateSectionFields = (String) request.getParameter("updateSections");
        // ��ȡ�ڸýڵ���������sectionFields
        String newSectionFields = (String) request.getParameter("newSectionFields");
        
        boolean isAJAX = false;
        if ("1".equals(request.getParameter("checkOptionalNode")))
            isAJAX = true;

        updateSectionFields = updateSectionFields + newSectionFields;

        updateSectionFields = updateSectionFields.trim().toUpperCase();

        // ��ȡ�����е�field��request_no
        FormSectionFieldVO requestVo = SystemFieldHelper.getInstance().getBasicFieldById(
                CommonName.SYSTEM_ID_REQUEST_NO);
        String requestNo = (String) request.getParameter(requestVo.getFieldId());

        String[] sectionFields = StringUtil.split(updateSectionFields, ",");
        if (sectionFields == null || sectionFields.length == 0) {
            request.setAttribute(CommonName.COMMON_TIP_INFO, I18NMessageHelper.getMessage("common.nofieldupdate"));
            return mapping.findActionLocation("fail");
        }
        IDBManager dbManager = null;

        try {
            dbManager = DBManagerFactory.getDBManager();
            // dbManager.startTransaction();
            FormManageDAO formDao = new FormManageDAO(dbManager);

            // ����ô���ʵ���Ѿ������ڵ�ǰ�ڵ㣬�������ٴ�update����
            String currentNodeId = (String) request.getParameter("currentNodeId");
            WorkFlowProcessDAO processDao = new WorkFlowProcessDAO(dbManager);
            WorkFlowProcessVO process = processDao.getProcessVO(requestNo);
            if (process != null && !currentNodeId.equals(process.getNodeId())) {
                request.setAttribute(CommonName.COMMON_TIP_INFO, I18NMessageHelper.getMessage("common.frmnotupdated"));
                return mapping.findActionLocation("fail");
            }

            FormManageVO form = formDao.getFormBySystemId(Integer.parseInt(formSystemId));
            Collection sectionList = form.getSectionList();
            Iterator sectionIt = sectionList.iterator();
            boolean canUpdatedFlag = false;

            dbManager.startTransaction();
            while (sectionIt.hasNext()) {
                FormSectionVO section = (FormSectionVO) sectionIt.next();
                Collection fieldList = section.getFieldList();
                Iterator fieldIt = fieldList.iterator();
                HashMap fieldMap = new HashMap();
                while (fieldIt.hasNext()) {
                    FormSectionFieldVO field = (FormSectionFieldVO) fieldIt.next();
                    fieldMap.put(field.getFieldId().toLowerCase(), field);
                }
                StringBuffer fieldStr = new StringBuffer("");
                StringBuffer valueStr = new StringBuffer("");
                // ����section�Ƿ�����޸�
                for (int i = 0; i < sectionFields.length; i++) {
                    if (sectionFields[i].toUpperCase().indexOf(section.getSectionId().toUpperCase()) > -1) {
                        canUpdatedFlag = true;
                        break;
                    }
                }

                if (canUpdatedFlag) {
                    if ("01".equals(section.getSectionType())) { // 01 -- ������ʽ�������ύ��������
                        String[] columnNames = dbManager.getTableColumnNames(section.getTableName());
                        int count = 0;
                        for (int i = 0; i < columnNames.length; i++) {
                            if ("request_no".equals(columnNames[i])) {
                                continue;
                            }
                            String[] columnValues = request.getParameterValues(columnNames[i]);
                            // System.out.println("-----columnName="+columnNames[i]+",count="+columnValues.length);
                            if (columnValues != null) {
                                count = columnValues.length;
                                break;
                            }
                        }
                        // ���ڱ������ݣ���ɾ��֮ǰ���м�¼���ٲ������м�¼(��Ϊ��ҳ���Ͽ��Դ�����section�����ֶε�ֵ����������Ҫ�޸ĵ��ֶΣ���ͨ�������ֶεķ�ʽ�����������Կ�����ɾ������
                        // �����µ�)
                        String delSQL = "delete from " + section.getTableName() + " where request_no='" + requestNo
                                + "'";
                        formDao.executeSQL(delSQL);
                        for (int i = 0; i < count; i++) {
                            for (int j = 0; j < columnNames.length; j++) {
                                if ("id".equals(columnNames[j])) {
                                    continue;
                                }
                                if (!requestVo.getFieldId().equals(columnNames[j])) {
                                    FormSectionFieldVO field = (FormSectionFieldVO) fieldMap.get(columnNames[j]
                                            .toLowerCase());
                                    // �����checkbox������Ҫ��������
//                                    if (field.getFieldType() == 6) {
//                                        String columnName = "";
//                                        // ����ǵ�һ��record����Ҫ�Ӻ�׺
//                                        if (i == 0) {
//                                            columnName = columnNames[j];
//                                        } else {
//                                            columnName = columnNames[j] + "_" + i;
//                                        }
//                                        if (request.getParameterValues(columnName) != null) {
//                                            fieldStr.append(columnNames[j]).append(",");
//                                            String tempValue = "";
//                                            tempValue = getSpelledValues(columnName, request);
//                                            if (!"".equals(tempValue)) {
//                                                valueStr.append("'" + tempValue).append("',");
//                                            } else {
//                                                valueStr.append("'" + tempValue).append("',");
//                                            }
//                                        }
//                                    } else {// ��checkbox
                                        if (request.getParameterValues(columnNames[j]) != null) {
                                            if (!"".equals(request.getParameterValues(columnNames[j])[i])) {
                                                fieldStr.append(columnNames[j]).append(",");
                                                
                                                String temp = this.decdr(request.getParameterValues(columnNames[j])[i],
                                                        isAJAX);
                                                temp = StringUtil.replace(temp, "&#39;", "'");
                                                temp = StringUtil.replace(temp, "'", "''");
                                                if (field.getFieldType() == CommonName.FIELD_TYPE_NUMBER) {
                                                    temp = StringUtil.replace(temp, ",", "");
                                                }
                                                // temp = StringUtil.FormatHtml(temp);
                                                valueStr.append("'" + temp).append("',");
                                            } else {
                                                fieldStr.append(columnNames[j]).append(",");
                                                valueStr.append(" null,");
                                            }
                                        }
                                    //}
                                } else {
                                    // ��д������,request_no�ֶ�ÿ����section����һ�����ֶ�
                                    fieldStr.append(columnNames[j]).append(",");
                                    // valueStr.append("'"+request.getParameterValues(columnNames[j])[0]).append("',");
                                    valueStr.append("'" + requestNo).append("',");
                                }
                            }// end for j
                            if (!"".equals(fieldStr.toString())) {
                                String insertSQL = "insert into " + section.getTableName() + "("
                                        + fieldStr.substring(0, fieldStr.lastIndexOf(",")) + ") values("
                                        + valueStr.substring(0, valueStr.lastIndexOf(",")) + ")";
                                // System.out.println("---sql:"+insertSQL);
                                formDao.executeSQL(insertSQL);
                                fieldStr = new StringBuffer("");
                                valueStr = new StringBuffer("");
                            }
                        }// end for i
                    } else if ("02".equals(section.getSectionType()) || "03".equals(section.getSectionType())) { // end
                        // sectionType="01"
                        String[] columnNames = dbManager.getTableColumnNames(section.getTableName());

                        for (int j = 0; j < columnNames.length; j++) {
                            if (request.getParameter(columnNames[j]) == null
                                    || (requestVo.getFieldId().toUpperCase()).equals(columnNames[j].toUpperCase())) {
                                continue;
                            }
                            // ����ֶδ��ڿ����޸��ֶ��б��У�����ֶ�����Ҫ�޸ĵ�
                            if (updateSectionFields.indexOf((section.getSectionId() + "." + columnNames[j])
                                    .toUpperCase()) < 0) {
                                continue;
                            }

                            FormSectionFieldVO field = (FormSectionFieldVO) fieldMap.get(columnNames[j].toLowerCase());
                            fieldStr.append(columnNames[j]).append("=");
                            String tempValue = this.decdr(getSpelledValues(columnNames[j], request), isAJAX);
                            
                            if (!"".equals(tempValue)) {
                                tempValue = StringUtil.replace(tempValue, "&#39;", "'");
                                tempValue = StringUtil.replace(tempValue, "'", "''");
                                if (field.getFieldType() == CommonName.FIELD_TYPE_NUMBER) {
                                    tempValue = StringUtil.replace(tempValue, ",", "");
                                }
                                fieldStr.append("'" + tempValue).append("',");
                            } else {
                                String temp = (String) this.decdr(request.getParameter(columnNames[j]), isAJAX);
                                temp = StringUtil.replace(temp, "&#39;", "'");
                                temp = StringUtil.replace(temp, "'", "''");
                                if (field.getFieldType() == CommonName.FIELD_TYPE_NUMBER) {
                                    temp = StringUtil.replace(temp, ",", "");
                                }
                                // fieldStr.append("'"+request.getParameter(columnNames[j])).append("',");
                                if ("".equals(temp)) {
                                    fieldStr.append(" null,");
                                } else {
                                    fieldStr.append("'" + temp).append("',");
                                }
                            }
                        }
                        if (fieldStr != null && !"".equals(fieldStr.toString())) {
                            String insertSQL = "update " + section.getTableName() + " set "
                                    + fieldStr.substring(0, fieldStr.lastIndexOf(",")) + " where request_no='"
                                    + requestNo + "'";
                            formDao.executeSQL(insertSQL);
                        }
                    }// end sectionType=��02�� or sectionType="03"
                }
                canUpdatedFlag = false;
            }
            /** IT1247 2011-07 */
            process.setUpdateDate(new Date());
            processDao.saveLastUpdateDate(process);
            /** end */
            dbManager.commit();
            // out.print("success");
            request.setAttribute("updateForm", "true");
            
            if ("1".equals(request.getParameter("checkOptionalNode"))) {
                response.setContentType("text/xml;charset=GBK"); // it is very important
                response.setHeader("Cache-Control", "no-cache");
                response.setHeader("Pragma", "no-cache");
                PrintWriter out = response.getWriter();
                return null;
            }
        } catch (Exception e) {
            dbManager.rollback();
            e.printStackTrace();
            request.setAttribute(CommonName.COMMON_ERROR_INFOR, I18NMessageHelper.getMessage("common.failupdatefield")
                    + e.getMessage());
            // out.print("Fail to update form section.   "+e.getMessage());
            return mapping.findActionLocation("fail");
        } finally {
            if (dbManager != null) {
                dbManager.freeConnection();
            }
        }
        return mapping.findActionLocation("afterUpdateFormSection");
        // return null;
    }

    /**
     * ��ָ��������formɾ������formһ��Ҫ���ڡ�00��Draft��03��Reject״̬��
     * 
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionLocation deleteRequestedForm(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String requestNo = (String) request.getParameter("requestNo");
        IDBManager dbManager = null;
        response.setContentType("text/html;charset=GB2312"); // it is very important
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        PrintWriter out = response.getWriter();
        try {
            dbManager = DBManagerFactory.getDBManager();
            dbManager.startTransaction();
            FormManageDAO formDao = new FormManageDAO(dbManager);

            formDao.deleteRequestedFormContent(requestNo);
            formDao.deleteProcess(requestNo);
            formDao.deleteProcessTrace(requestNo);
            dbManager.commit();
            out.print("success");
        } catch (Exception e) {
            dbManager.rollback();
            out.print(I18NMessageHelper.getMessage("common.faildelform"));
        } finally {
            if (dbManager != null)
                dbManager.freeConnection();
            if (out != null)
                out.close();
        }
        return null;
    }

    /**
     * IT1002 �����������ʽ��sectionʱ������ɾ���Ѿ����ڵļ�¼��Ȼ���ٱ����section�����µļ�¼ ͨ�õ�form�����������ϱ���
     * 
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public synchronized ActionLocation saveFormFill(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        // String returnLabel = "saveSuccess";
        String returnLabel = "successTipPage";
        String formSystemId = (String) request.getParameter("formSystemId");
        // ��ȡ�����е�field��request_no,�������浽��sectionType='3'������section��ȥ
        FormSectionFieldVO requestVo = SystemFieldHelper.getInstance().getBasicFieldById(
                CommonName.SYSTEM_ID_REQUEST_NO);
        String requestNo = (String) request.getParameter(requestVo.getFieldId());
        String saveType = (String) request.getParameter("saveType");
        String isUrgent = "1"; // -----default value'1'----- No (not is urgent)
        boolean isAJAX = false;
        if ("1".equals(request.getParameter("checkOptionalNode")))
            isAJAX = true;
        String DEBUG = "";

        // String referFormLink="";
        String successmessage = "";
        if (saveType == null) {
            saveType = "new";
        }
        // String requestFormDate = request.getParameter("request_date");
        String attachmentIdentity = request.getParameter("attachmentIdentity");
        // if(request.getParameter("reference_form")!=null && !"".equals(request.getParameter("reference_form"))){
        // referFormLink = (String)request.getParameter("reference_form");
        // }
        // ��ȡ�����Ĳ������ͣ�00Ϊ��ʼ��������01Ϊ�ύ����
        String submitType = (String) request.getParameter("submitType");
        if (submitType == null || "".equals(submitType)) {
            submitType = "00";
        }

        // submitted by
        StaffVO staff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);

        // ��ȡrequester
        String requestStaffCode = (String) request.getParameter(CommonName.SYSTEM_ID_REQUEST_STAFF_CODE);
        // ��������ڣ�����Ϊsubmitted by
        if (requestStaffCode == null || "".equals(requestStaffCode)) {
            requestStaffCode = staff.getStaffCode();
        }
        StaffVO requester = StaffTeamHelper.getInstance().getStaffByCode(requestStaffCode);
        String orgName = CompanyHelper.getInstance().getOrgName(requester.getOrgId());

        IDBManager dbManager = null;
        FormManageVO form = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            // dbManager.startTransaction();
            // System.out.println("===== " + requestNo + " ===== saveFormFill - dbManager.startTransaction");
            FormManageDAO formDao = new FormManageDAO(dbManager);
            WorkFlowProcessDAO processDao = new WorkFlowProcessDAO(dbManager);
            WorkFlowProcessTraceVO traceVo = null;

            form = formDao.getFormBySystemId(Integer.parseInt(formSystemId));
            // 1.���Ҹ�form���ڼ���section
            Collection sectionList = form.getSectionList();
            Iterator sectionIt = sectionList.iterator();
            Iterator tempIt = sectionList.iterator();
            FormSectionVO tempSection = (FormSectionVO) tempIt.next();
            String newRequestNo = requestNo;
            // ���������������Ϊ��֤������ȷ�ԣ������»�ȡһ��request_no;
            if (!"update".equals(saveType) || requestNo.equals("")) {
                // || requestNo.equals(request.getSession().getAttribute(this.IS_PENDING_RN))) {
                newRequestNo = FieldUtil.getRequestNo(Integer.parseInt(formSystemId), form.getFormId(),
                        tempSection.getSectionId(), dbManager);
                requestNo = newRequestNo;

            } else if ("01".equals(submitType)) { // �����submit����������Ҫ����Ƿ������ظ��ύ�����磬�ô���ʵ���Ѿ������ڡ�00����draft��״̬,"03"Reject״̬�����������ٴ�submit
                System.out.println("===== " + requestNo + " ===== saveFormFill - dbManager.startTransaction");
                WorkFlowProcessVO process = processDao.getProcessVO(requestNo);
                if (process != null && (!"00".equals(process.getStatus()) && !"03".equals(process.getStatus()))) {
                    request.setAttribute(CommonName.COMMON_TIP_INFO,
                            I18NMessageHelper.getMessage("common.frmsubmitted"));
                    returnLabel = "fail";
                    return mapping.findActionLocation(returnLabel);
                }
            }

            long startTime = System.currentTimeMillis();
            try {
                dbManager.startTransaction();
                while (sectionIt.hasNext()) {
                    FormSectionVO section = (FormSectionVO) sectionIt.next();
                    Collection fieldList = section.getFieldList();
                    Iterator fieldIt = fieldList.iterator();
                    HashMap fieldMap = new HashMap();
                    while (fieldIt.hasNext()) {
                        FormSectionFieldVO field = (FormSectionFieldVO) fieldIt.next();
                        fieldMap.put(field.getFieldId().toLowerCase(), field);
                    }
                    // 2.���ÿ��section���õ�����ӵ�е�field���ֶΣ���id
                    // Collection fieldList = section.getFieldList();
                    StringBuffer fieldStr = new StringBuffer("");
                    StringBuffer valueStr = new StringBuffer("");

                    if ("01".equals(section.getSectionType())) { // 01 -- ������ʽ�������ύ��������
                        // Iterator fieldIt = fieldList.iterator();
                        String[] columnNames = dbManager.getTableColumnNames(section.getTableName());

                        int count = 0;
                        for (int i = 0; i < columnNames.length; i++) {
                            if ("request_no".equals(columnNames[i])) {
                                continue;
                            }
                            String[] columnValues = request.getParameterValues(columnNames[i]);
                            // System.out.println("-----columnName="+columnNames[i]+",count="+columnValues.length);
                            if (columnValues != null) {
                                count = columnValues.length;
                                break;
                            }
                        }

                        // Begin 2007-08-21,������޸Ĳ��������ڱ�����ʽ�ģ���ɾ��֮ǰ���м�¼���ٲ������м�¼
                        if ("update".equals(saveType)) {
                            String delSQL = "delete from " + section.getTableName() + " where request_no='" + requestNo
                                    + "'";
                            // newRequestNo = requestNo;
                            formDao.executeSQL(delSQL);
                            // System.out.println("----------------:"+delSQL);
                            delSQL = "";
                        }
                        // end 2007-08-21

                        // ������޸ģ���update,����INSERT---------------�÷�ʽ����2007-08-21
                        // if(!"update".equals(saveType)){
                        // ��ȡÿ����¼�����ݣ�Ȼ�󱣴�
                        for (int i = 0; i < count; i++) {
                            for (int j = 0; j < columnNames.length; j++) {
                                if ("id".equals(columnNames[j])) {
                                    continue;
                                }
                                System.out.print("\n" + columnNames[j] + '=');
                                if (!requestVo.getFieldId().equals(columnNames[j])) {
                                    FormSectionFieldVO field = (FormSectionFieldVO) fieldMap.get(columnNames[j]
                                            .toLowerCase());
                                    if (field == null)
                                        continue;// �п��ܸ�column�Ѿ��ֹ���teflow_form_section_field�г�ȥ�����ܸ���2011-5-5

                                    // �����checkbox������Ҫ��������
//                                    if (field.getFieldType() == 6) {
//                                        String columnName = "";
//                                        // ����ǵ�һ��record����Ҫ�Ӻ�׺
//                                        if (i == 0) {
//                                            columnName = columnNames[j];
//                                        } else {
//                                            columnName = columnNames[j] + "_" + i;
//                                        }
//
//                                        if (request.getParameterValues(columnName) != null) {
//                                            fieldStr.append(columnNames[j]).append(",");
//                                            String tempValue = "";
//                                            // ����ǵ�һ��record����Ҫ�Ӻ�׺
//                                            tempValue = request.getParameterValues(columnNames[j])[i];
//                                            if (!"".equals(tempValue)) {
//                                                valueStr.append("'" + tempValue).append("',");
//                                            } else {
//                                                valueStr.append("'" + tempValue).append("',");
//                                            }
//                                        }
//                                    } else {// ��checkbox
                                        if (request.getParameterValues(columnNames[j]) != null) {
                                            // begin 2008/07/02 Robin
                                            if (i >= request.getParameterValues(columnNames[j]).length) {
                                                fieldStr.append(columnNames[j]).append(",");
                                                valueStr.append(" null,");
                                                continue;
                                            }
                                            // end 2008/07/02 Robin
                                            if (!"".equals(request.getParameterValues(columnNames[j])[i])) {
                                                fieldStr.append(columnNames[j]).append(",");
                                                String temp = this.decdr(request.getParameterValues(columnNames[j])[i],
                                                        isAJAX);
                                                System.out.println(temp);
                                                temp = StringUtil.replace(temp, "&#39;", "'");
                                                temp = StringUtil.replace(temp, "'", "''");
                                                if (field.getFieldType() == CommonName.FIELD_TYPE_NUMBER) {
                                                    temp = StringUtil.replace(temp, ",", "");
                                                }
                                                valueStr.append("'" + temp).append("',");
                                            } else {
                                                fieldStr.append(columnNames[j]).append(",");
                                                valueStr.append(" null,");
                                            }
                                        }
                                    //}
                                } else {
                                    // ��д������,request_no�ֶ�ÿ����section����һ�����ֶ�
                                    fieldStr.append(columnNames[j]).append(",");
                                    // valueStr.append("'"+request.getParameterValues(columnNames[j])[0]).append("',");
                                    valueStr.append("'" + newRequestNo).append("',");
                                }
                            }
                            if (!"".equals(fieldStr.toString())) {
                                String insertSQL = "insert into " + section.getTableName() + "("
                                        + fieldStr.substring(0, fieldStr.lastIndexOf(",")) + ") values("
                                        + valueStr.substring(0, valueStr.lastIndexOf(",")) + ")";
                                // System.out.print("INSERT SQL:"+insertSQL);
                                // dbManager.executeUpdate(insertSQL);
                                System.out.println("\n" + insertSQL);
                                DEBUG = insertSQL;
                                formDao.executeSQL(insertSQL);
                                fieldStr = new StringBuffer("");
                                valueStr = new StringBuffer("");
                            }
                        }
                    } else if ("02".equals(section.getSectionType()) || "03".equals(section.getSectionType())) {
                        String[] columnNames = dbManager.getTableColumnNames(section.getTableName());
                        // ������޸ģ���update,����INSERT
                        if (!"update".equals(saveType)) {
                            for (int j = 0; j < columnNames.length; j++) {
                                if (!requestVo.getFieldId().equals(columnNames[j])) {// ����Ƿ�request_no�ֶ�
                                    if (request.getParameter(columnNames[j]) != null
                                            && !"".equals(request.getParameter(columnNames[j]))) {
                                        FormSectionFieldVO field = (FormSectionFieldVO) fieldMap.get(columnNames[j]
                                                .toLowerCase());
                                        fieldStr.append(columnNames[j]).append(",");
                                        String tempValue = this
                                                .decdr(getSpelledValues(columnNames[j], request), isAJAX);

                                        if (!"".equals(tempValue)) {
                                            tempValue = StringUtil.replace(tempValue, "&#39;", "'");
                                            tempValue = StringUtil.replace(tempValue, "'", "''");
                                            if (field.getFieldType() == CommonName.FIELD_TYPE_NUMBER) {
                                                tempValue = StringUtil.replace(tempValue, ",", "");
                                            }
                                            valueStr.append("'" + tempValue).append("',");
                                            System.out.println(columnNames[j] + '=' + tempValue);
                                        } else {
                                            String temp = this.decdr((String) request.getParameter(columnNames[j]),
                                                    isAJAX);
                                            temp = StringUtil.replace(temp, "&#39;", "'");
                                            temp = StringUtil.replace(temp, "'", "''");
                                            if (field.getFieldType() == CommonName.FIELD_TYPE_NUMBER) {
                                                temp = StringUtil.replace(temp, ",", "");
                                            }
                                            // valueStr.append("'"+request.getParameter(columnNames[j])).append("',");
                                            valueStr.append("'" + temp).append("',");
                                            System.out.println(columnNames[j] + '=' + temp);
                                        }
                                    }
                                } else {
                                    // ��д������,request_no�ֶ�ÿ����section����һ�����ֶ�
                                    fieldStr.append(columnNames[j]).append(",");
                                    valueStr.append("'" + newRequestNo).append("',");
                                }
                            }
                            if (valueStr != null && !"".equals(valueStr)) {
                                String insertSQL = "insert into " + section.getTableName() + "("
                                        + fieldStr.substring(0, fieldStr.lastIndexOf(","))

                                        + ") values(" + valueStr.substring(0, valueStr.lastIndexOf(",")) + ")";

                                // System.out.print("INSERT SQL:"+insertSQL);
                                // dbManager.executeUpdate(insertSQL);
                                // System.out.println("----- insertSQL: " + insertSQL);
                                System.out.println("\n" + insertSQL);
                                DEBUG = insertSQL;
                                formDao.executeSQL(insertSQL);
                            }
                        } else {// �޸Ĳ�����update������
                            for (int j = 0; j < columnNames.length; j++) {
                                if (request.getParameter(columnNames[j]) != null
                                        && !(requestVo.getFieldId().toUpperCase()).equals(columnNames[j].toUpperCase())) {
                                    FormSectionFieldVO field = (FormSectionFieldVO) fieldMap.get(columnNames[j]
                                            .toLowerCase());
                                    fieldStr.append(columnNames[j]).append("=");
                                    String tempValue = this.decdr(getSpelledValues(columnNames[j], request), isAJAX);
                                    if (!"".equals(tempValue)) {
                                        tempValue = StringUtil.replace(tempValue, "&#39;", "'");
                                        tempValue = StringUtil.replace(tempValue, "'", "''");
                                        if (field.getFieldType() == CommonName.FIELD_TYPE_NUMBER) {
                                            tempValue = StringUtil.replace(tempValue, ",", "");
                                        }
                                        fieldStr.append("'" + tempValue).append("',");
                                    } else {
                                        String temp = this.decdr((String) request.getParameter(columnNames[j]), isAJAX);
                                        temp = StringUtil.replace(temp, "&#39;", "'");
                                        temp = StringUtil.replace(temp, "'", "''");
                                        if (field.getFieldType() == CommonName.FIELD_TYPE_NUMBER) {
                                            temp = StringUtil.replace(temp, ",", "");
                                        }

                                        // fieldStr.append("'"+request.getParameter(columnNames[j])).append("',");
                                        if ("".equals(temp)) {
                                            fieldStr.append(" null,");
                                        } else {
                                            fieldStr.append("'" + temp).append("',");
                                        }
                                    }
                                } else if (request.getParameter(columnNames[j]) == null
                                        && !(requestVo.getFieldId().toUpperCase()).equals(columnNames[j].toUpperCase())) {
                                    fieldStr.append(columnNames[j]).append("= null,");
                                }
                            }
                            if (fieldStr != null && !"".equals(fieldStr.toString())) {
                                String insertSQL = "update " + section.getTableName() + " set "
                                        + fieldStr.substring(0, fieldStr.lastIndexOf(",")) + " where request_no='"
                                        + requestNo + "'";
                                System.out.println("\n" + insertSQL);
                                DEBUG = insertSQL;
                                formDao.executeSQL(insertSQL);
                                fieldStr = new StringBuffer("");
                            }
                        }
                    } else if ("00".equals(section.getSectionType())) { // attached file
                        // System.out.println(saveType);
                        if (!"update".equals(saveType)) {
                            // String[] columnNames = dbManager.getTableColumnNames(section.getTableName());
                            // String deleteSql =
                            // "delete from "+section.getTableName()+" where request_no='"+newRequestNo+"'";
                            // String insertSql =
                            // "insert into "+section.getTableName()+"(request_no,file_name,file_description) select '"+newRequestNo+"',file_name,file_description from teflow_upload_file_temp where staff_code='"+staff.getStaffCode()+"' and request_form_date='"+requestFormDate.trim()+"'";
                            String insertSql = "insert into " + section.getTableName()
                                    + "(request_no,file_name,file_description) select '" + newRequestNo
                                    + "',file_name,file_description from teflow_upload_file_temp where staff_code='"
                                    + staff.getStaffCode() + "' and attachment_id='" + attachmentIdentity + "'";
                            String deleteTempSql = "delete from teflow_upload_file_temp where staff_code='"
                                    + staff.getStaffCode() + "' and attachment_id='" + attachmentIdentity + "'";
                            // formDao.executeSQL(deleteSql);
                            formDao.executeSQL(insertSql);
                            formDao.executeSQL(deleteTempSql);
                            // System.out.println(insertSql);
                            // System.out.println(deleteTempSql);
                        }
                    } // end sectionType
                }// end while
                 // �ж��Ƿ����ϵͳ�ֶΡ�urgent_level��(Is Urgent),������ڣ�����Ҫ��ȡ����ֵ
                if (request.getParameter("urgent_level") != null && !"".equals(request.getParameter("urgent_level"))) {
                    isUrgent = (String) request.getParameter("urgent_level");
                }

                // ��ȡ�����Ĳ������ͣ�00Ϊ��ʼ��������01Ϊ�ύ����
                // ��ʼ����������
                traceVo = new WorkFlowProcessTraceVO();
                traceVo.setIsUrgent(isUrgent);
                traceVo.setFormSystemId(Integer.parseInt(formSystemId));
                if (!"update".equals(saveType)) {
                    traceVo.setRequestNo(newRequestNo);
                } else {
                    traceVo.setRequestNo(request.getParameter("request_no"));
                }

                traceVo.setHandleStaffCode(staff.getStaffCode());
                traceVo.setCurrentNodeId("0");// beginNode

                // ��ȡѡ����Ҫ���ʼ����͵�Ա��
                String ccToStaffs = request.getParameter("ccStaffCode");
                traceVo.setCcStaffs(ccToStaffs);

                // �޸�(update)������(00)
                if ("update".equals(saveType) && "00".equals(submitType)) { // ������޸Ĳ�������ֻ��Ҫ����������ݵ��޸ģ�����Ҫ�漰�����̴����ı��
                    // ��Ҫ���κβ���
                } else if ("update".equals(saveType) && "01".equals(submitType)) {// �޸�(update)���ύ(01)
                    // ��ʼ����
                    traceVo.setHandleType("01");
                    traceVo.setHandleComments("Requester submitted the form");
                    dbManager.executeUpdate("update teflow_wkf_process set urgent_level='" + isUrgent
                            + "',request_staff_code='" + requestStaffCode + "', update_date=getdate() "
                            + "where request_no='" + traceVo.getRequestNo() + "'");
                    // processDao.nodeProcess(traceVo,staff.getStaffCode());
                    // System.out.println("===== " + requestNo +
                    // " ===== update/submit: processDao.nodeProcess - begin");
                    // processDao.nodeProcess(traceVo, requestStaffCode);

                    String nextApproverStaffCode = request.getParameter(CommonName.NEXT_APPROVER_STAFF_CODE); // Can be
                                                                                                              // null
                    if (nextApproverStaffCode != null) {
                        nextApproverStaffCode = new DelegateDAO(dbManager).getStaffsByDeputy(nextApproverStaffCode, "");
                    }

                    processDao.nodeProcess(traceVo, requestStaffCode, nextApproverStaffCode, true);

                    // System.out.println("===== " + requestNo + " ===== update/submit: processDao.nodeProcess - end");
                } else if ("new".equals(saveType) && "00".equals(submitType)) { // ����(new)������(00)
                    // ��ʼ�������form������
                    traceVo.setHandleType("00");
                    traceVo.setHandleComments("Requester drafted the form");
                    // processDao.drawOutForm(traceVo,staff.getStaffCode());
                    processDao.drawOutForm(traceVo, requestStaffCode);

                } else if ("new".equals(saveType) && "01".equals(submitType)) {// ����(new)���ύ(01)
                    // ��ʼ�������form������
                    traceVo.setHandleType("00");
                    traceVo.setHandleComments("Requester drafted the form");
                    // processDao.drawOutForm(traceVo,staff.getStaffCode());
                    processDao.drawOutForm(traceVo, requestStaffCode);
                    // ��ʼ����
                    traceVo.setHandleType("01");
                    traceVo.setHandleComments("Requester submitted the form");
                    // processDao.nodeProcess(traceVo,staff.getStaffCode());
                    // System.out.println("===== " + requestNo + " ===== new/submit: processDao.nodeProcess - begin");
                    // processDao.nodeProcess(traceVo, requestStaffCode);
                    String nextApproverStaffCode = request.getParameter(CommonName.NEXT_APPROVER_STAFF_CODE); // Can be
                                                                                                              // null
                    if (nextApproverStaffCode != null) {
                        nextApproverStaffCode = new DelegateDAO(dbManager).getStaffsByDeputy(nextApproverStaffCode, "");
                    }

                    processDao.nodeProcess(traceVo, requestStaffCode, nextApproverStaffCode, true);
                    // System.out.println("===== " + requestNo + " ===== new/submit: processDao.nodeProcess - end");
                }

                dbManager.commit();
  
                System.out.println("Commit Time: " + (System.currentTimeMillis() - startTime));
                processDao.processCallback(traceVo);
            } catch (Exception ex) {
                dbManager.rollback();
                System.out.println("dbManager.rollback: " + requestNo + " : " + DEBUG);
                for (int i = 0; i < ex.getStackTrace().length; i++)
                    System.out.println(ex.getStackTrace()[i]);

                ex.printStackTrace();
                request.setAttribute(CommonName.COMMON_ERROR_INFOR, I18NMessageHelper.getMessage("common.frmsavefail")
                        + ex.getMessage() + " : " + DEBUG);
                return mapping.findActionLocation("fail");
            }

            if ("01".equals(submitType)) {
                String handleType = "01";
                WorkFlowProcessVO process = processDao.getProcessVO(requestNo);
                EmailTemplateDAO emailTemplateDao = new EmailTemplateDAO(dbManager);
                EmailTemplateVO template = emailTemplateDao.getEmailTemplateByAction(handleType);

                // For delayed node
                if (CommonName.NODE_TYPE_DELAYED.equals(process.getNodeType()) && "1".equals(process.getInProcess())
                        && "SYS".equals(process.getInProcessStaffCode())) {
                    template = null;
                }

                if (template != null) {
                    // to get the staff emails who will need to handle
                    String handleStaffList = process.getCurrentProcessor();
                    String[] staffs = StringUtil.split(handleStaffList, ",");
                    if (staffs != null && staffs.length > 0) {
                        String handleComments = traceVo.getHandleComments();
                        String[] ccStaff = null;
                        if (traceVo.getCcStaffs() != null && !"".equals(traceVo.getCcStaffs())) {
                            ccStaff = StringUtil.split(traceVo.getCcStaffs(), ",");
                        }
                        // System.out.println("===== " + requestNo + " ===== send email - begin");
                        EFlowEmailUtil.sendEmail(template, requestStaffCode, requestNo, staff.getStaffCode(), handleComments, staffs,
                                formSystemId, ccStaff, process);
                        // System.out.println("===== " + requestNo + " ===== send email - end");
                    } else {
                        // ����email��administrator��form������
                        String adminEmailAddress = ParamConfigHelper.getInstance().getParamValue(
                                CommonName.PARAM_ADMIN_EMAIL);
                        if (adminEmailAddress != null && !"".equals(adminEmailAddress)) {
                            adminEmailAddress = adminEmailAddress + "," + staff.getEmail();
                        } else {
                            adminEmailAddress = staff.getEmail();
                        }
                        // System.out.println("adminEmailAddress: " + adminEmailAddress);
                        String subject = "Error of E-Flow";
                        String mailContent = "<font face='Arial' size='2'>Request No.(<font color='red'><b>"
                                + requestNo + "</b></font>) can't find the approver in node <b>"
                                + process.getNodeName() + "</b> (" + process.getNodeId() + ").<br><br>"
                                + "Requester: <b>" + requester.getStaffName() + "</b> (<font color='red'><b>" + orgName
                                + "</b></font>)." + "</font>";
                        EFlowEmailUtil.sendEmail(subject, mailContent, adminEmailAddress, process);
                    }
                }

                // For waiting node
                EmailTemplateVO template3 = null;
                if (CommonName.NODE_TYPE_WAITING.equals(process.getNodeType())) {
                    template3 = emailTemplateDao.getEmailTemplateByAction(CommonName.HANDLE_TYPE_WAITING_REMINDER);
                }
                if (template3 != null) {
                    String[] toStaff = null;
                    if (process.getSubmittedBy().trim().equals(process.getRequestStaffCode().trim())) {
                        toStaff = new String[] {process.getRequestStaffCode()};
                    } else {
                        toStaff = new String[] {process.getRequestStaffCode(), process.getSubmittedBy().trim()};
                    }

                    double _limitedHours = process.getLimitedHours();
                    if (_limitedHours == 0.0) {
                        double limitedHours = CommonName.DEFAULT_WAITING_NODE_LIMITED_HOURS;
                        String paramLimitedHours = ParamConfigHelper.getInstance().getParamValue(
                                CommonName.PARAM_WAITING_NODE_LIMITED_HOURS);
                        if (paramLimitedHours != null && !"".equals(paramLimitedHours)) {
                            try {
                                limitedHours = Double.parseDouble(paramLimitedHours);
                            } catch (Exception ex) {
                                //
                            }
                        }
                        _limitedHours = limitedHours;
                    }
                    EFlowEmailUtil.sendEmail(template3, process.getRequestStaffCode(), requestNo, staff.getStaffCode(), ""
                            + _limitedHours, toStaff, formSystemId, null, process);
                }
            }
            // dbManager.commit();
            System.out.println("End Time: " + (System.currentTimeMillis() - startTime));
            // System.out.println("===== " + requestNo + " ===== saveFormFill - dbManager.commit");
            // ����ɹ���ϵͳ�Զ���ת�������������form�б�����ʱ��ֻ��ʾ�ñ����form

            if ("1".equals(request.getParameter("checkOptionalNode"))) {
                response.setContentType("text/xml;charset=GBK"); // it is very important
                response.setHeader("Cache-Control", "no-cache");
                response.setHeader("Pragma", "no-cache");
                PrintWriter out = response.getWriter();
                out.print(newRequestNo);
                System.out.println("New Requset No Dispatched: " + newRequestNo);
                return null;
            }
            request.setAttribute("requestNo", traceVo.getRequestNo());
            // �����form�����˱������Ҫ����Ĵ�������������ת���м䴦��ҳ��
            if (form != null && form.getAfterSaveUrl() != null && !"".equals(form.getAfterSaveUrl())) {
                request.setAttribute("functionLabel", form.getAfterSaveUrl());
                // request.setAttribute("url","wkfProcessAction.it?method=listPersonalApplyForm");
                return mapping.findActionLocation("afterSavingHandle");
            }
        } catch (Exception e) {
            String message = "Unknown";
            if (e != null && !"".equals(e.getMessage())) {
                message = e.getMessage();
            }
            if (message == null || "".equals(message)) {
                message = "Unknown";
            }
            request.setAttribute(CommonName.COMMON_ERROR_INFOR, I18NMessageHelper.getMessage("common.frmsavefail")
                    + message);
            e.printStackTrace();
            returnLabel = "fail";
        } finally {
            if (dbManager != null) {
                dbManager.freeConnection();
            }
        }

        if ("successTipPage".equals(returnLabel)) {
            if ("00".equals(submitType)) {
                successmessage = I18NMessageHelper.getMessage("common.frmsavesuc") + "(" + requestNo + ")";
                request.setAttribute( CommonName.RETURN_URL , "formManageAction.it?method=displayFormContent&operateType=view&status=00&requestNo="+requestNo+"&formSystemId="+formSystemId);
            } else if ("01".equals(submitType)) {
                // successmessage = I18NMessageHelper.getMessage("common.frmsubsuc") + "(" + requestNo + ")";
                successmessage = I18NMessageHelper.getMessage("common.frmsubsuc", new String[] {requestNo});
                request.setAttribute( CommonName.RETURN_URL , "/welcome.jsp");
            }
            request.setAttribute(CommonName.COMMON_OK_INFOR, successmessage);
            /*request.setAttribute(CommonName.RETURN_URL, "/wkfProcessAction.it?method=listPersonalApplyForm&requestNo=" + requestNo);*/
        }
        return mapping.findActionLocation(returnLabel);
    }

    /**
     * IT1002 �����������ʽ��sectionʱ������ɾ���Ѿ����ڵļ�¼��Ȼ���ٱ����section�����µļ�¼ ͨ�õ�form�����������ϱ���
     * 
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionLocation saveFormFillAuto(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
    	
        //String returnLabel = "successTipPage";
    	 String formSystemId = (String) request.getParameter("formSystemId");
         // ��ȡ�����е�field��request_no,�������浽��sectionType='3'������section��ȥ
         FormSectionFieldVO requestVo = SystemFieldHelper.getInstance().getBasicFieldById(
                 CommonName.SYSTEM_ID_REQUEST_NO);
         String requestNo = (String) request.getParameter(requestVo.getFieldId());
         String saveType = (String) request.getParameter("saveType");
         String isUrgent = "1"; // -----default value'1'----- No (not is urgent)

         String DEBUG = "";

         // String referFormLink="";
         String successmessage = "";
         if (saveType == null) {
             saveType = "new";
         }
         // String requestFormDate = request.getParameter("request_date");
         String attachmentIdentity = request.getParameter("attachmentIdentity");
         // if(request.getParameter("reference_form")!=null && !"".equals(request.getParameter("reference_form"))){
         // referFormLink = (String)request.getParameter("reference_form");
         // }
         // ��ȡ�����Ĳ������ͣ�00Ϊ��ʼ��������01Ϊ�ύ����
         String submitType = (String) request.getParameter("submitType");
         if (submitType == null || "".equals(submitType)) {
             submitType = "00";
         }

         // submitted by
         StaffVO staff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);

         // ��ȡrequester
         String requestStaffCode = (String) request.getParameter(CommonName.SYSTEM_ID_REQUEST_STAFF_CODE);
         // ��������ڣ�����Ϊsubmitted by
         if (requestStaffCode == null || "".equals(requestStaffCode)) {
             requestStaffCode = staff.getStaffCode();
         }
         StaffVO requester = StaffTeamHelper.getInstance().getStaffByCode(requestStaffCode);
         String orgName = CompanyHelper.getInstance().getOrgName(requester.getOrgId());

         IDBManager dbManager = null;
         FormManageVO form = null;
         try {
             dbManager = DBManagerFactory.getDBManager();
             // dbManager.startTransaction();
             // System.out.println("===== " + requestNo + " ===== saveFormFill - dbManager.startTransaction");
             FormManageDAO formDao = new FormManageDAO(dbManager);
             WorkFlowProcessDAO processDao = new WorkFlowProcessDAO(dbManager);
             WorkFlowProcessTraceVO traceVo = null;

             form = formDao.getFormBySystemId(Integer.parseInt(formSystemId));
             // 1.���Ҹ�form���ڼ���section
             Collection sectionList = form.getSectionList();
             Iterator sectionIt = sectionList.iterator();
             Iterator tempIt = sectionList.iterator();
             FormSectionVO tempSection = (FormSectionVO) tempIt.next();
             String newRequestNo = requestNo;
             // ���������������Ϊ��֤������ȷ�ԣ������»�ȡһ��request_no;
             if (!"update".equals(saveType) || requestNo.equals("")) {
                 // || requestNo.equals(request.getSession().getAttribute(this.IS_PENDING_RN))) {
                 newRequestNo = FieldUtil.getRequestNo(Integer.parseInt(formSystemId), form.getFormId(),
                         tempSection.getSectionId(), dbManager);
                 requestNo = newRequestNo;

             } 

             long startTime = System.currentTimeMillis();
             try {
                 dbManager.startTransaction();
                 while (sectionIt.hasNext()) {
                     FormSectionVO section = (FormSectionVO) sectionIt.next();
                     Collection fieldList = section.getFieldList();
                     Iterator fieldIt = fieldList.iterator();
                     HashMap fieldMap = new HashMap();
                     while (fieldIt.hasNext()) {
                         FormSectionFieldVO field = (FormSectionFieldVO) fieldIt.next();
                         fieldMap.put(field.getFieldId().toLowerCase(), field);
                     }
                     // 2.���ÿ��section���õ�����ӵ�е�field���ֶΣ���id
                     // Collection fieldList = section.getFieldList();
                     StringBuffer fieldStr = new StringBuffer("");
                     StringBuffer valueStr = new StringBuffer("");

                     if ("01".equals(section.getSectionType())) { // 01 -- ������ʽ�������ύ��������
                         // Iterator fieldIt = fieldList.iterator();
                         String[] columnNames = dbManager.getTableColumnNames(section.getTableName());

                         int count = 0;
                         for (int i = 0; i < columnNames.length; i++) {
                             if ("request_no".equals(columnNames[i])) {
                                 continue;
                             }
                             String[] columnValues = request.getParameterValues(columnNames[i]);
                             // System.out.println("-----columnName="+columnNames[i]+",count="+columnValues.length);
                             if (columnValues != null) {
                                 count = columnValues.length;
                                 break;
                             }
                         }

                         // Begin 2007-08-21,������޸Ĳ��������ڱ�����ʽ�ģ���ɾ��֮ǰ���м�¼���ٲ������м�¼
                         if ("update".equals(saveType)) {
                             String delSQL = "delete from " + section.getTableName() + " where request_no='" + requestNo
                                     + "'";
                             // newRequestNo = requestNo;
                             formDao.executeSQL(delSQL);
                             // System.out.println("----------------:"+delSQL);
                             delSQL = "";
                         }
                         // end 2007-08-21

                         // ������޸ģ���update,����INSERT---------------�÷�ʽ����2007-08-21
                         // if(!"update".equals(saveType)){
                         // ��ȡÿ����¼�����ݣ�Ȼ�󱣴�
                         for (int i = 0; i < count; i++) {
                             for (int j = 0; j < columnNames.length; j++) {
                                 if ("id".equals(columnNames[j])) {
                                     continue;
                                 }
                                 System.out.print("\n" + columnNames[j] + '=');
                                 if (!requestVo.getFieldId().equals(columnNames[j])) {
                                     FormSectionFieldVO field = (FormSectionFieldVO) fieldMap.get(columnNames[j]
                                             .toLowerCase());
                                     if (field == null)
                                         continue;// �п��ܸ�column�Ѿ��ֹ���teflow_form_section_field�г�ȥ�����ܸ���2011-5-5

                                         if (request.getParameterValues(columnNames[j]) != null) {
                                             // begin 2008/07/02 Robin
                                             if (i >= request.getParameterValues(columnNames[j]).length) {
                                                 fieldStr.append(columnNames[j]).append(",");
                                                 valueStr.append(" null,");
                                                 continue;
                                             }
                                             // end 2008/07/02 Robin
                                             if (!"".equals(request.getParameterValues(columnNames[j])[i])) {
                                                 fieldStr.append(columnNames[j]).append(",");
                                                 String temp = this.decdr(request.getParameterValues(columnNames[j])[i],true);
                                                 System.out.println(temp);
                                                 temp = StringUtil.replace(temp, "&#39;", "'");
                                                 temp = StringUtil.replace(temp, "'", "''");
                                                 if (field.getFieldType() == CommonName.FIELD_TYPE_NUMBER) {
                                                     temp = StringUtil.replace(temp, ",", "");
                                                 }
                                                 valueStr.append("'" + temp).append("',");
                                             } else {
                                                 fieldStr.append(columnNames[j]).append(",");
                                                 valueStr.append(" null,");
                                             }
                                         }
                                     //}
                                 } else {
                                     // ��д������,request_no�ֶ�ÿ����section����һ�����ֶ�
                                     fieldStr.append(columnNames[j]).append(",");
                                     // valueStr.append("'"+request.getParameterValues(columnNames[j])[0]).append("',");
                                     valueStr.append("'" + newRequestNo).append("',");
                                 }
                             }
                             if (!"".equals(fieldStr.toString())) {
                                 String insertSQL = "insert into " + section.getTableName() + "("
                                         + fieldStr.substring(0, fieldStr.lastIndexOf(",")) + ") values("
                                         + valueStr.substring(0, valueStr.lastIndexOf(",")) + ")";
                                 // System.out.print("INSERT SQL:"+insertSQL);
                                 // dbManager.executeUpdate(insertSQL);
                                 System.out.println("\n" + insertSQL);
                                 DEBUG = insertSQL;
                                 formDao.executeSQL(insertSQL);
                                 fieldStr = new StringBuffer("");
                                 valueStr = new StringBuffer("");
                             }
                         }
                     } else if ("02".equals(section.getSectionType()) || "03".equals(section.getSectionType())) {
                         String[] columnNames = dbManager.getTableColumnNames(section.getTableName());
                         // ������޸ģ���update,����INSERT
                         if (!"update".equals(saveType)) {
                             for (int j = 0; j < columnNames.length; j++) {
                                 if (!requestVo.getFieldId().equals(columnNames[j])) {// ����Ƿ�request_no�ֶ�
                                     if (request.getParameter(columnNames[j]) != null
                                             && !"".equals(request.getParameter(columnNames[j]))) {
                                         FormSectionFieldVO field = (FormSectionFieldVO) fieldMap.get(columnNames[j]
                                                 .toLowerCase());
                                         fieldStr.append(columnNames[j]).append(",");
                                         String tempValue = this.decdr(getSpelledValues(columnNames[j], request), true);

                                         if (!"".equals(tempValue)) {
                                             tempValue = StringUtil.replace(tempValue, "&#39;", "'");
                                             tempValue = StringUtil.replace(tempValue, "'", "''");
                                             if (field.getFieldType() == CommonName.FIELD_TYPE_NUMBER) {
                                                 tempValue = StringUtil.replace(tempValue, ",", "");
                                             }
                                             valueStr.append("'" + tempValue).append("',");
                                             System.out.println(columnNames[j] + '=' + tempValue);
                                         } else {
                                             String temp = this.decdr((String) request.getParameter(columnNames[j]),true);
                                             temp = StringUtil.replace(temp, "&#39;", "'");
                                             temp = StringUtil.replace(temp, "'", "''");
                                             if (field.getFieldType() == CommonName.FIELD_TYPE_NUMBER) {
                                                 temp = StringUtil.replace(temp, ",", "");
                                             }
                                             // valueStr.append("'"+request.getParameter(columnNames[j])).append("',");
                                             valueStr.append("'" + temp).append("',");
                                             System.out.println(columnNames[j] + '=' + temp);
                                         }
                                     }
                                 } else {
                                     // ��д������,request_no�ֶ�ÿ����section����һ�����ֶ�
                                     fieldStr.append(columnNames[j]).append(",");
                                     valueStr.append("'" + newRequestNo).append("',");
                                 }
                             }
                             if (valueStr != null && !"".equals(valueStr)) {
                                 String insertSQL = "insert into " + section.getTableName() + "("
                                         + fieldStr.substring(0, fieldStr.lastIndexOf(","))

                                         + ") values(" + valueStr.substring(0, valueStr.lastIndexOf(",")) + ")";

                                 // System.out.print("INSERT SQL:"+insertSQL);
                                 // dbManager.executeUpdate(insertSQL);
                                 // System.out.println("----- insertSQL: " + insertSQL);
                                 System.out.println("\n" + insertSQL);
                                 DEBUG = insertSQL;
                                 formDao.executeSQL(insertSQL);
                             }
                         } else {// �޸Ĳ�����update������
                             for (int j = 0; j < columnNames.length; j++) {
                                 if (request.getParameter(columnNames[j]) != null
                                         && !(requestVo.getFieldId().toUpperCase()).equals(columnNames[j].toUpperCase())) {
                                     FormSectionFieldVO field = (FormSectionFieldVO) fieldMap.get(columnNames[j]
                                             .toLowerCase());
                                     fieldStr.append(columnNames[j]).append("=");
                                     String tempValue = this.decdr(getSpelledValues(columnNames[j], request), true);
                                     if (!"".equals(tempValue)) {
                                         tempValue = StringUtil.replace(tempValue, "&#39;", "'");
                                         tempValue = StringUtil.replace(tempValue, "'", "''");
                                         if (field.getFieldType() == CommonName.FIELD_TYPE_NUMBER) {
                                             tempValue = StringUtil.replace(tempValue, ",", "");
                                         }
                                         fieldStr.append("'" + tempValue).append("',");
                                     } else {
                                         String temp = this.decdr((String) request.getParameter(columnNames[j]), true);
                                         temp = StringUtil.replace(temp, "&#39;", "'");
                                         temp = StringUtil.replace(temp, "'", "''");
                                         if (field.getFieldType() == CommonName.FIELD_TYPE_NUMBER) {
                                             temp = StringUtil.replace(temp, ",", "");
                                         }

                                         // fieldStr.append("'"+request.getParameter(columnNames[j])).append("',");
                                         if ("".equals(temp)) {
                                             fieldStr.append(" null,");
                                         } else {
                                             fieldStr.append("'" + temp).append("',");
                                         }
                                     }
                                 } else if (request.getParameter(columnNames[j]) == null
                                         && !(requestVo.getFieldId().toUpperCase()).equals(columnNames[j].toUpperCase())) {
                                     fieldStr.append(columnNames[j]).append("= null,");
                                 }
                             }
                             if (fieldStr != null && !"".equals(fieldStr.toString())) {
                                 String insertSQL = "update " + section.getTableName() + " set "
                                         + fieldStr.substring(0, fieldStr.lastIndexOf(",")) + " where request_no='"
                                         + requestNo + "'";
                                 System.out.println("\n" + insertSQL);
                                 DEBUG = insertSQL;
                                 formDao.executeSQL(insertSQL);
                                 fieldStr = new StringBuffer("");
                             }
                         }
                     } else if ("00".equals(section.getSectionType())) { // attached file
                         if (!"update".equals(saveType)) {
                        	 String insertSql = "insert into " + section.getTableName()
                                     + "(request_no,file_name,file_description) select '" + newRequestNo
                                     + "',file_name,file_description from teflow_upload_file_temp where staff_code='"
                                     + staff.getStaffCode() + "' and attachment_id='" + attachmentIdentity + "'";
                             String deleteTempSql = "delete from teflow_upload_file_temp where staff_code='"
                                     + staff.getStaffCode() + "' and attachment_id='" + attachmentIdentity + "'";

                             formDao.executeSQL(insertSql);
                             formDao.executeSQL(deleteTempSql);
                         }
                     } // end sectionType
                 }// end while
                  // �ж��Ƿ����ϵͳ�ֶΡ�urgent_level��(Is Urgent),������ڣ�����Ҫ��ȡ����ֵ
                 if (request.getParameter("urgent_level") != null && !"".equals(request.getParameter("urgent_level"))) {
                     isUrgent = (String) request.getParameter("urgent_level");
                 }

                 // ��ȡ�����Ĳ������ͣ�00Ϊ��ʼ��������01Ϊ�ύ����
                 // ��ʼ����������
                 traceVo = new WorkFlowProcessTraceVO();
                 traceVo.setIsUrgent(isUrgent);
                 traceVo.setFormSystemId(Integer.parseInt(formSystemId));
                 if (!"update".equals(saveType)) {
                     traceVo.setRequestNo(newRequestNo);
                 } else {
                     traceVo.setRequestNo(request.getParameter("request_no"));
                 }

                 traceVo.setHandleStaffCode(staff.getStaffCode());
                 traceVo.setCurrentNodeId("0");// beginNode

                 // ��ȡѡ����Ҫ���ʼ����͵�Ա��
                 String ccToStaffs = request.getParameter("ccStaffCode");
                 traceVo.setCcStaffs(ccToStaffs);

                 // �޸�(update)������(00)
                 if ("update".equals(saveType) && "00".equals(submitType)) { // ������޸Ĳ�������ֻ��Ҫ����������ݵ��޸ģ�����Ҫ�漰�����̴����ı��
                     // ��Ҫ���κβ���
                 }  else if ("new".equals(saveType) && "00".equals(submitType)) { // ����(new)������(00)
                     // ��ʼ�������form������
                     traceVo.setHandleType("00");
                     traceVo.setHandleComments("Requester drafted the form");
                     // processDao.drawOutForm(traceVo,staff.getStaffCode());
                     processDao.drawOutForm(traceVo, requestStaffCode);

                 } 

                 dbManager.commit();
   
                 System.out.println("Commit Time: " + (System.currentTimeMillis() - startTime));
                 processDao.processCallback(traceVo);
             } catch (Exception ex) {
                 dbManager.rollback();
                 System.out.println("dbManager.rollback: " + requestNo + " : " + DEBUG);
                 for (int i = 0; i < ex.getStackTrace().length; i++)
                     System.out.println(ex.getStackTrace()[i]);

                 ex.printStackTrace();
                 request.setAttribute(CommonName.COMMON_ERROR_INFOR, I18NMessageHelper.getMessage("common.frmsavefail")
                         + ex.getMessage() + " : " + DEBUG);
                 return null;
             }

             // dbManager.commit();
             System.out.println("End Time: " + (System.currentTimeMillis() - startTime));
             // System.out.println("===== " + requestNo + " ===== saveFormFill - dbManager.commit");
             // ����ɹ���ϵͳ�Զ���ת�������������form�б�����ʱ��ֻ��ʾ�ñ����form

        	 response.setContentType("application/json;charset=GBK"); // it is very important
             response.setHeader("Cache-Control", "no-cache");
             response.setHeader("Pragma", "no-cache");
             PrintWriter out = response.getWriter();
             JSONObject json = new JSONObject(); 
             json.put("requestNo", newRequestNo);    
             out.print(json.toString());
             
             System.out.println("New Requset No Dispatched: " + newRequestNo);
             return null;
             
         } catch (Exception e) {
             String message = "Unknown";
             if (e != null && !"".equals(e.getMessage())) {
                 message = e.getMessage();
             }
             if (message == null || "".equals(message)) {
                 message = "Unknown";
             }
             request.setAttribute(CommonName.COMMON_ERROR_INFOR, I18NMessageHelper.getMessage("common.frmsavefail")
                     + message);
             e.printStackTrace();
         } finally {
             if (dbManager != null) {
                 dbManager.freeConnection();
             }
         }
         
         return null;
    }

    private String getSpelledValues(String paramName, HttpServletRequest request) {
        // ����Ǵ��ڶ��ѡ������ѡ�ࣩ���򽫶��ѡ��𰸰��ո�ʽ��02,03��ƴװ������һ���ֶ���
        if (request.getParameterValues(paramName) != null && request.getParameterValues(paramName).length >= 1) {
            String[] valueList = request.getParameterValues(paramName);
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
   
    public void isNextProcessorOptional(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        IDBManager dbManager = null;
        String requestNo = request.getParameter(CommonName.SYSTEM_ID_REQUEST_NO);
        String formSystemId = request.getParameter("formSystemId");
        String currentNodeId = request.getParameter("currentNodeId");

        response.setContentType("text/xml;charset=GBK"); // it is very important
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        PrintWriter out = response.getWriter();

        if (requestNo == null)
            requestNo = "";// In case not saved draft
        if (currentNodeId == null || "".equals(currentNodeId))
            currentNodeId = "0";// Begin

        StaffVO staff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);

        try {
            dbManager = DBManagerFactory.getDBManager();
            WorkFlowNodeDAO nodeDao = new WorkFlowNodeDAO(dbManager);
            WorkFlowItemVO nextNode = nodeDao.getNextNodeById(Integer.parseInt(formSystemId), currentNodeId, requestNo,
                    staff.getStaffCode());

            if (CommonName.NODE_TYPE_OPTIONAL.equals(nextNode.getItemType())) {
                out.print("1");
            } else if (CommonName.NODE_TYPE_SELECTAPPROVER.equals(nextNode.getItemType())) {
                out.print("2");
            } else {
                out.print("0");
            }

        } catch (Exception ex) {
            throw ex;
        } finally {
            if (dbManager != null) {
                dbManager.freeConnection();
            }
        }
        return;
    }

    private static String decdr(String str, boolean booln) {
        if (booln)
            return CommonUtil.decoderURL(str);
        else
            return str;
    }
    
    public ActionLocation editComment(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	String comment = request.getParameter("comment");
    	String requestNo = request.getParameter("requestNo");
    	int processId = Integer.parseInt(request.getParameter("processId"));
    	IDBManager dbManager = null;
    	try {
			dbManager = DBManagerFactory.getDBManager();
			OTRecordDAO dao = new OTRecordDAO(dbManager);
			dao.editProcessComment(comment, processId, requestNo);
		} catch (Exception e) {
			dbManager.rollback();
            e.printStackTrace();
            request.setAttribute(CommonName.COMMON_ERROR_INFOR, e.toString());
		} finally {
            if (dbManager != null)
                dbManager.freeConnection();
        }
		
		return null;
    }

    public ActionLocation supplementAttachFile(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        IDBManager dbManager = null;
        String returnLabel = "tempsuccess";
        int formSystemId = Integer.parseInt(request.getParameter("formSystemId"));
        try {
            dbManager = DBManagerFactory.getDBManager();
            FormManageDAO formDao = new FormManageDAO(dbManager);
            Collection<FormSectionVO> sections = formDao.getSectionListByForm(formSystemId);
            FormSectionVO section = null;
            for (FormSectionVO tmpSection : sections) {
                if (tmpSection.getSectionType().equals("00")) {
                    section = tmpSection;
                    break;
                }
            }
            request.getRequestDispatcher("/uploadAction.it?method=enter&sectionId=" + section.getSectionId()).forward(
                    request, response);
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute(CommonName.COMMON_ERROR_INFOR, e.toString());
            returnLabel = "fail";
        } finally {
            if (dbManager != null)
                dbManager.freeConnection();
        }
        return mapping.findActionLocation(returnLabel);
    }
}