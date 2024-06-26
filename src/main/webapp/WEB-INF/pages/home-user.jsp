<%@ include file="/common/taglibs.jsp"%>
<head>
	<title><fmt:message key="home.title"/></title>
	<!-- jquery -->
	<script src="<c:url value="/scripts/vendor/jquery-ui-1.11.4/external/jquery/jquery-2.2.4.min.js"/>"></script>
	<script src="<c:url value="/js/bootstrap.min.js"/>"></script>
	<script src="<c:url value="/js/mustache.js"/>"></script>
	<script src="<c:url value="/scripts/vendor/bootbox.min.js"/>"></script>
	<link rel="stylesheet" href="<c:url value="/css/blinkeffect.css"/>">
	<script type="text/javascript">
	$(document).ready(function () {
		$(document).on("click", ".pagamentoRitardo", function(e) {
			var idcorsa = $(this).attr('id');
			window.open('${pageContext.request.contextPath}/pagamentoRitardo?courseId='+idcorsa,'_self');
		});
		
		$(document).on("click", ".pagamentoSupplemento", function(e) {
			var idSupplemento = $(this).attr('id');
			window.open('${pageContext.request.contextPath}/pagamentoSupplemento?idSupplemento='+idSupplemento,'_self');
		});
	}); // fine ready
	</script>
</head>
<body>
<div class="content-area home-content">
<div class="container">
<div class="">
	<%@ include file="/common/messages.jsp" %>
	<c:if test="${fn:contains(tipoRuoliList, 'AUTISTA_ROLE') && autista.attivo }">
	  		<%@ include file="/common/menu_autista.jsp"%>
	</c:if>
	<div class="main-heading-row">
	<h3><fmt:message key="home.heading"/>, 
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
	</h3>
	</div>
	<c:if test="${pageContext.request.isUserInRole('ROLE_VENDITORE')}">

	</c:if>

	<c:if test="${pageContext.request.isUserInRole('ROLE_ADMIN')}">
		<ul class="list-inline">
			<li><a href="<c:url value='/admin/admin-gestioneSupplementi'/>">GESTIONE SUPPLEMENTI</a></li>
		  	<li><a href="<c:url value='/admin/admin-gestioneRitardi'/>">GESTIONE RITARDI</a></li>
		  	<li><a href="<c:url value='/admin/admin-gestioneRimborsi'/>">GESTIONE RIMBORSI</a></li>
		  	<li><a href="<c:url value='/admin/admin-vediFatture'/>">VEDI FATTURE</a></li>
		  	<li><a href="<c:url value='/admin/admin-recensioni'/>">RECENSIONI</a></li>
		</ul>
	</c:if>

	<c:if test="${pageContext.request.isUserInRole('ROLE_GEST_AUTISTA')}">
		<ul class="list-inline">
		  <li><a href="<c:url value='/admin/gestioneAutista'/>">GESTIONE AUTISTI</a></li>
		  <li><a href="<c:url value='/admin/admin-gestioneCorse'/>">GESTIONE CORSE</a></li>
		</ul>
	</c:if>
	
	<c:if test="${pageContext.request.isUserInRole('ROLE_COMMERCIALISTA')}">
		<ul class="list-inline">
			<li><a href="<c:url value='/admin/admin-vediFatture'/>">VEDI FATTURE</a></li>
		</ul>
	</c:if>
	
	<c:if test="${fn:contains(tipoRuoliList, 'CLIENTE_ROLE') && not empty recensioneTransferUser && not empty recensioneTransferUser.ricercaTransfertList_Approvati}">
		<p>Con l'inserimento ${fn:length(recensioneTransferUser.ricercaTransfertList_Approvati) gt 1 ? 'di almeno una' : 'della'} Recensione <b>attiverai il Codice Sconto</b> del 
			<b>${recensioneTransferUser.percentualeSconto}%</b> per il prossimo Servizio Transfer che acquisterai!<br>
			Il Codice sconto da inserire nel momento dell'acquisto è: <b><ins>${recensioneTransferUser.codiceSconto}</ins></b></p>
		<p>CODICE SCONTO RECENSIONE ATTIVO = ${recensioneTransferUser.codiceScontoAttivo  ? 'SI' : 'NO'}
		<c:if test="${recensioneTransferUser.codiceScontoAttivo}">
				<br>CODICE SCONTO RECENSIONE USATO = ${recensioneTransferUser.codiceScontoUsato  ? 'SI' : 'NO'} </c:if>
		</p>
		<ul class="list-inline">
			<li><a href="<c:url value='/scrivi-recensione?token=${recensioneTransferUser.urlTockenPageScriviRecensone}'/>">
				${fn:length(recensioneTransferUser.ricercaTransfertList_Approvati) gt 1 ? 'SCRIVI RECENSIONI' : 'SCRIVI RECENSIONE'}</a></li>
		</ul>
	</c:if>
	
	<hr>

	<c:if test="${not empty mapLinkInfoMancanti}">
		<div class="alert alert-info h4" role="alert">
		<strong>Infomazioni Mancanti!</strong> Per favore, inserisci le seguenti infomazioni.
		<hr class="message-inner-separator">
		<ul>
			<c:forEach items="${mapLinkInfoMancanti}" var="link" >
	    		<li><a href="<c:url value='${link.key}'/>" class="alert-link">${link.value}</a></li>
	    		<c:if test = "${fn:contains(link.key, 'insert-documenti')}">
		    		<ul>
					<c:forEach var="listaDocumentiObj" items="${listaDocumenti}">
		    			<li><em>${listaDocumentiObj}</em></li>
		    		</c:forEach>
					</ul>
	    		</c:if>
			</c:forEach>
		</ul>
		</div>
	</c:if>
	
	<c:if test="${(fn:contains(tipoRuoliList, 'AUTISTA_ROLE') && autista.attivo) || pageContext.request.isUserInRole('ROLE_ADMIN')}">
		<%@ include file="/common/info_servizio_autisti.jsp"%>
	</c:if>
	
	<c:if test="${fn:contains(tipoRuoliList, 'CLIENTE_ROLE') && corseDaEseguireCliente + corseEseguiteCliente > 0}">
		<%@ include file="/common/PanelCorseCliente.jsp"%>
	</c:if>

	<c:if test="${fn:contains(tipoRuoliList, 'AUTISTA_ROLE') && autista.attivo && corseDaEseguire + corseDisponibili + corseEseguite > 0 }">
		<%@ include file="/common/PanelCorseAutista.jsp"%>
	</c:if>
	
	<div class="row"></div>
	
	<!-- ALERT RITARDI -->
	<c:if test="${not empty ritardi}">
	<div class="panel panel-default">
		<div class="panel-heading"><strong><big>Ritardi <span class="glyphicon glyphicon-time"></span></big></strong></div>
	  	<div class="panel-body">
		<c:forEach items="${ritardi}" var="varObjRitardo">
		<div class="alert ${not varObjRitardo.pagatoAndata || not varObjRitardo.pagatoRitorno ? 'alert-success': 'alert-info'} ">
			<c:set var="totalePrezzoRitardi" value="${varObjRitardo.prezzoAndata.add(varObjRitardo.prezzoRitorno)}"/>
			<p class="h4"><strong>Ritardo Prelevamento Passeggero - Costo Totale Ritardo <fmt:formatNumber value="${totalePrezzoRitardi}" pattern="0.00" />&euro;</strong>
			<span class="pull-right">
				<a class="btn btn-sm btn-info checkFatturaRitardoDisponibile" id="${varObjRitardo.ricercaTransfert.id}">
					<span class="glyphicon glyphicon-download-alt"></span> <span class="glyphicon glyphicon-time"></span> Scarica Fattura Ritardo</a>
				<c:choose>
					<c:when test="${not varObjRitardo.pagatoAndata || not varObjRitardo.pagatoRitorno}">
					<a class="btn btn-sm btn-success pagamentoRitardo" id="${varObjRitardo.ricercaTransfert.id}">
						<span class="fa fa-credit-card-alt"></span> Esegui il Pagamento</a>
					</c:when>
					<c:otherwise>
	           	 		<strong><ins>Pagamento Eseguito</ins></strong>
	         		</c:otherwise>
         		</c:choose>
			</span>
			</p>
			<p><strong>Id Corsa:</strong> ${varObjRitardo.ricercaTransfert.id} <br> 
			<strong>Partenza:</strong> ${varObjRitardo.ricercaTransfert.formattedAddress_Partenza} 
			<strong> Giorno:</strong> <fmt:formatDate pattern="dd MMMM yyyy, HH:mm" value="${varObjRitardo.ricercaTransfert.dataOraPrelevamentoDate}" /><br>
			<strong>Arrivo:</strong> ${varObjRitardo.ricercaTransfert.formattedAddress_Arrivo} 
			<c:if test="${varObjRitardo.ricercaTransfert.ritorno}">
				<strong> Giorno:</strong> <fmt:formatDate pattern="dd MMMM yyyy, HH:mm" value="${varObjRitardo.ricercaTransfert.dataOraRitornoDate}" />
			</c:if><br>
			<strong> ${varObjRitardo.ricercaTransfert.ritorno ? 'ANDATA e RITORNO' : 'SOLO ANDATA'}</strong></p>
			
		<hr class="message-inner-separator">
		
			<p><strong>Numero Ritardo Ore Andata:</strong> <c:out value="${varObjRitardo.numeroMezzoreRitardiAndata / 2}"></c:out>h
			<strong>- Costo:</strong> <c:out value="${varObjRitardo.prezzoAndata}"></c:out>
			<c:if test="${varObjRitardo.ricercaTransfert.ritorno && varObjRitardo.numeroMezzoreRitardiAndata > 0}">
				<br><strong>Numero Ritardo Ore Ritorno:</strong> <c:out value="${varObjRitardo.numeroMezzoreRitardiRitorno / 2}"></c:out>h
				<strong>- Costo:</strong> <c:out value="${varObjRitardo.prezzoRitorno}"></c:out>
			</c:if>
			<br><small>Costo ogni 30 minuti Ritardo ${VALORE_EURO_ORA_RITARDO_CLIENTE_CON_TASSA_SERVZIO}&euro;(iva inclusa)</small>
			</p>
		</div>
		</c:forEach>
	</div>
	</div>
	</c:if>
	
	<!-- ALERT SUPPLEMENTI -->
	<c:if test="${not empty supplementi}">
	<div class="panel panel-default">
		<div class="panel-heading"><strong><big>Supplementi <i class="fa fa-plus-square" aria-hidden="true"></i></big></strong></div>
	  	<div class="panel-body">
		<c:forEach items="${supplementi}" var="varObjSupplemento">
		<div class="alert ${not varObjSupplemento.pagato ? 'alert-success': 'alert-info'} ">
			<p class="h4"><strong>Prezzo Supplemento <fmt:formatNumber value="${varObjSupplemento.prezzo}" pattern="0.00" />&euro;</strong>
			<span class="pull-right">
				<a class="btn btn-sm btn-info checkFatturaSupplementoDisponibile" id="${varObjSupplemento.id}">
					<span class="glyphicon glyphicon-download-alt"></span> <i class="fa fa-plus-square" aria-hidden="true"></i> Scarica Fattura Supplemento</a>
				<c:choose>
					<c:when test="${not varObjSupplemento.pagato}">
					<a class="btn btn-sm btn-success pagamentoSupplemento" id="${varObjSupplemento.id}">
						<span class="fa fa-credit-card-alt"></span> Esegui il Pagamento</a>
					</c:when>
					<c:otherwise>
	           	 		<strong><ins>Pagamento Eseguito</ins></strong>
	         		</c:otherwise>
         		</c:choose>
			</span>
			</p>
			<p><strong>Id Corsa:</strong> ${varObjSupplemento.ricercaTransfert.id} <br> 
			<strong>Partenza:</strong> ${varObjSupplemento.ricercaTransfert.formattedAddress_Partenza} 
			<strong> Giorno:</strong> <fmt:formatDate pattern="dd MMMM yyyy, HH:mm" value="${varObjSupplemento.ricercaTransfert.dataOraPrelevamentoDate}" /><br>
			<strong>Arrivo:</strong> ${varObjSupplemento.ricercaTransfert.formattedAddress_Arrivo} 
			<c:if test="${varObjSupplemento.ricercaTransfert.ritorno}">
				<strong> Giorno:</strong> <fmt:formatDate pattern="dd MMMM yyyy, HH:mm" value="${varObjSupplemento.ricercaTransfert.dataOraRitornoDate}" />
			</c:if><br>
			<strong> ${varObjSupplemento.ricercaTransfert.ritorno ? 'ANDATA e RITORNO' : 'SOLO ANDATA'}</strong></p>
			
		<hr class="message-inner-separator">
			<p><strong>Prezzo:</strong> <fmt:formatNumber value="${varObjSupplemento.prezzo}" pattern="0.00" />&euro;</p>
			<p><strong>Descrizione:</strong> <c:out value="${varObjSupplemento.descrizione}"></c:out></p>
		</div>
		</c:forEach>
	</div>
	</div>
	</c:if>

</div>
</div>
</div>

<%@ include file="/scripts/ConfirmSubmit.jsp"%>
</body>