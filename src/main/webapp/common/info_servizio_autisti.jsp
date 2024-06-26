<div class="serinfo">
	<!-- <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&#10006;</button> -->
	<p><big><u>
	Nell'attuale periodo di emergenza Covid, le iscrizione degli autisti alla piattaforma <fmt:message key="webapp.apollotransfert.name"/> verranno processate a data da definirsi.
	</u></big></p>

	<p>Stiamo lavorando sull'ottimizzazione e posizionamento di ApolloTransfert sui Motori di Ricerca.</p>

	<!-- INZIO -->
	<p>
	Attualmente vendiamo i servizi in tre modalità:<br>
	1) Facciamo noi il prezzo, i prezzi sono consultabili alla pagina 
	<a href="${pageContext.request.contextPath}/insert-tariffe" style="color: black;">www.apollotransfert.com/insert-tariffe</a> i quali sono da considerare senza il nostro rincaro, 
	il prezzo indicato è il compenso finale che ti pagheremo. Questo tipo di servizio è facoltativo, cioè noi ti proponiamo (a te e ad altri) di fare il servizio e tu sei libero 
	di scegliere di farlo o non farlo. <br><br>

	2) Tramite preventivo, il cliente ti chiederà (attraverso di noi) il prezzo per uno specifico transfer e tu inserirai il tuo prezzo finale e data scadenza preventivo, 
	non devi sommarci la nostra commissione, la aggiungiamo noi. Se il cliente la accetta lui farà il pagamento e tu ricevere una Email e un Sms di conferma, a questo punto sei 
	tenuto ad eseguire il servizio. <br><br>
	
	3) Vendita tramite "agenda autista", è una app raggiungibile alla pagina 
	<a href="${pageContext.request.contextPath}/agenda-autista" style="color: black;">www.apollotransfert.com/agenda-autista</a> in cui devi configurare il tuo tariffario e 
	selezionare i giorni e orari in cui sei disponibile alla vendita, cioè quando vuoi che noi vendiamo per te. Sei tenuto ad eseguire i servizi venduti tramite l'agenda.<br> 
	Video tutorial su come configurare l'agenda: 
	<a href="https://www.youtube.com/playlist?list=PLQ1aJ0zetbCIbZKtChcEeTW8c14MHMleI" style="color: black;" target="_blank">YouTube Video tutorial</a>.<br> 
	Se non riesci a configurare l'agenda telefona telefona al <fmt:message key="cellulare.matteo.esteso"/>.
	<br><br>
	
	<u>Tutti i servizi vengono comunicati tramite Email e via SMS al numero. Quando i servizi sono confermati riceverai una Email e un Sms 
	di conferma. <c:if test = "${not empty user}"> EMAIL: ${user.email} SMS: ${user.phoneNumber}.</c:if></u></p>
	
	<%@ include file="/common/info_fatturazione.jsp"%>
</div>