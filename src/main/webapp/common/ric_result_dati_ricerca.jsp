<div class=" form-steps">
<div class="div-dep-2">
	<div class="row head-dep-2-row">
		<div class="col-md-6">
			<h3 class="text-right"><span class="prnt-dep"><c:choose>
			<c:when test="${not empty ricercaTransfert.formattedAddress_Partenza}">${ricercaTransfert.formattedAddress_Partenza}</c:when>
			<c:when test="${empty ricercaTransfert.formattedAddress_Partenza}">${ricercaTransfert.name_Partenza}</c:when>
			</c:choose></span></h3>
		</div>
		<div class="col-md-6">
			<h3 class="text-left"><span class="prnt-arr"><c:choose>
			<c:when test="${not empty ricercaTransfert.formattedAddress_Arrivo}">${ricercaTransfert.formattedAddress_Arrivo}</c:when>
			<c:when test="${empty ricercaTransfert.formattedAddress_Arrivo}">${ricercaTransfert.name_Arrivo}</c:when>
			</c:choose></span></h3>
		</div>
	</div>
	
	<div class="row dep-arr-info dep-2-row text-center">
		<div class="col-md-4">
			<h5><img src="<c:url value='/nuova_grafica/'/>images/info-calendar.png"><b>Partenza:</b><span class="prnt-date"><fmt:formatDate pattern="dd MMM, yy" value="${ricercaTransfert.dataOraPrelevamentoDate}" /></span></h5></div>
		<div class="col-md-4">	
			<h5><img src="<c:url value='/nuova_grafica/'/>images/info-clock.png"><b>Ora:</b><span class="prnt-time"><fmt:formatDate pattern="HH:mm" value="${ricercaTransfert.dataOraPrelevamentoDate}" /></span></h5>
		</div>
		<div class="col-md-4">	
			<h5><img src="<c:url value='/nuova_grafica/'/>images/info-group.png"><b>Num. Passeggeri:</b><span class="prnt-pgr">${ricercaTransfert.numeroPasseggeri}</span></h5>
		</div>
	</div>
	<div class="row dep-arr-info dep-2-row text-center">
		<div class="col-md-4">	
			<h5><img src="<c:url value='/nuova_grafica/'/>images/info-distance.png"><b>Distanza:</b><span class="prnt-dis">${ricercaTransfert.distanzaText}</span></h5>
		</div>
		<div class="col-md-4">	
			<h5><img src="<c:url value='/nuova_grafica/'/>images/info-duration.png"><b>Durata:</b><span class="prnt-dur">${ricercaTransfert.durataConTrafficoText}</span></h5>
		</div>
		<div class="col-md-4"></div>
	</div>
	<div class="row dep-arr-info dep-2-row text-center">
		<div class="col-md-12">	
			<h5><i class="fa fa-pencil-square-o" aria-hidden="true"></i> <b>Note per l'Autista:</b><span class="prnt-dis">${ricercaTransfert.notePerAutista}</span></h5>
		</div>
	</div>
</div>
	
<c:if test="${ricercaTransfert.ritorno && not empty ricercaTransfert.dataOraRitorno}">
<div class="div-dep-2 ">
	<div class="row head-dep-2-row">
		<div class="col-md-6">
			<h3 class="text-right"><span class="prnt-arr"><c:choose>
			<c:when test="${not empty ricercaTransfert.formattedAddress_Arrivo}">${ricercaTransfert.formattedAddress_Arrivo}</c:when>
			<c:when test="${empty ricercaTransfert.formattedAddress_Arrivo}">${ricercaTransfert.name_Arrivo}</c:when>
			</c:choose></span></h3>
		</div>
		<div class="col-md-6">
			<h3 class="text-left"><span class="prnt-dep"><c:choose>
			<c:when test="${not empty ricercaTransfert.formattedAddress_Partenza}">${ricercaTransfert.formattedAddress_Partenza}</c:when>
			<c:when test="${empty ricercaTransfert.formattedAddress_Partenza}">${ricercaTransfert.name_Partenza}</c:when>
			</c:choose></span></h3>
		</div>
	</div>
	
	<div class="row dep-arr-info dep-2-row text-center">
		<div class="col-md-4">
			<h5><img src="<c:url value='/nuova_grafica/'/>images/info-calendar.png"><b>Ritorno:</b><span class="prnt-date-arr"><fmt:formatDate pattern="dd MMM, yy" value="${ricercaTransfert.dataOraRitornoDate}"/></span></h5></div>
		<div class="col-md-4">	
			<h5><img src="<c:url value='/nuova_grafica/'/>images/info-clock.png"><b>Ora:</b><span class="prnt-time-arr"><fmt:formatDate pattern="HH:mm" value="${ricercaTransfert.dataOraRitornoDate}"/></span></h5>
		</div>
		<div class="col-md-4">	
			<h5><img src="<c:url value='/nuova_grafica/'/>images/info-group.png"><b>Num. Passeggeri:</b><span class="prnt-pgr">${ricercaTransfert.numeroPasseggeri}</span></h5>
		</div>
	</div>
	<div class="row dep-arr-info dep-2-row text-center">
		<div class="col-md-4">	
			<h5><img src="<c:url value='/nuova_grafica/'/>images/info-distance.png"><b>Distanza:</b><span class="prnt-dis">${ricercaTransfert.distanzaTextRitorno}</span></h5>
		</div>
		<div class="col-md-4">	
			<h5><img src="<c:url value='/nuova_grafica/'/>images/info-duration.png"><b>Durata:</b><span class="prnt-dur">${ricercaTransfert.durataConTrafficoTextRitorno}</span></h5>
		</div>
		<div class="col-md-4">	
			<h5><img src="<c:url value='/nuova_grafica/'/>images/info-duration.png"><b>Totale km Andata + Ritorno:</b><span class="prnt-ret"><c:out value="${((ricercaTransfert.distanzaValue + ricercaTransfert.distanzaValueRitorno) / 1000)} km" /></span></h5>
		</div>
	</div>
</div>
</c:if>
</div>