<script type="text/javascript">
$(function () {
    setNavigation();
});
function setNavigation() {
    var path = window.location.pathname;
    path = path.replace(/\/$/, "");
    path = decodeURIComponent(path);
    //console.log('path: '+path);
    //console.log('window.location.pathname: '+window.location.pathname);
    //console.log('aaaa: '+ '${pageContext.request.contextPath}' );
    
    $(".nav a").each(function () {
        var href = $(this).attr('href');
        var resultSub = path.substring(0, href.length);
        if (resultSub === href && path === resultSub) {
        	//console.log('root: '+'${pageContext.request.contextPath}');
            //console.log('resultSub: '+resultSub);
        	//console.log('href: '+href);
            $(this).closest('li').addClass('active');
        }
    });
    if( window.location.pathname === '/' || path === '${pageContext.request.contextPath}' ){
    	document.getElementById("menuItemRoot").classList.add("active");
    	//console.log('menuItemRoot classList add');
    }
}

// serve per velocizzare la pagina al caricamento
$("#btnSubmitNewsLetter").attr("disabled", true); 
setTimeout(function(){
	jQuery.ajax({
		url: 'https://www.google.com/recaptcha/api.js?onload=onloadCallback&render=explicit&hl=<fmt:message key="language.code"/>',
	    dataType: 'script',
		success: function(result) { 
			$("#btnSubmitNewsLetter").attr("disabled", false); 
		},
		async: true
	});
}, 4000); //4000
var capcha1;
var onloadCallback = function(){
capcha1 = grecaptcha.render("capcha_111",{
  "sitekey": "${RECAPTCHA_PUBLIC_GLOBAL}",
  "badge": "inline",
  "type": "image",
  "size": "invisible",
  "callback": "onSubmitNewsLetter"
	});
};
$('#btnSubmitNewsLetter').click(function( event ) {
	if( $('#checkPrivacyPolicyId').is(":checked") ){
		event.preventDefault(capcha1);
		grecaptcha.execute(capcha1);
	}
});
function onSubmitNewsLetter(token) {
	var grecaptchaResponse = grecaptcha.getResponse(capcha1);
	//alert( grecaptchaResponse ); 
	var emailNewsLetter = $("#emailNewsLetter").val();
	$.ajax({
		type: "POST",
		url: '${pageContext.request.contextPath}/submit-news-letter',
		data: {
			emailNewsLetter: emailNewsLetter,
			captcha: grecaptchaResponse
		},
		beforeSend: function(){
			$("#idModalEsitoSubscriptNewsLetterText").html('');
			$(".img-loader").fadeIn(500);
			$(".newsLetterPopUp").fadeIn(500);
		},
		success: function(result) {
			$(".img-loader").fadeOut(500); //nascondo il loader
			$("#idModalEsitoSubscriptNewsLetterText").html('<div class="text-primary"><strong>'+result['message']+'</strong></div>');
			grecaptcha.reset(capcha1);
		},
		error: function(){
			alert("ERRORE NEWSLETTER");
			grecaptcha.reset(capcha1);
		}
	});
};
</script>