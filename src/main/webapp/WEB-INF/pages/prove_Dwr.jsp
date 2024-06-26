<%@ include file="/common/taglibs.jsp"%>

<head>
<title><fmt:message key="home.title"/></title>
<script src="<c:url value="/scripts/vendor/jquery-ui-1.11.4/external/jquery/jquery-2.2.4.min.js"/>"></script>
<script src="<c:url value="/js/bootstrap.min.js"/>"></script>

</head>
<body>
<%
try{
%>

<div class="content-area home-content">
<div class="container">
<div class="col-md-12">
<%@ include file="/common/messages.jsp" %>

<!-- VEDERE \apollon\src\main\webapp\WEB-INF\dwr.xml -->

<!-- NON FUNZIONANO LE CHAMATE A SERVER REMOTO. FUNZIONA SOLO SE LE CHIAMATE PARTONO DA PAGINA DELLA APPLICAZIONE
<script src="https://www.apollotransfert.com/dwr/engine.js"></script>
<script src="https://www.apollotransfert.com/dwr/util.js"></script>
<script src="https://www.apollotransfert.com/dwr/interface/ProvaDwr.js"></script>  -->

<script src="<c:url value="/dwr/engine.js"/>"></script>
<script src="<c:url value="/dwr/util.js"/>"></script>
<script src="<c:url value="/dwr/interface/ProvaDwr.js"/>"></script>

<script language="javascript">
//onloadmethod();
//function onloadmethod() {
	dwr.engine.setActiveReverseAjax(true);
	ReverseClass.callReverseDWR();
//}
</script>
<ul id="updates"> // Here server will update its data
</ul>


	
</div>
</div>
</div>
<%
}catch(final Exception e) {
	e.getMessage();
	e.printStackTrace();
}
%>
</body>