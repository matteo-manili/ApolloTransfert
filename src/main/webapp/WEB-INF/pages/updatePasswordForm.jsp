<%@ include file="/common/taglibs.jsp" %>

<head>
    <title><fmt:message key="updatePassword.title"/></title>
</head>

<body id="updatePassword">

<%@ include file="/common/messages.jsp" %>

<div class="content-area home-content">
<div class="container">
<div class="col-md-12">


<div class=" ">

	<h3>
		<c:choose>
	        <c:when test="${not empty token}">
	            <p><fmt:message key="updatePassword.passwordReset.message"/></p>
	        </c:when>
	        <c:otherwise>
	            <p><fmt:message key="updatePassword.changePassword.message"/></p>
	        </c:otherwise>
	    </c:choose>
	</h3>

	<form method="post" id="updatePassword" action="<c:url value='/updatePassword'/>" class="" autocomplete="off">
		
        <!--  <div class="form-group">
            <label class="control-label"><fmt:message key="user.username"/></label>
        </div> -->
        <input type="hidden" name="username" class="form-control" id="username" value="<c:out value="${username}" escapeXml="true"/>" required>


	    <c:choose>
	    	<c:when test="${not empty token}">
			    <input type="hidden" name="token" value="<c:out value="${token}" escapeXml="true" />"/>
	    	</c:when>
	    	<c:otherwise>
		        <div class="form-group">
		        	<label class="control-label"><fmt:message key="updatePassword.currentPassword.label"/></label>
                    <input type="password" class="form-control" name="currentPassword" id="currentPassword"  autofocus>
		        </div>
	    	</c:otherwise>
	    </c:choose>

        <div class="form-group">
        	<label class="control-label"><fmt:message key="updatePassword.newPassword.label"/></label>
            <input type="password" class="form-control" name="password" id="password" >
        </div>
	    
	    <div class="form-group">
		    <button type="submit" class="btn btn-default" name="cancel">
				<fmt:message key="button.cancel"/>
            </button>
            <!-- metto utente ciro disabled perché è un utente di test dove la gente esterna fa un giro nell'applicazione -->
		    <button type="submit" class="btn btn-primary" ${(username == 'ciro') ? 'disabled' : ''}>
		        <fmt:message key='updatePassword.changePasswordButton'/>
		    </button>
	    </div>
	</form>
</div>

</div>
</div>
</div>

<script src="<c:url value="/scripts/vendor/bootstrap-without-jquery.min.js"/>"></script>

</body>