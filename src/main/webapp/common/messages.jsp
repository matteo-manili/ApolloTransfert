<%-- Error Messages --%>
<c:if test="${not empty errors}">
    <div class="alert alert-danger alert-dismissable">
        <a href="#" data-dismiss="alert" class="close">&times;</a>
        <c:forEach var="error" items="${errors}">
            <c:out value="${error}"/><br>
        </c:forEach>
    </div>
    <c:remove var="errors"/>
</c:if>
<%-- Success Messages --%>
<c:if test="${not empty successMessages}">
	<c:forEach var="msg" items="${successMessages}">
		<div class="alert alert-success alert-dismissable">
		<a href="#" data-dismiss="alert" class="close">&times;</a>
		<c:out value="${msg}"/><br>
		</div>
	</c:forEach>
	<c:remove var="successMessages" scope="session"/>
</c:if>

<!-- nuova grafica
<div class="row step-2-conf step-3-conf">
	<p>Utente: <b>Matteo Manili</b> Telefono: <b>123123123</b> Email: <b>abc@gmail.com</b></p>
</div>  -->