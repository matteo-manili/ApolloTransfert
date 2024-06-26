<%@ include file="/common/taglibs.jsp"%>

<head>
    <title>Lista Visitatori</title>
    <meta name="menu" content="AdminMenu"/>
	

	<script type="text/javascript">
	$(document).ready(function () {

		
	}); // fine ready
	</script>
	
</head>
<body>

<div class="content-area home-content">
<div class="container">
<div class="">

	<h3 style="margin-top: -20px;">Visitatori Pagina Home</h3>

	<%@ include file="/common/messages.jsp" %>

	<!-- TABELLA -->
	<div class="col-sm-12">
		<div class="form-group row well">
			<form method="get" action="${ctx}/admin/admin-tableVisitatori" id="searchForm" class="form-inline" role="form">
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
			<display:table name="visitatoriList" cellspacing="0" cellpadding="0" requestURI="" 
    			id="rowVisit" partialList="true" pagesize="${page_size_table}" size="resultSize" class="table table-condensed table-striped" export="true">
				<display:column property="id"  titleKey="Id" />
				<display:column property="ipAddress" titleKey="IpAddress" />
				<display:column property="dataVisita" format="{0,date,dd/MM/yyyy HH.mm.ss}" titleKey="Data" />
				<display:column property="deviceType" titleKey="DeviceType" />
				<display:column property="nomeProviderIpLocationApi" titleKey="PaginaProvenienza"/>
				<display:column titleKey="Zona" media="html">
          		${rowVisit.city} | ${rowVisit.regionName} | ${rowVisit.countryName} | ${rowVisit.countryCode} 
      			</display:column>
				<display:column property="hostname" titleKey="hostname" />
				
				<!-- <display : column property="provider" titleKey="provider" />
				<display : column property="regionCode" titleKey="regionC" />
				<display : column property="postalCode" titleKey="postalC" />
				<display : column property="latitude" titleKey="lat" />
				<display : column property="longitude" titleKey="long" /> -->
	    	</display:table>
		</div>
	</div>

</div>
</div>
</div>

<script src="<c:url value="/scripts/vendor/bootstrap-without-jquery.min.js"/>"></script>

</body>