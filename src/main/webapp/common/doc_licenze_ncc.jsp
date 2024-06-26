<div class="form-group row">
	<div class="col-sm-6">
		<div class='input-group'>
			<span class='input-group-addon'>
				<i class='fa fa-file-text'></i>
			</span>
			<input type="text" id="inputZoneMobile" class=" form-control " placeholder="Inserisci Comune Licenza...">
		</div>
	</div>
</div>
<c:if test="${not empty autistaLicenzeNcc}">
	<div class="panel panel-info">
	<div class="panel-heading">Autista Licenza NCC del Comune di <strong>${autistaLicenzeNcc.comune.nomeComune}&nbsp;<i class="fa fa-university"></i></strong></div>
		<div class="panel-body">
			<input type="hidden" name="id-comune-licenza" value="${autistaLicenzeNcc.comune.id}">
			<div class="row">
				<div class="col-sm-6 form-group">
					<label class="control-label ">Numero Licenza (Sono valore Numerico)</label>
					<input type="text" name="numero-licenza" value="${autistaLicenzeNcc.numeroLicenza}" class="form-control ">
				</div>
			</div>
			<div class="row">
				<div class="col-sm-12 form-group">
					<label class="control-label ">Carica Documento Licenza NCC (MAX ${maxMbDcument}MB)</label>
					<input type="file" name="licenza-documento" data-show-upload="false" class="file"> 
				</div>
			</div>
		</div>
		<div class="panel-footer">
			<button type="submit" name="aggiungi-licenza" class="btn btn-info attesaLoader">Inserisci Licenza</button>
		</div>
	</div>
</c:if>

<c:if test="${not empty autistaLicenzeNccList && fn:length(autistaLicenzeNccList) gt 0}">
	<div class="form-group list-group ">
	<c:forEach items="${autistaLicenzeNccList}" var="varObj">
		<div class=" list-group-item clearfix  ">
			<span class="fa fa-file-text"></span> Licenza NCC del Comune di <strong>${varObj.comune.nomeComune} N. ${varObj.numeroLicenza}&nbsp;<i class="fa fa-university"></i></strong><br>
			<a href="<c:url value="/getFileAmazonStore?objectModel=AutistaLicenzeNcc&objectModelId=${varObj.id}&objectModelFileName=nomeFileDocumentoLicenza"/>" id="trigger">${varObj.nomeFileDocumentoLicenza}</a> 
			<div class="pull-right">
				<span class="text-${varObj.approvato ? 'success' : 'danger'}">
					${varObj.approvato ? '<small>(Documento Licenza NCC approvato)</small>' : '<small>(Documento Licenza NCC da approvare)</small>'}
				</span>
				<button type="button" name="remove-licenza-ncc" value="${varObj.id}" class="btn btn-xs btn-danger alertConfirmGenerale"
					${varObj.approvato ? 'disabled' : ''}>Elimina Licenza
         				<span class="glyphicon glyphicon-trash"></span>
       			</button>
       			<c:if test="${pageContext.request.isUserInRole('ROLE_ADMIN')}">
					<input type="checkbox" ${varObj.approvato ? 'checked':''} data-toggle="toggle" data-size="small" 
						onchange="$('#licenza-ncc-approvato-id').val(${varObj.id});$('#autistaForm').submit();" data-onstyle="success" data-offstyle="warning">
				</c:if>
			</div>
		</div>
	</c:forEach>
	<input type="hidden" id="licenza-ncc-approvato-id" name="licenza-ncc-approvato">
	</div>
</c:if>