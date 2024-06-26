<%@ include file="/common/taglibs.jsp"%>
<head>
<title>Gestione Province</title>
<meta name="menu" content="AdminMenu"/>

<!-- jquery -->
<script src="<c:url value="/scripts/vendor/jquery-ui-1.11.4/external/jquery/jquery-2.2.4.min.js"/>"></script>
<script src="<c:url value="/js/bootstrap.min.js"/>"></script>

<link rel="stylesheet" href="<c:url value='/css/ChosenBootstrap.css'/>">
<script src="<c:url value="/scripts/vendor/chosen_v1.5.1/chosen.jquery.min.js"/>"></script>

<script type="text/javascript">
$(document).ready(function () {

	$('[data-toggle="tooltip"]').tooltip();
	
	$(".setTariffa").on("keyup", function(event) {
	       var idElement = $(this).attr('id');
	       var tariffaVal = $(this).val();
	       $.ajax({
		  type: 'POST',
		  url: '${pageContext.request.contextPath}/impostaTariffaProvincia',
		  dataType: "json",
		  data : {
			  idElement : idElement, 
			  tariffaVal : tariffaVal
			},
			beforeSend: function(){
				$("#img-load-"+idElement).show();
			},
			success: function( result ) {
				if( result['esito'] ){
					$("#img-load-"+idElement).hide();
				}
			},
			error: function (data) {
				alert('errore ajax impostaTariffaProvincia');
			}
		});
	   }); //fine keyup setTariffa
	
	
	
	/*
	$(function() {
	      $('.chosen-select').chosen( { enable_split_word_search: true,  } );
	}); //fine funzioni chois
	*/
	function myFunction(p1, p2) {
		//alert(123);
		$("#provincia_tag").prepend("<option value="+ p1 +">"+p2+"</option>");
		$("#provincia_tag").trigger("chosen:updated");
	}
	
	$(function() {
	      $('.chosen-select').chosen( );
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

	<h3 style="margin-top: -20px;">Gestione Province e Tariffe</h3>
	
	<%@ include file="/common/messages.jsp" %>
	
	<c:if test="${not empty provincia}">
	<form:form commandName="provincia" method="post" action="admin-tableProvince" id="provinciaForm" autocomplete="off" onsubmit="">
	<form:hidden path="id"/><form:hidden path="regioni.id"/>
	<form:hidden path="tariffaBase"/><form:hidden path="lat"/><form:hidden path="lng"/>
	<div class="well">
		<div class="form-group row">
			<label for="nomeProvinciaID" class="col-sm-1 form-control-label">Nome</label>
			<div class="col-sm-4">
				<form:input path="nomeProvincia" class="form-control" id="nomeProvinciaID" tabindex="1" />
			</div>
			
			<label for="siglaProvinciaID" class="col-sm-1 form-control-label">Sigla</label>
			<div class="col-sm-1">
				<form:input path="siglaProvincia" class="form-control" id="siglaProvinciaID" tabindex="3" />
			</div>
			
			<label for="regione_tag" class="col-sm-1 form-control-label">Regione</label>
			<div class="col-sm-3">
				<form:select path="regioni.nomeRegione" id="regione_tag" class="chosen-select" tabindex="2">
					<c:if test="${empty provincia.regioni.id}">
						<option selected value="">- Seleziona Regione -</option>
					</c:if>
					<c:forEach items="${regioniList}" var="varReg">
						<option ${varReg.id == provincia.regioni.id ? 'selected' : ''} value="${varReg.id}">${varReg.nomeRegione}</option>
					</c:forEach>
				</form:select>
			</div>
		</div>
		
		<div class="form-group row">
			<label for="provincia_tag" class="col-sm-1 form-control-label">Province confinanti</label>
			<div class="col-sm-10">
				<form:select path="provinceConfinanti_TAG" id="provincia_tag" multiple="multiple" class="chosen-select" tabindex="4">
					<c:forEach items="${provincia.provinceConfinanti}" var="varTAG">
						<form:option selected="selected" value="${varTAG.id}">${varTAG.nomeProvincia}</form:option>
					</c:forEach>
				</form:select>
			</div>
		</div>
		
		<!-- 
		<div class="form-group row">
			wewe: ${provincia.provinceConfinanti}
		</div>  -->
		
		<div class="form-group row">
			<label for="numeroAbitantiID" class="col-sm-2 form-control-label">numeroAbitanti</label>
			<div class="col-sm-2">
				<form:input path="numeroAbitanti" class="form-control" id="numeroAbitantiID" />
			</div>
			
			<label for="isolaID" class="col-sm-1 form-control-label">isola</label>
			<div class="col-sm-2">
				<form:select path="isola" class="form-control" id="isolaID" tabindex="4">
					<form:option value="true" label="true"/>
					<form:option value="false" label="false"/>
				</form:select>
			</div>
			
			<label for="percentualeServizioID" class="col-sm-2 form-control-label">percentualeServizio</label>
			<div class="col-sm-1">
				<form:input path="percentualeServizio" class="form-control" id="percentualeServizioID" />
			</div>
			
			<label class="col-sm-2 form-control-label">tariffaBase: <c:out value="${provincia.tariffaBase}"/></label>
		</div>
	
		<div class="form-group row">
			<div class="pull-right">
				<button type="submit" name="cancel" class="btn btn-warning" tabindex="5">annulla <span class="fa fa-ban"></span></button>
				<c:choose>
				    <c:when test="${modifica}">
				    	<button type="button" name="elimina" class="btn btn-danger alertConfirmGenerale" tabindex="6">elimina provincia <span class="fa fa-trash-o"></span></button>
						<button type="submit" name="modifica" class="btn btn-success" tabindex="6">modifica provincia <span class="fa fa-pencil"></span></button>
				    </c:when>    
				    <c:otherwise>
						<button type="submit" name="aggiungi" class="btn btn-success">Aggiungi Provincia <span class="fa fa-plus"></span></button>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
	
		<c:if test="${not empty infoGoogle}">
		<div class="table-responsive">
			<table class="table table-bordered">
			<thead>
				<tr><th>#</th><th>formattedAddress</th><th>name</th><th>lat</th><th>lng</th><th>Provincia</th><th>Sigla Provincia</th>
					<th>place_id</th><th>listTypes</th></tr>
			</thead>
			<tbody>
				<tr>
					<td>INFO PROVINCIA</td><td>${infoGoogle.formattedAddress}</td>
					<td>${infoGoogle.name}</td><td>${infoGoogle.lat}</td>
					<td>${infoGoogle.lng}</td><td>${infoGoogle.comune}</td>
					<td>${infoGoogle.siglaProvicia}</td><td>${infoGoogle.place_id}</td>
					<td>${infoGoogle.listTypes}</td>
				</tr>
			</tbody>
			</table>
		</div>
		</c:if>
 	</div>
	
	<div class="well">

		<div class="form-group row">
			<label for="tariffaRegioneID" class="col-sm-1 form-control-label">tariffaBase</label>
			<div class="col-sm-1">
				<input type="text" class="form-control" name="tariffa-regione" id="tariffaRegioneID"> 
			</div>
			
			<!-- Select Regini -->
			<div class="col-sm-3">
				<select name="regione-tariffe" class="form-control">
					<option value="" selected>Imposta Tariffa per Regione</option>
					<option value="0">Tutta Italia</option>
					<c:forEach items="${regioniList}" var="varReg">
						<option ${varReg.id == provincia.regioni.id ? '' : ''} value="${varReg.id}">${varReg.nomeRegione}</option>
					</c:forEach>
				</select>
			</div>
			
			<div class="col-sm-3">
				<button type="button" name="imposta-tariffa-regione" class="btn btn-info alertConfirmGenerale">Imposta Tariffa Regione <span class="fa fa-globe"></span></button>
			</div>
		</div>
		

		
		<a href="#panel-macro-regioni" data-toggle="collapse"><big>Tariffe Provincia - Marco Regioni (Visualizza/Nascondi)</big></a>
		<div id="panel-macro-regioni" class="collapse ">
			<c:forEach items="${TabellaMacroRegioni}" var="varObj">
			<div class="panel panel-primary">
				<div class="panel-heading">${varObj.macroRegione.nome}</div>
				<div class="panel-body" style="">
				
					<c:forEach items="${varObj.regioni_Entity}" var="varObjReg">
					<div class="col-xs-12 col-sm-6 col-md-6 col-lg-6 " >
					<div class="panel panel-info"  >
						<div class="panel-heading" >${varObjReg.regione.nomeRegione}</div>
						<div class="panel-body" style="">
						
							<c:forEach items="${varObjReg.province_Entity}" var="varObjProv">
							<div class="col-xs-12 col-sm-12 col-md-6 col-lg-6">
							<div class="input-group">
								<span class="input-group-addon" data-toggle="tooltip" data-placement="top" title="Num Abit. 
									<fmt:formatNumber type="number" value="${varObjProv.provincia.numeroAbitanti}" />">
									<small>${varObjProv.provincia.nomeProvincia}</small></span>
								<input type="text" value="${varObjProv.provincia.tariffaBase}" class="form-control input-sm setTariffa" 
									id="${varObjProv.provincia.id}">
								<span class="input-group-addon">
								<i class="fa fa-spinner fa-pulse fa-1x fa-fw" style="display: none;" id="img-load-${varObjProv.provincia.id}"></i>
								</span>
							</div>
							</div>
							</c:forEach>
							
						</div>
					</div>
					</div>
					</c:forEach>
				</div>
			</div>
			</c:forEach>
		</div>

	</div>
	
	</form:form>
	</c:if>
	
	<!-- TABELLA -->
	<div class="col-sm-12">
		<div class="form-group row">
			<form method="get" action="${ctx}/admin/admin-tableProvince" id="searchForm" class="form-inline" role="form">
			<input id="search_type" name="search_type" type="hidden">
		 		<div class="form-group">
					<div class="input-group">
						<input type="text" name="q" class="form-control" size="30" id="query" value="${param.q}" placeholder="<fmt:message key="search.enterTerms"/>">
						<!-- <span class="input-group-btn">
							<button type="submit" name="ricerca" id="button.search" class="btn btn-default">ricerca</button>
						</span> -->
						<div class="input-group-btn">
							<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" 
								aria-expanded="false">ricerca <span class="caret"></span></button>
							<ul class="dropdown-menu">
					          <li><a onclick="$('#search_type').val('provincia'); $('#searchForm').submit()" href="#">Per Provincia</a></li>
					          <li role="separator" class="divider"></li>
					          <li><a onclick="$('#search_type').val('regione'); $('#searchForm').submit()" href="#">Per Regione</a></li>
					        </ul>
						</div>
						
					</div><!-- /input-group -->
			 		<button type="submit" class="btn btn-primary">Nuova Provincia <span class="fa fa-plus"></span></button>
		 		</div>
			</form>
		</div> <!-- fine row  -->
	
	
		<div class="form-group row">
			<display:table name="provinceList" cellspacing="0" cellpadding="0" requestURI="" defaultsort="0" id="province" pagesize="30" 
				class="table table-condensed table-striped" export="false">
				
				<display:column property="id" titleKey="id"/>
				
				<display:column property="regioni.nomeRegione" titleKey="Regione"/>
				
		        <display:column property="nomeProvincia" sortable="true" titleKey="Provincia" url="/admin/admin-tableProvince" paramId="idProvincia" paramProperty="id"/>
		        
		        <display:column property="isola" sortable="true" titleKey="isola" />
		        
				<display:column property="tariffaBase" titleKey="tariffaBase"/>
				
				<display:column property="percentualeServizio" titleKey="percentualeServizio"/>
	    	</display:table>
		</div> 
	</div> 
	
</div>
</div>
</div>

<%@ include file="/scripts/ConfirmSubmit.jsp"%>

</body>