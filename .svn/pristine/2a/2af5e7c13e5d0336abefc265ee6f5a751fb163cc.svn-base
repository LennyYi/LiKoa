package com.aiait.eflow.housekeeping.dao;

import java.sql.*;
import java.util.*;

import com.aiait.eflow.housekeeping.vo.*;
import com.aiait.eflow.util.*;
import com.aiait.framework.common.DataType;
import com.aiait.framework.db.*;
import com.aiait.framework.exception.DAOException;
import com.aiait.framework.vo.BaseVO;

public class EmailTemplateDAO extends BaseDAOImpl {

    public EmailTemplateDAO(IDBManager dbManager) {
        super(dbManager);
    }

    public EmailTemplateVO getEmailTemplateByAction(String action) throws DAOException {
        String sql = "select * from teflow_email_template where applied_action like '%" + action + "%'";
        Collection list = dbManager.query(sql);
        if (list == null || list.size() == 0)
            return null;
        HashMap map = (HashMap) list.iterator().next();
        EmailTemplateVO vo = convertMapToVo(map);
        return vo;
    }

    public EmailTemplateVO getEmailTemplateByAction(String action, String formType) throws DAOException {
        String SQL = "select a.id, a.name, a.applied_action, "
                + "case when b.email_subject is null then a.email_subject else b.email_subject end as email_subject, "
                + "case when b.email_content is null then a.email_content else b.email_content end as email_content, "
                + "case when b.description is null then a.description else b.description end as description "
                + "from teflow_email_template a left join ("
                + "select * from teflow_email_template_formtype where form_type = ?) b on (a.id = b.id) "
                + "where (a.applied_action = ?)";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(SQL);
            int i = 1;
            stm.setString(i++, formType);
            stm.setString(i++, action);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                EmailTemplateVO vo = new EmailTemplateVO();
                vo.setId(rs.getInt("id"));
                vo.setName(rs.getString("name"));
                vo.setAppliedAction(rs.getString("applied_action"));
                vo.setEmailSubject(rs.getString("email_subject"));
                vo.setEmailContent(rs.getString("email_content"));
                vo.setDescription(rs.getString("description"));
                return vo;
            } else {
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

    public String getOtherExistAction(int id) throws DAOException {
        String sql = "select applied_action from teflow_email_template where id<>" + id;
        Collection list = dbManager.query(sql);
        if (list == null || list.size() == 0)
            return "";
        Iterator it = list.iterator();
        String resultStr = "";
        while (it.hasNext()) {
            HashMap map = (HashMap) it.next();
            if (!"".equals(FieldUtil.convertSafeString(map, "APPLIED_ACTION"))) {
                resultStr = resultStr + FieldUtil.convertSafeString(map, "APPLIED_ACTION") + ",";
            }
        }
        if (!"".equals(resultStr)) {
            resultStr = resultStr.substring(0, resultStr.length() - 1);
        }
        return resultStr;
    }

    public int delete(BaseVO arg0) throws DAOException {
        EmailTemplateVO vo = (EmailTemplateVO) arg0;
        String sql = "delete from teflow_email_template where id=" + vo.getId();
        dbManager.executeUpdate(sql);
        return 0;
    }

    public int save(BaseVO arg0) throws DAOException {
        EmailTemplateVO vo = (EmailTemplateVO) arg0;

        StringBuffer sql = new StringBuffer(
                "insert into teflow_email_template(name,email_subject,email_content,description,applied_action) ");
        sql.append(" values(?,?,?,?,?)");
        Object[] obj = new Object[5];
        obj[0] = vo.getName();
        obj[1] = vo.getEmailSubject();
        obj[2] = vo.getEmailContent();
        obj[3] = vo.getDescription();
        obj[4] = vo.getAppliedAction();
        int[] dataType = { DataType.VARCHAR, DataType.VARCHAR, DataType.VARCHAR, DataType.VARCHAR, DataType.VARCHAR };

        dbManager.executeUpdate(sql.toString(), obj, dataType);
        return 0;
    }

    public int update(BaseVO arg0) throws DAOException {
        EmailTemplateVO vo = (EmailTemplateVO) arg0;
        StringBuffer sql = new StringBuffer("update teflow_email_template set name=?,");
        sql.append("email_subject=?,email_content=?,description=?,applied_action=? where id=?");
        Object[] obj = new Object[6];
        obj[0] = vo.getName();
        obj[1] = vo.getEmailSubject();
        obj[2] = vo.getEmailContent();
        obj[3] = vo.getDescription();
        obj[4] = vo.getAppliedAction();
        obj[5] = "" + vo.getId();

        int[] dataType = { DataType.VARCHAR, DataType.VARCHAR, DataType.VARCHAR, DataType.VARCHAR, DataType.VARCHAR,
                DataType.INT };
        dbManager.executeUpdate(sql.toString(), obj, dataType);

        return 0;
    }

    public EmailTemplateVO getEmailTemplate(int id) throws DAOException {
        EmailTemplateVO vo = null;
        String sql = "select * from teflow_email_template where id=" + id;
        Collection list = dbManager.query(sql);
        if (list == null || list.size() == 0)
            return vo;
        HashMap map = (HashMap) list.iterator().next();
        vo = convertMapToVo(map);
        return vo;
    }

    public Collection getEmailTemplateList() throws DAOException {
        String sql = "select * from teflow_email_template order by id";
        Collection list = dbManager.query(sql);
        if (list == null || list.size() == 0)
            return new ArrayList();
        Iterator it = list.iterator();
        Collection result = new ArrayList();
        while (it.hasNext()) {
            HashMap map = (HashMap) it.next();
            EmailTemplateVO vo = convertMapToVo(map);
            vo.setFormtypes(this.getFormTypes(vo.getId()));
            result.add(vo);
        }
        return result;
    }

    public List getFormTypes(int id) throws DAOException {
        String SQL = "select b.* from teflow_email_template_formtype a, teflow_form_type b where (a.form_type = b.form_type_id) and (a.id = ?)";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(SQL);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            List formtypes = new ArrayList();
            while (rs.next()) {
                FormTypeVO vo = new FormTypeVO();
                vo.setFormTypeId(rs.getString("form_type_id"));
                vo.setFormTypeName(rs.getString("form_type_name"));
                formtypes.add(vo);
            }
            return formtypes;
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

    public EmailTemplateVO getByFormType(int id, String formType) throws DAOException {
        String SQL = "select * from teflow_email_template_formtype where (id = ?) and (form_type = ?)";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(SQL);
            int i = 1;
            stm.setInt(i++, id);
            stm.setString(i++, formType);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                EmailTemplateVO vo = new EmailTemplateVO();
                vo.setId(id);
                vo.setEmailSubject(rs.getString("email_subject"));
                vo.setEmailContent(rs.getString("email_content"));
                vo.setDescription(rs.getString("description"));
                return vo;
            } else {
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

    public void saveByFormType(EmailTemplateVO vo, String formType) throws DAOException {
        String SQL = "insert into teflow_email_template_formtype "
                + "(id, form_type, email_subject, email_content, description) values (?, ?, ?, ?, ?)";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            this.deleteByFormType(vo.getId(), formType);
            stm = conn.prepareStatement(SQL);
            int i = 1;
            stm.setInt(i++, vo.getId());
            stm.setString(i++, formType);
            stm.setString(i++, vo.getEmailSubject());
            stm.setString(i++, vo.getEmailContent());
            stm.setString(i++, vo.getDescription());
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

    public void deleteByFormType(int id, String formType) throws DAOException {
        String SQL = "delete from teflow_email_template_formtype where (id = ?) and (form_type = ?)";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(SQL);
            int i = 1;
            stm.setInt(i++, id);
            stm.setString(i++, formType);
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

    private EmailTemplateVO convertMapToVo(HashMap map) {
        EmailTemplateVO vo = new EmailTemplateVO();
        vo.setId(FieldUtil.convertSafeInt(map, "ID", 0));
        vo.setName(DataConvertUtil.convertISOToGBK(FieldUtil.convertSafeString(map, "NAME")));
        vo.setEmailSubject(DataConvertUtil.convertISOToGBK(FieldUtil.convertSafeString(map, "EMAIL_SUBJECT")));
        vo.setEmailContent(DataConvertUtil.convertISOToGBK(FieldUtil.convertSafeString(map, "EMAIL_CONTENT")));
        vo.setDescription(DataConvertUtil.convertISOToGBK(FieldUtil.convertSafeString(map, "DESCRIPTION")));
        vo.setAppliedAction(FieldUtil.convertSafeString(map, "APPLIED_ACTION"));
        return vo;
    }
}
