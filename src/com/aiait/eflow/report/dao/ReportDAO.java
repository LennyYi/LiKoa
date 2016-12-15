package com.aiait.eflow.report.dao;

import java.sql.*;
import java.util.*;

import com.aiait.eflow.common.helper.CompanyHelper;
import com.aiait.eflow.report.vo.PersonalHandleSummaryVO;
import com.aiait.eflow.util.*;
import com.aiait.eflow.wkf.vo.WorkFlowProcessVO;
import com.aiait.framework.db.*;
import com.aiait.framework.exception.DAOException;
import com.aiait.framework.vo.BaseVO;

public class ReportDAO extends BaseDAOImpl {

    public ReportDAO(IDBManager dbManager) {
        super(dbManager);
    }

    public Collection getAllPublishedReport() throws DAOException {
        String sql = "select report_system_id,report_id,report_name from teflow_report where status = 0";
        Collection list = dbManager.query(sql.toString());
       /* Collection result = new ArrayList();
        if (list == null || list.size() == 0)
            return result;
        Iterator it = list.iterator();
        while (it.hasNext()) {
            HashMap map = (HashMap) it.next();
            PersonalHandleSummaryVO summaryVo = new PersonalHandleSummaryVO();
            summaryVo.setHandleStaffCode(FieldUtil.convertSafeString(map, "HANDLE_BY"));
            summaryVo.setHandleCount(FieldUtil.convertSafeInt(map, "TOTAL_NUM", 0));
            result.add(summaryVo);
        }*/
        return list;
    }

    
    public Collection personalSummary(PersonalHandleSummaryVO vo) throws DAOException {
        StringBuffer sql = new StringBuffer(
                "select count(process_id) as Total_Num,current_processor as handle_by from teflow_wkf_process_trace ");
        sql.append(" where flow_id in (select flow_id from teflow_wkf_define where form_system_id=")
                .append(vo.getFormSystemId()).append(") and node_id='").append(vo.getNodeId()).append("' ");

        String endDateStr = "";
        if (vo.getEndDateStr() != null && !"".equals(vo.getEndDateStr())) {
            endDateStr = vo.getEndDateStr() + " 23:59:59";
        }
        if (vo.getBeginDateStr() != null && !"".equals(vo.getBeginDateStr())) {
            if (!"".equals(endDateStr)) {
                sql.append(" and handle_date between '").append(vo.getBeginDateStr() + " 00:00:00' and '")
                        .append(endDateStr).append("'");
            } else {
                sql.append(" and handle_date >='").append(vo.getBeginDateStr() + " 00:00:00'");
            }
        } else {
            if (!"".equals(endDateStr)) {
                sql.append(" and handle_date<='").append(endDateStr).append("'");
            }
        }

        sql.append(" group by current_processor");
        System.out.println("sql:" + sql.toString());
        Collection list = dbManager.query(sql.toString());
        Collection result = new ArrayList();
        if (list == null || list.size() == 0)
            return result;
        Iterator it = list.iterator();
        while (it.hasNext()) {
            HashMap map = (HashMap) it.next();
            PersonalHandleSummaryVO summaryVo = new PersonalHandleSummaryVO();
            summaryVo.setHandleStaffCode(FieldUtil.convertSafeString(map, "HANDLE_BY"));
            summaryVo.setHandleCount(FieldUtil.convertSafeInt(map, "TOTAL_NUM", 0));
            result.add(summaryVo);
        }
        return result;
    }

    public void summaryHandleByPersonal() {

    }

    /**
     * 
     * @param startDateStr
     *            yyyy-mm-dd
     * @param endDateStr
     *            yyyy-mm-dd
     * @param forms
     *            'form_id1','form_id2'...
     * @return
     * @throws DAOException
     */
    public Collection statusMonitoring(String startDateStr, String endDateStr, String forms) throws DAOException {

        if (endDateStr != null && endDateStr.length() > 0) {
            endDateStr += " 23:59:59";
        }

        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT flow_status, ");
        sql.append(" SUM(CASE org_id WHEN 'Z07001' THEN count_value ELSE 0 END) AS 'BJ',");
        sql.append(" SUM(CASE org_id WHEN 'Z07002' THEN count_value ELSE 0 END) AS 'SH',");
        sql.append(" SUM(CASE org_id WHEN 'Z07003' THEN count_value ELSE 0 END) AS 'CHO',");
        sql.append(" SUM(CASE org_id WHEN 'Z07004' THEN count_value ELSE 0 END) AS 'JS',");
        sql.append(" SUM(CASE org_id WHEN 'Z07005' THEN count_value ELSE 0 END) AS 'GD',");
        sql.append(" SUM(CASE org_id WHEN 'Z07009' THEN count_value ELSE 0 END) AS 'SZ',");
        // sql.append(" SUM(CASE org_id WHEN 'Z07011' THEN count_value ELSE 0 END) AS 'NJ',");
        // sql.append(" SUM(CASE org_id WHEN 'Z07012' THEN count_value ELSE 0 END) AS 'SuZh',");
        // sql.append(" SUM(CASE org_id WHEN 'Z07013' THEN count_value ELSE 0 END) AS 'GZ',");
        sql.append(" SUM(CASE org_id WHEN 'Z07016' THEN count_value ELSE 0 END) AS 'JM',");
        sql.append(" SUM(CASE org_id WHEN 'Z07017' THEN count_value ELSE 0 END) AS 'DG',");
        sql.append(" SUM(CASE org_id WHEN 'Z07018' THEN count_value ELSE 0 END) AS 'FS',");
        sql.append(" SUM(count_value) AS 'Total'");
        sql.append(" FROM (");

        sql.append(" SELECT form_id, flow_status, org_id, org_name, COUNT(form_id) AS count_value FROM veflow_flow_status");
        sql.append(" WHERE (form_status = '0') ");
        if (startDateStr != null && startDateStr.length() > 0) {
            sql.append(" AND (submission_date >= '" + startDateStr + "') ");
        }
        if (endDateStr != null && endDateStr.length() > 0) {
            sql.append(" AND (submission_date <= '" + endDateStr + "') ");
        }
        if (forms != null && forms.length() > 0) {
            sql.append(" AND (ltrim(str(form_system_id)) in (" + forms + ")) ");
        }
        sql.append(" GROUP BY form_id, flow_status, org_id, org_name) DERIVEDTBL");

        sql.append(" GROUP BY flow_status;");

        // System.out.print(sql.toString());
        return dbManager.query(sql.toString());

        // Collection result = new ArrayList();
        // if (list == null || list.size() == 0)
        // return result;
        // Iterator it = list.iterator();
        // while (it.hasNext()) {
        // HashMap map = (HashMap) it.next();
        // PersonalHandleSummaryVO summaryVo = new PersonalHandleSummaryVO();
        // summaryVo.setHandleStaffCode(FieldUtil.convertSafeString(map,
        // "HANDLE_BY"));
        // summaryVo.setHandleCount(FieldUtil.convertSafeInt(map, "TOTAL_NUM",
        // 0));
        // result.add(summaryVo);
        // }
        // return result;
    }

    /**
     * 
     * @param startDateStr
     *            yyyy-mm-dd
     * @param endDateStr
     *            yyyy-mm-dd
     * @return
     * @throws DAOException
     */
    public Collection processingAmount(String startDateStr, String endDateStr, String orgs, String forms,
            String formType) throws DAOException {

        if (endDateStr != null && endDateStr.length() > 0) {
            endDateStr += " 23:59:59";
        }

        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT DERIVEDTBL.form_id, DERIVEDTBL.org_id, ");
        sql.append(" SUM(CASE flow_status WHEN 'Completed' THEN count_value ELSE 0 END) AS 'Completed',");
        sql.append(" SUM(CASE flow_status WHEN 'Submitted' THEN count_value ELSE 0 END) AS 'Submitted', ");
        sql.append(" SUM(CASE flow_status WHEN 'Rejected' THEN count_value ELSE 0 END) AS 'Rejected', ");
        sql.append(" SUM(CASE flow_status WHEN 'In Progress' THEN count_value ELSE 0 END) AS 'In Progress', ");
        sql.append(" SUM(CASE flow_status WHEN 'Draft' THEN 0 ELSE count_value END) AS 'Total' ");
        sql.append(" FROM (");
        sql.append("  SELECT form_id, flow_status, org_id, COUNT(form_id) AS count_value");
        sql.append("  FROM veflow_flow_status ");
        sql.append("  WHERE (form_status = '0') ");

        if (startDateStr != null && startDateStr.length() > 0) {
            sql.append(" AND (submission_date >= '" + startDateStr + "') ");
        }
        if (endDateStr != null && endDateStr.length() > 0) {
            sql.append(" AND (submission_date <= '" + endDateStr + "') ");
        }

        if (forms != null && forms.length() > 0) {
            sql.append(" AND (ltrim(str(form_system_id)) in (" + forms + ") ) ");
        }

        if (orgs != null && orgs.length() > 0) {
            sql.append(" AND (org_id in (" + orgs + ") ) ");
        }

        if (formType != null && formType.length() > 0) {
            sql.append(" AND (form_type = '" + formType + "') ");
        }

        sql.append("  GROUP BY form_id, flow_status, org_id) DERIVEDTBL");
        sql.append(" GROUP BY form_id, org_id; ");

        return dbManager.query(sql.toString());
    }

    public List processingProgress(String startDateStr, String endDateStr, String orgs, String forms, String formType)
            throws DAOException {
        StringBuffer sql = new StringBuffer();
        sql.append("select a.request_no, a.submission_date, a.handle_date, coalesce(a.org_id, d.org_id) org_id, c.form_type, c.form_system_id, c.form_id, c.form_name, e.org_name, f.waiting_handle_date ");
        sql.append("from teflow_wkf_process a left join (");
        sql.append("select a.request_no, max(a.handle_date) as waiting_handle_date ");
        sql.append("from teflow_wkf_process_trace a, teflow_wkf_detail b ");
        sql.append("where (a.flow_id = b.flow_id) and (a.node_id = b.node_id) ");
        if (startDateStr != null && startDateStr.length() > 0) {
            sql.append(" and handle_date >= '" + startDateStr + "' ");
        }
        sql.append("and (b.node_type = 'a') and (a.handle_type = '03') group by a.request_no");
        sql.append(") f on (a.request_no = f.request_no), ");
        sql.append("teflow_wkf_define b, teflow_form c, tpma_staffbasic d, teflow_company e ");
        sql.append("where (a.flow_id = b.flow_id) and (b.form_system_id = c.form_system_id) ");
        sql.append("and (a.request_staff_code = d.staff_code) and (coalesce(a.org_id,d.org_id) = e.org_id) and (a.status = '04') ");

        if (startDateStr != null && startDateStr.length() > 0) {
            sql.append("and (a.submission_date >= '" + startDateStr + "') ");
        }
        if (endDateStr != null && endDateStr.length() > 0) {
            endDateStr += " 23:59:59";
            sql.append("and (a.submission_date <= '" + endDateStr + "') ");
        }
        if (formType != null && formType.length() > 0) {
            sql.append("and (c.form_type = '" + formType + "') ");
        }
        if (forms != null && forms.length() > 0) {
            sql.append("and (ltrim(str(c.form_system_id)) in (" + forms + ")) ");
        }
        if (orgs != null && orgs.length() > 0) {
            sql.append("and (coalesce(a.org_id,d.org_id) in (" + orgs + ")) ");
        }

        sql.append("order by c.form_type, c.form_id, d.org_id, a.request_no");

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(sql.toString());
            ResultSet rs = stm.executeQuery();
            List list = new ArrayList();
            while (rs.next()) {
                WorkFlowProcessVO vo = new WorkFlowProcessVO();
                vo.setRequestNo(rs.getString("request_no"));
                vo.setSubmissionDate(rs.getTimestamp("submission_date"));
                vo.setHandleDate(rs.getTimestamp("handle_date"));
                vo.setFormType(rs.getString("form_type"));
                vo.setFormSystemId(rs.getInt("form_system_id"));
                vo.setFormId(rs.getString("form_id"));
                vo.setFormName(rs.getString("form_name"));
                vo.setOrgId(rs.getString("org_id"));
                vo.setOrgName(rs.getString("org_name"));
                vo.setWaitNodeProcessDate(rs.getTimestamp("waiting_handle_date"));
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

    public List processingProgressByNode(String startDateStr, String endDateStr, String orgs, String forms,
            String formType, String nodeName) throws DAOException {
        StringBuffer sql = new StringBuffer();
        sql.append("select a.request_no, a.submission_date, a.handle_date, coalesce(a.org_id, d.org_id) org_id, c.form_type, c.form_system_id, c.form_id, c.form_name, e.org_name, f.handle_hours ");
        sql.append("from teflow_wkf_process a left join (");
        sql.append("select a.request_no, sum(a.handle_hours) as handle_hours ");
        sql.append("from teflow_wkf_process_trace a, teflow_wkf_detail b ");
        sql.append("where (a.flow_id = b.flow_id) and (a.node_id = b.node_id) ");
        if (CompanyHelper.getInstance().getEFlowCompany().equals(CompanyHelper.EFlow_AIA_CHINA)) {
            sql.append("and (a.handle_type <> '07') ");
        }
        sql.append("and (b.node_alias like '%" + nodeName + "%') ");
        if (startDateStr != null && startDateStr.length() > 0) {
            sql.append("and (a.handle_date >= '" + startDateStr + "') ");
        }
        sql.append("group by a.request_no");
        sql.append(") f on (a.request_no = f.request_no), ");
        sql.append("teflow_wkf_define b, teflow_form c, tpma_staffbasic d, teflow_company e ");
        sql.append("where (a.flow_id = b.flow_id) and (b.form_system_id = c.form_system_id) ");
        sql.append("and (a.request_staff_code = d.staff_code) and (coalesce(a.org_id, d.org_id) = e.org_id) and (a.status = '04') ");
        if (startDateStr != null && startDateStr.length() > 0) {
            sql.append("and (a.submission_date >= '" + startDateStr + "') ");
        }
        if (endDateStr != null && endDateStr.length() > 0) {
            endDateStr += " 23:59:59";
            sql.append("and (a.submission_date <= '" + endDateStr + "') ");
        }
        if (formType != null && formType.length() > 0) {
            sql.append("and (c.form_type = '" + formType + "') ");
        }
        if (forms != null && forms.length() > 0) {
            sql.append("and (ltrim(str(c.form_system_id)) in (" + forms + ")) ");
        }
        if (orgs != null && orgs.length() > 0) {
            sql.append("and (coalesce(a.org_id,d.org_id) in (" + orgs + ")) ");
        }
        sql.append("order by c.form_type, c.form_id, d.org_id, a.request_no");

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(sql.toString());
            ResultSet rs = stm.executeQuery();
            List list = new ArrayList();
            while (rs.next()) {
                WorkFlowProcessVO vo = new WorkFlowProcessVO();
                vo.setRequestNo(rs.getString("request_no"));
                vo.setSubmissionDate(rs.getTimestamp("submission_date"));
                vo.setHandleDate(rs.getTimestamp("handle_date"));
                vo.setFormType(rs.getString("form_type"));
                vo.setFormSystemId(rs.getInt("form_system_id"));
                vo.setFormId(rs.getString("form_id"));
                vo.setFormName(rs.getString("form_name"));
                vo.setOrgId(rs.getString("org_id"));
                vo.setOrgName(rs.getString("org_name"));
                vo.setHandleHours(rs.getDouble("handle_hours"));
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

    /**
     * 
     * @param startDateStr
     *            yyyy-mm-dd
     * @param endDateStr
     *            yyyy-mm-dd
     * @param orgId
     * @param requestNo
     * @param requestedBy
     * @param formSystemId
     * @param formType
     * @return
     * @throws DAOException
     */
    public int processingTrailCount(String startDateStr, String endDateStr, String compStartDateStr,
            String compEndDateStr, String orgId, String teamCode, String requestNo, String requestedBy,
            String formSystemId, String formType, String status) throws DAOException {
        StringBuffer subsql = buildSubSql(startDateStr, endDateStr, compStartDateStr, compEndDateStr, orgId, teamCode,
                requestNo, requestedBy, formSystemId, formType, status);

        StringBuffer sql = new StringBuffer();
        sql.append("select count(*) from ( ");
        sql.append(subsql.toString());
        sql.append(" ) DERIVEDTBL ");

        return dbManager.getRecordCount(sql.toString());
    }

    public Collection processingTrail(String startDateStr, String endDateStr, String compStartDateStr,
            String compEndDateStr, String orgId, String teamCode, String requestNo, String requestedBy,
            String formSystemId, String formType, String status, int pageSize, int currentPage) throws DAOException {

        StringBuffer subsql = buildSubSql(startDateStr, endDateStr, compStartDateStr, compEndDateStr, orgId, teamCode,
                requestNo, requestedBy, formSystemId, formType, status);

        StringBuffer sql = new StringBuffer();
        sql.append("select * from ( ");
        sql.append(subsql.toString());
        sql.append(" ) DERIVEDTBL ");
        sql.append(" order by request_no, handle_date ");

        return dbManager.query(sql.toString(), pageSize, currentPage);
    }

    public Collection processingTrail(String startDateStr, String endDateStr, String compStartDateStr,
            String compEndDateStr, String orgId, String teamCode, String requestNo, String requestedBy,
            String formSystemId, String formType, String status) throws DAOException {

        StringBuffer subsql = buildSubSql(startDateStr, endDateStr, compStartDateStr, compEndDateStr, orgId, teamCode,
                requestNo, requestedBy, formSystemId, formType, status);

        StringBuffer sql = new StringBuffer();
        sql.append("select * from ( ");
        sql.append(subsql.toString());
        sql.append(" ) DERIVEDTBL ");
        sql.append(" order by request_no, handle_date ");

        return dbManager.query(sql.toString());
    }

    private StringBuffer buildSubSql(String startDateStr, String endDateStr, String compStartDateStr,
            String compEndDateStr, String orgId, String teamCode, String requestNo, String requestedBy,
            String formSystemId, String formType, String status) throws DAOException {
        if (endDateStr != null && endDateStr.length() > 0) {
            endDateStr += " 23:59:59";
        }

        StringBuffer subsql = new StringBuffer();

        String specialFieldSql = " SELECT distinct a.form_system_id, section_id, field_id FROM teflow_form_special_field a, teflow_form b "
                + " WHERE (a.field_type = '2') and a.form_system_id=b.form_system_id and b.status='0' "; // published
                                                                                                         // forms only
        Collection spFields = dbManager.query(specialFieldSql);

        String formids = "";

        if (spFields != null && spFields.size() > 0) {
            int i = 1;
            Iterator it = spFields.iterator();

            subsql.append(" SELECT t.request_no, t.submission_date, t.complete_date, t.requestor, t.team_name, sec.content, ");
            // special field can not be date type
            subsql.append(" t.node_name, t.handle_date, t.handle_type, ");
            subsql.append(" t.processor, t.handle_comments, t.attach_file ");
            subsql.append(" FROM veflow_flow_trail t INNER JOIN (");

            while (it.hasNext()) {
                HashMap map = (HashMap) it.next();
                String form_sys_id = FieldUtil.convertSafeString(map, "FORM_SYSTEM_ID");
                String section_id = FieldUtil.convertSafeString(map, "SECTION_ID");
                String field_id = FieldUtil.convertSafeString(map, "FIELD_ID");

                subsql.append(" select CAST(" + field_id + " AS varchar(1000)) AS content, request_no ");
                subsql.append(" from teflow_" + form_sys_id + "_" + section_id);

                if (i < spFields.size()) {
                    subsql.append(" UNION ");
                }

                if (i == 1) {
                    formids = form_sys_id;
                } else {
                    formids += ", " + form_sys_id;
                }

                i++;
            }

            subsql.append(") as sec ON t.request_no = sec.request_no ");
            subsql.append(" WHERE (1=1) ");

            makeCondition(startDateStr, endDateStr, compStartDateStr, compEndDateStr, orgId, teamCode, requestNo,
                    requestedBy, formSystemId, formType, status, subsql);
        }

        if (subsql.length() > 0) {
            subsql.append(" UNION ");
        }
        subsql.append(" SELECT t.request_no, t.submission_date, t.complete_date, t.requestor, t.team_name, ");
        subsql.append(" '' AS content, ");
        subsql.append(" t.node_name, t.handle_date, t.handle_type, ");
        subsql.append(" t.processor, t.handle_comments, t.attach_file ");

        subsql.append(" FROM veflow_flow_trail t ");
        if (formids.length() > 0) {
            subsql.append(" WHERE (t.form_system_id not in (" + formids + ")) ");
        } else {
            subsql.append(" WHERE (1=1) ");
        }

        makeCondition(startDateStr, endDateStr, compStartDateStr, compEndDateStr, orgId, teamCode, requestNo,
                requestedBy, formSystemId, formType, status, subsql);
        return subsql;
    }

    private void makeCondition(String startDateStr, String endDateStr, String compStartDateStr, String compEndDateStr,
            String orgId, String teamCode, String requestNo, String requestedBy, String formSystemId, String formType,
            String status, StringBuffer subsql) {
        if (startDateStr != null && startDateStr.length() > 0) {
            subsql.append(" AND (t.submission_date >= '" + startDateStr + "') ");
        }
        if (endDateStr != null && endDateStr.length() > 0) {
            subsql.append(" AND (t.submission_date <= '" + endDateStr + "') ");
        }
        if (compStartDateStr != null && compStartDateStr.length() > 0) {
            subsql.append(" AND (t.complete_date >= '" + compStartDateStr + "') ");
        }
        if (compEndDateStr != null && compEndDateStr.length() > 0) {
            subsql.append(" AND (t.complete_date <= '" + compEndDateStr + "') ");
        }

        if (!StringUtil.isEmptyString(orgId)) {
            subsql.append(" AND (t.org_id = '" + orgId + "') ");
        }

        if (!StringUtil.isEmptyString(teamCode)) {
            subsql.append(" AND (t.Team_code = '" + teamCode + "') ");
        }

        if (!StringUtil.isEmptyString(requestNo)) {
            subsql.append(" AND (t.request_no = '" + requestNo + "') ");
        }

        if (!StringUtil.isEmptyString(requestedBy)) {
            subsql.append(" AND (t.req_staff_code = '" + requestedBy + "') ");
        }

        if (!StringUtil.isEmptyString(formSystemId)) {
            subsql.append(" AND (t.form_system_id = '" + formSystemId + "') ");
        }

        if (!StringUtil.isEmptyString(formType)) {
            subsql.append(" AND (t.form_type = '" + formType + "') ");
        }

        if (!StringUtil.isEmptyString(status)) {
            subsql.append(" AND (status = '" + status + "') ");
        } else { // default:SUBMITTED
            subsql.append(" AND (status = '01') ");
        }
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

}
