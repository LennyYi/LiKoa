package com.aiait.eflow.wkf.action;

import java.io.*;
import java.util.*;

import javax.servlet.http.*;

import com.aiait.eflow.common.*;
import com.aiait.eflow.common.helper.*;
import com.aiait.eflow.formmanage.dao.FormManageDAO;
import com.aiait.eflow.formmanage.vo.*;
import com.aiait.eflow.housekeeping.dao.StaffDAO;
import com.aiait.eflow.housekeeping.vo.*;
import com.aiait.eflow.util.StringUtil;
import com.aiait.eflow.wkf.dao.*;
import com.aiait.eflow.wkf.util.WorkFlowXmlUtil;
import com.aiait.eflow.wkf.vo.*;
import com.aiait.framework.db.*;
import com.aiait.framework.exception.DAOException;
import com.aiait.framework.mvc.action.*;
import com.aiait.framework.util.CommonUtil;

/**
 * 工作流设计ACTION
 * 
 * @author ASNPGJ3 Robin Hou
 * @version 1.0
 * @date 2007-04-13
 * 
 *       Change Log Task_ID Author Modify_Date Description IT0958 Robin 11/02/2007 DS-004 Add a full name property for
 *       node
 */
public class WorkFlowDesignAction extends DispatchAction {

    /**
     * 进入workflow基础信息的编辑页面
     * 
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionLocation editFlowBaseInfor(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String returnLabel = "flowBaseInforPage";
        String saveType = (String) request.getParameter("saveType");
        // 获取所有的form
        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            FormManageDAO formDao = new FormManageDAO(dbManager);
            // Collection formList = formDao.getFormList("0");
            Collection formList = formDao.getFormList("");
            request.setAttribute("formList", formList);
            if (saveType != null && "edit".equals(saveType)) {
                WorkFlowDefineDAO defineDao = new WorkFlowDefineDAO(dbManager);
                String flowId = (String) request.getParameter("flowId");
                WorkFlowVO flow = defineDao.getWorkFlow(Integer.parseInt(flowId));
                request.setAttribute("flow", flow);
            }
        } catch (Exception e) {
            request.setAttribute("formList", null);
            e.printStackTrace();
        } finally {
            if (dbManager != null)
                dbManager.freeConnection();
        }
        return mapping.findActionLocation(returnLabel);
    }

    /**
     * 保存workflow基础信息
     * 
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionLocation saveFlowBaseInfor(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        StaffVO currentStaff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
        // 保存form的基础信息
        String saveType = (String) request.getParameter("saveType");
        WorkFlowVO flow = new WorkFlowVO();
        flow.setFlowName(CommonUtil.decoderURL((String) request.getParameter("flowName")));
        flow.setDescription(CommonUtil.decoderURL((String) request.getParameter("description")));
        flow.setFormSystemId(Integer.parseInt((String) request.getParameter("formSystemId")));
        flow.setOrgId(currentStaff.getOrgId());
        flow.setAfterHandleUrl((String) request.getParameter("afterHandleUrl"));
        flow.setOrgId((String) request.getParameter("orgId"));
        IDBManager dbManager = null;
        PrintWriter out = response.getWriter();
        try {
            dbManager = DBManagerFactory.getDBManager();
            WorkFlowDefineDAO defineDao = new WorkFlowDefineDAO(dbManager);
            if (saveType != null && "edit".equals(saveType)) {
                if (flow.getFormSystemId() != 0) {
                    int num = defineDao.getLinkedFormNum(flow.getFormSystemId(),
                            Integer.parseInt((String) request.getParameter("flowId")));
                    if (num > 0) {
                        out.print("The selected form have already linked other flow,please select other form!");
                        return null;
                    }
                }
                flow.setFlowBaseId(Integer.parseInt((String) request.getParameter("flowId")));
                defineDao.update(flow);
            } else {
                if (flow.getFormSystemId() != 0) {
                    int num = defineDao.getLinkedFormNum(flow.getFormSystemId(), 0);
                    if (num > 0) {
                        out.print("The selected form have already linked other flow,please select other form!");
                        return null;
                    }
                }
                defineDao.save(flow);
            }
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
     * copy flow (Include copy workflow base information and workflow flow information
     * 
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionLocation copyFlow(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String flowId = (String) request.getParameter("flowId");
        IDBManager dbManager = null;
        PrintWriter out = response.getWriter();
        try {
            dbManager = DBManagerFactory.getDBManager();
            WorkFlowDefineDAO defineDao = new WorkFlowDefineDAO(dbManager);
            dbManager.startTransaction();
            defineDao.copyFlow(Integer.parseInt(flowId));
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

    public ActionLocation listFlow(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String returnLabel = "flowlist";
        // IT0958-DS011 Begin
        String flowname = (String) request.getParameter("flowName");
        String formType = (String) request.getParameter("formType");
        String orgId = (String) request.getParameter("orgId");

        String superOrgIds = "";
        StaffVO currentStaff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
        // modify by Robin Hou 2008-06-19
        // 如果orgId为空，则默认查询其所在公司以及其所有下级公司的所有form
        if (orgId == null || "".equals(orgId)) {
            orgId = currentStaff.getOrgId();
            superOrgIds = currentStaff.getLowerCompanys(); // CompanyHelper.getInstance().getLowerCompany(orgId);
        } else {
            superOrgIds = "'" + orgId + "'";
        }
        // end modify

        // System.out.println(formType);
        FormManageVO queryVo = new FormManageVO();
        WorkFlowVO wfVo = new WorkFlowVO();
        if (flowname != null && !"".equals(flowname)) {
            wfVo.setFlowName(flowname);
        } else {
            wfVo.setFlowName("");
        }

        if (formType != null && !"".equals(formType)) {
            queryVo.setFormType("'" + formType + "'");
        } else {
            String formTypes = "";
            AuthorityHelper authority = AuthorityHelper.getInstance();
            Collection typeList = FormTypeHelper.getInstance().getFormTypeList();
            if (typeList != null && typeList.size() > 0) {
                Iterator it = typeList.iterator();
                while (it.hasNext()) {
                    FormTypeVO vo = (FormTypeVO) it.next();
                    if (authority.checkAuthorityByFormType(currentStaff.getCurrentRoleId(),
                            ModuleOperateName.MODULE_FORM_MANAGE, vo.getFormTypeId())) {
                        formTypes = formTypes + "'" + vo.getFormTypeId() + "',";
                    }
                }
            }
            /**
             * if(authority.checkAuthority(ModuleOperateName.MODULE_FORM_MANAGE,8)){ formTypes = formTypes + "'01',"; }
             * if(authority.checkAuthority(ModuleOperateName.MODULE_FORM_MANAGE,9)){ formTypes = formTypes + "'02',"; }
             * if(authority.checkAuthority(ModuleOperateName.MODULE_FORM_MANAGE,10)){ formTypes = formTypes + "'03',"; }
             * if(authority.checkAuthority(ModuleOperateName.MODULE_FORM_MANAGE,11)){ formTypes = formTypes + "'04',"; }
             * if(authority.checkAuthority(ModuleOperateName.MODULE_FORM_MANAGE,12)){ formTypes = formTypes + "'05',"; }
             **/
            if (!"".equals(formTypes)) {
                formTypes = formTypes.substring(0, formTypes.length() - 1);
            }

            queryVo.setFormType(formTypes);
        }
        queryVo.setOrgId(superOrgIds);
        // IT0958-DS011 End
        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            WorkFlowDefineDAO defineDao = new WorkFlowDefineDAO(dbManager);
            // IT0958-DS011 Begin
            Collection flowList = defineDao.getFlowByFormType(queryVo, wfVo, formType);
            // IT0958-DS011 end
            request.setAttribute("flowList", flowList);
        } catch (Exception e) {
            request.setAttribute("flowList", null);
            e.printStackTrace();
        } finally {
            if (dbManager != null)
                dbManager.freeConnection();
        }
        return mapping.findActionLocation(returnLabel);
    }

    /**
     * 进入指定流程的设计页面
     * 
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionLocation showDesign(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String returnLabel = "designPage";
        String flowId = (String) request.getParameter("flowId");
        // 获取当前流程中最大节点ID的值，用来作为新增节点的种子值，如果没有节点存在，则给与返回"0"
        IDBManager dbManager = null;
        int maxNodeId = 0;
        try {
            dbManager = DBManagerFactory.getDBManager();
            WorkFlowNodeDAO nodeDao = new WorkFlowNodeDAO(dbManager);
            WorkFlowDefineDAO flowDao = new WorkFlowDefineDAO(dbManager);
            maxNodeId = nodeDao.getMaxNodeIdByFlow(Integer.parseInt(flowId));
            WorkFlowVO flow = flowDao.getWorkFlow(Integer.parseInt(flowId));
            request.setAttribute("flow", flow);
        } catch (DAOException e) {
            e.printStackTrace();
        } finally {
            if (dbManager != null)
                dbManager.freeConnection();
        }
        request.setAttribute("maxNodeId", "" + maxNodeId);
        return mapping.findActionLocation(returnLabel);
    }

    /**
     * 将指定form的流程显示出来
     * 
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionLocation showFormFlow(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String returnLabel = "showCurrentFormFlow";
        String formSystemId = (String) request.getParameter("formSystemId");

        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            // get the correct flow by the form_system_id
            WorkFlowDefineDAO flowDao = new WorkFlowDefineDAO(dbManager);
            WorkFlowVO flow = flowDao.getWorkFlowByForm(Integer.parseInt(formSystemId));
            request.setAttribute("flowId", "" + flow.getFlowBaseId());
        } catch (DAOException e) {
            e.printStackTrace();
        } finally {
            if (dbManager != null)
                dbManager.freeConnection();
        }
        return mapping.findActionLocation(returnLabel);
    }

    /**
     * 加载指定的workflow
     * 
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionLocation loadWorkFlow(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String flowId = (String) request.getParameter("flowId");
        if (flowId != null && !"".equals(flowId)) {
            IDBManager dbManager = null;
            try {
                dbManager = DBManagerFactory.getDBManager();
                WorkFlowDefineDAO defineDao = new WorkFlowDefineDAO(dbManager);
                WorkFlowNodeDAO nodeDao = new WorkFlowNodeDAO(dbManager);
                WorkFlowVO flow = defineDao.getWorkFlow(Integer.parseInt(flowId));
                Collection nodeList = nodeDao.getNodeListByFlow(Integer.parseInt(flowId));
                HashMap conditionMap = nodeDao.getConditionByFlow(Integer.parseInt(flowId));
                if (conditionMap != null) {
                    flow.setConditionMap(conditionMap);
                }
                if (nodeList == null) {
                    nodeList = initDesign();
                }
                flow.setItemList(nodeList);
                // 如果是用户查看自己申请的form当前所处的环节流程图，则需要加载该form当前的流程处理实例
                String requestNo = (String) request.getParameter("requestNo");
                if (requestNo != null && !"".equals(requestNo)) {
                    WorkFlowProcessDAO processDao = new WorkFlowProcessDAO(dbManager);
                    WorkFlowProcessVO process = processDao.getProcessVO(requestNo);
                    flow.setCurrentNodeId(process.getNodeId());
                }
                printXmlData(response, flow);
            } catch (Exception e) {
                e.printStackTrace();
                PrintWriter out = response.getWriter();
                out.print("fail");
                if (out != null)
                    out.close();
            } finally {
                if (dbManager != null)
                    dbManager.freeConnection();
            }
        }
        return null;
    }

    /**
     * 保存指定的workflow
     * 
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionLocation saveWorkFlow(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String returnLabel = "designPage";
        String flowId = (String) request.getParameter("flowId");
        String xmldata = (String) request.getParameter("xmldata");
        if (xmldata == null || "".equals(xmldata.trim())) {
            throw new Exception("Failed to save, please retry");
        }
        // System.out.println("--------xmldata:"+xmldata);
        Collection nodeList = WorkFlowXmlUtil.parseXmlData(xmldata);
        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            dbManager.startTransaction();
            WorkFlowNodeDAO nodeDao = new WorkFlowNodeDAO(dbManager);
            nodeDao.deleteNodesByFlow(Integer.parseInt(flowId));
            Iterator nodeIt = nodeList.iterator();
            while (nodeIt.hasNext()) {
                WorkFlowItemVO node = (WorkFlowItemVO) nodeIt.next();
                nodeDao.save(node);
            }
            // 整理条件设置表的数据，将无关的条件删除（例如，节点已经删除了，或者节点间的关系不存在了等等）
            // nodeDao.deleteNodeConditionsByFlow(Integer.parseInt(flowId));
            WorkFlowDefineDAO flowDao = new WorkFlowDefineDAO(dbManager);
            WorkFlowVO flow = flowDao.getWorkFlow(Integer.parseInt(flowId));
            int maxNodeId = nodeDao.getMaxNodeIdByFlow(Integer.parseInt(flowId));
            request.setAttribute("maxNodeId", "" + maxNodeId);
            request.setAttribute("flow", flow);
            dbManager.commit();
        } catch (Exception e) {
            dbManager.rollback();
            e.printStackTrace();
        } finally {
            if (dbManager != null)
                dbManager.freeConnection();
        }
        return mapping.findActionLocation(returnLabel);
    }

    /**
     * delete指定的workflow
     * 
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionLocation deleteWorkFlow(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String[] flowId = (String[]) request.getParameterValues("flowId");
        IDBManager dbManager = null;
        PrintWriter out = response.getWriter();
        try {
            dbManager = DBManagerFactory.getDBManager();
            WorkFlowNodeDAO nodeDao = new WorkFlowNodeDAO(dbManager);
            WorkFlowDefineDAO defineDao = new WorkFlowDefineDAO(dbManager);
            // if exist that any flow have linked form,it can't be deleted!
            for (int i = 0; i < flowId.length; i++) {
                WorkFlowVO flow = defineDao.getWorkFlow(Integer.parseInt(flowId[i]));
                if (flow.getFormSystemId() > 0) {
                    out.print("There is at least one flow which have linked form,it can't be deleted");
                    return null;
                }
            }
            dbManager.startTransaction();
            for (int i = 0; i < flowId.length; i++) {
                nodeDao.deleteNodesByFlow(Integer.parseInt(flowId[i]));
                defineDao.deleteFlowBaseInfo(Integer.parseInt(flowId[i]));
            }
            dbManager.commit();
            out.print("success");
        } catch (DAOException e) {
            dbManager.rollback();
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
     * 进入指定折线的属性编辑页面（节点间流转的条件设置）
     * 
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionLocation enterConditionSet(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String returnLabel = "nodeConditionPage";
        String flowId = (String) request.getParameter("flowId");
        String formSystemId = (String) request.getParameter("formSystemId");
        String beginNodeId = (String) request.getParameter("beginNodeId");
        String endNodeId = (String) request.getParameter("endNodeId");
        IDBManager dbManager = null;
        // 获取该flow所关联的form的相关信息
        try {
            dbManager = DBManagerFactory.getDBManager();
            FormManageDAO formDao = new FormManageDAO(dbManager);
            FormManageVO form = formDao.getFormBySystemId(Integer.parseInt(formSystemId));
            request.setAttribute("form", form);
            // 获取条件的前节点与后节点的名称
            WorkFlowNodeDAO nodeDao = new WorkFlowNodeDAO(dbManager);
            String beginNodeName = (nodeDao.getNodeById(Integer.parseInt(flowId), beginNodeId)).getName();
            WorkFlowItemVO endNode = nodeDao.getNodeById(Integer.parseInt(flowId), endNodeId);
            if (endNode == null) {
                request.setAttribute("error", "Please save the designing flow before setting the condition!");
                request.setAttribute("returnType", "close");
                return mapping.findActionLocation("fail");
            }
            String endNodeName = endNode.getName();
            request.setAttribute("beginNodeName", beginNodeName);
            request.setAttribute("endNodeName", endNodeName);
            // 如果是修改，则需要准备该折线已经设定的条件，如果还没有设置，则设为null
            if (flowId != null && !"".equals(flowId) && beginNodeId != null && !"".equals(beginNodeId)) {
                // NodeConditionVO condition = nodeDao.getConditionVo(Integer.parseInt(flowId),beginNodeId,endNodeId);
                Collection conditionList = nodeDao.getConditionVo(Integer.parseInt(flowId), beginNodeId, endNodeId);
                request.setAttribute("conditionList", conditionList);
            }
        } catch (DAOException e) {
            e.printStackTrace();
        } finally {
            if (dbManager != null)
                dbManager.freeConnection();
        }
        return mapping.findActionLocation(returnLabel);
    }

    /**
     * 保存设置的节点流转条件（节点间流转的条件设置）
     * 
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionLocation saveNodeCondition(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String returnLabel = "tempsuccess";
        String saveType = (String) request.getParameter("saveType");
        if (saveType == null)
            saveType = "add";
        String flowId = (String) request.getParameter("flowId");

        String beginNodeId = (String) request.getParameter("beginNodeId");
        String endNodeId = (String) request.getParameter("endNodeId");

        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            WorkFlowNodeDAO nodeDao = new WorkFlowNodeDAO(dbManager);
            WorkFlowDefineDAO flowDao = new WorkFlowDefineDAO(dbManager); // 20080710
            dbManager.startTransaction();

            // 20080710
            WorkFlowVO flow = flowDao.getWorkFlow(Integer.parseInt(flowId));
            int formSystemId = flow.getFormSystemId();
            String prefixTableName = "teflow_" + formSystemId + "_";
            // 生成一个已经将这些条件组合好的sql语句
            StringBuffer querySQL = new StringBuffer("");

            // 先删除该节点所有条件，然后插入新条件
            nodeDao.deleteNodeConditionByFlow(Integer.parseInt(flowId), beginNodeId, endNodeId);
            String[] conditionStr = request.getParameterValues("conditionStr");
            if (conditionStr != null && conditionStr.length > 0) {
                StringBuffer subWhereStr = new StringBuffer("");
                String andOr = "";
                String compareValue = "";
                HashMap tableNameMap = new HashMap();
                String sectionTableName = "", sectionTableName2 = "";
                NodeConditionVO condition = new NodeConditionVO();
                for (int i = 0; i < conditionStr.length; i++) {
                    // System.out.println("--------:"+conditionStr);
                    String[] temp = StringUtil.split(conditionStr[i], "||"); // sectionId || fieldId || compareType ||
                                                                             // fieldValue || logicType || fieldLabel ||
                                                                             // compareLabel || isFunction
                    // System.out.println(temp[0]+"."+temp[1]+""+temp[2]+temp[3]+temp[4]+"");
                    condition.setFlowId(Integer.parseInt(flowId));
                    condition.setBeginNodeId(beginNodeId);
                    condition.setEndNodeId(endNodeId);
                    condition.setSectionId(temp[0]);
                    condition.setFieldId(temp[1]);
                    condition.setCompareType(temp[2]);
                    condition.setCompareValue(temp[3]);
                    condition.setLogicType(temp[4]);
                    condition.setFieldLabel(temp[5]);
                    condition.setCompareLabel(temp[6]);
                    condition.setIsFunction(temp[7]);
                    condition.setConditionType("2");
                    nodeDao.saveConditionVo(condition, saveType);

                    // 如果是最后一个条件，则逻辑符号为空
                    if (i == (conditionStr.length - 1)) {
                        andOr = "  ";
                    } else {
                        if (condition.getLogicType() != null && "01".equals(condition.getLogicType())) {
                            andOr = " and ";
                        } else if ("02".equals(condition.getLogicType())) {
                            andOr = " or ";
                        } else {
                            andOr = " and ";
                        }
                    }
                    if ("2".equals(condition.getIsFunction())) {
                        compareValue = "getamountbytitle()";
                    } else if ("3".equals(condition.getIsFunction())) {
                        compareValue = condition.getCompareValue();
                        sectionTableName2 = compareValue.split("[.]")[0];
                    } else {
                        compareValue = condition.getCompareValue();
                    }
                    sectionTableName = prefixTableName + condition.getSectionId();
                    if (("like").equals(condition.getCompareType().trim().toLowerCase())) {
                        subWhereStr.append(" ").append(sectionTableName).append(".").append(condition.getFieldId())
                                .append(" like '%").append(compareValue).append("%' ").append(andOr);
                    } else {
                        subWhereStr.append(" ").append(sectionTableName).append(".").append(condition.getFieldId())
                                .append(condition.getCompareType()).append("  '").append(compareValue).append("' ")
                                .append(andOr);
                    }

                    if (i == 0) {
                        querySQL.append("select " + (sectionTableName) + ".request_no from ");
                    }
                    if (tableNameMap.containsKey(sectionTableName) == false) {
                        tableNameMap.put(sectionTableName, sectionTableName);
                        querySQL.append(" ").append(sectionTableName).append(",");
                    }
                    if ("".equals(sectionTableName2) == false && tableNameMap.containsKey(sectionTableName2) == false) {
                        tableNameMap.put(sectionTableName2, sectionTableName2);
                        querySQL.append(" ").append(sectionTableName2).append(",");
                    }
                }
                // 去掉最后一个逗号","
                querySQL = new StringBuffer(querySQL.substring(0, querySQL.length() - 1));
                querySQL.append(" where 1=1 ");
                Object[] tableName = tableNameMap.keySet().toArray();
                if (tableName.length > 1) {
                    for (int i = 1; i < tableName.length; i++) {
                        querySQL.append(" and ").append(tableName[0]).append(".request_no=").append(tableName[i])
                                .append(".request_no ");
                    }
                }
                querySQL.append(" and (").append(subWhereStr).append(")");
                nodeDao.updateConditionSubSQL(querySQL.toString(), condition);
            }

            dbManager.commit();
        } catch (DAOException e) {
            dbManager.rollback();
            e.printStackTrace();
            mapping.findActionLocation("fail");
        } finally {
            if (dbManager != null)
                dbManager.freeConnection();
        }
        return mapping.findActionLocation(returnLabel);
    }

    /**
     * 进入指定节点的属性编辑页面
     * 
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionLocation enterNodePropertySet(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String returnLabel = "nodePropertyPage";
        String flowId = (String) request.getParameter("flowId");
        String nodeId = (String) request.getParameter("nodeId");
        if (flowId != null && !"".equals(flowId) && nodeId != null && !"".equals(nodeId)) {
            // 获取该节点的属性信息
            IDBManager dbManager = null;
            try {
                dbManager = DBManagerFactory.getDBManager();
                WorkFlowNodeDAO nodeDao = new WorkFlowNodeDAO(dbManager);
                WorkFlowItemVO node = nodeDao.getNodeById(Integer.parseInt(flowId), nodeId);
                if (node != null) {
                    request.setAttribute("flowNode", node);
                }
            } catch (DAOException e) {
                e.printStackTrace();
            } finally {
                if (dbManager != null)
                    dbManager.freeConnection();
            }
        }
        // 准备Approver相关信息
        Collection approverGroupList = BaseDataHelper.getInstance().getApproverGroupList();
        request.setAttribute("approverGroupList", approverGroupList);
        // 准备staff信息
        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            StaffDAO staffDao = new StaffDAO(dbManager);
            Collection staffList = staffDao.getAllStaff();
            request.setAttribute("staffList", staffList);
            // 如果该flow已经绑定了form，则获取该form的section信息
            FormManageDAO formDao = new FormManageDAO(dbManager);
            Collection sectionList = formDao.getFormSectionListByFlowId(Integer.parseInt(flowId));
            request.setAttribute("sectionList", sectionList);
            // Get form field list
            if (!sectionList.isEmpty()) {
                Iterator it = sectionList.iterator();
                FormSectionVO section = (FormSectionVO) it.next();
                int formSystemId = section.getFormSystemId();
                Collection fieldList = formDao.getFieldListByForm(formSystemId);
                request.setAttribute("fieldList", fieldList);
            }
        } catch (DAOException e) {
            e.printStackTrace();
        } finally {
            if (dbManager != null)
                dbManager.freeConnection();
        }
        return mapping.findActionLocation(returnLabel);
    }

    private Collection initDesign() {
        WorkFlowItemVO beginItem = new WorkFlowItemVO();
        beginItem.setItemId("0");
        beginItem.setName("Begin");
        beginItem.setLimiteDate("0");
        beginItem.setPosX("1000px");
        beginItem.setPosY("1400px");

        WorkFlowItemVO endItem = new WorkFlowItemVO();
        endItem.setItemId("-1");
        endItem.setName("End");
        endItem.setLimiteDate("0");
        endItem.setPosX("5000px");
        endItem.setPosY("1400px");

        Collection itemList = new ArrayList();
        itemList.add(beginItem);
        itemList.add(endItem);

        return itemList;
    }

    // DS-006
    private void printXmlData(HttpServletResponse response, WorkFlowVO flow) throws IOException {

        response.setContentType("text/xml;charset=GB2312"); // it is very important
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        PrintWriter out = response.getWriter();
        out.println("<?xml version=\"1.0\" encoding=\"GB2312\"?>");
        out.println("<WorkFlow>");
        if (flow.itemList != null && flow.itemList.size() > 0) {
            Iterator it = flow.itemList.iterator();
            String spaceSign = "";
            HashMap conditionMap = flow.getConditionMap();
            while (it.hasNext()) {
                out.println("<WorkFlowItem>");
                WorkFlowItemVO item = (WorkFlowItemVO) it.next();
                out.println("<ItemID>" + item.getItemId() + "</ItemID>");
                out.println("<Name>" + item.getName() + "</Name>");
                out.println("<NodeAlias>" + item.getNodeAlias() + "</NodeAlias>");
                out.println("<NodeType>" + item.getItemType() + "</NodeType>");
                out.println("<LimiteDate>" + item.getLimiteDate() + "</LimiteDate>");
                out.println("<PosX>" + item.getPosX() + "</PosX>");
                out.println("<PosY>" + item.getPosY() + "</PosY>");

                out.println("<processorField>" + item.getProcessorField() + "</processorField>");
                out.println("<companyField>" + item.getCompanyField() + "</companyField>");
                out.println("<delaytimeField>" + item.getDelaytimeField() + "</delaytimeField>");
                out.println("<approveHandle>" + item.getApproveHandle() + "</approveHandle>");
                out.println("<rejectHandle>" + item.getRejectHandle() + "</rejectHandle>");
                out.println("<approveAlias>" + item.getApproveAlias() + "</approveAlias>");
                out.println("<rejectAlias>" + item.getRejectAlias() + "</rejectAlias>");

                // 该节点只是在查看当前form所处的流程时才有用（2007-04-27）
                if (flow.getCurrentNodeId() != null && !"".equals(flow.getCurrentNodeId())) {
                    if (flow.getCurrentNodeId().equals(item.getItemId())) {
                        out.println("<CurrentItem>true</CurrentItem>");
                        // System.out.println("true");
                    } else {
                        out.println("<CurrentItem>false</CurrentItem>");
                        // System.out.println("false");
                    }
                }

                out.println("<ApproverList>");

                if (item.getApproveGroupId() != null && !"".equals(item.getApproveGroupId())) {
                    out.print(item.getApproveGroupId());
                    spaceSign = ",";
                }
                if (item.getApprovestaffCode() != null && !"".equals(item.getApprovestaffCode())) {
                    out.print(spaceSign + item.getApprovestaffCode());
                }
                spaceSign = "";
                out.println("</ApproverList>");

                out.println("<UpdateSections>");
                if (item.getUpdateSections() != null) {
                    out.print(item.getUpdateSections());
                } else {
                    out.print("");
                }
                out.println("</UpdateSections>");

                out.println("<NewSectionFields>");
                if (item.getFillSectionFields() != null) {
                    out.print(item.getFillSectionFields());
                } else {
                    out.print("");
                }
                out.println("</NewSectionFields>");

                out.println("</WorkFlowItem>");

                if (!item.getItemId().equals("0")) {
                    if (item.getPriDepId() != null && !"".equals(item.getPriDepId())) {
                        if (item.getPriDepId().indexOf(",") > -1) {
                            String[] priId = item.getPriDepId().split(",");
                            for (int i = 0; i < priId.length; i++) {
                                out.println("<Relation>");
                                out.println("<MasterItem>" + priId[i] + "</MasterItem>");
                                out.println("<SecondItem>" + item.getItemId() + "</SecondItem>");
                                if (conditionMap != null && conditionMap.containsKey(priId[i] + "&" + item.getItemId())) {
                                    // 将比较符号进行格式化
                                    String temp = StringUtil
                                            .replace((String) (conditionMap.get(priId[i] + "&" + item.getItemId())),
                                                    "<", "&lt;");
                                    temp = StringUtil.replace(temp, ">", "&gt;");
                                    out.println("<Title>" + temp + "</Title>");
                                } else {
                                    out.println("<Title>" + "</Title>");
                                }
                                out.println("</Relation>");
                            }
                        } else {
                            out.println("<Relation>");
                            out.println("<MasterItem>" + item.getPriDepId() + "</MasterItem>");
                            out.println("<SecondItem>" + item.getItemId() + "</SecondItem>");
                            if (conditionMap != null
                                    && conditionMap.containsKey(item.getPriDepId() + "&" + item.getItemId())) {
                                // 将比较符号进行格式化
                                String temp = StringUtil.replace(
                                        (String) (conditionMap.get(item.getPriDepId() + "&" + item.getItemId())), "<",
                                        "&lt;");
                                temp = StringUtil.replace(temp, ">", "&gt;");
                                out.println("<Title>" + temp + "</Title>");
                                // out.println("<Title>"+conditionMap.get(item.getPriDepId()+"&"+item.getItemId())+"</Title>");
                            } else {
                                out.println("<Title>" + "</Title>");
                            }
                            out.println("</Relation>");
                        }
                    }
                }
            }

        }
        out.println("</WorkFlow>");
        if (out != null)
            out.close();
    }
}
