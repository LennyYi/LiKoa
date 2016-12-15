package com.aiait.eflow.report.vo;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

import com.aiait.eflow.util.OverDueUtil;
import com.aiait.eflow.wkf.vo.WorkFlowProcessVO;

/**
 * ProcessProgressTable
 * 
 * @author Tinger Li
 * @version 2014-04-30
 */
public class ProcessProgressTable implements Serializable {

    private static final long serialVersionUID = 1L;

    protected List dataList;
    protected int[] measureDays;

    protected List progressList;
    protected ProcessProgressVO totalProgress;

    protected HashMap map;

    protected SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

    public ProcessProgressTable(int[] measureDays) {
        if (measureDays == null) {
            throw new IllegalArgumentException("measureDays can not be null");
        }
        this.measureDays = measureDays;
    }

    public void loadData(List dataList, String progressType) {
        if (dataList == null) {
            throw new IllegalArgumentException("dataList can not be null");
        }
        this.dataList = dataList;

        this.progressList = new ArrayList();
        this.map = new HashMap();
        this.totalProgress = new ProcessProgressVO();
        this.totalProgress.setSubtotals(new int[this.measureDays.length + 1]);
        this.totalProgress.setPercents(new double[this.measureDays.length + 1]);

        // Count for subtotals
        for (int i = 0; i < this.dataList.size(); i++) {
            WorkFlowProcessVO processVo = (WorkFlowProcessVO) this.dataList.get(i);
            String key = this.getKey(processVo.getFormId(), processVo.getOrgId());
            ProcessProgressVO progress = (ProcessProgressVO) this.map.get(key);
            if (progress == null) {
                progress = new ProcessProgressVO();
                progress.setFormId(processVo.getFormId());
                progress.setFormName(processVo.getFormName());
                progress.setOrgId(processVo.getOrgId());
                progress.setOrgName(processVo.getOrgName());
                progress.setSubtotals(new int[this.measureDays.length + 1]);
                progress.setPercents(new double[this.measureDays.length + 1]);
                progress.setDetail(new String[this.measureDays.length + 1]);
                this.map.put(key, progress);
                this.progressList.add(progress);
            }
            double days = 0;
            if ("1".equals(progressType)) {
                String fromDate = dateFormat.format(processVo.getSubmissionDate());
                String toDate = dateFormat.format(processVo.getHandleDate());
                days = OverDueUtil.computeInvertalDays(fromDate, toDate);
            } else if ("2".equals(progressType)) {
                String fromDate = dateFormat.format(processVo.getWaitNodeProcessDate());
                String toDate = dateFormat.format(processVo.getHandleDate());
                days = OverDueUtil.computeInvertalDays(fromDate, toDate);
            } else {
                days = Math.round((processVo.getHandleHours() / 7.5) * 100) / 100.00;
            }
            int j = 0;
            for (; j < this.measureDays.length; j++) {
                double measureDay = this.measureDays[j];
                if (days <= measureDay) {
                    progress.getSubtotals()[j]++;
                    if (progress.getDetail()[j] == null)
                        progress.getDetail()[j] = processVo.getRequestNo() + ",<br>";
                    else
                        progress.getDetail()[j] = progress.getDetail()[j] + processVo.getRequestNo() + ",<br>";
                    break;
                }
            }
            if (j == this.measureDays.length) {
                progress.getSubtotals()[j]++;
                if (progress.getDetail()[j] == null)
                    progress.getDetail()[j] = processVo.getRequestNo() + ",<br>";
                else
                    progress.getDetail()[j] = progress.getDetail()[j] + processVo.getRequestNo() + ",<br>";
            }
            progress.setTotal(progress.getTotal() + 1);
        }

        // Count for percents
        for (int i = 0; i < this.progressList.size(); i++) {
            ProcessProgressVO progress = (ProcessProgressVO) this.progressList.get(i);
            double total = progress.getTotal();
            for (int j = 0; j < progress.getSubtotals().length; j++) {
                double subtotal = progress.getSubtotals()[j];
                progress.getPercents()[j] = Math.round((subtotal / total) * 10000) / 100.0;
                this.totalProgress.getSubtotals()[j] += progress.getSubtotals()[j];
            }
            this.totalProgress.setTotal(this.totalProgress.getTotal() + progress.getTotal());
        }

        // Count for total percent
        double total = this.totalProgress.getTotal();
        for (int i = 0; i < this.totalProgress.getSubtotals().length; i++) {
            double subtotal = this.totalProgress.getSubtotals()[i];
            this.totalProgress.getPercents()[i] = Math.round((subtotal / total) * 10000) / 100.0;
        }
    }

    /**
     * @return the progressList
     */
    public List getProgressList() {
        return progressList;
    }

    /**
     * @return the totalProgress
     */
    public ProcessProgressVO getTotalProgress() {
        return totalProgress;
    }

    /**
     * @return the dataList
     */
    public List getDataList() {
        return dataList;
    }

    /**
     * @return the measureDays
     */
    public int[] getMeasureDays() {
        return measureDays;
    }

    protected String getKey(String formId, String orgId) {
        return (formId + "_" + orgId).toLowerCase();
    }

}
