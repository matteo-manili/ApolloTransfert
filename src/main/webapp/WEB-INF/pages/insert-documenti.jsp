<%@ include file="/common/taglibs.jsp"%>
<head>
    <title>Documenti</title>
    
	<!-- the main fileinput CSS plugin file -->
	<!-- 
    <link rel="stylesheet" href="<c:url value="/scripts/vendor/bootstrap-fileinput-master/css/fileinput.min.css"/>">  -->
    <link rel="stylesheet" href="<c:url value="/scripts/vendor/bootstrap-fileinput-master/css/fileinput.css"/>">
    
    <!-- jquery -->
	<link rel="stylesheet" href="<c:url value="/scripts/vendor/jquery-ui-1.11.4/jquery-ui.css"/>">
	<script src="<c:url value="/scripts/vendor/jquery-ui-1.11.4/external/jquery/jquery-2.2.4.min.js"/>"></script>
	<script src="<c:url value="/js/bootstrap.min.js"/>"></script>
	<script src="<c:url value="/scripts/vendor/jquery-ui-1.11.4/jquery-ui.min.js"/>"></script>
	
	<!-- the main fileinput JS plugin file -->
	<script src="<c:url value="/scripts/vendor/bootstrap-fileinput-master/js/fileinput.js"/>"></script>
	<script src="<c:url value="/scripts/vendor/bootstrap-fileinput-master/js/locales/"/><fmt:message key="language.code"/>.js"></script>
	
	<!-- toggle bottoni-checkbox -->
	<script src="<c:url value="/scripts/vendor/bootstrap-toggle.min.js"/>"></script>
	<link rel="stylesheet" href="<c:url value="/css/bootstrap-toggle.min.css"/>">


<style>
.btn-file {
/*
padding: 5px 10px;
  font-size: 12px;
  line-height: 1.5;
  */
}
</style>
	<script type="text/javascript">
	$(document).ready(function () {
		
		/*
		var url = document.location.toString();
		if (url.match('#')) {
		    $('.nav-tabs a[href="#' + url.split('#')[1] + '"]').tab('show');
		} 
		// Change hash for page-reload
		$('.nav-tabs a').on('shown.bs.tab', function (e) {
		    window.location.hash = e.target.hash;
		})
		*/
		//$('.nav-tabs a[href="#' + '${tab_doc}' + '"]').tab('show');
	
		
		//---------------------------------
	    // LISTA COMUNI INSERIMENTO LICENZE
	    //---------------------------------
		$( "#inputZoneMobile" ).autocomplete({
			source: function( request, response ) {
				$.ajax({
				type: 'POST',
					url: '${pageContext.request.contextPath}/autocompleteComuni?term='+request.term,
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
								"<div class='col-sm-12 '>"+
									"<div class=' col-sm-7'>"+
											"<p class='h4' >"+value.text+"</p>"+
									"</div>"+
									"<div class=' col-sm-5'>"+
										"<button type='submit' id="+value.value+" name='seleziona-comune-licenza' value="+value.value+" class='btn btn-primary btn-sm attesaLoader'>Seleziona Comune <span class='fa fa-file-text'></span></button>"+
									"</div>"+
								"</div>";
								$("#listResultTerrMobil").html(htmlRes);
							})
						);
						$("#modalResultTerrMobil").modal('show');
						$("#inputZoneMobile").val("");
					},
				   error: function (req, status, error) {
						alert('errore ajax autocomplete autocompleteComuni');
				  }
			});
			},
			minLength: 2,
			delay : 1000
		});
		
		
		//---------------------------------------------------------------------
	    // SOTTO AUTISTI AZIENDA - ISCRIZIONE RUOLO CONDUCENTI - LISTA PROVINCE
	    //---------------------------------------------------------------------
		$( "#inputProvinciaRuoloConducenti" ).autocomplete({
			source: function( request, response ) {
				$.ajax({
				type: 'POST',
					url: '${pageContext.request.contextPath}/autocompleteProvince?term='+request.term,
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
								"<div class='col-sm-12 '>"+
									"<div class=' col-sm-7'>"+
											"<p class='h4' >"+value.text+"</p>"+
									"</div>"+
									"<div class=' col-sm-5 '>"+
										"<button type='submit' id="+value.value+" name='seleziona-provincia-ruolo' value="+value.value+" class='btn btn-primary btn-sm attesaLoader'>Selez. Camera di Commercio <span class='fa fa-file-text'></span></button>"+
									"</div>"+
								"</div>";
								$("#listProvinciaRuoloConducenti").html(htmlRes);
							})
						);
						$("#modalResultprovinciaRuoloConducenti").modal('show');
						$("#inputProvinciaRuoloConducenti").val("");
					},
				   error: function (req, status, error) {
						alert('errore ajax autocomplete autocompleteProvince');
				  }
			});
			},
			minLength: 2,
			delay : 1000
		});
		
		
		//-------------------------------------------------------
	    // AUTISTA - ISCRIZIONE RUOLO CONDUCENTI - LISTA PROVINCE
	    //-------------------------------------------------------
		$( "#inputProvinciaRuoloConducenti-collaboratore" ).autocomplete({
			source: function( request, response ) {
				$.ajax({
				type: 'POST',
					url: '${pageContext.request.contextPath}/autocompleteProvince?term='+request.term,
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
								"<div class='col-sm-12 '>"+
									"<div class=' col-sm-7'>"+
											"<p class='h4' >"+value.text+"</p>"+
									"</div>"+
									"<div class=' col-sm-5 '>"+
										"<button type='submit' id="+value.value+" name='seleziona-provincia-ruolo-collaboratore' value="+value.value+" class='btn btn-primary btn-sm attesaLoader'>Selez. Camera di Commercio <span class='fa fa-file-text'></span></button>"+
									"</div>"+
								"</div>";
								$("#listProvinciaRuoloConducenti").html(htmlRes);
							})
							);
						$("#modalResultprovinciaRuoloConducenti").modal('show');
						$("#inputProvinciaRuoloConducenti-collaboratore").val("");
					},
				   error: function (req, status, error) {
						alert('errore ajax autocomplete autocompleteProvince');
				  }
			});
			},
			minLength: 2,
			delay : 1000
		});
		
		
		
		
	}); // fine ready
	</script>

</head>
<body>

<div class="content-area home-content">
<div class="container">
<div class="col-md-12">

    <spring:bind path="autista.*">
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

	<%@ include file="/common/menu_autista.jsp" %>
	
	<h3>Documenti (${autista.user.fullName})</h3>

    <form:form commandName="autista" modelAttribute="autista" method="post" action="insert-documenti" id="autistaForm" autocomplete="off" enctype="multipart/form-data">
		<form:hidden path="id"/>

		<c:if test="${not empty autista.autistaDocumento.messaggioAutistaDocumenti}">
		<div class="row ">
			<div class="form-group col-sm-12 ">
				<div class="alert alert-info h5" role="alert">
			  		<span>${autista.autistaDocumento.messaggioAutistaDocumenti}</span>
				</div>
			</div>
		</div>
		</c:if>

		<c:if test="${pageContext.request.isUserInRole('ROLE_ADMIN')}">
		<div class="form-group row ">
			<div class="col-sm-6 form-group">
				<input type="checkbox" ${autista.autistaDocumento.approvatoGenerale ? 'checked':''}  
					onchange="$('#approvato-generale-id').val(${autista.autistaDocumento.approvatoGenerale});$('#autistaForm').submit();" 
						 data-on="APPROVATO <i class='fa fa-thumbs-up'></i>" data-off="NON APPROVATO <i class='fa fa-thumbs-down'></i>" 
							data-width="180" data-onstyle="success" data-offstyle="danger" data-toggle="toggle" data-size="small">
				<input type="hidden" id="approvato-generale-id" name="approvato-generale">
			</div>
		</div>
		<div class="form-group row">
			<div class="col-sm-10 form-group">
				<textarea class="form-control" name="messaggio-autista" placeholder="Note per l'Autista...">${autista.autistaDocumento.messaggioAutistaDocumenti}</textarea> 
			</div>
			<div class="col-sm-2 form-group">
				<button type="submit" name="salva-messaggio-autista" class="btn btn-info attesaLoader">Salva Note</button>
			</div>
		</div>
		</c:if>

		<div class="form-group ">
		<ul class="nav nav-tabs " role="tablist">
			<li role="presentation" class="${tab_doc == 'collaboratoreTab' ? 'active' : ''}">
				<a href="#collaboratoreTab" data-toggle="tab">
					Collaboratore  ${autista.azienda ? '(Azienda)' : '(Privato)'}&nbsp;${tab_doc_check['collaboratoreTab-completato'] ? '<span class="label label-success">ok</span>' : '<span class="label label-warning">inserisci</span>'}
					&nbsp;${tab_doc_check['collaboratoreTab-approv'] ? '<span class="label label-primary">Approvato <i class="fa fa-thumbs-up"></i></span>' : '<span class="label label-default">Da Approvare</span>'}</a></li>
				
			<c:if test="${not autista.azienda}">
				<li role="presentation" class="${tab_doc == 'iscrizioneRuoloTab' ? 'active' : ''}">
					<a href="#iscrizioneRuoloTab" data-toggle="tab">
					Iscrizione Ruolo ${tab_doc_check['iscrizioneRuoloTab-completato'] ? '<span class="label label-success">ok</span>' : '<span class="label label-warning">inserisci</span>'}
					&nbsp;${tab_doc_check['collaboratoreTab-approv'] ? '<span class="label label-primary">Approvato <i class="fa fa-thumbs-up"></i></span>' : '<span class="label label-default">Da Approvare</span>'}</a></li>
			</c:if>
				
			<li role="presentation" class="${tab_doc == 'licenzeTab' ? 'active' : ''}">
				<a href="#licenzeTab" data-toggle="tab">
				Licenza/e NCC ${tab_doc_check['licenzeTab-completato'] ? '<span class="label label-success">ok</span>' : '<span class="label label-warning">inserisci</span>'}
				&nbsp;${tab_doc_check['licenzeTab-approv'] ? '<span class="label label-primary">Approvato <i class="fa fa-thumbs-up"></i></span>' : '<span class="label label-default">Da Approvare</span>'}</a></li>
				
			<!-- Solo le aziende -->
			<c:if test="${autista.azienda}">
				<li role="presentation" class="${tab_doc == 'dipendentiTab' ? 'active' : ''}">
					<a href="#dipendentiTab" data-toggle="tab">
					Dipendenti Autisti ${tab_doc_check['dipendentiTab-completato'] ? '<span class="label label-success">ok</span>' : '<span class="label label-warning">inserisci</span>'}
					&nbsp;${tab_doc_check['dipendentiTab-approv'] ? '<span class="label label-primary">Approvato <i class="fa fa-thumbs-up"></i></span>' : '<span class="label label-default">Da Approvare</span>'}</a></li>
			</c:if>
			
			<li role="presentation" class="${tab_doc == 'cartaCircolazioneTab' ? 'active' : ''}">
				<a href="#cartaCircolazioneTab" data-toggle="tab">
				Carta di Circolazione ${tab_doc_check['cartaCircolazioneTab-completato'] ? '<span class="label label-success">ok</span>' : '<span class="label label-warning">inserisci</span>'}
				&nbsp;${tab_doc_check['cartaCircolazioneTab-approv'] ? '<span class="label label-primary">Approvato <i class="fa fa-thumbs-up"></i></span>' : '<span class="label label-default">Da Approvare</span>'}</a></li>


			<!-- Contratto -->
			<li role="presentation" class="${tab_doc == 'contrattoTab' ? 'active' : ''}">
				<a href="#contrattoTab" data-toggle="tab">
				<strong>Contratto <i class="fa fa-file-text-o"></i> <i class="fa fa-pencil"></i>
					<c:if test="${documentiApprovatiEsclusoContratto && documentiCompletatiEsclusoContratto || autista.autistaDocumento.approvatoGenerale}">
						${tab_doc_check['contrattoTab-completato'] ? '<span class="label label-success">ok</span>' : '<span class="label label-warning">inserisci</span>'}
						&nbsp;${tab_doc_check['contrattoTab-approv'] ? '<span class="label label-primary">Approvato <i class="fa fa-thumbs-up"></i></span>' : '<span class="label label-default">Da Approvare</span>'}
					</c:if>
				</strong></a>
			</li>
		</ul>
		</div>

		<div class="form-group ">
		<div class="tab-content ">
			<!-- INFO COLLABORATORE -->
			<div class="tab-pane ${tab_doc == 'collaboratoreTab' ? 'active' : ''}" id="collaboratoreTab">
				<%@ include file="/common/doc_info_collaboratore.jsp" %>
			</div>
			
			<!-- INFO COLLABORATORE - ISCRIZIONE RUOLO -->
			<c:if test="${not autista.azienda}">
				<div class="tab-pane ${tab_doc == 'iscrizioneRuoloTab' ? 'active' : ''}" id="iscrizioneRuoloTab">
					<%@ include file="/common/doc_iscrizione_ruolo_collaboratore.jsp" %>
				</div>
			</c:if>
			
			<!-- DOCUMENTO LICENZA/E NCC -->
			<div class="tab-pane ${tab_doc == 'licenzeTab' ? 'active' : ''}" id="licenzeTab">
				<%@ include file="/common/doc_licenze_ncc.jsp" %>
			</div>
		
			<!-- DOCUMENTO DIPENDENTI AUTISTI ISCRIZIONE RUOLO -->
			<c:if test="${autista.azienda}">
				<div class="tab-pane ${tab_doc == 'dipendentiTab' ? 'active' : ''}" id="dipendentiTab">
					<%@ include file="/common/doc_dipendenti_autisti.jsp" %>
				</div>
			</c:if>
			
			<!-- DOCUMENTI CARTA CIRCOLAZIONE AUTOVEICOLI -->
			<div class="tab-pane ${tab_doc == 'cartaCircolazioneTab' ? 'active' : ''}" id="cartaCircolazioneTab">
				<%@ include file="/common/doc_carta_circolazione_auto.jsp" %>
			</div>
			
			<div class="tab-pane ${tab_doc == 'contrattoTab' ? 'active' : ''}" id="contrattoTab">
				<c:choose>
					<c:when test="${documentiCompletatiEsclusoContratto || autista.autistaDocumento.approvatoGenerale}">
					<c:choose>
						<c:when test="${documentiApprovatiEsclusoContratto}">
							<%@ include file="/common/doc_collaboratore_contratto.jsp" %>
				    	</c:when>    
					    <c:otherwise>
					    	<div class="alert alert-info"><strong>Nota!</strong> Attendere Approvazione di tutti i Documenti.</div>
					    </c:otherwise>
					</c:choose>
			    	</c:when>   
				    <c:otherwise>
						<div class="alert alert-info"><strong>Nota!</strong> Inserire Documenti mancanti.</div>
				    </c:otherwise>
				</c:choose>
			</div>
		</div>
		</div>


		<!-- POP UP MODALS - CUMUNI (LICENZE NCC) E PROVINCIE (ISCRIZIONE RUOLO) -->
		<div id="modalResultprovinciaRuoloConducenti" class="modal fade" tabindex="-1" role="dialog">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">Provincia Camera di Commercio
						<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					</div> 
					<div class="modal-body">
						<div class="row">
							<div id="listProvinciaRuoloConducenti"></div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div id="modalResultTerrMobil" class="modal fade" tabindex="-1" role="dialog">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">Comuni
						<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					</div> 
					<div class="modal-body">
						<div class="row">
							<div id="listResultTerrMobil"></div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!-- FINE POP UP MODALS -->
    </form:form>
    
    
</div>
</div>
</div>


<script type="text/javascript">
$(".file").fileinput({
	language: '<fmt:message key="language.code"/>'
});
</script>
<%@ include file="/scripts/ConfirmSubmit.jsp"%>

</body>
