package com.aiait.nova.webservices;

import javax.jws.WebService; 

@WebService 
public class HelloWebservice {
    /** 
     * Web�����е�ҵ�񷽷� 
     * 
     * @return һ���ַ��� 
     */ 
    public String doSomething(String username) { 
            return username + " is doing something!"; 
    } 
}