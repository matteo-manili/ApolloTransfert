<script id="corseAgendaAutistaTpl" type="text/template">
{{#idcorsa}}
<div id="panel_{{idcorsa}}" class="panel panel-primary" style="margin-top: 10px; margin-bottom: 10px;">
	<div class="panel-heading" data-toggle="" data-target="#collapse_{{idcorsa}}" style="font-size: large;">
		<span class="label label-success">{{idcorsa}}</span><span style="padding-left:10px;">{{intestazione}}</span>

		<a class="btn btn-sm btn-info pull-right checkFatturaDisponibileAutista" id="{{idcorsa}}">
			<span class="glyphicon glyphicon-download-alt"></span> Scarica Copia Fattura Cliente</a></div>
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
				<div class="panel-heading">{{fullNameAutisa}} | INFORMAZIONI CORSA</div>
				<div class="panel-body">
					{{#prezzoTotaleAutista}}
					<div class="col-sm-12 row text-info" style="margin-top: 10px;">
						<div class="col-sm-4"><b>Compenso Totale Corsa (IVA Inclusa):</b></div>
						<div class="col-sm-6"><b>{{prezzoTotaleAutista}}&euro;</b></div>
					</div>
					{{/prezzoTotaleAutista}}

					<div class="col-sm-12 row text-info" style="margin-top: 10px;">
						<div class="col-sm-12"><b><u>AUTOVEICOLO ANDATA</u></b></div>
					</div>
					<div class="col-sm-12 row text-info">
						<div class="col-sm-4"><b>Compenso Corsa (IVA Inclusa):</b></div>
						<div class="col-sm-6"><b>{{andataAutoveicolo.prezzoAutista}}&euro;</b></div>
					</div>
					<div class="col-sm-12 row text-info">
						<div class="col-sm-4"><b>Autoveicolo Richiesto:</b></div>
						<div class="col-sm-6"><b>{{andataAutoveicolo.autoveicoloRichiesto}}</b></div>
					</div>
					{{#ritornoAutoveicolo}}
					<div class="col-sm-12 row text-info" style="margin-top: 10px;">
						<div class="col-sm-12"><b><u>AUTOVEICOLO RITORNO</u></b></div>
					</div>
					<div class="col-sm-12 row text-info">
						<div class="col-sm-4"><b>Compenso Corsa (IVA Inclusa):</b></div>
						<div class="col-sm-6"><b>{{ritornoAutoveicolo.prezzoAutista}}&euro;</b></div>
					</div>
					<div class="col-sm-12 row text-info">
						<div class="col-sm-4"><b>Autoveicolo Richiesto:</b></div>
						<div class="col-sm-6"><b>{{ritornoAutoveicolo.autoveicoloRichiesto}}</div>
					</div>
					{{/ritornoAutoveicolo}}

					<div class="col-sm-12 row text-info" style="margin-top: 10px;">
						<div class="col-sm-4"><b>Num. Passeggeri:</b></div>
						<div class="col-sm-6"><b>{{numPasseggeri}}</b></div>
					</div>

					{{#notePerAutista}}
					<div class="col-sm-12 row text-info" style="margin-top: 10px;">
						<div class="col-sm-4"><b>Note per l'Autista:</b></div>
						<div class="col-sm-6"><b>{{notePerAutista}}</b></div>
					</div>
					{{/notePerAutista}}

					<div class="col-sm-12 row text-info" style="margin-top: 15px;">
						<div class="col-sm-4"><strong>Contatto Cliente</strong></div>
						<div class="col-sm-6">
							<button type="button" id="{{idcorsa}}" value="{{tokenAutista}}" class="btn btn-sm btn-primary alertConfirmInfoCliente"><span class="glyphicon glyphicon-phone"></span> Info Cliente</button>
						</div>
					</div>
					<div class="col-sm-12 row" style="margin-top: 15px;">
						<div class="col-sm-12"><p class="text-success"><strong><ins>Il Cliente ha eseguito il Pagamento! Sei tenuto ad Eseguire la Corsa.</ins><br>
							In caso di impossibilit&agrave; di eseguire la Corsa sei pregato di disdire la Corsa telefonando il prima possibile al numero <a class='btn btn-sm btn-info' href='tel:<fmt:message key="cellulare.matteo"/>'><span class='glyphicon glyphicon-phone'></span><fmt:message key="cellulare.matteo.esteso"/></a> pena la Cancellazione dell'account.</strong></p></div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
{{/idcorsa}}
</script>

<script id="corseAgendaCompletateAutistaTpl" type="text/template">
{{#idcorsa}}
<div id="panel_{{idcorsa}}" class="panel panel-primary" style="margin-top: 10px; margin-bottom: 10px;">
	<div class="panel-heading" data-toggle="" data-target="#collapse_{{idcorsa}}" style="font-size: large;">
		<span class="label label-success">{{idcorsa}}</span><span style="padding-left:10px;">{{intestazione}}</span>

		<a class="btn btn-sm btn-info pull-right checkFatturaDisponibileAutista" id="{{idcorsa}}">
			<span class="glyphicon glyphicon-download-alt"></span> Scarica Copia Fattura Cliente</a></div>
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
				<div class="panel-heading">{{fullNameAutisa}} | INFORMAZIONI CORSA</div>
				<div class="panel-body">
					{{#prezzoTotaleAutista}}
					<div class="col-sm-12 row text-info" style="margin-top: 10px;">
						<div class="col-sm-4"><b>Compenso Totale Corsa (IVA Inclusa):</b></div>
						<div class="col-sm-6"><b>{{prezzoTotaleAutista}}&euro;</b></div>
					</div>
					{{/prezzoTotaleAutista}}

					<div class="col-sm-12 row text-info" style="margin-top: 10px;">
						<div class="col-sm-12"><b><u>AUTOVEICOLO ANDATA</u></b></div>
					</div>
					<div class="col-sm-12 row text-info">
						<div class="col-sm-4"><b>Compenso Corsa (IVA Inclusa):</b></div>
						<div class="col-sm-6"><b>{{andataAutoveicolo.prezzoAutista}}&euro;</b></div>
					</div>
					<div class="col-sm-12 row text-info">
						<div class="col-sm-4"><b>Autoveicolo Richiesto:</b></div>
						<div class="col-sm-6"><b>{{andataAutoveicolo.autoveicoloRichiesto}}</b></div>
					</div>
					{{#ritornoAutoveicolo}}
					<div class="col-sm-12 row text-info" style="margin-top: 10px;">
						<div class="col-sm-12"><b><u>AUTOVEICOLO RITORNO</u></b></div>
					</div>
					<div class="col-sm-12 row text-info">
						<div class="col-sm-4"><b>Compenso Corsa (IVA Inclusa):</b></div>
						<div class="col-sm-6"><b>{{ritornoAutoveicolo.prezzoAutista}}&euro;</b></div>
					</div>
					<div class="col-sm-12 row text-info">
						<div class="col-sm-4"><b>Autoveicolo Richiesto:</b></div>
						<div class="col-sm-6"><b>{{ritornoAutoveicolo.autoveicoloRichiesto}}</div>
					</div>
					{{/ritornoAutoveicolo}}

					<div class="col-sm-12 row text-info" style="margin-top: 10px;">
						<div class="col-sm-4"><b>Num. Passeggeri:</b></div>
						<div class="col-sm-6"><b>{{numPasseggeri}}</b></div>
					</div>

					{{#notePerAutista}}
					<div class="col-sm-12 row text-info" style="margin-top: 10px;">
						<div class="col-sm-4"><b>Note per l'Autista:</b></div>
						<div class="col-sm-6"><b>{{notePerAutista}}</b></div>
					</div>
					{{/notePerAutista}}

					<div class="col-sm-12 row text-info" style="margin-top: 15px;">
						<div class="col-sm-4"><strong>Contatto Cliente</strong></div>
						<div class="col-sm-6">
							<button type="button" id="{{idcorsa}}" value="{{tokenAutista}}" class="btn btn-sm btn-primary alertConfirmInfoCliente"><span class="glyphicon glyphicon-phone"></span> Info Cliente</button>
						</div>
					</div>
					<div class="col-sm-12 row" style="margin-top: 15px;">
						<div class="col-sm-12"><p class="text-success"><strong><ins>Corsa Completata.</ins><br>
							Per info sul Pagamento Contattare <a class='btn btn-sm btn-info' href='tel:<fmt:message key="cellulare.matteo"/>'><span class='glyphicon glyphicon-phone'></span><fmt:message key="cellulare.matteo.esteso"/></a> (Anche Whatsapp).</strong></p></div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
{{/idcorsa}}
</script>


<script id="corsaParticolarePrenotazioneTpl" type="text/template">
{{#idcorsa}}
<div id="panel_{{idcorsa}}" class="panel panel-primary" style="margin-top: 10px; margin-bottom: 10px;">
	<div class="panel-heading" data-toggle="" data-target="#collapse_{{idcorsa}}" style="font-size: large;">
		<span class="label label-success">{{idcorsa}}</span><span style="padding-left:10px;">{{intestazione}}</span></div>
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
				<div class="panel-heading">{{#preventivoInviato}}{{fullNameAutisa}} | PREVENTIVO INVIATO{{/preventivoInviato}}
					{{^preventivoInviato}}{{fullNameAutisa}} | INSERISCI VALORI PREVENTIVO{{/preventivoInviato}}</div>
				<div class="panel-body ">
				
				{{^preventivoInviato}}

				<div class="col-sm-12 row ">
					<div class="col-sm-12 ">
						1) Inserire il Compenso Autista nel campo testuale nel formato '123.00' e fare click sul Pulsante Blu "Inserisci Prezzo (compreso iva)".
					</div>
				</div>
				<div class="col-sm-12 row ">
					<div class="col-sm-12 ">
						2) Fare click sul Pulsante Blu "Scegli Periodo Validit&agrave; Preventivo" e dalla Tendina selezionare la durata del preventivo 
							oltre il quale il Cliente non può comprare la Corsa.
					</div>
				</div>
				<div class="col-sm-12 row ">
					<div class="col-sm-12 ">
						3) Fare click sul Pulsante Verde "Invia Preventivo al Cliente" per Inviare il Preventivo al Cliente.
					</div>
				</div>

				<div class="col-sm-12 row " style="margin-top: 15px;">
					<div class="col-sm-6 ">
						<input type='text' id='prezzoPreventivoId-{{idcorsaPart}}' value='{{prezzoAutista}}' class='form-control'>
					</div>
					<div class="col-sm-6 ">
						<input type="button" id="{{idcorsaPart}}" class='btn btn-sm btn-primary setPreventivoPrezzo' value='Inserisci Prezzo (compreso iva)'>
					</div>
				</div>
				<div class="col-sm-12 row ">
					<div class="col-sm-6 ">
						<div class="text-primary"><strong><big>
							{{#validitaPreventivoData}}Periodo Massimo Validit&agrave; Preventivo: <span><b>{{validitaPreventivoData}}</b></span>{{/validitaPreventivoData}}
							{{^validitaPreventivoData}}Seleziona Periodo Massimo Validit&agrave; Preventivo{{/validitaPreventivoData}}
							</big></strong></div>
					</div>
					<div class="col-sm-6 ">
						<input type="button" id="{{idcorsaPart}}" class='btn btn-sm btn-primary apriPopUpValiditaPreventivo' value='Scegli Periodo Validit&agrave; Preventivo'>
					</div>
				</div>
				{{/preventivoInviato}}
	
				<div class=" ">
				<div class="col-sm-12 row text-success" style="margin-top: 15px;">
					<div class="col-sm-12 "><b><big>Compenso Autista (Compresa IVA): {{prezzoAutista}}&euro;</big></b></div>
				</div>
				<div class="col-sm-12 row text-success">
					<div class="col-sm-12 "><b><big>Scadenza Preventivo Giorno Ora: {{validitaPreventivoData}}</big></b></div>
				</div>
				{{#descServizioMultiplo}}
				<div class="col-sm-12 row text-info">
					<div class="col-sm-3"><b>TIPO SERVIZIO:</b></div>
					<div class="col-sm-9"><b>{{descServizioMultiplo}}</b></div>
				</div>
				{{/descServizioMultiplo}}
				<div class="col-sm-12 row text-info">
					<div class="col-sm-3"><b>AUTOVEICOLO RICHIESTO:</b></div>
					<div class="col-sm-9"><b>{{autoveicoloRichiesto}}</b></div>
				</div>
				<div class="col-sm-12 row text-info">
					<div class="col-sm-3"><b>NUM. PASSEGGERI:</b></div>
					<div class="col-sm-9"><b>{{numPasseggeri}}</b></div>
				</div>
				{{#notePerAutista}}
				<div class="col-sm-12 row text-info">
					<div class="col-sm-3"><b>NOTE PER L'AUTISTA:</b></div>
					<div class="col-sm-9"><b><ins>{{notePerAutista}}</ins></b></div>
				</div>
				{{/notePerAutista}}
				</div>

				<div class=" ">
				{{#corsaValida}}
					{{#preventivoInviato}}
						<div class="col-sm-12 row" style="margin-top: 15px;">
							<div class="col-sm-12"><p class="text-success"><strong>Hai inviato il Preventivo al Cliente!<br> <ins>Ti confermeremo la Corsa via SMS e via EMAIL appena riceveremo il pagamento dal Cliente.</ins><br>
								In caso di impossibilit&agrave; di eseguire la Corsa sei pregato di disdire la Corsa telefonando il prima possibile al numero <a class='btn btn-sm btn-info' href='tel:<fmt:message key="cellulare.matteo"/>'><span class='glyphicon glyphicon-phone'></span><fmt:message key="cellulare.matteo.esteso"/></a> pena la Cancellazione dell'account.</p></strong></div>
						</div>
					{{/preventivoInviato}}
					{{^preventivoInviato}}
						<div class="col-sm-12 row" style="margin-top: 15px;">
							<div class="col-sm-12"><p class="text-info"><strong>Un Cliente ha richiesto il Preventivo per questa Corsa.<br>
								Compila i dati del Preventivo ed invialo al Cliente.<br> Avvenuto il pagamento del cliente ti impegnerai ad eseguire la corsa.
							</div>
						</div>
						<div class="col-sm-12 row" style="margin-top: 15px;">
							<div class="col-sm-3">
								<input type="button" id="{{idcorsaPart}}" class="btn btn-success inviaPreventivoClienteCorsaParticolare" value="Invia Preventivo al Cliente">
							</div>
						</div>
					{{/preventivoInviato}}
				{{/corsaValida}}
				{{^corsaValida}}
					<div class="col-sm-12 row" style="margin-top: 15px;">
						<div class="col-sm-12"><p class="text-danger"><strong>La corsa non è più valida perché l'orario di prelevemanto è stato superato o è tra meno di un ora.</p></strong></div>
					</div>
				{{/corsaValida}}
				</div>
			</div>
		</div>
	</div>
</div>
</div>
{{/idcorsa}}
</script>

<script id="corseParticolariAutistaTpl" type="text/template">
{{#idcorsa}}
<div id="panel_{{idcorsa}}" class="panel panel-primary" style="margin-top: 10px; margin-bottom: 10px;">
	<div class="panel-heading" data-toggle="" data-target="#collapse_{{idcorsa}}" style="font-size: large;">
		<span class="label label-success">{{idcorsa}}</span><span style="padding-left:10px;">{{intestazione}}</span>

		<a class="btn btn-sm btn-info pull-right checkFatturaDisponibileAutista" id="{{idcorsa}}">
			<span class="glyphicon glyphicon-download-alt"></span> Scarica Copia Fattura Cliente</a></div>
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
				<div class="panel-heading">{{fullNameAutisa}} | INFORMAZIONI CORSA</div>
				<div class="panel-body">
					<div class="col-sm-12 row text-success">
						<div class="col-sm-12 "><strong><big>Compenso Autista (Compresa IVA): {{prezzoAutista}}&euro;</big></strong></div>
					</div>
					{{#descServizioMultiplo}}
					<div class="col-sm-12 row text-info">
						<div class="col-sm-3"><b>TIPO SERVIZIO:</b></div>
						<div class="col-sm-9"><b>{{descServizioMultiplo}}</b></div>
					</div>
					{{/descServizioMultiplo}}
					<div class="col-sm-12 row text-info">
						<div class="col-sm-3"><b>AUTOVEICOLO RICHIESTO:</b></div>
						<div class="col-sm-9"><b>{{autoveicoloRichiesto}}</div>
					</div>
					<div class="col-sm-12 row text-info">
						<div class="col-sm-3"><b>NUM. PASSEGGERI:</b></div>
						<div class="col-sm-9"><b>{{numPasseggeri}}</b></div>
					</div>
					{{#notePerAutista}}
					<div class="col-sm-12 row text-info">
						<div class="col-sm-3"><b>NOTE PER L'AUTISTA:</b></div>
						<div class="col-sm-9"><b>{{notePerAutista}}</b></div>
					</div>
					{{/notePerAutista}}
					<div class="col-sm-12 row text-info" style="margin-top: 15px;">
						<div class="col-sm-3"><strong>CLIENTE</strong></div>
						<div class="col-sm-9">
							<button type="button" id="{{idcorsa}}" value="{{tokenAutista}}" class="btn btn-sm btn-primary alertConfirmInfoCliente"><span class="glyphicon glyphicon-phone"></span> Info Cliente</button>
						</div>
					</div>
					<div class="col-sm-12 row" style="margin-top: 15px;">
						<div class="col-sm-12"><p class="text-success"><strong><ins>Il Cliente ha eseguito il Pagamento! Sei tenuto ad Eseguire la Corsa.</ins><br>
							In caso di impossibilit&agrave; di eseguire la Corsa sei pregato di disdire la Corsa telefonando il prima possibile al numero <a class='btn btn-sm btn-info' href='tel:<fmt:message key="cellulare.matteo"/>'><span class='glyphicon glyphicon-phone'></span><fmt:message key="cellulare.matteo.esteso"/></a> pena la Cancellazione dell'account.</strong></p></div>
					</div>
				</div>
			</div>
			
		</div>
	</div>
</div>
{{/idcorsa}}
</script>

<script id="corseParticolariCompletateAutistaTpl" type="text/template">
{{#idcorsa}}
<div id="panel_{{idcorsa}}" class="panel panel-primary" style="margin-top: 10px; margin-bottom: 10px;">
	<div class="panel-heading" data-toggle="" data-target="#collapse_{{idcorsa}}" style="font-size: large;">
		<span class="label label-success">{{idcorsa}}</span><span style="padding-left:10px;">{{intestazione}}</span></div>
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
						<div class="col-sm-2">
							{{#andata.radioInApprov}}<span class="text-warning"><strong> IN APPROVAZIONE</strong></span>{{/andata.radioInApprov}}
							{{#andata.radioApprov}}<span class="text-success"><strong> APPROVATA</strong></span>{{/andata.radioApprov}}
							{{#andata.radioNonApprov}}<span class="text-danger"><strong> NON APPROVATA</strong></span>{{/andata.radioNonApprov}}
						</div>
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
						<div class="col-sm-2">
							{{#ritorno.radioInApprov}}<span class="text-warning"><strong> IN APPROVAZIONE</strong></span>{{/ritorno.radioInApprov}}
							{{#ritorno.radioApprov}}<span class="text-success"><strong> APPROVATA</strong></span>{{/ritorno.radioApprov}}
							{{#ritorno.radioNonApprov}}<span class="text-danger"><strong> NON APPROVATA</strong></span>{{/ritorno.radioNonApprov}}
						</div>
					</div>
				</div>
			</div>
			{{/ritorno}}
			<div class="panel panel-default">
				<div class="panel-heading">{{fullNameAutisa}} | INFORMAZIONI CORSA</div>
				<div class="panel-body">
					<div class="col-sm-12 row">
						<div class="col-sm-12 "><strong><big>Compenso Autista (Compresa IVA): {{prezzoAutista}}&euro;</big></strong></div>
					</div>
					<div class="col-sm-12 row" style="margin-top: 15px;">
						<div class="col-sm-3">AUTOVEICOLO RICHIESTO:</div>
						<div class="col-sm-9">{{autoveicoloRichiesto}}</div>
					</div>
					{{#notePerAutista}}
					<div class="col-sm-12 row" style="margin-top: 15px;">
						<div class="col-sm-3">NOTE PER L'AUTISTA:</div>
						<div class="col-sm-9">{{notePerAutista}}</div>
					</div>
					{{/notePerAutista}}
					<div class="col-sm-12 row ">
						<div class="col-sm-3"><strong>CLIENTE</strong></div>
						<div class="col-sm-9">
							<button type="button" id="{{idcorsa}}" value="{{tokenAutista}}" class="btn btn-sm btn-primary alertConfirmInfoCliente"><span class="glyphicon glyphicon-phone"></span> Info Cliente</button>
						</div>
					</div>
					<div class="col-sm-12 row" style="margin-top: 15px;">
						<div class="col-sm-3">Num Passeggeri:</div>
						<div class="col-sm-9">{{numPasseggeri}}</div>
					</div>
					<div class="col-sm-12 row" style="margin-top: 15px;">
						<div class="col-sm-12"><p class="text-success"><strong><ins>Corsa Completata.</ins><br>
							Per info sul Pagamento Contattare <a class='btn btn-sm btn-info' href='tel:<fmt:message key="cellulare.matteo"/>'><span class='glyphicon glyphicon-phone'></span><fmt:message key="cellulare.matteo.esteso"/></a> (Anche Whatsapp).</strong></p></div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
{{/idcorsa}}
</script>


<script id="corseMedieDisponibiliAutistaTpl" type="text/template">
{{#idcorsa}}
<div id="panel_{{idcorsa}}" class="panel panel-primary" style="margin-top: 10px; margin-bottom: 10px;">
	<div class="panel-heading" data-toggle="" data-target="#collapse_{{idcorsa}}" style="font-size: large;">
		<span class="label label-success">{{idcorsa}}</span><span style="padding-left:10px;">{{intestazione}}</span></div>

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
				<div class="panel-heading">{{fullNameAutisa}} | INFORMAZIONI CORSA</div>   
				<div class="panel-body">
				<div class="col-sm-12 row">
					<div class="col-sm-10 text-primary"><strong><big>{{prezzoAutista}}</big></strong></div>
				</div>

{{#corsaPrenotabile}}
	<div class="col-sm-12 row" style="margin-top: 15px;">
		<div class="col-sm-6"><input type="button" id="{{idcorsaMedia}}" class="btn btn-sm btn-success alertConfirmPrenota" value="Prenota Corsa"></div>
		<div class="col-sm-6"><p class="text-primary"><strong>Questa Corsa &egrave; disponibile, Prenotala!</strong></p></div>
	</div>
{{/corsaPrenotabile}}

{{#corsaGiaPrenotata}}
	<div class="col-sm-12 row" style="margin-top: 15px;">
		<div class="col-sm-3"><input type="button" id="{{idcorsaMedia}}" class="btn btn-sm btn-primary alertConfirmRiserva" value="Riserva Corsa"></div>
		<div class="col-sm-9">Un altro autista ha gi&agrave; prenotato la Corsa prima di te, Riservala nel caso egli la annullasse 
			sarai avvisato con un SMS con almeno {{numMaxOreDisdettaCorsa}} ore di tempo prima del prelevamento cliente.
		</div>
	</div>
{{/corsaGiaPrenotata}}

{{#corsaRiservata}}
	<div class="col-sm-12 row" style="margin-top: 15px;">
		<div class="col-sm-3"><input type="button" id="{{idcorsaMedia}}" class="btn btn-sm btn-warning alertConfirmTogliRiserva" value="Cancella Riserva"></div>
		<div class="col-sm-9">Hai Riservato questa Corsa, qualora l'autista in carico la annullasse sarai avvisato con un SMS con almeno {{numMaxOreDisdettaCorsa}} ore di tempo prima del prelevamento cliente.
		</div>
	</div>
{{/corsaRiservata}}

{{#corsaNonApprovata}}
	<div class="col-sm-12 row" style="margin-top: 15px;">
		<div class="col-sm-3"><input type="button" id="{{idcorsaMedia}}" class="btn btn-sm btn-danger" value="Corsa Cancellata"></div>
		<div class="col-sm-9">La corsa &egrave; stata Cancellata.
		</div>
	</div>
{{/corsaNonApprovata}}

				<div class="col-sm-12 row" style="margin-top: 15px;">
					<div class="col-sm-3">AUTOVEICOLO/I RICHIESTO/I:</div>
					<div class="col-sm-9">{{autoveicoloRichiesto}}</div>
				</div>
				{{#notePerAutista}}
				<div class="col-sm-12 row" style="margin-top: 15px;">
					<div class="col-sm-3">NOTE PER L'AUTISTA:</div>
					<div class="col-sm-9">{{notePerAutista}}</div>
				</div>
				{{/notePerAutista}}
				<div class="col-sm-12 row" style="margin-top: 15px;">
					<div class="col-sm-3"><strong>CLIENTE</strong></div>
					<div class="col-sm-9">
						<button type="button" id="{{idcorsa}}" value="{{tokenAutista}}" class="btn btn-sm btn-primary alertConfirmInfoCliente"><span class="glyphicon glyphicon-phone"></span> Info Cliente</button>
					</div>
				</div>
				<div class="col-sm-12 row" style="margin-top: 15px;">
					<div class="col-sm-3">Num Passeggeri:</div>
					<div class="col-sm-9">{{numPasseggeri}}</div>
				</div>

			</div>
		</div>

	</div>
</div>
</div>
{{/idcorsa}}
</script>

<script id="corseMedieAutistaTpl" type="text/template">
{{#idcorsa}}
<div id="panel_{{idcorsa}}" class="panel panel-primary" style="margin-top: 10px; margin-bottom: 10px;">
	<div class="panel-heading" data-toggle="" data-target="#collapse_{{idcorsa}}" style="font-size: large;">
		<span class="label label-success">{{idcorsa}}</span><span style="padding-left:10px;">{{intestazione}}</span>
			<a class="btn btn-sm btn-info pull-right checkFatturaDisponibileAutista" id="{{idcorsa}}">
				<span class="glyphicon glyphicon-download-alt"></span> Scarica Copia Fattura Cliente</a></div>
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
				<div class="panel-heading">{{fullNameAutisa}} | INFORMAZIONI CORSA</div>
				<div class="panel-body">
				<div class="col-sm-12 row">
					<div class="col-sm-10"><strong>{{prezzoAutista}}</strong></div> 
				</div>
				{{#daEseguire}}
				<div class="col-sm-12 row" style="margin-top: 15px;">
					<div class="col-sm-5"><input type="button" id="{{idcorsaMedia}}" class="btn btn-sm btn-danger alertConfirm" value="Disdici Corsa"></div>
					<div class="col-sm-7">Puoi disdire la corsa fino a {{numMaxOreDisdettaCorsa}} ore prima della ora di prelevamento.</div>
				</div>
				{{/daEseguire}}
				<div class="col-sm-12 row" style="margin-top: 15px;">
					<div class="col-sm-3">AUTOVEICOLO/I RICHIESTO/I:</div>
					<div class="col-sm-9">{{autoveicoloRichiesto}}</div>
				</div>
				{{#notePerAutista}}
				<div class="col-sm-12 row" style="margin-top: 15px;">
					<div class="col-sm-3">NOTE PER L'AUTISTA:</div>
					<div class="col-sm-9">{{notePerAutista}}</div>
				</div>
				{{/notePerAutista}}
				<div class="col-sm-12 row" style="margin-top: 15px;">
					<div class="col-sm-3"><strong>CLIENTE</strong></div>
					<div class="col-sm-9">
						<button type="button" id="{{idcorsa}}" value="{{tokenAutista}}" class="btn btn-sm btn-primary alertConfirmInfoCliente"><span class="glyphicon glyphicon-phone"></span> Info Cliente</button>
					</div>
				</div>
				<div class="col-sm-12 row" style="margin-top: 15px;">
					<div class="col-sm-3">Num Passeggeri:</div>
					<div class="col-sm-9">{{numPasseggeri}}</div>
				</div>
			</div>
		</div>

	</div>
</div>
</div>
{{/idcorsa}}
</script>

<script id="corseMedieCompletateAutistaTpl" type="text/template">
{{#idcorsa}}
<div id="panel_{{idcorsa}}" class="panel panel-primary" style="margin-top: 10px; margin-bottom: 10px;">
	<div class="panel-heading" data-toggle="" data-target="#collapse_{{idcorsa}}" style="font-size: large;">
		<span class="label label-success">{{idcorsa}}</span><span style="padding-left:10px;">{{intestazione}}</span></div>

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

						<div class="col-sm-2">
							{{#andata.radioInApprov}}<span class="text-warning"><strong> IN APPROVAZIONE</strong></span>{{/andata.radioInApprov}}
							{{#andata.radioApprov}}<span class="text-success"><strong> APPROVATA</strong></span>{{/andata.radioApprov}}
							{{#andata.radioNonApprov}}<span class="text-danger"><strong> NON APPROVATA</strong></span>{{/andata.radioNonApprov}}
						</div>

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

						<div class="col-sm-2">
							{{#ritorno.radioInApprov}}<span class="text-warning"><strong> IN APPROVAZIONE</strong></span>{{/ritorno.radioInApprov}}
							{{#ritorno.radioApprov}}<span class="text-success"><strong> APPROVATA</strong></span>{{/ritorno.radioApprov}}
							{{#ritorno.radioNonApprov}}<span class="text-danger"><strong> NON APPROVATA</strong></span>{{/ritorno.radioNonApprov}}
						</div>

					</div>
				</div>
			</div>
			{{/ritorno}}

			<div class="panel panel-default">
				<div class="panel-heading">{{fullNameAutisa}} | INFORMAZIONI CORSA</div>
				<div class="panel-body">
				<div class="col-sm-12 row">
					<div class="col-sm-10"><strong>{{prezzoAutista}}</strong></div>
				</div>

				{{#daEseguire}}
				<div class="col-sm-12 row" style="margin-top: 15px;">
					<div class="col-sm-3"><input type="button" id="{{idcorsaMedia}}" class="btn btn-sm btn-danger alertConfirm" value="Disdici Corsa"></div>
					<div class="col-sm-9">Puoi disdire la corsa fino a {{numMaxOreDisdettaCorsa}} ore prima della ora di prelevamento.</div>
				</div>
				{{/daEseguire}}

				<div class="col-sm-12 row" style="margin-top: 15px;">
					<div class="col-sm-3">AUTOVEICOLO/I RICHIESTO/I:</div>
					<div class="col-sm-9">{{autoveicoloRichiesto}}</div>
				</div>
				{{#notePerAutista}}
				<div class="col-sm-12 row" style="margin-top: 15px;">
					<div class="col-sm-3">NOTE PER L'AUTISTA:</div>
					<div class="col-sm-9">{{notePerAutista}}</div>
				</div>
				{{/notePerAutista}}

				<div class="col-sm-12 row" style="margin-top: 15px;">
					<div class="col-sm-3"><strong>CLIENTE</strong></div>
					<div class="col-sm-9">
						<button type="button" id="{{idcorsa}}" value="{{tokenAutista}}" class="btn btn-sm btn-primary alertConfirmInfoCliente"><span class="glyphicon glyphicon-phone"></span> Info Cliente</button>
					</div>
				</div>
				<div class="col-sm-12 row" style="margin-top: 15px;">
					<div class="col-sm-3">Num Passeggeri:</div>
					<div class="col-sm-9">{{numPasseggeri}}</div>
				</div>
			</div>
		</div>

	</div>
</div>
</div>
{{/idcorsa}}
</script>


