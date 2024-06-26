<%@ include file="/common/info_autista_privato_azienda.jsp" %>
<div class="form-group ">
	<div class="alert alert-info" role="alert">
		<p><a href="<c:url value="/userform" />" class="alert-link">Vai alla Pagina Modifca Profilo per impostare l'opzione Privato/Azienda.</a></p>
	</div>
</div>

<div class="form-group row">
	<div class="col-sm-6 form-group">
		<label class=" form-control-label"><fmt:message key="user.partita.iva"/></label>
		<input type="text" value="${autista.autistaDocumento.partitaIva}" name="collaboratore-partita-iva" 
			${autista.autistaDocumento.approvatoDocumenti ? 'readonly' : ''} class="form-control" />
	</div>
	<div class="col-sm-6 form-group">
		<label class=" form-control-label"><fmt:message key="user.azienda.cognome.nome"/></label>
		<input type="text" value="${autista.autistaDocumento.partitaIvaDenominazione}" 
			${autista.autistaDocumento.approvatoDocumenti ? 'readonly' : ''} name="collaboratore-partita-iva-denominazione" class="form-control" />
	</div>
</div>
<div class="form-group row">
	<div class="col-sm-6 form-group">
		<label class=" form-control-label">IBAN</label>
		<input type="text" value="${autista.autistaDocumento.iban}" name="collaboratore-iban" class="form-control"
			${autista.autistaDocumento.approvatoDocumenti ? 'readonly' : ''}  />
	</div>
	<div class="col-sm-3 form-group">
		<label class=" form-control-label">Numero Patente</label>
		<input type="text" value="${autista.autistaDocumento.documentiPatente.numeroPatente}" name="collaboratore-patente" 
			class="form-control" ${autista.autistaDocumento.approvatoDocumenti ? 'readonly' : ''} />
	</div>
	<div class="col-sm-3 form-group">
		<label class=" form-control-label">Scadenza Patente</label>
		<input type="text" name="collaboratore-scadenza-patente" class="form-control" ${autista.autistaDocumento.approvatoDocumenti ? 'readonly' : ''} 
		  value="<fmt:formatDate pattern="dd/MM/yyyy" value="${autista.autistaDocumento.documentiPatente.dataScadenzaPatente}"/>" placeholder="Data Scadenza 01/01/2030..." />
	</div>
</div>
<!-- DOCUMENTO PATENTE FRONTE -->
<div class=" form-group">				
	<label class="control-label ">Carica Documento Patente "Fronte" (MAX ${maxMbDcument}MB)</label>
	<input type="file" name="collaboratore-documento-patente-fronte" data-show-upload="false" 
		${autista.autistaDocumento.approvatoDocumenti ? 'readonly' : ''} class="file "> 
</div>
<c:if test="${not empty autista.autistaDocumento.documentiPatente.nomeFilePatenteFronte}">	
<div class="row form-group ">
	<div class=" col-sm-5 ">
		<a href="<c:url value="/getFileAmazonStore?objectModel=DocumentiPatente&objectModelId=${autista.autistaDocumento.documentiPatente.id}&objectModelFileName=nomeFilePatenteFronte"/>" id="trigger">${autista.autistaDocumento.documentiPatente.nomeFilePatenteFronte}</a>
	</div>
	<div class=" col-sm-5 ">
		<button type="button" name="remove-collaboratore-documento-patente-fronte" class="btn btn-xs btn-danger alertConfirmGenerale" 
			${autista.autistaDocumento.approvatoDocumenti ? 'disabled' : ''} value="${autista.autistaDocumento.documentiPatente.id}">Elimina Documento Patente "Fronte"
				<span class="glyphicon glyphicon-trash"></span>
     	</button>
	</div>
</div>
</c:if>

<!-- DOCUMENTO PATENTE RETRO -->
<div class=" form-group">				
	<label class="control-label ">Carica Documento Patente "Retro" (MAX ${maxMbDcument}MB)</label>
	<input type="file" name="collaboratore-documento-patente-retro" data-show-upload="false" 
		${autista.autistaDocumento.approvatoDocumenti ? 'readonly' : ''} class="file"> 
</div>
<c:if test="${not empty autista.autistaDocumento.documentiPatente.nomeFilePatenteRetro}">	
<div class=" form-group ">
	<div class=" col-sm-5 ">
		<a href="<c:url value="/getFileAmazonStore?objectModel=DocumentiPatente&objectModelId=${autista.autistaDocumento.documentiPatente.id}&objectModelFileName=nomeFilePatenteRetro"/>" id="trigger">${autista.autistaDocumento.documentiPatente.nomeFilePatenteRetro}</a>
	</div>
	<div class=" col-sm-5 ">
		<button type="button" name="remove-collaboratore-documento-patente-retro" class="btn btn-xs btn-danger alertConfirmGenerale" 
			${autista.autistaDocumento.approvatoDocumenti ? 'disabled' : ''} value="${autista.autistaDocumento.documentiPatente.id}">Elimina Documento Patente "Retro"
				<span class="glyphicon glyphicon-trash"></span>
     	</button>
	</div>
</div>
</c:if>

<!-- DOCUMENTO AGGIUNTIVO -->
<div class=" form-group">				
	<label class="control-label ">Carica Eventuale Documento Aggiuntivo (MAX ${maxMbDcument}MB)</label>
	<input type="file" name="collaboratore-documento-aggiuntivo" data-show-upload="false" 
		${autista.autistaDocumento.approvatoDocumenti ? 'readonly' : ''} class="file"> 
</div>
<c:if test="${not empty autista.autistaDocumento.documentoAggiuntivo}">	
<div class="row form-group ">
	<div class=" col-sm-5 ">
		<a href="<c:url value="/getFileAmazonStore?objectModel=Autista&objectModelId=${autista.id}&objectModelFileName=documentoAggiuntivo"/>" id="trigger">${autista.autistaDocumento.documentoAggiuntivo}</a>
	</div>
	<div class=" col-sm-5 ">
		<button type="button" name="remove-collaboratore-documento-aggiuntivo" class="btn btn-xs btn-danger alertConfirmGenerale" 
			${autista.autistaDocumento.approvatoDocumenti ? 'disabled' : ''} value="${autista.id}">Elimina Documento Aggiuntivo
				<span class="glyphicon glyphicon-trash"></span>
     	</button>
	</div>
</div>
</c:if>


<!-- SUBMIT -->
<div class=" form-group ">
	<button type="submit" name="inserisci-info-collaboratore" class="btn btn-info attesaLoader"
		${autista.autistaDocumento.approvatoDocumenti ? 'disabled' : ''}>Salva Info Collaboratore</button>
	<c:if test="${pageContext.request.isUserInRole('ROLE_ADMIN')}">
		<input type="checkbox" ${autista.autistaDocumento.approvatoDocumenti ? 'checked':''} data-toggle="toggle" data-size="small" 
			onchange="$('#info-collaboratore-approvato-id').val(${autista.autistaDocumento.approvatoDocumenti});$('#autistaForm').submit();" data-onstyle="success" data-offstyle="warning">
			<input type="hidden" id="info-collaboratore-approvato-id" name="info-collaboratore-approvato">
	</c:if>
</div>

<div class=" pull-right">
	<span class=" text-${autista.autistaDocumento.approvatoDocumenti ? 'success' : 'danger'}">
		${autista.autistaDocumento.approvatoDocumenti ? '(Documenti Collaboratore approvati)' : '(Documenti Collaboratore da approvare)'}
	</span>
</div>