<%
try {
%>
<c:if test="${ricercaTransfert.riepilogo eq false}">
	<%@ include file="/common/ric_standard.jsp"%>
	<%@ include file="/common/ric_result.jsp"%>
	<%@ include file="/common/ric_richiesta_preventivi.jsp"%>
</c:if>
<c:if test="${ricercaTransfert.prenotazione eq true 
&& ricercaTransfert.riepilogo eq false 
&& (ricercaTransfert.verificatoCustomer == true || not empty ricercaTransfert.ricTransfert_IdUser)
&& ricercaTransfert.richiestaPreventivi_Inviata 
&& not empty richAutistPart}">
	<%@ include file="/scripts/ModalInvioSmsEmail.jsp"%>
	<%@ include file="/common/ric_payment.jsp"%>
</c:if>
<c:if test="${ricercaTransfert.prenotazione eq true && ricercaTransfert.riepilogo eq true}">
	<%@ include file="/common/ric_riepilogo.jsp"%>
	<%@ include file="/common/ric_result.jsp"%>
	<%@ include file="/common/ric_richiesta_preventivi.jsp"%>
</c:if>
<%		 
}
catch (Exception e) {
out.println("An exception occurred: " + e.getMessage());
}
%>