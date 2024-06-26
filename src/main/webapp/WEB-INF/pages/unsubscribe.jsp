<%@ include file="/common/taglibs.jsp"%>
<head>
<title>Unsubscribe</title>
<meta name="description" content="Unsubscribe">
<link rel="canonical" href="<fmt:message key="https.w3.domain.apollotransfert.name"/><c:url value='/unsubscribe'/>"/>
<!-- recaptcha invisibile -->
<script src="https://www.google.com/recaptcha/api.js?hl=<fmt:message key="language.code"/>"></script>
<!-- jquery -->
<script src="<c:url value="/scripts/vendor/jquery-ui-1.11.4/external/jquery/jquery-2.2.4.min.js"/>"></script>
</head>
<body>

<div class="content-area home-content">
<div class="container">
<div class="col-md-12">

	<%@ include file="/common/messages.jsp" %>

	<form method="post" action="<c:url value='/unsubscribe'/>" id="unsubscribeFormId" class="">
		<div class="g-recaptcha" data-sitekey="${RECAPTCHA_PUBLIC_GLOBAL}" data-size="invisible" data-callback="onSubmit"></div>
		
		<c:if test="${not empty email}">
			<input type="hidden" name="email" value="${email}">
				<div class="well well-lg">
   					<h2>Annulla Iscrizione</h2>
   					<p>Non vuoi pi&ugrave; ricevere email all'indirizzo <strong>${email}</strong> da <strong><fmt:message key="w3.domain.apollotransfert.name"/></strong>?</p>
				</div>
				<div class="col-sm-12 ">
					<div class="row">
						<button type="submit" name="continua-iscrizione"
							class="btn btn-md btn-success tipoSubmit">Voglio continuare Ricevere Email</button>
						<button type="submit" name="annulla-iscrizione"
							class="btn btn-md btn-primary tipoSubmit">Non voglio pi&ugrave; Ricevere Email</button>
					</div>
				</div>
		</c:if>
	</form>
	
</div>
</div>
</div>

<script type="text/javascript">
var RecaptchaOptions = {
	lang : '<fmt:message key="language.code"/>' // Unavailable while writing this code (just for audio challenge)
};

var buttonName;

$(".tipoSubmit").click(function(){
	//alert(this.name);
	buttonName = this.name;
	});
	
$('#unsubscribeFormId').submit(function (event) {
	//alert(111);
    event.preventDefault();
    //grecaptcha.reset(); //per aggiornare la recaptcha
    grecaptcha.execute();
  });
  
function onSubmit(token) {
	//alert(222);
	//alert(buttonName);
	//$("#attesaLoader").modal('show');
	$(".attesaLoaderPopUp").fadeIn(300);
	var form = document.getElementById('unsubscribeFormId');
	$('<input>').attr({type:'hidden', id:'foo',name:buttonName}).appendTo(form);
	document.getElementById("unsubscribeFormId").submit();
}


</script>

<%@ include file="/scripts/ConfirmSubmit.jsp"%>
<!-- bootstrap JS -->
<script async src="<c:url value="/js/bootstrap.min.js"/>"></script>
</body>