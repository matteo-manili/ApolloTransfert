<%@ include file="/common/taglibs.jsp"%>

<head>
<title>Agenzie Viaggi</title>
<meta name="menu" content="AdminMenu"/>

<!-- jquery -->
<link rel="stylesheet" href="<c:url value="/scripts/vendor/jquery-ui-1.11.4/jquery-ui.css"/>">

<script src="<c:url value="/scripts/vendor/jquery-ui-1.11.4/external/jquery/jquery-2.2.4.min.js"/>"></script>
<script src="<c:url value="/js/bootstrap.min.js"/>"></script>
<script src="<c:url value="/scripts/vendor/jquery-ui-1.11.4/jquery-ui.min.js"/>"></script>
</head>
<body>

<div class="content-area home-content">
<div class="container">
<div class="col-md-12">

	<div class="well ">
	<p><strong>Il Venditore dopo aver venduto almeno un Transfer potrà visualizzare la lista di Contatti di Hotel, 
	Agenzie Viaggi e Enti Turistici filtrati per Provincia, nella lista sono presenti il <code>nome</code>, <code>email</code>, 
	<code>l'indirizzo</code>, <code>sito web</code>.
	Il telefono dovà essere ricercato visionando il Sito Web della struttura. Se il sito web non è presente si ricercherà usando il dominio della 
	email, esempio: se la email è info@hotel5stelleroma.com, il sito probabilmente sarà www.hotel5stelleroma.com</strong></p>
	</div>
	
	<%@ include file="/common/messages.jsp" %>


	<c:if test="${not empty agenzieViaggio}">
	<div class="well">
		<form:form commandName="agenzieViaggio" method="post" action="contatti-marketing" id="agenzieViaggioForm" autocomplete="off" onsubmit="">
		
		<form:hidden path="id"/>

		<div class="form-group row">
			<label for="emailID" class="col-sm-1 form-control-label">email</label>
			<div class="col-sm-4">
				<form:input path="email" class="form-control" id="emailID" tabindex="1" />
			</div>
			
			<label for="nomeID" class="col-sm-1 form-control-label">nome</label>
			<div class="col-sm-3">
				<form:input path="nome" class="form-control" id="nomeID" tabindex="2" />
			</div>
			
			<label for="unsubscribeID" class="col-sm-1 form-control-label">unsubscribe</label>
			<div class="col-sm-2">
				<form:input path="unsubscribe" class="form-control" id="unsubscribeID" tabindex="3" />
			</div>
		</div>
		
		<div class="form-group row">
			<label for="citta_e_indirizzoID" class="col-sm-1 form-control-label">indirizzo</label>
			<div class="col-sm-4">
				<form:input path="citta_e_indirizzo" class="form-control" id="citta_e_indirizzoID" tabindex="4" />
			</div>
			<label for="dataInvioLastEmailID" class="col-sm-1 form-control-label">dataInvioLastEmail</label>
			<div class="col-sm-3">
				<form:input path="dataInvioLastEmail" class="form-control" id="dataInvioLastEmail" tabindex="5" />
			</div>
			<label for="numeroEmailInviateID" class="col-sm-1 form-control-label">numeroEmailInviate</label>
			<div class="col-sm-2">
				<form:input path="numeroEmailInviate" class="form-control" id="numeroEmailInviateID" tabindex="6" />
			</div>
		</div>
		
		<div class="form-group row">
			<label for="parametriScontoID" class="col-sm-1 form-control-label">parametriSconto</label>
			<div class="col-sm-4">
				<form:input path="parametriSconto" class="form-control" id="parametriScontoID" tabindex="4" />
			</div>
			<label for="sitoWebScrapingID" class="col-sm-1 form-control-label">sitoWebScraping</label>
			<div class="col-sm-3">
				<form:input path="sitoWebScraping" class="form-control" id="sitoWebScrapingID" tabindex="5" />
			</div>
			<label for="sitoWebID" class="col-sm-1 form-control-label">sitoWeb</label>
			<div class="col-sm-2">
				<form:input path="sitoWeb" class="form-control" id="sitoWebID" tabindex="6" />
			</div>
		</div>
		
		<div class="form-group ">
			<div class=" text-right">
				<button type="submit" name="cancel" class="btn btn-warning" tabindex="8">annulla <span class="fa fa-ban"></span></button>
				<c:choose>
				<c:when test="${modifica}">
					<button type="button" name="elimina" class="btn btn-danger alertConfirmGenerale" tabindex="9">elimina contatto <span class="fa fa-trash-o"></span></button>
					<button type="submit" name="modifica" class="btn btn-success" tabindex="10">modifica contatto <span class="fa fa-pencil"></span></button>
				</c:when>    
				<c:otherwise>
					<button type="submit" name="aggiungi" class="btn btn-success" tabindex="9">salva contatto <span class="fa fa-plus"></span></button>
				</c:otherwise>
				</c:choose>
			</div>
		</div>
		
		</form:form>
		</div> <!-- fine well  -->
	</c:if>
 	
	<c:if test="${not empty agenzieViaggio}">
	<!-- TABELLA -->
	<div class="col-sm-12">
		<div class="form-group row"><p><code><strong>Ricerca Contatti per: citta_e_indirizzo, email, nome, sitoWeb, sitoWebScraping, parametriSconto</strong></code></p></div>
		<div class="form-group row">
			<form method="get" action="${ctx}/contatti-marketing" id="searchForm" class="form-inline" role="form">
		 		<div class="form-group">
					<div class="input-group">
						<label class="sr-only" for="query">ricerca</label>
						<input type="text" name="q" class="form-control" size="30" id="query" value="${param.q}" placeholder="<fmt:message key="search.enterTerms"/>">
						<span class="input-group-btn">
							<button type="submit" name="ricerca" id="button.search" class="btn btn-primary">ricerca</button>
						</span>
					</div><!-- /input-group -->
					<a class="btn btn-info" href="<c:url value='/contatti-marketing?ordinamento=unsubscribe'/>">
						<span class="fa fa-sort-amount-desc"></span> Ordina per Unsubscribe</a>
			 		<button type="submit" class="btn btn-primary">Nuovo Contatto <span class="fa fa-plus"></span></button>
		 		</div>
			</form>
		</div> <!-- fine row  -->
	
		<div class="form-group row">
			<display:table name="agenzieViaggioList" cellspacing="0" cellpadding="0" requestURI="" defaultsort="0" id="idAgenzia" 
				pagesize="${page_size_table}" class="table table-condensed table-striped" export="true">
				<display:column property="id" titleKey="id" url="/contatti-marketing" paramId="idAgenzia" paramProperty="id"/>
				<display:column property="email" titleKey="email" url="/contatti-marketing" paramId="idAgenzia" paramProperty="id"/>
				<display:column property="nome" titleKey="nome"/>
				<display:column property="citta_e_indirizzo" titleKey="citta_e_indirizzo"/>
				<display:column property="sitoWeb" titleKey="sitoWeb" />
      			<display:column property="unsubscribe" titleKey="unsubscribe"/>
      			<display:column property="numeroEmailInviate" titleKey="numeroEmailInviate"/>
      			<display:column property="dataInvioLastEmail" titleKey="dataInvioLastEmail"/>
      			<display:column property="sitoWebScraping" titleKey="sitoWebScraping" />
      			<display:column property="parametriSconto" titleKey="parametriSconto"/>     
	    	</display:table>
		</div>
	</div>
	</c:if>

</div>
</div>
</div>

<%@ include file="/scripts/ConfirmSubmit.jsp"%>

</body>