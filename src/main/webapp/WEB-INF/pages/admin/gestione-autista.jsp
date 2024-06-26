<%@ include file="/common/taglibs.jsp"%>

<head>
<title><fmt:message key="gestioneAutista.title" /></title>

<!-- jquery -->
<link rel="stylesheet" href="<c:url value="/scripts/vendor/jquery-ui-1.11.4/jquery-ui.css"/>">

<script src="<c:url value="/scripts/vendor/jquery-ui-1.11.4/external/jquery/jquery-2.2.4.min.js"/>"></script>
<script src="<c:url value="/js/bootstrap.min.js"/>"></script>
<script src="<c:url value="/scripts/vendor/jquery-ui-1.11.4/jquery-ui.min.js"/>"></script>

<!-- input telefono -->
<script src="<c:url value="/scripts/vendor/intl-tel-input-9.0.3/build/js/utils.js"/>"></script>
<script src="<c:url value="/scripts/vendor/intl-tel-input-9.0.3/build/js/intlTelInput.min.js"/>"></script>
<link rel="stylesheet" href="<c:url value="/scripts/vendor/intl-tel-input-9.0.3/build/css/intlTelInput.css"/>"> 

<!-- toggle bottoni-checkbox -->
<script src="<c:url value="/scripts/vendor/bootstrap-toggle.min.js"/>"></script>
<link rel="stylesheet" href="<c:url value="/css/bootstrap-toggle.min.css"/>">

<script type="text/javascript">
$(document).ready(function() {
	
	//$("#phoneId").intlTelInput("setNumber", ${autista.user.phoneNumber});
	//$("#phoneId").intlTelInput("setNumber", "");
	$("#phoneId").intlTelInput(
			{
				//setNumber: "${autista.user.phoneNumber}",
				autoPlaceholder : true,
				nationalMode : true,
				preferredCountries : [ 'it', 'es', 'fr', 'de',
						'nl', 'gb', 'us' ]
			});
	function setPhone() {
		//alert(111);
		var verifica = $("#phoneId").intlTelInput("isValidNumber");
		if (verifica) {
			$("#numberTelAutistaId").val(
					$("#phoneId").intlTelInput("getNumber"));
			$("#valid-msg").removeClass("hide");
			$("#error-msg").addClass("hide");
		} else {
			$("#numberTelAutistaId").val("noValidNumber")
			$("#error-msg").removeClass("hide");
			$("#valid-msg").addClass("hide");
		}
	}
	;

	//setPhone();
	// al cambio della bandierina
	$("#phoneId").on("countrychange", function(e, countryData) {
		setPhone();
	});

	$("#phoneId").change(function() {
		setPhone();
	});
	$('#autistaFormId').submit(function() {
		setPhone();
	});
	$("#phoneId").keyup(function() {
		//alert('wewewe');
		reset();
		setPhone();

	});
	var reset = function() {
		$("#valid-msg").addClass("hide");
		$("#error-msg").addClass("hide");
	};

}); // fine ready
</script>

</head>
<body>

<div class="content-area home-content">
<div class="container">
<div class="">

<h3 style="margin-top: -20px;">Gestione Autisti</h3>

<%@ include file="/common/messages.jsp"%>

<spring:bind path="autista.*">
	<c:if test="${not empty status.errorMessages}">
		<div class="alert alert-danger alert-dismissable">
			<a href="#" data-dismiss="alert" class="close">&times;</a>
			<c:forEach var="error" items="${status.errorMessages}">
				<c:out value="${error}" escapeXml="false" />
				<br />
			</c:forEach>
		</div>
	</c:if>
</spring:bind>

<c:if test="${not empty autista.id}">
	<%@ include file="/common/menu_autista.jsp"%>
</c:if>


<c:if test="${not empty autista}">
<form:form commandName="autista" modelAttribute="autista" method="post" action="gestioneAutista" id="autistaFormId" autocomplete="off" onsubmit="" cssClass="well">

	<input type="hidden" value="${autista.id}" name="autista-id" />
	<input type="hidden" value="${autista.user.id}" name="autista-user-id" />

	<div class="form-group row">
		<label for="firstNameID" class="col-sm-2 form-control-label">Nome*</label>
		<div class="col-sm-4">
			<input type="text" value="${autista.user.firstName}" name="first-name" class="form-control"
				${fn:contains(autista.user.roles, 'ROLE_ADMIN') ? 'placeholder="utente admin" readonly':''} id="firstNameID" />
		</div>
		<label for="lastNameID" class="col-sm-2 form-control-label">Cognome*</label>
		<div class="col-sm-4">
			<input type="text" value="${autista.user.lastName}"
				name="last-name" class="form-control"
				${fn:contains(autista.user.roles, 'ROLE_ADMIN') ? 'placeholder="utente admin" readonly':''} id="lastNameID" />
		</div>
	</div>
	<div class="form-group row">
		<label for="phoneId" class="col-sm-2 form-control-label">Telefono*</label>
		<div class="col-sm-4">
			<input type="tel" class="form-control intl-tel-input" value="${autista.user.phoneNumber}" name="number-tel-autista-pre"
					${fn:contains(autista.user.roles, 'ROLE_ADMIN') ? 'placeholder="utente admin" readonly':''}
						id="phoneId"> <input type="hidden" name="number-tel-autista" id="numberTelAutistaId"> 
				<span id="valid-msg" class="hide text-success">Valid</span>
				<span id="error-msg" class="hide text-danger">Invalid number</span>
		</div>
		<label for="emailID" class="col-sm-2 form-control-label">email</label>
		<div class="col-sm-4">
			<c:choose>
				<c:when test="${fn:contains(autista.user.roles, 'ROLE_ADMIN')}">
					<form:input path="user.email" class="form-control" id="emailID" readonly="true" />
				</c:when>
				<c:otherwise>
					<form:input path="user.email" class="form-control" id="emailID" />
				</c:otherwise>
			</c:choose>
		</div>
	</div>

	<div class="form-group row">
		<label for="passwordID" class="col-sm-2 form-control-label">${not empty autista.user.id ? 'modifica password':'password*'}</label>
		<div class="col-sm-4">
			<c:choose>
				<c:when test="${not empty autista.user.id}">
					<input type="text" name="newPassword" class="form-control" id="passwordID"
						${fn:contains(autista.user.roles, 'ROLE_ADMIN') ? 'placeholder="utente admin" readonly':''}>
					<input type="hidden" name="password" value="${autista.user.password}" class="form-control" id="passwordID">
				</c:when>
				<c:otherwise>
					<input type="text" name="password" class="form-control" id="passwordID">
				</c:otherwise>
			</c:choose>
		</div>

		<c:if test="${not empty autista && autista.attivo}">
			<div class="col-sm-4">
			<p class="${(autista.autistaDocumento.approvatoGenerale) ? 'text-success' : 'text-danger'}">
				<strong>
				${(autista.autistaDocumento.approvatoGenerale) ? 'Profilo Approvato' : 'Profilo Non Approvato'}
				</strong>
			</p>
			</div>
		</c:if>
		
		<div class="col-sm-1">
			<p class="text-info"><strong>${autista.user.username}</strong></p>
		</div>
		
		
	</div>

	<div class="form-group row">
		<!--  
		<label for="numeroCartaIdentitaID" class="col-sm-3 form-control-label">Numero Carta Identita</label>
		<div class="col-sm-3">
			<input type="text" value="${autista.autistaDocumento.numeroCartaIdentita}" name="carta-identita" class="form-control"
				id="numeroCartaIdentitaID" />
		</div> -->
		
		<label class="col-sm-1 form-control-label">Attivo</label>
		<div class="col-sm-2">
			<form:checkbox path="attivo" data-toggle="toggle" data-size="small" data-onstyle="success" data-offstyle="warning"/>
		</div>
		
		<label class="col-sm-3 form-control-label">Privato o Azienda</label>
		<div class="col-sm-2">
			<input type="checkbox" id="tipoAutistaAziendaCheckID" name="tipoAutistaAziendaCheck" ${autista.azienda ? 'checked':''}  
				data-toggle="toggle" data-size="small" data-on="<i class='fa fa-industry'></i> AZIENDA" data-off="<i class='fa fa-user'></i> PRIVATO" data-onstyle="info" data-offstyle="success">
		</div>
		
		<label class="col-sm-1 form-control-label">Iscrizione</label>
		<div class="col-sm-3">
			<p class="text-info">
				<strong><fmt:formatDate pattern="dd MMMM, yyyy" value="${autista.user.signupDate}" /></strong>
			</p>
		</div>
		
	</div>

	<div class="form-group row">
		<label for="numCorseEseguiteID" class="col-sm-1 form-control-label">TotCorse</label>
		<div class="col-sm-1">
			<form:input path="numCorseEseguite" class="form-control" id="numCorseEseguiteID" readonly="true" />
		</div>

		<label  class="col-sm-1 form-control-label">docCompl</label>
		<div class="col-sm-1">
			<p class="text-info"><strong>${autista.documentiCompletatiFrazione}</strong></p>
		</div>
		
		<label  class="col-sm-1 form-control-label">docApprov</label>
		<div class="col-sm-1">
			<p class="text-info"><strong>${autista.documentiApprovatiFrazione}</strong></p>
		</div>

		<label class="col-sm-1 form-control-label">Bannato</label>
		<div class="col-sm-1">
			<form:checkbox path="bannato" data-toggle="toggle" data-size="small" data-off="<i class='fa fa-thumbs-up'></i> NO" 
				data-on="<i class='fa fa-ban'></i> SI" data-offstyle="success" data-onstyle="danger" />
		</div>
	</div>

	<div class="form-group row">
		<label for="noteID" class="col-sm-1 form-control-label">Note</label>
		<div class="col-sm-11">
			<textarea name="note" class="form-control" id="noteID" rows="3">${autista.note}</textarea>
		</div>
	</div>
	
	<div class="form-group row">

		<div class="pull-right ">
			<c:choose>
				<c:when test="${modifica}">
					<button type="submit" name="cancel" class="btn btn-warning">
						Annulla Modifica <span class="fa fa-ban"></span>
					</button>
					<button type="submit" name="modifica" class="btn btn-success">
						Modifica Autista <span class="fa fa-pencil"></span>
					</button>
				</c:when>
				<c:otherwise>
					<button type="submit" name="cancel" class="btn btn-warning">
						Annulla <span class="fa fa-ban"></span>
					</button>
					<button type="submit" name="aggiungi" class="btn btn-primary">
						Aggiungi Autista <span class="fa fa-plus"></span>
					</button>
				</c:otherwise>
			</c:choose>

		</div>
	</div>
</form:form>
</c:if>

<!-- PULSANTI TABELLA -->
<div class="col-sm-12">
	<div class="form-group row">
	<p><code><strong>Ricerca Autista per: firstName, lastName, username, phoneNumber, email, partitaIva, partitaIvaDenominazione, numeroPatente, 
	numeroRuoloConducenti, numeroCAP, targa.</strong></code></p>
	</div>
	<div class="form-group row">
		<form method="get" action="${ctx}/admin/gestioneAutista"
			id="searchForm" class="form-inline" role="search">
			<div class="form-group">
				<div class="input-group">
					<label class="sr-only" for="query">ricerca</label> <input
						type="text" name="q" class="form-control" size="30" id="query" value="${param.q}" 
						placeholder="<fmt:message key="search.enterTerms"/>"> <span class="input-group-btn">
						<button type="submit" name="ricerca" id="button.search"
							class="btn btn-default">ricerca</button>
					</span>
				</div>
				<!-- /input-group -->
				<a class="btn btn-info" href="<c:url value='/admin/gestioneAutista?ordinamento=documenti'/>">
					<span class="fa fa-sort-amount-desc"></span> <span class="fa fa-file-text"></span> Ordina per Documenti</a>
				<button type="submit" class="btn btn-primary">
					Crea Nuovo Autista <span class="fa fa-plus"></span>
				</button>
			</div>
		</form>
	</div>
	
	
	<!-- TABELLA  -->
	<div class=" row">
	 
		<display:table name="autistiList" cellspacing="0" cellpadding="0" requestURI="" 
			id="autisti" partialList="true" pagesize="${page_size_table}" size="resultSize" class="table table-condensed table-striped">  
			<display:column property="user.signupDate" format="{0,date,dd/MM/yyyy}" sortable="true" titleKey="Iscrizione" style="width: 20%" />
						
			<display:column property="user.firstName" sortable="true" titleKey="Nome" style="width: 15%" url="/admin/gestioneAutista" 
				paramId="idAutista" paramProperty="id" />

			<display:column property="user.lastName" sortable="true" titleKey="Congome" style="width: 20%" />

			<display:column property="user.phoneNumber" sortable="true" titleKey="Telefono" style="width: 20%" />

			<display:column property="user.email" sortable="true" titleKey="Email" style="width: 25%" />
			
			<display:column sortable="true" titleKey="ApprovatoGenerale" media="html">
           		${autisti.autistaDocumento.approvatoGenerale ? 'SI' : 'NO'}
       		</display:column>
				
			<display:column property="documentiCompletatiFrazione" sortable="true" titleKey="Doc.Compl." style="width: 25%" />
				
			<display:column property="documentiApprovatiFrazione" sortable="true" titleKey="Doc.Approv" style="width: 25%" />
				
       		<display:column sortable="true" titleKey="Azienda" media="html">
           		${autisti.azienda ? 'SI' : 'NO'}
       		</display:column>
       		
       		<display:column property="numCorseEseguite" sortable="true" titleKey="TotCorseEseguite" style="width: 5%" />
       		
       		<display:column sortable="true" titleKey="Attivo" media="html">
           		${autisti.attivo ? 'SI' : 'NO'}
       		</display:column>
       		
       		<display:column sortable="true" titleKey="Bannato" media="html">
           		${autisti.bannato ? 'SI' : 'NO'}
       		</display:column>
	        		
		</display:table>
	</div>
</div>


</div>
</div>
</div>

</body>