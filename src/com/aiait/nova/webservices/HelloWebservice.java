package com.aiait.nova.webservices;

import javax.jws.WebService; 

@WebService 
public class HelloWebservice {
    /** 
     * Web服务中的业务方法 
     * 
     * @return 一个字符串 
     */ 
    public String doSomething(String username) { 
            return username + " is doing something!"; 
    } 
}