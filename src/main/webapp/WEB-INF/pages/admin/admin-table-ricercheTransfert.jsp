<%@ include file="/common/taglibs.jsp"%>

<head>
    <title>Lista Ricerche Transfert</title>
    <meta name="menu" content="AdminMenu"/>
	
</head>
<body>

<div class="content-area home-content">
<div class="container">
<div class="">

	<h3 style="margin-top: -20px;">Ricerche Transfert</h3>
	
	<%@ include file="/common/messages.jsp" %>

	<!-- TABELLA -->
	<div class="col-sm-12">
		<div class="form-group row well">
			<form method="get" action="${ctx}/admin/admin-tableRicercheTransfert" id="searchForm" class="form-inline" role="form">
		 		<div class="form-group">
					<div class="input-group">
						<label class="sr-only" for="query">ricerca</label>
						<input type="text" name="q" class="form-control" size="30" id="query" value="${param.q}" placeholder="<fmt:message key="search.enterTerms"/>">
						<span class="input-group-btn">
							<button type="submit" name="ricerca" id="button.search" class="btn btn-default">ricerca</button>
						</span>
					</div><!-- /input-group -->
		 		</div>
			</form>
		</div> <!-- fine row  -->

		<div class="form-group row">
			<display:table name="ricercheTransfertList" cellspacing="0" cellpadding="0" requestURI="" 
    			id="ricercheTransfert" partialList="true" pagesize="${page_size_table}" size="resultSize" class="table table-condensed table-striped" export="true">
	                   
				<display:column property="id" sortable="true" titleKey="id" />
				<display:column property="visitatore.fullNameRegion" sortable="true" titleKey="visitatore" />
				<display:column property="dataRicerca" format="{0,date,dd/MM/yyyy HH.mm}" sortable="true" titleKey="dataRic" />
		        <display:column property="partenzaRequest" sortable="true" titleKey="partenzaRequest" url="/" paramId="courseId" paramProperty="id"/>
		        <display:column property="formattedAddress_Partenza" sortable="true" titleKey="formatPartenza"/>
		        
				<display:column property="dataOraPrelevamentoDate" format="{0,date,dd/MM/yyyy HH.mm.ss}" sortable="true" titleKey="dataPrelev" />
				<display:column property="arrivoRequest" sortable="true" titleKey="arrivoRequest"/>
				<display:column property="formattedAddress_Arrivo" sortable="true" titleKey="formatArrivo"/>
				
				<display:column property="dataOraRitornoDate" format="{0,date,dd/MM/yyyy HH.mm}" sortable="true" titleKey="dataRit" />
				<display:column property="numeroPasseggeri" sortable="true" titleKey="Pass" />
				<fmt:formatNumber var="totKm" value="${(ricercheTransfert.distanzaValue + ricercheTransfert.distanzaValueRitorno) / 1000}" maxFractionDigits="0" />
				<display:column sortable="true" titleKey="TotKm" media="html">${totKm}km</display:column>
       			<display:column property="tipoServizio" sortable="true" titleKey="tipoServizio" />
				<display:column property="user.username" sortable="true" titleKey="cliente" />
				<display:column sortable="true" titleKey="modifica transfer" media="html">
					<a href="<c:url value='/admin/admin-modificaTransfer?idCorsa='/>${ricercheTransfert.id}">Modifica</a>
       			</display:column>
	    	</display:table>
		</div> <!-- fine row  -->
	</div> <!-- fine row  -->
	
</div>
</div>
</div> <!-- fine div container  -->

<script src="<c:url value="/scripts/vendor/bootstrap-without-jquery.min.js"/>"></script>

</body>