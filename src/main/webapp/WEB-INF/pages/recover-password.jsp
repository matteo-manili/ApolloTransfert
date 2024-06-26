<%@ include file="/common/taglibs.jsp" %>
<head>
<title><fmt:message key="updatePassword.title"/></title>
<meta name="description" content="Reinvio password">
<link rel="canonical" href="<fmt:message key="https.w3.domain.apollotransfert.name"/><c:url value='/recoverpass'/>"/>
<!-- jquery -->
<link rel="stylesheet" href="<c:url value="/scripts/vendor/jquery-ui-1.11.4/jquery-ui.css"/>">
<script src="<c:url value="/scripts/vendor/jquery-ui-1.11.4/external/jquery/jquery-2.2.4.min.js"/>"></script>
<script src="<c:url value="/js/bootstrap.min.js"/>"></script>
</head>
<body>
<div class="content-area home-content">
<div class="container">
<div class="col-md-12">
	<%@ include file="/common/messages.jsp" %>
	<h2><fmt:message key="login.forgotPassword"/></h2>
	<div class=" ">
		<form method="post" action="<c:url value='/recoverpass'/>" class="">
			<div class="form-group">
		           <label for="inputEmail">Hai dimenticato la password? inserisci Email o Numero di Telefono</label>
		           <input type="text" class="form-control" name="emailPhoneNumber" id="emailPhoneNumber_id" placeholder="Email o Telefono" autofocus>
			</div>
			<button type="submit" class="btn btn-primary attesaLoader center-block">invia password</button>
		</form>
	</div>
</div>
</div>	
</div>

<%@ include file="/scripts/ConfirmSubmit.jsp"%>

</body>