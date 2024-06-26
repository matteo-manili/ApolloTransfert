<script src="<c:url value="/scripts/vendor/bootbox.min.js"/>"></script>
<c:choose>
<c:when test = "${ricercaTransfert.tipoServizio == 'AGA'}">
	<div class="page-header text-primary">
		<h3>Pagamento <fmt:formatNumber value="${ricercaTransfert.agendaAutistaScelta.prezzoTotaleCliente}" pattern="0.00" />&euro;</h3>
	</div>
</c:when>
<c:when test = "${ricercaTransfert.tipoServizio == 'ST'}">
	<div class="page-header text-primary">
	<c:choose>
	<c:when test = "${ricercaTransfert.pagamentoParziale eq false}">
		<h3>Pagamento <fmt:formatNumber value="${ricercaTransfert.richiestaMediaScelta.prezzoTotaleCliente}" pattern="0.00" />&euro;</h3>
	</c:when>
	<c:when test = "${ricercaTransfert.pagamentoParziale eq true}">
		<h3>Pagamento <fmt:message key="prezzo.cliente.parziale.piu.prezzo.autista">
			<fmt:param value="${ricercaTransfert.richiestaMediaScelta.prezzoCommissioneServizio}"/>
			<fmt:param value="${ricercaTransfert.richiestaMediaScelta.prezzoTotaleAutista}"/>
		</fmt:message></h3>
	</c:when>
	</c:choose>
	</div>
</c:when>
<c:when test = "${ricercaTransfert.tipoServizio == 'PART' or ricercaTransfert.tipoServizio == 'MULTIP'}">
	<div class="page-header text-primary">
	<c:choose>
	<c:when test = "${ricercaTransfert.tipoServizio == 'PART'}">
		<c:set var="prezzoTotaleClientePart" value="${richAutistPart.prezzoTotaleCliente}" />
	</c:when>
	<c:when test = "${ricercaTransfert.tipoServizio == 'MULTIP'}">
		<c:forEach items="${richAutistPart.richiestaAutistaParticolareMultiploList}" var="varObj" varStatus="stat">
  			<c:set var="tokenRichAutistaPart_Multiplo" value="${tokenRichAutistaPart_Multiplo}${varObj.token}-" />
		</c:forEach>
		<c:set var="prezzoTotaleClientePart" value="${prezzoTotaleClienteMultiplo}" />
	</c:when>
	</c:choose>
	<h3>Pagamento <fmt:formatNumber value="${prezzoTotaleClientePart}" pattern="0.00" />&euro;</h3>
		<input type="hidden" value="${tokenRichAutistaPart_Multiplo}" name="da_togliere">
	</div>
</c:when>
<c:otherwise>
</c:otherwise>
</c:choose>

<div class="alert alert-danger" id="a_x200" style="display: none;">
	<b>Error!</b> <span class="payment-errors"></span>
</div>

<!-- GRAFICA METODO PAGAMENTO -->
<div class="div-sel-car div-card-detail">
	<div class="row head-sel-car">
		<h3>Metodo Pagamento</h3>
	</div>
	<div class="row sel-car-btn">
		<div class="pay-tab">
			<ul>
				<li class="wwd-li wwd-1 wwd-li-active" data-div="wwd-1">Carta di Credito</li>
				<li class="wwd-li wwd-2"  data-div="wwd-2">PayPal</li>
			</ul>
		</div>
		<!-- STRIPE -->
		<div class="wwd-1 sec sec-hide" data-div="wwd-1" style="display: block;">
			<div class="col-md-12">
				<div>
					<div class="form-group input-group">
						<span class="input-group-addon"><i class="fas fa-user"></i></span>
						<input type="text" class="form-control" id="cardholder-name-id" name="cardholder-name" placeholder="Nome Titolare Carta di Credito" />
					</div>
					<div class="form-group ">
						<div id="card-element" style="padding-left: 10px;"></div>
						<div id="card-errors" class="text-danger"></div>
					</div>
					<input type="button" class="eseguiPagamentoStripe " value="Esegui Pagamento">
				</div>
			</div>
			<div class="col-md-12 pay-ex-info text-center">
				<p><fmt:message key='payment.info'/></p>
			</div>
		</div>
		<!-- PAYPAL -->
		<div class="wwd-2 sec sec-hide" data-div="wwd-2" >
			<div class="col-md-12">
				<div>
					<div id="paypal-button"></div>
				</div>
			</div>
			<div class="col-md-12 pay-ex-info text-center">
				<p><fmt:message key='payment.info'/></p>
			</div>
		</div>
	</div>
</div>

<!-- stripe payment -->
<script src="https://js.stripe.com/v3/"></script>
<script src="https://www.paypal.com/sdk/js?client-id=${ID_CLIENT}&currency=EUR"></script>
<script type="text/javascript">
$(document).ready(function() {
	$(document).on("click", ".inviaSubmitSconto", function(e) {
		$('.changeFormValidation').attr("required", false); 
		var formElement = $(this).closest('form'); //.attr('id');
		var nameElement = $(this).attr('name');
		var valueElement = $(this).val();
		$(formElement).append("<input type='hidden' name='"+nameElement+"' value='"+valueElement+"' />");
		$( formElement ).submit();
	});
	$('.open-popup-sconto').on('click',function(){
		$(".pop-up-sconto").fadeIn(100, function() {  
            $("#inserisciCodiceSconto").focus();
        });
	});
	$('#ricercaTransfertForm').validator({
		focus: false
	});
	$('#ricercaTransfertForm').validator('validate').has('.has-error').length;
	// questo valore serve alla libreria 1000hz-bootstrap-validator/0.11.5/validator.min.js 
	// di settare il padding superiore quando viene attivato il focus sul campo dopo la validazione errata.
	//$.fn.validator.Constructor.FOCUS_OFFSET = '210';
}); // fine ready

var cardholderNameId = document.getElementById("cardholder-name-id");
console.log( cardholderNameId.getAttribute("placeholder") );
//tabs click functions
$('[data-div]').on('click',function() {
if ( this.tagName == 'LI' ) {
	$('.wwd-li-active').removeClass('wwd-li-active');	
	$(this).addClass('wwd-li-active');
	var getTarget = $(this).data('div');		
	$('.sec-hide').each(function() {
		if($(this).hasClass(getTarget) == true) {
			$('.sec-hide').hide();
			$(this).fadeIn();
			if ($(window).width() <= 767 && $(window).width() >= 100) {
				var hheight = $(".bg-img").height();
				var section = $(this).offset().top-hheight;
			}
		}
	});
}
});

var idRicTransfert = '${ricercaTransfert.id}'; var idRichAutistaPart;
<c:choose>
<c:when test = "${ricercaTransfert.tipoServizio == 'PART'}">
	idRichAutistaPart = '${richAutistPart.id}';
</c:when>
<c:when test = "${ricercaTransfert.tipoServizio == 'MULTIP'}">
	idRichAutistaPart = '${tokenRichAutistaPart_Multiplo}';
</c:when>
</c:choose>

var $form = $('#ricercaTransfertForm');

//PAGAMENTO STRIPE
var stripe = Stripe('${STRIPE_PUBLISCHABLE_KEY}');
var elements = stripe.elements();
var style = {
	base: {
		color: 'black',
		lineHeight: '24px',
		fontFamily: '"Helvetica Neue", Helvetica, sans-serif',
		fontSmoothing: 'antialiased',
		fontSize: '20px', '::placeholder': {
			color: 'black'
		}
	},
	invalid: {
		color: '#fa755a',
		iconColor: '#fa755a'
	}
};
// Create an instance of the card Element
var card = elements.create('card', {style: style});
// Add an instance of the card Element into the `card-element` <div>
card.mount('#card-element');
// Handle real-time validation errors from the card Element.
card.addEventListener('change', function(event) {
	var displayError = document.getElementById('card-errors');
	if (event.error) {
		displayError.textContent = event.error.message;
	}else{
		displayError.textContent = '';
	}
});

var form = document.getElementById('ricercaTransfertForm');
$(document).on("click", ".eseguiPagamentoStripe", function(event) {
	$(".attesaLoaderPopUp").fadeIn(300);
	
if( ControllaCampi() == false ){
	$('.pop-up-container').fadeOut(500);
	var checkbox = $("input[name='check-privacy-policy-pay']:checkbox"); 
		console.log( checkbox.is(":checked") ); 
		$(".emailSmsPopUp").fadeIn(500);
		$(".img-loader").fadeOut(500); 
		$("#idModalEsitoInvioTitle").html('SMS');
		$("#idModalEsitoInvioText").html("<div class='text-primary'>Inserisci i dati richiesti e acconsenti al Trattamento dei Dati</div>");
	return false;
}else {
	var clientSecretStripe;
	$.ajax({
		type: 'POST',
		url: '${pageContext.request.contextPath}/GetClientSecretStripe',
		dataType: "json",
		data: {
			idRicTransfert : idRicTransfert,
			idRichAutistaPart : idRichAutistaPart
		},
		success: function(result) {
			clientSecretStripe = result['GetClientSecretStripe'];
		},
		error: function (req, status, error) {
			$('.pop-up-container').fadeOut(500);
		},
		async: false // le chiamate ajax sono asincrone di default, quindi per fare le assegnazioni di variabili javascript mettere async: false
	});

	$.ajax({
		type: 'POST',
		url: '${pageContext.request.contextPath}/controlloPrePayment',
		dataType: "json",
		data: {
			idRicTransfert : idRicTransfert,
			idRichAutistaPart : idRichAutistaPart
		},
		success: function(resultAjax) {
			if(resultAjax.hasOwnProperty('tipoErrore')) {
				ShowErrorMessage( resultAjax, idRicTransfert );
				
			}else {
				// ESEGUO IL PAGAMENTO STRIPE
				event.preventDefault();
				var name = form.querySelector('input[name=cardholder-name]').value;
			  	stripe.confirmCardPayment(clientSecretStripe, {
			    payment_method: {
			      card: card,
			      billing_details: {
			        name: name
			      }
			    }
			  	}).then(function(resultConfirmCardPay) {
				    if (resultConfirmCardPay.error) {
				    	$('.pop-up-container').fadeOut(500);
						// Show error to your customer (e.g., insufficient funds)
				      	console.log(resultConfirmCardPay.error.message);
				    } else {
				      	// The payment has been processed!
						if (resultConfirmCardPay.paymentIntent.status === 'succeeded') {
							$form.append($('<input type="hidden" name="pagamento-servizio">').val('pagamento-servizio'));
							$form.append($('<input type="hidden" name="paymentIntentId">').val(resultConfirmCardPay.paymentIntent.id));
							$form.append($('<input type="hidden" name="id-ricerca-transfert">').val('${ricercaTransfert.id}'));
							<c:choose>
							<c:when test = "${ricercaTransfert.tipoServizio == 'PART'}">
								$form.append($('<input type="hidden" name="id-autista-part">').val('${richAutistPart.id}'));
							</c:when>
							<c:when test = "${ricercaTransfert.tipoServizio == 'MULTIP'}">
								$form.append($('<input type="hidden" name="id-autista-part">').val('${tokenRichAutistaPart_Multiplo}'));
							</c:when>
							</c:choose>
							$form.append($('<input type="hidden" name="cliente-firstName">').val( $('#clienteFirstNameId').val() ));
							$form.append($('<input type="hidden" name="cliente-lastName">').val( $('#clienteLastNameId').val() ));
							$form.append($('<input type="hidden" name="cliente-email">').val( $('#clienteEmailId').val() ));
							if( $('#phoneId').length ){
								$form.append($('<input type="hidden" name="cliente-telefono">').val( $('#phoneId').intlTelInput('getNumber') ));	
							}
							$form.get(0).submit();
						}else{
							alert('pagamento NON eseguito');
						}
					}
				});
			}
		},
		error: function (req, status, error) {
			alert('errore ajax controlloPrePayment');
			//window.location.replace('${pageContext.request.contextPath}/home-user');
			location.reload(true);
		}
	});
}
}); // fine inviaSubmit


// PAGAMENTO PAYPAL
<c:choose>
<c:when test = "${ricercaTransfert.tipoServizio == 'AGA'}">
	var totaleEuroPayPal = ${ricercaTransfert.agendaAutistaScelta.prezzoTotaleCliente};
</c:when>
<c:when test = "${ricercaTransfert.tipoServizio == 'ST'}">
	var totaleEuroPayPal = ${ricercaTransfert.pagamentoParziale == true ? ricercaTransfert.richiestaMediaScelta.prezzoCommissioneServizio : ricercaTransfert.richiestaMediaScelta.prezzoTotaleCliente};
</c:when>
<c:when test = "${ricercaTransfert.tipoServizio == 'PART' or ricercaTransfert.tipoServizio == 'MULTIP'}">
	var totaleEuroPayPal = ${prezzoTotaleClientePart};
</c:when>
</c:choose>

paypal.Buttons({
	/*
	onInit: function(data, actions) {
		validatePaypalButton(actions);
		$('.changeFormValidation').change('input',function(e){
		    validatePaypalButton(actions);
		});
    },
    */
    style: {
        shape: 'pill',
        color: 'gold',
        layout: 'horizontal',
        label: 'paypal',
        
    },
	onClick: function() {
		//alert(222);
		if( ControllaCampi() == false ){
			return false;
		}else{
			return true;
		}
		
	},
    createOrder: function(data, actions) {
		return actions.order.create({
			purchase_units: [{
			amount: {currency: 'EUR', value: totaleEuroPayPal },
			description: 'idCorsa: ${ricercaTransfert.id} tipo: ${ricercaTransfert.tipoServizio}'
			}]
		});
	},
	onApprove: function(data, actions) {
		$.ajax({
			type: 'POST',
			url: '${pageContext.request.contextPath}/controlloPrePayment',
			dataType: "json",
			data: {
				idRicTransfert : idRicTransfert,
				idRichAutistaPart : idRichAutistaPart
			},
			beforeSend: function(){ },
			success: function(result) {
				if(result.hasOwnProperty('tipoErrore')) {
					ShowErrorMessage( result, idRicTransfert );
					
				} else{
					// ESEGUO IL PAGAMENTO PAYPAL
					//$("#attesaLoader").modal('show');
					$(".attesaLoaderPopUp").fadeIn(300);
					return actions.order.capture().then(function(details) {
						//Show a success page to the buyer
						//alert('paypal execute'); //alert(data.payerID); //alert(data.paymentID); //alert(data.returnUrl);
						//actions.redirect(); //alert(data); //alert(actions);
						//https://www.paypal.com/businessexp/summary?token=EC-6P017336UG830080J
						//questo ti mand a https://www.sandbox.paypal.com/myaccount/home
						$form.append($('<input type="hidden" name="pagamento-servizio">').val('pagamento-servizio'));
						$form.append($('<input type="hidden" name="payment-paypal-id">').val( data.orderID ));
						$form.append($('<input type="hidden" name="id-ricerca-transfert">').val('${ricercaTransfert.id}'));
						<c:choose>
						<c:when test = "${ricercaTransfert.tipoServizio == 'PART'}">
							$form.append($('<input type="hidden" name="id-autista-part">').val('${richAutistPart.id}'));
						</c:when>
						<c:when test = "${ricercaTransfert.tipoServizio == 'MULTIP'}">
							$form.append($('<input type="hidden" name="id-autista-part">').val('${tokenRichAutistaPart_Multiplo}'));
						</c:when>
						</c:choose>
						$form.append($('<input type="hidden" name="cliente-firstName">').val( $('#clienteFirstNameId').val() ));
						$form.append($('<input type="hidden" name="cliente-lastName">').val( $('#clienteLastNameId').val() ));
						$form.append($('<input type="hidden" name="cliente-email">').val( $('#clienteEmailId').val() ));
						if( $('#phoneId').length ){
							$form.append($('<input type="hidden" name="cliente-telefono">').val( $('#phoneId').intlTelInput('getNumber') ));
						}
						$form.get(0).submit();
					});
				}
			}, //fine success
			error: function (req, status, error) {
				alert('errore ajax controlloPrePayment');
				//window.location.replace('${pageContext.request.contextPath}/home-user');
				location.reload(true);
			}
		});
	},
	onCancel: function(data) {
		//alert('paypal onCancel');
		// https://www.paypal.com/businessexp/summary?token=EC-6P017336UG830080J
		//questo ti mand a https://www.sandbox.paypal.com/myaccount/home
		//actions.redirect(); 
	},
	onError: function(err) {
		//alert('paypal onError');
		//alert(err);
		//alert(err.name);
		// Show an error page here, when an error occurs
		$form.append($('<input type="hidden" name="pagamento-servizio">').val('pagamento-servizio'));
		$form.append($('<input type="hidden" name="payment-paypal-error">').val( err ));
		$form.append($('<input type="hidden" name="id-ricerca-transfert">').val('${ricercaTransfert.id}'));
		<c:choose>
		<c:when test = "${ricercaTransfert.tipoServizio == 'PART'}">
			$form.append($('<input type="hidden" name="id-autista-part">').val('${richAutistPart.id}'));
		</c:when>
		<c:when test = "${ricercaTransfert.tipoServizio == 'MULTIP'}">
			$form.append($('<input type="hidden" name="id-autista-part">').val('${tokenRichAutistaPart_Multiplo}'));
		</c:when>
		</c:choose>
		$form.append($('<input type="hidden" name="cliente-firstName">').val(null));
		$form.append($('<input type="hidden" name="cliente-lastName">').val(null));
		$form.append($('<input type="hidden" name="cliente-email">').val(null));
		$form.append($('<input type="hidden" name="cliente-telefono">').val( null ));
		// Submit the form:
		$form.get(0).submit();
	}
}).render('#paypal-button');

function ControllaCampi() {
	if ($('#ricercaTransfertForm').validator('validate').has('.has-error').length) {
		return false;
	}else{
		return true;
	}
}

function ShowErrorMessage(result, idRicTransfert) {
	if( result['tipoErrore'] == "dataRicercaScaduta" ){
		bootbox.alert({
			message: result['messErrore'],
			callback: function () {
			window.location.replace('${pageContext.request.contextPath}/?courseId='+idRicTransfert);
			}
		});
	}else if( result['tipoErrore'] == "dataPartenzaDeveEssereUnOraSuccessivaDaAdesso" ){
		bootbox.alert({
			message: result['messErrore'],
			callback: function () {
			window.location.replace('${pageContext.request.contextPath}/?courseId='+idRicTransfert);
			}
		});
	}else if( result['tipoErrore'] == "autistiNonDisponibili" ){
		bootbox.alert({
			message: result['messErrore'],
			callback: function () {
			window.location.replace('${pageContext.request.contextPath}/?courseId='+idRicTransfert);
			}
		});
	}else if( result['tipoErrore'] == "pagamentoNonAutorizzato" ){
		bootbox.alert({
			message: result['messErrore'],
			callback: function () {
			window.location.replace('${pageContext.request.contextPath}/?courseId='+idRicTransfert);
			}
		});
	}
}
</script>