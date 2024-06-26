<!-- DOCUMENTO CONTRATTO -->
<div class=" form-group">				
	<a href="<c:url value="/pdfDownloadContratto?idAutista=${autista.id}"/>" ><span class="glyphicon glyphicon-download-alt"></span> Scarica Contratto da Firmare</a>
</div>

<!-- Primo foglio contatto -->
<div class="form-group">				
	<label class="control-label ">Carica Documento Contratto (MAX ${maxMbDcument}MB)</label>
	<input type="file" name="documento-contratto" data-show-upload="false" 
		${autista.autistaDocumento.approvatoContratto ? 'readonly' : ''} class="file"> 
</div>
<c:if test="${not empty autista.autistaDocumento.nomeFileContratto}">	
<div class="row form-group ">
	<div class=" col-sm-5 ">
		<a href="<c:url value="/getFileAmazonStore?objectModel=Autista&objectModelId=${autista.id}&objectModelFileName=nomeFileContratto"/>" id="trigger">${autista.autistaDocumento.nomeFileContratto}</a>
	</div>
	<div class=" col-sm-5 ">
		<button type="button" name="remove-documento-contratto" class="btn btn-xs btn-danger alertConfirmGenerale" 
			${autista.autistaDocumento.approvatoContratto ? 'disabled' : ''} value="${autista.id}">Elimina Documento Contratto
				<span class="glyphicon glyphicon-trash"></span>
     	</button>
	</div>
</div>
</c:if>

<!-- Secondo foglio contatto -->
<div class="form-group">				
	<label class="control-label ">Carica Documento Contratto [Eventuale Foglio 2] (MAX ${maxMbDcument}MB)</label>
	<input type="file" name="documento-contratto-2" data-show-upload="false" 
		${autista.autistaDocumento.approvatoContratto ? 'readonly' : ''} class="file"> 
</div>
<c:if test="${not empty autista.autistaDocumento.nomeFileContratto_2}">	
<div class="row form-group ">
	<div class=" col-sm-5 ">
		<a href="<c:url value="/getFileAmazonStore?objectModel=Autista&objectModelId=${autista.id}&objectModelFileName=nomeFileContratto_2"/>" id="trigger">${autista.autistaDocumento.nomeFileContratto_2}</a>
	</div>
	<div class=" col-sm-5 ">
		<button type="button" name="remove-documento-contratto-2" class="btn btn-xs btn-danger alertConfirmGenerale" 
			${autista.autistaDocumento.approvatoContratto ? 'disabled' : ''} value="${autista.id}">Elimina Documento Contratto 2
				<span class="glyphicon glyphicon-trash"></span>
     	</button>
	</div>
</div>
</c:if>


<!-- SUBMIT -->
<div class=" form-group ">
	<button type="submit" name="inserisci-contratto" class="btn btn-info attesaLoader"
		${autista.autistaDocumento.approvatoContratto ? 'disabled' : ''}>Salva Contratto</button>
	<c:if test="${pageContext.request.isUserInRole('ROLE_ADMIN')}">
		<input type="checkbox" ${autista.autistaDocumento.approvatoContratto ? 'checked':''} data-toggle="toggle" data-size="small" 
			onchange="$('#contratto-approvato-id').val(${autista.autistaDocumento.approvatoContratto});$('#autistaForm').submit();" data-onstyle="success" data-offstyle="warning">
			<input type="hidden" id="contratto-approvato-id" name="contratto-approvato">
	</c:if>
</div>

<div class=" pull-right">
	<span class=" text-${autista.autistaDocumento.approvatoContratto ? 'success' : 'danger'}">
		${autista.autistaDocumento.approvatoContratto ? '(Contratto approvato)' : '(Documento Contratto da approvare)'}
	</span>
</div>