<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
   <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h1>MÉDICO NUMERO: ${username}</h1>
	<h1>NUMERO DO UTENTE: ${utente}</h1>
	<!--
	<c:forEach items="${listaConsultas}" var="consulta">
   		<tr id = "texto_tab">
    		<td> <button onclick="confirmarConsulta(${consulta.getId}))"> Consultas Pendentes</button> </td>
    	</tr>
	</c:forEach>
	-->
</body>
</html>