<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html lang="<fmt:message key="language.code"/>">
<head>
<!-- Google Tag Manager -->
<script>(function(w,d,s,l,i){w[l]=w[l]||[];w[l].push({'gtm.start':
new Date().getTime(),event:'gtm.js'});var f=d.getElementsByTagName(s)[0],
j=d.createElement(s),dl=l!='dataLayer'?'&l='+l:'';j.async=true;j.src=
'https://www.googletagmanager.com/gtm.js?id='+i+dl;f.parentNode.insertBefore(j,f);
})(window,document,'script','dataLayer','GTM-PD6WDV2');</script>
<!-- End Google Tag Manager -->
<!-- Global site tag (gtag.js) - Google Ads: 1011317208 -->
<script src="https://www.googletagmanager.com/gtag/js?id=AW-1011317208" async></script>
<script>
window.dataLayer = window.dataLayer || [];
function gtag(){dataLayer.push(arguments);}
gtag('js', new Date());
gtag('config', 'AW-1011317208');
</script>
<!-- Facebook Pixel Code -->
<script>
!function(f,b,e,v,n,t,s)
{if(f.fbq)return;n=f.fbq=function(){n.callMethod?
n.callMethod.apply(n,arguments):n.queue.push(arguments)};
if(!f._fbq)f._fbq=n;n.push=n;n.loaded=!0;n.version='2.0';
n.queue=[];t=b.createElement(e);t.async=!0;
t.src=v;s=b.getElementsByTagName(e)[0];
s.parentNode.insertBefore(t,s)}(window, document,'script',
'https://connect.facebook.net/en_US/fbevents.js');
fbq('init', '669241703502668');
fbq('track', 'PageView');
</script>
<noscript><img height="1" width="1" style="display:none" src="https://www.facebook.com/tr?id=669241703502668&ev=PageView&noscript=1"/></noscript>
<!-- End Facebook Pixel Code -->
<meta charset="utf-8">
<title><decorator:title/> | <fmt:message key="webapp.apollotransfert.name"/></title>
<meta name="robots" content="index, follow">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="google-site-verification" content="7a6mL_30IP4IIeHkPsed-sk-klSNvFIYNIoTOr8EqRI" />
<meta name="msvalidate.01" content="E95ADFF454F4AFFCA5AD17D4BEAF4CD0" />
<!-- default image facebook -->
<meta property="og:image" content="https://www.apollotransfert.com/nuova_grafica/images/main-banner.jpg">
<meta property="og:image:type" content="image/jpg">
<meta property="og:image:width" content="1440">
<meta property="og:image:height" content="1596">
<meta property="og:url" content="<fmt:message key="https.w3.domain.apollotransfert.name"/>">
<meta property="og:type" content="website">
<meta property="og:title" content="<fmt:message key="w3.domain.apollotransfert.name"/>">
<meta property="og:description" content="<fmt:message key="index.mess.title"/>">
<meta property="fb:app_id" content="669241703502668">

<!-- favicon -->
<link rel="apple-touch-icon" sizes="180x180" href="<c:url value='/images/favicon/apple-touch-icon.png'/>">
<link rel="icon" type="image/png" sizes="32x32" href="<c:url value='/images/favicon/favicon-32x32.png'/>">
<link rel="icon" type="image/png" sizes="16x16" href="<c:url value='/images/favicon/favicon-16x16.png'/>">
<link rel="manifest" href="<c:url value='/images/favicon/site.webmanifest'/>">
<link rel="mask-icon" href="<c:url value='/images/favicon/safari-pinned-tab.svg'/>" color="#5bbad5">
<meta name="msapplication-TileColor" content="#da532c">
<meta name="theme-color" content="#191919">
<%@ include file="/scripts/decorators_default_dati_strutturali.jsp"%>

<!-- 
<t:assets type="css"/>  --> 

<link rel="stylesheet" type="text/css" href="<c:url value='/nuova_grafica/'/>css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="<c:url value='/nuova_grafica/'/>style.css">
<link rel="stylesheet" type="text/css" href="<c:url value='/nuova_grafica/'/>custom.css">
<link rel="stylesheet" type="text/css" href="https://use.fontawesome.com/releases/v5.0.9/css/all.css?integrity=sha384-5SOiIsAziJl6AWe0HWRKTXlfcSHKmYV4RBF18PPJ173Kzn7jzMyFuTtk8JA7QQG1&crossorigin=anonymous">
<link rel="stylesheet" type="text/css" href="<c:url value='/nuova_grafica/'/>css/font-awesome.min.css">
<link rel="stylesheet" type="text/css" href="<c:url value='/nuova_grafica/'/>css/wickedpicker.css">
<link rel="stylesheet" href="<c:url value='/nuova_grafica/flags/'/>flags.css">

<decorator:head/>
<script type="text/javascript" src="<c:url value='/scripts'/>/iubenda_banner.js" async></script>
</head>
<body>
<!-- Google Tag Manager (noscript) -->
<noscript><iframe src="https://www.googletagmanager.com/ns.html?id=GTM-PD6WDV2" height="0" width="0" style="display:none;visibility:hidden"></iframe></noscript>
<!-- End Google Tag Manager (noscript) -->

<nav class="navbar navbar-default ">
	<div class="container">
	<div class="navbar-header">
		<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
		<span class="sr-only">Toggle navigation</span>
		<span class="icon-bar"></span>
		<span class="icon-bar"></span>
		<span class="icon-bar"></span>
		</button>
		<!-- <a class="navbar-brand" href="#">Project name</a>  -->
		<a class="navbar-brand" href="<c:url value='/'/><fmt:message key="language.code"/>"><img src="<c:url value='/nuova_grafica/'/>images/logo.png" class="img-responsive" alt="<fmt:message key="webapp.apollotransfert.name"/>" title="<fmt:message key="webapp.apollotransfert.name"/>"></a>
	</div>
	<div id="navbar" class="navbar-collapse collapse">
		<ul class="nav navbar-nav "> <!-- contenuti primari -->
			<c:if test="${pageContext.request.isUserInRole('ROLE_ADMIN')}">
				<li class="dropdown">
				<a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false"><fmt:message key="menu.admin"/><span class="caret"></span></a>
				<ul class="dropdown-menu">
				<li><a href="<c:url value='/admin/gestioneAutista'/>"><fmt:message key="menu.gest.autisti"/></a></li>
	        	<li><a href="<c:url value='/admin/admin-gestioneCorse'/>"><fmt:message key="menu.gest.corse"/></a></li>
	            <li><a href="<c:url value='/admin/admin-tableRicercheTransfert'/>"><fmt:message key="menu.ricerche.transfert"/></a></li>
	           	<li role="separator" class="divider"></li>
	            <li><a href="<c:url value='/admin/users'/>"><fmt:message key="menu.admin.users"/></a></li>
	            <li><a href="<c:url value='/admin/activeUsers'/>"><fmt:message key="home.activeUsers"/></a></li>
	            <li><a href="<c:url value='/admin/admin-tableVisitatori'/>"><fmt:message key="menu.visitatori"/></a></li>
	            <li><a href="<c:url value='/admin/admin-chatZendesk'/>"><fmt:message key="menu.chat.zendesk"/></a></li>
	            <li role="separator" class="divider"></li>
	            <li><a href="<c:url value='/contatti-marketing'/>"><fmt:message key="menu.tabelle.contatti.marketing"/></a></li>
	            <li><a href="<c:url value='/admin/admin-tableAeroporti'/>"><fmt:message key="menu.tabelle.aeroporti"/></a></li>
	            <li><a href="<c:url value='/admin/admin-tablePortiNavali'/>"><fmt:message key="menu.tabelle.portiNavali"/></a></li>
	            <li><a href="<c:url value='/admin/admin-tableMusei'/>"><fmt:message key="menu.tabelle.musei"/></a></li>
	            <li><a href="<c:url value='/admin/admin-tableComuni'/>"><fmt:message key="menu.tabelle.comuni"/></a></li>
	            <li><a href="<c:url value='/admin/admin-tableProvince'/>"><fmt:message key="menu.tabelle.province"/></a></li>
	            <li><a href="<c:url value='/admin/admin-tableModelloAutoveicolo'/>"><fmt:message key="menu.tabelle.modello.autoveicolo"/></a></li>
	            <li role="separator" class="divider"></li>
	            <li><a href="<c:url value='/admin/admin-tableGestioneApplicazione'/>"><fmt:message key="menu.tabelle.gestioneApplicazione"/></a></li>
	            <li role="separator" class="divider"></li>
				<li><a href="<c:url value='/prove'/>"><fmt:message key="menu.prove"/></a></li>
				</ul>
				</li>
			</c:if>
			<c:choose>
			<c:when test="${empty pageContext.request.remoteUser}">
				<li><a href="<c:url value='/'/><fmt:message key="language.code"/>/chi-siamo"><fmt:message key="menu.chi.siamo"/></a></li>
				<!-- <li><a href="<c:url value='/faq'/><fmt:message key="language.code"/>/faq"><fmt:message key="menu.faq"/></a></li> Attendere il testo prima di metterla online -->
				<li><a href="<c:url value='/'/><fmt:message key="language.code"/>/contatti"><fmt:message key="menu.contatti"/></a></li>
				<li><a href="<c:url value='/'/><fmt:message key="language.code"/>/tariffe-transfer"><fmt:message key="menu.tariffe"/></a></li>
				<li><a href="<c:url value='/'/><fmt:message key="language.code"/>/collaboratori"><fmt:message key="menu.lavora.con.noi.autista"/></a></li>
				<li><a href="<c:url value='/'/><fmt:message key="language.code"/>/ncc-agenzie-viaggio"><fmt:message key="menu.ncc.agenzie.viaggio"/></a></li>
				<li><a href="<c:url value='/'/><fmt:message key="language.code"/>/ncc-aziende"><fmt:message key="menu.ncc.aziende"/></a></li>
				<li><a href="<c:url value='/consigli-di-viaggio'/>"><fmt:message key="menu.consigli.di.viaggio"/></a></li>
				<li class="dropdown">
				<a href="#" class="dropdown-toggle" data-toggle="dropdown">
				<span class="flag flag-<fmt:message key="header.menu.flag.country"/>"></span>&nbsp;<fmt:message key="header.menu.language.name"/>&nbsp;<span class="caret"></span></a>
				<ul class="dropdown-menu">
					<li><a href="<c:url value='/it'/>"><span class="flag flag-<fmt:message key="header.menu.select.flag.italia"/>"></span>&nbsp;<fmt:message key="header.menu.select.lang.italia"/></a></li>
					<li><a href="<c:url value='/en'/>"><span class="flag flag-<fmt:message key="header.menu.select.flag.inglese"/>"></span>&nbsp;<fmt:message key="header.menu.select.lang.inglese"/></a></li>
					<li><a href="<c:url value='/es'/>"><span class="flag flag-<fmt:message key="header.menu.select.flag.spagnolo"/>"></span>&nbsp;<fmt:message key="header.menu.select.lang.spagnolo"/></a></li>
				</ul>
				</li>
			</c:when>
			<c:when test="${not empty pageContext.request.remoteUser}">
				<li class="dropdown">
				<a href="#" class="dropdown-toggle" data-toggle="dropdown">Menu<span class="caret"></span></a>
				<ul class="dropdown-menu">
					<li><a href="<c:url value='/'/><fmt:message key="language.code"/>/chi-siamo"><fmt:message key="menu.chi.siamo"/></a></li>
					<!-- <li><a href="<c:url value='/faq'/><fmt:message key="language.code"/>/faq"><fmt:message key="menu.faq"/></a></li> Attendere il testo prima di metterla online -->
					<li><a href="<c:url value='/'/><fmt:message key="language.code"/>/contatti"><fmt:message key="menu.contatti"/></a></li>
					<li><a href="<c:url value='/'/><fmt:message key="language.code"/>/tariffe-transfer"><fmt:message key="menu.tariffe"/></a></li>
					<li><a href="<c:url value='/'/><fmt:message key="language.code"/>/collaboratori"><fmt:message key="menu.lavora.con.noi.autista"/></a></li>
					<li><a href="<c:url value='/'/><fmt:message key="language.code"/>/ncc-agenzie-viaggio"><fmt:message key="menu.ncc.agenzie.viaggio"/></a></li>
					<li><a href="<c:url value='/'/><fmt:message key="language.code"/>/ncc-aziende"><fmt:message key="menu.ncc.aziende"/></a></li>
					<li><a href="<c:url value='/consigli-di-viaggio'/>"><fmt:message key="menu.consigli.di.viaggio"/></a></li>
				</ul>
				</li>
			</c:when>
			</c:choose>
			<c:if test="${not empty pageContext.request.remoteUser}">
				<li><a href="<c:url value='/'/><fmt:message key="language.code"/>/home-user"><fmt:message key="home.title"/></a></li>
				<li><a href="<c:url value='/'/><fmt:message key="language.code"/>/userform"><fmt:message key="menu.user"/></a></li>
				<li class="dropdown">
				<a href="#" class="dropdown-toggle" data-toggle="dropdown">
				<span class="flag flag-<fmt:message key="header.menu.flag.country"/>"></span>&nbsp;<fmt:message key="header.menu.language.name"/>&nbsp;<span class="caret"></span></a>
				<ul class="dropdown-menu">
					<li><a href="<c:url value='/it'/>"><span class="flag flag-<fmt:message key="header.menu.select.flag.italia"/>"></span>&nbsp;<fmt:message key="header.menu.select.lang.italia"/></a></li>
					<li><a href="<c:url value='/en'/>"><span class="flag flag-<fmt:message key="header.menu.select.flag.inglese"/>"></span>&nbsp;<fmt:message key="header.menu.select.lang.inglese"/></a></li>
					<li><a href="<c:url value='/es'/>"><span class="flag flag-<fmt:message key="header.menu.select.flag.spagnolo"/>"></span>&nbsp;<fmt:message key="header.menu.select.lang.spagnolo"/></a></li>
				</ul>
				</li>
			</c:if>
		</ul>
		<ul class="nav navbar-nav navbar-right"> <!-- contenuti secondari -->
			<c:choose>
			<c:when test="${empty pageContext.request.remoteUser}">
				<li id="menuItemRoot"><a href="<c:url value='/'/><fmt:message key="language.code"/>"><i class="fa fa-car"></i> <fmt:message key="home.ricerca"/></a></li>
				<li><a href="<c:url value='/'/><fmt:message key="language.code"/>/login"><fmt:message key="menu.login"/></a></li>
				<li><a href="<c:url value='/'/><fmt:message key="language.code"/>/signup"><fmt:message key="menu.registrati"/></a></li>
			</c:when>
			<c:when test="${not empty pageContext.request.remoteUser}">
				<li id="menuItemRoot"><a  href="<c:url value='/'/><fmt:message key="language.code"/>"><i class="fa fa-car"></i> <fmt:message key="home.ricerca"/></a></li>
				<li><a href="<c:url value='/logout'/>"><fmt:message key="user.logout"/></a></li>
			</c:when>
			</c:choose>
		</ul>
	</div><!--/.nav-collapse -->
	</div>
</nav>
<decorator:body/>
<!-- FOOTER PAGE -->
<footer>
	<div class="container">
		<div class="col-md-4">
			<a class="footerlogo" href="<c:url value='/'/><fmt:message key="language.code"/>"><img src="<c:url value='/nuova_grafica/'/>images/flogo.png" class="img-responsive" alt="<fmt:message key="webapp.apollotransfert.name"/>" title="<fmt:message key="webapp.apollotransfert.name"/>"></a>
			<div class="widget">
				<ul class="coninfo">
					<li><i class="fa fa-map-marker"></i><fmt:message key="indirizzo.sede"/></li>
					<li><a href="tel:<fmt:message key="cellulare.matteo"/>"><i class="fa fa-mobile"></i>Tel: <fmt:message key="cellulare.matteo.esteso"/></a></li>
					<li><a href="tel:<fmt:message key="telefono.fisso.sede"/>"><i class="fa fa-phone"></i>Tel: <fmt:message key="telefono.fisso.sede.esteso"/></a></li>
					<li><a href="https://api.whatsapp.com/send?phone=<fmt:message key="cellulare.matteo.noplus"/>" target="_blank"><i class="fa fa-whatsapp"></i>WhatsApp</a></li>
					<li><a href="mailto:<fmt:message key="email.info.apollotransfert"/>"><i class="fa fa-envelope"></i><fmt:message key="email.info.apollotransfert"/></a></li>
					<li><i class="fa fa-info-circle pr-10"></i><span style="text-transform: uppercase;"><fmt:message key="intestatario.ditta"/></span><small> p.iva</small> <fmt:message key="partita.iva"/></li>
				</ul>
			</div>
		</div>
		<div class="col-md-3">
			<div class="widget">
				<h3>Social Media</h3>
				<ul>
					<li><a href="https://www.facebook.com/apollotransfert" target="_blank">Facebook</a></li>
					<li><a href="https://twitter.com/apollotransfert" target="_blank">Twitter</a></li>
					<li><a href="https://www.linkedin.com/company/apollotransfert" target="_blank">Linkedin</a></li>
				</ul>
			</div>
			<div class="widget">
				<h3>Policy</h3>
				<ul>
					<li><a href="<c:url value='/privacy-policy'/>">Privacy Policy</a></li>
					<li><a href="<c:url value='/cookie-policy'/>">Cookie Policy</a></li>
				</ul>
			</div>
		</div>
		<div class="col-md-5">
			<div class="widget">
				<h3>Newsletter</h3><input type="email" id="emailNewsLetter" placeholder="Email"><span id="capcha_111"></span>
				<button id="btnSubmitNewsLetter"><fmt:message key="button.iscriviti"/></button>
				<label class="checkbox-inline "><input type="checkbox" name="checkPrivacyPolicy" id="checkPrivacyPolicyId">
				<fmt:message key="privacy.policy.desc"><fmt:param value="${pageContext.request.contextPath}/privacy-policy"/></fmt:message>
				<div class="help-block with-errors"></div></label>
			</div>
			<div class="widget">
				<script type="text/javascript" src="//widget.trustpilot.com/bootstrap/v5/tp.widget.bootstrap.min.js" async></script>
				<div class="trustpilot-widget" data-locale="it-IT" data-template-id="5419b6a8b0d04a076446a9ad" data-businessunit-id="5ce69298f81cda0001472504" data-style-height="25px" 
				data-style-width="100%" data-theme="dark"><a href="https://it.trustpilot.com/review/www.apollotransfert.com" target="_blank" rel="noopener">Trustpilot</a></div>
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
<%@ include file="/scripts/decorators_default_js.jsp"%>
<t:assets type="js"/>
<%= (request.getAttribute("scripts") != null) ? request.getAttribute("scripts") : "" %>
<script type="text/javascript" src="//cdn.iubenda.com/cs/iubenda_cs.js" charset="UTF-8" async></script>
</body>
</html>