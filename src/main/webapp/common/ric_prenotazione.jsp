<c:if test="${not empty ricercaTransfert && ricercaTransfert.ricercaRiuscita eq true}">

</c:if>
<div ${ricercaTransfert.verificatoCustomer ? 'style="display: true;"' : ''}>
	<div class="div-verify">
		<c:if test="${ricercaTransfert.tipoServizio == 'AGA'}">
			<c:set var="AGENDA_AUTISTA_SCELTA" value="${ricercaTransfert.agendaAutistaScelta}"/>
			<div class="row head-verify">
			<h3>
			<c:if test="${not empty AGENDA_AUTISTA_SCELTA.agendaAutista_AutistaAndata}">
				<p><big><b>ANDATA - Autista: ${AGENDA_AUTISTA_SCELTA.agendaAutista_AutistaAndata.firstName}, 
				Prezzo: ${AGENDA_AUTISTA_SCELTA.agendaAutista_AutistaAndata.prezzoCliente}&euro;,
				Classe Autoveicolo: </b><fmt:message key="${AGENDA_AUTISTA_SCELTA.agendaAutista_AutistaAndata.classeAutoveicoloReale.nome}"/></big>
				<br>${descrizioneCategorieAutoMap[AGENDA_AUTISTA_SCELTA.agendaAutista_AutistaAndata.classeAutoveicoloReale.id]}</p>
			</c:if>
			<c:if test="${not empty AGENDA_AUTISTA_SCELTA.agendaAutista_AutistaRitorno}">
				<p><big><b>RITORNO - Autista: ${AGENDA_AUTISTA_SCELTA.agendaAutista_AutistaRitorno.firstName},
				Prezzo: ${AGENDA_AUTISTA_SCELTA.agendaAutista_AutistaRitorno.prezzoCliente}&euro;,
				Classe Autoveicolo: </b><fmt:message key="${AGENDA_AUTISTA_SCELTA.agendaAutista_AutistaRitorno.classeAutoveicoloReale.nome}"/></big>
				<br>${descrizioneCategorieAutoMap[AGENDA_AUTISTA_SCELTA.agendaAutista_AutistaRitorno.classeAutoveicoloReale.id]}</p>
			</c:if>
			</h3>
			</div>
			<div class="row row-verify">
				<p><b>Prezzo Totale: <fmt:formatNumber value="${AGENDA_AUTISTA_SCELTA.prezzoTotaleCliente}" pattern="0.00"/>&euro;</b></p>
				<p>Prezzo tutto compreso iva inclusa</p>
			</div>
		</c:if>
		<c:if test="${ricercaTransfert.tipoServizio == 'ST'}">
		<div class="row head-verify">
		<h3>
			<p><big><b>Classe Autoveicolo: </b><fmt:message key="${ricercaTransfert.richiestaMediaScelta.classeAutoveicolo.nome}"/></big>
			<br>${descrizioneCategorieAutoMap[ricercaTransfert.richiestaMediaScelta.classeAutoveicolo.id]}</p>
		</h3>
		</div>
		<div class="row row-verify">
			<c:choose>
				<c:when test = "${ricercaTransfert.pagamentoParziale eq false}">
					<p><b>Prezzo Totale: <fmt:formatNumber value="${ricercaTransfert.richiestaMediaScelta.prezzoTotaleCliente}" pattern="0.00"/>&euro;</b></p>
				</c:when>
				<c:when test = "${ricercaTransfert.pagamentoParziale eq true}">
					<p><b>Prezzo: <fmt:message key="prezzo.cliente.parziale.piu.prezzo.autista">
						<fmt:param value="${ricercaTransfert.richiestaMediaScelta.prezzoCommissioneServizio}"/>
						<fmt:param value="${ricercaTransfert.richiestaMediaScelta.prezzoTotaleAutista}"/>
					</fmt:message></b></p>
				</c:when>
			</c:choose>
			<!-- Non lo faccio vedere al Cliente 
			<p>Tariffa per km: <fmt:formatNumber value="${ricercaTransfert.richiestaMediaScelta.tariffaPerKm}" pattern="0.00"/>&euro;/km</p> -->
			<c:if test="${ricercaTransfert.scontoRitorno eq true}">
			<p>Sconto Ritorno entro ${numMaxOreAttesaRitorno} ore: ${percentualeScontoRitorno}%</p>
			</c:if>
			<c:if test="${not empty ricercaTransfert.richiestaMediaScelta.maggiorazioneNotturna}">
			<p>Maggiorazione Notturna ${ricercaTransfert.richiestaMediaScelta.maggiorazioneNotturna}&euro;</p> 
			</c:if>
			<p>Prezzo tutto compreso iva inclusa</p>
		</div>
		</c:if>
	</div>
	
</div> <!-- FINE DIV DISPLAY = TRUE | FALSE -->