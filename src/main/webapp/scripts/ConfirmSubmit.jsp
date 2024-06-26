<script src="<c:url value="/scripts/vendor/bootbox.min.js"/>"></script>
<script type="text/javascript">
// QUANDO LE FUNZIONI VENGONO CHIAMATE DA onsubmit DEL FORM OPPUERE DAL onclick DELLE INPUT, 
// LE FUNZIONI DEVONO ESSERE SCRITTE FUORI DALLO JQUERY PERKE SI TROVA IN UN ALTRO CONTESTO.
// VEDERE: http://stackoverflow.com/questions/3371632/why-cant-i-use-onclick-to-execute-a-function-inside-a-jquery-document-ready

$(document).ready(function() {
	
$('.close-popup').on('click',function(){
	$('.pop-up-container').fadeOut(500);
});
	
//$(".attesaLoaderPopUp").fadeIn(300);
	
//----------- LOADER
$(document).on("click", ".attesaLoader", function(e) {
	// raccolgo le class scritte dentro la class, se è presente il confirm generale non devo fare il loader, bensi farlo dopo il submit del confirm
	var classList = $(this).attr('class').split(/\s+/); 
	if( $.inArray( "alertConfirmGenerale", classList ) > -1 == false ){ //torna false se è presente la class alertConfirmGenerale
		//$("#attesaLoader").modal('show');
		$(".attesaLoaderPopUp").fadeIn(300);
	}
}); 

//-------- CONFIRM GENERALE (per fare si che funzioni l'input deve essere un button non un submit)
$(document).on("click", ".alertConfirmGenerale", function(e) {
	var formElement = $(this).closest('form'); //.attr('id');
	var nameElement = $(this).attr('name');
	var valueElement = $(this).val();
	//alert(formElement);
	//alert(nameElement);
	//alert(valueElement);
	bootbox.confirm({
	    //title: "",
	    message: "Sei sicuro di continuare?",
	    buttons: {
	    	confirm: {
	            label: '<i class="glyphicon glyphicon-ok"></i> <fmt:message key="button.continua"/>'
	        },
	        cancel: {
	            label: '<i class="glyphicon glyphicon-remove"></i> <fmt:message key="button.cancel"/>'
	        }
	    },
	    callback: function (resultBootBox) {
	    	if(resultBootBox){
	    		$(formElement).append("<input type='hidden' name='"+nameElement+"' value='"+valueElement+"' />");
	    		$( formElement ).submit();
	    		var classList = $(this).attr('class').split(/\s+/);
	    		if( $.inArray( "attesaLoader", classList ) < -1 == false ){
	    			//$("#attesaLoader").modal('show');
	    			$(".attesaLoaderPopUp").fadeIn(300);
	    		}
	    	}
	    }
	});
}); // fine alertConfirmGenerale
//-------- per fare si che funzioni l'input deve essere un button non un submit
$(document).on("click", ".inviaSubmit", function(e) {
	var formElement = $(this).closest('form'); //.attr('id');
	var nameElement = $(this).attr('name');
	var valueElement = $(this).val();
	//alert(formElement.attr('id'));
	//alert(nameElement);
	//alert(valueElement);
	$(formElement).append("<input type='hidden' name='"+nameElement+"' value='"+valueElement+"' />");
	$( formElement ).submit();
}); // fine inviaSubmit


}); // fine ready
</script>

<!-- Forse il class="nocontent" esclude il testo dalla indicizzazione su google, vedere: https://support.google.com/customsearch/answer/2364585?hl=it -->
<div class="pop-up-container attesaLoaderPopUp nocontent">
	<div class="pop-up">
		<div class="container">	
			<div class="row">
				<div class="col-md-8 col-md-offset-2 text-center">
					<div class="pop-bg row">	
						<h4>Attendere Perfavore</h4>
						<div class="col-md-12" >
							<img src="<c:url value='/nuova_grafica/'/>images/loader.gif">
						</div>
					</div>	
				</div>
			</div>
		</div>	
	</div>
</div>

<!--googleoff: index-->
<!-- Forse il class="nocontent" esclude il testo dalla indicizzazione su google, vedere: https://support.google.com/customsearch/answer/2364585?hl=it -->
<div id="attesaLoader" class="modal fade nocontent" data-backdrop="static" tabindex="-1" role="dialog">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
            <!-- <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                	<span aria-hidden="true">&times;</span></button>  -->
        		<h4 class="modal-title" id="gridSystemModalLabel">Attendere Perfavore</h4>
            </div> 
            <div class="modal-body" >
				<div class="col-xs-offset-5 col-sm-offset-5 col-md-offset-5 col-lg-offset-5 center-block" id="loader"></div>
            </div>
            <!-- 
            <div class="modal-footer">
                <button type="button" class="btn btn-info" data-dismiss="modal">continua</button>
            </div>  -->
        </div>
    </div>
</div>
<!--googleon: index-->