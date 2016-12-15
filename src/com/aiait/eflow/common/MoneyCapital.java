package com.aiait.eflow.common;
import  java.math.BigDecimal;


public class MoneyCapital {
	//public static final boolean NEGATIVE = false ;     // �Ƿ���������ʾ 
	public static final boolean NEGATIVE = true ;     // �Ƿ���������ʾ
    public static final String[][] CAPITAL;
    static {
       CAPITAL=new String[][]{
               {"��", "Ҽ", "��", "��", "��", "��", "½", "��", "��", "��"},     // 0-9 
               {"ʰ", "��" , "Ǫ" },// ʰ,��,Ǫ 
               {"��"},             // �� 
               {"��"},             // �� 
               {"Ԫ"},             // ��λ 
               {"��", "��"},       // С��λ,{"��","��","��"} 
               //{"��", "��"}        // ��������ǰ׺��ʽ,��NEGATIVE=trueʱ��Ч 
               {"", "��"}
       };
   }
    /** 
    * ת���ɴ�д���
    *  @param  strMoney(String):�淶�����ֻ�����ʽ�ַ���
     */ 
    public   static  String toFrmString(String strMoney) {
       StringBuffer sb=new  StringBuffer( 100 );
       
       String[] tmp=strMoney.trim().split("\\.");
       
        // --------����λ����--------------- 
       char [] ci=tmp[ 0 ].toCharArray();
       
       char  theBit='0' ;         // ��ǰ����λ 
       int  zeroBit=0 ;         // ��ʼ��λ 
       int  bitLen=0 ;             // ��ǰλ������λ��(�Ӹ�λ��ʼ0����) 
       boolean  flag=false ;     // �Ƿ������Ч������λ 
       for ( int  index=0; index<ci.length; index++){
           theBit=ci[index];                 // ȡ����ǰ���������λ 
           bitLen=ci.length-index-1;         // ���㵱ǰ�����������ʲôλ�� 
           
            if (zeroBit>0 && theBit!='0'){
               sb.append(CAPITAL[0][0]);     // ��ǰ���� 
           }
            if (theBit!='0') sb.append(CAPITAL[0][theBit-'0']);     // ��д���� 
           
            if (bitLen%8==0){               
                if (bitLen==0){
                    if (ci.length>1 || theBit!='0') sb.append(CAPITAL[4][0]);// Ԫ 
               } else {                
                   sb.append(CAPITAL[3][0]);// �� 
               }               
           } else {               
                if (bitLen%4==0){
                    if (theBit!='0' || zeroBit==0 || zeroBit-bitLen<3){
                       sb.append(CAPITAL[ 2 ][ 0 ]);// �� 
                   }
               } else {
                    // Ǫ��ʰ 
                    if (theBit!='0') sb.append(CAPITAL[1][bitLen%4-1]);     // Ǫ��ʰ 
               }
           }           
            // ��鲢����������λ
            if (theBit=='0'){
               zeroBit = zeroBit==0?bitLen:zeroBit;
           } else {
               zeroBit=0;
               flag=true ;
           }           
       }
        // --------С��λ����--------------- 
        char[] cf=null ;
        if (tmp.length>1){
           cf=tmp[1].toCharArray();
           
            for (int index=0; index<cf.length; index++) {
               theBit=cf[index];                 // ȡ����ǰ�����С��λ 
                if (zeroBit>0 && theBit!='0' && flag){
                   sb.append(CAPITAL[0][0]);     // ��ǰ���� 
               }
                if (theBit!='0') sb.append(CAPITAL[0][theBit-'0']);     // ��д���� 
                if (theBit!='0') sb.append(CAPITAL[5][index]);             // �Ƿ�               
               zeroBit  =  theBit == '0'?1:0;
           }
       }       
        return  sb.length()==0?(CAPITAL[0][0]+CAPITAL[4][0]):sb.toString();
   }
    /** 
    * ����������ʽת���ɴ�д
    *  @param  strMoney(String):���ҵ�������ʽ�ַ���
     */ 
	public static String parseMoney(String strMoney) throws NumberFormatException {
       BigDecimal bd=new BigDecimal(strMoney);
        // ������ 
        //if (bd.signum()==-1 && !NEGATIVE) throw new NumberFormatException("Money Can't be negative");
       if (bd.signum()==-1) throw new NumberFormatException("Money Can't be negative");
       
        try {
           bd.setScale(CAPITAL[5].length);
        }catch(ArithmeticException e){
            throw new NumberFormatException("ֻ��Ϊ"+CAPITAL[5].length+"λС�� ");
        }
        // ��д���ת��
        if (NEGATIVE && bd.signum()!=0) {
            return  CAPITAL[6][bd.signum()==- 1?1:0]+toFrmString(bd.abs().toString());
       } else {
            return  toFrmString(bd.toString());
       }
       
   }
}
