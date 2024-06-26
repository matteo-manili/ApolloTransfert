<%@ include file="/common/taglibs.jsp"%>
<head>
    <title>Vedi Fatture</title>
	<!-- jquery -->
	<link rel="stylesheet" href="<c:url value="/scripts/vendor/jquery-ui-1.11.4/jquery-ui.css"/>">
	<script src="<c:url value="/scripts/vendor/jquery-ui-1.11.4/external/jquery/jquery-2.2.4.min.js"/>"></script>
	<script src="<c:url value="/js/bootstrap.min.js"/>"></script>
	<script src="<c:url value="/scripts/vendor/jquery-ui-1.11.4/jquery-ui.min.js"/>"></script>
	<%@ include file="/scripts/apollotransfert-js-utility.jsp"%>
</head>
<body>

<div class="content-area home-content">
<div class="container">
<div class="">

	<h3 style="margin-top: -20px;">Vedi Fatture</h3>
	<c:choose>
        <c:when test="${ stripeSWITCH == false && payPalSWITCH == false }">
			<div class="alert alert-info " role="alert">
				<strong><p>Modalit&agrave; in STATO di TEST.</p></strong> 
			</div>
        </c:when>
        <c:when test="${ stripeSWITCH == true || payPalSWITCH == true }">
			<div class="alert alert-success " role="alert">
				<strong><p>Modalit&agrave; in STATO di PRODUZIONE.</p></strong>
			</div>
        </c:when>
	</c:choose>
	
	<%@ include file="/common/messages.jsp" %>
	<c:if test="${not empty ricercaTransfert}">
		<form:form commandName="ricercaTransfert" method="post" action="admin-vediFatture" id="vediFattureAdmin" autocomplete="off" onsubmit="">
		<form:hidden path="id"/>
			<%@ include file="/common/gestione-rimborso.jsp" %>
			<div class="well">
				<!-- pulsanti -->
				<div class="row  ">
					<div class=" pull-right">
						<button type="submit" name="cancel" class="btn btn-warning">Annulla <span class="fa fa-ban"></span></button>

						<button type="button" id="${ricercaTransfert.id}" class="btn btn-info checkFatturaDisponibile">Scarica Fattura Per Cliente 
						<span class="glyphicon glyphicon-download-alt"></span></button>
					</div>
				</div>
			</div>
		</form:form>
	</c:if>

	<div class="col-sm-12">
		<div class="form-group row">
			<form method="get" action="${ctx}/admin/admin-vediFatture" id="searchForm" class="form-inline" role="form">
				<div class="input-group">
					<label class="sr-only" for="query">ricerca</label>
					<input type="text" name="q" class="form-control" size="30" id="query" value="${param.q}" placeholder="Inserisci IdCorsa o Num.Fattura">
					<span class="input-group-btn">
						<button type="submit" name="ricerca" id="button.search" class="btn btn-default">ricerca</button>
					</span>
				</div>
			</form>
		</div> 
		
		<!-- TABELLA  -->
		<div class="form-group row">
			<display:table name="fattureList" cellspacing="0" cellpadding="0" requestURI=""
	                   defaultsort="" id="rowFattura" pagesize="50" class="table table-condensed table-striped" export="true">
				<display:column sortable="true" >
					<c:if test="${not empty rowFattura.ricercaTransfert}">
		         		${rowFattura.ricercaTransfert.id}
		         	</c:if>
		         	<c:if test="${not empty rowFattura.ricercaTransfertRimborso}">
		         		${rowFattura.ricercaTransfertRimborso.id}
		         	</c:if>
		         	<c:if test="${not empty rowFattura.ritardi}">
		         		${rowFattura.ritardi.ricercaTransfert.id}
		         	</c:if>
		         	<c:if test="${not empty rowFattura.supplementi}">
		         		${rowFattura.supplementi.ricercaTransfert.id}
		         	</c:if>
       			</display:column>
				
				<display:column property="progressivoFattura" sortable="true" />
				
				<display:column sortable="false" > 
        			<c:if test="${not empty rowFattura.ricercaTransfert || not empty rowFattura.ricercaTransfertRimborso}">
		         		<a id="${rowFattura.id}" href="#" onclick="return false;" class="pdfDownloadFatturaCommercialista">
		         			<i class="fa fa-file-text"></i> Scarica Fattura</a>
		         	</c:if>
		         	<c:if test="${not empty rowFattura.ritardi}">
		         		<a id="${rowFattura.ritardi.ricercaTransfert.id}" href="#" onclick="return false;" class="checkFatturaRitardoDisponibile">
		         			<i class="fa fa-file-text"></i> Scarica Fatt. Ritardo</a>
		         	</c:if>
		         	<c:if test="${not empty rowFattura.supplementi}">
		         		<a id="${rowFattura.supplementi.id}" href="#" onclick="return false;" class="checkFatturaSupplementoDisponibile">
		         			<i class="fa fa-file-text"></i> Scarica Fatt. Supplemento</a>
		         	</c:if>
    			</display:column>
    			
				<c:set var="prezzoTotCliente" value="${(rowFattura.ricercaTransfert.pagamentoParziale) ? rowFattura.ricercaTransfert.richiestaMediaScelta.prezzoCommissioneServizio : rowFattura.ricercaTransfert.richiestaMediaScelta.prezzoTotaleCliente}" />
				<c:set var="prezzoTotClienteParticolare" value="${rowFattura.ricercaTransfert.richiestaAutistaParticolareAcquistato.prezzoTotaleCliente}" />
				<c:set var="prezzoTotClienteMultiplo" value="${rowFattura.ricercaTransfert.richiestaAutistaParticolareAcquistato_Multiplo}" />
				<c:set var="prezzoTotClienteRimborso" value="${rowFattura.ricercaTransfertRimborso.richiestaMediaScelta.rimborsoCliente}" />
				<c:set var="prezzoTotClienteRimborsoParticolare" value="${rowFattura.ricercaTransfertRimborso.richiestaAutistaParticolareAcquistato.rimborsoCliente}" />
				<c:set var="prezzoTotClienteRimborsoMultiplo" value="${rowFattura.ricercaTransfertRimborso.rimborsoClienteRichiestaAutistaMultiplo}" />
				<display:column sortable="false" >
					<c:if test="${not empty prezzoTotCliente}">${prezzoTotCliente}&euro;</c:if>
					<c:if test="${not empty prezzoTotClienteParticolare}">${prezzoTotClienteParticolare}&euro;</c:if>
					<c:if test="${not empty prezzoTotClienteMultiplo}">${prezzoTotClienteMultiplo}&euro;</c:if>
					<c:if test="${not empty prezzoTotClienteRimborso}"><em>(NotaDiCredito ${prezzoTotClienteRimborso}&euro; IdCorsa ${rowFattura.ricercaTransfertRimborso})</em></c:if>
					<c:if test="${not empty prezzoTotClienteRimborsoParticolare}"><em>(NotaDiCredito ${prezzoTotClienteRimborsoParticolare}&euro; IdCorsa ${rowFattura.ricercaTransfertRimborso})</em></c:if>
					<c:if test="${not empty prezzoTotClienteRimborsoMultiplo}"><em>(NotaDiCredito ${prezzoTotClienteRimborsoMultiplo}&euro; IdCorsa ${rowFattura.ricercaTransfertRimborso})</em></c:if>
       			</display:column>
       			
       			<c:choose>
       			<c:when test="${not empty rowFattura.ricercaTransfert}">
	       			<display:column sortable="false">
					<b>ProvPay ${fn:contains(rowFattura.ricercaTransfert.idPaymentProvider, 'ch_') ? 'Stripe' : 'PayPal'}</b>
					</display:column>
	       			
	       			<display:column sortable="false">
					ProvPay Lordo ${not empty rowFattura.ricercaTransfert.providerPagamentoInfoInfo.amount ? rowFattura.ricercaTransfert.providerPagamentoInfo.amount : '-'}&euro;
					</display:column>
					
					<display:column sortable="false">
					ProvPay Refund ${not empty rowFattura.ricercaTransfert.providerPagamentoInfo.refund ? rowFattura.ricercaTransfert.providerPagamentoInfo.refund : '-'}&euro;
					</display:column>
	       			
	       			<display:column sortable="false">
					ProvPay Tassa ${not empty rowFattura.ricercaTransfert.providerPagamentoInfo.fee ? rowFattura.ricercaTransfert.providerPagamentoInfo.fee : '-'}&euro;
					</display:column>

					<c:set var="provPayNetto" value="${ not empty rowFattura.ricercaTransfert.providerPagamentoInfo.amount && not empty rowFattura.ricercaTransfert.providerPagamentoInfo.fee 
						? rowFattura.ricercaTransfert.providerPagamentoInfo.amount - rowFattura.ricercaTransfert.providerPagamentoInfo.fee : ''}" />
					<display:column sortable="false">
					ProvPay Netto ${provPayNetto}&euro;
					</display:column>
       			
	       			<c:set var="prezzoTotAutista" value="${(rowFattura.ricercaTransfert.pagamentoParziale) ? '' : rowFattura.ricercaTransfert.richiestaMediaAutistaCorsaConfermata.prezzoTotaleAutista}" />
	       			<c:set var="prezzoTotAutistaParticolare" value="${rowFattura.ricercaTransfert.richiestaAutistaParticolareAcquistato.prezzoTotaleAutista}" />
	       			<display:column sortable="false" >
	       				<c:if test="${not empty prezzoTotAutista}">CompensoAutista ${prezzoTotAutista}&euro;</c:if>
	       				<c:if test="${not empty prezzoTotAutistaParticolare}">CompensoAutista ${prezzoTotAutistaParticolare}&euro;</c:if>
	       			</display:column>
      			</c:when>
       			<c:otherwise>
       				<display:column sortable="false"></display:column>
       				<display:column sortable="false"></display:column>
       				<display:column sortable="false"></display:column>
       				<display:column sortable="false"></display:column>
       				<display:column sortable="false"></display:column>
       				<display:column sortable="false"></display:column>
       			</c:otherwise>
     			</c:choose>

				<display:column sortable="false">
					<fmt:formatDate pattern="dd/MM/yyyy" value="${rowFattura.ricercaTransfert.dataRicerca}" />
					<fmt:formatDate pattern="dd/MM/yyyy" value="${rowFattura.ricercaTransfertRimborso.dataRicerca}" />
				</display:column>
				
				<display:column sortable="false" >
					<c:if test="${not empty rowFattura.ricercaTransfert}">
		         		<a href="<c:url value='/admin/admin-vediFatture'/>?idCorsa=${rowFattura.ricercaTransfert.id}">${rowFattura.ricercaTransfert.partenzaRequest}</a>
		         	</c:if>
		         	<c:if test="${not empty rowFattura.ricercaTransfertRimborso}">
		         		<a href="<c:url value='/admin/admin-vediFatture'/>?idCorsa=${rowFattura.ricercaTransfertRimborso.id}">${rowFattura.ricercaTransfertRimborso.partenzaRequest}</a>
		         	</c:if>
       			</display:column>
       			
       			<display:column sortable="false" >
					<c:if test="${not empty rowFattura.ricercaTransfert}">
		         		<a href="<c:url value='/admin/admin-vediFatture'/>?idCorsa=${rowFattura.ricercaTransfert.id}">${rowFattura.ricercaTransfert.arrivoRequest}</a>
		         	</c:if>
		         	<c:if test="${not empty rowFattura.ricercaTransfertRimborso}">
		         		<a href="<c:url value='/admin/admin-vediFatture'/>?idCorsa=${rowFattura.ricercaTransfertRimborso.id}">${rowFattura.ricercaTransfertRimborso.arrivoRequest}</a>
		         	</c:if>
       			</display:column>
       			
       			<display:column sortable="false" >
					<c:if test="${not empty rowFattura.ricercaTransfert}">
		         		${rowFattura.ricercaTransfert.ritorno ? "RIT." : "S.A."}
		         	</c:if>
		         	<c:if test="${not empty rowFattura.ricercaTransfertRimborso}">
		         		${rowFattura.ricercaTransfertRimborso.ritorno ? "RIT." : "S.A."}
		         	</c:if>
       			</display:column>
       			
       			<c:choose>
       			<c:when test="${not empty rowFattura.ricercaTransfert}">
	       			<display:column sortable="false">
						Tipo Serv. ${not empty rowFattura.ricercaTransfert.tipoServizio ? rowFattura.ricercaTransfert.tipoServizio : '-'}
					</display:column>
				</c:when>
	       			<c:otherwise>
	       				<display:column sortable="false"></display:column>
	       			</c:otherwise>
     			</c:choose>
       			
	    	</display:table>
		</div>
	</div>

</div>
</div>
</div>
<%@ include file="/scripts/ConfirmSubmit.jsp"%>

</body>