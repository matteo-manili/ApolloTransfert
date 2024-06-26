<%@ page language="java" isErrorPage="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>

<html>
<head>
    <title><fmt:message key="errorPage.title"/></title>
</head>

<body id="error">
<div class="content-area home-content">
<div class="container">
<div class="col-md-12">

	<div class="container">
	<h1><fmt:message key="errorPage.heading"/></h1>
	<%@ include file="/common/messages.jsp" %>
	<p><fmt:message key="errorPage.message"/></p>
	</div>
	<script src="<c:url value="/scripts/vendor/bootstrap-without-jquery.min.js"/>"></script>
    
</div>
</div>
</div>
</body>
</html>
