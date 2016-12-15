package com.aiait.eflow.reportmanage.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.aiait.eflow.common.CommonName;
import com.aiait.eflow.common.helper.SystemFieldHelper;
import com.aiait.eflow.reportmanage.vo.ReportManageVO;
import com.aiait.eflow.reportmanage.vo.ReportSectionFieldVO;
import com.aiait.eflow.reportmanage.vo.ReportSectionVO;
import com.aiait.eflow.util.DataConvertUtil;
import com.aiait.eflow.util.FieldUtil;
import com.aiait.framework.common.DataType;
import com.aiait.framework.db.BaseDAOImpl;
import com.aiait.framework.db.IDBManager;
import com.aiait.framework.exception.DAOException;
import com.aiait.framework.vo.BaseVO;

public class ReportManageDAO extends BaseDAOImpl {

	public ReportManageDAO(IDBManager dbManager) {
		super(dbManager);
	}

	public void executeSQL(String sql) throws DAOException {
		dbManager.executeUpdate(sql);
	}
	

	public Collection getReportList(ReportManageVO report) throws DAOException {
		Collection resultList = new ArrayList();
		StringBuffer reportSQL = new StringBuffer(
				"select * from teflow_report where 1=1 ");
		if (report.getStatus() != null && !"".equals(report.getStatus())) {
			reportSQL.append(" and status='").append(report.getStatus())
					.append("'");
		}
		if (report.getReportType() != null
				&& !"".equals(report.getReportType())) {
			// IT0958-DS011 begin
			reportSQL.append(" and report_type in (" + report.getReportType()
					+ ") ");
			// IT0958-DS011 end
		}
		if (report.getReportId() != null && !"".equals(report.getReportId())) {
			reportSQL.append(" and report_id like '%" + report.getReportId()
					+ "%'");
		}
		if (report.getOrgId() != null && !"".equals(report.getOrgId())) {
			reportSQL.append(" and org_id in (" + report.getOrgId() + ") ");
		}
		reportSQL.append(" order by create_date desc");
		// System.out.println(reportSQL.toString());
		Collection list = dbManager.query(reportSQL.toString());
		if (list.size() <= 0)
			return resultList;
		Iterator it = list.iterator();
		while (it.hasNext()) {
			HashMap map = (HashMap) it.next();
			ReportManageVO reportVo = convertMap(map);
			resultList.add(reportVo);
		}
		return resultList;
	}

	public ReportManageVO getReportBySystemId(int reportSystemId)
			throws DAOException {
		StringBuffer reportSQL = new StringBuffer(
				"select * from teflow_report where 1=1 ");
		reportSQL.append(" and report_system_id=").append(reportSystemId);
		// System.out.println("query sql : " + reportSQL.toString());
		Collection list = dbManager.query(reportSQL.toString());
		if (list.size() <= 0)
			return null;
		HashMap map = (HashMap) list.iterator().next();
		ReportManageVO report = new ReportManageVO();
		report.setReportName(DataConvertUtil.convertISOToGBK((String) map
				.get("REPORT_NAME")));
		if (map.get("REPORT_DESCRIPTION") != null)
			report.setDescription(DataConvertUtil.convertISOToGBK((String) map
					.get("REPORT_DESCRIPTION")));

		report.setReportId((String) map.get("REPORT_ID"));
		report.setReportSystemId(reportSystemId);
		report.setReportType(FieldUtil.convertSafeString(map, "REPORT_TYPE"));
		report.setDisplayType(FieldUtil.convertSafeString(map, "DISPLAY_TYPE"));
		report.setStatus(FieldUtil.convertSafeString(map, "STATUS"));
		report.setActionType(FieldUtil.convertSafeString(map, "ACTION_TYPE"));
		report.setActionMessage(FieldUtil.convertSafeString(map,
				"ACTION_MESSAGE"));
		report.setOrgId(FieldUtil.convertSafeString(map, "ORG_ID"));
		report.setPre_validation_url(FieldUtil.convertSafeString(map,
				"PRE_VALIDATION_URL"));
		report.setAfterSaveUrl(FieldUtil.convertSafeString(map,
				"AFTER_SAVE_URL"));
		report.setSectionList(getSectionListByReport(reportSystemId));

		return report;
	}

	public Collection getSectionListByReport(int reportSystemId)
			throws DAOException {
		StringBuffer sectionSQL = new StringBuffer(
				"select * from teflow_report_section where 1=1 ");
		sectionSQL.append(" and report_system_id=").append(reportSystemId)
				.append(" order by order_id,section_type desc,section_id ");
		Collection list = dbManager.query(sectionSQL.toString());
		if (list.size() <= 0)
			return new ArrayList();

		Collection resultList = new ArrayList();

		Iterator it = list.iterator();
		while (it.hasNext()) {
			HashMap map = (HashMap) it.next();
			ReportSectionVO section = new ReportSectionVO();
			section.setReportSystemId(reportSystemId);
			section.setSectionId((String) map.get("SECTION_ID"));
			if (map.get("SECTION_TYPE") != null) {
				section.setSectionType((String) map.get("SECTION_TYPE"));
			}
			if (map.get("SECTION_REMARK") != null) {
				section.setSectionRemark(DataConvertUtil
						.convertISOToGBK((String) map.get("SECTION_REMARK")));
			}
			if (map.get("TABLE_NAME") != null) {
				section.setTableName((String) map.get("TABLE_NAME"));
			}
			if (map.get("ORDER_ID") != null) {
				section.setOrderId((String) map.get("ORDER_ID"));
			} else {
				section.setOrderId("1");
			}
			// Add by Colin Wang for Search function
			if (map.get("SECTION_URL") != null) {
				section.setSectionUrl((String) map.get("SECTION_URL"));
			}
			if (map.get("PAGECOLCOUNT") != null) {
				
				section.setPageColCount(Integer.valueOf((String) map.get("PAGECOLCOUNT")));
			}
            if (map.get("remarkSectionId".toUpperCase()) != null) {
				
				section.setRemakSection((String) map.get("remarkSectionId".toUpperCase()));
			}
			// if sectionType="01", the section is table, it need add id field

			section.setFieldList(getSectionFieldListByReport(reportSystemId,
					section.getSectionId(), section.getSectionType()));
			
			section.setHtmlCode(this.getReportHtmlSecion(reportSystemId,section.getSectionId()));
			
			resultList.add(section);
		}
		return resultList;
	}

	public Collection getSectionFieldListByReport(int reportSystemId,
			String sectionId, String sectionType) throws DAOException {
		StringBuffer fieldSQL = new StringBuffer(
				"select * from teflow_report_section_field where 1=1 ");
		fieldSQL.append(" and report_system_id=").append(reportSystemId)
				.append(" and section_id='").append(sectionId).append("' ");
		fieldSQL.append(" order by order_id");
		Collection list = dbManager.query(fieldSQL.toString());
		Collection resultList = new ArrayList();
		
		if (list.size() <= 0)
			return resultList;
		
		Iterator it = list.iterator();
		while (it.hasNext()) {
			HashMap map = (HashMap) it.next();
			ReportSectionFieldVO field = parseField(map, reportSystemId);
			field.setSectionType(sectionType);
			resultList.add(field);
		}
		
		return resultList;
	}
	
	
	public HashMap getFieldContentByReport(String requestNo,String reportSystemId) throws DAOException {
        HashMap reportFieldValueMap = new HashMap();
        //ReportSectionFieldVO requestVo = SystemFieldHelper.getInstance().getBasicFieldById(CommonName.SYSTEM_ID_REQUEST_NO);
        String queryReportSectionTable = "SELECT * FROM teflow_report_section WHERE report_system_id = "+reportSystemId;
        Collection reportSectionList = dbManager.query(queryReportSectionTable);
        if (reportSectionList == null || reportSectionList.size() == 0)
            return null;
        Iterator it = reportSectionList.iterator();
        String sectionTableName = "";
        while (it.hasNext()) {
            HashMap sectionMap = (HashMap) it.next();
           
            sectionTableName = (String) sectionMap.get("TABLE_NAME");
            if (sectionTableName == null || "".equals(sectionTableName))
                continue;
            StringBuffer querySQL = new StringBuffer(" select * from " + sectionTableName);
            querySQL.append(" where ").append("request_no = '").append(requestNo).append("'");
            // if is table type, sort by id
            if ("01".equals((String) sectionMap.get("SECTION_TYPE")) || "04".equals((String) sectionMap.get("SECTION_TYPE")) || "05".equals((String) sectionMap.get("SECTION_TYPE")))
                querySQL.append(" order by id ");
            if("06".equals((String) sectionMap.get("SECTION_TYPE"))){
            	querySQL.append(" order by rowidx,colidx ");
            	String sql2 = "select count(distinct colidx) as colno from "+sectionTableName+" where request_no='"+requestNo+"'";
            	String sql3 = "select count(distinct rowidx) as rowno from "+sectionTableName+" where request_no='"+requestNo+"'";
            	Collection colNo = dbManager.query(sql2);
            	Collection rowNo = dbManager.query(sql3);
            	if(colNo != null && colNo.size() != 0){
            		Iterator colIt = colNo.iterator();
            		HashMap colMap = (HashMap) colIt.next();
            		reportFieldValueMap.put(sectionMap.get("SECTION_ID")+"_colNo", colMap.get("COLNO"));
            	}
            	if(rowNo != null && rowNo.size() != 0){
            		Iterator rowIt = rowNo.iterator();
            		HashMap rowMap = (HashMap) rowIt.next();
            		reportFieldValueMap.put(sectionMap.get("SECTION_ID")+"_rowNo", rowMap.get("ROWNO"));
            	}
            	String sql4 = "select * from TRptRemark";
            	Collection remark = dbManager.query(sql4);
            	if(remark != null && remark.size() != 0){
            		
            		Iterator remarkIt = remark.iterator();
            		while (remarkIt.hasNext()) {
            		HashMap remarkMap = (HashMap) remarkIt.next();
            		reportFieldValueMap.put((String)remarkMap.get("REMARKCODE"), (String)remarkMap.get("REMARKCONTENT"));
            		}
            	}
            	
            }
            	
            Collection list = dbManager.query(querySQL.toString());
            reportFieldValueMap.put(sectionMap.get("SECTION_ID"), list);
        }
        return reportFieldValueMap;
    }
	
	
	public String getReportScript(int reportSystemId) throws DAOException {
        String SQL = "select * from teflow_report_script where (report_system_id = ?)";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(SQL);
            int i = 1;
            stm.setInt(i++, reportSystemId);
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
	
	public String getReportHtmlSecion(int reportSystemId,String sectionid) throws DAOException {
        String SQL = "select * from teflow_report_html where (report_system_id = ? and section_id=?)";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(SQL);
            int i = 1;
            stm.setInt(i++, reportSystemId);
            stm.setString(i++, sectionid);
            ResultSet rs = stm.executeQuery();
            if (!rs.next()) {
                return null;
            }
            return rs.getString("html");
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
	
	public String getReportStyle(int reportSystemId) throws DAOException {
        String SQL = "select * from teflow_report_css_style where report_system_id = ?";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(SQL);
            int i = 1;
            stm.setInt(i++, reportSystemId);
            ResultSet rs = stm.executeQuery();
            if (!rs.next()) {
                return null;
            }
            return rs.getString("css_style");
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
	
	
	public void saveReportStyle(int reportSystemId,String css) throws DAOException {
        String SQL_INS = "insert into teflow_report_css_style (report_system_id,css_style) values(?,?)";
        String SQL_DEL = "delete from teflow_report_css_style where report_system_id = ?";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(SQL_DEL);
            int i = 1;
            stm.setInt(i++, reportSystemId);
            stm.executeUpdate();

            stm.close();
            stm = conn.prepareStatement(SQL_INS);
            i = 1;
            stm.setInt(i++, reportSystemId);
            stm.setString(i++, css);
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

	

	private ReportSectionFieldVO parseField(HashMap map, int reportSystemId) {
		ReportSectionFieldVO field = new ReportSectionFieldVO();
		field.setReportSystemId(reportSystemId);
		field.setFieldId((String) map.get("FIELD_ID"));
		field.setSectionId((String) map.get("SECTION_ID"));
		field.setFieldLabel(DataConvertUtil.convertISOToGBK((String) map
				.get("FIELD_LABEL")));
		if (map.get("FIELD_LENGTH") != null) {
			field
					.setFieldLength((new Integer((String) map
							.get("FIELD_LENGTH"))).intValue());
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
			field.setDataType((new Integer((String) map.get("DATA_TYPE")))
					.intValue());
		}
		if (map.get("FIELD_TYPE") != null) {
			field.setFieldType((new Integer((String) map.get("FIELD_TYPE")))
					.intValue());
		}
		if (map.get("SOURCE_TYPE") != null) {
			field.setFieldDataSourceType((new Integer((String) map
					.get("SOURCE_TYPE"))).intValue());
		}
		field.setFieldDataSrSQL((String) map.get("SOURCE_SQL"));
		if (map.get("ORDER_ID") != null) {
			field.setOrderId(Integer.parseInt((String) map.get("ORDER_ID")));
		}
		field.setDecimalDigits(FieldUtil.convertSafeInt(map, "DECIMAL_DIGITS",
				0));
		field.setHighLevel(FieldUtil.convertSafeInt(map, "HIGH_LEVEL", -1));
		field.setIsSingleRow(FieldUtil.convertSafeInt(map, "IS_SINGLEROW", -1));
		field.setControlsWidth(FieldUtil.convertSafeInt(map, "CONTROLS_WIDTH",
				30)); // default 30 columns
		field.setControlsHeight(FieldUtil.convertSafeInt(map,
				"CONTROLS_HEIGHT", 3)); // default 3 rows
		field.setFieldComments(DataConvertUtil.convertISOToGBK(FieldUtil
				.convertSafeString(map, "FIELD_COMMENTS")));
		field.setCommentContent(DataConvertUtil.convertISOToGBK(FieldUtil
				.convertSafeString(map, "COMMENT_CONTENT")));
		field
				.setDefaultValue(FieldUtil.convertSafeString(map,
						"DEFAULT_VALUE"));
		field.setColumnWidth(FieldUtil.convertSafeInt(map, "COLUMN_WIDTH", 10)); // default设定为10%
		
		field.setIsReadonly(FieldUtil.convertSafeInt(map, "IS_READONLY", -1));
		
		field.setIsSingleLabel(FieldUtil.convertSafeInt(map, "IS_SINGLELABEL", -1));
		//field.setAlignedPosition(FieldUtil.convertSafeInt(map, "ALIGNED_POSITION", -1));
		field.setCssStr(FieldUtil.convertSafeString(map,"CSS_STYLE"));
		field.setBorder(FieldUtil.convertSafeInt(map, "BORDER", -1));
		field.setAligned(FieldUtil.convertSafeInt(map, "ALIGNED", 1));

		return field;
	}

	private ReportManageVO convertMap(HashMap map) {
		ReportManageVO report = new ReportManageVO();
		report.setReportSystemId(Integer.parseInt((String) map
				.get("REPORT_SYSTEM_ID")));
		report.setReportId((String) map.get("REPORT_ID"));
		if (map.get("REPORT_NAME") != null) {
			report.setReportName(DataConvertUtil.convertISOToGBK((String) map
					.get("REPORT_NAME")));
		}
		if (map.get("REPORT_TYPE") != null) {
			report.setReportType((String) map.get("REPORT_TYPE"));
		}
		
		if (map.get("DISPLAY_TYPE") != null) {
			report.setDisplayType((String) map.get("DISPLAY_TYPE"));
		}
		if (map.get("REPORT_IMAGE") != null) {
			report.setReportSystemImg((String) map.get("REPORT_IMAGE"));
		}
		if (map.get("REPORT_DESCRIPTION") != null) {
			report.setDescription(DataConvertUtil.convertISOToGBK((String) map
					.get("REPORT_DESCRIPTION")));
		}
		if (map.get("STATUS") != null) {
			report.setStatus((String) map.get("STATUS"));
		}
		if (map.get("CREATE_DATE") != null) {
			report.setCreateDateStr((String) map.get("CREATE_DATE"));
		}
		report.setActionType(FieldUtil.convertSafeString(map, "ACTION_TYPE"));
		report.setActionMessage(FieldUtil.convertSafeString(map,
				"ACTION_MESSAGE"));
		report.setOrgId(FieldUtil.convertSafeString(map, "ORG_ID"));
		report.setPre_validation_url(FieldUtil.convertSafeString(map,
				"PRE_VALIDATION_URL"));
		report.setAfterSaveUrl(FieldUtil.convertSafeString(map,
				"AFTER_SAVE_URL"));

		return report;
	}
	
	
	public ReportSectionVO getReportSectionInfor(int reportSystemId, String sectionId) throws DAOException {
        String sql = "select * from teflow_report_section where report_system_id=" + reportSystemId + " and section_id='"
                + sectionId + "' order by section_type desc,section_id";
        Collection list = dbManager.query(sql);
        if (list == null || list.size() == 0)
            return null;
        HashMap map = (HashMap) list.iterator().next();
        ReportSectionVO vo = new ReportSectionVO();
        vo.setSectionId(sectionId);
        vo.setReportSystemId(reportSystemId);
        vo.setSectionRemark(DataConvertUtil.convertISOToGBK(FieldUtil.convertSafeString(map, "SECTION_REMARK")));
        vo.setSectionType(FieldUtil.convertSafeString(map, "SECTION_TYPE"));
        vo.setTableName(FieldUtil.convertSafeString(map, "TABLE_NAME"));
        vo.setPageColCount(FieldUtil.convertSafeInt(map, "PAGECOLCOUNT", 0));
        vo.setRemakSection(FieldUtil.convertSafeString(map, "REMARKSECTIONID"));
        return vo;
    }
	
	 public Map getAllReportScript() throws DAOException {
	        String SQL = "select * from teflow_report_script";

	        Connection conn = dbManager.getJDBCConnection();
	        PreparedStatement stm = null;

	        try {
	            stm = conn.prepareStatement(SQL);
	            int i = 1;
	            HashMap scriptMap = new HashMap();
	            ResultSet rs = stm.executeQuery();
	            while (rs.next()) {
	                scriptMap.put(rs.getString("report_system_id"), rs.getString("script"));
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
	
	
	 public Map getAllReportHtml() throws DAOException {
	        String SQL = "select * from teflow_report_html";

	        Connection conn = dbManager.getJDBCConnection();
	        PreparedStatement stm = null;

	        try {
	            stm = conn.prepareStatement(SQL);
	            int i = 1;
	            HashMap htmlMap = new HashMap();
	            ResultSet rs = stm.executeQuery();
	            while (rs.next()) {
	                htmlMap.put(rs.getString("report_system_id")+"_"+rs.getString("section_id"), rs.getString("html"));
	            }
	            return htmlMap;
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
	
	public boolean checkReportSectionId(int reportSystemId, String sectionId) throws DAOException {
        String sql = "select * from teflow_report_section where report_system_id=" + reportSystemId + " and section_id='"
                + sectionId + "'";
        Collection list = dbManager.query(sql);
        if (list == null || list.size() == 0)
            return false;
        return true;
    }

	public void saveBaseReport(ReportManageVO report) throws DAOException {

		String sql = "INSERT INTO teflow_report(report_id,report_name,report_type,report_description,status,org_id,display_type) values(?,?,?,?,?,?,?)";
		// paramValues.add(new Date());
		Object[] obj = new Object[7];
		obj[0] = report.getReportId();
		obj[1] = report.getReportName();
		obj[2] = report.getReportType();
		obj[3] = report.getDescription();
		obj[4] = report.getStatus();
		obj[5] = report.getOrgId();
		obj[6] = report.getDisplayType();

		int[] dataType = { DataType.VARCHAR, DataType.VARCHAR,
				DataType.VARCHAR, DataType.VARCHAR, DataType.VARCHAR,
				DataType.VARCHAR, DataType.VARCHAR};
		dbManager.executeUpdate(sql, obj, dataType);
		sql = "select report_system_id from teflow_report where report_id='"+report.getReportId()+"'";
		
		Connection conn = dbManager.getJDBCConnection();
	    PreparedStatement stm = null;
	    try {
	         stm = conn.prepareStatement(sql);
	         ResultSet rs = stm.executeQuery();
	         if (!rs.next()) {
	            return;
	         }
	                       
	    ReportSectionVO title = new ReportSectionVO();
	    title.setReportSystemId(rs.getInt("report_system_id"));
	    title.setSectionId("title");
	    title.setSectionType("02");
	    title.setTableName("teflow_report_"+rs.getInt("report_system_id")+"_title");
	    title.setOrderId("8");
	    List<ReportSectionFieldVO> titlelist = new ArrayList<ReportSectionFieldVO>();
	    ReportSectionFieldVO titlefield = new ReportSectionFieldVO();
	    titlefield.setReportSystemId(rs.getInt("report_system_id"));
	    titlefield.setSectionId("title");
	    titlefield.setFieldId("field_title");
	    titlefield.setDataType(1);
	    titlefield.setFieldType(1);
	    titlefield.setIsRequired(false);
	    titlefield.setFieldLength(100);
	    titlefield.setFieldLabel("Title");
	    titlefield.setIsSingleRow(1);
	    titlefield.setIsReadonly(1);
	    titlefield.setCssStr("0,0,12,normal,400,黑体;0,0,15,normal,400,黑体");
	    titlefield.setIsSingleLabel(1);
	    titlefield.setDefaultValue(report.getReportName());
	    this.saveReportSecitonField(titlefield);
	    titlelist.add(titlefield);
	    title.setFieldList(titlelist);	    
	    this.createSectionTable(title);
	    this.saveSection(title);
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

	public void updateBaseReport(ReportManageVO report) throws DAOException {
		String sql = "UPDATE teflow_report SET report_id=?,report_name=?,report_type=?,report_description=?,org_id=?,display_type=? WHERE report_system_id=?";
		Object[] obj = new Object[7];
		obj[0] = report.getReportId();
		obj[1] = report.getReportName();
		obj[2] = report.getReportType();
		obj[3] = report.getDescription();
		obj[4] = report.getOrgId();
		obj[5] = report.getDisplayType();
		obj[6] = "" + report.getReportSystemId();
		
		int[] dataType = { DataType.VARCHAR, DataType.VARCHAR,
				DataType.VARCHAR, DataType.VARCHAR, DataType.VARCHAR,DataType.VARCHAR,
				DataType.INT };
		dbManager.executeUpdate(sql, obj, dataType);
	}
	
	public void saveSection(ReportSectionVO section) throws DAOException {
        String sql = "INSERT INTO teflow_report_section(report_system_id,section_id,section_type,section_remark,table_name,section_url,PageColCount,remarkSectionId) values(?,?,?,?,?,?,?,?)";
        Object[] obj = new Object[8];
        obj[0] = "" + section.getReportSystemId();
        obj[1] = section.getSectionId();
        obj[2] = section.getSectionType();
        obj[3] = section.getSectionRemark();
        obj[4] = section.getTableName();
        obj[5] = section.getSectionUrl();
        obj[6] = ""+section.getPageColCount();
        obj[7] = section.getRemakSection();
        int[] dataType = {DataType.INT, DataType.VARCHAR, DataType.VARCHAR, DataType.VARCHAR, DataType.VARCHAR, DataType.VARCHAR,DataType.INT,DataType.VARCHAR};
        dbManager.executeUpdate(sql, obj, dataType);
    }
	
	
	public void updateReportSectionInfor(ReportSectionVO section) throws DAOException {

        String sql = "update teflow_report_section set section_remark='" + section.getSectionRemark()
                + "',section_type='" + section.getSectionType() + "', PageColCount="+section.getPageColCount() + ",remarkSectionId='"+section.getRemakSection()+"' where report_system_id="
                + section.getReportSystemId() + " and section_id='" + section.getSectionId() + "'";
        // System.out.println(sql);
        dbManager.executeUpdate(sql);
    }
	
	public void saveReportSecitonField(ReportSectionFieldVO field) throws DAOException {
        // IT0958 begin
        String sql = "INSERT INTO teflow_report_section_field(report_system_id,section_id,field_id,field_label,field_type,is_required,data_type,field_length,source_type,source_sql,order_id,decimal_digits,high_level,is_singlerow,controls_width,controls_height,field_comments,comment_content,"
                + "default_value,is_readonly,css_style,border,is_singlelabel,aligned)";
        sql = sql + " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        Object[] obj = new Object[24];

        obj[0] = "" + field.getReportSystemId();
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
        obj[13] = "" + field.getIsSingleRow();
        obj[14] = "" + field.getControlsWidth();
        obj[15] = "" + field.getControlsHeight();
        obj[16] = field.getFieldComments();
        obj[17] = field.getCommentContent();
        obj[18] = field.getDefaultValue();
        obj[19] = "" + field.getIsReadonly();
        obj[20] = "" + field.getCssStr();
        obj[21] = "" + field.getBorder();
        obj[22] = "" + field.getIsSingleLabel();
        obj[23] = ""+field.getAligned();
        

        int[] dataType = {DataType.INT, DataType.VARCHAR, DataType.VARCHAR, DataType.VARCHAR, DataType.INT,
                DataType.INT, DataType.INT, DataType.INT, DataType.INT, DataType.VARCHAR, DataType.INT, DataType.INT,
                DataType.INT, DataType.INT, DataType.INT, DataType.INT, DataType.VARCHAR,
                DataType.VARCHAR, DataType.VARCHAR, DataType.INT, DataType.VARCHAR, DataType.INT, DataType.INT, DataType.INT};
        // IT0958 end
        dbManager.executeUpdate(sql, obj, dataType);
    }
	
	public void updateReportSectionField(ReportSectionFieldVO field) throws DAOException {
        // IT0958 begin
        String sql = "UPDATE teflow_report_section_field set field_label=?,field_type=?,is_required=?,data_type=?,field_length=?,source_type=?,source_sql=?,order_id=?,decimal_digits=?,high_level=?,is_singlerow=?,controls_width=?,controls_height=?,field_comments=?,comment_content=?,"
                + "default_value=?,is_readonly=?, css_style=?,border=?,is_singlelabel=?,aligned=?";
        sql = sql + " WHERE report_system_id=? and section_id=? and field_id=?";
        Object[] obj = new Object[24];
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

        obj[10] = "" + field.getIsSingleRow();
        obj[11] = "" + field.getControlsWidth();
        obj[12] = "" + field.getControlsHeight();
        obj[13] = field.getFieldComments();
        obj[14] = field.getCommentContent();
        obj[15] = field.getDefaultValue();

        obj[16] = "" + field.getIsReadonly();
        obj[17] = field.getCssStr();
        obj[18] = "" + field.getBorder();
        obj[19] = "" + field.getIsSingleLabel();
        obj[20] = ""+field.getAligned();

        obj[21] = "" + field.getReportSystemId();
        obj[22] = field.getSectionId();
        obj[23] = field.getFieldId();
        

        int[] dataType = {DataType.VARCHAR, DataType.INT, DataType.INT, DataType.INT, DataType.INT, DataType.INT,
                DataType.VARCHAR, DataType.INT, DataType.INT, DataType.INT, DataType.INT, DataType.INT,
                DataType.INT, DataType.VARCHAR, DataType.VARCHAR, DataType.VARCHAR, DataType.INT,DataType.VARCHAR, DataType.INT, DataType.INT,DataType.INT ,DataType.INT, DataType.VARCHAR,
                DataType.VARCHAR};
        // IT0958 begin
        dbManager.executeUpdate(sql, obj, dataType);
    }
	
	
	public void updateSectionOrder(int reportSystemId, String sectionId, String orderId) throws DAOException {
        String updateSQL = "update teflow_report_section set order_id='" + orderId + "' where report_system_id="
                + reportSystemId + " and section_id='" + sectionId + "' ";
        dbManager.executeUpdate(updateSQL);
    }
	
	public void addSectionTableColumn(ReportSectionFieldVO field) throws DAOException {
        // section对应数据保存的表名格式是固定的：teflow_report的ID_section的ID
        StringBuffer addSQL = new StringBuffer("alter table " + FieldUtil.getSectionDataTableName(field) + "  add ")
                .append(getColumnStr(field));
        // System.out.println("sql:"+addSQL.toString());
        dbManager.executeUpdate(addSQL.toString());
    }
	
	public void alertSectionTableColumn(ReportSectionFieldVO field) throws DAOException {
        StringBuffer alertSQL = new StringBuffer("alter table " + FieldUtil.getSectionDataTableName(field)
                + " ALTER COLUMN " + getColumnStr(field));
        // System.out.println("sql:"+alertSQL.toString());
        dbManager.executeUpdate(alertSQL.toString());
    }
	
	
	public void createSectionTable(ReportSectionVO section) throws DAOException {
        // section对应数据保存的表名格式是固定的：teflow_Report的ID_section的ID
        StringBuffer sql = new StringBuffer("create table dbo.teflow_report_" + section.getReportSystemId()).append("_")
                .append(section.getSectionId()).append(" ( ");
               

        Collection fieldList = section.getFieldList();
        
        if ("01".equals(section.getSectionType()) || "00".equals(section.getSectionType()) || "04".equals(section.getSectionType()) || "05".equals(section.getSectionType()) || 
				"06".equals(section.getSectionType()) || "09".equals(section.getSectionType()) || "0A".equals(section.getSectionType()) || "0B".equals(section.getSectionType())) { 
			ReportSectionFieldVO field = new ReportSectionFieldVO();
			field.setFieldId("id");
			field.setFieldType(CommonName.FIELD_TYPE_IDENTITY);
			field.setDataType(3);// numberic
			field.setFieldLength(20);
			field.setDecimalDigits(0);
			field.setIsRequired(true);
			fieldList.add(field);
		}
        
        Iterator fieldIt = fieldList.iterator();
        // int length = 50;
        while (fieldIt.hasNext()) {
            ReportSectionFieldVO field = (ReportSectionFieldVO) fieldIt.next();
            sql.append(getColumnStr(field)).append(",");
        }
        
        if("06".equals(section.getSectionType())){
        	sql.append("rowidx decimal(20,0) NOT NULL, colidx decimal(10,0) NOT NULL, itemvalue varchar(500) NULL," +
        			"class varchar(10) NULL,itemtype INT NULL,RemarkCode varchar(100) NULL,Editable varchar(1) NULL, Merged varchar(10) NULL,");
        }else if("09".equals(section.getSectionType()) || "0A".equals(section.getSectionType()) || "0B".equals(section.getSectionType()) || "04".equals(section.getSectionType())){
        	sql.append("VarName varchar(100) NOT NULL, VarValue NVARCHAR(max) NULL, RemarkCode varchar(100) NULL,Editable varchar(1) NULL,");
        }
        
        
       
        ReportSectionFieldVO requestVo = SystemFieldHelper.getInstance().getBasicFieldByIdforReport(
                    CommonName.SYSTEM_ID_REQUEST_NO);
        sql.append(getColumnStr(requestVo)).append(",");
             

        sql = new StringBuffer(sql.toString().substring(0, sql.toString().length() - 1));

        sql.append(") on [PRIMARY]");

        dbManager.executeUpdate("if exists (select [name] from SysObjects where [NAME]='teflow_report_"
                + section.getReportSystemId() + "_" + section.getSectionId() + "')  drop table dbo.teflow_report_"
                + section.getReportSystemId() + "_" + section.getSectionId());
         System.out.println("create table sql : " + sql.toString());
        dbManager.executeUpdate(sql.toString());
        // dbManager.executeUpdate("GRANT  SELECT ,  UPDATE ,  INSERT ,  DELETE  ON dbo.teflow_"+section.getReportSystemId()+"_"+section.getSectionId()+"  TO [pmauser]");
    }
	
	 public void deleteSection(int reportSystemId, String sectionId) throws DAOException {
	        dbManager.executeUpdate("delete from teflow_report_section_field where report_system_id=" + reportSystemId + " and "
	                + " section_id='" + sectionId + "'");
	        dbManager.executeUpdate("delete from teflow_report_section where report_system_id=" + reportSystemId + " and "
	                + " section_id='" + sectionId + "'");
	    }
	
	public void delSectionTableColumn(ReportSectionFieldVO field) throws DAOException {
        // section对应数据保存的表名格式是固定的：teflow_report的ID_section的ID
        StringBuffer delSQL = new StringBuffer("if exists (select 1 from SYSCOLUMNS WHERE ID=OBJECT_ID('"+ FieldUtil.getSectionDataTableName(field)+"') and NAME='"+ field.getFieldId() +"')  alter table " + FieldUtil.getSectionDataTableName(field)
                + "  drop COLUMN " + field.getFieldId() + " ");
        // System.out.println("sql:"+delSQL.toString());
        dbManager.executeUpdate(delSQL.toString());
    }
	
	
	public void updateSectionColumnWidth(int reportSystemId, String sectionId,
			String fieldId, int columnWidth) throws DAOException {
		String sql = "update teflow_report_section_field set column_width="
				+ columnWidth + " where report_system_id=" + reportSystemId
				+ " and section_id='" + sectionId + "' and field_id='"
				+ fieldId + "'";
		dbManager.executeUpdate(sql);
	}
	
	public void deleteReportSectionField(int reportSystemId, String sectionId, String fieldId) throws DAOException {
        String sql = "DELETE FROM teflow_report_section_field WHERE report_system_id=? and section_id=? and field_id=?";
        Object[] obj = new Object[3];
        obj[0] = "" + reportSystemId;
        obj[1] = sectionId;
        obj[2] = fieldId;
        int[] dataType = {DataType.INT, DataType.VARCHAR, DataType.VARCHAR};
        dbManager.executeUpdate(sql, obj, dataType);
    }
	
	
	public void saveReportScript(int reportSystemId, String script) throws DAOException {
        String SQL_INS = "insert into teflow_report_script(report_system_id, script) values(?, ?)";
        String SQL_DEL = "delete from teflow_report_script where (report_system_id = ?)";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(SQL_DEL);
            int i = 1;
            stm.setInt(i++, reportSystemId);
            stm.executeUpdate();

            stm.close();
            stm = conn.prepareStatement(SQL_INS);
            i = 1;
            stm.setInt(i++, reportSystemId);
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
	
	
	public void saveReportHtml(int reportSystemId,String sectionId, String html) throws DAOException {
        String SQL_INS = "insert into teflow_report_html(report_system_id, section_id,html) values(?, ?,?)";
        String SQL_DEL = "delete from teflow_report_html where (report_system_id = ? and section_id=?)";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(SQL_DEL);
            int i = 1;
            stm.setInt(i++, reportSystemId);
            stm.setString(i++, sectionId);
            stm.executeUpdate();

            stm.close();
            stm = conn.prepareStatement(SQL_INS);
            i = 1;
            stm.setInt(i++, reportSystemId);
            stm.setString(i++, sectionId);
            stm.setString(i++, html);
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
	
	
	private String getColumnStr(ReportSectionFieldVO field) {
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
        	sql.append(" VARCHAR (").append(length).append(") ");
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
	
	
	public ReportSectionFieldVO getField(int reportSystemId, String sectionId, String fieldId) throws DAOException {
        StringBuffer fieldSQL = new StringBuffer("select * from teflow_report_section_field where 1=1 ");
        fieldSQL.append(" and report_system_id=").append(reportSystemId).append(" and section_id='").append(sectionId)
                .append("' ").append(" and field_id='").append(fieldId).append("'");
        Collection list = dbManager.query(fieldSQL.toString());
        Iterator it = list.iterator();
        // System.out.println("QUERY SQL:" + fieldSQL.toString());
        if (!it.hasNext())
            return new ReportSectionFieldVO();
        HashMap map = (HashMap) it.next();
        ReportSectionFieldVO field = parseField(map, reportSystemId);
        return field;
    }
	
	
	public int getMaxFieldNo(int reportSystemId, String sectionId) throws DAOException {
        String sql = "select Max(Convert(int,SUBSTRING(field_id, 1 + { fn LENGTH('field_" + sectionId
                + "_') }, { fn LENGTH(field_id) } - { fn LENGTH('field_" + sectionId
                + "') }))) as field_id from teflow_report_section_field where report_system_id=" + reportSystemId
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
	
	
	public boolean checkReport(int reportSystemId) throws DAOException {
        String sql = "select section_id from teflow_report_section where report_system_id=" + reportSystemId + "";
        Collection sectionList = dbManager.query(sql);
        if (sectionList == null || sectionList.size() == 0)
            return false;
        sectionList = null;
        sql = "select section_id from teflow_report_section where report_system_id=" + reportSystemId
                + " and section_id not in(SELECT section_id from teflow_report_section_field where report_system_id="
                + reportSystemId + ") AND section_type not in('06','09','0A','0B','04')";
        sectionList = dbManager.query(sql);
        if (sectionList != null && sectionList.size() > 0)
            return false;
        return true;
    }
	
	
	
	public void publishReport(int reportSystemId) throws DAOException {
        String updateSql = "update teflow_report set status='0' where report_system_id = " + reportSystemId;

        Collection sectionList = getSectionListByReport(reportSystemId);
        Iterator sectionIt = sectionList.iterator();
        while (sectionIt.hasNext()) {
            ReportSectionVO section = (ReportSectionVO) sectionIt.next();
           
                createSectionTable(section);
            
        }

        dbManager.executeUpdate(updateSql);

    }

	public void updateReportInstance(int reportSystemId,String formSystemId,String requestNo,String reportNo,String fieldId) throws Exception {
       String getTableName = "select table_name from teflow_form_section s inner join teflow_form_section_field f "
        		+ "on s.form_system_id = f.form_system_id and s.section_id = f.section_id where f.form_system_id = ? and f.field_id = ?";
        Object[] objs2 = new Object[2];
        objs2[0] = formSystemId;
        objs2[1] = fieldId;
        int[] dataType2 = {DataType.INT, DataType.VARCHAR};
        Collection list = dbManager.query(getTableName,objs2,dataType2);
        if (list == null || list.size() == 0){
        	throw new Exception("Cannot find the table name for the field:"+fieldId);
        }
        HashMap map = (HashMap) list.iterator().next();
        String tableName = FieldUtil.convertSafeString(map, "TABLE_NAME");
        
        String updateSql = "update "+tableName+" set "+fieldId+" = ? where request_no = ?";
        Object[] objs3 = new Object[2];
        objs3[0] = reportNo;
        objs3[1] = requestNo;
        int[] dataType3 = {DataType.VARCHAR, DataType.VARCHAR};
        dbManager.executeUpdate(updateSql,objs3,dataType3);
        
        updateSql = "insert into teflow_report_instance (report_system_id,form_request_no,report_no,repost_vsersion) values(?,?,?,"
        		+ "1+(select isNull( max(repost_vsersion),0) from teflow_report_instance where report_system_id=? and form_request_no=? ))" ;
        Object[] objs = new Object[5];
        objs[0] = ""+reportSystemId;
        objs[1] = requestNo;
        objs[2] = reportNo;
        objs[3] = ""+reportSystemId;
        objs[4] = requestNo;
        int[] dataType = {DataType.INT, DataType.VARCHAR, DataType.VARCHAR, DataType.INT, DataType.VARCHAR};
        dbManager.executeUpdate(updateSql,objs,dataType);
    }
	
	
	@Override
	public int delete(BaseVO arg0) throws DAOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int save(BaseVO arg0) throws DAOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(BaseVO arg0) throws DAOException {
		// TODO Auto-generated method stub
		return 0;
	}

}
