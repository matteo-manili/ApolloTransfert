<%@ include file="/common/taglibs.jsp"%>
<head>
	<title><fmt:message key="tragitti.title"/></title>
	<!-- jquery -->
	<link rel="stylesheet" href="<c:url value="/scripts/vendor/jquery-ui-1.11.4/jquery-ui.css"/>">
	<script src="<c:url value="/scripts/vendor/jquery-ui-1.11.4/external/jquery/jquery-2.2.4.min.js"/>"></script>
	<script src="<c:url value="/js/bootstrap.min.js"/>"></script>
	<script src="<c:url value="/scripts/vendor/jquery-ui-1.11.4/jquery-ui.min.js"/>"></script>
	<!-- chosen -->
	<link rel="stylesheet" href="<c:url value='/css/ChosenBootstrap.css'/>">
	<script src="<c:url value="/scripts/vendor/chosen_v1.5.1/chosen.jquery.min.js"/>"></script>
	<style type="text/css">
	</style>
</head>

<body>

<div class="content-area home-content">
<div class="container">
<div class="col-md-12">

<spring:bind path="autista.*">
	<c:if test="${not empty status.errorMessages}">
		<div class="alert alert-danger alert-dismissable">
			<a href="#" data-dismiss="alert" class="close">&times;</a>
			<c:forEach var="error" items="${status.errorMessages}">
			<c:out value="${error}" escapeXml="false"/><br/>
			</c:forEach>
		</div>
	</c:if>
</spring:bind>

<%@ include file="/common/messages.jsp" %>

<%@ include file="/common/menu_autista.jsp" %>

<h3>Zone di Lavoro (${autista.user.fullName})</h3>
 
<form:form commandName="autista" modelAttribute="autista" method="post" action="insert-tragitti" id="autistaFormId" autocomplete="off">
<form:hidden path="id"/>
<c:set var="tooltipINFRA" value="Le Infrastrutture sono punti di interesse relativi ai Territori. La spunta su di essi equivale a inglobarli nel Serivizio."/>
<c:set var="tooltipTerritorio" value="Territorio: è l'aera in cui si svolgono i Servizi. (A escusione del serivzio 'Lunga Percorrenza')."/>
<!-- NON LO USO PIU' PERKE IL CHOSEN NON FUNZIONA SUI BROWSER DEI TELEFONINI, L'HO MESSO A HIDDEN -->
<div class="row form-group hidden-xs hidden-sm hidden-md hidden-lg" style="display: none;">
	<div class="col-sm-10 ">
		<label for="listZoneSelezionate_TAG" class=" form-control-label text-info">
			Inserire la <strong>Provincia dell'ubicazione della Rimessa NCC</strong> ed eventuali Provincie di lavoro confinanti.</label>
		<form:select path="listZoneSelezionate_TAG" multiple="multiple" class="chosen-select " tabindex="1" data-placeholder="Viterbo (PROVINCIA).. Roma (PROVINCIA)..">
			<c:forEach items="${listTAG}" var="varTAG">
				<option selected="selected" value="${varTAG.value}">${varTAG.label}</option>
			</c:forEach>
		</form:select>
	</div>
	<div class="col-sm-2 ">
		<button type="submit" name="aggiorna" class="btn btn-primary attesaLoader" tabindex="2">Aggiungi Territorio <span class="fa fa-plus-circle"></span></button>
	</div>
</div>
<!-- select autocomplete per mobile -->
<div class="row ">
<div class="form-group col-sm-12 ">
	<div class="alert alert-info h4" role="alert">
  		<span>Inserire la <strong>Provincia di Lavoro</strong> dove avverr&agrave; il carico o lo scarico Passeggeri, ed eventuale Seconda provincia confinante.<br>
  			<c:if test="${not empty listProvConfinanti}">
  				(<c:forEach items="${listProvConfinanti}" var="item" varStatus="status">
					    ${item.nomeProvincia} <c:if test="${!status.last}">, </c:if>
				</c:forEach>)
			</c:if>
		</span>
	</div>
	<input id="inputZoneMobile" class=" form-control col-sm-3" placeholder="Viterbo (PROVINCIA).. Roma (PROVINCIA)..">
</div>
</div>
<!-- MODAL -->
<div id="modalResultTerrMobil" class="modal fade" tabindex="-1" role="dialog">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">result
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
			</div> 
			<div class="modal-body">
				<div class="row">
					<div id="listResultTerrMobil"></div>
				</div>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
$(document).ready(function(){
	$(".toolTip").tooltip();
	$( "#inputZoneMobile" ).autocomplete({
		source: function( request, response ) {
			$.ajax({
			type: 'POST',
				url: '${pageContext.request.contextPath}/listaZoneChosen?term='+request.term,
				//scriptCharset: "iso-8859-1",
				dataType: "json",
				contentType: 'application/json',
				data: {	},
				beforeSend: function(){ },
				success: function( data ) {
					//response( data );
					var htmlRes = "";
					response(
						$.map(data, function(value) {
							htmlRes = htmlRes + 
							"<div class=''>"+
								"<div class=' col-sm-8'>"+
										"<p class='h4' >"+value.text+"</p>"+
								"</div>"+
							"</div>"+
							"<div class=''>"+
								"<div class=' col-sm-4'>"+
										"<button type='submit' id="+value.value+" name='aggiorna-mobile' value="+value.value+" class='btn btn-primary btn-sm attesaLoader'>Aggiungi Provincia <span class='fa fa-plus-circle'></span></button>"+
								"</div>"+
							"</div>";
							$("#listResultTerrMobil").html(htmlRes);
						})
					);
					$("#modalResultTerrMobil").modal('show');
					$("#inputZoneMobile").val("");
				},
			   error: function (req, status, error) {
					alert('errore ajax autocomplete listaZoneChosen');
			  }
		});
		},
		minLength: 2,
		delay : 1000
	} );

	$(function() {
        $('.chosen-select').chosen( { enable_split_word_search: false } );
        $('.chosen-select-deselect').chosen({ allow_single_deselect: true });
        $('.chosen-choices input').autocomplete({
        	minLength: 2,
        	delay : 1000,
      	  	source: function( request, response ) {
      		var term = $('.chosen-choices input').val();
      		//alert(term);
      	    $.ajax({
      	    	type: 'POST',
					url: '${pageContext.request.contextPath}/listaZoneChosen?term='+term,
					//scriptCharset: "iso-8859-1",
					dataType: "json",
					contentType: 'application/json',
					data: {	},
					beforeSend: function(){
						//$('ul.chosen-results').empty();
						//$("#listZoneSelezionate_TAG").empty();
						//$("#listZoneSelezionate_TAG").trigger("chosen:updated");
						//$("#listZoneSelezionate_TAG").prepend("");
						//$("#listZoneSelezionate_TAG").trigger("chosen:updated");
					},
					success: function( data ) {
						response($.map(data.reverse(), function(value) {
							//$('ul.chosen-results').append('<li class="active-result">' + value.text + '</li>');
					         myFunction(value.value,value.text);
						}));
	      	      } //fine success
	      	      ,
		           error: function (req, status, error) {
		                alert('errore ajax autocomplete listaZoneChosen chosen');
		          }
      	    });
      	  }
      	});
	}); //fine funzioni chois
	
	function myFunction(p1, p2) {
		$("#listZoneSelezionate_TAG").prepend("<option value="+ p1 +">"+p2+"</option>");
		$("#listZoneSelezionate_TAG").trigger("chosen:updated");
	}
	
}); // fine read
</script>

<c:if test="${not empty autistaZoneList}">
	<!-- prima tabella  -->
	<div class="panel panel-default ">
		<div class="panel-heading text-right"> 
			<button type="button" name="rimuovi-tutti" class="btn btn-danger btn-sm alertConfirmGenerale">Elimina Territori</button>
		</div>
		<div class="panel-body">
			<c:forEach items="${autistaZoneList}" var="varObj" varStatus="status" begin="0" >
			<div class=" row">
				<div class=" col-sm-5">
					<p class="h4" >
					<c:choose>
					    <c:when test="${not empty varObj.province}">
					    	${varObj.province.nomeProvincia} (Provincia)
					    </c:when>
				    </c:choose>
					</p>
				</div>
				<div class=" col-sm-2">
					<c:choose>
					    <c:when test="${not empty varObj.province}">
					    	<button type="submit" name="rimuovi" value="PRO#${varObj.province.id}" class="btn btn-danger btn-sm attesaLoader"> <span class="fa fa-trash"></span></button>
					    </c:when>

			    	</c:choose>
				</div>
				<div class="hidden col-sm-3">
					<div class="checkbox">
					<label><input type="checkbox" class="" name="autistaZoneList.servizioAttivo[${varObj.id}]"
							value="${varObj.id}" ${varObj.servizioAttivo == true ? 'checked':''} /><span>Seriviz. Standard</span></label>
					</div>
				</div>
			</div>
			</c:forEach>
		</div>
	</div>
	<div class="form-group text-right">
		<button type="submit" name="salva" class="btn btn-success btn-lg attesaLoader" tabindex="3">Salva Modifiche <span class="fa fa-check"></span></button>
	</div>
</c:if>


</form:form>


</div>
</div>
</div>


<%@ include file="/scripts/ConfirmSubmit.jsp"%>


<!-- questa validazione funziona se nel form si usa il pulsante submit e non un link come sto usando -->
<c:set var="scripts" scope="request">
<script type="text/javascript">
// This is here so we can exclude the selectAll call when roles is hidden
// nel bottone submit: onclick="onFormSubmit( autoveicoloForm );"
function onFormSubmit(theForm) {
    return validateAutista(theForm);
}

</script>
</c:set>

<v:javascript formName="autista" staticJavascript="false"/>
<script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"></script>



</body>