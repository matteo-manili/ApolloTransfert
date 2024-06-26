<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html lang="it">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
<title><decorator:title/></title>
<decorator:head/>
<!-- favicon -->
<link rel="apple-touch-icon" sizes="180x180" href="<c:url value='/images/favicon/apple-touch-icon.png'/>">
<link rel="icon" type="image/png" sizes="32x32" href="<c:url value='/images/favicon/favicon-32x32.png'/>">
<link rel="icon" type="image/png" sizes="16x16" href="<c:url value='/images/favicon/favicon-16x16.png'/>">
<link rel="manifest" href="<c:url value='/images/favicon/site.webmanifest'/>">
<link rel="mask-icon" href="<c:url value='/images/favicon/safari-pinned-tab.svg'/>" color="#5bbad5">
<!-- Bootstrap core CSS -->
<link href="<c:url value="/consigli-viaggio-grafica/vendor/bootstrap/css/bootstrap.min.css"/>" rel="stylesheet">
<!-- Custom fonts for this template -->
<link href="<c:url value="/consigli-viaggio-grafica/vendor/fontawesome-free/css/all.min.css"/>" rel="stylesheet" type="text/css">
<link href='https://fonts.googleapis.com/css?family=Lora:400,700,400italic,700italic' rel='stylesheet' type='text/css'>
<link href='https://fonts.googleapis.com/css?family=Open+Sans:300italic,400italic,600italic,700italic,800italic,400,300,600,700,800' rel='stylesheet' type='text/css'>
<!-- Custom styles for this template -->
<link href="<c:url value="/consigli-viaggio-grafica/css/clean-blog.min.css"/>" rel="stylesheet">
</head>
<body>
<!-- Navigation -->
<nav class="navbar navbar-expand-lg navbar-light fixed-top" id="mainNav">
	<div class="container">
		<a class="navbar-brand" href="<c:url value="/consigli-di-viaggio"/>"><fmt:message key="menu.consigli.di.viaggio"/></a>
		<button class="navbar-toggler navbar-toggler-right" type="button" data-toggle="collapse" data-target="#navbarResponsive" aria-controls="navbarResponsive" 
			aria-expanded="false" aria-label="Toggle navigation"> Menu <i class="fas fa-bars"></i>
		</button>
		<div class="collapse navbar-collapse" id="navbarResponsive">
			<ul class="navbar-nav ml-auto">
				<li class="nav-item"><a class="nav-link" href="<c:url value="/"/>"><fmt:message key="webapp.apollotransfert.name"/></a></li>
				<li class="nav-item"><a class="nav-link" href="<c:url value='/contatti'/>"><fmt:message key="menu.contatti"/></a></li>
				<!-- <li class="nav-item"><a class="nav-link" href="<c:url value="/consigli-viaggio-grafica/original-page/"/>about.html">About</a></li>
				<li class="nav-item"><a class="nav-link" href="<c:url value="/consigli-di-viaggio/original-page/"/>post.html">Sample Post</a></li> -->
			</ul>
		</div>
	</div>
</nav>
<!-- Page Header -->
<header class="masthead" style="background-image: url('<c:url value="/consigli-viaggio-grafica/"/>img/home-transfer.jpg')">
	<div class="overlay"></div>
	<div class="container">
		<div class="row">
			<div class="col-lg-8 col-md-10 mx-auto">
				<div class="site-heading"></div>
			</div>
		</div>
	</div>
</header>

<decorator:body/>

<hr>
<!-- Footer -->
<footer>
	<div class="container">
		<div class="row">
			<div class="col-lg-8 col-md-10 mx-auto">
				<ul class="list-inline text-center">
					<li class="list-inline-item">
					<a href="https://www.facebook.com/apollotransfert"> <span class="fa-stack fa-lg"><i class="fas fa-circle fa-stack-2x"></i>
						<i class="fab fa-facebook-f fa-stack-1x fa-inverse"></i></span></a>
					</li>
					<li class="list-inline-item">
					<a href="https://twitter.com/apollotransfert"><span class="fa-stack fa-lg"><i class="fas fa-circle fa-stack-2x"></i>
						<i class="fab fa-twitter fa-stack-1x fa-inverse"></i></span></a>
					</li>
					<li class="list-inline-item">
					<a href="https://www.linkedin.com/company/apollotransfert"><span class="fa-stack fa-lg"><i class="fas fa-circle fa-stack-2x"></i>
						<i class="fab fa-linkedin fa-stack-1x fa-inverse"></i></span></a>
					</li>
				</ul>
			</div>
		</div>
	</div>
</footer>
<!-- Bootstrap core JavaScript -->
<script src="<c:url value="/consigli-viaggio-grafica/"/>vendor/jquery/jquery.min.js"></script>
<script src="<c:url value="/consigli-viaggio-grafica/"/>vendor/bootstrap/js/bootstrap.bundle.min.js"></script>
<!-- Custom scripts for this template -->
<script src="<c:url value="/consigli-viaggio-grafica/"/>js/clean-blog.min.js"></script>


</body>
</html>