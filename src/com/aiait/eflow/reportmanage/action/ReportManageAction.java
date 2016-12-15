package com.aiait.eflow.reportmanage.action;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.aiait.eflow.basedata.dao.BaseDataDAO;
import com.aiait.eflow.basedata.vo.BaseDataVO;
import com.aiait.eflow.common.CommonName;
import com.aiait.eflow.common.ModuleOperateName;
import com.aiait.eflow.common.helper.AuthorityHelper;
import com.aiait.eflow.common.helper.CompanyHelper;
import com.aiait.eflow.housekeeping.vo.StaffVO;
import com.aiait.eflow.reportmanage.dao.ReportManageDAO;
import com.aiait.eflow.reportmanage.helper.ReportHtmlHelper;
import com.aiait.eflow.reportmanage.helper.ReportScriptHelper;
import com.aiait.eflow.reportmanage.helper.ReportTypeHelper;
import com.aiait.eflow.reportmanage.util.ExportFileUtil;
import com.aiait.eflow.reportmanage.vo.ReportManageVO;
import com.aiait.eflow.reportmanage.vo.ReportSectionFieldVO;
import com.aiait.eflow.reportmanage.vo.ReportSectionVO;
import com.aiait.eflow.reportmanage.vo.ReportTypeVO;
import com.aiait.eflow.util.FieldUtil;
import com.aiait.eflow.util.StringUtil;
import com.aiait.framework.db.DBManagerFactory;
import com.aiait.framework.db.IDBManager;
import com.aiait.framework.exception.DAOException;
import com.aiait.framework.i18n.I18NMessageHelper;
import com.aiait.framework.mvc.action.ActionLocation;
import com.aiait.framework.mvc.action.DispatchAction;
import com.aiait.framework.mvc.action.ModuleMapping;
import com.aiait.framework.util.CommonUtil;
import com.lowagie.text.DocumentException;

public class ReportManageAction extends DispatchAction {

	public ActionLocation manageReport(ModuleMapping mapping,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String returnLabel = "showManageReport";
		String reportId = (String) request.getParameter("reportId");
		String reportType = (String) request.getParameter("reportType");
		String status = (String) request.getParameter("status");
		String orgId = (String) request.getParameter("orgId");

		String superOrgIds = "";
		StaffVO currentStaff = (StaffVO) request.getSession().getAttribute(
				CommonName.CURRENT_STAFF_INFOR);
		// modify by Robin Hou 2008-06-19
		// 如果orgId为空，则默认查询其所在公司以及其所有下级公司的所有form
		if (orgId == null || "".equals(orgId)) {
			orgId = currentStaff.getOrgId();
			superOrgIds = currentStaff.getLowerCompanys(); // CompanyHelper.getInstance().getLowerCompany(orgId);
		} else {
			superOrgIds = "'" + orgId + "'";
		}
		// end modify

		IDBManager dbManager = null;
		try {
			dbManager = DBManagerFactory.getDBManager();
			ReportManageDAO reportDao = new ReportManageDAO(dbManager);
			ReportManageVO queryVo = new ReportManageVO();
			if (reportId != null) {
				queryVo.setReportId(reportId.trim());
			}

			if (reportType != null && !"".equals(reportType)) {
				queryVo.setReportType("'" + reportType + "'");
			} else {
				String reportTypes = "";

				AuthorityHelper authority = AuthorityHelper.getInstance();

				Collection typeList = ReportTypeHelper.getInstance()
						.getReportTypeList();
				if (typeList != null && typeList.size() > 0) {
					Iterator it = typeList.iterator();
					while (it.hasNext()) {
						ReportTypeVO typeVo = (ReportTypeVO) it.next();
						if (authority.checkAuthorityByReportType(currentStaff
								.getCurrentRoleId(),
								ModuleOperateName.MODULE_FORM_MANAGE, typeVo
										.getReportTypeId())) {
							reportTypes = reportTypes + "'"
									+ typeVo.getReportTypeId() + "',";
						}
					}
				}

				if (!"".equals(reportTypes)) {
					reportTypes = reportTypes.substring(0,
							reportTypes.length() - 1);
				}
				queryVo.setReportType(reportTypes);
			}
			queryVo.setOrgId(superOrgIds);
			// IT0958-DS011 End
			if (status != null) {
				queryVo.setStatus(status);
			}
			// 获取所有的form
			Collection reportList = reportDao.getReportList(queryVo);
			request.setAttribute("reportList", reportList);
		} catch (DAOException e) {
			e.printStackTrace();
			returnLabel = "fail";
			request.setAttribute(CommonName.COMMON_ERROR_INFOR,
					I18NMessageHelper.getMessage("common.databaseerror")
							+ ":<br>" + e.toString());
		} finally {
			dbManager.freeConnection();
		}
		return mapping.findActionLocation(returnLabel);
	}

	public ActionLocation enterEditReport(ModuleMapping mapping,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String returnLabel = "newReportPage";
		String type = (String) request.getParameter("type");
		if (type == null) {
			type = "new";
		}
		if ("edit".equals(type) || "editWholeReport".equals(type)) {
			String reportSystemId = (String) request
					.getParameter("reportSystemId");
			IDBManager dbManager = null;
			try {
				dbManager = DBManagerFactory.getDBManager();
				ReportManageDAO reportDao = new ReportManageDAO(dbManager);
				ReportManageVO report = reportDao.getReportBySystemId(Integer
						.parseInt(reportSystemId));
				request.setAttribute("wholeReport", report);

			} catch (DAOException e) {
				e.printStackTrace();
				returnLabel = "fail";
			} finally {
				dbManager.freeConnection();
			}
		}
		if ("editWholeReport".equals(type)) {
			returnLabel = "editWholeReportPage";
		}

		return mapping.findActionLocation(returnLabel);
	}

	public ActionLocation saveBaseReport(ModuleMapping mapping,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ReportManageVO report = new ReportManageVO();
		String reportId = CommonUtil.decoderURL((String) request
				.getParameter("reportId"));
		String reportName = CommonUtil.decoderURL((String) request
				.getParameter("reportName"));
		String reportDescription = (String) request
				.getParameter("reportDescription");

		String orgId = (String) request.getParameter("orgId");
		String reportType = (String) request.getParameter("reportType");
		String saveType = (String) request.getParameter("saveType");
		String displayType =  (String) request.getParameter("displayType");
	
		reportDescription = CommonUtil.decoderURL(reportDescription);

		report.setReportId(reportId);
		report.setReportName(reportName);
		report.setReportType(reportType);
		report.setDescription(reportDescription);
        report.setDisplayType(displayType);
		report.setOrgId(orgId);

		if ("new".equals(saveType)) {
			report.setStatus("1");
		} else {
			report.setReportSystemId(Integer.parseInt((String) request
					.getParameter("reportSystemId")));
			report.setStatus(request.getParameter("status"));
		}

		PrintWriter out = response.getWriter();
		IDBManager dbManager = null;
		try {
			dbManager = DBManagerFactory.getDBManager();
			ReportManageDAO reportDao = new ReportManageDAO(dbManager);
			if ("new".equals(saveType)) {
				reportDao.saveBaseReport(report);
			} else {
				reportDao.updateBaseReport(report);
			}
			out.println("successfully");
		} catch (DAOException e) {
			out.println("unsuccessfully");
			e.printStackTrace();
		} finally {
			if (out != null)
				out.close();
			dbManager.freeConnection();
		}
		return null;
	}

	public ActionLocation enterEditReportSection(ModuleMapping mapping,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String returnLabel = "sectionEditPage";
		String type = (String) request.getParameter("type");
		if ("edit".equals(type)) {
			IDBManager dbManager = null;
			String reportSystemId = (String) request
					.getParameter("reportSystemId");
			String sectionId = (String) request.getParameter("sectionId");
			try {
				dbManager = DBManagerFactory.getDBManager();
				ReportManageDAO reportDao = new ReportManageDAO(dbManager);
				ReportSectionVO section = reportDao.getReportSectionInfor(
						Integer.parseInt(reportSystemId), sectionId);
				if (section == null) {
					request.setAttribute(CommonName.COMMON_TIP_INFO,
							I18NMessageHelper.getMessage("common.happenerror"));
					mapping.findActionLocation("fail");
				}
				request.setAttribute("section", section);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				dbManager.freeConnection();
			}
		}
		return mapping.findActionLocation(returnLabel);
	}

	public ActionLocation saveReportSection(ModuleMapping mapping,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String reportSystemId = (String) request.getParameter("reportSystemId");
		String sectionId = (String) request.getParameter("sectionId");
		String sectionType = (String) request.getParameter("sectionType");
	    String sectionUrl = (String)request.getParameter("sectionUrl");
		String sectionRemark = CommonUtil.decoderURL((String) request
				.getParameter("sectionRemark"));
		String PageColCountStr = (String)request.getParameter("PageColCount");
		String remarkSectionId = (String)request.getParameter("remarkSectionId");
		
		
		// System.out.println("sectionRemark: " + sectionRemark);
		String type = (String) request.getParameter("type");
		if (type == null) {
			type = "new";
		}

		ReportSectionVO section = new ReportSectionVO();
		ReportSectionFieldVO field = new ReportSectionFieldVO();
		field.setReportSystemId(Integer.parseInt(reportSystemId));
		field.setSectionId(sectionId);

		section.setReportSystemId(Integer.parseInt(reportSystemId));
		section.setSectionId(sectionId);
		section.setSectionType(sectionType);
		section.setSectionRemark(sectionRemark);
		section.setSectionUrl(sectionUrl);
		section.setRemakSection(remarkSectionId);
		if(PageColCountStr!=null && !"".equals(PageColCountStr)){
			Integer pageColCount = Integer.parseInt(PageColCountStr);
			section.setPageColCount(pageColCount);
		}
		
		if ("new".equals(type)) {
			section.setTableName(FieldUtil.getSectionDataTableName(field));
		}
		IDBManager dbManager = null;
		PrintWriter out = response.getWriter();
		StringBuffer result = new StringBuffer("");
		response.setHeader("Cache-Control", "no-cache");
		try {
			dbManager = DBManagerFactory.getDBManager();
			ReportManageDAO reportDao = new ReportManageDAO(dbManager);
			dbManager.startTransaction();
			if ("new".equals(type)) {// it is new
				// 每个report中section_id必须是唯一的，不能存在重复
				if (reportDao.checkReportSectionId(Integer
						.parseInt(reportSystemId), section.getSectionId()) == true) {
					out.print("multi-sectionid");
					return null;
				}
	
				// save report
				reportDao.saveSection(section);
				

				if ("01".equals(sectionType) || "04".equals(sectionType) || "05".equals(sectionType)) {
					result
							.append(
									"<div id=\"div"
											+ section.getSectionId()
											+ "\"><b>"
											+ section.getSectionRemark()
											+ "</b>:<input type='button' name='addBtn"
											+ section.getSectionId()
											+ "' value='Add Column' onclick='addCol("
											+ "\""
											+ "sectionTable"
											+ section.getSectionId()
											+ "\",\""
											+ section.getSectionId()
											+ "\")' class=btn3_mouseout onmouseover=\"this.className='btn3_mouseover'\" onmouseout=\"this.className='btn3_mouseout'\" onmousedown=\"this.className='btn3_mousedown'\" onmouseup=\"this.className='btn3_mouseup'\" >")
							.append(
									"&nbsp;<input type='button' name='delBtn"
											+ section.getSectionId()
											+ "' value='Delete Column' onclick='deleteCol(\"sectionTable"
											+ section.getSectionId()
											+ "\")' class=btn3_mouseout onmouseover=\"this.className='btn3_mouseover'\" onmouseout=\"this.className='btn3_mouseout'\" onmousedown=\"this.className='btn3_mousedown'\" onmouseup=\"this.className='btn3_mouseup'\" >")
							.append(
									"&nbsp;<input type='button' name='addColField"
											+ section.getSectionId()
											+ "' value='Add Field' onclick='addColField(\"sectionTable"
											+ section.getSectionId()
											+ "\",\""
											+ section.getSectionId()
											+ "\")' class=btn3_mouseout onmouseover=\"this.className='btn3_mouseover'\" onmouseout=\"this.className='btn3_mouseout'\" onmousedown=\"this.className='btn3_mousedown'\" onmouseup=\"this.className='btn3_mouseup'\" >")
							.append(
									"&nbsp;<input type='button' name='delField"
											+ section.getSectionId()
											+ "' value='Delete Field' onclick='deleteField(\"sectionTable"
											+ section.getSectionId()
											+ "\",\""
											+ section.getSectionId()
											+ "\",\"01\")' class=btn3_mouseout onmouseover=\"this.className='btn3_mouseover'\" onmouseout=\"this.className='btn3_mouseout'\" onmousedown=\"this.className='btn3_mousedown'\" onmouseup=\"this.className='btn3_mouseup'\" >")
							.append(
									"&nbsp;<input type='button' name='delSection"
											+ section.getSectionId()
											+ "' value='Delete Section' onclick='deleteSection(\"sectionTable"
											+ section.getSectionId()
											+ "\",\"div"
											+ section.getSectionId()
											+ "\",\""
											+ section.getSectionType()
											+ "\",\""
											+ section.getSectionId()
											+ "\")' class=btn3_mouseout onmouseover=\"this.className='btn3_mouseover'\" onmouseout=\"this.className='btn3_mouseout'\" onmousedown=\"this.className='btn3_mousedown'\" onmouseup=\"this.className='btn3_mouseup'\" >")
							.append(
									"&nbsp;<input type='button' name='adjustField"
											+ section.getSectionId()
											+ "' value='Adjust Fields Order' onclick='adjustFieldOrder(\""
											+ section.getSectionId()
											+ "\",\""
											+ section.getSectionType()
											+ "\")' class=btn3_mouseout onmouseover=\"this.className='btn3_mouseover'\" onmouseout=\"this.className='btn3_mouseout'\" onmousedown=\"this.className='btn3_mousedown'\" onmouseup=\"this.className='btn3_mouseup'\" >")
							.append(
									"&nbsp;<input type='button' name='adjustColumn"
											+ section.getSectionId()
											+ "' value='Adjust Column Width' onclick='adjustColumnWidth(\""
											+ section.getSectionId()
											+ "\",\""
											+ section.getSectionType()
											+ "\")' class=btn3_mouseout onmouseover=\"this.className='btn3_mouseover'\" onmouseout=\"this.className='btn3_mouseout'\" onmousedown=\"this.className='btn3_mousedown'\" onmouseup=\"this.className='btn3_mouseup'\" >")
							.append("</div>");
				} else if ("02".equals(sectionType) || "03".equals(sectionType)) {
					result
							.append(
									"<div id=\"div"
											+ section.getSectionId()
											+ "\"><b>"
											+ section.getSectionRemark()
											+ ":</b><input type='button' name='addBtn"
											+ section.getSectionId()
											+ "' value='Add Row' onclick='addRow("
											+ "\""
											+ "sectionTable"
											+ section.getSectionId()
											+ "\",\""
											+ section.getSectionId()
											+ "\")' class=btn3_mouseout onmouseover=\"this.className='btn3_mouseover'\" onmouseout=\"this.className='btn3_mouseout'\" onmousedown=\"this.className='btn3_mousedown'\" onmouseup=\"this.className='btn3_mouseup'\" >")
							.append(
									"&nbsp;<input type='button' name='delBtn"
											+ section.getSectionId()
											+ "' value='Delete Row' onclick='deleteRow("
											+ "\""
											+ "sectionTable"
											+ section.getSectionId()
											+ "\""
											+ ")' class=btn3_mouseout onmouseover=\"this.className='btn3_mouseover'\" onmouseout=\"this.className='btn3_mouseout'\" onmousedown=\"this.className='btn3_mousedown'\" onmouseup=\"this.className='btn3_mouseup'\" >")
							.append(
									"&nbsp;<input type='button' name='addField"
											+ section.getSectionId()
											+ "' value='Add Field' onclick='addField("
											+ "\""
											+ "sectionTable"
											+ section.getSectionId()
											+ "\""
											+ ",\""
											+ section.getSectionId()
											+ "\",\""
											+ section.getSectionType()
											+ "\")' class=btn3_mouseout onmouseover=\"this.className='btn3_mouseover'\" onmouseout=\"this.className='btn3_mouseout'\" onmousedown=\"this.className='btn3_mousedown'\" onmouseup=\"this.className='btn3_mouseup'\" >")
							.append(
									"&nbsp;<input type='button' name='delField"
											+ section.getSectionId()
											+ "' value='Delete Field' onclick='deleteField(\"sectionTable"
											+ section.getSectionId()
											+ "\",\""
											+ section.getSectionId()
											+ "\",\"02\")' class=btn3_mouseout onmouseover=\"this.className='btn3_mouseover'\" onmouseout=\"this.className='btn3_mouseout'\" onmousedown=\"this.className='btn3_mousedown'\" onmouseup=\"this.className='btn3_mouseup'\" >")
							.append(
									"&nbsp;<input type='button' name='delSection"
											+ section.getSectionId()
											+ "' value='Delete Section' onclick='deleteSection(\"sectionTable"
											+ section.getSectionId()
											+ "\",\"div"
											+ section.getSectionId()
											+ "\",\""
											+ section.getSectionType()
											+ "\",\""
											+ section.getSectionId()
											+ "\")' class=btn3_mouseout onmouseover=\"this.className='btn3_mouseover'\" onmouseout=\"this.className='btn3_mouseout'\" onmousedown=\"this.className='btn3_mousedown'\" onmouseup=\"this.className='btn3_mouseup'\"> ")
							.append(
									"&nbsp;<input type='button' name='adjustField"
											+ section.getSectionId()
											+ "' value='Adjust Fields Order' onclick='adjustFieldOrder(\""
											+ section.getSectionId()
											+ "\",\""
											+ section.getSectionType()
											+ "\")' class=btn3_mouseout onmouseover=\"this.className='btn3_mouseover'\" onmouseout=\"this.className='btn3_mouseout'\" onmousedown=\"this.className='btn3_mousedown'\" onmouseup=\"this.className='btn3_mouseup'\" >")
							.append("</div>");
				}
				// IT1321
				reportDao.createSectionTable(section);
			} else { // it is update
				reportDao.updateReportSectionInfor(section);
				out.print("success");
			}
			dbManager.commit();
			out.println(result.toString());
		} catch (DAOException e) {
			out.println("fail");
			dbManager.rollback();
			e.printStackTrace();
		} finally {
			if (out != null)
				out.close();
			dbManager.freeConnection();
		}
		return null;
	}

	public ActionLocation enterEditReportField(ModuleMapping mapping,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String returnLabel = "fieldEditPage";
		String type = (String) request.getParameter("type");
		String reportSystemId = (String) request.getParameter("reportSystemId");
		String sectionId = (String) request.getParameter("sectionId");
		if ("edit".equals(type)) {
			String fieldId = (String) request.getParameter("fieldId");
			IDBManager dbManager = null;
			try {
				dbManager = DBManagerFactory.getDBManager();
				ReportManageDAO reportDao = new ReportManageDAO(dbManager);
				// get formField
				ReportSectionFieldVO field = reportDao.getField(Integer
						.parseInt(reportSystemId), sectionId, fieldId);
				request.setAttribute("field", field);
				// if field type is '4' or '6', it needs to get the masterId
				if (field.getFieldType() == 4 || field.getFieldType() == 6) {
					BaseDataDAO baseDataDao = new BaseDataDAO(dbManager);
					BaseDataVO masterVo = baseDataDao.getMasterData(Integer
							.parseInt(reportSystemId), sectionId, fieldId);
					request.setAttribute("masterVo", masterVo);
				}
			} catch (DAOException e) {
				returnLabel = "fail";
				e.printStackTrace();
			} finally {
				dbManager.freeConnection();
			}
		} else {
			// initial field_id
			IDBManager dbManager = null;
			try {
				dbManager = DBManagerFactory.getDBManager();
				ReportManageDAO reportDao = new ReportManageDAO(dbManager);
				int maxFieldNo = reportDao.getMaxFieldNo(Integer
						.parseInt(reportSystemId), sectionId);
				String fieldId = "field_" + sectionId + "_" + maxFieldNo;
				ReportSectionFieldVO field = new ReportSectionFieldVO();
				field.setFieldId(fieldId);
				request.setAttribute("field", field);
			} catch (DAOException e) {
				returnLabel = "fail";
				e.printStackTrace();
			} finally {
				dbManager.freeConnection();
			}
		}
		return mapping.findActionLocation(returnLabel);
	}

	public ActionLocation saveReportField(ModuleMapping mapping,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String result = "success";
		ReportSectionFieldVO field = new ReportSectionFieldVO();
		String reportSystemId = (String) request.getParameter("reportSystemId");
		String sectionId = (String) request.getParameter("sectionId");
		String fieldId = (String) request.getParameter("fieldId");
		String fieldLabel = CommonUtil.decoderURL((String) request
				.getParameter("fieldLabel"));
		String isRequired = (String) request.getParameter("isRequired");
		String length = (String) request.getParameter("length");
		String fieldType = (String) request.getParameter("fieldType");
		String dataType = (String) request.getParameter("dataType");
		String saveType = (String) request.getParameter("saveType");
		String reportStatus = (String) request.getParameter("reportStatus");// 0---published
		// 1---design
		String highLevel = (String) request.getParameter("highLevel"); // -1----No,1-----Yes
		// IT0958 begin
		// String ismoney = (String) request.getParameter("isMoney"); //
		// -1----not
		// money,1-----is
		// money
		String issinglrow = (String) request.getParameter("isSingleRow"); // -1----not
		// single,1----single
		String defaultValue = CommonUtil.decoderURL((String) request.getParameter("defaultValue"));
		// IT0958 end
		String isReadonly = (String) request.getParameter("isReadonly"); // -1
		// 非只读；1
		// 只读。默认为-1

		String isSingleLabel = (String) request.getParameter("isSingleLabel");
		
		String border = (String) request.getParameter("border");
		
		String cssStr = CommonUtil.decoderURL((String) request.getParameter("cssStr"));
		
		String aligned = (String) request.getParameter("aligned");

		if (isReadonly == null || "".equals(isReadonly)) {
			isReadonly = "-1";
		}

		if (highLevel == null || "".equals(highLevel)) {
			highLevel = "-1";
		}

		if (saveType == null) {
			saveType = "new"; // default is 'new'
		}

		field.setReportSystemId(Integer.parseInt(reportSystemId));
		field.setSectionId(sectionId);
		field.setFieldId(fieldId);
		field.setFieldLabel(fieldLabel);
		field.setHighLevel(Integer.parseInt(highLevel));
		// IT0958 begin

		field.setIsSingleRow(Integer.parseInt(issinglrow));
		// IT0958 end
		field.setDefaultValue(defaultValue);

		field.setIsReadonly(Integer.parseInt(isReadonly));

		field.setIsSingleLabel(Integer.parseInt(isSingleLabel));
		//field.setAlignedPosition(Integer.parseInt(alignedPosition));
		field.setBorder(Integer.parseInt(border));
		field.setCssStr(cssStr);
		
		field.setAligned(Integer.parseInt(aligned));

		if (fieldType != null) {
			field.setFieldType(Integer.parseInt(fieldType));
		} else {
			field.setFieldType(1);
		}
		if (dataType != null) {
			field.setDataType(Integer.parseInt(dataType));
		} else {
			field.setFieldType(1);
		}

		if ("true".equals(isRequired)) {
			field.setIsRequired(true);
		} else {
			field.setIsRequired(false);
		}

		
			String controlsWidth = (String) request
					.getParameter("controlsWidth");
			if (controlsWidth != null && !"".equals(controlsWidth)) {
				field.setControlsWidth(Integer.parseInt(controlsWidth));
			} else {
				field.setControlsWidth(0);
			}
		

		
			String controlsHeight = (String) request
					.getParameter("controlsHeight");
			if (controlsHeight != null && !"".equals(controlsHeight)) {
				field.setControlsHeight(Integer.parseInt(controlsHeight));
			} else {
				field.setControlsHeight(0);
			}
		
		
			String fieldComments = CommonUtil.decoderURL((String) request
					.getParameter("fieldComments"));
			if (fieldComments != null && !"".equals(fieldComments)) {
				field.setFieldComments(fieldComments);
			} else {
				field.setFieldComments("");
			}
		
		
			String strcommentContent = CommonUtil.decoderURL((String) request
					.getParameter("commentContent"));
			if (strcommentContent != null && !"".equals(strcommentContent)) {
				field.setCommentContent(strcommentContent);
			} else {
				field.setCommentContent("");
			}
		
		// IT0958 end

		if (length != null) {
			field.setFieldLength(Integer.parseInt(length));
		} else {
			field.setFieldLength(10); // default is 10
		}

		/*
		 * if (field.getFieldType() == 5) { // number if
		 * (request.getParameter("decimalDigits") != null) {
		 * field.setDecimalDigits(Integer.parseInt((String) request
		 * .getParameter("decimalDigits"))); } }
		 */

		if (request.getParameter("orderId") != null) {
			field.setOrderId(Integer.parseInt((String) request
					.getParameter("orderId")));
		}
		PrintWriter out = response.getWriter();
		IDBManager dbManager = null;
		try {
			dbManager = DBManagerFactory.getDBManager();
			dbManager.startTransaction();

			ReportManageDAO reportDao = new ReportManageDAO(dbManager);
			BaseDataDAO baseDataDao = new BaseDataDAO(dbManager);

			if ("new".equals(saveType)) {
				// 如果新增的类型是“checkbox”或“select”，则需要检查
				// save reportField
				reportDao.saveReportSecitonField(field);
				// 如果该report的状态是“published(0)”的，则需要modify section table（增加一列）                
				if ("0".equals(reportStatus) && field.getIsSingleLabel()!=0) {
					reportDao.addSectionTableColumn(field);
				}
			} else {
				// 如果该report的状态是“published(0)”的，则需要modify section
				// table（修改该列的属性（这里暂时只能修改其长度，而且长度只能变长，不能减小））
				if ("0".equals(reportStatus)) {
					// 单文本框，多文本框，数字,单选一，多选多，需要检查
					if ((field.getFieldType() == 1 || field.getFieldType() == 2)) {
						// 获取该字段之前的长度
						ReportSectionFieldVO oldField = reportDao.getField(
								field.getReportSystemId(),
								field.getSectionId(), field.getFieldId());
						if (oldField.getFieldLength() > field.getFieldLength()) {
							out.println("length");
							out.close();
							return null;
						}
					}
					reportDao.alertSectionTableColumn(field);
				}
				reportDao.updateReportSectionField(field);
			}
			
			dbManager.commit();
		} catch (Exception e) {
			dbManager.rollback();
			result = "Save fail";
			out.print(result);
			e.printStackTrace();
		} finally {
			dbManager.freeConnection();
		}
		out.print(result);
		if (out != null)
			out.close();
		return null;
	}

	public ActionLocation publishReport(ModuleMapping mapping,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String returnLabel = "Publish report successfully!";
		String[] reportIds = request.getParameterValues("reportSystemId");
		IDBManager dbManager = null;
		response.setContentType("text/html;charset=GB2312"); // it is very
																// important
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");
		PrintWriter out = response.getWriter();
		try {
			dbManager = DBManagerFactory.getDBManager();
			ReportManageDAO reportDao = new ReportManageDAO(dbManager);
			ReportManageVO report = null;
			for (int i = 0; i < reportIds.length; i++) {
				report = reportDao.getReportBySystemId(Integer.parseInt(reportIds[i]));
				if (reportDao.checkReport(Integer.parseInt(reportIds[i])) == false) {
					out.print("("
							+ report.getReportId()
							+ ")-"
							+ I18NMessageHelper
									.getMessage("common.reportpublishtip"));
					return null;
				}
				// if the report already have been published, it can't be
				// published again

				if ("0".equals(report.getStatus())) {
					out.print("(" + report.getReportId() + ")-"
							+ I18NMessageHelper.getMessage("common.reportpubed"));
					return null;
				}
			}
			//
			dbManager.startTransaction();
			for (int i = 0; i < reportIds.length; i++) {
				reportDao.publishReport(Integer.parseInt(reportIds[i]));
			}
			out.print(returnLabel);
			dbManager.commit();
		} catch (DAOException e) {
			dbManager.rollback();
			e.printStackTrace();
			returnLabel = "Fail to publish report!";
			out.print(returnLabel);
		} finally {
			if (out != null)
				out.close();
			dbManager.freeConnection();
		}
		return null;
	}
	
	public ActionLocation displayReportContent(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String returnLabel = "listReportContent";
        // 获取指定report类型的标识
        String reportSystemId = request.getParameter("reportSystemId");
        if (reportSystemId == null) {
            reportSystemId = (String) request.getAttribute("reportSystemId");
        }
        // 获取该表单实例的标识
        String requestNo = (String) request.getParameter("reportNo");
        String operateType =  "view";
        request.setAttribute("operateType", operateType);
        

        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            ReportManageDAO reportDao = new ReportManageDAO(dbManager);

            HashMap sectionFieldMap = reportDao.getFieldContentByReport(requestNo,reportSystemId);
            request.setAttribute("sectionFieldMap", sectionFieldMap);

            StaffVO staff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);

          
            ReportManageVO report = reportDao.getReportBySystemId(Integer.parseInt(reportSystemId));
            request.setAttribute("listReport", report);
            
            String css = reportDao.getReportStyle(Integer.parseInt(reportSystemId));
			request.setAttribute("css", css);
          
        } catch (DAOException e) {
            e.printStackTrace();
            returnLabel = "fail";
        } finally {
            dbManager.freeConnection();
        }

        return mapping.findActionLocation(returnLabel);
    }
	
	
	public ActionLocation getReportScript(ModuleMapping mapping,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String returnLabel = "getReportScript";
		String reportSystemId = request.getParameter("reportSystemId");
		String script = "";

		IDBManager dbManager = null;
		try {
			
			dbManager = DBManagerFactory.getDBManager();
			ReportManageDAO reportDao = new ReportManageDAO(dbManager);
			script = reportDao.getReportScript(Integer.parseInt(reportSystemId));
			request.setAttribute("script", script);

		} finally {
			if (dbManager != null) {
				dbManager.freeConnection();
			}
		}
		return mapping.findActionLocation(returnLabel);
	}
	
	
	public ActionLocation getReportCSSStyle(ModuleMapping mapping,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String returnLabel = "getReportCSS";
		String reportSystemId = request.getParameter("reportSystemId");
		String css = "";

		IDBManager dbManager = null;
		try {
			dbManager = DBManagerFactory.getDBManager();
			ReportManageDAO reportDao = new ReportManageDAO(dbManager);
			css = reportDao.getReportStyle(Integer.parseInt(reportSystemId));
			request.setAttribute("css", css);

		} finally {
			if (dbManager != null) {
				dbManager.freeConnection();
			}
		}
		return mapping.findActionLocation(returnLabel);
	}
	
	public ActionLocation loadReportCSSJS(ModuleMapping mapping,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String reportSystemId = request.getParameter("reportSystemId");
		String fileType = request.getParameter("fileType");
		fileType = (fileType==null ? "" : fileType);
		String css = "";
		PrintWriter pw = response.getWriter(); 
		IDBManager dbManager = null;
		try {
			dbManager = DBManagerFactory.getDBManager();
			ReportManageDAO reportDao = new ReportManageDAO(dbManager);
			if("css".equals(fileType)) {
				css = reportDao.getReportStyle(Integer.parseInt(reportSystemId));
			} else if("js".equals(fileType)) {
				css = reportDao.getReportScript(Integer.parseInt(reportSystemId));
			}
			request.setAttribute(fileType, css);
			pw.print(css);    
		} finally {
			if (pw != null)
	        	pw.close();
			if (dbManager != null) {
				dbManager.freeConnection();
			}
		}
		return null;
	}
	
	
	public ActionLocation preAdjustSectionOrder(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String reportSystemId = (String) request.getParameter("reportSystemId");
        String returnUrl = "adjustSectionOrderPage";
        // 获取该report的所有section
        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            ReportManageDAO reportDao = new ReportManageDAO(dbManager);
            Collection sectionList = reportDao.getSectionListByReport(Integer.parseInt(reportSystemId));
            request.setAttribute("sectionList", sectionList);
        } catch (DAOException e) {
            e.printStackTrace();
            request.setAttribute(CommonName.COMMON_ERROR_INFOR, e.getMessage());
            returnUrl = "fail";
        } finally {
            if (dbManager != null)
                dbManager.freeConnection();
        }
        return mapping.findActionLocation(returnUrl);
    }
	
	public ActionLocation saveAdjustSectionOrder(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String reportSystemId = (String) request.getParameter("reportSystemId");
        // 获取该report的所有section
        IDBManager dbManager = null;
        PrintWriter out = response.getWriter();
        try {
            dbManager = DBManagerFactory.getDBManager();
            dbManager.startTransaction();
            ReportManageDAO reportDao = new ReportManageDAO(dbManager);
            Collection sectionList = reportDao.getSectionListByReport(Integer.parseInt(reportSystemId));
            if (sectionList != null) {
                Iterator it = sectionList.iterator();
                while (it.hasNext()) {
                    ReportSectionVO section = (ReportSectionVO) it.next();
                    String sectionId = (String) request.getParameter("sectionId_" + section.getSectionId());
                    String orderId = (String) request.getParameter("orderId_" + section.getSectionId());
                    reportDao.updateSectionOrder(Integer.parseInt(reportSystemId), sectionId, orderId);
                }
            }
            dbManager.commit();
            out.print("success");
        } catch (DAOException e) {
            dbManager.rollback();
            e.printStackTrace();
            out.print("fail");
            return null;
        } finally {
            if (dbManager != null)
                dbManager.freeConnection();
            if (out != null)
                out.close();
        }
        return null;
    }
	
	
	public ActionLocation deleteReportSection(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String reportSystemId = (String) request.getParameter("reportSystemId");
        String sectionId = (String) request.getParameter("sectionId");
        IDBManager dbManager = null;
        response.setContentType("text/html;charset=GB2312"); // it is very important
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        PrintWriter out = response.getWriter();
        try {
            dbManager = DBManagerFactory.getDBManager();
            dbManager.startTransaction();
            ReportManageDAO reportDao = new ReportManageDAO(dbManager);
            reportDao.deleteSection(Integer.parseInt(reportSystemId), sectionId);
            dbManager.commit();
            out.print("success");
        } catch (DAOException e) {
            out.println(I18NMessageHelper.getMessage("common.delsecfail"));
            dbManager.rollback();
        } finally {
            if (out != null)
                out.close();
            if (dbManager != null)
                dbManager.freeConnection();
        }
        return null;

    }
	
	public ActionLocation deleteReportField(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String reportSystemId = (String) request.getParameter("reportSystemId");
        String sectionId = (String) request.getParameter("sectionId");
        String fieldId = (String) request.getParameter("fieldId");
        IDBManager dbManager = null;
        response.setContentType("text/html;charset=GB2312"); // it is very important
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        PrintWriter out = response.getWriter();
        try {
            dbManager = DBManagerFactory.getDBManager();
            ReportManageDAO reportDao = new ReportManageDAO(dbManager);
            BaseDataDAO baseDao = new BaseDataDAO(dbManager);
            // 如果该report的状态是“published（0）”，则需要修改其相应的表结构
            String reportStatus = (String) request.getParameter("reportStatus");// 0---published 1---design
            if ("0".equals(reportStatus)) {
                ReportSectionFieldVO field = new ReportSectionFieldVO();
                field.setReportSystemId(Integer.parseInt(reportSystemId));
                field.setSectionId(sectionId);
                field.setFieldId(fieldId);
                reportDao.delSectionTableColumn(field);
            }
            // 删除该字段的定义记录
            reportDao.deleteReportSectionField(Integer.parseInt(reportSystemId), sectionId, fieldId);
            // 删除该字段对应的与baseData的关联（可能不会存在关联，但也要执行）
            baseDao.deleteFieldBaseDataLink(Integer.parseInt(reportSystemId), sectionId, fieldId);
            out.print("success");
        } catch (DAOException e) {
            out.print(I18NMessageHelper.getMessage("common.delfieldfail"));
            e.printStackTrace();
        } finally {
            if (out != null)
                out.close();
            dbManager.freeConnection();
        }
        return null;
    }
	
	
	public ActionLocation editReportScript(ModuleMapping mapping,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String returnLabel = "editReportScript";
		String reportSystemId = request.getParameter("reportSystemId");
		String script = "";

		IDBManager dbManager = null;
		try {
			dbManager = DBManagerFactory.getDBManager();
			ReportManageDAO reportDao = new ReportManageDAO(dbManager);
			script = reportDao.getReportScript(Integer.parseInt(reportSystemId));
			request.setAttribute("script", script);
		} catch (DAOException ex) {
			ex.printStackTrace();
			returnLabel = "fail";
		} finally {
			if (dbManager != null) {
				dbManager.freeConnection();
			}
		}
		return mapping.findActionLocation(returnLabel);
	}
	
	public ActionLocation editReportHtml(ModuleMapping mapping,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String returnLabel = "editReportHtml";
		String reportSystemId = request.getParameter("reportSystemId");
		String sectionId = request.getParameter("sectionId");
		String html = "";

		IDBManager dbManager = null;
		try {
			dbManager = DBManagerFactory.getDBManager();
			ReportManageDAO reportDao = new ReportManageDAO(dbManager);
			html = reportDao.getReportHtmlSecion(Integer.parseInt(reportSystemId), sectionId);

			request.setAttribute("html", html);
		} catch (DAOException ex) {
			ex.printStackTrace();
			returnLabel = "fail";
		} finally {
			if (dbManager != null) {
				dbManager.freeConnection();
			}
		}
		return mapping.findActionLocation(returnLabel);
	}
	
	
	
	public ActionLocation saveReportScript(ModuleMapping mapping,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String returnLabel = "saveReportScript";
		String reportSystemId = request.getParameter("reportSystemId");
		String script = request.getParameter("script");
		script = script == null ? "" : script.trim();
		System.out.println("script: " + script.length());

		IDBManager dbManager = null;
		try {
			dbManager = DBManagerFactory.getDBManager();
			dbManager.startTransaction();
			ReportManageDAO reportDao = new ReportManageDAO(dbManager);
			reportDao.saveReportScript(Integer.parseInt(reportSystemId), script);
			dbManager.commit();
			request.setAttribute("script", script);
			ReportScriptHelper.getInstance().refresh();
		} catch (DAOException ex) {
			dbManager.rollback();
			ex.printStackTrace();
			returnLabel = "fail";
		} finally {
			if (dbManager != null) {
				dbManager.freeConnection();
			}
		}
		return mapping.findActionLocation(returnLabel);
	}

	public ActionLocation saveReportHtml(ModuleMapping mapping,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String returnLabel = "saveReportHtml";
		String reportSystemId = request.getParameter("reportSystemId");
		String sectionId = request.getParameter("sectionId");
		String html = request.getParameter("html");
		html = html == null ? "" : html.trim();
		System.out.println("html: " + html.length());

		IDBManager dbManager = null;
		try {
			dbManager = DBManagerFactory.getDBManager();
			dbManager.startTransaction();
			ReportManageDAO reportDao = new ReportManageDAO(dbManager);
			reportDao.saveReportHtml(Integer.parseInt(reportSystemId), sectionId,html);
			dbManager.commit();
			request.setAttribute("html", html);
			ReportHtmlHelper.getInstance().refresh();
		} catch (DAOException ex) {
			dbManager.rollback();
			ex.printStackTrace();
			returnLabel = "fail";
		} finally {
			if (dbManager != null) {
				dbManager.freeConnection();
			}
		}
		return mapping.findActionLocation(returnLabel);
	}
	
	
	public ActionLocation editReportStyle(ModuleMapping mapping,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String returnLabel = "editReportStyle";
		String reportSystemId = request.getParameter("reportSystemId");
		String css = "";

		IDBManager dbManager = null;
		try {
			dbManager = DBManagerFactory.getDBManager();
			ReportManageDAO reportDao = new ReportManageDAO(dbManager);
			css = reportDao.getReportStyle(Integer.parseInt(reportSystemId));

			request.setAttribute("css", css);
		} catch (DAOException ex) {
			ex.printStackTrace();
			returnLabel = "fail";
		} finally {
			if (dbManager != null) {
				dbManager.freeConnection();
			}
		}
		return mapping.findActionLocation(returnLabel);
	}
	
	public ActionLocation saveReportStyle(ModuleMapping mapping,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String returnLabel = "saveReportStyle";
		String reportSystemId = request.getParameter("reportSystemId");
		String css = request.getParameter("css");
		css = css == null ? "" : css.trim();

		IDBManager dbManager = null;
		try {
			dbManager = DBManagerFactory.getDBManager();
			dbManager.startTransaction();
			ReportManageDAO reportDao = new ReportManageDAO(dbManager);
			reportDao.saveReportStyle(Integer.parseInt(reportSystemId),css);
			dbManager.commit();
			request.setAttribute("css", css);
			ReportHtmlHelper.getInstance().refresh();
		} catch (DAOException ex) {
			dbManager.rollback();
			ex.printStackTrace();
			returnLabel = "fail";
		} finally {
			if (dbManager != null) {
				dbManager.freeConnection();
			}
		}
		return mapping.findActionLocation(returnLabel);
	}
	
	public ActionLocation preAdjustColumnWidth(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String reportSystemId = (String) request.getParameter("reportSystemId");
        String sectionId = (String) request.getParameter("sectionId");
        String sectionType = (String) request.getParameter("sectionType");
        String returnUrl = "adjustColumnWidthPage";
        // 获取该report的所有section
        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            ReportManageDAO reportDao = new ReportManageDAO(dbManager);
            Collection sectionFieldList = reportDao.getSectionFieldListByReport(Integer.parseInt(reportSystemId), sectionId,
                    sectionType);
            request.setAttribute("sectionFieldList", sectionFieldList);
        } catch (DAOException e) {
            e.printStackTrace();
            request.setAttribute(CommonName.COMMON_ERROR_INFOR, e.getMessage());
            returnUrl = "fail";
        } finally {
            if (dbManager != null)
                dbManager.freeConnection();
        }
        return mapping.findActionLocation(returnUrl);
    }
	
	
	public ActionLocation saveAdjustColumnWidth(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String reportSystemId = (String) request.getParameter("reportSystemId");
        String sectionId = (String) request.getParameter("sectionId");
        // String sectionType = (String)request.getParameter("sectionType");
        // 获取该section的所有fields
        IDBManager dbManager = null;
        PrintWriter out = response.getWriter();
        try {
            dbManager = DBManagerFactory.getDBManager();
            dbManager.startTransaction();
            ReportManageDAO reportDao = new ReportManageDAO(dbManager);
            Enumeration paramNames = request.getParameterNames();
            String name = "";
            String[] paramValues;
            int columnWidth = 10; // default is 10%
            while (paramNames.hasMoreElements()) {
                name = (String) paramNames.nextElement();
                if (name != null && name.indexOf("columnWidth||") > -1) { // 连续的两个"|"符号
                    if ((String) request.getParameter(name) != null && !"".equals((String) request.getParameter(name))) {
                        try {
                            columnWidth = Integer.parseInt((String) request.getParameter(name));
                        } catch (Exception e) {
                        }
                    }
                    // System.out.println("orderId="+orderId);
                    paramValues = StringUtil.split(name, "||"); // paramValues[0]='orderId',paramValues[1]=fieldId
                    if (paramValues != null && paramValues.length == 2) {
                        reportDao.updateSectionColumnWidth(Integer.parseInt(reportSystemId), sectionId, paramValues[1],
                                columnWidth);
                    }
                }
            }
            dbManager.commit();
            out.print("success");
        } catch (DAOException e) {
            dbManager.rollback();
            e.printStackTrace();
            out.print("fail");
            return null;
        } finally {
            if (dbManager != null)
                dbManager.freeConnection();
            if (out != null)
                out.close();
        }
        return null;
    }
	
	public ActionLocation exportPDF(ModuleMapping mapping,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// 获取指定report类型的标识
		String reportSystemId = request.getParameter("reportSystemId");
		// 获取该表单实例的标识
		String requestNo = (String) request.getParameter("requestNo");
		IDBManager dbManager = null;
		try {
			dbManager = DBManagerFactory.getDBManager();
			ReportManageDAO reportDao = new ReportManageDAO(dbManager);
			ReportManageVO report = reportDao.getReportBySystemId(Integer
					.parseInt(reportSystemId));
			HashMap sectionFieldMap = reportDao.getFieldContentByReport(requestNo,reportSystemId);


			DocumentException ex = null;
			ByteArrayOutputStream baosPDF = null;
			try {
				baosPDF = ExportFileUtil.exportPDF(report, sectionFieldMap,
						null);

				StringBuffer sbFilename = new StringBuffer();
				// sbFilename.append("filename_");
				// sbFilename.append(System.currentTimeMillis());
				sbFilename.append(requestNo);
				sbFilename.append(".pdf");

				response.setHeader("Cache-Control", "max-age=30");
				response.setContentType("application/pdf");

				StringBuffer sbContentDispValue = new StringBuffer();
				if (!CompanyHelper.getInstance().getEFlowCompany().equals(
						CompanyHelper.EFlow_AIA)) {
					sbContentDispValue.append("inline");
				} else {
					sbContentDispValue.append("attachment");
				}
				sbContentDispValue.append("; filename=");
				sbContentDispValue.append(sbFilename);

				response.setHeader("Content-disposition", sbContentDispValue
						.toString());

				response.setContentLength(baosPDF.size());
				ServletOutputStream sos;
				sos = response.getOutputStream();

				baosPDF.writeTo(sos);
				sos.flush();

			} catch (DocumentException dex) {
				response.setContentType("text/html");
				PrintWriter writer = response.getWriter();
				writer.println(this.getClass().getName()
						+ " caught an exception: " + dex.getClass().getName()
						+ "<br>");
				writer.println("<pre>");
				dex.printStackTrace(writer);
				writer.println("</pre>");
			} finally {
				if (baosPDF != null) {
					baosPDF.reset();
				}
			}

		} catch (DAOException e) {
			e.printStackTrace();
		} finally {
			dbManager.freeConnection();
		}
		return null;
	}

	public ActionLocation triggerGenerateReport(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
    	response.setContentType("application/json;charset=GBK"); //it is very important
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");
		
        String spName = (String) request.getParameter("sourceSql");
        String requestNo = (String) request.getParameter("requestNo");
        String reportSystemId = (String) request.getParameter("reportSystemId");
        String formSystemId = (String) request.getParameter("formSystemId");
        String fieldId = (String) request.getParameter("fieldId");
        String reportNo = StringUtil.generateId("rep_"+reportSystemId);
        
        
        IDBManager dbManager = null;
        IDBManager compassManager = null;
        PrintWriter out = response.getWriter();
        try {
            dbManager = DBManagerFactory.getDBManager();
            List<String> paramList = new ArrayList<String>();
            paramList.add(requestNo);
            paramList.add(reportNo);
            dbManager.prepareCall(spName, paramList);

            ReportManageDAO reportDao = new ReportManageDAO(dbManager);
            reportDao.updateReportInstance(Integer.parseInt(reportSystemId),formSystemId, requestNo, reportNo,fieldId);
            
            out.print("{status:'success',reportNo:'"+reportNo+"'}");
        } catch (Exception e) {
            e.printStackTrace();
            out.print("{status:'fail'}");
        } finally {
            if (dbManager != null)
                dbManager.freeConnection();
            if (out != null)
                out.close();
        }
		return null;
	}
	
}
