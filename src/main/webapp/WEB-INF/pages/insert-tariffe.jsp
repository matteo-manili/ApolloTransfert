<%@ include file="/common/taglibs.jsp"%>
<head>
<title><fmt:message key="tariffe.title"/></title>
<link rel="stylesheet" href="<c:url value="/scripts/vendor/jquery-ui-1.11.4/jquery-ui.css"/>">
<script src="<c:url value="/scripts/vendor/jquery-ui-1.11.4/external/jquery/jquery-2.2.4.min.js"/>"></script>
<script src="<c:url value="/js/bootstrap.min.js"/>"></script>
	
<script type="text/javascript">
jQuery(document).ready(function(){
	$(".toolTip").tooltip();
}); //fine ready
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

<h3>Tariffe (${autista.user.fullName})</h3>

<%
try {
%>

<c:if test="${empty tariffe_Zone_List }">
	<div class="alert alert-info h4"><big><strong><i class="fa fa-exclamation-triangle "></i> Nota!</strong>
		<a href="<c:url value='insert-tragitti'/>" class="alert-link">Inserisci Zona Lavoro</a></big>
	</div>
</c:if>
<c:if test="${not empty tariffe_Zone_List }">
	<c:if test="${empty tariffe_Zone_List[0].tariffe_AutoveicoliTariffeList}">
		<div class="alert alert-info h4"><big><strong><i class="fa fa-exclamation-triangle "></i> Nota!</strong>
			<a href="<c:url value='insert-autoveicolo'/>" class="alert-link">Inserisci Autoveicolo</a></big>
		</div>
	</c:if>
</c:if>

<!-- NOME ZONA - PROVINCA O COMUNE -->
<c:forEach items="${tariffe_Zone_List}" var="varObj" varStatus="status" begin="0">
<c:if test="${not empty varObj.tariffe_AutoveicoliTariffeList}">
<div class=" panel panel-default">
	<div class="panel-heading"><h4 class="panel-title">
	<c:choose>
		<c:when test="${not empty varObj.autistaZona.province}">
			<div class="panel-heading"><strong>${varObj.autistaZona.province.nomeProvincia} (Provincia)</strong></div>
		</c:when>
	</c:choose>
	</h4></div>
	<div class="panel-body">
	
	<c:forEach items="${varObj.tariffe_AutoveicoliTariffeList}" var="varObjAutoTariffa" varStatus="status" begin="0">
		<div class="panel panel-info">
		<div class="panel-heading"><h4 class="panel-title"><h3><span class="label label-primary"><i class="fa fa-car"></i> <fmt:message key="${varObjAutoTariffa.autoveicolo.classeAutoveicoloReale.nome}"/></span></h3>
			<strong> ${varObjAutoTariffa.autoveicolo.modelloAutoNumeroPosti.modelloAutoScout.marcaAutoScout.name}&nbsp;
				${varObjAutoTariffa.autoveicolo.modelloAutoNumeroPosti.modelloAutoScout.name}&nbsp;
				${varObjAutoTariffa.autoveicolo.annoImmatricolazione}&nbsp;
				${varObjAutoTariffa.autoveicolo.targa}&nbsp;
				<fmt:message key="posti.auto.autista">
					<fmt:param value="${varObjAutoTariffa.autoveicolo.modelloAutoNumeroPosti.numeroPostiAuto.numero}"/>
				</fmt:message></strong></h4>
				&nbsp;${varObjAutoTariffa.autoveicolo.autoveicoloCartaCircolazione.approvatoCartaCircolazione ? '<span class="label label-success">Auto Attiva (Approvata Carta Circolazione)</span>':'<span class="label label-default">Auto Non Approvata (Caricare Carta Circolazione)</span>'}
		
		</div>
		<div class="panel-body">
		<ul class="list-inline">
			<c:forEach items="${varObjAutoTariffa.compensoAutistaCorse}" var="varObjCompensoAutistaCorse">
			<li class="list-group-item">
				<h4><span class="label label-primary">Compenso ${varObjCompensoAutistaCorse.compensoAutista}&euro;</span></h4>
				<h4><span class="label label-success">${varObjCompensoAutistaCorse.kilometri} km</span></h4>
				<h5><span class="label label-danger">${varObjCompensoAutistaCorse.tariffaPerKm} &euro;/km</span></h5>
			</li>
			</c:forEach>
		</ul>
		</div>
		</div>
	</c:forEach>
	
	</div> <!-- fine panel body -->
</div> <!-- fine panel default -->
</c:if>
</c:forEach>

<!-- Tariffe per provincia -->
<c:if test="${not empty tariffe_Zone_List }">
	<div class="alert alert-info">
		<strong>Le Tariffe si intendono per Corse di Sola Andata (Ritorno Senza Passeggeri) e sono il compenso Effettivo dell'Autista.</strong>
		<span class="testo-anno-auto-prima-classe"></span>
	</div>
</c:if>


<!-- Classificazione modelli Auto -->
<div class="panel panel-default">
	<!-- Default panel contents -->
	<div class="panel-heading"><strong>Classificazione Modelli Auto</strong></div>
	<div class="panel-body">
		<div class="form-group">
			<input type="text" id="inputMarca" onkeyup="myFunction()" class="form-control " placeholder="Filtra per Marca esempio... Mercedes">
		</div>
  	</div>
  	
  	<div class="table-responsive">
	<table class="table table-bordered table-striped" id="myTable">
		<tr>
			<th><h6><strong>Marca e Modello</strong></h6></th>
			<th class="text-center"><h6><strong><fmt:message key="${classi_auto_list[0].nome}"/></strong></h6></th>
			<th class="text-center"><h6><strong><fmt:message key="${classi_auto_list[1].nome}"/> dal (${anno_minino_prima_classe})&#42;</strong></h6></th>
			<th class="text-center"><h6><strong><fmt:message key="${classi_auto_list[2].nome}"/> dal (${anno_minino_prima_classe})&#42;</strong></h6></th>
			<th class="text-center"><h6><strong><fmt:message key="${classi_auto_list[3].nome}"/></strong></h6></th>
			<th class="text-center"><h6><strong><fmt:message key="${classi_auto_list[4].nome}"/> dal (${anno_minino_prima_classe})&#42;</strong></h6></th>
		</tr>
		<c:forEach items="${modelloAutoveicoloList}" var="varObj" varStatus="status" begin="0">
		<tr>
			<td><h6>${varObj.marcaAutoScout.name}&nbsp;${varObj.name}</h6></td>
			<td class="text-center">${varObj.classeAutoveicolo.id == 1 ? '<i class="fa fa-circle"></i>':''}</td>
			<td class="text-center">${varObj.classeAutoveicolo.id == 2 ? '<i class="fa fa-circle"></i>':''}</td>
			<td class="text-center">${varObj.classeAutoveicolo.id == 3 ? '<i class="fa fa-circle"></i>':''}</td>
			<td class="text-center">${varObj.classeAutoveicolo.id == 4 ? '<i class="fa fa-circle"></i>':''}</td>
			<td class="text-center">${varObj.classeAutoveicolo.id == 5 ? '<i class="fa fa-circle"></i>':''}</td>
		</tr>
		</c:forEach>
	</table>
	</div>
	<div class="panel-footer">
		<span class="testo-anno-auto-prima-classe"></span>
	</div>
</div>


<script type="text/javascript">
$(".testo-anno-auto-prima-classe").append("<h6><big><strong>&#42;</strong></big>Gli Autoveicoli: <strong><fmt:message key='${classi_auto_list[1].nome}'/></strong> " 
	+"e <strong><fmt:message key='${classi_auto_list[4].nome}'/></strong> immatricolati prima del <strong>${anno_minino_prima_classe}</strong> "
	+"si considerano <strong><fmt:message key='${classi_auto_list[0].nome}'/></strong>.<br>"
	+"<big><strong>&#42;</strong></big>Gli Autoveicoli: <strong><fmt:message key='${classi_auto_list[2].nome}'/></strong> " 
	+"immatricolati tra il <strong>${anno_minino_prima_classe}</strong> e il <strong>${anno_minino_luxury_intermedia}</strong> si considerano <strong><fmt:message key='${classi_auto_list[1].nome}'/></strong> "
	+"e quelli immatricolati prima del <strong>${anno_minino_luxury_intermedia}</strong> si considerano <strong><fmt:message key='${classi_auto_list[0].nome}'/></strong>.</h6>");
</script>


<script>
function myFunction() {
  var input, filter, table, tr, td, i;
  input = document.getElementById("inputMarca");
  filter = input.value.toUpperCase();
  table = document.getElementById("myTable");
  tr = table.getElementsByTagName("tr");
  for (i = 0; i < tr.length; i++) {
    td = tr[i].getElementsByTagName("td")[0];
    if (td) {
      if (td.innerHTML.toUpperCase().indexOf(filter) > -1) {
        tr[i].style.display = "";
      } else {
        tr[i].style.display = "none";
      }
    }       
  }
}
</script>


</div>
</div>
</div>

<%@ include file="/scripts/ConfirmSubmit.jsp"%>

<%
}catch (final Exception e) {
	e.getMessage();
}
%>

<!-- questa validazione funziona se nel form si usa il pulsante submit e non un link come sto usando -->
<c:set var="scripts" scope="request">
<script type="text/javascript">
// This is here so we can exclude the selectAll call when roles is hidden
// nel bottone submit: onclick="onFormSubmit( autoveicoloForm );"
function onFormSubmit(theForm) {
    return validateTariffe(theForm);
}
</script>
</c:set>

<v:javascript formName="autista" staticJavascript="false"/>
<script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"></script>

</body>