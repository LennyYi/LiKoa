function ul_sel(){
  alert('Hello');
}

function servicePriseTS(obj){
    if(obj.value=='1'){
      $("field_S5_2").required = "false";
   }else{
     $("field_S5_2").required = "true";
  }
}


function jdesRoleChange(obj){
   var index = obj.rowIndex;
   if(index=='-1') index = '0';
   if(obj.value=='02'){
      document.getElementsByName("field_3_5")[index].disabled = 'true';
      document.getElementsByName("field_3_7")[index].disabled = '';
   }else{
      document.getElementsByName("field_3_5")[index].disabled = "";
      document.getElementsByName("field_3_7")[index].disabled = 'true';   
   }
 }

function jp_njp(){
  alert("对于没有区分日文和非日文标准的组别，成绩统一填写在此项");
}

function check_late_BPO02(){
  var late=document.getElementsByName("field_1_4")[0].value;
  if(late==1){
    alert("由于您存在迟到或早退记录，已经没有申请该奖金的资格，建议重新确认输入或放弃申请该奖金");
  }
}

function check_holiday_BPO02(){
  var holiday=document.getElementsByName("field_1_6")[0].value;
  if(holiday.substr(0,1)==".")
    document.getElementsByName("field_1_6")[0].value="0"+holiday;  
  if(holiday>0.5){
    alert("由于您本月的无薪假和病假天数超过0.5天，已经没有申请该奖金的资格，建议重新确认输入或放弃申请该奖金");
  }
}

function check_present_day_BPO02(){
  var present=document.getElementsByName("field_1_5")[0].value;
  var workday=document.getElementsByName("field_1_10")[0].value;
  if(present!=""&&workday!=""){
    if(present<(workday*0.8)){
      alert("由于你的本月出勤日少于于工作日的80%，已经没有申请该奖金的资格，建议重新确认输入或放弃申请该奖金");
    }
  }
}

function check_late_BPO03(){
  var late=document.getElementsByName("field_1_12")[0].value;
  if(late==1){
    alert("由于您存在迟到或早退记录，已经没有申请该奖金的资格，建议重新确认输入或放弃申请该奖金");
  }
}

function check_holiday_BPO03(){
  var holiday=document.getElementsByName("field_1_14")[0].value;
  if(holiday.substr(0,1)==".")
    document.getElementsByName("field_1_14")[0].value="0"+holiday;  
  if(holiday>0.5){
    alert("由于您本月的无薪假和病假天数超过0.5天，已经没有申请该奖金的资格，建议重新确认输入或放弃申请该奖金");
  }
}

function check_present_day_BPO03(){
  var present=document.getElementsByName("field_1_13")[0].value;
  var workday=document.getElementsByName("field_1_15")[0].value;
  if(present!=""&&workday!=""){
    if(present<workday*0.8){
      alert("由于你的本月出勤日少于于工作日的80%，已经没有申请该奖金的资格，建议重新确认输入或放弃申请该奖金");
    }
  }
}

function check_staff_validation_BPO03(){
    var validation=document.getElementsByName("field_1_6")[0].value;
    if(validation==2){
      alert("由于你最近三个月没有都能通过在职员工评估，已经没有申请该奖金的资格，建议重新确认输入或放弃申请该奖金");
    }
}

function check_Qmark_BPO03(){
    var qmark=document.getElementsByName("field_1_5")[0].value;
    if(qmark==2){
      alert("由于你过去一年没有获得Q-mark，已经没有申请该奖金的资格，建议重新确认输入或放弃申请该奖金");
    }
}

function tutor_range_BPO03(){
    var rank=document.getElementsByName("field_1_10")[0].value;
    if(rank>2){
      alert("你的本月组内导师效率评比排名处于前两位之外，请认真确认输入再提交表单");
    }
}