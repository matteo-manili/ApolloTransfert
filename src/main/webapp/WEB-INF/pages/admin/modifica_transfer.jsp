<%@ include file="/common/taglibs.jsp"%>
<head>
<title>Modifica Transfer ${ricercaTransfert.id}</title>
<script src="<c:url value="/scripts/vendor/jquery-ui-1.11.4/external/jquery/jquery-2.2.4.min.js"/>"></script>
<script src="<c:url value="/js/bootstrap.min.js"/>"></script>
<script src="<c:url value="/scripts/vendor/bootbox.min.js"/>"></script>
<!-- CSS calendar e time -->
<link rel="stylesheet" href="<c:url value="/scripts/vendor/pickadate.js-master/lib/compressed/themes/default.total.css"/>">
<!-- apollotransfert-js-utility -->
<%@ include file="/scripts/apollotransfert-js-utility.jsp"%>
<script type="text/javascript">
jQuery(document).ready(function(){
	$("#modificaTransferId").click(function() {
		$.ajax({
	    	type: 'POST',
            url: '${pageContext.request.contextPath}/modificaTransfer',
            dataType: "json",
			data: {
				data : $("#modificaTransferFormId").serialize()
			},
			beforeSend: function(){ },
			success: function(result) {
				if(result.esito){
					bootbox.confirm({
					    //title: "Modifica Eseguita",
					    message: "<strong><p class='text-success'>Modifica Eseguita.</p></strong>",
					    buttons: {
					        confirm: {
					            label: '<i class="fa fa-check"></i> Ok'
					        }
					    },
					    callback: function (resultBootBox) {
					    	location.reload();
					    }
					});
				}else{
					bootbox.alert("<strong><p class='text-muted'>Modifica Non Eseguita.</p></strong>");
				}
			}, //fine success
	        error: function (req, status, error) {
			alert('errore ajax modificaTransfer');}
		});
	});
}); // fine ready
</script>
</head>
<body>

<div class="content-area home-content">
<div class="container">
<div class="">

	<%@ include file="/common/messages.jsp" %>
	<c:if test="${not empty ricercaTransfert}">
	<div class="well ">
	<form:form commandName="ricercaTransfert" method="post" action="admin-modificaTransfer" id="modificaTransferFormId" autocomplete="off">
		<form:hidden path="id"/>
		
		<p class=""><big><strong><ins>TipoServizio: ${ricercaTransfert.tipoServizio}&nbsp;Corsa ID: ${ricercaTransfert.id}</ins></strong>
		<br>Prezzo Totale Cliente: ${ricercaTransfert.richiestaMediaScelta.prezzoTotaleCliente}
			|&nbsp;Comm. Servizio: ${ricercaTransfert.richiestaMediaScelta.prezzoCommissioneServizio + ricercaTransfert.richiestaMediaScelta.prezzoCommissioneServizioIva}
			|&nbsp;Comm. Venditore: ${ricercaTransfert.richiestaMediaScelta.prezzoCommissioneVenditore}
		<br>Nome e Telefono Cliente: ${ricercaTransfert.user.fullName}&nbsp;${ricercaTransfert.user.phoneNumber}<br>
		Numero Passeggeri: ${ricercaTransfert.numeroPasseggeri} | Nome e Telefono Passeggero: <span id="nomeTelefonoPasseggero-${ricercaTransfert.id}">${ricercaTransfert.nomeTelefonoPasseggero}</span>
		</big></p>
		<c:choose>
		<c:when test = "${ricercaTransfert.tipoServizio == 'ST'}">
			<div class=" ">
			<div class="form-group row col-lg-11">
				<big><strong>Autisti</strong>&nbsp;|&nbsp;<ins><fmt:message key="${ricercaTransfert.richiestaMediaScelta.classeAutoveicolo.nome}"/></ins></big>
				<ul class="">
				<c:forEach items="${ricercaTransfert.richiestaMediaScelta.richiestaMediaAutista}" var="varObj" varStatus="loop">
		  			<div class="col-xs-12 col-sm-6 col-md-6 col-lg-6 ">
		  			<li>${varObj.autista.user.fullName}&nbsp;<c:forEach items="${varObj.richiestaMediaAutistaAutoveicolo}" var="varObjAutistaAutoveicolo" varStatus="loopC">
							${varObjAutistaAutoveicolo.autoveicolo.marcaModello}&nbsp;${varObjAutistaAutoveicolo.autoveicolo.annoImmatricolazione} 
							<c:if test="${!loopC.last}"> | </c:if>
						</c:forEach>
			  			<div class="input-group">
							<input type="text" name="compenso_autista_${varObj.id}" class="form-control input-sm " value="${varObj.prezzoTotaleAutista}"/>
							<span class="input-group-btn">
							<button type="button" name="modifica-prezzo-autista" value="${varObj.id}" class="btn btn-primary input-sm alertConfirmGenerale">
								Modifica Prezzo</button>
							</span>
						</div>
		  			</li>
		  			</div>
		  		</c:forEach>
				</ul>
			</div>
			</div>
			
			<div class=" ">
			<div class="form-group row col-lg-11 ">
			<ul class="list-inline">
			<c:forEach items="${ricercaTransfert.richiestaMediaNonSceltaList}" var="varObj" varStatus="loop">
				<li><a data-toggle="modal" href="#classeAutoveicoloTransferModal_${varObj.classeAutoveicolo.id}"><ins>
				<i class="fa fa-users" aria-hidden="true"></i> 
					Autisti | <fmt:message key="${varObj.classeAutoveicolo.nome}"/></ins></a></li>
			</c:forEach>
			</ul>
			</div>
			</div>
			
			<c:forEach items="${ricercaTransfert.richiestaMediaNonSceltaList}" var="varObj" varStatus="loopA">
			<div class="modal fade col-xs-12" id="classeAutoveicoloTransferModal_${varObj.classeAutoveicolo.id}"  tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	    		<div class="modal-dialog">
	        	<div class="modal-content">
	            <div class="modal-header">
	                <h3 class="modal-title"><i class="fa fa-car fa-1x" aria-hidden="true"></i> <fmt:message key="${varObj.classeAutoveicolo.nome}"/></h3>
	            </div>
		            <div class="modal-body">
					<c:forEach items="${varObj.richiestaMediaAutista}" var="varObjAutista" varStatus="loopB">
						${varObjAutista.autista.user.fullName} | ${varObjAutista.autista.user.phoneNumber} | ${varObjAutista.prezzoTotaleAutista}&euro;
						<br>
						<c:forEach items="${varObjAutista.richiestaMediaAutistaAutoveicolo}" var="varObjAutistaAutoveicolo" varStatus="loopC">
							${varObjAutistaAutoveicolo.autoveicolo.marcaModello}&nbsp;${varObjAutistaAutoveicolo.autoveicolo.annoImmatricolazione} 
							<c:if test="${!loopC.last}"> | </c:if>
						</c:forEach>
						<br>
						<a href="<c:url value='/admin/admin-modificaTransfer?idCorsa=${ricercaTransfert.id}&idRichiestaMediaAutista=${varObjAutista.id}'/>">
						<i class="fa fa-user-plus" aria-hidden="true"></i> Aggiungi Autista alla Corsa</a>
						<c:if test="${!loopB.last}"> <hr> </c:if>
					</c:forEach>
					</div>
		    	</div>
				</div>
			</div>
			</c:forEach>
		</c:when>
		<c:when test = "${ricercaTransfert.tipoServizio == 'PART'}">
			<c:set var = "richiestaAutistaParticolare" value="${ricercaTransfert.richiestaAutistaParticolareAcquistato}"/>
			<p class=""><big>Autista: ${richiestaAutistaParticolare.autoveicolo.autista.user.fullName}&nbsp;${richiestaAutistaParticolare.autoveicolo.marcaModello}
	  				&nbsp;${richiestaAutistaParticolare.autoveicolo.annoImmatricolazione}</big>
	  		</p>
		</c:when>
		<c:when test = "${ricercaTransfert.tipoServizio == 'MULTIP'}">
			<c:forEach items="${ricercaTransfert.richiestaAutistaParticolareAcquistato_Multiplo}" var="varObj" varStatus="loop">
	    		<p class=""><big>Autista: ${varObj.autoveicolo.autista.user.fullName}&nbsp;${varObj.autoveicolo.marcaModello}
		  				&nbsp;${varObj.autoveicolo.annoImmatricolazione}</big>
		  		</p>
    		</c:forEach>
		</c:when>
		</c:choose>
		
		<c:if test = "${ricercaTransfert.tipoServizio == 'PART' || ricercaTransfert.tipoServizio == 'MULTIP'}">
		<p>
		<label class="checkbox-inline">
		<input type="checkbox" class="" name="check-ricezione-preventivi" 
		${empty ricercaTransfert.cancellaRicezionePreventiviCliente || ricercaTransfert.cancellaRicezionePreventiviCliente == false ? '' : 'checked'}>
		<fmt:message key="messaggio.stop.ricezione.preventivi"/></label>
		</p>
		</c:if>
		
		<input type="hidden" value="${ricercaTransfert.id}" name="idTransfer">
		<input type="hidden" value="${ricercaTransfert.place_id_Partenza}" id="partenzaPlaceID" name="place_id_Partenza">
		<input type="hidden" value="${ricercaTransfert.place_id_Arrivo}" id="arrivoPlaceID" name="place_id_Arrivo">
		<div class="form-group ">
			<div class="row col-xs-12 col-sm-6 col-md-6 col-lg-11">
				<div class=" ">
					${ricercaTransfert.partenzaRequest}
					<input type="text" value="${ricercaTransfert.formattedAddress_Partenza}" name="formattedAddress_Partenza" id="idPartenza" class="form-control"><br>
				</div>
			</div>
			<div class=" row col-xs-12 col-sm-6 col-md-6 col-lg-11">
				<div class=" ">
					${ricercaTransfert.arrivoRequest}
					<input type="text" value="${ricercaTransfert.formattedAddress_Arrivo}" name="formattedAddress_Arrivo" id="idArrivo" class="form-control"><br>
				</div>
			</div>

			<div class=" row">
				<div class="col-xs-2 col-sm-2 col-md-2 col-lg-2">
					<button type="button" class="btn btn-sm btn-primary invertiAndataRitorno">Inverti Partenza Arrivo</button>
				</div>
			</div>
		</div>
	
		<div class="row ">
			<div class="form-group col-xs-12 col-sm-6 col-md-6 col-lg-11">
				<label for="calendarioFrom" class=" hidden-xs">Giorno Prelevameto</label>
				<div class="input-group"><span class="input-group-addon"><i class="fa fa-calendar"></i></span>
						<input type="text" id="calendarioFrom" class="form-control" placeholder="Giorno Prelevamento">
						<input type="hidden" value="${ricercaTransfert.dataOraPrelevamento}" name="dataOraPrelevamento" id="calendarioFromHidden">
				</div>
			</div>
			<div class="form-group col-xs-12 col-sm-6 col-md-6 col-lg-11">
				<label for="idOraPrelevamento" class=" hidden-xs">Ora</label>
				<div class="input-group"><span class="input-group-addon"><i class="fa fa-clock-o"></i></span>
					<input type="text" value="${ricercaTransfert.oraPrelevamento}" name="oraPrelevamento" id="idOraPrelevamento" 
						class="form-control timepicker" placeholder="Ora Prelevamento">
				</div>
			</div>
			<div class="form-group col-xs-2 col-sm-1">
				<label for="chekboxLargeID" class="">Ritorno
					<input type="checkbox" name="ritorno" class="chekBoxRitorno form-control" id="chekboxLargeID"
						${ricercaTransfert.ritorno ? 'checked':''}>
				</label>
			</div>
		</div>
		<!-- RITORNO -->
		<div id="date-ritorno" style="display:none">
			<div class="row ">
				<div class="form-group col-xs-12 col-sm-6 col-md-6 col-lg-6">
					<label for="calendarioTo" class="hidden-xs">Giorno Ritorno</label>
					<div class="input-group"><span class="input-group-addon"><i class="fa fa-calendar"></i></span>
						<input type="text" id="calendarioTo" class="form-control" placeholder="Giorno Ritorno">
						<input type="hidden" value="${ricercaTransfert.dataOraRitorno}" name="dataOraRitorno" id="calendarioToHidden">
					</div>
				</div>
				<div class="form-group col-xs-9 col-sm-4 col-md-3 col-lg-3">
					<label for="idOraRitorno" class="hidden-xs">Ora</label>
					<div class="input-group"><span class="input-group-addon"><i class="fa fa-clock-o"></i></span>
						<input type="text" value="${ricercaTransfert.oraRitorno}" name="oraRitorno" id="idOraRitorno" 
						class="form-control timepicker" placeholder="Ora Ritorno">
					</div>
				</div>
			</div>
		</div>
		<div class="row ">
			<div class="form-group col-xs-12 col-sm-4 col-md-4 col-lg-4">
				<label for="idNumPasseggeri" class=" form-control-label">Num. Passeggeri</label>
				<div class="">
					<div class=" input-group"><span class="input-group-addon"><i class="fa fa-user"></i></span>
		   			<select name="numeroPasseggeri" class="form-control" id="idNumPasseggeri">
						<!-- <option value="0">Num. Passeggeri</option> -->
						<!-- RICORDATI DI TOGLIERE IL SELECTED SUL 2 ${i == 2 ? 'selected':''} -->
				        <c:forEach var="i" begin="1" end="8">
		 					<option ${ricercaTransfert.numeroPasseggeri == i ? 'selected':''} value="${i}"> ${i} </option>  
						</c:forEach>
		     		</select>
		     		</div>
				</div>
			</div>
			<div class="form-group col-xs-12 col-sm-8 col-md-6 col-lg-6">
				<textarea class="form-control" rows="3" name="noteAutista" id="noteAutistaId" maxlength="500"
					placeholder="Informazioni importanti per l'Autista">${ricercaTransfert.notePerAutista}</textarea>
			</div>
		</div>
		<div class=" ">
			<div class="form-group ">
				<button type="button" class="btn btn-md btn-danger " id="modificaTransferId" name="" ><i class="fa fa-pencil-square-o"></i> Modifica Corsa</button>
				<a class="btn btn-md btn-info openDatiPasseggeroModal" id="${ricercaTransfert.id}"> 
					<span class="fa fa-user"></span> <span class="fa fa-pencil-square-o"></span> <fmt:message key="messaggio.modifica.dati.passeggero.corsa"></fmt:message></a>
			</div>
		</div>
		
		<div class=" ">
		<div class="form-group ">
			<textarea class="form-control" rows="3" name="note" id="noteId" maxlength="500" placeholder="Note Corsa" >${ricercaTransfert.note}</textarea>
		</div>
		</div>
		
	</form:form>
	</div>
	<!-- Modal Dati Passeggero -->
	<%@ include file="/common/modifica-dati-passeggero-modal.jsp"%>

	</c:if>
	
</div>
</div>
</div>

<!-- calendario e time-->
<script src="<c:url value="/scripts/vendor/pickadate.js-master/lib/compressed/total.js"/>"></script>
<script src="<c:url value="/scripts/vendor/pickadate.js-master/lib/compressed/translations/"/><fmt:message key="language.code"/>.js"></script>
<script type="text/javascript">
jQuery(document).ready(function(){
	$(".invertiAndataRitorno").click(function(){
		var partenza = $('#idPartenza').val();
		var arrivo = $('#idArrivo').val();
		var partenzaPlaceId = $('#partenzaPlaceID').val();
		var arrivoPlaceId = $('#arrivoPlaceID').val();
		$('#idPartenza').val( arrivo );
		$('#idArrivo').val( partenza );
		$('#partenzaPlaceID').val( arrivoPlaceId );
		$('#arrivoPlaceID').val( partenzaPlaceId );
	});
	//----- CALENDARIO -----------------
	var from_$input = $('#calendarioFrom').pickadate({
		min: true,
		onStart: function (){
			if( $('#calendarioFromHidden').val() != '' ){
				var date = new Date();
				date.setTime( $('#calendarioFromHidden').val() );
				//alert( date );
				this.set( 'select', date );
			}
           },
		//format: 'd mmmm, yyyy',
		formatSubmit: 'yyyy/mm/dd',
		onClose: function() {
			  $(document.activeElement).blur();
			}
	}), from_picker = from_$input.pickadate('picker')

	var to_$input = $('#calendarioTo').pickadate({
		min: true,
		onStart: function (){
			if( $('#calendarioToHidden').val() != '' ){
				var date = new Date();
				date.setTime( $('#calendarioToHidden').val() );
				//alert( date );
				this.set( 'select', date );
			}
           },
		//format: 'd mmmm, yyyy',
		formatSubmit: 'yyyy/mm/dd',
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
	// When something is selected, update the "from" and "to" limits.
	from_picker.on('set', function(event) {
	  if ( event.select ) {
		  $('#calendarioFromHidden').val(  event.select );
	    to_picker.set('min', from_picker.get('select'))
	  } else if ( 'clear' in event ) {
		  $('#calendarioFromHidden').val( '' );
	    to_picker.set('min', false)
	  }
	})
	to_picker.on('set', function(event) {
	  if ( event.select ) {
		  $('#calendarioToHidden').val( event.select );
	    from_picker.set('max', to_picker.get('select'))
	  } else if ( 'clear' in event ) {
		  $('#calendarioToHidden').val( '' );
	    from_picker.set('max', false)
	  }
	});
	if(document.getElementById('chekboxLargeID').checked) {
		$("#date-ritorno").show();
    } else {
        $("#date-ritorno").hide();
    }
	$('#chekboxLargeID').click(function() {
	    if( $(this).is(':checked')) {
	        $("#date-ritorno").show();
	    } else {
	        $("#date-ritorno").hide();
	        $('#calendarioToHidden').val( '' );
	        to_picker.set('clear');
	        from_picker.set('max', false);
	        
	        $('#idOraRitorno').val( '' );
	    }
	});
	//-----OROLOGIO--------------
	$('.timepicker').pickatime({
	  format: 'HH:i',
	  //formatSubmit: 'h:i',
	  interval: 15,
	  /* onStart: function (){
			this.set( 'select', '12:00' );
          } */
         onClose: function() {
		  $(document.activeElement).blur();
		}
	});
}); // fine ready
</script>
<%@ include file="/scripts/ConfirmSubmit.jsp"%>
</body>