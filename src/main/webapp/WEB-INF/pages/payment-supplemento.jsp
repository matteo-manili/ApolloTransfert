<%@ include file="/common/taglibs.jsp"%>
<head>
	<title><fmt:message key="home.title"/></title>
	
	<link rel="stylesheet" href="<c:url value="/scripts/vendor/jquery-ui-1.11.4/jquery-ui.css"/>">
	<script src="<c:url value="/scripts/vendor/jquery-ui-1.11.4/external/jquery/jquery-2.2.4.min.js"/>"></script>
	<script src="<c:url value="/js/bootstrap.min.js"/>"></script>
	<script src="<c:url value="/scripts/vendor/jquery-ui-1.11.4/jquery-ui.min.js"/>"></script>
	<script src="<c:url value="/scripts/vendor/jquery-ui-1.11.4/i18n/jquery-ui-i18n.min.js"/>"></script>
	<script src="<c:url value="/scripts/vendor/bootbox.min.js"/>"></script>
	<script src="https://js.stripe.com/v3/"></script>
	<script src="https://www.paypal.com/sdk/js?client-id=${ID_CLIENT}&currency=EUR&disable-funding=card"></script>
	<c:set var="tipoPagamentoStripe" value="pagamento-stripe" />
	<c:set var="tipoPagamentoPayPal" value="pagamento-paypal" />

</head>

<body>
<%
try {
%>
<div class="content-area home-content">
<div class="">
<div class="">

<spring:bind path="supplemento.*">
	<c:if test="${not empty status.errorMessages}">
		<div class="alert alert-danger alert-dismissable">
			<a href="#" data-dismiss="alert" class="close">&times;</a>
			<c:forEach var="error" items="${status.errorMessages}">
				<c:if test = "${error != 'null'}">   
					<c:out value="${error}" escapeXml="false"/><br/>
				</c:if>
			</c:forEach>
		</div>
	</c:if>
</spring:bind>

<%@ include file="/common/messages.jsp" %>

<c:choose>
<c:when test="${not supplemento.pagato}">
	
	<c:if test="${false && (smsSkebbyAbilitato == false || stripeSWITCH == false || payPalSWITCH == false) }">
		<div class="alert alert-success h4" role="alert">
			<strong>
			<p><big>Attenzione!</big> Apollotransfert &egrave; in fase di test, le funzioni di prenotazione e di pagamento sono inattive.</p>
			<p class="">Grazie per l'attenzione, presto saremo on-line!</p></strong>
			<hr>
			<strong>
			<p><big>Warning!</big> Apollotransfert is under test, booking and payment functions are inactive.</p>
			<p class="">Thanks for your attention, we will soon be online!</p></strong>
		</div>
	</c:if>
	
	<form method="post" action="<c:url value='/pagamentoSupplemento'/>" id="payment-form">
		<input type="hidden" value="${supplemento.id}" name="supplemento-id">

		<div class="page-header text-primary">
			<c:set var="totalePrezzoRitardi" value="${supplemento.prezzo}"/>
			<h3>Pagamento Supplemento <fmt:formatNumber value="${totalePrezzoRitardi}" pattern="0.00" />&euro;</h3>
		</div>
	
		<div class="alert alert-danger" id="a_x200" style="display: none;">
			<strong>Error!</strong> <span class="payment-errors"></span>
		</div>

		<div class="alert alert-success">
			<p class="h4"><strong>Supplemento Extra Corsa </strong></p>
			<p><strong> Id Corsa:</strong> ${supplemento.ricercaTransfert.id} <br> 
			<strong> Partenza:</strong> ${supplemento.ricercaTransfert.formattedAddress_Partenza} 
			<strong> Giorno:</strong> <fmt:formatDate pattern="HH:mm, dd MMMM, yyyy" value="${supplemento.ricercaTransfert.dataOraPrelevamentoDate}" /><br>
			<strong> Arrivo:</strong> ${supplemento.ricercaTransfert.formattedAddress_Arrivo} 
			<c:if test="${supplemento.ricercaTransfert.ritorno}">
				<strong> Giorno:</strong> <fmt:formatDate pattern="HH:mm, dd MMMM, yyyy" value="${supplemento.ricercaTransfert.dataOraRitornoDate}" />
			</c:if><br>
			<strong> ${supplemento.ricercaTransfert.ritorno ? 'ANDATA e RITORNO' : 'SOLO ANDATA'}</strong></p>
		<hr class="message-inner-separator">
			<p><strong>Prezzo:</strong> <fmt:formatNumber value="${supplemento.prezzo}" pattern="0.00" />&euro;</p>
			<p><strong>Descrizione:</strong> <c:out value="${supplemento.descrizione}"></c:out></p>
		</div>
		
		<div class="panel panel-default">
		<div class="panel-heading"><big>Metodo Pagamento</big></div>
		<div class="panel-body">
			<!-- TAB - STRIPE | PAYPAL -->
			<ul class="nav nav-tabs" role="tablist">
				<li role="presentation" class="active"><a href="#tab_stripe" role="tab" aria-controls="pay" data-toggle="tab">
					<i class="fa fa-credit-card" aria-hidden="true"></i> Carta di Credito</a></li>
				<li role="presentation"><a href="#tab_paypal" role="tab" aria-controls="pay" data-toggle="tab">
					<i class="fa fa-paypal" aria-hidden="true"></i> PayPal</a></li>
			</ul>
			<!-- Tabs -->
			<div class="tab-content">
				<div role="tabpanel" class="tab-pane active" id="tab_stripe" style="padding: 20px;">

					<div class="row">
						<div class="form-group col-sm-8">
							<label class="control-label" for="cardholder-name-id">Nome Titolare</label>
			      			<input type="text" id="cardholder-name-id" name="cardholder-name" class="form-control" 
			      				placeholder="Mario Rossi" required="required" />
						</div>
					</div>
					<div class="row">
						<div class="form-group col-sm-8">
							<label class="control-label" for="card-element">Numero Carta</label>
							<div id="card-element" class="form-control"></div>
						</div>
						<div class="form-group col-sm-8">
							<div id="card-errors" class="text-danger"></div>
						</div>
					</div>
					<div class="row">
						<div class="form-group col-sm-8">
			      			<button type="submit" id="card-button" data-secret="${client_secret}" class="btn btn-sm btn-success">Esegui Pagamento</button>
						</div>
					</div>
					
				</div>
				<!-- Tab PayPal -->
				<div role="tabpanel" class="tab-pane" id="tab_paypal" style="padding: 20px;">
					<div id="paypal-button"></div>
				</div>
			</div>
		</div> <!-- fine panel-body  -->
		<div class="panel-footer"><fmt:message key='payment.info'/></div>
		</div> <!-- fine panel-default -->
	</form>
	
	
	<script type="text/javascript">
	//alert('${STRIPE_PUBLISCHABLE_KEY}');
	var stripe = Stripe('${STRIPE_PUBLISCHABLE_KEY}');
	var elements = stripe.elements();
	var cardElement = elements.create('card'); 
	cardElement.mount('#card-element');
	
	var form = document.getElementById('payment-form');
	var cardholderName = document.getElementById('cardholder-name-id');
	var cardButton = document.getElementById('card-button');
	var clientSecret = cardButton.dataset.secret;
	
	form.addEventListener('submit', function(event) {
		event.preventDefault();
		stripe.handleCardPayment(clientSecret, cardElement, {
			payment_method_data: {
				billing_details: {name: cardholderName.value, email: '${supplemento.ricercaTransfert.user.email}', phone: '${supplemento.ricercaTransfert.user.phoneNumber}'}
			}
		}).then(function(result) {
			if (result.error) {
				// Display error.message in your UI.
				var errorElement = document.getElementById('card-errors');
				errorElement.textContent = result.error.message;
			}else{
				stripe.retrievePaymentIntent(clientSecret).then(function(result) {
				if (result.error) {
					// Inform the user if there was an error
					var errorElement = document.getElementById('card-errors');
					errorElement.textContent = result.error.message;
				}else {
					//alert('result.paymentIntent.id: '+result.paymentIntent.id);
					stripeTokenHandler(result.paymentIntent.id);
				}
				});
			}
		});
	});
	
	function stripeTokenHandler(paymentIntentId) {
		//alert('sasa: '+paymentIntentId);
		//$("#attesaLoader").modal('show');
		$(".attesaLoaderPopUp").fadeIn(300);
		var $form = $('#payment-form');
		$form.append($('<input type="hidden" name="stripePaymentIntent">').val(paymentIntentId));
		$form.append($('<input type="hidden" name="${tipoPagamentoStripe}">').val('${tipoPagamentoStripe}'));
		$form.append($('<input type="hidden" name="id-ricerca-transfert">').val('${supplemento.ricercaTransfert.id}'));
		$form.get(0).submit();
	}
	
	// Handle real-time validation errors from the card Element.
	cardElement.addEventListener('change', function(event) {
		var displayError = document.getElementById('card-errors');
		if (event.error) {
			displayError.textContent = event.error.message;
		}else{
			displayError.textContent = '';
		}
	});
	</script>
	
	<script type="text/javascript">
		var $form = $('#payment-form');
		var totaleEuro =  '${supplemento.prezzo}';
		paypal.Buttons({
	        createOrder: function(data, actions) {
	    		return actions.order.create({
	    			purchase_units: [{
	    			amount: {value: totaleEuro, currency: 'EUR' },
	    			description: 'idCorsa: ${supplemento.ricercaTransfert.id} tipo: ${tipoPagamentoPayPal}'
	    			}]
	    		});
	    	},
	        onApprove: function(data, actions) {
				// ESEGUO IL PAGAMENTO PAYPAL
        		return actions.order.capture().then(function(details) {
	                // https://www.paypal.com/businessexp/summary?token=EC-6P017336UG830080J
					//questo ti mand a https://www.sandbox.paypal.com/myaccount/home
				    $form.append($('<input type="hidden" name="${tipoPagamentoPayPal}">').val('${tipoPagamentoPaypal}'));
				    $form.append($('<input type="hidden" name="payment-paypal-id">').val( data.orderID ));
				    $form.append($('<input type="hidden" name="id-ricerca-transfert">').val('${supplemento.ricercaTransfert.id}'));
				    // Submit the form:
				    $form.get(0).submit();
	            }); 
	        },
	        onCancel: function(data) { },
	        onError: function(err) {
				$form.append($('<input type="hidden" name="${tipoPagamentoPayPal}">').val('${tipoPagamentoPaypal}'));
	        	$form.append($('<input type="hidden" name="payment-error">').val( err ));
			    $form.append($('<input type="hidden" name="id-ricerca-transfert">').val('${supplemento.ricercaTransfert.id}'));
			    // Submit the form:
			    $form.get(0).submit();
	        }
		}).render('#paypal-button');
	</script>
</c:when>
<c:otherwise>
	<div class="alert alert-success">
		<p><strong>Pagamento Supplemento già eseguito,</strong>
			<a href="<c:url value='home-user'/>" class="alert-link">Vai alla Home <i class="fa fa-home"></i></a></p>
	</div>
</c:otherwise>
</c:choose>

</div>
</div>
</div>
<%		 
}
catch (Exception e) {
out.println("An exception occurred: " + e.getMessage());
}
%>
<%@ include file="/scripts/ConfirmSubmit.jsp"%>
</body>
