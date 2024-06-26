<c:choose>
<c:when test="${not empty user.email && not fn:contains(user.email, '@apollostransfert.com')}">
	<p><c:if test="${not empty user}">Utente: <b>${user.fullName}</b></c:if>
	<c:set value="${ricercaTransfert.phoneNumberCustomer}" var="phone"/>
	Telefono: <b>
	<c:choose>
		<c:when test="${not empty phone}">
			<c:out value="${fn:substring(phone, 0, 3)}.${fn:substring(phone,3,6)}.${fn:substring(phone,6,9)}.${fn:substring(phone,9,15)}"/>
		</c:when>
		<c:when test="${not empty user.phoneNumber}">
			<c:out value="${fn:substring(user.phoneNumber, 0, 3)}.${fn:substring(user.phoneNumber,3,6)}.${fn:substring(user.phoneNumber,6,9)}.${fn:substring(user.phoneNumber,9,15)}"/>
		</c:when>
	</c:choose>
	</b>
	Email: <b>${user.email}</b>
	</p>
	<input type="hidden" name="cliente-firstName" id="clienteFirstNameId" value="${user.firstName}">
	<input type="hidden" name="cliente-lastName" id="clienteLastNameId" value="${user.lastName}">
	<input type="hidden" name="cliente-email" id="clienteEmailId" value="${user.email}">
	<input type="hidden" name="conf-cliente-email" id="confClienteEmailId" value="${user.email}">
	
</c:when>
<c:when test="${empty user && ricercaTransfert.tipoServizio == 'AGA'}">
	<%@ include file="/common/ric_dati_cliente.jsp"%>
</c:when>
<c:when test="${empty user && ricercaTransfert.tipoServizio == 'ST'}">
	<%@ include file="/common/ric_dati_cliente.jsp"%>
</c:when>
<c:when test="${empty user && (ricercaTransfert.tipoServizio == 'PART' or ricercaTransfert.tipoServizio == 'MULTIP') && (empty ricercaTransfert.richiestaPreventivi_Inviata || ricercaTransfert.richiestaPreventivi_Inviata == false) && empty richAutistPart }">
	<!-- STEP PREVENTIVI RICHIESTA NON INVIATA -->
	<%@ include file="/common/ric_dati_cliente.jsp"%>
</c:when>
<c:when test="${empty user && (ricercaTransfert.tipoServizio == 'PART' or ricercaTransfert.tipoServizio == 'MULTIP') && ricercaTransfert.richiestaPreventivi_Inviata == true && empty richAutistPart }">
	<!-- STEP PREVENTIVI RICHIESTA INVIATA -->
	<div class="  ">
		<p>Nome: <b>${ricercaTransfert.ricTransfert_Nome}</b> Cognome: <b>${ricercaTransfert.ricTransfert_Cognome}</b> 
			Email: <b>${ricercaTransfert.ricTransfert_Email}</b> Telefono: <b><c:out value="${fn:substring(ricercaTransfert.phoneNumberCustomer, 0, 3)}.${fn:substring(ricercaTransfert.phoneNumberCustomer,3,6)}.${fn:substring(ricercaTransfert.phoneNumberCustomer,6,9)}.${fn:substring(ricercaTransfert.phoneNumberCustomer,9,15)}"/></b>
		</p>
	</div>
</c:when>
<c:when test="${empty user && (ricercaTransfert.tipoServizio == 'PART' or ricercaTransfert.tipoServizio == 'MULTIP') && ricercaTransfert.richiestaPreventivi_Inviata == true && not empty richAutistPart && ricercaTransfert.riepilogo == false }">
	<!-- STEP ACQUISTO CORSA -->
	<%@ include file="/common/ric_dati_cliente.jsp"%>
</c:when>
<c:when test="${empty user && (ricercaTransfert.tipoServizio == 'PART' or ricercaTransfert.tipoServizio == 'MULTIP') && ricercaTransfert.richiestaPreventivi_Inviata == true && not empty richAutistPart && ricercaTransfert.riepilogo == true }">
	<!-- STEP RIEPILOGO ACQUISTO CORSA -->
	<div class="  ">
		<p>Nome: <b>${ricercaTransfert.ricTransfert_Nome}</b> Cognome: <b>${ricercaTransfert.ricTransfert_Cognome}</b> 
			Email: <b>${ricercaTransfert.ricTransfert_Email}</b> Telefono: <b><c:out value="${fn:substring(ricercaTransfert.phoneNumberCustomer, 0, 3)}.${fn:substring(ricercaTransfert.phoneNumberCustomer,3,6)}.${fn:substring(ricercaTransfert.phoneNumberCustomer,6,9)}.${fn:substring(ricercaTransfert.phoneNumberCustomer,9,15)}"/></b>
		</p>
	</div>
</c:when>
</c:choose>