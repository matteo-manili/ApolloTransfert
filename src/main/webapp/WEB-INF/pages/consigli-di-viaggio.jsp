<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<head>
<title>Consigli di Viaggio, informazioni utili per viaggiare in Italia</title>
<meta name="description" content="Articoli per consigliare e orientare il viaggiatore nelle città e nelle infrastrutture di transito passeggeri in Italia | <fmt:message key="webapp.apollotransfert.name"/>">
</head>
<body>
<!-- Main Content -->
<div class="container">
	<div class="row">
		<div class="col-lg-8 col-md-10 mx-auto">
			<c:forEach items="${listPost}" var="varObj" varStatus="loop">
				<div class="post-preview">
					<a href="<c:url value="/consigli-di-viaggio/"/><fmt:message key="${varObj.first}"/>">
						<h2 class="post-title" style="text-transform: uppercase;"><fmt:message key="${varObj.second}"/></h2>
						<h3 class="post-subtitle"><fmt:message key="${varObj.third}"/></h3>
					</a>
				</div>
				<c:if test="${!loop.last}"><hr></c:if>
			 </c:forEach>
			<!-- Pager -->
			<!-- <div class="clearfix"><a class="btn btn-primary float-right" href="<c:url value="/consigli-di-viaggio/"/>#">Older Posts &rarr;</a></div> -->
		</div>
	</div>
</div>
</body>
</html>