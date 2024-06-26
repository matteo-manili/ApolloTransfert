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
	<%@ include file="/common/messages.jsp" %>
	<%@ include file="/common/TemplateCorseAutista_js.jsp"%>
	<%@ include file="/common/TemplateCorseAutista.jsp"%>
	<div class=" ">
		<p class=""><strong><fmt:message key="info.cliente.ritardo.addebito.costo.mezzora">
			<fmt:param value="${VALORE_EURO_ORA_RITARDO_CLIENTE_CON_TASSA_SERVZIO}"/>
			<fmt:param value="${VALORE_EURO_ORA_RITARDO_CLIENTE}"/>
			</fmt:message></strong></p>
	</div>
	<div id="corseA"></div>
	<div id="corseC"></div>
	<div id="corseB"></div>
	<div id="corseD"></div>
</div>
</div>
</div>
</body>