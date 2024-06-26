<%@ include file="/common/taglibs.jsp"%>

<!-- OFFERTE TRANSFERT PART -->
<c:if test="${ricercaTransfert.ricercaRiuscita eq true}">
	<c:set var="RIC_RESUTL_PART" value="${ricercaTransfert.risultatoAutistiParticolare}"/>
	<c:choose>
		<c:when test="${ricercaTransfert.verificatoCustomer || not empty ricercaTransfert.ricTransfert_IdUser || not empty user}">
			<%@ include file="/common/ric_payment_dati_cliente.jsp"%>
			<c:choose>
			<c:when test = "${empty ricercaTransfert.richiestaPreventivi_Inviata}">
				<div class="  ">
				
					<div class="form-group ">
					<p>Scrivi in maniera semplice e sintetica le caratteristiche che richiedi dal servizio, ad esempio: Servizio da Spola, Tappe intermedie, Attese, Disponibilità Oraria, 
					Trasporto Carrozzine, Trasporto Animali, Minimo numero mezzi richiesti (in caso di molti passeggeri o di Eventi).<br>
					<u>Se hai domande o dubbi telefonaci o scrivici</u>. 
					</p>
					</div>
					
					<div class="form-group ">
					<div class=" input-group form-ImpInfo">
						<span class="input-group-addon"><i class="far fa-file-alt"></i></span>
						<textarea class="form-control" rows="6" name="noteAutista" id="noteAutistaId" placeholder="Informazioni importanti per l'Autista">${ricercaTransfert.notePerAutista}</textarea>
					</div>
					</div>
					
					<c:if test="${AMBIENTE_PRODUZIONE}">
					<!-- Event snippet for Richiesta Preventivi conversion page
					In your html page, add the snippet and call gtag_report_conversion when someone clicks on the chosen link or button. -->
					<script>
					function gtag_report_conversion(url) {
					  var callback = function () {
					    if (typeof(url) != 'undefined') {
					      window.location = url;
					    }
					  };
					  gtag('event', 'conversion', {
					      'send_to': 'AW-1011317208/wv6tCKjsvewBENjzneID',
					      'event_callback': callback
					  });
					  console.log('google conversione');
					  return false;
					}
					</script>
					</c:if>
					
					<div class="form-group">
						<button type="submit" onclick="gtag_report_conversion();" id="ricercaID" name="richiedi-preventivi" class="btn btn-success attesaLoader ">
						Richiedi Preventivi <i class="fa fa-arrow-right" ></i> <i class="fa fa-file-text-o" ></i></button>
					</div>
				</div>
				
				<script type="text/javascript">
				$(document).ready(function() {
					$('#ricercaTransfertForm').validator({
						focus: false
					});
					$('#ricercaTransfertForm').validator('validate').has('.has-error').length;
				});
				</script>
				
			</c:when>
			<c:when test = "${not empty ricercaTransfert.richiestaPreventivi_Inviata && ricercaTransfert.richiestaPreventivi_Inviata eq true && richAutistPart == null 
				&& ricercaTransfert.riepilogo eq false}">
				<div class=" ">
					<p><b>ABBIAMO INVIATO LA RICHIESTA PREVENTIVI AI NOSTRI AUTISTI, MOLTO PRESTO RICEVEVERAI VIA SMS ED EMAIL I LORO PREZZI!</b></p>
				</div>
			</c:when>
			</c:choose>
		</c:when>
		<c:otherwise>
		<c:choose>
		<c:when test="${empty ricercaTransfert.ricTransfert_IdUser && not empty RIC_RESUTL_PART.resultParticolare}">
			<c:set var="messaggioPreventivo" value="${numAutisti == 1 ? 'Per Richiedere il Preventivo devi Confermare il tuo Telefono.' : 'Per Richiedere i Preventivi devi Confermare il tuo Telefono.'}"/>
			<div class="div-verify">
				<div class="row head-verify">
					<p><h3><i class="fa fa-info-circle" aria-hidden="true"></i> Riceverai i preventivi in tempo reale al tuo Indirizzo Email e via Sms sul tuo Telefono.</h3></p>
					<p><h3><i class="fa fa-info-circle" aria-hidden="true"></i> I Tuoi dati saranno utilizzati esclusivamente per la Comunicazione dei Preventivi.</h3></p>
					<p><h3><i class="fa fa-info-circle" aria-hidden="true"></i> Se i Dati della Corsa non sono corretti <a style="color:inherit; text-decoration: none;" href="<c:url value='/?courseId=${ricercaTransfert.id}'/>">
						<ins>fai click qui per modificarli.</ins></a></h3></p>
					<p><h3><i class="fa fa-info-circle" aria-hidden="true"></i> ${messaggioPreventivo}</h3></p>
				</div>
			</div>
			<%@ include file="/common/ric_confirm_sms_customer.jsp"%>
		</c:when>
		</c:choose>
		</c:otherwise>
	</c:choose>

	<c:choose>
		<c:when test="${richAutistPart == null && ricercaTransfert.riepilogo eq false}">
			<div class="div-verify">
				<div class="row ">
					<c:set var="numAutisti" value="${fn:length(RIC_RESUTL_PART.autistiEffettivi)}"/>
					<c:set var="numAutoveicoli" value="${fn:length(RIC_RESUTL_PART.resultParticolare)}"/>
					<h3>
					<c:choose>
					<c:when test="${numAutisti == 1}">
						<p><big>C'è ${fn:length(RIC_RESUTL_PART.autistiEffettivi)} Autista con ${numAutoveicoli} Autoveicoli per questa Corsa.</big></p>
					</c:when>
					<c:when test="${numAutisti > 1}">
						<p><big>Ci sono ${fn:length(RIC_RESUTL_PART.autistiEffettivi)} Autisti con ${numAutoveicoli} Autoveicoli per questa Corsa.</big></p>
					</c:when>
					</c:choose>
					</h3>
				</div>
				<div class="row dep-arr-info  ">
					<c:forEach items="${RIC_RESUTL_PART.resultParticolare}" var="varObj">
						<div class="">	
						<h5>
						<span class="prnt-pgr"><!-- firstName -->
						<i class="fa fa-car" aria-hidden="true"></i> <b>${varObj.autoveicolo.autista.user.firstName}, ${varObj.autoveicolo.modelloAutoNumeroPosti.modelloAutoScout.marcaAutoScout.name}, 
						<fmt:message key="${varObj.classeAutoveicoloScelta.nome}"/></b><br>
						<fmt:message key="${varObj.classeAutoveicoloScelta.nome}.desc"/><br>
						<i>${descrizioneCategorieAutoMap[varObj.classeAutoveicoloScelta.id]}</i>
						</span>
						</h5>
						</div>
					</c:forEach>
				</div>
			</div>
		</c:when>
		<c:when test="${not empty richAutistPart && ricercaTransfert.tipoServizio == 'PART'}">
			<div class="div-verify">
				<div class="row head-verify">
					<h3>
					<p><big><b>Classe Autoveicolo: </b><fmt:message key="${richAutistPart.classeAutoveicoloScelta.nome}"/></big>
					<br>${descrizioneCategorieAutoMap[richAutistPart.classeAutoveicoloScelta.id]}</p>
					</h3>
				</div>
				<div class="row row-verify">
					<c:set var = "inizialeCognome" value = "${fn:substring(richAutistPart.autoveicolo.autista.user.lastName, 0, 1)}." />
					<c:choose>
						<c:when test="${not richAutistPart.autoveicolo.autista.azienda && not empty richAutistPart.autoveicolo.autista.autistaDocumento.documentiIscrizioneRuolo}">
							<p><b>Nome Autista: </b>${richAutistPart.autoveicolo.autista.user.firstName}&nbsp;${inizialeCognome}</p>
							<p><b>Iscritto al Ruolo Conducenti di:</b> ${richAutistPart.autoveicolo.autista.autistaDocumento.documentiIscrizioneRuolo.provinciaRuoloConducenti.nomeProvincia}
							<b>&nbsp;Numero Iscrizione:</b> ${richAutistPart.autoveicolo.autista.autistaDocumento.documentiIscrizioneRuolo.numeroRuoloConducenti}
							<b>&nbsp;Data Iscrizione:</b> <fmt:formatDate pattern="dd/MM/yyyy" 
								value="${richAutistPart.autoveicolo.autista.autistaDocumento.documentiIscrizioneRuolo.dataIscrizioneRuoloConducenti}"/></p>
						</c:when>
						<c:when test="${richAutistPart.autoveicolo.autista.azienda}">
							<p><b>Responsabile Aziendale: </b>${richAutistPart.autoveicolo.autista.user.firstName}&nbsp;${inizialeCognome}</p>
						</c:when>
					</c:choose>
					<p><b>Marca e Modello Autoveicolo: </b>${richAutistPart.autoveicolo.marcaModello}&nbsp;<fmt:message key="posti.auto.autista">
						<fmt:param value="${richAutistPart.autoveicolo.modelloAutoNumeroPosti.numeroPostiAuto.numero}"/></fmt:message></p>
					<p><ins><b>Prezzo Totale (Prezzo tutto compreso iva inclusa): <fmt:formatNumber value="${richAutistPart.prezzoTotaleCliente}" pattern="0.00"/>&euro;</b></ins></p>
					<p><ins><b>Tempo validit&agrave; Preventivo (Tempo Massimo Acquisto Corsa): <fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${richAutistPart.preventivo_validita_data_Date}" /></b></ins></p>
				</div>
			</div>
		</c:when>
		<c:when test="${not empty richAutistPart && ricercaTransfert.tipoServizio == 'MULTIP'}">
		<c:forEach items="${richAutistPart.richiestaAutistaParticolareMultiploList}" var="varObj">
			<div class="div-verify">
				<div class="row head-verify">
					<h3>
					<p><big><b>Classe Autoveicolo: </b><fmt:message key="${varObj.classeAutoveicoloScelta.nome}"/></big>
					<br>${descrizioneCategorieAutoMap[varObj.classeAutoveicoloScelta.id]}</p>
					</h3>
				</div>
				<div class="row row-verify">
					<c:set var = "inizialeCognome" value = "${fn:substring(varObj.autoveicolo.autista.user.lastName, 0, 1)}." />
					<c:choose>
						<c:when test="${not varObj.autoveicolo.autista.azienda && not empty varObj.autoveicolo.autista.autistaDocumento.documentiIscrizioneRuolo}">
							<p><b>Nome Autista: </b>${varObj.autoveicolo.autista.user.firstName}&nbsp;${inizialeCognome}</p>
							<p><b>Iscritto al Ruolo Conducenti di:</b> ${varObj.autoveicolo.autista.autistaDocumento.documentiIscrizioneRuolo.provinciaRuoloConducenti.nomeProvincia}
							<b>&nbsp;Numero Iscrizione:</b> ${varObj.autoveicolo.autista.autistaDocumento.documentiIscrizioneRuolo.numeroRuoloConducenti}
							<b>&nbsp;Data Iscrizione:</b> <fmt:formatDate pattern="dd/MM/yyyy" 
								value="${varObj.autoveicolo.autista.autistaDocumento.documentiIscrizioneRuolo.dataIscrizioneRuoloConducenti}"/></p>
						</c:when>
						<c:when test="${varObj.autoveicolo.autista.azienda}">
							<p><b>Responsabile Aziendale: </b>${varObj.autoveicolo.autista.user.firstName}&nbsp;${inizialeCognome}</p>
						</c:when>
					</c:choose>
					<p><b>Marca e Modello Autoveicolo: </b>${varObj.autoveicolo.marcaModello}&nbsp;<fmt:message key="posti.auto.autista">
						<fmt:param value="${varObj.autoveicolo.modelloAutoNumeroPosti.numeroPostiAuto.numero}"/></fmt:message></p>
					<p><ins><b>Prezzo Totale (Prezzo tutto compreso iva inclusa): <fmt:formatNumber value="${varObj.prezzoTotaleCliente}" pattern="0.00"/>&euro;</b></ins></p>
					<p><ins><b>Tempo validit&agrave; Preventivo (Tempo Massimo Acquisto Corsa): <fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${varObj.preventivo_validita_data_Date}" /></b></ins></p>
				</div>
			</div>
		</c:forEach>
		</c:when>
	</c:choose>
	
</c:if>	<!-- FINE OFFERTE TRANSFERT PART -->