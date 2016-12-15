package com.aiait.eflow.wkf.vo;

import java.io.Serializable;
import java.sql.*;
import java.util.HashMap;

public class TableDataListVO implements Serializable {

    protected HashMap map = new HashMap();

    public int getMaxLine(String requestNo) {
        Integer lineNum = (Integer) this.map.get(requestNo);
        return lineNum == null ? 0 : lineNum.intValue();
    }

    public Object getFieldValue(String requestNo, String fieldId, int line) {
        if (line < 1) {
            throw new IllegalArgumentException("line must be >= 1");
        }
        String key = this.getKey(requestNo, fieldId, line);
        return this.map.get(key);
    }

    public void setFieldValue(String requestNo, String fieldId, int line, Object value) {
        if (line < 1) {
            throw new IllegalArgumentException("line must be >= 1");
        }
        if (value == null) {
            return;
        }
        String key = this.getKey(requestNo, fieldId, line);
        this.map.put(key, value);
        // System.out.println("key/value: " + key + "/" + value);
        // Set max line
        int maxLine = this.getMaxLine(requestNo);
        if (maxLine < line) {
            this.map.put(requestNo, Integer.valueOf(line));
            // System.out.println("requestNo/line: " + requestNo + "/" + line);
        }
    }

    // Load data of table sections
    public void loadData(ResultSet rs) throws Exception {
        ResultSetMetaData meta = rs.getMetaData();
        // rs.beforeFirst();
        String requestNo = null;
        int line = 0;
        while (rs.next()) {
            String reqNo = rs.getString("request_no");
            if (!reqNo.equals(requestNo)) {
                requestNo = reqNo;
                line = 1;
            } else {
                line++;
            }
            for (int i = 1; i <= meta.getColumnCount(); i++) {
                String colName = meta.getColumnName(i);
                if (colName.equalsIgnoreCase("request_no") || colName.equalsIgnoreCase("id")) {
                    continue;
                }
                Object value = rs.getObject(colName);
                this.setFieldValue(requestNo, colName, line, value);
            }
        }
    }

    protected String getKey(String requestNo, String fieldId, int line) {
        return (requestNo + "_" + fieldId + "_" + line).toLowerCase();
    }

}
