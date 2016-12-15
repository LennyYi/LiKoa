package com.aiait.eflow.util;

import java.math.BigDecimal;
import java.util.HashMap;

import com.aiait.eflow.formmanage.dao.FormManageDAO;
import com.aiait.eflow.formmanage.vo.FormManageVO;
import com.aiait.eflow.formmanage.vo.FormSectionFieldVO;
import com.aiait.eflow.reportmanage.vo.ReportSectionFieldVO;
import com.aiait.framework.db.DBManagerFactory;
import com.aiait.framework.db.IDBManager;

public class FieldUtil {

    public static void main(String[] args) {
        String s = "TF-01-SPR_04192007_01";
        System.out.println(s.substring("TF-01-SPR_04192007_".length()));
        String temp = s.substring("TF-01-SPR_04192007_".length());
        System.out.println(Integer.parseInt(temp));
    }

    public static String getRequestNo(int formSystemId, String formId, String sectionId) {
        // form申请表单流水号的生成，生成规则：form_id + "_" + 申请日期（mmddyyyy）+ "_" + 流水号
        String requestNo = formId + "_" + StringUtil.getCurrentDateStr("yyyyMMdd") + "_";
        String maxNo = "";
        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            FormManageDAO dao = new FormManageDAO(dbManager);
            FormManageVO form = dao.getFormBaseInforBySystemId(formSystemId);
            FormSectionFieldVO field = new FormSectionFieldVO();
            field.setFormSystemId(formSystemId);
            field.setSectionId(sectionId);
            if (form != null && !"1".equals(form.getStatus())) {
                maxNo = dao.getMaxRequestNo(field, requestNo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (dbManager != null)
                dbManager.freeConnection();
        }
        if ("".equals(maxNo) || maxNo == null) {
            maxNo = "01";
        } else {
            // System.out.println("maxNo = " + maxNo);
            int no = Integer.parseInt(maxNo) + 1;
            if (no > 9) {
                maxNo = "" + no;
            } else {
                maxNo = "0" + no;
            }
        }
        requestNo = requestNo + maxNo;
        return requestNo;
    }

    public static String getRequestNo(int formSystemId, String formId, String sectionId, IDBManager dbManager) {
        // form申请表单流水号的生成，生成规则：form_id + "_" + 申请日期（mmddyyyy）+ "_" + 流水号
        String requestNo = formId + "_" + StringUtil.getCurrentDateStr("yyyyMMdd") + "_";
        String maxNo = "";
        try {
            FormManageDAO dao = new FormManageDAO(dbManager);
            FormManageVO form = dao.getFormBaseInforBySystemId(formSystemId);
            FormSectionFieldVO field = new FormSectionFieldVO();
            field.setFormSystemId(formSystemId);
            field.setSectionId(sectionId);
            if (form != null && !"1".equals(form.getStatus())) {
                maxNo = dao.getMaxRequestNo(field, requestNo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if ("".equals(maxNo) || maxNo == null) {
            maxNo = "01";
        } else {
            // System.out.println("maxNo = " + maxNo);
            int no = Integer.parseInt(maxNo) + 1;
            if (no > 9) {
                maxNo = "" + no;
            } else {
                maxNo = "0" + no;
            }
        }
        requestNo = requestNo + maxNo;
        return requestNo;
    }

    /**
     * 获取生成的form section数据保存表的表名
     * 
     * @param field
     * @return
     */
    public static String getSectionDataTableName(FormSectionFieldVO field) {
        // section对应数据保存的表名格式是固定的：teflow_form的ID_section的ID
        String tableName = "teflow_" + field.getFormSystemId() + "_" + field.getSectionId();
        return tableName;
    }
    
    public static String getSectionDataTableName(ReportSectionFieldVO field) {
        // section对应数据保存的表名格式是固定的：teflow_form的ID_section的ID
        String tableName = "teflow_report_" + field.getReportSystemId() + "_" + field.getSectionId();
        return tableName;
    }

    public static int convertSafeInt(String src, int defaultValue) {
        if (src == null) {
            return defaultValue;
        } else {
            return Integer.parseInt(src);
        }
    }

    public static String convertSafeString(String src, String defaultValue) {
        if (src == null) {
            return defaultValue;
        } else {
            return src;
        }
    }

    public static String convertSafeString(HashMap map, String key) {
        return convertSafeString(map, key, "");
    }

    public static String convertSafeString(HashMap map, String key, String defaultValue) {
        if (((String) map.get(key)) != null) {
            return ((String) map.get(key)).trim();
        } else {
            return defaultValue;
        }
    }

    public static int convertSafeInt(HashMap map, String key, int defaultValue) {
        if (((String) map.get(key)) != null && !"".equals((String) map.get(key))) {
            return Integer.parseInt((String) map.get(key));
        } else {
            return defaultValue;
        }
    }

    public static double convertSafeDouble(HashMap map, String key, float defaultValue) {
        if (((String) map.get(key)) != null && !"".equals((String) map.get(key))) {
            return (Double.parseDouble((String) map.get(key)));
        } else {
            return defaultValue;
        }
    }

    public static float convertSafeFloat(HashMap map, String key, float defaultValue) {
        if (((String) map.get(key)) != null && !"".equals((String) map.get(key))) {
            return (Float.parseFloat((String) map.get(key)));
        } else {
            return defaultValue;
        }
    }

    public static BigDecimal convertSafeDecimal(HashMap map, String key, BigDecimal defaultValue) {
        if (map.get(key) != null && !"".equals((String) map.get(key))) {
            return (new BigDecimal((String) map.get(key)));
        } else {
            return defaultValue;
        }
    }

}
