<%@ include file="/common/taglibs.jsp"%>
<head>
<title><fmt:message key="signup.title"/></title>
<meta name="description" content="Registrati a <fmt:message key="webapp.apollotransfert.name"/>">
<link rel="canonical" href="<fmt:message key="https.w3.domain.apollotransfert.name"/><c:url value='/signup'/>"/>

<script src="<c:url value="/scripts/vendor/jquery-ui-1.11.4/external/jquery/jquery-2.2.4.min.js"/>"></script>
<script src="<c:url value="/js/bootstrap.min.js"/>"></script>

<!-- input telefono -->
<script src="<c:url value="/scripts/vendor/intl-tel-input-9.0.3/build/js/utils.js"/>"></script>
<script src="<c:url value="/scripts/vendor/intl-tel-input-9.0.3/build/js/intlTelInput.min.js"/>"></script>
<link rel="stylesheet" href="<c:url value="/scripts/vendor/intl-tel-input-9.0.3/build/css/intlTelInput.css"/>"> 
	
<!-- toggle bottoni-checkbox -->
<script src="<c:url value="/scripts/vendor/bootstrap-toggle.min.js"/>"></script>
<link rel="stylesheet" href="<c:url value="/css/bootstrap-toggle.min.css"/>">

<script type="text/javascript">
$(document).ready(function () {
	
	//$("#phoneId").intlTelInput("setNumber", ${autista.user.phoneNumber});
	//$("#phoneId").intlTelInput("setNumber", "");

	$.getScript('<c:url value="/scripts/vendor/intl-tel-input-9.0.3/build/js/Start_intlTelInput.js"/>');

	function setPhone(){
		//alert(111);
		var verifica = $("#phoneId").intlTelInput("isValidNumber");
		if(verifica){
			$("#numberTelAutistaId").val( $("#phoneId").intlTelInput("getNumber") );
			$("#valid-msg").removeClass("hide");
			$("#error-msg").addClass("hide");
		}else{
			$("#numberTelAutistaId").val( "noValidNumber" )
			$("#error-msg").removeClass("hide");
			$("#valid-msg").addClass("hide");
		}
	};
	// al cambio della bandierina
	$("#phoneId").on("countrychange", function(e, countryData) {
		setPhone();
	});
	$("#phoneId").change(function(){
		setPhone();
	});
	$('#signupFormId').submit(function() {
		setPhone();
	});
	$("#phoneId").keyup(function() {
		//alert('wewewe');
		reset();
		setPhone();
	});
	var reset = function() {
		$("#valid-msg").addClass("hide");
		$("#error-msg").addClass("hide");
	};
}); // fine ready
</script>
</head>

<body>

<!-- grafica nuova
<div class="page-banner" style="">
	<h1><fmt:message key="signup.heading"/></h1>
</div>  -->

<div class="content-area home-content">
<div class="container">
<div class="col-md-12">

<spring:bind path="user.*">
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

<div class=" ">

	<h2><fmt:message key="signup.heading"/></h2>

	<form:form id="signupFormId" commandName="user" modelAttribute="user" method="post" action="signup" autocomplete="off" onsubmit="">
		<div class="row form-group">
			<label for="firstNameID" class="col-sm-2 form-control-label">Nome</label>
			<div class="col-sm-4">
				<form:input path="firstName" class="form-control" id="firstNameID" tabindex="1" autocomplete="on" />
			</div>
			<label for="lastNameID" class="col-sm-2 form-control-label">Cognome</label>
			<div class="col-sm-4">
				<form:input path="lastName" class="form-control" id="lastNameID" tabindex="2" autocomplete="on" />
			</div>
		</div>
		<div class="row form-group">
			<label for="emailID" class="col-sm-2 form-control-label">Email</label>
			<div class="col-sm-4">
				<form:input path="email" class="form-control" id="emailID" tabindex="3" autocomplete="on" />
			</div>
			<label for="phoneId" class="col-sm-2 form-control-label">Telefono</label>
			<div class="col-sm-4">
				<input type="tel" class="form-control intl-tel-input" value="${user.phoneNumber}" 
					name="number-tel-autista-pre" id="phoneId" tabindex="4">
				<input type="hidden" name="number-tel-autista" id="numberTelAutistaId">
				<span id="valid-msg" class="hide text-success">Valid</span>
				<span id="error-msg" class="hide text-danger">Invalid number</span>
			</div>
		</div>
		
		<div class="row form-group">
			<label for="passwordID" class="col-sm-2 form-control-label">Password</label>
			<div class="col-sm-4">
				<input type="password" name="password" class="form-control" id="passwordID" tabindex="5">
			</div>
			<label for="confirmPasswordID" class="col-sm-2 form-control-label">Conferma Password</label>
			<div class="col-sm-4">
				<input type="password" name="conferma-password" class="form-control" id="confirmPasswordID" tabindex="6">
			</div>
		</div>
		
		<c:if test="${empty ambienteVenditore || ambienteVenditore == false}">
		<!-- TIPO USER -->
		<div class="row form-group">
			<label class="col-sm-2 form-control-label">Registrami come</label>
				<div class="btn-group " data-toggle="buttons">
					<label class="btn btn-primary btn-sm ${tipoUser == 1 ? 'active' : ''}">
					<input type="radio" name="tipoUser" value="1" ${tipoUser == 1 ? 'checked' : ''}>
					<i class='fa fa-user'></i> Cliente</label>
					
					<label class="btn btn-primary btn-sm ${tipoUser == 2 ? 'active' : ''}">
					<input type="radio" name="tipoUser" value="2" ${tipoUser == 2 ? 'checked' : ''}>
					<i class='fa fa-car'></i> Autista</label>
					
					<label class="btn btn-primary btn-sm ${tipoUser == 3 ? 'active' : ''}">
					<input type="radio" name="tipoUser" value="3" ${tipoUser == 3 ? 'checked' : ''}>
					<i class="fa fa-handshake-o" aria-hidden="true"></i> Venditore</label>
				</div>
		</div>
		</c:if>
        
        <div class="row form-group">
			<div class="col-sm-12 ">
				<label class="checkbox-inline"><form:checkbox path="checkPrivacyPolicy" class="" tabindex="7" /><fmt:message key="privacy.policy.desc">
					<fmt:param value="${pageContext.request.contextPath}/privacy-policy"/></fmt:message></label>
			</div>
		</div>
		
		<div class="row form-group">
			<div class="col-sm-12 ">
				<button type="submit" class="btn btn-primary btn-lg" name="save" onclick="bCancel=false" tabindex="8">
						<fmt:message key="button.register"/></button>
				<button type="submit" class="btn btn-default btn-lg" name="cancel" onclick="bCancel=true" tabindex="9">
					<fmt:message key="button.cancel"/></button>
			</div>
		</div>
	</form:form>
</div> 
</div>
</div>
</div>
<%@ include file="/scripts/ConfirmSubmit.jsp"%>
</body>