<!-- MODAL EMAIL-SMS CLIENTE-->
<div class="pop-up-container pop-up-email emailSmsPopUp">
	<div class="pop-up">
		<div class="container">	
			<div class="row">
				<div class="col-md-8 col-md-offset-2 text-center">
					<i class="fas fa-times-circle close-popup"></i>
					<div class="pop-bg row">	
						<h4><fmt:message key="webapp.apollotransfert.name"/> - <span id="idModalEsitoInvioTitle"></span></h4>
						<div class="col-md-12 img-loader" >
							<img src="<c:url value='/nuova_grafica/'/>images/loader.gif">
						</div>
						<div class="col-md-6 col-md-offset-3 message-esito" >
							<p><div id="idModalEsitoInvioText"></div></p>
						</div>
					</div>	
				</div>
			</div>
		</div>	
	</div>
</div>

<div class="modal fade col-xs-12" id="invioSmsEmailConfirm" data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><fmt:message key="webapp.apollotransfert.name"/> - <span id="idModalEsitoInvioTitle_OLD"></span></h4>
            </div>
            <div class="modal-body ">
				<div class="col-xs-offset-5 col-sm-offset-5 col-md-offset-5 col-lg-offset-5" id="loader_OLD"></div>
				<div id="idModalEsitoInvioText_OLD"></div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-info" data-dismiss="modal">Continua</button>
            </div>
        </div>
    </div>
</div>