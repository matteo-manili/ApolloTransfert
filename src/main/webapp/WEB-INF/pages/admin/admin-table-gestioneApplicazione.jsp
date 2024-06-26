<%@ include file="/common/taglibs.jsp"%>
<head>
<title>Gestione Applicazione</title>
<meta name="menu" content="AdminMenu"/>

<!-- jquery -->
<link rel="stylesheet" href="<c:url value="/scripts/vendor/jquery-ui-1.11.4/jquery-ui.css"/>">

<script src="<c:url value="/scripts/vendor/jquery-ui-1.11.4/external/jquery/jquery-2.2.4.min.js"/>"></script>
<script src="<c:url value="/js/bootstrap.min.js"/>"></script>
<script src="<c:url value="/scripts/vendor/jquery-ui-1.11.4/jquery-ui.min.js"/>"></script>

<!-- toggle bottoni-checkbox -->
<script src="<c:url value="/scripts/vendor/bootstrap-toggle.min.js"/>"></script>
<link rel="stylesheet" href="<c:url value="/css/bootstrap-toggle.min.css"/>">


<script type="text/javascript">
$(document).ready(function () {
$(function() {
	$('#toggle-event').change(function() {
		//alert( $(this).prop('checked') );
		if( $(this).prop('checked') ){
			$('input[name="switchTestProduzione"]').val("TEST");
		}else{
			$('input[name="switchTestProduzione"]').val("PRODUZIONE");
		}
		$("#gestioneApplicazioneForm").submit();
	})
})
}); // fine ready
</script>
</head>
<body>

<div class="content-area home-content">
<div class="col-sm-12 ">

<h3 style="margin-top: -20px;">Gestione Applicazione</h3>
<%@ include file="/common/messages.jsp" %>

<c:if test="${not empty gestioneApplicazione}">
<form:form commandName="gestioneApplicazione" method="post" action="admin-tableGestioneApplicazione" id="gestioneApplicazioneForm" autocomplete="off" onsubmit="">
<form:hidden path="id"/>

<div class="well ">
	<p>
	<input type="checkbox" id="toggle-event"  ${switchTestProduzione? '' : 'checked'}  
		data-toggle="toggle" data-size="small" data-on="TEST" data-off="PRODUZIONE" data-onstyle="warning" data-offstyle="success">
	<input type="hidden" name="switchTestProduzione"></p>
	
	<p>
	<button type="button" name="aggiorna-sitemap" class="btn btn-sm btn-info alertConfirmGenerale attesaLoader">
		Aggiorna sitemap.xml <span class="fa fa-refresh"></span></button></p>
	
	<p>
	<button type="button" name="invia-email-agenzie-viaggio-fiera-milano" class="btn btn-sm btn-info alertConfirmGenerale attesaLoader">
		Invia Email AgenzieViaggio FieraMilano <span class="fa fa-paper-plane"></span></button></p>
	
	<p>
	<button type="button" name="set-parameter-ip-domain-mondoserver" class="btn btn-sm btn-info alertConfirmGenerale attesaLoader">
		Set Parametertro da SWITCH_TEST_PRODUZIONE String </button></p>
</div>


<div class="well">
	<div class="form-group row">
		<label for="nameID" class="col-sm-2 form-control-label">nome parametro</label>
		<div class="col-sm-12">
			<form:input path="name" class="form-control" id="nameID" />
		</div>
	</div>

	<div class="form-group row">
		<label for="valueNumberID" class="col-sm-2 form-control-label">valueNumber</label>
		<div class="col-sm-4">
			<form:input path="valueNumber" class="form-control" id="valueNumberID" />
		</div>
		<label for="valueStringID" class="col-sm-2 form-control-label">valueString</label>
		<div class="col-sm-4">
			<form:input path="valueString" class="form-control" id="valueStringID" />
		</div>
	</div>

	<div class="form-group row">
		<label for="commentoID" class="col-sm-2 form-control-label">commento</label>
		<div class="col-sm-12">
			<form:textarea path="commento" rows="4" class="form-control" id="commentoID" />
		</div>
	</div>


	<div class="form-group row">
		<div class="pull-right">
			<button type="submit" name="cancel" class="btn btn-warning">annulla <span class="fa fa-ban"></span></button>
			<c:choose>
			    <c:when test="${modifica}">
			    	<button type="button" name="elimina" class="btn btn-danger alertConfirmGenerale">elimina parametro <span class="fa fa-trash-o"></span></button>
					<button type="submit" name="modifica" class="btn btn-success">modifica parametro <span class="fa fa-pencil"></span></button>
			    </c:when>    
			    <c:otherwise>
					<button type="submit" name="aggiungi" class="btn btn-success">salva parametro <span class="fa fa-plus"></span></button>
				</c:otherwise>
			</c:choose>

		</div>
	</div>
</div> <!-- fine div col-md-12  -->
</form:form>
</c:if>


<div class="col-sm-12">
	<div class="form-group row">
		<form method="get" action="${ctx}/admin/admin-tableGestioneApplicazione" id="searchForm" class="form-inline" role="form">
	 		<div class="form-group">
				<div class="input-group">
					<label class="sr-only" for="query">ricerca</label>
					<input type="text" name="q" class="form-control" size="30" id="query" value="${param.q}" placeholder="<fmt:message key="search.enterTerms"/>">
					<span class="input-group-btn">
						<button type="submit" name="ricerca" id="button.search" class="btn btn-default">ricerca</button>
					</span>
				</div><!-- /input-group -->
				
		 		<button type="submit" class="btn btn-primary">Nuovo Parametro <span class="fa fa-plus"></span></button>
	 		</div>
		</form>
	</div> <!-- fine row  -->

	<!-- TABELLA -->
	<div class=" row">
		<display:table name="gestioneApplicazioneList" cellspacing="0" cellpadding="0" requestURI=""
                   defaultsort="" id="gestioneApplicazione" pagesize="50" class="table table-condensed table-striped" export="false">
			<display:column property="id" sortable="true" titleKey="id" />
	        <display:column property="name" sortable="true" titleKey="nome parametro" 
	                        url="/admin/admin-tableGestioneApplicazione" paramId="idGestioneApplicazione" paramProperty="id"/>
	        <display:column property="valueNumber" sortable="true" titleKey="valueNumber" /> 
			<display:column property="valueString" sortable="true" titleKey="valueString" />
			<display:column property="commento" sortable="true" titleKey="commento" />

    	</display:table>
	</div> <!-- fine row  -->
</div> <!-- fine col-sm-12  -->


<%@ include file="/scripts/ConfirmSubmit.jsp"%>

</div>
</div>
</body>