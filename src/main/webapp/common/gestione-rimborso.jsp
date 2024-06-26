<%
try {
%>
<div class="well">
	<p><strong>Cliente: </strong>${ricercaTransfert.user.fullName} <strong> Telefono: </strong>${ricercaTransfert.user.phoneNumber} 
	<strong> Email: </strong>${ricercaTransfert.user.email}</p>
	
	<c:if test="${ricercaTransfert.tipoServizio == 'ST' || ricercaTransfert.tipoServizio == 'PART' || ricercaTransfert.tipoServizio == 'MULTIP'}">
		<!-- autistaAssegnatoCorsa -->
		<c:if test="${not empty autistaAssegnatoCorsa}">
			<c:choose>
			<c:when test = "${ricercaTransfert.tipoServizio == 'ST' || ricercaTransfert.tipoServizio == 'PART'}">
				<p><strong>Autista: </strong>${autistaAssegnatoCorsa.user.fullName} <strong> Telefono: </strong>${autistaAssegnatoCorsa.user.phoneNumber} 
				<strong> Email: </strong>${autistaAssegnatoCorsa.user.email}</p>
			</c:when>
			<c:when test = "${ricercaTransfert.tipoServizio == 'MULTIP'}">
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
	<c:if test="${ricercaTransfert.tipoServizio == 'AGA'}">
		<p><strong>Autista Andata: </strong>${agendaAutistaScelta.agendaAutista_AutistaAndata.fullName} <strong> 
			Telefono: </strong>${agendaAutistaScelta.agendaAutista_AutistaAndata.phoneNumber} <strong> Email: </strong>${agendaAutistaScelta.agendaAutista_AutistaAndata.email}</p>
		
		<c:if test="${not empty agendaAutistaScelta.agendaAutista_AutistaRitorno}">
			<p><strong>Autista Ritorno: </strong>${agendaAutistaScelta.agendaAutista_AutistaRitorno.fullName} <strong> 
				Telefono: </strong>${agendaAutistaScelta.agendaAutista_AutistaRitorno.phoneNumber} <strong> Email: </strong>${agendaAutistaScelta.agendaAutista_AutistaRitorno.email}</p>	
		</c:if>
	</c:if>
	
	<p><strong>Id Corsa:</strong> ${ricercaTransfert.id} 
	
	<!--richiestaMediaScelta oppure prezzoCorsaCliente -->
	<c:choose>
	<c:when test = "${ricercaTransfert.tipoServizio == 'ST'}">
		<c:choose>
		<c:when test = "${ricercaTransfert.pagamentoParziale eq false}">
			<strong> Prezzo Corsa Cliente: </strong><fmt:formatNumber value="${ricercaTransfert.richiestaMediaScelta.prezzoTotaleCliente}" pattern="0.00" />&euro;
		</c:when>
		<c:when test = "${ricercaTransfert.pagamentoParziale eq true}">
			<strong> Prezzo Corsa Cliente: </strong><fmt:message key="prezzo.cliente.parziale.piu.prezzo.autista">
				<fmt:param value="${ricercaTransfert.richiestaMediaScelta.prezzoCommissioneServizio}"/>
				<fmt:param value="${ricercaTransfert.richiestaMediaScelta.prezzoTotaleAutista}"/>
			</fmt:message>
		</c:when>
		</c:choose>
		<c:if test = "${not empty ricercaTransfert.richiestaMediaScelta.maggiorazioneNotturna}">
			<fmt:message key="prezzo.autista.inclusa.maggiorazione.notturna">
				<fmt:param value="${ricercaTransfert.richiestaMediaScelta.maggiorazioneNotturna}"/>
			</fmt:message>
		</c:if>
	</c:when>
	<c:when test = "${ricercaTransfert.tipoServizio == 'PART' || ricercaTransfert.tipoServizio == 'MULTIP'}">
		<strong> Prezzo Corsa Cliente: </strong><fmt:formatNumber value="${prezzoCorsaCliente}" pattern="0.00" />&euro;
	</c:when>
	</c:choose>
	
	<strong> Totale Rimborsato: </strong><fmt:formatNumber pattern="0.00" minFractionDigits="2" 
		value="${(ricercaTransfert.richiestaMediaScelta.rimborsoCliente != null) ? ricercaTransfert.richiestaMediaScelta.rimborsoCliente : 0 }" />&euro;<br>
	
	<strong>Partenza:</strong> ${ricercaTransfert.formattedAddress_Partenza} 
	<strong> Giorno Ora Prelevamento:</strong> <fmt:formatDate pattern="dd MMMM yyyy, HH:mm" value="${ricercaTransfert.dataOraPrelevamentoDate}" /><br>
	<strong> Arrivo:</strong> ${ricercaTransfert.formattedAddress_Arrivo} 
	<c:if test="${ricercaTransfert.ritorno}">
		<strong> Giorno Ora Prelevamento:</strong> <fmt:formatDate pattern="dd MMMM yyyy, HH:mm" value="${ricercaTransfert.dataOraRitornoDate}" />
	</c:if><br>
	<strong> ${ricercaTransfert.ritorno ? 'ANDATA e RITORNO' : 'SOLO ANDATA'}</strong></p>
	<!-- panel info payment -->
	<div class="panel panel-info">
	<div class="panel-heading"><strong>${TypePaymentProvider != null && TypePaymentProvider == 'STRIPE' ? '<span class="fa fa-credit-card fa-2x"></span>' : ''}
			${TypePaymentProvider != null && TypePaymentProvider == 'PAYPAL' ? '<span class="fa fa-cc-paypal fa-2x"></span>' : ''}<big> Informazioni Provider Pagamento</big></strong></div>
	<div class="panel-body">
		<ul class="list-group">
		  	<li class="list-group-item"><strong>Nome Pagatore: </strong>${NomeClienteByProvider}</li>
		  	<li class="list-group-item"><strong>Totale Lordo: </strong><fmt:formatNumber pattern="0.00" minFractionDigits="2" 
					value="${(AmountByProvider != null) ? AmountByProvider : 0 }" />&euro;</li>
			<li class="list-group-item"><strong>Totale Netto: </strong><fmt:formatNumber pattern="0.00" minFractionDigits="2" 
					value="${(AmountByProviderNetto != null) ? AmountByProviderNetto : 0 }" />&euro;</li>
			<li class="list-group-item"><strong>Commissione Servizio Provider: </strong><fmt:formatNumber pattern="0.00" minFractionDigits="2" 
					value="${(AmountByProviderFee != null) ? AmountByProviderFee : 0 }" />&euro;</li>
			<li class="list-group-item"><strong>Totale Rimborsato: </strong><fmt:formatNumber pattern="0.00" minFractionDigits="2" 
					value="${(AmountRimborsoByProvider != null) ? AmountRimborsoByProvider : 0 }" />&euro;</li>
		</ul>
	</div>
	<div class="panel-footer"><fmt:message key='payment.info'/></div>
	</div>
</div>
<%		 
}
catch (Exception e) {
out.println("An exception occurred: " + e.getMessage());
}
%>