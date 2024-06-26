<%@ include file="/common/taglibs.jsp"%>

<page:applyDecorator name="default">

<head>
    <title><fmt:message key="404.title"/></title>
    <meta name="heading" content="<fmt:message key='404.title'/>"/>
</head>

<body>
<div class="content-area home-content">
<div class="container">
<div class="col-md-12">
	<div class="alert alert-info h4" role="alert">
		<p><fmt:message key="404.message">
		<fmt:param><c:url value="/"/></fmt:param>
		</fmt:message></p>
	</div>
</div>
</div>
</div>
	
<script src="<c:url value="/scripts/vendor/bootstrap-without-jquery.min.js"/>"></script>
</body>

</page:applyDecorator>