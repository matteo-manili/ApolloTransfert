<script type="text/javascript">
jQuery(document).ready(function(){
	
	$('.open-popup-login').on('click',function(){
		$(".pop-up-login").fadeIn(100, function() {  
            $("#j_username").focus();
        });
	});
	
	$( "#loginButton" ).click(function() {
		$.ajax({
			url: '${pageContext.request.contextPath}/j_security_check',  
			data: { j_username: $('#j_username').val(), j_password: $('#j_password').val() }, 
			type: "POST",
			beforeSend: function (xhr) {
				xhr.setRequestHeader("X-Ajax-call", "true"); // funziona solo da chiamate interne al server. altrimenti toglierlo e mettere: dataType: 'json',
			},
			success: function(result) {      
				if (result == "success") {
					$("#ajax_login_error_").html('<div  class="text-success">login riuscito</div>') ;
					location.reload();
					return true;
				}else {           
					$("#ajax_login_error_").html('<p class="text-danger">login non riuscito, credenziali errate</p>') ;
					return false;           
				}
			},
			error: function(XMLHttpRequest, textStatus, errorThrown){
				alert('Bad user/password');
				return false; 
			}
		});
	});
}); // fine ready
</script>

<!-- POPUP LOGIN -->
<div class="pop-up-container pop-up-login" id="loginModal">
	<div class="pop-up">
		<div class="container">	
			<div class="row">
				<div class="col-md-8 col-md-offset-2 text-center">
					<i class="fas fa-times-circle close-popup"></i>
					<div class="pop-bg row">	
						<h4>LOGIN</h4>
						<div style="display:block;" class="col-md-6 col-md-offset-3">
							<span class="col-xs-12 col-sm-12 col-md-12 col-lg-12" id="ajax_login_error_"></span>
							<div class="form-group input-group">
								<span class="input-group-addon"><i class="fas fa-envelope"></i></span>
								<input type="text" id="j_username" class="form-control" placeholder="Inserisci Email o Numero di Telefono" />
							</div>
							<div class="form-group input-group">
								<span class="input-group-addon"><i class="fas fa-lock"></i></span>
								<input type="password" class="form-control" id="j_password" autocomplete="on" placeholder="<fmt:message key="label.password"/>">
							</div>
							<input type="button" name="login-button-popup" id="loginButton" class="btn btn-info " value="<fmt:message key='button.login'/>">
						</div>
					</div>	
				</div>
			</div>
		</div>	
	</div>
</div>

<%@ include file="/scripts/ModalInvioSmsEmail.jsp"%>


<c:if test="${not ricercaTransfert.verificatoCustomer}">

<!-- vedere: https://github.com/jackocnr/intl-tel-input
<demo: http://jackocnr.com/intl-tel-input.html 
 per il ajax spring login vedere: https://gal-levinsky.blogspot.it/2011/08/spring-security-3-ajax-login.html?showComment=1484708527942#c804789591960377995
 per autenticaziones personalizzate vedere: http://www.baeldung.com/spring_redirect_after_login
-->
<script src="<c:url value="/scripts/vendor/intl-tel-input-9.0.3/build/js/utils.js"/>"></script>
<script src="<c:url value="/scripts/vendor/intl-tel-input-9.0.3/build/js/intlTelInput.min.js"/>"></script>
<link rel="stylesheet" href="<c:url value="/scripts/vendor/intl-tel-input-9.0.3/build/css/intlTelInput.css"/>"> 


<div ${ricercaTransfert.verificatoCustomer ? 'style="display: none;"' : ''}>
	<!-- NUOVA GRAFICA AUTENTICAZIONE CLIETE -->
	<div class="div-verify">
		<div class="row head-verify">
			<h3>
			<p style="padding-bottom: 25px;"><u><big><strong>Scegli il metodo di Autenticazione</strong></big></u></p>
			
			<p>&#8226; Accedi col tuo Account Google <span style="display: inline-block" id="my-signin2"></span></p>
			<script src="https://apis.google.com/js/platform.js?onload=renderButton" async defer></script>
			<meta name="google-signin-scope" content="profile email"><meta name="google-signin-client_id" content="${GOOGLE_CLIENT_ID}">
			<script type="text/javascript">
			function onSuccess(googleUser) {
				console.log(123);
				var profile = googleUser.getBasicProfile();
				console.log('ID: ' + profile.getId()); // Do not send to your backend! Use an ID token instead.
				console.log('Name: ' + profile.getName());
				console.log('Email: ' + profile.getEmail()); // This is null if the 'email' scope is not present.
				var verificatoCustomer = ${ricercaTransfert.verificatoCustomer};
				if( googleUser.getAuthResponse().id_token != null && verificatoCustomer == false){
					//$("#id-google-signin-token").val( googleUser.getAuthResponse().id_token );
					//var $form = $('#ricercaTransfertForm');
					//$form.append($('<input type="hidden" name="google-signin-token">').val( googleUser.getAuthResponse().id_token ));
					var $form = $('#ricercaTransfertForm');
					$form.append($('<input type="hidden" name="google-signin-token">').val( googleUser.getAuthResponse().id_token ));
					//disconnette l'account google da questo sito, probabilmente da fare automanticamente o manualmente nella prossima pagina (ric_payment forse) perché l'utente potrebbe 
					//collegarsi senza volerlo e non poter più usare altri metodi di autenticazione (login e invio sms) 
					signOut();
					console.log('faccio il submit google: '+ googleUser.getAuthResponse().id_token);
					$form.get(0).submit();
				}
			}
			function onFailure(error) {
			      console.log(error);
		    }
			function renderButton() {
				gapi.signin2.render('my-signin2', {
					'scope': 'profile email', 'width': 160, 'height': 40,
					'theme': 'dark', //'theme': 'light',
					'onsuccess': onSuccess, 'onfailure': onFailure
				});
			}
			//disconnette l'account google da questo sito, probabilmente da fare automanticamente o manualmente nella prossima pagina (ric_payment forse) perché l'utente potrebbe 
			//collegarsi senza volerlo e non poter più usare altri metodi di autenticazione (login e invio sms) 
			function signOut() {
				var auth2 = gapi.auth2.getAuthInstance();
				auth2.signOut().then(function () {
					console.log('User signed out.');
				});
			}
			</script>
			
			<p>&#8226; Accedi col tuo Account Facebook <fb:login-button size="xlarge" scope="public_profile,email" onlogin="checkLoginState();"></fb:login-button>
			<script>
			window.fbAsyncInit = function() {
			  FB.init({
			    appId      : '${FB_APP_ID}',
			    cookie     : true,
			    xfbml      : true,
			    version    : 'v9.0'
			  });
			  FB.AppEvents.logPageView();   
			};
			
			(function(d, s, id){
			   var js, fjs = d.getElementsByTagName(s)[0];
			   if (d.getElementById(id)) {return;}
			   js = d.createElement(s); js.id = id;
			   js.src = "https://connect.facebook.net/it_IT/sdk.js";
			   fjs.parentNode.insertBefore(js, fjs);
			 }(document, 'script', 'facebook-jssdk'));
			 
			FB.getLoginStatus(function(response) {
			    statusChangeCallback(response);
			});
			
			function checkLoginState() {
				  FB.getLoginStatus(function(response) {
				    statusChangeCallback(response);
				  });
				}
				
			function statusChangeCallback(response) {
				console.log('statusChangeCallback');
				console.log(response);
				console.log(response.authResponse.accessToken);
				//alert(response.authResponse.accessToken);
				if (response.status === 'connected') {
					//window.location.href= '<c:url value="/prove?access_token="/>'+response.authResponse.accessToken; 
					var $form = $('#ricercaTransfertForm');
					$form.append($('<input type="hidden" name="facebook-signin-token">').val( response.authResponse.accessToken ));
					console.log('faccio il submit facebook: '+ response.authResponse.accessToken);
					$form.get(0).submit();
				} else {
					// The person is not logged into your app or we are unable to tell.
					document.getElementById('status').innerHTML = 'Please log ' + 'into this app.';
				}
			}
			</script>

			<p>&#8226; Se sei Registrato ad <fmt:message key="webapp.apollotransfert.name"/>&nbsp;<a class="open-popup-login" onClick="return false;" href="#"><span style="color: white;"><b><ins>fai il Login</ins></b></span></a>
			</p>

			<p>&#8226; Inserisci <b>Numero di telefono</b> per verificare le tue credenziali, ti invieremo un SMS con il <b>codice</b> da inserire nel campo <b>Codice SMS</b>.</p>
			</h3>
		</div>
		
		<div class="row row-verify">
			<div class="col-md-12">
				<div class="form-group input-group">
				<label class="checkbox-inline"><input type="checkbox" name="check-privacy-policy" class="" 
					required ${not empty ricercaTransfert.phoneNumberCustomer ? 'checked' : '' }>
					<fmt:message key="privacy.policy.desc"><fmt:param value="${pageContext.request.contextPath}/privacy-policy"/></fmt:message>
					<div class=" with-errors"></div></label>
				</div>
			</div>
		</div>
		
		<div class="row row-verify">
			<div class="col-md-6">
				<div class="row">
					<div class="col-md-9">
						<div class="form-group input-group">
							<span class="input-group-addon"><i class="fas fa-mobile-alt"></i></span>
							<input type="tel" class="form-control" id="phoneId" placeholder="Numero Telefono" value="${ricercaTransfert.phoneNumberCustomer}" 
								name="number-tel-customer" autocomplete="off" />
						</div>
					</div>
					<div class="col-md-3">
						<input type="button" id="inviaSmsId" value="invia SMS" >  
					</div>
					<div class="col-xs-3 col-sm-3 col-md-2 col-lg-2">
						<c:if test="${ricercaTransfert.verificatoCustomer}">
							<h4><strong><p class="text-success"><small>Numero Confermato</small></p></strong></h4>
						</c:if>
						<span id="valid-msg" class="hide text-success"><small>Valid</small></span>
						<span id="error-msg" class="hide text-danger"><small>Invalid number</small></span>
					</div>
				</div>
			</div>
			<div class="col-md-6">
				<div class="row">
					<div class="col-md-7">
						<div class="form-group input-group">
							<span class="input-group-addon"><i class="far fa-keyboard"></i></span>
							<input type="text" class="form-control" maxlength="4" name="codice-verifica-sms-customer" placeholder="Codice SMS" />
						</div>
					</div>
					<div class="col-md-5">
						<input type="button" class="inviaSubmit" name="verifica-sms-customer" id="verificaCodeSmsId" value="Verifica Codice">
					</div>
				</div>
			</div>
		</div>
	</div>
</div>


	
<script type="text/javascript">
$(document).ready(function() {
	var local = '<fmt:message key="language.code"/>';
	$.getScript('<c:url value="/scripts/vendor/intl-tel-input-9.0.3/build/js/Start_intlTelInput.js"/>');
	//$('#ricercaTransfertForm').validator('validate').has('.has-error').length;
	
	// serve a togliere lo scroll
	$('#ricercaTransfertForm').validator({
		focus: false
	});
	
	function ControllaCampi() {
		var checkbox = $("input[name='check-privacy-policy']:checkbox"); //console.log( checkbox.is(":checked") ); 
		console.log(123);
		if( checkbox.is(":checked") == false ){
			$(".emailSmsPopUp").fadeIn(500);
			$(".img-loader").fadeOut(500); 
			$("#idModalEsitoInvioTitle").html('SMS');
			$("#idModalEsitoInvioText").html("<div class='text-primary'>E' necessario acconsentire al Trattamento dei Dati</div>");
		}
		if ($('#ricercaTransfertForm').validator('validate').has('.has-error').length) {
			return false;
		}else{
			return true;
		}
	}
	
	$("#inviaSmsId").click(function() {
		if( ControllaCampi() == false ) {
			return false;
		}
		if( $('#phoneId').val() ) {
			if( $("#phoneId").intlTelInput("isValidNumber") ){
				var numberTelCustomer = $("#phoneId").intlTelInput("getNumber");
				var idRichTransfert = '${ricercaTransfert.id}'; 
				$.ajax({
					type: 'POST',
					url: '${pageContext.request.contextPath}/inviaSMSCustomer',
					dataType: "json",
					data: { 
					numberTelCustomer : numberTelCustomer,
					idRichTransfert : idRichTransfert
					},
					beforeSend: function(){
						//$("#invioSmsEmailConfirm").modal('show');
						//document.getElementById('loader').style.display = 'block'; // visualizzo il div
						$("#idModalEsitoInvioTitle").html('SMS');
						$("#idModalEsitoInvioText").html('');
						$(".img-loader").fadeIn(500);
						$(".emailSmsPopUp").fadeIn(500);
					},
					success: function(data) {
						var json_x = data;
						var esitoInvioSms = json_x['esitoInvioSms'];
						var erroreDate = json_x['erroreDate'];
						if( esitoInvioSms == 'success' ){
							$(".img-loader").fadeOut(500); //nascondo il loader
							$("#idModalEsitoInvioTitle").html('SMS');
							$("#idModalEsitoInvioText").html('<div class="text-primary"><strong>SMS INVIATO</strong><br>'+
									'Ti abbiamo inviato un Codice sul tuo Telefono, Inserisci il Codice SMS</div>');
							//$("#inviaSmsId").prop('disabled', true);
							//$("#verificaCodeSmsId").prop('disabled', false);
						}else{
							$(".img-loader").fadeOut(500); //nascondo il loader
							$("#idModalEsitoInvioTitle").html('SMS');
							$("#idModalEsitoInvioText").html('<div class="text-info"><strong>SMS non inviato</strong><br>'+
									'Numero telefonico non valido, Riprova un altro Numero</div>');
							//$("#inviaSmsId").prop('disabled', false);
							//$("#verificaCodeSmsId").prop('disabled', true);
						}
						if( erroreDate == 'OK' ){
							window.location.href = "<c:url value='/'/>?courseId="+idRichTransfert; // <c:url value='/'/>
						}
					}, //fine success
					error: function (req, status, error) {
						alert('errore ajax inviaSMSCustomer');
					}
				});
			}else{
				$("#idModalEsitoInvioTitle").html('SMS');
				$("#idModalEsitoInvioText").html('<div class="text-primary"><strong>numero telefono non corretto</strong></div>');
				$(".img-loader").fadeOut(0); //nascondo il loader
				$(".emailSmsPopUp").fadeIn(0);
			}
		}else{
			bootbox.alert({
				backdrop: true,
				message: "Inserisci numero telefono."
			});
		}
	});
	
	var telInput = $("#phoneId"),
	errorMsg = $("#error-msg"),
	validMsg = $("#valid-msg");
	var reset = function() {
		telInput.removeClass("error");
		errorMsg.addClass("hide");
		validMsg.addClass("hide");
		};
	// on blur: validate
	telInput.keyup(function() {
		reset();
		$("#idModalEsitoInvioText").html('');
		if ($.trim(telInput.val())) {
			if (telInput.intlTelInput("isValidNumber")) {
				//validMsg.removeClass("hide");
				//$("#inviaSmsId").prop('disabled', false);
				//$("#verificaCodeSmsId").prop('disabled', false);
			} else {
				//telInput.addClass("error");
				//errorMsg.removeClass("hide");
				//$("#inviaSmsId").prop('disabled', true);
				//$("#verificaCodeSmsId").prop('disabled', true);
			}
		}
	});
	// on keyup / change flag: reset
	telInput.on("keyup change", reset);

});
</script>

<c:set var="scripts" scope="request">
	<%@ include file="/scripts/login.js"%>
</c:set>

</c:if>
