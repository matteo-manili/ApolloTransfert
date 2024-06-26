<script type="text/javascript">
$(document).ready(function () {
	$(document).on("click", ".modificaTransfer", function(e) {
		var idCorsa = $(this).attr('id');
		//window.location.replace('${pageContext.request.contextPath}/admin/admin-modificaTransfer?idCorsa='+idCorsa);
		window.open('${pageContext.request.contextPath}/admin/admin-modificaTransfer?idCorsa='+idCorsa,'_blank');
	});
	
	//--------CONFIRM INSERISCI TELEFONO PASSEGGERO CORSA
	$(document).on("click", ".inviaEmailCorsaAcquistataCliente", function(e) {
	var idCorsa = $(this).attr('id');
	//alert( idCorsa );
	bootbox.prompt({
		title: "Invia email Corsa Acquistata - Inserisci Email Cliente",
        value: $(this).val(),
        inputType: 'text',
	    buttons: {
	        cancel: {
	            label: '<i class="fa fa-times"></i> Annulla'
	        },
	        confirm: {
	            label: '<i class="fa fa-check"></i> Invia Email' 
	        }
	    },
	    callback: function (resultBootBox) {
	    	if(resultBootBox){
		   		$.ajax({
			    	type: 'POST',
		            url: '${pageContext.request.contextPath}/inviaEmailCorsaAcquistataCliente',
		            dataType: "json",
					data: {
					idRicTransfert : idCorsa,
					emailCliente : resultBootBox
					},
					beforeSend: function(){ },
					success: function(result) {
					}, //fine success
			        error: function (req, status, error) {
					alert('errore ajax inviaEmailCorsaAcquistataCliente');}
				});
			}
		}
	});
	});
	//--------CONFIRM INSERISCI TELEFONO PASSEGGERO CORSA
	
	$(document).on("click", ".gestioneRimborso", function(e) {
		var idCorsa = $(this).attr('id');
		//window.location.replace('${pageContext.request.contextPath}/admin/admin-gestioneRimborsi?idCorsa='+idCorsa);
		window.open('${pageContext.request.contextPath}/admin/admin-gestioneRimborsi?idCorsa='+idCorsa,'_blank');
	});
	
	$(document).on("click", ".gestioneRitardo", function(e) {
		var idRitardo = $(this).attr('id');
		//window.location.replace('${pageContext.request.contextPath}/admin/admin-gestioneRitardi?idRitardi='+idRitardo);
		window.open('${pageContext.request.contextPath}/admin/admin-gestioneRitardi?idRitardi='+idRitardo,'_blank');
	});
	
	$(document).on("click", ".gestioneSupplemento", function(e) {
		var idSupplemento = $(this).attr('id');
		window.open('${pageContext.request.contextPath}/admin/admin-gestioneSupplementi?idSupplementi='+idSupplemento,'_blank');
	});
	
	
	$(document).on("click", ".paginaGestioneSupplementi", function(e) {
		window.open('${pageContext.request.contextPath}/admin/admin-gestioneSupplementi','_blank');
	});
	
	//--------CONFIRM NOTE CORSA
	$(document).on("click", ".insertRitardo", function(e) {
	var idCorsa = $(this).attr('id');
	var ritardoAndataRitorno = $(this).attr('name');
	var ritardoAndataRitornoValue = $(this).attr('value');
	//alert( idCorsa );
	bootbox.prompt({
		title: "Inserisci Numero di Mezzore ritado "+ritardoAndataRitorno.toUpperCase(),
        value: ritardoAndataRitornoValue,
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
	           //alert(resultBootBox);
		   		$.ajax({
			    	type: 'POST',
		            url: '${pageContext.request.contextPath}/inserisciRitardoCorsaAdmin',
		            dataType: "json",
					data: {
					idRicTransfert : idCorsa,
					ritardoAndataRitorno : ritardoAndataRitorno,
		            numeroMezzore : resultBootBox
					},
					beforeSend: function(){ },
					success: function(result) {
					}, //fine success
			        error: function (req, status, error) {
					alert('errore ajax inserisciRitardoCorsaAdmin');}
				});
			}
		}
	});
	});
	
   	//--------CONFIRM NOTE CORSA
	$(document).on("click", ".alertConfirmNoteCorsa", function(e) {
	var idCorsa = $(this).attr('id');
	//alert( idCorsa );
	bootbox.prompt({
		title: "Scrivi nota corsa",
        inputType: 'textarea',
        value: $(this).val(),
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
	           //alert(resultBootBox);
	   			//-----Ajax scriviNoteCorseAdmin inizio
		   		$.ajax({
			    	type: 'POST',
		            url: '${pageContext.request.contextPath}/scriviNoteCorseAdmin',
		            dataType: "json",
					data: {
					idRicTransfert : idCorsa,
		            noteCorsa : resultBootBox
					},
					beforeSend: function(){ },
					success: function(result) {
					}, //fine success
			        error: function (req, status, error) {
					alert('errore ajax scriviNoteCorseAdmin');}
				});
				//-----Ajax scriviNoteCorseAdmin fine
			}
		}
	});
	}); // fine alertConfirm
	//--------CONFIRM NOTE CORSA
	
}); // fine ready
</script>