package com.aiait.eflow.housekeeping.dao;

import java.sql.*;
import java.util.*;

import com.aiait.eflow.housekeeping.vo.*;
import com.aiait.framework.db.*;
import com.aiait.framework.exception.DAOException;
import com.aiait.framework.vo.BaseVO;

public class LeaveAIASSDAO extends BaseDAOImpl {

    public LeaveAIASSDAO(IDBManager dbManager) {
        super(dbManager);
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

    public Collection getMonthlyList() throws DAOException {
        String SQL = "select year(submission_date) as year, month(submission_date) as month, count(*) as total "
                + "from teflow_wkf_process group by year(submission_date), month(submission_date) order by year desc, month desc";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;
        Collection monthlyList = new Vector();

        try {
            stm = conn.prepareStatement(SQL);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                LeaveMonthlyVO leaveMonthly = new LeaveMonthlyVO();
                leaveMonthly.setYear(rs.getString("year"));
                leaveMonthly.setMonth(rs.getString("month"));
                leaveMonthly.setTotal(rs.getInt("total"));
                monthlyList.add(leaveMonthly);
            }
            return monthlyList;
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

    public Collection getMonthlyRecords(String year, String month) throws DAOException {
        String SQL = "select a.request_no, a.submission_date, a.submit_staff_code, c.staff_name as submit_staff_name, "
                + "a.request_staff_code, d.staff_name as request_staff_name, b.company_id, e.org_name as company_name, "
                + "b.team_code, f.team_name, b2.field_02_1 as type_code, g.option_label as type_name, "
                + "b2.field_02_2 as apply_days, b2.field_02_3 as from_date, b2.field_02_5 as to_date, b2.field_02_7 as remark, "
                + "b2.field_02_4 as from_time_code, h.option_label as from_time_name, "
                + "b2.field_02_6 as to_time_code, i.option_label as to_time_name "
                + "from teflow_wkf_process a left join tpma_staffbasic c on (a.submit_staff_code = c.staff_code) "
                + "left join tpma_staffbasic d on (a.request_staff_code = d.staff_code), "
                + "teflow_110_01 b left join teflow_company e on (b.company_id = e.org_id) "
                + "left join tpma_team f on (b.team_code = f.team_code), teflow_110_02 b2, "
                + "(select * from teflow_base_data_detail where master_id in ("
                + "select master_id from teflow_field_basedata where (form_system_id = 110) and (section_id = '02') and (field_id = 'field_02_1'))) g, "
                + "(select * from teflow_base_data_detail where master_id in ("
                + "select master_id from teflow_field_basedata where (form_system_id = 110) and (section_id = '02') and (field_id = 'field_02_4'))) h, "
                + "(select * from teflow_base_data_detail where master_id in ("
                + "select master_id from teflow_field_basedata where (form_system_id = 110) and (section_id = '02') and (field_id = 'field_02_6'))) i "
                + "where (a.request_no = b.request_no) and (b.request_no = b2.request_no) "
                + "and (b2.field_02_1 = g.option_value) and (b2.field_02_4 = h.option_value) "
                + "and (b2.field_02_6 = i.option_value) and (year(a.submission_date) = ?) and (month(a.submission_date) = ?) "
                + "order by submission_date";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;
        Collection monthlyRecords = new Vector();

        try {
            stm = conn.prepareStatement(SQL);
            int i = 1;
            stm.setInt(i++, Integer.parseInt(year));
            stm.setInt(i++, Integer.parseInt(month));
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                LeaveVO leave = new LeaveVO();
                leave.getFormBasicData().setRequestNo(rs.getString("request_no"));
                leave.getFormBasicData().setRequestDate(rs.getTimestamp("submission_date"));
                leave.getFormBasicData().setSubmiterCode(rs.getString("submit_staff_code"));
                leave.getFormBasicData().setSubmiterName(rs.getString("submit_staff_name"));
                leave.getFormBasicData().setRequesterCode(rs.getString("request_staff_code"));
                leave.getFormBasicData().setRequesterName(rs.getString("request_staff_name"));
                leave.getFormBasicData().setCompanyCode(rs.getString("company_id"));
                leave.getFormBasicData().setCompanyName(rs.getString("company_name"));
                leave.getFormBasicData().setTeamCode(rs.getString("team_code"));
                leave.getFormBasicData().setTeamName(rs.getString("team_name"));

                leave.setTypeCode(rs.getString("type_code"));
                leave.setTypeName(rs.getString("type_name"));
                leave.setApplyDays(rs.getDouble("apply_days"));
                leave.setFromDate(rs.getDate("from_date"));
                leave.setToDate(rs.getDate("to_date"));
                leave.setFromTimeCode(rs.getString("from_time_code"));
                leave.setFromTimeName(rs.getString("from_time_name"));
                leave.setToTimeCode(rs.getString("to_time_code"));
                leave.setToTimeName(rs.getString("to_time_name"));
                leave.setRemark(rs.getString("remark"));
                monthlyRecords.add(leave);
            }
            return monthlyRecords;
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
