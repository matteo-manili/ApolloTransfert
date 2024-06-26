<%@ include file="/common/taglibs.jsp"%>
<head>
	<title>Chat Zendesk</title>
	<meta name="menu" content="Home"/>

	<!-- jquery -->
	<script src="<c:url value="/scripts/vendor/jquery-ui-1.11.4/external/jquery/jquery-2.2.4.min.js"/>"></script>
	<script src="<c:url value="/js/bootstrap.min.js"/>"></script>

	
	<script type="text/javascript">
	$(document).ready(function () {
		
	}); // fine ready
	</script>
	
</head>
<body>

<div class="content-area home-content">
<div class="container">
<div class="">

	<%@ include file="/common/messages.jsp" %>

	<!-- Include Chat Zendesk -->
	<object style="width: 100%;" height="600" data="https://dashboard.zopim.com"> 
   		Your browser doesn’t support the object tag. 
	</object>

</div>
</div>
</div>

</body>