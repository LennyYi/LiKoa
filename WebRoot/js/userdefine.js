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
  alert("����û���������ĺͷ����ı�׼����𣬳ɼ�ͳһ��д�ڴ���");
}

function check_late_BPO02(){
  var late=document.getElementsByName("field_1_4")[0].value;
  if(late==1){
    alert("���������ڳٵ������˼�¼���Ѿ�û������ý�����ʸ񣬽�������ȷ��������������ý���");
  }
}

function check_holiday_BPO02(){
  var holiday=document.getElementsByName("field_1_6")[0].value;
  if(holiday.substr(0,1)==".")
    document.getElementsByName("field_1_6")[0].value="0"+holiday;  
  if(holiday>0.5){
    alert("���������µ���н�ٺͲ�����������0.5�죬�Ѿ�û������ý�����ʸ񣬽�������ȷ��������������ý���");
  }
}

function check_present_day_BPO02(){
  var present=document.getElementsByName("field_1_5")[0].value;
  var workday=document.getElementsByName("field_1_10")[0].value;
  if(present!=""&&workday!=""){
    if(present<(workday*0.8)){
      alert("������ı��³����������ڹ����յ�80%���Ѿ�û������ý�����ʸ񣬽�������ȷ��������������ý���");
    }
  }
}

function check_late_BPO03(){
  var late=document.getElementsByName("field_1_12")[0].value;
  if(late==1){
    alert("���������ڳٵ������˼�¼���Ѿ�û������ý�����ʸ񣬽�������ȷ��������������ý���");
  }
}

function check_holiday_BPO03(){
  var holiday=document.getElementsByName("field_1_14")[0].value;
  if(holiday.substr(0,1)==".")
    document.getElementsByName("field_1_14")[0].value="0"+holiday;  
  if(holiday>0.5){
    alert("���������µ���н�ٺͲ�����������0.5�죬�Ѿ�û������ý�����ʸ񣬽�������ȷ��������������ý���");
  }
}

function check_present_day_BPO03(){
  var present=document.getElementsByName("field_1_13")[0].value;
  var workday=document.getElementsByName("field_1_15")[0].value;
  if(present!=""&&workday!=""){
    if(present<workday*0.8){
      alert("������ı��³����������ڹ����յ�80%���Ѿ�û������ý�����ʸ񣬽�������ȷ��������������ý���");
    }
  }
}

function check_staff_validation_BPO03(){
    var validation=document.getElementsByName("field_1_6")[0].value;
    if(validation==2){
      alert("���������������û�ж���ͨ����ְԱ���������Ѿ�û������ý�����ʸ񣬽�������ȷ��������������ý���");
    }
}

function check_Qmark_BPO03(){
    var qmark=document.getElementsByName("field_1_5")[0].value;
    if(qmark==2){
      alert("�������ȥһ��û�л��Q-mark���Ѿ�û������ý�����ʸ񣬽�������ȷ��������������ý���");
    }
}

function tutor_range_BPO03(){
    var rank=document.getElementsByName("field_1_10")[0].value;
    if(rank>2){
      alert("��ı������ڵ�ʦЧ��������������ǰ��λ֮�⣬������ȷ���������ύ��");
    }
}