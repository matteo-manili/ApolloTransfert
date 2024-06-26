<%@ include file="/common/taglibs.jsp"%>
<head>
<title>Gestione Province</title>
<meta name="menu" content="AdminMenu"/>

<!-- jquery -->
<script src="<c:url value="/scripts/vendor/jquery-ui-1.11.4/external/jquery/jquery-2.2.4.min.js"/>"></script>
<script src="<c:url value="/js/bootstrap.min.js"/>"></script>

<link rel="stylesheet" href="<c:url value='/css/ChosenBootstrap.css'/>">
<script src="<c:url value="/scripts/vendor/chosen_v1.5.1/chosen.jquery.min.js"/>"></script>

<script type="text/javascript">
$(document).ready(function () {

	
}); // fine ready
</script>
</head>
<body>

<div class="content-area home-content">
<div class="container">
<div class="">

	<h3 style="margin-top: -20px;">Gestione Recensioni</h3>
	
	<%@ include file="/common/messages.jsp" %>
	
	<c:if test="${not empty recensioneTransferUtil}">
	<!-- commandName="recensioneTransferUtil" modelAttribute="recensioneTransferUtil" -->
	<form:form method="post" commandName="recensioneTransferUtil" action="admin-recensioni" id="recensioneTransferUtilForm" autocomplete="off" onsubmit="">
	<input type="hidden" value="${recensioneTransferUtil.idUser}" name="user-id" />
	
	<div class="well">
		
		<div class="form-group row">
			<label class="col-sm-2 form-control-label">urlTockenPageScriviRecensone</label>
			<div class="col-sm-4">
				<form:input path="urlTockenPageScriviRecensone" class="form-control" disabled="true" />
			</div>
			<label class="col-sm-2 form-control-label">codiceSconto</label>
			<div class="col-sm-4">
				<form:input path="codiceSconto" class="form-control" disabled="true" />
			</div>
		</div>
		
		<div class="form-group row">
			<label class="col-sm-2 form-control-label">percentualeSconto</label>
			<div class="col-sm-2">
				<form:input path="percentualeSconto" class="form-control" disabled="true" />
			</div>
			<label class="col-sm-2 form-control-label">codiceScontoUsato</label>
			<div class="col-sm-2">
				<form:input path="codiceScontoUsato" class="form-control" disabled="true" />
			</div>
			<label class="col-sm-2 form-control-label">codiceScontoAttivo</label>
			<div class="col-sm-2">
				<form:input path="codiceScontoAttivo" class="form-control" disabled="true" />
			</div>
		</div>
		

		<div class="form-group row">
			<c:forEach items="${recensioneTransferUtil.ricercaTransfertList_Totali}" var="ite" varStatus="loop">
				<p><b>ID User:</b> ${ite.user.id} | <b>ID Transfer:</b> ${ite.id} | <b>${ite.ritorno? 'Andata e Ritorno' : 'Solo Andata'}</b> | <b>Giorno:</b> <fmt:formatDate pattern="dd MMMM yyyy, HH:mm" value="${ite.dataOraPrelevamentoDate}" /> 
				| <b>Partenza:</b> ${ite.partenzaRequest}&nbsp;<i class="fa fa-arrow-right" aria-hidden="true"></i>&nbsp;<b>Arrivo:</b> ${ite.arrivoRequest}<p>
				
				<p><b>Approvazione Andata:</b> ${ite.approvazioneAndata}<b> Approvazione Ritorno:</b> ${ite.approvazioneRitorno}</p>
				
				<input type="hidden" name="id-transfer" value="${ite.id}">
				
				<p><select class="form-control" name="stella-punteggio-${ite.id}">
					<option value="0" ${empty ite.punteggioStelleRecensione ? 'selected' : ''}>Indica il punteggio da 1 a 5 Stelle</option>
					<option value="1" ${not empty ite.punteggioStelleRecensione && ite.punteggioStelleRecensione == 1 ? 'selected' : ''}>1 Stella</option>
					<c:forEach var="i" begin="2" end="5">
						<option value="${i}" ${not empty ite.punteggioStelleRecensione && ite.punteggioStelleRecensione == i ? 'selected' : ''}>${i} Stelle</option>
					</c:forEach>
				</select></p>
				
				<p><textarea type="text" name="text-recensione-${ite.id}" rows="6" class="form-control" 
					placeholder="Scrivi Recensione">${ite.recensione}</textarea></p>
		
				<div class="form-group row">
					<div class="col-sm-3">
						<button type="submit" name="salva-recensione" class="btn btn-sm btn-primary">Salva Recensione</button>
					</div>
					<div class="col-sm-3" class="" >
						<label class="checkbox-inline" ><input type="checkbox" style="width: 20px; height: 20px;" 
							${ite.recensioneApprovata ? 'checked' : ''} name="approvata-recensione-${ite.id}">Approvata</label>
					</div>
				</div>
				<c:if test="${!loop.last}"><hr></c:if>  
			</c:forEach>
	
		</div>
 	</div>
	</form:form>
	</c:if>
	
	<!-- TABELLA -->
	<div class="col-sm-12">
		<div class="form-group row well">
			<form method="get" action="${ctx}/admin/admin-recensioni" id="searchForm" class="form-inline" role="form">
		 		<div class="form-group">
					<div class="input-group">
						<label class="sr-only" for="query">ricerca</label>
						<input type="text" name="q" class="form-control" size="30" id="query" value="${param.q}" placeholder="idTransfert">
						<span class="input-group-btn">
							<button type="submit" name="ricerca" id="button.search" class="btn btn-default">ricerca</button>
						</span>
					</div><!-- /input-group -->
		 		</div>
			</form>
		</div> <!-- fine row  -->
	
		<div class="form-group row">
			<display:table name="recensioniList" cellspacing="0" cellpadding="0" requestURI="" defaultsort="0" id="recensione" pagesize="30" 
				class="table table-condensed table-striped" export="false">
				<display:column property="idRicercaTransfert" titleKey="idRicercaTransfert" />
				<display:column property="user.id" titleKey="idUser" url="/admin/admin-recensioni" paramId="idUser" paramProperty="user.id"/>
				<display:column property="user.email" titleKey="email" />
				<display:column property="user.billingInformation.denominazioneCliente" titleKey="denominazioneCliente" />
				<display:column property="recensioneApprovata" titleKey="recensioneApprovata"/>
				<display:column property="punteggioStelleRecensione" titleKey="punteggioStelleRecensione" />
				<display:column property="recensione" titleKey="recensione"/>
	    	</display:table>
		</div> 
	</div> 
	
</div>
</div>
</div>

<%@ include file="/scripts/ConfirmSubmit.jsp"%>

</body>