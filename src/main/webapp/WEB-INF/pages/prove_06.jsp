<%@ include file="/common/taglibs.jsp"%>

<head>
<title><fmt:message key="home.title"/></title>
<script src="<c:url value="/scripts/vendor/jquery-ui-1.11.4/external/jquery/jquery-2.2.4.min.js"/>"></script>
<script src="<c:url value="/js/bootstrap.min.js"/>"></script>

<script type="text/javascript">
var aaa = '${pageContext.request.contextPath}';

 
jQuery(document).ready(function(){

}); // fine ready
</script>

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

<form>
	<input id="message" type="text" value="message_test">
	<input onclick="wsSendMessage();" value="Echo" type="button">
	<input onclick="wsCloseConnection();" value="Disconnect" type="button">
</form>
<br>
<textarea id="echoText" rows="5" cols="30"></textarea>
<script type="text/javascript">

// 192.168.100.102
// ip.domain.mondoserver.apollotransfert=192.168.100.103
//var webSocket = new WebSocket("wss://localhost:8443/sambet/chiamatasocket", "protocol1");
//var webSocket = new WebSocket("wss://sambet.it/chiamatasocket", "protocol1");
var webSocket = new WebSocket('wss://apollotransfert.com:10191/chiamatasocket', 'protocol1');

//var webSocket = new WebSocket("wss://192.168.100.102:10190/chiamatasocket");
//var webSocket = new WebSocket("wss://apollotransfert.com:10190/chiamatasocket");
//var webSocket = new WebSocket("wss://www.apollotransfert.com:10191/chiamatasocket");

var echoText = document.getElementById("echoText");
echoText.value = "";
var message = document.getElementById("message");
webSocket.onopen = function(message){ wsOpen(message);};
webSocket.onmessage = function(message){ wsGetMessage(message);};
webSocket.onclose = function(message){ wsClose(message);};
webSocket.onerror = function(message){ wsError(message);};
function wsOpen(message){
	echoText.value += "Connected ... \n";
}
function wsSendMessage(){
	webSocket.send(message.value);
	echoText.value += "Message sended to the server : " + message.value + "\n";
}
function wsCloseConnection(){
	webSocket.close();
}
function wsGetMessage(message){
	echoText.value += "Message received from to the server: " + message.data + "\n";
}
function wsClose(message){
	echoText.value += "Disconnect ... \n";
}
function wserror(message){
	echoText.value += "Error ... \n";
}



</script>


</div>
</div>
</div>

		
<%
}catch(final Exception e) {
	e.getMessage();
	e.printStackTrace();
}
%>
</body>
