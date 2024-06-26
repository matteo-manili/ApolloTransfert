<%@ include file="/common/taglibs.jsp"%>
<head>
	<title>Scrivi Recensione</title>
	<meta name="menu" content="Home"/>

	<!-- jquery -->
	<script src="<c:url value="/scripts/vendor/jquery-ui-1.11.4/external/jquery/jquery-2.2.4.min.js"/>"></script>
	<script src="<c:url value="/js/bootstrap.min.js"/>"></script>
	
	<c:if test="${ registrazioneEseguita eq true }">

	</c:if>
	<!-- Google Code for Registrazione Autista Eseguita Conversion Page -->
</head>

<%
try {
%>

<body>
<div class="content-area home-content">
<div class="container">
<div class="">
	
	<%@ include file="/common/messages.jsp" %>
	<c:choose>
	<c:when test = "${ not empty user && fn:length(recensioneTransferUser.ricercaTransfertList_Approvati) gt 0 }">

		<div class="main-heading-row">
		<h2><fmt:message key="home.heading"/>, 
			<c:choose>
		        <c:when test="${not empty user.firstName && not empty user.lastName}">
		           ${user.fullName}
		        </c:when>
		        <c:when test="${empty user.firstName || empty user.lastName}">
		           <small><c:set value="${user.phoneNumber}" var="phone"/>
		           <c:out value="${fn:substring(phone, 0, 3)}.${fn:substring(phone,3,6)}.${fn:substring(phone,6,9)}.${fn:substring(phone,9,15)}"/>&nbsp;
		           <a href="<c:url value='userform'/>" class="alert-link">(inserisci nome e cognome)</a></small>
		        </c:when>
		     </c:choose>
		</h2>
		</div>
		
		<h3>
			<p>${fn:length(recensioneTransferUser.ricercaTransfertList_Approvati) gt 1 ? 'Di seguito sono elencati i Servizi Transfers che hai acquistato presso il nostro portale' 
			: 'Di seguito è presente il Servizio Transfer che hai acquistato presso il nostro portale'}, Ti chiediamo di scrivere una rencensione per aiutarci a migliorare 
			i nostri Servizi.</p>
			
			<p>Con l'inserimento ${fn:length(recensioneTransferUser.ricercaTransfertList_Approvati) gt 1 ? 'di almeno una' : 'della'} Recensione <b>attiverai il Codice Sconto</b> del 
			<b>${recensioneTransferUser.percentualeSconto}%</b> per il prossimo Servizio Transfer che acquisterai!<br>
			Il Codice sconto da inserire nel momento dell'acquisto è: <b><ins>${recensioneTransferUser.codiceSconto}</ins></b></p>
			
			<p>Sul Nostro Pannello delle Recensioni sarà visualizzato solamente il tuo nome (${user.firstName}), la email (${user.email}) e il testo della Recensione.</p>
			
			<p>Grazie da ApolloTransfert</p>
		</h3>
		
		<h3><ins>${fn:length(recensioneTransferUser.ricercaTransfertList_Approvati) gt 1 ? 'Servizi Acquistati' : 'Servizio Acquistato'}</ins></h3>
		
		<form method="post" action="<c:url value='/scrivi-recensione'/>" class="">
		<input type="hidden" name="url-token-recensione" value="${recensioneTransferUser.urlTockenPageScriviRecensone}">
		<div class="conform">
		<c:forEach items="${recensioneTransferUser.ricercaTransfertList_Approvati}" var="ite" varStatus="loop">
			<p><b>ID Transfer:</b> ${ite.id} | <b>${ite.ritorno? 'Andata e Ritorno' : 'Solo Andata'}</b> | <b>Giorno:</b> <fmt:formatDate pattern="dd MMMM yyyy, HH:mm" value="${ite.dataOraPrelevamentoDate}" /> 
			| <b>Partenza:</b> ${ite.partenzaRequest}&nbsp;<i class="fa fa-arrow-right" aria-hidden="true"></i>&nbsp;<b>Arrivo:</b> ${ite.arrivoRequest}  
			<p>
			
			<input type="hidden" name="id-transfer" value="${ite.id}">
			
			<p><select class="form-control" name="stella-punteggio-${ite.id}" ${ite.recensioneApprovata ? 'disabled' : ''}>
				<option value="0" ${empty ite.punteggioStelleRecensione ? 'selected' : ''}>Indica il punteggio da 1 a 5 Stelle</option>
				<option value="1" ${not empty ite.punteggioStelleRecensione && ite.punteggioStelleRecensione == 1 ? 'selected' : ''}>1 Stella</option>
				<c:forEach var="i" begin="2" end="5">
					<option value="${i}" ${not empty ite.punteggioStelleRecensione && ite.punteggioStelleRecensione == i ? 'selected' : ''}>${i} Stelle</option>
					
				</c:forEach>
			</select>
			</p>
			
			<p><textarea type="text" name="text-recensione-${ite.id}" ${ite.recensioneApprovata ? 'disabled' : ''} rows="6" class="form-control" 
				placeholder="Scrivi Recensione">${ite.recensione}</textarea>
			</p>
			
			<c:if test="${ite.recensioneApprovata}"><p><b>Recensione Approvata</b></p></c:if>
	
			<c:if test="${not ite.recensioneApprovata}">
			<p><button type="submit" name="salva-recensione" class="btn btn-md btn-primary" ${ite.recensioneApprovata ? 'disabled="disabled"' : ''}>Salva Recensione</button>
			</p></c:if>
			 
			<c:if test="${!loop.last}"><hr></c:if>  
		
		</c:forEach>
		</div>
		</form>
	</c:when>
	<c:otherwise>
		<h2>Pagina Recesioni non disponibile<h2>
	</c:otherwise>
	</c:choose>

</div>
</div>
</div>

<%@ include file="/scripts/ConfirmSubmit.jsp"%>
</body>

<%		 
}
catch (Exception e) {
out.println("An exception occurred: " + e.getMessage());
}
%>