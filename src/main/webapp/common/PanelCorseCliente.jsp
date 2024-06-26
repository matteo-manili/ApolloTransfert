<div class=" ">
<p class=" "><strong><fmt:message key="info.cliente.modifica.dati.fattura">
	<fmt:param value="${linkModificaDatiFattura}"/><fmt:param value="${textLinkModificaDatiFattura}"/></fmt:message></strong></p>
<p class=" "><strong><fmt:message key="info.cliente.numero.ore.disdetta.rimborso.transfer">
	<fmt:param value="${maxOreDisdettaCliente}"/></fmt:message></strong></p>
<p class=" "><strong><fmt:message key="info.cliente.ritardo.addebito.costo.mezzora">
	<fmt:param value="${VALORE_EURO_ORA_RITARDO_CLIENTE_CON_TASSA_SERVZIO}"/>
	<fmt:param value="${VALORE_EURO_ORA_RITARDO_CLIENTE}"/></fmt:message></strong></p>
</div>
<%@ include file="/common/TemplateCorseCliente.jsp"%>
<%@ include file="/common/TemplateCorseCliente_js.jsp"%>
<!-- Nav tabs cliente -->
<ul class="nav nav-tabs" role="tablist">
<li role="presentation" class="active"><a href="#corse_ese_cliente" aria-controls="corse_cliente" role="tab" data-toggle="tab">
  	Corse da Eseguire <span class="label label-success">${corseDaEseguireCliente}</span></a></li>
<li role="presentation"><a href="#corse_compl_cliente" aria-controls="corse_cliente" role="tab" data-toggle="tab">
	Corse Completate <span class="label label-default">${corseEseguiteCliente}</span></a></li>
</ul>
<!-- Tab panes -->
<div class="tab-content">
	<div role="tabpanel" class="tab-pane active" id="corse_ese_cliente">
		<div id="corseClienteA"></div>
		 <c:if test="${corseDaEseguireCliente == 0}">
			<div class="col-sm-12 row">
				<h5><strong>Non ci sono Corse da Eseguire da visualizzare</strong></h5>
			</div>
		</c:if>
	</div>
	<div role="tabpanel" class="tab-pane" id="corse_compl_cliente">
		<div id="corseClienteB"></div>
		<c:if test="${corseEseguiteCliente == 0}">
			<div class="col-sm-12 row">
				<h5>Non ci sono Corse Completate da visualizzare</h5>
			</div>
		</c:if>
	</div>
</div>