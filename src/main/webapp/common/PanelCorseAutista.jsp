<%@ include file="/common/TemplateCorseAutista.jsp"%>
<%@ include file="/common/TemplateCorseAutista_js.jsp"%>
	<!-- Nav tabs Autista -->
<ul class="nav nav-tabs" role="tablist">
  <li role="presentation" class="active"><a href="#corse_ese" aria-controls="corse" role="tab" data-toggle="tab">
  	Corse da Eseguire <span class="label label-success">${corseDaEseguire}</span></a></li>
<li role="presentation"><a href="#corse_disp" aria-controls="corse" role="tab" data-toggle="tab">
	Corse Disponibili <span class="label label-warning">${corseDisponibili}</span></a></li>
<li role="presentation"><a href="#corse_compl" aria-controls="corse" role="tab" data-toggle="tab">
	Corse Completate <span class="label label-default">${corseEseguite}</span></a></li>
</ul>
<!-- Tab panes -->
<div class="tab-content">
	<div role="tabpanel" class="tab-pane active" id="corse_ese">
		<div id="corseA"></div>
		 <c:if test="${corseDaEseguire == 0}">
			<div class="col-sm-12 row">
				<h5><strong>Non ci sono Corse da Eseguire da visualizzare</strong></h5>
			</div>
		</c:if>
	</div>
	<div role="tabpanel" class="tab-pane" id="corse_disp">
		<div id="corseC"></div>
		<c:if test="${corseDisponibili == 0}">
			<div class="col-sm-12 row">
				<h5><strong>Non ci sono Corse Disponibili da visualizzare</strong></h5>
			</div>
		</c:if>
	</div>
	<div role="tabpanel" class="tab-pane" id="corse_compl">
		<div id="corseB"></div>
		<c:if test="${corseEseguite == 0}">
			<h5><strong>Non ci sono Corse Disponibili da visualizzare</strong></h5>
		</c:if>
	</div>
</div>