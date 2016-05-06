<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<h3>Authorized users only. Admin.</h3>
<table class="formtable">
	<tr>
		<td><b>Username</b></td>
		<td><b>Email</b></td>
		<td><b>Role</b></td>
		<td><b>Enabled</b></td>
	</tr>
	<c:forEach items="${users}" var="user">
		<tr>
			<td><c:out value="${user.username}"></c:out></td>
			<td><c:out value="${user.email}"></c:out></td>
			<td><c:out value="${user.authority}"></c:out></td>
			<td><c:out value="${user.enabled}"></c:out></td>
		</tr>
	</c:forEach>
</table>
