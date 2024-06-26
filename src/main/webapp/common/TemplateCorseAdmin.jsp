<script id="corsaAgendaAutistaAdminTpl" type="text/template">
{{#idcorsa}}
<div id="panel_{{idcorsa}}" class="panel panel-primary">
	<div onclick="$.ajax({type:'POST', url:'${pageContext.request.contextPath}/impostaCollapsePanelCorsaAdmin',dataType:'text',
		data: {idRicTransfert:{{idcorsa}} },error: function (req, status, error) {alert(error);}});"
			class="panel-heading" data-toggle="{{collapsePanel}}" data-target="#collapse_{{idcorsa}}" style="font-size: large;">
		<span class="label label-success">{{idcorsa}}</span><span style="padding-left:10px;">{{intestazione}}</span>
			<span style="font-size: x-large; padding-left:10px;" class="glyphicon {{flagApprov_A}}"></span>
			{{#ritorno}}
				{{#flagApprov_R}}<span style="font-size: x-large; padding-left:10px;" class="glyphicon {{flagApprov_R}}"></span>{{/flagApprov_R}}{{/ritorno}}
			{{#ritardo.numeroMezzoreAndata}}
				<span style="font-size: x-large; padding-left:10px;" class="glyphicon glyphicon-time">A{{ritardo.numeroMezzoreAndata}}</span>{{/ritardo.numeroMezzoreAndata}}
			{{#ritardo.numeroMezzoreRitorno}}
				<span style="font-size: x-large; padding-left:10px;" class="glyphicon glyphicon-time">R{{ritardo.numeroMezzoreRitorno}}</span>{{/ritardo.numeroMezzoreRitorno}}
	</div>
	<div id="collapse_{{idcorsa}}" class="panel-collapse {{collapsePanel}}"><!-- aggiungi collapse al class se lo vuoi chiuso -->
	<div class="panel-body">
		<div class="panel {{andata.colorPanel}}">
		<div class="panel-heading">{{#andata.blinkeffect}}<span class="blinkeffect">&#9899;</span>{{/andata.blinkeffect}} {{andata.intestazione}}</div>
		<div class="panel-body">
		<div class="col-sm-12 row">
			<div class="col-sm-2">ANDATA</div>
			<div class="col-sm-2">{{andata.oraPrelev}}</div>
			<div class="col-sm-1">{{andata.durata}}</div>
			<div class="col-sm-1">{{andata.distanza}}</div>
			<div class="col-sm-6">
				<label class="checkbox-inline text-warning">
					<input type="radio" onchange="$.ajax({type:'POST', url:'${pageContext.request.contextPath}/impostaApprovazioneCorsaAdmin',dataType:'text',
						data: {idRicTransfert:{{idcorsa}},idAutista_Andata:{{idAutista_Andata}},andataRitorno:'A',numApprov:1 },error: function (req, status, error) {alert(error);}});"
						name="approvata-A-{{idcorsa}}" {{andata.radioInApprov}}><b> IN APPROVAZIONE</b>
				</label>
				<label class="checkbox-inline text-success">
					<input type="radio" onchange="$.ajax({type:'POST', url:'${pageContext.request.contextPath}/impostaApprovazioneCorsaAdmin',dataType:'text',
						data: {idRicTransfert:{{idcorsa}},idAutista_Andata:{{idAutista_Andata}},andataRitorno:'A',numApprov:2 },error: function (req, status, error) {alert(error);}});"
						name="approvata-A-{{idcorsa}}" {{andata.radioApprov}}><b> APPROVATA</b>
				</label>
				<label class="checkbox-inline text-danger">
					<input type="radio" onchange="$.ajax({type:'POST', url:'${pageContext.request.contextPath}/impostaApprovazioneCorsaAdmin',dataType:'text',
						data: {idRicTransfert:{{idcorsa}},idAutista_Andata:{{idAutista_Andata}},andataRitorno:'A',numApprov:3 },error: function (req, status, error) {alert(error);}});"
						name="approvata-A-{{idcorsa}}" {{andata.radioNonApprov}}><b> NON APPROVATA</b>
				</label>
			</div>
		</div>
		</div>
		</div>
		{{#ritorno}}
		<div class="panel {{ritorno.colorPanel}}">
		<div class="panel-heading">{{#ritorno.blinkeffect}}<span class="blinkeffect">&#9899;</span>{{/ritorno.blinkeffect}} {{ritorno.intestazione}}</div>
		<div class="panel-body">
		<div class="col-sm-12 row">
			<div class="col-sm-2">RITORNO</div>
			<div class="col-sm-2">{{ritorno.oraPrelev}}</div>
			<div class="col-sm-1">{{ritorno.durata}}</div>
			<div class="col-sm-1">{{ritorno.distanza}}</div>
			<div class="col-sm-6">
				<label class="checkbox-inline text-warning">
					<input type="radio" onchange="$.ajax({type:'POST', url:'${pageContext.request.contextPath}/impostaApprovazioneCorsaAdmin',dataType:'text',
						data: {idRicTransfert:{{idcorsa}},idAutista_Ritorno:{{idAutista_Ritorno}},andataRitorno:'R',numApprov:1 },error: function (req, status, error) {alert(error);}});"
						name="approvata-R-{{idcorsa}}" {{ritorno.radioInApprov}}><b> IN APPROVAZIONE</b>
				</label>
				<label class="checkbox-inline text-success">
					<input type="radio" onchange="$.ajax({type:'POST', url:'${pageContext.request.contextPath}/impostaApprovazioneCorsaAdmin',dataType:'text',
						data: {idRicTransfert:{{idcorsa}},idAutista_Ritorno:{{idAutista_Ritorno}},andataRitorno:'R',numApprov:2 },error: function (req, status, error) {alert(error);}});"
						name="approvata-R-{{idcorsa}}" {{ritorno.radioApprov}}><b> APPROVATA</b>
				</label>
				<label class="checkbox-inline text-danger">
					<input type="radio" onchange="$.ajax({type:'POST', url:'${pageContext.request.contextPath}/impostaApprovazioneCorsaAdmin',dataType:'text',
						data: {idRicTransfert:{{idcorsa}},idAutista_Ritorno:{{idAutista_Ritorno}},andataRitorno:'R',numApprov:3 },error: function (req, status, error) {alert(error);}});"
						name="approvata-R-{{idcorsa}}" {{ritorno.radioNonApprov}}><b> NON APPROVATA</b>
				</label>
			</div>
		</div>
		</div>
		</div>
		{{/ritorno}}
		<div class="panel panel-default">
		<div class="panel-heading">INFORMAZIONI CORSA</div>
		<div class="panel-body">
			<div class="col-sm-12 row">
				<div class="col-sm-1"><b>AUTISTA ANDATA</b></div>
				<div class="col-sm-2">{{fullNameAutisa_Andata}}</div>
				<div class="col-sm-2">
					<a class="btn btn-sm btn-primary" href="tel:{{telAutista_Andata}}"><span class="glyphicon glyphicon-phone"></span> {{telAutista_Andata}}</a>
				</div>
				<div class="col-sm-2">{{corseEffAutista_Andata}} (corse.eff.)</div>
				<div class="col-sm-2">{{percServAutista_Andata}}&#37; (perc.serv.)</div>
				<div class="col-sm-2">{{prezzoCommServ_Andata}}&euro; (comm.serv.)</div>
				<div class="col-sm-1">{{prezzoAutista_Andata}}&euro;</div>
			</div>
			<div class="col-sm-12 row" style="margin-top: 15px;">
				<div class="col-sm-3">AUTOVEICOLO RICHIESTO:</div>
				<div class="col-sm-9">{{autoveicoloRichiesto_Andata}}</div>
			</div>
			<div class="col-sm-12 row" style="margin-bottom: 15px;">
				<div class="col-sm-3">INFO AUTISTA:</div>
				<div class="col-sm-9">{{noteAutista_Andata}}</div>
			</div>
			{{#ritorno}}
			<div class="col-sm-12 row">
				<div class="col-sm-1"><b>AUTISTA RITORNO</b></div>
				<div class="col-sm-2">{{fullNameAutisa_Ritorno}}</div>
				<div class="col-sm-2">
					<a class="btn btn-sm btn-primary" href="tel:{{telAutista_Ritorno}}"><span class="glyphicon glyphicon-phone"></span> {{telAutista_Andata}}</a>
				</div>
				<div class="col-sm-2">{{corseEffAutista_Ritorno}} (corse.eff.)</div>
				<div class="col-sm-2">{{percServAutista_Ritorno}}&#37; (perc.serv.)</div>
				<div class="col-sm-2">{{prezzoCommServ_Ritorno}}&euro; (comm.serv.)</div>
				<div class="col-sm-1">{{prezzoAutista_Ritorno}}&euro;</div>
			</div>
			<div class="col-sm-12 row" style="margin-top: 15px;">
				<div class="col-sm-3">AUTOVEICOLO RICHIESTO:</div>
				<div class="col-sm-9">{{autoveicoloRichiesto_Ritorno}}</div>
			</div>
			<div class="col-sm-12 row" style="margin-bottom: 15px;">
				<div class="col-sm-3">INFO AUTISTA:</div>
				<div class="col-sm-9">{{noteAutista_Ritorno}}</div>
			</div>
			{{/ritorno}}
			{{#notePerAutista}}
			<div class="col-sm-12 row">
				<div class="col-sm-3">NOTE PER L'AUTISTA:</div>
				<div class="col-sm-9">{{notePerAutista}}</div>
			</div>
			{{/notePerAutista}}
			<div class="col-sm-12 row ">
					<div class="col-sm-1"><b>CLIENTE</b></div>
					<div class="col-sm-2">{{fullNameCliente}}</div>
					<div class="col-sm-2">
						<a class="btn btn-sm btn-primary" href="tel:{{telCliente}}"><span class="glyphicon glyphicon-phone"></span> {{telCliente}}</a></div>
					<div class="col-sm-1">{{prezzoCliente}}&euro;</div>
			</div>
			{{#noteCliente}}
			<div class="col-sm-12 row" style="margin-top: 15px;">
				<div class="col-sm-12">NOTE CLIENTE: {{noteCliente}}</div>
			</div>
			{{/noteCliente}}
			<div class="col-sm-12 row" style="margin-top: 15px;">
				<div class="col-sm-4">
					<button type="button" id="{{idcorsa}}" name="andata" value="{{ritardo.numeroMezzoreAndata}}" class="btn btn-sm btn-success insertRitardo">
						<span class="glyphicon glyphicon-time"></span> <span class="fa fa-pencil-square-o"></span> Inserisci Num. Mezzore di Ritardo Andata {{ritardo.numeroMezzoreAndata}}</button>
				</div>
				{{#ritorno}}
					<div class="col-sm-4">
						<button type="button" id="{{idcorsa}}" name="ritorno" value="{{ritardo.numeroMezzoreRitorno}}" class="btn btn-sm btn-success insertRitardo">
							<span class="glyphicon glyphicon-time"></span> <span class="fa fa-pencil-square-o"></span> Inserisci Num. Mezzore di Ritaro Ritorno {{ritardo.numeroMezzoreRitorno}}</button>
					</div>
				{{/ritorno}}
				{{#ritardo}}
				<div class="col-sm-3">
				<a class="btn btn-sm btn-info gestioneRitardo" id="{{ritardo.id}}">
					<span class="fa fa-share"></span> <span class="glyphicon glyphicon-time"></span> Gestisci Ritardo</a>
				</div>
				<div class="col-sm-3">
				<a class="btn btn-sm btn-info checkFatturaRitardoDisponibile" id="{{idcorsa}}">
					<span class="glyphicon glyphicon-download-alt"></span> <span class="glyphicon glyphicon-time"></span> Scarica Fattura Ritardo</a>
				</div>
				{{/ritardo}}
			</div>
			<div class="col-sm-12 row" style="margin-top: 15px;">
				<div class="col-sm-3">
				<a class="btn btn-sm btn-info paginaGestioneSupplementi" id="">
					<span class="fa fa-share"></span> <span class="glyphicon glyphicon-time"></span> Pagina Gestione Supplementi</a>
				</div>
			</div>
			<!-- Supplemento -->
			{{#supplemento}}
			<div class="col-sm-12 row" style="margin-top: 15px;">
				<div class="col-sm-3">
				<a class="btn btn-sm btn-info gestioneSupplemento" id="{{id}}">
					<span class="fa fa-share"></span> <span class="glyphicon glyphicon-time"></span> Gestisci Supplemento</a>
				</div>
				<div class="col-sm-3">
					supplemento.id: {{id}} supplementoPrezzo: {{prezzo}} supplementoPagato: {{pagato}} supplementoDescrizione: {{descrizione}}
				</div>
				<div class="col-sm-3">
				<a class="btn btn-sm btn-info checkFatturaSupplementoDisponibile" id="{{id}}">
					<span class="glyphicon glyphicon-download-alt"></span> <span class="glyphicon glyphicon-time"></span> Scarica Fattura Supplemento</a>
				</div>
			</div>
			{{/supplemento}}
			<!-- Fatturazione -->
			<div class="col-sm-12 row " style="margin-top: 15px;">
				<div class="col-sm-2">
				<a class="btn btn-sm btn-danger modificaTransfer" id="{{idcorsa}}">
					<span class="fa fa-share"></span> Modifica Transfer</a>
				</div>
				<div class="col-sm-2">
				<a class="btn btn-sm btn-success gestioneRimborso" id="{{idcorsa}}">
					<span class="fa fa-share"></span> <span class="fa fa-credit-card"></span> Gestisci Rimborso</a>
				</div>
				<div class="col-sm-2">
				<a class="btn btn-sm btn-info checkFatturaDisponibile" id="{{idcorsa}}">
					<span class="glyphicon glyphicon-download-alt"></span> Scarica Fattura Corsa</a>
				</div>
				<div class="col-sm-3">
				<a class="btn btn-sm btn-info inviaEmailCorsaAcquistataCliente" id="{{idcorsa}}">
					<span class="fa fa-envelope"></span> <span class="fa fa-user"></span> <fmt:message key="messaggio.invia.email.corsa.acquistata.cliente"></fmt:message></a>
				</div>
			</div>
			<div class="col-sm-12 row" style="margin-top: 15px;">
				<div class="col-sm-12">
					<textarea id="{{idcorsa}}" class="form-control alertConfirmNoteCorsa" rows="3" placeholder="scrivi note corsa...." >{{noteCorsa}}</textarea>
				</div>
			</div>
		</div>
		</div>
	</div>
	</div>
</div>
{{/idcorsa}}
</script>

<script id="corseMultipleAdminTpl" type="text/template">
{{#idcorsa}}
<div id="panel_{{idcorsa}}" class="panel panel-primary">
	<div onclick="$.ajax({type:'POST', url:'${pageContext.request.contextPath}/impostaCollapsePanelCorsaAdmin',dataType:'text',
		data: {idRicTransfert:{{idcorsa}} },error: function (req, status, error) {alert(error);}});"
			class="panel-heading" data-toggle="{{collapsePanel}}" data-target="#collapse_{{idcorsa}}" style="font-size: large;">
		<span class="label label-success">{{idcorsa}}</span><span style="padding-left:10px;">{{intestazione}}</span>
			<span style="font-size: x-large; padding-left:10px;" class="glyphicon {{flagApprov_A}}"></span>
			{{#ritorno}}
				{{#flagApprov_R}}<span style="font-size: x-large; padding-left:10px;" class="glyphicon {{flagApprov_R}}"></span>{{/flagApprov_R}}{{/ritorno}}
			{{#ritardo.numeroMezzoreAndata}}
				<span style="font-size: x-large; padding-left:10px;" class="glyphicon glyphicon-time">A{{ritardo.numeroMezzoreAndata}}</span>{{/ritardo.numeroMezzoreAndata}}
			{{#ritardo.numeroMezzoreRitorno}}
				<span style="font-size: x-large; padding-left:10px;" class="glyphicon glyphicon-time">R{{ritardo.numeroMezzoreRitorno}}</span>{{/ritardo.numeroMezzoreRitorno}}
	</div>
	<div id="collapse_{{idcorsa}}" class="panel-collapse {{collapsePanel}}"><!-- aggiungi collapse al class se lo vuoi chiuso -->
	<div class="panel-body">
		<div class="panel {{andata.colorPanel}}">
		<div class="panel-heading">{{#andata.blinkeffect}}<span class="blinkeffect">&#9899;</span>{{/andata.blinkeffect}} {{andata.intestazione}}</div>
		<div class="panel-body">
		<div class="col-sm-12 row">
			<div class="col-sm-2">ANDATA</div>
			<div class="col-sm-2">{{andata.oraPrelev}}</div>
			<div class="col-sm-1">{{andata.durata}}</div>
			<div class="col-sm-1">{{andata.distanza}}</div>
			<div class="col-sm-6">
				<label class="checkbox-inline text-warning">
					<input type="radio" onchange="$.ajax({type:'POST', url:'${pageContext.request.contextPath}/impostaApprovazioneCorsaAdmin',dataType:'text',
						data: {idRicTransfert:{{idcorsa}},idAutista:null,andataRitorno:'A',numApprov:1 },error: function (req, status, error) {alert(error);}});"
						name="approvata-A-{{idcorsa}}" {{andata.radioInApprov}}><b> IN APPROVAZIONE</b>
				</label>
				<label class="checkbox-inline text-success">
					<input type="radio" onchange="$.ajax({type:'POST', url:'${pageContext.request.contextPath}/impostaApprovazioneCorsaAdmin',dataType:'text',
						data: {idRicTransfert:{{idcorsa}},idAutista:null,andataRitorno:'A',numApprov:2 },error: function (req, status, error) {alert(error);}});"
						name="approvata-A-{{idcorsa}}" {{andata.radioApprov}}><b> APPROVATA</b>
				</label>
				<label class="checkbox-inline text-danger">
					<input type="radio" onchange="$.ajax({type:'POST', url:'${pageContext.request.contextPath}/impostaApprovazioneCorsaAdmin',dataType:'text',
						data: {idRicTransfert:{{idcorsa}},idAutista:null,andataRitorno:'A',numApprov:3 },error: function (req, status, error) {alert(error);}});"
						name="approvata-A-{{idcorsa}}" {{andata.radioNonApprov}}><b> NON APPROVATA</b>
				</label>
			</div>
		</div>
		</div>
		</div>
		{{#ritorno}}
		<div class="panel {{ritorno.colorPanel}}">
		<div class="panel-heading">{{#ritorno.blinkeffect}}<span class="blinkeffect">&#9899;</span>{{/ritorno.blinkeffect}} {{ritorno.intestazione}}</div>
		<div class="panel-body">
		<div class="col-sm-12 row">
			<div class="col-sm-2">RITORNO</div>
			<div class="col-sm-2">{{ritorno.oraPrelev}}</div>
			<div class="col-sm-1">{{ritorno.durata}}</div>
			<div class="col-sm-1">{{ritorno.distanza}}</div>
			<div class="col-sm-6">
				<label class="checkbox-inline text-warning">
					<input type="radio" onchange="$.ajax({type:'POST', url:'${pageContext.request.contextPath}/impostaApprovazioneCorsaAdmin',dataType:'text',
						data: {idRicTransfert:{{idcorsa}},idAutista:null,andataRitorno:'R',numApprov:1 },error: function (req, status, error) {alert(error);}});"
						name="approvata-R-{{idcorsa}}" {{ritorno.radioInApprov}}><b> IN APPROVAZIONE</b>
				</label>
				<label class="checkbox-inline text-success">
					<input type="radio" onchange="$.ajax({type:'POST', url:'${pageContext.request.contextPath}/impostaApprovazioneCorsaAdmin',dataType:'text',
						data: {idRicTransfert:{{idcorsa}},idAutista:null,andataRitorno:'R',numApprov:2 },error: function (req, status, error) {alert(error);}});"
						name="approvata-R-{{idcorsa}}" {{ritorno.radioApprov}}><b> APPROVATA</b>
				</label>
				<label class="checkbox-inline text-danger">
					<input type="radio" onchange="$.ajax({type:'POST', url:'${pageContext.request.contextPath}/impostaApprovazioneCorsaAdmin',dataType:'text',
						data: {idRicTransfert:{{idcorsa}},idAutista:null,andataRitorno:'R',numApprov:3 },error: function (req, status, error) {alert(error);}});"
						name="approvata-R-{{idcorsa}}" {{ritorno.radioNonApprov}}><b> NON APPROVATA</b>
				</label>
			</div>
		</div>
		</div>
		</div>
		{{/ritorno}}
		<div class="panel panel-default">
		<div class="panel-heading">INFORMAZIONI CORSA</div>
		<div class="panel-body">
			<!-- FINE DATI AUTISTI CORSA MULTIPLA -->
			{{#joCorsaPartList}}
				<div class="col-sm-12 row">
					<div class="col-sm-3"><b>Autista Autoveicolo</b></div><div class="col-sm-9">
					<b>{{fullNameAutisa}}</b> - {{autoveicoloRichiesto}} - {{classeAutoveicoloSceltaCliente}} - ({{descrizioneCategorieAuto}}) 
					<br>
					<b>PREZZO:</b> {{prezzoCliente}} 
					<b>RIMBORSO:</b> {{rimborsoCliente}}
					{{#noteAutista}}<br><b>INFO AUTISTA:</b> {{noteAutista}}{{/noteAutista}}
					</div>
				</div>
			{{/joCorsaPartList}}
			<div class="col-sm-12 row" style="margin-top: 15px;">
				<div class="col-sm-3"><b>Prezzo Corsa</b></div><div class="col-sm-5">{{prezzoClienteTotale}}&euro;</div>
			</div>
			<div class="col-sm-12 row">
				<div class="col-sm-3"><b>Num Passeggeri</b></div>
				<div class="col-sm-5">{{numPasseggeriTotale}}</div>
			</div>
			<div class="col-sm-12 row">
				<div class="col-sm-3"><b>Rimborso</b></div><div class="col-sm-5">{{rimborsoClienteTotale}}&euro;</div>
			</div>
			<div class="col-sm-12 row">
				<div class="col-sm-3"><b>Nome e Telefono Passeggero</b></div><div id="nomeTelefonoPasseggero-{{idcorsa}}" class="col-sm-5">{{nomeTelefonoPasseggero}}</div>
			</div>
			<!-- FINE DATI AUTISTI CORSA MULTIPLA -->
			{{#notePerAutista}}
			<div class="col-sm-12 row">
				<div class="col-sm-3"><b>NOTE PER L'AUTISTA:</b></div>
				<div class="col-sm-9">{{notePerAutista}}</div>
			</div>
			{{/notePerAutista}}
			<div class="col-sm-12 row " style="margin-top: 15px;">
					<div class="col-sm-1"><b>CLIENTE</b></div>
					<div class="col-sm-2">{{fullNameCliente}}</div>
					<div class="col-sm-2">
						<a class="btn btn-sm btn-primary" href="tel:{{telCliente}}"><span class="glyphicon glyphicon-phone"></span> {{telCliente}}</a>
					</div>
			</div>
			{{#noteCliente}}
			<div class="col-sm-12 row" style="margin-top: 15px;">
				<div class="col-sm-12">NOTE CLIENTE: {{noteCliente}}</div>
			</div>
			{{/noteCliente}}
			<div class="col-sm-12 row" style="margin-top: 15px;">
				<div class="col-sm-4">
					<button type="button" id="{{idcorsa}}" name="andata" value="{{ritardo.numeroMezzoreAndata}}" class="btn btn-sm btn-success insertRitardo">
						<span class="glyphicon glyphicon-time"></span> <span class="fa fa-pencil-square-o"></span> Inserisci Num. Mezzore di Ritaro Andata {{ritardo.numeroMezzoreAndata}}</button>
				</div>
				{{#ritorno}}
					<div class="col-sm-4">
						<button type="button" id="{{idcorsa}}" name="ritorno" value="{{ritardo.numeroMezzoreRitorno}}" class="btn btn-sm btn-success insertRitardo">
							<span class="glyphicon glyphicon-time"></span> <span class="fa fa-pencil-square-o"></span> Inserisci Num. Mezzore di Ritaro Ritorno {{ritardo.numeroMezzoreRitorno}}</button>
					</div>
				{{/ritorno}}
				{{#ritardo}}
				<div class="col-sm-3">
				<a class="btn btn-sm btn-info gestioneRitardo" id="{{ritardo.id}}">
					<span class="fa fa-share"></span> <span class="glyphicon glyphicon-time"></span> Gestisci Ritardo</a>
				</div>
				<div class="col-sm-3">
				<a class="btn btn-sm btn-info checkFatturaRitardoDisponibile" id="{{idcorsa}}">
					<span class="glyphicon glyphicon-download-alt"></span> <span class="glyphicon glyphicon-time"></span> Scarica Fattura Ritardo</a>
				</div>
				{{/ritardo}}
			</div>
			<div class="col-sm-12 row" style="margin-top: 15px;">
				<div class="col-sm-3">
				<a class="btn btn-sm btn-info paginaGestioneSupplementi" id="">
					<span class="fa fa-share"></span> <span class="glyphicon glyphicon-time"></span> Pagina Gestione Supplementi</a>
				</div>
			</div>
			<!-- Supplemento -->
			{{#supplemento}}
			<div class="col-sm-12 row" style="margin-top: 15px;">
				<div class="col-sm-3">
				<a class="btn btn-sm btn-info gestioneSupplemento" id="{{id}}">
					<span class="fa fa-share"></span> <span class="glyphicon glyphicon-time"></span> Gestisci Supplemento</a>
				</div>
				<div class="col-sm-3">
					supplemento.id: {{id}} supplementoPrezzo: {{prezzo}} supplementoPagato: {{pagato}} supplementoDescrizione: {{descrizione}}
				</div>
				<div class="col-sm-3">
				<a class="btn btn-sm btn-info checkFatturaSupplementoDisponibile" id="{{id}}">
					<span class="glyphicon glyphicon-download-alt"></span> <span class="glyphicon glyphicon-time"></span> Scarica Fattura Supplemento</a>
				</div>
			</div>
			{{/supplemento}}
			<!-- Fatturazione -->
			<div class="col-sm-12 row " style="margin-top: 15px;">
				<div class="col-sm-2">
				<a class="btn btn-sm btn-danger modificaTransfer" id="{{idcorsa}}">
					<span class="fa fa-share"></span> Modifica Transfer</a>
				</div>
				<div class="col-sm-2">
				<a class="btn btn-sm btn-success gestioneRimborso" id="{{idcorsa}}">
					<span class="fa fa-share"></span> <span class="fa fa-credit-card"></span> Gestisci Rimborso</a>
				</div>
				<div class="col-sm-2">
				<a class="btn btn-sm btn-info checkFatturaDisponibile" id="{{idcorsa}}">
					<span class="glyphicon glyphicon-download-alt"></span> Scarica Fattura Corsa</a>
				</div>
				<div class="col-sm-3">
				<a class="btn btn-sm btn-info inviaEmailCorsaAcquistataCliente" id="{{idcorsa}}">
					<span class="fa fa-envelope"></span> <span class="fa fa-user"></span> <fmt:message key="messaggio.invia.email.corsa.acquistata.cliente"></fmt:message></a>
				</div>
			</div>
			<div class="col-sm-12 row" style="margin-top: 15px;">
				<div class="col-sm-12">
					<textarea id="{{idcorsa}}" class="form-control alertConfirmNoteCorsa" rows="3" placeholder="scrivi note corsa...." >{{noteCorsa}}</textarea>
				</div>
			</div>
		</div>
		</div>
	</div>
	</div>
</div>
{{/idcorsa}}
</script>

<script id="corsaParticolareAdminTpl" type="text/template">
{{#idcorsa}}
<div id="panel_{{idcorsa}}" class="panel panel-primary">
	<div onclick="$.ajax({type:'POST', url:'${pageContext.request.contextPath}/impostaCollapsePanelCorsaAdmin',dataType:'text',
		data: {idRicTransfert:{{idcorsa}} },error: function (req, status, error) {alert(error);}});"
			class="panel-heading" data-toggle="{{collapsePanel}}" data-target="#collapse_{{idcorsa}}" style="font-size: large;">
		<span class="label label-success">{{idcorsa}}</span><span style="padding-left:10px;">{{intestazione}}</span>
			<span style="font-size: x-large; padding-left:10px;" class="glyphicon {{flagApprov_A}}"></span>
			{{#ritorno}}
				{{#flagApprov_R}}<span style="font-size: x-large; padding-left:10px;" class="glyphicon {{flagApprov_R}}"></span>{{/flagApprov_R}}{{/ritorno}}
			{{#ritardo.numeroMezzoreAndata}}
				<span style="font-size: x-large; padding-left:10px;" class="glyphicon glyphicon-time">A{{ritardo.numeroMezzoreAndata}}</span>{{/ritardo.numeroMezzoreAndata}}
			{{#ritardo.numeroMezzoreRitorno}}
				<span style="font-size: x-large; padding-left:10px;" class="glyphicon glyphicon-time">R{{ritardo.numeroMezzoreRitorno}}</span>{{/ritardo.numeroMezzoreRitorno}}
	</div>
	<div id="collapse_{{idcorsa}}" class="panel-collapse {{collapsePanel}}"><!-- aggiungi collapse al class se lo vuoi chiuso -->
	<div class="panel-body">
		<div class="panel {{andata.colorPanel}}">
		<div class="panel-heading">{{#andata.blinkeffect}}<span class="blinkeffect">&#9899;</span>{{/andata.blinkeffect}} {{andata.intestazione}}</div>
		<div class="panel-body">
		<div class="col-sm-12 row">
			<div class="col-sm-2">ANDATA</div>
			<div class="col-sm-2">{{andata.oraPrelev}}</div>
			<div class="col-sm-1">{{andata.durata}}</div>
			<div class="col-sm-1">{{andata.distanza}}</div>
			<div class="col-sm-6">
				<label class="checkbox-inline text-warning">
					<input type="radio" onchange="$.ajax({type:'POST', url:'${pageContext.request.contextPath}/impostaApprovazioneCorsaAdmin',dataType:'text',
						data: {idRicTransfert:{{idcorsa}},idAutista:{{idAutista}},andataRitorno:'A',numApprov:1 },error: function (req, status, error) {alert(error);}});"
						name="approvata-A-{{idcorsa}}" {{andata.radioInApprov}}><b> IN APPROVAZIONE</b>
				</label>
				<label class="checkbox-inline text-success">
					<input type="radio" onchange="$.ajax({type:'POST', url:'${pageContext.request.contextPath}/impostaApprovazioneCorsaAdmin',dataType:'text',
						data: {idRicTransfert:{{idcorsa}},idAutista:{{idAutista}},andataRitorno:'A',numApprov:2 },error: function (req, status, error) {alert(error);}});"
						name="approvata-A-{{idcorsa}}" {{andata.radioApprov}}><b> APPROVATA</b>
				</label>
				<label class="checkbox-inline text-danger">
					<input type="radio" onchange="$.ajax({type:'POST', url:'${pageContext.request.contextPath}/impostaApprovazioneCorsaAdmin',dataType:'text',
						data: {idRicTransfert:{{idcorsa}},idAutista:{{idAutista}},andataRitorno:'A',numApprov:3 },error: function (req, status, error) {alert(error);}});"
						name="approvata-A-{{idcorsa}}" {{andata.radioNonApprov}}><b> NON APPROVATA</b>
				</label>
			</div>
		</div>
		</div>
		</div>
		{{#ritorno}}
		<div class="panel {{ritorno.colorPanel}}">
		<div class="panel-heading">{{#ritorno.blinkeffect}}<span class="blinkeffect">&#9899;</span>{{/ritorno.blinkeffect}} {{ritorno.intestazione}}</div>
		<div class="panel-body">
		<div class="col-sm-12 row">
			<div class="col-sm-2">RITORNO</div>
			<div class="col-sm-2">{{ritorno.oraPrelev}}</div>
			<div class="col-sm-1">{{ritorno.durata}}</div>
			<div class="col-sm-1">{{ritorno.distanza}}</div>
			<div class="col-sm-6">
				<label class="checkbox-inline text-warning">
					<input type="radio" onchange="$.ajax({type:'POST', url:'${pageContext.request.contextPath}/impostaApprovazioneCorsaAdmin',dataType:'text',
						data: {idRicTransfert:{{idcorsa}},idAutista:{{idAutista}},andataRitorno:'R',numApprov:1 },error: function (req, status, error) {alert(error);}});"
						name="approvata-R-{{idcorsa}}" {{ritorno.radioInApprov}}><b> IN APPROVAZIONE</b>
				</label>
				<label class="checkbox-inline text-success">
					<input type="radio" onchange="$.ajax({type:'POST', url:'${pageContext.request.contextPath}/impostaApprovazioneCorsaAdmin',dataType:'text',
						data: {idRicTransfert:{{idcorsa}},idAutista:{{idAutista}},andataRitorno:'R',numApprov:2 },error: function (req, status, error) {alert(error);}});"
						name="approvata-R-{{idcorsa}}" {{ritorno.radioApprov}}><b> APPROVATA</b>
				</label>
				<label class="checkbox-inline text-danger">
					<input type="radio" onchange="$.ajax({type:'POST', url:'${pageContext.request.contextPath}/impostaApprovazioneCorsaAdmin',dataType:'text',
						data: {idRicTransfert:{{idcorsa}},idAutista:{{idAutista}},andataRitorno:'R',numApprov:3 },error: function (req, status, error) {alert(error);}});"
						name="approvata-R-{{idcorsa}}" {{ritorno.radioNonApprov}}><b> NON APPROVATA</b>
				</label>
			</div>
		</div>
		</div>
		</div>
		{{/ritorno}}
		<div class="panel panel-default">
		<div class="panel-heading">INFORMAZIONI CORSA</div>
		<div class="panel-body">
			<div class="col-sm-12 row">
				<div class="col-sm-1"><b>AUTISTA</b></div>
				<div class="col-sm-2">{{fullNameAutisa}}</div>
				<div class="col-sm-2">
					<a class="btn btn-sm btn-primary" href="tel:{{telAutista}}"><span class="glyphicon glyphicon-phone"></span> {{telAutista}}</a>
				</div>
				<div class="col-sm-2">{{corseEffAutista}} (corse.eff.)</div>
				<div class="col-sm-2">{{percServAutista}}&#37; (perc.serv.)</div>
				<div class="col-sm-2">{{prezzoCommServ}}&euro; (comm.serv.)</div>
				<div class="col-sm-1">{{prezzoAutista}}&euro;</div>
			</div>
			<div class="col-sm-12 row" style="margin-top: 15px;">
				<div class="col-sm-3">AUTOVEICOLO RICHIESTO:</div>
				<div class="col-sm-9">{{autoveicoloRichiesto}}</div>
			</div>
			{{#notePerAutista}}
			<div class="col-sm-12 row">
				<div class="col-sm-3">NOTE PER L'AUTISTA:</div>
				<div class="col-sm-9">{{notePerAutista}}</div>
			</div>
			{{/notePerAutista}}
			<div class="col-sm-12 row" style="margin-bottom: 15px;">
				<div class="col-sm-3">INFO AUTISTA:</div>
				<div class="col-sm-9">{{noteAutista}}</div>
			</div>
			<div class="col-sm-12 row ">
					<div class="col-sm-1"><b>CLIENTE</b></div>
					<div class="col-sm-2">{{fullNameCliente}}</div>
					<div class="col-sm-2">
						<a class="btn btn-sm btn-primary" href="tel:{{telCliente}}"><span class="glyphicon glyphicon-phone"></span> {{telCliente}}</a></div>
					<div class="col-sm-1">{{prezzoCliente}}&euro;</div>
			</div>
			{{#noteCliente}}
			<div class="col-sm-12 row" style="margin-top: 15px;">
				<div class="col-sm-12">NOTE CLIENTE: {{noteCliente}}</div>
			</div>
			{{/noteCliente}}
			<div class="col-sm-12 row" style="margin-top: 15px;">
				<div class="col-sm-4">
					<button type="button" id="{{idcorsa}}" name="andata" value="{{ritardo.numeroMezzoreAndata}}" class="btn btn-sm btn-success insertRitardo">
						<span class="glyphicon glyphicon-time"></span> <span class="fa fa-pencil-square-o"></span> Inserisci Num. Mezzore di Ritaro Andata {{ritardo.numeroMezzoreAndata}}</button>
				</div>
				{{#ritorno}}
					<div class="col-sm-4">
						<button type="button" id="{{idcorsa}}" name="ritorno" value="{{ritardo.numeroMezzoreRitorno}}" class="btn btn-sm btn-success insertRitardo">
							<span class="glyphicon glyphicon-time"></span> <span class="fa fa-pencil-square-o"></span> Inserisci Num. Mezzore di Ritaro Ritorno {{ritardo.numeroMezzoreRitorno}}</button>
					</div>
				{{/ritorno}}
				{{#ritardo}}
				<div class="col-sm-3">
				<a class="btn btn-sm btn-info gestioneRitardo" id="{{ritardo.id}}">
					<span class="fa fa-share"></span> <span class="glyphicon glyphicon-time"></span> Gestisci Ritardo</a>
				</div>
				<div class="col-sm-3">
				<a class="btn btn-sm btn-info checkFatturaRitardoDisponibile" id="{{idcorsa}}">
					<span class="glyphicon glyphicon-download-alt"></span> <span class="glyphicon glyphicon-time"></span> Scarica Fattura Ritardo</a>
				</div>
				{{/ritardo}}
			</div>
			<div class="col-sm-12 row" style="margin-top: 15px;">
				<div class="col-sm-3">
				<a class="btn btn-sm btn-info paginaGestioneSupplementi" id="">
					<span class="fa fa-share"></span> <span class="glyphicon glyphicon-time"></span> Pagina Gestione Supplementi</a>
				</div>
			</div>
			<!-- Supplemento -->
			{{#supplemento}}
			<div class="col-sm-12 row" style="margin-top: 15px;">
				<div class="col-sm-3">
				<a class="btn btn-sm btn-info gestioneSupplemento" id="{{id}}">
					<span class="fa fa-share"></span> <span class="glyphicon glyphicon-time"></span> Gestisci Supplemento</a>
				</div>
				<div class="col-sm-3">
					supplemento.id: {{id}} supplementoPrezzo: {{prezzo}} supplementoPagato: {{pagato}} supplementoDescrizione: {{descrizione}}
				</div>
				<div class="col-sm-3">
				<a class="btn btn-sm btn-info checkFatturaSupplementoDisponibile" id="{{id}}">
					<span class="glyphicon glyphicon-download-alt"></span> <span class="glyphicon glyphicon-time"></span> Scarica Fattura Supplemento</a>
				</div>
			</div>
			{{/supplemento}}
			<!-- Fatturazione -->
			<div class="col-sm-12 row " style="margin-top: 15px;">
				<div class="col-sm-2">
				<a class="btn btn-sm btn-danger modificaTransfer" id="{{idcorsa}}">
					<span class="fa fa-share"></span> Modifica Transfer</a>
				</div>
				<div class="col-sm-2">
				<a class="btn btn-sm btn-success gestioneRimborso" id="{{idcorsa}}">
					<span class="fa fa-share"></span> <span class="fa fa-credit-card"></span> Gestisci Rimborso</a>
				</div>
				<div class="col-sm-2">
				<a class="btn btn-sm btn-info checkFatturaDisponibile" id="{{idcorsa}}">
					<span class="glyphicon glyphicon-download-alt"></span> Scarica Fattura Corsa</a>
				</div>
				<div class="col-sm-3">
				<a class="btn btn-sm btn-info inviaEmailCorsaAcquistataCliente" id="{{idcorsa}}">
					<span class="fa fa-envelope"></span> <span class="fa fa-user"></span> <fmt:message key="messaggio.invia.email.corsa.acquistata.cliente"></fmt:message></a>
				</div>
			</div>
			<div class="col-sm-12 row" style="margin-top: 15px;">
				<div class="col-sm-12">
					<textarea id="{{idcorsa}}" class="form-control alertConfirmNoteCorsa" rows="3" placeholder="scrivi note corsa...." >{{noteCorsa}}</textarea>
				</div>
			</div>
		</div>
		</div>
	</div>
	</div>
</div>
{{/idcorsa}}
</script>

<script id="corsaMediaAdminTpl" type="text/template">
{{#idcorsa}}
<div id="panel_{{idcorsa}}" class="panel panel-primary">
	<div onclick="$.ajax({type:'POST', url:'${pageContext.request.contextPath}/impostaCollapsePanelCorsaAdmin',dataType:'text',
		data: {idRicTransfert:{{idcorsa}} },error: function (req, status, error) {alert(error);}});"
			class="panel-heading" data-toggle="{{collapsePanel}}" data-target="#collapse_{{idcorsa}}" style="font-size: large;">
			<span class="label label-success">{{idcorsa}}</span><span style="padding-left:10px;">{{intestazione}}</span> 
					<span style="font-size: x-large; padding-left:10px;" class="glyphicon {{flagApprov_A}}"></span>
					{{#ritorno}}
					{{#flagApprov_R}}<span style="font-size: x-large; padding-left:10px;" class="glyphicon {{flagApprov_R}}"></span>{{/flagApprov_R}}{{/ritorno}}

				{{#idAutistaAssegnato}}
					<span style="font-size: x-large; padding-left:10px;" class="glyphicon glyphicon-user"></span>{{/idAutistaAssegnato}}
				{{^idAutistaAssegnato}}
					<span style="font-size: x-large; padding-left:10px;" class="glyphicon glyphicon-warning-sign"></span>{{/idAutistaAssegnato}}
				{{#ritardo.numeroMezzoreAndata}}
					<span style="font-size: x-large; padding-left:10px;" class="glyphicon glyphicon-time">A{{ritardo.numeroMezzoreAndata}}</span>{{/ritardo.numeroMezzoreAndata}}
				{{#ritardo.numeroMezzoreRitorno}}
					<span style="font-size: x-large; padding-left:10px;" class="glyphicon glyphicon-time">R{{ritardo.numeroMezzoreRitorno}}</span>{{/ritardo.numeroMezzoreRitorno}}
				</div>
	<div id="collapse_{{idcorsa}}" class="panel-collapse {{collapsePanel}}"><!-- aggiungi collapse al class se lo vuoi chiuso -->
	<div class="panel-body">
		<div class="panel {{andata.colorPanel}}">
		<div class="panel-heading">{{#andata.blinkeffect}}<span class="blinkeffect">&#9899;</span>{{/andata.blinkeffect}} {{andata.intestazione}}</div>
		<div class="panel-body">
		<div class="col-sm-12 row">
				<div class="col-sm-2">ANDATA</div>
				<div class="col-sm-2">{{andata.oraPrelev}}</div>
				<div class="col-sm-1">{{andata.durata}}</div>
				<div class="col-sm-1">{{andata.distanza}}</div>
				<div class="col-sm-6">
					<label class="checkbox-inline text-warning">
						<input type="radio" onchange="$.ajax({type:'POST', url:'${pageContext.request.contextPath}/impostaApprovazioneCorsaAdmin',dataType:'text',
							data: {idRicTransfert:{{idcorsa}},idAutista:{{idAutistaAssegnato}},andataRitorno:'A',numApprov:1 },error: function (req, status, error) {alert(error);}});"
							name="approvata-A-{{idcorsa}}" {{andata.radioInApprov}}><b> IN APPROVAZIONE</b>
					</label>
					<label class="checkbox-inline text-success">
						<input type="radio" onchange="$.ajax({type:'POST', url:'${pageContext.request.contextPath}/impostaApprovazioneCorsaAdmin',dataType:'text',
							data: {idRicTransfert:{{idcorsa}},idAutista:{{idAutistaAssegnato}},andataRitorno:'A',numApprov:2 },error: function (req, status, error) {alert(error);}});"
							name="approvata-A-{{idcorsa}}" {{andata.radioApprov}}><b> APPROVATA</b>
					</label>
					<label class="checkbox-inline text-danger">
						<input type="radio" onchange="$.ajax({type:'POST', url:'${pageContext.request.contextPath}/impostaApprovazioneCorsaAdmin',dataType:'text',
							data: {idRicTransfert:{{idcorsa}},idAutista:{{idAutistaAssegnato}},andataRitorno:'A',numApprov:3 },error: function (req, status, error) {alert(error);}});"
							name="approvata-A-{{idcorsa}}" {{andata.radioNonApprov}}><b> NON APPROVATA</b>
					</label>
				</div>
			</div>
		</div>
		</div>
		{{#ritorno}}
		<div class="panel {{ritorno.colorPanel}}">
			<div class="panel-heading">{{#ritorno.blinkeffect}}<span class="blinkeffect">&#9899;</span>{{/ritorno.blinkeffect}} {{ritorno.intestazione}}</div>
			<div class="panel-body">
			<div class="col-sm-12 row">
				<div class="col-sm-2">RITORNO</div>
				<div class="col-sm-2">{{ritorno.oraPrelev}}</div>
				<div class="col-sm-1">{{ritorno.durata}}</div>
				<div class="col-sm-1">{{ritorno.distanza}}</div>
				<div class="col-sm-6">
					<label class="checkbox-inline text-warning">
						<input type="radio" onchange="$.ajax({type:'POST', url:'${pageContext.request.contextPath}/impostaApprovazioneCorsaAdmin',dataType:'text',
							data: {idRicTransfert:{{idcorsa}},idAutista:{{idAutistaAssegnato}},andataRitorno:'R',numApprov:1 },error: function (req, status, error) {alert(error);}});"
							name="approvata-R-{{idcorsa}}" {{ritorno.radioInApprov}}><b> IN APPROVAZIONE</b>
					</label>
					<label class="checkbox-inline text-success">
						<input type="radio" onchange="$.ajax({type:'POST', url:'${pageContext.request.contextPath}/impostaApprovazioneCorsaAdmin',dataType:'text',
							data: {idRicTransfert:{{idcorsa}},idAutista:{{idAutistaAssegnato}},andataRitorno:'R',numApprov:2 },error: function (req, status, error) {alert(error);}});"
							name="approvata-R-{{idcorsa}}" {{ritorno.radioApprov}}><b> APPROVATA</b>
					</label>
					<label class="checkbox-inline text-danger">
						<input type="radio" onchange="$.ajax({type:'POST', url:'${pageContext.request.contextPath}/impostaApprovazioneCorsaAdmin',dataType:'text',
							data: {idRicTransfert:{{idcorsa}},idAutista:{{idAutistaAssegnato}},andataRitorno:'R',numApprov:3 },error: function (req, status, error) {alert(error);}});"
							name="approvata-R-{{idcorsa}}" {{ritorno.radioNonApprov}}><b> NON APPROVATA</b>
					</label>
				</div>
			</div>
			</div>
		</div>
		{{/ritorno}}

		<div class="panel panel-default">
		<div class="panel-heading">INFORMAZIONI CORSA</div>
		<div class="panel-body">
			<!-- lista autisti -->
			<ul>
			{{#autisti}}
			<div class="col-sm-12 row" style="">
				<div class="col-sm-2"><li>
					{{#autista.assegnato}}
						<a href="/admin/gestioneAutista?idAutista={{autista.idAutista}}" target="_blank"><b><p class="text-success">{{autista.fullNameAutisa}}</p></b></a>{{/autista.assegnato}}
					{{^autista.assegnato}}
						<a href="/admin/gestioneAutista?idAutista={{autista.idAutista}}" target="_blank"><b><p class="text-muted">{{autista.fullNameAutisa}}</p></b></a>{{/autista.assegnato}}
				</div>
				<div class="col-sm-2">
					{{#autista.assegnato}}
						<input type="button" class="btn btn-xs btn-danger" value="Disdici Corsa"
							onclick="$.ajax({type:'POST', url:'${pageContext.request.contextPath}/assegnaDisdiciCorsaAutistaAdmin',dataType:'text',
								data: {idRicTransfert:{{idcorsa}},idAutista:{{autista.idAutista}} },error: function (req, status, error) {alert(error);}});">
					{{/autista.assegnato}}
					{{^autista.assegnato}}
						<input type="button" class="btn btn-xs btn-warning" value="Assegna Corsa"
							onclick="$.ajax({type:'POST', url:'${pageContext.request.contextPath}/assegnaDisdiciCorsaAutistaAdmin',dataType:'text',
								data: {idRicTransfert:{{idcorsa}},idAutista:{{autista.idAutista}} },error: function (req, status, error) {alert(error);}});">
					{{/autista.assegnato}} 
				</div>
				<div class="col-sm-2">
					<input type="button" class="btn btn-xs btn-success" value="InvioSmsEmailCorsaDisp"
						onclick="$.ajax({type:'POST', url:'${pageContext.request.contextPath}/inviaSMSCorsaDisponibileAutista',dataType:'text',
							data: {idRicTransfert:{{idcorsa}},idAutista:{{autista.idAutista}} }
							,success: function(result){alert('sms-email inviati esito: '+result);},error: function(req, status, error){alert(error);}});">
				</div>
				<div class="col-sm-3 small">
					{{autista.ordineAutista}} / {{autista.ordineChiamataPrenotata}} / {{autista.invioSms}} (Ordine/Ord.riservaz./Invio SMS)
				</div>
				<div class="col-sm-1 small">{{autista.corseEffAutista}} (corse.eff.)</div>
				<div class="col-sm-1 small">{{autista.percServAutista}}&#37; (perc.serv.)</div>
				<div class="col-sm-1 small">{{autista.prezzoCommServ}}&euro; (comm.serv.)</div>
				<div class="col-sm-1 small">{{autista.prezzoAutista}}&euro; {{autista.tariffaPerKm}}(&euro;/km)</div>
			</div>
			
			<div class="col-sm-12 row" style="margin-left: 5px; margin-top: 5px;">
				<div class="col-sm-12 small">AUTOVEICOLO/I RICHIESTO/I: {{autista.autoveicoloRichiesto}}</div>
			</div>
			<div class="col-sm-12 row" style="margin-left: 5px; margin-top: 5px; margin-bottom: 10px;">
				<div class="col-sm-2"><a class="btn btn-xs btn-primary" href="tel:{{autista.telAutista}}"><span class="glyphicon glyphicon-phone"></span> {{autista.telAutista}}</a></div>
				{{#autista.noteAutista}}<div class="col-sm-10 small"><em>INFO AUTISTA: {{autista.noteAutista}}</em></div>{{/autista.noteAutista}}
			</div>
			</li>
			{{/autisti}}
			</ul>
			<div class="col-sm-12 row" style="margin-top: 15px;">
					<div class="col-sm-3"><b>CLIENTE {{fullNameCliente}}</b></div>
					<div class="col-sm-3">
						<a class="btn btn-sm btn-primary" href="tel:{{telCliente}}"><span class="glyphicon glyphicon-phone"></span> {{telCliente}}</a></div>
					<div class="col-sm-3"><b>{{classeAutoveicoloSceltaCliente}} {{prezzoCliente}}</b></div>
					<div class="col-sm-3"><b>Num. Passeggeri: {{numPasseggeri}}</b></div>
			</div>
			<div class="col-sm-12 row" style="margin-top: 15px;">
				<div class="col-sm-3"><b>Rimborso: {{rimborsoCliente}}&euro;</b></div>
				<div class="col-sm-3"><b>Maggior. Notturna: {{maggiorazioneNotturna}}&euro;</b></div>
				<div class="col-sm-4"><b>Nome e Telefono Passeggero: {{nomeTelefonoPasseggero}}</b></div>
			</div>
			{{#notePerAutista}}
			<div class="col-sm-12 row" style="margin-top: 15px;">
				<em><div class="col-sm-12 ">NOTE PER L'AUTISTA: {{notePerAutista}}</div></em>
			</div>
			{{/notePerAutista}}
			<div class="col-sm-12 row" style="margin-top: 15px;">
				<div class="col-sm-4">
					<button type="button" id="{{idcorsa}}" name="andata" value="{{ritardo.numeroMezzoreAndata}}" class="btn btn-sm btn-success insertRitardo">
						<span class="glyphicon glyphicon-time"></span> <span class="fa fa-pencil-square-o"></span> Inserisci Num. Mezzore di Ritaro Andata {{ritardo.numeroMezzoreAndata}}</button>
				</div>
				{{#ritorno}}
					<div class="col-sm-4">
						<button type="button" id="{{idcorsa}}" name="ritorno" value="{{ritardo.numeroMezzoreRitorno}}" class="btn btn-sm btn-success insertRitardo">
							<span class="glyphicon glyphicon-time"></span> <span class="fa fa-pencil-square-o"></span> Inserisci Num. Mezzore di Ritaro Ritorno {{ritardo.numeroMezzoreRitorno}}</button>
					</div>
				{{/ritorno}}
				{{#ritardo}}
				<div class="col-sm-3">
				<a class="btn btn-sm btn-info gestioneRitardo" id="{{ritardo.id}}">
					<span class="fa fa-share"></span> <span class="glyphicon glyphicon-time"></span> Gestisci Ritardo</a>
				</div>
				<div class="col-sm-3">
				<a class="btn btn-sm btn-info checkFatturaRitardoDisponibile" id="{{idcorsa}}">
					<span class="glyphicon glyphicon-download-alt"></span> <span class="glyphicon glyphicon-time"></span> Scarica Fattura Ritardo</a>
				</div>
				{{/ritardo}}
			</div>
			<div class="col-sm-12 row" style="margin-top: 15px;">
				<div class="col-sm-3">
				<a class="btn btn-sm btn-info paginaGestioneSupplementi" id="">
					<span class="fa fa-share"></span> <span class="glyphicon glyphicon-time"></span> Pagina Gestione Supplementi</a>
				</div>
			</div>
			<!-- Supplemento -->
			{{#supplemento}}
			<div class="col-sm-12 row" style="margin-top: 15px;">
				<div class="col-sm-3">
				<a class="btn btn-sm btn-info gestioneSupplemento" id="{{id}}">
					<span class="fa fa-share"></span> <span class="glyphicon glyphicon-time"></span> Gestisci Supplemento</a>
				</div>
				<div class="col-sm-3">
					supplemento.id: {{id}} supplementoPrezzo: {{prezzo}} supplementoPagato: {{pagato}} supplementoDescrizione: {{descrizione}}
				</div>
				<div class="col-sm-3">
				<a class="btn btn-sm btn-info checkFatturaSupplementoDisponibile" id="{{id}}">
					<span class="glyphicon glyphicon-download-alt"></span> <span class="glyphicon glyphicon-time"></span> Scarica Fattura Supplemento</a>
				</div>
			</div>
			{{/supplemento}}

			<!-- Fatturazione -->
			<div class="col-sm-12 row " style="margin-top: 15px;">
				<div class="col-sm-2">
				<a class="btn btn-sm btn-danger modificaTransfer" id="{{idcorsa}}">
					<span class="fa fa-share"></span> Modifica Transfer</a>
				</div>
				<div class="col-sm-2">
				<a class="btn btn-sm btn-success gestioneRimborso" id="{{idcorsa}}">
					<span class="fa fa-share"></span> <span class="fa fa-credit-card"></span> Gestisci Rimborso</a>
				</div>
				<div class="col-sm-2">
				<a class="btn btn-sm btn-info checkFatturaDisponibile" id="{{idcorsa}}">
					<span class="glyphicon glyphicon-download-alt"></span> Scarica Fattura Corsa</a>
				</div>
				<div class="col-sm-3">
				<a class="btn btn-sm btn-info inviaEmailCorsaAcquistataCliente" id="{{idcorsa}}">
					<span class="fa fa-envelope"></span> <span class="fa fa-user"></span> <fmt:message key="messaggio.invia.email.corsa.acquistata.cliente"></fmt:message></a>
				</div>
			</div>
			{{#noteCliente}}
			<div class="col-sm-12 row" style="margin-top: 15px;">
				<div class="col-sm-12">NOTE CLIENTE: {{noteCliente}}</div>
			</div>
			{{/noteCliente}}
			<div class="col-sm-12 row" style="margin-top: 15px;">
				<div class="col-sm-12">
					<textarea id="{{idcorsa}}" class="form-control alertConfirmNoteCorsa" rows="3" placeholder="scrivi note corsa...." >{{noteCorsa}}</textarea>
				</div>
			</div>
		</div>
		</div>
	</div>
	</div>
</div>

{{/idcorsa}}
</script>
