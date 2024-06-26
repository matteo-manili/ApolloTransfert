<c:if test="${ricercaTransfert.verificatoCustomer && not ricercaTransfert.pagamentoEseguitoMedio}">

<%@ include file="/common/ric_payment_dati_cliente.jsp"%>

<c:choose>
<c:when test="${ricercaTransfert.vecchioPrezzo == null}">
	<!-- INSERIMENTO CODICE SCONTO -->
	<div class="">
		<h4>Se possiedi un codice sconto <a class="open-popup-sconto" onClick="return false;" href="#">
		<span style=""><b><ins>inseriscilo qui</ins></b></span></a></h4>
	</div>
	
	<!-- POPUP SCONTO -->
	<div class="pop-up-container pop-up-sconto" id="ScontoModal">
		<div class="pop-up">
			<div class="container">
				<div class="row">
					<div class="col-md-8 col-md-offset-2 text-center">
						<i class="fas fa-times-circle close-popup"></i>
						<div class="pop-bg row">	
							<h4>LOGIN</h4>
							<div style="display:block;" class="col-md-6 col-md-offset-3">
								<div class="form-group input-group">
									<span class="input-group-addon"><i class="fas fa-key"></i></span>
									<input type="text" name="codice-sconto" id="inserisciCodiceSconto" class="form-control" placeholder="Inserisci Codice Sconto" />
								</div>
								<input type="button" name="applica-sconto" id="ScontoButton" class="inviaSubmitSconto btn btn-info " value="Applica Sconto">
							</div>
						</div>	
					</div>
				</div>
			</div>	
		</div>
	</div>
</c:when>
<c:otherwise>
	<!-- RISULTATO SCONTO -->
	<div class="row step-2-conf step-3-conf">
		<p>Sconto Applicato! Vecchio Prezzo: ${ricercaTransfert.vecchioPrezzo}euro | Codice Sconto: ${ricercaTransfert.codiceSconto} | Percentuale Sconto: ${ricercaTransfert.percentualeSconto}%</p>
	</div>
</c:otherwise>
</c:choose>

<%@ include file="/common/ric_payment.jsp"%>

</c:if>