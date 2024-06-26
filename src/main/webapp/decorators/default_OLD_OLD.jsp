<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html lang="${language}">
<head>
    <meta charset="utf-8">
    <title><decorator:title/> | <fmt:message key="webapp.apollotransfert.name"/></title>
	<meta name="robots" content="index, follow">
    <meta name="keywords" content="transfer, booking, taxi, aeroporto, italia">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="shortcut icon" href="<c:url value='/images/favicon.png'/>">
	<meta name="google-site-verification" content="7a6mL_30IP4IIeHkPsed-sk-klSNvFIYNIoTOr8EqRI" />
	<meta name="msvalidate.01" content="E95ADFF454F4AFFCA5AD17D4BEAF4CD0" />

	<script>
	// Google Tag Manager 
	(function(w,d,s,l,i){w[l]=w[l]||[];w[l].push({'gtm.start':
	new Date().getTime(),event:'gtm.js'});var f=d.getElementsByTagName(s)[0],
	j=d.createElement(s),dl=l!='dataLayer'?'&l='+l:'';j.async=true;j.src=
	'https://www.googletagmanager.com/gtm.js?id='+i+dl;f.parentNode.insertBefore(j,f);
	})(window,document,'script','dataLayer','GTM-PD6WDV2');
	</script>
	
	<t:assets type="css"/>
	<decorator:head/>
	
	<!--Start of Zendesk Chat Script (inserisce in tutte la pagine la chat)-->
	<script type="text/javascript">
	/*
	window.$zopim||(function(d,s){var z=$zopim=function(c){z._.push(c)},$=z.s=
	d.createElement(s),e=d.getElementsByTagName(s)[0];z.set=function(o){z.set.
	_.push(o)};z._=[];z.set._=[];$.async=!0;$.setAttribute("charset","utf-8");
	$.src="https://v2.zopim.com/?52tWCAwPwpOCvj97k1rzSnah2eNb7eie";z.t=+new Date;$.
	type="text/javascript";e.parentNode.insertBefore($,e)})(document,"script");
	*/
	</script>
	<!--End of Zendesk Chat Script-->
	</head>

<body itemscope itemtype="https://schema.org/WebPage">
	<!-- Google Tag Manager (noscript) -->
	<noscript><iframe src="https://www.googletagmanager.com/ns.html?id=GTM-PD6WDV2"
	height="0" width="0" style="display:none;visibility:hidden"></iframe></noscript>
	<!-- End Google Tag Manager (noscript) -->
	<script>
	(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
	(i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
	m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
	})(window,document,'script','https://www.google-analytics.com/analytics.js','ga');
	ga('create', 'UA-17284703-5', 'auto');
	ga('send', 'pageview');
	//ga('set', 'userId', {{${user.username}}}); // NON FUNZIONA - Imposta l'ID utente utilizzando l'ID utente della persona che ha eseguito l'accesso.
	</script>
	<c:set var="currentMenu" scope="request"><decorator:getProperty property="meta.menu"/></c:set>
	<nav class="navbar navbar-default" itemscope itemtype="https://schema.org/SiteNavigationElement">
		<div class="container-fluid">
	    	<div class="navbar-header">
				<a class="navbar-brand" href="<c:url value='/'/>">RICERCA TRANSFER&nbsp;<i class="fa fa-car"></i></a>
		       <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#myNavbar" aria-expanded="false"> 
					<span class="sr-only">Toggle navigation</span>
			        <span class="lang-sm lang" lang="${language}"></span>
			        <span class="glyphicon glyphicon-user"></span>
	      		</button>
			</div>
			<%@ include file="/common/menu.jsp" %>
		</div>
	</nav>
	<!-- Attenzione funziona con jquery quindi nelle pagine in cui non c'è la lib jquery questo loadCookieCorsaParticolare.js non funziona 
	non lo uso più perke non faccio più le corse particoari
	<script>var ctx = "${ctx}"</script>
	<script src="<c:url value="/scripts/loadCookieCorsaParticolare.js"/>"></script> -->
	<!-- BODY PAGE -->
    <div class=" content-inside">
    	<div class=""><!-- provare a togliere container, si allargano tutte le pagine -->
			<decorator:body/>
		</div>
	</div>
	<!-- FOOTER PAGE -->
	<footer>
	<div class="container">
	<ul class="text-center list-inline" itemscope itemtype="http://schema.org/Organization">
       	<li><i class="fa fa-info-circle pr-10"></i> <span itemprop="legalName"><fmt:message key="denominazione.ditta.apollotransfert"/></span><small> p.iva</small> <span itemprop="vatID"><fmt:message key="partita.iva"/></span></li>
       	<li><span itemprop="address" itemscope itemtype="https://schema.org/PostalAddress"><i class="fa fa-home pr-10"></i> <span itemprop="streetAddress"><fmt:message key="indirizzo.sede"/></span></span></li>
       	<li><a class="btn btn-xs btn-primary" href="tel:<fmt:message key="cellulare.matteo"/>" >Telefono <span class="glyphicon glyphicon-phone"></span> <span itemprop="telephone"><fmt:message key="cellulare.matteo.esteso"/></span></a></li>
       	<li><i class="fa fa-envelope pr-10"></i> <a href="mailto:<fmt:message key="email.info.apollotransfert"/>" target="_blank"><span itemprop="email"><fmt:message key="email.info.apollotransfert"/></span></a></li>
		<li><i class="fa fa-user pr-10"></i> <a href="<c:url value='/'/>collaboratori"><fmt:message key="menu.lavora.con.noi"/></a></li>
	</ul>
	</div>
	</footer>
	<t:assets type="js"/>
	<%= (request.getAttribute("scripts") != null) ?  request.getAttribute("scripts") : "" %>
</body>
</html>