package com.aiait.eflow.wkf.dao;

import java.io.IOException;
import java.net.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

import com.aiait.eflow.common.CommonName;
import com.aiait.eflow.common.helper.*;
import com.aiait.eflow.formmanage.dao.FormManageDAO;
import com.aiait.eflow.formmanage.vo.*;
import com.aiait.eflow.housekeeping.vo.StaffVO;
import com.aiait.eflow.util.*;
import com.aiait.eflow.wkf.util.DataMapUtil;
import com.aiait.eflow.wkf.vo.*;
import com.aiait.framework.common.DataType;
import com.aiait.framework.db.*;
import com.aiait.framework.exception.DAOException;
import com.aiait.framework.vo.BaseVO;

public class WorkFlowProcessDAO extends BaseDAOImpl {

    protected HashMap specialFieldMap = new HashMap();

    protected SimpleDateFormat datetimeFormatDB = new SimpleDateFormat(CommonName.FORMAT_DATETIME_DB);
    protected SimpleDateFormat datetimeFormat = new SimpleDateFormat(CommonName.FORMAT_DATETIME);

    /**
     * IT0973
     * 
     * @param requestNo
     * @throws DAOException
     */
    public void unlockForm(String requestNo) throws DAOException {
        String sql = "update teflow_wkf_process set inprocess='0',inprocess_staff_code='' ";
        sql = sql + " where request_no='" + requestNo + "' ";
        dbManager.executeUpdate(sql);
    }

    /**
     * 保存WorkFlowProcessTraceVO记录
     * 
     * @param vo
     * @throws DAOException
     */
    public void saveWorkFlowProcessTraceVO(WorkFlowProcessTraceVO vo) throws DAOException {
        String comments = "";
        if (vo.getHandleComments() != null) {
            comments = StringUtil.FormatSQL(vo.getHandleComments());
        }
        String insertSQL = "insert into teflow_wkf_process_trace(request_no,flow_id,node_id,current_processor,handle_date,handle_hours,handle_type,handle_comments,is_deputy,origin_processor,overdue_hours,attach_file,node_name) "
                + " select '"
                + vo.getRequestNo()
                + "',"
                + vo.getFlowId()
                + ",'"
                + vo.getCurrentNodeId()
                + "','"
                + vo.getHandleStaffCode()
                + "',getdate(),"
                + vo.getHandleHours()
                + ",'"
                + vo.getHandleType()
                + "','"
                + comments
                + "','"
                + vo.getDeputyFlag()
                + "','"
                + vo.getOriginProcessor()
                + "',"
                + vo.getDelayTime()
                + ",'"
                + vo.getFilePathName()
                + "',node_name from teflow_wkf_detail where flow_id="
                + vo.getFlowId()
                + " and node_id='" + vo.getCurrentNodeId() + "' ";
        // System.out.println("-----:"+insertSQL);
        dbManager.executeUpdate(insertSQL);
    }

    /**
     * 2.IT0958 更新当前处理实例中所邀请的专家
     * 
     * @param requestNo
     * @param experts
     * @throws DAOException
     */
    public void updateInvitedExperts(String requestNo, String experts) throws DAOException {
        String updateSQL = " update teflow_wkf_process set invited_expert='" + experts + "' where request_no='"
                + requestNo + "'";
        dbManager.executeUpdate(updateSQL);
    }

    /**
     * 获取曾经处理所经过的节点集合
     * 
     * @param requestNo
     *            该处理实例的流水号
     * @param filterAuto
     *            : if true, filter the auto-flow/optional nodes
     * @return
     * @throws DAOException
     */
    public Collection getProcessedNodeList(String requestNo, boolean filterAuto) throws DAOException {
        Collection list = new ArrayList();
        String selSQL = "select node_id,node_name,node_alias from teflow_wkf_detail where flow_id in (select distinct flow_id from teflow_wkf_process_trace "
                + " where request_no='"
                + requestNo
                + "') and node_id in (select distinct node_id from teflow_wkf_process_trace where request_no='"
                + requestNo + "' ";

        if (filterAuto) {
            selSQL += " and  handle_type <> '" + CommonName.HANDLE_TYPE_SKIPPED + "') " + " and  node_type <> '"
                    + CommonName.NODE_TYPE_AUTOFLOW + "'";
        } else {
            selSQL += ")";
        }
        selSQL += " order by node_id asc";

        Collection queryList = dbManager.query(selSQL);
        if (queryList == null || queryList.size() == 0)
            return list;
        Iterator it = queryList.iterator();
        while (it.hasNext()) {
            HashMap map = (HashMap) it.next();
            WorkFlowItemVO node = new WorkFlowItemVO();
            node.setItemId(FieldUtil.convertSafeString(map, "NODE_ID"));
            node.setName(DataConvertUtil.convertISOToGBK(FieldUtil.convertSafeString(map, "NODE_NAME")));
            node.setNodeAlias(DataConvertUtil.convertISOToGBK(FieldUtil.convertSafeString(map, "NODE_ALIAS",
                    node.getName())));
            list.add(node);
        }
        return list;
    }

    /**
     * IT0958 DS-013 获取需要导出生成文件的记录集
     * 
     * @param formSystemId
     * @return
     * @throws DAOException
     */
    public Collection getExportFileList(String formSystemId, String[] fields, String[] sectionIds,
            WorkFlowProcessVO conditionVo) throws DAOException {
        Collection result = new ArrayList();
        String[] tableNames = new String[sectionIds.length];
        for (int i = 0; i < sectionIds.length; i++) {
            Collection tempList = dbManager
                    .query("select table_name, section_type from teflow_form_section where form_system_id="
                            + formSystemId + " and section_id='" + sectionIds[i] + "'");
            HashMap map = (HashMap) tempList.iterator().next();
            String sectionType = (String) map.get("SECTION_TYPE");
            if ("00".equals(sectionType) || "01".equals(sectionType)) {
                // Ignore table(01) and attachment(00) section
                continue;
            }
            // System.out.println("----tableName="+"select table_name from teflow_form_section where form_system_id="+formSystemId+" and section_id='"+sectionIds[i]+"' ");
            tableNames[i] = (String) map.get("TABLE_NAME");
        }
        String selectFields = "";
        String[] temps = new String[2];
        for (int i = 0; i < fields.length; i++) {
            temps = StringUtil.split(fields[i], "||");
            for (int j = 0; j < sectionIds.length; j++) {
                if (sectionIds[j].equals(temps[0])) {
                    temps[0] = tableNames[j];
                    break;
                }
            }
            if (temps[0] == null) {
                // Field of ignored section type
                // System.out.println("Ignored field: " + temps[1]);
                continue;
            }
            selectFields = selectFields + " " + temps[0] + "." + temps[1] + ",";
        }
        if (selectFields.length() > 0) {
            selectFields = selectFields.substring(0, selectFields.length() - 1);
        }
        StringBuffer selSQL = new StringBuffer("select distinct ");
        selSQL.append(selectFields).append(" from ");
        for (int i = 0; i < sectionIds.length; i++) {
            if (tableNames[i] == null) {
                // Table of ignored section type
                continue;
            }
            selSQL.append(" ").append(tableNames[i]).append(",");
        }
        // selSQL = new StringBuffer(selSQL.toString().substring(0,selSQL.toString().length()-1));
        selSQL.append(
                " teflow_wkf_process, tpma_staffbasic s, teflow_wkf_process_trace ptrace, teflow_wkf_detail d where ")
                .append(tableNames[0].trim() + ".request_no=teflow_wkf_process.request_no ");
        selSQL.append(" and teflow_wkf_process.request_staff_code = s.staff_code ");
        selSQL.append(" and teflow_wkf_process.flow_id = ptrace.flow_id and teflow_wkf_process.request_no = ptrace.request_no ");
        selSQL.append(" and (teflow_wkf_process.flow_id = d.flow_id) and (teflow_wkf_process.node_id = d.node_id) ");

        if (conditionVo.getOrgId() != null && !"".equals(conditionVo.getOrgId())) {
            selSQL.append(" and COALESCE(teflow_wkf_process.org_id, s.org_id) = '" + conditionVo.getOrgId() + "' ");
        }
        if (conditionVo.getRequestNo() != null && !"".equals(conditionVo.getRequestNo())) {
            selSQL.append(" and teflow_wkf_process.request_no='" + conditionVo.getRequestNo() + "' ");
        }
        if (conditionVo.getRequestStaffCode() != null && !"".equals(conditionVo.getRequestStaffCode())) {
            selSQL.append(" and teflow_wkf_process.request_staff_code='").append(conditionVo.getRequestStaffCode())
                    .append("' ");
        }
        if (conditionVo.getStatus() != null && !"".equals(conditionVo.getStatus())) {
            selSQL.append(" and teflow_wkf_process.status='").append(conditionVo.getStatus()).append("' ");
        }
        if (conditionVo.getNodeType() != null && !"".equals(conditionVo.getNodeType())) {
            selSQL.append(" and d.node_type = '").append(conditionVo.getNodeType()).append("' ");
        }
        if (conditionVo.getRequestStaffCode() != null && !"".equals(conditionVo.getRequestStaffCode())) {
            selSQL.append(" and teflow_wkf_process.request_staff_code='").append(conditionVo.getRequestStaffCode())
                    .append("' ");
        }
        if (conditionVo.getBeginSubmissionDate() != null && !"".equals(conditionVo.getBeginSubmissionDate())) {
            selSQL.append(" and teflow_wkf_process.submission_date>='").append(conditionVo.getBeginSubmissionDate())
                    .append(" 00:00:00' ");
        }
        if (conditionVo.getEndSubmissionDate() != null && !"".equals(conditionVo.getEndSubmissionDate())) {
            selSQL.append(" and teflow_wkf_process.submission_date<='").append(conditionVo.getEndSubmissionDate())
                    .append(" 23:59:59' ");
        }

        if (!(StringUtil.isEmptyString(conditionVo.getBeginCompleteDate()))) {
            selSQL.append(" and ptrace.handle_date >= '").append(conditionVo.getBeginCompleteDate() + " 00:00:00' ");
        }
        if (!(StringUtil.isEmptyString(conditionVo.getEndCompleteDate()))) {
            selSQL.append(" and ptrace.handle_date <= '").append(conditionVo.getEndCompleteDate() + " 23:59:59' ");
        }
        if (!StringUtil.isEmptyString(conditionVo.getEndCompleteDate())
                || !StringUtil.isEmptyString(conditionVo.getBeginCompleteDate())) {
            selSQL.append(" and teflow_wkf_process.status = '04' "); // initial 00 submitted 01 Approved 02 Rejected 03
                                                                     // completed 04
            selSQL.append(" and ptrace.handle_type = '05' "); // Initial 00 Submit 01 WithDraw 02 Approve 03 Reject 04
                                                              // Completed 05Invite Expert 06Expert advise 07
        }

        String selWhere = "";
        if (sectionIds.length > 1) {
            for (int i = 1; i < sectionIds.length; i++) {
                if (tableNames[i] == null) {
                    // Table of ignored section type
                    continue;
                }
                selWhere = selWhere + " and " + tableNames[0].trim() + ".request_no=" + tableNames[i].trim()
                        + ".request_no ";
            }
            // if(!"".equals(selWhere)){
            // selWhere = " where "+selWhere.substring(0,selWhere.length()-4);
            // }
        }
        selSQL.append(selWhere);
        // System.out.println("-----SQL:"+selSQL.toString());
        result = dbManager.query(selSQL.toString());
        return result;
    }

    public TableDataListVO getExportTableDataList(String formSystemId, String[] sectionIds,
            WorkFlowProcessVO conditionVo) throws Exception {
        if (sectionIds == null || sectionIds.length == 0) {
            return null;
        }
        String inSectionIds = "";
        for (int i = 0; i < sectionIds.length; i++) {
            inSectionIds += "'" + sectionIds[i] + "',";
        }
        inSectionIds = inSectionIds.substring(0, inSectionIds.length() - 1);

        String sql = "select * from teflow_form_section where form_system_id = " + formSystemId
                + " and section_type = '01' and section_id in (" + inSectionIds + ")";

        StringBuffer subSql = new StringBuffer(
                "select distinct a.request_no from teflow_wkf_process a, teflow_wkf_process_trace b, tpma_staffbasic c, teflow_wkf_detail d "
                        + "where a.request_no = b.request_no and a.request_staff_code = c.staff_code "
                        + "and (a.flow_id = d.flow_id) and (a.node_id = d.node_id) ");
        if (conditionVo.getRequestNo() != null && !"".equals(conditionVo.getRequestNo())) {
            subSql.append(" and a.request_no = '" + conditionVo.getRequestNo() + "'");
        }
        if (conditionVo.getOrgId() != null && !"".equals(conditionVo.getOrgId())) {
            subSql.append(" and c.org_id = '" + conditionVo.getOrgId() + "'");
        }
        if (conditionVo.getRequestStaffCode() != null && !"".equals(conditionVo.getRequestStaffCode())) {
            subSql.append(" and a.request_staff_code = '" + conditionVo.getRequestStaffCode() + "'");
        }
        if (conditionVo.getStatus() != null && !"".equals(conditionVo.getStatus())) {
            subSql.append(" and a.status = '" + conditionVo.getStatus() + "'");
        }
        if (conditionVo.getNodeType() != null && !"".equals(conditionVo.getNodeType())) {
            subSql.append(" and d.node_type = '" + conditionVo.getNodeType() + "'");
        }
        if (conditionVo.getBeginSubmissionDate() != null && !"".equals(conditionVo.getBeginSubmissionDate())) {
            subSql.append(" and a.submission_date >= '" + conditionVo.getBeginSubmissionDate() + " 00:00:00'");
        }
        if (conditionVo.getEndSubmissionDate() != null && !"".equals(conditionVo.getEndSubmissionDate())) {
            subSql.append(" and a.submission_date <= '" + conditionVo.getEndSubmissionDate() + " 23:59:59'");
        }
        if (!(StringUtil.isEmptyString(conditionVo.getBeginCompleteDate()))) {
            subSql.append(" and b.handle_date >= '" + conditionVo.getBeginCompleteDate() + " 00:00:00'");
        }
        if (!(StringUtil.isEmptyString(conditionVo.getEndCompleteDate()))) {
            subSql.append(" and b.handle_date <= '" + conditionVo.getEndCompleteDate() + " 23:59:59'");
        }
        if (!StringUtil.isEmptyString(conditionVo.getEndCompleteDate())
                || !StringUtil.isEmptyString(conditionVo.getBeginCompleteDate())) {
            subSql.append(" and a.status = '04' and b.handle_type = '05'");
        }

        Connection conn = dbManager.getJDBCConnection();
        Statement stmt = null;
        try {
            List tableNames = new ArrayList();
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                tableNames.add(rs.getString("table_name"));
            }
            rs.close();
            if (tableNames.size() == 0) {
                return null;
            }
            TableDataListVO tableDataList = new TableDataListVO();
            for (int i = 0; i < tableNames.size(); i++) {
                sql = "select * from " + tableNames.get(i) + " where request_no in (" + subSql.toString()
                        + ") order by request_no, id";
                rs = stmt.executeQuery(sql);
                tableDataList.loadData(rs);
                rs.close();
            }
            return tableDataList;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }

    public TableDataListVO getExportTableDataListAdvance(String formSystemId, String[] sectionIds, Collection queryList)
            throws Exception {
        if (sectionIds == null || sectionIds.length == 0) {
            return null;
        }
        String inSectionIds = "";
        for (int i = 0; i < sectionIds.length; i++) {
            inSectionIds += "'" + sectionIds[i] + "',";
        }
        inSectionIds = inSectionIds.substring(0, inSectionIds.length() - 1);

        String sql = "select * from teflow_form_section where form_system_id = " + formSystemId
                + " and section_type = '01' and section_id in (" + inSectionIds + ")";

        String conditionStr = processConditionForCommonQuery(queryList, formSystemId);
        String subSql = "";
        if (!"".equals(conditionStr)) {
            subSql = "select distinct a.request_no from teflow_wkf_process a where " + conditionStr;
        } else {
            subSql = "select distinct a.request_no from teflow_wkf_process a";
        }

        Connection conn = dbManager.getJDBCConnection();
        Statement stmt = null;
        try {
            List tableNames = new ArrayList();
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                tableNames.add(rs.getString("table_name"));
            }
            rs.close();
            if (tableNames.size() == 0) {
                return null;
            }
            TableDataListVO tableDataList = new TableDataListVO();
            for (int i = 0; i < tableNames.size(); i++) {
                sql = "select * from " + tableNames.get(i) + " where request_no in (" + subSql
                        + ") order by request_no, id";
                rs = stmt.executeQuery(sql);
                tableDataList.loadData(rs);
                rs.close();
            }
            return tableDataList;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }

    /**
     * IT0958 Advance Query Form
     * 
     * @param queryList
     * @param formSystemId
     * @return
     * @throws DAOException
     */
    public Collection getCommonQueryList(Collection queryList, String formSystemId, String expFormId)
            throws DAOException {
        StringBuffer sql = new StringBuffer(
                "select a.request_no,a.flow_id,a.submission_date,a.receiving_date,a.previous_processor,a.request_staff_code,a.current_processor,a.status,c.form_name,c.form_system_id,a.node_id,d.node_type,d.node_name ");
        sql.append(" from teflow_wkf_process a,teflow_wkf_define b,teflow_form c,teflow_wkf_detail d where 1=1 ");
        if (formSystemId != null && !"".equals(formSystemId)) {
            sql.append(" and b.form_system_id=" + formSystemId);
        }
        String condtionStr = processConditionForCommonQuery(queryList, formSystemId);
        if (!"".equals(condtionStr)) {
            sql.append(" and (").append(condtionStr).append(")");
        }
        sql.append(" and a.flow_id=b.flow_id and b.form_system_id = c.form_system_id and a.flow_id = d.flow_id and a.node_id = d.node_id order by submission_date desc");

        // System.out.println(sql.toString());

        Collection list = dbManager.query(sql.toString());
        if (list == null && list.size() == 0)
            return null;
        Collection result = new ArrayList();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            HashMap map = (HashMap) it.next();

            if (expFormId != null && expFormId.length() > 0) {
                if (expFormId.indexOf((String) map.get("REQUEST_NO") + ",") == -1)
                    continue;
            }

            WorkFlowProcessVO vo = convertMapToVo(map);

            // Set special field
            // vo.setTipField(this.getSpecialField(vo.getRequestNo(), 1));
            vo.setTipField(this.getSpecialField(vo.getRequestNo(), 1, vo.getFormSystemId()));
            // Set highlight field
            // vo.setHighlightField(this.getSpecialField(vo.getRequestNo(), 2));
            vo.setHighlightField(this.getSpecialField(vo.getRequestNo(), 2, vo.getFormSystemId()));
            vo.setHighlightField2(this.getSpecialField(vo.getRequestNo(), 3, vo.getFormSystemId()));
            vo.setHighlightField3(this.getSpecialField(vo.getRequestNo(), 4, vo.getFormSystemId()));

            result.add(vo);
        }
        return result;
    }

    /**
     * Advanced Query By Form Type
     * 
     * @param queryList
     * @param formType
     * @return
     * @throws DAOException
     */
    public Collection getCommonQueryListByFormType(Collection queryList, String formType, String expFormId)
            throws DAOException {
        String SQL_1 = "select distinct form_system_id from teflow_form_type_field_mapping where (form_type_id = ?) order by form_system_id";

        String SQL_2 = "select * from teflow_form_type_field_mapping "
                + "where (form_type_id = ?) and (form_system_id = ?) and (type_field_id = ?) order by type_field_id";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(SQL_1);
            int i = 1;
            stm.setString(i++, formType);
            ResultSet rs = stm.executeQuery();
            Vector formIds = new Vector();
            while (rs.next()) {
                formIds.add(rs.getString("form_system_id"));
            }
            stm.close();

            Collection resultList = new ArrayList();
            stm = conn.prepareStatement(SQL_2);
            stm.setString(1, formType);

            // For each form
            for (int j = 0; j < formIds.size(); j++) {
                String formSystemId = (String) formIds.get(j);
                stm.setString(2, formSystemId);

                Iterator it = queryList.iterator();
                Collection conditionList = new ArrayList();
                boolean allFieldsMapping = true;
                while (it.hasNext()) {
                    NodeConditionVO nodeCondition = (NodeConditionVO) it.next();
                    stm.setString(3, nodeCondition.getFieldId());
                    rs = stm.executeQuery();
                    if (!rs.next()) {
                        allFieldsMapping = false;
                        break;
                    }
                    NodeConditionVO condition = new NodeConditionVO();
                    condition.setSectionId(rs.getString("section_id"));
                    condition.setFieldId(rs.getString("field_id"));
                    condition.setCompareType(nodeCondition.getCompareType());
                    condition.setCompareValue(nodeCondition.getCompareValue());
                    condition.setLogicType(nodeCondition.getLogicType());
                    condition.setFieldLabel(nodeCondition.getFieldLabel());
                    condition.setCompareLabel(nodeCondition.getCompareLabel());
                    condition.setIsFunction(nodeCondition.getIsFunction());
                    condition.setFiledType(nodeCondition.getFiledType());
                    conditionList.add(condition);
                }
                if (!allFieldsMapping) {
                    continue;
                }

                Collection resultListByForm = this.getCommonQueryList(conditionList, formSystemId, expFormId);
                resultList.addAll(resultListByForm);
            }

            return resultList;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new DAOException(ex);
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public Collection getExportExcelForCommonQuery(Collection queryList, String formSystemId, String[] sectionIds,
            String[] fields) throws DAOException {
        return getExportExcelForCommonQuery(queryList, formSystemId, sectionIds, fields, "", null);
    }

    /**
     * 
     * @param queryList
     * @param formSystemId
     * @param sectionIds
     * @param fields
     * @param queryType
     * @return
     * @throws DAOException
     */
    public Collection getExportExcelForCommonQuery(Collection queryList, String formSystemId, String[] sectionIds,
            String[] fields, String queryType, String currentStaffCode) throws DAOException {
        Collection result = new ArrayList();
        String[] tableNames = new String[sectionIds.length];
        for (int i = 0; i < sectionIds.length; i++) {
            Collection tempList = dbManager
                    .query("select table_name, section_type from teflow_form_section where form_system_id="
                            + formSystemId + " and section_id='" + sectionIds[i] + "'");
            HashMap map = (HashMap) tempList.iterator().next();
            String sectionType = (String) map.get("SECTION_TYPE");
            if ("00".equals(sectionType) || "01".equals(sectionType)) {
                // Ignore table(01) and attachment(00) section
                continue;
            }
            tableNames[i] = (String) map.get("TABLE_NAME");
        }
        String selectFields = "";
        String[] temps = new String[2];
        for (int i = 0; i < fields.length; i++) {
            temps = StringUtil.split(fields[i], "||");
            for (int j = 0; j < sectionIds.length; j++) {
                if (sectionIds[j].equals(temps[0])) {
                    temps[0] = tableNames[j];
                    break;
                }
            }
            if (temps[0] == null) {
                // Field of ignored section type
                // System.out.println("Ignored field: " + temps[1]);
                continue;
            }
            selectFields = selectFields + " " + temps[0] + "." + temps[1] + ",";
        }
        if (selectFields.length() > 0) {
            selectFields = selectFields.substring(0, selectFields.length() - 1);
        }
        StringBuffer selSQL = new StringBuffer("select distinct ");
        selSQL.append(selectFields).append(" from ");
        for (int i = 0; i < sectionIds.length; i++) {
            if (tableNames[i] == null) {
                // Table of ignored section type
                continue;
            }
            selSQL.append(" ").append(tableNames[i]).append(",");
        }
        // selSQL = new StringBuffer(selSQL.toString().substring(0,selSQL.toString().length()-1));
        selSQL.append(" teflow_wkf_process a where ").append(tableNames[0].trim() + ".request_no=a.request_no ");

        if ("01".equals(queryType)) { // query his requested forms
            selSQL.append(" and (a.request_staff_code='" + currentStaffCode + "' or a.submit_staff_code='"
                    + currentStaffCode + "') ");
        } else if ("02".equals(queryType)) { // query his processed forms
            selSQL.append(" and a.request_no in ( select request_no from teflow_wkf_process_trace where current_processor like '%"
                    + currentStaffCode + "%') ");
        }

        String conditionStr = processConditionForCommonQuery(queryList, formSystemId);
        if (!"".equals(conditionStr)) {
            // System.out.println("conditionStr: " + conditionStr);
            selSQL.append(" and (").append(conditionStr).append(")");
        }

        String selWhere = "";
        if (sectionIds.length > 1) {
            for (int i = 1; i < sectionIds.length; i++) {
                if (tableNames[i] == null) {
                    // Table of ignored section type
                    continue;
                }
                selWhere = selWhere + " and " + tableNames[0].trim() + ".request_no=" + tableNames[i].trim()
                        + ".request_no ";
            }
        }
        selSQL.append(selWhere);

        // System.out.println("-----SQL: "+selSQL.toString());
        result = dbManager.query(selSQL.toString());
        return result;
    }

    private String processConditionForCommonQuery(Collection queryList, String formSystemId) throws DAOException {
        StringBuffer condtionStr = new StringBuffer("");
        if (queryList != null && queryList.size() > 0) {
            Iterator it = queryList.iterator();
            String fieldValue = "";
            String logicValue = "";
            String fieldStr = "";
            while (it.hasNext()) {
                NodeConditionVO vo = (NodeConditionVO) it.next();
                String tableName = "";
                Collection tempList = dbManager
                        .query("select table_name from teflow_form_section where form_system_id=" + formSystemId
                                + " and section_id='" + vo.getSectionId() + "' ");
                tableName = (String) ((HashMap) tempList.iterator().next()).get("TABLE_NAME");
                // if("like".equals(vo.getCompareType().toLowerCase().trim()) ||
                // "not like".equals(vo.getCompareType().toLowerCase()) ||
                // "=".equals(vo.getCompareType().toLowerCase())){
                if ("like".equals(vo.getCompareType().toLowerCase().trim())
                        || "not like".equals(vo.getCompareType().toLowerCase())) {
                    fieldValue = "'%" + StringUtil.FormatSQL(vo.getCompareValue()) + "%'";
                    // vo.setCompareType("like");
                } else {
                    fieldValue = "'" + StringUtil.FormatSQL(vo.getCompareValue()) + "'";
                }
                if ("datetime".equals(vo.getFiledType())) {
                    fieldStr = "convert(datetime,convert(varchar," + vo.getFieldId() + ",101),101)";
                }
                if ("01".equals(vo.getLogicType())) {
                    logicValue = " and ";
                } else if ("02".equals(vo.getLogicType())) {
                    logicValue = " or ";
                } else {
                    logicValue = " and ";
                }
                if ("".equals(condtionStr.toString())) {
                    if ("empty".equals(vo.getCompareType().toLowerCase().trim())) {// 空值的情况
                        condtionStr.append("  a.request_no in (select request_no from " + tableName + " where rtrim("
                                + vo.getFieldId() + ") = '' or " + vo.getFieldId() + " is null)");
                    } else if ("notempty".equals(vo.getCompareType().toLowerCase().trim())) {// 非空值的情况
                        condtionStr.append("  a.request_no in (select request_no from " + tableName + " where rtrim("
                                + vo.getFieldId() + ") <> '' and " + vo.getFieldId() + " is not null)");
                    } else {
                        condtionStr.append("  a.request_no in (select request_no from " + tableName + " where "
                                + vo.getFieldId() + " " + vo.getCompareType() + " " + fieldValue + ")");
                    }
                } else {
                    if ("empty".equals(vo.getCompareType().toLowerCase().trim())) {// 空值的情况
                        condtionStr.append(" " + logicValue + " a.request_no in (select request_no from " + tableName
                                + " where rtrim(" + vo.getFieldId() + ") = '' or " + vo.getFieldId() + " is null)");
                    } else if ("notempty".equals(vo.getCompareType().toLowerCase().trim())) {// 非空值的情况
                        condtionStr.append(" " + logicValue + " a.request_no in (select request_no from " + tableName
                                + " where rtrim(" + vo.getFieldId() + ") <> '' and " + vo.getFieldId()
                                + " is not null)");
                    } else {
                        condtionStr.append(" " + logicValue + " a.request_no in (select request_no from " + tableName
                                + " where " + vo.getFieldId() + " " + vo.getCompareType() + " " + fieldValue + ")");
                    }
                }
                fieldValue = "";
                logicValue = "";
            }
        }
        return condtionStr.toString();
    }

    /**
     * 获取指定用户所申请的记录
     * 
     * @param staffCode
     * @return
     * @throws DAOException
     */
    public Collection getWaitingForManualFormList(WorkFlowProcessVO queryVo) throws DAOException {
        StringBuffer sql = new StringBuffer(
                "select a.request_no,a.flow_id,a.submission_date,a.receiving_date,a.previous_processor,a.current_processor,a.status,c.form_name,c.form_system_id,c.form_type ");
        sql.append(" from teflow_wkf_process a,teflow_wkf_define b,teflow_form c where 1=1 ");
        if (queryVo.getRequestNo() != null && !"".equals(queryVo.getRequestNo())) {
            sql.append(" and a.request_no='").append(queryVo.getRequestNo()).append("' ");
        }
        if (queryVo.getFormType() != null && !"".equals(queryVo.getFormType())) {
            sql.append(" and c.form_type='").append(queryVo.getFormType()).append("' ");
        }
        if (queryVo.getCurrentProcessor() != null && !"".equals(queryVo.getCurrentProcessor())) {
            sql.append(" and ','+rtrim(a.current_processor)+',' like '%,").append(queryVo.getCurrentProcessor())
                    .append(",%' ");
        }

        if (queryVo.getBeginSubmissionDate() != null && !"".equals(queryVo.getBeginSubmissionDate())) {
            if (queryVo.getEndSubmissionDate() != null && !"".equals(queryVo.getEndSubmissionDate())) {
                sql.append(" and a.submission_date between '" + queryVo.getBeginSubmissionDate() + " 00:00:00' and '"
                        + queryVo.getEndSubmissionDate() + " 23:59:59' ");
            } else {
                sql.append(" and a.submission_date>='" + queryVo.getBeginSubmissionDate() + " 00:00:00' ");
            }
        } else {
            if (queryVo.getEndSubmissionDate() != null && !"".equals(queryVo.getEndSubmissionDate())) {
                sql.append(" and a.submission_date<='" + queryVo.getEndSubmissionDate() + " 23:59:59' ");
            }
        }

        if (queryVo.getFormSystemId() > 0) {
            sql.append(" and b.form_system_id=" + queryVo.getFormSystemId());
        }

        sql.append(" and a.flow_id=b.flow_id and b.form_system_id = c.form_system_id order by submission_date desc");

        Collection list = dbManager.query(sql.toString());
        if (list == null && list.size() == 0)
            return null;
        Collection result = new ArrayList();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            HashMap map = (HashMap) it.next();
            WorkFlowProcessVO vo = convertMapToVo(map);
            result.add(vo);
        }
        return result;
    }

    /**
     * IT0958 : add filter : requested by 获取指定用户所申请的记录
     * 
     * @param staffCode
     * @return
     * @throws DAOException
     */
    /**
     * public Collection getInquiryFormList(WorkFlowProcessVO queryVo)throws DAOException{ StringBuffer sql = new
     * StringBuffer(
     * "select a.request_no,a.flow_id,a.submission_date,a.request_staff_code,a.receiving_date,a.previous_processor,a.current_processor,a.status,a.inprocess,c.form_name,c.form_system_id,a.node_id,a.is_deputy,a.origin_processor "
     * ); sql.append(" from teflow_wkf_process a,teflow_wkf_define b,teflow_form c where 1=1 ");
     * 
     * int index = 0; int parameterNum = 0; if(queryVo.getRequestNo()!=null && !"".equals(queryVo.getRequestNo())){
     * parameterNum++; } if(queryVo.getStatus()!=null && !"".equals(queryVo.getStatus())){ parameterNum++; }
     * if(queryVo.getRequestStaffCode()!=null && !"".equals(queryVo.getRequestStaffCode())){ parameterNum++; }
     * if(queryVo.getBeginSubmissionDate()!=null && !"".equals(queryVo.getBeginSubmissionDate())){ parameterNum++; }
     * if(queryVo.getEndSubmissionDate()!=null && !"".equals(queryVo.getEndSubmissionDate())){ parameterNum++; }
     * if(queryVo.getFormSystemId()>0){ parameterNum++; } if(queryVo.getInProcess()!=null &&
     * !"".equals(queryVo.getInProcess())){ parameterNum++; }
     * 
     * int[] dataType = new int[parameterNum]; Object[] parameters = new Object[parameterNum];
     * 
     * if(queryVo.getRequestNo()!=null && !"".equals(queryVo.getRequestNo())){ sql.append(" and a.request_no=? ");
     * dataType[index] = DataType.VARCHAR; parameters[index] = queryVo.getRequestNo(); index++; }
     * if(queryVo.getFormType()!=null && !"".equals(queryVo.getFormType())){
     * sql.append(" and c.form_type in (").append(queryVo.getFormType()).append(") "); } if(queryVo.getStatus()!=null &&
     * !"".equals(queryVo.getStatus())){ sql.append(" and a.status=? "); dataType[index] = DataType.VARCHAR;
     * parameters[index] = queryVo.getStatus(); index++; } //IT0958 DS-007 Begin if(queryVo.getRequestStaffCode()!=null
     * && !"".equals(queryVo.getRequestStaffCode())){ sql.append(" and a.request_staff_code=? "); dataType[index] =
     * DataType.VARCHAR; parameters[index] = queryVo.getRequestStaffCode(); index++; } //IT0958 DS-007 End
     * if(queryVo.getBeginSubmissionDate()!=null && !"".equals(queryVo.getBeginSubmissionDate())){
     * if(queryVo.getEndSubmissionDate()!=null && !"".equals(queryVo.getEndSubmissionDate())){ sql.append(
     * " and Convert(varchar(10),a.submission_date,101)>=? and Convert(varchar(10),a.submission_date,101)<=?");
     * dataType[index] = DataType.DATE; try{ parameters[index] = new
     * java.sql.Date(StringUtil.stringToSqlDate(queryVo.getBeginSubmissionDate
     * ()+" 00:00:00","MM/dd/yyyy HH:mm:ss").getTime()); }catch(Exception e){} index++;
     * 
     * dataType[index] = DataType.DATE; try{ parameters[index] = new
     * java.sql.Date(StringUtil.stringToSqlDate(queryVo.getEndSubmissionDate
     * ()+" 23:59:59","MM/dd/yyyy HH:mm:ss").getTime()); }catch(Exception e){} index++; }else{ sql.append(
     * " and a.submission_date>=? "); dataType[index] = DataType.DATE; try{ parameters[index] = new
     * java.sql.Date(StringUtil
     * .stringToSqlDate(queryVo.getBeginSubmissionDate()+" 00:00:00","MM/dd/yyyy HH:mm:ss").getTime()); }catch(Exception
     * e){} index++; } }else{ if(queryVo.getEndSubmissionDate()!=null && !"".equals(queryVo.getEndSubmissionDate())){
     * sql.append( " and a.submission_date<=? "); dataType[index] = DataType.DATE; try{ parameters[index] = new
     * java.sql.
     * Date(StringUtil.stringToSqlDate(queryVo.getEndSubmissionDate()+" 23:59:59","MM/dd/yyyy HH:mm:ss").getTime());
     * }catch(Exception e){} index++; } }
     * 
     * if(queryVo.getFormSystemId()>0){ sql.append(" and b.form_system_id=?"); dataType[index] = DataType.INT;
     * parameters[index] = ""+queryVo.getFormSystemId(); index++; }
     * 
     * //IT0973 BEGIN if(queryVo.getInProcess()!=null && !"".equals(queryVo.getInProcess())){
     * sql.append(" and a.inprocess=? "); dataType[index] = DataType.CHAR; parameters[index] = queryVo.getInProcess();
     * index++; } //IT0973 END
     * 
     * if(queryVo.getOrgId()!=null && !"".equals(queryVo.getOrgId())){
     * sql.append(" and c.org_id in ("+queryVo.getOrgId()+") "); }
     * 
     * sql.append(" and a.flow_id=b.flow_id and b.form_system_id = c.form_system_id order by submission_date desc");
     * 
     * //Collection list = dbManager.query(sql.toString()); Collection list =
     * dbManager.query(sql.toString(),parameters,dataType);
     * 
     * if(list==null && list.size()==0) return null; Collection result = new ArrayList(); Iterator it = list.iterator();
     * while(it.hasNext()){ HashMap map = (HashMap)it.next(); WorkFlowProcessVO vo = convertMapToVo(map);
     * result.add(vo); } return result; }
     **/

    /**
     * 获取指定用户所申请的记录
     * 
     * @param staffCode
     * @return
     * @throws DAOException
     */
    public Collection getAppliedFormListByStaff(StaffVO staff, WorkFlowProcessVO queryVo) throws DAOException {
        StringBuffer sql = new StringBuffer(
                "select a.request_no,a.flow_id,a.submission_date,a.receiving_date,a.request_staff_code,a.submit_staff_code," +
                "a.previous_processor,a.current_processor,a.status,a.open_flag, c.form_name,c.form_type,c.form_system_id," +
                "a.node_id,a.origin_processor,a.is_deputy,d.node_type,d.node_name,d.limited_hours,overtime = -1," +
                "a.inprocess_staff_code,a.urgent_level,a.invited_expert,inprocess = '-1' ");
        sql.append(" from teflow_wkf_process a " +
        		"inner join teflow_wkf_define b on a.flow_id = b.flow_id " +
        		"inner join teflow_form c on b.form_system_id = c.form_system_id " +
        		"inner join teflow_wkf_detail d on a.flow_id = d.flow_id and a.node_id = d.node_id " +
        		"where " );
        if (queryVo.getExpertAdviceFlag()) {
            // The field is used to indicate doing query by team scope.
            sql.append("(a.org_id = '" + staff.getOrgId() + "') and (a.team_code = '" + staff.getTeamCode() + "') ");
            if (queryVo.getTipField() != null) {
                // The id of forms that can be queried by team scope.
                String formIds = "";
                for (String formId : queryVo.getTipField()) {
                    formIds += "," + formId;
                }
                formIds = formIds.substring(1);
                sql.append("and (b.form_system_id in (" + formIds + ")) ");
            }
        } else {
            sql.append("(a.request_staff_code='" + staff.getStaffCode() + "' or a.submit_staff_code='"
                    + staff.getStaffCode() + "') ");
        }

        int index = 0;
        int parameterNum = 0;
        if (queryVo.getRequestNo() != null && !"".equals(queryVo.getRequestNo())) {
            parameterNum++;
        }
        if (queryVo.getFormType() != null && !"".equals(queryVo.getFormType())) {
            parameterNum++;
        }
        if (queryVo.getStatus() != null && !"".equals(queryVo.getStatus())) {
            parameterNum++;
        }
        if (queryVo.getSubmittedBy() != null && !"".equals(queryVo.getSubmittedBy())) {
            parameterNum++;
        }
        if (queryVo.getFormSystemId() > 0) {
            parameterNum++;
        }
        if (queryVo.getBeginSubmissionDate() != null && !"".equals(queryVo.getBeginSubmissionDate())) {
            parameterNum++;
        }
        if (queryVo.getEndSubmissionDate() != null && !"".equals(queryVo.getEndSubmissionDate())) {
            parameterNum++;
        }

        int[] dataType = new int[parameterNum];
        Object[] parameters = new Object[parameterNum];

        if (queryVo.getRequestNo() != null && !"".equals(queryVo.getRequestNo())) {
            sql.append(" and a.request_no=? ");
            dataType[index] = DataType.VARCHAR;
            parameters[index] = queryVo.getRequestNo();
            index++;
        }
        if (queryVo.getFormType() != null && !"".equals(queryVo.getFormType())) {
            sql.append(" and c.form_type=? ");
            dataType[index] = DataType.VARCHAR;
            parameters[index] = queryVo.getFormType();
            index++;
        }
        if (queryVo.getStatus() != null && !"".equals(queryVo.getStatus())) {
            sql.append(" and a.status=? ");
            dataType[index] = DataType.VARCHAR;
            parameters[index] = queryVo.getStatus();
            index++;
        }
        if (queryVo.getSubmittedBy() != null && !"".equals(queryVo.getSubmittedBy())) {
            sql.append(" and a.submit_staff_code=? ");
            dataType[index] = DataType.VARCHAR;
            parameters[index] = queryVo.getSubmittedBy();
            index++;
        }

        if (queryVo.getFormSystemId() > 0) {
            sql.append(" and b.form_system_id=? ");
            dataType[index] = DataType.INT;
            parameters[index] = "" + queryVo.getFormSystemId();
            index++;
        }

        if (queryVo.getBeginSubmissionDate() != null && !"".equals(queryVo.getBeginSubmissionDate())) {
            if (queryVo.getEndSubmissionDate() != null && !"".equals(queryVo.getEndSubmissionDate())) {
                sql.append(" and Convert(varchar(10),a.submission_date,101)>=? and Convert(varchar(10),a.submission_date,101)<=? ");
                dataType[index] = DataType.DATE;
                try {
                    parameters[index] = new java.sql.Date(StringUtil.stringToDate(
                            queryVo.getBeginSubmissionDate() + " 00:00:00", "MM/dd/yyyy hh:mm:ss").getTime());
                } catch (Exception e) {
                }

                index++;
                dataType[index] = DataType.DATE;
                try {
                    parameters[index] = new java.sql.Date(StringUtil.stringToDate(
                            queryVo.getEndSubmissionDate() + " 23:59:59", "MM/dd/yyyy hh:mm:ss").getTime());
                } catch (Exception e) {
                }
                index++;
            } else {
                sql.append(" and Convert(varchar(10),a.submission_date,101)>=? ");
                dataType[index] = DataType.DATE;
                try {
                    parameters[index] = new java.sql.Date(StringUtil.stringToDate(
                            queryVo.getBeginSubmissionDate() + " 00:00:00", "MM/dd/yyyy hh:mm:ss").getTime());
                } catch (Exception e) {
                }
                index++;
            }
        } else {
            if (queryVo.getEndSubmissionDate() != null && !"".equals(queryVo.getEndSubmissionDate())) {
                sql.append(" and Convert(varchar(10),a.submission_date,101)<=? ");
                dataType[index] = DataType.DATE;
                try {
                    parameters[index] = new java.sql.Date(StringUtil.stringToDate(
                            queryVo.getEndSubmissionDate() + " 23:59:59", "MM/dd/yyyy hh:mm:ss").getTime());
                } catch (Exception e) {
                }
                index++;
            }
        }
        
        /**********************************************************************************************/
        
        sql.append("union all " +
        		   "SELECT a.request_no, a.flow_id, a.Submission_date,a.receiving_date, a.request_staff_code, a.submit_staff_code, " +
        		   "a.previous_processor, a.current_processor, a.status, a.open_flag, c.form_name, c.form_type," +
        		   "c.form_system_id,a.node_id, a.origin_processor, a.is_deputy,d.node_type,d.node_name,d.limited_hours," +
        		   "overtime = (CASE when d.limited_hours>0 then Round(((d.limited_hours * 60 - DATEDIFF(minute, a.receiving_date, GETDATE()))/60.0),2) else 0 end),a.inprocess_staff_code,a.urgent_level,a.invited_expert,inprocess = isnull(a.inprocess, '0') ");
        sql.append("from teflow_wkf_process a " +
				"INNER JOIN teflow_wkf_define b ON a.flow_id = b.flow_id  "
		        + "INNER JOIN teflow_form c ON b.form_system_id = c.form_system_id " 
		        + "INNER JOIN teflow_wkf_detail d ON a.flow_id = d.flow_id AND a.node_id = d.node_id "
		        + "INNER JOIN tpma_staffbasic e ON a.request_staff_code = e.staff_code "
		        + " where (','+rtrim(a.current_processor)+',' like '%," + staff.getStaffCode()
		        + ",%' or ','+rtrim(a.invited_expert)+',' like '%[,/]" + staff.getStaffCode()
		        + "[,/]%') and (a.status<>'03' or (a.status='03' and a.node_id<>'0'))  and a.status<>'00'  " 
		        + " order by inprocess,urgent_level desc,open_flag,submission_date desc"); // 排除掉本人申请而被reject回来的form,以及还没有“submit”的form

        Collection list = dbManager.query(sql.toString(), parameters, dataType);

        // Collection list = dbManager.query(sql.toString());
        if (list == null && list.size() == 0)
            return null;
        Collection result = new ArrayList();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            HashMap map = (HashMap) it.next();
            WorkFlowProcessVO vo = convertMapToVo(map);

            // Set special field
            // vo.setTipField(this.getSpecialField(vo.getRequestNo(), 1));
            vo.setTipField(this.getSpecialField(vo.getRequestNo(), 1, vo.getFormSystemId()));
            // Set highlight field
            // vo.setHighlightField(this.getSpecialField(vo.getRequestNo(), 2));
            vo.setHighlightField(this.getSpecialField(vo.getRequestNo(), 2, vo.getFormSystemId()));

            result.add(vo);
        }
        return result;
    }

    /**
     * IT0958 : add filter "Requested By" 获取指定用户所要处理的form列表(包括需要当前用户给予建议的form)
     * 
     * @param staffCode
     * @return
     * @throws DAOException
     */
    public Collection getForDealFormListByStaff(String staffCode, WorkFlowProcessVO queryVo) throws DAOException {
        StringBuffer sql = new StringBuffer(
                "SELECT a.request_no, a.flow_id, a.Submission_date,a.request_staff_code, a.receiving_date, a.previous_processor, a.current_processor, a.status,a.open_flag, c.form_name, c.form_type,d.limited_hours,'overtime' = CASE when d.limited_hours>0 then Round(((d.limited_hours * 60 - DATEDIFF(minute, a.receiving_date, GETDATE()))/60.0),2) else  0 end,c.form_system_id,a.inprocess_staff_code,a.urgent_level,a.invited_expert,a.is_deputy,a.origin_processor,a.node_id, ");
        sql.append("isnull(a.inprocess, '0') as inprocess, ");
        sql.append("case when a.inprocess_staff_code='" + staffCode
                + "' then '0' when ','+rtrim(a.invited_expert)+',' like '%[,/]" + staffCode
                + "[,/]%' then '0' when inprocess_staff_code<>'' then '2' else '0' end as somebody_locked, ");
        sql.append("case a.invited_expert when '' then 'b' else 'a' end as has_invited ");
        sql.append("from teflow_wkf_process a INNER JOIN teflow_wkf_define b ON a.flow_id = b.flow_id INNER JOIN "
                + " teflow_form c ON b.form_system_id = c.form_system_id INNER JOIN teflow_wkf_detail d ON a.flow_id = d.flow_id AND a.node_id = d.node_id "
                + "INNER JOIN tpma_staffbasic e ON a.request_staff_code = e.staff_code "
                + " where (','+rtrim(a.current_processor)+',' like '%," + staffCode
                + ",%' or ','+rtrim(a.invited_expert)+',' like '%[,/]" + staffCode
                + "[,/]%') and (a.status<>'03' or (a.status='03' and a.node_id<>'0'))  and a.status<>'00'  "); // 排除掉本人申请而被reject回来的form,以及还没有“submit”的form

        int index = 0;
        int parameterNum = 0;
        if (queryVo.getRequestNo() != null && !"".equals(queryVo.getRequestNo())) {
            parameterNum++;
        }
        if (queryVo.getFormType() != null && !"".equals(queryVo.getFormType())) {
            parameterNum++;
        }
        if (queryVo.getOrgId() != null && !"".equals(queryVo.getOrgId())) {
            parameterNum++;
        }
        if (queryVo.getTeamCode() != null && !"".equals(queryVo.getTeamCode())) {
            parameterNum++;
        }
        if (queryVo.getRequestStaffCode() != null && !"".equals(queryVo.getRequestStaffCode())) {
            parameterNum++;
        }
        if (queryVo.getBeginSubmissionDate() != null && !"".equals(queryVo.getBeginSubmissionDate())) {
            parameterNum++;
        }
        if (queryVo.getEndSubmissionDate() != null && !"".equals(queryVo.getEndSubmissionDate())) {
            parameterNum++;
        }
        if (queryVo.getFormSystemId() > 0) {
            parameterNum++;
        }
        if (queryVo.getNodeType() != null && !"".equals(queryVo.getNodeType())) {
            parameterNum++;
        }

        int[] dataType = new int[parameterNum];
        Object[] parameters = new Object[parameterNum];

        if (queryVo.getRequestNo() != null && !"".equals(queryVo.getRequestNo())) {
            sql.append(" and a.request_no=? ");
            dataType[index] = DataType.VARCHAR;
            parameters[index] = queryVo.getRequestNo();
            index++;
        }
        if (queryVo.getFormType() != null && !"".equals(queryVo.getFormType())) {
            sql.append(" and c.form_type=? ");
            dataType[index] = DataType.VARCHAR;
            parameters[index] = queryVo.getFormType();
            index++;
        }
        if (queryVo.getOrgId() != null && !"".equals(queryVo.getOrgId())) {
            sql.append(" and e.org_id = ? ");
            dataType[index] = DataType.VARCHAR;
            parameters[index] = queryVo.getOrgId();
            index++;
        }
        if (queryVo.getTeamCode() != null && !"".equals(queryVo.getTeamCode())) {
            sql.append(" and e.team_code = ? ");
            dataType[index] = DataType.VARCHAR;
            parameters[index] = queryVo.getTeamCode();
            index++;
        }
        if (queryVo.getRequestStaffCode() != null && !"".equals(queryVo.getRequestStaffCode())) {
            sql.append(" and a.request_staff_code=? ");
            dataType[index] = DataType.VARCHAR;
            parameters[index] = queryVo.getRequestStaffCode();
            index++;
        }
        if (queryVo.getBeginSubmissionDate() != null && !"".equals(queryVo.getBeginSubmissionDate())) {
            if (queryVo.getEndSubmissionDate() != null && !"".equals(queryVo.getEndSubmissionDate())) {
                sql.append(" and Convert(varchar(10),a.submission_date,101)>=? and Convert(varchar(10),a.submission_date,101)<=?");
                dataType[index] = DataType.DATE;
                try {
                    parameters[index] = new java.sql.Date(StringUtil.stringToSqlDate(
                            queryVo.getBeginSubmissionDate() + " 00:00:00", "MM/dd/yyyy HH:mm:ss").getTime());
                } catch (Exception e) {
                }
                index++;

                dataType[index] = DataType.DATE;
                try {
                    parameters[index] = new java.sql.Date((StringUtil.stringToSqlDate(queryVo.getEndSubmissionDate()
                            + " 23:59:59", "MM/dd/yyyy HH:mm:ss")).getTime());
                    System.out.println("Date:" + parameters[index]);
                } catch (Exception e) {
                }
                index++;
            } else {
                sql.append(" and Convert(varchar(10),a.submission_date,101)>=? ");
                dataType[index] = DataType.DATE;
                try {
                    parameters[index] = new java.sql.Date(StringUtil.stringToSqlDate(
                            queryVo.getBeginSubmissionDate() + " 00:00:00", "MM/dd/yyyy HH:mm:ss").getTime());
                } catch (Exception e) {
                }
                index++;
            }
        } else {
            if (queryVo.getEndSubmissionDate() != null && !"".equals(queryVo.getEndSubmissionDate())) {
                sql.append(" and Convert(varchar(10),a.submission_date,101)<=? ");
                dataType[index] = DataType.DATE;
                try {
                    parameters[index] = new java.sql.Date(StringUtil.stringToSqlDate(
                            queryVo.getEndSubmissionDate() + " 23:59:59", "MM/dd/yyyy HH:mm:ss").getTime());
                } catch (Exception e) {
                }
                index++;
            }
        }

        if (queryVo.getFormSystemId() > 0) {
            sql.append(" and b.form_system_id=?");
            dataType[index] = DataType.INT;
            parameters[index] = "" + queryVo.getFormSystemId();
            index++;
        }

        if (queryVo.getNodeType() != null && !"".equals(queryVo.getNodeType())) {
            sql.append(" and d.node_type = ? ");
            dataType[index] = DataType.VARCHAR;
            parameters[index] = queryVo.getNodeType();
            index++;
        }

        // sql.append(" order by a.urgent_level desc, a.open_flag, a.inprocess desc, a.submission_date desc");
        // Move open_flag to first, for hiding 'hold'forms. 2013-6-19
        sql.append(" order by somebody_locked, a.urgent_level desc, a.open_flag, inprocess desc, has_invited, a.submission_date desc");

        // Collection list = dbManager.query(sql.toString());
        Collection list = dbManager.query(sql.toString(), parameters, dataType);

        if (list == null && list.size() == 0)
            return null;
        Collection result = new ArrayList();
        Iterator it = list.iterator();
        ParamConfigHelper param = ParamConfigHelper.getInstance();
        while (it.hasNext()) {
            HashMap map = (HashMap) it.next();
            WorkFlowProcessVO vo = convertMapToVo(map);
            boolean voReturnFlag = true;
            String lastInvitedDate = null;
            if (vo.getInvitedExpert() != null && !"".equals(vo.getInvitedExpert())
                    && vo.getInvitedExpert().indexOf(staffCode) >= 0) {
                vo.setExpertAdviceFlag(true);
                // the staff code is in the invited expert list.
                lastInvitedDate = getLastInvitedDate(vo.getRequestNo());
                boolean theStaffAdvised = isStaffAdvised(staffCode, vo.getRequestNo(), lastInvitedDate);
                vo.setExpertReplied(theStaffAdvised);

                if (!queryVo.isExpertMultiReply() && theStaffAdvised)
                    voReturnFlag = false;
                // current processor == advised staff == the staff.
                if (vo.getCurrentProcessor() != null && vo.getCurrentProcessor().equals(staffCode)) {
                    voReturnFlag = true;
                    vo.setExpertAdviceFlag(false);
                }

            } else {
                vo.setExpertAdviceFlag(false);

                if (vo.getInvitedExpert() != null && !"".equals(vo.getInvitedExpert())) {
                    lastInvitedDate = getLastInvitedDate(vo.getRequestNo());
                    boolean expertAdvised = isExpertAdvised(vo.getRequestNo(), lastInvitedDate);
                    vo.setExpertReplied(expertAdvised);
                }
            }

            // filter out the pay-by-check forms
            if ("CHECK_ONLY".equals(queryVo.getNodeName())) {
                String[] ids = param.getParamValue("invoice_form_ids").split(";");
                String sql2 = "SELECT field_05_2 as paymode, @vname as vender_name FROM @vinfo, teflow_@fid_05 t05 WHERE d.request_no='"
                        + vo.getRequestNo() + "' and d.request_no=t05.request_no";

                int i;
                boolean isCheckPay = false;
                for (i = 0; i < ids.length; i++) {
                    if (ids[i].equals("" + vo.getFormSystemId())) {
                        sql2 = sql2.replaceAll("@vname", param.getParamValue("payment_payee_name").split(";")[i]);
                        sql2 = sql2
                                .replaceAll("@vinfo", param.getParamValue("payment_vender_info_table").split(";")[i]);
                        sql2 = sql2.replaceAll("@fid", ids[i]);
                        // System.out.println(sql2);
                        Collection list2 = dbManager.query(sql2);
                        Iterator it2 = list2.iterator();
                        HashMap map2 = (HashMap) it2.next();

                        vo.setCcStaffs((String) map2.get("VENDER_NAME"));
                        if ("03".equals((String) map2.get("PAYMODE")))
                            isCheckPay = true;
                        break;
                    }
                }
                if (i == ids.length || !isCheckPay)
                    continue;
            }
            // Set special field
            // vo.setTipField(this.getSpecialField(vo.getRequestNo(), 1));
            vo.setTipField(this.getSpecialField(vo.getRequestNo(), 1, vo.getFormSystemId()));
            // Set highlight field
            // vo.setHighlightField(this.getSpecialField(vo.getRequestNo(), 2));
            vo.setHighlightField(this.getSpecialField(vo.getRequestNo(), 2, vo.getFormSystemId()));
            vo.setHighlightField2(this.getSpecialField(vo.getRequestNo(), 3, vo.getFormSystemId()));
            vo.setHighlightField3(this.getSpecialField(vo.getRequestNo(), 4, vo.getFormSystemId()));

            if (voReturnFlag) {
                result.add(vo);
            }
        }

        return result;
    }

    public String getLastInvitedDate(String requestNo) throws DAOException {

        String invitedDate = null;

        String sql = "SELECT TOP 1 handle_date FROM teflow_wkf_process_trace "
                + "WHERE (handle_type = '06') AND (request_no = '" + requestNo + "') " + "ORDER BY handle_date DESC ";

        Collection rs = dbManager.query(sql);
        if (rs.size() > 0) {
            Iterator it = rs.iterator();
            if (it.hasNext()) {
                HashMap map = (HashMap) it.next();
                invitedDate = FieldUtil.convertSafeString(map, "HANDLE_DATE");
            }
        }

        return invitedDate;
    }

    private boolean isStaffAdvised(String staffCode, String requestNo, String receivingDate) throws DAOException {

        if (receivingDate == null)
            return false;

        String sql = "select count(*) from teflow_wkf_process_trace "
                + "where handle_type = '07' and current_processor = '" + staffCode + "' and request_no = '" + requestNo
                + "' " + " and handle_date >= convert(datetime, '" + receivingDate + "', 21) ";
        int count = dbManager.getRecordCount(sql);
        return count > 0;
        // The recivingDate is used for: if there are multiple invite. the staff replied the first invite, but do not
        // reply the second invite.
    }

    private boolean isExpertAdvised(String requestNo, String receivingDate) throws DAOException {

        if (receivingDate == null)
            return false;

        // if there are more than one expert advised then the status is advised.
        String sql = "select count(*) from teflow_wkf_process_trace " + " where handle_type = '07' and request_no = '"
                + requestNo + "' " + " and handle_date >= convert(datetime, '" + receivingDate + "', 21) ";
        int count = dbManager.getRecordCount(sql);
        return count > 0;
    }

    /**
     * 获取指定用户已经处理过的form列表
     * 
     * @param staffCode
     * @param queryVo
     * @return
     * @throws DAOException
     */
    public Collection getMyDealedForm(String staffCode, WorkFlowProcessVO queryVo) throws DAOException {
        StringBuffer sql = new StringBuffer(
                "SELECT a.request_no, a.flow_id, a.Submission_date,a.request_staff_code, a.receiving_date, a.previous_processor, a.current_processor, a.status, c.form_name, c.form_type,d.limited_hours, 'overtime' = CASE when d.limited_hours>0 then  Round(((d.limited_hours * 60 - DATEDIFF(minute, a.receiving_date, GETDATE()))/60.0),2) else  0 end,c.form_system_id,a.inprocess,a.inprocess_staff_code,a.node_id,d.node_type,d.node_name ");
        sql.append("  from teflow_wkf_process a INNER JOIN teflow_wkf_define b ON a.flow_id = b.flow_id INNER JOIN "
                + " teflow_form c ON b.form_system_id = c.form_system_id INNER JOIN teflow_wkf_detail d ON a.flow_id = d.flow_id  AND a.node_id = d.node_id "
                + " where a.request_no in ( select request_no from teflow_wkf_process_trace where current_processor = '"
                + staffCode + "' ");
        if (queryVo.getBeginHandleDate() != null && !"".equals(queryVo.getEndHandleDate())) {
            sql.append(" and handle_date >='").append(queryVo.getBeginHandleDate()).append(" 00:00:00.000' ");
        }

        if (queryVo.getBeginSubmissionDate() != null && !"".equals(queryVo.getBeginSubmissionDate())) {
            sql.append(" and handle_date >='").append(queryVo.getBeginSubmissionDate()).append(" 00:00:00.000' ");
        }// 优化措施：handle_date必然在submit_date之后，可以缩小范围

        if (queryVo.getEndHandleDate() != null && !"".equals(queryVo.getEndHandleDate())) {
            sql.append(" and handle_date <='").append(queryVo.getEndHandleDate()).append(" 23:59:59.999' ");
        }
        sql.append(" )");
        int index = 0;
        int parameterNum = 0;
        if (queryVo.getRequestNo() != null && !"".equals(queryVo.getRequestNo())) {
            parameterNum++;
        }
        if (queryVo.getFormType() != null && !"".equals(queryVo.getFormType())) {
            parameterNum++;
        }
        if (queryVo.getBeginSubmissionDate() != null && !"".equals(queryVo.getBeginSubmissionDate())) {
            parameterNum++;
        }
        if (queryVo.getEndSubmissionDate() != null && !"".equals(queryVo.getEndSubmissionDate())) {
            parameterNum++;
        }
        if (queryVo.getFormSystemId() > 0) {
            parameterNum++;
        }

        int[] dataType = new int[parameterNum];
        Object[] parameters = new Object[parameterNum];

        if (queryVo.getRequestNo() != null && !"".equals(queryVo.getRequestNo())) {
            sql.append(" and a.request_no=? ");
            dataType[index] = DataType.VARCHAR;
            parameters[index] = queryVo.getRequestNo();
            index++;
        }
        if (queryVo.getFormType() != null && !"".equals(queryVo.getFormType())) {
            sql.append(" and c.form_type=? ");
            dataType[index] = DataType.VARCHAR;
            parameters[index] = queryVo.getFormType();
            index++;
        }
        if (queryVo.getBeginSubmissionDate() != null && !"".equals(queryVo.getBeginSubmissionDate())) {
            if (queryVo.getEndSubmissionDate() != null && !"".equals(queryVo.getEndSubmissionDate())) {
                sql.append(" and Convert(varchar(10),a.submission_date,101)>=? and Convert(varchar(10),a.submission_date,101)<=?");
                dataType[index] = DataType.DATE;
                try {
                    parameters[index] = new java.sql.Date(StringUtil.stringToSqlDate(
                            queryVo.getBeginSubmissionDate() + " 00:00:00", "MM/dd/yyyy hh:mm:ss").getTime());
                } catch (Exception e) {
                }
                index++;

                dataType[index] = DataType.DATE;
                try {
                    parameters[index] = new java.sql.Date(StringUtil.stringToSqlDate(
                            queryVo.getEndSubmissionDate() + " 23:59:59", "MM/dd/yyyy hh:mm:ss").getTime());
                } catch (Exception e) {
                }
                index++;
            } else {
                sql.append(" and Convert(varchar(10),a.submission_date,101)>=? ");
                dataType[index] = DataType.DATE;
                try {
                    parameters[index] = new java.sql.Date(StringUtil.stringToSqlDate(
                            queryVo.getBeginSubmissionDate() + " 00:00:00", "MM/dd/yyyy hh:mm:ss").getTime());
                } catch (Exception e) {
                }
                index++;
            }
        } else {
            if (queryVo.getEndSubmissionDate() != null && !"".equals(queryVo.getEndSubmissionDate())) {
                sql.append(" and Convert(varchar(10),a.submission_date,101)<=? ");
                dataType[index] = DataType.DATE;
                try {
                    parameters[index] = new java.sql.Date(StringUtil.stringToSqlDate(
                            queryVo.getEndSubmissionDate() + " 23:59:59", "MM/dd/yyyy hh:mm:ss").getTime());
                } catch (Exception e) {
                }
                index++;
            }
        }

        if (queryVo.getFormSystemId() > 0) {
            sql.append(" and b.form_system_id=?");
            dataType[index] = DataType.INT;
            parameters[index] = "" + queryVo.getFormSystemId();
            index++;
        }

        sql.append(" order by a.submission_date desc");
        // Collection list = dbManager.query(sql.toString());
        // System.out.println(sql.toString());
        Collection list = dbManager.query(sql.toString(), parameters, dataType);

        if (list == null && list.size() == 0)
            return null;
        Collection result = new ArrayList();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            HashMap map = (HashMap) it.next();
            WorkFlowProcessVO vo = convertMapToVo(map);

            // Set special field
            // vo.setTipField(this.getSpecialField(vo.getRequestNo(), 1));
            vo.setTipField(this.getSpecialField(vo.getRequestNo(), 1, vo.getFormSystemId()));
            // Set highlight field
            // vo.setHighlightField(this.getSpecialField(vo.getRequestNo(), 2));
            vo.setHighlightField(this.getSpecialField(vo.getRequestNo(), 2, vo.getFormSystemId()));

            result.add(vo);
        }
        return result;
    }

    /**
     * 获取指定用户已经处于超时状态的form列表
     * 
     * @param staffCode
     * @param queryVo
     * @return
     * @throws DAOException
     */
    public Collection getAllOvertimeForm(WorkFlowProcessVO queryVo) throws DAOException {
        StringBuffer sql = new StringBuffer(
                "SELECT a.request_no, a.flow_id, a.Submission_date,a.request_staff_code, a.receiving_date, a.previous_processor,d.node_name,a.current_processor, a.status, c.form_name, c.form_type,d.limited_hours, str(Round(((d.limited_hours * 60 - DATEDIFF(minute, a.receiving_date, GETDATE()))/60.0),2),10,2) as overtime,c.form_system_id,a.inprocess,a.inprocess_staff_code, a.open_flag ");
        sql.append("  from teflow_wkf_process a INNER JOIN teflow_wkf_define b ON a.flow_id = b.flow_id INNER JOIN "
                + " teflow_form c ON b.form_system_id = c.form_system_id INNER JOIN teflow_wkf_detail d ON a.flow_id = d.flow_id  AND a.node_id = d.node_id, tpma_staffbasic e "
                + " WHERE a.request_staff_code = e.staff_code and a.status<>'00' and a.status<>'03' and Round(((d.limited_hours * 60 - DATEDIFF(minute, a.receiving_date, GETDATE()))/60.0),2) < 0 and d.limited_hours>0");

        int index = 0;
        int parameterNum = 0;
        if (!"".equals(queryVo.getRequestNo()) && queryVo.getRequestNo() != null) {
            parameterNum++;
        }
        if (queryVo.getBeginSubmissionDate() != null && !"".equals(queryVo.getBeginSubmissionDate())) {
            parameterNum++;
        }
        if (queryVo.getEndSubmissionDate() != null && !"".equals(queryVo.getEndSubmissionDate())) {
            parameterNum++;
        }
        if (queryVo.getFormSystemId() > 0) {
            parameterNum++;
        }
        if (queryVo.getOrgId() != null && !queryVo.getOrgId().equals("")) {
            parameterNum++;
        }
        if (queryVo.getRequestStaffCode() != null && !"".equals(queryVo.getRequestStaffCode())) {
            parameterNum++;
        }

        int[] dataType = new int[parameterNum];
        Object[] parameters = new Object[parameterNum];

        if (!"".equals(queryVo.getRequestNo()) && queryVo.getRequestNo() != null) {
            sql.append(" and a.request_no=? ");
            dataType[index] = DataType.VARCHAR;
            parameters[index] = "" + queryVo.getRequestNo();
            index++;
        }
        if (queryVo.getFormType() != null && !"".equals(queryVo.getFormType())) {
            sql.append(" and c.form_type in (" + queryVo.getFormType() + ")");
        }
        if (queryVo.getBeginSubmissionDate() != null && !"".equals(queryVo.getBeginSubmissionDate())) {
            if (queryVo.getEndSubmissionDate() != null && !"".equals(queryVo.getEndSubmissionDate())) {
                sql.append(" and Convert(varchar(10),a.submission_date,101)>=? and Convert(varchar(10),a.submission_date,101)<=?");
                dataType[index] = DataType.DATE;
                try {
                    parameters[index] = StringUtil.stringToSqlDate(queryVo.getBeginSubmissionDate() + " 00:00:00",
                            "MM/dd/yyyy hh:mm:ss");
                } catch (Exception e) {
                }
                index++;

                dataType[index] = DataType.DATE;
                try {
                    parameters[index] = StringUtil.stringToSqlDate(queryVo.getEndSubmissionDate() + " 23:59:59",
                            "MM/dd/yyyy hh:mm:ss");
                } catch (Exception e) {
                }
                index++;
            } else {
                sql.append(" and Convert(varchar(10),a.submission_date,101)>=? ");
                dataType[index] = DataType.DATE;
                try {
                    parameters[index] = StringUtil.stringToSqlDate(queryVo.getBeginSubmissionDate() + " 00:00:00",
                            "MM/dd/yyyy hh:mm:ss");
                } catch (Exception e) {
                }
                index++;
            }
        } else {
            if (queryVo.getEndSubmissionDate() != null && !"".equals(queryVo.getEndSubmissionDate())) {
                sql.append(" and Convert(varchar(10),a.submission_date,101)<=? ");
                dataType[index] = DataType.DATE;
                try {
                    parameters[index] = StringUtil.stringToSqlDate(queryVo.getEndSubmissionDate() + " 23:59:59",
                            "MM/dd/yyyy hh:mm:ss");
                } catch (Exception e) {
                }
                index++;
            }
        }

        if (queryVo.getFormSystemId() > 0) {
            sql.append(" and b.form_system_id=? ");
            dataType[index] = DataType.INT;
            parameters[index] = "" + queryVo.getFormSystemId();
            index++;
        }

        if (queryVo.getOrgId() != null && !queryVo.getOrgId().equals("")) {
            sql.append(" and e.org_id = ? ");
            dataType[index] = DataType.VARCHAR;
            parameters[index] = queryVo.getOrgId();
            index++;
        }

        if (queryVo.getRequestStaffCode() != null && !"".equals(queryVo.getRequestStaffCode())) {
            sql.append(" and a.request_staff_code=? ");
            dataType[index] = DataType.VARCHAR;
            parameters[index] = queryVo.getRequestStaffCode();
            index++;
        }

        sql.append(" order by overtime asc ");
        // System.out.println("sql:" + sql.toString());
        // Collection list = dbManager.query(sql.toString());
        Collection list = dbManager.query(sql.toString(), parameters, dataType);

        if (list == null && list.size() == 0)
            return null;
        Collection result = new ArrayList();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            HashMap map = (HashMap) it.next();
            WorkFlowProcessVO vo = convertMapToVo(map);
            if ("2".equals(map.get("OPEN_FLAG")))
                vo.setRemainTime("0.0");

            // Set special field
            // vo.setTipField(this.getSpecialField(vo.getRequestNo(), 1));
            vo.setTipField(this.getSpecialField(vo.getRequestNo(), 1, vo.getFormSystemId()));
            // Set highlight field
            // vo.setHighlightField(this.getSpecialField(vo.getRequestNo(), 2));
            vo.setHighlightField(this.getSpecialField(vo.getRequestNo(), 2, vo.getFormSystemId()));

            result.add(vo);
        }
        return result;
    }

    /**
     * 获取已经结束（完成）的form列表
     * 
     * @param staffCode
     * @param queryVo
     * @return
     * @throws DAOException
     */
    public Collection getClosedForm(WorkFlowProcessVO queryVo) throws DAOException {
        StringBuffer sql = new StringBuffer(
                "SELECT a.request_no, a.flow_id, a.Submission_date,a.request_staff_code, c.form_name, c.form_type,c.form_system_id,a.inprocess,a.inprocess_staff_code,d.handle_date,d.current_processor ");
        sql.append("  from teflow_wkf_process a INNER JOIN teflow_wkf_define b ON a.flow_id = b.flow_id INNER JOIN "
                + " teflow_form c ON b.form_system_id = c.form_system_id INNER JOIN teflow_wkf_process_trace d ON a.flow_id = d.flow_id AND a.request_no=d.request_no AND d.handle_type='05' "
                + " where a.status='04' ");
        if (queryVo.getRequestNo() != null && !"".equals(queryVo.getRequestNo())) {
            sql.append(" and a.request_no='" + queryVo.getRequestNo() + "'");
        }
        if (queryVo.getFormType() != null && !"".equals(queryVo.getFormType())) {
            sql.append(" and c.form_type='" + queryVo.getFormType() + "'");
        }
        if (queryVo.getBeginSubmissionDate() != null && !"".equals(queryVo.getBeginSubmissionDate())) {
            if (queryVo.getEndSubmissionDate() != null && !"".equals(queryVo.getEndSubmissionDate())) {
                sql.append(" and a.submission_date between '" + queryVo.getBeginSubmissionDate() + " 00:00:00' and '"
                        + queryVo.getEndSubmissionDate() + " 23:59:59' ");
            } else {
                sql.append(" and a.submission_date>='" + queryVo.getBeginSubmissionDate() + " 00:00:00' ");
            }
        } else {
            if (queryVo.getEndSubmissionDate() != null && !"".equals(queryVo.getEndSubmissionDate())) {
                sql.append(" and a.submission_date<='" + queryVo.getEndSubmissionDate() + " 23:59:59' ");
            }
        }
        if (queryVo.getFormSystemId() > 0) {
            sql.append(" and b.form_system_id=" + queryVo.getFormSystemId());
        }
        sql.append(" order by a.submission_date desc");
        Collection list = dbManager.query(sql.toString());
        if (list == null && list.size() == 0)
            return null;
        Collection result = new ArrayList();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            HashMap map = (HashMap) it.next();
            WorkFlowProcessVO vo = convertMapToVo(map);
            result.add(vo);
        }
        return result;
    }

    protected WorkFlowProcessVO convertMapToVo(HashMap map) {
        WorkFlowProcessVO vo = new WorkFlowProcessVO();
        vo.setRequestNo(FieldUtil.convertSafeString(map, "REQUEST_NO"));

        vo.setSubmissionDateStr(FieldUtil.convertSafeString(map, "SUBMISSION_DATE"));
        vo.setReceivingDateStr(FieldUtil.convertSafeString(map, "RECEIVING_DATE"));

        if (map.get("LIMITED_HOURS") != null && !"".equals((String) map.get("LIMITED_HOURS"))) {
            vo.setLimitedHours(Double.parseDouble((String) map.get("LIMITED_HOURS")));
        }
        if (map.get("OVERTIME") != null && !"".equals(map.get("OVERTIME"))) {
            if (Double.parseDouble((String) map.get("OVERTIME")) != 0) {
                String currentDate = StringUtil.getDateStr(new Date(), "yyyy-MM-dd HH:mm:ss");

                // double intervalHours = OverDueUtil.computeInvertalHours(vo.getReceivingDateStr());
                // double d = (double)((int)Math.round((Double.parseDouble((String)map.get("OVERTIME")) +
                // intervalHours)*100)/100.00);
                // vo.setRemainTime(""+d);
                if (vo.getReceivingDateStr() != null && !"".equals(vo.getReceivingDateStr())) {
                    vo.setRemainTime(""
                            + OverDueUtil.computeOverdueHours(vo.getReceivingDateStr(), currentDate,
                                    vo.getLimitedHours()));
                }
            } else {
                vo.setRemainTime((String) map.get("OVERTIME"));
            }
        }
        vo.setCurrentProcessor(FieldUtil.convertSafeString(map, "CURRENT_PROCESSOR"));
        vo.setNodeName(DataConvertUtil.convertISOToGBK(FieldUtil.convertSafeString(map, "NODE_NAME")));
        vo.setNodeId(FieldUtil.convertSafeString(map, "NODE_ID"));
        vo.setNodeType(FieldUtil.convertSafeString(map, "NODE_TYPE"));
        vo.setFormName(DataConvertUtil.convertISOToGBK(FieldUtil.convertSafeString(map, "FORM_NAME")));
        vo.setFormSystemId(FieldUtil.convertSafeInt(map, "FORM_SYSTEM_ID", -1));
        vo.setFormType(FieldUtil.convertSafeString(map, "FORM_TYPE"));
        vo.setPreviousProcessor(FieldUtil.convertSafeString(map, "PREVIOUS_PROCESSOR"));
        vo.setStatus(FieldUtil.convertSafeString(map, "STATUS"));
        vo.setInProcess(FieldUtil.convertSafeString(map, "INPROCESS"));
        vo.setInProcessStaffCode(FieldUtil.convertSafeString(map, "INPROCESS_STAFF_CODE"));
        vo.setHandleDateStr(FieldUtil.convertSafeString(map, "HANDLE_DATE"));
        vo.setRequestStaffCode(FieldUtil.convertSafeString(map, "REQUEST_STAFF_CODE"));
        vo.setSubmittedBy(FieldUtil.convertSafeString(map, "SUBMIT_STAFF_CODE"));
        vo.setOpenFlag(FieldUtil.convertSafeString(map, "OPEN_FLAG", "0"));
        vo.setIsUrgent(FieldUtil.convertSafeString(map, "URGENT_LEVEL", "1"));
        vo.setInvitedExpert(FieldUtil.convertSafeString(map, "INVITED_EXPERT", ""));
        vo.setIsDeputy(FieldUtil.convertSafeString(map, "IS_DEPUTY"));
        vo.setOriginProcessor(FieldUtil.convertSafeString(map, "ORIGIN_PROCESSOR"));
        vo.setCcStaffs(FieldUtil.convertSafeString(map, "CC_STAFFS"));
        vo.setRemaineApproversNum(FieldUtil.convertSafeInt(map, "REMAIN_APPROVERS_NUM", 0));
        vo.setOrgId(FieldUtil.convertSafeString(map, "ORG_ID"));
        vo.setTeamCode(FieldUtil.convertSafeString(map, "TEAM_CODE"));
        return vo;
    }

    public WorkFlowProcessDAO(IDBManager dbManager) {
        super(dbManager);
    }

    public void processStep(WorkFlowItemVO node) throws DAOException {

    }

    public int delete(BaseVO arg0) throws DAOException {
        // TODO Auto-generated method stub
        return 0;
    }

    public int save(BaseVO arg0) throws DAOException {
        // TODO Auto-generated method stub
        return 0;
    }

    public int update(BaseVO arg0) throws DAOException {
        // TODO Auto-generated method stub
        return 0;
    }

    public void adjustProcessorLog(AdjustProcessorLogVO vo) throws DAOException {
        String sql = "insert into teflow_wkf_process_adjust_log(request_no,node_id,original_processor,adjust_to_processor,operate_staff_code,operate_date) select '"
                + vo.getRequestNo()
                + "',node_id,current_processor,'"
                + vo.getAdjustToProcessor()
                + "','"
                + vo.getOperateStaffCode()
                + "',getdate() from teflow_wkf_process where request_no='"
                + vo.getRequestNo() + "'";
        dbManager.executeUpdate(sql);
    }

    public void adjustProcessor(String requestNo, String nodeId, String adjustToProcessor, String originProcessor)
            throws DAOException {
        String setDeputy = originProcessor.trim().equals("") ? "origin_processor = '', is_deputy = '',"
                : "origin_processor = '" + originProcessor + "', is_deputy = '1',";
        int remainApprovers = adjustToProcessor.split(",").length;
        String sql = "update teflow_wkf_process set current_processor='" + adjustToProcessor
                + "', remain_approvers_num=" + remainApprovers + ", " + setDeputy
                + " inprocess='0', inprocess_staff_code='' where request_no='" + requestNo + "' ";
        dbManager.executeUpdate(sql);
    }

    public void adjustProcessor(String requestNo, String adjustToProcessor) throws DAOException {
        int remainApprovers = adjustToProcessor.split(",").length;
        String sql = "update teflow_wkf_process set current_processor='" + adjustToProcessor
                + "', remain_approvers_num=" + remainApprovers
                + ", inprocess='0', inprocess_staff_code='' where request_no='" + requestNo + "' ";
        dbManager.executeUpdate(sql);
    }

    /**
     * 对指定form进行锁定与解锁操作
     * 
     * @param requestNo
     * @param lockUnlock
     *            1---锁定操作，0----解锁操作
     * @throws DAOException
     */
    public void lockForm(String requestNo, String lockUnlock, String staffCode) throws DAOException {
        String updateSQL = "update teflow_wkf_process set inprocess='" + lockUnlock + "',inprocess_staff_code='"
                + staffCode + "' where request_no='" + requestNo + "'";
        dbManager.executeUpdate(updateSQL);
    }

    /**
     * 获取指定form的处理实例记录
     * 
     * @param requestNo
     * @return
     * @throws DAOException
     */
    public WorkFlowProcessVO getProcessVO(String requestNo) throws DAOException {
        String sql = "select a.*, b.form_system_id, c.node_type, c.node_name, c.limited_hours, c.approve_alias, c.reject_alias, d.form_type "
                + "from teflow_wkf_process a, teflow_wkf_define b, teflow_wkf_detail c, teflow_form d "
                + "where (a.flow_id = b.flow_id) and (a.flow_id = c.flow_id) and (a.node_id = c.node_id) and (b.form_system_id = d.form_system_id) "
                + "and (a.request_no = '" + requestNo + "')";

        Collection list = dbManager.query(sql);
        if (list == null || list.size() == 0)
            return null;
        HashMap map = (HashMap) list.iterator().next();
        WorkFlowProcessVO vo = new WorkFlowProcessVO();
        vo.setFlowId(FieldUtil.convertSafeInt(map, "FLOW_ID", -1));
        vo.setRequestNo(requestNo);
        vo.setFormSystemId(FieldUtil.convertSafeInt(map, "FORM_SYSTEM_ID", 0));
        vo.setFormType(FieldUtil.convertSafeString(map, "FORM_TYPE"));
        vo.setInProcess(FieldUtil.convertSafeString(map, "INPROCESS", "0"));
        vo.setCurrentProcessor(FieldUtil.convertSafeString(map, "CURRENT_PROCESSOR"));
        vo.setInProcessStaffCode(FieldUtil.convertSafeString(map, "INPROCESS_STAFF_CODE"));
        vo.setRequestStaffCode(FieldUtil.convertSafeString(map, "REQUEST_STAFF_CODE"));
        vo.setNodeId(FieldUtil.convertSafeString(map, "NODE_ID", "-1"));
        vo.setNodeType(FieldUtil.convertSafeString(map, "NODE_TYPE"));
        vo.setNodeName(FieldUtil.convertSafeString(map, "NODE_NAME"));
        vo.setLimitedHours(FieldUtil.convertSafeDouble(map, "LIMITED_HOURS", 0));
        vo.setApproveAlias(FieldUtil.convertSafeString(map, "APPROVE_ALIAS"));
        vo.setRejectAlias(FieldUtil.convertSafeString(map, "REJECT_ALIAS"));
        vo.setOpenFlag(FieldUtil.convertSafeString(map, "OPEN_FLAG"));
        vo.setStatus(FieldUtil.convertSafeString(map, "STATUS"));
        vo.setHasNextNode(FieldUtil.convertSafeString(map, "HAS_NEXT_NODE", "0"));
        vo.setInvitedExpert(FieldUtil.convertSafeString(map, "INVITED_EXPERT"));
        vo.setSubmittedBy(FieldUtil.convertSafeString(map, "SUBMIT_STAFF_CODE"));
        try {
            java.util.Date date = this.datetimeFormatDB.parse(FieldUtil.convertSafeString(map, "SUBMISSION_DATE"));
            vo.setSubmissionDate(date);
            vo.setSubmissionDateStr(this.datetimeFormat.format(date));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        vo.setCcStaffs(FieldUtil.convertSafeString(map, "CC_STAFFS"));
        vo.setIsDeputy(FieldUtil.convertSafeString(map, "IS_DEPUTY"));
        vo.setOrgId(FieldUtil.convertSafeString(map, "ORG_ID"));
        vo.setTeamCode(FieldUtil.convertSafeString(map, "TEAM_CODE"));
        vo.setOriginProcessor(FieldUtil.convertSafeString(map, "ORIGIN_PROCESSOR"));
        vo.setRemaineApproversNum(FieldUtil.convertSafeInt(map, "REMAIN_APPROVERS_NUM", 0));
        return vo;
    }

    /**
     * 获取指定form的处理轨迹记录集合
     * 
     * @param requestNo
     * @return
     * @throws DAOException
     */
    @Deprecated
    public Collection getProcessTraceList(String requestNo) throws DAOException {
        Collection resultList = new ArrayList();
        // String querySQL =
        // "select a.*,b.node_name from teflow_wkf_process_trace a,teflow_wkf_detail b where a.flow_id=b.flow_id and a.node_id=b.node_id and a.request_no = '"
        // + requestNo + "' order by a.process_id";
        String querySQL = "select a.* from teflow_wkf_process_trace a where  a.request_no = '" + requestNo
                + "' order by a.process_id";
        // querySQL = querySQL + " and a.staff_code = b.staff_code";
        Collection list = dbManager.query(querySQL);
        if (list != null && list.size() > 0) {
            Iterator it = list.iterator();
            while (it.hasNext()) {
                WorkFlowProcessTraceVO vo = new WorkFlowProcessTraceVO();
                HashMap map = (HashMap) it.next();
                vo.setNodeName(FieldUtil.convertSafeString(map, "NODE_NAME"));
                vo.setRequestNo(requestNo);
                vo.setHandleStaffCode(FieldUtil.convertSafeString(map, "CURRENT_PROCESSOR"));
                vo.setHandleDateStr(FieldUtil.convertSafeString(map, "HANDLE_DATE"));
                vo.setHandleType(FieldUtil.convertSafeString(map, "HANDLE_TYPE"));
                vo.setHandleComments(DataConvertUtil.convertISOToGBK(FieldUtil
                        .convertSafeString(map, "HANDLE_COMMENTS")));
                vo.setFilePathName(DataConvertUtil.convertISOToGBK(FieldUtil.convertSafeString(map, "ATTACH_FILE")));
                resultList.add(vo);
            }
        }
        return resultList;
    }

    /**
     * 获取指定form的处理轨迹记录集合
     * 
     * @param requestNo
     * @param filterAuto
     *            : if true, filter the auto-flow/optional nodes
     * @return
     * @throws DAOException
     */
    public Collection getProcessTraceList(String requestNo, boolean filterAuto) throws DAOException {
        Collection resultList = new ArrayList();

        String querySQL = "select a.*, b.approve_alias, b.reject_alias from teflow_wkf_process_trace a"
                + " left join teflow_wkf_detail b on (a.flow_id = b.flow_id) and (a.node_id = b.node_id)"
                + " where a.request_no = '" + requestNo + "' order by a.process_id";

        if (filterAuto) {
            querySQL = "select a.*, b.approve_alias, b.reject_alias from teflow_wkf_process_trace a"
                    + " left join teflow_wkf_detail b on (a.flow_id = b.flow_id) and (a.node_id = b.node_id)"
                    + " where a.request_no = '" + requestNo + "' and ((a.handle_type = '"
                    + CommonName.HANDLE_TYPE_COMPLETE + "') or"
                    + " substring(a.handle_comments,1,25) <> 'System automatically skip' and a.handle_type <> '"
                    + CommonName.HANDLE_TYPE_SKIPPED + "') order by a.process_id"; // 如果是最后一个节点，则无论如何都显示
        }

        Collection list = dbManager.query(querySQL);
        if (list != null && list.size() > 0) {
            Iterator it = list.iterator();
            while (it.hasNext()) {
                WorkFlowProcessTraceVO vo = new WorkFlowProcessTraceVO();
                HashMap map = (HashMap) it.next();
                vo.setProcessId(FieldUtil.convertSafeInt(map, "PROCESS_ID", 0));
                vo.setNodeName(FieldUtil.convertSafeString(map, "NODE_NAME"));
                vo.setRequestNo(requestNo);
                vo.setHandleStaffCode(FieldUtil.convertSafeString(map, "CURRENT_PROCESSOR"));
                vo.setHandleDateStr(FieldUtil.convertSafeString(map, "HANDLE_DATE"));
                vo.setHandleType(FieldUtil.convertSafeString(map, "HANDLE_TYPE"));
                vo.setHandleComments(DataConvertUtil.convertISOToGBK(FieldUtil
                        .convertSafeString(map, "HANDLE_COMMENTS")));
                vo.setFilePathName(DataConvertUtil.convertISOToGBK(FieldUtil.convertSafeString(map, "ATTACH_FILE")));
                vo.setApproveAlias(FieldUtil.convertSafeString(map, "APPROVE_ALIAS"));
                vo.setRejectAlias(FieldUtil.convertSafeString(map, "REJECT_ALIAS"));
                resultList.add(vo);
            }
        }
        return resultList;
    }

    /**
     * 返回下一个节点的id号
     * 
     * @param vo
     * @throws DAOException
     */
    public String nodeProcess(WorkFlowProcessTraceVO vo, String requestStaffCode) throws DAOException {
        String currentDate = StringUtil.getDateStr(new Date(), "yyyy-MM-dd HH:mm:ss");
        String receiveDate = null;
        double handleHours = 0.0;
        double overdueHours = 0.0;
        String sql = "select a.receiving_date,b.limited_hours from teflow_wkf_process a,teflow_wkf_detail b ";
        sql = sql + "where a.request_no='" + vo.getRequestNo()
                + "' and a.flow_id = b.flow_id and a.node_id = b.node_id ";
        Collection list = dbManager.query(sql);
        if (list != null && !list.isEmpty()) {
            HashMap map = (HashMap) list.iterator().next();
            receiveDate = FieldUtil.convertSafeString(map, "RECEIVING_DATE");
            if (!"".equals(receiveDate)) {
                handleHours = this.getHandleHours(vo.getRequestNo(), null);
                String limitedHours = FieldUtil.convertSafeString(map, "LIMITED_HOURS");
                if (!"".equals(limitedHours) && Double.parseDouble(limitedHours) != 0) {
                    overdueHours = OverDueUtil.computeOverdueHours(receiveDate, currentDate,
                            Double.parseDouble(limitedHours));
                }
            }
        }

        Collection paramList = new ArrayList();

        int[] outParameterPosition = {13};
        int[] returnType = {dbManager.DATA_TYPE_VARCHAR};

        paramList.add("" + vo.getFormSystemId());
        paramList.add(vo.getRequestNo());
        paramList.add(vo.getCurrentNodeId());
        paramList.add(vo.getHandleStaffCode());
        paramList.add(vo.getHandleType());
        paramList.add(vo.getHandleComments());
        paramList.add(requestStaffCode);
        paramList.add("" + handleHours);
        paramList.add("" + overdueHours);
        paramList.add(vo.getFilePathName());
        paramList.add(vo.getRejectToNodeId());
        paramList.add(vo.getNextApprover());
        paramList.add("");

        String[] result = dbManager.prepareCall4("poef_wkf_node_process", paramList, outParameterPosition, returnType);

        if (result != null) {
            return result[0];
        }
        return "";
    }

    public String nodeProcess(WorkFlowProcessTraceVO vo, String requestStaffCode, String nextApproverStaffCode,
            boolean isSubmitted) throws DAOException {
        String retVal = this.nodeProcess(vo, requestStaffCode); // Run #1
        WorkFlowProcessVO process = this.getProcessVO(vo.getRequestNo());
        if (process != null) {
            if (CommonName.NODE_TYPE_OPTIONAL.equals(process.getNodeType())
                    || CommonName.NODE_TYPE_SELECTAPPROVER.equals(process.getNodeType())) {
                if (nextApproverStaffCode != null && !"".equals(nextApproverStaffCode.trim())) {
                    // User pick the processors!
                    if (nextApproverStaffCode.indexOf("/") > -1) {
                        String[] tmp = nextApproverStaffCode.split("/");
                        dbManager.executeUpdate("update teflow_wkf_process set current_processor='" + tmp[1]
                                + "', origin_processor='" + tmp[0] + "', is_deputy='1' where request_no='"
                                + vo.getRequestNo() + "'");
                    } else {
                        dbManager.executeUpdate("update teflow_wkf_process set current_processor='"
                                + nextApproverStaffCode + "' where request_no='" + vo.getRequestNo() + "'");
                    }

                    return retVal;

                } else { // User choose to skip the node!
                    vo.setHandleType(CommonName.HANDLE_TYPE_SKIPPED);
                    vo.setHandleComments("Skipped");
                    vo.setHandleStaffCode(requestStaffCode);
                    vo.setCurrentNodeId(process.getNodeId());
                    retVal = this.nodeProcess(vo, requestStaffCode); // Run #2

                    if (isSubmitted) {
                        dbManager.executeUpdate("update teflow_wkf_process set status='" + CommonName.STATUS_SUBMITTED
                                + "' where request_no='" + vo.getRequestNo() + "'");
                    } else {
                        dbManager.executeUpdate("update teflow_wkf_process set status='" + CommonName.STATUS_INPROGRESS
                                + "' where request_no='" + vo.getRequestNo() + "'");
                    }
                }
            } else if (CommonName.NODE_TYPE_DELAYED.equals(process.getNodeType())) {
                WorkFlowNodeDAO nodeDAO = new WorkFlowNodeDAO(this.dbManager);
                WorkFlowItemVO node = nodeDAO.getNodeById(process.getFlowId(), process.getNodeId());
                String delaytimeField = node.getDelaytimeField();
                // System.out.println("DelaytimeField: " + delaytimeField);
                if (delaytimeField != null && !delaytimeField.equals("")) {
                    String[] _delaytimeField = delaytimeField.split("\\.");
                    String sectionId = _delaytimeField[0];
                    String fieldId = _delaytimeField[1];
                    FormManageDAO formDAO = new FormManageDAO(this.dbManager);
                    FormSectionVO section = formDAO.getFormSectionInfor(process.getFormSystemId(), sectionId);
                    Object fieldValue = this.getFormFieldValue(process.getRequestNo(), section, fieldId, 1);
                    // System.out.println("fieldValue: " + fieldValue);
                    if (fieldValue != null) {
                        boolean delay = false;
                        if (fieldValue instanceof Timestamp) {
                            Timestamp time = (Timestamp) fieldValue;
                            Date now = new Date();
                            if (now.before(time)) {
                                delay = true;
                            }
                        } else {
                            // For other field type
                        }
                        if (delay) {
                            this.lockForm(process.getRequestNo(), "1", "SYS");
                            System.out.println("Delay Time: " + fieldValue);
                        }
                    }
                }
            }
        }
        return retVal;
    }

    /**
     * Process callback to 3rd party system.
     */
    public void processCallback(WorkFlowProcessTraceVO vo) throws DAOException {
        if (!"Yes".equalsIgnoreCase(ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_SERVICE_CALLBACK))) {
            return;
        }

        if (!(CommonName.HANDLE_TYPE_SUBMITT.equals(vo.getHandleType())
                || CommonName.HANDLE_TYPE_APPROVE.equals(vo.getHandleType())
                || CommonName.HANDLE_TYPE_REJECT.equals(vo.getHandleType()) || CommonName.HANDLE_TYPE_COMPLETE
                    .equals(vo.getHandleType()))) {
            return;
        }
        String handleType = DataMapUtil.convertHandleType(vo.getHandleType());
        // System.out.println("Handle Type: " + handleType);

        String SQL = "select * from teflow_wkf_process_callback where request_no = ?";

        String SQL_NODE = "select b.* from teflow_wkf_define a, teflow_wkf_detail b "
                + "where (a.flow_id = b.flow_id) and (a.form_system_id = ?) and (b.node_id = ?)";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            // Get callback
            stm = conn.prepareStatement(SQL);
            int i = 1;
            stm.setString(i++, vo.getRequestNo());
            ResultSet rs = stm.executeQuery();
            ArrayList<String> callbacks = new ArrayList<String>();
            while (rs.next()) {
                String callback = rs.getString("callback");
                // System.out.println("Callback: " + callback);
                callbacks.add(callback);
            }
            if (callbacks.isEmpty()) {
                return;
            }

            // Get node name
            rs.close();
            stm.close();
            stm = conn.prepareStatement(SQL_NODE);
            i = 1;
            stm.setInt(i++, vo.getFormSystemId());
            stm.setString(i++, vo.getCurrentNodeId());
            rs = stm.executeQuery();
            if (!rs.next()) {
                return;
            }
            String nodeName = rs.getString("node_name");
            // System.out.println("Node Id: " + vo.getCurrentNodeId());
            // System.out.println("Node Name: " + nodeName);

            // Get To node
            WorkFlowProcessVO process = this.getProcessVO(vo.getRequestNo());
            // System.out.println("To Node Id: " + process.getNodeId());
            // System.out.println("To Node Name: " + process.getNodeName());

            String params = "";
            params += "requestNo=" + vo.getRequestNo();
            params += "&nodeNo=" + vo.getCurrentNodeId();
            params += "&nodeName=" + URLEncoder.encode(nodeName, "UTF-8");
            params += "&toNodeNo=" + process.getNodeId();
            params += "&toNodeName=" + URLEncoder.encode(process.getNodeName(), "UTF-8");
            params += "&stateChange=" + handleType;

            // Do callback
            for (String callback : callbacks) {
                URLConnection urlConn = null;
                try {
                    URL url = new URL(callback);
                    if (url.getQuery() == null) {
                        callback += "?" + params;
                    } else {
                        callback += "&" + params;
                    }
                    url = new URL(callback);
                    urlConn = url.openConnection();
                    urlConn.setConnectTimeout(2000);
                    urlConn.setUseCaches(false);
                    long timeBegin = System.currentTimeMillis();
                    urlConn.connect();
                    String contentType = urlConn.getContentType();
                    // System.out.println("contentType: " + contentType);
                    System.out.println("Connect callback: " + callback + " in "
                            + (System.currentTimeMillis() - timeBegin) + " ms");
                } catch (MalformedURLException ex) {
                    ex.printStackTrace();
                    continue;
                } catch (SocketTimeoutException ex) {
                    ex.printStackTrace();
                    continue;
                } catch (IOException ex) {
                    ex.printStackTrace();
                    continue;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new DAOException(ex);
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public boolean isRequestCompleted(String requestno) throws DAOException {

        String sql = "SELECT COUNT(*) AS Expr1 FROM teflow_wkf_process WHERE (request_no = ?) AND (node_id = '-1') AND (status = '04')";
        int count = dbManager.getRecordCount(sql, new Object[] {requestno}, new int[] {DataType.VARCHAR});

        return count >= 1;

    }

    public String[] getNextProcessStaff(WorkFlowProcessTraceVO vo, String requestStaffCode, String approverGroupId)
            throws DAOException {
        Collection paramList = new ArrayList();

        paramList.add(approverGroupId);
        paramList.add("");
        paramList.add("" + vo.getFormSystemId());
        paramList.add("" + vo.getFlowId());
        paramList.add(vo.getRequestNo());
        paramList.add(vo.getCurrentNodeId());
        paramList.add(vo.getHandleStaffCode());
        paramList.add("");
        paramList.add("");
        paramList.add("");

        int[] parameterPosition = {8};
        int[] returnType = {dbManager.DATA_TYPE_VARCHAR};

        String[] result = dbManager.prepareCall4("poef_getNextProccessor", paramList, parameterPosition, returnType);

        if (result != null) {
            return StringUtil.split(result[0], ",");
        }
        return null;
    }

    /**
     * 获取Reject到的节点的处理人
     * 
     * @param requestNo
     * @param rejectToNodeId
     * @return
     * @throws DAOException
     */
    public String[] getRejectToStaff(WorkFlowProcessTraceVO vo) throws DAOException {
        String approverGroupId = "";
        String approverStaffCodes = "";
        String selSQL = " select approver_staff_code,approver_group_id from teflow_wkf_detail where flow_id in (select flow_id from teflow_wkf_define where form_system_id="
                + vo.getFormSystemId() + ") and node_id = '" + vo.getRejectToNodeId() + "'";
        // System.out.println(selSQL);
        Collection list = dbManager.query(selSQL);
        if (list == null || list.size() == 0)
            return null;
        HashMap map = (HashMap) list.iterator().next();
        approverGroupId = (String) map.get("APPROVER_GROUP_ID");
        approverStaffCodes = (String) map.get("APPROVER_STAFF_CODE");

        Collection paramList = new ArrayList();

        paramList.add(approverGroupId);
        paramList.add(approverStaffCodes);
        paramList.add("" + vo.getFormSystemId());
        paramList.add("" + vo.getFlowId());
        paramList.add(vo.getRequestNo());
        paramList.add(vo.getCurrentNodeId());
        paramList.add(vo.getHandleStaffCode());
        paramList.add("");
        paramList.add("");
        paramList.add("");

        int[] parameterPosition = {8, 9, 10};
        int[] returnType = {dbManager.DATA_TYPE_VARCHAR, dbManager.DATA_TYPE_VARCHAR, dbManager.DATA_TYPE_VARCHAR};

        String[] result = dbManager.prepareCall4("poef_getNextProccessor", paramList, parameterPosition, returnType);

        if (result != null) {
            return StringUtil.split(result[0], ",");
        }
        return null;
    }

    /**
     * 2011-5-31 IT1235 Add column org_id, team_code to teflow_wkf_process
     * 
     * @param vo
     * @param requestStaffCode
     * @throws DAOException
     */
    public void drawOutForm(WorkFlowProcessTraceVO vo, String requestStaffCode) throws DAOException {
        // 根据requestStaffCode找到所属公司、部门
        if (vo.getOrgId() == null || "".equals(vo.getOrgId())) {
            StaffVO staff = StaffTeamHelper.getInstance().getStaffByCode(requestStaffCode);
            vo.setOrgId(staff.getOrgId());
            vo.setTeamCode(staff.getTeamCode());
        }
        String processRecordSQL = "INSERT INTO teflow_wkf_process(request_no,flow_id,submission_date,previous_processor,node_id,status,request_staff_code,urgent_level,submit_staff_code,is_deputy,cc_staffs,org_id,team_code) ";
        processRecordSQL = processRecordSQL + " select '" + vo.getRequestNo() + "',flow_id,GETDATE(),'"
                + vo.getHandleStaffCode() + "','0','00','" + requestStaffCode + "','" + vo.getIsUrgent() + "','"
                + vo.getHandleStaffCode() + "','0','" + vo.getCcStaffs() + "','" + vo.getOrgId() + "','"
                + vo.getTeamCode() + "' from teflow_wkf_define where form_system_id =" + vo.getFormSystemId();
        String traceRecordSQL = "INSERT INTO teflow_wkf_process_trace(request_no,flow_id,node_id,current_processor,handle_date,handle_type,handle_comments,is_deputy,node_name) ";
        traceRecordSQL = traceRecordSQL + " select '" + vo.getRequestNo() + "',flow_id,'0','" + vo.getHandleStaffCode()
                + "',GETDATE(),'00','" + vo.getHandleComments()
                + "','0','Begin' from teflow_wkf_define where form_system_id =" + vo.getFormSystemId();
        dbManager.executeUpdate(processRecordSQL);
        dbManager.executeUpdate(traceRecordSQL);
    }

    /**
     * 撤销 已经提交的表单
     * 
     * @param vo
     * @param requestStaffCode
     * @throws DAOException
     */
    public void withDrawForm(WorkFlowProcessTraceVO vo, String handleStaffCode) throws DAOException {
        // 修改表单状态为"00",当前节点为“0”（begin)
        String wdSQL = "UPDATE teflow_wkf_process set status='00',node_id='0',current_processor='',origin_processor='',receiving_date=getdate() where request_no='"
                + vo.getRequestNo() + "'";
        dbManager.executeUpdate(wdSQL);
        // 记录操作轨迹
        String traceRecordSQL = "INSERT INTO teflow_wkf_process_trace(request_no,flow_id,node_id,current_processor,handle_date,handle_type,handle_comments,is_deputy,node_name) ";
        traceRecordSQL = traceRecordSQL + " select '" + vo.getRequestNo() + "',flow_id,'0','" + handleStaffCode
                + "',GETDATE(),'02','" + vo.getHandleComments()
                + "','0','Begin' from teflow_wkf_process where request_no = '" + vo.getRequestNo() + "' ";
        dbManager.executeUpdate(traceRecordSQL);
    }

    // Get special field
    public String[] getSpecialField(String requestNo, int type) {
        String SQL1 = "select d.form_system_id, d.section_id, d.section_remark, d.table_name, c.field_id, e.field_label "
                + "from teflow_wkf_process a, teflow_wkf_define b, teflow_form_special_field c, "
                + "teflow_form_section d, teflow_form_section_field e "
                + "where (a.flow_id = b.flow_id) and (b.form_system_id = c.form_system_id) "
                + "and (c.form_system_id = d.form_system_id) and (c.section_id = d.section_id) "
                + "and (c.form_system_id = e.form_system_id) and (c.section_id = e.section_id) and (c.field_id = e.field_id) "
                + "and (a.request_no = ?) and (c.field_type = ?)";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(SQL1);
            int i = 1;
            stm.setString(i++, requestNo);
            stm.setInt(i++, type);
            ResultSet rs = stm.executeQuery();
            if (!rs.next()) {
                return null;
            }
            String sectionName = rs.getString("section_remark");
            String fieldLabel = rs.getString("field_label");
            String tableName = rs.getString("table_name");
            String fieldId = rs.getString("field_id");

            String SQL2 = "select " + fieldId + " field_value from " + tableName + " where request_no = ?";

            rs.close();
            stm.close();
            stm = conn.prepareStatement(SQL2);
            i = 1;
            stm.setString(i++, requestNo);
            rs = stm.executeQuery();

            if (!rs.next()) {
                return null;
            }
            String value = rs.getString("field_value");

            String remark = "";
            if ("No".equalsIgnoreCase(ParamConfigHelper.getInstance().getParamValue(
                    CommonName.PARAM_HIGHLIGHT_FIELDLABEL))) {
                remark = "no_label";
            }

            return new String[] {sectionName, fieldLabel, value, remark};

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    /*
     * 2012-8-14 多选一字段需要显示business value而非code
     */
    public String[] getSpecialField(String requestNo, int type, int formSystemId) {
        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;
        ResultSet rs = null;
        String masterId = null;

        try {
            String key = formSystemId + "_" + type;
            String[] specialField = (String[]) this.specialFieldMap.get(key);
            if (specialField == null) {
                String SQL1 = "select b.form_system_id, b.section_id, b.section_remark, b.table_name, a.field_id, c.field_label, d.master_id "
                        + "from (teflow_form_special_field a left outer join teflow_field_basedata d on a.form_system_id = d.form_system_id and a.section_id = d.section_id and a.field_id = d.field_id),"
                        + "teflow_form_section b, teflow_form_section_field c "
                        + "where (a.form_system_id = b.form_system_id) and (a.section_id = b.section_id) "
                        + "and (a.form_system_id = c.form_system_id) and (a.section_id = c.section_id) and (a.field_id = c.field_id) "
                        + "and (a.form_system_id = ?) and (a.field_type = ?)";

                stm = conn.prepareStatement(SQL1);
                int i = 1;
                stm.setInt(i++, formSystemId);
                stm.setInt(i++, type);
                rs = stm.executeQuery();
                if (!rs.next()) {
                    return null;
                }
                specialField = new String[5];
                specialField[0] = rs.getString("section_remark");
                specialField[1] = rs.getString("field_label");
                specialField[2] = rs.getString("table_name");
                specialField[3] = rs.getString("field_id");
                specialField[4] = rs.getString("master_id");
                this.specialFieldMap.put(key, specialField);
                rs.close();
                stm.close();
            }
            String sectionName = specialField[0];
            String fieldLabel = specialField[1];
            String tableName = specialField[2];
            String fieldId = specialField[3];
            masterId = specialField[4];

            String SQL2 = "select " + fieldId + " field_value from " + tableName + " where request_no = ?";

            stm = conn.prepareStatement(SQL2);
            stm.setString(1, requestNo);
            rs = stm.executeQuery();
            if (!rs.next()) {
                return null;
            }
            String value = rs.getString("field_value");

            // 1.Is it base data field?
            String businessValue = BaseDataHelper.getInstance().getLabelValue(masterId, "", value);
            // 2.Is it system field?
            if ("" == businessValue) {
                FormSectionFieldVO vo = SystemFieldHelper.getInstance().getSystemFieldById(fieldId);
                Collection sysOpList = vo == null ? null : vo.getOptionList();
                if (sysOpList != null && sysOpList.size() > 0) {
                    Iterator it = sysOpList.iterator();
                    while (it.hasNext()) {
                        DictionaryDataVO op = (DictionaryDataVO) it.next();
                        if (op.getId().equals(value)) {
                            businessValue = op.getValue();
                            break;
                        }
                    }
                }
            }
            // 3.Is it team?
            if ("" == businessValue) {
                if (fieldId.equals("team_code"))
                    businessValue = StaffTeamHelper.getInstance().getTeamNameByCode(value);
            }

            if ("" != businessValue)
                value = businessValue;

            String remark = "";
            if ("No".equalsIgnoreCase(ParamConfigHelper.getInstance().getParamValue(
                    CommonName.PARAM_HIGHLIGHT_FIELDLABEL))) {
                remark = "no_label";
            }

            return new String[] {sectionName, fieldLabel, value, remark};

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public boolean isStaffProcessCompleted(String staffCode) throws DAOException {
        if (staffCode == null || "".equals(staffCode))
            return false;

        String sql = "SELECT COUNT(request_no) AS Expr1 FROM teflow_wkf_process " + " WHERE (request_staff_code = '"
                + staffCode + "') " + " AND (status = '01' OR status = '02')";
        int count = dbManager.getRecordCount(sql);
        return count == 0;
    }

    public static void main(String[] args) {
        double t = 82 / 60.0;
        System.out.println(t);
    }

    public List getWaitingNodeForms(boolean includeDH) throws DAOException {
        String SQL = "select a.*, b.last_process_date from ("
                + "select a.*, b.form_system_id, c.node_name, c.node_type, c.limited_hours, d.form_type "
                + "from teflow_wkf_process a, teflow_wkf_define b, teflow_wkf_detail c, teflow_form d "
                + "where (a.flow_id = b.flow_id) and (a.flow_id = c.flow_id) and (a.node_id = c.node_id) "
                + "and (c.node_type = 'a'"
                + (includeDH ? " or c.node_name in ('DH', 'DH\\TL', 'DH/TL')" : "")
                + ") and (b.form_system_id = d.form_system_id)) a left join ("
                + "select request_no, max(process_date) as last_process_date from teflow_waiting_node_process group by request_no"
                + ") b on (a.request_no = b.request_no)";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(SQL);
            ResultSet rs = stm.executeQuery();
            List forms = new ArrayList();
            while (rs.next()) {
                WorkFlowProcessVO vo = new WorkFlowProcessVO();
                vo.setRequestNo(rs.getString("request_no"));
                vo.setRequestStaffCode(rs.getString("request_staff_code"));
                vo.setSubmittedBy(rs.getString("submit_staff_code"));
                vo.setFormSystemId(rs.getInt("form_system_id"));
                vo.setFormType(rs.getString("form_type"));
                vo.setFlowId(rs.getInt("flow_id"));
                vo.setNodeId(rs.getString("node_id"));
                vo.setNodeName(rs.getString("node_name"));
                vo.setNodeType(rs.getString("node_type"));
                vo.setReceivingDate(rs.getTimestamp("receiving_date"));
                vo.setLimitedHours(rs.getDouble("limited_hours"));
                vo.setWaitNodeProcessDate(rs.getTimestamp("last_process_date"));
                forms.add(vo);
            }
            return forms;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new DAOException(ex);
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public void saveWaitingNodeProcess(WorkFlowProcessVO vo) throws DAOException {
        String SQL = "insert into teflow_waiting_node_process (request_no, process_date, process_type) values (?, ?, ?)";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(SQL);
            int i = 1;
            stm.setString(i++, vo.getRequestNo());
            stm.setTimestamp(i++, new Timestamp(vo.getWaitNodeProcessDate().getTime()));
            stm.setString(i++, vo.getWaitNodeProcessType());
            stm.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new DAOException(ex);
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public List<WorkFlowProcessVO> getDelayedNodeForms() throws DAOException {
        String SQL = "select a.*, b.form_system_id, c.node_name, c.node_type, c.limited_hours, d.form_type "
                + "from teflow_wkf_process a, teflow_wkf_define b, teflow_wkf_detail c, teflow_form d "
                + "where (a.flow_id = b.flow_id) and (a.flow_id = c.flow_id) and (a.node_id = c.node_id) and (c.node_type = 'd') "
                + "and (b.form_system_id = d.form_system_id) and (a.inprocess = '1') and (a.inprocess_staff_code = 'SYS')";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(SQL);
            ResultSet rs = stm.executeQuery();
            List<WorkFlowProcessVO> forms = new ArrayList<WorkFlowProcessVO>();
            while (rs.next()) {
                WorkFlowProcessVO vo = new WorkFlowProcessVO();
                vo.setRequestNo(rs.getString("request_no"));
                vo.setRequestStaffCode(rs.getString("request_staff_code"));
                vo.setSubmittedBy(rs.getString("submit_staff_code"));
                vo.setFormSystemId(rs.getInt("form_system_id"));
                vo.setFormType(rs.getString("form_type"));
                vo.setFlowId(rs.getInt("flow_id"));
                vo.setNodeId(rs.getString("node_id"));
                vo.setNodeName(rs.getString("node_name"));
                vo.setNodeType(rs.getString("node_type"));
                vo.setCurrentProcessor(rs.getString("current_processor"));
                vo.setReceivingDate(rs.getTimestamp("receiving_date"));
                vo.setLimitedHours(rs.getDouble("limited_hours"));
                forms.add(vo);
            }
            return forms;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new DAOException(ex);
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public boolean isUrgent(int formSystemId, String requestNo) throws DAOException {

        String sql = "select section_id from teflow_form_section_field where form_system_id ="
                + Integer.toString(formSystemId) + " and field_id='urgent_level'";

        try {
            Collection list = dbManager.query(sql);
            if (list != null && list.size() != 0) {
                HashMap map = (HashMap) list.iterator().next();
                String sectionId = (String) map.get("SECTION_ID");

                sql = "select urgent_level from teflow_" + formSystemId + "_" + sectionId + " where request_no='"
                        + requestNo + "'";
                list = dbManager.query(sql);
                if (list != null && list.size() != 0) {
                    map = (HashMap) list.iterator().next();
                    String urgentLevel = (String) map.get("URGENT_LEVEL");
                    if ("2".equals(urgentLevel)) {
                        return true;
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new DAOException(ex);
        }
        return false;
    }

    public Object getFormFieldValue(String requestNo, FormSectionVO section, String fieldId, int rowId)
            throws DAOException {
        String SQL = "select " + fieldId + " field_value from " + section.getTableName() + " where request_no = ?";
        boolean hasRowId = false;
        if (section.getSectionType().equals(CommonName.FORM_SECTION_TYPE_TABLE)
                || section.getSectionType().equals(CommonName.FORM_SECTION_TYPE_ATTACHMENT)
                || section.getSectionType().equals(CommonName.FORM_SECTION_TYPE_JQGRID_TABLE)) {
            SQL += " order by id";
            hasRowId = true;
        }
        // System.out.println("getFormFieldValue - SQL: " + SQL);

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(SQL);
            int i = 1;
            stm.setString(i++, requestNo);
            ResultSet rs = stm.executeQuery();
            if (!rs.next()) {
                return null;
            }
            if (!hasRowId || rowId == 1) {
                return rs.getObject("field_value");
            } else {
                int row = 1;
                while (rs.next()) {
                    row++;
                    if (row == rowId) {
                        return rs.getObject("field_value");
                    }
                }
                return null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new DAOException(ex);
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public List<Map<String, Object>> getFormSectionValue(String requestNo, FormSectionVO section) throws DAOException {
        String SQL = "select * from " + section.getTableName() + " where request_no = ?";
        if (section.getSectionType().equals(CommonName.FORM_SECTION_TYPE_TABLE)
                || section.getSectionType().equals(CommonName.FORM_SECTION_TYPE_ATTACHMENT)
                || section.getSectionType().equals(CommonName.FORM_SECTION_TYPE_JQGRID_TABLE)) {
            SQL += " order by id";
        }

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            List<Map<String, Object>> valuelist = new ArrayList<Map<String, Object>>();
            stm = conn.prepareStatement(SQL);
            int i = 1;
            stm.setString(i++, requestNo);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Map<String, Object> values = new HashMap<String, Object>();
                Iterator fieldIt = section.getFieldList().iterator();
                while (fieldIt.hasNext()) {
                    FormSectionFieldVO field = (FormSectionFieldVO) fieldIt.next();
                    if (field.getFieldId().equalsIgnoreCase("request_no")) {
                        continue;
                    }
                    values.put(field.getFieldId(), rs.getObject(field.getFieldId()));
                }
                valuelist.add(values);
            }
            return valuelist;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new DAOException(ex);
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    /**
     * 利用teflow_wkf_process表的cc_staffs字段，存放已经发过提醒的用户
     * 
     */
    public List getAdviserRemindForms() throws DAOException {
        String SQL = "select * from teflow_wkf_process a where isnull(invited_expert,'') <> ''"
                + " and not exists(select * from teflow_wkf_process_trace "
                + " where handle_type = '07' and request_no = a.request_no " + " and handle_date >= a.receiving_date) "
                + " and invited_expert <> isnull(cc_staffs,'')";

        try {
            List forms = new ArrayList();
            List listRS = (List) dbManager.query(SQL);
            Iterator it = listRS.iterator();
            while (it.hasNext()) {
                HashMap map = (HashMap) it.next();
                WorkFlowProcessVO vo = convertMapToVo(map);
                forms.add(vo);
            }
            return forms;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new DAOException(ex);
        }
    }

    /**
     * 利用teflow_wkf_process表的cc_staffs字段，存放已经发过提醒的用户
     * 
     */
    public void updateRemindedExperts(WorkFlowProcessVO vo) throws DAOException {
        String SQL = "update teflow_wkf_process set cc_staffs= '" + vo.getInvitedExpert() + "' where request_no = '"
                + vo.getRequestNo() + "'";

        try {
            dbManager.executeUpdate(SQL);
            return;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new DAOException(ex);
        }
    }

    /**
     * 更新字段在中途被修改过的时间，目的是判断是否适用auto approve
     * 
     */
    public void saveLastUpdateDate(WorkFlowProcessVO vo) throws DAOException {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        String SQL = "update teflow_wkf_process set update_date=? where request_no =?";

        try {
            Connection conn = dbManager.getJDBCConnection();
            PreparedStatement stm = conn.prepareStatement(SQL);
            stm.setString(1, dateFormat.format(vo.getUpdateDate()));
            stm.setString(2, vo.getRequestNo());
            stm.execute();
            return;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new DAOException(ex);
        }
    }

    public Timestamp getLastHandleDate(String requestNo) throws DAOException {
        String SQL = "select max(handle_date) handle_date from teflow_wkf_process_trace where request_no = ? group by request_no";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(SQL);
            stm.setString(1, requestNo);
            ResultSet rs = stm.executeQuery();
            if (!rs.next()) {
                return null;
            }
            return rs.getTimestamp("handle_date");
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new DAOException(ex);
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public double getHandleHours(String requestNo, Timestamp curHandleDate) throws DAOException {
        Timestamp lastHandleDate = this.getLastHandleDate(requestNo);
        if (lastHandleDate == null) {
            return 0;
        }
        String dateFormat = "yyyy-MM-dd HH:mm:ss";
        String _lastHandleDate = StringUtil.getDateStr(lastHandleDate, dateFormat);
        if (curHandleDate == null) {
            curHandleDate = new Timestamp(System.currentTimeMillis());
        }
        String _curHandleDate = StringUtil.getDateStr(curHandleDate, dateFormat);
        double handleHours = OverDueUtil.computeInvertalHours(_lastHandleDate, _curHandleDate);
        return handleHours >= 0 ? handleHours : 0;
    }

    public List<WorkFlowProcessTraceVO> getNullHandleHoursProcessTraces() throws DAOException {
        String SQL = "select * from teflow_wkf_process_trace where handle_hours is null order by request_no, process_id";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            List<WorkFlowProcessTraceVO> list = new ArrayList<WorkFlowProcessTraceVO>();
            stm = conn.prepareStatement(SQL);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                WorkFlowProcessTraceVO vo = new WorkFlowProcessTraceVO();
                vo.setRequestNo(rs.getString("request_no"));
                vo.setProcessId(rs.getInt("process_id"));
                vo.setHandleDate(rs.getTimestamp("handle_date"));
                list.add(vo);
            }
            return list;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new DAOException(ex);
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public void updateHandleHours(WorkFlowProcessTraceVO traceVO) throws DAOException {
        String SQL = "update teflow_wkf_process_trace set handle_hours = ? where process_id = ?";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(SQL);
            int i = 1;
            stm.setDouble(i++, traceVO.getHandleHours());
            stm.setInt(i++, traceVO.getProcessId());
            stm.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new DAOException(ex);
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

}
