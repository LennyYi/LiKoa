package com.aiait.eflow.util;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Iterator;

import com.aiait.eflow.common.helper.StaffTeamHelper;
import com.aiait.eflow.formmanage.vo.FormSectionFieldVO;
import com.aiait.eflow.housekeeping.vo.StaffVO;
import com.aiait.eflow.housekeeping.vo.TeamVO;
import com.aiait.eflow.reportmanage.vo.ReportSectionFieldVO;

public class HtmlUtil {
	
	public static String decoderURL(String value)throws UnsupportedEncodingException{
		if(value!=null){
			value = java.net.URLDecoder.decode(value,"UTF-8"); //
			value = java.net.URLDecoder.decode(value,"UTF-8");
		}
		return value;
	}
	
	/**
	 * 生成指定月份的年月下拉选择框
	 * @param fieldId 生成的选择框的控件ID
	 * @param months
	 * @return
	 */
	public static String createYearMonthSelect(String fieldName,int months,boolean blank){
		StringBuffer str = new StringBuffer("");
		str.append("<select name='").append(fieldName).append("' onmousewheel='event.returnValue=false'>");
		
		if(blank){
			str.append("<option value=''></option>");
		}
		Calendar calendar = new GregorianCalendar(); 
		//current year
		int currentYear = calendar.get(Calendar.YEAR);
		int currentMonth = calendar.get(Calendar.MONTH) + 1;
		int pastMonth = 0;
		int pastYear = 0 ;
		//生成当前月之前的months个月
		for(int i=0;i<months;i++){
			pastMonth = currentMonth - (months-i);
			if(pastMonth<0){
				pastYear = currentYear - 1;
			}
			if(pastMonth>0){
				if(pastMonth>=10){
					str.append("<option value='").append(currentYear+"/"+pastMonth)
					.append("'>").append(currentYear+"/"+pastMonth).append("</option>");
				}else{
					str.append("<option value='").append(currentYear+"/"+"0"+pastMonth)
					.append("'>").append(currentYear+"/"+"0"+pastMonth).append("</option>");
				}
			}else{
				pastMonth = 12 + pastMonth;
				if(pastMonth>=10){
	                 str.append("<option value='").append(pastYear+"/"+pastMonth)
						.append("'>").append(pastYear+"/"+pastMonth).append("</option>");
				}else{
	                 str.append("<option value='").append(pastYear+"/0"+pastMonth)
						.append("'>").append(pastYear+"/0"+pastMonth).append("</option>");
				}
			}
			//currentMonth = pastMonth;
		}
		currentMonth = calendar.get(Calendar.MONTH) + 1;
		currentYear = calendar.get(Calendar.YEAR);
		if(currentMonth>9){
		   str.append("<option value='").append(currentYear+"/"+currentMonth).append("' selected>").append(currentYear+"/"+currentMonth).append("</option>");
		}else{
			 str.append("<option value='").append(currentYear+"/0"+currentMonth).append("' selected>").append(currentYear+"/0"+currentMonth).append("</option>");
		}
		//生成当前月之后的months个月
		for(int i=0;i<months;i++){
			pastMonth = currentMonth + 1;
			if(pastMonth>0){
				if(pastMonth>=10){
					str.append("<option value='").append(currentYear+"/"+pastMonth)
					.append("'>").append(currentYear+"/"+pastMonth).append("</option>");
				}else{
					str.append("<option value='").append(currentYear+"/"+"0"+pastMonth)
					.append("'>").append(currentYear+"/"+"0"+pastMonth).append("</option>");
				}
			}else{
				currentYear = currentYear + 1;
				pastMonth = 12;
                 str.append("<option value='").append(currentYear+"/"+pastMonth)
					.append("'>").append(currentYear+"/"+pastMonth).append("</option>");
			}
			currentMonth = pastMonth;
		}
		str.append("</select>");
		return str.toString();
	}
	
	public static String createRequestTeamSelect(String teamCode,
			FormSectionFieldVO field) {
		return createRequestTeamSelect(teamCode,field,null);
	}
	
	public static String createRequestTeamSelect(String teamCode,
			ReportSectionFieldVO field) {
		return createRequestTeamSelect(teamCode,field,null);
	}
	
	
	public static String createRequestTeamSelect(String teamCode,
			ReportSectionFieldVO field, StaffVO staff) {
		StringBuffer selectControl = new StringBuffer("");

		Collection teamList = staff.getTeamList(); //.getInstance().getTeamList();
		selectControl.append("<select name='" + field.getFieldId()
				+ "' onchange=\"javascript:getOptionList(this.value)\" onmousewheel='event.returnValue=false'>");
		if (teamList != null) {
			Iterator teamIt = teamList.iterator();
			while (teamIt.hasNext()) {
				TeamVO team = (TeamVO) teamIt.next();
				if (teamCode.equals(team.getTeamCode())) {
					selectControl.append("<option value='" + team.getTeamCode()
							+ "' selected>" + team.getTeamName() + "</option>");
				} else {
					selectControl.append("<option value='" + team.getTeamCode()
							+ "'>" + team.getTeamName() + "</option>");
				}
			}
		}
		selectControl.append("</select>");

		return selectControl.toString();
	}
	
	
	
	/**
	 * 生成requester所属Team的下拉选择框的html
	 * 
	 * @param teamCode
	 * @param field
	 * @return
	 */
	public static String createRequestTeamSelect(String teamCode,
			FormSectionFieldVO field, StaffVO staff) {
		StringBuffer selectControl = new StringBuffer("");

		Collection teamList = staff.getTeamList(); //.getInstance().getTeamList();
		selectControl.append("<select name='" + field.getFieldId()
				+ "' onchange=\"javascript:getOptionList(this.value)\" onmousewheel='event.returnValue=false'>");
		if (teamList != null) {
			Iterator teamIt = teamList.iterator();
			while (teamIt.hasNext()) {
				TeamVO team = (TeamVO) teamIt.next();
				if (teamCode.equals(team.getTeamCode())) {
					selectControl.append("<option value='" + team.getTeamCode()
							+ "' selected>" + team.getTeamName() + "</option>");
				} else {
					selectControl.append("<option value='" + team.getTeamCode()
							+ "'>" + team.getTeamName() + "</option>");
				}
			}
		}
		selectControl.append("</select>");

		return selectControl.toString();
	}

	/**
	 * 生成requester的下拉选择框的html
	 * 
	 * @param staff
	 * @param field
	 * @return
	 */
	public static String createRequestStaffSelect(String staffCode, String teamCode, FormSectionFieldVO field) {
        StringBuffer selectControl = new StringBuffer("");

        Collection staffList = StaffTeamHelper.getInstance().getStaffListByTeam(teamCode);
        selectControl.append("<select name='" + field.getFieldId()
                + "' onchange=\"javascript:changeRequester(this.value)\" onmousewheel='event.returnValue=false'>");
        if (staffList != null) {
            Iterator staffIt = staffList.iterator();
            while (staffIt.hasNext()) {
                StaffVO vo = (StaffVO) staffIt.next();
                if (staffCode.equals(vo.getStaffCode())) {
                    selectControl.append("<option value='" + vo.getStaffCode() + "' selected>" + vo.getStaffName()
                            + "(" + vo.getLogonId() + ")" + "</option>");
                } else {
                    selectControl.append("<option value='" + vo.getStaffCode() + "'>" + vo.getStaffName() + "("
                            + vo.getLogonId() + ")" + "</option>");
                }
            }
        }
        selectControl.append("</select>");

        return selectControl.toString();
    }
	
	
	public static String createRequestStaffSelect(String staffCode, String teamCode, ReportSectionFieldVO field) {
        StringBuffer selectControl = new StringBuffer("");

        Collection staffList = StaffTeamHelper.getInstance().getStaffListByTeam(teamCode);
        selectControl.append("<select name='" + field.getFieldId()
                + "' onchange=\"javascript:changeRequester(this.value)\" onmousewheel='event.returnValue=false'>");
        if (staffList != null) {
            Iterator staffIt = staffList.iterator();
            while (staffIt.hasNext()) {
                StaffVO vo = (StaffVO) staffIt.next();
                if (staffCode.equals(vo.getStaffCode())) {
                    selectControl.append("<option value='" + vo.getStaffCode() + "' selected>" + vo.getStaffName()
                            + "(" + vo.getLogonId() + ")" + "</option>");
                } else {
                    selectControl.append("<option value='" + vo.getStaffCode() + "'>" + vo.getStaffName() + "("
                            + vo.getLogonId() + ")" + "</option>");
                }
            }
        }
        selectControl.append("</select>");

        return selectControl.toString();
    }

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Calendar calendar = new GregorianCalendar(); 
       
        
		System.out.print(calendar.get(Calendar.DATE));
	}

}
