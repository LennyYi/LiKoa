<%@page import="java.util.*,com.aiait.eflow.housekeeping.vo.*,com.aiait.eflow.common.helper.*,com.aiait.eflow.common.*"%>
<%@ taglib uri="/WEB-INF/purview.tld" prefix="purview"%>
<%@ page contentType="text/html;charset=GBK" pageEncoding="GBK"%>
<%@ include file="/common/loading.jsp"%>
<%@ include file="/housekeeping/leaveBalanceSettingJS.jsp"%>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n"%>
<html>
<head>
<title>Leave Balance</title>
<i18n:jsmessage jsRelativePath="js" prefixName="message" />
<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
<script src="<%=request.getContextPath()%>/js/webtools.js" language="JavaScript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/calendar.js"></script>
<SCRIPT language="javascript" src="<%=request.getContextPath()%>/js/sorttable.js"></SCRIPT>
<script language="javascript" src="<%=request.getContextPath()%>/js/resizeCol.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/ajax.js"></script>
</head>

<%
	//
    StaffVO currentStaff = (StaffVO)request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);

	LeaveBalanceCommonInforVO leaveBalanceCommonInforVO = (LeaveBalanceCommonInforVO) request
			.getAttribute("leaveBalanceCommonInfor");

	String staffType ="1"; 
	String onBoardDate = "";
	double preWorkingExperience=0.00;
	double grade=0.00;
	String old_grade = "";
	String effectiveDate = "";
	String medicalException="";
	String updatedLeave = "";
	String updatedMedical = "";
	
	if (leaveBalanceCommonInforVO != null){
		staffType = leaveBalanceCommonInforVO.getStaffType();
		onBoardDate = leaveBalanceCommonInforVO.getOnBoardDate();
		preWorkingExperience = leaveBalanceCommonInforVO.getPreWorkExperience();
		grade = leaveBalanceCommonInforVO.getNewGrade();
		old_grade = Double.toString(leaveBalanceCommonInforVO.getOldGrade());  
		old_grade = old_grade =="-1" ? "" : old_grade;
		effectiveDate = leaveBalanceCommonInforVO.getGradeEffectDate();		
		medicalException = leaveBalanceCommonInforVO.getMedicalException();
		updatedLeave = leaveBalanceCommonInforVO.getUpdatedLeave();
		updatedMedical = leaveBalanceCommonInforVO.getUpdatedMedical();
	}

	
	String updateEntitlement = (String)request.getParameter("updateEntitlement");
	String updateMedicalEntitlement = (String)request.getParameter("updateMedicalEntitlement");
	
	Collection list = (Collection) request
			.getAttribute("leaveBalanceList");
	
	Collection gradeHistoryList = (Collection) request
	.getAttribute("gradeHistoryList");
	
	String staffCode = request.getParameter("staffCode");
	String staffName = StaffTeamHelper.getInstance()
			.getStaffNameByCode(staffCode);

	String year = request.getParameter("year");
	year = year == null ? "" : year;

	String entitle = request.getParameter("entitle");
	entitle = entitle == null ? "" : entitle;

	String carryForward = request.getParameter("carry_forward");
	carryForward = carryForward == null ? "" : carryForward;

	String applied = request.getParameter("applied");
	applied = applied == null ? "" : applied;

	String balance = request.getParameter("balance");
	balance = balance == null ? "" : balance;

	String statutoryEntitle = request.getParameter("statutory_entitle");
	statutoryEntitle = statutoryEntitle == null ? "" : statutoryEntitle;

	String companyEntitle = request.getParameter("company_entitle");
	companyEntitle = companyEntitle == null ? "" : companyEntitle;

	String forfeit = request.getParameter("forfeit");
	forfeit = forfeit == null ? "" : forfeit;

	String sickTotalEntitle = request.getParameter("sick_total_entitle");
	sickTotalEntitle = sickTotalEntitle == null ? "" : sickTotalEntitle;

	String sickApplied = request.getParameter("sick_applied");
	sickApplied = sickApplied == null ? "" : sickApplied;

	String sickBalance = request.getParameter("sick_balance");
	sickBalance = sickBalance == null ? "" : sickBalance;
		
	//for Medical Claim
	Collection medicalBalanceList = (Collection) request.getAttribute("medicalBalanceList");
	
	String yearMed = request.getParameter("yearMed");
    String staffCEntitlement = request.getParameter("staffCEntitlement");  
    String staffCApplied = request.getParameter("staffCApplied");
    String staffCBalance = request.getParameter("staffCBalance");
    String staffHEntitlement = request.getParameter("staffHEntitlement");
    String staffHApplied = request.getParameter("staffHApplied");
    String staffHBalance = request.getParameter("staffHBalance");
    String connubialName = request.getParameter("connubialName");
    String connubialCEntitlement = request.getParameter("connubialCEntitlement");
    String connubialCApplied = request.getParameter("connubialCApplied");
    String connubialCBalance = request.getParameter("connubialCBalance");
    String connubialHEntitlement = request.getParameter("connubialHEntitlement");
    String connubialHApplied = request.getParameter("connubialHApplied");
    String connubialHBalance = request.getParameter("connubialHBalance");
    String childName = request.getParameter("childName");
    String childCEntitlement = request.getParameter("childCEntitlement");
    String childCApplied = request.getParameter("childCApplied"); 
    String childCBalance = request.getParameter("childCBalance");
    String childHEntitlement = request.getParameter("childHEntitlement");
    String childHApplied = request.getParameter("childHApplied");
    String childHBalance = request.getParameter("childHBalance");
    
    yearMed = yearMed == null ? "" : yearMed;
    staffCEntitlement = staffCEntitlement == null ? "" : staffCEntitlement;
    staffCApplied = staffCApplied == null ? "" : staffCApplied;
    staffCBalance = staffCBalance == null ? "" : staffCBalance;
    staffHEntitlement = staffHEntitlement == null ? "" : staffHEntitlement;
    staffHApplied = staffHApplied == null ? "" : staffHApplied;
    staffHBalance = staffHBalance == null ? "" : staffHBalance;
    connubialName = connubialName == null ? "" : connubialName;
    connubialCEntitlement = connubialCEntitlement == null ? "" : connubialCEntitlement;
    connubialCApplied = connubialCApplied == null ? "" : connubialCApplied;
    connubialCBalance = connubialCBalance == null ? "" : connubialCBalance;
    connubialHEntitlement = connubialHEntitlement == null ? "" : connubialHEntitlement;
    connubialHApplied = connubialHApplied == null ? "" : connubialHApplied;
    connubialHBalance = connubialHBalance == null ? "" : connubialHBalance;
    childName = childName == null ? "" : childName;
    childCEntitlement = childCEntitlement == null ? "" : childCEntitlement;
    childCApplied = childCApplied == null ? "" : childCApplied;
    childCBalance = childCBalance == null ? "" : childCBalance;
    childHEntitlement = childHEntitlement == null ? "" : childHEntitlement;
    childHApplied = childHApplied == null ? "" : childHApplied;
    childHBalance = childHBalance == null ? "" : childHBalance;
%>
<body>
<form name="myForm" method="post">
<table width="100%" border="0">

     <!-- For Common Information Maintenance-->
     <tr>
		<td colspan='2'><image border='0' framespacing='0' frameborder='0' id="imgcon1" src="images/Rminus.png" style="float:left;" onClick="disp2(1,true);disp2(2,false)"/>&nbsp;&nbsp;</td>
	</tr>      
    <tr class="tr1">
        <td align="center" colspan="6"><b><i18n:message key="housekeeping_leavebalance.common_information" /></b></td>
    </tr>
    <tr>
        <td colspan="6">
        <div id='div1' style="display:block;position:relative;left:1px;">
            <table width="100%" bordercolor="#6595D6" border="1" cellspacing="0" cellpadding="3"
            style="border-collapse: collapse;">
            <tr>
                <td width="16%" height="20" class="tr3">
                <div align="right"><i18n:message key="housekeeping_user.staffcode" /></div>
                </td>
                <td width="16%" height="20"><input type="hidden" value="<%=staffCode%>" name="staffCode"><%=staffCode%>&nbsp;</td>
                
                <td width="17%" height="20" class="tr3">
                <div align="right"><i18n:message key="housekeeping_user.staffname" /></div>
                </td>
                <td width="17%" height="20"><%=staffName%>&nbsp;</td>
                
                
                <td width="17%" height="20" class="tr3">
                <div align="right"><i18n:message key="housekeeping_leavebalance.staff_type" /></div>
                </td>
                <td width="17%" height="20">
                    <select name="staff_type" > 
                        <option value="1" <%=(CommonName.LEAVE_BALANCE_STAFF_PERMANENT.equals(staffType)?"selected":"")%>>Permanent</option>
                        <option value="2" <%=(CommonName.LEAVE_BALANCE_STAFF_INTERN.equals(staffType)?"selected":"")%>>Intern</option>
                        <option value="3" <%=(CommonName.LEAVE_BALANCE_STAFF_EC.equals(staffType)?"selected":"")%>>EC</option>
                    </select>
                </td>     
            </tr> 
            
            <tr>
                <td width="16%" height="20" class="tr3">
                <div align="right"><i18n:message key="housekeeping_leavebalance.onboard_date" />(mm/dd/yyyy)</div>
                </td>
                <td width="16%" height="20"><input name="onboard_date" onclick='setday(this)'  Type="text" value="<%=onBoardDate%>" class="text2" id="onboard_date">
                </td>
                
                <td width="17%" height="20" class="tr3">
                <div align="right"><i18n:message key="housekeeping_leavebalance.pre_working_experience" /></div>
                </td>
                <td width="17%" height="20"><input type="text" value="<%=preWorkingExperience%>" id="pre_working_experience" name="pre_working_experience"
                    onKeyPress="checkKey()" onKeyUp="validateDouble(this)"></td>
                
                <td width="17%" height="20" class="tr3">
                <div align="right"><i18n:message key="housekeeping_leavebalance.grade"/></div>
                </td>
                 <td width="17%" height="20"><input type="text" value="<%=grade%>" id="grade" name="grade"
                    onKeyPress="checkKey()" onKeyUp="validateDouble(this)"></td>          
            </tr>
            
            <tr>
                <td width="16%" height="20" class="tr3">
                <div align="right"><i18n:message key="housekeeping_leavebalance.old_grade"/></div>
                </td>
                 <td width="16%" height="20"><input type="text" value="<%=old_grade%>" id="old_grade" name="old_grade"
                    onKeyPress="checkKey()" onKeyUp="validateDouble(this)"></td>
                    
                <td width="17%" height="20" class="tr3">
                <div align="right"><i18n:message key="housekeeping_leavebalance.effective_date"/>(mm/dd/yyyy)</div>
                </td>
                <td width="17%" height="20"><input name="effective_date" onclick='setday(this)'  Type="text" value="<%=effectiveDate%>" class="text2" id="effective_date">
                </td>
                
                <td width="17%" height="20" class="tr3">
                <div align="right"><i18n:message key="housekeeping_leavebalance.medicalException" /></div>
                </td>
                <td width="17%" height="20">
                    <select name="medicalException" > 
                        <option value=""></option>
                        <option value="Y" <%=("Y".equals(medicalException)?"selected":"")%>>Yes</option>
                        <option value="N" <%=("N".equals(medicalException)?"selected":"")%>>No</option>
                    </select>
                </td>         
            </tr>
            
            <tr>                              
                <td width="16%" height="20" class="tr3">
                <div align="right"><i18n:message key="housekeeping_leavebalance.updatedLeave" /></div>
                </td>
                <td width="16%" height="20">
                    <select name="updatedLeave" > 
                        <option value=""></option>
                        <option value="Y" <%=("Y".equals(updatedLeave)?"selected":"")%>>Yes</option>
                        <option value="N" <%=("N".equals(updatedLeave)?"selected":"")%>>No</option>
                    </select>
                </td>   
                
                <td width="17%" height="20" class="tr3">
                <div align="right"><i18n:message key="housekeeping_leavebalance.update" /></div>
                </td>
                <td width="17%" height="20"><input type="checkbox" onclick="manualUpdateStaffEntitlement(this)" name="updateEntitlement" <%="true".equals(updateEntitlement)?"checked":""%>></td> 
                         
                <td width="17%" height="20" class="tr3"></td>
                <td width="17%" height="20"></td>                             
            </tr>
            
            <tr>                
                <td width="16%" height="20" class="tr3">
                <div align="right"><i18n:message key="housekeeping_leavebalance.updatedMedical" /></div>
                </td>
                <td width="16%" height="20">
                    <select name="updatedMedical" > 
                        <option value=""></option>
                        <option value="Y" <%=("Y".equals(updatedMedical)?"selected":"")%>>Yes</option>
                        <option value="N" <%=("N".equals(updatedMedical)?"selected":"")%>>No</option>
                    </select>
                </td>   
                
                <td width="17%" height="20" class="tr3">
                <div align="right"><i18n:message key="housekeeping_leavebalance.updateMedical" /></div>
                </td>
                <td width="17%" height="20"><input type="checkbox" onclick="manualUpdateStaffMedicalEntitlement(this)" name="updateMedicalEntitlement" <%="true".equals(updateMedicalEntitlement)?"checked":""%>></td>    
                
                <td width="17%" height="20" class="tr3"></td>
                <td width="17%" height="20"></td>                             
            </tr>            
            
            <tr> 
            	 <td width="100%" align="center" colspan="6">
		            <input type="button" name="saveBtn"
		            value='<i18n:message key="button.save"/>' onclick="saveCommonInformation('<%=currentStaff.getStaffCode()%>')" class="btn3_mouseout"
		            onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
		            onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">&nbsp;&nbsp;
		            
		            <input type="button" name="deleteBtn" 
		            value='<i18n:message key="button.delete"/>' onclick="deleteGradeItem()"
		            class="btn3_mouseout" onmouseover="this.className='btn3_mouseover'"
		            onmouseout="this.className='btn3_mouseout'" onmousedown="this.className='btn3_mousedown'"
		            onmouseup="this.className='btn3_mouseup'">&nbsp;&nbsp;
		                    
		            <input type="button" name="closeBtn"
		            value='<i18n:message key="button.close"/>' onclick="closeForm()" class="btn3_mouseout"
		            onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
		            onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
		            
            	 </td>
            </tr>         
            </table>
            </div>
        </td>
    </tr>
    
    <tr>
        <td colspan="6">
        <div id='div2' style="display:block;position:relative;left:1px;">
        <table width="100%" border="0" cellpadding="0" cellspacing="1" class="sortable" id="gradeTable"
            style="border: 1px #8899cc solid;">
            <tr class="liebiao_tou">
                <td align='center'><input type="checkbox" name="allBtn" onclick="selectAll(this,'gradeItemId')"></td>
                <td align='center'><i18n:message key="housekeeping_leavebalance.grade" /></td>
                <td align='center'><i18n:message key="housekeeping_leavebalance.old_grade" /></td>               
                <td align='center'><i18n:message key="housekeeping_leavebalance.effective_date" /></td>
                <td align='center'><i18n:message key="housekeeping_leavebalance.updatedLeave" /></td>
                <td align='center'><i18n:message key="housekeeping_leavebalance.updatedMedical" /></td>
            </tr>
            <%
            	//
            	if (gradeHistoryList != null) {
            		int i = 1;
            		Iterator it = gradeHistoryList.iterator();
            		while (it.hasNext()) {
            			LeaveBalanceCommonInforVO vo = (LeaveBalanceCommonInforVO) it.next();
            %>
            <tr class="tr_change">
                <td align='center'><input type="checkbox" name="gradeItemId" value="<%=vo.getGradeEffectDate()%>"></td>
                <td align='center'><%=vo.getNewGrade()%>&nbsp;</td>
                <td align='center'><%=vo.getOldGrade()%>&nbsp;</td>             
                <td align='center'><%=vo.getGradeEffectDate()%>&nbsp;</td>
                <td align='center'><%=vo.getUpdatedLeave()%>&nbsp;</td>
                <td align='center'><%=vo.getUpdatedMedical()%>&nbsp;</td>
            </tr>
            <%
            	//
            			i++;
            		}
            	}
            %>
        </table>
        </div>
        </td>
    </tr>
	<tr>
		<td colspan='2'>&nbsp;&nbsp;</td>
	</tr>   
	<tr>
		<td colspan='2'>&nbsp;&nbsp;</td>
	</tr>	
	   
     <!-- For Leave Maintenance-->
    <% if ("Yes".equalsIgnoreCase(ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_LEAVE_BALANCE))) {%>
       	<tr>
			<td colspan='2'><image border='0' framespacing='0' frameborder='0' id="imgcon11" src="images/Rminus.png" style="float:left;" onClick="disp2(11,true);disp2(12,false)"/>&nbsp;&nbsp;</td>
		</tr> 	
	    <tr class="tr1">
	        <td align="center" colspan="6"><b><i18n:message key="housekeeping_leavebalance.title" /></b></td>
	    </tr>
	      
	    <tr>
	        <td colspan="6">
	        <div id='div11' style="display:block;position:relative;left:1px;">
		        <table width="100%" bordercolor="#6595D6" border="1" cellspacing="0" cellpadding="3"
		            style="border-collapse: collapse;">
		            <!-- For Annual Leave -->
		            <tr>                
		                <td width="16%" height="20" class="tr3">
		                <div align="right"><i18n:message key="housekeeping_leavebalance.year" /></div>
		                </td>
		                <td width="16%" height="20"><input type="text" value="<%=year%>" id="year" name="year"
		                    onKeyPress="checkInt()"  onKeyUp="getRowValue()" maxlength="4"></td>
		                    
		                <td width="17%" height="20" class="tr3">
		                <div align="right"><i18n:message key="housekeeping_leavebalance.annual_carry_forward" /></div>
		                </td>
		                <td width="17%" height="20"><input type="text" value="<%=carryForward%>" id="carry_forward"
		                    name="carry_forward" onKeyPress="checkKey()" onKeyUp="validateDays(this,'annual')"></td>
		                    
		                 <td width="17%" height="20" class="tr3">
		                <div align="right"><i18n:message key="housekeeping_leavebalance.annual_statutory_entitle" /></div>
		                </td>
		                <td width="17%" height="20"><input type="text" value="<%=statutoryEntitle%>" id="statutory_entitle" name="statutory_entitle"></td>  
		            </tr>
		            <tr>
		                <td width="16%" height="20" class="tr3">
		                <div align="right"><i18n:message key="housekeeping_leavebalance.annual_company_entitle" /></div>
		                </td>
		                <td width="16%" height="20"><input type="text" value="<%=companyEntitle%>" id="company_entitle" name="company_entitle"></td>    
		                
		                <td width="17%" height="20" class="tr3">
		                <div align="right"><i18n:message key="housekeeping_leavebalance.annual_total_entitle" /></div>
		                </td>
		                <td width="17%" height="20"><input type="text" value="<%=entitle%>" id="entitle" name="entitle"
		                    onKeyPress="checkKey()" onKeyUp="validateDays(this,'annual')"></td>
		                                        
		                <td width="17%" height="20" class="tr3">
		                <div align="right"><i18n:message key="housekeeping_leavebalance.annual_applied" /></div>
		                </td>
		                <td width="17%" height="20"><input type="text" value="<%=applied%>" id="applied" name="applied"
		                    onKeyPress="checkKey()" onKeyUp="validateDays(this,'annual')"></td>           
		            </tr>
		            <tr>          
		                <td width="16%" height="20" class="tr3">
		                <div align="right"><i18n:message key="housekeeping_leavebalance.annual_forfeit" /></div>
		                </td>
		                <td width="16%" height="20"><input type="text" value="<%=forfeit%>" id="forfeit" name="forfeit"
		                    onKeyPress="checkKey()" onKeyUp="validateDays(this,'annual')"></td>
		            </tr>
		            <tr>
		                <td width="16%" height="20" class="tr3">
		                <div align="right"><i18n:message key="housekeeping_leavebalance.annual_balance" /></div>
		                <input type="hidden" value="<%=balance%>" id="balance" name="balance"></td>
		                <td width="84%" height="20" colspan="5" id="balance_td"><%=balance%>&nbsp;</td>
		            </tr>
		            
		            <!-- For Sick Leave -->
		            <tr>
		
		                <td width="16%" height="20" class="tr3">
		                <div align="right"><i18n:message key="housekeeping_leavebalance.sick_total_entitle" /></div>
		                </td>
		                <td width="16%" height="20"><input type="text" value="<%=sickTotalEntitle%>" id="sick_total_entitle" name="sick_total_entitle"
		                    onKeyPress="checkKey()" onKeyUp="validateDays(this,'sick')"></td>
		                                        
		                <td width="17%" height="20" class="tr3">
		                <div align="right"><i18n:message key="housekeeping_leavebalance.sick_applied" /></div>
		                </td>
		                <td width="17%" height="20"><input type="text" value="<%=sickApplied%>" id="sick_applied" name="sick_applied"
		                    onKeyPress="checkKey()" onKeyUp="validateDays(this,'sick')"></td>
		                    
		            </tr>
		            <tr>
		                <td width="16%" height="20" class="tr3">
		                <div align="right"><i18n:message key="housekeeping_leavebalance.sick_balance" /></div>
		                <input type="hidden" value="<%=sickBalance%>" id="sick_balance" name="sick_balance"></td>
		                <td width="84%" height="20" colspan="5" id="sick_balance_td"><%=sickBalance%>&nbsp;</td>
		            </tr> 
		            <tr> 
		            	 <td width="100%" align="center" colspan="6">	
					        <input type="button" name="saveBtn"value='<i18n:message key="button.save"/>' onclick="saveItem()" 
					        	class="btn3_mouseout" onmouseover="this.className='btn3_mouseover'" 
					        	onmouseout="this.className='btn3_mouseout'" onmousedown="this.className='btn3_mousedown'" 
					        	onmouseup="this.className='btn3_mouseup'">&nbsp;&nbsp;
					        <input type="button" name="deleteBtn" value='<i18n:message key="button.delete"/>' onclick="deleteItem()"
					            class="btn3_mouseout" onmouseover="this.className='btn3_mouseover'"
					            onmouseout="this.className='btn3_mouseout'" onmousedown="this.className='btn3_mousedown'"
					            onmouseup="this.className='btn3_mouseup'">&nbsp;&nbsp; 
					        <input type="Reset" name="resetBtn" value='<i18n:message key="button.reset"/>' onclick="removeBalance()"
					            class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" 
					            onmouseout="this.className='btn3_mouseout'"onmousedown="this.className='btn3_mousedown'" 
					            onmouseup="this.className='btn3_mouseup'">&nbsp;&nbsp;        
					        <input type="button" name="closeBtn"
					            value='<i18n:message key="button.close"/>' onclick="closeForm()" class="btn3_mouseout"
					            onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
					            onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">

				            <!-- For manual job usage-->
				            <input type="button" name="yearlyJobBtn"
				            	value='<i18n:message key="housekeeping_leavebalance.generate"/>' onclick="manualYearlyJob()" class="btn3_mouseout"
				           		onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
				           		onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
		            	 </td>	              
		            </tr>
		        </table>
	        </div>
	        </td>
	    </tr>
	    <tr>
	        <td colspan="6">
	        <div id='div12' style="display:block;position:relative;left:1px;">
		        <table width="100%" border="0" cellpadding="0" cellspacing="1" class="sortable" id="mytable"
		            style="border: 1px #8899cc solid;">
		            <tr class="liebiao_tou">
		                <td align='center'><input type="checkbox" name="allBtn" onclick="selectAll(this,'itemId')"></td>
		                <td align='center'><i18n:message key="housekeeping_leavebalance.year" /></td>
		                <td align='center'><i18n:message key="housekeeping_leavebalance.annual_carry_forward" /></td>               
		                <td align='center'><i18n:message key="housekeeping_leavebalance.annual_statutory_entitle" /></td>
		                <td align='center'><i18n:message key="housekeeping_leavebalance.annual_company_entitle" /></td>
		                <td align='center'><i18n:message key="housekeeping_leavebalance.annual_total_entitle" /></td>                
		                <td align='center'><i18n:message key="housekeeping_leavebalance.annual_applied" /></td>
		                <td align='center'><i18n:message key="housekeeping_leavebalance.annual_forfeit" /></td>
		                <td align='center'><i18n:message key="housekeeping_leavebalance.annual_balance" /></td>
		                
		                <td align='center'><i18n:message key="housekeeping_leavebalance.sick_total_entitle" /></td>
		                <td align='center'><i18n:message key="housekeeping_leavebalance.sick_applied" /></td>
		                <td align='center'><i18n:message key="housekeeping_leavebalance.sick_balance" /></td>
		            </tr>
		            <%
		            	//
		            	if (list != null) {
		            		int i = 1;
		            		Iterator it = list.iterator();
		            		while (it.hasNext()) {
		            			LeaveBalanceVO vo = (LeaveBalanceVO) it.next();
		            %>
		            <tr class="tr_change">
		                <td align='center'><input type="checkbox" name="itemId" value="<%=vo.getYear()%>"></td>
		                <td align='center'><%=vo.getYear()%>&nbsp;</td>
		                <td align='center'><%=vo.getAnnualCarryForwardDays()%>&nbsp;</td>             
		                <td align='center'><%=vo.getAnnualStatutoryEntitleDays()%>&nbsp;</td>
		                <td align='center'><%=vo.getAnnualCompanyEntitleDays()%>&nbsp;</td>
		                <td align='center'><%=vo.getAnnualTotalEntitleDays()%>&nbsp;</td>               
		                <td align='center'><%=vo.getAnnualAppliedDays()%>&nbsp;</td>
		                <td align='center'><%=vo.getAnnualForfeitDays()%>&nbsp;</td>
		                <td align='center'><%=vo.getAnnualBalanceDays()%>&nbsp;</td>
		                
		                <td align='center'><%=vo.getSickTotalEntitleDays()%>&nbsp;</td>
		                <td align='center'><%=vo.getSickAppliedDays()%>&nbsp;</td>
		                <td align='center'><%=vo.getSickBalanceDays()%>&nbsp;</td>
		            </tr>
		            <%
		            	//
		            			i++;
		            		}
		            	}
		            %>
		        </table>
	        </div>
	        </td>
	    </tr>
		<tr>
			<td colspan='2'>&nbsp;&nbsp;</td>
		</tr>  
		<tr>
			<td colspan='2'>&nbsp;&nbsp;</td>
		</tr>			    	   
    <%}%>
    
     <!-- For Medical Maintenance-->
    <% if ("Yes".equalsIgnoreCase(ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_MEDICAL_CLAIM))) {%>
       	<tr>
			<td colspan='2'><image border='0' framespacing='0' frameborder='0' id="imgcon21" src="images/Rminus.png" style="float:left;" onClick="disp2(21,true);disp2(22,false)"/>&nbsp;&nbsp;</td>
		</tr> 	
	    <tr class="tr1">
	        <td align="center" colspan="12"><b><i18n:message key="housekeeping_medicalbalance.title" /></b></td>
	    </tr>
	      
	    <tr>
	        <td colspan="12">
	        <div id='div21' style="display:block;position:relative;left:1px;">
		        <table id="tableMedical" width="100%" bordercolor="#6595D6" border="1" cellspacing="0" cellpadding="3"
		            style="border-collapse: collapse;">
		            <tr>                
		                <td width="8%" height="20" class="tr3">
		                <div align="right"><i18n:message key="housekeeping_medicalbalance.year" /></div>
		                </td>
		                <td width="8%" height="20"><input type="text" value="<%=yearMed%>" id="yearMed" name="yearMed"
		                    onKeyPress="checkInt()"  onKeyUp="getRowValueMed()" maxlength="4"></td>
		                    
		                <td width="8%" height="20" class="tr3">
		                <div align="right"><i18n:message key="housekeeping_medicalbalance.connubialName" /></div>
		                </td>
		                <td width="8%" height="20"><input type="text" value="<%=connubialName%>" id="connubialName" name="connubialName"></td>
		                    
                 		<td width="8%" height="20" class="tr3">
		                <div align="right"><i18n:message key="housekeeping_medicalbalance.childName" /></div>
		                </td>
		                <td width="8%" height="20"><input type="text" value="<%=childName%>" id="childName" name="childName"></td>
		            </tr>
		            <tr>
		                <td width="8%" height="20" class="tr3">
		                <div align="right"><i18n:message key="housekeeping_medicalbalance.staffCEntitlement" /></div>
		                </td>
		                <td width="8%" height="20"><input type="text" value="<%=staffCEntitlement%>" id="staffCEntitlement"
		                    name="staffCEntitlement" onKeyPress="checkKey()" onKeyUp="validateMedAmt(this,'staffCEntitlement', 'staffCApplied', 'staffCBalance')"></td>
		                    
		                 <td width="8%" height="20" class="tr3">
		                <div align="right"><i18n:message key="housekeeping_medicalbalance.staffCApplied" /></div>
		                </td>
		                <td width="8%" height="20"><input type="text" value="<%=staffCApplied%>" id="staffCApplied" 
		                	name="staffCApplied" onKeyPress="checkKey()" onKeyUp="validateMedAmt(this,'staffCEntitlement', 'staffCApplied', 'staffCBalance')"></td>  
		                
		                <td width="8%" height="20" class="tr3">
		                <div align="right"><i18n:message key="housekeeping_medicalbalance.staffCBalance" /></div>
		                <input type="hidden" value="<%=staffCBalance%>" id="staffCBalance" name="staffCBalance"/>
		                </td>
		                <td width="8%" height="20" id="staffCBalance_td"><%=staffCBalance%>&nbsp;</td>    
		                		                
		                <td width="8%" height="20" class="tr3">
		                <div align="right"><i18n:message key="housekeeping_medicalbalance.staffHEntitlement" /></div>
		                </td>
		                <td width="8%" height="20"><input type="text" value="<%=staffHEntitlement%>" id="staffHEntitlement" 
		                name="staffHEntitlement" onKeyPress="checkKey()" onKeyUp="validateMedAmt(this,'staffHEntitlement', 'staffHApplied', 'staffHBalance')"></td>
		                                        
		                <td width="8%" height="20" class="tr3">
		                <div align="right"><i18n:message key="housekeeping_medicalbalance.staffHApplied" /></div>
		                </td>
		                <td width="8%" height="20"><input type="text" value="<%=staffHApplied%>" id="staffHApplied" 
		                name="staffHApplied" onKeyPress="checkKey()" onKeyUp="validateMedAmt(this,'staffHEntitlement', 'staffHApplied', 'staffHBalance')"></td>    
		                           
		                <td width="10%" height="20" class="tr3">
		                <div align="right"><i18n:message key="housekeeping_medicalbalance.staffHBalance" /></div>
		                <input type="hidden" value="<%=staffHBalance%>" id="staffHBalance" name="staffHBalance">
		                </td>
		                <td width="10%" height="20" id="staffHBalance_td"><%=staffHBalance%>&nbsp;</td>    
		                                                  
		            </tr>

		            <tr>
		                <td width="8%" height="20" class="tr3">
		                <div align="right"><i18n:message key="housekeeping_medicalbalance.connubialCEntitlement" /></div></td>
		                <td width="8%" height="20">
		                <input type="text" value="<%=connubialCEntitlement%>" id="connubialCEntitlement" name="connubialCEntitlement"
		                 onKeyPress="checkKey()" onKeyUp="validateMedAmt(this,'connubialCEntitlement', 'connubialCApplied', 'connubialCBalance')"></td>
		
		                <td width="8%" height="20" class="tr3">
		                <div align="right"><i18n:message key="housekeeping_medicalbalance.connubialCApplied" /></div></td>
		                <td width="8%" height="20">
		                <input type="text" value="<%=connubialCApplied%>" id="connubialCApplied" name="connubialCApplied"
		                 onKeyPress="checkKey()" onKeyUp="validateMedAmt(this,'connubialCEntitlement', 'connubialCApplied', 'connubialCBalance')"></td>
		                                        
		                <td width="8%" height="20" class="tr3">
		                <div align="right"><i18n:message key="housekeeping_medicalbalance.connubialCBalance" /></div>
		                <input type="hidden" value="<%=connubialCBalance%>" id="connubialCBalance" name="connubialCBalance"></td>
		                <td width="8%" height="20" id="connubialCBalance_td"><%=connubialCBalance%>&nbsp;</td>    

		                <td width="8%" height="20" class="tr3">
		                <div align="right"><i18n:message key="housekeeping_medicalbalance.connubialHEntitlement" /></div></td>		                
		                <td width="8%" height="20">
		                <input type="text" value="<%=connubialHEntitlement%>" id="connubialHEntitlement" name="connubialHEntitlement"
		                 onKeyPress="checkKey()" onKeyUp="validateMedAmt(this,'connubialHEntitlement', 'connubialHApplied', 'connubialHBalance')"></td>
		                
		                <td width="8%" height="20" class="tr3">
		                <div align="right"><i18n:message key="housekeeping_medicalbalance.connubialHApplied" /></div></td>
		                <td width="8%" height="20">
		                <input type="text" value="<%=connubialHApplied%>" id="connubialHApplied" name="connubialHApplied"
		                 onKeyPress="checkKey()" onKeyUp="validateMedAmt(this,'connubialHEntitlement', 'connubialHApplied', 'connubialHBalance')"></td>
		                
		                <td width="10%" height="20" class="tr3">
		                <div align="right"><i18n:message key="housekeeping_medicalbalance.connubialHBalance" /></div>
		                <input type="hidden" value="<%=connubialHBalance%>" id="connubialHBalance" name="connubialHBalance"></td>
		                <td width="10%" height="20" id="connubialHBalance_td"><%=connubialHBalance%>&nbsp;</td> 
		            </tr>   
		            
		            <tr>
		                <td width="8%" height="20" class="tr3">
		                <div align="right"><i18n:message key="housekeeping_medicalbalance.childCEntitlement" /></div></td>		                
		                <td width="8%" height="20">
		                <input type="text" value="<%=childCEntitlement%>" id="childCEntitlement" name="childCEntitlement"
		                 onKeyPress="checkKey()" onKeyUp="validateMedAmt(this,'childCEntitlement', 'childCApplied', 'childCBalance')"></td>

		                <td width="8%" height="20" class="tr3">
		                <div align="right"><i18n:message key="housekeeping_medicalbalance.childCApplied" /></div></td>
		                <td width="8%" height="20">
		                <input type="text" value="<%=childCApplied%>" id="childCApplied" name="childCApplied"
		                 onKeyPress="checkKey()" onKeyUp="validateMedAmt(this,'childCEntitlement', 'childCApplied', 'childCBalance')"></td>
		                                        
		                <td width="8%" height="20" class="tr3">
		                <div align="right"><i18n:message key="housekeeping_medicalbalance.childCBalance" /></div>
		                <input type="hidden" value="<%=childCBalance%>" id="childCBalance" name="childCBalance"></td>
		                <td width="8%" height="20" id="childCBalance_td"><%=childCBalance%>&nbsp;</td> 		                

		                <td width="8%" height="20" class="tr3">
		                <div align="right"><i18n:message key="housekeeping_medicalbalance.childHEntitlement" /></div></td>
		                <td width="8%" height="20">
		                <input type="text" value="<%=childHEntitlement%>" id="childHEntitlement" name="childHEntitlement"
		                 onKeyPress="checkKey()" onKeyUp="validateMedAmt(this,'childHEntitlement', 'childHApplied', 'childHBalance')"></td>		                 		                
		                
		                <td width="8%" height="20" class="tr3">
		                <div align="right"><i18n:message key="housekeeping_medicalbalance.childHApplied" /></div></td>
		                <td width="8%" height="20">
		                <input type="text" value="<%=childHApplied%>" id="childHApplied" name="childHApplied"
		                 onKeyPress="checkKey()" onKeyUp="validateMedAmt(this,'childHEntitlement', 'childHApplied', 'childHBalance')"></td>		
	                
		                <td width="10%" height="20" class="tr3">
		                <div align="right"><i18n:message key="housekeeping_medicalbalance.childHBalance" /></div>
		                <input type="hidden" value="<%=childHBalance%>" id="childHBalance" name="childHBalance"></td>
		                <td width="10%" height="20" id="childHBalance_td"><%=childHBalance%>&nbsp;</td> 		                
		            </tr> 
		            <tr> 
		            	 <td width="100%" align="center" colspan="12">	  	
					        <input type="button" name="saveBtn"value='<i18n:message key="button.save"/>' onclick="saveMedItem()" 
					        	class="btn3_mouseout" onmouseover="this.className='btn3_mouseover'" 
					        	onmouseout="this.className='btn3_mouseout'" onmousedown="this.className='btn3_mousedown'" 
					        	onmouseup="this.className='btn3_mouseup'">&nbsp;&nbsp;
					        <input type="button" name="deleteBtn" value='<i18n:message key="button.delete"/>' onclick="deleteMedItem()"
					            class="btn3_mouseout" onmouseover="this.className='btn3_mouseover'"
					            onmouseout="this.className='btn3_mouseout'" onmousedown="this.className='btn3_mousedown'"
					            onmouseup="this.className='btn3_mouseup'">&nbsp;&nbsp; 
					        <input type="Reset" name="resetBtn" value='<i18n:message key="button.reset"/>' onclick="removeBalance()"
					            class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" 
					            onmouseout="this.className='btn3_mouseout'"onmousedown="this.className='btn3_mousedown'" 
					            onmouseup="this.className='btn3_mouseup'">&nbsp;&nbsp;        
					        <input type="button" name="closeBtn"
					            value='<i18n:message key="button.close"/>' onclick="closeForm()" class="btn3_mouseout"
					            onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
					            onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">	
					            
					        <!-- For manual job usage-->
				            <input type="button" name="yearlyJobBtn"
				            	value='<i18n:message key="housekeeping_medicalbalance.generate"/>' onclick="manualYearlyJobMed('<%=currentStaff.getStaffCode()%>')" class="btn3_mouseout"
				           		onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
				           		onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">   	            	 
		            	 </td>
		            </tr>	 	                     
		        </table>
	        </div>
	        </td>
	    </tr>
	        
	    <tr>
	        <td colspan="12">
	        <div id='div22' style="display:block;position:relative;left:1px;">
		        <table width="100%" border="0" cellpadding="0" cellspacing="1" class="sortable" id="medtable"
		            style="border: 1px #8899cc solid;">
		            <tr class="liebiao_tou">
		                <td align='center'><input type="checkbox" name="allBtn" onclick="selectAll(this,'medItemId')"></td>
		                <td align='center'><i18n:message key="housekeeping_medicalbalance.year" /></td>
		                <td align='center'><i18n:message key="housekeeping_medicalbalance.staffCEntitlement" /></td>               
		                <td align='center'><i18n:message key="housekeeping_medicalbalance.staffCApplied" /></td>
		                <td align='center'><i18n:message key="housekeeping_medicalbalance.staffCBalance" /></td>
		                <td align='center'><i18n:message key="housekeeping_medicalbalance.staffHEntitlement" /></td>                
		                <td align='center'><i18n:message key="housekeeping_medicalbalance.staffHApplied" /></td>
		                <td align='center'><i18n:message key="housekeeping_medicalbalance.staffHBalance" /></td>
		                
		                <td align='center'><i18n:message key="housekeeping_medicalbalance.connubialName" /></td>
		                <td align='center'><i18n:message key="housekeeping_medicalbalance.connubialCEntitlement" /></td>	                
		                <td align='center'><i18n:message key="housekeeping_medicalbalance.connubialCApplied" /></td>
		                <td align='center'><i18n:message key="housekeeping_medicalbalance.connubialCBalance" /></td>
		                <td align='center'><i18n:message key="housekeeping_medicalbalance.connubialHEntitlement" /></td>
		                <td align='center'><i18n:message key="housekeeping_medicalbalance.connubialHApplied" /></td>
		                <td align='center'><i18n:message key="housekeeping_medicalbalance.connubialHBalance" /></td>
		                
		                <td align='center'><i18n:message key="housekeeping_medicalbalance.childName" /></td>
		                <td align='center'><i18n:message key="housekeeping_medicalbalance.childCEntitlement" /></td>	                
		                <td align='center'><i18n:message key="housekeeping_medicalbalance.childCApplied" /></td>
		                <td align='center'><i18n:message key="housekeeping_medicalbalance.childCBalance" /></td>
		                <td align='center'><i18n:message key="housekeeping_medicalbalance.childHEntitlement" /></td>
		                <td align='center'><i18n:message key="housekeeping_medicalbalance.childHApplied" /></td>
		                <td align='center'><i18n:message key="housekeeping_medicalbalance.childHBalance" /></td>
		            </tr>
		            <%
		            	//
		            	if (medicalBalanceList != null) {
		            		int i = 1;
		            		Iterator it = medicalBalanceList.iterator();
		            		while (it.hasNext()) {
		            			MedicalBalanceVO vo = (MedicalBalanceVO) it.next();
		            %>
		            <tr class="tr_change">
		                <td align='center'><input type="checkbox" name="medItemId" value="<%=vo.getYear()%>"></td>
		                <td align='center'><%=vo.getYear()%>&nbsp;</td>
		                <td align='center'><%=vo.getStaffCEntitlement()%>&nbsp;</td>             
		                <td align='center'><%=vo.getStaffCApplied()%>&nbsp;</td>
		                <td align='center'><%=vo.getStaffCBalance()%>&nbsp;</td>
		                <td align='center'><%=vo.getStaffHEntitlement()%>&nbsp;</td>               
		                <td align='center'><%=vo.getStaffHApplied()%>&nbsp;</td>
		                <td align='center'><%=vo.getStaffHBalance()%>&nbsp;</td>
	                
	               		<td align='center'><%=vo.getConnubialName()%>&nbsp;</td> 
		                <td align='center'><%=vo.getConnubialCEntitlement()%>&nbsp;</td>             
		                <td align='center'><%=vo.getConnubialCApplied()%>&nbsp;</td>
		                <td align='center'><%=vo.getConnubialCBalance()%>&nbsp;</td>
		                <td align='center'><%=vo.getConnubialHEntitlement()%>&nbsp;</td>               
		                <td align='center'><%=vo.getConnubialHApplied()%>&nbsp;</td>
		                <td align='center'><%=vo.getConnubialHBalance()%>&nbsp;</td>
		                
		                <td align='center'><%=vo.getChildName()%>&nbsp;</td> 
		                <td align='center'><%=vo.getChildCEntitlement()%>&nbsp;</td>             
		                <td align='center'><%=vo.getChildCApplied()%>&nbsp;</td>
		                <td align='center'><%=vo.getChildCBalance()%>&nbsp;</td>
		                <td align='center'><%=vo.getChildHEntitlement()%>&nbsp;</td>               
		                <td align='center'><%=vo.getChildHApplied()%>&nbsp;</td>
		                <td align='center'><%=vo.getChildHBalance()%>&nbsp;</td>
		            </tr>
		            <%
		            	//
		            			i++;
		            		}
		            	}
		            %>
		        </table>
	        </div>
	        </td>
	    </tr>
		<tr>
			<td colspan='2'>&nbsp;&nbsp;</td>
		</tr>  	    	   
    <%}%>    
</table>
</form>
</body>
</html>
<script language="javascript">
	setResizeAble(mytable);
</script>