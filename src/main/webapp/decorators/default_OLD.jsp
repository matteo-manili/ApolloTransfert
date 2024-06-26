<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html lang="${language}">
<head>
<!-- Google Tag Manager -->
<script>(function(w,d,s,l,i){w[l]=w[l]||[];w[l].push({'gtm.start':
new Date().getTime(),event:'gtm.js'});var f=d.getElementsByTagName(s)[0],
j=d.createElement(s),dl=l!='dataLayer'?'&l='+l:'';j.async=true;j.src=
'https://www.googletagmanager.com/gtm.js?id='+i+dl;f.parentNode.insertBefore(j,f);
})(window,document,'script','dataLayer','GTM-PD6WDV2');</script>
<!-- End Google Tag Manager -->
<!-- Global site tag (gtag.js) - Google Ads: 1011317208 -->
<script async src="https://www.googletagmanager.com/gtag/js?id=AW-1011317208"></script>
<script>
window.dataLayer = window.dataLayer || [];
function gtag(){dataLayer.push(arguments);}
gtag('js', new Date());
gtag('config', 'AW-1011317208');
</script>
<meta charset="utf-8">
<title><decorator:title/> | <fmt:message key="webapp.apollotransfert.name"/></title>
<meta name="robots" content="index, follow">
<meta name="keywords" content="transfer, booking, taxi, aeroporto, italia">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="google-site-verification" content="7a6mL_30IP4IIeHkPsed-sk-klSNvFIYNIoTOr8EqRI" />
<meta name="msvalidate.01" content="E95ADFF454F4AFFCA5AD17D4BEAF4CD0" />
<!-- favicon -->
<link rel="apple-touch-icon" sizes="180x180" href="<c:url value='/images/favicon/apple-touch-icon.png'/>">
<link rel="icon" type="image/png" sizes="32x32" href="<c:url value='/images/favicon/favicon-32x32.png'/>">
<link rel="icon" type="image/png" sizes="16x16" href="<c:url value='/images/favicon/favicon-16x16.png'/>">
<link rel="manifest" href="<c:url value='/images/favicon/site.webmanifest'/>">
<link rel="mask-icon" href="<c:url value='/images/favicon/safari-pinned-tab.svg'/>" color="#5bbad5">
<meta name="msapplication-TileColor" content="#da532c">
<meta name="theme-color" content="#191919">

  <!--<t:assets type="css"/> -->

<link rel="stylesheet" type="text/css" href="<c:url value='/nuova_grafica/'/>css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="<c:url value='/nuova_grafica/'/>style.css">
<link rel="stylesheet" type="text/css" href="<c:url value='/nuova_grafica/'/>custom.css">
<link rel="stylesheet" type="text/css" href="https://use.fontawesome.com/releases/v5.0.9/css/all.css?integrity=sha384-5SOiIsAziJl6AWe0HWRKTXlfcSHKmYV4RBF18PPJ173Kzn7jzMyFuTtk8JA7QQG1&crossorigin=anonymous">
<link rel="stylesheet" type="text/css" href="<c:url value='/nuova_grafica/'/>css/font-awesome.min.css">
<link rel="stylesheet" type="text/css" href="<c:url value='/nuova_grafica/'/>css/wickedpicker.css">

<decorator:head/>
<script type="text/javascript">
var _iub = _iub || [];
_iub.csConfiguration = {"whitelabel":false,"lang":"it","siteId":1904909,"cookiePolicyId":95138514,"cookiePolicyUrl":"https://apollotransfert.com/cookie-policy", "banner":{ "acceptButtonDisplay":true,"customizeButtonDisplay":true,"position":"float-top-center","acceptButtonColor":"#0073CE","acceptButtonCaptionColor":"white","customizeButtonColor":"#DADADA","customizeButtonCaptionColor":"#4D4D4D","rejectButtonColor":"#0073CE","rejectButtonCaptionColor":"white","textColor":"black","backgroundColor":"white","rejectButtonDisplay":true }};
</script>
</head>
<body itemscope itemtype="https://schema.org/WebPage">
<!-- Google Tag Manager (noscript) -->
<noscript><iframe src="https://www.googletagmanager.com/ns.html?id=GTM-PD6WDV2" height="0" width="0" style="display:none;visibility:hidden"></iframe></noscript>
<!-- End Google Tag Manager (noscript) -->
<header>
<div class="container">
	<div class="col-md-4 logo">
		<a class="navbar-brand" href="<c:url value='/'/>"><img src="<c:url value='/nuova_grafica/'/>images/logo.png" class="img-responsive" alt="<fmt:message key="webapp.apollotransfert.name"/>" 
			title="<fmt:message key="webapp.apollotransfert.name"/>"></a>
	</div>
	<div class="col-md-8 text-right">
		<div class="topnav">
			<!-- <select id="language" name="language" style="font-size:16px;">
			<option value="it">ITA</option><option value="en">ENG</option></select> -->
			<c:choose>
			<c:when test="${empty pageContext.request.remoteUser}">
				<ul class="logindiv" style="font-size:16px;">
				<li><a href="<c:url value='/login'/>"><fmt:message key="menu.login"/></a></li>
				<li><a href="<c:url value='/signup'/>"><fmt:message key="menu.registrati"/></a></li>
				<li><a href="<c:url value='/chi-siamo'/>"><fmt:message key="menu.chi.siamo"/></a></li>
				<!-- <li><a href="<c:url value='/faq'/>"><fmt:message key="menu.faq"/></a></li> Attendere il testo prima di metterla online -->
				<li><a href="<c:url value='/contatti'/>"><fmt:message key="menu.contatti"/></a></li>
				</ul>
			</c:when>
			<c:when test="${not empty user.firstName && not empty user.lastName}">
				<ul class="logindiv" style="font-size:16px;">
				<li><a href="<c:url value='/userform'/>"><fmt:message key="home.heading"/>, ${user.fullName}</a></li>
				<li><a href="<c:url value='/chi-siamo'/>"><fmt:message key="menu.chi.siamo"/></a></li>
				<!-- <li><a href="<c:url value='/faq'/>"><fmt:message key="menu.faq"/></a></li> Attendere il testo prima di metterla online -->
				<li><a href="<c:url value='/contatti'/>"><fmt:message key="menu.contatti"/></a></li>
				</ul>
			</c:when>
			</c:choose>
		</div>
		<div class="navdiv">
			<button class="navbar-toggle" type="button" data-toggle="collapse" data-target=".navbar-collapse">
			<span class="sr-only">Toggle navigation</span><span class="icon-bar"></span><span class="icon-bar"></span><span class="icon-bar"></span>
			</button>
			<c:set var="currentMenu" scope="request"><decorator:getProperty property="meta.menu"/></c:set>
			<nav class="collapse navbar-collapse" role="navigation">
				<ul class="nav navbar-nav text-right">
					<%@ include file="/common/menu.jsp" %>
					<c:if test="${empty pageContext.request.remoteUser}">
						<li class="hidemenu"><a href="<c:url value='/login'/>"><fmt:message key="menu.login"/></a></li>
						<li class="hidemenu"><a href="<c:url value='/signup'/>"><fmt:message key="menu.registrati"/></a></li>
					</c:if>
				</ul>
			</nav>
		</div>
	</div>
</div>
</header>
<!-- BODY PAGE -->
<decorator:body/>
<!-- FOOTER PAGE -->
<footer>
	<div class="container">
		<div class="col-md-4">
			<a class="footerlogo" href="<c:url value='/'/>"><img src="<c:url value='/nuova_grafica/'/>images/flogo.png" class="img-responsive" alt="Apollo" title="Apollo"></a>
			<div class="widget">
				<ul class="coninfo">
					<li><i class="fa fa-map-marker"></i><span itemprop="address" itemscope itemtype="https://schema.org/PostalAddress"><span itemprop="streetAddress">
						<fmt:message key="indirizzo.sede"/></span></span></li>
					<li><a href="tel:<fmt:message key="cellulare.matteo"/>"><i class="fa fa-mobile"></i>Tel: <span itemprop="telephone"><fmt:message key="cellulare.matteo.esteso"/></span></a></li>
					<li><a href="tel:<fmt:message key="telefono.fisso.sede"/>"><i class="fa fa-phone"></i>Tel: <span itemprop="telephone"><fmt:message key="telefono.fisso.sede.esteso"/></span></a></li>
					<li><a href="https://api.whatsapp.com/send?phone=<fmt:message key="cellulare.matteo.noplus"/>" target="_blank"><i class="fa fa-whatsapp"></i>WhatsApp</a></li>
					<li><a href="mailto:<fmt:message key="email.info.apollotransfert"/>"><i class="fa fa-envelope"></i><span itemprop="email"><fmt:message key="email.info.apollotransfert"/></span></a></li>
					<!-- <li><a href="https://www.apollotransfert.com"><i class="fa fa-globe"></i>Website: www.apollotransfert.com</a></li>  -->
					<li><i class="fa fa-info-circle pr-10"></i><span itemprop="legalName"><span style="text-transform: uppercase;"><fmt:message key="intestatario.ditta"/></span></span><small> p.iva</small> <span itemprop="vatID"><fmt:message key="partita.iva"/></span></li>
				</ul>
			</div>
		</div>
		<div class="col-md-2">
			<div class="widget">
				<h3>Social Media</h3>
				<ul>
					<li><a href="https://www.facebook.com/apollotransfert" target="_blank">Facebook</a></li>
					<li><a href="https://twitter.com/apollotransfert" target="_blank">Twitter</a></li>
					<li><a href="https://www.linkedin.com/company/apollotransfert" target="_blank">Linkedin</a></li>
				</ul>
			</div>
		</div>
		<div class="col-md-2">
			<div class="widget">
				<h3>Policy</h3>
				<ul>
					<li><a href="<c:url value='/privacy-policy'/>">Privacy Policy</a></li>
					<li><a href="<c:url value='/cookie-policy'/>">Cookie Policy</a></li>
				</ul>
			</div>
		</div>
		<div class="col-md-4">
			<div class="widget">
				<h3>Newsletter</h3><input type="email" id="emailNewsLetter" placeholder="Email"><span id="capcha_111"></span>
				<button id="btnSubmitNewsLetter"><fmt:message key="button.iscriviti"/></button>
				<label class="checkbox-inline"><input type="checkbox" name="checkPrivacyPolicy" id="checkPrivacyPolicyId">
				<fmt:message key="privacy.policy.desc"><fmt:param value="${pageContext.request.contextPath}/privacy-policy"/></fmt:message>
				<div class="help-block with-errors"></div></label>
			</div>
		</div>
	</div>
</footer>
<div class="pop-up-container pop-up-email newsLetterPopUp">
	<div class="pop-up">
		<div class="container">	
			<div class="row">
				<div class="col-md-8 col-md-offset-2 text-center">
					<i class="fas fa-times-circle close-popup"></i>
					<div class="pop-bg row">	
						<h4><fmt:message key="webapp.apollotransfert.name"/> - NEWSLETTER</h4>
						<div class="col-md-12 img-loader" >
							<img src="<c:url value='/nuova_grafica/'/>images/loader.gif">
						</div>
						<div class="col-md-6 col-md-offset-3 message-esito" >
							<p><div id="idModalEsitoSubscriptNewsLetterText"></div></p>
						</div>
					</div>	
				</div>
			</div>
		</div>	
	</div>
</div>
<script type="text/javascript">
// serve per velocizzare la pagina al caricamento
$("#btnSubmitNewsLetter").attr("disabled", true); 
setTimeout(function(){
	jQuery.ajax({
		url: 'https://www.google.com/recaptcha/api.js?onload=onloadCallback&render=explicit&hl=${language}',
	    dataType: 'script',
		success: function(result) { 
			$("#btnSubmitNewsLetter").attr("disabled", false); 
		},
		async: true
	});
}, 4000); //4000
var capcha1;
var onloadCallback = function(){
capcha1 = grecaptcha.render("capcha_111",{
  "sitekey": "${RECAPTCHA_PUBLIC_GLOBAL}",
  "badge": "inline",
  "type": "image",
  "size": "invisible",
  "callback": "onSubmitNewsLetter"
	});
};
$('#btnSubmitNewsLetter').click(function( event ) {
	if( $('#checkPrivacyPolicyId').is(":checked") ){
		event.preventDefault(capcha1);
		grecaptcha.execute(capcha1);
	}
});
function onSubmitNewsLetter(token) {
	var grecaptchaResponse = grecaptcha.getResponse(capcha1);
	//alert( grecaptchaResponse ); 
	var emailNewsLetter = $("#emailNewsLetter").val();
	$.ajax({
		type: "POST",
		url: '${pageContext.request.contextPath}/submit-news-letter',
		data: {
			emailNewsLetter: emailNewsLetter,
			captcha: grecaptchaResponse
		},
		beforeSend: function(){
			$("#idModalEsitoSubscriptNewsLetterText").html('');
			$(".img-loader").fadeIn(500);
			$(".newsLetterPopUp").fadeIn(500);
		},
		success: function(result) {
			$(".img-loader").fadeOut(500); //nascondo il loader
			$("#idModalEsitoSubscriptNewsLetterText").html('<div class="text-primary"><strong>'+result['message']+'</strong></div>');
			grecaptcha.reset(capcha1);
		},
		error: function(){
			alert("ERRORE NEWSLETTER");
			grecaptcha.reset(capcha1);
		}
	});
};
</script>
<t:assets type="js"/>
<%= (request.getAttribute("scripts") != null) ? request.getAttribute("scripts") : "" %>
<script type="text/javascript" src="//cdn.iubenda.com/cs/iubenda_cs.js" charset="UTF-8" async></script>
</body>
</html>