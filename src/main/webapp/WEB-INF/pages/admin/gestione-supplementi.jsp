<%@ include file="/common/taglibs.jsp"%>
<head>
    <title>Gestione Supplementi</title>
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
<h3 style="margin-top: -20px;">Gestione Supplementi</h3>
<%@ include file="/common/messages.jsp" %>
<%
try {
%>
<c:if test="${not empty gestioneSupplementi}">
	<c:if test="${modifica}">
		<c:set var="totalePrezzo" value="${gestioneSupplementi.prezzo}"/>
		<div class="well">
			<p><strong>Cliente: </strong>${gestioneSupplementi.ricercaTransfert.user.fullName} <strong> Telefono: </strong>${gestioneSupplementi.ricercaTransfert.user.phoneNumber} 
			<strong> Email: </strong>${gestioneSupplementi.ricercaTransfert.user.email} <strong> Costo Supplemento</strong> ${totalePrezzo}&euro;</p>
			
			<c:if test = "${gestioneSupplementi.ricercaTransfert.tipoServizio == 'ST' || gestioneSupplementi.ricercaTransfert.tipoServizio == 'PART' || gestioneSupplementi.ricercaTransfert.tipoServizio == 'MULTIP'}">
				<c:if test="${not empty autistaAssegnatoCorsa}">
					<c:choose>
					<c:when test = "${gestioneSupplementi.ricercaTransfert.tipoServizio == 'ST' || gestioneSupplementi.ricercaTransfert.tipoServizio == 'PART'}">
						<p><strong>Autista: </strong>${autistaAssegnatoCorsa.user.fullName} <strong> Telefono: </strong>${autistaAssegnatoCorsa.user.phoneNumber} 
						<strong> Email: </strong>${autistaAssegnatoCorsa.user.email}</p>
					</c:when>
					<c:when test = "${gestioneSupplementi.ricercaTransfert.tipoServizio == 'MULTIP'}">
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
			<c:if test="${gestioneSupplementi.ricercaTransfert.tipoServizio == 'AGA'}">
				<p><strong>Autista Andata: </strong>${agendaAutistaScelta.agendaAutista_AutistaAndata.fullName} <strong> 
					Telefono: </strong>${agendaAutistaScelta.agendaAutista_AutistaAndata.phoneNumber} <strong> Email: </strong>${agendaAutistaScelta.agendaAutista_AutistaAndata.email}</p>
				<c:if test="${not empty agendaAutistaScelta.agendaAutista_AutistaRitorno}">
					<p><strong>Autista Ritorno: </strong>${agendaAutistaScelta.agendaAutista_AutistaRitorno.fullName} <strong> 
						Telefono: </strong>${agendaAutistaScelta.agendaAutista_AutistaRitorno.phoneNumber} <strong> Email: </strong>${agendaAutistaScelta.agendaAutista_AutistaRitorno.email}</p>	
				</c:if>
			</c:if>
			
			
			<p><strong> Id Corsa:</strong> ${gestioneSupplementi.ricercaTransfert.id}
			<strong> Prezzo Corsa Cliente:</strong> ${prezzoCorsaCliente}&euro;
			
			<c:if test = "${not empty gestioneSupplementi.ricercaTransfert.richiestaMediaScelta.maggiorazioneNotturna}">
			<fmt:message key="prezzo.autista.inclusa.maggiorazione.notturna">
				<fmt:param value="${gestioneSupplementi.ricercaTransfert.richiestaMediaScelta.maggiorazioneNotturna}"/>
			</fmt:message>
			</c:if>
			<br> 
			<strong> Partenza:</strong> ${gestioneSupplementi.ricercaTransfert.formattedAddress_Partenza} 
			<strong> Giorno:</strong> <fmt:formatDate pattern="HH:mm, dd MMMM, yyyy" value="${gestioneSupplementi.ricercaTransfert.dataOraPrelevamentoDate}" /><br>
			<strong> Arrivo:</strong> ${gestioneSupplementi.ricercaTransfert.formattedAddress_Arrivo} 
			<c:if test="${gestioneSupplementi.ricercaTransfert.ritorno}">
				<strong> Giorno:</strong> <fmt:formatDate pattern="HH:mm, dd MMMM, yyyy" value="${gestioneSupplementi.ricercaTransfert.dataOraRitornoDate}" />
			</c:if><br>
			
			<strong> ${gestioneSupplementi.ricercaTransfert.ritorno ? 'ANDATA e RITORNO' : 'SOLO ANDATA'}</strong></p>
			<p><big><ins><strong>SOLLECITI INVITATI: ${gestioneSupplementi.fatture.numeroSollecitiInviatiRitardo}</strong></ins></big></p>
		</div>
	</c:if>
	<form:form commandName="gestioneSupplementi" method="post" action="admin-gestioneSupplementi" id="gestioneSupplementiForm" autocomplete="off" onsubmit="">
	<form:hidden path="id"/>
	<form:hidden path="ricercaTransfert.id"/> <!-- mi server per risalire al transfert ID nel controller -->
	<div class="well">
		<div class="row form-group">
		
			<c:if test="${!modifica}">
				<div class="">
					<label class="col-sm-1 form-control-label">ricercaTransfert</label>
					<div class="col-sm-2">
						<input type="text" name="ricercaTransfertId" class="form-control"> 
					</div>
				</div>
			</c:if>
			<div class="">
				<label class="col-sm-1 form-control-label">Descrizione</label>
				<div class="col-sm-5">
					<form:textarea path="descrizione" class="form-control" rows="6" />
				</div>
			</div>
			<div class="">
				<label class="col-sm-1 form-control-label">Prezzo</label>
				<div class="col-sm-2">
					<form:input path="prezzo" class="form-control" />
				</div>
			</div>
			<c:if test="${modifica}">
				<div class="col-sm-3">
					<p class="${not gestioneSupplementi.pagato ? 'text-danger' : 'text-success'}">
					<strong>${not gestioneSupplementi.pagato ? 'Pagamento Non Eseguito' : 'Pagamento Eseguito'}</strong></p>
				</div>
			</c:if>
		</div>
		<!-- pulsanti -->
		<div class="form-group row ">
			<div class=" pull-right">
				<button type="submit" name="cancel" class="btn btn-md btn-default">Annulla </button>
				<c:choose>
				    <c:when test="${modifica}">
				    	<button type="submit" name="modifica" class="btn btn-md btn-success">
				    		Modifica Supplemento <span class="fa fa-pencil"></span></button>
				    	<button type="submit" name="elimina" class="btn btn-md btn-danger">
				    		Elimina Supplemento <span class="fa fa-trash"></span></button>
						<button type="button" id="${gestioneSupplementi.id}" class="btn btn-md btn-info checkFatturaSupplementoDisponibile">
							Scarica Fattura Supplemento <span class="glyphicon glyphicon-download-alt"></span> <span class="glyphicon glyphicon-time"></span></button>
						<button type="button" name="invia-sollecito" class="btn btn-md btn-warning alertConfirmGenerale" ${not gestioneSupplementi.pagato ? '' : 'disabled'}>
							Invia Sollecito <span class="fa fa-envelope"></span> <span class="fa fa-mobile"></span> <span class="fa fa-bell"></span></button>
				    </c:when>    
				    <c:otherwise>
						<button type="submit" name="aggiungi" class="btn btn-md btn-success">Salva Supplemento <span class="fa fa-pencil"></span></button>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
	</div> 
	</form:form>
</c:if>

<!-- TABELLA SOTTO -->
<div class="col-sm-12">
	<div class="form-group row">
		<form method="get" action="${ctx}/admin/admin-gestioneSupplementi" id="searchForm" class="form-inline" role="form">
	 		<div class="form-group">
				<div class="input-group">
					<label class="sr-only" for="query">ricerca</label>
					<input type="text" name="q" class="form-control" size="30" id="query" value="${param.q}" placeholder="<fmt:message key="search.enterTerms"/>">
					<span class="input-group-btn">
						<button type="submit" name="ricerca" id="button.search" class="btn btn-default">ricerca</button>
					</span>
				</div><!-- /input-group -->
				
		 		<button type="submit" class="btn btn-primary">Nuovo Supplemento <span class="fa fa-plus"></span></button>
	 		</div>
		</form>
	</div> <!-- fine row  -->

	<!-- TABELLA -->
	<div class="form-group row">
		<display:table name="gestioneSupplementiList" cellspacing="0" cellpadding="0" requestURI=""
                   defaultsort="" id="rowSupplemento" pagesize="50" class="table table-condensed table-striped" export="false">
			
			<display:column property="ricercaTransfert.id" sortable="true" titleKey="ID" />
			
			<display:column property="fatture.numeroSollecitiInviatiRitardo" sortable="true" titleKey="NumSolleciti" /> 

			<display:column sortable="true" titleKey="ricercaTransfert" media="html">
				<a href="${ctx}/admin/admin-gestioneSupplementi?idSupplementi=${rowSupplemento.id}">
				${rowSupplemento.ricercaTransfert.formattedAddress_Partenza} -> ${rowSupplemento.ricercaTransfert.formattedAddress_Arrivo}</a>
      		</display:column>
			
			<display:column property="prezzo" sortable="true" titleKey="prezzo" />
			
			<display:column sortable="true" titleKey="pagato" media="html">
          		${rowSupplemento.pagato} 
      		</display:column>
      		
      		<display:column sortable="true" titleKey="descrizione" media="html">
          		${rowSupplemento.descrizione} 
      		</display:column>
      		
      		<display:column property="ricercaTransfert.tipoServizio" sortable="true" titleKey="tipoServizio" />
      		
    	</display:table>
	</div>
</div>
<%		 
}
catch (Exception e) {
out.println("An exception occurred: " + e.getMessage());
}
%>
</div>
</div>
</div>
<%@ include file="/scripts/ConfirmSubmit.jsp"%>

</body>