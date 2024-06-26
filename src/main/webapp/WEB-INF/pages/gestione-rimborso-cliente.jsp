<%@ include file="/common/taglibs.jsp"%>
<head>
    <title>Gestione Rimborso</title>
	<!-- jquery -->
	<link rel="stylesheet" href="<c:url value="/scripts/vendor/jquery-ui-1.11.4/jquery-ui.css"/>">
	<script src="<c:url value="/scripts/vendor/jquery-ui-1.11.4/external/jquery/jquery-2.2.4.min.js"/>"></script>
	<script src="<c:url value="/js/bootstrap.min.js"/>"></script>
	<script src="<c:url value="/scripts/vendor/jquery-ui-1.11.4/jquery-ui.min.js"/>"></script>
	<%@ include file="/scripts/apollotransfert-js-utility.jsp"%>
</head>
<body>
<div class="content-area home-content">
<div class="container">
<div class="">
	<h3 style="margin-top: -20px;">Gestione Rimborso</h3>
	<%@ include file="/common/messages.jsp" %>
	<c:if test="${not empty ricercaTransfert}">
		<form:form commandName="ricercaTransfert" method="post" action="gestioneRimborsoCliente" id="gestioneRimborsoCliente" autocomplete="off" onsubmit="">
		<form:hidden path="id"/>
			<%@ include file="/common/gestione-rimborso.jsp" %>
			<!-- pulsanti -->
			<div class="well">
			<div class="row ">
				<div class=" pull-right">
					<button type="submit" name="cancel" class="btn btn-warning">Annulla <span class="fa fa-ban"></span></button>
					<button type="button" name="esegui-rimborso-cliente" class="btn btn-danger alertConfirmGenerale"><fmt:message key="messaggio.cliente.cancella.corsa"/>
						<span class="fa fa-arrow-left"></span> <span class="fa fa-credit-card"></span></button>
					<button type="button" id="${ricercaTransfert.id}" class="btn btn-info checkFatturaDisponibile">Scarica Fattura 
					<span class="glyphicon glyphicon-download-alt"></span></button>
				</div>
			</div>
		</form:form>
	</c:if>
</div>
</div>
</div>
<%@ include file="/scripts/ConfirmSubmit.jsp"%>
</body>