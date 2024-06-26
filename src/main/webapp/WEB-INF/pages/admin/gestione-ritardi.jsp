<%@ include file="/common/taglibs.jsp"%>

<head>
    <title>Gestione Ritardi</title>
	<!-- jquery -->
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
<h3 style="margin-top: -20px;">Gestione Ritardi</h3>
<%@ include file="/common/messages.jsp" %>
<c:if test="${not empty gestioneRitardi}">
	<c:set var="totalePrezzoRitardi" value="${gestioneRitardi.prezzoAndata.add(gestioneRitardi.prezzoRitorno)}"/>
	<div class="well">
		<p><strong>Cliente: </strong>${gestioneRitardi.ricercaTransfert.user.fullName} <strong> Telefono: </strong>${gestioneRitardi.ricercaTransfert.user.phoneNumber} 
		<strong> Email: </strong>${gestioneRitardi.ricercaTransfert.user.email} <strong> Costo Totale Ritardi A/R</strong> ${totalePrezzoRitardi}&euro;</p>

		<c:if test = "${gestioneRitardi.ricercaTransfert.tipoServizio == 'ST' || gestioneRitardi.ricercaTransfert.tipoServizio == 'PART' || gestioneRitardi.ricercaTransfert.tipoServizio == 'MULTIP'}">
			<c:if test="${not empty autistaAssegnatoCorsa}">
				<c:choose>
				<c:when test = "${gestioneRitardi.ricercaTransfert.tipoServizio == 'ST' || gestioneRitardi.ricercaTransfert.tipoServizio == 'PART'}">
					<p><strong>Autista: </strong>${autistaAssegnatoCorsa.user.fullName} <strong> Telefono: </strong>${autistaAssegnatoCorsa.user.phoneNumber} 
					<strong> Email: </strong>${autistaAssegnatoCorsa.user.email}</p>
				</c:when>
				<c:when test = "${gestioneRitardi.ricercaTransfert.tipoServizio == 'MULTIP'}">
					<c:forEach items="${autistaAssegnatoCorsa}" var="varObj" varStatus="loop">
						<p><strong>Autista ${loop.index + 1}: </strong>${varObj.user.fullName} <strong> Telefono: </strong>${varObj.user.phoneNumber} 
						<strong> Email: </strong>${varObj.user.email}</p>
	      			</c:forEach>
				</c:when>
				</c:choose>
			</c:if>
			<c:if test="${empty autistaAssegnatoCorsa}">
				<p class="text-danger"><strong>Autista non assegnato alla Corsa</strong></p>
			</c:if>
		</c:if>
		<c:if test="${gestioneRitardi.ricercaTransfert.tipoServizio == 'AGA'}">
			<p><strong>Autista Andata: </strong>${agendaAutistaScelta.agendaAutista_AutistaAndata.fullName} <strong> 
				Telefono: </strong>${agendaAutistaScelta.agendaAutista_AutistaAndata.phoneNumber} <strong> Email: </strong>${agendaAutistaScelta.agendaAutista_AutistaAndata.email}</p>
			<c:if test="${not empty agendaAutistaScelta.agendaAutista_AutistaRitorno}">
				<p><strong>Autista Ritorno: </strong>${agendaAutistaScelta.agendaAutista_AutistaRitorno.fullName} <strong> 
					Telefono: </strong>${agendaAutistaScelta.agendaAutista_AutistaRitorno.phoneNumber} <strong> Email: </strong>${agendaAutistaScelta.agendaAutista_AutistaRitorno.email}</p>	
			</c:if>
		</c:if>
		
		
		
		<p><strong> Id Corsa:</strong> ${gestioneRitardi.ricercaTransfert.id}
		<strong> Prezzo Corsa Cliente:</strong> ${prezzoCorsaCliente}&euro;
		<c:if test = "${not empty gestioneRitardi.ricercaTransfert.richiestaMediaScelta.maggiorazioneNotturna}">
		<fmt:message key="prezzo.autista.inclusa.maggiorazione.notturna">
			<fmt:param value="${gestioneRitardi.ricercaTransfert.richiestaMediaScelta.maggiorazioneNotturna}"/>
		</fmt:message>
		</c:if>
		<br> 
		<strong> Partenza:</strong> ${gestioneRitardi.ricercaTransfert.formattedAddress_Partenza} 
		<strong> Giorno:</strong> <fmt:formatDate pattern="HH:mm, dd MMMM, yyyy" value="${gestioneRitardi.ricercaTransfert.dataOraPrelevamentoDate}" /><br>
		<strong> Arrivo:</strong> ${gestioneRitardi.ricercaTransfert.formattedAddress_Arrivo} 
		<c:if test="${gestioneRitardi.ricercaTransfert.ritorno}">
			<strong> Giorno:</strong> <fmt:formatDate pattern="HH:mm, dd MMMM, yyyy" value="${gestioneRitardi.ricercaTransfert.dataOraRitornoDate}" />
		</c:if><br>
		
		<strong> ${gestioneRitardi.ricercaTransfert.ritorno ? 'ANDATA e RITORNO' : 'SOLO ANDATA'}</strong></p>
		<p><big><ins><strong>SOLLECITI INVITATI: ${gestioneRitardi.fatture.numeroSollecitiInviatiRitardo}</strong></ins></big></p>
	</div>
	
	<form:form commandName="gestioneRitardi" method="post" action="admin-gestioneRitardi" id="gestioneRitardiForm" autocomplete="off" onsubmit="">
	<form:hidden path="id"/>
	<form:hidden path="ricercaTransfert.id"/> <!-- mi server per risalire al transfert ID nel controller -->
	<div class="well">
		<div class="row form-group">
			<div class="">
				<label class="col-sm-2 form-control-label">Numero Mezzore Andata</label>
				<div class="col-sm-2">
					<form:input path="numeroMezzoreRitardiAndata" class="form-control" />
				</div>
			</div>
			<div class="">
				<label class="col-sm-2 form-control-label">Prezzo Calcolato Andata</label>
				<div class="col-sm-2">
					<form:input path="prezzoAndata" class="form-control" disabled="true" />
				</div>
			</div>
			<div class="col-sm-3">
				<p class="${not gestioneRitardi.pagatoAndata || not gestioneRitardi.pagatoRitorno ? 'text-danger' : 'text-success'}">
				<strong>${not gestioneRitardi.pagatoAndata || not gestioneRitardi.pagatoRitorno ? 'Pagamento Non Eseguito' : 'Pagamento Eseguito'}</strong></p>
			</div>
		</div>
		<c:if test="${gestioneRitardi.ricercaTransfert.ritorno}">
			<div class="row form-group">
				<div class="">
					<label class="col-sm-2 form-control-label">Numero Mezzore Ritorno</label>
					<div class="col-sm-2">
						<form:input path="numeroMezzoreRitardiRitorno" class="form-control" />
					</div>
				</div>
				<div class="">
					<label class="col-sm-2 form-control-label">Prezzo Calcolato Ritorno</label>
					<div class="col-sm-2">
						<form:input path="prezzoRitorno" class="form-control" disabled="true" />
					</div>
				</div>
				<div class="">

				</div>
			</div>
		</c:if>
		
		<!-- pulsanti -->
		<div class="form-group row ">
			<div class=" pull-right">
				<button type="submit" name="cancel" class="btn btn-default">Annulla <span class="fa fa-ban"></span></button>
				
				<button type="submit" name="modifica" class="btn btn-success">Modifica Ritardo <span class="fa fa-pencil"></span></button>

				<button type="button" id="${gestioneRitardi.ricercaTransfert.id}" class="btn btn-info checkFatturaRitardoDisponibile">
					Scarica Fattura Ritardo <span class="glyphicon glyphicon-download-alt"></span> <span class="glyphicon glyphicon-time"></span></button>
				
				<button type="button" name="invia-sollecito" class="btn btn-warning alertConfirmGenerale" ${not gestioneRitardi.pagatoAndata || not gestioneRitardi.pagatoRitorno ? '' : 'disabled'}>
					Invia Sollecito <span class="fa fa-envelope"></span> <span class="fa fa-mobile"></span> <span class="fa fa-bell"></span></button>
			</div>
		</div>
	</div> 
	</form:form>
</c:if>

<div class="col-sm-12">
	<div class="form-group row">
		<form method="get" action="${ctx}/admin/admin-gestioneRitardi" id="searchForm" class="form-inline" role="form">
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
		<display:table name="gestioneRitardiList" cellspacing="0" cellpadding="0" requestURI=""
                   defaultsort="" id="rowRitardo" pagesize="50" class="table table-condensed table-striped" export="false">
			
			<display:column property="ricercaTransfert.id" sortable="true" titleKey="ID" />
			<display:column property="fatture.numeroSollecitiInviatiRitardo" sortable="true" titleKey="NumSolleciti" /> 
			
			<display:column property="ricercaTransfert.formattedAddress_Partenza" sortable="true" titleKey="Andata" url="/admin/admin-gestioneRitardi" 
	        	paramId="idRitardi" paramProperty="id" />
			<display:column property="numeroMezzoreRitardiAndata" sortable="true" titleKey="NumMezzoreA" />  
			<display:column property="prezzoAndata" sortable="true" titleKey="prezzoA" />  
			<display:column sortable="true" titleKey="pagatoA" media="html">
          		${rowRitardo.numeroMezzoreRitardiAndata == 0 or rowRitardo.pagatoAndata ? '<div class="text-success h4"><strong>OK</strong></div>' : '<div class="text-danger h4"><strong>NO</strong></div>'} 
      		</display:column>
      			
			<display:column property="ricercaTransfert.formattedAddress_Arrivo" sortable="true" titleKey="Ritorno" url="/admin/admin-gestioneRitardi" 
	        	paramId="idRitardi" paramProperty="id" />
			<display:column property="numeroMezzoreRitardiRitorno" sortable="true" titleKey="NumMezzoreR" />
			<display:column property="prezzoRitorno" sortable="true" titleKey="prezzoR" /> 
       		<display:column sortable="true" titleKey="pagatoR" media="html" >
           		${rowRitardo.numeroMezzoreRitardiRitorno == 0 or rowRitardo.pagatoRitorno ? '<div class="text-success h4"><strong>OK</strong></div>' : '<div class="text-danger h4"><strong>NO</strong></div>'} 
       		</display:column>
       		<display:column property="ricercaTransfert.tipoServizio" sortable="true" titleKey="tipoRitardo" />
       		
    	</display:table>
	</div>
</div>

</div>
</div>
</div>
<%@ include file="/scripts/ConfirmSubmit.jsp"%>

</body>