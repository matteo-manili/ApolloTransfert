<%@ include file="/common/taglibs.jsp" %>
<head>
<title><fmt:message key="login.title"/></title>
<meta name="description" content="Accedi a <fmt:message key="webapp.apollotransfert.name"/>">
<link rel="canonical" href="<fmt:message key="https.w3.domain.apollotransfert.name"/><c:url value='/login'/>"/>
<script src="<c:url value="/scripts/vendor/jquery-ui-1.11.4/external/jquery/jquery-2.2.4.min.js"/>"></script>
<script src="<c:url value="/js/bootstrap.min.js"/>"></script>
</head>
<body>
<!-- grafica nuova 
<div class="page-banner" style="">
	<h1><fmt:message key="login.heading"/></h1>
</div> -->
<div class="content-area home-content">
<div class="container">
<div class="col-md-12">
	<%@ include file="/common/messages.jsp" %>

	<h2><fmt:message key="login.heading"/></h2>
	<form method="post" id="loginForm" action="<c:url value='/j_security_check'/>" onsubmit="saveUsername(this); /*return validateForm(this)*/ " class="" autocomplete="off">
	    <div class="  ">
			<c:if test="${param.error != null}">
			    <div class="alert alert-danger alert-dismissable">
			        <fmt:message key="errors.password.mismatch"/>
			    </div>
			</c:if>
		</div>
		<div class=" form-group ">
			<label for="exampleInputEmail1">Inserisci Email o Numero di Telefono</label>
			<input type="text" name="j_username" id="j_username" class="form-control" placeholder="Email o Numero di Telefono" tabindex="1" required autofocus>
		</div>
		<div class=" form-group ">
			<label for="exampleInputEmail1">Inserisci Password</label>
			<input type="password" class="form-control" name="j_password" id="j_password" autocomplete="on" tabindex="2" placeholder="<fmt:message key="label.password"/>" 
				required>
		</div>
	    <div class="row form-group">
		    <div class="form-group col-sm-2 hidden">
				<label class="">
				<c:if test="${appConfig['rememberMeEnabled']}">
					<fmt:message key="login.rememberMe"/>
	                <input type="checkbox" class="form-control" name="_spring_security_remember_me" id="rememberMe" tabindex="3"/>
	            </c:if>	
	            </label>
	        </div>
	        <div class="  col-xs-5 col-sm-5 col-md-5 col-lg-5">
				<strong><a rel="nofollow" href="<c:url value='/'/>recoverpass"><fmt:message key="login.forgotPassword"/></a></strong>
	           </div>
	           <div class="  col-xs-7 col-sm-7 col-md-7 col-lg-7">
	           	<strong><fmt:message key="login.signup"><fmt:param><c:url value="/signup"/></fmt:param></fmt:message></strong>
	           </div>
		</div>
		<div class="center-block form-group  ">
			<button class="center-block btn btn-primary " type="submit" name="login" tabindex="4"><fmt:message key='button.login'/></button>
			</div>
		</form>
	</div>
</div>
</div>
<c:set var="scripts" scope="request">
	<%@ include file="/scripts/login.js"%>
</c:set>
<%@ include file="/scripts/ConfirmSubmit.jsp"%>
</body>