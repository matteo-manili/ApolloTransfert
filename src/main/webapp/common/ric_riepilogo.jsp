<div class="row form-row ">
	<div class="alert alert-info h4" role="alert">
	    <a href="<c:url value='home-user'/>" class="alert-link">Vai alla Home per i Dettagli della Corsa <i class="fa fa-home"></i></a>
	</div>
</div>

<!-- NON LO USO PIU funziona solo quando ho gli utente con le email fake nomeUtente@apollotransfert.com -->
<c:if test="${ fn:containsIgnoreCase(ricercaTransfert.user.email, '@apollotransfert.com') }">
	<div class="alert alert-info h4" role="alert">
		<a data-toggle="modal" href="#insertEmailModal" class="alert-link">Vuoi ricevere gli Aggiornamenti della Corsa via Email? <i class="fa fa-envelope"></i></a>
	</div>

	<!-- MODAL - AGGIORNAMENTI VIA EMAIL? -->
	<div class="modal fade col-xs-12" id="insertEmailModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	    <div class="modal-dialog">
	        <div class="modal-content">
	            <div class="modal-header">
	                <h4 class="modal-title"><i class="fa fa-envelope-o"></i> Inserisci Email</h4>
	            </div>
	            
	            <div class="modal-body">
		            <div class="row form-group">
		            	<span class="col-xs-12 col-sm-12 col-md-12 col-lg-12" id="esito_save_email"></span>
						<div class=" col-xs-12 col-sm-12 col-md-12 col-lg-12 ">
							<input type="text" id="emailCustomerId" class="form-control" autofocus="autofocus">
						</div>
					</div>
	            </div>
	            
	            <div class="modal-footer">
	            	<button type="button" class="btn btn-warning" data-dismiss="modal">Close</button>
	            	<button type="button" id="save-email-cliente-id" class="btn btn-info ">Salva</button>
	            </div>
	        </div>
	        <!-- /.modal-content -->
	    </div>
	    <!-- /.modal-dialog -->
	</div>

	<script type="text/javascript">
	jQuery(document).ready(function(){
		// questo serve a far vedere il prompt focus nel modal
		$('#insertEmailModal').on('shown.bs.modal', function() {
		    $('#emailCustomerId').focus();
		})
		$( "#save-email-cliente-id" ).click(function() {
			var emailCustomer = $('#emailCustomerId').val();
			$.ajax({
		    	type: 'POST',
	            url: '${pageContext.request.contextPath}/SalvaEmailClienteRiepilogo',
	            dataType: "json",
				data: {
					emailCustomer : emailCustomer
				},
				beforeSend: function(){ },
				success: function(result) {
					
					bootbox.alert({
						backdrop: true,
						message: result['esitoSalvaEmail-mesage']
					});
					
					if( result['esitoSalvaEmail'] ){
						// chiudo il modal
						$('#insertEmailModal').modal('toggle');
					}
	
				}, //fine success
		        error: function (req, status, error) {
				alert('errore ajax SalvaEmailClienteRiepilogo');}
			});
		});
		
	}); // fine ready
	</script>
</c:if>