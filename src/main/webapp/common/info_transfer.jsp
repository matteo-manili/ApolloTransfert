<div class="modal fade col-xs-12" id="infoTransferModal"  tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h3 class="modal-title"><i class="fa fa-info-circle fa-1x" aria-hidden="true"></i> Dall'Acquisto alla Esecuzione del Transfer</h3>
            </div>
            <div class="modal-body">
            	<smalll>
	           	<h4>Ricerca, Acquisto e Lavorazione della Corsa</h4>
				<ul>
				<li>Il Cliente ricercher&agrave; il Transfer dalla home del sito e visualizzer&agrave; i Transfers disponibili, 
					cio&egrave; dove vi &egrave; un adeguato numero di Autisti NCC disposti a eseguire il Transfer sul territorio richiesto.</li>
				<li>Il Transfer potr&agrave; essere acquistato solo da ${margineOreMinimoCorsaMedia} ore prima del prelevamento, perché gli Autisti abbiano il tempo di organizzarsi.</li>
				<li>I metodi di Pagamento sono Carta (Prepagata, Debito e Credito) e PayPal.</li>
				<li>Acquistato il Transfer, automaticamente verranno informati gli Autisti. Gli Autisti avvisati saranno di numero ${numAutistiCorsaMedia} 
					fino a 10-20 nelle province pi&ugrave; grandi come Milano o Roma.</li>
				</ul>
				
				<h4>Cliente, Gestione della Corsa</h4>
				<ul>
				<li>Il Cliente, acquistata la Corsa visualizzer&agrave; nella home-user il Pannello di Controllo della Corsa.</li>
				<li>Il Cliente potr&agrave; inserire il Nome e Telefono di un Passeggero nel Pannello della Corsa nel caso in cui il Cliente e il 
					Passeggero sono due persone diverse (Ad esempio Agenzia di Viaggio e Turista). 
					L'autista visualizzer&agrave; ambedue i Nomi e Numeri di Telefono. </li>
				<li>Il Cliente da ${numOreInfoAutistaCliente} ore prima del prelevamento potr&agrave; visualizzare il Contatto telefonico dell'Autista, 
					ugualmente l'autista ${numOreInfoAutistaCliente} ore prima del prelevamento visualizzer&agrave; il Contatto del Cliente e del Passeggero.</li>
				<li>Il Cliente potr&agrave; annullare e auto-rimborsarsi autonomamente il Transfer fino a ${maxOreDisdettaCliente} ore prima del Prelevamento attraverso il Panello della Corsa.</li>
				<li>L'acquirente potr&agrave; Scaricare la Fattura e aggiornare i dati di fatturazione.</li>
				<li><fmt:message key="info.cliente.ritardo.addebito.costo.mezzora">
					<fmt:param value="${VALORE_EURO_ORA_RITARDO_CLIENTE_CON_TASSA_SERVZIO}"/>
					<fmt:param value="${VALORE_EURO_ORA_RITARDO_CLIENTE}"/></fmt:message></li> 
				<li>Eventuali disservizi saranno informati il prima possibile e rimborsati, il numero in caso di assistenza &egrave; 
					<a class="btn btn-xs btn-primary" href="tel:<fmt:message key="cellulare.matteo"/>" ><span class="glyphicon glyphicon-phone"></span> <span itemprop="telephone"><fmt:message key="cellulare.matteo.esteso"/></span></a> 
						reperibile H24.</li>
				</ul>
				</smalll>
            </div>
            
            <div class="modal-footer">
            	<button type="button" class="btn btn-primary btn-lg" data-dismiss="modal">CHIUDI</button>
            </div>
            
        </div>
    </div>
</div>