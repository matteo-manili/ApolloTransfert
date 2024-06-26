<%@ include file="/common/taglibs.jsp"%>
<head>
<link rel="canonical" href="<fmt:message key="https.w3.domain.apollotransfert.name"/><c:url value='/tariffe-transfer'/>"/>
<title>Prezzi Noleggio Con Conducente | ${menuTerrTariffeTransfer.title}</title>
<meta name="description" content="${menuTerrTariffeTransfer.metaDescription}"/>
<!-- <meta name="description" content="Viaggia in Italia a prezzi vantaggiosi, con la nostra piattaforma di noleggio NCC che ti permette di affittare van e auto in totale sicurezza. Vai alle tariffe!"/> -->
<!-- jquery -->
<script src="<c:url value="/scripts/vendor/jquery-ui-1.11.4/external/jquery/jquery-2.2.4.min.js"/>"></script>

<script type="text/javascript">
$(document).ready(function () {
	//alert( window.location.pathname )
	//alert( '${menuTerrTariffeTransfer.urlCanonical}' )
	if( 'tariffe-transfer' !== '${menuTerrTariffeTransfer.urlCanonical}' ){
		$('html, body').animate({
		    scrollTop: $("#menuTariffeId").offset().top -60
		}, 2000);
	}
	
}); // fine ready
</script>

</head>
<body>
<div class="page-banner" style="background-image:url('${pageContext.request.contextPath}/nuova_grafica/images/main-banner/pexels-torsten-dettlaff-70912.jpg');">
<h1>Le nostre tariffe per il noleggio di transfer con conducente in tutta Italia</h1>
</div>
<div class="content-area home-content">
<div class="container">
<div class="col-md-12">
<%@ include file="/common/messages.jsp" %>
	
	<p>Noi di <b><fmt:message key="webapp.apollotransfert.name"/></b> ti offriamo il modo più <b>comodo</b>, <b>veloce</b> ed <b>economico</b> per usufruire di servizi di 
	noleggio con conducente in <b>ogni regione italiana</b>. Di seguito troverai un esempio delle nostre tariffe, che cambiano a seconda della provincia, del modello auto o van e 
	dell'anno di immatricolazione del veicolo. I prezzi che vedi indicati nella tabella sottostante sono degli esempi di viaggi con partenza o arrivo di sola andata, 
	con IVA inclusa.</p>

	<div class="main-heading-row"><h2 class="text-center"><strong></strong>Le nostre vetture con conducente</h2></div>

	<p><fmt:message key="webapp.apollotransfert.name"/> ti garantisce un servizio di trasporto standard e speciale all'insegna dell'affidabilità e con un parco mezzi di prestigio. 
	A seconda delle tue necessità o del numero di passeggeri, potrai scegliere tra le seguenti categorie di veicoli quella più giusta per te.</p>

	<ul>
	<c:forEach var="ite" items="${descrizioneCategorieAutoList}" varStatus="status"> 
  	<li>${ite}</li>
	</c:forEach>
	</ul>

	<div class="text-center "><a href="<c:url value='/contatti'/>" class="btn btn-primary ">Contattaci per informazioni</a></div>
	
	<div class="main-heading-row"><h1 class="text-center">Tariffe Transfer</h1></div>


	<div id="menuTariffeId" class="panel panel-default" style="">
		<div class="panel-heading">
		<h2>Tariffe Transfer</h2>
		<!-- MACRO REGIONI -->
		<p>
		<c:forEach items="${menuTerrTariffeTransfer.macroRegioni}" var="varObj" varStatus="loop">
			<c:if test="${!loop.first}">&nbsp;</c:if>${varObj.selezionato ? '<strong><ins>' : ''}
			<a class="attesaLoader" href="<c:url value='${varObj.url}'/>">${varObj.territorio}${varObj.selezionato ? '</ins></strong>' : ''}</a>
			<c:if test="${!loop.last}"> |</c:if>
		</c:forEach>
		</p>
		<!-- REGIONI -->
		<p>
		<c:forEach items="${menuTerrTariffeTransfer.regioni}" var="varObj" varStatus="loop">
			<c:if test="${!loop.first}">&nbsp;</c:if>${varObj.selezionato ? '<strong><ins>' : ''}
			<a class="attesaLoader" href="<c:url value='${varObj.url}'/>">${varObj.territorio}${varObj.selezionato ? '</ins></strong>' : ''}</a>
			<c:if test="${!loop.last}"> |</c:if>
		</c:forEach>
		</p>
		<!-- PROVINCE -->
		<p>
		<c:forEach items="${menuTerrTariffeTransfer.province}" var="varObj" varStatus="loop">
			<c:if test="${!loop.first}">&nbsp;</c:if>${varObj.selezionato ? '<strong><ins>' : ''}
			<a class="attesaLoader" href="<c:url value='${varObj.url}'/>">${varObj.territorio}${varObj.selezionato ? '</ins></strong>' : ''}</a>
			<c:if test="${!loop.last}"> |</c:if>
		</c:forEach>
		</p>
		</div>
		<div class="panel-footer">
		Le seguenti <strong>Tariffe</strong> sono esempi di Partenza/Arrivo di <strong>Province</strong> e <strong>Infrastrutture</strong> di interesse locale.<br>
		I <strong>Prezzi</strong> delle <strong>Tariffe</strong> si intendono di un <strong>Trasporto Sola Andata</strong>, <strong>iva inclusa</strong>.<br>
		Per Calcolare il Prezzo di uno specifico Transfer <a href="<c:url value='/'/>"><strong>Vai in questa pagina</strong></a>.
		</div>
	</div>
	<!-- RISULTATI -->
	<div class="panel panel-default">
	<div class="panel-body">
	<fmt:formatNumber var="NUMBER_COL" value="${fn:length(menuTerrTariffeTransfer.transferTariffeProvince.tariffa_Province_e_Infrastrutture) > 1 ? 24 / fn:length(menuTerrTariffeTransfer.transferTariffeProvince.tariffa_Province_e_Infrastrutture) : 12}" maxFractionDigits="0" />
	<c:forEach items="${menuTerrTariffeTransfer.transferTariffeProvince.tariffa_Province_e_Infrastrutture}" var="varObj" varStatus="loop">
		<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
		<div class="panel panel-default">
		<div class="panel-heading"><h3 class="panel-title">${varObj.titoloCorsaPartenza}&nbsp;<span class="glyphicon glyphicon-arrow-right"></span>&nbsp;${varObj.titoloCorsaArrivo}&nbsp;<span class="label label-success">${varObj.numeroKilometro} km</span></h3>
		</div>
		<div class="panel-body">
			<ul class="list-group">
				<c:forEach items="${varObj.tariffaClasseAuto}" var="varObj" >
				<li class="list-group-item">${varObj.titoloClasseAuto},&nbsp;${varObj.numeroPasseggeri},&nbsp;${varObj.classeAutoveicoloDesc}&nbsp;<span class="label label-primary">${varObj.prezzoClinte}&euro;</span>
				</li>
				</c:forEach>
			</ul>
		</div>
		</div>
		</div>
	</c:forEach>
	</div>
	</div>
	
</div>
</div>
</div>

<%@ include file="/scripts/ConfirmSubmit.jsp"%>
<!-- bootstrap JS -->
<script async src="<c:url value="/js/bootstrap.min.js"/>"></script>
</body>