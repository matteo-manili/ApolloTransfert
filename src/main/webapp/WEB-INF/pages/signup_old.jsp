<%@ include file="/common/taglibs.jsp" %>

<head>
    <title><fmt:message key="signup.title"/></title>
</head>

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
		
		//setPhone();

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

<body class="signup"/>

<div class="col-sm-2">
    <h2><fmt:message key="signup.heading"/></h2>
    <p><fmt:message key="signup.message"/></p>
</div>
<div class="col-sm-7">
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

    <form:form commandName="user" method="post" action="signup" id="signupForm" autocomplete="off"
               cssClass="well" onsubmit="/*return validateSignup(this)*/">
               
        <spring:bind path="user.username">
        <div class="form-group${(not empty status.errorMessage) ? ' has-error' : ''}">
        </spring:bind>
            <appfuse:label styleClass="control-label" key="user.username"/>
            <form:input cssClass="form-control" path="username" id="username" autofocus="true"/>
            <form:errors path="username" cssClass="help-block"/>
        </div>
        <div class="row">
            <spring:bind path="user.password">
            <div class="col-sm-6 form-group${(not empty status.errorMessage) ? ' has-error' : ''}">
            </spring:bind>
                <appfuse:label styleClass="control-label" key="user.password"/>
                <form:password cssClass="form-control" path="password" id="password" showPassword="true"/>
                <form:errors path="password" cssClass="help-block"/>
            </div>
            <spring:bind path="user.passwordHint">
            <div class="col-sm-6 form-group${(not empty status.errorMessage) ? ' has-error' : ''}">
            </spring:bind>
                <appfuse:label styleClass="control-label" key="user.passwordHint"/>
                <form:input cssClass="form-control" path="passwordHint" id="passwordHint"/>
                <form:errors path="passwordHint" cssClass="help-block"/>
            </div>
        </div>
        <div class="row">
            <spring:bind path="user.firstName">
            <div class="col-sm-6 form-group${(not empty status.errorMessage) ? ' has-error' : ''}">
            </spring:bind>
                <appfuse:label styleClass="control-label" key="user.firstName"/>
                <form:input cssClass="form-control" path="firstName" id="firstName" maxlength="50"/>
                <form:errors path="firstName" cssClass="help-block"/>
            </div>
            <spring:bind path="user.lastName">
            <div class="col-sm-6 form-group${(not empty status.errorMessage) ? ' has-error' : ''}">
            </spring:bind>
                <appfuse:label styleClass="control-label" key="user.lastName"/>
                <form:input cssClass="form-control" path="lastName" id="lastName" maxlength="50"/>
                <form:errors path="lastName" cssClass="help-block"/>
            </div>
        </div>
        <div class="row">
            <spring:bind path="user.email">
            <div class="col-sm-6 form-group${(not empty status.errorMessage) ? ' has-error' : ''}">
            </spring:bind>
                <appfuse:label styleClass="control-label" key="user.email"/>
                <form:input cssClass="form-control" path="email" id="email"/>
                <form:errors path="email" cssClass="help-block"/>
            </div>
            <div class="col-sm-6 form-group">
                <appfuse:label styleClass="control-label" key="user.phoneNumber"/>
                
                
                <input type="tel" class="form-control intl-tel-input"
                	name="number-tel-autista-pre" id="phoneId">
                <input type="hidden" name="number-tel-autista" id="numberTelAutistaId">
					<span id="valid-msg" class="hide text-success">Valid</span>
					<span id="error-msg" class="hide text-danger">Invalid number</span>
                
                
                <!-- 
                <form:input cssClass="form-control" path="phoneNumber" id="phoneNumber"/>  -->
            </div>
        </div>
        <div class="form-group">
            <appfuse:label styleClass="control-label" key="user.website"/>
            <form:input cssClass="form-control" path="website" id="website"/>
        </div>
        
        <%@ include file="/common/userForm_AddressFatturazione.jsp"%>
        
        <div class="form-group">
            <button type="submit" class="btn btn-primary" name="save" onclick="bCancel=false">
                <i class="icon-ok icon-white"></i> <fmt:message key="button.register"/>
            </button>
            <button type="submit" class="btn btn-default" name="cancel" onclick="bCancel=true">
                <i class="icon-remove"></i> <fmt:message key="button.cancel"/>
            </button>
        </div>
    </form:form>
</div>

<c:set var="scripts" scope="request">
<v:javascript formName="signup" staticJavascript="false"/>
<script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"></script>
</c:set>