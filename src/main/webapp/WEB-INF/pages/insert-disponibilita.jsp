<%@ include file="/common/taglibs.jsp"%>
<head>
	<title><fmt:message key="disponibilita.title"/></title>
	
	<link rel="stylesheet" href="<c:url value="/scripts/vendor/jquery-ui-1.11.4/jquery-ui.css"/>">
	
	<script src="<c:url value="/scripts/vendor/jquery-ui-1.11.4/external/jquery/jquery-2.2.4.min.js"/>"></script>
	<script src="<c:url value="/js/bootstrap.min.js"/>"></script>
	<script src="<c:url value="/scripts/vendor/jquery-ui-1.11.4/jquery-ui.min.js"/>"></script>
	
	<script src="<c:url value="/scripts/vendor/jquery-ui-1.11.4/i18n/jquery-ui-i18n.min.js"/>"></script>
	<script src="<c:url value="/scripts/vendor/jquery-ui.multidatespicker.js"/>"></script>


	<style type="text/css">
	/* begin: jQuery UI Datepicker moving pixels fix */
		table.ui-datepicker-calendar {border-collapse: separate;}
		.ui-datepicker-calendar td {border: 1px solid transparent;}
	/* end: jQuery UI Datepicker moving pixels fix */
	
	/* begin: jQuery UI Datepicker emphasis on selected dates */
	.ui-datepicker .ui-datepicker-calendar .ui-state-highlight a {
		background: #743620 none; /* a color that fits the widget theme */
		color: white; /* a color that is readeable with the color above */
	}
	/* end: jQuery UI Datepicker emphasis on selected dates */
	
	</style>


<script type="text/javascript">
jQuery(document).ready(function(){
	
	$(".toolTip").tooltip();
		
	$('.classCheckBoxSospendiAutoveicolo').on('change',function(){
		//alert(111);
		var form = $( "#autistaForm" );
		form.submit();
	});
       
});
</script>
	
</head>

<body>

<div class="content-area home-content">
<div class="container">
<div class="col-md-12">

	<spring:bind path="autista.*">
	        <c:if test="${not empty status.errorMessages}">
	            <div class="alert alert-danger alert-dismissable">
	                <a href="#" data-dismiss="alert" class="close">&times;</a>
	                <c:forEach var="error" items="${status.errorMessages}">
	                    <c:out value="${error}" escapeXml="false"/><br/>
	                </c:forEach>
	            </div>
	        </c:if>
	    </spring:bind>
	
	<%@ include file="/common/messages.jsp" %>

	<%@ include file="/common/menu_autista.jsp" %>
	
	<h3>Disponibilità (${autista.user.fullName})</h3>
	
	<form:form commandName="autista" modelAttribute="autista" method="post" action="insert-disponibilita" id="autistaForm" autocomplete="off">
	<form:hidden path="id"/>
	<c:set var="tooltipSospendiAutoveicolo" value="Non si riceverrano più chiamate per questo Autoveicolo"/>
	<c:set var="tooltipGiorniSospensioneServizio" value="Seleziona i giorni di sospensione dal servizio"/>
	
	<div class="col-sm-12 row form-group">
		<label class="form-control-label text-info">Piano Ferie: Seleziona i giorni di sospensione dal servizio.</label>
		<hr>
		<c:forEach items="${autoveicoli}" var="varAuto" varStatus="status" begin="0">
			<div class=" ">
				<div class="form-group col-sm-6">
					<h5 class="${varAuto.autoveicolo.autoveicoloSospeso == true ? 'text-muted':'text-primary'}">
					<strong>${varAuto.autoveicolo.modelloAutoNumeroPosti.modelloAutoScout.marcaAutoScout.name} &nbsp; ${varAuto.autoveicolo.modelloAutoNumeroPosti.modelloAutoScout.name} - ${varAuto.autoveicolo.targa} - <fmt:message key="${varAuto.autoveicolo.tipoAutoveicolo.nome}"/></strong>
					</h5>
				</div>
				<div class="form-group col-sm-6">
					<label class="${varAuto.autoveicolo.autoveicoloSospeso == true ? 'text-danger':'text-warning'} toolTip checkbox-inline" title="${tooltipSospendiAutoveicolo}">
					 <input type="checkbox" class="classCheckBoxSospendiAutoveicolo" name="autoveicoloSospeso_[${varAuto.autoveicolo.id}]" 
						value="${varAuto.autoveicolo.id}" ${varAuto.autoveicolo.autoveicoloSospeso == true ? 'checked':''} />
					 ${varAuto.autoveicolo.autoveicoloSospeso == true ? 'Autoveicolo Sospeso ':'Sospendi Autoveicolo '} </label>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-8 calendar toolTip" id="calendarAuto${varAuto.autoveicolo.id}">
					<input type="hidden" class="treMesi" id="id_text_calendar_${varAuto.autoveicolo.id}">
				</div>
			</div>
			
			<script type="text/javascript">
			$(document).ready(function () {
			var local = '<fmt:message key="language.code"/>';
				$(function() {
					$.datepicker.setDefaults( $.datepicker.regional[local] ); // questo setta il language del multiDatesPicker
					var idAuto = ${varAuto.autoveicolo.id};
					$( "#calendarAuto"+${varAuto.autoveicolo.id} ).multiDatesPicker({
						dateFormat: 'dd-mm-yy',
						numberOfMonths: [1,2],
						//altField: ".treMesi",
						//altField: '#treMesi_id',
						addDates: ${varAuto.dateSelezionateLong},
						disabled: ${varAuto.autoveicolo.autoveicoloSospeso},
						
						onSelect: function(dateText, inst) {
					        var date = $(this).val();
					        //var time = $('#time').val();
					        //alert(idElement+date);
					        $('#id_text_calendar_'+idAuto).val(date)
					        $.ajax({
					  		  type: 'POST',
					  		  url: '${pageContext.request.contextPath}/impostaDisponibilita',
					  		  data : {
					  			  date : date,
					  			  idAuto : idAuto
					  			},
					  			dataType: 'text',
					  			success: function( data ) {
					  				//alert(data);
					  				if (data!=''){
					  					//alert(idElement);
					  				}else{
					  					//$('#spanEsitoDateDisp').text('');
					  				}
					  			},
					  			error: function (data) {
					  				//$('#spanEsitoDateDisp').text('');
					  			}
					  		});
					    }
					}); //fine multiDatesPicker
			
				}); // fine function
			}); // fine ready
			
			</script>
			
		</c:forEach>
	
	</form:form>

</div>
</div>
</div>

</body>