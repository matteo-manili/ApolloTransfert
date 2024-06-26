<%@ include file="/common/taglibs.jsp" %>

<head>
    <title><fmt:message key="userList.title"/></title>
    <meta name="menu" content="AdminMenu"/>
    
    <!-- jquery -->
	<link rel="stylesheet" href="<c:url value="/scripts/vendor/jquery-ui-1.11.4/jquery-ui.css"/>">
	
	<script src="<c:url value="/scripts/vendor/jquery-ui-1.11.4/external/jquery/jquery-2.2.4.min.js"/>"></script>
	<script src="<c:url value="/js/bootstrap.min.js"/>"></script>
    
</head>

<body>

<div class="content-area home-content">
<div class="container">
<div class="">


  	<h3 style="margin-top: -20px;">Gestione Utenti</h3>
  	
  	<c:if test="${not empty searchError}">
	    <div class="alert alert-danger alert-dismissable">
	        <a href="#" data-dismiss="alert" class="close">&times;</a>
	        <c:out value="${searchError}"/>
	    </div>
	</c:if>
	
	<%@ include file="/common/messages.jsp" %>


    <form method="get" action="${ctx}/admin/users" id="searchForm" class="form-inline well" role="form">
    	
		<div class="form-group">
			
			<div class="input-group">
				<label class="sr-only" for="query">ricerca</label>
	            <input type="text" name="q" class="form-control" size="30" id="query" value="${param.q}"
	                   placeholder="<fmt:message key="search.enterTerms"/>" >
				<span class="input-group-btn">
	            	<button type="submit" name="ricerca" id="button.search" class="btn btn-default"><fmt:message key="button.search"/></button>
	        	</span>
	        </div><!-- /input-group -->
	        
	        <a class="btn btn-primary" href="<c:url value='/admin/userform-admin?method=Add&from=list'/>"><fmt:message key="button.add"/></a>
            
            <a class="btn btn-default" href="<c:url value='/home-user'/>"><fmt:message key="button.done"/></a>
	        
		</div>
    </form>

	
	<div class="row">
	    <display:table name="userList" cellspacing="0" cellpadding="0" requestURI="" 
	    	id="users" partialList="true" pagesize="${page_size_table}" size="resultSize" class="table table-condensed table-striped table-hover" export="true">
	                   
	        <display:column property="id" sortable="true" titleKey="id" style="width: 2%" />
	        
	        <display:column property="signupDate" format="{0,date,dd/MM/yyyy HH.mm.ss}" sortable="true" titleKey="signupDate" style="width: 10%"/>
	
	        <display:column property="username" escapeXml="true" sortable="true" titleKey="user.username" style="width: 10%"
	                        url="/admin/userform-admin?from=list" paramId="id" paramProperty="id"/>
	                        
	        <display:column property="fullName" escapeXml="true" sortable="true" titleKey="activeUsers.fullName" style="width: 12%"/>
	                        
			<display:column property="phoneNumber" escapeXml="true" sortable="true" titleKey="Telefono" style="width: 5%"/>
	                        
	        <display:column property="email" sortable="true" titleKey="user.email" autolink="true" media="html" style="width: 10%" />
	                        
			<display:column property="roles" sortable="true" titleKey="roles" style="width: 15%" />
			
			<display:column property="tipoRuoli" sortable="true" titleKey="tipoRuoli" style="width: 20%" />
	                        
			<display:column sortProperty="enabled" sortable="true" titleKey="user.enabled" media="html" style="width: 2%; padding-left: 15px">
	            <input type="checkbox" disabled="disabled" <c:if test="${users.enabled}">checked="checked"</c:if>/>
	        </display:column>
	        
	        <display:column property="enabled" titleKey="user.enabled" media="csv xml excel pdf"/>
	        
			<display:column property="email" titleKey="user.email" media="csv xml excel pdf"/>
	        <display:setProperty name="paging.banner.item_name"><fmt:message key="userList.user"/></display:setProperty>
	        <display:setProperty name="paging.banner.items_name"><fmt:message key="userList.users"/></display:setProperty>
	
	        <display:setProperty name="export.excel.filename" value="User List.xls"/>
	        <display:setProperty name="export.csv.filename" value="User List.csv"/>
	        <display:setProperty name="export.pdf.filename" value="User List.pdf"/>
	    </display:table>
	    
	</div>

</div>
</div>
</div>


</body>