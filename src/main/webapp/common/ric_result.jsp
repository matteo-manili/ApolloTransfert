<c:if test="${ricercaTransfert.richiestaMediaScelta == null && ricercaTransfert.ricercaRiuscita eq true}">
	<c:if test="${false && (smsSkebbyAbilitato == false || stripeSWITCH == false || payPalSWITCH == false) }">
		<div class="alert alert-success h4" role="alert">
			<b><p><big>Attenzione!</big> Apollotransfert &egrave; in fase di test, le funzioni di prenotazione e di pagamento sono inattive.<br>
				Grazie per l'attenzione, presto saremo on-line!</p></b>
			<hr>
			<b><p><big>Warning!</big> Apollotransfert is under test, booking and payment functions are inactive.<br>
				Thanks for your attention, we will soon be online!</p></b>
		</div>
	</c:if>
</c:if>

<!-- DATI RICERCA -->
<c:if test="${not empty ricercaTransfert && ricercaTransfert.ricercaRiuscita eq true}">
	<%@ include file="/common/ric_result_dati_ricerca.jsp"%>
</c:if>

<!-- OFFERTE TRANSFERT ST -->
<c:if test="${ricercaTransfert.ricercaRiuscita eq true && ricercaTransfert.prenotazione eq false && ricercaTransfert.tipoServizio == 'ST'}">
	<c:if test="${AMBIENTE_PRODUZIONE}">
	<!-- Event snippet for Button Acquista Corsa ST - AGA conversion page
	In your html page, add the snippet and call gtag_report_conversion when someone clicks on the chosen link or button. -->
	<script>
	function gtag_report_conversion(url) {
	  var callback = function () {
	    if (typeof(url) != 'undefined') {
	      window.location = url;
	    }
	  };
	  gtag('event', 'conversion', {
	      'send_to': 'AW-1011317208/1olXCLf9q-wBENjzneID',
	      'event_callback': callback
	  });
	  console.log('google conversione');
	  return false;
	}
	</script>
	</c:if>

	<div class="form-steps">
	<c:set var="RIS_AGENDA_AUTISTA" value="${ricercaTransfert.resultAgendaAutista}"/>
	<c:if test="${not empty RIS_AGENDA_AUTISTA.agendaAutista_AutistaAndata || not empty RIS_AGENDA_AUTISTA.agendaAutista_AutistaRitorno}">
		<h3><u><b>PREZZI AUTISTI - I nostri Autisti hanno comunicato i loro prezzi</b></u></h3>
		<c:set var = "textAcquistaCorsaAgendaAutista" value = "Acquista la Corsa"/>
		<c:choose>
		<c:when test="${ricercaTransfert.ritorno && empty RIS_AGENDA_AUTISTA.agendaAutista_AutistaRitorno}">
			<c:set var = "textAcquistaCorsaAgendaAutista" value = "Acquista la Corsa (SOLO ANDATA)"/>
		</c:when>
		<c:when test="${ricercaTransfert.ritorno && empty RIS_AGENDA_AUTISTA.agendaAutista_AutistaAndata}">
			<c:set var = "textAcquistaCorsaAgendaAutista" value = "Acquista la Corsa (SOLO RITORNO)"/>
		</c:when>
		</c:choose>
		<div class="div-sel-car text-center">
			<div class="row head-sel-car">
			<h4>
			<p><b><c:choose><c:when test="${not empty ricercaTransfert.formattedAddress_Partenza}">${ricercaTransfert.formattedAddress_Partenza}</c:when>
			<c:when test="${empty ricercaTransfert.formattedAddress_Partenza}">${ricercaTransfert.name_Partenza}</c:when></c:choose>
			&#x2192;&nbsp;<c:choose><c:when test="${not empty ricercaTransfert.formattedAddress_Arrivo}">${ricercaTransfert.formattedAddress_Arrivo}</c:when>
			<c:when test="${empty ricercaTransfert.formattedAddress_Arrivo}">${ricercaTransfert.name_Arrivo}</c:when></c:choose></b></p>
			<c:choose>
	  		<c:when test="${ fn:length(RIS_AGENDA_AUTISTA.agendaAutista_AutistaAndata) gt 0}">
			<c:forEach items="${RIS_AGENDA_AUTISTA.agendaAutista_AutistaAndata}" var="varObj" varStatus="status">
				<div class="radio">
				<label><input type="radio" name="radio_AgendaAutistaResultAndata" id="${varObj.idTariffario}" value="${varObj.prezzoCliente}" ${status.first ? 'checked' : ''}>
					${varObj.firstName} - ${varObj.nomeModelloAuto} (${varObj.nomeMarcaAuto})  - <fmt:message key="${varObj.classeAutoveicoloReale.nome}"/> 
						- Num. posti passeggeri: ${varObj.numeroPostiAutoveicolo} - <fmt:formatNumber value="${varObj.prezzoCliente}" pattern="0.00"/>&euro;</label>
				</div>
			</c:forEach>
			</c:when>
			<c:otherwise>
				<div class="radio">
				<label><input type="radio" name="radio_AgendaAutistaResultAndata" id="null" value="0" checked>
				<i>Non ci sono Autisti disponbili per la corsa di Andata</i></label></div>
				<!-- <p><u><i>Non ci sono Autisti disponbili per la corsa di Andata</i></u></p> -->
	  		</c:otherwise>
			</c:choose>
			
			<c:if test="${ricercaTransfert.ritorno}">
				<p><b><c:choose><c:when test="${not empty ricercaTransfert.formattedAddress_Arrivo}">${ricercaTransfert.formattedAddress_Arrivo}</c:when>
				<c:when test="${empty ricercaTransfert.formattedAddress_Arrivo}">${ricercaTransfert.name_Arrivo}</c:when></c:choose>
				&#x2192;&nbsp;<c:choose><c:when test="${not empty ricercaTransfert.formattedAddress_Partenza}">${ricercaTransfert.formattedAddress_Partenza}</c:when>
				<c:when test="${empty ricercaTransfert.formattedAddress_Partenza}">${ricercaTransfert.name_Partenza}</c:when></c:choose></b></p>
				<c:choose>
	  			<c:when test="${ fn:length(RIS_AGENDA_AUTISTA.agendaAutista_AutistaRitorno) gt 0}">
				<c:forEach items="${RIS_AGENDA_AUTISTA.agendaAutista_AutistaRitorno}" var="varObj" varStatus="status">
				<div class="radio">
				<label><input type="radio" name="radio_AgendaAutistaResultRitorno" id="${varObj.idTariffario}" value="${varObj.prezzoCliente}" ${status.first ? 'checked' : ''}>
					${varObj.firstName} - ${varObj.nomeMarcaAuto} - ${varObj.nomeModelloAuto} - <fmt:message key="${varObj.classeAutoveicoloReale.nome}"/> 
						- Numero Posti Passeggeri: ${varObj.numeroPostiAutoveicolo} - <fmt:formatNumber value="${varObj.prezzoCliente}" pattern="0.00"/>&euro;</label>
				</div>
				</c:forEach>
				</c:when>
				<c:otherwise>
					<div class="radio">
					<label><input type="radio" name="radio_AgendaAutistaResultRitorno" id="null" value="0" checked>
					<i>Non ci sono Autisti disponbili per la corsa di Ritorno</i></label></div>
					<!-- <p><u><i>Non ci sono Autisti disponbili per la corsa di Ritorno</i></u></p>  -->
	  			</c:otherwise>
				</c:choose>
			</c:if>
			</h4>
			</div>
			<div class="row sel-car-btn">
				<div class="button-acquista-corsa" style="">
					<button type="submit" onclick="gtag_report_conversion();" id="acquistaAgendaAutista" name="acquista-agenda-autista" class="btn btn-sm btn-primary ">
						${textAcquistaCorsaAgendaAutista}&nbsp;<span id="prezzoTotaleClienteBtn"></span>&euro;</button>
				</div>
			</div>
			<div class="row sel-car-btm-info">
				<div class="col-md-12">
					<p>Prezzo tutto compreso iva inclusa</p>
				</div>
			</div>
		</div>
	</c:if>
	
	<c:set var="RIS" value="${ricercaTransfert.resultAutistaTariffePrezzo}"/>
	<c:if test="${ricercaTransfert.ritorno && empty RIS.resultMedio && (empty RIS_AGENDA_AUTISTA.agendaAutista_AutistaAndata  || empty RIS_AGENDA_AUTISTA.agendaAutista_AutistaRitorno)}">
		<c:set var="VarAndataRitornoText" value="${empty RIS_AGENDA_AUTISTA.agendaAutista_AutistaAndata ? 'ANDATA' : 'RITORNO'}"/>
		<div class="step-w-extra-info">
		<div class="">
			<p>La Corsa di ${VarAndataRitornoText} non è disponibile. Ti ricordiamo che puoi ripetere la ricerca selezionando l'opzione "Richiesta Preventivi".
			Riceverai in poco tempo i Preventivi con Andata e Ritorno via Email e via SMS dai nostri Autisti.</p>
		</div>
		</div>
	</c:if>
	
	<!-- NEW RIS_SESSION -->
	
	<c:if test="${not empty RIS && RIS.totaleShowClasseAutoveicolo > 0 }">
	<h3><u><b>PREZZI APOLLOTRANSFERT - Penseremo noi a passare il Servizio ai nostri Autisti</b></u></h3>
	<fmt:formatNumber var="NUMBER_COL" value="${RIS.totaleShowClasseAutoveicolo > 1 ? 24 / RIS.totaleShowClasseAutoveicolo : 12}" maxFractionDigits="0" />
	<c:forEach items="${RIS.resultMedio}" var="varObj">
	<c:if test="${ varObj.showClasseAutoveicolo }">
		<div class="div-sel-car text-center">
			<div class="row head-sel-car">
				<h3><fmt:message key="${varObj.classeAutoveicolo.nome}"/>&nbsp;<fmt:formatNumber value="${varObj.prezzoTotaleCliente}" pattern="0.00"/>&euro;</h3>
				<h4><fmt:message key="${varObj.classeAutoveicolo.nome}.desc"/></h4>
				<h4>${descrizioneCategorieAutoMap[varObj.classeAutoveicolo.id]}</h4>
			</div>
			<div class="row sel-car-btn">
				<c:choose>
				<c:when test="${varObj.prezzoCommissioneServizio > 30 && varObj.prezzoTotaleAutista <= 300}">
				<div class="col-md-6">
					<div>
						<h4>Acquista Totalmente la Corsa</h4>
						<label>
						<button type="submit" onclick="gtag_report_conversion();" id="prenotaTransfertMedio-${varObj.classeAutoveicolo.id}" name="prenota-medio" value="${varObj.classeAutoveicolo.id}-false" 
							class="btn btn-sm btn-primary">Paga <fmt:formatNumber value="${varObj.prezzoTotaleCliente}" pattern="0.00"/>&euro;</button>
						</label>
					</div>
				</div>
				<div class="col-md-6">
					<div>
						<h4>Acquista il <fmt:formatNumber value="${varObj.prezzoCommissioneServizio / varObj.prezzoTotaleCliente * 100}" maxFractionDigits="0" />% 
						e il resto in Contanti all&#39;Autista <fmt:formatNumber value="${varObj.prezzoTotaleAutista}" pattern="0.00"/>&euro;</h4>
						<label>
						<button type="submit" onclick="gtag_report_conversion();" id="prenotaTransfertMedio-${varObj.classeAutoveicolo.id}" name="prenota-medio" value="${varObj.classeAutoveicolo.id}-true" 
							class="btn btn-sm btn-primary">Paga <fmt:formatNumber value="${varObj.prezzoCommissioneServizio}" pattern="0.00"/>&euro;</button>
						</label>
					</div>
				</div>
				</c:when>
				<c:otherwise>
				<div class="col-md-offset-3 col-md-6 ">
					<div class="button-acquista-corsa">
						<button type="submit" onclick="gtag_report_conversion();" id="prenotaTransfertMedio-${varObj.classeAutoveicolo.id}" name="prenota-medio" value="${varObj.classeAutoveicolo.id}-false" 
							class="btn btn-sm btn-primary ">Acquista la Corsa <fmt:formatNumber value="${varObj.prezzoTotaleCliente}" pattern="0.00"/>&euro;</button>
					</div>
				</div>
				</c:otherwise>
				</c:choose>
			</div>
			<div class="row sel-car-btm-info">
				<div class="col-md-12">
					<!-- Non lo faccio vedere al Cliente 
					<p>Tariffa per km: <fmt:formatNumber value="${varObj.tariffaPerKm}" pattern="0.00"/>&euro;/km</p> -->
					<!-- Non lo faccio vedere al Cliente 
					<p>Prezzo Autista: <fmt:formatNumber value="${varObj.prezzoTotaleAutista}" pattern="0.00"/>&euro;</p> -->
					<c:if test="${ricercaTransfert.scontoRitorno eq true}">
						<p>Sconto Ritorno entro ${numMaxOreAttesaRitorno} ore: ${percentualeScontoRitorno}%</p>
					</c:if>
					<c:if test="${not empty varObj.maggiorazioneNotturna}">
						<p>Inclusa Maggiorazione Notturna ${varObj.maggiorazioneNotturna}&euro;</p>
					</c:if>
					<p>Prezzo tutto compreso iva inclusa</p>
				</div>
			</div>
		</div>
	</c:if>
	</c:forEach>
	</c:if>
	

	<%@ include file="/common/info_servizio_clienti_generale.jsp"%>

	<script src="<c:url value="/scripts/vendor/intl-tel-input-9.0.3/build/js/utils.js"/>"></script>
	<script src="<c:url value="/scripts/vendor/intl-tel-input-9.0.3/build/js/intlTelInput.min.js"/>"></script>
	<link rel="stylesheet" href="<c:url value="/scripts/vendor/intl-tel-input-9.0.3/build/css/intlTelInput.css"/>"> 
	<%@ include file="/scripts/ModalInvioSmsEmail.jsp"%>
	
	<!-- ----------------------- -->
	<!-- INVIA EMAIL CORSA AMICO -->
	<!-- ----------------------- -->
	
	<div class="div-verify">
		<div class="row head-verify">
			<h3><fmt:message key="invia.preventivo.corsa.amico"/></h3>
		</div>
		<div class="row row-verify">
			<div class="col-md-offset-1 col-md-10 col-md-offset-1">
				<div class="row">
					<div class="col-md-9">
						<div class="form-group input-group">
							<span class="input-group-addon"><i class="fas fa-envelope"></i></span>
							<input type="email" class="form-control" id="idEmailCliente" placeholder="Inserisci Email" data-validate="false" />
						</div>
					</div>
					<div class="col-md-3">
						<input type="button" class="" id="inviaEmailCorsaCliente" value="Invia Email">  
					</div>
				</div>
			</div>
		</div>
	</div>
	</div>
	
	<script type="text/javascript">
	$(document).ready(function() {
		
	CalcolaPrezzoTotaleCliente();
	
	function CalcolaPrezzoTotaleCliente() {
		if( $("input[name=radio_AgendaAutistaResultAndata]").length > 0 || $("input[name=radio_AgendaAutistaResultRitorno]").length > 0 ) {
			var prezzoTotaleCliente = 0;
			var prezzoAndata = 0;
			var prezzoRitorno = 0; 
			var idTariffarioAndata; 
			var idTariffarioRitorno; 
			if( $("input[name=radio_AgendaAutistaResultAndata]").length > 0 ) {
				prezzoAndata =  $('input[name=radio_AgendaAutistaResultAndata]:checked', '#ricercaTransfertForm').val();
				idTariffarioAndata = $('input[name=radio_AgendaAutistaResultAndata]:checked', '#ricercaTransfertForm').attr('id');
			}
			if( $("input[name=radio_AgendaAutistaResultRitorno]").length > 0 ){
				prezzoRitorno = $('input[name=radio_AgendaAutistaResultRitorno]:checked', '#ricercaTransfertForm').val();
				idTariffarioRitorno = $('input[name=radio_AgendaAutistaResultRitorno]:checked', '#ricercaTransfertForm').attr('id');
			}
			prezzoTotaleCliente =  +prezzoAndata + +prezzoRitorno;
			//alert(prezzoTotaleCliente);
			document.getElementById("prezzoTotaleClienteBtn").innerHTML = (Math.round(prezzoTotaleCliente * 100) / 100).toFixed(2).replace(".", ",");
			$("#acquistaAgendaAutista").val( idTariffarioAndata+'-'+idTariffarioRitorno );
			return prezzoTotaleCliente;
		}else{
			return null;
		}
	}
	$('#ricercaTransfertForm input').on('change', function() {
		CalcolaPrezzoTotaleCliente();
	});
		
		
	$.getScript('<c:url value="/scripts/vendor/intl-tel-input-9.0.3/build/js/Start_intlTelInput.js"/>');
	$("#inviaEmailCorsaCliente").click(function(){
	if( $('#idEmailCliente').val() ) {
		var email = $( "#idEmailCliente" ).val();
		var idRichTransfert = '${ricercaTransfert.id}';
		$.ajax({
			type: 'POST',
			url: '${pageContext.request.contextPath}/inviaEmailCorsaCliente',
			dataType: "json",
			data: { 
				email : email,
				idRichTransfert : idRichTransfert
			},
			beforeSend: function(){
				//$("#invioSmsEmailConfirm").modal('show');
				$("#idModalEsitoInvioTitle").html('EMAIL');
				$("#idModalEsitoInvioText").html('');
				$(".img-loader").fadeIn(500);
				$(".emailSmsPopUp").fadeIn(500);
			},
			success: function(data) {
				var json_x = data;
				var EsitoInvio = json_x['EsitoInvio'];
				if( EsitoInvio == 'success' ){
					$(".img-loader").fadeOut(500); //nascondo il loader
					$("#idModalEsitoInvioTitle").html('EMAIL'); 
					$("#idModalEsitoInvioText").html('<div class="text-primary"><b>EMAIL INVIATA</b><br>'+
							'Abbiamo inviato la Corsa a '+email+'</div>');
				}else if( EsitoInvio == 'email-esaurite' ){
					$(".img-loader").fadeOut(500); //nascondo il loader
					$("#idModalEsitoInvioTitle").html('EMAIL');
					$("#idModalEsitoInvioText").html('<div class="text-info"><b>Email non inviata</b><br>'+
							'Messaggio non inviato, riprovare pi&ugrave; tardi.</div>');
				}else if( EsitoInvio == 'email-invalida' ){
					$(".img-loader").fadeOut(500); //nascondo il loader
					$("#idModalEsitoInvioTitle").html('EMAIL');
					$("#idModalEsitoInvioText").html('<div class="text-info"><b>Email non inviata</b><br>'+
							'Email non valida, Inserie una Email corretta.</div>');
				}
			}, //fine success
			error: function (req, status, error) {
				alert('errore ajax inviaEmailCorsaCliente');
			}
		});
	}else{
		bootbox.alert({
			backdrop: true,
			message: "Inserisci Email."
		});
	}
	});
	$("#inviaSmsCorsaCliente").click(function(){
		if( $("#phoneId").intlTelInput("isValidNumber") ){
			var numberTelCustomer = $("#phoneId").intlTelInput("getNumber");
			var idRichTransfert = '${ricercaTransfert.id}'; 
			//alert( numberTelCustomer );
			$.ajax({
				type: 'POST',
				url: '${pageContext.request.contextPath}/inviaSmsCorsaCliente',
				dataType: "json",
				data: { 
				numberTelCustomer : numberTelCustomer,
				idRichTransfert : idRichTransfert
				},
				beforeSend: function(){
					//$("#invioSmsEmailConfirm").modal('show');
					$("#idModalEsitoInvioTitle").html('SMS');
					$("#idModalEsitoInvioText").html('');
					$(".img-loader").fadeIn(500);
					$(".emailSmsPopUp").fadeIn(500);
				},
				success: function(data) {
					var json_x = data;
					var EsitoInvio = json_x['EsitoInvio'];
					if( EsitoInvio == 'success' ){
						$(".img-loader").fadeOut(500); //nascondo il loader
						$("#idModalEsitoInvioTitle").html('SMS');
						$("#idModalEsitoInvioText").html('<div class="text-primary"><b>SMS INVIATO</b><br>'+
								'Abbiamo inviato la Corsa al Numero '+numberTelCustomer+'</div>');
					}else{
						$(".img-loader").fadeOut(500); //nascondo il loader
						$("#idModalEsitoInvioTitle").html('SMS');
						$("#idModalEsitoInvioText").html('<div class="text-info"><b>SMS non inviato</b><br>'+
								'Messaggio non inviato, riprovare pi&ugrave; tardi.</div>');
					}
				}, //fine success
				error: function (req, status, error) {
					alert('errore ajax inviaSmsCorsaCliente');
				}
			});
		}else{
			if( !$("#phoneId").val() ) {
				bootbox.alert({
					backdrop: true,
					message: "Inserisci Numero Telefono."
				});
			}else{
				bootbox.alert({
					backdrop: true,
					message: "Inserisci un Numero di Telefono valido."
				});
			}
		}
	});
	}); // fine read jquery
	</script>
</c:if>	