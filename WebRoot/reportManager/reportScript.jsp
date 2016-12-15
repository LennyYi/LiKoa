<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%
	String script = (String) request.getAttribute("script");
	script = script == null ? "" : script;
	out.write(script);
%>