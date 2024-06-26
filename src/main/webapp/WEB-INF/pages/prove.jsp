<%@ include file="/common/taglibs.jsp"%>

<head>
<title><fmt:message key="home.title"/></title>
<script src="<c:url value="/scripts/vendor/jquery-ui-1.11.4/external/jquery/jquery-2.2.4.min.js"/>"></script>
<script src="<c:url value="/js/bootstrap.min.js"/>"></script>


<script type="text/javascript">
var aaa = '${pageContext.request.contextPath}';

// 2020-01-01 00:00:00
var JSONObject_BetFair = {"username" : "matteo.manili@gmail.com", "password" : "sasaasas"}
 
jQuery(document).ready(function(){
	
}); // fine ready

// This is called with the results from from FB.getLoginStatus().
function statusChangeCallback(response) {
	console.log('statusChangeCallback');
	console.log(response);
	console.log(response.authResponse.accessToken);
	//alert(response.authResponse.accessToken);
	if (response.status === 'connected') {
	window.location.href= '<c:url value="/prove?access_token="/>'+response.authResponse.accessToken; 
	} else {
	// The person is not logged into your app or we are unable to tell.
	document.getElementById('status').innerHTML = 'Please log ' +
	'into this app.';
	}
}

// This function is called when someone finishes with the Login
// Button. See the onlogin handler attached to it in the sample
// code below.
function checkLoginState() {
	FB.getLoginStatus(function(response) {
	statusChangeCallback(response);
	});
}

</script>


</head>
<body>
<%
try{
%>
<div class="content-area home-content">
<div class="container">
<div class="col-md-12">
<%@ include file="/common/messages.jsp" %>

<div id="html-response"></div>




<div class="fb-login-button" data-onlogin="checkLoginState();" data-size="large" data-button-type="continue_with" data-layout="default" 
	data-auto-logout-link="false" data-use-continue-as="false" data-width=""></div>
<div id="status"></div>


<div id="fb-root"></div>
<script async defer crossorigin="anonymous" src="https://connect.facebook.net/it_IT/sdk.js#xfbml=1&version=v9.0&appId=${FB_APP_ID}&autoLogAppEvents=1" nonce="N1mmPdfP"></script>


<!-- ----------------------------------------------------------------------------------------------------- 
<div class="row form-group hidden ">
	<input type="button" name="verifica-sms-customer" class="btn btn-sm btn-primary" id="loginButton" value="Verifica Codice SMS">
</div>
<div class="row hidden">
	<div>${device}</div>
</div>
<div class="hidden">${memory}</div> -->
<!-- ----------------------------------------------------------------------------------------------------- -->
	
</div>
</div>
</div>


<script type="text/javascript">

</script>

		
<%
}catch(final Exception e) {
	e.getMessage();
	e.printStackTrace();
}
%>
</body>
