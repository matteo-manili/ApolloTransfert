<%@ include file="/common/taglibs.jsp" %>

<head>
    <title>Data Access Error</title>
</head>

<body>

<div class="content-area home-content">
<div class="container">
<div class="col-md-12">


	<p> <h2>Data Access Failure</h2> <c:out value="${requestScope.exception.message}"/></p>
	
	<!-- <% 
	((Exception) request.getAttribute("exception")).printStackTrace(new java.io.PrintWriter(out));  
	%> -->
	
	<a href="home" onclick="history.back();return false">&#171; Back</a>

</div>
</div>
</div>

<script src="<c:url value="/scripts/vendor/bootstrap-without-jquery.min.js"/>"></script>
</body>