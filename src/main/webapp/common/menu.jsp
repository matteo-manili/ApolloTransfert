<menu:useMenuDisplayer name="Velocity" config="navbarMenu.vm" permissions="rolesAdapter" bundle="org.apache.struts.action.MESSAGE">
<c:if test="${empty pageContext.request.remoteUser}">
<menu:displayMenu name="HomeRicercaTransfer"/>
<menu:displayMenu name="Tariffe"/>
<menu:displayMenu name="LavoraConNoi"/>
<menu:displayMenu name="ConsigliDiViaggio"/>
</c:if>
<menu:displayMenu name="Home"/>
<menu:displayMenu name="UserMenu"/>
<menu:displayMenu name="AdminMenu"/>
<menu:displayMenu name="Logout"/>
</menu:useMenuDisplayer>