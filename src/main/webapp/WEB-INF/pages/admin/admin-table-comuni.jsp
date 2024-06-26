<%@ include file="/common/taglibs.jsp"%>

<head>
    <title>Gestione Comuni</title>
    <meta name="menu" content="AdminMenu"/>
	
	<!-- jquery -->
	<link rel="stylesheet" href="<c:url value="/scripts/vendor/jquery-ui-1.11.4/jquery-ui.css"/>">
	
	<script src="<c:url value="/scripts/vendor/jquery-ui-1.11.4/external/jquery/jquery-2.2.4.min.js"/>"></script>
	<script src="<c:url value="/js/bootstrap.min.js"/>"></script>
	<script src="<c:url value="/scripts/vendor/jquery-ui-1.11.4/jquery-ui.min.js"/>"></script>
	
	<link rel="stylesheet" href="<c:url value='/css/ChosenBootstrap.css'/>">
	<script src="<c:url value="/scripts/vendor/chosen_v1.5.1/chosen.jquery.min.js"/>"></script>
	
	
	<script type="text/javascript">
	$(document).ready(function () {

		function myFunction(p1, p2) {
			//alert(123);
			$("#provincia_tag").prepend("<option value="+ p1 +">"+p2+"</option>");
			$("#provincia_tag").trigger("chosen:updated");
		}
		
		$(function() {
	       $('.chosen-select').chosen( { enable_split_word_search: false, max_selected_options: 1 } );
	       $('.chosen-select-deselect').chosen({ allow_single_deselect: true });

	       $('.chosen-choices input').autocomplete({
	       	minLength: 2,
	       	delay : 500,
	     	  	source: function( request, response ) {
	     	  	
	     	  	var term = $('.chosen-choices input').val();
	     	    $.ajax({
	     	    	type: 'POST',
					url: '${pageContext.request.contextPath}/autocompleteProvince?term='+term,
					dataType: "json",
					contentType: 'application/json',
					data: {	},
					beforeSend: function(){
						//$('ul.chosen-results').empty();
						//$("#listComuni_tag").empty();
						//$("#listZoneSelezionate_TAG").trigger("chosen:updated");
						//$("#listZoneSelezionate_TAG").prepend("");
						//$("#listZoneSelezionate_TAG").trigger("chosen:updated");
					},
					success: function( data ) {
						response($.map(data.reverse(), function(value) {
							$('ul.chosen-results').append('<li class="active-result">' + value.text + '</li>');
								myFunction(value.value,value.text);
							}));
	      	      }, //fine success
		           error: function (req, status, error) {
		                alert('errore ajax autocomplete Chosen Comuni');
		          }
	      	    });
	      	  }
	      	});
	       
		}); //fine funzioni chois
	
		
	}); // fine ready
	</script>

	
	
</head>
<body>

<div class="content-area home-content">
<div class="container">
<div class="">

	<h3 style="margin-top: -20px;">Gestione Comuni</h3>
	
	<%@ include file="/common/messages.jsp" %>
	
	<div class="well">
		<!-- inzio form -->

		<c:if test="${not empty comune}">
			<form:form commandName="comune" method="post" action="admin-tableComuni" id="comuneForm" autocomplete="off" onsubmit="">
  			<form:hidden path="id"/>
  			<form:hidden path="province.id"/>
  			<form:hidden path="regioni.id"/>

			<div class="form-group row">
				<label for="nomeComuneID" class="col-sm-1 form-control-label">Nome</label>
				<div class="col-sm-4">
					<form:input path="nomeComune" class="form-control" id="nomeComuneID" tabindex="1" />
				</div>
				
				<label for="regione_tag" class="col-sm-1 form-control-label">Regione</label>
				<div class="col-sm-4">
					<form:select path="regioni.nomeRegione" id="regione_tag" class="chosen-select" tabindex="2">
						<c:if test="${empty comune.regioni.id}">
							<option selected value="">- Seleziona Regione -</option>
						</c:if>
						<c:forEach items="${regioniList}" var="varReg">
							<option ${varReg.id == comune.regioni.id ? 'selected' : ''} value="${varReg.id}">${varReg.nomeRegione}</option>
						</c:forEach>
					</form:select>
				</div>
			</div>
			
			<div class="form-group row">
				<label for="provincia_tag" class="col-sm-1 form-control-label">Provincia</label>
				<div class="col-sm-4">
					<form:select path="province.nomeProvincia" id="provincia_tag" multiple="multiple" class="chosen-select" tabindex="3">
						<c:if test="${ comune.province.id != null}">
							<option selected="selected" value="${comune.province.id}">${comune.province.nomeProvincia}</option>
						</c:if>
					</form:select>
				</div>
				
				<label for="isolaID" class="col-sm-1 form-control-label">isola</label>
				<div class="col-sm-2">
					<form:select path="isola" class="form-control" id="isolaID" tabindex="4">
						<form:option value="true" label="true"/>
						<form:option value="false" label="false"/>
					</form:select>
				</div>
				
				<label for="catastoID" class="col-sm-1 form-control-label">catasto</label>
				<div class="col-sm-2">
					<form:input path="catasto" class="form-control" id="catastoID" tabindex="4" />
				</div>
			</div>
			
			<div class="form-group ">

				<div class=" text-right">
					<button type="submit" name="cancel" class="btn btn-warning" tabindex="5">annulla <span class="fa fa-ban"></span></button>
					<c:choose>
					    <c:when test="${modifica}">
					    	<button type="button" name="elimina" class="btn btn-danger alertConfirmGenerale" tabindex="6">elimina comune <span class="fa fa-trash-o"></span></button>
							<button type="submit" name="modifica" class="btn btn-success" tabindex="6">modifica comune <span class="fa fa-pencil"></span></button>
					    </c:when>    
					    <c:otherwise>
							<button type="submit" name="aggiungi" class="btn btn-success">salva comune <span class="fa fa-plus"></span></button>
						</c:otherwise>
					</c:choose>
				</div>
			</div>
			</form:form>
		</c:if>

		<c:if test="${not empty infoGoogle}">
			<div class="table-responsive">
				<table class="table table-bordered">
				<thead>
					<tr><th>#</th><th>formattedAddress</th><th>name</th><th>lat</th><th>lng</th><th>Comune</th><th>Sigla Provincia</th>
						<th>place_id</th><th>listTypes</th></tr>
				</thead>
				<tbody>
					<tr>           
						<td>INFO COMUNE</td><td>${infoGoogle.formattedAddress}</td>
						<td>${infoGoogle.name}</td><td>${infoGoogle.lat}</td>
						<td>${infoGoogle.lng}</td><td>${infoGoogle.comune}</td>
						<td>${infoGoogle.siglaProvicia}</td><td>${infoGoogle.place_id}</td>
						<td>${infoGoogle.listTypes}</td>
					</tr>
				</tbody>
				</table>
			</div> <!-- fine div table -->
		</c:if>

 	</div> <!-- fine div col-md-12  -->
	
	
	
	
	<!-- TABELLA -->
	<div class="col-sm-12">
	
		<div class="form-group row">
			<form method="get" action="${ctx}/admin/admin-tableComuni" id="searchForm" class="form-inline" role="form">
		 		<div class="form-group">
					<div class="input-group">
						<label class="sr-only" for="query">ricerca</label>
						<input type="text" name="q" class="form-control" size="30" id="query" value="${param.q}" placeholder="<fmt:message key="search.enterTerms"/>">
						<span class="input-group-btn">
							<button type="submit" name="ricerca" id="button.search" class="btn btn-default">ricerca</button>
						</span>
					</div><!-- /input-group -->
					
			 		<button type="submit" class="btn btn-primary">Nuovo Comune <span class="fa fa-plus"></span></button>
		 		</div>
			</form>
		</div> <!-- fine row  -->
	
	
		<div class="form-group row">
			<display:table name="comuniList" cellspacing="0" cellpadding="0" requestURI=""
	                   defaultsort="1" id="comuni" pagesize="${page_size_table}" class="table table-condensed table-striped" export="false">
	                   
		        <display:column property="nomeComune" sortable="true" titleKey="Comune" url="/admin/admin-tableComuni" paramId="idComune" paramProperty="id"/>
		                        
				<display:column property="province.nomeProvincia" sortable="true" titleKey="Provincia" />
				
				<display:column property="isola" sortable="true" titleKey="isola" />

				<display:column property="regioni.nomeRegione" sortable="true" titleKey="Regione" />
		                        
				<display:column property="catasto" sortable="true" titleKey="catasto" />
	    	</display:table>
		</div> <!-- fine row  -->
		
	</div> <!-- fine row  -->
	
</div>
</div>
</div>

<%@ include file="/scripts/ConfirmSubmit.jsp"%>

</body>