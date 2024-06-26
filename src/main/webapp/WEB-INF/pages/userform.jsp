<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="userProfile.title"/></title>
    <meta name="menu" content="UserMenu"/>
    
    <!-- jquery -->
	<link rel="stylesheet" href="<c:url value="/scripts/vendor/jquery-ui-1.11.4/jquery-ui.css"/>">
	
	<script src="<c:url value="/scripts/vendor/jquery-ui-1.11.4/external/jquery/jquery-2.2.4.min.js"/>"></script>
	<script src="<c:url value="/js/bootstrap.min.js"/>"></script>
	<script src="<c:url value="/scripts/vendor/jquery-ui-1.11.4/jquery-ui.min.js"/>"></script>
	
	<!-- input telefono -->
	<script src="<c:url value="/scripts/vendor/intl-tel-input-9.0.3/build/js/utils.js"/>"></script>
	<script src="<c:url value="/scripts/vendor/intl-tel-input-9.0.3/build/js/intlTelInput.min.js"/>"></script>
	<link rel="stylesheet" href="<c:url value="/scripts/vendor/intl-tel-input-9.0.3/build/css/intlTelInput.css"/>"> 
	
	<!-- toggle bottoni-checkbox -->
	<script src="<c:url value="/scripts/vendor/bootstrap-toggle.min.js"/>"></script>
	<link rel="stylesheet" href="<c:url value="/css/bootstrap-toggle.min.css"/>">
	
<c:set var="delObject" scope="request"><fmt:message key="userList.user"/></c:set>

<script type="text/javascript">
$(document).ready(function () {
	$(function() {
		$('#tipoVenditoreCheckID').change(function() {
			//alert( $(this).prop('checked') );
			$("#userForm").submit();
		})
		$('#tipoAutistaCheckID').change(function() {
			//alert( $(this).prop('checked') );
			$("#userForm").submit();
		})
		$('#tipoAutistaAziendaCheckID').change(function() {
			//alert( $(this).prop('checked') );
			$("#userForm").submit();
		})
	})
	$.getScript('<c:url value="/scripts/vendor/intl-tel-input-9.0.3/build/js/Start_intlTelInput.js"/>');
	function setPhone(){
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
	$('#autistaFormId').submit(function() {
		setPhone();
	});
	$("#phoneId").keyup(function() {
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

<div class="content-area home-content">
<div class="container">
<div class="col-md-12">

	<spring:bind path="user.*">
	    <c:if test="${not empty status.errorMessages}">
	        <div class="alert alert-danger alert-dismissable">
	            <a href="#" data-dismiss="alert" class="close">&times;</a>
	            <c:forEach var="error" items="${status.errorMessages}">
	                <c:out value="${error}" escapeXml="false"/><br/>
	            </c:forEach>
	        </div>
	    </c:if>
	</spring:bind>
	    
	<%@ include file="/common/messages.jsp" %>
	
	<c:if test="${fn:contains(tipoRuoliList, 'AUTISTA_ROLE') && autista.attivo }">
   		<%@ include file="/common/menu_autista.jsp"%>
	</c:if>
    <form:form cssClass="" commandName="user" method="post" action="userform" id="userForm" autocomplete="off" enctype="multipart/form-data" onsubmit="/*return validateUser(this)*/">
        <form:hidden path="id"/>
        <form:hidden path="version"/>
        <div class="row form-group">
            <spring:bind path="user.firstName">
            <div class="col-sm-6 ${(not empty status.errorMessage) ? ' has-error' : ''}">
            </spring:bind>
                <appfuse:label styleClass="control-label" key="user.firstName"/>
                <form:input cssClass="form-control" path="firstName" id="firstName" maxlength="50" disabled="${(autista.autistaDocumento.approvatoGenerale) ? 'true' : 'false'}" />
                <form:errors path="firstName" cssClass="help-block"/>
            </div>
            <spring:bind path="user.lastName">
            <div class="col-sm-6 ${(not empty status.errorMessage) ? ' has-error' : ''}">
            </spring:bind>
                <appfuse:label styleClass="control-label" key="user.lastName"/>
                <form:input cssClass="form-control" path="lastName" id="lastName" maxlength="50" disabled="${(autista.autistaDocumento.approvatoGenerale) ? 'true' : 'false'}" />
                <form:errors path="lastName" cssClass="help-block"/>
            </div>
        </div>
        <div class="row form-group">
            <spring:bind path="user.email">
            <div class="col-sm-6 ${(not empty status.errorMessage) ? ' has-error' : ''}">
            </spring:bind>
                <appfuse:label styleClass="control-label" key="user.email"/>
                <form:input cssClass="form-control" path="email" id="email"/>
                <form:errors path="email" cssClass="help-block"/>
            </div>
            <div class="col-sm-6 ">
                <appfuse:label styleClass="control-label" key="user.phoneNumber"/> *
                <input type="tel" class="form-control intl-tel-input" value="${user.phoneNumber}" 
                	name="number-tel-autista-pre" id="phoneId" tabindex="3">
                <form:hidden path="phoneNumber" id="numberTelAutistaId"/>
                <span id="valid-msg" class="hide text-success">Valid</span>
				<span id="error-msg" class="hide text-danger">Invalid number</span>
            </div>
        </div>
        
        <c:if test="${pageContext.request.isUserInRole('ROLE_VENDITORE')}">
        <div class=" form-group">
        	<spring:bind path="user.codiceVenditore">
        	<div class="col-sm-6 ${(not empty status.errorMessage) ? ' has-error' : ''}">
        	</spring:bind>
	        	<label class="control-label">Codice Venditore</label>
				<form:input cssClass="form-control" path="codiceVenditore" id="idCodiceVenditore" maxlength="10" />
				<form:errors path="codiceVenditore" cssClass="help-block"/>
			</div>
        </div>
        </c:if>
        
        <div class=" form-group">
        	<c:if test="${pageContext.request.remoteUser == user.username}">
				<div class="col-sm-3">
	                <span class="help-block">
	                    <a href="<c:url value="/updatePassword" />"><strong><big><fmt:message key='updatePassword.changePasswordLink'/></big></strong></a>
	                </span>
	            </div>
            </c:if>
        </div>
        
        <c:if test="${empty ambienteVenditore || ambienteVenditore == false}">
        
        <!-- ATTIVA/DISATTIVA PROFILO VENDITORE -->
        <div class="form-group ">
        	<label class="col-xs-8 col-sm-3 col-md-3 col-lg-2 form-control-label">Attiva Profilo Venditore</label>
			<div class="col-sm-3">
				<input type="checkbox" id="tipoVenditoreCheckID" name="tipoVenditoreCheck" ${pageContext.request.isUserInRole('ROLE_VENDITORE') ? 'checked':''}  
					data-toggle="toggle" data-size="small" data-on="ATTIVATO" data-off="DISATTIVATO" data-onstyle="success" data-offstyle="info">
			</div>
        </div>
        
        <c:if test="${not empty autista && autista.attivo}">
			<%@ include file="/common/info_autista_privato_azienda.jsp" %>
        </c:if>
        
        <div class="form-group ">
			<label class="col-xs-8 col-sm-3 col-md-3 col-lg-2 form-control-label">Attiva Profilo Autista</label>
			<div class="col-sm-3">
				<input type="checkbox" id="tipoAutistaCheckID" name="tipoAutistaCheck" ${autista.attivo ? 'checked':''}  
					data-toggle="toggle" data-size="small" data-on="ATTIVATO" data-off="DISATTIVATO" data-onstyle="success" data-offstyle="info">
			</div>
			<c:if test="${not empty autista && autista.attivo}">
				<label class="col-xs-8 col-sm-3 col-md-3 col-lg-2 form-control-label">Privato o Azienda?</label>
				<div class="col-sm-3">
					<input type="checkbox" id="tipoAutistaAziendaCheckID" name="tipoAutistaAziendaCheck" ${autista.azienda ? 'checked':''}  
						data-toggle="toggle" data-size="small" data-on="<i class='fa fa-industry'></i> AZIENDA" data-off="<i class='fa fa-user'></i> PRIVATO" data-onstyle="info" data-offstyle="success">
				</div>
			</c:if>
		</div>
		
		
		<div class="form-group ">
			<c:if test="${not empty autista && autista.attivo}">
				<div class="col-sm-10">
				<p class="${(autista.autistaDocumento.approvatoGenerale) ? 'text-success' : 'text-danger'}">
					<strong>
					${(autista.autistaDocumento.approvatoGenerale) ? '(Profilo Approvato)' : '(Profilo non Approvato, Inserire tutti i Documenti e attendere l&apos;Approvazione)'}
					</strong>
				</p>
				</div>
			</c:if>
		</div>
		</c:if>
		
		<c:if test="${empty autista || !autista.attivo}">
        	<%@ include file="/common/userForm_AddressFatturazione.jsp"%>
        </c:if>
        
        <div class="form-group">
        	<!-- metto utente ciro disabled perché è un utente di test dove la gente esterna fa un giro nell'applicazione -->
			<button type="button" class="btn btn-danger alertConfirmGenerale" name="delete" ${(user.id == -3) ? 'disabled' : ''}>
	      		<fmt:message key="button.deleteUtente"/>
	    	</button>

            <button type="submit" class="btn btn-default" name="cancel" onclick="bCancel=true">
				<fmt:message key="button.cancel"/>
            </button>
            
            <button type="submit" class="btn btn-primary" name="save" onclick="bCancel=false">
                <fmt:message key="button.save"/>
            </button>
        </div>
    </form:form>

</div>
</div>
</div>

<%@ include file="/scripts/ConfirmSubmit.jsp"%>

<c:set var="scripts" scope="request">
<script type="text/javascript">
// This is here so we can exclude the selectAll call when roles is hidden
function onFormSubmit(theForm) {
    return validateUser(theForm);
}
</script>
</c:set>

<v:javascript formName="user" staticJavascript="false"/>
<script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"></script>
</body>
