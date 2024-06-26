<menu:useMenuDisplayer name="Velocity" config="navbarMenu.vm" permissions="rolesAdapter" bundle="org.apache.struts.action.MESSAGE">
<div class="collapse navbar-collapse" id="myNavbar">
	<ul class="nav navbar-nav navbar-right" id="">
		<li class="dropdown ">
			<a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">
			<span class="lang-sm lang" lang="${language}"></span> <span class="caret"></span></a>
			<ul class="dropdown-menu">
				<li><a href="${pageContext.request.requestURI}?locale=it">
				<span class="lang-sm lang-lbl" lang="it"></span></a></li> <!-- lang-sm lang-lbl-full -->
			<li><a href="${pageContext.request.requestURI}?locale=en"> 
				<span class="lang-sm lang-lbl" lang="en"></span></a></li>
			<li><a href="${pageContext.request.requestURI}?locale=es"> 
				<span class="lang-sm lang-lbl" lang="es"></span></a></li>
			</ul>
		</li> 
		<c:if test="${empty pageContext.request.remoteUser}">
			<menu:displayMenu name="Login"/>
			<menu:displayMenu name="Registrazione"/> 
			<menu:displayMenu name="ChiSiamo"/>
			<menu:displayMenu name="LavoraConNoi"/>
		</c:if>
		<menu:displayMenu name="Home"/>
		<menu:displayMenu name="UserMenu"/>
		<menu:displayMenu name="AdminMenu"/>
		<menu:displayMenu name="Contatti"/>
		<menu:displayMenu name="Logout"/>
    </ul>
</div>
</menu:useMenuDisplayer>