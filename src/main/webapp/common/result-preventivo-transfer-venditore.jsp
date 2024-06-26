<c:forEach items="${messaggiEsitoRicercaTransfert}" var="varObj">
<div class="alert alert-info">
	<strong>Info!</strong> ${varObj}
</div>
</c:forEach>

<c:if test="${not empty RIS.resultMedio}">
	<div class="panel panel-primary">
		<!-- TITOLO CORSA -->
		<div class="panel-heading">
			<strong>${ricercaTransfert.formattedAddress_Partenza}</strong>&nbsp;<span class="glyphicon glyphicon-arrow-right"></span>
				&nbsp;<strong>${ricercaTransfert.formattedAddress_Arrivo}</strong><br>
			<strong>Distanza: </strong>${ricercaTransfert.distanzaText}<strong>&nbsp;Durata: </strong>${ricercaTransfert.durataConTrafficoText}
		</div>
		<!-- RISULTATO PREZZI CORSA -->
		<div class="panel-body">
		<c:forEach items="${RIS.resultMedio}" var="varObj" varStatus="loop">
		<div class="row ">
			<div class=" col-xs-12 col-sm-12 col-md-12 col-lg-12">
				<div class="text-info "><strong><fmt:message key="${varObj.classeAutoveicolo.nome}"/>&nbsp;<fmt:message key="${varObj.classeAutoveicolo.nome}.num.pass"/>&nbsp;<fmt:message key="${varObj.classeAutoveicolo.nome}.desc"/></strong></div>
				<div class="text-info "><small><strong><em>${descrizioneCategorieAutoMap[varObj.classeAutoveicolo.id]}</em></strong></small></div>
			</div>
		</div>
		<div class="row ">
			<div class=" col-xs-4 col-sm-3 col-md-3 col-lg-3 ">
				<label class="control-label "><small>Compeso Autista </small></label>
				<fmt:formatNumber var="prezzoTotaleAutista" pattern="0.00" value="${varObj.prezzoTotaleAutista}" />
				<input type="text" value="${prezzoTotaleAutista}" class="form-control input-sm"  id="compensoAutistaId" disabled> 
			</div>
			<div class=" col-xs-4 col-sm-3 col-md-3 col-lg-3 ">
				<label class="control-label "><small>Commissione ApolloTransfert</small></label>
				<fmt:formatNumber var="prezzoCommissioneServizio" pattern="0.00" minFractionDigits="2" value="${varObj.prezzoCommissioneServizio + prezzoCommissioneServizioIva}" />
				<input type="text" value="${prezzoCommissioneServizio}" class="form-control input-sm"  id="compensoCommApolloId" disabled> 
			</div>
			<div class=" col-xs-4 col-sm-3 col-md-3 col-lg-3 bg-primary " style="border-radius: 5px;">
				<label class="control-label "><small>Commissione Venditore ${percentualeCommVenditore}&#37;</small></label>
				<fmt:formatNumber var="prezzoCommissioneVenditore" pattern="0.00" minFractionDigits="2" value="${varObj.prezzoCommissioneVenditore}" />
				<input type="text" value="${prezzoCommissioneVenditore}" class="form-control input-sm" style="margin-bottom: 15px;" name="tariffa-regione" id="compensoCommVenditore" disabled> 
			</div>
			<div class=" col-xs-4 col-sm-3 col-md-3 col-lg-3">
				<label class="control-label "><small>Totale Prezzo Cliente</small></label>
				<fmt:formatNumber var="prezzoTotaleCliente" pattern="0.00" minFractionDigits="2" value="${varObj.prezzoTotaleCliente}" />
				<input type="text" value="${prezzoTotaleCliente}" class="form-control input-sm" name="tariffa-regione" id="totalePrezzoCliente" disabled> 
			</div>
		</div>
		<c:if test="${!loop.last}"><div style="margin-bottom: 25px;"></div></c:if>
		</c:forEach>
		</div>
	</div>
	
	<div class="panel panel-primary">
		<div class="panel-heading"><strong>Scarica Fac-Simile Fattura Cliente</strong></div>
		<div class="panel-body">
		<c:forEach items="${RIS.resultMedio}" var="varObj" varStatus="loop">
			<div class=" col-xs-3 col-sm-2 col-md-2 col-lg-2 ">
			<a href="<c:url value="/pdfDownloadFatturaVenditoreTest?idClasseAutoveicolo=${varObj.classeAutoveicolo.id}&prezzoCommissioneServizioIva=${varObj.prezzoCommissioneServizioIva}&prezzoTotaleCliente=${varObj.prezzoTotaleCliente}"/>" >
			<span class="glyphicon glyphicon-download-alt"></span> <i class="fa fa-file-pdf-o" aria-hidden="true"></i> Fattura <fmt:message key="${varObj.classeAutoveicolo.nome}"/></a>
			</div>
		</c:forEach>
		</div>
	</div>
	</c:if>