<script type="text/javascript">
$(document).ready(function () {
	
	$(document).on("click", ".gestioneRimborsoCliente", function(e) {
		var idCorsa = $(this).attr('id');
		$.ajax({
	    	type: 'POST',
			url: '${pageContext.request.contextPath}/controlloDisdettaCorsaCliente',
			dataType: "json",
	        data: {
	        	idcorsa : idCorsa
	        },
	        beforeSend: function(){ },
	        success: function(result) {
                //alert(result['esito']);
                //----bootbox inizio
                if( result['esito'] ){
                	//alert(111);
                	bootbox.confirm({
						//title: "Vuoi Cancellare la Corsa ed Eseguire il Rimborso?",
 					   	message: '<fmt:message key="messaggio.cliente.cancella.corsa.domanda"></fmt:message>',
 					    buttons: {
 					        cancel: {
 					            label: '<i class="fa fa-times"></i> <fmt:message key="button.cancel"></fmt:message>'
 					        },
 					        confirm: {
 					            label: '<i class="fa fa-check"></i> <fmt:message key="button.continua"></fmt:message>'
 					        }
 					    },
 					    callback: function (resultBootBox) {
 					    	if(resultBootBox){
 					    		//$("#attesaLoader").modal('show');
 					    		$(".attesaLoaderPopUp").fadeIn(300);
 					    		window.location.replace('${pageContext.request.contextPath}/gestioneRimborsoCliente?idCorsa='+idCorsa);
 					    	}
 					    }
 					});
                }else{
                	bootbox.alert('<fmt:message key="messaggio.cliente.disdetta.rimborso.corsa.non.possibile"><fmt:param value="'+result['numMaxOreDisdettaCorsa']+'"/></fmt:message>');
                }
                //----bootbox fine

	         }, //fine success
			error: function (req, status, error) {
				alert('errore ajax controlloDisdettaCorsaCliente');
				//window.location.replace('${pageContext.request.contextPath}/home-user');
				location.reload(true);
			}
	    }); // fine ajax
	});
	
	//--------CONFIRM INFO AUTISTA INIZIO
	$(document).on("click", ".alertConfirmInfoAutista", function(e) {
		var idcorsa = $(this).attr('id');
		//alert(idcorsa);
		$.ajax({
			type: 'POST',
			url: '${pageContext.request.contextPath}/dammiInfoAutista', 
			dataType: "json",
			data: {
				idRicTransfert : idcorsa
			},
			beforeSend: function(){ },
			success: function(result) {
				//alert(result['esitoCorsa']);
				if( result['esitoCorsaMultipla'] ){
					var StringHtml = "<ul class='list-group'>";
					result['AutistiList'].forEach(function(entry) {
						StringHtml = StringHtml + "<li class='list-group-item'>"+
						"<a class='btn btn-sm btn-primary' href='tel:+"+entry[1]+"'><span class='glyphicon glyphicon-phone'></span> "+entry[0]+' '+entry[1]+"</a>"+
						"</li>"
					});
					StringHtml = StringHtml + "</ul>";
					bootbox.alert({
						backdrop: true,
						title: "Autisti",
						message: StringHtml
					});
				}
				if( result['esitoCorsaParticolare'] ){
					bootbox.alert({
						backdrop: true,
						title: result['fullName'],
						message: "<div class='row top-buffer text-center'>"+
							"<a class='btn btn-sm btn-primary' href='tel:+"+result['telefono']+"'><span class='glyphicon glyphicon-phone'></span> "+result['telefono']+"</a>"+
						"</div>"
					});
				}
				if( result['esitoCorsaMedio'] ){
					bootbox.alert({
						backdrop: true,
						title: result['fullName'],
						message: "<div class='row top-buffer text-center'>"+
							"<a class='btn btn-sm btn-primary' href='tel:+"+result['telefono']+"'><span class='glyphicon glyphicon-phone'></span> "+result['telefono']+"</a>"+
						"</div>"+
						"<div class='row text-center' style='margin-left:1px;margin-right:1px;'>"+
							"<strong>Autoveicolo/i:</strong> "+ result['autoveicoloRichiesto']+
						"</div>"
					});
				}
				if( result['esitoCorsaAgendaAutista'] ){
					var TextTelefoni = "";
					if(result.hasOwnProperty('telefonoRitorno')){
						TextTelefoni = "<a class='btn btn-sm btn-primary' href='tel:+"+result['telefonoAndata']+"'>"+"<span class='glyphicon glyphicon-phone'></span>"
							+"ANDATA "+result['telefonoAndata']+"</a>"
						+ "<a class='btn btn-sm btn-primary' href='tel:+"+result['telefonoRitorno']+"'><span class='glyphicon glyphicon-phone'></span>"
							+"RITORNO "+result['telefonoRitorno']+"</a>";
					}else{
						TextTelefoni = "<a class='btn btn-sm btn-primary' href='tel:+"+result['telefonoAndata']+"'><span class='glyphicon glyphicon-phone'>"
							+"</span>"+result['telefonoAndata']+"</a>";
					}
					bootbox.alert({
						backdrop: true,
						title: result['fullName'],
						message: "<div class='row top-buffer text-center'>"+TextTelefoni+"</div>"
					});
				}
				if( !result['esitoCorsa'] ){
					bootbox.alert({
						backdrop: true,
						message: "<big>E' possibile visualizzare le Info Autista a partire da "+result['NumOreInfoAutistaCliente']+" ore prima del prelevamento!</big>"
					});
				}
			}, //fine success
			error: function (req, status, error) {
				alert('errore ajax dammiInfoAutista');
				window.location.replace('${pageContext.request.contextPath}/home-user'); }
		});
	}); // fine alertConfirm
	//--------CONFIRM INFO AUTISTA FINE
	
	$(document).on("click", ".salvaDatiPasseggeroModal", function(e) {
		var idCorsa = $(this).attr('id');
		var nomePasseggero = $('#nomePasseggeroModal_'+idCorsa).val();
		var telefonoPasseggero = $('#telefonoPasseggeroModal_'+idCorsa).val();
		//alert(idCorsa);
		$.ajax({
	    	type: 'POST',
            url: '${pageContext.request.contextPath}/modificaDatiPasseggeroCorsa',
            dataType: "json",
			data: {
			idRicTransfert : idCorsa,
			modificaDati : true,
			nomePasseggero : nomePasseggero,
			telefonoPasseggero : telefonoPasseggero
			},
			beforeSend: function(){ },
			success: function(result) {
				if(result.esito){
					if ($('#nomeTelefonoPasseggero-'+idCorsa).length){ //controllo se esiste il Div cercandolo per ID
						$('#nomeTelefonoPasseggero-'+idCorsa).text(result.nomePasseggero+' '+result.telefonoPasseggero);
					}
				}
			}, //fine success
	        error: function (req, status, error) {
			alert('errore ajax datiPasseggeroModalSalva');}
		});
		$("#modificaDatiPasseggeroModal_"+idCorsa).modal('toggle');
	});
	
	$(document).on("click", ".openDatiPasseggeroModal", function(e) {
		var idCorsa = $(this).attr('id');
		$("#modificaDatiPasseggeroModal_"+idCorsa).modal('show');
	});
	
	$(document).on("click", ".checkFatturaSupplementoDisponibile", function(e) {
		var idSupplemento = $(this).attr('id');
		//alert(idSupplemento);
		$.ajax({
			type: 'POST',
			url: '${pageContext.request.contextPath}/checkFatturaSupplementoDisponibile',
			dataType: "json",
			data: {
				idSupplemento : idSupplemento
			},
			beforeSend: function(){ },
			success: function(result) { 
				//alert(result['fatturaDisponibile']);
				if( !result['fatturaDisponibile']  ){
					bootbox.alert({
							backdrop: true,
							message: "Fattura Supplemento Non disponibile."
					});
				}else{
					window.location.replace('${pageContext.request.contextPath}/pdfDownloadFatturaSupplemento?idSupplemento='+idSupplemento);
				}
				
			}, //fine success
			error: function (req, status, error) {
				//alert('errore ajax checkFatturaSupplementoDisponibile');
				window.location.replace('${pageContext.request.contextPath}/home-user'); }
		});
	});
	
	$(document).on("click", ".checkFatturaRitardoDisponibile", function(e) {
		var idcorsa = $(this).attr('id');
		//alert(idcorsa);
		$.ajax({
			type: 'POST',
			url: '${pageContext.request.contextPath}/checkFatturaRitardoDisponibile',
			dataType: "json",
			data: {
				idcorsa : idcorsa
			},
			beforeSend: function(){ },
			success: function(result) { 
				//alert(result['fatturaDisponibile']);
				if( !result['fatturaDisponibile']  ){
					bootbox.alert({
							backdrop: true,
							message: "Fattura Ritardo Non disponibile."
					});
				}else{
					window.location.replace('${pageContext.request.contextPath}/pdfDownloadFatturaRitardo?courseId='+idcorsa);
				}
				
			}, //fine success
			error: function (req, status, error) {
				//alert('errore ajax checkFatturaRitardoDisponibile');
				window.location.replace('${pageContext.request.contextPath}/home-user'); }
		});
	});

	//--------checkFatturaDisponibile INIZIO
	$(document).on("click", ".checkFatturaDisponibile", function(e) {
		var idcorsa = $(this).attr('id');
		//alert(idcorsa);
		$.ajax({
			type: 'POST',
			url: '${pageContext.request.contextPath}/checkFatturaDisponibile',
			dataType: "json",
			data: {
				idRicTransfert : idcorsa
			},
			beforeSend: function(){ },
			success: function(result) { 
				if( !result['nomeCliente']  ){
					bootbox.alert({
							backdrop: true,
							message: "Inserire nome e Cognome "+"<a href='${pageContext.request.contextPath}/userform'>Modifica Profilo</a>"
					});
				}else{
					window.location.replace('${pageContext.request.contextPath}/pdfDownloadFattura?courseId='+idcorsa);
				}
			}, //fine success
			error: function (req, status, error) {
				//alert('errore ajax checkFatturaDisponibile');
				window.location.replace('${pageContext.request.contextPath}/home-user'); }
		});
	}); // checkFatturaDisponibile FINE
	
	
	//--------pdfDownloadFatturaCommercialista INIZIO
	$(document).on("click", ".pdfDownloadFatturaCommercialista", function(e) {
		var fatturaId = $(this).attr('id');
		//alert(fatturaId);
		window.location.replace('${pageContext.request.contextPath}/pdfDownloadFatturaCommercialista?fatturaId='+fatturaId);
	}); // pdfDownloadFatturaCommercialista FINE
	
	
}); // fine ready
</script>