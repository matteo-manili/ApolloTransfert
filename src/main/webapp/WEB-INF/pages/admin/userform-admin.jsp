<%@ include file="/common/taglibs.jsp"%>
<head>
<title><fmt:message key="userProfile.title"/></title>
<meta name="menu" content="AdminMenu"/>

<!-- jquery -->
<link rel="stylesheet" href="<c:url value="/scripts/vendor/jquery-ui-1.11.4/jquery-ui.css"/>">

<script src="<c:url value="/scripts/vendor/jquery-ui-1.11.4/external/jquery/jquery-2.2.4.min.js"/>"></script>
<script src="<c:url value="/js/bootstrap.min.js"/>"></script>
<script src="<c:url value="/scripts/vendor/jquery-ui-1.11.4/jquery-ui.min.js"/>"></script>

<!-- input telefono -->
<script src="<c:url value="/scripts/vendor/intl-tel-input-9.0.3/build/js/utils.js"/>"></script>
<script src="<c:url value="/scripts/vendor/intl-tel-input-9.0.3/build/js/intlTelInput.min.js"/>"></script>
<link rel="stylesheet" href="<c:url value="/scripts/vendor/intl-tel-input-9.0.3/build/css/intlTelInput.css"/>"> 
</head>
<c:set var="delObject" scope="request"><fmt:message key="userList.user"/></c:set>
<script type="text/javascript">
	$(document).ready(function () {
		var msgDelConfirm = "<fmt:message key="delete.confirm"><fmt:param value="${delObject}"/></fmt:message>";
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

<body>

<div class="content-area home-content">
<div class="container">
<div class="">

    <h3 style="margin-top: -20px;">Gestione Utente Admin</h3>
    
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
    
	<div class="">
	    <form:form commandName="user" method="post" action="userform-admin" id="userFormAdmin" autocomplete="off" onsubmit="/*return validateUser(this)*/">
	        <form:hidden path="id"/>
	        <form:hidden path="version"/>
	        <input type="hidden" name="from" value="<c:out value="${param.from}"/>"/>
			<div class="row">
				<spring:bind path="user.username">
		        <div class="col-sm-6 form-group${(not empty status.errorMessage) ? ' has-error' : ''}">
		        </spring:bind>
		            <appfuse:label styleClass="control-label" key="user.username"/>
		            <c:choose>
					    <c:when test="${fn:contains(user.roles, 'ROLE_ADMIN')}">
							<form:input cssClass="form-control" path="username" readonly="true" id="username"/>
					    </c:when>
					    <c:otherwise>
							<form:input cssClass="form-control" path="username" id="username"/>
						</c:otherwise>
					</c:choose>
		            <form:errors path="username" cssClass="help-block"/>
		            <c:if test="${pageContext.request.remoteUser == user.username}">
		                <span class="help-block">
		                    <a href="<c:url value="/updatePassword" />"><fmt:message key='updatePassword.changePasswordLink'/></a>
		                </span>
		            </c:if>
		        </div>
		        
		        <div class="col-sm-6 form-group">
		        	<label class="control-label">Codice Venditore</label>
		        	<form:input cssClass="form-control" path="codiceVenditore" id="codiceVenditore" maxlength="50"/>
		        </div>
		        
			</div>
			
	        <div class="row">
	            <spring:bind path="user.firstName">
	            <div class="col-sm-6 form-group${(not empty status.errorMessage) ? ' has-error' : ''}">
	            </spring:bind>
	                <appfuse:label styleClass="control-label" key="user.firstName"/>
	                <c:choose>
					    <c:when test="${fn:contains(user.roles, 'ROLE_ADMIN')}">
							<form:input cssClass="form-control" path="firstName" id="firstName" readonly="true" maxlength="50"/>
					    </c:when>
					    <c:otherwise>
							<form:input cssClass="form-control" path="firstName" id="firstName" maxlength="50"/>
						</c:otherwise>
					</c:choose>
	                <form:errors path="firstName" cssClass="help-block"/>
	            </div>
	            
	            <spring:bind path="user.lastName">
	            <div class="col-sm-6 form-group${(not empty status.errorMessage) ? ' has-error' : ''}">
	            </spring:bind>
	                <appfuse:label styleClass="control-label" key="user.lastName"/>
	                <c:choose>
					    <c:when test="${fn:contains(user.roles, 'ROLE_ADMIN')}">
							<form:input cssClass="form-control" path="lastName" id="lastName" readonly="true" maxlength="50"/>
					    </c:when>
					    <c:otherwise>
							<form:input cssClass="form-control" path="lastName" id="lastName" maxlength="50"/>
						</c:otherwise>
					</c:choose>
	                <form:errors path="lastName" cssClass="help-block"/>
	            </div>
	        </div>
	        
	        <div class="row">
	            <spring:bind path="user.email">
	            <div class="col-sm-6 form-group${(not empty status.errorMessage) ? ' has-error' : ''}">
	            </spring:bind>
	                <appfuse:label styleClass="control-label" key="user.email"/>
	                <c:choose>
					    <c:when test="${fn:contains(user.roles, 'ROLE_ADMIN')}">
							<form:input cssClass="form-control" path="email" readonly="true" id="email"/>
					    </c:when>
					    <c:otherwise>
							<form:input cssClass="form-control" path="email" id="email"/>
						</c:otherwise>
					</c:choose>
	                
	                <form:errors path="email" cssClass="help-block"/>
	            </div>
	            
	            <div class="col-sm-6 form-group">
	                <appfuse:label styleClass="control-label" key="user.phoneNumber"/>
	                <input type="tel" class="form-control intl-tel-input" value="${user.phoneNumber}" name="number-tel-autista-pre"
	                	${fn:contains(user.roles, 'ROLE_ADMIN') ? 'placeholder="utente admin" readonly':''} id="phoneId">
	                <form:hidden path="phoneNumber" id="numberTelAutistaId"/>
	                <span id="valid-msg" class="hide text-success">Valid</span>
					<span id="error-msg" class="hide text-danger">Invalid number</span>
	            </div>
	        </div>
	        
	        <div class="form-group row">
				<label for="noteID" class="col-sm-2 form-control-label">note utente/cliente</label>
				<div class="col-sm-12">
					<form:textarea path="note" cssClass="form-control" id="noteID" rows="3"/>
				</div>
			</div>

	        <%@ include file="/common/userForm_AddressFatturazione.jsp"%>
	        
	        <!-- ROBA DI AMMINISTRATORE -->
			<c:choose>
			    <c:when test="${param.from == 'list' or param.method == 'Add'}">
			    
			    	<c:if test="${pageContext.request.isUserInRole('ROLE_ADMIN')}">
				        <div class="form-group">
				            <label class="control-label"><fmt:message key="userProfile.accountSetting"/></label>
				            <label class="checkbox-inline">
				                <form:checkbox path="enabled" id="enabled"/>
				                <fmt:message key="user.enabled"/>
				            </label>
				
				            <label class="checkbox-inline">
				                <form:checkbox path="accountExpired" id="accountExpired"/>
				                <fmt:message key="user.accountExpired"/>
				            </label>
				
				            <label class="checkbox-inline">
				                <form:checkbox path="accountLocked" id="accountLocked"/>
				                <fmt:message key="user.accountLocked"/>
				            </label>
				
				            <label class="checkbox-inline">
				                <form:checkbox path="credentialsExpired" id="credentialsExpired"/>
				                <fmt:message key="user.credentialsExpired"/>
				            </label>
				        </div>
					
					
				        <!-- ROLE -->
				        <div class="form-group">
				            <label for="userRoles" class="control-label"><fmt:message key="userProfile.assignRoles"/></label>
				            <select id="userRoles" name="userRoles" multiple="true" class="form-control">
				                <c:forEach items="${availableRoles}" var="role">
				                <option value="${role.value}" ${fn:contains(user.roles, role.value) ? 'selected' : ''}>${role.label}</option>
				                </c:forEach>
				            </select>
				        </div>
				        
				        <!-- TIPO RUOLI -->
			        	<div class="form-group">
			            	<label for="userTipoRuoli" class="control-label"><fmt:message key="userProfile.assignTipoRuoli"/></label>
			            	<c:forEach items="${availableTipoRuoli}" var="tipoRuoli">
			            	<label class="checkbox-inline">
			            		<input type="checkbox" name="userTipoRuoliCheck" value="${tipoRuoli.value}"
			                		${fn:contains(user.tipoRuoli, tipoRuoli.value) ? 'checked' : ''}>${tipoRuoli.label}</label>
							</c:forEach>
			        	</div>
			        </c:if>
			        
			        
			        
			    </c:when>
			    <c:when test="${not empty user.username}">
			        <div class="form-group">
			            <label class="control-label"><fmt:message key="user.roles"/>:</label> 
			            <div class="readonly">
			                <c:forEach var="role" items="${user.roleList}" varStatus="status">
			                    <c:out value="${role.label}"/><c:if test="${!status.last}">,</c:if>
			                    <input type="hidden" name="userRoles" value="<c:out value="${role.label}"/>"/>
			                </c:forEach>
			            </div>
			            
			            
			            <label class="control-label"><fmt:message key="userProfile.assignTipoRuoli"/>:</label> 
			            <div class="readonly">
			                <c:forEach var="role" items="${user.roleList}" varStatus="status">
			                    <c:out value="${role.label}"/><c:if test="${!status.last}">,</c:if>
			                    <input type="hidden" name="userRoles" value="<c:out value="${role.label}"/>"/>
			                </c:forEach>
			                <c:forEach var="tipoRuoli" items="${user.tipoRuoliList}" varStatus="status">
			                    <c:out value="${tipoRuoli.label}"/><c:if test="${!status.last}">,</c:if>
			                    <input type="hidden" name="userTipoRuoliCheck" value="<c:out value="${tipoRuoli.label}"/>"/>
			                </c:forEach>
			            </div>
			            <form:hidden path="enabled"/>
			            <form:hidden path="accountExpired"/>
			            <form:hidden path="accountLocked"/>
			            <form:hidden path="credentialsExpired"/>
			        </div>
			    </c:when>
			</c:choose>
	
	
	
	        <div class="form-group">
	            <c:if test="${param.from == 'list' and param.method != 'Add'}">
	              <button type="button" class="btn btn-default alertConfirmGenerale" name="delete">
	                  <span class="glyphicon glyphicon-trash"></span> <fmt:message key="button.delete"/>
	              </button>
	            </c:if>
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
