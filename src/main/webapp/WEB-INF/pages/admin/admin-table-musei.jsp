<%@ include file="/common/taglibs.jsp"%>

<head>
    <title>Gestione Musei</title>
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
		$("#listComuni_tag").prepend("<option value="+ p1 +">"+p2+"</option>");
		$("#listComuni_tag").trigger("chosen:updated");
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
					url: '${pageContext.request.contextPath}/autocompleteComuni?term='+term,
					dataType: "json",
					contentType: 'application/json',
					data: {	},
					beforeSend: function(){
						$('ul.chosen-results').empty();
						$("#listComuni_tag").empty();
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

	<h3 style="margin-top: -20px;">Gestione Musei</h3>
	
	<%@ include file="/common/messages.jsp" %>

	<div class="well">
		<c:if test="${not empty museo}">
			<form:form commandName="museo" method="post" action="admin-tableMusei" id="museoForm" autocomplete="off" onsubmit="">
  			<form:hidden path="id"/>
  			<form:hidden path="comuni.id"/>

			<div class="form-group row">
				<label for="nomeMuseoID" class="col-sm-2 form-control-label">Nome</label>
				<div class="col-sm-4">
					<form:input path="nomeMuseo" class="form-control" id="nomeMuseoID" tabindex="1" />
				</div>
				<label for="descID" class="col-sm-2 form-control-label">info</label>
				<div class="col-sm-4">
					<form:textarea path="descrizione" class="form-control" id="descID" tabindex="2" />
				</div>
			</div>
			<div class="form-group row">
				<label for="indirizzoID" class="col-sm-2 form-control-label">Indirizzo</label>
				<div class="col-sm-4">
					<form:input path="indirizzo" class="form-control" id="indirizzoID" tabindex="3" />
				</div>
				<label for="numeroVisiteAnnoID" class="col-sm-2 form-control-label">numeroVisiteAnno</label>
				<div class="col-sm-2">
					<form:input path="numeroVisiteAnno" class="form-control" id="numeroVisiteAnnoID" tabindex="3" />
				</div>
			</div>
			<div class="form-group row">
				<label for="latID" class="col-sm-2 form-control-label">Latitudine</label>
				<div class="col-sm-2">
					<form:input path="lat" class="form-control" id="latID" tabindex="4" />
				</div>
				<div class="col-sm-2" ></div>
				<label for="lngID" class="col-sm-2 form-control-label">Longitudine</label>
				<div class="col-sm-2">
					<form:input path="lng" class="form-control" id="lngID" tabindex="5" />
				</div>
			</div>
			<div class="form-group row">
				<label for="listComuni_tag" class="col-sm-2 form-control-label">Comune</label>
				<div class="col-sm-4">
					<form:select path="comuni.nomeComune" id="listComuni_tag" multiple="multiple" class="chosen-select" tabindex="6">
						<c:if test="${ museo.comuni.id != null}">
							<option selected="selected" value="${museo.comuni.id}">${museo.comuni.nomeComune}</option>
						</c:if>
					</form:select>
				</div>
				<label for="placeIdID" class="col-sm-2 form-control-label">Google PlaceId</label>
				<div class="col-sm-4">
					<form:input path="placeId" class="form-control" id="placeIdID" tabindex="7" />
				</div>
			</div>
			
			<div class="form-group ">
				<div class=" text-right">
					<button type="submit" name="cancel" class="btn btn-warning" tabindex="8">annulla <span class="fa fa-ban"></span></button>
					<c:choose>
					    <c:when test="${modifica}">
					    	<button type="button" name="elimina" class="btn btn-danger alertConfirmGenerale" tabindex="9">elimina museo <span class="fa fa-trash-o"></span></button>
							<button type="submit" name="modifica" class="btn btn-success" tabindex="10">modifica museo <span class="fa fa-pencil"></span></button>
					    </c:when>    
					    <c:otherwise>
							<button type="submit" name="aggiungi" class="btn btn-success" tabindex="9">salva museo <span class="fa fa-plus"></span></button>
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
						<td>INFO MUSEO</td><td>${infoGoogle.formattedAddress}</td>
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
			<form method="get" action="${ctx}/admin/admin-tableMusei" id="searchForm" class="form-inline" role="form">
		 		<div class="form-group">
					<div class="input-group">
						<label class="sr-only" for="query">ricerca</label>
						<input type="text" name="q" class="form-control" size="30" id="query" value="${param.q}" placeholder="<fmt:message key="search.enterTerms"/>">
						<span class="input-group-btn">
							<button type="submit" name="ricerca" id="button.search" class="btn btn-default">ricerca</button>
						</span>
					</div><!-- /input-group -->
					
			 		<button type="submit" class="btn btn-primary">Nuovo Museo <span class="fa fa-plus"></span></button>
		 		</div>
			</form>
		</div> <!-- fine row  -->
	
	
		<div class="form-group row">
			<display:table name="museiList" cellspacing="0" cellpadding="0" requestURI=""
	                   defaultsort="1" id="musei" pagesize="${page_size_table}" class="table table-condensed table-striped" export="false">
		        <display:column property="nomeMuseo" sortable="true" titleKey="Museo" url="/admin/admin-tableMusei" paramId="idMuseo" paramProperty="id"/>
				<display:column property="descrizione" sortable="true" titleKey="descrizione"/>
				<display:column property="numeroVisiteAnno" sortable="true" titleKey="numeroVisiteAnno"/>
				<display:column property="indirizzo" sortable="true" titleKey="indirizzo"/>
				<display:column property="comuni.nomeComune" sortable="true" titleKey="Comune"/>
				<display:column property="placeId" sortable="true" titleKey="placeID"/>
	    	</display:table>
		</div> <!-- fine row  -->
		
	</div> <!-- fine row  -->
	
	
</div>
</div>
</div>

<%@ include file="/scripts/ConfirmSubmit.jsp"%>

</body>