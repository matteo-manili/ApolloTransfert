<div class="well ">

	<ul class=" nav nav-pills  col-md-offset-2 col-lg-offset-3" role="tablist">

	  <!-- GESTIONE AUTISTI -->
	  <c:if test="${amministratoreAutisti}">
		  <li role="presentation"> <!-- /admin/gestioneAutista?idAutista=44 -->
		  <a href="<c:url value='/admin/gestioneAutista?idAutista=${menuAutista.id}'/>"><i class="fa fa-users"></i> Gest. Autisti</a></li>
	  </c:if>
	  
	  <!-- AUTOVEICOLO -->
	  <li role="presentation" class="${numeroMenu == 1 ? 'active' : ''}">
	  <a href="<c:url value='/insert-autoveicolo?idAutista=${menuAutista.id}'/>"><i class="fa fa-car"></i> Autoveicolo 
	  <c:choose>
		 <c:when test="${ATTRIBUTE_AUTISTA_AUTOVEICOLO}">
			<span class="label label-success">ok</span>
		</c:when>    
		<c:otherwise>
			<span class="label label-warning">inserisci</span>
		</c:otherwise>
		</c:choose>
	  </a></li>
	  
	  <!-- ZONE LAVORO -->
	  <li role="presentation" class="${numeroMenu == 2 ? 'active' : ''}">
	  <a href="<c:url value='/insert-tragitti?idAutista=${menuAutista.id}'/>"><i class="fa fa-globe"></i> Zona Lavoro 
	  <c:choose>
		 <c:when test="${ATTRIBUTE_AUTISTA_ZONA_LAVORO}">
			<span class="label label-success">ok</span>
		</c:when>    
		<c:otherwise>
			<span class="label label-warning">inserisci</span>
		</c:otherwise>
		</c:choose>
	  </a></li>
	  
	  <!-- TARIFFE -->
	  <li role="presentation" class="${numeroMenu == 3 ? 'active' : ''}">
	  <a href="<c:url value='/insert-tariffe?idAutista=${menuAutista.id}'/>"><i class="fa fa-list"></i> Tariffe 
	  <c:choose>
		 <c:when test="${ATTRIBUTE_AUTISTA_TARIFFE}">
			<span class="label label-success">ok</span>
		</c:when>    
		<c:otherwise>
			<span class="label label-warning">inserisci</span>
		</c:otherwise>
		</c:choose>
	  </a></li>
	  
	  <!-- DISPONIBILITA' 
	  <li role="presentation" class=" ${numeroMenu == 4 ? 'active' : ''}">
	  <a href="<c:url value='/insert-disponibilita?idAutista=${menuAutista.id}'/>"><i class="fa fa-calendar"></i> Disponibilit&agrave; 
	  <c:choose>
		 <c:when test="${ATTRIBUTE_AUTISTA_AUTO_DISPONIBILITA}">
			<span class="label label-success">ok</span>
		</c:when>
		<c:otherwise>
			<span class="label label-warning">inserisci</span>
		</c:otherwise>
		</c:choose>
	  </a></li> -->
	  
	  <!-- DOCUMENTI -->
	  <li role="presentation " class=" ${numeroMenu == 5 ? 'active' : ''}">
	  <a href="<c:url value='/insert-documenti?idAutista=${menuAutista.id}'/>"><i class="fa fa-file-text-o"></i> Documenti 
	  <c:choose>
		 <c:when test="${ATTRIBUTE_AUTISTA_DOCUMENTI}">
			<span class="label label-success">ok</span>
		</c:when>    
		<c:otherwise>
			<span class="label label-warning">inserisci</span>
		</c:otherwise>
		</c:choose>
	  </a></li>
	  
	</ul>
	</div>