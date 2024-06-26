<%@ include file="/common/taglibs.jsp"%>
<head>
<meta name="menu" content="ListaPreventiviCliente"/>
<title>Lista Preventivi Cliente</title>
<meta name="description" content="Lista Preventivi Cliente">
<!-- jquery -->
<script src="<c:url value="/scripts/vendor/jquery-ui-1.11.4/external/jquery/jquery-2.2.4.min.js"/>"></script>
<script type="text/javascript">
jQuery(document).ready(function(){
	$(".calcolaPrezzoServizioMultiplo").click(function(){
		var numPasseggeriTotali = ${ricercaTransfert.numeroPasseggeri};
		var prezzoTotale = 0; var numPasseggeriCoperti = 0; var richPartTokenAutistaMultiplo = '';
		$('.calcolaPrezzoServizioMultiplo').each(function (index, obj) {
		    if( obj.checked ){
		    	prezzoTotale = prezzoTotale + Number( $(this).val().split('-')[0] );
		    	numPasseggeriCoperti = numPasseggeriCoperti + parseInt( $(this).val().split('-')[1] );
		    	richPartTokenAutistaMultiplo = richPartTokenAutistaMultiplo + $(this).val().split('-')[2] + '-';
			}
		});
		prezzoTotale = prezzoTotale.toFixed(2);
		//alert("prezzoTotale: "+prezzoTotale);
		//alert("numPasseggeriCoperti: "+numPasseggeriCoperti);
		document.getElementById("prezzoTotale").innerHTML = prezzoTotale;
		document.getElementById("numPasseggeriCoperti").innerHTML = numPasseggeriCoperti;
		var urlHttps = "<fmt:message key='https.w3.domain.apollotransfert.name'></fmt:message>/?courseId=${ricercaTransfert.id}"
				+"&tokenRicTransfert=${ricercaTransfert.ricTransfert_Token}&richPartTokenAutistaMultiplo="+richPartTokenAutistaMultiplo;
		//alert( numPasseggeriCoperti +'-'+ numPasseggeriTotali );
		if( numPasseggeriCoperti >= numPasseggeriTotali ){
			//alert(77);
			document.getElementById("linkAcquistaCorsaMultipla").innerHTML = "<br><a href="+urlHttps+" class='alert-link'><strong>Acquista i Preventivi Selezionati</strong></a>";
		}else{
			//alert(88);
			document.getElementById("linkAcquistaCorsaMultipla").innerHTML = "";
		}
		//alert(999);
	});
	
	
	$(".checkRicezionePreventivi").change(function() {
	    $.ajax({
			type: 'POST',
			url: '${pageContext.request.contextPath}/cancellaAttivaRicezionePreventiviCliente',
			dataType: "json",
			data: {
				idRicercaTransfert : ${ricercaTransfert.id},
				cancellaRicezionePreventivi : this.checked,
				ricTransfert_Token : '${ricercaTransfert.ricTransfert_Token}'
			},
			beforeSend: function(){ },
			success: function(result) {
				if( result['esito'] ){
					$('#messaggioRicezionePreventiviId').html( '<fmt:message key="messaggio.stop.preventivi"/>' );
					$('.checkRicezionePreventivi').prop('checked', true);
	        	}else{
	        		$('#messaggioRicezionePreventiviId').html( '<fmt:message key="messaggio.ricevi.preventivi"/>' );
	        		$('.checkRicezionePreventivi').prop('checked', false);
	        	}
			},
			error: function (req, status, error) {
				alert('errore ajax cancellaAttivaRicezionePreventiviCliente');
				//window.location.replace('${pageContext.request.contextPath}/home-user');
				location.reload(true);
			}
		});
	});

}); // fine ready
</script>

<c:if test="${AMBIENTE_PRODUZIONE}">
<!-- Event snippet for Button Acquista Corsa - PART conversion page
In your html page, add the snippet and call gtag_report_conversion when someone clicks on the chosen link or button. -->
<script>
function gtag_report_conversion(url) {
  var callback = function () {
    if (typeof(url) != 'undefined') {
      window.location = url;
    }
  };
  gtag('event', 'conversion', {
      'send_to': 'AW-1011317208/mnw3CPnbvOwBENjzneID',
      'event_callback': callback
  });
  console.log('google conversione');
  return false;
}
</script>
</c:if>

</head>
<body>
<div class="content-area home-content">
<div class="container">
<div class="">
	<div class="row  ">
	<spring:bind path="ricercaTransfert.*">
	<c:if test="${not empty status.errorMessages}">
		<div class="alert alert-danger alert-dismissable">
			<a href="#" data-dismiss="alert" class="close">&times;</a>
			<c:forEach var="error" items="${status.errorMessages}">
				<c:if test = "${error != 'null'}">   
					<c:out value="${error}" escapeXml="false"/><br>
				</c:if>
			</c:forEach>
		</div>
	</c:if>
	</spring:bind>
	<%@ include file="/common/messages.jsp" %>
	</div>
	
	<c:if test="${not empty ricercaTransfert}">
		<%@ include file="/common/ric_result_dati_ricerca.jsp"%>
	</c:if>

	<div class="row ">
		<div class="form-group ">
			<div class="col-xs-9 col-sm-9 col-md-7 col-lg-6">
				<label class="checkbox-inline">
				<input type="checkbox" class="checkRicezionePreventivi" 
				${empty ricercaTransfert.cancellaRicezionePreventiviCliente || ricercaTransfert.cancellaRicezionePreventiviCliente == false ? '' : 'checked'}>
				<fmt:message key="messaggio.stop.ricezione.preventivi"/></label>
			</div>
			<div class="col-xs-3 col-sm-3 col-md-5 col-lg-6">
				<strong><u>
				<c:choose>
				<c:when test="${empty ricercaTransfert.cancellaRicezionePreventiviCliente || ricercaTransfert.cancellaRicezionePreventiviCliente == false}">
					<span id="messaggioRicezionePreventiviId"><fmt:message key="messaggio.ricevi.preventivi"/></span>
				</c:when>
				<c:when test="${ricercaTransfert.cancellaRicezionePreventiviCliente == true}">
					<span id="messaggioRicezionePreventiviId"><fmt:message key="messaggio.stop.preventivi"/></span>
				</c:when>
				</c:choose>
				</u></strong>
			</div>
		</div>
	</div>

	<div class="row ">
	<h2>Elenco Preventivi inviati dagli Autisti</h2>

	<c:if test="${ricercaTransfert.tipoServizio == 'MULTIP' && almenoUnPreventivoInviato && empty ricercaTransfert.richiestaAutistaMultiplo_Id}">
		<h3>
		Questa Corsa necessita di pi&ugrave; vetture per trasportare i ${ricercaTransfert.numeroPasseggeri} Passeggeri.
		<br>Selezionare le vetture per coprire il Numero dei Passeggeri.
		</h3>
		<p>
		<b>Numero Passeggeri Coperti: <span id="numPasseggeriCoperti"></span></b>
		<br><b>Prezzo Totale: <span id="prezzoTotale"></span></b>
		<span id="linkAcquistaCorsaMultipla"></span>
		</p>
	</c:if>
	</div>
	
	<div class="row ">
		<c:choose>
		<c:when test="${not almenoUnPreventivoInviato}">
			<p><strong>Non ci sono ancora Preventivi.</strong></p>
		</c:when>
		<c:otherwise>
			<hr>
			<c:set var="conta" value="0" />
			<c:forEach items="${ricercaTransfert.richiestaAutistaParticolare}" var="varObj">
				<c:if test="${not empty varObj.preventivo_inviato_cliente && varObj.preventivo_inviato_cliente == true}">
					<c:set var="conta" value="${conta + 1}" />
					<p>
					<c:if test="${ricercaTransfert.tipoServizio == 'MULTIP' && empty ricercaTransfert.richiestaAutistaMultiplo_Id}">
						<label for="chekboxAuto-${varObj.id}" class="">Seleziona Preventivo ${conta}
						<input type="checkbox" name="chekboxAuto-${varObj.id}" class="calcolaPrezzoServizioMultiplo form-control" id="chekboxAuto-${varObj.id}"
						value="${varObj.prezzoTotaleCliente}-${varObj.autoveicolo.numeroPostiPasseggeri}-${varObj.token}"></label>
						<input type="hidden" value="${varObj.prezzoTotaleCliente}-${varObj.autoveicolo.numeroPostiPasseggeri}">
					</c:if>
					<c:set var="inizialeCognome" value="${fn:substring(varObj.autoveicolo.autista.user.lastName, 0, 1)}." />
					<br><strong>Nome Autista: </strong>${varObj.autoveicolo.autista.user.firstName}&nbsp;${inizialeCognome}
					<br><strong>Modello e Classe Autoveicolo: </strong>${varObj.autoveicolo.marcaModello} (<fmt:message key="${varObj.classeAutoveicoloScelta.nome}"/>)
					<br><strong>Desc. Classe Autoveicolo: </strong> ${descrizioneCategorieAutoMap[varObj.classeAutoveicoloScelta.id]}
					<br><ins><strong>Numero Posti Passeggeri: ${varObj.autoveicolo.numeroPostiPasseggeri}</strong></ins>
					<br><ins><strong>Prezzo Totale (Prezzo tutto compreso iva inclusa): <fmt:formatNumber value="${varObj.prezzoTotaleCliente}" pattern="0.00"/>&euro;</strong></ins>
					<br><ins><strong>Tempo validit&agrave; Preventivo (Tempo Massimo Acquisto Corsa): <fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${varObj.preventivo_validita_data_Date}" /></strong></ins><br>
					</p>
					<c:choose>
					<c:when test="${ricercaTransfert.tipoServizio == 'MULTIP'}">
						<p><strong>
						<c:if test = "${fn:contains(ricercaTransfert.richiestaAutistaMultiplo_Id, varObj.id)}">
							<a href="https://${DammiUrl_InfoCorsa_Cliente}">PREVENTIVO ACQUISTATO - Visualizza Info Corsa e Info Autista</a>
						</c:if>
						</strong></p>
					</c:when>
					<c:when test="${ricercaTransfert.tipoServizio == 'PART'}">
						<p><strong>
						<c:choose>
						<c:when test="${ricercaTransfert.richiestaAutistaParticolare_Id == varObj.id}">
							<a href="https://${DammiUrl_InfoCorsa_Cliente}">PREVENTIVO ACQUISTATO - Visualizza Info Corsa e Info Autista</a>
						</c:when>
						<c:otherwise>
							<c:choose>
							<c:when test="${empty ricercaTransfert.richiestaAutistaParticolare_Id}">
								<a href="<c:url value='home'/>?courseId=${ricercaTransfert.id}&tokenRicTransfert=${ricercaTransfert.ricTransfert_Token}&richPartTokenAutista=${varObj.token}" 
								class="alert-link" onclick="gtag_report_conversion();"><strong>Acquista Questo Preventivo</strong></a>
							</c:when>
							<c:otherwise>
								<del>Acquista Questo Preventivo</del> 
							</c:otherwise>
							</c:choose>
						</c:otherwise>
						</c:choose>
						</strong></p>
					</c:when>
					</c:choose>
					<hr>
				</c:if>
			</c:forEach>
		
		</c:otherwise>
		</c:choose>
	</div>

</div>
</div>
</div>

<!-- bootstrap JS -->
<script async src="<c:url value="/js/bootstrap.min.js"/>"></script>
</body>