<c:if test="${not empty ricercaTransfert && ricercaTransfert.ricercaRiuscita eq false}">
<!-- CSS calendar e time -->
<link rel="stylesheet" href="<c:url value="/scripts/vendor/pickadate.js-master/lib/compressed/themes/default.total.css"/>">
<div class="form-step-1 form-steps ">
	<div class="form-group input-group <spring:bind path="ricercaTransfert.partenzaRequest">${(not empty status.errorMessage) ? ' has-error' : ''}</spring:bind>">
		<span class="input-group-addon"><i class="fas fa-map-marker-alt"></i></span>
		<input type="text" value="${ricercaTransfert.partenzaRequest}" name="partenzaRequest" class="form-control add-input" id="idPartenza" 
			placeholder="Partenza" autocomplete="off" required />
	</div>
	<div class="form-group input-group <spring:bind path="ricercaTransfert.arrivoRequest">${(not empty status.errorMessage) ? ' has-error' : ''}</spring:bind>">
		<span class="input-group-addon"><i class="fas fa-map-marker-alt"></i></span>
		<input type="text" value="${ricercaTransfert.arrivoRequest}" name="arrivoRequest" class="form-control add-input" id="idArrivo" 
			placeholder="Arrivo" autocomplete="off" required />
	</div>
	<div class="form-group input-group invert-form-group">
		<input type="button" class="form-control invertiAndataRitorno" id="invert-da" value="Inverti Partenza Arrivo">
	</div>
	<input type="hidden" id="routeP"><input type="hidden" id="streetNumberP"><input type="hidden" id="countryP">
	<input type="hidden" id="localityP"><input type="hidden" id="administAreaLevel_2P">
	<input type="hidden" id="routeA"><input type="hidden" id="streetNumberA"><input type="hidden" id="countryA">
	<input type="hidden" id="localityA"><input type="hidden" id="administAreaLevel_2A">
	<input type="hidden" value="${ricercaTransfert.place_id_Partenza}" id="partenzaPlaceID" name="place_id_Partenza" >
	<input type="hidden" value="${ricercaTransfert.place_id_Arrivo}" id="arrivoPlaceID" name="place_id_Arrivo" >
	<div id="hasErrorData" class=" <spring:bind path="ricercaTransfert.dataOraPrelevamento">${(not empty status.errorMessage) ? ' has-error' : ''}</spring:bind>">
		<div class="form-group input-group date-control ">
			<span class="input-group-addon"><i class="far fa-calendar-alt"></i></span>
			<input type="text" id="calendarioFrom" class="form-control" placeholder="Giorno Prelevamento" required />
			<input type="hidden" value="${ricercaTransfert.dataOraPrelevamento}" name="dataOraPrelevamento" id="calendarioFromHidden">
		</div> 
	</div>
	<div class="form-group input-group time-control">
		<div id="hasErrorOraPrelevamento" class=" <spring:bind path="ricercaTransfert.oraPrelevamento">${(not empty status.errorMessage) ? ' has-error' : ''}</spring:bind>">
			<div class=" input-group ">
				<span class="input-group-addon"><i class="far fa-clock"></i></span>
				<input type="text" value="${ricercaTransfert.oraPrelevamento}" name="oraPrelevamento" id="idOraPrelevamento" 
					class="form-control timepicker" placeholder="Ora Prelevamento" required />
			</div>
		</div>
		<span class="radio-holder">
			<label class="custom-check"><span class="ret-txt">Ritorno</span>
				<input type="checkbox" name="ritorno" id="chekboxLargeID" ${ricercaTransfert.ritorno ? 'checked':''}><span class="checkmark"></span>
			</label>
		</span>
	</div>
	<!-- RITORNO -->
	<div id="date-ritorno" style="display:none">
		<div class="">
			<div id="hasErrorDataRitorno" class=" <spring:bind path="ricercaTransfert.dataOraRitorno">${(not empty status.errorMessage) ? ' has-error' : ''}</spring:bind>">
				<div class="form-group input-group date-control ">
					<span class="input-group-addon"><i class="far fa-calendar-alt"></i></span>
					<input type="text" id="calendarioTo" class="form-control requiredReturn" placeholder="Giorno Ritorno"  />
					<input type="hidden" value="${ricercaTransfert.dataOraRitorno}" name="dataOraRitorno" id="calendarioToHidden">
				</div>
			</div>
			<div id="hasErrorOraRitorno" class=" <spring:bind path="ricercaTransfert.oraRitorno">${(not empty status.errorMessage) ? ' has-error' : ''}</spring:bind>">
				<div class="form-group input-group time-control ">
					<span class="input-group-addon"><i class="far fa-clock"></i></span>
					<input type="text" value="${ricercaTransfert.oraRitorno}" name="oraRitorno" id="idOraRitorno" 
						class="form-control timepicker requiredReturn " placeholder="Ora Ritorno" />
				</div>
			</div>
		</div>
		<c:if test="${not empty ricercaTransfert && ricercaTransfert.ritorno eq true && ricercaTransfert.scontoRitorno eq false
			&& not empty ricercaTransfert.dataOraPrelevamentoDate && ricercaTransfert.durataConTrafficoValue != 0}">
			<div class="form-group input-group step-w-extra-info">
				<p><em>Sar&agrave; applicato uno sconto del ${percentualeScontoRitorno}% se il Ritorno avviene non oltre le 
					<fmt:formatDate pattern="HH:mm" value="${dataScontoAppicabile}"/> del <fmt:formatDate pattern="dd MMMM" value="${dataScontoAppicabile}" /></em></p>
			</div>
		</c:if>
	</div>
	<div class="form-group input-group <spring:bind path="ricercaTransfert.numeroPasseggeri">${(not empty status.errorMessage) ? ' has-error' : ''}</spring:bind>">
		<span class="input-group-addon"><i class="fas fa-user"></i></span>
		<input type="number" name="numeroPasseggeri" class="form-control" placeholder="Num. Passeggeri" 
			<c:if test="${ricercaTransfert.numeroPasseggeri != 0}">value="${ricercaTransfert.numeroPasseggeri}"</c:if> required />
	</div>
	<div class="form-group input-group form-ImpInfo">
		<span class="input-group-addon"><i class="far fa-file-alt"></i></span>
		<textarea class="form-control" rows="6" name="noteAutista" id="noteAutistaId" placeholder="Informazioni importanti per l'Autista">${ricercaTransfert.notePerAutista}</textarea>
	</div>
	<div class="form-group ">
		<div class="form-check"> 
			<label class="form-check-label">
				<input class=" radio-inline radio-ric-transfer" type="radio" name="radioTipoServizio" id="ST" value="ST" checked> 
				<fmt:message key="index.mess.option.serv.standard"/></label>
			<label class="form-check-label">
				<input class=" radio-inline radio-ric-transfer" type="radio" name="radioTipoServizio" id="PART" value="PART"> 
				<fmt:message key="index.mess.option.serv.special"/></label>
        </div>
	</div>
	<div class="form-group">
		<c:if test="${AMBIENTE_PRODUZIONE}">
		<!-- Event snippet for Ricerca Transfer Eseguita conversion page
		In your html page, add the snippet and call gtag_report_conversion when someone clicks on the chosen link or button. -->
		<script>
		function gtag_report_conversion(url) {
		  var callback = function () {
		    if (typeof(url) != 'undefined') {
		      window.location = url;
		    }
		  };
		  gtag('event', 'conversion', {
		      'send_to': 'AW-1011317208/JkSvCJzxmuwBENjzneID',
		      'event_callback': callback
		  });
		  console.log('google conversione');
		  return false;
		}
		</script>
		</c:if>
		<button type="submit" onclick="gtag_report_conversion();" id="ricercaID" name="ricerca-transfert" class="btn btn-primary ${not empty RECAPTCHA_PUBLIC ? 'g-recaptcha' : 'attesaLoader'}"
		<c:if test="${not empty RECAPTCHA_PUBLIC}">data-sitekey='${RECAPTCHA_PUBLIC}' data-callback='onSubmit'</c:if>>Ricerca Corsa !</button>
	</div>
	
	<!-- PASSAGGI DA SVOLGERE PER ACQUISTO TRANSFER -->
	<div class="jumbotron" style="background-color: #1c74bb;">
		<div class="row" >
			<div class="col-xs-12 col-sm-6 col-md-6 col-lg-3" >
				<div class="thumbnail">
				<img src="<c:url value='/nuova_grafica/'/>images/ico-home-location-pin.png" alt="RICERCA TRANSFERT">
				<div class="caption">
				<p><strong>1. Ricerca un transfert in tutta Italia</strong></p>
				<p>Inserisci l'indirizzo di partenza e di arrivo, la data, l'orario e il numero dei passeggeri.</p>
				</div>
				</div>
			</div>
			<div class="col-xs-12 col-sm-6 col-md-6 col-lg-3">
				<div class="thumbnail">
				<img src="<c:url value='/nuova_grafica/'/>images/ico-home-insurance.png" alt="AUTO DISPONIBILI">
				<div class="caption">
				<p><strong>2. Scopri le auto disponibili</strong></p>
				<p>Scegli un Servizio Standard o un Servizio Speciale spuntando il bottone sottostante.</p>
				</div>
				</div>
			</div>
			<div class="col-xs-12 col-sm-6 col-md-6 col-lg-3">
				<div class="thumbnail">
				<img src="<c:url value='/nuova_grafica/'/>images/ico-home-payment-method.png" alt="PAGAMENTO">
				<div class="caption">
				<p><strong>3. Scegli il sistema di pagamento</strong></p>
				<p>Puoi pagare il noleggio auto con conducente con carta di credito Visa e Mastercard, o Paypal.</p>
				</div>
				</div>
			</div>
			<div class="col-xs-12 col-sm-6 col-md-6 col-lg-3">
				<div class="thumbnail">
				<img src="<c:url value='/nuova_grafica/'/>images/ico-home-order.png" alt="RIEPILOGO">
				<div class="caption">
				<p><strong>4. Riepilogo dell'ordine e conferma</strong></p>
				<p>Ricevi la conferma di pagamento con le informazioni dell'autista sul servizio acquistato.</p>
				</div>
				</div>
			</div>
		</div>
		<div class="row" >
			<div class="col-xs-12 col-sm-6 col-md-6 col-lg-6" >
				<div class="thumbnail" style="background-color: #D0D0D0;">
				<div class="caption">
					<h3><strong>Le nostre tariffe:</strong></h3>
					<p>Le tariffe di noleggio con conducente <b>pi˘ convenienti</b> da e per Roma, Milano, Bologna, Napoli e Milano.</p>
					<div class="text-center"><a href="<c:url value='/tariffe-transfer'/>" class="btn btn-default "><fmt:message key="menu.tariffe"/></a></div>
				</div>
				</div>
			</div>
			<div class="col-xs-12 col-sm-6 col-md-6 col-lg-6" >
				<div class="thumbnail" style="background-color: #D0D0D0;">
				<div class="caption">
					<h3><strong><fmt:message key="webapp.apollotransfert.name"/> per Agenzie Viaggio:</strong></h3>
					<p>Se sei un'<b>agenzia di viaggio</b>, <fmt:message key="webapp.apollotransfert.name"/> ti offre pacchetti speciali per acquistare servizi NCC a Roma e in tutta 
					Italia, per <b>organizzazione eventi</b>, agli <b>alberghi</b>, alle <b>strutture turistiche</b> o ai <b>resort</b>. Se sei il titolare di un'agenzia e hai bisogno di acquistare dei 
					pacchetti di noleggio con conducente a Roma e in tutta Italia, rivolgiti a noi.</p>
					<div class="text-center"><a href="<c:url value='/ncc-agenzie-viaggio'/>" class="btn btn-default"><fmt:message key="menu.ncc.agenzie.viaggio"/></a></div>
				</div>
				</div>
			</div>
		</div>
	</div>
	
	<!-- Servizio NCC Standard -->
	
	<div class="row" >
		<div class="col-xs-12 col-sm-6 col-md-6 col-lg-6">
		<h2>Servizio NCC Standard</h2>
		<p>Se viaggi senza condizioni particolari, scegli un <b>servizio Standard</b> di noleggio auto con conducente. Puoi richiedere un servizio NCC Standard quando:</p>
		<ul>
		  <li>prenoti un noleggio auto con conducente con <b>pi˘ di 24 ore di anticipo</b></li>
		  <li>viaggi con meno di <b>8 passeggeri</b></li>
		  <li><b>non hai bisogno</b> di trasportare carichi speciali nÈ animali</li>
		</ul>
		<p>Prenotando il servizio di noleggio auto Standard, siamo noi <fmt:message key="webapp.apollotransfert.name"/> a scegliere l'autista che fa al caso tuo direttamente dal nostro database di professionisti.</p>
		<p>Compilando il <b>modulo di prenotazione</b> con le tue informazioni di arrivo, partenza, data e orario, riceverai una fattura direttamente da noi. Una volta effettuato il pagamento, 
		potrai decidere se scaricare la fattura come azienda o privato. Inoltre, potrai sempre aggiungere <b>una tappa in pi˘</b> al tuo viaggio pagando un piccolo supplemento in base allo 
		spostamento aggiuntivo da fare.</p>
		</div>
		<div class="col-xs-12 col-sm-6 col-md-6 col-lg-6" >
		<h2>Servizio NCC per Trasporti Speciali</h2>
		<p>Se hai delle necessit‡ di viaggio particolari, <fmt:message key="webapp.apollotransfert.name"/> ti offre dei <b>servizi NCC speciali</b> a Roma e in tutta Italia. 
		Il servizio NCC per Trasporti Speciali fa al caso tuo se:</p>
		<ul>
		<li>hai bisogno di un noleggio auto con conducente in <b>meno di 24 ore</b></li>
		<li>viaggi con un gruppo di <b>8 o pi˘ persone</b></li>
		<li>viaggi <b>con animali</b></li>
		<li>stai trasportando <b>carichi speciali</b> come carrozzine o attrezzature sportive</li>
		</ul>
		<p>Prenotando un servizio di noleggio auto con conducente per trasporti speciali, sarai tu a metterti in contatto con gli autisti disponibili a effettuare il trasporto nella 
		tua area di riferimento. </p>
		<p>Compila il <b>modulo di prenotazione</b> con le informazioni di arrivo, partenza, data, orario ed elenca i servizi extra di cui hai bisogno nello spazio dedicato alle "Informazioni 
		importanti per l'autista". Nel messaggio dovrai specificare il numero di persone con cui sei viaggio, il peso e la razza del tuo animale da compagnia, oppure quale tipo di 
		attrezzatura speciale stai trasportando e le tempistiche di viaggio (se meno di 24 ore). </p>
		<p>A questo punto, riceverai una serie di <b>preventivi direttamente dagli autisti</b> che offrono quel servizio: non dovrai fare altro che scegliere il pi˘ conveniente ed effettuare 
		il pagamento. Il gioco Ë fatto!</p>
		</div>
	</div>
	
	
	
	<div class="row" >
		<div class="col-xs-12 col-sm-6 col-md-6 col-lg-6" >
			<div class="thumbnail" style="background-color: #D0D0D0;">
			<div class="caption">
				<h3 class="text-center"><strong>Le tratte NCC pi˘ vendute</strong></h3>
				<p>Noi di <fmt:message key="webapp.apollotransfert.name"/> siamo attivi su tutto il territorio italiano con i nostri servizi di trasporto con conducente comodamente gestibili online. Le tratte di viaggio 
				preferite dai nostri clienti sono:</p>
				<ul>
				<li>da e per l'Aeroporto di <b>Malpensa</b> e <b>Linate di Milano</b></li>
				<li>da e per l'Aeroporto di <b>Orio al Serio a Bergamo</b></li>
				<li>da e per l'Aeroporto <b>Leonardo da Vinci di Roma Fiumicino</b></li>
				<li>da e per l'Aeroporto <b>Capodichino di Napoli</b></li>
				<li>tratte di viaggio con conducente da e per <b>Modena, Bari, Palermo e Trapani</b></li>
				<li>noleggio NCC <b>Roma e provincia</b></li>
				</ul>
				<div class="text-center"><a href="<c:url value='/'/>" class="btn btn-primary text-center">Prenota</a></div>
			</div>
			</div>
		</div>
		<div class="col-xs-12 col-sm-6 col-md-6 col-lg-6" >
			<div class="thumbnail" style="background-color: #D0D0D0;">
			<div class="caption">
				<h3 class="text-center"><strong>Sei un' AUTISTA NCC? Lavora con <fmt:message key="webapp.apollotransfert.name"/> in tutta Italia:</strong></h3>
				<p>Il nostro portale web mette in contatto i viaggiatori con gli autisti in <b>ogni regione italiana</b>. Se sei un <b>autista</b> oppure un'
				<b>agenzia con autoveicoli di propriet‡</b> e sei in possesso della licenza comunale NCC, registrati alla nostra piattaforma e inizia a collaborare con noi.</p>
				<div class="text-center"><a href="<c:url value='/collaboratori'/>" class="btn btn-primary text-center">Registrati e Collabora con noi</a></div>
			</div>
			</div>
		</div>
	</div>

	<c:if test="${fn:length(recensioniApprovate) >= 3 && true}">
	<!-- INIZIO RECENSIONI -->
	<!-- prime tre recensione con "item active" -->
	<!-- successive tre recensione con "item" -->
	<link rel="stylesheet" href="<c:url value='/nuova_grafica/css/recensioni.css'/>">
	<h2 class="text-center title-striscia-blue">&#9733; LE RECENSIONI DEI NOSTRI CLIENTI &#9733;</h2>
	<div class="form-group">
		<div class="carousel-reviews broun-block">
            <div id="carousel-reviews" class="carousel slide" data-ride="carousel">
                <div class="carousel-inner">
					<c:set var="totalHours" value="${0}"/>
					<c:forEach var="varObj" items="${recensioniApprovate}" varStatus="loop">
						<c:if test="${loop.first}"><div class="item active"></c:if>
				    	<c:if test="${not loop.first and loop.index % 3 == 0 }"><div class="item"></c:if>
				    	<!-- 
				    	<p> <b>COUNT=${totalHours}</b> &nbsp; <b>${not empty varObj[2] ? varObj[2] : varObj[3]}</b> &nbsp; ${varObj[4]} &nbsp; ${varObj[0]} &nbsp; ${varObj[1]} &nbsp; loop=${loop.index} </p>
				    	 -->
				    	 <!-- col-md-4 col-sm-6 hidden-xs -->
				    	<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4">
			     			<div class="block-text rel zmin ">
						        <a title="" href="#" onclick="return false;">${varObj[0]}&nbsp;<i class="fa fa-arrow-right" aria-hidden="true"></i>&nbsp;${varObj[1]}</a>
							    <div class="mark">Punteggio:
								    <span class="rating-input">
								    	<c:set var="numStelle" value="${varObj[5]}" />
								    	<span data-value="0" class="glyphicon ${numStelle >= 1 ? 'glyphicon-star' : 'glyphicon-star-empty'}"></span>
								    	<span data-value="1" class="glyphicon ${numStelle >= 2 ? 'glyphicon-star' : 'glyphicon-star-empty'}"></span>
								    	<span data-value="2" class="glyphicon ${numStelle >= 3 ? 'glyphicon-star' : 'glyphicon-star-empty'}"></span>
								    	<span data-value="3" class="glyphicon ${numStelle >= 4 ? 'glyphicon-star' : 'glyphicon-star-empty'}"></span>
								    	<span data-value="4" class="glyphicon ${numStelle == 5 ? 'glyphicon-star' : 'glyphicon-star-empty'}"></span>
								    </span>
							    </div>
						        <p>${varObj[4]}</p>
					        </div>
							<div class="person-text rel ">
								<!--  <a title="" href="#">${not empty varObj[2] ? varObj[2] : varObj[3]}</a>
								<p><i>${not empty varObj[2] ? varObj[2] : varObj[3]}</i></p> -->
								<a href="mailto:${varObj[6]}">${not empty varObj[2] ? varObj[2] : varObj[3]}</a>
							</div>
						</div>
				    	<c:if test="${totalHours == 2 || loop.last}"> </div> 
				    	</c:if>
						<c:set var="totalHours" value="${totalHours == 2 ? 0 : totalHours + 1 }"/>
					</c:forEach>
				</div>
                <a class="left carousel-control" href="#carousel-reviews" role="button" data-slide="prev">
                    <span class="glyphicon glyphicon-chevron-left"></span>
                </a>
                <a class="right carousel-control" href="#carousel-reviews" role="button" data-slide="next">
                    <span class="glyphicon glyphicon-chevron-right"></span>
                </a>
            </div>
		    
		</div>
	</div> 
	</c:if>
</div>

<script id="gMap" src="https://maps.googleapis.com/maps/api/js?key=${GOOGLE_API_MAP_JS}&libraries=places&language=<fmt:message key="language.code"/>" async></script>
<!-- calendario e time-->
<script src="<c:url value="/scripts/vendor/pickadate.js-master/lib/compressed/total.js"/>"></script>
<script src="<c:url value="/scripts/vendor/pickadate.js-master/lib/compressed/translations/"/><fmt:message key="language.code"/>.js"></script>
<script type="text/javascript">
//serve per velocizzare la pagina al caricamento
$("#ricercaID").attr("disabled", true);
setTimeout(function(){
	jQuery.ajax({
		url: 'https://www.google.com/recaptcha/api.js?hl=<fmt:message key="language.code"/>',
	    dataType: 'script',
		success: function(result) { 
			$("#ricercaID").attr("disabled", false); 
		},	
		async: true
	});
}, 4000); //3000 //4000

$('#chekboxLargeID').click(function() {
	if( $(this).is(':checked')) { $('.requiredReturn').attr("required", true); $('.requiredReturn').trigger("change");
	}else{ $('.requiredReturn').attr("required", false); $('.requiredReturn').trigger("change"); }
});
$('#calendarioFrom').change(function(e) {
	if( !$(this).val() ) { $('#hasErrorData').addClass("has-error"); }else{ $('#hasErrorData').removeClass("has-error"); }
});
$('#idOraPrelevamento').change(function(e) {
	if( !$(this).val() ) { $('#hasErrorOraPrelevamento').addClass("has-error"); }else{ $('#hasErrorOraPrelevamento').removeClass("has-error"); }
});
$('#calendarioTo').change(function(e) {
	if( !$(this).val() ) { $('#hasErrorDataRitorno').addClass("has-error"); }else{ $('#hasErrorDataRitorno').removeClass("has-error"); }
});
$('#idOraRitorno').change(function(e) {
	if( !$(this).val() ) { $('#hasErrorOraRitorno').addClass("has-error"); }else{ $('#hasErrorOraRitorno').removeClass("has-error"); }
});

//----- CALENDARIO -----------------
var from_$input = $('#calendarioFrom').pickadate({
	min: true,
	onStart: function (){
		if( $('#calendarioFromHidden').val() != '' ){
			var date = new Date();
			date.setTime( $('#calendarioFromHidden').val() );
			//alert( date );
			this.set( 'select', date );
		}
          },
	//format: 'd mmmm, yyyy',
	formatSubmit: 'yyyy/mm/dd',
	onClose: function() {
		  $(document.activeElement).blur();
		}
}), from_picker = from_$input.pickadate('picker')
var to_$input = $('#calendarioTo').pickadate({
	min: true,
	onStart: function (){
		if( $('#calendarioToHidden').val() != '' ){
			var date = new Date();
			date.setTime( $('#calendarioToHidden').val() );
			//alert( date );
			this.set( 'select', date );
		}
          },
	//format: 'd mmmm, yyyy',
	formatSubmit: 'yyyy/mm/dd',
	onClose: function() {
		  $(document.activeElement).blur();
		}
}), to_picker = to_$input.pickadate('picker')
// Check if there‚Äôs a ‚Äúfrom‚Äù or ‚Äúto‚Äù date to start with.
if ( from_picker.get('value') ) {
  to_picker.set('min', from_picker.get('select'))
}
if ( to_picker.get('value') ) {
  from_picker.set('max', to_picker.get('select'))
}
// When something is selected, update the "from" and "to" limits.
from_picker.on('set', function(event) {
  if ( event.select ) {
	  $('#calendarioFromHidden').val(  event.select );
    to_picker.set('min', from_picker.get('select'))
  } else if ( 'clear' in event ) {
	  $('#calendarioFromHidden').val( '' );
    to_picker.set('min', false)
  }
})
to_picker.on('set', function(event) {
  if ( event.select ) {
	  $('#calendarioToHidden').val( event.select );
    from_picker.set('max', to_picker.get('select'))
  } else if ( 'clear' in event ) {
	  $('#calendarioToHidden').val( '' );
    from_picker.set('max', false)
  }
});
if(document.getElementById('chekboxLargeID').checked) {
	$("#date-ritorno").show();
   } else {
       $("#date-ritorno").hide();
   }
$('#chekboxLargeID').click(function() {
    if( $(this).is(':checked')) {
        $("#date-ritorno").show();
    } else {
        $("#date-ritorno").hide();
        $('#calendarioToHidden').val( '' );
        to_picker.set('clear');
        from_picker.set('max', false);
        
        $('#idOraRitorno').val( '' );
    }
});

//-----OROLOGIO--------------
$('.timepicker').pickatime({
  format: 'HH:i',
  //formatSubmit: 'h:i',
  interval: 15,
  /* onStart: function (){
		this.set( 'select', '12:00' );
         } */
        onClose: function() {
	  $(document.activeElement).blur();
	}
});


gMap.addEventListener('load', function() {
	// ------- GOOGLE MAPS AUTOCOMPLETE -------
	var options_OLD = { componentRestrictions: {country: ['${NAZIONI[0]}','${NAZIONI[1]}','${NAZIONI[2]}','${NAZIONI[3]}','${NAZIONI[4]}']} }; // max 5 (italia,francia,svizzera,austria,san marino)
	var options = { componentRestrictions: {country: ['IT','FR','CH','AT','CZ']} }; // max 5 (italia,francia,svizzera,austria,san marino)
	var autocompleteP = new google.maps.places.Autocomplete( $("#idPartenza")[0], options );
	var autocompleteA = new google.maps.places.Autocomplete($("#idArrivo")[0], options);
	google.maps.event.addListener(autocompleteP, 'place_changed', function() {
		var place = autocompleteP.getPlace();
		$('#partenzaPlaceID').val(place.place_id);
		var streetNumberBoolP = true; var routeP = ''; var streetNumberP = '';
		var localityP = ''; var administAreaLevel_2P = ''; var countryP = '';
		for(var i = 0; i < place.address_components.length; i++) {
	         var addressType = place.address_components[i].types[0];
	         if(addressType == 'street_number' || jQuery.inArray("point_of_interest", place.types) !== -1){
	             streetNumberBoolP = false;
	         }
	         if(componentForm[addressType]) {
	             var val = place.address_components[i][componentForm[addressType]];
	             if(addressType == 'route'){
	                 routeP = val +", "; $('#routeP').val(val);
	             }
	             if(addressType == 'street_number'){
	                 streetNumberP = val +", "; $('#streetNumberP').val(val);
	             }
	             if(addressType == 'locality'){
	                 localityP = val +", "; $('#localityP').val(val);
	             }
	             if(addressType == 'administrative_area_level_2'){
	                 administAreaLevel_2P = val +", "; $('#administAreaLevel_2P').val(val);
	             }
	             if(addressType == 'country'){
	                 countryP = val +", "; $('#countryP').val(val);
	             }
	         }
	     }
		controlStreetNumberAddress('idPartenza', streetNumberBoolP, routeP, streetNumberP, localityP, administAreaLevel_2P, countryP);
	});
	google.maps.event.addListener(autocompleteA, 'place_changed', function() {
		var place = autocompleteA.getPlace();
		$('#arrivoPlaceID').val(place.place_id);
		var streetNumberBoolA = true; var routeA = ''; var streetNumberA = '';
		var localityA = ''; var administAreaLevel_2A = ''; var countryA = '';
		for(var i = 0; i < place.address_components.length; i++) {
	         var addressType = place.address_components[i].types[0];
	         if(addressType == 'street_number' || jQuery.inArray("point_of_interest", place.types) !== -1){
	             streetNumberBoolA = false;
	         }
	         if(componentForm[addressType]) {
	             var val = place.address_components[i][componentForm[addressType]];
	             if(addressType == 'route'){
	                 routeA = val +", "; $('#routeA').val(val);
	             }
	             if(addressType == 'street_number'){
	                 streetNumberA = val +", "; $('#streetNumberA').val(val);
	             }
	             if(addressType == 'locality'){
	                 localityA = val +", "; $('#localityA').val(val);
	             }
	             if(addressType == 'administrative_area_level_2'){
	                 administAreaLevel_2A = val +", "; $('#administAreaLevel_2A').val(val);
	             }
	             if(addressType == 'country'){
	                 countryA = val +", "; $('#countryA').val(val);
	             }
	         }
	     }
		controlStreetNumberAddress('idArrivo', streetNumberBoolA, routeA, streetNumberA, localityA, administAreaLevel_2A, countryA);
	});
});

$(".invertiAndataRitorno").click(function(){
	var partenza = $('#idPartenza').val();
	var arrivo = $('#idArrivo').val();
	var partenzaPlaceId = $('#partenzaPlaceID').val();
	var arrivoPlaceId = $('#arrivoPlaceID').val();
	$('#idPartenza').val( arrivo );
	$('#idArrivo').val( partenza );
	$('#partenzaPlaceID').val( arrivoPlaceId );
	$('#arrivoPlaceID').val( partenzaPlaceId );
});

function controlStreetNumberAddress(idInput, streetNumberBool, route, streetNumber, locality, administAreaLevel_2, country) {
  var inputP = $("#"+idInput);
  if(route != '' && streetNumberBool){
      streetNumber = ", ";
      var totalStringAddress = route + streetNumber + locality + administAreaLevel_2 + country;
      inputP.val( totalStringAddress );
      setSelectionRange( document.getElementById( idInput ), route.length, route.length );
  }
}
var componentForm = {
    street_number: 'short_name',
    route: 'long_name',
    locality: 'long_name',
    localityS: 'short_name',
    administrative_area_level_1: 'short_name',
    administrative_area_level_2: 'short_name',
    administrative_area_level_3: 'short_name',
    country: 'long_name',
    postal_code: 'short_name'
};
//vedi: www.stackoverflow.com/questions/499126/jquery-set-cursor-position-in-text-area
function setSelectionRange(input, selectionStart, selectionEnd) {
    if (input.setSelectionRange) {
        input.focus();
        input.setSelectionRange(selectionStart, selectionEnd);
    }
    else if (input.createTextRange) {
        var range = input.createTextRange();
        range.collapse(true);
        range.moveEnd('character', selectionEnd);
        range.moveStart('character', selectionStart);
        range.select();
    }
}
$('#idPartenza').on('input',function(e){
		$('#partenzaPlaceID').val('');
});
$('#idArrivo').on('input',function(e){
		$('#arrivoPlaceID').val('');
});
</script>
</c:if>
