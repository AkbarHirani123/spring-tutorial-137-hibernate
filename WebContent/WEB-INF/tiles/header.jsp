<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<a class="title" href="<c:url value='/'/>">OFFERS</a>

<sec:authorize access="!isAuthenticated()">
	<a href='<c:url var="loginUrl" value="/login" />'></a>

	<form class="login" action="${loginUrl}" method="get">
		<input type="submit" value="Login" />
	</form>

</sec:authorize>

<sec:authorize access="isAuthenticated()">
	<a href='<c:url var="logoutUrl" value="/logout" />'></a>

	<form class="login" action="${logoutUrl}" method="post">

		<input type="submit" value="Log out" /> <input type="hidden"
			name="${_csrf.parameterName}" value="${_csrf.token}" />

	</form>
</sec:authorize>