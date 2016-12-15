package com.aiait.eflow.formmanage.dao;

import java.sql.*;
import java.util.*;

import com.aiait.eflow.common.CommonName;
import com.aiait.eflow.common.helper.SystemFieldHelper;
import com.aiait.eflow.formmanage.vo.*;
import com.aiait.eflow.util.*;
import com.aiait.framework.common.DataType;
import com.aiait.framework.db.*;
import com.aiait.framework.exception.DAOException;
import com.aiait.framework.vo.BaseVO;

public class FormManageDAO extends BaseDAOImpl {

    public FormManageDAO(IDBManager dbManager) {
        super(dbManager);
    }

    public void executeSQL(String sql) throws DAOException {
        dbManager.executeUpdate(sql);
    }

    public void copyfileName(String requestno, FormSectionVO sectionVo, String destfilename,
            FormAttachedFileVO attachedfile) throws DAOException {
        String SQL = "insert into " + sectionVo.getTableName()
                + " (file_name, file_description, request_no) values(?, ?, ?)";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(SQL);
            int i = 1;
            stm.setString(i++, destfilename);
            stm.setString(i++, attachedfile.getFieldescription());
            stm.setString(i++, requestno);
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

    public Collection getAttachedfiles(FormSectionVO sectionVo, String requestno) throws DAOException {
        Collection attachedfiles = new ArrayList();
        String strSql = "select file_name, file_description,id from " + sectionVo.getTableName()
                + " where request_no='" + requestno + "'";
        Collection list = dbManager.query(strSql);
        if (list == null || list.size() == 0)
            return null;
        Iterator it = list.iterator();
        while (it.hasNext()) {
            HashMap map = (HashMap) it.next();
            FormAttachedFileVO formattachedfilevo = new FormAttachedFileVO();
            formattachedfilevo.setFilename((String) map.get("FILE_NAME"));
            formattachedfilevo.setFieldescription((String) map.get("FILE_DESCRIPTION"));
            formattachedfilevo.setId((String) map.get("ID"));
            formattachedfilevo.setRequestno((String) map.get("REQUEST_NO"));
            attachedfiles.add(formattachedfilevo);
        }
        return attachedfiles;
    }

    private Collection createFillSectionField(FormSectionVO sectionVo) throws DAOException {

        String strSql = "SELECT fill_section_fields FROM teflow_wkf_detail WHERE (flow_id = "
                + "(SELECT flow_id FROM teflow_wkf_define WHERE form_system_id = '" + sectionVo.getFormSystemId()
                + "')) " + "AND (fill_section_fields <> NULL OR fill_section_fields <> '')";

        Collection list = dbManager.query(strSql);
        if (list == null || list.size() == 0)
            return new ArrayList();
        HashMap map = (HashMap) list.iterator().next();
        String Fill_Section_Fields = (String) map.get("FILL_SECTION_FIELDS");

        Collection fieldbox = new ArrayList();
        String[] FillSecFieldArray = StringUtil.split(Fill_Section_Fields, ",");
        for (int i = 0; i < FillSecFieldArray.length; i++) {
            String tmpstr = FillSecFieldArray[i];
            String[] field = StringUtil.split(tmpstr, ".");
            if (field[0].equals(sectionVo.getSectionId())) {
                fieldbox.add(field[1]);
            }
        }

        return fieldbox;
    }

    public void copySectionRecord(String oldRequestNo, String newRequestNo, FormSectionVO sectionVo)
            throws DAOException {

        ArrayList FillSectionField = (ArrayList) createFillSectionField(sectionVo);
        // for(int i=0;i<FillSectionField.size();i++){
        // System.out.println(FillSectionField.get(i));
        // }

        String copySQL = "insert into " + sectionVo.getTableName();
        StringBuffer columnStr = new StringBuffer("");
        StringBuffer selectStr = new StringBuffer("select ");
        Collection fieldList = sectionVo.getFieldList();
        Iterator fieldIt = fieldList.iterator();
        boolean hasRequestNo = false;
        while (fieldIt.hasNext()) {

            FormSectionFieldVO field = (FormSectionFieldVO) fieldIt.next();

            if (!FillSectionField.contains(field.fieldId)) {
                if (field.getFieldType() == CommonName.FIELD_TYPE_IDENTITY)
                    continue;
                columnStr.append(field.getFieldId()).append(",");
                if ("REQUEST_NO".equals(field.getFieldId().toUpperCase())) {
                    selectStr.append("'" + newRequestNo + "'").append(",");
                    hasRequestNo = true;
                } else if ("REQUEST_DATE".equals(field.getFieldId().toUpperCase())) {
                    selectStr.append("'" + StringUtil.getDateStr(new java.util.Date(), "MM/dd/yyyy HH:mm:ss")).append(
                            "',");
                } else if (field.getFieldType() == CommonName.FIELD_TYPE_REFFORM
                        && CommonName.FORM_SECTION_TYPE_BASIC.equals(sectionVo.getSectionType())
                        && field.getFieldLabel().toLowerCase().indexOf("copy") >= 0) {
                    selectStr.append("'" + oldRequestNo + "." + sectionVo.getFormSystemId() + "',");
                } else {
                    selectStr.append(field.getFieldId()).append(",");
                }

            }
        }
        // 如果没有包含“request_no”,则需要添加进去
        if (!hasRequestNo) {
            selectStr.append("'" + newRequestNo + "'").append(",");
            columnStr.append("request_no,");
        }
        // 去掉最后的“,”
        if (columnStr.length() > 0) {
            columnStr = new StringBuffer(columnStr.substring(0, columnStr.length() - 1));
        }
        if (selectStr.length() > 0) {
            selectStr = new StringBuffer(selectStr.substring(0, selectStr.length() - 1));
        }
        copySQL = copySQL + "(" + columnStr.toString() + ") " + selectStr.toString() + " from "
                + sectionVo.getTableName() + " where request_no='" + oldRequestNo + "'";
        // System.out.println(copySQL);
        dbManager.executeUpdate(copySQL);
    }

    /**
     * 获取指定的requestNo所对应的form
     * 
     * @param requestNo
     * @return
     * @throws DAOException
     */
    public FormManageVO getFormManageVObyRequestNo(String requestNo) throws DAOException {
        String sql = "select * from teflow_form where form_system_id in (select form_system_id from teflow_wkf_define where flow_id in (select flow_id from teflow_wkf_process where request_no='"
                + requestNo + "'))";
        Collection list = dbManager.query(sql);
        if (list == null || list.size() == 0)
            return null;
        HashMap map = (HashMap) list.iterator().next();
        return convertMap(map);
    }

    public void deleteRequestedFormContent(String requestNo) throws DAOException {
        String sql = "select section_type, table_name from teflow_form_section where form_system_id in (select form_system_id from teflow_wkf_define where flow_id in ("
                + "(select flow_id from teflow_wkf_process where request_no='" + requestNo + "')))";
        Collection list = dbManager.query(sql);
        if (list == null || list.size() == 0)
            return;
        Iterator it = list.iterator();
        while (it.hasNext()) {
            HashMap map = (HashMap) it.next();
            if (!"04".equals(map.get("SECTION_TYPE"))) { // 非04类型的section才存在对应的table
                String delSQL = "delete from " + (String) map.get("TABLE_NAME") + " where request_no='" + requestNo
                        + "' ";
                // System.out.println("delSQL: " + delSQL);
                dbManager.executeUpdate(delSQL);
            }
        }
    }

    public void deleteProcess(String requestNo) throws DAOException {
        String delSQL = "delete from teflow_wkf_process where request_no='" + requestNo + "' ";
        dbManager.executeUpdate(delSQL);
    }

    public void deleteProcessTrace(String requestNo) throws DAOException {
        String delSQL = "delete from teflow_wkf_process_trace where request_no='" + requestNo + "' ";
        dbManager.executeUpdate(delSQL);
    }

    public int getMaxFieldNo(int formSystemId, String sectionId) throws DAOException {
        String sql = "select Max(Convert(int,SUBSTRING(field_id, 1 + { fn LENGTH('field_" + sectionId
                + "_') }, { fn LENGTH(field_id) } - { fn LENGTH('field_" + sectionId
                + "') }))) as field_id from teflow_form_section_field where form_system_id=" + formSystemId
                + " and section_id='" + sectionId + "' and field_id like 'field_" + sectionId + "_%' ";
        Collection list = dbManager.query(sql);
        if (list == null || list.size() == 0)
            return 1;
        String fieldId = (String) ((HashMap) list.iterator().next()).get("FIELD_ID");
        if (fieldId != null && !"".equals(fieldId)) {
            String strNum = fieldId.substring(fieldId.lastIndexOf("_") + 1);
            int id = Integer.parseInt(strNum);
            return id + 1;
        } else {
            return 1;
        }
    }

    public void copyForm(int formSystemId) throws DAOException {
        Collection paramList = new ArrayList();
        paramList.add("" + formSystemId);
        dbManager.prepareCall("poef_formmgr_copy_form", paramList);
    }

    public void revokeForm(int formSystemId) throws DAOException {
        String sql = "update teflow_form set status='1' where form_system_id=" + formSystemId;
        dbManager.executeUpdate(sql);
    }

    public int getFlowNumLinkedForm(int formSystemId) throws DAOException {
        String sql = "select count(*) from teflow_wkf_define where form_system_id=" + formSystemId;
        int num = dbManager.getRecordCount(sql);
        return num;
    }

    public void deleteFormBase(int formSystemId) throws DAOException {
        String sql = "delete from teflow_form where form_system_id=" + formSystemId;
        dbManager.executeUpdate(sql);
    }

    public void deleteFormAllSections(int formSystemId) throws DAOException {
        String sql = "delete from teflow_form_section where form_system_id=" + formSystemId;
        dbManager.executeUpdate(sql);
    }

    public void deleteFormAllFields(int formSystemId) throws DAOException {
        String sql = "delete from teflow_form_section_field where form_system_id=" + formSystemId;
        dbManager.executeUpdate(sql);
    }

    public String getMaxRequestNo(FormSectionFieldVO field, String prefixRequestNo) throws DAOException {
        // String sql = "select max(request_no) request_no from " + FieldUtil.getSectionDataTableName(field) + " where "
        // + "request_no like '%" + prefixRequestNo + "%'";
        int index = prefixRequestNo.length() + 1;
        String sql = "select max(convert(int, substring(request_no, " + index + ", 5))) maxno from "
                + FieldUtil.getSectionDataTableName(field) + " where request_no like '%" + prefixRequestNo + "%'";

        Collection list = dbManager.query(sql);
        if (list == null || list.size() == 0)
            return "";
        Iterator it = list.iterator();
        HashMap map = (HashMap) it.next();
        return (String) map.get("MAXNO");
    }

    /**
     * 
     * @param formSystemId
     * @param sectionId
     * @return true ---- 该sectionId已经存在； false---未存在
     * @throws DAOException
     */
    public boolean checkFormSectionId(int formSystemId, String sectionId) throws DAOException {
        String sql = "select * from teflow_form_section where form_system_id=" + formSystemId + " and section_id='"
                + sectionId + "'";
        Collection list = dbManager.query(sql);
        if (list == null || list.size() == 0)
            return false;
        return true;
    }

    /**
     * 检查指定的form“formSystemId”指定状态的section记录是否存在
     * 
     * @param formSystemId
     * @param sectionType
     * @return true ---存在；false---不存在
     * @throws DAOException
     */
    public boolean checkFormBySectionType(int formSystemId, String sectionType) throws DAOException {
        String sql = "select * from teflow_form_section where form_system_id=" + formSystemId + " and section_type='"
                + sectionType + "'";
        Collection list = dbManager.query(sql);
        if (list == null || list.size() == 0)
            return false;
        return true;
    }

    /**
     * 检查指定form是否完整（完整：存在section记录，并且每个section都存在至少一条field记录）
     * 
     * @param formId
     * @return false---不完整； true---完整
     * @throws DAOException
     */
    public boolean checkForm(int formSystemId) throws DAOException {
        String sql = "select section_id from teflow_form_section where form_system_id=" + formSystemId + "";
        Collection sectionList = dbManager.query(sql);
        if (sectionList == null || sectionList.size() == 0)
            return false;
        sectionList = null;
        sql = "select section_id from teflow_form_section where form_system_id=" + formSystemId
                + " and section_id not in(SELECT section_id from teflow_form_section_field where form_system_id="
                + formSystemId + ") AND section_type<>'04' ";
        sectionList = dbManager.query(sql);
        if (sectionList != null && sectionList.size() > 0)
            return false;
        return true;
    }

    public void publishForm(int formSystemId) throws DAOException {
        String updateSql = "update teflow_form set status='0' where form_system_id = " + formSystemId;

        Collection sectionList = getSectionListByForm(formSystemId);
        Iterator sectionIt = sectionList.iterator();
        while (sectionIt.hasNext()) {
            FormSectionVO section = (FormSectionVO) sectionIt.next();
            if (!"04".equals(section.getSectionType())) { // 非04类型的都需要创建数据库表
                createSectionTable(section);
            }
        }

        dbManager.executeUpdate(updateSql);

    }

    public void disableForm(int formSystemId) throws DAOException {
        String sql = "update teflow_form set status='2' where  form_system_id = " + formSystemId;
        dbManager.executeUpdate(sql);
    }

    public void createSectionTable(FormSectionVO section) throws DAOException {
        // section对应数据保存的表名格式是固定的：teflow_form的ID_section的ID
        StringBuffer sql = new StringBuffer("create table dbo.teflow_" + section.getFormSystemId()).append("_")
                .append(section.getSectionId()).append(" ( ");

        Collection fieldList = section.getFieldList();
        Iterator fieldIt = fieldList.iterator();
        // int length = 50;
        while (fieldIt.hasNext()) {
            FormSectionFieldVO field = (FormSectionFieldVO) fieldIt.next();
            sql.append(getColumnStr(field)).append(",");
        }
        // 对于非baseInformation section则需要自动加上系统字段request_no
        if (!"03".equals(section.getSectionType())) {
            FormSectionFieldVO requestVo = SystemFieldHelper.getInstance().getBasicFieldById(
                    CommonName.SYSTEM_ID_REQUEST_NO);
            sql.append(getColumnStr(requestVo)).append(",");
        }
        //2014-5-7
      //20150413 hinson begin
//        if ("00".equals(section.getSectionType())){
//        	sql.append("[file_name] [varchar](100) NULL, [file_description] [varchar](100) NULL, [id] [numeric](12, 0) IDENTITY(1,1) NOT NULL,");
//        }
      //20150413 hinson end
        // 对于是表格记录形式的section则需要自动增加一个递增字段（attached file section也属于该类）
        // if("01".equals(section.getSectionType()) || "00".equals(section.getSectionType())){
        // sql.append(" id numeric(12,0) IDENTITY(1,1) NOT NULL,");
        // }

        sql = new StringBuffer(sql.toString().substring(0, sql.toString().length() - 1));

        sql.append(") on [PRIMARY]");

        dbManager.executeUpdate("if exists (select [name] from SysObjects where [NAME]='teflow_"
                + section.getFormSystemId() + "_" + section.getSectionId() + "')  drop table dbo.teflow_"
                + section.getFormSystemId() + "_" + section.getSectionId());
        //System.out.println("create table sql : " + sql.toString());
        dbManager.executeUpdate(sql.toString());
        // dbManager.executeUpdate("GRANT  SELECT ,  UPDATE ,  INSERT ,  DELETE  ON dbo.teflow_"+section.getFormSystemId()+"_"+section.getSectionId()+"  TO [pmauser]");
    }

    /**
     * 2007-11-26 修改说明：将字段都设为允许为空。不能为空的字段将在页面控制
     * 
     * @param field
     * @return
     */
    private String getColumnStr(FormSectionFieldVO field) {
        StringBuffer sql = new StringBuffer("");
        int length = 50;

        sql.append(field.getFieldId()).append(" ");
        if (field.getFieldLength() != 0) {
            length = field.getFieldLength();
        }
        switch (field.getDataType()) {
        case 1: // string
            sql.append(" VARCHAR (").append(length).append(") ");
            // if(field.getIsRequired()){
            // sql.append(" NOT NULL ");
            // }
            break;
        case 2: // date
            sql.append(" datetime ");
            // if(field.getIsRequired()){
            // sql.append(" NOT NULL ");
            // }
            break;
        case 3: // number
            sql.append(" numeric(");
            if (field.getFieldLength() > 0) {
                sql.append(field.getFieldLength());
            } else {
                sql.append("9");
            }
            if (field.getDecimalDigits() > 0) {
                sql.append("," + field.getDecimalDigits());
            } else {
                // sql.append(",0");
            }
            sql.append(")");
            if (field.getFieldType() == CommonName.FIELD_TYPE_IDENTITY) {
                sql.append(" IDENTITY(1,1) ");
                if (field.getIsRequired()) {
                    sql.append(" NOT NULL ");
                }
            } else {
                // if(field.getIsRequired()){
                // sql.append(" NOT NULL DEFAULT 0 ");
                // sql.append(" NOT NULL ");
                // }
            }
            break;
        }
        return sql.toString();
    }

    /**
     * 修改FORM对应的SECTION的字段：增加新字段
     * 
     * @param form
     * @throws DAOException
     */
    public void addSectionTableColumn(FormSectionFieldVO field) throws DAOException {
        // section对应数据保存的表名格式是固定的：teflow_form的ID_section的ID
        StringBuffer addSQL = new StringBuffer("alter table " + FieldUtil.getSectionDataTableName(field) + "  add ")
                .append(getColumnStr(field));
        // System.out.println("sql:"+addSQL.toString());
        dbManager.executeUpdate(addSQL.toString());
    }

    /**
     * 修改FORM对应的SECTION的字段：删除原有字段
     * 
     * @param form
     * @throws DAOException
     */
    public void delSectionTableColumn(FormSectionFieldVO field) throws DAOException {
        // section对应数据保存的表名格式是固定的：teflow_form的ID_section的ID
        StringBuffer delSQL = new StringBuffer("alter table " + FieldUtil.getSectionDataTableName(field)
                + "  drop COLUMN " + field.getFieldId() + " ");
        // System.out.println("sql:"+delSQL.toString());
        dbManager.executeUpdate(delSQL.toString());
    }

    /**
     * 修改FORM对应的SECTION的字段：修改指定字段的长度
     * 
     * @param form
     * @throws DAOException
     */
    public void alertSectionTableColumn(FormSectionFieldVO field) throws DAOException {
        StringBuffer alertSQL = new StringBuffer("alter table " + FieldUtil.getSectionDataTableName(field)
                + " ALTER COLUMN " + getColumnStr(field));
        // System.out.println("sql:"+alertSQL.toString());
        dbManager.executeUpdate(alertSQL.toString());
    }

    public void saveBaseForm(FormManageVO form) throws DAOException {
        // StringBuffer formSQL = new
        // StringBuffer("INSERT INTO teflow_form(form_id,form_name,form_description,status,create_date) ");
        // formSQL.append(" values('").append(form.getFormId()).append("','").append(form.getFormName()).append("','")
        // .append(form.getDescription()).append("','").append(form.getStatus()).append("','").append(new
        // Date()).append("')");
        // dbManager.executeUpdate(formSQL.toString());
        String sql = "INSERT INTO teflow_form(form_id,form_name,form_type,form_description,status,action_type,action_message,org_id,pre_validation_url,after_save_url,submitAlias,saveAlias) values(?,?,?,?,?,?,?,?,?,?,?,?)";
        // paramValues.add(new Date());
        Object[] obj = new Object[12];
        obj[0] = form.getFormId();
        obj[1] = form.getFormName();
        obj[2] = form.getFormType();
        obj[3] = form.getDescription();
        obj[4] = form.getStatus();
        obj[5] = form.getActionType();
        obj[6] = form.getActionMessage();
        obj[7] = form.getOrgId();
        obj[8] = form.getPre_validation_url();
        obj[9] = form.getAfterSaveUrl();
        obj[10] = form.getSubmitAlias();
        obj[11] = form.getSaveAlias();

        int[] dataType = {DataType.VARCHAR, DataType.VARCHAR, DataType.VARCHAR, DataType.VARCHAR, DataType.VARCHAR,
                DataType.VARCHAR, DataType.VARCHAR, DataType.VARCHAR, DataType.VARCHAR, DataType.VARCHAR,DataType.VARCHAR,DataType.VARCHAR};
        dbManager.executeUpdate(sql, obj, dataType);
    }

    public void updateBaseForm(FormManageVO form) throws DAOException {
        String sql = "UPDATE teflow_form SET form_id=?,form_name=?,form_type=?,form_description=?,action_type=?,action_message=?,org_id=?,pre_validation_url=?,after_save_url=?,submitAlias=?,saveAlias=? WHERE form_system_id=?";
        Object[] obj = new Object[12];
        obj[0] = form.getFormId();
        obj[1] = form.getFormName();
        obj[2] = form.getFormType();
        obj[3] = form.getDescription();
        obj[4] = form.getActionType();
        obj[5] = form.getActionMessage();
        obj[6] = form.getOrgId();
        obj[7] = form.getPre_validation_url();
        obj[8] = form.getAfterSaveUrl();
        obj[9] = form.getSubmitAlias();
        obj[10] = form.getSaveAlias();
        obj[11] = "" + form.getFormSystemId();
        int[] dataType = {DataType.VARCHAR, DataType.VARCHAR, DataType.VARCHAR, DataType.VARCHAR, DataType.VARCHAR,
                DataType.VARCHAR, DataType.VARCHAR, DataType.VARCHAR, DataType.VARCHAR,DataType.VARCHAR,DataType.VARCHAR, DataType.INT};
        dbManager.executeUpdate(sql, obj, dataType);
    }

    public void saveSection(FormSectionVO section) throws DAOException {
        String sql = "INSERT INTO teflow_form_section(form_system_id,section_id,section_type,section_remark,table_name) values(?,?,?,?,?)";
        Object[] obj = new Object[5];
        obj[0] = "" + section.getFormSystemId();
        obj[1] = section.getSectionId();
        obj[2] = section.getSectionType();
        obj[3] = section.getSectionRemark();
        obj[4] = section.getTableName();
        int[] dataType = {DataType.INT, DataType.VARCHAR, DataType.VARCHAR, DataType.VARCHAR, DataType.VARCHAR};
        dbManager.executeUpdate(sql, obj, dataType);
    }

    public void saveFormSecitonField(FormSectionFieldVO field) throws DAOException {
        // IT0958 begin
        String sql = "INSERT INTO teflow_form_section_field(form_system_id,section_id,field_id,field_label,field_type,is_required,data_type,field_length,source_type,source_sql,order_id,decimal_digits,high_level,is_money,is_singlerow,controls_width,controls_height,field_comments,comment_content,"
                + "default_value,event_click,event_dbclick,event_onfocus,event_onblur,event_onchange,is_readonly,report_type,report_system_id,is_disabled)";
        sql = sql + " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        Object[] obj = new Object[29];

        obj[0] = "" + field.getFormSystemId();
        obj[1] = field.getSectionId();
        obj[2] = field.getFieldId();
        
        obj[3] = field.getFieldLabel();
        obj[4] = "" + field.getFieldType();
        if (field.getIsRequired()) {
            obj[5] = "0";
        } else {
            obj[5] = "-1";
        }
        obj[6] = "" + field.getDataType();
        obj[7] = "" + field.getFieldLength();
        obj[8] = "" + field.getFieldDataSourceType();
        obj[9] = field.getFieldDataSrSQL();
        obj[10] = "" + field.getOrderId();
        obj[11] = "" + field.getDecimalDigits();
        obj[12] = "" + field.getHighLevel();
        obj[13] = "" + field.getIsMoney();
        obj[14] = "" + field.getIsSingleRow();
        obj[15] = "" + field.getControlsWidth();
        obj[16] = "" + field.getControlsHeight();
        obj[17] = field.getFieldComments();
        obj[18] = field.getCommentContent();
        obj[19] = field.getDefaultValue();
        obj[20] = field.getClickEvent();
        obj[21] = field.getDbclickEvent();
        obj[22] = field.getOnfocusEvent();
        obj[23] = field.getLostfocusEvent();
        obj[24] = field.getChangeEvent();
        obj[25] = "" + field.getIsReadonly();
        obj[26] = field.getReportType();
        obj[27] = "" + field.getReportSystemId();
        obj[28]=""+field.getIsDisabled();
        int[] dataType = {DataType.INT, DataType.VARCHAR, DataType.VARCHAR, DataType.VARCHAR, DataType.INT,
                DataType.INT, DataType.INT, DataType.INT, DataType.INT, DataType.VARCHAR, DataType.INT, DataType.INT,
                DataType.INT, DataType.INT, DataType.INT, DataType.INT, DataType.INT, DataType.VARCHAR,
                DataType.VARCHAR, DataType.VARCHAR, DataType.VARCHAR, DataType.VARCHAR, DataType.VARCHAR,
                DataType.VARCHAR, DataType.VARCHAR, DataType.INT, DataType.VARCHAR, DataType.INT,DataType.INT};
        // IT0958 end
        dbManager.executeUpdate(sql, obj, dataType);
    }

    public void updateFormSectionField(FormSectionFieldVO field) throws DAOException {
        // IT0958 begin
        String sql = "UPDATE teflow_form_section_field set field_label=?,field_type=?,is_required=?,data_type=?,field_length=?,source_type=?,source_sql=?,order_id=?,decimal_digits=?,high_level=?,is_money=?,is_singlerow=?,controls_width=?,controls_height=?,field_comments=?,comment_content=?,"
                + "default_value=?,event_click=?,event_dbclick=?,event_onfocus=?,event_onblur=?,event_onchange=?,is_readonly=?,report_type=?,report_system_id=?,is_disabled=? ";
        sql = sql + " WHERE form_system_id=? and section_id=? and field_id=?";
        Object[] obj = new Object[29];
        obj[0] = field.getFieldLabel();
        obj[1] = "" + field.getFieldType();
        if (field.getIsRequired()) {
            obj[2] = "0";
        } else {
            obj[2] = "-1";
        }
        obj[3] = "" + field.getDataType();
        obj[4] = "" + field.getFieldLength();
        obj[5] = "" + field.getFieldDataSourceType();
        obj[6] = field.getFieldDataSrSQL();
        obj[7] = "" + field.getOrderId();
        obj[8] = "" + field.getDecimalDigits();
        obj[9] = "" + field.getHighLevel();
        obj[10] = "" + field.getIsMoney();
        obj[11] = "" + field.getIsSingleRow();
        obj[12] = "" + field.getControlsWidth();
        obj[13] = "" + field.getControlsHeight();
        obj[14] = field.getFieldComments();
        obj[15] = field.getCommentContent();
        obj[16] = field.getDefaultValue();
        obj[17] = field.getClickEvent();
        obj[18] = field.getDbclickEvent();
        obj[19] = field.getOnfocusEvent();
        obj[20] = field.getLostfocusEvent();
        obj[21] = field.getChangeEvent();
        obj[22] = "" + field.getIsReadonly();
        
        obj[23] = field.getReportType();
        obj[24] = "" + field.getReportSystemId();
        obj[25] = "" + field.getIsDisabled();
        obj[26] = "" + field.getFormSystemId();
        obj[27] = field.getSectionId();
        obj[28] = field.getFieldId();
       
        int[] dataType = {DataType.VARCHAR, DataType.INT, DataType.INT, DataType.INT, DataType.INT, DataType.INT,
                DataType.VARCHAR, DataType.INT, DataType.INT, DataType.INT, DataType.INT, DataType.INT, DataType.INT,
                DataType.INT, DataType.VARCHAR, DataType.VARCHAR, DataType.VARCHAR, DataType.VARCHAR, DataType.VARCHAR,
                DataType.VARCHAR, DataType.VARCHAR, DataType.VARCHAR, DataType.INT,DataType.VARCHAR, DataType.INT,DataType.INT, DataType.INT, DataType.VARCHAR,
                DataType.VARCHAR};
        // IT0958 begin
        dbManager.executeUpdate(sql, obj, dataType);
    }

    public void deleteFormSectionField(int formSystemId, String sectionId, String fieldId) throws DAOException {
        String sql = "DELETE FROM teflow_form_section_field WHERE form_system_id=? and section_id=? and field_id=?";
        Object[] obj = new Object[3];
        obj[0] = "" + formSystemId;
        obj[1] = sectionId;
        obj[2] = fieldId;
        int[] dataType = {DataType.INT, DataType.VARCHAR, DataType.VARCHAR};
        dbManager.executeUpdate(sql, obj, dataType);
    }

    /**
     * 删除指定form的指定section
     * 
     * @param formSystemId
     * @param sectionId
     * @throws DAOException
     */
    public void deleteSection(int formSystemId, String sectionId) throws DAOException {
        dbManager.executeUpdate("delete from teflow_form_section_field where form_system_id=" + formSystemId + " and "
                + " section_id='" + sectionId + "'");
        dbManager.executeUpdate("delete from teflow_form_section where form_system_id=" + formSystemId + " and "
                + " section_id='" + sectionId + "'");
    }

    public Collection getFormList(String statusId, String formType) throws DAOException {
        Collection resultList = new ArrayList();
        StringBuffer formSQL = new StringBuffer("select * from teflow_form where 1=1 ");
        if (statusId != null && !"".equals(statusId)) {
            formSQL.append(" and status='").append(statusId).append("'");
        }
        if (formType != null && !"".equals(formType)) {
            formSQL.append(" and form_type='" + formType + "'");
        }
        formSQL.append(" order by form_id");
        Collection list = dbManager.query(formSQL.toString());
        if (list.size() <= 0)
            return resultList;
        Iterator it = list.iterator();
        while (it.hasNext()) {
            HashMap map = (HashMap) it.next();
            FormManageVO form = convertMap(map);
            resultList.add(form);
        }
        return resultList;
    }

    public Collection getFormList(FormManageVO form) throws DAOException {
        Collection resultList = new ArrayList();
        StringBuffer formSQL = new StringBuffer("select * from teflow_form where 1=1 ");
        if (form.getStatus() != null && !"".equals(form.getStatus())) {
            formSQL.append(" and status='").append(form.getStatus()).append("'");
        }
        if (form.getFormType() != null && !"".equals(form.getFormType())) {
            // IT0958-DS011 begin
            formSQL.append(" and form_type in (" + form.getFormType() + ") ");
            // IT0958-DS011 end
        }
        if (form.getFormId() != null && !"".equals(form.getFormId())) {
            formSQL.append(" and form_id like '%" + form.getFormId() + "%'");
        }
        if (form.getOrgId() != null && !"".equals(form.getOrgId())) {
            formSQL.append(" and org_id in (" + form.getOrgId() + ") ");
        }
        formSQL.append(" order by create_date desc");
        // System.out.println(formSQL.toString());
        Collection list = dbManager.query(formSQL.toString());
        if (list.size() <= 0)
            return resultList;
        Iterator it = list.iterator();
        while (it.hasNext()) {
            HashMap map = (HashMap) it.next();
            FormManageVO formVo = convertMap(map);
            resultList.add(formVo);
        }
        return resultList;
    }

    public Collection getFormList(String statusId) throws DAOException {
        return getFormList(statusId, null);
    }

    public Collection getNotLinkedForList() throws DAOException {
        Collection resultList = new ArrayList();
        String sql = "select * from teflow_form where status='0' and form_system_id not in (select form_system_id from teflow_wkf_define) ";
        Collection list = dbManager.query(sql);
        if (list.size() <= 0)
            return resultList;
        Iterator it = list.iterator();
        while (it.hasNext()) {
            HashMap map = (HashMap) it.next();
            FormManageVO form = convertMap(map);
            resultList.add(form);
        }
        return resultList;
    }

    /**
     * 获取指定公司下的指定类型的所有form的集合
     * 
     * @param formTypeId
     * @param orgId
     * @return
     * @throws DAOException
     */
    public Collection getNewFormListByType(String formTypeId, String orgId) throws DAOException {
        return getNewFormListByType(formTypeId, orgId, null);
    }

    /**
     * 获取指定公司下的指定类型的所有form(如果指定状态，则只获取该状态的form,否则获取所有form)集合
     * 
     * @param formTypeId
     * @param orgId
     * @return
     * @throws DAOException
     */
    public Collection getNewFormListByType(String formTypeId, String orgId, String status) throws DAOException {
        if (formTypeId == null || "".equals(formTypeId))
            return getAvailableForm(orgId, status);
        Collection resultList = new ArrayList();
        StringBuffer formSQL = new StringBuffer("select * from teflow_form where form_type='" + formTypeId
                + "' and form_system_id in (select form_system_id from teflow_wkf_define) ");
        if (orgId != null && !"".equals(orgId)) {
            formSQL.append(" and org_id in (").append(orgId).append(")");
        }
        if (status != null && !"".equals(status)) {
            formSQL.append(" and status='").append(status).append("' ");
        }
        formSQL.append(" order by form_id,form_name");
        // System.out.println(formSQL);
        Collection list = dbManager.query(formSQL.toString());
        if (list.size() <= 0)
            return resultList;
        Iterator it = list.iterator();
        while (it.hasNext()) {
            HashMap map = (HashMap) it.next();
            FormManageVO form = convertMap(map);
            resultList.add(form);
        }
        return resultList;
    }

    public Collection getFormListByTypeList(String formTypeList, String orgId, String status) throws DAOException {
        if (formTypeList == null || "".equals(formTypeList))
            return getAvailableForm(orgId, status);
        Collection resultList = new ArrayList();
        StringBuffer formSQL = new StringBuffer("select * from teflow_form where form_type in(" + formTypeList
                + ") and form_system_id in (select form_system_id from teflow_wkf_define) ");
        if (orgId != null && !"".equals(orgId)) {
            formSQL.append(" and org_id in(").append(orgId).append(") ");
        }
        if (status != null && !"".equals(status)) {
            formSQL.append(" and status='").append(status).append("' ");
        }
        formSQL.append(" order by form_id,form_name");
        // System.out.println(formSQL.toString());
        Collection list = dbManager.query(formSQL.toString());
        if (list.size() <= 0)
            return resultList;
        Iterator it = list.iterator();
        while (it.hasNext()) {
            HashMap map = (HashMap) it.next();
            FormManageVO form = convertMap(map);
            resultList.add(form);
        }
        return resultList;
    }

    public Collection getAvailableForm(String orgId, String status) throws DAOException {
        Collection resultList = new ArrayList();
        StringBuffer formSQL = new StringBuffer(
                "select * from teflow_form where form_system_id in (select form_system_id from teflow_wkf_define)  ");
        if (status != null && !"".equals(status)) {
            formSQL.append(" and status='").append(status).append("' ");
        }
        if (orgId != null && !"".equals(orgId)) {
            formSQL.append(" and org_id in(").append(orgId).append(") ");
        }
        formSQL.append(" order by form_id,form_name");
        Collection list = dbManager.query(formSQL.toString());
        if (list.size() <= 0)
            return resultList;
        Iterator it = list.iterator();
        while (it.hasNext()) {
            HashMap map = (HashMap) it.next();
            FormManageVO form = convertMap(map);
            resultList.add(form);
        }
        return resultList;
    }

    private FormManageVO convertMap(HashMap map) {
        FormManageVO form = new FormManageVO();
        form.setFormSystemId(Integer.parseInt((String) map.get("FORM_SYSTEM_ID")));
        form.setFormId((String) map.get("FORM_ID"));
        if (map.get("FORM_NAME") != null) {
            form.setFormName(DataConvertUtil.convertISOToGBK((String) map.get("FORM_NAME")));
        }
        if (map.get("FORM_TYPE") != null) {
            form.setFormType((String) map.get("FORM_TYPE"));
        }
        if (map.get("FORM_IMAGE") != null) {
            form.setFormSystemImg((String) map.get("FORM_IMAGE"));
        }
        if (map.get("FORM_DESCRIPTION") != null) {
            form.setDescription(DataConvertUtil.convertISOToGBK((String) map.get("FORM_DESCRIPTION")));
        }
        if (map.get("STATUS") != null) {
            form.setStatus((String) map.get("STATUS"));
        }
        if (map.get("CREATE_DATE") != null) {
            form.setCreateDateStr((String) map.get("CREATE_DATE"));
        }
        form.setActionType(FieldUtil.convertSafeString(map, "ACTION_TYPE"));
        form.setActionMessage(FieldUtil.convertSafeString(map, "ACTION_MESSAGE"));
        form.setOrgId(FieldUtil.convertSafeString(map, "ORG_ID"));
        form.setPre_validation_url(FieldUtil.convertSafeString(map, "PRE_VALIDATION_URL"));
        form.setAfterSaveUrl(FieldUtil.convertSafeString(map, "AFTER_SAVE_URL"));

        return form;
    }

    public String[] getTableCoulumnNames(String tableName) throws DAOException {

        return null;
    }

    public FormManageVO getFormBaseInforBySystemId(int formSystemId) throws DAOException {
        StringBuffer formSQL = new StringBuffer("select * from teflow_form where 1=1 ");
        formSQL.append(" and form_system_id=").append(formSystemId);
        // System.out.println("query sql : " + formSQL.toString());
        Collection list = dbManager.query(formSQL.toString());
        if (list.size() <= 0)
            return null;
        HashMap map = (HashMap) list.iterator().next();
        FormManageVO form = new FormManageVO();
        form.setFormName(DataConvertUtil.convertISOToGBK((String) map.get("FORM_NAME")));
        if (map.get("FORM_DESCRIPTION") != null)
            form.setDescription(DataConvertUtil.convertISOToGBK((String) map.get("FORM_DESCRIPTION")));

        form.setFormId((String) map.get("FORM_ID"));
        form.setFormSystemId(formSystemId);
        form.setFormType(FieldUtil.convertSafeString(map, "FORM_TYPE"));
        form.setStatus(FieldUtil.convertSafeString(map, "STATUS"));
        form.setOrgId(FieldUtil.convertSafeString(map, "ORG_ID"));
        form.setPre_validation_url(FieldUtil.convertSafeString(map, "PRE_VALIDATION_URL"));
        form.setAfterSaveUrl(FieldUtil.convertSafeString(map, "AFTER_SAVE_URL"));

        return form;
    }

    public FormManageVO getFormBySystemId(int formSystemId) throws DAOException {
        StringBuffer formSQL = new StringBuffer("select * from teflow_form where 1=1 ");
        formSQL.append(" and form_system_id=").append(formSystemId);
        // System.out.println("query sql : " + formSQL.toString());
        Collection list = dbManager.query(formSQL.toString());
        if (list.size() <= 0)
            return null;
        HashMap map = (HashMap) list.iterator().next();
        FormManageVO form = new FormManageVO();
        form.setFormName(DataConvertUtil.convertISOToGBK((String) map.get("FORM_NAME")));
        if (map.get("FORM_DESCRIPTION") != null)
            form.setDescription(DataConvertUtil.convertISOToGBK((String) map.get("FORM_DESCRIPTION")));
        form.setFormId((String) map.get("FORM_ID"));
        form.setFormSystemId(formSystemId);
        form.setFormType(FieldUtil.convertSafeString(map, "FORM_TYPE"));
        form.setStatus(FieldUtil.convertSafeString(map, "STATUS"));
        form.setActionType(FieldUtil.convertSafeString(map, "ACTION_TYPE"));
        form.setActionMessage(FieldUtil.convertSafeString(map, "ACTION_MESSAGE"));
        form.setOrgId(FieldUtil.convertSafeString(map, "ORG_ID"));
        form.setPre_validation_url(FieldUtil.convertSafeString(map, "PRE_VALIDATION_URL"));
        form.setAfterSaveUrl(FieldUtil.convertSafeString(map, "AFTER_SAVE_URL"));
        form.setSectionList(getSectionListByForm(formSystemId));
        form.setSubmitAlias(FieldUtil.convertSafeString(map,"SUBMITALIAS"));
        form.setSaveAlias(FieldUtil.convertSafeString(map,"SAVEALIAS"));
        return form;
    }

    public int getFormSystemId(String formId) throws DAOException {
        String SQL = "select form_system_id from teflow_form where form_id = ?";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(SQL);
            int i = 1;
            stm.setString(i++, formId);
            ResultSet rs = stm.executeQuery();
            if (!rs.next()) {
                return -1;
            }
            return rs.getInt("form_system_id");
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

    public int getFormSystemIdByFlowId(int flowId) throws DAOException {
        String SQL = "select form_system_id from teflow_wkf_define where flow_id = ?";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(SQL);
            int i = 1;
            stm.setInt(i++, flowId);
            ResultSet rs = stm.executeQuery();
            if (!rs.next()) {
                return -1;
            }
            return rs.getInt("form_system_id");
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

    public Collection getFieldListByForm(int formSystemId, boolean ingoreTableSection) throws DAOException {
        if (ingoreTableSection) {
            String sql = "select * from teflow_form_section_field where form_system_id = " + formSystemId
                    + " and section_id in (select section_id from teflow_form_section where form_system_id="
                    + formSystemId + " and section_type<>'00' and section_type<>'01') order by section_id,order_id";
            Collection list = dbManager.query(sql);
            if (list == null)
                return new ArrayList();
            Iterator it = list.iterator();
            Collection resultList = new ArrayList();
            while (it.hasNext()) {
                HashMap map = (HashMap) it.next();
                FormSectionFieldVO field = parseField(map, formSystemId);
                resultList.add(field);
            }
            return resultList;
        } else {
            return getFieldListByForm(formSystemId);
        }
    }

    public Collection getFieldListByForm(int formSystemId) throws DAOException {
        String sql = "select * from teflow_form_section_field where form_system_id = " + formSystemId
                + " order by section_id,order_id";
        Collection list = dbManager.query(sql);
        if (list == null)
            return new ArrayList();
        Iterator it = list.iterator();
        Collection resultList = new ArrayList();
        while (it.hasNext()) {
            HashMap map = (HashMap) it.next();
            FormSectionFieldVO field = parseField(map, formSystemId);
            resultList.add(field);
        }
        return resultList;
    }

    public HashMap getFieldHashMapListByForm(int formSystemId) throws DAOException {
        String sql = "select * from teflow_form_section_field where form_system_id = " + formSystemId;
        Collection list = dbManager.query(sql);
        HashMap map = new HashMap();
        if (list == null)
            return map;
        Iterator it = list.iterator();

        while (it.hasNext()) {
            HashMap temp = (HashMap) it.next();
            map.put(((String) temp.get("FIELD_ID")).toUpperCase(), temp);
        }
        return map;
    }

    public FormSectionVO getFormBaseSection(int formSystemId) throws DAOException {
        String sql = "select * from teflow_form_section where form_system_id=" + formSystemId
                + " and section_type='03'";
        Collection list = dbManager.query(sql);
        if (list == null || list.size() == 0)
            return null;
        HashMap map = (HashMap) list.iterator().next();
        FormSectionVO vo = new FormSectionVO();
        vo.setSectionId(FieldUtil.convertSafeString(map, "SECTION_ID"));
        vo.setFormSystemId(formSystemId);
        vo.setSectionRemark(DataConvertUtil.convertISOToGBK(FieldUtil.convertSafeString(map, "SECTION_REMARK")));
        vo.setSectionType(FieldUtil.convertSafeString(map, "SECTION_TYPE"));
        vo.setOrderId(FieldUtil.convertSafeString(map, "ORDER_ID", "1"));
        return vo;
    }

    public FormSectionVO getFormSectionInfor(int formSystemId, String sectionId) throws DAOException {
        String sql = "select * from teflow_form_section where form_system_id=" + formSystemId + " and section_id='"
                + sectionId + "' order by section_type desc,section_id";
        Collection list = dbManager.query(sql);
        if (list == null || list.size() == 0)
            return null;
        HashMap map = (HashMap) list.iterator().next();
        FormSectionVO vo = new FormSectionVO();
        vo.setSectionId(sectionId);
        vo.setFormSystemId(formSystemId);
        vo.setSectionRemark(DataConvertUtil.convertISOToGBK(FieldUtil.convertSafeString(map, "SECTION_REMARK")));
        vo.setSectionType(FieldUtil.convertSafeString(map, "SECTION_TYPE"));
        vo.setTableName(FieldUtil.convertSafeString(map, "TABLE_NAME"));
        return vo;
    }

    public void updateFormSectionInfor(FormSectionVO section) throws DAOException {

        String sql = "update teflow_form_section set section_remark='" + section.getSectionRemark()
                + "',section_type='" + section.getSectionType() + "' " + " where form_system_id="
                + section.getFormSystemId() + " and section_id='" + section.getSectionId() + "'";
        // System.out.println(sql);
        dbManager.executeUpdate(sql);
    }

    public void updateSectionOrder(int formSystemId, String sectionId, String orderId) throws DAOException {
        String updateSQL = "update teflow_form_section set order_id='" + orderId + "' where form_system_id="
                + formSystemId + " and section_id='" + sectionId + "' ";
        dbManager.executeUpdate(updateSQL);
    }

    /**
     * 设定fieldId的列宽
     * 
     * @param formSystemId
     * @param sectionId
     * @param fieldId
     * @param columnWidth
     * @throws DAOException
     */
    public void updateSectionColumnWidth(int formSystemId, String sectionId, String fieldId, int columnWidth)
            throws DAOException {
        String sql = "update teflow_form_section_field set column_width=" + columnWidth + " where form_system_id="
                + formSystemId + " and section_id='" + sectionId + "' and field_id='" + fieldId + "'";
        dbManager.executeUpdate(sql);
    }

    public void updateFieldOrder(int formSystemId, String sectionId, String fieldId, String orderId)
            throws DAOException {
        String sql = "update teflow_form_section_field set order_id=" + orderId + " where form_system_id="
                + formSystemId + " and section_id='" + sectionId + "' and field_id='" + fieldId + "'";
        dbManager.executeUpdate(sql);
    }

    public Collection getSectionListByForm(int formSystemId) throws DAOException {
        StringBuffer sectionSQL = new StringBuffer("select * from teflow_form_section where 1=1 ");
        sectionSQL.append(" and form_system_id=").append(formSystemId)
                .append(" order by order_id,section_type desc,section_id ");
        Collection list = dbManager.query(sectionSQL.toString());
        if (list.size() <= 0)
            return new ArrayList();

        Collection resultList = new ArrayList();

        Iterator it = list.iterator();
        while (it.hasNext()) {
            HashMap map = (HashMap) it.next();
            FormSectionVO section = new FormSectionVO();
            section.setFormSystemId(formSystemId);
            section.setSectionId((String) map.get("SECTION_ID"));
            if (map.get("SECTION_TYPE") != null) {
                section.setSectionType((String) map.get("SECTION_TYPE"));
            }
            if (map.get("SECTION_REMARK") != null) {
                section.setSectionRemark(DataConvertUtil.convertISOToGBK((String) map.get("SECTION_REMARK")));
            }
            if (map.get("TABLE_NAME") != null) {
                section.setTableName((String) map.get("TABLE_NAME"));
            }
            if (map.get("ORDER_ID") != null) {
                section.setOrderId((String) map.get("ORDER_ID"));
            } else {
                section.setOrderId("1");
            }
            //Add by Colin Wang for Search function
            if (map.get("SECTION_URL") != null) {
                section.setSectionUrl((String) map.get("SECTION_URL"));
            }
            if (map.get("EXPORT_EXCEL") != null) {
                section.setExport((String) map.get("EXPORT_EXCEL"));
            }
            // if sectionType="01", the section is table, it need add id field

            section.setFieldList(getSectionFieldListByForm(formSystemId, section.getSectionId(),
                    section.getSectionType()));
            resultList.add(section);
        }
        return resultList;
    }

    /**
     * 获取指定流程所绑定的form的section信息(包括其中的所有字段的信息)
     * 
     * @param flowId
     * @return
     * @throws DAOException
     */
    public Collection getFormSectionListByFlowId(int flowId) throws DAOException {
        Collection sectionList = new ArrayList();
        String sql = "select * from teflow_form_section where form_system_id in (select form_system_id from teflow_wkf_define where flow_id="
                + flowId + ") order by section_type desc";
        Collection list = dbManager.query(sql);
        if (list == null || list.size() == 0)
            return sectionList;
        Iterator it = list.iterator();
        while (it.hasNext()) {
            FormSectionVO section = new FormSectionVO();
            HashMap map = (HashMap) it.next();
            section.setFormSystemId(FieldUtil.convertSafeInt(map, "FORM_SYSTEM_ID", 0));
            section.setSectionId(FieldUtil.convertSafeString(map, "SECTION_ID"));
            section.setSectionRemark(DataConvertUtil.convertISOToGBK(FieldUtil.convertSafeString(map, "SECTION_REMARK")));
            section.setSectionType(FieldUtil.convertSafeString(map, "SECTION_TYPE"));
            section.setTableName(FieldUtil.convertSafeString(map, "TABLE_NAME"));
            if (map.get("ORDER_ID") != null) {
                section.setOrderId((String) map.get("ORDER_ID"));
            } else {
                section.setOrderId("1");
            }
            section.setFieldList(getSectionFieldListByForm(section.getFormSystemId(), section.getSectionId(),
                    section.getSectionType()));
            sectionList.add(section);
        }
        return sectionList;
    }

    public Collection getSectionFieldListByForm(int formSystemId, String sectionId, String sectionType)
            throws DAOException {
        StringBuffer fieldSQL = new StringBuffer("select * from teflow_form_section_field where 1=1 ");
        fieldSQL.append(" and form_system_id=").append(formSystemId).append(" and section_id='").append(sectionId)
                .append("' ");
        fieldSQL.append(" order by order_id");
        Collection list = dbManager.query(fieldSQL.toString());
        if (list.size() <= 0)
            return new ArrayList();
        Collection resultList = new ArrayList();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            HashMap map = (HashMap) it.next();
            FormSectionFieldVO field = parseField(map, formSystemId);
            field.setSectionType(sectionType);
            resultList.add(field);
        }
        if ("01".equals(sectionType) || "00".equals(sectionType)) { // table type or attahment type need 'id'
            FormSectionFieldVO field = new FormSectionFieldVO();
            field.setFieldId("id");
            field.setFieldType(CommonName.FIELD_TYPE_IDENTITY);
            field.setDataType(3);// numberic
            field.setFieldLength(12);
            field.setDecimalDigits(0);
            field.setIsRequired(true);
            resultList.add(field);
        }
        return resultList;
    }

    public FormSectionFieldVO getField(int formSystemId, String sectionId, String fieldId) throws DAOException {
        StringBuffer fieldSQL = new StringBuffer("select * from teflow_form_section_field where 1=1 ");
        fieldSQL.append(" and form_system_id=").append(formSystemId).append(" and section_id='").append(sectionId)
                .append("' ").append(" and field_id='").append(fieldId).append("'");
        Collection list = dbManager.query(fieldSQL.toString());
        Iterator it = list.iterator();
        // System.out.println("QUERY SQL:" + fieldSQL.toString());
        if (!it.hasNext())
            return new FormSectionFieldVO();
        HashMap map = (HashMap) it.next();
        FormSectionFieldVO field = parseField(map, formSystemId);
        return field;
    }

    private FormSectionFieldVO parseField(HashMap map, int formSystemId) {
        FormSectionFieldVO field = new FormSectionFieldVO();
        field.setFormSystemId(formSystemId);
        field.setFieldId((String) map.get("FIELD_ID"));
        field.setSectionId((String) map.get("SECTION_ID"));
        field.setFieldLabel(DataConvertUtil.convertISOToGBK((String) map.get("FIELD_LABEL")));
        if (map.get("FIELD_LENGTH") != null) {
            field.setFieldLength((new Integer((String) map.get("FIELD_LENGTH"))).intValue());
        }
        if (map.get("IS_REQUIRED") != null) {
            if ((new Integer((String) map.get("IS_REQUIRED"))).intValue() == 0) {
                field.setIsRequired(true);
            } else {
                field.setIsRequired(false);
            }
        } else {
            field.setIsRequired(false);
        }
        if (map.get("DATA_TYPE") != null) {
            field.setDataType((new Integer((String) map.get("DATA_TYPE"))).intValue());
        }
        if (map.get("FIELD_TYPE") != null) {
            field.setFieldType((new Integer((String) map.get("FIELD_TYPE"))).intValue());
        }
        if (map.get("SOURCE_TYPE") != null) {
            field.setFieldDataSourceType((new Integer((String) map.get("SOURCE_TYPE"))).intValue());
        }
        field.setFieldDataSrSQL((String) map.get("SOURCE_SQL"));
        if (map.get("ORDER_ID") != null) {
            field.setOrderId(Integer.parseInt((String) map.get("ORDER_ID")));
        }
        field.setDecimalDigits(FieldUtil.convertSafeInt(map, "DECIMAL_DIGITS", 0));
        field.setHighLevel(FieldUtil.convertSafeInt(map, "HIGH_LEVEL", -1));
        field.setIsMoney(FieldUtil.convertSafeInt(map, "IS_MONEY", -1));
        field.setIsSingleRow(FieldUtil.convertSafeInt(map, "IS_SINGLEROW", -1));
        field.setControlsWidth(FieldUtil.convertSafeInt(map, "CONTROLS_WIDTH", 30)); // default 30 columns
        field.setControlsHeight(FieldUtil.convertSafeInt(map, "CONTROLS_HEIGHT", 3)); // default 3 rows
        field.setFieldComments(DataConvertUtil.convertISOToGBK(FieldUtil.convertSafeString(map, "FIELD_COMMENTS")));
        field.setCommentContent(DataConvertUtil.convertISOToGBK(FieldUtil.convertSafeString(map, "COMMENT_CONTENT")));
        field.setDefaultValue(FieldUtil.convertSafeString(map, "DEFAULT_VALUE"));
        field.setColumnWidth(FieldUtil.convertSafeInt(map, "COLUMN_WIDTH", 10)); // default设定为10%
        field.setClickEvent(FieldUtil.convertSafeString(map, "EVENT_CLICK"));
        field.setDbclickEvent(FieldUtil.convertSafeString(map, "EVENT_DBCLICK"));
        field.setOnfocusEvent(FieldUtil.convertSafeString(map, "EVENT_ONFOCUS"));
        field.setLostfocusEvent(FieldUtil.convertSafeString(map, "EVENT_ONBLUR"));
        field.setChangeEvent(FieldUtil.convertSafeString(map, "EVENT_ONCHANGE"));
        field.setIsReadonly(FieldUtil.convertSafeInt(map, "IS_READONLY", -1));
        field.setIsDisabled(FieldUtil.convertSafeInt(map, "IS_DISABLED", -1));
        field.setReportType(FieldUtil.convertSafeString(map, "REPORT_TYPE"));
        field.setReportSystemId(FieldUtil.convertSafeInt(map, "REPORT_SYSTEM_ID",-1));
        field.setFieldDataSrSQL(FieldUtil.convertSafeString(map, "SOURCE_SQL"));
        return field;
    }

    /**
     * 获取指定form在流转中可以修改的sectionField）
     * 
     * @param formSystemId
     * @return HashMap: 其中key为每个可以修改的sectionField的sectionId.fieldId
     * @throws DAOException
     */
    public HashMap getCanUpdateSectionFieldMap(int formSystemId) throws DAOException {
        String sql = "select update_section_fields from teflow_wkf_detail where flow_id=(select flow_id from teflow_wkf_define where form_system_id="
                + formSystemId + ")" + " and update_section_fields is not null and update_section_fields<>''";
        Collection list = dbManager.query(sql);
        if (list == null || list.size() == 0)
            return new HashMap();
        HashMap map = new HashMap();
        Iterator it = list.iterator();
        String tempValue = "";
        while (it.hasNext()) {
            HashMap temp = (HashMap) it.next();
            tempValue = (String) temp.get("UPDATE_SECTION_FIELDS");
            if (tempValue != null && !"".equals(tempValue)) {
                String[] s = StringUtil.split(tempValue, ",");
                if (s.length > 0) {
                    for (int i = 0; i < s.length; i++) {
                        if (!map.containsKey(s[i])) {
                            map.put(s[i].trim().toUpperCase(), s[i].trim());
                        }
                    }
                }
            }
        }
        return map;
    }

    /**
     * 获取指定form在流转中只可以输入的字段的sectionField
     * 
     * @param formSystemId
     * @return HashMap: 其中key为每个只可以输入的字段的sectionField的sectionId.fieldId
     * @throws DAOException
     */
    public HashMap getOnlyFillSectionFieldMap(int formSystemId) throws DAOException {
        String sql = "select fill_section_fields from teflow_wkf_detail where flow_id=(select flow_id from teflow_wkf_define where form_system_id="
                + formSystemId + ")" + " and fill_section_fields is not null and fill_section_fields<>''";
        Collection list = dbManager.query(sql);
        if (list == null || list.size() == 0)
            return new HashMap();
        HashMap map = new HashMap();
        Iterator it = list.iterator();
        String tempValue = "";
        while (it.hasNext()) {
            HashMap temp = (HashMap) it.next();
            tempValue = (String) temp.get("FILL_SECTION_FIELDS");
            if (tempValue != null && !"".equals(tempValue)) {
                String[] s = StringUtil.split(tempValue, ",");
                if (s.length > 0) {
                    for (int i = 0; i < s.length; i++) {
                        if (!map.containsKey(s[i])) {
                            map.put(s[i].trim().toUpperCase(), s[i].trim());
                            // System.out.println(s[i].trim().toUpperCase());
                        }
                    }
                }
            }
        }
        return map;
    }
    
    public HashMap getHiddenSectionFieldMap(int formSystemId,int nodeId) throws DAOException {
        String sql = "select hidden_section_fields from teflow_wkf_detail where flow_id=(select flow_id from teflow_wkf_define where form_system_id="
                + formSystemId + ")" + " and node_id="+nodeId;     
        Collection list = dbManager.query(sql);
        if (list == null || list.size() == 0)
            return new HashMap();
        HashMap map = new HashMap();
        Iterator it = list.iterator();

        String tempValue = "";
       
        HashMap temp = (HashMap) it.next();
            tempValue = (String) temp.get("hidden_section_fields".toUpperCase());
            if (tempValue != null && !"".equals(tempValue)) {
                String[] s = StringUtil.split(tempValue, ",");
                if (s.length > 0) {
                    for (int i = 0; i < s.length; i++) {
                        if (!map.containsKey(s[i])) {
                            map.put(s[i].trim().toUpperCase(), s[i].trim());
                            // System.out.println(s[i].trim().toUpperCase());
                        }
                    }
                }
            }           
        
        return map;
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

    public HashMap getFieldContentByForm(String requestNo) throws DAOException {
        HashMap formFieldValueMap = new HashMap();
        FormSectionFieldVO requestVo = SystemFieldHelper.getInstance().getBasicFieldById(
                CommonName.SYSTEM_ID_REQUEST_NO);
        String queryFormSectionTable = "SELECT * FROM teflow_form_section WHERE (form_system_id = (SELECT form_system_id FROM teflow_wkf_define";
        queryFormSectionTable += " WHERE (flow_id IN (SELECT flow_id FROM teflow_wkf_process where request_no='"
                + requestNo + "'))))";
        System.out.println(queryFormSectionTable);
        System.out.println(requestVo.getFieldId());
        Collection formSectionList = dbManager.query(queryFormSectionTable);
        if (formSectionList == null || formSectionList.size() == 0)
            return null;
        Iterator it = formSectionList.iterator();
        String sectionTableName = "";
        while (it.hasNext()) {
            HashMap sectionMap = (HashMap) it.next();
            if ("04".equals((String) sectionMap.get("SECTION_TYPE")))
                continue;
            sectionTableName = (String) sectionMap.get("TABLE_NAME");
            if (sectionTableName == null || "".equals(sectionTableName))
                continue;
            StringBuffer querySQL = new StringBuffer(" select * from " + sectionTableName);
            querySQL.append(" where ").append(requestVo.getFieldId()).append(" = '").append(requestNo).append("'");
            // if is table type, sort by id
            if ("01".equals((String) sectionMap.get("SECTION_TYPE")))
                querySQL.append(" order by id ");
            Collection list = dbManager.query(querySQL.toString());
            formFieldValueMap.put(sectionMap.get("SECTION_ID"), list);
        }
        String remarkSQL = "select * from TRptRemark";
        Collection remarkList = dbManager.query(remarkSQL);
        if (remarkList != null && remarkList.size() != 0){
        	Iterator remarkit = remarkList.iterator();
        	while (remarkit.hasNext()) {
        		HashMap remarkMap = (HashMap) remarkit.next();
        		formFieldValueMap.put((String) remarkMap.get("REMARKCODE"), (String) remarkMap.get("REMARKCONTENT"));
        	}
        }
        
        return formFieldValueMap;
    }

    public void saveSpecialField(SpecialFieldVo specField) throws DAOException {
        String SQL_INS = "insert into teflow_form_special_field(form_system_id, field_type, section_id, field_id) values(?, ?, ?, ?)";
        String SQL_DEL = "delete from teflow_form_special_field where (form_system_id = ?) and (field_type = ?)";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(SQL_DEL);
            int i = 1;
            stm.setInt(i++, specField.getFormSystemId());
            stm.setInt(i++, specField.getFieldType());
            stm.executeUpdate();

            if (specField.getFieldId() == null) {
                return;
            }

            stm.close();
            stm = conn.prepareStatement(SQL_INS);
            i = 1;
            stm.setInt(i++, specField.getFormSystemId());
            stm.setInt(i++, specField.getFieldType());
            stm.setString(i++, specField.getSectionId());
            stm.setString(i++, specField.getFieldId());
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

    public SpecialFieldVo getSpecialField(SpecialFieldVo paramSpecField) throws DAOException {
        String SQL = "select * from teflow_form_special_field where (form_system_id = ?) and (field_type = ?)";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(SQL);
            int i = 1;
            stm.setInt(i++, paramSpecField.getFormSystemId());
            stm.setInt(i++, paramSpecField.getFieldType());
            ResultSet rs = stm.executeQuery();
            if (!rs.next()) {
                return null;
            }

            SpecialFieldVo specialField = new SpecialFieldVo();
            specialField.setFormSystemId(rs.getInt("form_system_id"));
            specialField.setFieldType(rs.getInt("field_type"));
            specialField.setSectionId(rs.getString("section_id"));
            specialField.setFieldId(rs.getString("field_id"));

            return specialField;
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

    public void saveFormScript(int formSystemId, String script) throws DAOException {
        String SQL_INS = "insert into teflow_form_script(form_system_id, script) values(?, ?)";
        String SQL_DEL = "delete from teflow_form_script where (form_system_id = ?)";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(SQL_DEL);
            int i = 1;
            stm.setInt(i++, formSystemId);
            stm.executeUpdate();

            stm.close();
            stm = conn.prepareStatement(SQL_INS);
            i = 1;
            stm.setInt(i++, formSystemId);
            stm.setString(i++, script);
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

    public String getFormScript(int formSystemId) throws DAOException {
        String SQL = "select * from teflow_form_script where (form_system_id = ?)";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(SQL);
            int i = 1;
            stm.setInt(i++, formSystemId);
            ResultSet rs = stm.executeQuery();
            if (!rs.next()) {
                return null;
            }
            return rs.getString("script");
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

    public Map getAllFormScript() throws DAOException {
        String SQL = "select * from teflow_form_script";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(SQL);
            int i = 1;
            HashMap scriptMap = new HashMap();
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                scriptMap.put(rs.getString("form_system_id"), rs.getString("script"));
            }
            return scriptMap;
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

    public void updateCommonFieldValue(String requestNo, String sectionId, String fieldId, String value)
            throws DAOException {
        String SQL = "select c.* from teflow_wkf_process a, teflow_wkf_define b, teflow_form_section c "
                + "where (a.flow_id = b.flow_id) and (b.form_system_id = c.form_system_id) "
                + "and (a.request_no = ?) and (c.section_id = ?)";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(SQL);
            int i = 1;
            stm.setString(i++, requestNo);
            stm.setString(i++, sectionId);
            ResultSet rs = stm.executeQuery();
            if (!rs.next()) {
                return;
            }
            String tableName = rs.getString("table_name");
            stm.close();

            String SQL_GET = "select * from " + tableName + " where request_no = '" + requestNo + "'";

            stm = conn.prepareStatement(SQL_GET);
            rs = stm.executeQuery();
            if (!rs.next()) {
                return;
            }
            ResultSetMetaData metaData = rs.getMetaData();
            int j = 1;
            for (; j <= metaData.getColumnCount(); j++) {
                String columnName = metaData.getColumnName(j);
                // System.out.println("columnName: " + columnName);
                if (columnName.equalsIgnoreCase(fieldId)) {
                    break;
                }
            }
            if (j > metaData.getColumnCount()) {
                return;
            }

            value = StringUtil.replace(value, "&#39;", "'");
            value = StringUtil.replace(value, "'", "''");
            String SQL_UPD = "update " + tableName + " set " + fieldId + " = '" + value + "' where request_no = '"
                    + requestNo + "'";

            stm = conn.prepareStatement(SQL_UPD);
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

    public String getCommonFieldValue(String requestNo, String sectionId, String fieldId) throws DAOException {
        String SQL = "select c.* from teflow_wkf_process a, teflow_wkf_define b, teflow_form_section c "
                + "where (a.flow_id = b.flow_id) and (b.form_system_id = c.form_system_id) "
                + "and (a.request_no = ?) and (c.section_id = ?)";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(SQL);
            int i = 1;
            stm.setString(i++, requestNo);
            stm.setString(i++, sectionId);
            ResultSet rs = stm.executeQuery();
            if (!rs.next()) {
                return null;
            }
            String tableName = rs.getString("table_name");
            stm.close();

            String SQL_GET = "select * from " + tableName + " where request_no = '" + requestNo + "'";

            stm = conn.prepareStatement(SQL_GET);
            rs = stm.executeQuery();
            if (!rs.next()) {
                return null;
            }
            ResultSetMetaData metaData = rs.getMetaData();
            int j = 1;
            for (; j <= metaData.getColumnCount(); j++) {
                String columnName = metaData.getColumnName(j);
                // System.out.println("columnName: " + columnName);
                if (columnName.equalsIgnoreCase(fieldId)) {
                    break;
                }
            }
            if (j > metaData.getColumnCount()) {
                return null;
            }
            String value = rs.getString(fieldId);

            return value;
        } catch (Exception ex) {
            ex.printStackTrace();
            // return null;
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

    public List getFieldListByFormType(String formType) throws DAOException {
        String SQL = "select * from teflow_form_type_field where (form_type_id = ?) order by type_field_id";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(SQL);
            int i = 1;
            stm.setString(i++, formType);
            ResultSet rs = stm.executeQuery();
            Vector fieldList = new Vector();
            while (rs.next()) {
                FormSectionFieldVO vo = new FormSectionFieldVO();
                vo.setFieldId(rs.getString("type_field_id"));
                vo.setFieldLabel(rs.getString("field_label"));
                fieldList.add(vo);
            }
            return fieldList;
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

    public FormSectionFieldVO getFormTypeField(String formType, String field_id) throws DAOException {
        String SQL = "select * from teflow_form_type_field where (form_type_id = ?) and (type_field_id = ?)";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(SQL);
            int i = 1;
            stm.setString(i++, formType);
            stm.setString(i++, field_id);
            ResultSet rs = stm.executeQuery();
            if (!rs.next()) {
                return null;
            }
            FormSectionFieldVO vo = new FormSectionFieldVO();
            vo.setFieldId(rs.getString("type_field_id"));
            vo.setFieldLabel(rs.getString("field_label"));
            vo.setFieldType(rs.getInt("field_type"));
            vo.setDataType(rs.getInt("data_type"));
            vo.setFieldComments(rs.getString("basedata_id"));
            return vo;
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

    public void saveExportFields(int formSystemId, int setNo, Collection fields) throws DAOException {
        String SQL_INS = "insert into teflow_form_export_field(form_system_id, set_no, section_id, field_id) values(?, ?, ?, ?)";
        String SQL_DEL = "delete from teflow_form_export_field where (form_system_id = ?) and (set_no = ?)";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(SQL_DEL);
            int i = 1;
            stm.setInt(i++, formSystemId);
            stm.setInt(i++, setNo);
            stm.executeUpdate();

            if (fields == null || fields.isEmpty()) {
                return;
            }

            stm.close();
            stm = conn.prepareStatement(SQL_INS);
            stm.setInt(1, formSystemId);
            stm.setInt(2, setNo);
            Iterator it = fields.iterator();
            while (it.hasNext()) {
                FormSectionFieldVO field = (FormSectionFieldVO) it.next();
                stm.setString(3, field.getSectionId());
                stm.setString(4, field.getFieldId());
                stm.executeUpdate();
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

    public List getExportFields(int formSystemId, int setNo) throws DAOException {
        String SQL = "select * from teflow_form_export_field where (form_system_id = ?) and (set_no = ?)";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(SQL);
            int i = 1;
            stm.setInt(i++, formSystemId);
            stm.setInt(i++, setNo);
            ResultSet rs = stm.executeQuery();
            List list = new ArrayList();
            while (rs.next()) {
                FormSectionFieldVO field = new FormSectionFieldVO();
                field.setFormSystemId(rs.getInt("form_system_id"));
                field.setSectionId(rs.getString("section_id"));
                field.setFieldId(rs.getString("field_id"));
                list.add(field);
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

}
