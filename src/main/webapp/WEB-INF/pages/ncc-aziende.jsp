<%@ include file="/common/taglibs.jsp"%>
<head>
<link rel="canonical" href="<fmt:message key="https.w3.domain.apollotransfert.name"/><c:url value='/ncc-aziende'/>"/>
<title>NCC per Aziende</title>
<meta name="description" content="Viaggia in Italia a prezzi vantaggiosi, con la nostra piattaforma di noleggio NCC che ti permette di affittare van e auto in totale sicurezza. 
Vai alle tariffe!"/>
<script src="<c:url value="/scripts/vendor/jquery-ui-1.11.4/external/jquery/jquery-2.2.4.min.js"/>"></script>
</head>
<body>
<div class="page-banner" style="background-image:url('${pageContext.request.contextPath}/nuova_grafica/images/main-banner/pexels-sora-shimazaki-5668440.jpg');">
<h1>NCC per Aziende in tutta italia</h1>
</div>
<!-- NUOVA GRAFICA -->
<div class="content-area">
<div class="container">
<div class="col-md-12">
<%@ include file="/common/messages.jsp" %>
	
	<p>Se un'azienda e per i tuoi clienti necessiti il servizio di NCC? rivolgiti a noi. Offriamo <b>servizi di trasporto NCC</b> da e per aeroporti, stazioni, 
	hotel, convegni, fiere e qualsiasi altra destinazione i tuoi clienti debbano raggiungere. Richiedi maggiori informazioni sui nostri pacchetti di 
	noleggio auto con conducente a prezzi agevolati!</p>
	
	<h2 class="text-center">Le nostre funzionalità dedicate alle aziende</h2>

	<%@ include file="/common/elenco-funzionalita.jsp"%>
	
	<!-- INIZIO RECENSIONI -->
	<c:if test="${fn:length(recensioniApprovate) >= 3 && true}">
	<!--  INIZIO RECENSIONI -->
	<!-- prime tre recensione con "item active" -->
	<!-- successive tre recensione con "item" -->
	<link rel="stylesheet" href="<c:url value='/nuova_grafica/css/recensioni.css'/>">
	<h2 class="text-center title-striscia-blue">&#9733; LE RECENSIONI DEI NOSTRI CLIENTI &#9733;</h2>
	<div class="form-group">
		<div class="carousel-reviews broun-block">
            <div id="carousel-reviews" class="carousel slide" data-ride="carousel">
                <div class="carousel-inner">
					<c:set var="totalHours" value="${0}"/>
					<c:forEach var="varObj" items="${recensioniApprovate}" varStatus="loop">				
						<c:if test="${loop.first}"><div class="item active"></c:if>
				    	<c:if test="${not loop.first and loop.index % 3 == 0 }"><div class="item"></c:if>
				    	<!-- 
				    	<p> <b>COUNT=${totalHours}</b> &nbsp; <b>${not empty varObj[2] ? varObj[2] : varObj[3]}</b> &nbsp; ${varObj[4]} &nbsp; ${varObj[0]} &nbsp; ${varObj[1]} &nbsp; loop=${loop.index} </p>
				    	 -->
				    	 <!-- col-md-4 col-sm-6 hidden-xs -->
				    	<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4">
			     			<div class="block-text rel zmin ">
						        <a title="" href="#" onclick="return false;">${varObj[0]}&nbsp;<i class="fa fa-arrow-right" aria-hidden="true"></i>&nbsp;${varObj[1]}</a>
							    <div class="mark">Punteggio:
								    <span class="rating-input">
								    	<c:set var="numStelle" value="${varObj[5]}" />
								    	<span data-value="0" class="glyphicon ${numStelle >= 1 ? 'glyphicon-star' : 'glyphicon-star-empty'}"></span>
								    	<span data-value="1" class="glyphicon ${numStelle >= 2 ? 'glyphicon-star' : 'glyphicon-star-empty'}"></span>
								    	<span data-value="2" class="glyphicon ${numStelle >= 3 ? 'glyphicon-star' : 'glyphicon-star-empty'}"></span>
								    	<span data-value="3" class="glyphicon ${numStelle >= 4 ? 'glyphicon-star' : 'glyphicon-star-empty'}"></span>
								    	<span data-value="4" class="glyphicon ${numStelle == 5 ? 'glyphicon-star' : 'glyphicon-star-empty'}"></span>
								    </span>
							    </div>
							    <p>${varObj[2]}&nbsp;${fn:substring(varObj[9], 0, 1)}.</p>
						        <p>${varObj[4]}</p>
							    <ins class="ab zmin sprite sprite-i-triangle block"></ins>
					        </div>
							<div class="person-text rel ">
				                  <img src=""/> 
								  <!--  <a title="" href="#">${not empty varObj[2] ? varObj[2] : varObj[3]}</a>
								  <p><i>${not empty varObj[2] ? varObj[2] : varObj[3]}</i></p> -->
								  <a href="mailto:${varObj[6]}">${varObj[8]}</a>
							</div>
						</div>
				    	<c:if test="${totalHours == 2 || loop.last}"> </div> 
				    	</c:if>
						<c:set var="totalHours" value="${totalHours == 2 ? 0 : totalHours + 1 }"/>
					</c:forEach>
				</div>
                <a class="left carousel-control" href="#carousel-reviews" role="button" data-slide="prev">
                    <span class="glyphicon glyphicon-chevron-left"></span>
                </a>
                <a class="right carousel-control" href="#carousel-reviews" role="button" data-slide="next">
                    <span class="glyphicon glyphicon-chevron-right"></span>
                </a>
            </div>
		    
		</div>
	</div> 
	</c:if>
	<!-- FINE RECENSIONI -->
	
	
</div>
</div>
</div>



<!-- bootstrap JS -->
<script async src="<c:url value="/js/bootstrap.min.js"/>"></script>
</body>