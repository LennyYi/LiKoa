package com.aiait.eflow._thailand.action;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.*;

import org.apache.poi.hssf.usermodel.HSSFRow;

import com.aiait.eflow._thailand.dao.ExpenseDAO;
import com.aiait.eflow._thailand.vo.ExpenseVO;
import com.aiait.eflow.common.CommonName;
import com.aiait.eflow.common.helper.ParamConfigHelper;
import com.aiait.eflow.housekeeping.vo.StaffVO;
import com.aiait.eflow.util.*;
import com.aiait.framework.db.*;
import com.aiait.framework.mvc.action.*;

public class ExpenseAction extends DispatchAction {

    public ActionLocation listExpense(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String returnLabel = "listExpense";
        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            ExpenseDAO dao = new ExpenseDAO(dbManager);
            List<ExpenseVO> list = dao.getExpenseList();
            request.setAttribute("expenseList", list);
        } catch (Exception ex) {
            ex.printStackTrace();
            request.setAttribute(CommonName.COMMON_ERROR_INFOR, ex.getMessage());
            returnLabel = CommonName.ERROR_PAGE;
        } finally {
            if (dbManager != null) {
                dbManager.freeConnection();
            }
        }
        return mapping.findActionLocation(returnLabel);
    }

    public ActionLocation editExpense(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String returnLabel = "editExpense";
        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            String id = request.getParameter("id");
            if (id != null) {
                String[] _id = id.split("_");
                ExpenseDAO dao = new ExpenseDAO(dbManager);
                ExpenseVO expense = dao.getExpense(_id[0], _id[1]);
                request.setAttribute("expense", expense);
            }
            request.setAttribute("spExpenses", ExpenseVO.getSpExpenses());
        } catch (Exception ex) {
            ex.printStackTrace();
            request.setAttribute(CommonName.COMMON_ERROR_INFOR, ex.getMessage());
            returnLabel = CommonName.ERROR_PAGE;
        } finally {
            if (dbManager != null) {
                dbManager.freeConnection();
            }
        }
        return mapping.findActionLocation(returnLabel);
    }

    public ActionLocation saveExpense(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            ExpenseDAO dao = new ExpenseDAO(dbManager);
            ExpenseVO expense = new ExpenseVO();
            expense.setAcCode(request.getParameter("code"));
            expense.setAcDesc(request.getParameter("desc"));
            expense.setAcSubCode(expense.getAcDescHashCode());
            expense.setType(request.getParameter("type"));
            expense.setRelateHR(request.getParameter("relateHR"));
            expense.setRelateRE(request.getParameter("relateRE"));
            expense.setRelateIT(request.getParameter("relateIT"));
            expense.setSpExpense(request.getParameter("spExpense"));
            expense.setFinance(request.getParameter("finance"));
            expense.setFsi(request.getParameter("fsi"));
            expense.setCapex(request.getParameter("capex"));
            dbManager.startTransaction();
            dao.saveExpense(expense);
            dbManager.commit();
        } catch (Exception ex) {
            if (dbManager != null) {
                dbManager.rollback();
            }
            ex.printStackTrace();
            request.setAttribute(CommonName.COMMON_ERROR_INFOR, ex.getMessage());
            return mapping.findActionLocation(CommonName.ERROR_PAGE);
        } finally {
            if (dbManager != null) {
                dbManager.freeConnection();
            }
        }
        return this.listExpense(mapping, request, response);
    }

    public ActionLocation deleteExpense(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            ExpenseDAO dao = new ExpenseDAO(dbManager);
            String[] ids = request.getParameterValues("id");
            if (ids != null) {
                dbManager.startTransaction();
                for (String id : ids) {
                    String[] _id = id.split("_");
                    dao.deleteExpense(_id[0], _id[1]);
                }
                dbManager.commit();
            }
        } catch (Exception ex) {
            if (dbManager != null) {
                dbManager.rollback();
            }
            ex.printStackTrace();
            request.setAttribute(CommonName.COMMON_ERROR_INFOR, ex.getMessage());
            return mapping.findActionLocation(CommonName.ERROR_PAGE);
        } finally {
            if (dbManager != null) {
                dbManager.freeConnection();
            }
        }
        return this.listExpense(mapping, request, response);
    }

    public ActionLocation selectUploadFile(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String returnLabel = "selectUploadFile";
        request.setAttribute("action", "_thailand/expenseAction.it?method=importExpense");
        request.setAttribute("title", "Upload Expense");
        return mapping.findActionLocation(returnLabel);
    }

    public ActionLocation importExpense(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String returnLabel = "selectUploadFile";
        StaffVO currentStaff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
        String userId = currentStaff.getLogonId();
        String fileName = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateName = dateFormat.format(new Date());
        HttpServletRequest multiRequest = null;
        try {
            multiRequest = new MultipartRequest(request, userId);
            File file = ((MultipartRequest) multiRequest).getFile("path");
            if (file == null) {
                throw new Exception("File path error");
            }
            fileName = dateName + "_" + ((MultipartRequest) multiRequest).getFileName(file);
            fileName = fileName.replaceAll("'", "");
            String uploadPath = ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_UPLOAD_DIR);
            if (uploadPath == null) {
                throw new Exception("The upload path not set, please contact the administrator.");
            }
            fileName = uploadPath + "/" + fileName;
            FileUtil.saveAs(file, fileName);
            ExcelUtil excelUtil = new ExcelUtil(fileName);
            this.saveExcelData(this.parseExcel(excelUtil));

            request.setAttribute(CommonName.COMMON_STATUS, "OK");
            request.setAttribute(CommonName.COMMON_MESSAGE, "[OK]");
        } catch (Exception ex) {
            ex.printStackTrace();
            request.setAttribute(CommonName.COMMON_STATUS, "ERROR");
            request.setAttribute(CommonName.COMMON_MESSAGE, ex.getMessage());
        } finally {
            if (multiRequest != null && multiRequest instanceof MultipartRequest) {
                // Delete the temp file
                ((MultipartRequest) multiRequest).deleteTemporaryFile();
            }
            if (fileName != null && !"".equals(fileName)) {
                FileUtil.deleteFile(fileName);
            }
        }
        return mapping.findActionLocation(returnLabel);
    }

    protected void saveExcelData(Collection<ExpenseVO> dataList) throws Exception {
        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            ExpenseDAO dao = new ExpenseDAO(dbManager);
            dbManager.startTransaction();
            for (ExpenseVO expense : dataList) {
                dao.saveExpense(expense);
            }
            dbManager.commit();
        } catch (Exception ex) {
            if (dbManager != null) {
                dbManager.rollback();
            }
            ex.printStackTrace();
            throw new Exception("Failed to save excel data: " + ex.getMessage());
        } finally {
            if (dbManager != null) {
                dbManager.freeConnection();
            }
        }
    }

    protected Collection<ExpenseVO> parseExcel(ExcelUtil excelUtil) throws Exception {
        int startLine = 2;
        int colNum = 10;
        int sheetIndex = 1;

        Collection<ExpenseVO> list = new ArrayList<ExpenseVO>();
        excelUtil.setSheet(sheetIndex);
        int rows = excelUtil.getSheet().getPhysicalNumberOfRows();
        if (rows >= startLine) {
            // Check format
            HSSFRow rowline = excelUtil.getSheet().getRow(1);
            if (rowline.getLastCellNum() < colNum) {
                throw new Exception("Excel file format error, Expense sheet should have " + colNum + " data columns.");
            }

            // Process
            for (int r = startLine; r <= rows; r++) {
                ExpenseVO expense = new ExpenseVO();
                int i = 1;

                // Type
                String type = excelUtil.getCellValue(sheetIndex, r, i++);
                type = type == null ? "" : type.trim();
                if (type.equalsIgnoreCase(ExpenseVO.TYPE_GOE)) {
                    expense.setType(ExpenseVO.TYPE_GOE);
                } else if (type.equalsIgnoreCase(ExpenseVO.TYPE_VEA)) {
                    expense.setType(ExpenseVO.TYPE_VEA);
                } else {
                    continue;
                }

                // A/C Code
                String code = excelUtil.getCellValue(sheetIndex, r, i++);
                code = code == null ? "" : code.trim();
                if ("".equals(code)) {
                    throw new Exception("Row/Column: " + r + "/" + --i + ": A/C Code cannot be null!");
                }
                expense.setAcCode(code);

                // A/C Description
                String desc = excelUtil.getCellValue(sheetIndex, r, i++);
                desc = desc == null ? "" : desc.trim();
                if ("".equals(desc)) {
                    throw new Exception("Row/Column: " + r + "/" + --i + ": A/C Description cannot be null!");
                }
                expense.setAcDesc(desc);
                expense.setAcSubCode(expense.getAcDescHashCode());

                // Relate HR
                String relateHR = excelUtil.getCellValue(sheetIndex, r, i++);
                relateHR = relateHR == null ? "" : relateHR.trim();
                if (relateHR.equalsIgnoreCase(ExpenseVO.YES)) {
                    expense.setRelateHR(ExpenseVO.YES);
                } else {
                    expense.setRelateHR(ExpenseVO.NO);
                }

                // Relate RE
                String relateRE = excelUtil.getCellValue(sheetIndex, r, i++);
                relateRE = relateRE == null ? "" : relateRE.trim();
                if (relateRE.equalsIgnoreCase(ExpenseVO.YES)) {
                    expense.setRelateRE(ExpenseVO.YES);
                } else {
                    expense.setRelateRE(ExpenseVO.NO);
                }

                // Relate IT
                String relateIT = excelUtil.getCellValue(sheetIndex, r, i++);
                relateIT = relateIT == null ? "" : relateIT.trim();
                if (relateIT.equalsIgnoreCase(ExpenseVO.YES)) {
                    expense.setRelateIT(ExpenseVO.YES);
                } else {
                    expense.setRelateIT(ExpenseVO.NO);
                }

                // Specific Expense
                String spExpense = excelUtil.getCellValue(sheetIndex, r, i++);
                spExpense = spExpense == null ? "" : spExpense.trim();
                for (String _spExpense : ExpenseVO.getSpExpenses()) {
                    if (spExpense.equalsIgnoreCase(_spExpense)) {
                        expense.setSpExpense(_spExpense);
                        break;
                    }
                }
                if (expense.getSpExpense() == null) {
                    expense.setSpExpense("");
                }

                // Finance
                String finance = excelUtil.getCellValue(sheetIndex, r, i++);
                finance = finance == null ? "" : finance.trim();
                if (finance.equalsIgnoreCase(ExpenseVO.YES)) {
                    expense.setFinance(ExpenseVO.YES);
                } else {
                    expense.setFinance(ExpenseVO.NO);
                }

                // FSI
                String fsi = excelUtil.getCellValue(sheetIndex, r, i++);
                fsi = fsi == null ? "" : fsi.trim();
                if (fsi.equalsIgnoreCase(ExpenseVO.YES)) {
                    expense.setFsi(ExpenseVO.YES);
                } else {
                    expense.setFsi(ExpenseVO.NO);
                }

                // CAPEX
                String capex = excelUtil.getCellValue(sheetIndex, r, i++);
                capex = capex == null ? "" : capex.trim();
                if (capex.equalsIgnoreCase(ExpenseVO.YES)) {
                    expense.setCapex(ExpenseVO.YES);
                } else {
                    expense.setCapex(ExpenseVO.NO);
                }

                list.add(expense);
            }
        }

        return list;
    }

}
