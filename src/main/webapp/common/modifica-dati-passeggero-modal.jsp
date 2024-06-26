<!-- Modal Dati Passeggero -->
<div class="modal fade col-xs-12" id="modificaDatiPasseggeroModal_${ricercaTransfert.id}" data-backdrop="static" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h3 class="modal-title">Inserisci Dati Passeggero</h3>
            </div>
            <div class="modal-body">
	            <form method="post" class="well " autocomplete="off">
	            	<span class="col-xs-12 col-sm-12 col-md-12 col-lg-12" id="esitoPasseggeroModal_${ricercaTransfert.id}"></span>
	            	<div class="row form-group  ">
						<div class=" ">
							<div class="input-group">
							<span class="input-group-addon" id="basic-addon1"><i class="fa fa-user" aria-hidden="true"></i></span>
							<input type="text" class="form-control" id="nomePasseggeroModal_${ricercaTransfert.id}" value="${ricercaTransfert.nomePasseggero}" placeholder="Nome Passeggero">
							</div>
						</div>
					</div>
					<div class="row form-group">
						<div class=" ">
							<div class="input-group">
							<span class="input-group-addon" id="basic-addon1"><i class="fa fa-phone" aria-hidden="true"></i></span>
							<input type="text" class="form-control" id="telefonoPasseggeroModal_${ricercaTransfert.id}" value="${ricercaTransfert.telefonoPasseggero}" placeholder="Telefono Passeggero">
							<span>Inserisci Prefessio, Esempio Italia: +39 328123456</span>
							</div>
						</div>
					</div>
				</form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-warning" data-dismiss="modal">Chiudi</button>
                <button type="button" id="${ricercaTransfert.id}" class="btn btn-info salvaDatiPasseggeroModal">Salva</button>
            </div>
        </div>
    </div>
</div>