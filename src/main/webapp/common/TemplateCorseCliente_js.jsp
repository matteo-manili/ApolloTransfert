<%@ include file="/scripts/apollotransfert-js-utility.jsp"%>
<script src="<c:url value="/scripts/vendor/bootbox.min.js"/>"></script>
<script type="text/javascript">
$(document).ready(function () {
	// ------- INIZIO CORSE CLIENTE -------
   	var jsonObj = ${mainCorseCliente};
	var html_CORSE_DA_ESEGUIRE_CLIENTE = "";
	var html_CORSE_ESEGUITA_CLIENTE = "";

	var corseAgendaAutistaClienteTpl = $('#corseAgendaAutistaClienteTpl').html();
	var corseAgendaAutistaCompletateClienteTpl = $('#corseAgendaAutistaCompletateClienteTpl').html();
	
	var corseMultipleClienteTpl = $('#corseMultipleClienteTpl').html();
	var corseMultipleCompletateClienteTpl = $('#corseMultipleCompletateClienteTpl').html();
	var corseParticolariClienteTpl = $('#corseParticolariClienteTpl').html();
	var corseParticolariCompletateClienteTpl = $('#corseParticolariCompletateClienteTpl').html();
	var corseMedieClienteTpl = $('#corseMedieClienteTpl').html();
	var corseMedieCompletateClienteTpl = $('#corseMedieCompletateClienteTpl').html();
	var datiPasseggeroModalTpl = $('#datiPasseggeroModalTpl').html();

	Mustache.parse(corseAgendaAutistaClienteTpl); // optional, speeds up future uses
	Mustache.parse(corseAgendaAutistaCompletateClienteTpl); // optional, speeds up future uses
	
	Mustache.parse(corseMultipleClienteTpl); // optional, speeds up future uses
	Mustache.parse(corseMultipleCompletateClienteTpl); // optional, speeds up future uses
	Mustache.parse(corseParticolariClienteTpl); // optional, speeds up future uses
	Mustache.parse(corseParticolariCompletateClienteTpl); // optional, speeds up future uses
	Mustache.parse(corseMedieClienteTpl); // optional, speeds up future uses
	Mustache.parse(corseMedieCompletateClienteTpl); // optional, speeds up future uses
	Mustache.parse(datiPasseggeroModalTpl); // optional, speeds up future uses

	$.each(jsonObj, function(index, val){
	for(var i=0; i < val.length; i++){
		var item = val[i];
        //alert(item.template);
		if(item.template == "AGENDA_AUTISTA_DA_ESEGUIRE"){
			html_CORSE_DA_ESEGUIRE_CLIENTE+= Mustache.render(corseAgendaAutistaClienteTpl, item);
			html_CORSE_DA_ESEGUIRE_CLIENTE+= Mustache.render(datiPasseggeroModalTpl, item);
	    }
        if(item.template == "AGENDA_AUTISTA_ESEGUITA"){
      	  html_CORSE_ESEGUITA_CLIENTE+= Mustache.render(corseAgendaAutistaCompletateClienteTpl, item);
        }
		if(item.template == "MULTIPLA_DA_ESEGUIRE"){
      	  html_CORSE_DA_ESEGUIRE_CLIENTE+= Mustache.render(corseMultipleClienteTpl, item);
		  html_CORSE_DA_ESEGUIRE_CLIENTE+= Mustache.render(datiPasseggeroModalTpl, item);
        }
        if(item.template == "MULTIPLA_ESEGUITA"){
      	  html_CORSE_ESEGUITA_CLIENTE+= Mustache.render(corseMultipleCompletateClienteTpl, item);
        }
        if(item.template == "PARTICOLARE_DA_ESEGUIRE"){
      	  html_CORSE_DA_ESEGUIRE_CLIENTE+= Mustache.render(corseParticolariClienteTpl, item);
		  html_CORSE_DA_ESEGUIRE_CLIENTE+= Mustache.render(datiPasseggeroModalTpl, item);
        }
        if(item.template == "PARTICOLARE_ESEGUITA"){
      	  html_CORSE_ESEGUITA_CLIENTE+= Mustache.render(corseParticolariCompletateClienteTpl, item);
        }
        if(item.template == "MEDIA_DA_ESEGUIRE"){
      	  html_CORSE_DA_ESEGUIRE_CLIENTE+= Mustache.render(corseMedieClienteTpl, item);
      	  html_CORSE_DA_ESEGUIRE_CLIENTE+= Mustache.render(datiPasseggeroModalTpl, item);
        }
        if(item.template == "MEDIA_ESEGUITA"){
      	  html_CORSE_ESEGUITA_CLIENTE+= Mustache.render(corseMedieCompletateClienteTpl, item);
        }
	}
	$("#corseClienteA").html(html_CORSE_DA_ESEGUIRE_CLIENTE);
	$("#corseClienteB").html(html_CORSE_ESEGUITA_CLIENTE);
	});
	// ------- FINE CORSE CLIENTE -------
    		
	function blink() {
	    $('.blinkeffect').fadeOut(500).fadeIn(500);
	}
	setInterval(blink, 1000);

}); // fine ready
</script>
