package com.aiait.eflow.wkf.action;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.*;

import com.aiait.eflow.basedata.dao.BaseDataDAO;
import com.aiait.eflow.common.*;
import com.aiait.eflow.common.helper.*;
import com.aiait.eflow.delegation.dao.DelegateDAO;
import com.aiait.eflow.formmanage.dao.FormManageDAO;
import com.aiait.eflow.formmanage.vo.*;
import com.aiait.eflow.housekeeping.dao.*;
import com.aiait.eflow.housekeeping.vo.*;
import com.aiait.eflow.util.*;
import com.aiait.eflow.wkf.dao.*;
import com.aiait.eflow.wkf.util.DataMapUtil;
import com.aiait.eflow.wkf.vo.*;
import com.aiait.framework.common.GlobalCommonName;
import com.aiait.framework.db.*;
import com.aiait.framework.exception.DAOException;
import com.aiait.framework.i18n.I18NMessageHelper;
import com.aiait.framework.mvc.action.*;
import com.aiait.framework.page.*;
import com.aiait.framework.util.CommonUtil;
import com.lowagie.text.*;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * NO. Task_ID Author Modify_Date Description 1 IT0958 Robin.Hou 11/01/2007 Modify 2 IT0958 Robin.Hou 11/01/2007 Invite
 * expert during processing form 3 IT0958 Robin.Hou 11/01/2007 Expert can give the advice during processing form 4
 * IT0958 Robin.Hou 11/02/2007 DS-006 When reject a form, current processor can select the reject to node 5 IT0958
 * Robin.Hou 11/02/2007 DS-007 During search form, it should add filter field "Requested_By" 6 IT0958 Robin.Hou
 * 11/02/2007 DS-013 Form can export to excel file 7 IT0958 Young.Yan 11/13/2007 Set the Invited-expert to null when
 * approved 8 IT0958 Robin.Hou 01/04/2008 DS-015 : add filter “SubmitBy” for ' listPersonalApplyForm' 9 4293096
 * Mario.Cao 05/10/2009 Add 'export to Sun AC upload file' methods: exportToSunAC,enterSettingExportSunAC 10 4293096
 * Mario.Cao 05/27/2009 Add 'generate invoice report' methods: exportToExcelInvoice 11 4293096 Mario.Cao 08/03/2009
 * Change email suffix to a template 12 N/A asnpg9a 12/07/2009 added search criteria "completed date".
 */

public class WorkFlowProcessAction extends DispatchAction {
    /**
     * Enter the personal work index page
     * 
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionLocation personalWorkIndex(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        long timeStart = System.currentTimeMillis();
        String resultLabel = "personalIndex";
        StaffVO currentStaff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
        // to get the count of all the formlist
        String supperOrgs = "";
        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            if (currentStaff.getOrgId() != null || !"".equals(currentStaff.getOrgId())) {
                CompanyDAO companyDao = new CompanyDAO(dbManager);
                supperOrgs = companyDao.getSuperCompanys(currentStaff.getOrgId());
                if (supperOrgs == null || "".equals(supperOrgs)) {
                    supperOrgs = "'" + currentStaff.getOrgId() + "'";
                }
            }
            // System.out.println("PersonalWorkIndex Time 1: " + (System.currentTimeMillis() - timeStart));

            WorkFlowProcessDAO dao = new WorkFlowProcessDAO(dbManager);
            WorkFlowProcessVO queryVo = new WorkFlowProcessVO();
            Collection list = dao.getAppliedFormListByStaff(currentStaff, queryVo);
            if (list == null) {
                request.setAttribute("appliedCount", "0");
            } else {
                request.setAttribute("appliedCount", "" + list.size());
            }
            // System.out.println("PersonalWorkIndex Time 2: " + (System.currentTimeMillis() - timeStart));
            PersonalWorkDAO pDao = new PersonalWorkDAO(dbManager);
            int publishedFormCount = pDao.getPublishedFormCount(supperOrgs);
            request.setAttribute("publishedFormCount", "" + publishedFormCount);
            // System.out.println("PersonalWorkIndex Time 3: " + (System.currentTimeMillis() - timeStart));
            int needHandleFormCount = pDao.getNeedHandleFormCount(currentStaff.getStaffCode());
            request.setAttribute("needHandleFormCount", "" + needHandleFormCount);
            // System.out.println("PersonalWorkIndex Time 4: " + (System.currentTimeMillis() - timeStart));

            // int completedFormCount = pDao.getCompletedFormCount();
            // request.setAttribute("completedFormCount", "" +
            // completedFormCount);
            String formTypes = "";
            AuthorityHelper authority = AuthorityHelper.getInstance();

            Collection typeList = FormTypeHelper.getInstance().getFormTypeList();
            if (typeList != null && typeList.size() > 0) {
                Iterator it = typeList.iterator();
                while (it.hasNext()) {
                    FormTypeVO typeVo = (FormTypeVO) it.next();
                    if (authority.checkAuthorityByFormType(currentStaff.getCurrentRoleId(),
                            ModuleOperateName.MODULE_FORM_INQUIRY, typeVo.getFormTypeId())) {
                        formTypes = formTypes + "'" + typeVo.getFormTypeId() + "',";
                    }
                }
            }
            /**
             * if(authority.checkAuthority(ModuleOperateName.MODULE_FORM_INQUIRY
             * ,ModuleOperateName.OPER_FORM_INQUIRY_TS)){ formTypes = formTypes + "'01',"; }
             * if(authority.checkAuthority(ModuleOperateName.
             * MODULE_FORM_INQUIRY,ModuleOperateName.OPER_FORM_INQUIRY_ADMIN)){ formTypes = formTypes + "'02',"; }
             * if(authority.checkAuthority(ModuleOperateName .MODULE_FORM_INQUIRY,
             * ModuleOperateName.OPER_FORM_INQUIRY_ACCOUNT)){ formTypes = formTypes + "'03',"; }
             * if(authority.checkAuthority(ModuleOperateName
             * .MODULE_FORM_INQUIRY,ModuleOperateName.OPER_FORM_INQUIRY_HR)){ formTypes = formTypes + "'04',"; }
             * if(authority.checkAuthority(ModuleOperateName
             * .MODULE_FORM_INQUIRY,ModuleOperateName.OPER_FORM_INQUIRY_EFLOW)){ formTypes = formTypes + "'05',"; }
             **/
            if (!"".equals(formTypes)) {
                formTypes = formTypes.substring(0, formTypes.length() - 1);
            }
            if (!"".equals(formTypes)) {
                int canQueryFormCount = pDao.getCanQueryFormCount(formTypes, supperOrgs);
                request.setAttribute("canQueryFormCount", "" + canQueryFormCount);
            } else {
                request.setAttribute("canQueryFormCount", "0");
            }
            // System.out.println("PersonalWorkIndex Time 5: " + (System.currentTimeMillis() - timeStart));
            queryVo.setFormType(formTypes);
            // Collection overTimeList = dao.getAllOvertimeForm(queryVo);
            int overtimeFormCount = 0;
            // if (overTimeList != null) {
            // overtimeFormCount = overTimeList.size();
            // }
            // System.out.println("PersonalWorkIndex Time 6: " + (System.currentTimeMillis() - timeStart));
            overtimeFormCount = pDao.getOvertimeFormCount(queryVo);
            request.setAttribute("overtimeFormCount", "" + overtimeFormCount);
        } catch (DAOException e) {
            resultLabel = "fail";
            e.printStackTrace();
        } finally {
            dbManager.freeConnection();
        }
        return mapping.findActionLocation(resultLabel);
    }

    /**
     * IT0958 DS-013 显示导出EXCEL文件的设置页面（选择需要导出的FIELD页面）
     * 
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionLocation enterSettingExportExcel(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String resultLabel = "exportExcelSetPage";
        String formSystemId = (String) request.getParameter("formSystemId");
        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            FormManageDAO formDao = new FormManageDAO(dbManager);
            FormManageVO form = formDao.getFormBySystemId(Integer.parseInt(formSystemId));
            request.setAttribute("form", form);
            List exportFields = formDao.getExportFields(Integer.parseInt(formSystemId), 1);
            request.setAttribute("exportFields", exportFields);
        } catch (DAOException e) {
            request.setAttribute(CommonName.COMMON_ERROR_INFOR, e.getMessage());
            resultLabel = "fail";
            e.printStackTrace();
        } finally {
            dbManager.freeConnection();
        }
        // 如果是AdvanceQuery中的导出，则需要处理其动态的查询条件
        return mapping.findActionLocation(resultLabel);
    }

    /**
     * IT0958 DS-013 生成Excel文件
     * 
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionLocation exportToExcel(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String resultLabel = "exportToExcelFile";
        String fileType = (String) request.getParameter("fileType");
        String pageSize = (String) request.getParameter("pageSize");
        // default is A4
        if (pageSize == null && "".equals(pageSize)) {
            pageSize = "2";
        }
        boolean landscape = true;
        Rectangle page = null;
        if (!landscape) {
            if ("".equals(pageSize) || "2".equals(pageSize)) {
                page = PageSize.A4;
            } else if ("1".equals(pageSize)) {
                page = PageSize.A3;
            } else if ("3".equals(pageSize)) {
                page = PageSize.B3;
            } else if ("4".equals(pageSize)) {
                page = PageSize.B4;
            }
        } else {
            if ("".equals(pageSize) || "2".equals(pageSize)) {
                page = PageSize.A4.rotate();
            } else if ("1".equals(pageSize)) {
                page = PageSize.A3.rotate();
            } else if ("3".equals(pageSize)) {
                page = PageSize.B3.rotate();
            } else if ("4".equals(pageSize)) {
                page = PageSize.B4.rotate();
            }
        }

        // default is 'excel'
        if (fileType == null || "".equals(fileType)) {
            fileType = "excel";
        }
        // queryType: 01 --- 来自于 Form Inquiry 模块的导出 （查询条件固定）
        // 02 --- 来自于 Advance Query 模块的导出（动态查询条件）
        String queryType = (String) request.getParameter("queryType");
        String formSystemId = (String) request.getParameter("formSystemId");
        String saveConfig = (String) request.getParameter("save_config");

        String[] temp = request.getParameterValues("fieldId");

        if (temp == null || temp.length < 1) {
            resultLabel = "fail";
            request.setAttribute(CommonName.COMMON_TIP_INFO, I18NMessageHelper.getMessage("common.notselfields"));
            return mapping.findActionLocation(resultLabel);
        }
        String[] fieldArray = new String[temp.length];

        Collection fields = new ArrayList();
        Collection list = new ArrayList();
        for (int i = 0; i < temp.length; i++) {
            String[] sectionField = StringUtil.split(temp[i], "||");
            fieldArray[i] = sectionField[1];
            if (list.contains(sectionField[0]) == false) {
                list.add(sectionField[0]);
            }
            FormSectionFieldVO field = new FormSectionFieldVO();
            field.setSectionId(sectionField[0]);
            field.setFieldId(sectionField[1]);
            fields.add(field);
        }
        String[] sectionIds = new String[list.size()];
        Iterator it = list.iterator();
        int i = 0;
        while (it.hasNext()) {
            sectionIds[i] = (String) it.next();
            i++;
        }

        if ("Yes".equalsIgnoreCase(saveConfig)) {
            IDBManager dbManager = null;
            try {
                dbManager = DBManagerFactory.getDBManager();
                FormManageDAO formDao = new FormManageDAO(dbManager);
                formDao.saveExportFields(Integer.parseInt(formSystemId), 1, fields);
            } catch (Exception ex) {
                ex.printStackTrace();
                request.setAttribute(CommonName.COMMON_ERROR_INFOR, ex.getMessage());
                resultLabel = "fail";
                return mapping.findActionLocation(resultLabel);
            } finally {
                if (dbManager != null) {
                    dbManager.freeConnection();
                }
            }
        }

        if ("01".equals(queryType)) {
            resultLabel = processForInquiryExport(request, temp, fieldArray, sectionIds);
        } else if ("02".equals(queryType)) {
            resultLabel = processForAdvanceQueryExport(request, temp, fieldArray, sectionIds);
        }
        if ("pdf".equals(fileType)) {
            HashMap formFieldMap = (HashMap) request.getAttribute("formFieldMap");
            Collection resultList = (ArrayList) request.getAttribute("resultList");
            FormManageVO form = (FormManageVO) request.getAttribute("form");

            TableDataListVO tableDataList = (TableDataListVO) request.getAttribute("tableDataList");
            HashMap utilMap = new HashMap();
            utilMap.put("tableDataList", tableDataList);

            DocumentException ex = null;
            ByteArrayOutputStream baosPDF = null;
            try {
                baosPDF = ExportFileUtil.exportPDF(page, resultList, form, fieldArray, formFieldMap, utilMap);

                StringBuffer sbFilename = new StringBuffer();
                sbFilename.append("filename_");
                sbFilename.append(System.currentTimeMillis());
                sbFilename.append(".pdf");

                response.setHeader("Cache-Control", "max-age=30");
                response.setContentType("application/pdf");

                StringBuffer sbContentDispValue = new StringBuffer();
                sbContentDispValue.append("inline");
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

            return null;
        }

        return mapping.findActionLocation(resultLabel);
    }

    /**
     * ePayment 生成ACE INTERFACE文件
     * 
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionLocation exportToAce(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String resultLabel = "exportToAce";
        String formType = (String) request.getParameter("formType");
        IDBManager dbManager = null;
        WorkFlowProcessVO vo = new WorkFlowProcessVO();

        if (formType != null && !"".equals(formType)) {
            vo.setFormType("'" + formType + "'");
        } else {
            // 如果是查询所有form，则需要知道当前user可以查看到的form类型
            String formTypes = "";
            StaffVO currentStaff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
            AuthorityHelper authority = AuthorityHelper.getInstance();
            Collection typeList = FormTypeHelper.getInstance().getFormTypeList();
            if (typeList != null && typeList.size() > 0) {
                Iterator it = typeList.iterator();
                while (it.hasNext()) {
                    FormTypeVO typeVo = (FormTypeVO) it.next();
                    if (authority.checkAuthorityByFormType(currentStaff.getCurrentRoleId(),
                            ModuleOperateName.MODULE_FORM_INQUIRY, typeVo.getFormTypeId())) {
                        formTypes = formTypes + "'" + typeVo.getFormTypeId() + "',";
                    }
                }
            }
            if (!"".equals(formTypes)) {
                formTypes = formTypes.substring(0, formTypes.length() - 1);
            }
            vo.setFormType(formTypes);
        }

        StaffVO currentStaff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
        String supperOrgs = currentStaff.getUpperCompanys();
        String lowerOrgs = currentStaff.getLowerCompanys();

        try {
            dbManager = DBManagerFactory.getDBManager();
            FormManageDAO formDao = new FormManageDAO(dbManager);
            Collection formSelectList = formDao.getFormListByTypeList(vo.getFormType(), supperOrgs, null);

            String orgId = (String) request.getParameter("companyId");
            String requestNo = (String) request.getParameter("requestNo");
            String status = (String) request.getParameter("status");
            String requestedBy = (String) request.getParameter("requestedBy");
            String beginSubmissionDate = (String) request.getParameter("beginSubmissionDate");
            String endSubmissionDate = (String) request.getParameter("endSubmissionDate");
            String lockedForm = (String) request.getParameter("lockedForm");
            String beginCompleteDate = (String) request.getParameter("beginCompletedDate");
            vo.setBeginCompleteDate(beginCompleteDate);
            String endCompleteDate = (String) request.getParameter("endCompletedDate");
            vo.setEndCompleteDate(endCompleteDate);

            if (lockedForm != null && !"".equals(lockedForm)) {
                vo.setInProcess(lockedForm);
            }

            if (requestNo != null && !"".equals(requestNo)) {
                vo.setRequestNo(requestNo);
            } else {
                vo.setRequestNo("");
            }

            if (status != null && !"".equals(status)) {
                vo.setStatus(status);
            }

            vo.setOrgId(orgId);

            if (requestedBy != null && !"".equals(requestedBy)) {
                vo.setRequestStaffCode(requestedBy);
            } else {
                vo.setRequestStaffCode("");
            }

            vo.setBeginSubmissionDate(beginSubmissionDate);
            vo.setEndSubmissionDate(endSubmissionDate);

            String formSystemId = (String) request.getParameter("formSystemId");
            if (formSystemId != null && !"".equals(formSystemId)) {
                vo.setFormSystemId(Integer.parseInt(formSystemId));
            }

            // List all records
            PageVO page = new PageVO();
            page.setPageSize(0);
            page.setCurrentPage(1);

            exportAceDAO dao = new exportAceDAO(dbManager);
            Collection list = dao.exportAce(vo, page, supperOrgs);
            request.setAttribute("exportAceList", list);
        } catch (DAOException e) {
            resultLabel = "fail";
            request.setAttribute(CommonName.COMMON_ERROR_INFOR, e.toString());
            e.printStackTrace();
        } finally {
            if (dbManager != null) {
                dbManager.freeConnection();
            }
        }
        return mapping.findActionLocation(resultLabel);
    }

    private String processForAdvanceQueryExport(HttpServletRequest request, String[] temp, String[] fieldArray,
            String[] sectionIds) throws Exception {
        return processForAdvanceQueryExport(request, temp, fieldArray, sectionIds, "");
    }

    private String processForAdvanceQueryExport(HttpServletRequest request, String[] temp, String[] fieldArray,
            String[] sectionIds, String queryType) throws Exception {
        String resultLabel = "exportToExcelFile";
        String formSystemId = (String) request.getParameter("formSystemId");
        Collection queryConditionList = new ArrayList();
        String[] conditionStr = request.getParameterValues("conditionStr");
        if (conditionStr != null && conditionStr.length > 0) {
            for (int i = 0; i < conditionStr.length; i++) {
                // System.out.println("--------:"+conditionStr);
                String[] conditionStrTemp = StringUtil.split(conditionStr[i], "||"); // sectionId
                                                                                     // ||
                                                                                     // fieldId
                                                                                     // ||
                                                                                     // compareType
                                                                                     // ||
                                                                                     // fieldValue
                                                                                     // ||
                                                                                     // logicType
                                                                                     // ||
                                                                                     // fieldLabel
                                                                                     // ||
                                                                                     // compareLabel
                                                                                     // ||
                                                                                     // isFunction
                // System.out.println(temp[0]+"."+temp[1]+""+temp[2]+temp[3]+temp[4]+"");
                NodeConditionVO condition = new NodeConditionVO();
                condition.setSectionId(conditionStrTemp[0]);
                condition.setFieldId(conditionStrTemp[1]);
                condition.setCompareType(conditionStrTemp[2]);
                condition.setCompareValue(conditionStrTemp[3]);
                condition.setLogicType(conditionStrTemp[4]);
                condition.setFieldLabel(conditionStrTemp[5]);
                condition.setCompareLabel(conditionStrTemp[6]);
                condition.setIsFunction(conditionStrTemp[7]);
                queryConditionList.add(condition);
            }
        }

        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            WorkFlowProcessDAO dao = new WorkFlowProcessDAO(dbManager);

            Collection list = null;
            if (queryType != null && !"".equals(queryType)) {
                StaffVO currentStaff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
                list = dao.getExportExcelForCommonQuery(queryConditionList, formSystemId, sectionIds, temp, queryType,
                        currentStaff.getStaffCode());
            } else {
                list = dao.getExportExcelForCommonQuery(queryConditionList, formSystemId, sectionIds, temp);
            }

            request.setAttribute("resultList", list);
            TableDataListVO tableDataList = dao.getExportTableDataListAdvance(formSystemId, sectionIds,
                    queryConditionList);
            request.setAttribute("tableDataList", tableDataList);
            FormManageDAO formDao = new FormManageDAO(dbManager);
            FormManageVO form = formDao.getFormBaseInforBySystemId(Integer.parseInt(formSystemId));

            HashMap formFieldMap = formDao.getFieldHashMapListByForm(Integer.parseInt(formSystemId));

            request.setAttribute("form", form);
            request.setAttribute("fieldArray", fieldArray);
            request.setAttribute("formFieldMap", formFieldMap);
        } catch (DAOException e) {
            resultLabel = "fail";
            e.printStackTrace();
        } finally {
            dbManager.freeConnection();
        }
        return resultLabel;
    }

    private String processForInquiryExport(HttpServletRequest request, String[] temp, String[] fieldArray,
            String[] sectionIds) throws Exception {
        String resultLabel = "exportToExcelFile";
        String formSystemId = (String) request.getParameter("formSystemId");
        String requestNo = (String) request.getParameter("requestNo");
        String status = (String) request.getParameter("status");
        String orgId = (String) request.getParameter("orgId");
        String requestedBy = (String) request.getParameter("requestedBy");
        String beginDate = (String) request.getParameter("beginDate");
        String endDate = (String) request.getParameter("endDate");
        String beginCompletedDate = (String) request.getParameter("beginCompletedDate");
        String endCompletedDate = (String) request.getParameter("endCompletedDate");
        String processType = request.getParameter("processType");

        WorkFlowProcessVO conditionVo = new WorkFlowProcessVO();
        conditionVo.setStatus(status);
        conditionVo.setRequestStaffCode(requestedBy);
        conditionVo.setBeginSubmissionDate(beginDate);
        conditionVo.setEndSubmissionDate(endDate);
        conditionVo.setRequestNo(requestNo);
        conditionVo.setOrgId(orgId);
        // System.out.println("orgId: " + orgId);
        conditionVo.setBeginCompleteDate(beginCompletedDate);
        conditionVo.setEndCompleteDate(endCompletedDate);
        conditionVo.setNodeType(processType);

        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            WorkFlowProcessDAO processDao = new WorkFlowProcessDAO(dbManager);
            Collection resultList = processDao.getExportFileList(formSystemId, temp, sectionIds, conditionVo);
            request.setAttribute("resultList", resultList);
            TableDataListVO tableDataList = processDao.getExportTableDataList(formSystemId, sectionIds, conditionVo);
            request.setAttribute("tableDataList", tableDataList);
            FormManageDAO formDao = new FormManageDAO(dbManager);
            FormManageVO form = formDao.getFormBaseInforBySystemId(Integer.parseInt(formSystemId));

            HashMap formFieldMap = formDao.getFieldHashMapListByForm(Integer.parseInt(formSystemId));

            request.setAttribute("form", form);
            request.setAttribute("fieldArray", fieldArray);
            request.setAttribute("formFieldMap", formFieldMap);

        } catch (DAOException e) {
            request.setAttribute(CommonName.COMMON_ERROR_INFOR, e.getMessage());
            resultLabel = "fail";
            e.printStackTrace();
        } finally {
            dbManager.freeConnection();
        }
        return resultLabel;
    }
    /**
     * 综合查询（查询指定条件的form）
     * 
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionLocation listInquiryFormAjax(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String formType = (String) request.getParameter("formType");
        IDBManager dbManager = null;
        WorkFlowProcessVO vo = new WorkFlowProcessVO();
        PrintWriter out = response.getWriter();
        // 增加分页查询功能
        String pagenum = (String) request.getParameter(GlobalCommonName.PAGE_CURRENT_NUM);
        if (pagenum == null) {
            pagenum = "1";
        }
        // HashMap paramMap = new HashMap(); //用来保存查询参数
        PageVO page = new PageVO(Integer.parseInt(ParamConfigHelper.getInstance().getParamValue(
                CommonName.EVERY_PAGE_RECORDS_NUM, CommonName.DEFAULT_RECORDS_NUM)));
        page.setCurrentPage(Integer.parseInt(pagenum));

        if (formType != null && !"".equals(formType)) {
            vo.setFormType("'" + formType + "'");
            // paramMap.put("formType",formType);
        } else {
            // 如果是查询所有form，则需要知道当前user可以查看到的form类型
            String formTypes = "";
            StaffVO currentStaff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
            AuthorityHelper authority = AuthorityHelper.getInstance();
            Collection typeList = FormTypeHelper.getInstance().getFormTypeList();
            if (typeList != null && typeList.size() > 0) {
                Iterator it = typeList.iterator();
                while (it.hasNext()) {
                    FormTypeVO typeVo = (FormTypeVO) it.next();
                    if (authority.checkAuthorityByFormType(currentStaff.getCurrentRoleId(),
                            ModuleOperateName.MODULE_FORM_INQUIRY, typeVo.getFormTypeId())) {
                        formTypes = formTypes + "'" + typeVo.getFormTypeId() + "',";
                    }
                }
            }
            if (!"".equals(formTypes)) {
                formTypes = formTypes.substring(0, formTypes.length() - 1);
            }
            vo.setFormType(formTypes);
            // paramMap.put("formType",formTypes);
        }
        StaffVO currentStaff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
        String supperOrgs = currentStaff.getUpperCompanys();// CompanyHelper.getInstance().getUpperCompany(currentStaff.getOrgId());
        String lowerOrgs = currentStaff.getLowerCompanys(); // CompanyHelper.getInstance().getLowerCompany(currentStaff.getOrgId());
        try {
            dbManager = DBManagerFactory.getDBManager();
            FormManageDAO formDao = new FormManageDAO(dbManager);
            
            Collection formSelectList = formDao.getFormListByTypeList(vo.getFormType(), supperOrgs, null);
            request.setAttribute("formSelectList", formSelectList);

            String orgId = (String) request.getParameter("companyId");

            // if user enter the page by clicking the left menu tree,it needn't
            // to query!
            String needQuery = (String) request.getParameter("needquery");
            if (needQuery != null && "false".equals(needQuery)) {
                Collection list = new ArrayList();
                request.setAttribute("overtimeFormList", list);
                // 赋予一个初始化的时间段（一个星期）
                ParamConfigHelper paramHelper = ParamConfigHelper.getInstance();
                String defaultDays = paramHelper.getParamValue("list_data_days");
                int days = 0;
                if (defaultDays == null || "".equals(defaultDays)) {
                    days = -7;
                } else {
                    days = -Integer.parseInt(defaultDays);
                }
                String beginSubmissionDate = "";
                String endSubmissionDate = "";
                String paramCode=ParamConfigHelper.getInstance().getParamValue("dateFormat");
                SimpleDateFormat bartDateFormat = new SimpleDateFormat(paramCode);
                Date now = new Date();
                endSubmissionDate = bartDateFormat.format(now);
                beginSubmissionDate = StringUtil.afterNDay(days, paramCode);
                request.setAttribute("beginSubmissionDate2", beginSubmissionDate);
                request.setAttribute("endSubmissionDate2", endSubmissionDate);

                // default same as submission date
                request.setAttribute("beginCompleteDate", beginSubmissionDate);
                request.setAttribute("endCompleteDate", endSubmissionDate);
            } else {
                String requestNo = (String) request.getParameter("requestNo");
                String status = (String) request.getParameter("status");

                // IT0958 DS-007 Begin
                String requestedBy = (String) request.getParameter("requestedBy");
                // IT0958 DS-008 End
                String beginSubmissionDate = (String) request.getParameter("beginSubmissionDate2");
                String endSubmissionDate = (String) request.getParameter("endSubmissionDate2");
                // IT0973 BEGIN
                String lockedForm = (String) request.getParameter("lockedForm");
                if (lockedForm != null && !"".equals(lockedForm)) {
                    vo.setInProcess(lockedForm);
                    // paramMap.put("lockedForm",lockedForm);
                }
                // IT0973 END

                if (requestNo != null && !"".equals(requestNo)) {
                    vo.setRequestNo(requestNo);
                    // paramMap.put("requestNo",requestNo);
                } else {
                    vo.setRequestNo("");
                }
                if (status != null && !"".equals(status)) {
                    vo.setStatus(status);
                    // paramMap.put("status",status);
                }

                vo.setOrgId(orgId);

                if (requestedBy != null && !"".equals(requestedBy)) {
                    vo.setRequestStaffCode(requestedBy);
                    // paramMap.put("requestedBy",requestedBy);
                } else {
                    vo.setRequestStaffCode("");
                }

                // vo.setOrgId(supperOrgs);
                // vo.setOrgId(lowerOrgs);
                // paramMap.put("orgId",supperOrgs);
                if ((beginSubmissionDate != null && !"".equals(beginSubmissionDate))
                        || (endSubmissionDate != null && !"".equals(endSubmissionDate))){
                	
                	vo.setBeginSubmissionDate(beginSubmissionDate);
                    vo.setEndSubmissionDate(endSubmissionDate);
                    System.err.println(vo.getBeginSubmissionDate());
                    System.err.println(vo.getEndSubmissionDate());
                	 vo.setStatus("04");//
                }else {
                    vo.setBeginSubmissionDate("");
                    vo.setEndSubmissionDate("");
                }

                // paramMap.put("beginSubmissionDate",beginSubmissionDate);
                // paramMap.put("endSubmissionDate",endSubmissionDate);

                String formSystemId = (String) request.getParameter("formSystemId");
                if (formSystemId != null && !"".equals(formSystemId)) {
                	
                    vo.setFormSystemId(Integer.parseInt(formSystemId));
                    // paramMap.put("formSystemId",formSystemId);
                }

               String nodeName = (String)request.getParameter("nodeNames");
               request.setAttribute("nodeName", nodeName);
               System.out.println("-----------"+nodeName);
               if(nodeName != null && !"".equals(nodeName)){
            	    vo.setNodeName(nodeName);
               }
            	   
                
                String beginCompletedDate = (String) request.getParameter("beginCompletedDate");
                vo.setBeginCompleteDate(beginCompletedDate);
                String endCompletedDate = (String) request.getParameter("endCompletedDate");
                vo.setEndCompleteDate(endCompletedDate);
                if ((beginCompletedDate != null && !"".equals(beginCompletedDate))
                        || (endCompletedDate != null && !"".equals(endCompletedDate))) {
                    // Set status as completed
                    vo.setStatus("04");
                    
                    //beginSubmissionDate setb
                }

                String processType = request.getParameter("processType");
                vo.setNodeType(processType);

                ListInquiryFormDAO dao = new ListInquiryFormDAO(dbManager);

                // page.setParamMap(paramMap);
                // Collection list = dao.getInquiryFormList(vo);
                //System.out.println("-----------"+request.getParameter("method"));
                //String ss = request.getParameter("nodeNames");
                //System.out.println("-----------"+ss);
                int totalRecordsNum = dao.getTotalRecordsNum(vo, page, supperOrgs);
                page = PageUtil.createPage(page, totalRecordsNum);
                Collection list = dao.searchInquiryForm(vo, page, supperOrgs);
                System.out.println("00000000");
                request.setAttribute(GlobalCommonName.PAGE_INFORMATION_NAME, page);
                request.setAttribute("inquiryFormList", list);
                if (orgId == null || "".equals(orgId)) {
                    orgId = currentStaff.getOrgId();
                }
                StaffDAO staffDao = new StaffDAO(dbManager);
                Collection staffList = null;
                if ("Y".equals(ParamConfigHelper.getInstance().getParamValue("show_terminated_staffs"))) {
                    staffList = staffDao.getATStaffListByCompany(orgId);
                    //staffDao.get
                } else {
                    staffList = staffDao.getStaffListByCompany(orgId);
                }
                request.setAttribute("staffList", staffList);
                System.out.println(Arrays.asList(staffList));
               
                //out.print(staffList);
                if(list!=null){
                	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                	Iterator formIt = list.iterator();
                	int i = 1;
                	StringBuffer sb = new StringBuffer();
                while(formIt.hasNext()){
                	WorkFlowProcessVO vo1 = (WorkFlowProcessVO)formIt.next();
                	
                	Date cDate = null;
                	if(vo1.getSubmissionDateStr()!=null && !"".equals(vo1.getSubmissionDateStr())){
                	   cDate = df.parse(vo1.getSubmissionDateStr());
                	}
                	String handleDate = "";
                	if (vo1.getHandleDateStr() != null && !"".equals(vo1.getHandleDateStr())) {
                	    handleDate = StringUtil.getDateStr(df.parse(vo1.getHandleDateStr()), "MM/dd/yyyy HH:mm:ss");
                	}
                	
                	String processing_by = null;
                    if (CommonName.NODE_TYPE_WAITING.equals(vo1.getNodeType())) {
                        processing_by = vo1.getNodeName();
                    } else {
                        processing_by = (!"-1".equals(vo1.getNodeId()) && vo1.getIsDeputy()!=null && "1".equals(vo1.getIsDeputy()) && vo1.getOriginProcessor()!=null && vo1.getOriginProcessor().indexOf(",")==-1 && (!"".equals(vo1.getCurrentProcessor())))?"<a href='#' title=\"It is "+StaffTeamHelper.getInstance().getStaffNameByCode(vo1.getOriginProcessor()).trim()+"'s deputy.\"><img  border=0 src='"+request.getContextPath()+"/images/deputy.gif'></a>":"";
                        processing_by += StaffTeamHelper.getInstance().getStaffNameByCode(vo1.getCurrentProcessor());
                    }
                	sb.append("<tr class='tr_change'> " + 
                  "<td align='center'>&nbsp;" + i + "&nbsp;</td> " + 
                  "<td align='center'><input type='checkbox' name='requestNos' value="+vo1.getRequestNo()+"></td> " + 
                  "<td ><a href='javascript:openFormWithLayer('" + request.getContextPath() + "/formManageAction.it?method=displayFormContent&operateType=view&viewFlag=false&requestNo=" + vo1.getRequestNo() + "&formSystemId=" + vo1.getFormSystemId() + "');'"+
                  	"	" + vo1.getHtmlTitleAttr() + ">" + vo1.getRequestNo() + "&nbsp;&nbsp;</td> " + 
                  "<td >" + vo1.getFormName() + "&nbsp;&nbsp;</td> " + 
                 " <td >" + vo1.getHighlightContent() + "</td> " + 
                " <td >" + DataMapUtil.covertNodeStatus(vo1.getStatus()) + "&nbsp;&nbsp;</td> " + 
                " <td >" + ((vo1.getSubmissionDateStr()!=null && !"".equals(vo1.getSubmissionDateStr()))?StringUtil.getDateStr(cDate,"yyyy-MM-dd"):"") + "&nbsp;&nbsp;</td> " + 
                " <td>" + StaffTeamHelper.getInstance().getStaffNameByCode(vo1.getRequestStaffCode()) + "&nbsp;&nbsp;</td> " + 
                "  <td >" + handleDate + "</td> " +
                "  <td >"+ processing_by
                  +" </td> " + 
                  "  <td id='a2' ></td> ");
               
                  if(true){ 
                	  sb.append("<td align='center' >");
                    if((vo1.getStatus()!=null && ("03".equals(vo1.getStatus()) || "02".equals(vo1.getStatus()) || "01".equals(vo1.getStatus())))){ 
                    	sb.append("<a href='javascript:openSelectStaffWindow('" + vo1.getRequestNo() + "','" + vo1.getNodeId() + "','" + vo1.getRequestStaffCode() + "','" + vo1.getFormSystemId() + "')> Reassign </a>" );
                    }
                   
                    sb.append("</td>");
                  }
                  sb.append("</tr>");
               i++; 
               
             
               }
                out.print(sb);
              }
            }

            
            
        } 
        
        catch (DAOException e) {
          
            request.setAttribute(CommonName.COMMON_ERROR_INFOR, e.toString());
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
     * 综合查询（查询指定条件的form）
     * 
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionLocation listInquiryForm(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String resultLabel = "listInquiryForm";
        String formType = (String) request.getParameter("formType");
        IDBManager dbManager = null;
        WorkFlowProcessVO vo = new WorkFlowProcessVO();
        
        // 增加分页查询功能
        String pagenum = (String) request.getParameter(GlobalCommonName.PAGE_CURRENT_NUM);
        if (pagenum == null) {
            pagenum = "1";
        }
        // HashMap paramMap = new HashMap(); //用来保存查询参数
        PageVO page = new PageVO(Integer.parseInt(ParamConfigHelper.getInstance().getParamValue(
                CommonName.EVERY_PAGE_RECORDS_NUM, CommonName.DEFAULT_RECORDS_NUM)));
        page.setCurrentPage(Integer.parseInt(pagenum));

        if (formType != null && !"".equals(formType)) {
            vo.setFormType("'" + formType + "'");
            // paramMap.put("formType",formType);
        } else {
            // 如果是查询所有form，则需要知道当前user可以查看到的form类型
            String formTypes = "";
            StaffVO currentStaff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
            AuthorityHelper authority = AuthorityHelper.getInstance();
            Collection typeList = FormTypeHelper.getInstance().getFormTypeList();
            if (typeList != null && typeList.size() > 0) {
                Iterator it = typeList.iterator();
                while (it.hasNext()) {
                    FormTypeVO typeVo = (FormTypeVO) it.next();
                    if (authority.checkAuthorityByFormType(currentStaff.getCurrentRoleId(),
                            ModuleOperateName.MODULE_FORM_INQUIRY, typeVo.getFormTypeId())) {
                        formTypes = formTypes + "'" + typeVo.getFormTypeId() + "',";
                    }
                }
            }
            if (!"".equals(formTypes)) {
                formTypes = formTypes.substring(0, formTypes.length() - 1);
            }
            vo.setFormType(formTypes);
            // paramMap.put("formType",formTypes);
        }
        StaffVO currentStaff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
        String supperOrgs = currentStaff.getUpperCompanys();// CompanyHelper.getInstance().getUpperCompany(currentStaff.getOrgId());
        String lowerOrgs = currentStaff.getLowerCompanys(); // CompanyHelper.getInstance().getLowerCompany(currentStaff.getOrgId());
        try {
            dbManager = DBManagerFactory.getDBManager();
            FormManageDAO formDao = new FormManageDAO(dbManager);
            
            Collection formSelectList = formDao.getFormListByTypeList(vo.getFormType(), supperOrgs, null);
            request.setAttribute("formSelectList", formSelectList);

            String orgId = (String) request.getParameter("companyId");

            // if user enter the page by clicking the left menu tree,it needn't
            // to query!
            String needQuery = (String) request.getParameter("needquery");
            if (needQuery != null && "false".equals(needQuery)) {
                Collection list = new ArrayList();
                request.setAttribute("overtimeFormList", list);
                // 赋予一个初始化的时间段（一个星期）
                ParamConfigHelper paramHelper = ParamConfigHelper.getInstance();
                String defaultDays = paramHelper.getParamValue("list_data_days");
                int days = 0;
                if (defaultDays == null || "".equals(defaultDays)) {
                    days = -7;
                } else {
                    days = -Integer.parseInt(defaultDays);
                }
                String beginSubmissionDate = "";
                String endSubmissionDate = "";
                String paramCode=ParamConfigHelper.getInstance().getParamValue("dateFormat");
                SimpleDateFormat bartDateFormat = new SimpleDateFormat(paramCode);
                Date now = new Date();
                endSubmissionDate = bartDateFormat.format(now);
                beginSubmissionDate = StringUtil.afterNDay(days, paramCode);
                request.setAttribute("beginSubmissionDate2", beginSubmissionDate);
                request.setAttribute("endSubmissionDate2", endSubmissionDate);

                // default same as submission date
                request.setAttribute("beginCompleteDate", beginSubmissionDate);
                request.setAttribute("endCompleteDate", endSubmissionDate);
            } else {
                String requestNo = (String) request.getParameter("requestNo");
                String status = (String) request.getParameter("status");

                // IT0958 DS-007 Begin
                String requestedBy = (String) request.getParameter("requestedBy");
                // IT0958 DS-008 End
                String beginSubmissionDate = (String) request.getParameter("beginSubmissionDate2");
                String endSubmissionDate = (String) request.getParameter("endSubmissionDate2");
                // IT0973 BEGIN
                String lockedForm = (String) request.getParameter("lockedForm");
                if (lockedForm != null && !"".equals(lockedForm)) {
                    vo.setInProcess(lockedForm);
                    // paramMap.put("lockedForm",lockedForm);
                }
                // IT0973 END

                if (requestNo != null && !"".equals(requestNo)) {
                    vo.setRequestNo(requestNo);
                    // paramMap.put("requestNo",requestNo);
                } else {
                    vo.setRequestNo("");
                }
                if (status != null && !"".equals(status)) {
                    vo.setStatus(status);
                    // paramMap.put("status",status);
                }

                vo.setOrgId(orgId);

                if (requestedBy != null && !"".equals(requestedBy)) {
                    vo.setRequestStaffCode(requestedBy);
                    // paramMap.put("requestedBy",requestedBy);
                } else {
                    vo.setRequestStaffCode("");
                }

                // vo.setOrgId(supperOrgs);
                // vo.setOrgId(lowerOrgs);
                // paramMap.put("orgId",supperOrgs);
                
                vo.setBeginSubmissionDate(beginSubmissionDate);
                vo.setEndSubmissionDate(endSubmissionDate);

                // paramMap.put("beginSubmissionDate",beginSubmissionDate);
                // paramMap.put("endSubmissionDate",endSubmissionDate);

                String formSystemId = (String) request.getParameter("formSystemId");
                if (formSystemId != null && !"".equals(formSystemId)) {
                	
                    vo.setFormSystemId(Integer.parseInt(formSystemId));
                    // paramMap.put("formSystemId",formSystemId);
                }

               String nodeName = (String)request.getParameter("nodeName");
               
               if(nodeName != null && !"".equals(nodeName)){
            	    vo.setNodeName(nodeName);
               }
               
                String beginCompletedDate = (String) request.getParameter("beginCompletedDate");
                vo.setBeginCompleteDate(beginCompletedDate);
                String endCompletedDate = (String) request.getParameter("endCompletedDate");
                vo.setEndCompleteDate(endCompletedDate);
                if ((beginCompletedDate != null && !"".equals(beginCompletedDate))
                        || (endCompletedDate != null && !"".equals(endCompletedDate))) {
                    // Set status as completed
                    vo.setStatus("04");
                    
                    //beginSubmissionDate setb
                }

                String processType = request.getParameter("processType");
                vo.setNodeType(processType);

                ListInquiryFormDAO dao = new ListInquiryFormDAO(dbManager);

                // page.setParamMap(paramMap);
                // Collection list = dao.getInquiryFormList(vo);
                //System.out.println("-----------"+request.getParameter("method"));
                //String ss = request.getParameter("nodeNames");
                //System.out.println("-----------"+ss);
                int totalRecordsNum = dao.getTotalRecordsNum(vo, page, supperOrgs);
                page = PageUtil.createPage(page, totalRecordsNum);
                Collection list = dao.searchInquiryForm(vo, page, supperOrgs);
                System.out.println("00000000");
                request.setAttribute(GlobalCommonName.PAGE_INFORMATION_NAME, page);
                request.setAttribute("inquiryFormList", list);
            }

            if (orgId == null || "".equals(orgId)) {
                orgId = currentStaff.getOrgId();
            }
            StaffDAO staffDao = new StaffDAO(dbManager);
            Collection staffList = null;
            if ("Y".equals(ParamConfigHelper.getInstance().getParamValue("show_terminated_staffs"))) {
                staffList = staffDao.getATStaffListByCompany(orgId);
                //staffDao.get
            } else {
                staffList = staffDao.getStaffListByCompany(orgId);
            }
            request.setAttribute("staffList", staffList);

        } catch (DAOException e) {
            resultLabel = "fail";
            request.setAttribute(CommonName.COMMON_ERROR_INFOR, e.toString());
            e.printStackTrace();
        } finally {
            dbManager.freeConnection();
        }
        return mapping.findActionLocation(resultLabel);

    }

    /**
     * Export inquiry form data
     */
    public ActionLocation exportInquiryForm(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String resultLabel = "exportInquiryForm";
        String formType = (String) request.getParameter("formType");
        IDBManager dbManager = null;
        WorkFlowProcessVO vo = new WorkFlowProcessVO();

        if (formType != null && !"".equals(formType)) {
            vo.setFormType("'" + formType + "'");
        } else {
            // 如果是查询所有form，则需要知道当前user可以查看到的form类型
            String formTypes = "";
            StaffVO currentStaff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
            AuthorityHelper authority = AuthorityHelper.getInstance();
            Collection typeList = FormTypeHelper.getInstance().getFormTypeList();
            if (typeList != null && typeList.size() > 0) {
                Iterator it = typeList.iterator();
                while (it.hasNext()) {
                    FormTypeVO typeVo = (FormTypeVO) it.next();
                    if (authority.checkAuthorityByFormType(currentStaff.getCurrentRoleId(),
                            ModuleOperateName.MODULE_FORM_INQUIRY, typeVo.getFormTypeId())) {
                        formTypes = formTypes + "'" + typeVo.getFormTypeId() + "',";
                    }
                }
            }
            if (!"".equals(formTypes)) {
                formTypes = formTypes.substring(0, formTypes.length() - 1);
            }
            vo.setFormType(formTypes);
        }

        StaffVO currentStaff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
        String supperOrgs = currentStaff.getUpperCompanys();
        String lowerOrgs = currentStaff.getLowerCompanys();

        try {
            dbManager = DBManagerFactory.getDBManager();
            FormManageDAO formDao = new FormManageDAO(dbManager);
            Collection formSelectList = formDao.getFormListByTypeList(vo.getFormType(), supperOrgs, null);

            String orgId = (String) request.getParameter("companyId");
            String requestNo = (String) request.getParameter("requestNo");
            String status = (String) request.getParameter("status");
            String requestedBy = (String) request.getParameter("requestedBy");
            String beginSubmissionDate = (String) request.getParameter("beginSubmissionDate");
            String endSubmissionDate = (String) request.getParameter("endSubmissionDate");
            String lockedForm = (String) request.getParameter("lockedForm");

            if (lockedForm != null && !"".equals(lockedForm)) {
                vo.setInProcess(lockedForm);
            }

            if (requestNo != null && !"".equals(requestNo)) {
                vo.setRequestNo(requestNo);
            } else {
                vo.setRequestNo("");
            }

            if (status != null && !"".equals(status)) {
                vo.setStatus(status);
            }

            vo.setOrgId(orgId);

            if (requestedBy != null && !"".equals(requestedBy)) {
                vo.setRequestStaffCode(requestedBy);
            } else {
                vo.setRequestStaffCode("");
            }
            
            String nodeName = (String)request.getParameter("nodeNames");
            System.out.println("-----------"+nodeName);
            if(nodeName != null && !"".equals(nodeName)){
         	    vo.setNodeName(nodeName);
            }

            vo.setBeginSubmissionDate(beginSubmissionDate);
            vo.setEndSubmissionDate(endSubmissionDate);

            String formSystemId = (String) request.getParameter("formSystemId");
            if (formSystemId != null && !"".equals(formSystemId)) {
                vo.setFormSystemId(Integer.parseInt(formSystemId));
            }

            String beginCompleteDate = (String) request.getParameter("beginCompletedDate");
            vo.setBeginCompleteDate(beginCompleteDate);
            String endCompleteDate = (String) request.getParameter("endCompletedDate");
            vo.setEndCompleteDate(endCompleteDate);

            String processType = request.getParameter("processType");
            vo.setNodeType(processType);

            // List all records
            PageVO page = new PageVO();
            page.setPageSize(0);
            page.setCurrentPage(1);

            ListInquiryFormDAO dao = new ListInquiryFormDAO(dbManager);
            Collection list = dao.exportInquiryForm(vo, page, supperOrgs,nodeName);
            request.setAttribute("exportFormList", list);
        } catch (DAOException e) {
            resultLabel = "fail";
            request.setAttribute(CommonName.COMMON_ERROR_INFOR, e.toString());
            e.printStackTrace();
        } finally {
            if (dbManager != null) {
                dbManager.freeConnection();
            }
        }
        return mapping.findActionLocation(resultLabel);
        
    }

    /**
     * Export the selected forms to PDF file.
     */
    public ActionLocation exportPDF(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        ArrayList<Object[]> formList = new ArrayList<Object[]>();
        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            FormManageDAO formDao = new FormManageDAO(dbManager);
            WorkFlowProcessDAO processDao = new WorkFlowProcessDAO(dbManager);

            HashMap formMap = new HashMap();
            String[] requestNos = request.getParameterValues("requestNos");
            for (String requestNo : requestNos) {
                // System.out.println("requestNo: " + requestNo);
                Object[] formObj = new Object[3];
                WorkFlowProcessVO processVo = processDao.getProcessVO(requestNo);
                int formSystemId = processVo.getFormSystemId();
                FormManageVO form = (FormManageVO) formMap.get(Integer.valueOf(formSystemId));
                if (form == null) {
                    form = formDao.getFormBySystemId(formSystemId);
                    formMap.put(Integer.valueOf(formSystemId), form);
                }
                formObj[0] = form;
                HashMap sectionFieldMap = formDao.getFieldContentByForm(requestNo);
                formObj[1] = sectionFieldMap;
                Collection traceList = processDao.getProcessTraceList(requestNo, true);
                formObj[2] = traceList;
                formList.add(formObj);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            response.setContentType("text/html");
            PrintWriter writer = response.getWriter();
            writer.println("Error: Get Data");
            return null;
        } finally {
            if (dbManager != null) {
                dbManager.freeConnection();
            }
        }

        // Export to PDF
        ByteArrayOutputStream baosPDF = null;
        try {
            baosPDF = ExportFileUtil.exportPDFs(formList);

            StringBuffer sbFilename = new StringBuffer();
            sbFilename.append("filename_");
            sbFilename.append(System.currentTimeMillis());
            sbFilename.append(".pdf");

            response.setHeader("Cache-Control", "max-age=30");
            response.setContentType("application/pdf");

            StringBuffer sbContentDispValue = new StringBuffer();
            sbContentDispValue.append("inline");
            sbContentDispValue.append("; filename=");
            sbContentDispValue.append(sbFilename);

            response.setHeader("Content-disposition", sbContentDispValue.toString());

            response.setContentLength(baosPDF.size());
            ServletOutputStream sos;
            sos = response.getOutputStream();

            baosPDF.writeTo(sos);
            sos.flush();

        } catch (DocumentException dex) {
            dex.printStackTrace();
            response.setContentType("text/html");
            PrintWriter writer = response.getWriter();
            writer.println("Error: exportPDF");
        } finally {
            if (baosPDF != null) {
                baosPDF.reset();
            }
        }
        return null;
    }

    public ActionLocation unlockForms(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String resultLabel = "enterListInquiryForm";
        String[] requestNos = request.getParameterValues("requestNos");
        OperateLogVO logVo = new OperateLogVO();
        StaffVO currentStaff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
        logVo.setOperateStaffCode(currentStaff.getStaffCode());
        logVo.setOperateType(CommonName.OPERATE_LOG_TYPE_UNLOCK);
        logVo.setOperateDateStr(StringUtil.getCurrentDateStr("yyyy/MM/dd HH:mm:ss"));

        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            WorkFlowProcessDAO dao = new WorkFlowProcessDAO(dbManager);
            OperateLogDAO logDao = new OperateLogDAO(dbManager);

            dbManager.startTransaction();
            for (int i = 0; i < requestNos.length; i++) {
                logVo.setOperateDescription("Unlock form(" + requestNos[i] + ")");
                dao.unlockForm(requestNos[i]);
                logDao.save(logVo);
            }
            dbManager.commit();
        } catch (DAOException e) {
            dbManager.rollback();
            resultLabel = "fail";
            request.setAttribute(CommonName.COMMON_ERROR_INFOR, e.getMessage());
            e.printStackTrace();
        } finally {
            dbManager.freeConnection();
        }

        return mapping.findActionLocation(resultLabel);
    }

    /**
     * 进入申请者的万能查询（查询指定条件的form） Task Id : IT0973
     * 
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionLocation enterAdvanceQuery(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String resultLabel = "advanceQueryForm";
        return mapping.findActionLocation(resultLabel);
    }

    /**
     * 显示申请者的万能查询结果（查询指定条件的form） Task Id : IT0973
     * 
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionLocation listRequesterAdvanceQuery(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String resultLabel = "listRequesterAdvanceQuery";

        String formSystemId = (String) request.getParameter("formSystemId");

        String queryType = (String) request.getParameter("queryType");
        // queryType: 01 --- query his requested forms
        // 02 --- query his processed forms
        if (queryType == null || "".equals(queryType)) {
            queryType = "01";
        }

        String isExport = request.getParameter("isExport");
        String[] temp = request.getParameterValues("columnId");

        if (temp == null || temp.length < 1) {
            resultLabel = "fail";
            request.setAttribute(CommonName.COMMON_TIP_INFO, I18NMessageHelper.getMessage("common.notselfields"));
            return mapping.findActionLocation(resultLabel);
        }
        String[] fieldArray = new String[temp.length];

        Collection list = new ArrayList();
        for (int i = 0; i < temp.length; i++) {
            String[] sectionField = StringUtil.split(temp[i], "||");
            fieldArray[i] = sectionField[1];
            if (list.contains(sectionField[0]) == false) {
                list.add(sectionField[0]);
            }
        }
        String[] sectionIds = new String[list.size()];
        Iterator it = list.iterator();
        int i = 0;
        while (it.hasNext()) {
            sectionIds[i] = (String) it.next();
            i++;
        }

        resultLabel = processForAdvanceQueryExport(request, temp, fieldArray, sectionIds, queryType);

        if ("exportToExcelFile".equals(resultLabel)) {
            if ("1".equals(isExport)) {
                resultLabel = "xlsAdvanceQueryForm";
            } else {
                resultLabel = "listRequesterAdvanceQuery";
            }
        }

        return mapping.findActionLocation(resultLabel);
    }

    /**
     * 监控中的万能查询（查询指定条件的form） Task Id : IT0958
     * 
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionLocation enterCommonQueryForm(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String resultLabel = "commonQueryForm";
        return mapping.findActionLocation(resultLabel);
    }

    /**
     * 万能查询（查询指定条件的form） Task Id : IT0958
     * 
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionLocation listCommonQueryForm(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String resultLabel = "listCommonQueryForm";
        String formType = (String) request.getParameter("formType");
        String formSystemId = (String) request.getParameter("formSystemId");
        String expFormId = (String) request.getParameter("exp_form_id");
        Collection queryConditionList = new ArrayList();

        boolean queryByForm = true;
        if (formSystemId == null || "".equals(formSystemId.trim())) {
            queryByForm = false;
        }

        String[] conditionStr = request.getParameterValues("conditionStr");
        if (conditionStr != null && conditionStr.length > 0) {
            for (int i = 0; i < conditionStr.length; i++) {
                // System.out.println("conditionStr-" + i + ": " +
                // conditionStr[i]);
                // sectionId || fieldId || compareType || fieldValue ||
                // logicType || fieldLabel || compareLabel ||
                // isFunction
                String[] temp = StringUtil.split(conditionStr[i], "||");
                // System.out.println(temp[0]+"."+temp[1]+""+temp[2]+temp[3]+temp[4]+"");
                NodeConditionVO condition = new NodeConditionVO();
                int j = 0;
                if (queryByForm) {
                    condition.setSectionId(temp[j++]);
                }
                condition.setFieldId(temp[j++]);
                condition.setCompareType(temp[j++]);
                condition.setCompareValue(temp[j++]);
                condition.setLogicType(temp[j++]);
                condition.setFieldLabel(temp[j++]);
                condition.setCompareLabel(temp[j++]);
                condition.setIsFunction(temp[j++]);
                condition.setFiledType(temp[j++]);
                queryConditionList.add(condition);
            }
        }

        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            WorkFlowProcessDAO dao = new WorkFlowProcessDAO(dbManager);
            Collection list = null;
            if (queryByForm) {
                list = dao.getCommonQueryList(queryConditionList, formSystemId, "");
            } else {
                list = dao.getCommonQueryListByFormType(queryConditionList, formType, expFormId);
            }

            request.setAttribute("commonQueryFormList", list);

        } catch (DAOException e) {
            resultLabel = "fail";
            e.printStackTrace();
        } finally {
            dbManager.freeConnection();
        }

        return mapping.findActionLocation(resultLabel);
    }

    /**
     * 手动调整处理人的查询页面
     * 
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionLocation listManualAdjustForm(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String resultLabel = "listAdjustForm";
        String formType = (String) request.getParameter("formType");
        StaffVO currentStaff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
        String supperOrgs = currentStaff.getUpperCompanys(); // CompanyHelper.getInstance().getUpperCompany(currentStaff.getOrgId());

        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            FormManageDAO formDao = new FormManageDAO(dbManager);
            Collection formSelectList = formDao.getNewFormListByType(formType, supperOrgs);
            request.setAttribute("formSelectList", formSelectList);
            // if user enter the page by clicking the left menu tree,it needn't
            // to query!
            String needQuery = (String) request.getParameter("needquery");
            if (needQuery != null && "false".equals(needQuery)) {
                Collection list = new ArrayList();
                request.setAttribute("overtimeFormList", list);
                // 赋予一个初始化的时间段（一个星期）
                ParamConfigHelper paramHelper = ParamConfigHelper.getInstance();
                String defaultDays = paramHelper.getParamValue("list_data_days");
                int days = 0;
                if (defaultDays == null || "".equals(defaultDays)) {
                    days = -7;
                } else {
                    days = -Integer.parseInt(defaultDays);
                }
                String beginSubmissionDate = "";
                String endSubmissionDate = "";
                SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyy/MM/dd");
                Date now = new Date();
                endSubmissionDate = bartDateFormat.format(now);
                beginSubmissionDate = StringUtil.afterNDay(days, "yyyy/MM/dd");
                request.setAttribute("beginSubmissionDate", beginSubmissionDate);
                request.setAttribute("endSubmissionDate", endSubmissionDate);
            } else {
                String requestNo = (String) request.getParameter("requestNo");
                String processingBy = (String) request.getParameter("processingBy");
                String beginSubmissionDate = (String) request.getParameter("beginSubmissionDate");
                String endSubmissionDate = (String) request.getParameter("endSubmissionDate");
                WorkFlowProcessVO vo = new WorkFlowProcessVO();
                if (requestNo != null && !"".equals(requestNo)) {
                    vo.setRequestNo(requestNo);
                } else {
                    vo.setRequestNo("");
                }
                if (processingBy != null && !"".equals(processingBy)) {
                    vo.setCurrentProcessor(processingBy);
                }
                if (formType != null && !"".equals(formType)) {
                    vo.setFormType(formType);
                } else {
                    vo.setFormType("");
                }
                vo.setBeginSubmissionDate(beginSubmissionDate);
                vo.setEndSubmissionDate(endSubmissionDate);
                String formSystemId = (String) request.getParameter("formSystemId");
                if (formSystemId != null && !"".equals(formSystemId)) {
                    vo.setFormSystemId(Integer.parseInt(formSystemId));
                }
                WorkFlowProcessDAO dao = new WorkFlowProcessDAO(dbManager);
                Collection list = dao.getWaitingForManualFormList(vo);
                request.setAttribute("inquiryFormList", list);
            }
        } catch (DAOException e) {
            resultLabel = "fail";
            e.printStackTrace();
        } finally {
            dbManager.freeConnection();
        }
        return mapping.findActionLocation(resultLabel);

    }
    
    /**
     * 获取所有form信息用新的grid方式
     */
    public ActionLocation listNewRequestForm(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String resultLabel = "listNewRequestForm";
        
        return mapping.findActionLocation(resultLabel);
    }

    /*
    public ActionLocation listPersonalApplyForm(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String resultLabel = "listPersonalForm";

        // 获取当前用户信息
        StaffVO currentStaff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
        String requestNo = (String) request.getParameter("requestNo");
        if (requestNo == null) {
            requestNo = (String) request.getAttribute("requestNo");
            if (requestNo != null)
                request.setAttribute("requestNo", requestNo);
        }
        String formType = (String) request.getParameter("formType");
        String beginSubmissionDate = (String) request.getParameter("beginSubmissionDate");
        String endSubmissionDate = (String) request.getParameter("endSubmissionDate");
        String status = (String) request.getParameter("status");
        String submittedBy = (String) request.getParameter("submittedBy");
        String formSystemId = (String) request.getParameter("formSystemId");
        String isExport = request.getParameter("isExport");

        String comeFrom = (String) request.getParameter("comefrom");
        if (comeFrom != null && "left".equals(comeFrom)) {
            // 赋予一个初始化的时间段（一个星期）
            ParamConfigHelper paramHelper = ParamConfigHelper.getInstance();
            String defaultDays = paramHelper.getParamValue("list_data_days");
            int days = 0;
            if (defaultDays == null || "".equals(defaultDays)) {
                days = -7;
            } else {
                days = -Integer.parseInt(defaultDays);
            }

            SimpleDateFormat bartDateFormat = new SimpleDateFormat("MM/dd/yyyy");
            Date now = new Date();
            endSubmissionDate = bartDateFormat.format(now);
            beginSubmissionDate = StringUtil.afterNDay(days, "MM/dd/yyyy");
            request.setAttribute("beginSubmissionDate", beginSubmissionDate);
            request.setAttribute("endSubmissionDate", endSubmissionDate);
        }

        WorkFlowProcessVO queryVo = new WorkFlowProcessVO();
        queryVo.setRequestNo(requestNo);
        queryVo.setFormType(formType);
        queryVo.setBeginSubmissionDate(beginSubmissionDate);
        queryVo.setEndSubmissionDate(endSubmissionDate);
        queryVo.setStatus(status);
        queryVo.setSubmittedBy(submittedBy);
        if (formSystemId != null && !"".equals(formSystemId)) {
            queryVo.setFormSystemId(Integer.parseInt(formSystemId));
        }

        // For query by team scope
        if (request.getParameter("team_forms") != null) {
            String teamForms = ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_TEAMSCOPE_QUERY_FORMS);
            queryVo.setTipField(teamForms.split(";"));
            queryVo.setExpertAdviceFlag(true);
        }

        String supperOrgs = currentStaff.getUpperCompanys(); // CompanyHelper.getInstance().getUpperCompany(currentStaff.getOrgId());

        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            WorkFlowProcessDAO dao = new WorkFlowProcessDAO(dbManager);
            Collection list = dao.getAppliedFormListByStaff(currentStaff, queryVo);
            request.setAttribute("formList", list);
            FormManageDAO formDao = new FormManageDAO(dbManager);
            Collection formSelectList = formDao.getNewFormListByType(formType, supperOrgs);
            request.setAttribute("formSelectList", formSelectList);
        } catch (DAOException e) {
            resultLabel = "fail";
            e.printStackTrace();
        } finally {
            dbManager.freeConnection();
        }

        if ("1".equals(isExport)) {
            return mapping.findActionLocation("xlsForPersonallForm");
        } else {
            return mapping.findActionLocation(resultLabel);
        }
    }
    */
    
    public ActionLocation listPersonalApplyForm(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String resultLabel = "listPersonalForm";
        
        return mapping.findActionLocation(resultLabel);
    }
    
    public ActionLocation getPersonalApplyFormList(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
    	
    	response.setContentType("application/json;charset=GBK"); //it is very important
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");
    	
    	PrintWriter pw = response.getWriter(); 
    	IDBManager dbManager = null;
    	StaffVO currentStaff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
    	try{     
	        /* = (String)request.getParameter("dataSQL");
	        
	        StaffVO staff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
	        String[] paramName = new String[]{"@staffCode","@teamCode","@orgId"}; 
	        String[] paramValue = new String[]{staff.getStaffCode(),staff.getTeamCode(),staff.getOrgId()};
	        for(int i=0;i<paramName.length;i++){
	        	sql = StringUtil.replace(sql,paramName[i],paramValue[i]);
	        }
	        sql = CommonUtil.decoderURL(sql);*/
	        
	        dbManager = DBManagerFactory.getDBManager();
	        
	        Collection paramList = new ArrayList();
	        
	        paramList.add(currentStaff.getStaffCode());
	        
	        //paramList.add("C0378");
	        
            Collection list = dbManager.prepareCall2("poef_wkf_getPersonalList", paramList);
            
	        JSONObject json = new JSONObject();    
	            
	        JSONArray array = new JSONArray();    

	        array.addAll(list);
	        
	        pw.print(array.toString());    
	        
	    }catch(Exception e){  
	          e.printStackTrace();  
	    } finally {
	        if (pw != null)
	        	pw.close();
	        if (dbManager != null)
	            dbManager.freeConnection();
	    }  
    	return null;
    }
    
    public ActionLocation getPersonalApprovalFormList(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
    	
    	response.setContentType("application/json;charset=GBK"); //it is very important
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");
    	
    	PrintWriter pw = response.getWriter(); 
    	IDBManager dbManager = null;
    	StaffVO currentStaff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
    	try{     
	        
	        dbManager = DBManagerFactory.getDBManager();
	        
	        Collection paramList = new ArrayList();
	        
	        paramList.add(currentStaff.getStaffCode());
	        
	        //paramList.add("C0378");
	        
            Collection list = dbManager.prepareCall2("poef_wkf_getPersonalList2", paramList);
            
	        JSONObject json = new JSONObject();    
	            
	        JSONArray array = new JSONArray();    

	        array.addAll(list);
	        
	        pw.print(array.toString());    
	        
	    }catch(Exception e){  
	          e.printStackTrace();  
	    } finally {
	        if (pw != null)
	        	pw.close();
	        if (dbManager != null)
	            dbManager.freeConnection();
	    }  
    	return null;
    }
    
    /**
     * 获取当前用户所要处理的form列表
     * 
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionLocation listWaitForDealForm(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String resultLabel = "listForDealForm";
        String requestNo = (String) request.getParameter("requestNo");
        String formType = (String) request.getParameter("formType");
        String orgId = (String) request.getParameter("orgId");
        String teamCode = (String) request.getParameter("teamCode");
        String requestedBy = (String) request.getParameter("requestedBy");
        String beginSubmissionDate = (String) request.getParameter("beginSubmissionDate");
        String endSubmissionDate = (String) request.getParameter("endSubmissionDate");

        String processType = request.getParameter("processType");
        String checkOnly = request.getParameter("checkOnly");
        String isExport = request.getParameter("isExport");

        String comeFrom = (String) request.getParameter("comefrom");
        if (comeFrom != null && "left".equals(comeFrom)) {
            // 赋予一个初始化的时间段（一个星期）
            ParamConfigHelper paramHelper = ParamConfigHelper.getInstance();
            String defaultDays = paramHelper.getParamValue("list_data_days");
            int days = 0;
            if (defaultDays == null || "".equals(defaultDays)) {
                days = -7;
            } else {
                days = -Integer.parseInt(defaultDays);
            }

            SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyy/MM/dd");
            Date now = new Date();
            endSubmissionDate = bartDateFormat.format(now);
            beginSubmissionDate = StringUtil.afterNDay(days, "yyyy/MM/dd");
            request.setAttribute("beginSubmissionDate", beginSubmissionDate);
            request.setAttribute("endSubmissionDate", endSubmissionDate);
        }

        WorkFlowProcessVO vo = new WorkFlowProcessVO();
        if (requestNo != null && !"".equals(requestNo)) {
            vo.setRequestNo(requestNo);
        } else {
            vo.setRequestNo("");
        }
        if (formType != null && !"".equals(formType)) {
            vo.setFormType(formType);
        } else {
            vo.setFormType("");
        }
        if (orgId != null && !orgId.equals("")) {
            vo.setOrgId(orgId);
        }
        if (teamCode != null && !teamCode.equals("")) {
            vo.setTeamCode(teamCode);
        }
        if (requestedBy != null && !"".equals(requestedBy)) {
            vo.setRequestStaffCode(requestedBy);
        } else {
            vo.setRequestStaffCode("");
        }
        vo.setBeginSubmissionDate(beginSubmissionDate);
        vo.setEndSubmissionDate(endSubmissionDate);
        String formSystemId = (String) request.getParameter("formSystemId");
        if (formSystemId != null && !"".equals(formSystemId)) {
            vo.setFormSystemId(Integer.parseInt(formSystemId));
        }

        vo.setNodeType(processType);

        // For CHO ePayment
        if ("Y".equals(checkOnly)) {
            vo.setNodeName("CHECK_ONLY");
        }

        String multiReply = ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_EXPERT_MULTI_ADVISE, "true");
        if ("true".equalsIgnoreCase(multiReply) || "yes".equalsIgnoreCase(multiReply)) {
            vo.setExpertMultiReply(true);
        } else {
            vo.setExpertMultiReply(false);
        }

        // 获取当前用户信息
        StaffVO currentStaff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
        String supperOrgs = currentStaff.getUpperCompanys(); // CompanyHelper.getInstance().getUpperCompany(currentStaff.getOrgId());
        request.setAttribute("companyList", CompanyHelper.getInstance().getCompanyList());

        long timeStart = System.currentTimeMillis();
        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            WorkFlowProcessDAO dao = new WorkFlowProcessDAO(dbManager);
            Collection list = dao.getForDealFormListByStaff(currentStaff.getStaffCode(), vo);
            request.setAttribute("dealFormList", list);
            FormManageDAO formDao = new FormManageDAO(dbManager);
            Collection formSelectList = formDao.getNewFormListByType(formType, supperOrgs);
            request.setAttribute("formSelectList", formSelectList);

            TeamDAO tdao = new TeamDAO(dbManager);
            StaffDAO sdao = new StaffDAO(dbManager);

            if (orgId == null || orgId.equals("")) {
                orgId = currentStaff.getOrgId();
            }
            Collection teamList = tdao.getTeamListByCompany(orgId);
            request.setAttribute("teamList", teamList);

            Collection staffList = null;
            if (teamCode == null || teamCode.equals("")) {
                staffList = sdao.getStaffListByCompany(orgId);
            } else {
                staffList = sdao.getStaffListByTeam(teamCode);
            }
            request.setAttribute("staffList", staffList);
        } catch (DAOException e) {
            resultLabel = "fail";
            e.printStackTrace();
        } finally {
            if (dbManager != null) {
                dbManager.freeConnection();
            }
            if ("yes".equalsIgnoreCase(ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_LOG_TIME))) {
                System.out.println("Waiting List Time: " + (System.currentTimeMillis() - timeStart));
            }
        }

        if ("1".equals(isExport)) {
            return mapping.findActionLocation("xlsForDealForm");
        } else if ("2".equals(isExport)) {
            return mapping.findActionLocation("xlsForDealFormVender");
        } else {
            return mapping.findActionLocation(resultLabel);
        }
    }

    /**
     * 获取当前用户所处理过的form列表
     * 
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionLocation listMyDealedForm(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String resultLabel = "listMyDealedForm";
        IDBManager dbManager = null;
        String formType = (String) request.getParameter("formType");
        StaffVO currentStaff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
        String supperOrgs = currentStaff.getUpperCompanys(); // CompanyHelper.getInstance().getUpperCompany(currentStaff.getOrgId());

        try {
            dbManager = DBManagerFactory.getDBManager();
            FormManageDAO formDao = new FormManageDAO(dbManager);
            Collection formSelectList = formDao.getNewFormListByType(formType, supperOrgs);
            request.setAttribute("formSelectList", formSelectList);
            // if user enter the page by clicking the left menu tree,it needn't
            // to
            // query!
            String needQuery = (String) request.getParameter("needquery");
            if (needQuery != null && "false".equals(needQuery)) {
                Collection list = new ArrayList();
                request.setAttribute("dealedFormList", list);
                // 赋予一个初始化的时间段（一个星期）
                ParamConfigHelper paramHelper = ParamConfigHelper.getInstance();
                String defaultDays = paramHelper.getParamValue("list_data_days");
                int days = 0;
                if (defaultDays == null || "".equals(defaultDays)) {
                    days = -7;
                } else {
                    days = -Integer.parseInt(defaultDays);
                }
                String beginSubmissionDate = "";
                String endSubmissionDate = "";
                SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyy/MM/dd");
                Date now = new Date();
                endSubmissionDate = bartDateFormat.format(now);
                beginSubmissionDate = StringUtil.afterNDay(days, "yyyy/MM/dd");
                request.setAttribute("beginSubmissionDate", beginSubmissionDate);
                request.setAttribute("endSubmissionDate", endSubmissionDate);
            } else {
                String requestNo = (String) request.getParameter("requestNo");

                String beginSubmissionDate = (String) request.getParameter("beginSubmissionDate");
                String endSubmissionDate = (String) request.getParameter("endSubmissionDate");

                String beginHandleDate = (String) request.getParameter("beginHandleDate");
                String endHandleDate = (String) request.getParameter("endHandleDate");

                WorkFlowProcessVO vo = new WorkFlowProcessVO();
                if (requestNo != null && !"".equals(requestNo)) {
                    vo.setRequestNo(requestNo);
                } else {
                    vo.setRequestNo("");
                }
                if (formType != null && !"".equals(formType)) {
                    vo.setFormType(formType);
                } else {
                    vo.setFormType("");
                }
                vo.setBeginSubmissionDate(beginSubmissionDate);
                vo.setEndSubmissionDate(endSubmissionDate);

                vo.setBeginHandleDate(beginHandleDate);
                vo.setEndHandleDate(endHandleDate);

                String formSystemId = (String) request.getParameter("formSystemId");
                if (formSystemId != null && !"".equals(formSystemId)) {
                    vo.setFormSystemId(Integer.parseInt(formSystemId));
                }

                WorkFlowProcessDAO dao = new WorkFlowProcessDAO(dbManager);
                Collection list = dao.getMyDealedForm(currentStaff.getStaffCode(), vo);
                request.setAttribute("dealedFormList", list);
            }
        } catch (DAOException e) {
            resultLabel = "fail";
            e.printStackTrace();
        } finally {
            dbManager.freeConnection();
        }
        return mapping.findActionLocation(resultLabel);
    }

    /**
     * 获取已超时的所有form列表
     * 
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionLocation listOvertimeForm(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String resultLabel = "listOvertimeForm";
        String formType = (String) request.getParameter("formType");
        String orgId = (String) request.getParameter("companyId");
        String requestedBy = (String) request.getParameter("requestedBy");
        StaffVO currentStaff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
        String supperOrgs = currentStaff.getUpperCompanys();

        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            FormManageDAO formDao = new FormManageDAO(dbManager);
            Collection formSelectList = formDao.getNewFormListByType(formType, supperOrgs);
            request.setAttribute("formSelectList", formSelectList);
            // if user enter the page by clicking the left menu tree,it needn't
            // to query!
            String needQuery = (String) request.getParameter("needquery");
            if (needQuery != null && "false".equals(needQuery)) {
                Collection list = new ArrayList();
                request.setAttribute("overtimeFormList", list);
                // 赋予一个初始化的时间段（一个星期）
                ParamConfigHelper paramHelper = ParamConfigHelper.getInstance();
                String defaultDays = paramHelper.getParamValue("list_data_days");
                int days = 0;
                if (defaultDays == null || "".equals(defaultDays)) {
                    days = -7;
                } else {
                    days = -Integer.parseInt(defaultDays);
                }
                String beginSubmissionDate = "";
                String endSubmissionDate = "";
                SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyy/MM/dd");
                Date now = new Date();
                endSubmissionDate = bartDateFormat.format(now);
                beginSubmissionDate = StringUtil.afterNDay(days, "yyyy/MM/dd");
                request.setAttribute("beginSubmissionDate", beginSubmissionDate);
                request.setAttribute("endSubmissionDate", endSubmissionDate);
            } else {
                String requestNo = (String) request.getParameter("requestNo");

                String beginSubmissionDate = (String) request.getParameter("beginSubmissionDate");
                String endSubmissionDate = (String) request.getParameter("endSubmissionDate");
                WorkFlowProcessVO vo = new WorkFlowProcessVO();
                if (requestNo != null && !"".equals(requestNo)) {
                    vo.setRequestNo(requestNo);
                } else {
                    vo.setRequestNo("");
                }
                if (formType != null && !"".equals(formType)) {
                    vo.setFormType(formType);
                } else {
                    // 如果是查询所有form，则需要知道当前user可以查看到的form类型
                    String formTypes = "";
                    AuthorityHelper authority = AuthorityHelper.getInstance();
                    Collection typeList = FormTypeHelper.getInstance().getFormTypeList();
                    if (typeList != null && typeList.size() > 0) {
                        Iterator it = typeList.iterator();
                        while (it.hasNext()) {
                            FormTypeVO typeVo = (FormTypeVO) it.next();
                            if (authority.checkAuthorityByFormType(currentStaff.getCurrentRoleId(),
                                    ModuleOperateName.MODULE_OVERDUE_FORM, typeVo.getFormTypeId())) {
                                formTypes = formTypes + "'" + typeVo.getFormTypeId() + "',";
                            }
                        }
                    }
                    if (!"".equals(formTypes)) {
                        formTypes = formTypes.substring(0, formTypes.length() - 1);
                    }
                    vo.setFormType(formTypes);
                }
                vo.setOrgId(orgId);
                if (requestedBy != null && !"".equals(requestedBy)) {
                    vo.setRequestStaffCode(requestedBy);
                } else {
                    vo.setRequestStaffCode("");
                }
                vo.setBeginSubmissionDate(beginSubmissionDate);
                vo.setEndSubmissionDate(endSubmissionDate);
                String formSystemId = (String) request.getParameter("formSystemId");
                if (formSystemId != null && !"".equals(formSystemId)) {
                    vo.setFormSystemId(Integer.parseInt(formSystemId));
                }
                WorkFlowProcessDAO dao = new WorkFlowProcessDAO(dbManager);
                Collection list = dao.getAllOvertimeForm(vo);
                request.setAttribute("overtimeFormList", list);
            }

            if (orgId == null || orgId.equals("")) {
                orgId = currentStaff.getOrgId();
            }
            StaffDAO staffDao = new StaffDAO(dbManager);
            Collection staffList = staffDao.getStaffListByCompany(orgId);
            request.setAttribute("staffList", staffList);

        } catch (DAOException e) {
            resultLabel = "fail";
            e.printStackTrace();
        } finally {
            dbManager.freeConnection();
        }
        return mapping.findActionLocation(resultLabel);
    }

    public ActionLocation exportOvertimeForm(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String resultLabel = "exportOvertimeForm";
        String requestNo = (String) request.getParameter("requestNo");
        String formType = (String) request.getParameter("formType");
        String orgId = (String) request.getParameter("companyId");
        String requestedBy = (String) request.getParameter("requestedBy");
        String beginSubmissionDate = (String) request.getParameter("beginSubmissionDate");
        String endSubmissionDate = (String) request.getParameter("endSubmissionDate");
        StaffVO currentStaff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
        String supperOrgs = currentStaff.getUpperCompanys();

        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            WorkFlowProcessVO vo = new WorkFlowProcessVO();
            if (requestNo != null && !"".equals(requestNo)) {
                vo.setRequestNo(requestNo);
            } else {
                vo.setRequestNo("");
            }
            if (formType != null && !"".equals(formType)) {
                vo.setFormType(formType);
            } else {
                // 如果是查询所有form，则需要知道当前user可以查看到的form类型
                String formTypes = "";
                AuthorityHelper authority = AuthorityHelper.getInstance();
                Collection typeList = FormTypeHelper.getInstance().getFormTypeList();
                if (typeList != null && typeList.size() > 0) {
                    Iterator it = typeList.iterator();
                    while (it.hasNext()) {
                        FormTypeVO typeVo = (FormTypeVO) it.next();
                        if (authority.checkAuthorityByFormType(currentStaff.getCurrentRoleId(),
                                ModuleOperateName.MODULE_OVERDUE_FORM, typeVo.getFormTypeId())) {
                            formTypes = formTypes + "'" + typeVo.getFormTypeId() + "',";
                        }
                    }
                }
                if (!"".equals(formTypes)) {
                    formTypes = formTypes.substring(0, formTypes.length() - 1);
                }
                vo.setFormType(formTypes);
            }
            vo.setOrgId(orgId);
            if (requestedBy != null && !"".equals(requestedBy)) {
                vo.setRequestStaffCode(requestedBy);
            } else {
                vo.setRequestStaffCode("");
            }
            vo.setBeginSubmissionDate(beginSubmissionDate);
            vo.setEndSubmissionDate(endSubmissionDate);
            String formSystemId = (String) request.getParameter("formSystemId");
            if (formSystemId != null && !"".equals(formSystemId)) {
                vo.setFormSystemId(Integer.parseInt(formSystemId));
            }
            WorkFlowProcessDAO dao = new WorkFlowProcessDAO(dbManager);
            Collection list = dao.getAllOvertimeForm(vo);
            request.setAttribute("overtimeFormList", list);
        } catch (DAOException e) {
            resultLabel = "fail";
            e.printStackTrace();
        } finally {
            if (dbManager != null) {
                dbManager.freeConnection();
            }
        }
        return mapping.findActionLocation(resultLabel);
    }

    /**
     * 获取已超时的所有form列表
     * 
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionLocation listClosedForm(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String resultLabel = "listClosedForm";
        String formType = (String) request.getParameter("formType");
        StaffVO currentStaff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
        String supperOrgs = currentStaff.getUpperCompanys(); // CompanyHelper.getInstance().getUpperCompany(currentStaff.getOrgId());

        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            FormManageDAO formDao = new FormManageDAO(dbManager);
            Collection formSelectList = formDao.getNewFormListByType(formType, supperOrgs);
            request.setAttribute("formSelectList", formSelectList);
            // if user enter the page by clicking the left menu tree,it needn't
            // to
            // query!
            String needQuery = (String) request.getParameter("needquery");
            if (needQuery != null && "false".equals(needQuery)) {
                Collection list = new ArrayList();
                request.setAttribute("closedFormList", list);
                // 赋予一个初始化的时间段（一个星期）
                ParamConfigHelper paramHelper = ParamConfigHelper.getInstance();
                String defaultDays = paramHelper.getParamValue("list_data_days");
                int days = 0;
                if (defaultDays == null || "".equals(defaultDays)) {
                    days = -7;
                } else {
                    days = -Integer.parseInt(defaultDays);
                }
                String beginSubmissionDate = "";
                String endSubmissionDate = "";
                SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyy/MM/dd");
                Date now = new Date();
                endSubmissionDate = bartDateFormat.format(now);
                beginSubmissionDate = StringUtil.afterNDay(days, "yyyy/MM/dd");
                request.setAttribute("beginSubmissionDate", beginSubmissionDate);
                request.setAttribute("endSubmissionDate", endSubmissionDate);
            } else {
                String requestNo = (String) request.getParameter("requestNo");
                String beginSubmissionDate = (String) request.getParameter("beginSubmissionDate");
                String endSubmissionDate = (String) request.getParameter("endSubmissionDate");
                WorkFlowProcessVO vo = new WorkFlowProcessVO();
                if (requestNo != null && !"".equals(requestNo)) {
                    vo.setRequestNo(requestNo);
                } else {
                    vo.setRequestNo("");
                }
                if (formType != null && !"".equals(formType)) {
                    vo.setFormType(formType);
                } else {
                    vo.setFormType("");
                }
                vo.setBeginSubmissionDate(beginSubmissionDate);
                vo.setEndSubmissionDate(endSubmissionDate);
                String formSystemId = (String) request.getParameter("formSystemId");
                if (formSystemId != null && !"".equals(formSystemId)) {
                    vo.setFormSystemId(Integer.parseInt(formSystemId));
                }
                WorkFlowProcessDAO dao = new WorkFlowProcessDAO(dbManager);
                Collection list = dao.getClosedForm(vo);
                request.setAttribute("closedFormList", list);
            }
        } catch (DAOException e) {
            resultLabel = "fail";
            e.printStackTrace();
        } finally {
            dbManager.freeConnection();
        }
        return mapping.findActionLocation(resultLabel);
    }

    /**
     * 对处于“submitted(01)”状态下并且属于本人提交的form进行withdraw（撤销）操作
     * 
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionLocation withDrawForm(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String returnLabel = "tempsuccess";
        String requestNo = (String) request.getParameter("requestNo");
        WorkFlowProcessTraceVO traceVo = new WorkFlowProcessTraceVO();
        traceVo.setRequestNo(requestNo);
        traceVo.setHandleComments("Requester withdraw the form");
        // 获取当前用户信息
        StaffVO currentStaff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
        IDBManager dbManager = null;
        // PrintWriter out = response.getWriter();
        String handleType = "02";// withdraw

        try {
            dbManager = DBManagerFactory.getDBManager();
            WorkFlowProcessDAO dao = new WorkFlowProcessDAO(dbManager);
            // 首先检查该处理实例是否已经流走，即当前状态不是“01”或者“00”（submitted)；如果当前状态不是01，则不能Withdraw该form
            WorkFlowProcessVO process = dao.getProcessVO(requestNo);
            if ("00".equals(process.getStatus())) {
                request.setAttribute(CommonName.COMMON_ERROR_INFOR,
                        "The form had been withdrawn, you can't withdraw it again!");
                returnLabel = "fail";
                return mapping.findActionLocation(returnLabel);
            }
            if (!"01".equals(process.getStatus())) {
                request.setAttribute(CommonName.COMMON_ERROR_INFOR,
                        "The form had been approved, you can't withdraw it!");
                return mapping.findActionLocation(returnLabel);
            }

            WorkFlowDefineDAO defineDao = new WorkFlowDefineDAO(dbManager);
            // send email to staff who submitted form
            // to get the email template
            EmailTemplateDAO emailTemplateDao = new EmailTemplateDAO(dbManager);
            EmailTemplateVO template = emailTemplateDao.getEmailTemplateByAction(handleType, process.getFormType());
            // to get the staff emails who will need to handle

            String handleStaffList = process.getCurrentProcessor();
            String[] staffs = StringUtil.split(handleStaffList, ",");
            WorkFlowVO flow = defineDao.getWorkFlow(process.getFlowId());
            if (template != null) {
                if (staffs != null && staffs.length > 0) {
                    String handleComments = "Staff (" + currentStaff.getStaffName()
                            + ") have withdrawn his/her requested form (" + requestNo + ")";
                    sendEmail(template, currentStaff.getStaffCode(), requestNo, currentStaff.getStaffCode(),
                            handleComments, staffs, "" + flow.getFormSystemId());
                }
            }
            dao.withDrawForm(traceVo, currentStaff.getStaffCode());
            request.setAttribute("url", "wkfProcessAction.it?method=listPersonalApplyForm&comefrom=left");
            request.setAttribute("refreshType", "self");
            // 如果设置了后处理，则需要进入后处理
            if (flow != null && flow.getAfterHandleUrl() != null && !"".equals(flow.getAfterHandleUrl())) {
                request.setAttribute("functionLabel", flow.getAfterHandleUrl());
                return mapping.findActionLocation("afterProcessingHandle");
            } else {
                return mapping.findActionLocation("tempsuccess");
            }
            // out.print("success");
        } catch (DAOException e) {
            e.printStackTrace();
            request.setAttribute(CommonName.COMMON_ERROR_INFOR, I18NMessageHelper.getMessage("common.failwithawfrm"));
            returnLabel = "fail";
        } finally {
            if (dbManager != null)
                dbManager.freeConnection();
            // if (out != null)
            // out.close();
        }
        return mapping.findActionLocation(returnLabel);
    }

    /**
     * 对表单开始处理之前，需要首先锁定该表单（用来告诉其他可以处理的人，有人正在处理）
     * 
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionLocation lockForm(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String requestNo = (String) request.getParameter("requestNo");
        // 首先检查该表单是否已经处在“锁定“状态，如果是，则返回不能再进行锁定操作
        IDBManager dbManager = null;
        PrintWriter out = response.getWriter();
        StaffVO currentStaff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
        try {
            dbManager = DBManagerFactory.getDBManager();
            WorkFlowProcessDAO dao = new WorkFlowProcessDAO(dbManager);
            WorkFlowProcessVO process = dao.getProcessVO(requestNo);
            if ("0".equals(process.getInProcess())
                    && ("," + process.getCurrentInProcessor() + ",").indexOf("," + currentStaff.getStaffCode().trim()
                            + ",") >= 0) {
                // 如果没有被锁定，则可以进行锁定操作，需要制定加索人
                // 2013-12-13 错Node锁定的修正
                dao.lockForm(requestNo, "1", currentStaff.getStaffCode());
                out.print("success");
            } else {
                out.print("lock");
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

    /**
     * 解除对锁定表单的锁定
     * 
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionLocation unLockForm(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String requestNo = (String) request.getParameter("requestNo");
        IDBManager dbManager = null;
        PrintWriter out = response.getWriter();
        try {
            dbManager = DBManagerFactory.getDBManager();
            WorkFlowProcessDAO dao = new WorkFlowProcessDAO(dbManager);
            // 解锁时，需要将原锁定人置空
            dao.lockForm(requestNo, "0", "");
            out.print("success");
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

    /**
     * 调整指定工单的当前处理人
     * 
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionLocation adjustProcessorForm(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String requestNo = (String) request.getParameter("reassignRequestNo");
        String nodeId = (String) request.getParameter("reassignNodeId");
        String adjustTo = (String) request.getParameter("selectStaff");
        // System.out.println("adjustTo old: " + adjustTo);

        String formRequesterStaffCode = (String) request.getParameter("reassignRequestStaffCode");
        String formSystemId = (String) (String) request.getParameter("reassignFormSystemId");

        StaffVO currentStaff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
        AdjustProcessorLogVO log = new AdjustProcessorLogVO();
        log.setRequestNo(requestNo);
        log.setNodeId(nodeId);
        // log.setAdjustToProcessor(adjustTo);
        log.setOperateStaffCode(currentStaff.getStaffCode());
        IDBManager dbManager = null;
        PrintWriter out = response.getWriter();
        String handleType = "03"; // approve
        try {
            dbManager = DBManagerFactory.getDBManager();
            DelegateDAO delegateDAO = new DelegateDAO(dbManager);
            EmailTemplateDAO emailTemplateDao = new EmailTemplateDAO(dbManager);
            WorkFlowProcessDAO processDao = new WorkFlowProcessDAO(dbManager);

            String originProcessor = "";
            if ("Yes".equalsIgnoreCase(ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_SMART_DEPUTY))) {
                adjustTo = delegateDAO.getStaffsByDeputy(adjustTo, null);
                String[] tempStaffs = adjustTo.split(",");
                adjustTo = "";
                String p = "";
                for (int i = 0; i < tempStaffs.length; i++) {
                    // Maybe has deputy.
                    String[] toStaff = tempStaffs[i].split("/");
                    if (toStaff.length > 1) {
                        tempStaffs[i] = toStaff[1];
                        originProcessor = toStaff[0];
                    }
                    adjustTo += p + tempStaffs[i];
                    p = ",";
                }
                if (tempStaffs.length > 1) {
                    // Only for 1 staff.
                    originProcessor = "";
                }
            }
            // System.out.println("adjustTo new: " + adjustTo);
            String[] toEmailStaffs = adjustTo.split(",");
            log.setAdjustToProcessor(adjustTo);
            dbManager.startTransaction();
            processDao.adjustProcessorLog(log);
            processDao.adjustProcessor(requestNo, nodeId, adjustTo, originProcessor);
            dbManager.commit();
            WorkFlowProcessVO processVo = processDao.getProcessVO(requestNo);
            EmailTemplateVO template = emailTemplateDao.getEmailTemplateByAction(handleType, processVo.getFormType());
            if (template != null) {
                sendEmail(template, formRequesterStaffCode, requestNo, "", "", toEmailStaffs, formSystemId, processVo);
            }
            out.print("success");
        } catch (DAOException e) {
            dbManager.rollback();
            e.printStackTrace();
            out.print(e.getMessage());
        } finally {
            if (dbManager != null)
                dbManager.freeConnection();
            if (out != null)
                out.close();
        }
        return null;
    }

    /**
     * 2.IT0958 进入专家邀请界面
     * 
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionLocation enterInviteExpert(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String pageLabel = "inviteExpertPage";
        String requestNo = (String) request.getParameter("requestNo");
        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            WorkFlowProcessDAO processDao = new WorkFlowProcessDAO(dbManager);
            WorkFlowProcessVO currentProcessVo = processDao.getProcessVO(requestNo);
            if (currentProcessVo.getInvitedExpert() != null && !"".equals(currentProcessVo.getInvitedExpert())) {
                String[] selectedExpertList = StringUtil.split(currentProcessVo.getInvitedExpert(), ",");
                for (int i = 0; i < selectedExpertList.length; i++) {
                    // Maybe has deputy.
                    String[] staff = StringUtil.split(selectedExpertList[i], "/");
                    if (staff.length > 1) {
                        selectedExpertList[i] = staff[0];
                    }
                }
                request.setAttribute("selectedExpertList", selectedExpertList);
            } else {
                request.setAttribute("selectedExpertList", null);
            }
            if ("1".equals(request.getParameter("nonIE")))
                pageLabel = pageLabel + "PAD";
        } catch (DAOException e) {
            e.printStackTrace();
            request.setAttribute(CommonName.COMMON_ERROR_INFOR, e.getMessage());
            return mapping.findActionLocation("fail");
        } finally {
            if (dbManager != null)
                dbManager.freeConnection();
        }
        return mapping.findActionLocation(pageLabel);
    }

    /**
     * 2.IT0958 保存所选定的邀请专家,同时需要： 1.增加一条trace记录，记录该操作 2.给所选专家发送email
     * 
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionLocation saveSelectedExpert(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String requestNo = (String) request.getParameter("requestNo");
        String expertList = (String) request.getParameter("expertList"); // 多个之间以“,”间隔
        // 如果不为空，则需要去掉最后一个多余的“,”
        if (expertList != null && !"".equals(expertList)
                && ",".equals(expertList.substring(expertList.length() - 1, expertList.length()))) {
            expertList = expertList.substring(0, expertList.length() - 1);
        }
        String inviteReason = (String) request.getParameter("reason");

        inviteReason = HtmlUtil.decoderURL(inviteReason);

        if (inviteReason == null) {
            inviteReason = "";
        }
        StaffVO currentStaff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
        WorkFlowProcessTraceVO traceVo = new WorkFlowProcessTraceVO();
        PrintWriter out = response.getWriter();
        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            // 需要检查该form是否已经不在该节点了，例如已经被“withdraw”了等
            WorkFlowProcessDAO processDao = new WorkFlowProcessDAO(dbManager);
            DelegateDAO delegateDAO = new DelegateDAO(dbManager);
            // dbManager.startTransaction();
            WorkFlowProcessVO currentProcessVo = processDao.getProcessVO(requestNo);
            traceVo.setRequestNo(requestNo);
            traceVo.setHandleStaffCode(currentStaff.getStaffCode());
            traceVo.setHandleHours(processDao.getHandleHours(requestNo, null));
            traceVo.setDelayTime(0);
            traceVo.setDeputyFlag("0");
            traceVo.setOriginProcessor("");
            traceVo.setFlowId(currentProcessVo.getFlowId());
            traceVo.setCurrentNodeId(currentProcessVo.getNodeId());
            traceVo.setHandleType(CommonName.HANDLE_TYPE_INVITED_EXPERT);
            if (expertList == null || "".equals(expertList.trim())) {
                traceVo.setHandleComments("Current approver canceled the invited advisers");
            } else {

                SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyyMMddHHmmdd");
                Date date = new Date();
                String dateName = bartDateFormat.format(date);
                if (request.getMethod().equals("POST") && MultipartRequest.isMultipart(request)) {
                    request = new MultipartRequest(request, currentStaff.getLogonId());
                    File upFile = ((MultipartRequest) request).getFile("path");
                    String fileName = "";
                    if (upFile != null) {
                        fileName = dateName + "_" + ((MultipartRequest) request).getFileName(upFile);
                        fileName = fileName.replaceAll("'", "");
                        String filePathName = "upload/processform/" + requestNo + "/" + fileName;

                        FileUtil.saveAs(upFile, ParamConfigHelper.getInstance().getParamValue("upload_file_dir") + "/"
                                + filePathName);
                    }
                    fileName = DataConvertUtil.convertGBKToISO(fileName);

                    traceVo.setFilePathName(fileName);
                }
                // end of file upload

                if ("Yes"
                        .equalsIgnoreCase(ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_SMART_DEPUTY))) {
                    expertList = delegateDAO.getStaffsByDeputy(expertList, currentStaff.getStaffCode(),
                            currentProcessVo.getOrgId(), currentProcessVo.getFormSystemId());
                }

                String comments = "Current approver invited advisers (";
                String[] tempStaff = StringUtil.split(expertList, ",");
                String staffName = "";
                for (int i = 0; i < tempStaff.length; i++) {
                    // Maybe has deputy.
                    String[] expert = StringUtil.split(tempStaff[i], "/");
                    staffName = staffName + StaffTeamHelper.getInstance().getStaffNameByCode(expert[0]).trim() + ",";
                }
                if (staffName.length() > 0) {
                    staffName = staffName.substring(0, staffName.length() - 1);
                }
                comments = comments + staffName.trim() + ")";
                if (!"".equals(inviteReason)) {
                    comments = comments + ", invited reason: " + inviteReason;
                }
                traceVo.setHandleComments(comments);
            }

            try {
                dbManager.startTransaction();
                processDao.saveWorkFlowProcessTraceVO(traceVo);
                processDao.updateInvitedExperts(requestNo, expertList);
                dbManager.commit();
            } catch (Exception ex) {
                dbManager.rollback();
                ex.printStackTrace();
                out.print(I18NMessageHelper.getMessage("common.failinvad") + ex.getMessage());
                return null;
            }

            // send email to experts
            EmailTemplateDAO emailTemplateDao = new EmailTemplateDAO(dbManager);
            EmailTemplateVO template = emailTemplateDao.getEmailTemplateByAction(CommonName.HANDLE_TYPE_INVITED_EXPERT,
                    currentProcessVo.getFormType());
            if (template != null && expertList != null && !"".equals(expertList)) {
                String[] tempStaff = StringUtil.split(expertList, ",");
                for (int i = 0; i < tempStaff.length; i++) {
                    // Maybe has deputy.
                    String[] toStaff = StringUtil.split(tempStaff[i], "/");
                    if (toStaff.length > 1) {
                        tempStaff[i] = toStaff[1];
                    }
                }
                sendEmail(template, currentProcessVo.getRequestStaffCode(), requestNo, currentStaff.getStaffCode(),
                        inviteReason, tempStaff, "" + currentProcessVo.getFormSystemId());
            }
            // dbManager.commit();
            out.print("Success");
        } catch (DAOException e) {
            // dbManager.rollback();
            e.printStackTrace();
            out.print(I18NMessageHelper.getMessage("common.failinvad") + e.getMessage());
            if (request instanceof MultipartRequest)
                ((MultipartRequest) request).deleteTemporaryFile();
            return null;
        } catch (Exception e) {
            out.print(e.getMessage());
            if (request instanceof MultipartRequest)
                ((MultipartRequest) request).deleteTemporaryFile();
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
     * 3.IT0958 进入专家填写建议界面
     * 
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionLocation enterExpertAdvise(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String requestNo = (String) request.getParameter("requestNo");
        StaffVO currentStaff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
        // 首先检查当前处理人是否还在该form实例的邀请专家列表中，如果不在，则不允许填写建议
        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            WorkFlowProcessDAO processDao = new WorkFlowProcessDAO(dbManager);
            WorkFlowProcessVO currentProcessVo = processDao.getProcessVO(requestNo);
            if (currentProcessVo.getInvitedExpert() == null || "".equals(currentProcessVo.getInvitedExpert())
                    || currentProcessVo.getInvitedExpert().indexOf(currentStaff.getStaffCode()) < 0) {
                request.setAttribute(CommonName.COMMON_TIP_INFO, I18NMessageHelper.getMessage("common.notinvited"));
                return mapping.findActionLocation("fail");
            }
        } catch (DAOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (dbManager != null)
                dbManager.freeConnection();
        }
        if ("1".equals(request.getParameter("nonIE")))
            return mapping.findActionLocation("expertAdvicePagePAD");
        else
            return mapping.findActionLocation("expertAdvicePage");
    }

    /**
     * 3.IT0958 保存专家建议
     * 
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionLocation saveExpertAdvice(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String requestNo = (String) request.getParameter("requestNo");
        // String handleComments = (String) request.getParameter("handleComments");
        StaffVO currentStaff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);

        WorkFlowProcessTraceVO traceVo = new WorkFlowProcessTraceVO();
        traceVo.setRequestNo(requestNo);
        traceVo.setHandleType(CommonName.HANDLE_TYPE_EXPERT_ADVISE);

        StaffVO staff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
        request.getSession().setAttribute("url", "wkfProcessAction.it?method=listWaitForDealForm");
        String userId = staff.getLogonId();
        String filePathName = "";
        String fileName = "";
        String handleComments = "";
        try {
            SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyyMMddHHmmdd");
            Date date = new Date();
            String dateName = bartDateFormat.format(date);
            if (request.getMethod().equals("POST") && MultipartRequest.isMultipart(request)) {
                request = new MultipartRequest(request, userId);//
                // for (int i = 0; i < 2; i++) {
                File upFile = ((MultipartRequest) request).getFile("path");
                if (upFile != null) {
                    fileName = dateName + "_" + ((MultipartRequest) request).getFileName(upFile);
                    fileName = fileName.replaceAll("'", "");
                    filePathName = "upload/processform/" + requestNo + "/" + fileName;
                    // FileUtil.saveAs(upFile, Global.WEB_ROOT_PATH + "/"
                    // + filePathName);
                    FileUtil.saveAs(upFile, ParamConfigHelper.getInstance().getParamValue("upload_file_dir") + "/"
                            + filePathName);
                }
                fileName = DataConvertUtil.convertGBKToISO(fileName);
                // }
                // out.print("Upload successfully!");
                traceVo.setFilePathName(fileName);

                // Get comments
                handleComments = (String) request.getParameter("handleComments");
                handleComments = handleComments == null ? "" : handleComments;
            }
        } finally {
            if (request instanceof MultipartRequest) {
                // delete the temp file
                ((MultipartRequest) request).deleteTemporaryFile();
            }
        }

        IDBManager dbManager = null;
        if (traceVo.getFilePathName() == null) {
            traceVo.setFilePathName("");
        }
        try {
            dbManager = DBManagerFactory.getDBManager();
            WorkFlowProcessDAO processDao = new WorkFlowProcessDAO(dbManager);
            WorkFlowProcessVO currentProcessVo = processDao.getProcessVO(requestNo);
            // 首先检查当前处理人是否还在该form实例的邀请专家列表中，如果不在，则不允许填写建议
            if (currentProcessVo.getInvitedExpert() == null || "".equals(currentProcessVo.getInvitedExpert())
                    || currentProcessVo.getInvitedExpert().indexOf(currentStaff.getStaffCode()) < 0) {
                request.setAttribute(CommonName.COMMON_TIP_INFO, I18NMessageHelper.getMessage("common.notinvited"));
                return mapping.findActionLocation("fail");
            }

            String[] expertList = currentProcessVo.getInvitedExpert().split(",");
            for (int i = 0; i < expertList.length; i++) {
                if (expertList[i].indexOf("/" + currentStaff.getStaffCode()) != -1) {
                    String[] expert = expertList[i].split("/");
                    String expertName = StaffTeamHelper.getInstance().getStaffNameByCode(expert[0]);
                    handleComments = "(On behalf of " + expertName + ") " + handleComments;
                    break;
                }
            }

            traceVo.setCurrentNodeId(currentProcessVo.getNodeId());
            traceVo.setFormSystemId(currentProcessVo.getFormSystemId());
            traceVo.setHandleStaffCode(staff.getStaffCode());
            traceVo.setHandleHours(processDao.getHandleHours(requestNo, null));
            traceVo.setFlowId(currentProcessVo.getFlowId());
            traceVo.setDeputyFlag("0");
            traceVo.setOriginProcessor("");
            traceVo.setHandleComments("" + handleComments);
            processDao.saveWorkFlowProcessTraceVO(traceVo);

            // IT1002:send mail to the owner of this node
            String handleType = "07"; // give invited feedback
            EmailTemplateDAO emailTemplateDao = new EmailTemplateDAO(dbManager);
            EmailTemplateVO template = emailTemplateDao.getEmailTemplateByAction(handleType,
                    currentProcessVo.getFormType());

            String[] toStaff = currentProcessVo.getCurrentProcessor().split(",");

            try {
                sendEmail(template, currentProcessVo.getRequestStaffCode(), requestNo, staff.getStaffCode(),
                        handleComments, toStaff, "" + currentProcessVo.getFormSystemId());
            } catch (Exception e) {
                e.printStackTrace();
            }

            if ("1".equals(request.getParameter("nonIE"))) {
                PrintWriter out = response.getWriter();
                return mapping.findActionLocation("listForDealForm");
            }
            return mapping.findActionLocation("tempsuccess");
        } catch (DAOException e) {
            e.printStackTrace();
            request.setAttribute(CommonName.COMMON_ERROR_INFOR, e.getMessage());
            return mapping.findActionLocation("fail");
        } finally {
            if (dbManager != null)
                dbManager.freeConnection();
        }
    }

    /**
     * 进入环节审核操作页面
     * 
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionLocation enterAuditForm(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String pageLabel = "auditPage";
        IDBManager dbManager = null;
        String formSystemId = (String) request.getParameter("formSystemId");
        String currentNodeId = (String) request.getParameter("currentNodeId");
        String requestNo = (String) request.getParameter("requestNo");
        String handleType = (String) request.getParameter("handleType");
        try {
            dbManager = DBManagerFactory.getDBManager();
            // 需要检查该form是否已经不在该节点了，例如已经被“withdraw”了等
            WorkFlowProcessDAO processDao = new WorkFlowProcessDAO(dbManager);
            WorkFlowProcessVO currentProcessVo = processDao.getProcessVO(requestNo);
            if (currentProcessVo == null || !currentNodeId.equals(currentProcessVo.getNodeId())) {
                // request.setAttribute(CommonName.COMMON_TIP_INFO,
                // I18NMessageHelper.getMessage("common.formnotinnode"));
                request.setAttribute(CommonName.COMMON_TIP_INFO,
                        I18NMessageHelper.getMessage("common.formnotinnode", new String[] {requestNo}));
                request.setAttribute("returnType", "close");
                return mapping.findActionLocation("fail");
            }
            if ("03".equals(handleType)) { // approve,如果是 Approve操作，则需要检查下一个节点
                WorkFlowNodeDAO nodeDao = new WorkFlowNodeDAO(dbManager);
                StaffVO currentStaff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
                // 如果存在分支的情况，则需要检查是否有“该form没有找到符合条件的下一个处理节点”的标识
                WorkFlowItemVO nextNode = nodeDao.getNextNodeById(Integer.parseInt(formSystemId), currentNodeId,
                        requestNo, currentStaff.getStaffCode());
                if (nextNode.getItemId() == null || "".equals(nextNode.getItemId())) {// 没有找到
                    // 发送email给
                    //
                    request.setAttribute(CommonName.COMMON_TIP_INFO,
                            I18NMessageHelper.getMessage("common.notfindbranch"));
                    request.setAttribute("returnType", "close");
                    return mapping.findActionLocation("fail");
                }

                if (CommonName.NODE_TYPE_OPTIONAL.equals(nextNode.getItemType())) {
                    request.setAttribute("showSelectProcessor", "1"); // 让用户选择下一处理人，可省略
                } else if (CommonName.NODE_TYPE_SELECTAPPROVER.equals(nextNode.getItemType())) {
                    request.setAttribute("showSelectProcessor", "2"); // 让用户选择下一处理人，必选
                }
            } else if ("04".equals(handleType)) { // 如果是reject操作，则需要将之前所经过的节点列出。2007-11-02
                                                  // IT0958 DS-006
                Collection processedNodeList = processDao.getProcessedNodeList(requestNo, true);
                request.setAttribute("processedNodeList", processedNodeList);
                
        		BaseDataDAO baseDao = new BaseDataDAO(dbManager);
        		//获取公共的field集合
        		Collection list = baseDao.getMasterDataByType("00");
        		request.setAttribute("fieldList",list);
            }
            if ("1".equals(request.getParameter("nonIE")))
                pageLabel = pageLabel + "PAD";
        } catch (DAOException e) {
            e.printStackTrace();
            request.setAttribute(CommonName.COMMON_ERROR_INFOR, e.toString());
            return mapping.findActionLocation("fail");
        } finally {
            if (dbManager != null)
                dbManager.freeConnection();
        }
        return mapping.findActionLocation(pageLabel);
    }

    /**
     * 环节审核操作(包括各种类型的操作,handleType：03---approve,04---reject,05-----complete)
     * 
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    /**
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    /**
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    /**
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionLocation auditForm(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String requestNo = (String) request.getParameter("requestNo");
        String handleComments = (String) request.getParameter("handleComments");
        handleComments = StringUtil.decoderURL(handleComments);
        StaffVO currentStaff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
        String formSystemId = (String) request.getParameter("formSystemId");
        String currentNodeId = (String) request.getParameter("currentNodeId");
        String handleType = (String) request.getParameter("handleType");
        String formRequesterStaffCode = (String) request.getParameter("requestStaffCode");
        String handleStaffCode = currentStaff.getStaffCode();
        // IT0958 DS-006 Begin
        String rejectToNodeId = (String) request.getParameter("rejectToNode"); // 获取拒绝到的节点（如果没有选择，则默认回到申请人）
        // IT0958 DS-006 End
        // String contextPath = request.getParameter("contextPath");

        String nextApproverStaffCode = (String) request.getParameter(CommonName.NEXT_APPROVER_STAFF_CODE);
        // if(nextApproverStaffCode==null){
        // nextApproverStaffCode = "";
        // }

        String formReturn = request.getParameter("formReturn");
        formReturn = formReturn == null ? "" : formReturn;

        WorkFlowProcessTraceVO traceVo = new WorkFlowProcessTraceVO();
        traceVo.setRequestNo(requestNo);
        traceVo.setHandleType(handleType);
        traceVo.setCurrentNodeId(currentNodeId);
        traceVo.setFormSystemId(Integer.parseInt(formSystemId));
        traceVo.setHandleStaffCode(handleStaffCode);
        traceVo.setHandleComments(handleComments);
        traceVo.setRejectToNodeId(rejectToNodeId);
        traceVo.setNextApprover(nextApproverStaffCode);

        StaffVO staff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
        // request.getSession().setAttribute(CommonName.RETURN_URL,"/wkfProcessAction.it?method=listWaitForDealForm");
        request.getSession().setAttribute("url", "/welcome.jsp");
        String userId = staff.getLogonId();
        String filePathName = "";
        String fileName = "";
        MultipartRequest multiRequest = null;
        try {
            SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyyMMddHHmmdd");
            Date date = new Date();
            String dateName = bartDateFormat.format(date);
            if (request.getMethod().equals("POST") && MultipartRequest.isMultipart(request)) {
                multiRequest = new MultipartRequest(request, userId);//
                // for (int i = 0; i < 2; i++) {
                File upFile = ((MultipartRequest) multiRequest).getFile("path");
                if (upFile != null) {
                    fileName = dateName + "_" + ((MultipartRequest) multiRequest).getFileName(upFile);
                    fileName = fileName.replaceAll("'", "");
                    filePathName = "upload/processform/" + requestNo + "/" + fileName;
                    // FileUtil.saveAs(upFile, Global.WEB_ROOT_PATH + "/"
                    // + filePathName);
                    FileUtil.saveAs(upFile, ParamConfigHelper.getInstance().getParamValue("upload_file_dir") + "/"
                            + filePathName);
                }
                fileName = DataConvertUtil.convertGBKToISO(fileName);
                // }
                // out.print("Upload successfully!");
                traceVo.setFilePathName(fileName);
            }
        } finally {
            if (multiRequest != null && multiRequest instanceof MultipartRequest) {
                // delete the temp file
                ((MultipartRequest) multiRequest).deleteTemporaryFile();
            }
        }

        IDBManager dbManager = null;
        WorkFlowVO flow = null;
        // PrintWriter out = response.getWriter();
        try {
            dbManager = DBManagerFactory.getDBManager();
            WorkFlowProcessDAO processDao = new WorkFlowProcessDAO(dbManager);
            WorkFlowDefineDAO flowDao = new WorkFlowDefineDAO(dbManager);
            WorkFlowProcessVO processVo = processDao.getProcessVO(requestNo);
            // 2011-8-15 需要检查该form是否已经不在该节点了，例如已经被“withdraw”了等
            if (processVo == null || !currentNodeId.equals(processVo.getNodeId())) {
                request.setAttribute(CommonName.COMMON_TIP_INFO,
                        I18NMessageHelper.getMessage("common.formnotinnode", new String[] {requestNo}));
                request.setAttribute("returnType", "close");
                return mapping.findActionLocation("fail");
            }
            String nodeType = processVo.getNodeType();
            int remainApprovers = processVo.getRemaineApproversNum();
            boolean isNeedEmail = true;
            flow = flowDao.getWorkFlowByForm(Integer.parseInt(formSystemId));
            String nextNodeId = null;
            // long startTime = System.currentTimeMillis();
            try {
                if (!"04".equals(handleType)) { // not reject or return form, to check the status
                    if (processDao.isRequestCompleted(requestNo)) {
                        System.out.println("WorkFlowProcessAction.auditForm: request " + requestNo + " is completed ");
                        return mapping.findActionLocation("tempsuccess");
                    }
                }

                dbManager.startTransaction();
                // System.out.println("===== " + requestNo +
                // " ===== auditForm - dbManager.startTransaction");
                // nextNodeId = processDao.nodeProcess(traceVo, formRequesterStaffCode);
                nextNodeId = processDao.nodeProcess(traceVo, formRequesterStaffCode, nextApproverStaffCode, false);
                processVo = processDao.getProcessVO(requestNo);
                if (!(CommonName.NODE_TYPE_DELAYED.equals(processVo.getNodeType())
                        && "1".equals(processVo.getInProcess()) && "SYS".equals(processVo.getInProcessStaffCode()))) {
                    processDao.lockForm(requestNo, "0", "");
                }
                if (!(CommonName.NODE_TYPE_MULTIAPPROVER.equals(nodeType) && remainApprovers > 1)) {// 2013-12-20
                                                                                                    // Multi-approver
                    processDao.updateInvitedExperts(requestNo, "");
                }
                dbManager.commit();
                // System.out.println("Commit Time: " + (System.currentTimeMillis() - startTime));
                processDao.processCallback(traceVo);
            } catch (Exception ex) {
                dbManager.rollback();
                System.out.println("dbManager.rollback: " + requestNo);
                ex.printStackTrace();
                request.setAttribute(CommonName.COMMON_ERROR_INFOR, ex.getMessage());
                return mapping.findActionLocation("fail");
            }
            // System.out.println("nextNodeId: " + nextNodeId);
            StaffVO requester = StaffTeamHelper.getInstance().getStaffByCode(processVo.getRequestStaffCode());
            String orgName = CompanyHelper.getInstance().getOrgName(requester.getOrgId());
            if (CommonName.HANDLE_TYPE_APPROVE.equals(handleType)) { // approve
                // 如果存在分支的情况，则需要检查是否有“该form没有找到符合条件的下一个处理节点”的标识
                if ("-1".equals(processVo.getHasNextNode())) {// 没有找到
                    // 发送email给administrator
                    String adminEmailAddress = ParamConfigHelper.getInstance().getParamValue(
                            CommonName.PARAM_ADMIN_EMAIL);
                    StaffVO Submitter = StaffTeamHelper.getInstance().getStaffByCode(processVo.getSubmittedBy());
                    if (adminEmailAddress != null && !"".equals(adminEmailAddress)) {
                        adminEmailAddress = adminEmailAddress + "," + Submitter.getEmail();
                    } else {
                        adminEmailAddress = Submitter.getEmail();
                    }
                    String subject = "Error of E-Flow";
                    String mailContent = "<font face='Arial' size='2'>Request No.(<font color='red'><b>" + requestNo
                            + "</b></font>) can't find the suitable branch, current node is <b>"
                            + processVo.getNodeName() + "</b> (" + processVo.getNodeId() + ").<br><br>"
                            + "Requester: <b>" + requester.getStaffName() + "</b> (<font color='red'><b>" + orgName
                            + "</b></font>)." + "</font>";
                    EFlowEmailUtil.sendEmail(subject, mailContent, adminEmailAddress, processVo);
                    //
                    request.setAttribute(CommonName.COMMON_TIP_INFO,
                            I18NMessageHelper.getMessage("common.notfindbranch"));
                    request.setAttribute("returnType", "close");
                    return mapping.findActionLocation("fail");
                }
            }

            // send email to staff who submitted form
            // to get the email template
            EmailTemplateDAO emailTemplateDao = new EmailTemplateDAO(dbManager);
            EmailTemplateVO template = emailTemplateDao.getEmailTemplateByAction(handleType, processVo.getFormType());

            // For payment
            EmailTemplateVO template2 = null;
            if (CommonName.NODE_TYPE_PAYMENT.equals(nodeType)
                    && (CommonName.HANDLE_TYPE_APPROVE.equals(handleType) || CommonName.HANDLE_TYPE_COMPLETE
                            .equals(handleType))) {
                template2 = emailTemplateDao.getEmailTemplateByAction(CommonName.HANDLE_TYPE_PAYMENT,
                        processVo.getFormType());
            }
            if (template2 != null) {
                String[] toStaff = null;
                if (processVo.getSubmittedBy().trim().equals(processVo.getRequestStaffCode().trim())) {
                    toStaff = new String[] {processVo.getRequestStaffCode()};
                } else {
                    toStaff = new String[] {processVo.getRequestStaffCode(), processVo.getSubmittedBy().trim()};
                }
                sendEmail(template2, processVo.getRequestStaffCode(), requestNo, handleStaffCode,
                        traceVo.getHandleComments(), toStaff, Integer.toString(processVo.getFormSystemId()), processVo);
            }

            // For waiting node
            EmailTemplateVO template3 = null;
            if (CommonName.NODE_TYPE_WAITING.equals(processVo.getNodeType())) {
                template3 = emailTemplateDao.getEmailTemplateByAction(CommonName.HANDLE_TYPE_WAITING_REMINDER,
                        processVo.getFormType());
            }
            if (template3 != null) {
                String[] toStaff = null;
                if (processVo.getSubmittedBy().trim().equals(processVo.getRequestStaffCode().trim())) {
                    toStaff = new String[] {processVo.getRequestStaffCode()};
                } else {
                    toStaff = new String[] {processVo.getRequestStaffCode(), processVo.getSubmittedBy().trim()};
                }

                double _limitedHours = processVo.getLimitedHours();
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
                sendEmail(template3, processVo.getRequestStaffCode(), requestNo, handleStaffCode, "" + _limitedHours,
                        toStaff, Integer.toString(processVo.getFormSystemId()), processVo);
            }

            // String emailTo =
            // StaffTeamHelper.getInstance().getStaffByCode(formRequesterStaffCode).getEmail();
            if (template != null) {
                if ("04".equals(handleType)) {// reject
                    // 如果没有选择rejectTo的节点，则返回到开是节点。同时 send email to 'submitted
                    // by'与'requester'
                    if (rejectToNodeId == null || "".equals(rejectToNodeId) || "0".equals(rejectToNodeId)) {
                        // String[] toStaff = {formRequesterStaffCode};
                        String[] toStaff = null;
                        if (formRequesterStaffCode.equals(processVo.getSubmittedBy())) {
                            toStaff = new String[] {processVo.getSubmittedBy()};
                        } else {
                            toStaff = new String[] {processVo.getSubmittedBy(), formRequesterStaffCode};
                        }
                        sendEmail(template, formRequesterStaffCode, requestNo, handleStaffCode, handleComments,
                                toStaff, formSystemId, processVo);
                    } else {
                        // 发送email给需要处理的人
                        String[] toStaff = processDao.getRejectToStaff(traceVo);
                        if (toStaff != null) {
                            // 采用approve模板发送
                            // handleType = "03";
                            template = emailTemplateDao.getEmailTemplateByAction("03", processVo.getFormType());
                            sendEmail(template, formRequesterStaffCode, requestNo, handleStaffCode, handleComments,
                                    toStaff, formSystemId, processVo);
                        }
                    }

                } else if (("05".equals(handleType) || "-1".equals(nextNodeId)) && template2 == null) {// complete
                    // 如果submit的人与requester不是同一个人，则需要发送email给两个人
                    // send email to requester
                    String[] toStaff = null;
                    if (processVo.getSubmittedBy().trim().equals(formRequesterStaffCode.trim())) {
                        toStaff = new String[] {formRequesterStaffCode};
                    } else {
                        toStaff = new String[] {formRequesterStaffCode, processVo.getSubmittedBy().trim()};
                    }
                    template = emailTemplateDao.getEmailTemplateByAction("05", processVo.getFormType());
                    sendEmail(template, formRequesterStaffCode, requestNo, handleStaffCode, handleComments, toStaff,
                            formSystemId, processVo);

                    // IT1315 start Email#2 to all approvers
                    String emailToAll = ParamConfigHelper.getInstance().getParamValue("email_to_all");
                    String dedup = ",";
                    if (!"".equals(emailToAll)) {
                        toStaff = new String[] {formRequesterStaffCode};
                        if ("all".equalsIgnoreCase(emailToAll)
                                || ("," + emailToAll + ",").indexOf("," + formSystemId + ",") > -1) {
                            ArrayList<WorkFlowProcessTraceVO> prcocessTrace = (ArrayList) processDao
                                    .getProcessTraceList(requestNo, true);
                            for (WorkFlowProcessTraceVO tmpVo : prcocessTrace) {
                                if (tmpVo.getHandleStaffCode().equals(processVo.getSubmittedBy())
                                        || tmpVo.getHandleStaffCode().equals(formRequesterStaffCode)
                                        || dedup.indexOf("," + tmpVo.getHandleStaffCode() + ",") > -1) {
                                    continue;
                                } else {
                                    dedup = dedup + tmpVo.getHandleStaffCode() + ",";
                                }
                            }
                        }
                        dedup = dedup.substring(1);
                        if (!"".equals(dedup)) {
                            template = emailTemplateDao.getEmailTemplateByAction(CommonName.HANDLE_TYPE_COMPLETE_CC,
                                    processVo.getFormType());
                            sendEmail(template, formRequesterStaffCode, requestNo, handleStaffCode, handleComments,
                                    dedup.split(","), formSystemId, processVo);
                        }
                    }
                    // IT1315 end
                } else if ("03".equals(handleType)) {// approve,send email to
                    // next processor
                    String[] toStaff = {};
                    String[] approverStaffList = {};
                    // 获取下一个环节的处理人
                    WorkFlowNodeDAO nodeDao = new WorkFlowNodeDAO(dbManager);
                    // WorkFlowItemVO nextNode = nodeDao.getNextNodeById(Integer
                    // .parseInt(formSystemId), currentNodeId,requestNo);
                    WorkFlowItemVO nextNode = nodeDao.getNodeById(processVo.getFlowId(), nextNodeId);

                    if (CommonName.NODE_TYPE_DELAYED.equals(processVo.getNodeType())
                            && "1".equals(processVo.getInProcess()) && "SYS".equals(processVo.getInProcessStaffCode())) {
                        // For delayed node
                        isNeedEmail = false;
                    } else if (CommonName.NODE_TYPE_MULTIAPPROVER.equals(nodeType) && remainApprovers > 1) {
                        // IT1346 Do nothing because no need to send email yet
                        isNeedEmail = false;
                    } else if (nextApproverStaffCode != null && !"".equals(nextApproverStaffCode)) {
                        toStaff = new String[1];
                        toStaff[0] = nextApproverStaffCode;
                    } else {
                        String approverStaff = processVo.getCurrentProcessor(); // IT1321
                        if (approverStaff != null && !"".equals(approverStaff)) {
                            approverStaffList = StringUtil.split(approverStaff, ",");
                        }
                        String dedup = ",";
                        if (approverStaffList != null && approverStaffList.length > 0) {
                            for (int i = 0; i < approverStaffList.length; i++) {
                                if (dedup.indexOf("," + approverStaffList[i] + ",") != -1) {
                                    continue;
                                } else {
                                    dedup = dedup + approverStaffList[i] + ",";
                                }
                            }
                        }
                        toStaff = dedup.substring(1).split(",");
                    }
                    if (isNeedEmail && toStaff != null && toStaff.length > 0) {
                        sendEmail(template, formRequesterStaffCode, requestNo, handleStaffCode, handleComments,
                                toStaff, formSystemId, processVo);
                    } else if (nextNodeId != null && !"-1".equals(nextNodeId) && !"0".equals(nextNodeId) && isNeedEmail) {
                        // 如果没有找到下一个处理的人(并且下一个节点不是最后一个节点)，则需要发email给administrator
                        // 发送email给administrator与form申请者
                        String adminEmailAddress = ParamConfigHelper.getInstance().getParamValue(
                                CommonName.PARAM_ADMIN_EMAIL);
                        StaffVO Submitter = StaffTeamHelper.getInstance().getStaffByCode(processVo.getSubmittedBy());
                        if (adminEmailAddress != null && !"".equals(adminEmailAddress)) {
                            adminEmailAddress = adminEmailAddress + "," + Submitter.getEmail();
                        } else {
                            adminEmailAddress = Submitter.getEmail();
                        }
                        String subject = "Error of E-Flow";
                        String mailContent = "<font face='Arial' size='2'>Request No.(<font color='red'><b>"
                                + requestNo + "</b></font>) can't find the approver in node <b>" + nextNode.getName()
                                + "</b> (" + nextNode.getItemId() + ").<br><br>" + "Requester: <b>"
                                + requester.getStaffName() + "</b> (<font color='red'><b>" + orgName + "</b></font>)."
                                + "</font>";
                        EFlowEmailUtil.sendEmail(subject, mailContent, adminEmailAddress, processVo);
                    }
                }
            }
            // dbManager.commit();
            // System.out.println("End Time: " + (System.currentTimeMillis() - startTime));
            // System.out.println("===== " + requestNo +
            // " ===== auditForm - dbManager.commit");
            /*
             * if ("04".equals(handleType) && !"".equals(formReturn)) { request.setAttribute("url",
             * "/wkfProcessAction.it?method=listInquiryForm"); } else {
             */
            request.setAttribute("url", "/welcome.jsp");
            /* } */
            // System.out.println("url: " + request.getAttribute("url"));
            // request.setAttribute(CommonName.COMMON_OK_INFOR, "You processed form (" + requestNo + ") successfully");
            request.setAttribute(CommonName.COMMON_OK_INFOR,
                    I18NMessageHelper.getMessage("common.frmprocessed", new String[] {requestNo}));
           
            // 如果该流程设置了“后处理”，则需要进入后处理
            WorkFlowNodeDAO nodeDao = new WorkFlowNodeDAO(dbManager);
            WorkFlowItemVO node = nodeDao.getNodeById(processVo.getFlowId(), currentNodeId);
            request.setAttribute("refreshType", "parent1");
            if (("03".equals(handleType) || "05".equals(handleType)) && !"".equals(node.getApproveHandle())) {
                request.setAttribute("functionLabel", node.getApproveHandle());
                return mapping.findActionLocation("afterProcessingHandle");
            } 
            //20150303 begin: enable the reject handler
//            else if ("04".equals(handleType)){
//            	request.setAttribute("refreshType", "parent");
//            	if ("1".equals(request.getParameter("nonIE"))) {
//                    return mapping.findActionLocation("listForDealForm");
//                }
//            	return mapping.findActionLocation("tempsuccess");
//            }
//            else if ("04".equals(handleType) && !"".equals(node.getRejectHandle())) {
//                request.setAttribute("functionLabel", node.getRejectHandle());
//                return mapping.findActionLocation("afterProcessingHandle");
//            } 
            else if ("04".equals(handleType) && !"".equals(node.getRejectHandle())) {
                request.setAttribute("functionLabel", node.getRejectHandle());
                request.setAttribute("refreshType", "parent");
                return mapping.findActionLocation("afterProcessingHandle");
            } 
            else if ("04".equals(handleType)){
            	request.setAttribute("refreshType", "parent");
            	if ("1".equals(request.getParameter("nonIE"))) {
                    return mapping.findActionLocation("listForDealForm");
                }
            	return mapping.findActionLocation("tempsuccess");
            }
            //20150303 end
            else if (!"04".equals(handleType) && flow != null && flow.getAfterHandleUrl() != null
                    && !"".equals(flow.getAfterHandleUrl())) {
                request.setAttribute("functionLabel", flow.getAfterHandleUrl());
                return mapping.findActionLocation("afterProcessingHandle");
            } else {
                if ("1".equals(request.getParameter("nonIE"))) {
                    return mapping.findActionLocation("listForDealForm");
                }
                return mapping.findActionLocation("tempsuccess");
            }
        } catch (Exception e) {
            // dbManager.rollback();
            e.printStackTrace();
            request.setAttribute(CommonName.COMMON_ERROR_INFOR, e.getMessage());
            return mapping.findActionLocation("fail");
        } finally {
            // System.out.println("dbManager.freeConnection");
            if (dbManager != null) {
                dbManager.freeConnection();
            }
        }
    }

    // For batch process
    public boolean auditForm2(WorkFlowProcessTraceVO traceVo) throws Exception {
        String requestNo = traceVo.getRequestNo();
        String handleStaffCode = traceVo.getHandleStaffCode();

        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            WorkFlowProcessDAO processDao = new WorkFlowProcessDAO(dbManager);
            WorkFlowProcessVO processVo = processDao.getProcessVO(requestNo);
            traceVo.setCurrentNodeId(processVo.getNodeId());
            traceVo.setFormSystemId(processVo.getFormSystemId());
            String formSystemId = Integer.toString(processVo.getFormSystemId());

            String nodeType = processVo.getNodeType();
            // System.out.println("nodeType: " + nodeType);

            // Get Handle Type
            WorkFlowNodeDAO nodeDao = new WorkFlowNodeDAO(dbManager);
            WorkFlowItemVO nextNode = nodeDao.getNextNodeById(processVo.getFormSystemId(), processVo.getNodeId(),
                    requestNo, handleStaffCode);
            String handleType = traceVo.getHandleType();
            if (handleType.equals(CommonName.HANDLE_TYPE_APPROVE) && "-1".equals(nextNode.getItemId())) {
                handleType = CommonName.HANDLE_TYPE_COMPLETE;
                traceVo.setHandleType(handleType);
            }

            String nextNodeId = null;
            try {
                if (processDao.isRequestCompleted(requestNo)) {
                    System.out.println("WorkFlowProcessAction.auditForm2: request " + requestNo + " is completed ");
                    return true;
                }
                dbManager.startTransaction();
                nextNodeId = processDao.nodeProcess(traceVo, processVo.getRequestStaffCode());
                processDao.lockForm(requestNo, "0", "");
                processDao.updateInvitedExperts(requestNo, "");
                dbManager.commit();
                processDao.processCallback(traceVo);
            } catch (Exception ex) {
                dbManager.rollback();
                System.out.println("dbManager.rollback: " + requestNo);
                ex.printStackTrace();
                return false;
            }

            processVo = processDao.getProcessVO(requestNo);
            StaffVO requester = StaffTeamHelper.getInstance().getStaffByCode(processVo.getRequestStaffCode());
            String orgName = CompanyHelper.getInstance().getOrgName(requester.getOrgId());

            if (CommonName.HANDLE_TYPE_APPROVE.equals(handleType)) {
                if ("-1".equals(processVo.getHasNextNode())) {
                    // 没有找到符合条件的下一个处理节点, 发送email给administrator
                    String adminEmailAddress = ParamConfigHelper.getInstance().getParamValue(
                            CommonName.PARAM_ADMIN_EMAIL);
                    StaffVO Submitter = StaffTeamHelper.getInstance().getStaffByCode(processVo.getSubmittedBy());
                    if (adminEmailAddress != null && !"".equals(adminEmailAddress)) {
                        adminEmailAddress = adminEmailAddress + "," + Submitter.getEmail();
                    } else {
                        adminEmailAddress = Submitter.getEmail();
                    }
                    String subject = "Error of E-Flow";
                    String mailContent = "<font face='Arial' size='2'>Request No.(<font color='red'><b>" + requestNo
                            + "</b></font>) can't find the suitable branch, current node is <b>"
                            + processVo.getNodeName() + "</b> (" + processVo.getNodeId() + ").<br><br>"
                            + "Requester: <b>" + requester.getStaffName() + "</b> (<font color='red'><b>" + orgName
                            + "</b></font>)." + "</font>";
                    EFlowEmailUtil.sendEmail(subject, mailContent, adminEmailAddress, processVo);
                    return false;
                }
            }

            // Send email
            EmailTemplateDAO emailTemplateDao = new EmailTemplateDAO(dbManager);
            EmailTemplateVO template = emailTemplateDao.getEmailTemplateByAction(handleType, processVo.getFormType());

            // For payment
            EmailTemplateVO template2 = null;
            if (CommonName.NODE_TYPE_PAYMENT.equals(nodeType)) {
                template2 = emailTemplateDao.getEmailTemplateByAction(CommonName.HANDLE_TYPE_PAYMENT,
                        processVo.getFormType());
            }
            if (template2 != null) {
                String[] toStaff = null;
                if (processVo.getSubmittedBy().trim().equals(processVo.getRequestStaffCode().trim())) {
                    toStaff = new String[] {processVo.getRequestStaffCode()};
                } else {
                    toStaff = new String[] {processVo.getRequestStaffCode(), processVo.getSubmittedBy().trim()};
                }
                sendEmail(template2, processVo.getRequestStaffCode(), requestNo, handleStaffCode,
                        traceVo.getHandleComments(), toStaff, formSystemId, processVo);
            }

            if (template != null) {
                if (CommonName.HANDLE_TYPE_REJECT.equals(handleType)) {
                    String rejectToNodeId = traceVo.getRejectToNodeId();
                    if (rejectToNodeId == null || "".equals(rejectToNodeId) || "0".equals(rejectToNodeId)) {
                        String[] toStaff = null;
                        if (processVo.getRequestStaffCode().equals(processVo.getSubmittedBy())) {
                            toStaff = new String[] {processVo.getRequestStaffCode()};
                        } else {
                            toStaff = new String[] {processVo.getRequestStaffCode(), processVo.getSubmittedBy()};
                        }
                        sendEmail(template, processVo.getRequestStaffCode(), requestNo, handleStaffCode,
                                traceVo.getHandleComments(), toStaff, formSystemId, processVo);
                    } else {
                        // 发送email给需要处理的人
                        String[] toStaff = processDao.getRejectToStaff(traceVo);
                        if (toStaff != null) {
                            // 采用Approve模板发送
                            template = emailTemplateDao.getEmailTemplateByAction(CommonName.HANDLE_TYPE_APPROVE,
                                    processVo.getFormType());
                            sendEmail(template, processVo.getRequestStaffCode(), requestNo, handleStaffCode,
                                    traceVo.getHandleComments(), toStaff, formSystemId, processVo);
                        }
                    }
                } else if (CommonName.HANDLE_TYPE_COMPLETE.equals(handleType)) {
                    if (template2 == null) {
                        // 如果submit的人与requester不是同一个人，则需要发送email给两个人
                        String[] toStaff = null;
                        if (processVo.getRequestStaffCode().equals(processVo.getSubmittedBy())) {
                            toStaff = new String[] {processVo.getRequestStaffCode()};
                        } else {
                            toStaff = new String[] {processVo.getRequestStaffCode(), processVo.getSubmittedBy()};
                        }
                        sendEmail(template, processVo.getRequestStaffCode(), requestNo, handleStaffCode,
                                traceVo.getHandleComments(), toStaff, formSystemId, processVo);
                    }
                } else if (CommonName.HANDLE_TYPE_APPROVE.equals(handleType)) {
                    // Next processor
                    String[] toStaff = {};
                    String[] approverGroupStaffList = {};
                    String[] approverStaffList = {};

                    // 获取下一个环节的处理人
                    nextNode = nodeDao.getNodeById(processVo.getFlowId(), nextNodeId);
                    if (traceVo.getNextApprover() != null && !"".equals(traceVo.getNextApprover())) {
                        toStaff = new String[1];
                        toStaff[0] = traceVo.getNextApprover();
                    } else {
                        String approverGroup = nextNode.getApproveGroupId();
                        if (approverGroup != null && !"".equals(approverGroup)) {
                            approverGroupStaffList = processDao.getNextProcessStaff(traceVo,
                                    processVo.getRequestStaffCode(), approverGroup);
                        }
                        String approverStaff = nextNode.getApprovestaffCode();
                        if (approverStaff != null && !"".equals(approverStaff)) {
                            approverStaffList = StringUtil.split(approverStaff, ",");
                        }
                        toStaff = new String[approverGroupStaffList.length + approverStaffList.length];
                        int begin = 0;
                        if (approverGroupStaffList != null && approverGroupStaffList.length > 0) {
                            for (int i = 0; i < approverGroupStaffList.length; i++) {
                                toStaff[i] = approverGroupStaffList[i];
                            }
                            begin = approverGroupStaffList.length;
                        }
                        if (approverStaffList != null && approverStaffList.length > 0) {
                            for (int i = begin; i < approverStaffList.length + approverGroupStaffList.length; i++) {
                                toStaff[i] = approverStaffList[i - begin];
                            }
                        }
                    }
                    if (toStaff != null && toStaff.length > 0) {
                        sendEmail(template, processVo.getRequestStaffCode(), requestNo, handleStaffCode,
                                traceVo.getHandleComments(), toStaff, formSystemId, processVo);
                    } else if (nextNodeId != null && !"-1".equals(nextNodeId) && !"0".equals(nextNodeId)) {
                        // 如果没有找到下一个处理的人(并且下一个节点不是最后一个节点)，则需要发email给administrator
                        // 发送email给administrator与form申请者
                        String adminEmailAddress = ParamConfigHelper.getInstance().getParamValue(
                                CommonName.PARAM_ADMIN_EMAIL);
                        StaffVO Submitter = StaffTeamHelper.getInstance().getStaffByCode(processVo.getSubmittedBy());
                        if (adminEmailAddress != null && !"".equals(adminEmailAddress)) {
                            adminEmailAddress = adminEmailAddress + "," + Submitter.getEmail();
                        } else {
                            adminEmailAddress = Submitter.getEmail();
                        }
                        String subject = "Error of E-Flow";
                        String mailContent = "<font face='Arial' size='2'>Request No.(<font color='red'><b>"
                                + requestNo + "</b></font>) can't find the approver in node <b>" + nextNode.getName()
                                + "</b> (" + nextNode.getItemId() + ").<br><br>" + "Requester: <b>"
                                + requester.getStaffName() + "</b> (<font color='red'><b>" + orgName + "</b></font>)."
                                + "</font>";
                        EFlowEmailUtil.sendEmail(subject, mailContent, adminEmailAddress, processVo);
                    }
                }
            }

            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        } finally {
            if (dbManager != null) {
                dbManager.freeConnection();
            }
        }
    }

    public static void sendEmail(EmailTemplateVO template, String formRequesterStaffCode, String requestNo,
            String handleStaffCode, String handleComments, String[] toEmailStaffs, String formSystemId)
            throws Exception {
        sendEmail(template, formRequesterStaffCode, requestNo, handleStaffCode, handleComments, toEmailStaffs,
                formSystemId, null);
    }

    public static void sendEmail(EmailTemplateVO template, String formRequesterStaffCode, String requestNo,
            String handleStaffCode, String handleComments, String[] toEmailStaffs, String formSystemId,
            WorkFlowProcessVO processVo) throws Exception {
        String emailTo = "";
        String staffName = "";
        boolean isUrgent = false;

        for (int i = 0; i < toEmailStaffs.length; i++) {
            // System.out.println("toEmailStaffs[i]: " + toEmailStaffs[i]);
            if (toEmailStaffs[i] != null && StaffTeamHelper.getInstance().getStaffByCode(toEmailStaffs[i]) != null) {
                if (StaffTeamHelper.getInstance().getStaffByCode(toEmailStaffs[i]).getEmail() != null
                        && !"".equals(StaffTeamHelper.getInstance().getStaffByCode(toEmailStaffs[i]).getEmail())) {
                    emailTo = emailTo + StaffTeamHelper.getInstance().getStaffByCode(toEmailStaffs[i]).getEmail() + ",";
                }
            }
        }
        // System.out.println("emailTo: " + emailTo);
        if (!"".equals(emailTo)) {
            emailTo = emailTo.substring(0, emailTo.length() - 1);
        } else {// 如果email地址为空，则不发送email 2008-07-11 add by Robin Hou
            return;
        }

        for (int i = 0; i < toEmailStaffs.length; i++) {
            if (toEmailStaffs[i] != null && StaffTeamHelper.getInstance().getStaffByCode(toEmailStaffs[i]) != null) {
                if (StaffTeamHelper.getInstance().getStaffByCode(toEmailStaffs[i]).getEmail() != null
                        && !"".equals(StaffTeamHelper.getInstance().getStaffByCode(toEmailStaffs[i]).getEmail())) {
                    staffName = staffName
                            + StaffTeamHelper.getInstance().getStaffByCode(toEmailStaffs[i]).getStaffName() + ",";
                }
            }
        }
        if (!"".equals(staffName)) {
            staffName = staffName.substring(0, staffName.length() - 1);
        }

        // if(template!=null){
        ParamConfigHelper paramHelper = ParamConfigHelper.getInstance();
        String emailHost = paramHelper.getParamValue("email_host_ip");
        String emailUserName = paramHelper.getParamValue("email_user_name");
        // String emailAccount = paramHelper.getParamValue("email_from_account");
        EmailUtil sendmail = new EmailUtil();
        sendmail.setHost(emailHost);
        sendmail.setUserName(emailUserName);

        sendmail.setTo(emailTo);

        // sendmail.setCopyTo(teamLeaderEmails);
        // sendmail.setFrom(emailAccount);
        sendmail.setSubject(template.getEmailSubject());
        String emailContent = template.getEmailContent();

        // String emailSuffix = ResourceHelper.getInstance().getEmailSuffix();
        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            if (processVo == null) {
                WorkFlowProcessDAO processDao = new WorkFlowProcessDAO(dbManager);
                processVo = processDao.getProcessVO(requestNo);
            }
            sendmail.setFrom(EFlowEmailUtil.getEmailFromAccount(processVo.getFormType()));
            EmailTemplateDAO emailTemplateDao = new EmailTemplateDAO(dbManager);
            EmailTemplateVO templateSuf = emailTemplateDao.getEmailTemplateByAction(CommonName.HANDLE_SUFFIX,
                    processVo.getFormType());
            // System.out.println("Form Type: " + processVo.getFormType());
            String emailSuffix = templateSuf.getEmailContent();
            if (emailSuffix != null) {
                emailContent = emailContent + emailSuffix;
            }
            /** IT1177 begin */
            WorkFlowProcessDAO workFlowProcessDao = new WorkFlowProcessDAO(dbManager);
            if (workFlowProcessDao.isUrgent(processVo.getFormSystemId(), processVo.getRequestNo())) {
                isUrgent = true;
                sendmail.setSubject(template.getEmailSubject() + "  (Urgent)");
            }
            /** IT1177 end */
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (dbManager != null) {
                dbManager.freeConnection();
            }
        }

        if (handleComments != null && !"".equals(handleComments)) {
            handleComments = DataConvertUtil.convertISOToGBK(handleComments);
        }

        // Process for deputy
        String deputyFor = "";
        String deputyMarkStart = " <!-- ";
        String deputyMarkEnd = " --> ";
        if (processVo != null && processVo.isDealByDeputy()) {
            deputyFor = StaffTeamHelper.getInstance().getStaffNameByCode(processVo.getOriginProcessor()).trim();
            deputyMarkStart = " ";
            deputyMarkEnd = " ";
        }
        // IT1177
        String duetime = "";
        String duetimeStart = " <!-- ";
        String duetimeEnd = " --> ";

        // IT1197 begin
        String urgentStart = " <!-- ";
        String urgentEnd = " --> ";
        if (isUrgent) {// is urgent case
            urgentStart = "";
            urgentEnd = "";
        } else if (processVo != null && processVo.getLimitedHours() != 0.0) {
            // IT1197 end
            duetime = Double.toString(processVo.getLimitedHours());
            duetimeStart = "";
            duetimeEnd = "";
        }

        String[] paramList = {CommonName.EMAIL_TEMPLATE_PARAM_REQUEST_NO, CommonName.EMAIL_TEMPLATE_PARAM_HANDLE_BY,
                CommonName.EMAIL_TEMPLATE_PARAM_RECEIVE_STAFF, CommonName.EMAIL_TEMPLATE_PARAM_CURRENT_DATE,
                CommonName.EMAIL_TEMPLATE_PARAM_COMMENTS, CommonName.EMAIL_TEMPLATE_PARAM_FORM_SYSTEM_ID,
                CommonName.EMAIL_TEMPLATE_PARAM_REQUESTED_BY, CommonName.EMAIL_TEMPLATE_PARAM_DEPUTY_MARK_START,
                CommonName.EMAIL_TEMPLATE_PARAM_DEPUTY_FOR, CommonName.EMAIL_TEMPLATE_PARAM_DEPUTY_MARK_END,
                CommonName.EMAIL_TEMPLATE_PARAM_DUETIME_VAL, CommonName.EMAIL_TEMPLATE_PARAM_DUETIME_START,
                CommonName.EMAIL_TEMPLATE_PARAM_DUETIME_END, CommonName.EMAIL_TEMPLATE_PARAM_URGENT_MARK_START,
                CommonName.EMAIL_TEMPLATE_PARAM_URGENT_MARK_END};

        String[] paramValue = {requestNo, StaffTeamHelper.getInstance().getStaffNameByCode(handleStaffCode), staffName,
                StringUtil.getCurrentDateStr("MM/dd/yyyy HH:mm:ss"), handleComments, formSystemId,
                StaffTeamHelper.getInstance().getStaffByCode(formRequesterStaffCode).getStaffName(), deputyMarkStart,
                deputyFor, deputyMarkEnd, duetime, duetimeStart, duetimeEnd, urgentStart, urgentEnd};

        for (int i = 0; i < paramList.length; i++) {
            if (emailContent.indexOf(paramList[i]) > -1) {
                // emailContent = emailContent.replaceAll(paramList[i],
                // paramValue[i]);
                emailContent = StringUtil.replace(emailContent, paramList[i], paramValue[i]);
            }
        }
        sendmail.setContent(emailContent);

        // sendmail.sendMail(); 2013-11-19
        sendmail.sendMailAsync();

        // }
    }

    // For CHO ePayment
    public ActionLocation batchPaymentSetting(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String resultLabel = "batchPaymentSetting";
        return mapping.findActionLocation(resultLabel);
    }

    // For CHO ePayment
    public ActionLocation batchPayment(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String voucherNo = request.getParameter("voucherNo");
        // System.out.println("voucherNo: " + voucherNo);
        String payDate = request.getParameter("payDate");
        // System.out.println("payDate: " + payDate);

        String[] requestNoList = request.getParameterValues("id");
        if (requestNoList == null) {
            return this.listWaitForDealForm(mapping, request, response);
        }

        StaffVO currentStaff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);

        for (int i = 0; i < requestNoList.length; i++) {
            String requestNo = requestNoList[i];
            System.out.println(currentStaff.getStaffCode() + " Batch payment requestNo: " + requestNo);

            IDBManager dbManager = null;
            try {
                dbManager = DBManagerFactory.getDBManager();
                FormManageDAO formDao = new FormManageDAO(dbManager);
                // CA Form No
                String CAFormNo = formDao.getCommonFieldValue(requestNo, "04", "field_04_6");
                dbManager.startTransaction();
                formDao.updateCommonFieldValue(requestNo, "05", "field_05_25", voucherNo);
                formDao.updateCommonFieldValue(requestNo, "05", "field_05_24", payDate);
                if (CAFormNo != null) {
                    // ePayment ID
                    formDao.updateCommonFieldValue(CAFormNo, "07", "field_07_19", requestNo);
                }
                dbManager.commit();
            } catch (Exception ex) {
                dbManager.rollback();
                ex.printStackTrace();
                return mapping.findActionLocation("fail");
            } finally {
                if (dbManager != null) {
                    dbManager.freeConnection();
                }
            }

            // Approve or complete the form
            WorkFlowProcessTraceVO traceVo = new WorkFlowProcessTraceVO();
            traceVo.setRequestNo(requestNo);
            traceVo.setHandleStaffCode(currentStaff.getStaffCode());
            traceVo.setHandleComments("");
            traceVo.setHandleType(CommonName.HANDLE_TYPE_APPROVE);
            this.auditForm2(traceVo);
        }

        return this.listWaitForDealForm(mapping, request, response);
    }

    public ActionLocation overdueNotify(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String[] requestNoList = request.getParameterValues("id");
        if (requestNoList == null) {
            return this.listOvertimeForm(mapping, request, response);
        }

        StaffVO currentStaff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);

        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            EmailTemplateDAO emailTemplateDao = new EmailTemplateDAO(dbManager);
            HashMap templateMap = new HashMap();
            EmailTemplateVO template = null;

            WorkFlowProcessDAO processDao = new WorkFlowProcessDAO(dbManager);
            for (int i = 0; i < requestNoList.length; i++) {
                String requestNo = requestNoList[i];
                WorkFlowProcessVO processVo = processDao.getProcessVO(requestNo);
                String processor = processVo.getCurrentProcessor();
                if (processor == null || processor.equals("")) {
                    continue;
                }
                String overtime = request.getParameter(requestNo);
                int index = overtime.indexOf("-");
                overtime = index == -1 ? overtime : overtime.substring(index + 1);
                String[] toStaff = StringUtil.split(processor, ",");
                template = (EmailTemplateVO) templateMap.get(processVo.getFormType());
                if (template == null) {
                    template = emailTemplateDao.getEmailTemplateByAction(CommonName.ACTION_OVERDUE_NOTIFICATION,
                            processVo.getFormType());
                    if (template == null) {
                        String msg = "Can not find email template for 'Overdue Notification'";
                        request.setAttribute(CommonName.COMMON_ERROR_INFOR, msg);
                        return mapping.findActionLocation("fail");
                    }
                    templateMap.put(processVo.getFormType(), template);
                }
                sendEmail(template, processVo.getRequestStaffCode(), requestNo, currentStaff.getStaffCode(), overtime,
                        toStaff, Integer.toString(processVo.getFormSystemId()), processVo);
            }
        } catch (DAOException ex) {
            ex.printStackTrace();
            return mapping.findActionLocation("fail");
        } finally {
            if (dbManager != null) {
                dbManager.freeConnection();
            }
        }

        return this.listOvertimeForm(mapping, request, response);
    }

    public ActionLocation batchApproveSetting(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String resultLabel = "batchApproveSetting";
        return mapping.findActionLocation(resultLabel);
    }

    public ActionLocation batchApprove(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String comments = request.getParameter("comments");
        comments = CommonUtil.decoderURL(comments);
        // System.out.println("comments: " + comments);

        String[] requestNoList = request.getParameterValues("id");
        if (requestNoList == null) {
            return this.listWaitForDealForm(mapping, request, response);
        }

        StaffVO currentStaff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);

        for (int i = 0; i < requestNoList.length; i++) {
            String requestNo = requestNoList[i];
            System.out.println(currentStaff.getStaffCode() + " Batch approve requestNo: " + requestNo);

            WorkFlowProcessTraceVO traceVo = new WorkFlowProcessTraceVO();
            traceVo.setRequestNo(requestNo);
            traceVo.setHandleStaffCode(currentStaff.getStaffCode());
            traceVo.setHandleComments(comments);
            traceVo.setHandleType(CommonName.HANDLE_TYPE_APPROVE);
            this.auditForm2(traceVo);
        }

        return this.listWaitForDealForm(mapping, request, response);
    }

    public ActionLocation batchRejectSetting(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String resultLabel = "batchRejectSetting";
        return mapping.findActionLocation(resultLabel);
    }

    public ActionLocation batchReject(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String comments = request.getParameter("comments");
        comments = CommonUtil.decoderURL(comments);
        // System.out.println("comments: " + comments);

        String[] requestNoList = request.getParameterValues("id");
        if (requestNoList == null) {
            return this.listWaitForDealForm(mapping, request, response);
        }

        StaffVO currentStaff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);

        for (int i = 0; i < requestNoList.length; i++) {
            String requestNo = requestNoList[i];
            System.out.println(currentStaff.getStaffCode() + " Batch reject requestNo: " + requestNo);

            WorkFlowProcessTraceVO traceVo = new WorkFlowProcessTraceVO();
            traceVo.setRequestNo(requestNo);
            traceVo.setHandleStaffCode(currentStaff.getStaffCode());
            traceVo.setHandleComments(comments);
            traceVo.setHandleType(CommonName.HANDLE_TYPE_REJECT);
            this.auditForm2(traceVo);
        }

        return this.listWaitForDealForm(mapping, request, response);
    }

    public ActionLocation successTipPage(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String resultLabel = "successTipPage";

        String msg = java.net.URLDecoder.decode(request.getParameter("success_message"), "UTF-8");
        request.setAttribute("success_message", msg);
        return mapping.findActionLocation(resultLabel);
    }

    public ActionLocation holdForm(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String requestNo = request.getParameter("requestNo");
        IDBManager dbManager = null;
        PrintWriter out = response.getWriter();

        try {
            dbManager = DBManagerFactory.getDBManager();
            WorkFlowNodeDAO nodeDao = new WorkFlowNodeDAO(dbManager);
            nodeDao.updateNodeOpenFlag(requestNo, "2");
            out.print("success");

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
}
