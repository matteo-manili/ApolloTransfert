<%@ include file="/common/taglibs.jsp"%>
<head>
<link rel="canonical" href="https://www.apollotransfert.com/chi-siamo"/>
<title>Servizi Transfer NCC | Roma | Italia</title>
<meta name="description" content="Stai organizzando i tuoi viaggi in Italia? La nostra piattaforma online ti offre servizi transfer NCC per trasporti standard e speciali. 
Prenota subito!"/>
<script src="${pageContext.request.contextPath}/scripts/vendor/jquery-ui-1.11.4/external/jquery/jquery-2.2.4.min.js"></script>
<script type="text/javascript">
/* */
//------------------- CHIAMATA AJAX 1 ------------------------
var callAjax_1 = new XMLHttpRequest();
callAjax_1.onreadystatechange = function() {
    if (callAjax_1.readyState == XMLHttpRequest.DONE ) {
       if (callAjax_1.status == 200) {
var data = JSON.parse(callAjax_1.responseText);
var totaleAutisti = data.totaleAutisti;
var t = document.createTextNode( totaleAutisti );
document.getElementById("totaleAutisti").appendChild(t);
// var html = "<div class='panel panel-success'>"+
// "<div class='panel-heading'><strong><big>"+ totaleAutisti +"</big></strong> Autisti Certificati per Provincia da Febbraio 2017</div>"+
// "<div class='panel-body'><ul class='list-group row '>";
var html = "<div class='rectable'>"
+"<div><h3><strong>"+ totaleAutisti +"</strong> Autisti Certificati per Provincia da Febbraio 2017 <span style='float:right;' onclick='showtable()'>visualizza/nascondi &#9660;</span></h3></div>"
+"<table id='tableAutistiCert' class='table table-responsive table-striped ' style='display: none;'>"
+"<thead><tr><th>Name</th><th>Quantity</th><th>Name</th><th>Quantity</th></tr></thead><tbody>";
var div = document.createElement('div');
div.innerHTML = html;
document.getElementById('divListAutistiPerProvincia').appendChild(div);
// rimuovo gli elementi che hanno numero di autisti > 0 e ricreo l'array "arrProvincePulite"
var arrProvincePulite = []; var index = 0;
for(var i = 0; i < data.autistiProvincia.length; i++) {
	if( data.autistiProvincia[i].autisti.length > 0 ) {
		arrProvincePulite.splice(index, 0, data.autistiProvincia[i]);
		index++;
	}
}
for(var i = 0; i < arrProvincePulite.length; i++) {
	html = html + "<tr>";
	html = html + "<td>"+arrProvincePulite[i].nomeProvincia+"</td><td>"+arrProvincePulite[i].autisti.length+"</td>";
	if(i+1 < arrProvincePulite.length){
		i++;
		html = html + "<td>"+arrProvincePulite[i].nomeProvincia+"</td><td>"+arrProvincePulite[i].autisti.length+"</td>";
	}else{
		html = html + "<td> - </td><td> - </td>";
	}
	html = html + "</tr>";
}
html = html + "</tbody></table></div>";
div.innerHTML = html;
document.getElementById('divListAutistiPerProvincia').appendChild(div);
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

function showtable() { 
	var x = document.getElementById("tableAutistiCert");
	if (x.style.display === "none") {
		x.style.display = "block";
	} else {
		x.style.display = "none";
	}
}
</script>
</head>
<body>
<div class="page-banner" style="background-image:url('${pageContext.request.contextPath}/nuova_grafica/images/main-banner/pexels-torsten-dettlaff-70912.jpg');">
<h1>Chi siamo</h1>
</div>

<div class="content-area">
<div class="container">
<div class="col-md-12">
	
	<div class="main-heading-row">
		<h1 class="text-center">La piattaforma di servizi transfer NCC con autisti in tutta Italia</h1>
		<h2 class="text-center">La soluzione giusta per i tuoi spostamenti sul territorio nazionale</h2>
	</div>
	
	<p><b><fmt:message key="webapp.apollotransfert.name"/></b> è un portale web che mette a tua disposizione servizi di <b>transfer NCC</b> a Roma e in <b>ogni regione italiana</b>. La nostra 
	piattaforma ti aiuta a metterti in contatto con i migliori autisti italiani che offrono servizi di <b>viaggio standard o speciali</b>. Se hai bisogno di trasportare attrezzature 
	sportive o animali da compagnia, rivolgiti ai nostri professionisti attivi in tutta Italia.</p>
	
	<p>Rivolgendoti ad <b><fmt:message key="webapp.apollotransfert.name"/></b>, potrai prenotare dei servizi di Transfer NCC con trasporti in <b>auto</b> o <b>van</b>, a seconda del numero di 
	passeggeri. Il nostro servizio di prenotazioni <b>nasce nel 2018</b> per rispondere alle esigenze di turisti, professionisti e passeggeri in gran parte delle province italiane.</p>
	
	<p>Attualmente <b><fmt:message key="webapp.apollotransfert.name"/></b> si avvale della collaborazione di <b><span id="totaleAutisti"></span> autisti con regolare certificato</b> di abilitazione professionale, iscrizione 
	all'albo del ruolo dei conducenti e licenza comunale NCC. I nostri collaboratori sono organizzarti in base al loro territorio di riferimento e con veicoli di proprietà per 
	offrirti un servizio efficiente, comodo, gestibile online e <b>sempre personalizzato</b>.</p>
	
	<div class="row" style="margin-top: 25px;">
		<div class="col-xs-12 col-sm-6 col-md-6 col-lg-6" >
			<div class="thumbnail" style="background-color: #D0D0D0;">
			<div class="caption">
				<h3><strong>Metodi di pagamento:</strong></h3>
				<p>Acquistare un servizio di noleggio con conducente non è mai stato così semplice. Con <fmt:message key="webapp.apollotransfert.name"/> puoi scegliere se acquistare un servizio 
				Standard attraverso la nostra piattaforma online, pagando con carta di credito Visa o Mastercard oppure con il circuito PayPal. Nel caso di un trasporto NCC con condizioni 
				speciali di viaggio, come un gruppo di passeggeri o il trasporto di animali da compagnia, riceverai dei preventivi direttamente dagli autisti della tua area di riferimento. 
				Potrai pagare con carta di credito Visa e Mastercard, oppure con PayPal.</p>
			</div>
			</div>
		</div>
		<div class="col-xs-12 col-sm-6 col-md-6 col-lg-6" >
			<div class="thumbnail" style="background-color: #D0D0D0;">
			<div class="caption">
				<h3><strong>Condizioni di Viaggio:</strong></h3>
				<ul>
				<li>Download pdf della fattura.</li>
				<li>In fattura: nome e cognome dell'autista, telefono cellulare, marca, modello e targa dell'autoveicolo.</li>
				<li>Nome e telefono del passeggero.</li>
				<li>È possibile cancellare e ricevere il rimborso fino a 36 ore prima del servizio di noleggio.</li>
				<li>Notifica SMS 24 ore e 5 ore prima del trasferimento.</li>
				<li>In caso di ritardo da parte del passeggero, l'autista chiederà un supplemento di 10 euro in contanti per 30 minuti di ritardo, 15 euro pagando con carta.</li>
				<li>Le tappe intermedie richiederanno un supplemento.</li>
				</ul>
			</div>
			</div>
		</div>
	</div>

	<div class="row" style="margin-top: 25px;">
		<div class="col-xs-12 col-sm-6 col-md-6 col-lg-6" >
			<div class="text-center "><a href="<c:url value='/'/>" class=" btn btn-primary">PRENOTA</a></div>
		</div>
		<div class="col-xs-12 col-sm-6 col-md-6 col-lg-6" >
			<div class="text-center "><a href="<c:url value='/contatti'/>" class="btn btn-primary ">CONTATTACI</a></div>
		</div>
	</div>
	
	<div id="divListAutistiPerProvincia"></div> 
	
</div>
</div>
</div>
<script src="${pageContext.request.contextPath}/scripts/vendor/bootstrap-without-jquery.min.js"></script>
</body>