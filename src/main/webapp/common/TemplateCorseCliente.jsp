<script id="datiPasseggeroModalTpl" type="text/template">
{{#idcorsa}}
<!-- Modal Dati Passeggero -->
<div class="modal fade col-xs-12" id="modificaDatiPasseggeroModal_{{idcorsa}}" data-backdrop="static" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h3 class="modal-title">Inserisci Dati Passeggero</h3> 
            </div>
            <div class="modal-body">
	            <form method="post" class=" " autocomplete="off">
	            	<span class="col-xs-12 col-sm-12 col-md-12 col-lg-12" id="esitoPasseggeroModal_{{idcorsa}}"></span>
	            	<div class="row form-group">
						<div class=" ">
							<div class="input-group">
							<span class="input-group-addon" id="basic-addon1"><i class="fa fa-user" aria-hidden="true"></i></span>
							<input type="text" class="form-control" id="nomePasseggeroModal_{{idcorsa}}" value="{{nomePasseggero}}" placeholder="Nome Passeggero">
							</div>
						</div>
					</div>
					<div class="row form-group">
						<div class=" ">
							<div class="input-group">
							<span class="input-group-addon" id="basic-addon1"><i class="fa fa-phone" aria-hidden="true"></i></span>
							<input type="text" class="form-control" id="telefonoPasseggeroModal_{{idcorsa}}" value="{{telefonoPasseggero}}" placeholder="Telefono Passeggero">
							<span>Inserisci Prefessio, Esempio Italia: +39 328123456</span>
							</div>
						</div>
					</div>
				</form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-warning" data-dismiss="modal">Chiudi</button>
                <button type="button" id="{{idcorsa}}" class="btn btn-info salvaDatiPasseggeroModal">Salva</button>
            </div>
        </div>
    </div>
</div>
{{/idcorsa}}
</script>

<script id="corseAgendaAutistaClienteTpl" type="text/template">
{{#idcorsa}}
<div id="panel_{{idcorsa}}" class="panel panel-primary" style="margin-top: 10px; margin-bottom: 10px;">
	<div class="panel-heading" data-toggle="" data-target="#collapse_{{idcorsa}}" style="font-size: large;">
		<span class="label label-success">{{idcorsa}}</span><span style="padding-left:10px;">{{intestazione}}</span>
			<a class="btn btn-sm btn-info pull-right checkFatturaDisponibile" id="{{idcorsa}}">
				<span class="glyphicon glyphicon-download-alt"></span> Scarica Fattura</a></div>
		<div id="collapse_{{idcorsa}}" class="panel-collapse">
		<div class="panel-body">
			<div class="panel {{andata.colorPanel}}">
				<div class="panel-heading">{{#andata.blinkeffect}}<span class="blinkeffect">&#9899;</span>{{/andata.blinkeffect}} {{andata.intestazione}}</div>
				<div class="panel-body " style="">
				<div class="col-sm-12 row">
						<div class="col-sm-1">ANDATA</div>
						<div class="col-sm-4">{{andata.oraPrelev}} (giorno - ora prelevamento)</div>
						<div class="col-sm-4">{{andata.durata}} (durata transfert prevista)</div>
						<div class="col-sm-3">{{andata.distanza}} (distanza)</div>
					</div>
				</div>
			</div>
			{{#ritorno}}
			<div class="panel {{ritorno.colorPanel}}">
				<div class="panel-heading">{{#ritorno.blinkeffect}}<span class="blinkeffect">&#9899;</span>{{/ritorno.blinkeffect}} {{ritorno.intestazione}}</div>
				<div class="panel-body">
				<div class="col-sm-12 row">
						<div class="col-sm-1">RITORNO</div>
						<div class="col-sm-4">{{ritorno.oraPrelev}} (giorno - ora prelevamento)</div>
						<div class="col-sm-4">{{ritorno.durata}} (durata transfert prevista)</div>
						<div class="col-sm-3">{{ritorno.distanza}} (distanza)</div>
					</div>
				</div>
			</div>
			{{/ritorno}}
			<div class="panel panel-default">
				<div class="panel-heading">INFORMAZIONI CORSA</div>
				<div class="panel-body">
					<div class="col-sm-12 row">
						<div class="col-sm-9">
							<button type="button" id="{{idcorsa}}" class="btn btn-sm btn-primary alertConfirmInfoAutista">
								<span class="glyphicon glyphicon-phone"></span> Info Autista</button>
							<a class="btn btn-sm btn-info openDatiPasseggeroModal" id="{{idcorsa}}">
								<span class="fa fa-user"></span> <fmt:message key="messaggio.modifica.dati.passeggero.corsa"></fmt:message></a>
							<a class="btn btn-sm btn-danger gestioneRimborsoCliente" id="{{idcorsa}}">
								<span class="fa fa-credit-card"></span> <fmt:message key="messaggio.cliente.cancella.corsa"></fmt:message></a>
						</div>
					</div>
					{{#notePerAutista}}
					<div class="col-sm-12 row" style="margin-top: 10px;">
						<div class="col-sm-3"><strong>NOTE PER L'AUTISTA</strong></div>
						<div class="col-sm-9"><em>{{notePerAutista}}</em></div>
					</div>
					{{/notePerAutista}}
					<!-- Autista Andata -->
					<div class="col-sm-12 row" style="margin-top: 10px;">
						<div class="col-sm-12"><strong><u>AUTISTA ANDATA</u></strong></div>
					</div>
					<div class="col-sm-12 row">
						<div class="col-sm-3"><strong>Autoveicolo Autista</strong></div><div class="col-sm-9">{{autoveicoloRichiesto_Andata}}</div>
					</div>
					<div class="col-sm-12 row">
						<div class="col-sm-3"><strong>Categoria Auto</strong></div><div class="col-sm-9">{{classeAutoveicoloSceltaCliente_Andata}}</div>
					</div>
					<div class="col-sm-12 row">
						<div class="col-sm-3"><strong>Info Categoria Auto</strong></div><div class="col-sm-9">{{descrizioneCategorieAuto_Andata}}</div>
					</div>
					{{#ritorno}}
					<!-- Autista Ritorno -->
					<div class="col-sm-12 row" style="margin-top: 10px;">
						<div class="col-sm-12"><strong><u>AUTISTA RITORNO</u></strong></div>
					</div>
					<div class="col-sm-12 row">
						<div class="col-sm-3"><strong>Autoveicolo Autista</strong></div><div class="col-sm-9">{{autoveicoloRichiesto_Ritorno}}</div>
					</div>
					<div class="col-sm-12 row">
						<div class="col-sm-3"><strong>Categoria Auto</strong></div><div class="col-sm-9">{{classeAutoveicoloSceltaCliente_Ritorno}}</div>
					</div>
					<div class="col-sm-12 row">
						<div class="col-sm-3"><strong>Info Categoria Auto</strong></div><div class="col-sm-9">{{descrizioneCategorieAuto_Ritorno}}</div>
					</div>
					{{/ritorno}}
					<div class="col-sm-12 row" style="margin-top: 10px;">
						<div class="col-sm-3"><strong>Prezzo Corsa</strong></div><div class="col-sm-5">{{prezzoCliente}}&euro;</div>
					</div>
					<div class="col-sm-12 row">
						<div class="col-sm-3"><strong>Num Passeggeri</strong></div><div class="col-sm-5">{{numPasseggeri}}</div>
					</div>
					<div class="col-sm-12 row">
						<div class="col-sm-3"><strong>Rimborso</strong></div><div class="col-sm-5">{{rimborsoCliente}}&euro;</div>
					</div>
					<div class="col-sm-12 row">
						<div class="col-sm-3"><strong>Nome e Telefono Passeggero</strong></div><div id="nomeTelefonoPasseggero-{{idcorsa}}" class="col-sm-5">{{nomeTelefonoPasseggero}}</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
{{/idcorsa}}
</script>

<script id="corseAgendaAutistaCompletateClienteTpl" type="text/template">
{{#idcorsa}}
<div id="panel_{{idcorsa}}" class="panel panel-primary" style="margin-top: 10px; margin-bottom: 10px;">
	<div class="panel-heading" data-toggle="" data-target="#collapse_{{idcorsa}}" style="font-size: large;">
		<span class="label label-success">{{idcorsa}}</span><span style="padding-left:10px;">{{intestazione}}</span>
			<a class="btn btn-sm btn-info pull-right checkFatturaDisponibile" id="{{idcorsa}}">
				<span class="glyphicon glyphicon-download-alt"></span> Scarica Fattura</a></div>
		<div id="collapse_{{idcorsa}}" class="panel-collapse">
		<div class="panel-body">
			<div class="panel {{andata.colorPanel}}">
				<div class="panel-heading">{{#andata.blinkeffect}}<span class="blinkeffect">&#9899;</span>{{/andata.blinkeffect}} {{andata.intestazione}}</div>
				<div class="panel-body">
				<div class="col-sm-12 row">
						<div class="col-sm-1">ANDATA</div>
						<div class="col-sm-4">{{andata.oraPrelev}} (giorno - ora prelevamento)</div>
						<div class="col-sm-4">{{andata.durata}} (durata transfert prevista)</div>
						<div class="col-sm-3">{{andata.distanza}} (distanza)</div>
					</div>
				</div>
			</div>
			{{#ritorno}}
			<div class="panel {{ritorno.colorPanel}}">
				<div class="panel-heading">{{#ritorno.blinkeffect}}<span class="blinkeffect">&#9899;</span>{{/ritorno.blinkeffect}} {{ritorno.intestazione}}</div>
				<div class="panel-body">
				<div class="col-sm-12 row">
						<div class="col-sm-1">RITORNO</div>
						<div class="col-sm-4">{{ritorno.oraPrelev}} (giorno - ora prelevamento)</div>
						<div class="col-sm-4">{{ritorno.durata}} (durata transfert prevista)</div>
						<div class="col-sm-3">{{ritorno.distanza}} (distanza)</div>
					</div>
				</div>
			</div>
			{{/ritorno}}
			<div class="panel panel-default">
				<div class="panel-heading">INFORMAZIONI CORSA</div>
				<div class="panel-body">
					<div class="col-sm-12 row">
						<div class="col-sm-9">
							<button type="button" id="{{idcorsa}}" class="btn btn-sm btn-primary alertConfirmInfoAutista">
								<span class="glyphicon glyphicon-phone"></span> Info Autista</button>
							<a class="btn btn-sm btn-info openDatiPasseggeroModal" id="{{idcorsa}}">
								<span class="fa fa-user"></span> <fmt:message key="messaggio.modifica.dati.passeggero.corsa"></fmt:message></a>
							<a class="btn btn-sm btn-danger gestioneRimborsoCliente" id="{{idcorsa}}">
								<span class="fa fa-credit-card"></span> <fmt:message key="messaggio.cliente.cancella.corsa"></fmt:message></a>
						</div>
					</div>
					{{#notePerAutista}}
					<div class="col-sm-12 row" style="margin-top: 10px;">
						<div class="col-sm-3"><strong>NOTE PER L'AUTISTA</strong></div>
						<div class="col-sm-9"><em>{{notePerAutista}}</em></div>
					</div>
					{{/notePerAutista}}
					<!-- Autista Andata -->
					<div class="col-sm-12 row" style="margin-top: 10px;">
						<div class="col-sm-12"><strong>AUTISTA ANDATA</strong></div>
					</div>
					<div class="col-sm-12 row">
						<div class="col-sm-3"><strong>Autoveicolo Autista</strong></div><div class="col-sm-9">{{autoveicoloRichiesto_Andata}}</div>
					</div>
					<div class="col-sm-12 row">
						<div class="col-sm-3"><strong>Categoria Auto</strong></div><div class="col-sm-9">{{classeAutoveicoloSceltaCliente_Andata}}</div>
					</div>
					<div class="col-sm-12 row">
						<div class="col-sm-3"><strong>Info Categoria Auto</strong></div><div class="col-sm-9">{{descrizioneCategorieAuto_Andata}}</div>
					</div>
					{{#ritorno}}
					<!-- Autista Ritorno -->
					<div class="col-sm-12 row" style="margin-top: 10px;">
						<div class="col-sm-12"><strong>AUTISTA RITORNO</strong></div>
					</div>
					<div class="col-sm-12 row">
						<div class="col-sm-3"><strong>Autoveicolo Autista</strong></div><div class="col-sm-9">{{autoveicoloRichiesto_Ritorno}}</div>
					</div>
					<div class="col-sm-12 row">
						<div class="col-sm-3"><strong>Categoria Auto</strong></div><div class="col-sm-9">{{classeAutoveicoloSceltaCliente_Ritorno}}</div>
					</div>
					<div class="col-sm-12 row">
						<div class="col-sm-3"><strong>Info Categoria Auto</strong></div><div class="col-sm-9">{{descrizioneCategorieAuto_Ritorno}}</div>
					</div>
					{{/ritorno}}
					<div class="col-sm-12 row" style="margin-top: 10px;">
						<div class="col-sm-3"><strong>Prezzo Corsa</strong></div><div class="col-sm-5">{{prezzoCliente}}&euro;</div>
					</div>
					<div class="col-sm-12 row">
						<div class="col-sm-3"><strong>Num Passeggeri</strong></div><div class="col-sm-5">{{numPasseggeri}}</div>
					</div>
					<div class="col-sm-12 row">
						<div class="col-sm-3"><strong>Rimborso</strong></div><div class="col-sm-5">{{rimborsoCliente}}&euro;</div>
					</div>
					<div class="col-sm-12 row">
						<div class="col-sm-3"><strong>Nome e Telefono Passeggero</strong></div><div id="nomeTelefonoPasseggero-{{idcorsa}}" class="col-sm-5">{{nomeTelefonoPasseggero}}</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
{{/idcorsa}}
</script>

<script id="corseMedieClienteTpl" type="text/template">
{{#idcorsa}}
<div id="panel_{{idcorsa}}" class="panel panel-primary" style="margin-top: 10px; margin-bottom: 10px;"> 
	<div class="panel-heading" data-toggle="" data-target="#collapse_{{idcorsa}}" style="font-size: large;">
		<span class="label label-success">{{idcorsa}}</span><span style="padding-left:10px;">{{intestazione}}</span>
			<a class="btn btn-sm btn-info pull-right checkFatturaDisponibile" id="{{idcorsa}}">
				<span class="glyphicon glyphicon-download-alt"></span> Scarica Fattura</a></div>
		<div id="collapse_{{idcorsa}}" class="panel-collapse">
		<div class="panel-body">

			<div class="panel {{andata.colorPanel}}">
				<div class="panel-heading">{{#andata.blinkeffect}}<span class="blinkeffect">&#9899;</span>{{/andata.blinkeffect}} {{andata.intestazione}}</div>
				<div class="panel-body">
				<div class="col-sm-12 row">
						<div class="col-sm-1">ANDATA</div>
						<div class="col-sm-4">{{andata.oraPrelev}} (giorno - ora prelevamento)</div>
						<div class="col-sm-4">{{andata.durata}} (durata transfert prevista)</div>
						<div class="col-sm-3">{{andata.distanza}} (distanza)</div>
					</div>
				</div>
			</div>

			{{#ritorno}}
			<div class="panel {{ritorno.colorPanel}}">
				<div class="panel-heading">{{#ritorno.blinkeffect}}<span class="blinkeffect">&#9899;</span>{{/ritorno.blinkeffect}} {{ritorno.intestazione}}</div>
				<div class="panel-body">
				<div class="col-sm-12 row">
						<div class="col-sm-1">RITORNO</div>
						<div class="col-sm-4">{{ritorno.oraPrelev}} (giorno - ora prelevamento)</div>
						<div class="col-sm-4">{{ritorno.durata}} (durata transfert prevista)</div>
						<div class="col-sm-3">{{ritorno.distanza}} (distanza)</div>
					</div>
				</div>
			</div>
			{{/ritorno}}

			<div class="panel panel-default">
				<div class="panel-heading">INFORMAZIONI CORSA</div>
				<div class="panel-body">
					<div class="col-sm-12 row">
						<div class="col-sm-9">
							<button type="button" id="{{idcorsa}}" class="btn btn-sm btn-primary alertConfirmInfoAutista">
								<span class="glyphicon glyphicon-phone"></span> Info Autista</button>
							<a class="btn btn-sm btn-info openDatiPasseggeroModal" id="{{idcorsa}}">
								<span class="fa fa-user"></span> <fmt:message key="messaggio.modifica.dati.passeggero.corsa"></fmt:message></a>
							<a class="btn btn-sm btn-danger gestioneRimborsoCliente" id="{{idcorsa}}">
								<span class="fa fa-credit-card"></span> <fmt:message key="messaggio.cliente.cancella.corsa"></fmt:message></a>
						</div>
					</div>
					{{#notePerAutista}}
					<div class="col-sm-12 row">
						<div class="col-sm-3"><strong>NOTE PER L'AUTISTA</strong></div>
						<div class="col-sm-9"><em>{{notePerAutista}}</em></div>
					</div>
					{{/notePerAutista}}
					<div class="col-sm-12 row">
						<div class="col-sm-3"><strong>Categoria Auto</strong></div><div class="col-sm-9">{{classeAutoveicoloSceltaCliente}}</div>
					</div>
					<div class="col-sm-12 row">
						<div class="col-sm-3"><strong>Info Categoria Auto</strong></div><div class="col-sm-9">{{descrizioneCategorieAuto}}</div>
					</div>
					<div class="col-sm-12 row">
						<div class="col-sm-3"><strong>Prezzo Corsa</strong></div><div class="col-sm-5">{{prezzoCliente}}&euro;</div>
					</div>
					<div class="col-sm-12 row">
						<div class="col-sm-3"><strong>Num Passeggeri</strong></div>
						<div class="col-sm-5">{{numPasseggeri}}</div>
					</div>
					<div class="col-sm-12 row">
						<div class="col-sm-3"><strong>Rimborso</strong></div><div class="col-sm-5">{{rimborsoCliente}}&euro;</div>
					</div>
					<div class="col-sm-12 row">
						<div class="col-sm-3"><strong>Nome e Telefono Passeggero</strong></div><div id="nomeTelefonoPasseggero-{{idcorsa}}" class="col-sm-5">{{nomeTelefonoPasseggero}}</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
{{/idcorsa}}
</script>

<script id="corseMedieCompletateClienteTpl" type="text/template">
{{#idcorsa}}
<div id="panel_{{idcorsa}}" class="panel panel-primary" style="margin-top: 10px; margin-bottom: 10px;">
	<div class="panel-heading" data-toggle="" data-target="#collapse_{{idcorsa}}" style="font-size: large;">
		<span class="label label-success">{{idcorsa}}</span><span style="padding-left:10px;">{{intestazione}}</span>
			<a class="btn btn-sm btn-info pull-right checkFatturaDisponibile" id="{{idcorsa}}">
				<span class="glyphicon glyphicon-download-alt"></span> Scarica Fattura</a></div>
		<div id="collapse_{{idcorsa}}" class="panel-collapse">
		<div class="panel-body">
			<div class="panel {{andata.colorPanel}}">
				<div class="panel-heading">{{#andata.blinkeffect}}<span class="blinkeffect">&#9899;</span>{{/andata.blinkeffect}} {{andata.intestazione}}</div>
				<div class="panel-body">
				<div class="col-sm-12 row">
						<div class="col-sm-1">ANDATA</div>
						<div class="col-sm-3">{{andata.oraPrelev}} (giorno - ora prelevamento)</div>
						<div class="col-sm-3">{{andata.durata}} (durata transfert prevista)</div>
						<div class="col-sm-3">{{andata.distanza}} (distanza)</div>
					</div>
				</div>
			</div>

			{{#ritorno}}
			<div class="panel {{ritorno.colorPanel}}">
				<div class="panel-heading">{{#ritorno.blinkeffect}}<span class="blinkeffect">&#9899;</span>{{/ritorno.blinkeffect}} {{ritorno.intestazione}}</div>
				<div class="panel-body">
				<div class="col-sm-12 row">
						<div class="col-sm-1">RITORNO</div>
						<div class="col-sm-3">{{ritorno.oraPrelev}} (giorno - ora prelevamento)</div>
						<div class="col-sm-3">{{ritorno.durata}} (durata transfert prevista)</div>
						<div class="col-sm-3">{{ritorno.distanza}} (distanza)</div>
					</div>
				</div>
			</div>
			{{/ritorno}}

			<div class="panel panel-default">
				<div class="panel-heading">INFORMAZIONI CORSA</div>
				<div class="panel-body">
					<div class="col-sm-12 row">
						<div class="col-sm-9">
							<button type="button" id="{{idcorsa}}" class="btn btn-sm btn-primary alertConfirmInfoAutista">
								<span class="glyphicon glyphicon-phone"></span> Info Autista</button>
						</div>
					</div>
					{{#notePerAutista}}
					<div class="col-sm-12 row">
						<div class="col-sm-3"><strong>NOTE PER L'AUTISTA</strong></div>
						<div class="col-sm-9">{{notePerAutista}}</div>
					</div>
					{{/notePerAutista}}
					<div class="col-sm-12 row">
						<div class="col-sm-3"><strong>Categoria Auto</strong></div><div class="col-sm-9">{{classeAutoveicoloSceltaCliente}}</div>
					</div>
					<div class="col-sm-12 row">
						<div class="col-sm-3"><strong>Info Categoria Auto</strong></div><div class="col-sm-9">{{descrizioneCategorieAuto}}</div>
					</div>
					<div class="col-sm-12 row">
						<div class="col-sm-3"><strong>Prezzo Corsa</strong></div><div class="col-sm-5">{{prezzoCliente}}&euro;</div>
					</div>
					<div class="col-sm-12 row">
						<div class="col-sm-3"><strong>Num Passeggeri</strong></div>
						<div class="col-sm-5">{{numPasseggeri}}</div>
					</div>
					<div class="col-sm-12 row">
						<div class="col-sm-3"><strong>Rimborso</strong></div><div class="col-sm-5">{{rimborsoCliente}}&euro;</div>
					</div>
					<div class="col-sm-12 row">
						<div class="col-sm-3"><strong>Nome e Telefono Passeggero</strong></div><div id="nomeTelefonoPasseggero-{{idcorsa}}" class="col-sm-5">{{nomeTelefonoPasseggero}}</div>
					</div>
				</div>
		</div>

	</div>
</div>
</div>
{{/idcorsa}}
</script>

<script id="corseParticolariClienteTpl" type="text/template">
{{#idcorsa}}
<div id="panel_{{idcorsa}}" class="panel panel-primary" style="margin-top: 10px; margin-bottom: 10px;"> 
	<div class="panel-heading" data-toggle="" data-target="#collapse_{{idcorsa}}" style="font-size: large;">
		<span class="label label-success">{{idcorsa}}</span><span style="padding-left:10px;">{{intestazione}}</span>
			<a class="btn btn-sm btn-info pull-right checkFatturaDisponibile" id="{{idcorsa}}">
				<span class="glyphicon glyphicon-download-alt"></span> Scarica Fattura</a></div>
		<div id="collapse_{{idcorsa}}" class="panel-collapse">
		<div class="panel-body">
			<div class="panel {{andata.colorPanel}}">
				<div class="panel-heading">{{#andata.blinkeffect}}<span class="blinkeffect">&#9899;</span>{{/andata.blinkeffect}} {{andata.intestazione}}</div>
				<div class="panel-body">
				<div class="col-sm-12 row">
						<div class="col-sm-1">ANDATA</div>
						<div class="col-sm-4">{{andata.oraPrelev}} (giorno - ora prelevamento)</div>
						<div class="col-sm-4">{{andata.durata}} (durata transfert prevista)</div>
						<div class="col-sm-3">{{andata.distanza}} (distanza)</div>
					</div>
				</div>
			</div>
			{{#ritorno}}
			<div class="panel {{ritorno.colorPanel}}">
				<div class="panel-heading">{{#ritorno.blinkeffect}}<span class="blinkeffect">&#9899;</span>{{/ritorno.blinkeffect}} {{ritorno.intestazione}}</div>
				<div class="panel-body">
				<div class="col-sm-12 row">
						<div class="col-sm-1">RITORNO</div>
						<div class="col-sm-4">{{ritorno.oraPrelev}} (giorno - ora prelevamento)</div>
						<div class="col-sm-4">{{ritorno.durata}} (durata transfert prevista)</div>
						<div class="col-sm-3">{{ritorno.distanza}} (distanza)</div>
					</div>
				</div>
			</div>
			{{/ritorno}}
			<div class="panel panel-default">
				<div class="panel-heading">INFORMAZIONI CORSA</div>
				<div class="panel-body">
					<div class="col-sm-12 row">
						<div class="col-sm-9">
							<button type="button" id="{{idcorsa}}" class="btn btn-sm btn-primary alertConfirmInfoAutista">
								<span class="glyphicon glyphicon-phone"></span> Info Autista</button>
							<a class="btn btn-sm btn-info openDatiPasseggeroModal" id="{{idcorsa}}">
								<span class="fa fa-user"></span> <fmt:message key="messaggio.modifica.dati.passeggero.corsa"></fmt:message></a>
							<a class="btn btn-sm btn-danger gestioneRimborsoCliente" id="{{idcorsa}}">
								<span class="fa fa-credit-card"></span> <fmt:message key="messaggio.cliente.cancella.corsa"></fmt:message></a>
						</div>
					</div>
					{{#notePerAutista}}
					<div class="col-sm-12 row">
						<div class="col-sm-3"><strong>NOTE PER L'AUTISTA</strong></div>
						<div class="col-sm-9"><em>{{notePerAutista}}</em></div>
					</div>
					<div class="col-sm-12 row">
						<div class="col-sm-3"><strong>Autoveicolo Autista</strong></div><div class="col-sm-9">{{autoveicoloRichiesto}}</div>
					</div>
					{{/notePerAutista}}
					<div class="col-sm-12 row">
						<div class="col-sm-3"><strong>Categoria Auto</strong></div><div class="col-sm-9">{{classeAutoveicoloSceltaCliente}}</div>
					</div>
					<div class="col-sm-12 row">
						<div class="col-sm-3"><strong>Info Categoria Auto</strong></div><div class="col-sm-9">{{descrizioneCategorieAuto}}</div>
					</div>
					<div class="col-sm-12 row">
						<div class="col-sm-3"><strong>Prezzo Corsa</strong></div><div class="col-sm-5">{{prezzoCliente}}&euro;</div>
					</div>
					<div class="col-sm-12 row">
						<div class="col-sm-3"><strong>Num Passeggeri</strong></div>
						<div class="col-sm-5">{{numPasseggeri}}</div>
					</div>
					<div class="col-sm-12 row">
						<div class="col-sm-3"><strong>Rimborso</strong></div><div class="col-sm-5">{{rimborsoCliente}}&euro;</div>
					</div>
					<div class="col-sm-12 row">
						<div class="col-sm-3"><strong>Nome e Telefono Passeggero</strong></div><div id="nomeTelefonoPasseggero-{{idcorsa}}" class="col-sm-5">{{nomeTelefonoPasseggero}}</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
{{/idcorsa}}
</script>

<script id="corseParticolariCompletateClienteTpl" type="text/template">
{{#idcorsa}}
<div id="panel_{{idcorsa}}" class="panel panel-primary" style="margin-top: 10px; margin-bottom: 10px;">
	<div class="panel-heading" data-toggle="" data-target="#collapse_{{idcorsa}}" style="font-size: large;">
		<span class="label label-success">{{idcorsa}}</span><span style="padding-left:10px;">{{intestazione}}</span>
			<a class="btn btn-sm btn-info pull-right checkFatturaDisponibile" id="{{idcorsa}}">
				<span class="glyphicon glyphicon-download-alt"></span> Scarica Fattura</a></div>
		<div id="collapse_{{idcorsa}}" class="panel-collapse">
		<div class="panel-body">
			<div class="panel {{andata.colorPanel}}">
				<div class="panel-heading">{{#andata.blinkeffect}}<span class="blinkeffect">&#9899;</span>{{/andata.blinkeffect}} {{andata.intestazione}}</div>
				<div class="panel-body">
				<div class="col-sm-12 row">
						<div class="col-sm-1">ANDATA</div>
						<div class="col-sm-3">{{andata.oraPrelev}} (giorno - ora prelevamento)</div>
						<div class="col-sm-3">{{andata.durata}} (durata transfert prevista)</div>
						<div class="col-sm-3">{{andata.distanza}} (distanza)</div>
					</div>
				</div>
			</div>
			{{#ritorno}}
			<div class="panel {{ritorno.colorPanel}}">
				<div class="panel-heading">{{#ritorno.blinkeffect}}<span class="blinkeffect">&#9899;</span>{{/ritorno.blinkeffect}} {{ritorno.intestazione}}</div>
				<div class="panel-body">
				<div class="col-sm-12 row">
						<div class="col-sm-1">RITORNO</div>
						<div class="col-sm-3">{{ritorno.oraPrelev}} (giorno - ora prelevamento)</div>
						<div class="col-sm-3">{{ritorno.durata}} (durata transfert prevista)</div>
						<div class="col-sm-3">{{ritorno.distanza}} (distanza)</div>
					</div>
				</div>
			</div>
			{{/ritorno}}

			<div class="panel panel-default">
				<div class="panel-heading">INFORMAZIONI CORSA</div>
				<div class="panel-body">
					<div class="col-sm-12 row">
						<div class="col-sm-9">
							<button type="button" id="{{idcorsa}}" class="btn btn-sm btn-primary alertConfirmInfoAutista">
								<span class="glyphicon glyphicon-phone"></span> Info Autista</button>
						</div>
					</div>
					{{#notePerAutista}}
					<div class="col-sm-12 row">
						<div class="col-sm-3"><strong>NOTE PER L'AUTISTA</strong></div>
						<div class="col-sm-9"><em>{{notePerAutista}}</em></div>
					</div>
					{{/notePerAutista}}
					<div class="col-sm-12 row">
						<div class="col-sm-3"><strong>Autoveicolo Autista</strong></div><div class="col-sm-9">{{autoveicoloRichiesto}}</div>
					</div>
					<div class="col-sm-12 row">
						<div class="col-sm-3"><strong>Categoria Auto</strong></div><div class="col-sm-9">{{classeAutoveicoloSceltaCliente}}</div>
					</div>
					<div class="col-sm-12 row">
						<div class="col-sm-3"><strong>Info Categoria Auto</strong></div><div class="col-sm-9">{{descrizioneCategorieAuto}}</div>
					</div>
					<div class="col-sm-12 row">
						<div class="col-sm-3"><strong>Prezzo Corsa</strong></div><div class="col-sm-5">{{prezzoCliente}}&euro;</div>
					</div>
					<div class="col-sm-12 row">
						<div class="col-sm-3"><strong>Num Passeggeri</strong></div>
						<div class="col-sm-5">{{numPasseggeri}}</div>
					</div>
					<div class="col-sm-12 row">
						<div class="col-sm-3"><strong>Rimborso</strong></div><div class="col-sm-5">{{rimborsoCliente}}&euro;</div>
					</div>
					<div class="col-sm-12 row">
						<div class="col-sm-3"><strong>Nome e Telefono Passeggero</strong></div><div id="nomeTelefonoPasseggero-{{idcorsa}}" class="col-sm-5">{{nomeTelefonoPasseggero}}</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
{{/idcorsa}}
</script>

<script id="corseMultipleClienteTpl" type="text/template">
{{#idcorsa}}
<div id="panel_{{idcorsa}}" class="panel panel-primary" style="margin-top: 10px; margin-bottom: 10px;"> 
	<div class="panel-heading" data-toggle="" data-target="#collapse_{{idcorsa}}" style="font-size: large;">
		<span class="label label-success">{{idcorsa}}</span><span style="padding-left:10px;">{{intestazione}}</span>
			<a class="btn btn-sm btn-info pull-right checkFatturaDisponibile" id="{{idcorsa}}">
				<span class="glyphicon glyphicon-download-alt"></span> Scarica Fattura</a></div>
		<div id="collapse_{{idcorsa}}" class="panel-collapse">
		<div class="panel-body">
			<div class="panel {{andata.colorPanel}}">
				<div class="panel-heading">{{#andata.blinkeffect}}<span class="blinkeffect">&#9899;</span>{{/andata.blinkeffect}} {{andata.intestazione}}</div>
				<div class="panel-body">
				<div class="col-sm-12 row">
						<div class="col-sm-1">ANDATA</div>
						<div class="col-sm-4">{{andata.oraPrelev}} (giorno - ora prelevamento)</div>
						<div class="col-sm-4">{{andata.durata}} (durata transfert prevista)</div>
						<div class="col-sm-3">{{andata.distanza}} (distanza)</div>
					</div>
				</div>
			</div>
			{{#ritorno}}
			<div class="panel {{ritorno.colorPanel}}">
				<div class="panel-heading">{{#ritorno.blinkeffect}}<span class="blinkeffect">&#9899;</span>{{/ritorno.blinkeffect}} {{ritorno.intestazione}}</div>
				<div class="panel-body">
				<div class="col-sm-12 row">
						<div class="col-sm-1">RITORNO</div>
						<div class="col-sm-4">{{ritorno.oraPrelev}} (giorno - ora prelevamento)</div>
						<div class="col-sm-4">{{ritorno.durata}} (durata transfert prevista)</div>
						<div class="col-sm-3">{{ritorno.distanza}} (distanza)</div>
					</div>
				</div>
			</div>
			{{/ritorno}}
			<div class="panel panel-default">
				<div class="panel-heading">INFORMAZIONI CORSA</div>
				<div class="panel-body">
					<div class="col-sm-12 row">
						<div class="col-sm-9">
							<button type="button" id="{{idcorsa}}" class="btn btn-sm btn-primary alertConfirmInfoAutista">
								<span class="glyphicon glyphicon-phone"></span> Info Autista</button>
							<a class="btn btn-sm btn-info openDatiPasseggeroModal" id="{{idcorsa}}">
								<span class="fa fa-user"></span> <fmt:message key="messaggio.modifica.dati.passeggero.corsa"></fmt:message></a>
							<a class="btn btn-sm btn-danger gestioneRimborsoCliente" id="{{idcorsa}}">
								<span class="fa fa-credit-card"></span> <fmt:message key="messaggio.cliente.cancella.corsa"></fmt:message></a>
						</div>
					</div>
					{{#notePerAutista}}
					<div class="col-sm-12 row">
						<div class="col-sm-3"><strong>NOTE PER L'AUTISTA</strong></div>
						<div class="col-sm-9"><em>{{notePerAutista}}</em></div>
					</div>
					{{/notePerAutista}}
					
					{{#joCorsaPartList}}
						<div class="col-sm-12 row" style="margin-top: 15px;">
							<div class="col-sm-3"><strong>Autista Autoveicolo</strong></div><div class="col-sm-9">
							<b>{{fullNameAutisa}}</b> - {{autoveicoloRichiesto}} - {{classeAutoveicoloSceltaCliente}} - ({{descrizioneCategorieAuto}}) 
							<br>
							<b>PREZZO:</b> {{prezzoCliente}}&euro;
							<b>RIMBORSO:</b> {{rimborsoCliente}}&euro;
							</div>
						</div>
					{{/joCorsaPartList}}
					
					<div class="col-sm-12 row" style="margin-top: 15px;">
						<div class="col-sm-3"><strong>Prezzo Corsa</strong></div><div class="col-sm-5">{{prezzoClienteTotale}}&euro;</div>
					</div>
					<div class="col-sm-12 row">
						<div class="col-sm-3"><strong>Num Passeggeri</strong></div>
						<div class="col-sm-5">{{numPasseggeriTotale}}</div>
					</div>
					<div class="col-sm-12 row">
						<div class="col-sm-3"><strong>Rimborso</strong></div><div class="col-sm-5">{{rimborsoClienteTotale}}&euro;</div>
					</div>
					<div class="col-sm-12 row">
						<div class="col-sm-3"><strong>Nome e Telefono Passeggero</strong></div><div id="nomeTelefonoPasseggero-{{idcorsa}}" class="col-sm-5">{{nomeTelefonoPasseggero}}</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
{{/idcorsa}}
</script>

<script id="corseMultipleCompletateClienteTpl" type="text/template">
{{#idcorsa}}
<div id="panel_{{idcorsa}}" class="panel panel-primary" style="margin-top: 10px; margin-bottom: 10px;"> 
	<div class="panel-heading" data-toggle="" data-target="#collapse_{{idcorsa}}" style="font-size: large;">
		<span class="label label-success">{{idcorsa}}</span><span style="padding-left:10px;">{{intestazione}}</span>
			<a class="btn btn-sm btn-info pull-right checkFatturaDisponibile" id="{{idcorsa}}">
				<span class="glyphicon glyphicon-download-alt"></span> Scarica Fattura</a></div>
		<div id="collapse_{{idcorsa}}" class="panel-collapse">
		<div class="panel-body">
			<div class="panel {{andata.colorPanel}}">
				<div class="panel-heading">{{#andata.blinkeffect}}<span class="blinkeffect">&#9899;</span>{{/andata.blinkeffect}} {{andata.intestazione}}</div>
				<div class="panel-body">
				<div class="col-sm-12 row">
						<div class="col-sm-1">ANDATA</div>
						<div class="col-sm-4">{{andata.oraPrelev}} (giorno - ora prelevamento)</div>
						<div class="col-sm-4">{{andata.durata}} (durata transfert prevista)</div>
						<div class="col-sm-3">{{andata.distanza}} (distanza)</div>
					</div>
				</div>
			</div>
			{{#ritorno}}
			<div class="panel {{ritorno.colorPanel}}">
				<div class="panel-heading">{{#ritorno.blinkeffect}}<span class="blinkeffect">&#9899;</span>{{/ritorno.blinkeffect}} {{ritorno.intestazione}}</div>
				<div class="panel-body">
				<div class="col-sm-12 row">
						<div class="col-sm-1">RITORNO</div>
						<div class="col-sm-4">{{ritorno.oraPrelev}} (giorno - ora prelevamento)</div>
						<div class="col-sm-4">{{ritorno.durata}} (durata transfert prevista)</div>
						<div class="col-sm-3">{{ritorno.distanza}} (distanza)</div>
					</div>
				</div>
			</div>
			{{/ritorno}}
			<div class="panel panel-default">
				<div class="panel-heading">INFORMAZIONI CORSA</div>
				<div class="panel-body">
					<div class="col-sm-12 row">
						<div class="col-sm-9">
							<button type="button" id="{{idcorsa}}" class="btn btn-sm btn-primary alertConfirmInfoAutista">
								<span class="glyphicon glyphicon-phone"></span> Info Autista</button>
						</div>
					</div>
					{{#notePerAutista}}
					<div class="col-sm-12 row">
						<div class="col-sm-3"><strong>NOTE PER L'AUTISTA</strong></div>
						<div class="col-sm-9"><em>{{notePerAutista}}</em></div>
					</div>
					{{/notePerAutista}}
					
					{{#joCorsaPartList}}
						<div class="col-sm-12 row" style="margin-top: 15px;">
							<div class="col-sm-3"><strong>Autista Autoveicolo</strong></div><div class="col-sm-9">
							<b>{{fullNameAutisa}}</b> - {{autoveicoloRichiesto}} - {{classeAutoveicoloSceltaCliente}} - ({{descrizioneCategorieAuto}}) 
							<br>
							<b>PREZZO:</b> {{prezzoCliente}}&euro;
							<b>RIMBORSO:</b> {{rimborsoCliente}}&euro;
							</div>
						</div>
					{{/joCorsaPartList}}
					
					<div class="col-sm-12 row" style="margin-top: 15px;">
						<div class="col-sm-3"><strong>Prezzo Corsa</strong></div><div class="col-sm-5">{{prezzoClienteTotale}}&euro;</div>
					</div>
					<div class="col-sm-12 row">
						<div class="col-sm-3"><strong>Num Passeggeri</strong></div>
						<div class="col-sm-5">{{numPasseggeriTotale}}</div>
					</div>
					<div class="col-sm-12 row">
						<div class="col-sm-3"><strong>Rimborso</strong></div><div class="col-sm-5">{{rimborsoClienteTotale}}&euro;</div>
					</div>
					<div class="col-sm-12 row">
						<div class="col-sm-3"><strong>Nome e Telefono Passeggero</strong></div><div id="nomeTelefonoPasseggero-{{idcorsa}}" class="col-sm-5">{{nomeTelefonoPasseggero}}</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
{{/idcorsa}}
</script>

