<%@ include file="/common/taglibs.jsp"%>
<head>
<title><fmt:message key="gestioneCorse.title"/></title>
<meta name="breadcrumb" content="<fmt:message key="breadcrumb.gestione.corse"/>"/>
<meta name="description" content="" />
<meta name="keywords" content="" />
<!-- jquery -->
<link rel="stylesheet" href="<c:url value="/scripts/vendor/jquery-ui-1.11.4/jquery-ui.css"/>">
<script src="<c:url value="/scripts/vendor/jquery-ui-1.11.4/external/jquery/jquery-2.2.4.min.js"/>"></script>
<script src="<c:url value="/js/bootstrap.min.js"/>"></script>
<!-- per il parcing del template -->
 <script src="<c:url value="/js/mustache.js"/>"></script>
<!-- calendario e time-->
<script src="<c:url value="/scripts/vendor/pickadate.js-master/lib/compressed/picker.js"/>"></script>
<script src="<c:url value="/scripts/vendor/pickadate.js-master/lib/compressed/picker.date.js"/>"></script>
<script src="<c:url value="/scripts/vendor/pickadate.js-master/lib/compressed/picker.time.js"/>"></script>
<script src="<c:url value="/scripts/vendor/pickadate.js-master/lib/compressed/translations/"/><fmt:message key="language.code"/>.js"></script>
<link rel="stylesheet" href="<c:url value="/scripts/vendor/pickadate.js-master/lib/compressed/themes/default.css"/>">
<link rel="stylesheet" href="<c:url value="/scripts/vendor/pickadate.js-master/lib/compressed/themes/default.date.css"/>">
<link rel="stylesheet" href="<c:url value="/scripts/vendor/pickadate.js-master/lib/compressed/themes/default.time.css"/>">
<!-- alert confirm bootstrap -->
<script src="<c:url value="/scripts/vendor/bootbox.min.js"/>"></script>
<!-- per far brillare le corse in esecuzione -->
<link rel="stylesheet" href="<c:url value="/css/blinkeffect.css"/>">

<%@ include file="/scripts/apollotransfert-js-utility.jsp"%>
<%@ include file="/scripts/apollotransfert-js-utility-admin.jsp"%>
<%@ include file="/common/TemplateCorseAdmin.jsp"%>

<script type="text/javascript">
$(document).ready(function () {
	
	var corsaAgendaAutistaAdminTpl = $('#corsaAgendaAutistaAdminTpl').html();
	var corseMultipleAdminTpl = $('#corseMultipleAdminTpl').html();
	var corsaParticolareAdminTpl = $('#corsaParticolareAdminTpl').html();
    var corsaMediaAdminTpl = $('#corsaMediaAdminTpl').html();
    
    Mustache.parse(corsaAgendaAutistaAdminTpl); // optional, speeds up future uses
    Mustache.parse(corseMultipleAdminTpl); // optional, speeds up future uses
    Mustache.parse(corsaParticolareAdminTpl); // optional, speeds up future uses
    Mustache.parse(corsaMediaAdminTpl); // optional, speeds up future uses

   	AjaxCall();
	
   	$(".aggiornaPagina").on('click', function(event){
   		AjaxCall();
   	});
    
	function Start_EveryAjaxCall(){
		var everyTimeMillSeconds = 10000; // 1000 � uguale a un secondo
		var repeater = setInterval(function () {
		AjaxCall();
		}, everyTimeMillSeconds);
	}
	
	function AjaxCall(){
	    $.ajax({
	    	type: 'POST',
			url: '${pageContext.request.contextPath}/caricaGestioneCorseAdmin',
			dataType: "json",
	        data: {
	        	dateFrom : $('#calendarioFromHidden').val(),
	        	dateTo : $('#calendarioToHidden').val(),
	        	codCorsa : $('#codCorsaId').val(),
	        	inApprov : $('#inApprov').is(':checked'),
	        	approv : $('#approv').is(':checked'),
	        	nonApprov : $('#nonApprov').is(':checked')
	        },
	        beforeSend: function(){
	        	//$("#idEsito-"+idElement).html("<h4>contattando l'autista..</h4>");
	        },
	        success: function(result) {
                //alert(result['mainCorse']);
                var html="";
                $.each(result, function(index, val){
                  for(var i=0; i < val.length; i++){
	                  var item = val[i];
                      //alert(item.template);
                      
                      if(item.template == "AGENDA_AUTISTA_DA_ESEGUIRE" || item.template == "AGENDA_AUTISTA_ESEGUITA"){
                        html+= Mustache.render(corsaAgendaAutistaAdminTpl, item);
                      
                      }else if(item.template == "MULTIPLA_DA_ESEGUIRE" || item.template == "MULTIPLA_ESEGUITA"){
                        html+= Mustache.render(corseMultipleAdminTpl, item);
                        
                      }else if(item.template == "PARTICOLARE_DA_ESEGUIRE" || item.template == "PARTICOLARE_ESEGUITA"){
                        html+= Mustache.render(corsaParticolareAdminTpl, item);
                      
                      }else if( item.template == "MEDIA" ){
                        html+= Mustache.render(corsaMediaAdminTpl, item);
                      }
                  }
                  $("#corseA").html(html);
                });

	         }, //fine success
			error: function (req, status, error) {
				//alert('errore ajax caricaGestioneCorseAdmin - ritorno a home-user');
				//location.reload(true); //questo ricarica la pagina
		    	window.location.replace('${pageContext.request.contextPath}/home-user');
			}
	    });
	}

	function blink() {
	    $('.blinkeffect').fadeOut(500).fadeIn(500);
	}
	setInterval(blink, 1000);

	//----- CALENDARIO ---------
	var from_$input = $('#calendarioFrom').pickadate({
		//format: 'd mmmm, yyyy',
		formatSubmit: 'yyyy/mm/dd',
		onClose: function() {
			  $(document.activeElement).blur();
			}
	}), from_picker = from_$input.pickadate('picker')

	 //$('#calendarioFromHidden').val( event.select )
	 //$('#calendarioToHidden').val( event.select );

	var to_$input = $('#calendarioTo').pickadate({
		onClose: function() {
			  $(document.activeElement).blur();
			}
	}), to_picker = to_$input.pickadate('picker')

	// Check if there’s a “from” or “to” date to start with.
	if ( from_picker.get('value') ) {
	  to_picker.set('min', from_picker.get('select'))
	}
	if ( to_picker.get('value') ) {
	  from_picker.set('max', to_picker.get('select'))
	}

	// When something is selected, update the “from” and “to” limits.
	from_picker.on('set', function(event) {
	  if ( event.select ) {
		  $('#calendarioFromHidden').val(  event.select );
	    to_picker.set('min', from_picker.get('select'))
	  }
	  else if ( 'clear' in event ) {
		  $('#calendarioFromHidden').val( '' );
	    to_picker.set('min', false)
	  }
	})
	to_picker.on('set', function(event) {
	  if ( event.select ) {
		  $('#calendarioToHidden').val(  event.select );
	    from_picker.set('max', to_picker.get('select'))
	  }
	  else if ( 'clear' in event ) {
		  $('#calendarioToHidden').val( '' );
	    from_picker.set('max', false)
	  }
	});
	
}); // fine ready
</script>
</head>
<body>
<%@ include file="/common/messages.jsp" %>
<div class="content-area home-content">
<div class="container">
<div class="">

	<c:if test="${pageContext.request.isUserInRole('ROLE_GEST_AUTISTA')}">
		<div class="alert alert alert-success" role="alert">		
			Le funzioni di Assegnazione e Disdetta Corsa sono disabilitate, questa pagina permette solo la consulazione delle Corse
		</div>
	</c:if>

	<div class=" " style="/*position: fixed;*/" >
		<div class="row form-group " >
			<div class=" col-sm-3">
				<div class="input-group ">
					<span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span>
					<input type="text" class="form-control" id="calendarioFrom" placeholder="da...">
					<input type="hidden" id="calendarioFromHidden">
				</div>
			</div>
			<div class=" col-sm-3">
				<div class="input-group ">
					<span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span>
					<input type="text" class="form-control" id="calendarioTo" placeholder="a...">
					<input type="hidden" id="calendarioToHidden">
				</div>
			</div>
			<div class=" col-sm-2">
				<input type="text" class="form-control" id="codCorsaId" placeholder="num. corsa">
			</div>
			<div class=" col-sm-2 ">
				<button type="button" class="btn btn-success btn-sm aggiornaPagina">AGGIORNA PAGINA <span class="fa fa-refresh"></span></button>
			</div>
			<div class="col-sm-2 ">
				<label class="checkbox text-warning">
					<input type="checkbox" id="inApprov" class="" checked value=""><strong>(Da Eseguire) In Approvazione</strong></label>
				<label class="checkbox text-success">
					<input type="checkbox" id="approv" class="" value=""><strong>(Eseguite) Approvata</strong></label>
				<label class="checkbox text-danger">
					<input type="checkbox" id="nonApprov" class="" value=""><strong>(Non Eseguite) Non Approvata</strong></label>
			</div>
		</div>
	</div>
	
	<div class=" form-group" style="/*padding-top: 250px;*/">
		<div id="corseA"></div>
	</div>
	
</div>
</div>
</div> <!-- fine div container  -->

</body>