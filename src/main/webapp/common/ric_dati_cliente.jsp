<div class="div-sel-car text-center data-client">
	<div class="row head-sel-car">
		<h3>DATI CLIENTE</h3>
	</div>
	<div class="row sel-car-btn">
	
		<div class=" col-sm-6">
		<div class="form-group input-group">
			<span class="input-group-addon"><i class="fas fa-user"></i></span>
			<input type="text" id="clienteFirstNameId" name="cliente-firstName" placeholder="Nome" value="${ricercaTransfert.ricTransfert_Nome}" maxlength="65" 
				class="form-control changeFormValidation" required />
			<!-- aggiungere help-block al class per visualizzare più informazioni sull'errore (da togliere nel mobile perché copre gli input) -->
			<div class=" with-errors"></div> 
		</div>
		</div>
		<div class=" col-sm-6">
		<div class="form-group input-group">
			<span class="input-group-addon"><i class="fas fa-user"></i></span>
			<input type="text"  id="clienteLastNameId" name="cliente-lastName" placeholder="Cognome" value="${ricercaTransfert.ricTransfert_Cognome}" maxlength="65" 
				class="form-control changeFormValidation" required />
			<!-- aggiungere help-block al class per visualizzare più informazioni sull'errore (da togliere nel mobile perché copre gli input) -->
			<div class=" with-errors"></div> 
		</div>
		</div>
		<div class=" col-sm-12">
		<div class="form-group input-group">
			<span class="input-group-addon"><i class="fas fa-envelope"></i></span>
			<input type="email"  id="clienteEmailId" name="cliente-email" placeholder="Email" value="${ricercaTransfert.ricTransfert_Email}" maxlength="65" 
				class="form-control changeFormValidation" required />
			<!-- aggiungere help-block al class per visualizzare più informazioni sull'errore (da togliere nel mobile perché copre gli input) -->
			<div class=" with-errors"></div> 
		</div>
		</div>
		<div class=" col-sm-12">
		<div class="form-group input-group">
			<span class="input-group-addon"><i class="fas fa-envelope"></i></span>
			<input type="email" id="confClienteEmailId" name="conf-cliente-email"placeholder="Conferma Email" value="${ricercaTransfert.ricTransfert_Email}" maxlength="65" 
				data-match="#clienteEmailId" data-match-error="Email Non Corrispondenti" class="form-control changeFormValidation" required />
			<!-- aggiungere help-block al class per visualizzare più informazioni sull'errore (da togliere nel mobile perché copre gli input) -->
			<div class=" with-errors"></div> 
		</div>
		</div>
		
		<div class=" col-sm-12">
		<c:choose>
		<c:when test="${not empty ricercaTransfert.phoneNumberCustomer}">
			<div class="form-group input-group">
			Telefono Confermato: <b><c:out value="${fn:substring(ricercaTransfert.phoneNumberCustomer, 0, 3)}.${fn:substring(ricercaTransfert.phoneNumberCustomer,3,6)}.${fn:substring(ricercaTransfert.phoneNumberCustomer,6,9)}.${fn:substring(ricercaTransfert.phoneNumberCustomer,9,15)}"/></b>
			</div>
		</c:when>
		<c:otherwise>
		
			<!-- vedere: https://github.com/jackocnr/intl-tel-input
			<demo: http://jackocnr.com/intl-tel-input.html 
			 per il ajax spring login vedere: https://gal-levinsky.blogspot.it/2011/08/spring-security-3-ajax-login.html?showComment=1484708527942#c804789591960377995
			 per autenticaziones personalizzate vedere: http://www.baeldung.com/spring_redirect_after_login
			-->
			<script src="<c:url value="/scripts/vendor/intl-tel-input-9.0.3/build/js/utils.js"/>"></script>
			<script src="<c:url value="/scripts/vendor/intl-tel-input-9.0.3/build/js/intlTelInput.min.js"/>"></script>
			
			<link rel="stylesheet" href="<c:url value="/scripts/vendor/intl-tel-input-9.0.3/build/css/intlTelInput.css"/>"> 
			<div class="form-group input-group">
				<span class="input-group-addon"><i class="fas fa-mobile-alt"></i></span>
				<input type="tel" class="form-control" id="phoneId" placeholder="Numero Telefono" autocomplete="off" required />
				<input type="hidden" id="idClienteTelefono" name="cliente-telefono" /> <!-- serve per memorizzare il telefono quando si richiedono i preventivi -->
			</div>
			<script type="text/javascript">
			$.getScript('<c:url value="/scripts/vendor/intl-tel-input-9.0.3/build/js/Start_intlTelInput.js"/>');
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
				$('#idClienteTelefono').val( $('#phoneId').intlTelInput('getNumber') );
				$("#idModalEsitoInvioText").html('');
				if ($.trim(telInput.val())) {
					if (telInput.intlTelInput("isValidNumber")) {
						 // fai qualcosa
					}else{
						// fai qualcosa
					}
				}
			});
			telInput.on("countrychange", function() { // al cambio di bandiera stato
				reset();
				$('#idClienteTelefono').val( $('#phoneId').intlTelInput('getNumber') );
			});
			// on keyup / change flag: reset
			telInput.on("keyup change", reset);
			</script>	
 		</c:otherwise>
 		</c:choose>
		</div>
		<c:if test="${empty user}">
			<div class=" col-sm-12">
			<div class="form-group input-group ">
				<label class="checkbox-inline"><input type="checkbox" name="check-privacy-policy-pay" class="changeFormValidation" required>
				<fmt:message key="privacy.policy.desc"><fmt:param value="${pageContext.request.contextPath}/privacy-policy"/></fmt:message>
				<!-- aggiungere help-block al class per visualizzare più informazioni sull'errore (da togliere nel mobile perché copre gli input) -->
				<div class=" with-errors"></div></label>
			</div>
			</div>
		</c:if>
	</div>
</div>