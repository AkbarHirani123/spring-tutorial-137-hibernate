<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<script type="text/javascript">
	$(document).ready(function() {
		document.f.username.focus();
	});
</script>

<h3>Login with Username and Password</h3>

<form name='f' action='${pageContext.request.contextPath}/login'
	method='POST'>
	<table class="formtable">
		<tr>
			<td>User:</td>
			<td><input type='text' name='username' value=''></td>
		</tr>
		<tr>
			<td>Password:</td>
			<td><input type='password' name='password' /></td>
		</tr>
		<tr>
			<td>Remember me:</td>
			<td><input type='checkbox' name='remember-me' /></td>
		</tr>
		<tr>
			<td colspan='1'><input name="submit" type="submit" value="Login" /></td>
			<td><c:if test="${param.error != null}">
					<a class="error">Login failed. Enter correct username and
						password. </a>
				</c:if></td>
		</tr>
		<input type="hidden" name="${_csrf.parameterName}"
			value="${_csrf.token}" />
	</table>
</form>

<p>
	<a href="<c:url value="/newaccount"/>">Create account</a>
</p>