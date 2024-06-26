<%@ include file="/common/taglibs.jsp"%>

<head>
<title><fmt:message key="home.title"/></title>
<script src="<c:url value="/scripts/vendor/jquery-ui-1.11.4/external/jquery/jquery-2.2.4.min.js"/>"></script>
<script src="<c:url value="/js/bootstrap.min.js"/>"></script>


<!-- ID client OAuth 2.0 APOLLOTRANSFERT PRODUZIONE: 969110475011-87vqmm871usgcoikhcjlssfmrs3gndif.apps.googleusercontent.com -->
<script src="https://apis.google.com/js/platform.js" async defer></script>
<meta name="google-signin-scope" content="profile email">
<meta name="google-signin-client_id" content="969110475011-87vqmm871usgcoikhcjlssfmrs3gndif.apps.googleusercontent.com">


<script type="text/javascript">
var aaa = '${pageContext.request.contextPath}';

// 2020-01-01 00:00:00
var JSONObject_BetFair = {"username" : "matteo.manili@gmail.com", "password" : "sasaasas"}
 
jQuery(document).ready(function(){


	
	
}); // fine ready

/*
$.ajax({
	
	  url: 'https://identitysso-cert.betfair.it',
	  //url: 'https://identitysso.betfair.it/api/login', 
	  //url: 'https://www.apollotransfert.com/api_Giornata_GiornataOrario_UpdateOrario',
	  type: "POST",
	  dataType: 'json',
	  data: JSON.stringify( JSONObject_BetFair ),
	  dataType: 'json',
	  contentType: "application/json; charset=utf-8",
	  
	  beforeSend: function (xhr) {
			//xhr.setRequestHeader("X-Application", "tRaEVLp8c0xHlK7G");  
			//xhr.setRequestHeader("Accept", "application/json");	
			//xhr.setRequestHeader("Content-type", "application/json");
			//xhr.setRequestHeader("X-Authentication", "a+bZQ5dlOgA5CBYPBsXJphuQ/DCzElImlQ5tJiyQ9p4=");
			xhr.setRequestHeader("Access-Control-Allow-Origin", "*");
			xhr.setRequestHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
			xhr.setRequestHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
			xhr.setRequestHeader("Access-Control-Allow-Credentials", "true");
			xhr.setRequestHeader("Access-Control-Max-Age", "1800");

			xhr.setRequestHeader("Accept", "application/json");  
			//xhr.setRequestHeader("X-Authentication", "tRaEVLp8c0xHlK7G");
		  	xhr.setRequestHeader("X-Application", "tRaEVLp8c0xHlK7G");
		  	
		  	xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
		},
	  success: function(data) {
		  var myJSON = JSON.stringify(data);
		  document.getElementById("html-response").innerHTML = myJSON;

		  $.each(data, function () {
		        //alert("fromKm: "+this.fromKm+ " toKm: "+this.toKm+ " euro: "+this.euro+ " raggio: "+this.raggio);
		    });
	  },
	  error: function(XMLHttpRequest, textStatus, errorThrown) {
	    console.log('erroreeeee');
	  }
	});
 */
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


<div class="g-signin2" data-onsuccess="onSignIn"></div>
<br><br>

<form method="post" action="<c:url value='/proveLoginGoogle'/>" id="proveLoginGoogleForm" name="">
<input type="text" name="id_token" id="id_id_token" class="form-control">
<br>
<button type="submit" name="ricerca" id="button.search" class="btn btn-default"><fmt:message key="button.search"/></button>
</form>

<br><br>
<a href="#" onclick="signOut();">Sign out</a>


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

function onSignIn(googleUser) {
	console.log(123);
	var profile = googleUser.getBasicProfile();
	console.log('ID: ' + profile.getId()); // Do not send to your backend! Use an ID token instead.
	console.log('Name: ' + profile.getName());
	console.log('Given Name: ' + profile.getGivenName());
	console.log('Family Name: ' + profile.getFamilyName());
	console.log('Image URL: ' + profile.getImageUrl());
	console.log('Email: ' + profile.getEmail()); // This is null if the 'email' scope is not present.
	
	$("#id_id_token").val( googleUser.getAuthResponse().id_token );
}



function signOut() {
	var auth2 = gapi.auth2.getAuthInstance();
	auth2.signOut().then(function () {
	  console.log('User signed out.');
	});
}

</script>

		
<%
}catch(final Exception e) {
	e.getMessage();
	e.printStackTrace();
}
%>
</body>
