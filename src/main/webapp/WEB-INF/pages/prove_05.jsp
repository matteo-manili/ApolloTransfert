<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="home.title"/></title>
    <meta name="breadcrumb" content="<fmt:message key="breadcrumb.prove"/>"/>
	
	<!-- jquery -->
	<link rel="stylesheet" href="<c:url value="/scripts/vendor/jquery-ui-1.11.4/jquery-ui.css"/>">
	<script src="<c:url value="/scripts/vendor/jquery-ui-1.11.4/jquery-ui.min.js"/>"></script>
	<script src="<c:url value="/scripts/vendor/jquery-ui-1.11.4/i18n/jquery-ui-i18n.min.js"/>"></script>

	<!-- geo plugin  -->
	<script language="JavaScript" src="http://www.geoplugin.net/javascript.gp" type="text/javascript"></script>

	<!-- dataTables -->
	<link rel="stylesheet" href="<c:url value="/scripts/vendor/DataTables/css/dataTables.bootstrap.css"/>">
	<script src="<c:url value="/scripts/vendor/DataTables/js/jquery.dataTables.js"/>"></script>
	<script src="<c:url value="/scripts/vendor/DataTables/js/dataTables.bootstrap.js"/>"></script>

	<!-- google api -->
	<script src="http://maps.google.com/maps/api/js"></script>

	<!-- jQuery countdown360 vedere: https://github.com/johnschult/jquery.countdown360 -->
	<script src="<c:url value="/js/jquery.countdown360.js"/>"></script>

	<script type="text/javascript">
	$(document).ready(function () {

		
	}); // fine ready
	</script>


</head>
<body>

<%@ include file="/common/messages.jsp" %>

<div class="">
	<div class="">
		<div class="row">
			<div class="col-sm-12" >	
			<!-- ----------------------------------------------------------------------------------------------------- -->
				
				<!-- vedere: https://github.com/jackocnr/intl-tel-input
				<demo: http://jackocnr.com/intl-tel-input.html -->
				<form id="contactForm" class="form-horizontal">
				    <div class="form-group">
				        <label class="col-sm-2 control-label">Phone number 3289126869</label>
				        <div class="col-sm-3">
				            <input type="tel" class="form-control" id="phoneId">
				        </div>
				        <div class="col-sm-2">
				        	<span id="valid-msg" class="hide text-success">Valid</span>
							<span id="error-msg" class="hide text-danger">Invalid number</span> 
				        </div>
				        <div class="col-sm-2">
				        	<input type="button" class="btn btn-sm btn-primary" id="btnTel" value="check">
				        </div>
				    </div>
				</form>
				
				<script type="text/javascript">
				
				$(document).ready(function() {
					
					//var stocazzo = "<fmt:message key='webapp.nameApollon'/>";
					//alert(stocazzo);
					
					var local = '${pageContext.request.locale.language}';
					//alert(local);

				  $.getScript('<c:url value="/scripts/vendor/intl-tel-input-9.0.3/build/js/Start_intlTelInput.js"/>');
				  
				  $("#btnTel").click(function(){
					  
					  var verifica = $("#phoneId").intlTelInput("isValidNumber");
					  //var country = $("#phoneId").intlTelInput.("getCountry");
					  var country = "aaa";
					  var extension = $("#phoneId").intlTelInput("getNumber");
					  alert( verifica + extension );
					});
				  
					var telInput = $("#phoneId"),
					errorMsg = $("#error-msg"),
					validMsg = $("#valid-msg");

					var reset = function() {
					telInput.removeClass("error");
					  errorMsg.addClass("hide");
					  validMsg.addClass("hide");
					};

					// on blur: validate
					telInput.blur(function() {
					  reset();
					  if ($.trim(telInput.val())) {
					    if (telInput.intlTelInput("isValidNumber")) {
					      validMsg.removeClass("hide");
					    } else {
					      telInput.addClass("error");
					      errorMsg.removeClass("hide");
					    }
					  }
					});

					// on keyup / change flag: reset
					telInput.on("keyup change", reset);

				});
				  
				</script>
			
				

			
				<%@ include file="/common/ric_payment_medio.jsp"%>
			
			
			
			
			
			
			<br><br>
			<!-- ----------------------------------------------------------------------------------------------------- -->
			</div>
		</div>
	</div>
</div>
</body>