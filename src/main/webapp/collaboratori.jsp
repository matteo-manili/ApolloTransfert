<%@ include file="/common/taglibs.jsp"%>
<head>
<meta name="menu" content="LavoraConNoi"/>
<link rel="canonical" href="https://www.apollotransfert.com/collaboratori"/>
<title>Autisti NCC | Roma | Italia</title>
<meta name="description" content="Se un autista NCC o un'agenzia di noleggio auto con conducente? Inizia a collaborare con la nostra piattaforma online, in tutta Italia. 
Scopri di più!">
<script src="${pageContext.request.contextPath}/scripts/vendor/jquery-ui-1.11.4/external/jquery/jquery-2.2.4.min.js"></script>
<script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
<script type="text/javascript">
	//------------------- CHIAMATA AJAX 1 ------------------------
   var callAjax_1 = new XMLHttpRequest();
   callAjax_1.onreadystatechange = function() {
       if (callAjax_1.readyState == XMLHttpRequest.DONE ) {
          if (callAjax_1.status == 200) {
       	   
			var data = JSON.parse(callAjax_1.responseText);

			var html = "<div class='rectable'>"
			+"<h3><strong>"+ data.totaleAutisti +"</strong> Autisti Registrati per Regione da Febbraio 2017</h3>"
			+"<table id='example' class='table' style='width:100%'>"
			+"<thead><tr><th>Name</th><th>Quantity</th><th>Name</th><th>Quantity</th></tr></thead><tbody>";
			
			var div = document.createElement('div');
			div.innerHTML = html;
			document.getElementById('divListAutistiPerRegione').appendChild(div);

			for(var i = 0; i < data.autistiRegione.length; i++){
				html = html + "<tr>";
				html = html + "<td>"+data.autistiRegione[i].nomeRegione+"</td><td>"+data.autistiRegione[i].autisti.length+"</td>";
				if(i+1 < data.autistiRegione.length){
					i++;
					html = html + "<td>"+data.autistiRegione[i].nomeRegione+"</td><td>"+data.autistiRegione[i].autisti.length+"</td>";
				}else{
					html = html + "<td> - </td><td> - </td>";
				}
				html = html + "</tr>";
			}
			
			html = html + "</tbody></table></div>";
			div.innerHTML = html;
			document.getElementById('divListAutistiPerRegione').appendChild(div);
          }
          else if (callAjax_1.status == 400) {
       	   //alert('errore ajax CollaboratoriRegioneAutistaList');
          }
          else {
       	   //alert('errore ajax CollaboratoriRegioneAutistaList 222');
          }
       }
   };
   callAjax_1.open("POST", "${pageContext.request.contextPath}/CollaboratoriRegioneAutistaList", true);
   callAjax_1.send();
   
   //------------------- CHIAMATA AJAX 2 ------------------------
   var callAjax_2 = new XMLHttpRequest();
   callAjax_2.onreadystatechange = function() {
      	if (callAjax_2.readyState == XMLHttpRequest.DONE ) {
          	if (callAjax_2.status == 200) {
       	   
       	   	//alert(callAjax_2.responseText);
       	   	var data = JSON.parse(callAjax_2.responseText)
       	   
			var t = document.createTextNode( data.margineOreMinimoCorsaMedia );
       	    document.getElementById("margineOreMinimoCorsaMedia").appendChild(t);
       	    
       	    var t = document.createTextNode( data.numAutistiCorsaMedia );
       	    document.getElementById("numAutistiCorsaMedia").appendChild(t);
       	    
       	    var t = document.createTextNode( data.percentualeServizio );
       	    document.getElementById("percentualeServizio1").appendChild(t);
       	    
       	 	var t = document.createTextNode( data.percentualeServizio );
    	    document.getElementById("percentualeServizio2").appendChild(t);
          }
          else if (callAjax_2.status == 400) {
       	   //alert('errore ajax CollaboratoriInfoCorsaMedia');
          }
          else {
       	   //alert('errore ajax CollaboratoriInfoCorsaMedia 222');
          }
       }
   };
   callAjax_2.open("POST", "${pageContext.request.contextPath}/CollaboratoriInfoCorsaMedia", true);
   callAjax_2.send();
</script>
</head>

<body>
<div class="page-banner" style="background-image:url('${pageContext.request.contextPath}/nuova_grafica/images/main-banner/conduzir.png');">
	<h1>Diventa un&apos;autista di <fmt:message key="webapp.apollotransfert.name"/></h1>
</div>

<div class="content-area">
<div class="container">
<div class="col-md-12">


<p><b><fmt:message key="webapp.apollotransfert.name"/></b> ti offre la possibilità di lavorare come autista con la nostra piattaforma di noleggio con conducente. Il nostro portale 
web mette in contatto i viaggiatori con gli autisti <b>in ogni regione italiana</b>. Se sei un <b>autista</b> oppure un'<b>agenzia con autoveicoli</b> di proprietà e sei in possesso 
della licenza comunale NCC, registrati alla nostra piattaforma e inizia a collaborare con noi.</p>


<h3 class="text-center"><b>Se sei un Autistao una Azienda/Cooperativa con Autoveicolo/i e Licenza/e Comunale NCC 
<a href="${pageContext.request.contextPath}/signup?tipoUser=autista">Registrati e Collabora con noi</a></b></h3>

<div class="panel-group" id="accordion">
		
	<div class="panel panel-default">
		<div class="panel-heading-apollon">
			<h4 class="panel-title">
				<a data-toggle="collapse" data-parent="#accordion" href="#col1">Come funziona ApolloTransfert?</a>
			</h4>
		</div>
		<div id="col1" class="panel-collapse collapse in">
			<div class="panel-body-white">
				<p>Tramite la <a target="_blank" href="${pageContext.request.contextPath}/">Funzione di Ricerca</a> 
				ApolloTransfert offre al Cliente Servizi Transfer acquistabili Online.<br>
				Relativamente alla Provincia dove si effetuer&agrave; il prelevamento 
				e alla Categoria Auto scelta, il Sistema Contatter&agrave; gli Autisti iscritti e Approvati nel Portale.<br>
				Nel momento in cui il Ciente acquista la Corsa, il Sistema la comunica all'Autista, quindi eseguita la Corsa
				ApolloTransfert provveder&agrave; a pagare l'Autista.</p>
			</div>
		</div>
	</div>
	
	<div class="panel panel-default">
		<div class="panel-heading-apollon">
			<h4 class="panel-title">
				<a data-toggle="collapse" data-parent="#accordion" href="#col6">Quali requisiti sono necessari per collaborare con ApolloTransfert?</a>
			</h4>
		</div>
		<div id="col6" class="panel-collapse collapse">
			<div class="panel-body-white">
				<p><b>Gli Autisti Professionisti devono disporre di:</b><br>
				- Partita IVA <br>
				- Codice IBAN del proprio Conto Corrente<br>
				- Licenza NCC Comunale<br>
				- Iscrizione al Ruolo e CAP<br>
				- Carta di Circolazione di autoveicolo adibito a uso di Terzi da Noleggio con Conducente.</p>
				
				<p><b>Le Aziende e le Cooperative devono disporre di:</b><br> 
				- Documento Patente del Responsabile Aziendale o del Presidente della Cooperativa<br>
				- Partita IVA<br>
				- Codice IBAN del Conto Corrente<br>
				- Licenza/e NCC Comunale<br>
				- Almeno un Dipendente o Associato (con Iscrizione al Ruolo e CAP)<br> 
				- Almeno un autoveicolo con Carta di Circolazione adibita a uso di Terzi da Noleggio con Conducente.</p>
				
				<p><b>A Conclusione dell'inserimento dei Dati e dei Documenti questi verranno Verificati, e quando Approvati, si contrattualizzer&agrave; 
				la Collaborazione attraverso una Scrittura Privata tra il Collaboratore e ApolloTransfert.</b></p>
			</div>
		</div>
	</div>
	
	<div class="panel panel-default">
		<div class="panel-heading-apollon">
			<h4 class="panel-title">
				<a data-toggle="collapse" data-parent="#accordion" href="#col2">Come viene comunicata la Corsa?</a>
			</h4>
		</div>
		<div id="col2" class="panel-collapse collapse">
			<div class="panel-body-white">
				<p>Le Corse vengono comunicate all'Autista tramite SMS ed EMAIL almeno <span id="margineOreMinimoCorsaMedia"></span> 
				ore prima del prelevamento del Cliente.</p>
				<p>Il Sistema comunicher&agrave; la Corsa a minimo <span id="numAutistiCorsaMedia"></span> 
				autisti presenti sul territorio, il primo di essi che la confermer&agrave; si aggiudicher&agrave; la Corsa.</p>
				<p>L'autista NCC che si aggiudicher&agrave; la corsa disporr&agrave; della copia della fattura che dimostrer&agrave; l'ora e 
				il giorno della prenotazione della Corsa.</p>
			</div>
		</div>
	</div>
	
	<div class="panel panel-default">
		<div class="panel-heading-apollon">
			<h4 class="panel-title">
				<a data-toggle="collapse" data-parent="#accordion" href="#col3">In che Ordine vengono inviati gli Avvisi SMS ed Email delle Corse, agli Autisti?</a>
			</h4>
		</div>
		<div id="col3" class="panel-collapse collapse">
			<div class="panel-body-white">
				<p>Gli Avvisi delle Corse Disponbili vengono inviati uno alla volta tramite SMS ed Email agli Autisti a partire dall'acquisto del Transfer 
				- ogni 5 minuti - nel seguente ordine:<br> 
				1) L'Autista con meno corse eseguite.<br>
				2) L'Autista Registrato da pi&ugrave; tempo al portale.</p>
			</div>
		</div>
	</div>
	
	<div class="panel panel-default">
		<div class="panel-heading-apollon">
			<h4 class="panel-title">
				<a data-toggle="collapse" data-parent="#accordion" href="#col5">Come si svolge il Pagamento e la Fatturazione? Che percentuale prende ApolloTransfert?</a>
			</h4>
		</div>
		<div id="col5" class="panel-collapse collapse">
			<div class="panel-body-white">
				<p>L'autista (o la Azienda o Cooperativa) successivamente eseguita la Corsa ricever&agrave; in 2/3 giorni il suo compenso tramite bonifico,
				e quindi emetter&agrave; fattura ad ApolloTransfert.</p>
				<p>ApolloTransfert non prende alcuna percentuale dal compenso dell'Autista perch&eacute; il costo del
				servizio viene aggiunto al prezzo finale offerto al Cliente, ed &egrave; il <span id="percentualeServizio2"></span>&#37;.</p>
			</div>
		</div>
	</div>
	
	<div class="panel panel-default">
		<div class="panel-heading-apollon">
			<h4 class="panel-title">
				<a data-toggle="collapse" data-parent="#accordion" href="#col4">Come vengono impostate le Tariffe?</a>
			</h4>
		</div>
		<div id="col4" class="panel-collapse collapse">
			<div class="panel-body-white">
				<p>Le Tariffe &euro;/km sono impostate da ApolloTransfert secondo la Provincia dove si effettua il prelievo della Corsa, 
				la Tipologia di autoveicolo (Van o Auto), l'Anno di Immatricolazione e il Modello Auto.</p>
				
				<p>Le Tariffe Autista al Lordo sono consultabili in questa pagina scalando il <span id="percentualeServizio1"></span>&#37; 
				<big><a href="${pageContext.request.contextPath}/tariffe-transfer">Tariffe Cliente</a></big></p>
				
				<p>Le Tariffe Autista al Netto sono consultabili in questa pagina (accessibile previa Registrazione) 
				<big><a href="${pageContext.request.contextPath}/insert-tariffe">Tariffe Autista</a></big></p>
			</div>
		</div>
	</div>
</div> <!-- fine accordion -->


<div class="jumbotron" style="background-color: #1c74bb;">
	<div class="row" >
		<h2 class="text-center title-striscia-blue">Modalità di Erogazione dei servizi di NCC:</h2>
		<div class="col-xs-12 col-sm-6 col-md-6 col-lg-4">
			<div class="thumbnail">
			<div class="caption">
				<p><h2><b>1.</b></h2> <b>Facciamo noi</b> il prezzo, i prezzi sono consultabili alla pagina 
				<a href="${pageContext.request.contextPath}/insert-tariffe">www.apollotransfert.com/insert-tariffe</a> i quali sono da considerare senza il nostro rincaro, 
				il prezzo indicato è il compenso finale che ti pagheremo. Questo tipo di servizio è <b>facoltativo</b>, cioè noi ti proponiamo (a te e ad altri) di fare il servizio e tu sei libero 
				di scegliere di farlo o non farlo.</p>
			</div>
			</div>
		</div>
		<div class="col-xs-12 col-sm-6 col-md-6 col-lg-4">
			<div class="thumbnail">
			<div class="caption">
				<p><h2><b>2.</b></h2> Tramite <b>preventivo</b>, il cliente ti chiederà (attraverso di noi) il prezzo per uno specifico transfer e tu inserirai il tuo prezzo finale e data scadenza preventivo, 
				non devi sommarci la nostra commissione, la aggiungiamo noi. Se il cliente la accetta lui farà il pagamento e tu ricevere una Email e un Sms di conferma, a questo punto sei 
				tenuto ad eseguire il servizio.</p>
			</div>
			</div>
		</div>
		<div class="col-xs-12 col-sm-6 col-md-6 col-lg-4">
			<div class="thumbnail">
			<div class="caption">
				<p><h2><b>3.</b></h2> Vendita tramite <b>"agenda autista"</b>, è una app raggiungibile alla pagina 
				<a href="${pageContext.request.contextPath}/agenda-autista">www.apollotransfert.com/agenda-autista</a> in cui devi configurare il tuo tariffario e 
				selezionare i giorni e orari in cui sei disponibile alla vendita, cioè quando vuoi che noi vendiamo per te. Sei tenuto ad eseguire i servizi venduti tramite l'agenda.<br> 
				<b>Video tutorial</b> su come configurare l'agenda: 
				<a href="https://www.youtube.com/playlist?list=PLQ1aJ0zetbCIbZKtChcEeTW8c14MHMleI" target="_blank">YouTube Video tutorial</a>.<br> 
				Se non riesci a configurare l'agenda telefona telefona al <b><fmt:message key="cellulare.matteo.esteso"/></b>.</p>
			</div>
			</div>
		</div>
	</div>
</div>


<div class="text-center"><a href="${pageContext.request.contextPath}/signup?tipoUser=autista" class="btn btn-primary text-center">REGISTRATI</a></div>


<!-- LISTA AUTISTA PER REGIONE -->
<div id="divListAutistiPerRegione"></div>


</div>
</div>
</div>
</body>