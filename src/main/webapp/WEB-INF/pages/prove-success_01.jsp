<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="home.title"/></title>
    <meta name="breadcrumb" content="<fmt:message key="breadcrumb.prove"/>"/>


	<!-- jquery -->
	<link rel="stylesheet" href="<c:url value="/scripts/vendor/jquery-ui-1.11.4/jquery-ui.css"/>" th:src="@{<c:url value="/scripts/vendor/jquery-ui-1.11.4/jquery-ui.css"/>}">
	<script src="<c:url value="/scripts/vendor/jquery-ui-1.11.4/jquery-ui.min.js"/>" th:src="@{<c:url value="/scripts/vendor/jquery-ui-1.11.4/jquery-ui.min.js"/>}"></script>
	
	<script src="<c:url value="/scripts/jquery.lettering-0.6.1.min.js"/>" th:src="@{<c:url value="/scripts/jquery.lettering-0.6.1.min.js"/>}"></script>
	<script src="<c:url value="/scripts/braintreeToken.js"/>" th:src="@{<c:url value="/scripts/braintreeToken.js"/>}"></script>



	<script type="text/javascript">
	jQuery(document).ready(function(){
		

	}); // fine ready
	</script>


</head>
<body>

<%@ include file="/common/messages.jsp" %>

<div class="">
	<div class="">
		<div class="row">
			<div class="col-sm-12" >
			<!-- ----------------------------------------------------------------------------------------------------- -->




		${transaction.getStatus()}<br>
		${transaction.getAmount()}<br>
		${creditCard.getCardType()}<br>
		${customer.getFirstName()}<br>
		${creditCard.getBin()}<br>
		${transaction.getId()}<br>
		${isSuccess}<br>
		${transaction.getStatus()}<br>
		${transaction.getStatus()}<br>
		${transaction.getStatus()}<br>
		${transaction.getStatus()}<br>
		${transaction.getStatus()}<br>
		${transaction.getStatus()}<br>







			<!-- ----------------------------------------------------------------------------------------------------- -->
			</div>
		</div>
	</div>
</div>
</body>
