<!-- GRAFICA NUOVA -->
<div class="form-group data-client ">

	<div class="head-sel-car row ">
		<h3><i class="fa fa-file-text"></i> <fmt:message key="user.dati.fatturazione.cliente"/></h3>
	</div>
	<div class="sel-car-btn row ">
		<div class="col-md-12">
		
			<div class="form-group input-group">
				<fmt:message key="user.azienda.cognome.nome" var="aziendaCognomeNome" />
				<form:input cssClass="form-control" placeholder="${aziendaCognomeNome}" path="billingInformation.denominazioneCliente" id="billingInformation.denominazioneCliente"/>
			</div>
			
			<div class="form-group input-group">
				<fmt:message key="user.partita.iva.codice.fiscale" var="partitaIvaCodiceFiscale" />
				<form:input cssClass="form-control" placeholder="${partitaIvaCodiceFiscale}" path="billingInformation.codiceFiscalePartitaIva" id="billingInformation.codiceFiscalePartitaIva"/>
			</div>
			
			<div class="form-group input-group">
				<fmt:message key="user.address.address" var="addressAddress" />
        		<form:input cssClass="form-control" placeholder="${addressAddress}" path="billingInformation.address" id="billingInformation.address"/>
			</div>
			
			<div class="form-group input-group">
				<fmt:message key="user.address.city" var="addressCity" />
				<form:input cssClass="form-control" placeholder="${addressCity}" path="billingInformation.city" id="billingInformation.city"/>
			</div>
			
			<div class="form-group input-group">
				<fmt:message key="user.address.province" var="addressProvince" />
            	<form:input cssClass="form-control" placeholder="${addressProvince}" path="billingInformation.province" id="billingInformation.province"/>
			</div>
			
			<div class="form-group input-group">
				<fmt:message key="user.address.postalCode" var="addressPostalCode" />
            	<form:input cssClass="form-control" placeholder="${addressPostalCode}" path="billingInformation.postalCode" id="billingInformation.postalCode"/>
			</div>
			
			<div class="form-group input-group">
				<!--  <appfuse:label styleClass="control-label" key="user.address.country"/>
	        	<appfuse:country name="country-select"  prompt="" default="${user.billingInformation.country}"/>  -->
				<!-- ricordati di inserire di creare la tabella nazioni e usare la select per selezionare le nazioni -->
				<fmt:message key="user.address.country" var="addressCountry" />
            	<form:input cssClass="form-control" placeholder="${addressCountry}" path="billingInformation.country" id="billingInformation.country"/>
			</div>

		</div>
	</div>
</div>


