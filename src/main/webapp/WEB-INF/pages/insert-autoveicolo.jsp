<%@ include file="/common/taglibs.jsp"%>
<head>
<title><fmt:message key="autoveicolo.title"/></title>
<link rel="stylesheet" href="<c:url value="/scripts/vendor/jquery-ui-1.11.4/jquery-ui.css"/>">
<script src="<c:url value="/scripts/vendor/jquery-ui-1.11.4/external/jquery/jquery-2.2.4.min.js"/>"></script>
<script src="<c:url value="/js/bootstrap.min.js"/>"></script>
<script src="<c:url value="/scripts/vendor/jquery-ui-1.11.4/jquery-ui.min.js"/>"></script>

<script type="text/javascript">
$(document).ready(function() {
	$( "#inputMarcheAutoMobile" ).autocomplete({
		source: function( request, response ) {
		$.ajax({
			type: 'POST',
			url: '${pageContext.request.contextPath}/autocompleteMarcheAutoScout?term='+request.term,
			//scriptCharset: "iso-8859-1",
			dataType: "json",
			contentType: 'application/json',
			data: {	},
			beforeSend: function(){},
			success: function( data ) {
			//response( data );
			var htmlRes = "";
			response(
				$.map(data, function(value) {
					htmlRes = htmlRes + 
					"<div class='col-sm-12'>"+
					"<div class=' col-sm-7'>"+
					"<p class='h4' >"+value.text+"</p>"+
					"</div>"+
					"<div class=' col-sm-4'>"+
					"<button type='submit' id="+value.value+" name='seleziona-marca-autoscout' value="+value.value+" class='btn btn-primary btn-sm attesaLoader'>Seleziona Marca <span class='fa fa-car'></span></button>"+
					"</div>"+
					"</div>";
					$("#listResultResultMarcheAutoScout").html(htmlRes);
				})
			);
			$("#modalResultResultMarcheAutoScout").modal('show');
			$("#inputMarcheAutoMobile").val("");
			},
			error: function (req, status, error) {
			alert('errore ajax autocomplete autocompleteResultMarcheAutoScout');
			}
		});
		},
		minLength: 2,
		delay : 1000
	});

	$( "#inputModelloAutoMobile" ).autocomplete({
		source: function( request, response ) {
		var idMarcaAutoScout = $('#idMarcaAutoScout').val();
		//alert( idMarcaAutoScout );
		if( !idMarcaAutoScout ){
			bootbox.alert({
			backdrop: true,
			message: "Seleziona Marca Autoveicolo."
			});
		}else{
			$.ajax({
				type: 'POST',
				url: '${pageContext.request.contextPath}/autocompleteModelliAutoScout?term='+request.term+'&idMarcaAutoScout='+idMarcaAutoScout,
				//scriptCharset: "iso-8859-1",
				dataType: "json",
				contentType: 'application/json',
				data: {	},
				beforeSend: function(){},
				success: function( data ) {
				//response( data );
				var htmlRes = "";
				response(
					$.map(data, function(value) {
						htmlRes = htmlRes + 
						"<div class='col-sm-12'>"+
						"<div class=' col-sm-7'>"+
						"<p class='h4' >"+value.text+"</p>"+
						"</div>"+
						"<div class=' col-sm-5'>"+
						"<button type='submit' id="+value.value+" name='seleziona-modello-autoscout' value="+value.value+" class='btn btn-primary btn-sm attesaLoader'>Seleziona Modello <span class='fa fa-car'></span></button>"+
						"</div>"+
						"</div>";
						$("#listResultModelliAutoScout").html(htmlRes);
					})
				);
				$("#modalResultModelliAutoScout").modal('show');
				$("#inputModelloAutoMobile").val("");
				},
				error: function (req, status, error) {
				alert('errore ajax autocomplete autocompleteModelliAutoScout');
			}});
		}},
		minLength: 1,
		delay : 2000
	});

	$('#targaAutoveicolo').keyup(function(){
		this.value = this.value.toUpperCase();
	});

});
</script>
</head>
<body>
<div class="content-area home-content">
<div class="container">
<div class="col-md-12">

	<spring:bind path="autoveicolo.*">
		<c:if test="${not empty status.errorMessages}">
			<div class="alert alert-danger alert-dismissable">
				<a href="#" data-dismiss="alert" class="close">&times;</a>
				<c:forEach var="error" items="${status.errorMessages}">
				<c:out value="${error}" escapeXml="false"/><br/>
				</c:forEach>
			</div>
		</c:if>
	</spring:bind>

	<%@ include file="/common/messages.jsp" %>

	<%@ include file="/common/menu_autista.jsp"%>

	<h3 class="">Autoveicolo (${autoveicolo.autista.user.fullName})</h3>

	<form:form commandName="autoveicolo" method="post" action="insert-autoveicolo" id="autoveicoloForm" cssClass="" autocomplete="off" 
		onsubmit="/*return validateAutoveicolo(this)*/">
		<form:hidden path="id"/>
		<input type="hidden" name="autista-id" value="${autoveicolo.autista.id}">
		<input type="hidden" id="idMarcaAutoScout" name="marca-auto-scout" value="${MarcaAutoScout.idAutoScout}">
		<input type="hidden" id="idModelloAutoScout" name="modello-auto-scout" value="${ModelloAutoScout.id}"> 
		<input type="hidden" name="Modifica" value="${Modifica}"> 


		<c:if test="${autoveicolo.autoveicoloCartaCircolazione.approvatoCartaCircolazione && !pageContext.request.isUserInRole('ROLE_ADMIN')}">
			<div class="row">
				<div class="form-group col-xs-12 col-sm-12 h4">
					<p class="text-primary"><ins><strong><fmt:message key="autoveicolo.approvato.non.modificabile.messagio.autista"/></strong><ins/></p>
				</div>
			</div>
		</c:if>


		<!-- auto scout marca e modello -->
		<div class="form-group row">
			<label for="inputMarcheAutoMobile" class="col-sm-1 form-control-label">Marca</label>
			<div class="col-sm-5">
				<div class='input-group'>
					<span class='input-group-addon'>
					<i class='fa fa-car'></i>
					</span>
					<input type="text" id="inputMarcheAutoMobile" value="${MarcaAutoScout.name}" class=" form-control " placeholder="Scrivi Marca Auto..."
						${autoveicolo.autoveicoloCartaCircolazione.approvatoCartaCircolazione && !pageContext.request.isUserInRole('ROLE_ADMIN') ? 'disabled' : ''}>
				</div>
			</div>

			<label for="inputModelloAutoMobile" class="col-sm-1 form-control-label">Modello&#42;</label>
			<div class="col-sm-5">
				<div class='input-group'>
					<span class='input-group-addon'><i class='fa fa-car'></i></span>
					<input type="text" id="inputModelloAutoMobile" value="${ModelloAutoScout.name}" class=" form-control " placeholder="Scrivi Modello Auto..."
						${autoveicolo.autoveicoloCartaCircolazione.approvatoCartaCircolazione && !pageContext.request.isUserInRole('ROLE_ADMIN') ? 'disabled' : ''}>
				</div>
			</div>
		</div>

		<!-- MODAL MARCHE AUTOSCOUT -->
		<div id="modalResultResultMarcheAutoScout" class="modal fade" tabindex="-1" role="dialog">
			<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">Marche Auto
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				</div> 
				<div class="modal-body">
					<div class="row">
					<div id="listResultResultMarcheAutoScout"></div>
					</div>
				</div>
			</div>
			</div>
		</div>

		<!-- MODAL MODELLI AUTOSCOUT -->
		<div id="modalResultModelliAutoScout" class="modal fade" tabindex="-1" role="dialog">
			<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">Modelli Auto
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				</div> 
				<div class="modal-body">
					<div class="row">
					<div id="listResultModelliAutoScout"></div>
					</div>
				</div>
			</div>
			</div>
		</div>

		<!-- Targa -->
		<div class="form-group row">
			<label for="targaAutoveicolo" class="col-sm-1 form-control-label">Targa</label>
			<div class="col-sm-2">
				<form:input path="targa" class="form-control" id="targaAutoveicolo" placeholder="es. KMW-123" 
					disabled="${autoveicolo.autoveicoloCartaCircolazione.approvatoCartaCircolazione && !pageContext.request.isUserInRole('ROLE_ADMIN') ? 'true' : 'false'}" />
			</div>
	
			<!-- Select Numero Posti Auto -->
			<label for="numeroPostiAutoID" class="col-sm-2 form-control-label">Numero Posti&#42;</label>
			<div class="col-sm-3">
			<select name="numero-posti-auto" class="form-control" ${autoveicolo.autoveicoloCartaCircolazione.approvatoCartaCircolazione && !pageContext.request.isUserInRole('ROLE_ADMIN') ? 'disabled' : ''}>
				<option value=""><fmt:message key="autoveicolo.selezionaAutoveicolo"/></option>
				<c:forEach items="${ModelloAutoNumeroPostiList}" var="varObj">
					<option value="${varObj.id}" ${varObj.id == autoveicolo.modelloAutoNumeroPosti.id ? 'selected':''}>
					<fmt:message key="posti.auto.autista">
						<fmt:param value="${varObj.numeroPostiAuto.numero}"/>
					</fmt:message>
					</option>
				</c:forEach>
			</select>
			</div>
	
			<!-- Anno Immatricolazione -->
			<label for="annoImmatricolazione" class="col-sm-2 form-control-label">Anno Immatric.</label>
			<div class="col-sm-2">
				<form:input path="annoImmatricolazione" maxlength="4" class="form-control" id="annoImmatricolazione" placeholder="es: 2010" 
					disabled="${autoveicolo.autoveicoloCartaCircolazione.approvatoCartaCircolazione && !pageContext.request.isUserInRole('ROLE_ADMIN') ? 'true' : 'false'}"/>
			</div>
		</div>
		
		<div class="row col-lg-offset-1">
			<div class="form-group col-xs-12 col-sm-12">
				<p class="text-primary"><em>&#42;Se il <strong>Modello Autoveicolo</strong> oppure il <strong>Numero Posti</strong> non &egrave; presente  
				<a href="<c:url value="/contatti"/>"><strong><ins>Scriveteci</ins></strong></a> oppure contattateci al 
				<a href="tel:<fmt:message key="cellulare.matteo"/>"><strong><ins><fmt:message key="cellulare.matteo.esteso"/></ins></strong></a> (anche whatsapp).
				</em></p>
			</div>
		</div>
		
		<c:if test="${pageContext.request.isUserInRole('ROLE_ADMIN')}">
		<div class="form-group row">
			<label for="info" class="col-sm-4 form-control-label">info. (visibile solo da Admin)</label>
			<div class="col-sm-8">
				<form:textarea path="info" class="form-control" id="info"/>
			</div>
		</div>
		</c:if>

		<!-- bottoni salvataggio -->
		<div class="form-group row">
		<div class="pull-right">
			<button type="submit" name="cancel" class="btn btn-form btn-warning">Annulla</button>
			<c:choose>
			<c:when test="${Modifica}">
				<button type="submit" name="modifica-auto" class="btn btn-form btn-primary" 
					${autoveicolo.autoveicoloCartaCircolazione.approvatoCartaCircolazione && !pageContext.request.isUserInRole('ROLE_ADMIN') ? 'disabled' : ''}>
					Salva Modifiche <span class="fa fa-pencil"></span></button>
			</c:when>    
			<c:otherwise>
				<button type="submit" name="aggiungi" class="btn btn-form btn-success">Inserisci Autoveicolo <span class="fa fa-car"></span></button>
			</c:otherwise>
			</c:choose>
		</div>
		</div>
	</form:form>

	
	<div class="row col-sm-12">
	<h3 class="page-header">Autoveicoli inseriti</h3>
	<c:forEach items="${autoveicoloList}" var="varObj">
		<div class="row " style="padding-bottom: 5px;">
		<div class=" col-sm-6"><strong>${varObj.modelloAutoNumeroPosti.modelloAutoScout.marcaAutoScout.name}&nbsp;${varObj.modelloAutoNumeroPosti.modelloAutoScout.name}</strong>&nbsp;
			<fmt:message key="posti.auto.autista">
				<fmt:param value="${varObj.modelloAutoNumeroPosti.numeroPostiAuto.numero}"/>
			</fmt:message>&nbsp;Targa:&nbsp;${varObj.targa}&nbsp;Anno:&nbsp;${varObj.annoImmatricolazione}
		</div>
		<div class=" col-sm-6" style="padding-bottom: 10px;">
			<c:if test="${not varObj.autoveicoloCancellato}">
				<a class="btn btn-primary btn-sm" href="<c:url value='/insert-autoveicolo?modifica=${varObj.id}&idAutista=${varObj.autista.id}'/>">
				modifica auto <span class="fa fa-pencil"></a>
			</c:if>
			<c:choose>
			<c:when test="${varObj.autoveicoloCancellato}">
				<a class="btn btn-success btn-sm" href="<c:url value='/insert-autoveicolo?riabilita-auto=${varObj.id}&idAutista=${varObj.autista.id}'/>">
				attiva auto <span class="fa fa-check-square"></a>
			</c:when>
			<c:otherwise>
				<a class="btn btn-danger btn-sm" href="<c:url value='/insert-autoveicolo?elimina=${varObj.id}&idAutista=${varObj.autista.id}'/>">
				disattiva auto <span class="fa fa-ban"></span></a>
			</c:otherwise>
			</c:choose>
			<small>
				${varObj.autoveicoloCancellato ? 
				'<span class="text-danger">AUTO DISATTIVATA</span>':'<span class="text-success">AUTO ATTIVA</span>'}&nbsp;-&nbsp;
				${varObj.autoveicoloCartaCircolazione.approvatoCartaCircolazione ? 
				'<span class="text-success">APPROVATA <i class="fa fa-thumbs-up"></i></span>':'<span class="text-danger">NON APPROVATA <i class="fa fa-thumbs-down"></i></span>'}
			</small>
		</div>
		</div>
	</c:forEach>
	</div>
	
</div>
</div>
</div>

<%@ include file="/scripts/ConfirmSubmit.jsp"%>

<!-- questa validazione funziona se nel form si usa il pulsante submit e non un link come sto usando -->
<c:set var="scripts" scope="request">
<script type="text/javascript">
// This is here so we can exclude the selectAll call when roles is hidden
// nel bottone submit: onclick="onFormSubmit( autoveicoloForm );"
function onFormSubmit(theForm) {
	return validateAutoveicolo(theForm);
}
</script>
</c:set>

<v:javascript formName="autoveicolo" staticJavascript="false"/>
<script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"></script>

</body>