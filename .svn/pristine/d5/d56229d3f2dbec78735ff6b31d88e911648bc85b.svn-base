package com.aiait.eflow.report.dao;

import java.sql.*;
import java.text.*;
import java.util.*;

import com.aiait.eflow.common.helper.ParamConfigHelper;
import com.aiait.eflow.housekeeping.vo.StaffVO;
import com.aiait.eflow.report.vo.*;
import com.aiait.eflow.wkf.vo.WorkFlowProcessVO;
import com.aiait.framework.db.*;
import com.aiait.framework.exception.DAOException;
import com.aiait.framework.vo.BaseVO;

public class EpaymentReportDAO extends BaseDAOImpl {

    public EpaymentReportDAO(IDBManager dbManager) {
        super(dbManager);
    }

    public Collection search() throws DAOException {
        return null;
    }

    public Collection cashierQuery() throws DAOException {
        String strSQL = "SELECT DISTINCT a.current_processor, s.staff_name FROM teflow_wkf_process_trace a, teflow_wkf_detail b, tpma_staffbasic s"
                + " where a.flow_id=b.flow_id and a.node_id=b.node_id and a.current_processor=s.staff_code"
                + " and a.handle_type='05' and b.node_type='8' order by staff_name";// who complete
        Collection list = dbManager.query(strSQL);
        Iterator it = list.iterator();
        Collection result = new ArrayList();
        while (it.hasNext()) {
            HashMap map = (HashMap) it.next();
            StaffVO staff = new StaffVO();
            staff.setStaffCode((String) map.get("CURRENT_PROCESSOR"));
            staff.setStaffName((String) map.get("STAFF_NAME"));
            result.add(staff);
        }
        return result;
    }

    public Collection invoiceQuery(String[] fid, String begin, String end, String orgId, String flowOrgId,
            String status, String orderPhrase) throws DAOException {

        String strSQL = "";
        String strSQL2 = "";
        String strSQLLoop = "";
        StringBuffer sql = new StringBuffer(
                "select DISTINCT a.request_no,a.request_staff_code,a.submission_date,b.form_system_id,@amount as amount,@payee as payee_name,@pay_date as pay_date ");
        sql.append(" from teflow_wkf_process a,teflow_wkf_define b,teflow_@systemid_05 t5,@venderInfo ,tpma_staffbasic s, teflow_@systemid_01 t1 ");
        sql.append(" WHERE b.form_system_id=@systemid and a.request_staff_code = s.staff_code and a.status not in ('03') ");

        if (orgId != null && !"".equals(orgId)) {
            sql.append(" and s.org_id='" + orgId + "'");
        }
        if (flowOrgId != null && !"".equals(flowOrgId)) {
            sql.append(" and t1.flow_org_id='" + flowOrgId + "'");
        }
        String endDateStr = "";
        if (end != null && !"".equals(end)) {
            endDateStr = end + " 23:59:59";
        }
        if (begin != null && !"".equals(begin)) {
            if (!"".equals(endDateStr)) {
                sql.append(" and a.submission_date between '").append(begin + " 00:00:00' and '").append(endDateStr)
                        .append("'");
            } else {
                sql.append(" and a.submission_date >='").append(begin + " 00:00:00'");
            }
        } else {
            if (!"".equals(endDateStr)) {
                sql.append(" and a.submission_date <='").append(endDateStr).append("'");
            }
        }
        if ("01".equals(status)) {
            sql.append(" and t5.@got_invoice = '01' ");
        } else {
            sql.append(" and t5.@got_invoice <> '01' ");
        }
        // sql.append(" and isnull(t5.@amount,0) > 0 ");
        sql.append(" and a.flow_id=b.flow_id and a.request_no=t5.request_no and a.request_no=d.request_no and a.request_no=t1.request_no ");

        if (orderPhrase != null && !"".equals(orderPhrase)) {
            sql.append(" order by " + orderPhrase);
        } else {
            sql.append("order by a.submission_date desc");
        }
        // sql.append("order by e.@pay_date desc");

        ParamConfigHelper param = ParamConfigHelper.getInstance();
        strSQL = sql.toString().replaceAll("@pay_date", param.getParamValue("invoice_pay_date"));
        strSQL = strSQL.replaceAll("@got_invoice", param.getParamValue("invoice_got"));
        strSQL = strSQL.replaceAll("@amount", param.getParamValue("invoice_amount"));

        Collection result = new ArrayList();

        for (int i = 0; i < fid.length; i++) {

            strSQLLoop = strSQL;
            strSQLLoop = strSQLLoop.replaceAll("@payee", param.getParamValue("payment_payee_name").split(";")[i]);
            strSQLLoop = strSQLLoop.replaceAll("@venderInfo",
                    param.getParamValue("payment_vender_info_table").split(";")[i]);
            // System.out.println(strSQLLoop.replaceAll("@systemid", fid[i]));

            Collection list = dbManager.query(strSQLLoop.replaceAll("@systemid", fid[i]));
            if (list == null || list.size() == 0)
                continue;
            Iterator it = list.iterator();
            while (it.hasNext()) {
                HashMap map = (HashMap) it.next();
                InvoiceInfoVO tempvo = new InvoiceInfoVO();
                tempvo.setRequestNo((String) map.get("REQUEST_NO"));
                tempvo.setRequestStaffCode((String) map.get("REQUEST_STAFF_CODE"));
                // tempvo.setFormName((String)map.get("FORM_NAME"));
                tempvo.setPayDateStr((String) map.get("PAY_DATE"));
                tempvo.setAmount(Double.parseDouble((String) map.get("AMOUNT")));
                tempvo.setFormSystemId((String) map.get("FORM_SYSTEM_ID"));
                tempvo.setPayee((String) map.get("PAYEE_NAME"));

                // Merge the remark info
                strSQL2 = "SELECT top 1 * FROM teflow_" + fid[i] + "_06 where request_no='" + tempvo.getRequestNo()
                        + "' and field_06_1='2' order by id";
                // System.out.println(strSQL2);
                Collection list2 = dbManager.query(strSQL2.replaceAll("@requestNo", tempvo.getRequestNo()));
                Iterator it2 = list2.iterator();
                if (it2.hasNext()) {
                    HashMap map2 = (HashMap) it2.next();
                    tempvo.setRemark((String) map2.get(param.getParamValue("invoice_remark").toUpperCase()));
                }

                result.add(tempvo);
            }
        }
        return result;
    }

    public Collection caQuery(String[] fid, String begin, String end, String orgId, String requestedBy, String status,
            String orderPhrase) throws DAOException {

        ParamConfigHelper param = ParamConfigHelper.getInstance();
        String CA_form_id = param.getParamValue("ca_form");

        StringBuffer sql = new StringBuffer(
                "SELECT a.request_no, a.request_staff_code, a.receiving_date, b.form_system_id, e.field_05_1 as ca_amount, f.field_03_6 as remark, h.request_no as history, h.deal_date as repaydate, h.type as htype ");

        sql.append(" from (teflow_wkf_process a left outer join teflow_ebank_exp_history h on a.request_no=h.request_no) , teflow_wkf_define b , teflow_"
                + CA_form_id
                + "_05 e, teflow_"
                + CA_form_id
                + "_03 f, tpma_staffbasic s "
                + " Where a.flow_id = b.flow_id and a.request_staff_code = s.staff_code and s.org_id='"
                + orgId
                + "'"
                + "   and a.request_no = e.request_no and a.request_no = f.request_no and a.status = '04' " + // completed
                "   and b.form_system_id = " + CA_form_id); // TODO:test code

        if ("".equals(status)) {
            sql.append(" and (h.type = 2 or h.type is null or ( h.type = 1 and a.request_no not in (select request_no from teflow_ebank_exp_history where type = 2 )))");
        } else if ("01".equals(status)) {
            sql.append(" and h.type =2 ");
        } else if ("02".equals(status)) {
            sql.append(" and (h.type = 1 or h.type is null ) and (a.request_no not in (select request_no from teflow_ebank_exp_history where type = 2 )) ");
        }

        String strSQL2 = "";
        String strSQL3 = "";
        String endDateStr = "";
        if (end != null && !"".equals(end)) {
            endDateStr = end + " 23:59:59";
        }
        if (begin != null && !"".equals(begin)) {
            if (!"".equals(endDateStr)) {
                sql.append(" and a.submission_date between '").append(begin + " 00:00:00' and '").append(endDateStr)
                        .append("'");
            } else {
                sql.append(" and a.submission_date >='").append(begin + " 00:00:00'");
            }
        } else {
            if (!"".equals(endDateStr)) {
                sql.append(" and a.submission_date <='").append(endDateStr).append("'");
            }
        }

        if (requestedBy != null && !"".equals(requestedBy)) {
            sql.append(" and a.request_staff_code='" + requestedBy + "' ");
        }

        if (orderPhrase != null && !"".equals(orderPhrase)) {
            sql.append(" order by " + orderPhrase);
        } else {
            sql.append(" order by a.request_no");
        }
        // System.out.println(sql);

        Collection list = dbManager.query(sql.toString());

        if (list == null && list.size() == 0)
            return null;
        Collection result = new ArrayList();
        Iterator it = list.iterator();

        strSQL2 = "SELECT a.form_system_id,section_id,field_id FROM teflow_form_section_field a,teflow_form b"
                + " Where a.form_system_id=b.form_system_id and b.status=0 and a.field_comments = 'CA_FORM_ID'";

        Collection list2 = dbManager.query(strSQL2);

        while (it.hasNext()) {
            HashMap map = (HashMap) it.next();
            CAInfoVO tempvo = new CAInfoVO();
            tempvo.setRequestNo((String) map.get("REQUEST_NO"));
            tempvo.setFormSystemId((String) map.get("FORM_SYSTEM_ID"));
            tempvo.setRequestStaffCode((String) map.get("REQUEST_STAFF_CODE"));
            tempvo.setCompleteDate((String) map.get("RECEIVING_DATE"));
            tempvo.setCaAmount(Double.parseDouble((String) map.get("CA_AMOUNT")));
            tempvo.setStatus((!"02".endsWith("" + map.get("HTYPE")) || map.get("HISTORY") == null) ? "未还款" : "已还款");
            tempvo.setRepayDate((String) map.get("REPAYDATE"));
            tempvo.setCheckNo((String) map.get("REMARK"));

            // Merge the remark info
            String epayments = "";
            String amounts = "";
            String paymodes = "";
            String receipts = "";
            String paymentStatus = "";

            Iterator it2 = list2.iterator();
            while (it2.hasNext()) {
                HashMap map2 = (HashMap) it2.next();
                String tbl_ref = "teflow_" + (String) map2.get("FORM_SYSTEM_ID") + "_"
                        + (String) map2.get("SECTION_ID");
                String tbl_05 = "teflow_" + (String) map2.get("FORM_SYSTEM_ID") + "_05";

                strSQL3 = "SELECT a.request_no, field_05_1 as amount, field_05_2 as paymode, field_05_21 as receipt, c.status FROM "
                        + tbl_ref
                        + " a,"
                        + tbl_05
                        + " b, teflow_wkf_process c WHERE a.request_no=b.request_no and a.request_no=c.request_no "
                        + " and "
                        + (String) map2.get("FIELD_ID")
                        + "='"
                        + tempvo.getRequestNo()
                        + "' and c.status not in('03')";
                // System.out.println(strSQL3);
                Collection list3 = dbManager.query(strSQL3);
                Iterator it3 = list3.iterator();
                while (it3.hasNext()) {
                    // System.out.println(list3.size());
                    HashMap map3 = (HashMap) it3.next();
                    epayments += (String) map3.get("REQUEST_NO") + "<br>";
                    amounts += (String) map3.get("AMOUNT") + "<br>";
                    paymodes += "11".equals((String) map3.get("PAYMODE")) ? "工资扣减<br>" : "<br>";
                    receipts += (String) map3.get("RECEIPTS") + "<br>";
                    paymentStatus += "04".equals((String) map3.get("STATUS")) ? "N" : "Y";
                }
            }
            tempvo.setFormNo(epayments);
            tempvo.setRemark(amounts);
            tempvo.setPaymodes(paymodes);
            tempvo.setHasMidwayPayment(paymentStatus);
            // tempvo.setCheckNo(receipts);

            result.add(tempvo);
        }
        return result;
    }

    public int invoiceReceive(String[] id) throws DAOException {

        String strSQL = "";
        StringBuffer sql = new StringBuffer(
                "UPDATE teflow_@systemid_05 set @got_invoice = '01', @remark = isnull(@remark,'') + '; '+convert(varchar,getdate())+' 收到发票' where request_no = '@requestNo'");
        ParamConfigHelper param = ParamConfigHelper.getInstance();

        StringBuffer sql2 = new StringBuffer("INSERT INTO teflow_ebank_exp_history VALUES('@requestNo', 12, '"
                + new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new java.util.Date()) + "')");

        for (int i = 0; i < id.length; i++) {
            if (id[i].indexOf("=") < 0)
                continue;
            strSQL = sql.toString();
            strSQL = strSQL.replaceAll("@systemid", id[i].split("=")[1]);
            strSQL = strSQL.replaceAll("@requestNo", id[i].split("=")[0]);
            strSQL = strSQL.replaceAll("@got_invoice", param.getParamValue("invoice_got"));
            strSQL = strSQL.replaceAll("@remark", param.getParamValue("invoice_remark"));
            System.out.println(strSQL);
            dbManager.executeUpdate(strSQL);

            strSQL = sql2.toString();
            strSQL = strSQL.replaceAll("@requestNo", id[i].split("=")[0]);
            System.out.println(strSQL);
            dbManager.executeUpdate(strSQL);
        }
        return 0;
    }

    public int caRepaid(String[] id) throws DAOException {

        String strSQL = "";
        StringBuffer sql = new StringBuffer("INSERT INTO teflow_ebank_exp_history VALUES('@requestNo', 2, '"
                + new SimpleDateFormat("MM/dd/yyyy").format(new java.util.Date()) + "')");

        for (int i = 0; i < id.length; i++) {
            strSQL = sql.toString();
            if (id[i].indexOf("=") < 0)
                continue;
            strSQL = strSQL.replaceAll("@requestNo", id[i].split("=")[0]);
            dbManager.executeUpdate(strSQL);
        }
        return 0;
    }

    public Collection paymentQuery(String[] fid, String begin, String end, String paymode, String cashierStaffCD,
            String orderPhrase, boolean mergeAccount, boolean writeHistory) throws DAOException {

        String strSQL = "";
        String strSQL2 = "";
        String strSQL3 = "";
        String strSQLLoop = "";
        StringBuffer sql = new StringBuffer(
                "SELECT DISTINCT a.request_no,e.@payBank as payBank, e.@payAcc as payAcc, k.@payName as payName, e.@amount as amount, h.request_no as remark ");// e.@remark
        // as
        // remark
        // ");
        sql.append(" FROM (teflow_wkf_process a left outer join teflow_ebank_exp_history h on a.request_no=h.request_no and h.type =1),teflow_wkf_define b,teflow_form c, teflow_wkf_process_trace f, ");
        sql.append(" (teflow_@systemid_05 e LEFT OUTER JOIN teflow_bank k ON e.@payAcc=k.account_code )");
        sql.append(" WHERE b.form_system_id=@systemid and a.status='04' and f.handle_date >= a.receiving_date and e.@amount > 0 ");
        sql.append(" and @payMode='" + paymode + "'");
        // sql.append(" and (h.type =1 or h.type is null) ");
        // sql.append(" and a.request_staff_code = s.staff_code " );
        // sql.append(" and k.org_id in (null,'"+orgId+"')" );

        sql.append(" and a.flow_id=b.flow_id and b.form_system_id = c.form_system_id ");
        sql.append(" and a.request_no=e.request_no and a.request_no=f.request_no ");
        sql.append(" and a.previous_processor= '" + cashierStaffCD + "'");

        String endDateStr = "";
        if (end != null && !"".equals(end)) {
            endDateStr = end + " 23:59:59";
        }
        if (begin != null && !"".equals(begin)) {
            if (!"".equals(endDateStr)) {
                sql.append(" and f.handle_date between '").append(begin + " 00:00:00' and '").append(endDateStr)
                        .append("'");
            } else {
                sql.append(" and f.handle_date >='").append(begin + " 00:00:00'");
            }
        } else {
            if (!"".equals(endDateStr)) {
                sql.append(" and f.handle_date <='").append(endDateStr).append("'");
            }
        }

        // if(orderPhrase!=null && !"".equals(orderPhrase)){
        // sql.append(" order by "+orderPhrase);
        // } else{
        sql.append(" order by payBank");
        // }

        ParamConfigHelper param = ParamConfigHelper.getInstance();
        // strSQL = sql.toString().replaceAll("@pay_date", param.getParamValue("payment_pay_date"));
        strSQL = sql.toString().replaceAll("@amount", param.getParamValue("payment_amount"));
        strSQL = strSQL.replaceAll("@payBank", param.getParamValue("payment_pay_bank"));
        strSQL = strSQL.replaceAll("@payAcc", param.getParamValue("payment_pay_acc"));
        strSQL = strSQL.replaceAll("@payName", param.getParamValue("payment_pay_name"));
        strSQL = strSQL.replaceAll("@payMode", param.getParamValue("payment_pay_mode"));

        Collection result = new ArrayList();
        HashMap accMap = new HashMap();
        List sortList = new ArrayList();
        DecimalFormat df = new DecimalFormat("#.00");

        for (int i = 0; i < fid.length; i++) {
            ;
            strSQLLoop = strSQL.replaceAll("@systemid", fid[i]);

            // System.out.println(strSQLLoop);

            Collection list = dbManager.query(strSQLLoop);

            if (list == null || list.size() == 0)
                continue;
            Iterator it = list.iterator();
            while (it.hasNext()) {
                HashMap map = (HashMap) it.next();
                PaymentVO tempvo = new PaymentVO();
                tempvo.setRequestNo((String) map.get("REQUEST_NO"));
                tempvo.setFormName((String) map.get("FORM_NAME"));
                tempvo.setPayBank((String) map.get("PAYBANK"));
                tempvo.setPayAccount((String) map.get("PAYACC"));
                tempvo.setPayName((String) map.get("PAYNAME"));
                tempvo.setAmount(Double.parseDouble((String) map.get("AMOUNT")));
                tempvo.setRemark(map.get("REMARK") == null ? "" : "已有导出记录");

                // Merge the verder info
                strSQL2 = "select @payeeBank as payeeBank,@payeeAcc as payeeAcc,@payeeName as payeeName,@province as province,@city as city, @remark as purpose"
                        + " from @venderInfo where d.request_no='" + tempvo.getRequestNo() + "'";

                strSQL2 = strSQL2.replaceAll("@venderInfo",
                        param.getParamValue("payment_vender_info_table").split(";")[i]);
                strSQL2 = strSQL2.replaceAll("@payeeBank", param.getParamValue("payment_payee_bank").split(";")[i]);
                strSQL2 = strSQL2.replaceAll("@payeeAcc", param.getParamValue("payment_payee_acc").split(";")[i]);
                strSQL2 = strSQL2.replaceAll("@payeeName", param.getParamValue("payment_payee_name").split(";")[i]);
                strSQL2 = strSQL2.replaceAll("@province", param.getParamValue("payment_province").split(";")[i]);
                strSQL2 = strSQL2.replaceAll("@city", param.getParamValue("payment_city").split(";")[i]);
                strSQL2 = strSQL2.replaceAll("@remark", param.getParamValue("payment_remark").split(";")[i]);

                Collection list2 = dbManager.query(strSQL2.replaceAll("@requestNo", tempvo.getRequestNo()));
                // System.out.println(strSQL2);

                Iterator it2 = list2.iterator();
                if (it2.hasNext()) {
                    HashMap map2 = (HashMap) it2.next();
                    tempvo.setPayeeBank((String) map2.get("PAYEEBANK"));
                    tempvo.setPayeeAccount((String) map2.get("PAYEEACC"));
                    tempvo.setPayeeName((String) map2.get("PAYEENAME"));
                    tempvo.setPayeeProvince((String) map2.get("PROVINCE"));
                    tempvo.setPayeeCity((String) map2.get("CITY"));
                    tempvo.setPurpose(map2.get("PURPOSE") == null ? "" : (String) map2.get("PURPOSE"));
                }

                if (writeHistory) {
                    strSQL3 = "delete from teflow_ebank_exp_history where request_no='" + tempvo.getRequestNo()
                            + "' and type=1";
                    dbManager.executeUpdate(strSQL3);
                    System.out.println(strSQL3);
                    strSQL3 = "insert into teflow_ebank_exp_history(request_no,type) values ('" + tempvo.getRequestNo()
                            + "', 1)";
                    dbManager.executeUpdate(strSQL3);
                }

                // TODO: tempvo.getRequestNo() become DIRTY after below process!!
                if (mergeAccount) {
                    String reqNoAmt = tempvo.getRequestNo() + "(" + df.format(tempvo.getAmount()) + ")";
                    PaymentVO venderEbank = (PaymentVO) accMap.get(tempvo.getPayAccount() + "$"
                            + tempvo.getPayeeAccount());
                    if (venderEbank == null) {
                        if (!"".equals(tempvo.getRemark())) {
                            tempvo.setRemark(tempvo.getRemark() + "(" + tempvo.getRequestNo() + "). ");
                        }
                        if (!"".equals(tempvo.getPurpose())) {
                            tempvo.setPurpose(tempvo.getPurpose() + "; ");
                        }
                        tempvo.setRequestNo(reqNoAmt);
                        accMap.put(tempvo.getPayAccount() + "$" + tempvo.getPayeeAccount(), tempvo);
                        sortList.add(tempvo.getPayAccount() + "$" + tempvo.getPayeeAccount());
                    } else {
                        venderEbank.setRequestNo(venderEbank.getRequestNo() + ", " + reqNoAmt);
                        venderEbank.setAmount(venderEbank.getAmount() + tempvo.getAmount());
                        if (!"".equals(tempvo.getRemark())) {
                            venderEbank.setRemark(venderEbank.getRemark() + tempvo.getRemark() + "("
                                    + tempvo.getRequestNo() + "). ");
                        }
                        if (!"".equals(tempvo.getPurpose())) {
                            venderEbank.setPurpose(venderEbank.getPurpose() + tempvo.getPurpose() + "; ");
                        }
                    }
                } else {
                    result.add(tempvo);
                }
            }
        }
        if (mergeAccount) {
            if (orderPhrase != null && orderPhrase.endsWith("Desc"))
                Collections.sort(sortList, Collections.reverseOrder());
            else
                Collections.sort(sortList);

            for (int i = 0; i < sortList.size(); i++) {
                String account = (String) sortList.get(i);
                result.add(accMap.get(account));
            }
        }

        return result;
    }

    public Collection staffQuery(String[] fid, String begin, String end, String paymode, String cashierStaffCD,
            String orderPhrase, boolean mergeAccount, boolean writeHistory, HashMap amountMap) throws DAOException {
        String strSQL = "";
        String strSQL3 = "";
        StringBuffer sql = new StringBuffer(
                "SELECT DISTINCT a.request_no,a.request_staff_code,d.@amount as amount,s.bank_detail,s.account_no as PAYEEACC, h.request_no as history, r.remark, s.chinese_name as cnname, m.org_id, m.org_name");
        sql.append(" from (teflow_wkf_process a left outer join teflow_ebank_exp_history h on a.request_no=h.request_no "
                + "left outer join teflow_ebank_exp_remark r on a.request_no = r.request_no),"
                + "teflow_wkf_define b,teflow_form c,teflow_@systemid_05 d,teflow_wkf_process_trace f,tpma_staffbasic s,teflow_company m");
        sql.append(" where b.form_system_id=@systemid and a.status='04' and f.handle_date >= a.receiving_date ");
        sql.append(" and (@payMode='" + paymode + "' or @payMode is null) and d.@amount > 0 ");
        sql.append(" and a.request_staff_code = s.staff_code ");// and s.org_id='"+orgId+"'");
        sql.append(" and s.org_id = m.org_id ");
        sql.append(" and a.previous_processor = '" + cashierStaffCD + "'");
        sql.append(" and (h.type =1 or h.type is null) ");

        String endDateStr = "";
        if (end != null && !"".equals(end)) {
            endDateStr = end + " 23:59:59";
        }
        if (begin != null && !"".equals(begin)) {
            if (!"".equals(endDateStr)) {
                sql.append(" and f.handle_date between '").append(begin + " 00:00:00' and '").append(endDateStr)
                        .append("'");
            } else {
                sql.append(" and f.handle_date >='").append(begin + " 00:00:00'");
            }
        } else {
            if (!"".equals(endDateStr)) {
                sql.append(" and f.handle_date <='").append(endDateStr).append("'");
            }
        }
        sql.append(" and a.flow_id=b.flow_id and b.form_system_id = c.form_system_id and a.request_no=d.request_no and a.request_no=f.request_no ");

        if (orderPhrase != null && !"".equals(orderPhrase)) {
            // System.out.println("orderPhrase: " + orderPhrase);
            // sql.append(" order by " + orderPhrase);
            sql.append(" ORDER BY m.org_id, a.request_no ");
        } else {
            if (mergeAccount) {
                sql.append(" ORDER BY m.org_id, s.account_no, a.request_no ");
            } else {
                sql.append(" ORDER BY m.org_id, a.request_no ");
            }
        }

        ParamConfigHelper param = ParamConfigHelper.getInstance();
        // strSQL = sql.toString().replaceAll("@pay_date", param.getParamValue("payment_pay_date"));
        strSQL = sql.toString().replaceAll("@amount", param.getParamValue("staff_amount"));
        strSQL = strSQL.replaceAll("@payMode", param.getParamValue("staff_pay_mode"));
        // strSQL = strSQL.replaceAll("@payeeAcc", param.getParamValue("staff_payee_acc"));

        Collection result = new ArrayList();
        HashMap accMap = new HashMap();
        List sortList = new ArrayList();
        DecimalFormat df = new DecimalFormat("#.00");

        double totalAmount = 0;

        for (int i = 0; i < fid.length; i++) {
            // System.out.println(strSQL.replaceAll("@systemid", fid[i]));
            Collection list = dbManager.query(strSQL.replaceAll("@systemid", fid[i]));
            if (list == null || list.size() == 0)
                continue;
            Iterator it = list.iterator();
            while (it.hasNext()) {
                HashMap map = (HashMap) it.next();
                StaffEbankVO tempvo = new StaffEbankVO();
                tempvo.setRequestNo((String) map.get("REQUEST_NO"));
                tempvo.setPayeeBank((String) map.get("BANK_DETAIL"));
                tempvo.setPayeeAccount((String) map.get("PAYEEACC"));
                tempvo.setRequestStaffCode((String) map.get("REQUEST_STAFF_CODE"));
                tempvo.setAmount(Double.parseDouble((String) map.get("AMOUNT")));
                tempvo.setRemark(map.get("HISTORY") == null ? "" : "已有导出记录");
                if (map.get("REMARK") != null) {
                    String sp = "".equals(tempvo.getRemark()) ? "" : " ";
                    tempvo.setRemark(tempvo.getRemark() + sp + map.get("REMARK"));
                }
                tempvo.setRequestStaffCNName((String) map.get("CNNAME"));
                tempvo.setOrgId((String) map.get("ORG_ID"));
                tempvo.setOrgName((String) map.get("ORG_NAME"));

                totalAmount += tempvo.getAmount();

                if (mergeAccount) {
                    String reqNoAmt = tempvo.getRequestNo() + "(" + df.format(tempvo.getAmount()) + ")";
                    StaffEbankVO staffEbank = (StaffEbankVO) accMap.get(tempvo.getPayeeAccount());
                    if (staffEbank == null) {
                        if (!"".equals(tempvo.getRemark())) {
                            tempvo.setRemark(tempvo.getRemark() + "(" + tempvo.getRequestNo() + "). ");
                        }
                        tempvo.setRequestNo(reqNoAmt);
                        accMap.put(tempvo.getPayeeAccount(), tempvo);
                        sortList.add(tempvo.getOrgId() + "_" + tempvo.getPayeeAccount());
                    } else {
                        staffEbank.setRequestNo(staffEbank.getRequestNo() + ", " + reqNoAmt);
                        staffEbank.setAmount(staffEbank.getAmount() + tempvo.getAmount());
                        if (!"".equals(tempvo.getRemark())) {
                            staffEbank.setRemark(staffEbank.getRemark() + tempvo.getRemark() + "("
                                    + tempvo.getRequestNo() + "). ");
                        }
                    }
                } else {
                    accMap.put(tempvo.getRequestNo(), tempvo);
                    sortList.add(tempvo.getOrgId() + "#" + tempvo.getRequestNo());
                }

                if (writeHistory) {
                    strSQL3 = "delete from teflow_ebank_exp_history where request_no='" + tempvo.getRequestNo()
                            + "' and type=1 ";
                    dbManager.executeUpdate(strSQL3);
                    strSQL3 = "insert into teflow_ebank_exp_history(request_no,type) values ('" + tempvo.getRequestNo()
                            + "',1)";
                    dbManager.executeUpdate(strSQL3);
                }
            }
        }

        amountMap.put("totalAmount", totalAmount);

        if (mergeAccount) {
            if (orderPhrase != null && orderPhrase.endsWith("Desc"))
                Collections.sort(sortList, Collections.reverseOrder());
            else
                Collections.sort(sortList);

            for (int i = 0; i < sortList.size(); i++) {
                String account = ((String) sortList.get(i)).split("_")[1];
                result.add(accMap.get(account));
            }
        } else {
            Collections.sort(sortList);
            for (int i = 0; i < sortList.size(); i++) {
                String reqNo = ((String) sortList.get(i)).split("#")[1];
                result.add(accMap.get(reqNo));
            }
        }

        return result;
    }

    public void savePaymentRemark(String requestNo, String remark) throws DAOException {
        String SQL_QRY = "select request_no from teflow_wkf_process where request_no = ?";
        String SQL_INS = "insert into teflow_ebank_exp_remark (request_no, remark) values (?, ?)";
        String SQL_DEL = "delete from teflow_ebank_exp_remark where request_no = ?";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(SQL_QRY);
            int i = 1;
            stm.setString(i++, requestNo);
            ResultSet rs = stm.executeQuery();
            if (!rs.next()) {
                return;
            }
            stm.close();

            stm = conn.prepareStatement(SQL_DEL);
            i = 1;
            stm.setString(i++, requestNo);
            stm.executeUpdate();
            stm.close();

            stm = conn.prepareStatement(SQL_INS);
            i = 1;
            stm.setString(i++, requestNo);
            stm.setString(i++, remark);
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

    public List queryInterPayment(WorkFlowProcessVO param, List<String[]> tableConfig) throws DAOException {
        if (tableConfig.isEmpty()) {
            return new ArrayList();
        }

        String requestOrgId = param.getOrgId();
        String costOrgId = param.getOrgName();

        SimpleDateFormat dateFormat1 = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
        String completeDateFrom = param.getBeginCompleteDate();
        String completeDateTo = param.getEndCompleteDate();
        try {
            completeDateFrom = completeDateFrom == null || "".equals(completeDateFrom) ? ""
                    : " and (a.handle_date >= '" + dateFormat2.format(dateFormat1.parse(completeDateFrom)) + "')";
            completeDateTo = completeDateTo == null || "".equals(completeDateTo) ? "" : " and (a.handle_date < '"
                    + dateFormat2.format(dateFormat1.parse(completeDateTo)) + " 23:59:59.999')";
        } catch (ParseException ex) {
            ex.printStackTrace();
            throw new DAOException(ex);
        }

        String SQL = "select * from (";
        String union = "";
        String fInvoice = ParamConfigHelper.getInstance().getParamValue("invoice_got");
        String fInv_s = fInvoice.split("_")[1];
        for (String[] config : tableConfig) {
            SQL += union;
            SQL += "select a.request_no, a.handle_date, a.org_id, a.team_code, a.request_staff_code, a.status, ";
            String tb = "teflow_" + config[0] + "_" + config[1];
            String fContent = "b.field_" + config[1] + "_" + config[2];
            String fAmount = "b.field_" + config[1] + "_" + config[3];
            String tc = config.length == 5 ? "teflow_" + config[0] + "_" + fInv_s : null;
            SQL += fContent + " as content, " + fAmount + " as amount, b.cost_company, b.id as itemId, "
                    + (tc == null ? "null" : "c." + fInvoice) + " as inv_status ";
            SQL += "from teflow_wkf_process a, " + tb + " b" + (tc == null ? " " : ", " + tc + " c ");
            SQL += "where (a.request_no = b.request_no)" + (tc == null ? "" : " and (a.request_no = c.request_no)")
                    + " and (a.status = '04') and (a.org_id = '" + requestOrgId + "') and (b.cost_company = '"
                    + costOrgId + "')" + completeDateFrom + completeDateTo;

            union = " union ";
        }
        SQL += ") a order by handle_date, request_no, itemId";
        // System.out.println("SQL: " + SQL);

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(SQL);
            ResultSet rs = stm.executeQuery();
            List forms = new ArrayList();
            String requestNo = "";
            int noteNo = 0;
            while (rs.next()) {
                InterPaymentVO vo = new InterPaymentVO();
                vo.setDate(rs.getDate("handle_date"));
                vo.setRequestNo(rs.getString("request_no"));
                if (!requestNo.equals(vo.getRequestNo())) {
                    requestNo = vo.getRequestNo();
                    noteNo = 0;
                }
                noteNo++;
                vo.setNoteNo(requestNo + "-" + noteNo);
                vo.setCostOrg(rs.getString("cost_company"));
                vo.setRequestOrg(rs.getString("org_id"));
                vo.setTeam(rs.getString("team_code"));
                vo.setRequestBy(rs.getString("request_staff_code"));
                vo.setStatus(rs.getString("status"));
                vo.setContent(rs.getString("content"));
                vo.setAmount(rs.getBigDecimal("amount"));
                vo.setInvStatus(rs.getString("inv_status"));
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
