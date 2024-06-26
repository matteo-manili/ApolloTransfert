<!-- MODAL VALIDITA PREVENTIVO -->
<div id="modalValiditaPreventivo" class="modal fade" tabindex="-1" role="dialog">
	<div class="modal-dialog">
	<div class="modal-content">
		<div class="modal-header">Scegli Periodo di Tempo Validit&agrave; Preventivo
			<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
		</div> 
		<div class="modal-body">
			<div class="row">
			<div id="listValiditaPreventivo"></div>
			</div>
		</div>
	</div>
	</div>
</div>

<script type="text/javascript">
	$(document).ready(function () {
		
    	var jsonObj = ${mainCorse};
        var html_CORSE_DA_ESEGUIRE = "";
        var html_CORSE_ESEGUITA = "";
        var html_CORSE_DISPONIBILI = "";
        var html_PARTICOLARE_PRENOTAZIONE = "";
        
        var corseAgendaAutistaTpl = $('#corseAgendaAutistaTpl').html();
        var corseAgendaCompletateAutistaTpl = $('#corseAgendaCompletateAutistaTpl').html();
        var corsaParticolarePrenotazioneTpl = $('#corsaParticolarePrenotazioneTpl').html();
        var corseParticolariAutistaTpl = $('#corseParticolariAutistaTpl').html();
        var corseParticolariCompletateAutistaTpl = $('#corseParticolariCompletateAutistaTpl').html();
		var corseMedieAutistaTpl = $('#corseMedieAutistaTpl').html();
		var corseMedieCompletateAutistaTpl = $('#corseMedieCompletateAutistaTpl').html();
		var corseMedieDisponibiliAutistaTpl = $('#corseMedieDisponibiliAutistaTpl').html();

		Mustache.parse(corseAgendaAutistaTpl);   // optional, speeds up future uses
        Mustache.parse(corseAgendaCompletateAutistaTpl);   // optional, speeds up future uses
		Mustache.parse(corsaParticolarePrenotazioneTpl);   // optional, speeds up future uses
        Mustache.parse(corseParticolariAutistaTpl);   // optional, speeds up future uses
        Mustache.parse(corseParticolariCompletateAutistaTpl);   // optional, speeds up future uses
        Mustache.parse(corseMedieAutistaTpl);   // optional, speeds up future uses
        Mustache.parse(corseMedieCompletateAutistaTpl);   // optional, speeds up future uses
        Mustache.parse(corseMedieDisponibiliAutistaTpl);   // optional, speeds up future uses

        $.each(jsonObj, function(index, val){
        	for(var i=0; i < val.length; i++){
	        	var item = val[i];
	        	//alert(item.template);
	        	if(item.template == "AGENDA_AUTISTA_DA_ESEGUIRE"){
	        		html_CORSE_DA_ESEGUIRE+= Mustache.render(corseAgendaAutistaTpl, item);
	        	}
	        	if(item.template == "AGENDA_AUTISTA_ESEGUITA"){
	        		html_CORSE_ESEGUITA+= Mustache.render(corseAgendaCompletateAutistaTpl, item);
	        	}
	        	if(item.template == "PARTICOLARE_PRENOTAZIONE"){
	        		html_PARTICOLARE_PRENOTAZIONE+= Mustache.render(corsaParticolarePrenotazioneTpl, item);
	        	}
	        	if(item.template == "PARTICOLARE_DA_ESEGUIRE"){
	        		html_CORSE_DA_ESEGUIRE+= Mustache.render(corseParticolariAutistaTpl, item);
	        	}
	        	if(item.template == "PARTICOLARE_ESEGUITA"){
	        		html_CORSE_ESEGUITA+= Mustache.render(corseParticolariCompletateAutistaTpl, item);
	        	}
	        	if(item.template == "MEDIA_DA_ESEGUIRE"){
	        		html_CORSE_DA_ESEGUIRE+= Mustache.render(corseMedieAutistaTpl, item);
	        	}
	        	if(item.template == "MEDIA_ESEGUITA"){
	        		html_CORSE_ESEGUITA+= Mustache.render(corseMedieCompletateAutistaTpl, item);
	        	}
	        	if(item.template == "MEDIA_DISPONIBILE"){
	        		html_CORSE_DISPONIBILI+= Mustache.render(corseMedieDisponibiliAutistaTpl, item);
	        	}
        	}
        	$("#corseA").html(html_CORSE_DA_ESEGUIRE);
        	$("#corseB").html(html_CORSE_ESEGUITA);
        	$("#corseC").html(html_CORSE_DISPONIBILI);
        	$("#corseD").html(html_PARTICOLARE_PRENOTAZIONE);
       	});
     	// ------- FINE CORSE AUTISTA -------
		
     	
		//--------CONFIRM INFO CLIENTE INIZIO
		$(document).on("click", ".alertConfirmInfoCliente", function(e) {
			var idcorsa = $(this).attr('id');
			var tokenAutista = $(this).attr('value');
			//alert( idcorsa ); alert( tokenAutista );
			//-----Ajax prenotaCorsa inizio
			$.ajax({
				type: 'POST',
				url: '${pageContext.request.contextPath}/dammiInfoCliente',
				dataType: "json",
				data: {
					idRicTransfert : idcorsa,
					tokenAutista : tokenAutista
				},
				beforeSend: function(){ },
				success: function(result) {
					//alert(result['esito']);
					if( !result['esito'] ){
						bootbox.alert({
							backdrop: true,
							message: result['messaggio']  
						});
					}else{
						bootbox.alert({
							backdrop: true,
							title: result['titolo'],
							message: result['messaggio']  
						});
					}
				}, //fine success
				error: function (req, status, error) {
					alert('errore ajax dammiInfoCliente');
					//window.location.replace('${pageContext.request.contextPath}/home-user'); 
					location.reload(true);
				}
			});
			//-----Ajax prenotaCorsa fine
		}); // fine alertConfirm
		//--------CONFIRM INFO CLIENTE FINE
		
		//--------CONFIRM PRENOTA CORSA PARTICOLARE INIZIO
		$(document).on("click", ".inviaPreventivoClienteCorsaParticolare", function(e) {
			var idcorsaParticolare = $(this).attr('id');
			//alert( idRicTransfert );
			bootbox.confirm({
			    //title: "",
			    message: "Vuoi Inviare il Preventivo?",
			    buttons: {
			        cancel: {
			            label: '<i class="fa fa-times"></i> Annulla'
			        },
			        confirm: {
			            label: '<i class="fa fa-check"></i> Continua'
			        }
			    },
			    callback: function(resultBootBox) {
			    	if(resultBootBox){
			    		//-----Ajax inviaPreventivoClienteCorsaParticolare inizio
			    		$.ajax({
				    	type: 'POST',
						url: '${pageContext.request.contextPath}/inviaPreventivoClienteCorsaParticolare',
						dataType: "json",
				        data: {
				        	idcorsaParticolare : idcorsaParticolare
				        },
				        beforeSend: function(){ },
				        success: function(result) {
				        	if( result['esito'] ){
								bootbox.alert({
									message: result['messaggio'],
									callback: function () {
										location.reload(true);
								    }
								});
				        	}else{
				        		bootbox.alert({
									message: result['messaggio'],
									callback: function () {
										location.reload(true);
								    }
								});
				        	}
				         }, //fine success
						error: function (req, status, error) {
							alert('errore ajax inviaPreventivoClienteCorsaParticolare');
							//window.location.replace('${pageContext.request.contextPath}/home-user');
							location.reload(true);
						}
				    });
			    	//-----Ajax inviaPreventivoClienteCorsaParticolare fine
			    	}
			    }
			});
		}); // fine alertConfirm
		//--------CONFIRM PRENOTA CORSA PARTICOLARE FINE
		
		$(document).on("click", ".setPreventivoPrezzo", function(e) {
			var idcorsaParticolare = $(this).attr('id');
			var prezzo = document.getElementById( 'prezzoPreventivoId-'+idcorsaParticolare ).value;
			//alert(idcorsaParticolare);alert(prezzo);
			$.ajax({
		    	type: 'POST',
				url: '${pageContext.request.contextPath}/setPreventivoPrezzo',
				dataType: "json",
		        data: {
		        	idcorsaParticolare : idcorsaParticolare,
		        	prezzo : prezzo
		        },
		        beforeSend: function(){ },
				success: function(result) {
					if( result['esito'] ){
						bootbox.alert({
							message: result['messaggio'],
							callback: function () {
								location.reload(true);
							}
						});
					}else{
						bootbox.alert({
							message: result['messaggio'],
							callback: function () {
								location.reload(true);
							}
						});
					}
				}, //fine success
				error: function (req, status, error) {
					alert('errore ajax setPreventivoPrezzo');
					//window.location.replace('${pageContext.request.contextPath}/home-user');
					location.reload(true);
				}
		    });
		});
		
		$(document).on("click", ".apriPopUpValiditaPreventivo", function(e) {
			var idcorsaParticolare = $(this).attr('id');
			var bbb = JSON.parse( '${ValiditaPreventivo}' );
			/* alert(bbb.length);
			for(var i = 0; i < bbb.length; i++) {
			    var obj = bbb[i];
			    alert(obj.idPeriodo);
			} */
			var htmlRes = "";
			for(var i = 0; i < bbb.length; i++) {
				var obj = bbb[i];
				htmlRes = htmlRes + 
				"<div class='col-sm-12 '>"+
				"<div class=' col-sm-6'>"+
				"<p class='h4'>"+obj.testoPeriodo+"</p>"+
				"</div>"+
				"<div class=' col-sm-6'>"+
				"<button type='button' id="+idcorsaParticolare+"-"+obj.idPeriodo+" name='seleziona-periodo' value='' class='btn btn-primary btn-sm setPreventivoPeriodoValidita'>"+
					"Seleziona Periodo Validit&agrave; &nbsp;&nbsp;<span class='fa fa-calendar'></span></button>"+
				"</div>"+
				"</div>";
				$("#listValiditaPreventivo").html(htmlRes);
			}
			$("#modalValiditaPreventivo").modal('show');
		});
		
		
		$(document).on("click", ".setPreventivoPeriodoValidita", function(e) {
			var idcorsa = $(this).attr('id');
			var fields = idcorsa.split('-'); var idcorsaParticolare = fields[0]; var idPeriodo = fields[1];
			//alert(idcorsaParticolare);alert(idPeriodo);
			$.ajax({
		    	type: 'POST',
				url: '${pageContext.request.contextPath}/setPreventivoPeriodoValidita',
				dataType: "json",
		        data: {
		        	idcorsaParticolare : idcorsaParticolare,
		        	idPeriodo : idPeriodo
		        },
		        beforeSend: function(){ },
		        success: function(result) {
					//alert(result);
					location.reload(true);
		         }, //fine success
				error: function (req, status, error) {
					alert('errore ajax setPreventivoPeriodoValidita');
					//window.location.replace('${pageContext.request.contextPath}/home-user');
					location.reload(true);
				}
		    });
		});
		
		
		//--------checkFatturaDisponibile INIZIO
		$(document).on("click", ".checkFatturaDisponibileAutista", function(e) {
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
					if( !result['nomeCliente'] ){
						bootbox.alert({
								backdrop: true,
								message: "Fattura non ancora disponibile." });
					}else{
						window.location.replace('${pageContext.request.contextPath}/pdfDownloadFattura?courseId='+idcorsa);
					}
					
				}, //fine success
				error: function (req, status, error) {
					alert('errore ajax checkFatturaDisponibile');
					window.location.replace('${pageContext.request.contextPath}/home-user'); }
			});
		}); // checkFatturaDisponibile FINE
		
		
		//--------CONFIRM CANCELLA RISERVA CORSA INIZIO
		$(document).on("click", ".alertConfirmTogliRiserva", function(e) {
			var idcorsaMedia = $(this).attr('id');
			//alert( idRicTransfert );
			bootbox.confirm({
				    //title: "",
				    message: "Vuoi Cancellare la Riserva da questa Corsa?",
				    buttons: {
				        cancel: {
				            label: '<i class="fa fa-times"></i> Annulla'
				        },
				        confirm: {
				            label: '<i class="fa fa-check"></i> Continua'
				        }
				    },
				    callback: function (resultBootBox) {
				    	if(resultBootBox){
				    		//-----Ajax prenotaCorsa inizio
				    		$.ajax({
					    	type: 'POST',
							url: '${pageContext.request.contextPath}/togliPrenotaCorsaMediaAutista',
							dataType: "json",
					        data: {
					        	idcorsaMedia : idcorsaMedia
					        },
					        beforeSend: function(){ },
					        success: function(result) {
					        	if( result['esito']  ){
									bootbox.alert({
										message: "Hai Cancellato la Riseva da questa Corsa",
										callback: function () {
											//window.location.replace('${pageContext.request.contextPath}/home-user');
											location.reload(true);
									    }
									});
					        	}
					         }, //fine success
							error: function (req, status, error) {
								alert('errore ajax prenotaCorsaMediaAutista');
								//window.location.replace('${pageContext.request.contextPath}/home-user');
								location.reload(true);
							}
					    });
				    		//-----Ajax prenotaCorsa fine
				    	}
				    }
				});

		}); // fine alertConfirm
		//--------CONFIRM CANCELLA RISERVA CORSA FINE


		//--------CONFIRM RISERVA CORSA INIZIO
		$(document).on("click", ".alertConfirmRiserva", function(e) {
			var idcorsaMedia = $(this).attr('id');
			//alert( idRicTransfert );
			bootbox.confirm({
				    //title: "",
				    message: "Vuoi Riservare la Corsa?",
				    buttons: {
				        cancel: {
				            label: '<i class="fa fa-times"></i> Annulla'
				        },
				        confirm: {
				            label: '<i class="fa fa-check"></i> Continua'
				        }
				    },
				    callback: function (resultBootBox) {
				    	if(resultBootBox){
				    		//-----Ajax prenotaCorsa inizio
				    		$.ajax({
					    	type: 'POST',
							url: '${pageContext.request.contextPath}/prenotaCorsaMediaAutista',
							dataType: "json",
					        data: {
					        	idcorsaMedia : idcorsaMedia
					        },
					        beforeSend: function(){ },
					        success: function(result) {

					        	if( result['esito'] == "corsaConfermata" ){
									bootbox.alert({
										message: "Corsa Confermata<br>"+
											"Eseguita la corsa ti bonificheremo la somma di "+ result['prezzoAutista'] +" &euro;",
										callback: function () {
											//window.location.replace('${pageContext.request.contextPath}/home-user');
											location.reload(true);
									    }
									});
					        	}else{
					        		bootbox.alert({
										message: "Hai prenotato questa Corsa ma un altro autista l'ha gi&agrave; prenotata prima di te, <br>"+
											"qualora egli la annullasse sarai avvisato con un SMS con almeno "+ result['numMaxOreDisdettaCorsa'] +" ore di tempo prima del prelevamento cliente.",
										callback: function () {
											//window.location.replace('${pageContext.request.contextPath}/home-user');
											location.reload(true);
									    }
									});

					        	}
					         }, //fine success
							error: function (req, status, error) {
								alert('errore ajax prenotaCorsaMediaAutista');
								//window.location.replace('${pageContext.request.contextPath}/home-user');
								location.reload(true);
							}
					    });
				    		//-----Ajax prenotaCorsa fine
				    	}
				    }
				});
		}); // fine alertConfirm
		//--------CONFIRM RISERVA CORSA FINE


		//--------CONFIRM PRENOTA CORSA INIZIO
		$(document).on("click", ".alertConfirmPrenota", function(e) {
			var idcorsaMedia = $(this).attr('id');
			//alert( idRicTransfert );
			bootbox.confirm({
				    //title: "",
				    message: "Vuoi Prenotare la Corsa?",
				    buttons: {
				        cancel: {
				            label: '<i class="fa fa-times"></i> Annulla'
				        },
				        confirm: {
				            label: '<i class="fa fa-check"></i> Continua'
				        }
				    },
				    callback: function (resultBootBox) {
				    	if(resultBootBox){
				    		//-----Ajax prenotaCorsa inizio
				    		$.ajax({
					    	type: 'POST',
							url: '${pageContext.request.contextPath}/prenotaCorsaMediaAutista',
							dataType: "json",
					        data: {
					        	idcorsaMedia : idcorsaMedia
					        },
					        beforeSend: function(){ },
					        success: function(result) {
					        	if( result['esito'] == "corsaConfermata" ){
									bootbox.alert({
										message: "Corsa Confermata!<br>"+
											"Eseguita la corsa ti bonificheremo la somma di "+ result['prezzoAutista'] +" &euro;",
										callback: function () {
											//window.location.replace('${pageContext.request.contextPath}/home-user');
											location.reload(true);
									    }
									});
					        	}else{
					        		bootbox.alert({
										message: "Hai riservato questa Corsa ma un altro autista l'ha gi&agrave; prenotata prima di te, <br>"+
											"qualora egli la annullasse sarai avvisato con un SMS con almeno "+ result['numMaxOreDisdettaCorsa'] +" ore di tempo prima del prelevamento cliente.",
										callback: function () {
											//window.location.replace('${pageContext.request.contextPath}/home-user');
											location.reload(true);
									    }
									});
					        	}
					         }, //fine success
							error: function (req, status, error) {
								alert('errore ajax prenotaCorsaMediaAutista');
								//window.location.replace('${pageContext.request.contextPath}/home-user');
								location.reload(true);
							}
					    });
				    		//-----Ajax prenotaCorsa fine
				    	}
				    }
				});
		}); // fine alertConfirm
		//--------CONFIRM PRENOTA CORSA FINE


		//-----CONFIRM-----------
		$(document).on("click", ".alertConfirm", function(e) {
			var idcorsaMedia = $(this).attr('id');
			//alert( idRicTransfert );
			$.ajax({
		    	type: 'POST',
				url: '${pageContext.request.contextPath}/controlloDisdettaCorsaMediaAutista',
				dataType: "json",
		        data: {
		        	idcorsaMedia : idcorsaMedia
		        },
		        beforeSend: function(){ },
		        success: function(result) {
	                //alert(result['esito']);
	                //----bootbox inizio
	                if( result['esito'] ){
	                	 bootbox.confirm({
	 					    title: "Vuoi Disdire la Corsa?",
	 					    message: "Una volta disdetta la corsa potrai prenotarla nuavamente in <strong>Corse Disponibili</strong>.",
	 					    buttons: {
	 					        cancel: {
	 					            label: '<i class="fa fa-times"></i> Annulla'
	 					        },
	 					        confirm: {
	 					            label: '<i class="fa fa-check"></i> Continua'
	 					        }
	 					    },
	 					    callback: function (resultBootBox) {
	 					    	if(resultBootBox){
	 					    		//-----Ajax disdiciCorsa inizio
	 					    		$.ajax({
								    	type: 'POST',
										url: '${pageContext.request.contextPath}/disdiciCorsaMediaAutista',
										dataType: "json",
								        data: {
								        	idcorsaMedia : idcorsaMedia
								        },
								        beforeSend: function(){ },
								        success: function(result) {
								        	//window.location.replace('${pageContext.request.contextPath}/home-user');
								        	location.reload(true);
								         }, //fine success
										error: function (req, status, error) {
											alert('errore ajax disdiciCorsaMediaAutista');
											//window.location.replace('${pageContext.request.contextPath}/home-user');
											location.reload(true);
										}
								    });
	 					    		//-----Ajax disdiciCorsa fine
	 					    	}
	 					    }
	 					});
	                }else{
	                	bootbox.alert("Non puoi pi&ugrave; disdire questa corsa! <br> In caso di impossibilit&agrave; di eseguire la corsa "+
	                			"sei pregato di contattarci il prima possibile, pena la cancellazione dell'account.");
	                } //----bootbox fine
		         }, //fine success
				error: function (req, status, error) {
					alert('errore ajax controlloDisdettaCorsaMediaAutista');
					//window.location.replace('${pageContext.request.contextPath}/home-user');
					location.reload(true);
				}
		    }); // fine ajax
		}); // fine alertConfirm

		//----- FINE CONFIRM------------

        function blink() {
		    $('.blinkeffect').fadeOut(500).fadeIn(500);
		}
		setInterval(blink, 1000);

	}); // fine ready
	</script>
