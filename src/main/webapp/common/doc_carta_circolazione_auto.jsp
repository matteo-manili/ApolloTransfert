<c:choose>
	<c:when test="${not empty autoveicoloList}">
		<c:forEach items="${autoveicoloList}" var="varObj">
		<div class="row" style="padding-bottom: 20px;">
			<div class="col-sm-12 form-group" >				
				<label class="control-label ">${varObj.modelloAutoNumeroPosti.modelloAutoScout.marcaAutoScout.name}
				&nbsp;${varObj.modelloAutoNumeroPosti.modelloAutoScout.name}&nbsp;${varObj.targa}&nbsp;${varObj.annoImmatricolazione}
				&nbsp;<fmt:message key="posti.auto.autista"><fmt:param value="${varObj.modelloAutoNumeroPosti.numeroPostiAuto.numero}"/></fmt:message> 
				- Carica Documento Carta Circolazione (MAX ${maxMbDcument}MB)</label>
				<input type="file" name="auto-documento-carta-circolazione-${varObj.id}" data-show-upload="false" 
					${varObj.autoveicoloCartaCircolazione.approvatoCartaCircolazione ? 'readonly' : ''} class="file "> 
			</div>
			<c:if test="${not empty varObj.autoveicoloCartaCircolazione.nomeFileCartaCircolazione}">	
				<div class="col-sm-3 form-group">
					<a href="<c:url value="/getFileAmazonStore?objectModel=Autoveicolo&objectModelId=${varObj.id}&objectModelFileName=nomeFileCartaCircolazione"/>" id="trigger">${varObj.autoveicoloCartaCircolazione.nomeFileCartaCircolazione}</a>
				</div>
				<div class="col-sm-4 form-group">
					<span class=" text-${varObj.autoveicoloCartaCircolazione.approvatoCartaCircolazione ? 'success' : 'danger'}">
						${varObj.autoveicoloCartaCircolazione.approvatoCartaCircolazione ? '<small>(Carta di Circolazione approvata)</small>' : '<small>(Carta di Circolazione da approvare)</small>'}
					</span>
				</div>
				<div class="col-sm-5 form-group">
					<button type="button" name="remove-auto-documento-carta-circolazione" value="${varObj.id}" class="btn btn-xs btn-danger alertConfirmGenerale"
						${varObj.autoveicoloCartaCircolazione.approvatoCartaCircolazione ? 'disabled' : ''}>Elimina Documento Carta Circolazione
		        				<span class="glyphicon glyphicon-trash"></span>
		      			</button>
		      			<c:if test="${pageContext.request.isUserInRole('ROLE_ADMIN')}">
					<input type="checkbox" ${varObj.autoveicoloCartaCircolazione.approvatoCartaCircolazione ? 'checked':''} data-toggle="toggle" data-size="small" 
						onchange="$('#carta-circolazione-approvata-id').val(${varObj.id});$('#autistaForm').submit();" data-onstyle="success" data-offstyle="warning">
					</c:if>
				</div>
			</c:if>
		</div>
		</c:forEach>
		<input type="hidden" id="carta-circolazione-approvata-id" name="carta-circolazione-approvata">
		<div class="form-group ">
			<button type="submit" name="inserisci-carta-circolazione" class="btn btn-info attesaLoader">Salva Carta di Circolazione</button>
		</div>
	</c:when>
	
	<c:otherwise>
		<div class="alert alert-info">
	  		<a href="<c:url value="/insert-autoveicolo"/>">Inserisci un Autoveicolo</a>
		</div>
	</c:otherwise>
</c:choose>