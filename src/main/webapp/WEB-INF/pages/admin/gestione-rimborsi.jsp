<%@ include file="/common/taglibs.jsp"%>

<head>
    <title>Gestione Rimborsi</title>
	<link rel="stylesheet" href="<c:url value="/scripts/vendor/jquery-ui-1.11.4/jquery-ui.css"/>">
	<script src="<c:url value="/scripts/vendor/jquery-ui-1.11.4/external/jquery/jquery-2.2.4.min.js"/>"></script>
	<script src="<c:url value="/js/bootstrap.min.js"/>"></script>
	<script src="<c:url value="/scripts/vendor/jquery-ui-1.11.4/jquery-ui.min.js"/>"></script>
	<%@ include file="/scripts/apollotransfert-js-utility.jsp"%>
	<script type="text/javascript">
	$(document).ready(function () {
		
	}); // fine ready
	</script>
</head>
<body>
<div class="content-area home-content">
<div class="container">
<div class="">
	<h3 style="margin-top: -20px;">Gestione Rimborsi</h3>
	<c:choose>
        <c:when test="${ stripeSWITCH == false && payPalSWITCH == false }">
			<div class="alert alert-info " role="alert">
				<strong><p><big>Attenzione!</big> Modalit&agrave; in STATO di TEST.</p></strong>
			</div>
        </c:when>
        <c:when test="${ stripeSWITCH == true || payPalSWITCH == true }">
			<div class="alert alert-success " role="alert">
				<strong><p><big>Attenzione!</big> Modalit&agrave; in STATO di PRODUZIONE.</p></strong>
			</div>
        </c:when>
	</c:choose>
	
	<%@ include file="/common/messages.jsp" %>
	<c:if test="${not empty ricercaTransfert}">
		<form:form commandName="ricercaTransfert" method="post" action="admin-gestioneRimborsi" id="gestioneRimborsiAdmin" autocomplete="off" onsubmit="">
		<form:hidden path="id"/>
		
			<%@ include file="/common/gestione-rimborso.jsp" %>
			
			<div class="well">
				<div class="row form-group">
					<p><ins><strong>AVVISI INVITATI RIMBORSO: </strong>${(NumeroAvvisiInviatiRimborso != null) ? NumeroAvvisiInviatiRimborso : 0}</ins></p>
				</div>
				<div class="row form-group">
					<div class="col-sm-1">
						<label class=" form-control-label"><small>Interi</small></label>
					</div>
					<div class="col-sm-2">
						<input type="text" name="valore-rimborso-interi" class="form-control">
					</div>
					<div class="col-sm-3">
						<label class=" form-control-label"><small>Decimali (inserire 2 decimali es: 3.00)</small></label>
					</div>
					<div class="col-sm-2">
						<input type="text" maxlength="2" name="valore-rimborso-decimali" class="form-control">
					</div>
				</div>
				<!-- pulsanti -->
				<div class="row  ">
					<div class=" pull-right">
						<button type="submit" name="cancel" class="btn btn-warning">Annulla <span class="fa fa-ban"></span></button>
						
						<button type="button" name="esegui-rimborso" class="btn btn-success alertConfirmGenerale">Esegui Rimborso 
							<span class="fa fa-arrow-left"></span> <span class="fa fa-credit-card"></span></button>
						
						<button type="button" id="${ricercaTransfert.id}" class="btn btn-info checkFatturaDisponibile">Scarica Fattura 
						<span class="glyphicon glyphicon-download-alt"></span></button>
			
						<button type="button" name="invia-avviso-rimborso-eseguito" class="btn btn-danger alertConfirmGenerale" 
							${( (not empty ricercaTransfert.richiestaMediaScelta  && ricercaTransfert.richiestaMediaScelta.rimborsoCliente == null) 
								|| 
								(not empty ricercaTransfert.richiestaAutistaParticolareAcquistato && ricercaTransfert.richiestaAutistaParticolareAcquistato.rimborsoCliente == null)) ? 'disabled' : '' }>Invia Avviso Rimborso Eseguito
								<span class="fa fa-envelope"></span> <span class="fa fa-mobile"></span> <span class="fa fa-bell"></span></button>
					</div>
				</div>
			</div>
		</form:form>
	</c:if>

	<div class="col-sm-12">
		<div class="form-group row">
			<form method="get" action="${ctx}/admin/admin-gestioneRimborsi" id="searchForm" class="form-inline" role="form">
				<div class="input-group">
					<label class="sr-only" for="query">ricerca</label>
					<input type="text" name="q" class="form-control" size="30" id="query" value="${param.q}" placeholder="<fmt:message key="search.enterTerms"/>">
					<span class="input-group-btn">
						<button type="submit" name="ricerca" id="button.search" class="btn btn-default">ricerca</button>
					</span>
				</div>
			</form>
		</div> 
	
		<!-- TABELLA -->
		<div class="form-group row">
			<display:table name="ricercaTransfertList" cellspacing="0" cellpadding="0" requestURI=""
	                   defaultsort="" id="rowRicercaTransfert" pagesize="50" class="table table-condensed table-striped" export="false">
				
				<display:column property="id" sortable="true" titleKey="id" />
				<display:column property="visitatore.fullNameRegion" sortable="true" titleKey="visitatore" />
				<display:column property="dataRicerca" format="{0,date,dd/MM/yyyy HH.mm.ss}" sortable="true" titleKey="dataRic" />
		        <display:column property="partenzaRequest" sortable="true" titleKey="partenzaRequest" url="/admin/admin-gestioneRimborsi" paramId="idCorsa" paramProperty="id"/>
		        <display:column property="formattedAddress_Partenza" sortable="true" titleKey="formatPartenza"/>
		        
				<display:column property="dataOraPrelevamentoDate" format="{0,date,dd/MM/yyyy HH.mm.ss}" sortable="true" titleKey="dataPrelev" />
				<display:column property="arrivoRequest" sortable="true" titleKey="arrivoRequest"/>
				<display:column property="formattedAddress_Arrivo" sortable="true" titleKey="formatArrivo"/>
				
				<display:column property="dataOraRitornoDate" format="{0,date,dd/MM/yyyy HH.mm.ss}" sortable="true" titleKey="dataRit" />
				<display:column property="numeroPasseggeri" sortable="true" titleKey="Pass" />
				
				<fmt:formatNumber var="totKm" value="${(rowRicercaTransfert.distanzaValue + rowRicercaTransfert.distanzaValueRitorno) / 1000}" maxFractionDigits="0" />
				<display:column sortable="true" titleKey="TotKm" media="html">
					${totKm}km
       			</display:column>
				
				<c:set var="prezzoTotCliente" value="${(rowRicercaTransfert.pagamentoParziale) ? rowRicercaTransfert.richiestaMediaScelta.prezzoCommissioneServizio : rowRicercaTransfert.richiestaMediaScelta.prezzoTotaleCliente}" />
				<display:column sortable="true" titleKey="prezzoTot">
					${prezzoTotCliente}&euro;
       			</display:column>
				
				<display:column sortable="true" titleKey="rimborsoCliente">
					${rowRicercaTransfert.richiestaMediaScelta.rimborsoCliente}&euro;
       			</display:column>
				<display:column property="user.username" sortable="true" titleKey="cliente" />
				<display:column property="tipoServizio" sortable="true" titleKey="tipoServizio" />
	    	</display:table>
		</div>
	</div>
	
</div>
</div>
</div>

<%@ include file="/scripts/ConfirmSubmit.jsp"%>
</body>