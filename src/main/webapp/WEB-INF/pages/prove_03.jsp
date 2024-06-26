<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="home.title"/></title>
    <meta name="breadcrumb" content="<fmt:message key="breadcrumb.prove"/>"/>
	
	<!-- jquery -->
	<link rel="stylesheet" href="<c:url value="/scripts/vendor/jquery-ui-1.11.4/jquery-ui.css"/>">
	<script src="<c:url value="/scripts/vendor/jquery-ui-1.11.4/external/jquery/jquery.js"/>"></script>
	<script src="<c:url value="/scripts/vendor/jquery-ui-1.11.4/jquery-ui.min.js"/>"></script>
	<script src="<c:url value="/scripts/vendor/jquery-ui-1.11.4/i18n/jquery-ui-i18n.min.js"/>"></script>

	<!-- geo plugin  -->
	<script language="JavaScript" src="http://www.geoplugin.net/javascript.gp" type="text/javascript"></script>

	<!-- dataTables -->
	<link rel="stylesheet" href="<c:url value="/scripts/vendor/DataTables/css/dataTables.bootstrap.css"/>">
	<script src="<c:url value="/scripts/vendor/DataTables/js/jquery.dataTables.js"/>"></script>
	<script src="<c:url value="/scripts/vendor/DataTables/js/dataTables.bootstrap.js"/>"></script>

	<!-- google api -->
	<script src="http://maps.google.com/maps/api/js"></script>

	<!-- jQuery countdown360 vedere: https://github.com/johnschult/jquery.countdown360 -->
	<script src="<c:url value="/js/jquery.countdown360.js"/>"></script>

	<script type="text/javascript">

	$(document).ready(function () {

		
		
	}); // fine ready
	</script>


</head>
<body>

<div class="">
	<div class="">
		<div class="row">
			<div class="col-sm-12" >	
			<!-- ----------------------------------------------------------------------------------------------------- -->
			
		
			<form action="" method="POST">
			  <script
			    src="https://checkout.stripe.com/checkout.js" class="stripe-button"
			    data-key="pk_test_8JXDT4fNn8DcTegyJF6UYtbS"
			    data-amount="5000"
			    data-name="Apollotransfert"
			    data-description="Widget"
			    data-image="<c:url value='/images/favicon1.png'/>"
			    data-locale="auto"
			    data-zip-code="false"
			    data-currency="eur">
			  </script>
			</form>

	
	
			<!-- metodo pagamento personalizzato -->

			<script type="text/javascript" src="https://js.stripe.com/v2/"></script>
			
			<script type="text/javascript">
			  Stripe.setPublishableKey('pk_test_8JXDT4fNn8DcTegyJF6UYtbS'); // TEST
			  //Stripe.setPublishableKey('pk_live_3Lb0XKJkFOXMMR35EhXqc2jw'); // LIVE 
			</script>

			<!-- Non dare nomi agli elementi dettagli pagamento di forma! 
			Questo impedisce che i dati provenienti da toccare il server, 
			il che significa che non è più necessario preoccuparsi fornendoci i registri, 
			la crittografia dei dati di titolari di carta, o altri oneri di conformità PCI. -->
			<form action="" method="POST" id="payment-form">
			  <span class="payment-errors"></span>
			
			
				<label>
					<span>totale euro</span>
					<input type="text" size="20" value="0050" name="totaleEuro">
				</label>
			
			  <div class="form-row">
			    <label>
			      <span>Card Number</span>
			      <input type="text" size="20" data-stripe="number">
			    </label>
			  </div>
			
			  <div class="form-row">
			    <label>
			      <span>Expiration (MM/YY)</span>
			      <input type="text" size="2" data-stripe="exp_month">
			    </label>
			    <span> / </span>
			    <input type="text" size="2" data-stripe="exp_year">
			  </div>
			
			  <div class="form-row">
			    <label>
			      <span>CVC</span>
			      <input type="text" size="4" data-stripe="cvc">
			    </label>
			  </div>
			
			  <div class="form-row">
			    <label>
			      <span>Billing Zip</span>
			      <input type="text" size="6" data-stripe="address_zip">
			    </label>
			  </div>
			
			  <input type="submit" class="submit" value="Submit Payment">
			</form>
			
			
			<script type="text/javascript">

				$(document).ready(function () {
					
					$(function() {
					  	var $form = $('#payment-form');
					  	$form.submit(function(event) {
					  		
					  		alert(111);
					  		
						    // Disable the submit button to prevent repeated clicks:
						    $form.find('.submit').prop('disabled', true);
	
						    // Request a token from Stripe:
						    Stripe.card.createToken($form, stripeResponseHandler);
	
						    // Prevent the form from being submitted:
						    return false;
						});
					});
					
					
					
					function stripeResponseHandler(status, response) {
						  // Grab the form:
						  var $form = $('#payment-form');
	
						  if (response.error) { // Problem!
							  
							  alert('errore pagamento');
	
						    // Show the errors on the form:
						    $form.find('.payment-errors').text(response.error.message);
						    $form.find('.submit').prop('disabled', false); // Re-enable submission
	
						  } else { // Token was created!
							
							  
							alert(333);
						    // Get the token ID:
						    var token = response.id;
						    alert('token: '+token);
						    // Insert the token ID into the form so it gets submitted to the server:
						    $form.append($('<input type="hidden" name="stripeToken">').val(token));
	
						    // Submit the form:
						    $form.get(0).submit();
						  }
					};
					
					
			
				}); // fine ready
			</script>




			<br><br>
			<!-- ----------------------------------------------------------------------------------------------------- -->
			</div>
		</div>
	</div>
</div>
</body>