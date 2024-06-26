<%@ include file="/common/taglibs.jsp"%>
<head>
<link rel="canonical" href="<fmt:message key="https.w3.domain.apollotransfert.name"/><c:url value='/contatti'/>"/>
<title>Van e Auto con Conducente | Roma | Italia</title>
<meta name="description" content="Viaggia in Italia a prezzi vantaggiosi, con la nostra piattaforma di noleggio NCC che ti permette di affittare van e auto in totale sicurezza. 
Vai alle tariffe!"/>
<script src="<c:url value="/scripts/vendor/jquery-ui-1.11.4/external/jquery/jquery-2.2.4.min.js"/>"></script>
</head>
<body>
<div class="page-banner" style="background-image:url('<c:url value='/nuova_grafica/'/>images/contact-us.png');">
	<h1>Contatti</h1>
</div>
<div class="content-area">
<div class="container">
<div class="col-md-12">
	<%@ include file="/common/messages.jsp" %>
	
	<div class="main-heading-row"><h1 class="text-center">La soluzione veloce e affidabile per il noleggio NCC a Roma e in Italia</h1></div>
	
	
	<p>Sei un autista e lavori con il tuo mezzo in proprio? <br>
	Sei titolare di un'agenzia di viaggio e vorresti acquistare pacchetti di transfert per i tuoi clienti? <br>
	Sei il proprietario di un sito turistico e ti piacerebbe estendere ulteriormente i tuoi servizi? <br>
	<b><fmt:message key="webapp.apollotransfert.name"/></b> ha pensato a questo e molto altro. La nostra piattaforma di noleggio NCC ti consente di lavorare come <b>autista privato</b> o 
	come <b>agenzia</b>, offrendoti la possibilità di guadagnare e aumentare il numero dei tuoi clienti. <br>
	Se invece <b>sei titolare di un sito internet con servizi turistici</b>, inserendo <b><fmt:message key="webapp.apollotransfert.name"/></b> tra le aziende collaboratrici potrai guadagnare 
	da ogni corsa acquistata, in modo rapido e sicuro.</p>
	
	<h3 class="text-center"><strong>Non esitare a contattarci per richiedere maggiori informazioni, siamo sempre a tua disposizione anche su WhatsApp!</strong></h3>
	
	<div class="col-md-12 ">
		<form method="post" action="<c:url value='/'/><fmt:message key="language.code"/>/contatti" id="contattiFormId" class="">
			<div class="conform">
				<c:choose>
				<c:when test="${not empty userFullName}">
					<div class="form-group ">
						<p class="h4"><strong>Salve ${userFullName}, ti risponderemo a ${userEmail}</strong></p> 
				   	</div>
				</c:when>
				<c:otherwise>
					<input type="text" value="${NomeMittente}" name="cliente-firstName" id="clienteFirstName" ${emailInviata ? 'disabled' : ''} maxlength="65" 
					placeholder="Nome" class="form-control" required>
					<input type="text" value="${EmailMittente}" name="cliente-email" id="clienteEmail" ${emailInviata ? 'disabled' : ''} maxlength="65" 
					placeholder="Email" class="form-control" required>
				</c:otherwise>
				</c:choose>
				<textarea type="text" name="text-message" ${emailInviata ? 'disabled' : ''} rows="6" class="form-control" 
					placeholder="Scrivi Messaggio" required>${textMessaggio}</textarea>
	  		</div>
	  		<div class="form-group">
		  		<label class="checkbox-inline"><input type="checkbox" name="check-privacy-policy" ${checkPrivacyPolicy ? 'checked' : ''} required>
				<fmt:message key="privacy.policy.desc"><fmt:param value="${pageContext.request.contextPath}/privacy-policy"/></fmt:message></label>
	  		</div>
	  		<div class="form-group">
	  			<div class="g-recaptcha" data-sitekey="${RECAPTCHA_PUBLIC_GLOBAL}" data-size="invisible" data-callback="onSubmit"></div>
	  			<button id="inviaMessaggio" type="submit" class="btn btn-primary" ${emailInviata ? 'disabled="disabled"' : ''}>Invia Messaggio</button>
	  			<a href="<c:url value="/contatti"/>" class="btn btn-primary">Nuovo Messaggio</a>
			</div>
			<div class="form-group">
				
			</div>
		</form>
	</div>

</div>
</div>
</div>

<script type="text/javascript">
jQuery(document).ready(function(){

});

// serve per velocizzare la pagina al caricamento
$("#inviaMessaggio").attr("disabled", true); 
setTimeout(function(){
	jQuery.ajax({
		url: 'https://www.google.com/recaptcha/api.js?hl=<fmt:message key="language.code"/>',
	    dataType: 'script',
		success: function(result) { 
			$("#inviaMessaggio").attr("disabled", false); 
		},
		async: true
	});
}, 3000); //3000 //4000

//alert('<fmt:message key="language.code"/>');
$('#contattiFormId').submit(function (event) {
    event.preventDefault();
    //grecaptcha.reset(); //per aggiornare la recaptcha
    grecaptcha.execute();
});
function onSubmit(token) {
	//$("#attesaLoader").modal('show');
	$(".attesaLoaderPopUp").fadeIn(300);
	var form = document.getElementById('contattiFormId');
	$('<input>').attr({type:'hidden', id:'foo',name:'invia-messaggio'}).appendTo(form);
	document.getElementById("contattiFormId").submit();
}
</script>
	
<%@ include file="/scripts/ConfirmSubmit.jsp"%>
<!-- bootstrap JS -->
<script async src="<c:url value="/js/bootstrap.min.js"/>"></script>
</body>