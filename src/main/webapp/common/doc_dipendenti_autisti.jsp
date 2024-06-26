<div class="row form-group ">
	<div class="col-sm-10">
		<div class='input-group'>
			<span class='input-group-addon'>
				<i class='fa fa-file-text'></i>
			</span>
			<input type="text" id="inputProvinciaRuoloConducenti" class="form-control" placeholder="Inserisci Provincia Camera di Commercio Albo dei Conducenti, Dipendente">
		</div>
	</div>
</div>
<%
try{
%>
<c:if test="${not empty autistaSottoAutista}">
	<div class="panel panel-info">
	<div class="panel-heading">Autista iscritto al Ruolo dei <i>"Conducenti Servizi Pubblici non di Linea"</i> della Camera di Commercio di <strong>${autistaSottoAutista.documentiIscrizioneRuolo.provinciaRuoloConducenti.nomeProvincia}</strong></div>
		<div class="panel-body">
			<input type="hidden" name="sotto-autista-id-provincia-ruolo" value="${autistaSottoAutista.documentiIscrizioneRuolo.provinciaRuoloConducenti.id}" placeholder="Numero Ruolo Conducenti Dipendente...">
			<div class="row">
				<div class="col-sm-6 form-group">
					<input type="text" name="sotto-autista-numero-ruolo" value="${autistaSottoAutista.documentiIscrizioneRuolo.numeroRuoloConducenti}" class=" form-control " placeholder="Numero Albo Ruolo Conducenti Dipendente...">
				</div>
				<div class="col-sm-6 form-group">
					<input type="text" name="sotto-autista-data-iscrizione-ruolo" value="<fmt:formatDate pattern="dd/MM/yyyy" value="${autistaSottoAutista.documentiIscrizioneRuolo.dataIscrizioneRuoloConducenti}"/>" class=" form-control " placeholder="01/01/2016 data iscrizione...">
				</div>
			</div>
			<div class="row">
				<div class="col-sm-6 form-group">
					<input type="text" name="sotto-autista-nome-autista" value="${autistaSottoAutista.nome}" class=" form-control " placeholder="Nome Autista Dipendente...">
				</div>
				<div class="col-sm-6 form-group">
					<input type="text" name="sotto-autista-cognome-autista" value="${autistaSottoAutista.cognome}" class=" form-control " placeholder="Cognome Autista Dipendente...">
				</div>
			</div>
			<div class="row">
				<div class="col-sm-3 form-group">
					<input type="text" name="sotto-autista-patente-autista" value="${autistaSottoAutista.documentiPatente.numeroPatente}" class=" form-control " placeholder="Numero Patente Dipendente Dipendente...">
				</div>
				
				<div class="col-sm-3 form-group">
					<input type="text" name="sotto-autista-scadenza-patente-autista" class="form-control" 
						value="<fmt:formatDate pattern="dd/MM/yyyy" value="${autistaSottoAutista.documentiPatente.dataScadenzaPatente}"/>" placeholder="01/01/2030 Data Scad. Patente Dipendente..." />
				</div>
				<div class="col-sm-3 form-group">
					<input type="text" name="sotto-autista-cap-autista" value="${autistaSottoAutista.documentiCap.numeroCAP}" class=" form-control " placeholder="Numero CAP Dipendente...">
				</div>
				
				<div class="col-sm-3 form-group">
					<input type="text" name="sotto-autista-scadenza-cap-autista" class="form-control" 
						value="<fmt:formatDate pattern="dd/MM/yyyy" value="${autistaSottoAutista.documentiCap.dataScadenzaCAP}"/>" placeholder="01/01/2030 Data Scad. CAP Dipendente..." />
				</div>

			</div>
			<div class="row">
				<div class="col-sm-12 form-group">
					<label class="control-label ">Carica Documento Ruolo Conducenti (MAX ${maxMbDcument}MB)</label>
					<input type="file" name="sotto-autista-documento-iscrizione-ruolo" data-show-upload="false" class="file"> 
				</div>
			</div>
			<div class="row">
				<div class="col-sm-12 form-group">
					<label class="control-label ">Carica Documento Patente "Fronte"(MAX ${maxMbDcument}MB)</label>
					<input type="file" name="sotto-autista-documento-patente-fronte" data-show-upload="false" class="file"> 
				</div>
			</div>
			<div class="row">
				<div class="col-sm-12 form-group">
					<label class="control-label ">Carica Documento Patente "Retro"(MAX ${maxMbDcument}MB)</label>
					<input type="file" name="sotto-autista-documento-patente-retro" data-show-upload="false" class="file"> 
				</div>
			</div>
			<div class="row">
				<div class="col-sm-12 form-group">
					<label class="control-label ">Carica Documento CAP (MAX ${maxMbDcument}MB)</label>
					<input type="file" name="sotto-autista-documento-cap" data-show-upload="false" class="file"> 
				</div>
			</div>
		</div>
		<div class="panel-footer">
			<button type="submit" name="inserisci-sotto-autista" class="btn btn-info attesaLoader">Inserisci Dipendente</button>
		</div>
	</div>
</c:if>
<c:if test="${not empty autistaSottoAutistiList && fn:length(autistaSottoAutistiList) gt 0}">
	<div class=" list-group ">
	<c:forEach items="${autistaSottoAutistiList}" var="varObj">
		<div class=" list-group-item clearfix ">
			<span class="fa fa-user"></span>
			<strong>${varObj.nome}&nbsp;${varObj.cognome}</strong> <br>
			Iscritto all'Albo del Ruolo dei Conducenti di <strong>${varObj.documentiIscrizioneRuolo.provinciaRuoloConducenti.nomeProvincia}</strong> 
			Numero <strong>${varObj.documentiIscrizioneRuolo.numeroRuoloConducenti}</strong> Data Iscrizione: <strong><fmt:formatDate pattern="dd/MM/yyyy" value="${varObj.documentiIscrizioneRuolo.dataIscrizioneRuoloConducenti}"/></strong><br>
			Patente: <strong>${varObj.documentiPatente.numeroPatente}</strong> Scadenza Patente: <strong><fmt:formatDate pattern="dd/MM/yyyy" value="${varObj.documentiPatente.dataScadenzaPatente}" /></strong>
			CAP: <strong>${varObj.documentiCap.numeroCAP}</strong> Scadenza CAP: <strong><fmt:formatDate pattern="dd/MM/yyyy" value="${varObj.documentiCap.dataScadenzaCAP}" /></strong><br>
			<a href="<c:url value="/getFileAmazonStore?objectModel=DocumentiIscrizioneRuolo&objectModelId=${varObj.documentiIscrizioneRuolo.id}&objectModelFileName=nomeFileDocumentoRuoloConducenti"/>" id="trigger">${varObj.documentiIscrizioneRuolo.nomeFileDocumentoRuoloConducenti}</a><br>
			<a href="<c:url value="/getFileAmazonStore?objectModel=DocumentiPatente&objectModelId=${varObj.documentiPatente.id}&objectModelFileName=nomeFilePatenteFronte"/>" id="trigger">${varObj.documentiPatente.nomeFilePatenteFronte}</a><br>
			<a href="<c:url value="/getFileAmazonStore?objectModel=DocumentiPatente&objectModelId=${varObj.documentiPatente.id}&objectModelFileName=nomeFilePatenteRetro"/>" id="trigger">${varObj.documentiPatente.nomeFilePatenteRetro}</a><br>
			<a href="<c:url value="/getFileAmazonStore?objectModel=DocumentiCap&objectModelId=${varObj.documentiCap.id}&objectModelFileName=nomeFileCAP"/>" id="trigger">${varObj.documentiCap.nomeFileCAP}</a> 
			<span class="pull-right">
				<span class="text-${varObj.approvato ? 'success' : 'danger'}">
					${varObj.approvato ? '<small>(Documenti Autista approvati)</small>' : '<small>(Documenti Autista da approvare)</small>'}
				</span>
				<button type="button" name="remove-sotto-autista" value="${varObj.id}" class="btn btn-xs btn-danger alertConfirmGenerale"
					${varObj.approvato ? 'disabled' : ''}>Elimina Dipendente Autista
         				<span class="glyphicon glyphicon-trash"></span>
       			</button>
       			<c:if test="${pageContext.request.isUserInRole('ROLE_ADMIN')}">
				<input type="checkbox" ${varObj.approvato ? 'checked':''} data-toggle="toggle" data-size="small" 
					onchange="$('#sotto-autista-approvato-id').val(${varObj.id});$('#autistaForm').submit();" data-onstyle="success" data-offstyle="warning">
				</c:if>
			</span>
		</div>
	</c:forEach>
	<input type="hidden" id="sotto-autista-approvato-id" name="sotto-autista-approvato">
	</div>
</c:if>
<%
}catch(Exception exc){
	exc.getMessage();
}
%>