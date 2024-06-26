<%@ include file="/common/taglibs.jsp"%>

<page:applyDecorator name="default">

<head>
    <title><fmt:message key="403.title"/></title>
    <meta name="breadcrumb" content="<fmt:message key='403.title'/>"/>
</head>

<body>
<div class="content-area home-content">
<div class="container">
<div class="col-md-12">
	
	<div class="alert alert-info h4" role="alert">
	<h1><fmt:message key="403.title"/></h1>
	<p class="text-center">
	 <fmt:message key="403.message">
	<fmt:param><c:url value="/"/></fmt:param>
	</fmt:message>
	</p>
	<a class="btn btn-info" href="<c:url value='/home-user'/>">TORNA ALLA HOME</a>
	</div>
	
</div>
</div>
</div>
	
	<script src="<c:url value="/scripts/vendor/bootstrap-without-jquery.min.js"/>"></script>
</body>
</page:applyDecorator>