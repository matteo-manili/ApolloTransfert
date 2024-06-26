<%@ include file="/common/taglibs.jsp"%>

<head>
<title>Gestione Modelli Autoveicoli</title>
<meta name="menu" content="AdminMenu"/>

<!-- jquery -->
<link rel="stylesheet" href="<c:url value="/scripts/vendor/jquery-ui-1.11.4/jquery-ui.css"/>">

<script src="<c:url value="/scripts/vendor/jquery-ui-1.11.4/external/jquery/jquery-2.2.4.min.js"/>"></script>
<script src="<c:url value="/js/bootstrap.min.js"/>"></script>
<script src="<c:url value="/scripts/vendor/jquery-ui-1.11.4/jquery-ui.min.js"/>"></script>


<script type="text/javascript">
$(document).ready(function () {

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
							"<div class='col-sm-12 '>"+
								"<div class=' col-sm-7'>"+
										"<p class='h4' >"+value.text+"</p>"+
								"</div>"+
								"<div class=' col-sm-5'>"+
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
	
}); // fine ready
</script>
	
</head>
<body>

<div class="content-area home-content">
<div class="container">
<div class="">

	
	<h3 style="margin-top: -20px;">Gestione Modelli Autoveicoli</h3>
	
	<spring:bind path="modelloAutoveicolo.*">
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
	
	<div class="well">
		<form:form commandName="modelloAutoveicolo" method="post" action="admin-tableModelloAutoveicolo" id="modelloAutoveicoloForm" autocomplete="off" onsubmit="">
 		<form:hidden path="id"/>
		<input type="hidden" name="marca-auto-scout" value="${modelloAutoveicolo.marcaAutoScout.id}">  
		<input type="hidden" name="modifica" value="${modifica}">  
		
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
		
		<!-- auto scout marca e modello -->
		<div class="form-group row">
			<label for="inputMarcheAutoMobile" class="col-sm-1 form-control-label">Marca</label>
		  	<div class="col-sm-4">
				<div class='input-group'>
					<span class='input-group-addon'>
						<i class='fa fa-car'></i>
					</span>
					<input type="text" id="inputMarcheAutoMobile" value="${modelloAutoveicolo.marcaAutoScout.name}" class=" form-control " placeholder="Scrivi Marca Auto...">
				</div>
			</div>
			
			<label for="inputModelloAutoMobile" class="col-sm-1 form-control-label">Modello</label>
			<div class="col-sm-5">
				<div class='input-group'>
					<span class='input-group-addon'>
						<i class='fa fa-car'></i>
					</span>
					<input type="text" id="inputModelloAutoMobile" name="modello-auto" value="${modelloAutoveicolo.name}" class=" form-control " placeholder="Scrivi Modello Auto...">
				</div>
			</div>
		</div>
		
		<!-- CLASSE AUTOVEICOLO -->
		<div class="form-group row ">
			<div class=" ">
				<label class="text-primary"><big><strong>Modello Auto Immatricolato dal ${AnniMaxImmatricPrimaClasse}</strong></big></label>
			</div>
			<div class=" ">
				<div class="btn-group" data-toggle="buttons">
					<c:forEach items="${classeAutoveicoloList}" var="varObj">
					<label class="btn btn-primary ${modelloAutoveicolo.classeAutoveicolo.id == varObj.id ? 'active':''}">
						<input type="radio" name="classe-autoveicolo" id="option-${varObj.id}" autocomplete="off" value="${varObj.id}"
							${modelloAutoveicolo.classeAutoveicolo.id == varObj.id ? 'checked':''}> <fmt:message key="${varObj.nome}"/></label>
					</c:forEach>
				</div>
			</div>
		</div>
		
		<!-- POSTI AUTO AUTOVEICOLO -->
		<div class="form-group row ">
			<div class="btn-group" data-toggle="buttons">
			<c:forEach items="${numeroPostiAutoList}" var="varNumPosti">
				<c:set var="contains" value="false" />
				<c:forEach items="${modelloAutoNumeroPostiList}" var="varModelloAutoPostiAuto">
					<c:if test="${varModelloAutoPostiAuto.numeroPostiAuto.id == varNumPosti.id}">
						<c:set var="contains" value="true" />
					</c:if>
				</c:forEach>
				<label class="btn btn-primary btn-sm ${contains ? 'active':''}">
    				<input type="checkbox" name="numero-posti-auto-${varNumPosti.id}" autocomplete="off" 
    					value="${varNumPosti.id}" ${contains ? 'checked':''}> 
					<fmt:message key="posti.auto.autista">
						<fmt:param value="${varNumPosti.numero}"/>
					</fmt:message>
				</label>
			</c:forEach>
			</div>
		</div>
		
		
		<div class="form-group row">
			<div class="pull-right">
				<button type="submit" name="cancel" class="btn btn-warning">Annulla <span class="fa fa-ban"></span></button>
				<c:choose>
				    <c:when test="${modifica}">
				    	<button type="submit" name="modifica-modello" class="btn btn-success">Modifica Modello <span class="fa fa-pencil"></span></button>
				    	<button type="button" name="elimina-modello" class="btn btn-danger alertConfirmGenerale">Elimina Modello <span class="fa fa-trash"></span></button>
				    </c:when>    
				    <c:otherwise>
						<button type="submit" name="inserisci-modello" class="btn btn-success">Inserisci Modello <span class="fa fa-car"></span></button>
				    </c:otherwise>
				</c:choose>
			</div>
		</div>
		</form:form>
	</div> <!-- fine well  -->


	<div class="col-sm-12">
		<div class="form-group row">
			<form method="get" action="${ctx}/admin/admin-tableModelloAutoveicolo" id="searchForm" class="form-inline" role="form">
				<input id="search_type" name="search_type" type="hidden">
		 		<div class="form-group">
					<div class="input-group">
						<input type="text" name="q" class="form-control" size="30" id="query" value="${param.q}" placeholder="<fmt:message key="search.enterTerms"/>">
						
						<div class="input-group-btn">
							<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" 
								aria-expanded="false">ricerca <span class="caret"></span></button>
							<ul class="dropdown-menu">
					          <li><a onclick="$('#search_type').val('marca'); $('#searchForm').submit()" href="#">Per Marca</a></li>
					          <li role="separator" class="divider"></li>
					          <li><a onclick="$('#search_type').val('modello'); $('#searchForm').submit()" href="#">Per Modello</a></li>
					          <li role="separator" class="divider"></li>
					          <li><a onclick="$('#search_type').val('lista-completa'); $('#searchForm').submit()" href="#">Lista Completa Modelli</a></li>
					        </ul>
						</div>
						
					</div><!-- /input-group -->
					<button type="submit" name="ordina-modelli-utilizzati" class="btn btn-info btn-sm">Modeli Utilizzati dagli Autisti 
					<span class="fa fa-user"></span> <span class="fa fa-car"></span> <span class="fa fa-sort-amount-desc"></span></button> 
		 		</div>
			</form>
		</div> <!-- fine row  -->
	
	
		<!-- TABELLA -->
		<div class="form-group row">
			<display:table name="modelloAutoveicoloList" cellspacing="0"  cellpadding="0" requestURI="" defaultsort="0" id="row" 
				pagesize="15" class="table table-condensed table-striped" export="false">
				
				<display:column property="id" sortable="true" titleKey="id"
					url="/admin/admin-tableModelloAutoveicolo" paramId="idModelloAutoveicolo" paramProperty="id"/>
				
				<display:column property="marcaAutoScout.name" sortable="true" titleKey="Marca" 
		        	url="/admin/admin-tableModelloAutoveicolo" paramId="idModelloAutoveicolo" paramProperty="id"/>
		        
		        <display:column property="name" sortable="true" titleKey="Modello" 
		        	url="/admin/admin-tableModelloAutoveicolo" paramId="idModelloAutoveicolo" paramProperty="id"/>
				
				<display:column property="classeAutoveicolo.description" sortable="true" titleKey="Classe" />
				
				<display:column property="modelloAutoNumeroPosti" sortable="true" titleKey="Numero Posti" />

	    	</display:table>
		</div> <!-- fine row  -->
	</div> <!-- fine tabella  -->
	
</div>
</div>
</div>

<%@ include file="/scripts/ConfirmSubmit.jsp"%>

</body>