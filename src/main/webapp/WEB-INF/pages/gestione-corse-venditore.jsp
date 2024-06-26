<%@ include file="/common/taglibs.jsp"%>
<head>
<title>Gestione Corse Venditore</title>

<!-- jquery -->
<link rel="stylesheet" href="<c:url value="/scripts/vendor/jquery-ui-1.11.4/jquery-ui.css"/>">
<script src="<c:url value="/scripts/vendor/jquery-ui-1.11.4/external/jquery/jquery-2.2.4.min.js"/>"></script>
<script src="<c:url value="/js/bootstrap.min.js"/>"></script>
<%@ include file="/scripts/apollotransfert-js-utility.jsp"%>
</head>

<body>
<div class="content-area home-content">
<div class="container">
<div class="col-md-12">


<%@ include file="/common/messages.jsp" %>

<div class="">

	<form method="post" action="<c:url value='/gestione-corse-venditore'/>" class="">
	
	</form>
	
	<c:if test="${empty ricercheTransfertVenditore}">
	<div class="alert alert-info h4"><big><strong><i class="fa fa-exclamation-triangle "></i> Nota!</strong>
		Non ci sono Ancora Corse Vendute</big>
	</div>
	</c:if>
	
	<c:forEach items="${ricercheTransfertVenditore}" var="ricercaTransfert" varStatus="loop">
	<div class="row ">
		<div class="panel panel-primary">
			<!-- PANEL CORSA -->
			<div class="panel-heading">
				<strong><big>COD. CORSA ${ricercaTransfert.id} | ${ricercaTransfert.ritorno ? 'ANDATA e RITORNO' : 'SOLO ANDATA'}</big></strong><br>
				<strong>Cliente: </strong>${ricercaTransfert.user.fullName}, <strong>Cell: </strong>${ricercaTransfert.user.phoneNumber}, <strong>Email: </strong>${ricercaTransfert.user.email}
				<br>Nome e Telefono Passeggero: <span id="nomeTelefonoPasseggero-${ricercaTransfert.id}">${ricercaTransfert.nomeTelefonoPasseggero}</span>
			</div>
			<!-- RISULTATO PREZZI CORSA -->
			<div class="panel-body">
				<!-- PANEL CORSA ANDATA -->
				<div class="panel ${ricercaTransfert.approvazioneAndata == 1 ?'panel-info':''}${ricercaTransfert.approvazioneAndata == 2 ?'panel-success':''}${ricercaTransfert.approvazioneAndata == 3 ?'panel-danger':''}">
				<div class="panel-heading">
					<strong>ANDATA | STATO CORSA: <span class="text-uppercase"><ins><fmt:message key="${ricercaTransfert.approvazioneAndata}.status.corsa"/></ins></span></strong><br>
					<strong>${ricercaTransfert.formattedAddress_Partenza}</strong>&nbsp;<span class="glyphicon glyphicon-arrow-right"></span>
					&nbsp;<strong>${ricercaTransfert.formattedAddress_Arrivo}</strong>
				</div>
				<div class="panel-body">
					<strong>Giorno Ora Prelevamento:</strong> <fmt:formatDate pattern="dd MMMM yyyy, HH:mm" value="${ricercaTransfert.dataOraPrelevamentoDate}" /> 
					&nbsp;<strong>Distanza: </strong>${ricercaTransfert.distanzaText}<strong>&nbsp;Durata: </strong>${ricercaTransfert.durataConTrafficoText}
				</div>
				</div>
				<!-- PANEL CORSA RITORNO -->
				<c:if test="${ricercaTransfert.ritorno}">
				<div class="panel ${ricercaTransfert.approvazioneRitorno == 1 ?'panel-info':''}${ricercaTransfert.approvazioneRitorno == 2 ?'panel-success':''}${ricercaTransfert.approvazioneRitorno == 3 ?'panel-danger':''}">
				<div class="panel-heading">
					<strong>RITORNO | STATO CORSA: <span class="text-uppercase"><ins><fmt:message key="${ricercaTransfert.approvazioneRitorno}.status.corsa"/></ins></span></strong><br>
					<strong>${ricercaTransfert.formattedAddress_Arrivo}</strong>&nbsp;<span class="glyphicon glyphicon-arrow-right"></span>
					&nbsp;<strong>${ricercaTransfert.formattedAddress_Partenza}</strong>
				</div>
				<div class="panel-body">
					<strong>Giorno Ora Prelevamento:</strong> <fmt:formatDate pattern="dd MMMM yyyy, HH:mm" value="${ricercaTransfert.dataOraRitornoDate}" />
					&nbsp;<strong>Distanza: </strong>${ricercaTransfert.distanzaTextRitorno}<strong>&nbsp;Durata: </strong>${ricercaTransfert.durataConTrafficoTextRitorno}
				</div>
				</div>
				</c:if>
				<!-- PANEL INTROTI CORSA -->
				<div class="panel panel-info">
				<div class="panel-heading">
					<strong>Introiti Corsa</strong>
				</div>
				<div class="panel-body">
					<div class="row ">
						<div class=" col-xs-12 col-sm-12 col-md-12 col-lg-12">
							<div class="text-info "><strong><fmt:message key="${ricercaTransfert.richiestaMediaScelta.classeAutoveicolo.nome}"/>&nbsp;<fmt:message key="${ricercaTransfert.richiestaMediaScelta.classeAutoveicolo.nome}.num.pass"/>&nbsp;<fmt:message key="${ricercaTransfert.richiestaMediaScelta.classeAutoveicolo.nome}.desc"/></strong></div>
							<div class="text-info "><small><strong><em>${descrizioneCategorieAutoMap[ricercaTransfert.richiestaMediaScelta.classeAutoveicolo.id]}</em></strong></small></div>
						</div>
					</div>
					<div class="row ">
						<div class=" col-xs-4 col-sm-3 col-md-3 col-lg-3 ">
							<label class="control-label "><small>Compeso Autista </small></label>
							<fmt:formatNumber var="prezzoTotaleAutista" pattern="0.00" value="${ricercaTransfert.richiestaMediaScelta.prezzoTotaleAutista}" />
							<input type="text" value="${prezzoTotaleAutista}" class="form-control input-sm"  id="compensoAutistaId" disabled> 
						</div>
						<div class=" col-xs-4 col-sm-3 col-md-3 col-lg-3 ">
							<label class="control-label "><small>Commissione ApolloTransfert ${ricercaTransfert.percentualeServizioRichiestaMediaScelta}&#37;</small></label>
							<fmt:formatNumber var="prezzoCommissioneServizio" pattern="0.00" minFractionDigits="2" value="${ricercaTransfert.richiestaMediaScelta.prezzoCommissioneServizio + prezzoCommissioneServizioIva}" />
							<input type="text" value="${prezzoCommissioneServizio}" class="form-control input-sm"  id="compensoCommApolloId" disabled> 
						</div>
						<div class=" col-xs-4 col-sm-3 col-md-3 col-lg-3 bg-primary " style="border-radius: 5px;">
							<label class="control-label "><small>Commissione Venditore ${ricercaTransfert.percentualeServizioVenditoreRichiestaMediaScelta}&#37;</small></label>
							<fmt:formatNumber var="prezzoCommissioneVenditore" pattern="0.00" minFractionDigits="2" value="${ricercaTransfert.richiestaMediaScelta.prezzoCommissioneVenditore}" />
							<input type="text" value="${prezzoCommissioneVenditore}" class="form-control input-sm" style="margin-bottom: 15px;" name="tariffa-regione" id="compensoCommVenditore" disabled> 
						</div>
						<div class=" col-xs-4 col-sm-3 col-md-3 col-lg-3">
							<label class="control-label "><small>Totale Prezzo Cliente</small></label>
							<fmt:formatNumber var="prezzoTotaleCliente" pattern="0.00" minFractionDigits="2" value="${ricercaTransfert.richiestaMediaScelta.prezzoTotaleCliente}" />
							<input type="text" value="${prezzoTotaleCliente}" class="form-control input-sm" name="tariffa-regione" id="totalePrezzoCliente" disabled> 
						</div>
					</div> 
				</div>
				</div>
			</div> <!-- fine body -->
			
			<div class="panel-footer">
				<div class="">
					<button type="button" id="${ricercaTransfert.id}" class="btn btn-primary alertConfirmInfoAutista">
						<span class="glyphicon glyphicon-info-sign"></span> Info Autista</button>
					<a class="btn btn-sm btn-info openDatiPasseggeroModal" id="${ricercaTransfert.id}">
						<span class="fa fa-user"></span> <fmt:message key="messaggio.modifica.dati.passeggero.corsa"></fmt:message></a>
					<a class="btn btn-sm btn-danger gestioneRimborsoCliente" id="${ricercaTransfert.id}">
						<span class="fa fa-credit-card"></span> <fmt:message key="messaggio.cliente.cancella.corsa"></fmt:message></a>
					<a class="btn btn-sm btn-info checkFatturaDisponibile" id="${ricercaTransfert.id}">
						<span class="glyphicon glyphicon-download-alt"></span> Scarica Fattura</a>
				</div>
			</div> <!-- fine footer -->
		</div>
	
		<!-- Modal Dati Passeggero -->
		<%@ include file="/common/modifica-dati-passeggero-modal.jsp"%>
	
	
	</div>
	</c:forEach>
</div> <!-- fine well  -->

</div>
</div>
</div>

<%@ include file="/scripts/ConfirmSubmit.jsp"%>
</body>