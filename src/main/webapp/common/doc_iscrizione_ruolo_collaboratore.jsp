<div class="row form-group ">
	<div class="col-sm-6">
		<div class='input-group'>
			<span class='input-group-addon'>
				<i class='fa fa-file-text'></i>
			</span>
			<input type="text" ${autista.autistaDocumento.approvatoDocumenti ? 'readonly' : ''} id="inputProvinciaRuoloConducenti-collaboratore" class="form-control" autocomplete="off" placeholder="Inserisci Provincia Camera di Commercio Albo dei Condicenti...">
		</div>
	</div>
</div>
<c:if test="${not empty autista.autistaDocumento.documentiIscrizioneRuolo}">
<div class="panel panel-info">
	<div class="panel-heading">Autista iscritto al Ruolo dei <i>"Conducenti Servizi Pubblici non di Linea"</i> della Camera di Commercio di <big><strong>${autista.autistaDocumento.documentiIscrizioneRuolo.provinciaRuoloConducenti.nomeProvincia}&nbsp;<i class="fa fa-university"></i></strong></big></div>
	<div class="panel-body">
		<input type="hidden" name="collaboratore-id-provincia-ruolo" value="${autista.autistaDocumento.documentiIscrizioneRuolo.provinciaRuoloConducenti.id}" placeholder="Numero Ruolo Conducenti...">
		<div class="row">
			<div class="col-sm-3 form-group">
				<label class=" form-control-label">Numero Albo Ruolo</label>
				<input type="text" ${autista.autistaDocumento.approvatoDocumenti ? 'readonly' : ''} name="collaboratore-numero-ruolo" value="${autista.autistaDocumento.documentiIscrizioneRuolo.numeroRuoloConducenti}" class=" form-control " placeholder="Numero Albo Ruolo Conducenti...">
			</div>
			<div class="col-sm-3 form-group">
				<label class=" form-control-label">Data Iscrizione Ruolo</label>
				<input type="text" ${autista.autistaDocumento.approvatoDocumenti ? 'readonly' : ''} name="collaboratore-data-iscrizione-ruolo" value="<fmt:formatDate pattern="dd/MM/yyyy" value="${autista.autistaDocumento.documentiIscrizioneRuolo.dataIscrizioneRuoloConducenti}"/>" class="form-control " placeholder="Data Iscrizione 01/01/2016...">
			</div>
			<div class="col-sm-3 form-group">
				<label class=" form-control-label">Numero CAP</label>
				<input type="text" ${autista.autistaDocumento.approvatoDocumenti ? 'readonly' : ''} name="collaboratore-cap" class="form-control" value="${autista.autistaDocumento.documentiCap.numeroCAP}" placeholder="Numero Cert. di Abilit. Prof. CAP..."/>
			</div>
			<div class="col-sm-3 form-group">
				<label class=" form-control-label">Data Scadenza CAP</label>
				<input type="text" ${autista.autistaDocumento.approvatoDocumenti ? 'readonly' : ''} name="collaboratore-scadenza-cap" value="<fmt:formatDate pattern="dd/MM/yyyy" value="${autista.autistaDocumento.documentiCap.dataScadenzaCAP}"/>" class="form-control " placeholder="Data Scadenza 01/01/2030...">
			</div>
		</div>
		<div class="row">
			<div class="col-sm-12 form-group">
				<label class="control-label ">Carica Documento Ruolo Conducenti (MAX ${maxMbDcument}MB)</label>
				<input type="file" name="collaboratore-documento-iscrizione-ruolo" data-show-upload="false" 
					${autista.autistaDocumento.approvatoDocumenti ? 'readonly' : ''} class="file"> 
			</div>
		</div>
		<c:if test="${not empty autista.autistaDocumento.documentiIscrizioneRuolo.nomeFileDocumentoRuoloConducenti}">	
		<div class="row form-group ">
			<div class=" col-sm-5 ">
				<a href="<c:url value="/getFileAmazonStore?objectModel=DocumentiIscrizioneRuolo&objectModelId=${autista.autistaDocumento.documentiIscrizioneRuolo.id}&objectModelFileName=nomeFileDocumentoRuoloConducenti"/>" id="trigger">${autista.autistaDocumento.documentiIscrizioneRuolo.nomeFileDocumentoRuoloConducenti}</a>
			</div>
			<div class=" col-sm-5 ">
				<button type="button" name="remove-collaboratore-documento-iscrizione-ruolo" value="${autista.autistaDocumento.documentiIscrizioneRuolo.id}" class="btn btn-xs btn-danger alertConfirmGenerale"
					${autista.autistaDocumento.approvatoDocumenti ? 'disabled' : ''}>Elimina Documento Ruolo Conducenti
         				<span class="glyphicon glyphicon-trash"></span>
       			</button>
			</div>
		</div>
		</c:if>
		<!-- DOCUMENTO CAP -->
		<div class=" form-group">				
			<label class="control-label ">Carica Documento CAP (MAX ${maxMbDcument}MB)</label>
			<input type="file" name="collaboratore-documento-cap" data-show-upload="false" 
				${autista.autistaDocumento.approvatoDocumenti ? 'readonly' : ''} class="file"> 
		</div>
		<c:if test="${not empty autista.autistaDocumento.documentiCap.nomeFileCAP}">	
		<div class="row form-group ">
			<div class=" col-sm-5 ">
				<a href="<c:url value="/getFileAmazonStore?objectModel=DocumentiCap&objectModelId=${autista.autistaDocumento.documentiCap.id}&objectModelFileName=nomeFileCAP"/>" id="trigger">${autista.autistaDocumento.documentiCap.nomeFileCAP}</a>
			</div>
			<div class=" col-sm-5 ">
				<button type="button" name="remove-collaboratore-documento-cap" class="btn btn-xs btn-danger alertConfirmGenerale" 
					${autista.autistaDocumento.approvatoDocumenti ? 'disabled' : ''} value="${autista.autistaDocumento.documentiCap.id}">Elimina Documento CAP 
						<span class="glyphicon glyphicon-trash"></span>
		     	</button>
			</div>
		</div>
		</c:if>
		
	</div>
</div>

<!-- SUBMIT -->
<div class=" form-group ">
	<button type="submit" ${autista.autistaDocumento.approvatoDocumenti ? 'disabled' : ''} name="salva-ruolo-collaboratore" class="btn btn-info attesaLoader">Salva Documento</button>
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
</c:if>