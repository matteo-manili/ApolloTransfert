<%@ include file="/common/taglibs.jsp"%>
<head>
<link rel="canonical" href="<fmt:message key="https.w3.domain.apollotransfert.name"/><c:url value='/'/>"/>
<title>Noleggio auto con conducente | Roma | Italia</title>
<meta name="description" content="Hai bisogno di un noleggio auto con conducente a Roma e in tutta Italia? Rivolgiti alla nostra piattaforma e organizza il viaggio online. Prenota ora!">
<script src="<c:url value="/scripts/vendor/jquery-ui-1.11.4/external/jquery/jquery-2.2.4.min.js"/>"></script>
<script type="text/javascript">
var RecaptchaOptions = {
	lang : '<fmt:message key="language.code"/>' // Unavailable while writing this code (just for audio challenge)
};
function onSubmit(token) {
	//$("#attesaLoader").modal('show');
	$(".attesaLoaderPopUp").fadeIn(300);
	document.getElementById("ricercaTransfertForm").submit();
}
//alert( window.location.search.substring(1) );
//alert('memorizzaIpUtente');
$.ajax({
	type: 'POST',
	url: '${pageContext.request.contextPath}/memorizzaIpUtente',
	dataType: "json",
	data: { },
	beforeSend: function(){ },
	success: function(result) {
		//alert( result['ipAddress'] );
		//alert( result['idVisitatore'] );
		$('#id-visitatore').val( result['idVisitatore'] );
	}, //fine success
	error: function (req, status, error) {
		//alert('errore ajax memorizzaIpUtente');
	}
});
</script>

<script type="text/javascript">
//------------------- CHIAMATA AJAX 1 ------------------------
/*
var callAjax_1 = new XMLHttpRequest();
callAjax_1.onreadystatechange = function() {
if (callAjax_1.readyState == XMLHttpRequest.DONE ) {
	if (callAjax_1.status == 200) {
		var data = JSON.parse(callAjax_1.responseText);
		var totaleAutisti = data.totaleAutisti;
		var t = document.createTextNode( totaleAutisti );
		document.getElementById("totaleAutisti").appendChild(t);
	}
	else if (callAjax_1.status == 400) {
		//alert('errore ajax CollaboratoriProvinciaAutistaList');
	}
	else {
		//alert('errore ajax CollaboratoriProvinciaAutistaList 222');
	}
}
};
callAjax_1.open("POST", "${pageContext.request.contextPath}/CollaboratoriProvinciaAutistaList", true);
callAjax_1.send();
*/
</script>

<c:if test="${googleConvPagamentoEseguito && AMBIENTE_PRODUZIONE}">
<!-- Event snippet for Pagamento Eseguito conversion page -->
<script>
gtag('event', 'conversion', {
	'send_to': 'AW-1011317208/mFSwCPn3-G0Q2POd4gM',
	'value': ${prezzoCommissioneApollo},
	'currency': 'EUR',
	'transaction_id': '${ricercaTransfert.id}'
});
console.log('google conversione');
</script>
</c:if>
</head>
<body>

<c:if test="${ricercaTransfert.ricercaRiuscita eq false}">
<div class="page-banner" style="background-image:url('${pageContext.request.contextPath}/nuova_grafica/images/main-banner/man-driving-car.jpg');">
<h1><fmt:message key="index.mess.title"/></h1>
<h2>Acquistare un transfert online non è mai stato così semplice e veloce</h2>
</div>
<h2 class="text-center title-striscia-blue"><span id="totaleAutisti">382</span> Autisti in tutta Italia al tuo servizio</h2>
</c:if>

<div class="content-area home-content">
<div class="container">
<div class="col-md-12 ">
<noscript>
<div class="alert alert-danger h4" role="alert">
<p><strong>JavaScript disabilitato!</strong></p>
<p>Per avere a disposizione tutte le funzionalit&agrave; di questo sito &egrave; necessario abilitare 
Javascript.</p> <p>Qui ci sono tutte le <a href="http://www.enable-javascript.com/it/" target="_blank"> 
istruzioni su come abilitare JavaScript nel tuo browser</a>.</p>
</div>
</noscript>
<form:form commandName="ricercaTransfert" method="post" action="" id="ricercaTransfertForm" autocomplete="off" data-toggle="validator" role="form">
<input type="hidden" value="${ricercaTransfert.id}" name="id">
<input type="hidden" id="id-visitatore" name="id-visitatore">
<div class="row steps-row">
<ul>
	<c:choose>
		<c:when test="${ricercaTransfert.ricercaRiuscita eq true && ricercaTransfert.riepilogo == false}">
			<li data-back="form-step-1" class="step-1 steps first step-active">
			<a style="color: inherit;" href="<c:url value='/?courseId=${ricercaTransfert.id}'/>">
			<span><img src="<c:url value='/nuova_grafica/'/>images/step-search.png">Ricerca Transfer</span></a></li>
		</c:when>
		<c:otherwise>
			<li data-back="form-step-1" class="step-1 steps first step-active">
			<a style="color: inherit;" href="#" onclick="return false;"><span><img src="<c:url value='/nuova_grafica/'/>images/step-search.png">Ricerca Transfer</span></a></li>
		</c:otherwise>
	</c:choose>
	<c:choose>
		<c:when test="${ricercaTransfert.prenotazione eq true && ricercaTransfert.riepilogo == false}">
			<li data-back="form-step-2" class="step-2 steps second <c:out value="${progressTracker[1]}"/>">
			<a style="color: inherit;" href="javascript:history.back()"><span><img src="<c:url value='/nuova_grafica/'/>images/step-available.png">Auto Disponibili</span></a></li>
		</c:when>
		<c:otherwise>
			<li data-back="form-step-2" class="step-2 steps second <c:out value="${progressTracker[1]}"/>">
			<a style="color: inherit;" href="#" onclick="return false;"><span><img src="<c:url value='/nuova_grafica/'/>images/step-available.png">Auto Disponibili</span></a></li>
		</c:otherwise>
	</c:choose>
	<li data-back="form-step-3" class="step-3 steps third <c:out value="${progressTracker[2]}"/>">
	<a style="color: inherit;" href="#" onclick="return false;"><span><img src="<c:url value='/nuova_grafica/'/>images/step-payment.png">Pagamento</span></a></li>
		
	<li data-back="form-step-4" class="step-4 steps last <c:out value="${progressTracker[3]}"/>">
	<a style="color: inherit;" href="#" onclick="return false;"><span><img src="<c:url value='/nuova_grafica/'/>images/step-summary.png">Riepilogo</span></a></li>
</ul>
</div>

<div class="row ">
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
<div class="row form-row ">
	<c:if test="${ricercaTransfert.tipoServizio == 'PART' or ricercaTransfert.tipoServizio == 'MULTIP'}">
		<%@ include file="/common/ric_particolare.jsp"%>
	</c:if>
	<c:if test="${ricercaTransfert.prenotazione eq false && (ricercaTransfert.tipoServizio == 'ST' || ricercaTransfert.tipoServizio == 'AGA')}">
		<%@ include file="/common/ric_standard.jsp"%>
		<%@ include file="/common/ric_result.jsp"%>
	</c:if>
	<c:if test="${ricercaTransfert.prenotazione eq true && ricercaTransfert.riepilogo eq false && (ricercaTransfert.tipoServizio == 'ST' || ricercaTransfert.tipoServizio == 'AGA')}">
		<%@ include file="/common/ric_result.jsp"%>
		<%@ include file="/common/ric_prenotazione.jsp"%>
		<%@ include file="/common/ric_confirm_sms_customer.jsp"%>
		<%@ include file="/common/ric_payment_medio.jsp"%>
	</c:if>				
	<c:if test="${ricercaTransfert.prenotazione eq true && ricercaTransfert.riepilogo eq true && (ricercaTransfert.tipoServizio == 'ST' || ricercaTransfert.tipoServizio == 'AGA')}">
		<%@ include file="/common/ric_riepilogo.jsp"%>
		<%@ include file="/common/ric_result.jsp"%>
		<%@ include file="/common/ric_prenotazione.jsp"%>
	</c:if>
</div>
</form:form>
</div>
</div>
</div>
<%@ include file="/scripts/ConfirmSubmit.jsp"%>
<!-- bootstrap JS -->
<script async src="<c:url value="/js/bootstrap.min.js"/>"></script>
</body>