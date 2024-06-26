<%@ include file="/common/taglibs.jsp"%>
<head>
	<title><fmt:message key="home.title"/></title>
	<meta name="breadcrumb" content="<fmt:message key="breadcrumb.homeUtente"/>"/>
	<!-- jquery -->
	<link rel="stylesheet" href="<c:url value="/scripts/vendor/jquery-ui-1.11.4/jquery-ui.css"/>">
	<script src="<c:url value="/scripts/vendor/jquery-ui-1.11.4/external/jquery/jquery-2.2.4.min.js"/>"></script>
	<script src="<c:url value="/js/bootstrap.min.js"/>"></script>
	<script src="<c:url value="/scripts/vendor/jquery-ui-1.11.4/jquery-ui.min.js"/>"></script>
	<script src="<c:url value="/js/mustache.js"/>"></script>
	<script src="<c:url value="/scripts/vendor/bootbox.min.js"/>"></script>
	<link rel="stylesheet" href="<c:url value="/css/blinkeffect.css"/>">
</head>
<body>
<div class="content-area home-content">
<div class="container">
<div class="">

	<div class="">
	<c:if test="${corseDaEseguireCliente + corseEseguiteCliente > 0}">
		<%@ include file="/common/PanelCorseCliente.jsp"%>
	</c:if>
	</div>
	
</div>
</div>
</div>
<!--container end-->
</body>