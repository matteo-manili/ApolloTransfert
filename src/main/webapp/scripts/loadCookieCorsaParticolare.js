
jQuery(document).ready(function(){
		//$.cookie("checkCorsaParticolare", null, { });
		//$.removeCookie("checkCorsaParticolare");
		checkCookie();
		setInterval(checkCookie, 20000); // ogni 3 secondi (3000 = 3 secondi)
		
		function checkCookie() {
			var cookieValue = $.cookie("checkCorsaParticolare");
			if( $.cookie("checkCorsaParticolare") != null ){
				var cookieValue = $.cookie("checkCorsaParticolare");
				//alert(cookieValue);
				var str_array = cookieValue.split('#');
				for(var i = 0; i < str_array.length; i++) {
					var element = str_array[i];
				   	var values = element.split('-');
				   	//alert(values[0] +' '+values[1]);
				   	if(values[0] != null && values[1] != null){
				   		 checkCorsaParticolareAjax(values[0], values[1]);
				   	}
				}
			}
		}
		
		// DEPRECATO NON ESISTE PIU' IL FIELD chiamataPrenotata
		function checkCorsaParticolareAjax(idRicTransfert, idAuto){
		    $.ajax({
		    	type: 'POST',
				url: ctx+'/checkCorsaParticolareAjax',
				dataType: "json",
		        data: { 
		        	idRicTransfert : idRicTransfert,
		        	idAuto : idAuto
		        },
		        beforeSend: function(){},
		        success: function(result) {
		            if(result['chiamataPrenotata'] != null && result['chiamataPrenotata'] == true ) {
		            	// vedere: http://bootstrap-notify.remabledesigns.com/
		            	$.notify({
		            		icon: "fa fa-car",
		            		message: "Auto Disponibile!" + " <strong>" +result['autoMess'] + "</strong><br>" + 
		            		result['corsaMess'],
		            		url: "${pageContext.request.contextPath}/?coursePartId="+result['idCorsaPart']
		            	}
		            	,{	type: "info",
		            		delay: 8000,
		            		url_target: "_self",
		            		allow_dismiss: true,
		            		mouse_over: "pause"
		            	});
		            }else{
		            	//alert('222');
		            }
		            if(result['corsaScaduta'] != null && result['corsaScaduta'] == true ){
		            	var cookieValue = $.cookie("checkCorsaParticolare");
		            	$.cookie("checkCorsaParticolare", cookieValue.replace(idRicTransfert+'-'+idAuto, ''));
		            	//alert('333');
		            }
		         }, //fine success
				error: function (req, status, error) {
					//DECOMMENTARE PER CAPIRE QUANDO NON FUNZIONA
					//alert('errore ajax checkCorsaParticolareAjax');
				}
		    });
		}
		
	}); // fine ready