<%@ include file="/common/taglibs.jsp"%>
<head>
<title>Tariffe Autisti</title>
<meta name="description" content="Transfer NCC Noleggio Auto Con Conducente "> 
<link rel="canonical" href="<fmt:message key="https.w3.domain.apollotransfert.name"/><c:url value='/tariffe-autisti'/>"/>

<script src="<c:url value="/scripts/vendor/jquery-ui-1.11.4/external/jquery/jquery-2.2.4.min.js"/>"></script>
<script src="<c:url value="/js/bootstrap.min.js"/>"></script>

<!-- calendario e time-->
<script src="<c:url value="/scripts/vendor/pickadate.js-master/lib/compressed/picker.js"/>"></script>
<script src="<c:url value="/scripts/vendor/pickadate.js-master/lib/compressed/picker.date.js"/>"></script>
<script src="<c:url value="/scripts/vendor/pickadate.js-master/lib/compressed/picker.time.js"/>"></script>
<script src="<c:url value="/scripts/vendor/pickadate.js-master/lib/compressed/translations/"/><fmt:message key="language.code"/>.js"></script>
<link rel="stylesheet" href="<c:url value="/scripts/vendor/pickadate.js-master/lib/compressed/themes/default.css"/>">
<link rel="stylesheet" href="<c:url value="/scripts/vendor/pickadate.js-master/lib/compressed/themes/default.date.css"/>">
<link rel="stylesheet" href="<c:url value="/scripts/vendor/pickadate.js-master/lib/compressed/themes/default.time.css"/>">

<!-- alert confirm bootstrap -->
<script src="<c:url value="/scripts/vendor/bootbox.min.js"/>"></script>

<script type="text/javascript">
if( true ){
	$.ajax({
		type: 'POST',
		url: '${pageContext.request.contextPath}/memorizzaIpUtente',
		dataType: "json",
		data: { },
		beforeSend: function(){ },
		success: function(result) { }, //fine success
		error: function (req, status, error) {
			//alert('errore ajax memorizzaIpUtente');
		}
	});
}
	
$(document).ready(function () {
	//----- CALENDARIO ---------
	var from_$input = $('#calendarioFrom').pickadate({
		//format: 'd mmmm, yyyy',
		formatSubmit: 'yyyy/mm/dd',
		onClose: function() {
			  $(document.activeElement).blur();
			}
	}), from_picker = from_$input.pickadate('picker')
	
	 //$('#calendarioFromHidden').val( event.select )
	 //$('#calendarioToHidden').val( event.select );
	
	var to_$input = $('#calendarioTo').pickadate({
		onClose: function() {
			  $(document.activeElement).blur();
			}
	}), to_picker = to_$input.pickadate('picker')
	
	// Check if there’s a “from” or “to” date to start with.
	if ( from_picker.get('value') ) {
	  to_picker.set('min', from_picker.get('select'))
	}
	if ( to_picker.get('value') ) {
	  from_picker.set('max', to_picker.get('select'))
	}
	
	// When something is selected, update the “from” and “to” limits.
	from_picker.on('set', function(event) {
	  if ( event.select ) {
		  $('#calendarioFromHidden').val(  event.select );
	    to_picker.set('min', from_picker.get('select'))
	  }
	  else if ( 'clear' in event ) {
		  $('#calendarioFromHidden').val( '' );
	    to_picker.set('min', false)
	  }
	})
	to_picker.on('set', function(event) {
	  if ( event.select ) {
		  $('#calendarioToHidden').val(  event.select );
	    from_picker.set('max', to_picker.get('select'))
	  }
	  else if ( 'clear' in event ) {
		  $('#calendarioToHidden').val( '' );
	    from_picker.set('max', false)
	  }
	});
	
}); // fine ready
</script>

</head>
<body>
<%@ include file="/common/messages.jsp" %>
<div class="content-area home-content">
<div class="container">
<div class="col-md-12">

	<div class="row form-group ">
		<div class=" col-sm-1">dal giorno</div>
		<div class=" col-sm-5">
			<div class="input-group ">
				<span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span>
				<input type="text" class="form-control" id="calendarioFrom" placeholder="da...">
				<input type="hidden" id="calendarioFromHidden">
			</div>
		</div>
		<div class=" col-sm-1">al giorno</div>
		<div class=" col-sm-5">
			<div class="input-group ">
				<span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span>
				<input type="text" class="form-control" id="calendarioTo" placeholder="a...">
				<input type="hidden" id="calendarioToHidden">
			</div>
		</div>
	</div>
	<div class="row form-group ">
		<div class=" col-sm-3">Partenza</div>
		<div class=" col-sm-9">
			<input type="text" value="${ricercaTransfert.partenzaRequest}" name="partenzaRequest" class="form-control" id="idPartenza" placeholder="Partenza">
		</div>
	</div>
	<div class="row form-group ">
		<div class=" col-sm-3">Arrivo (valorizzare se provincia Arrivo � diversa dalla provincia Partenza)</div>
		<div class=" col-sm-9 ">
			<input type="text" value="${ricercaTransfert.arrivoRequest}" name="arrivoRequest" class="form-control" id="idArrivo" placeholder="Arrivo">
		</div>
	</div>
	
</div>
</div>
</div>
<%@ include file="/scripts/AutocompleteRicercaTransferGoogleMapUtil.js"%>

</body>