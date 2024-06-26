<%@ include file="/common/taglibs.jsp" %>

<head>
    <title><fmt:message key="activeUsers.title"/></title>
    <meta name="menu" content="AdminMenu"/>
</head>
<body id="activeUsers">

<div class="content-area home-content">
<div class="container">
<div class="">

	<div class="row">
	
	    <h3 style="margin-top: -20px;"><fmt:message key="activeUsers.heading"/></h3>
	
	    <p><fmt:message key="activeUsers.message"/></p>

	
	    <display:table name="applicationScope.userNames" id="user" cellspacing="0" cellpadding="0" 
	    	defaultsort="1" class="table table-condensed table-striped table-hover" pagesize="50" requestURI="">
	        
	        <display:column titleKey="activeUsers.fullName" sortable="true">
	            <c:out value="${user.firstName} ${user.lastName}" escapeXml="true"/>
	            <c:if test="${not empty user.email}">
	                <a href="mailto:<c:out value="${user.email}"/>">
	                    <img src="<c:url value="/images/iconEmail.gif"/>"
	                         alt="<fmt:message key="icon.email"/>" class="icon"/></a>
	            </c:if>
	        </display:column>
	        <display:column property="email" escapeXml="true"  title="email" sortable="true"/>
	        <display:column property="phoneNumber" escapeXml="true" title="phone" sortable="true"/>
	        <display:column property="username" escapeXml="true" titleKey="user.username" sortable="true"/>
	
	        <display:setProperty name="paging.banner.item_name" value="user"/>
	        <display:setProperty name="paging.banner.items_name" value="users"/>
	    </display:table>
	</div>
	
	<script src="<c:url value="/scripts/vendor/bootstrap-without-jquery.min.js"/>"></script>
	
</div>
</div>
</div>
	
</body>
