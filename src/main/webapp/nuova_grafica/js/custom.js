var $ = jQuery.noConflict();
$("document").ready(function() {
	
	$('.row.sel-car-btn input').on('change', function() {
		
		var cprice = $(".row.sel-car-btn input[name='optradio']:checked").parent('label').text();
		
		$('.row.sel-car-btn .col-md-6 label').removeClass('active');
		$(this).closest('label').addClass('active');
		
		var cname = $(this).closest( ".div-sel-car" ).find('h3').html();
		var cpara = $(this).closest( ".div-sel-car" ).find('.sel-car-btm-info .col-md-12').html();
		
		//console.log(cprice);
		$('.carname').text(cname);
		$('.carprice').text(cprice);
		$('.bottom-p').html(cpara);
		
		
		//var c = $(".row.sel-car-btn input[name='optradio']:checked").closest(".sel-car-btm-info").find("p").text();
		
		//var two = $(".row.sel-car-btn input[name='optradio']:checked").closest(".head-sel-car").find("h3").text();
		
		//alert(a + " b " + b + " caap " + c );
		
	});
	
	
	$( function() {
		$( ".datepicker" ).datepicker();
	} );
	
	$('.timepicker').wickedpicker();
	
	$("li:first-child, tr:first-child, td:first-child").addClass("first");
	$("li:last-child, tr:last-child, td:last-child").addClass("last");
	
	//Get Year
	var now = new Date;
	thecopyrightYear = now.getYear();
	if (thecopyrightYear < 1900) thecopyrightYear = thecopyrightYear + 1900;
	$(".curYear").html(thecopyrightYear);
	
	$('#example').DataTable();
	
	/*Ammar JS Start*/
	
    $('#ret-check').change(function(){
    var c = this.checked ? 'block' : 'none';
    $('.return-date-time').css('display', c);
	});
	
	/*$('#ret-check').change(function(){
	  var c = this.checked ? "checked" : "unchecked";
		if(c == "checked"){
			addret();
		}else{
			remret();
		}
    
	});*/

	
	$('.home-content').each(function(){
  $("header .navdiv nav .navbar-nav li.first a").addClass("current");
 });
	
	/*$("#submit-step-1").click(function () {
		$(".form-step-1").fadeOut(500);
		$(".info-row").fadeOut(500);
		$(".form-step-2").delay(500).fadeIn(500);
		$(".steps-row ul li").removeClass("step-active");
		$(".step-2").addClass("step-active");
	});*/
	$("#submit-step-1").click(function () {
		var numItems = $('.form-step-1 .has-error').length;
		if(numItems >= 1){
			alert("Please fill in the form completely.");
		}
		else {
			var depdate = $("#datepicker-departure").val();
			var deptime = $("#timepicker-departure").val();
			var arrdate = $("#datepicker-arrival").val();
			var arrtime = $("#timepicker-arrival").val();
			if ( arrdate.length > 0 ){
				$(".div-dep-2-arr").css("display","block"); 
			}
			var deppgr = $("#inputPerson").val();
			
			/*distance start*/
			var origin = document.getElementById("inputDeparture").value;
			var destination = document.getElementById("inputArrival").value;
			
			if ( origin.length > 0 && destination.length > 0 && depdate.length > 0 && deptime.length > 0 && deppgr.length > 0){
			
			
			var origin1 = origin;
			/*var origin2 = 'Nagan Chowrangi Flyover, Karachi';*/
			var destinationA = destination;
			/*var destinationB = {lat: 50.087, lng: 14.421};*/
			
			var geocoder = new google.maps.Geocoder;
			/*var myerror = 0;*/
			
			var service = new google.maps.DistanceMatrixService;
			service.getDistanceMatrix({
			origins: [origin1],
			destinations: [destinationA],
			travelMode: 'DRIVING',
			unitSystem: google.maps.UnitSystem.METRIC,
			avoidHighways: false,
			avoidTolls: false
			}, function(response, status) {
			if (status !== 'OK') {
			alert('Error was: ' + status);
			} else { 
			var originList = response.originAddresses;
			var destinationList = response.destinationAddresses;
			/*alert(originList.length + " andv " + destinationList.length);
			if ( originList.length <= 1 && destinationList.length <= 1 ){
				myerror = 1;
			}*/

			for (var i = 0; i < originList.length; i++) {
			  var results = response.rows[i].elements;
			  for (var j = 0; j < results.length; j++) {
					var outdep = originList[i];
					var outarr = destinationList[j];
					var outdis = results[j].distance.text;
					var outtime = results[j].duration.text;		
					/*alert("dep: " + originList[i] + "arr: " + destinationList[j] + "distance: " + results[j].distance.text + "time: " + results[j].duration.text );*/
					$('.prnt-dep').text(origin);
					$('.prnt-arr').text(destination);
					$('.prnt-dis').text(outdis);
					var suffix = outdis.match(/\d+/);
					var suffixbytwo = suffix * 2;
					$('.prnt-ret').text(suffixbytwo);
					$('.prnt-dur').text(outtime);
			  }
			}
			}
			
			/*if(myerror == 1) {alert("error 1");}else {alert("error 0");}*/
			});
			/*distance end*/
			$('.prnt-date').text(depdate);
			$('.prnt-time').text(deptime);
			$('.prnt-date-arr').text(arrdate);
			$('.prnt-time-arr').text(arrtime);
			$('.prnt-pgr').text(deppgr);
			
			$(".steps-row ul li").removeClass("step-active");
		$(".step-2").addClass("step-active");
			/*step-1 fadeOut*/
			$(".form-step-1").fadeOut(500);
			$(".info-row").delay(500).fadeOut(500);
			$(".form-step-2").delay(500).fadeIn(500);
		}else {alert("Please fill all fields properly.");}
		}
	});
	
	$("#btn-ver-email").click(function () {
			var verEmail = $("#inputEmail").val();
			var isChecked = $('.row.sel-car-btn input[type="radio"]').attr('checked');
			if ( verEmail.length > 0 && validateEmail(verEmail)){
				$('.usr-email-out').text(verEmail);
				$(".pop-up-email").fadeIn(500);
				$(".pop-up img").delay(3000).fadeOut(500);
				$(".pop-bg .col-md-6").delay(3500).fadeIn(1000);
			}else {
				alert("Please Enter Valid Email.");
			}
	});
	
	$("#btn-ver-phone").click(function () {
			var verSms = $("#inputPhone").val();
			if(isNumeric(verSms) && verSms.length == 11) { 
			$(".pop-up-sms").fadeIn(500);
			$(".pop-up img").delay(3000).fadeOut(500);
			$(".pop-bg .col-md-6").delay(3500).fadeIn(1000);
			} 
			else { alert('Input Valid number 11 digits number'); }
	});
	
	$(".phlogin-link").click(function () {
			$(".pop-up-login").fadeIn(500);
			$(".pop-bg").fadeIn(500);
	});
	
	$("#ver-email-pro, #ver-email-pr").click(function () {
			$(".steps-row ul li").removeClass("step-active");
			$(".step-3").addClass("step-active");
				
			$(".pop-up-login").fadeOut(500);
			$(".pop-up-email").fadeOut(500);
			$(".form-step-2").delay(500).fadeOut(500);
			$(".form-step-3").delay(500).fadeIn(500);
			$('.form-step-3 .has-error').removeClass('has-error');
	});
	
	$("#ver-login").click(function () {
			var logemail = $("#logemail").val();
			var logpass = $("#logpass").val();
			if ( logemail != "" && logpass != "" ){
				$(".steps-row ul li").removeClass("step-active");
				$(".step-3").addClass("step-active");
					
				$(".pop-up-login").fadeOut(500);
				$(".form-step-2").delay(500).fadeOut(500);
				$(".form-step-3").delay(500).fadeIn(500);
				$('.form-step-3 .has-error').removeClass('has-error');
			}else {
				alert("Please fill the both fields.");
				
			}
			
	});
	
	$("#ver-sms-pro").click(function () {
			var verSmsCode = $("#inputSmsCode").val();
			if ( verSmsCode == 1234 ){
				$(".steps-row ul li").removeClass("step-active");
			$(".step-3").addClass("step-active");
				
			$(".pop-up-sms").fadeOut(500);
			$(".form-step-2").delay(500).fadeOut(500);
			$(".form-step-3").delay(500).fadeIn(500);
			$('.form-step-3 .has-error').removeClass('has-error');
			}else {
				alert("Please Enter Valid Code.");
			}
			
	});
	$("#PaymentBtnCard").click(function () {
		var depdate = $("#inputName").val();
		var deptime = $("#inputCogname").val();
		var arrdate = $("#inputCliEmail").val();
		var arrtime = $("#inputCardName").val();
		var deppgr = $("#inputCardNumber").val();
			
		if ( depdate.length > 0 && deptime.length > 0 && arrdate.length > 0 && arrtime.length > 0 && deppgr.length > 0 && validateEmail(arrdate)){
			if(deppgr.length == 14){
				$(".form-step-3").fadeOut(500);
				$(".form-step-4").delay(500).fadeIn(500);
				$(".steps-row ul li").removeClass("step-active");
				$(".step-4").addClass("step-active");
			}
			else {alert("Please input valid 14 digits card number");}
		}
		else {alert("Please fill all the form fields properly.");}
		/*$(".form-step-3").fadeOut(500);
		$(".form-step-4").delay(500).fadeIn(500);
		$(".steps-row ul li").removeClass("step-active");
		$(".step-4").addClass("step-active");*/
	});
	$("#PaymentBtnPaypal").click(function () {
		var depdate = $("#inputName").val();
		var deptime = $("#inputCogname").val();
		var arrdate = $("#inputCliEmail").val();
		var arrtime = $("#inputPaypalName").val();
		var deppgr = $("#inputPaypalNumber").val();
			
		if ( depdate.length > 0 && deptime.length > 0 && arrdate.length > 0 && arrtime.length > 0 && deppgr.length > 0){
			if(deppgr.length == 14){
				$(".form-step-3").fadeOut(500);
				$(".form-step-4").delay(500).fadeIn(500);
				$(".steps-row ul li").removeClass("step-active");
				$(".step-4").addClass("step-active");
			}
			else {alert("Please input valid 14 digits card number");}
		}
		else {alert("Please fill all the form fields properly.");}
		/*$(".form-step-3").fadeOut(500);
		$(".form-step-4").delay(500).fadeIn(500);
		$(".steps-row ul li").removeClass("step-active");
		$(".step-4").addClass("step-active");*/
	});
	
	//tabs click functions
	$('[data-div]').on('click',function(){	
	if ( this.tagName == 'LI' ) {
		$('.wwd-li-active').removeClass('wwd-li-active');	
		$(this).addClass('wwd-li-active');
		var getTarget = $(this).data('div');		
	$('.sec-hide').each(function(){
		if($(this).hasClass(getTarget) == true){
			$('.sec-hide').hide();
			$(this).fadeIn();
			if ($(window).width() <= 767 && $(window).width() >= 100) {
				var hheight = $(".bg-img").height();
				var section = $(this).offset().top-hheight;
				$("body,html").animate({scrollTop:section},450);
			}
			}
		});
	}
		
	
	});
	
	//steps click functions
	$('[data-steps]').on('click',function(){
		var getTarget = $(this).data('steps');	
		$('.form-steps').each(function(){
			if($(this).hasClass(getTarget) == true){
				$('.form-steps').hide();
				$(this).fadeIn();
				}
		});
		
		//Progress Bar Remove Active class
		$('.steps-row ul li').each(function(){
			if($(this).attr("data-back") == getTarget){
				$('.steps-row ul li').removeClass("step-active");
				$(this).addClass("step-active");
				}
		});
	
	});


$('[data-contnt]').each(function(){
	var onloadtarget = getUrlVars()['rel'];
	if($(this).data('contnt') == onloadtarget){
		$('[data-contnt]').hide();
		$('.wwd-li-active').removeClass('wwd-li-active');
		$('[data-div="'+onloadtarget+'"]').addClass('wwd-li-active');
		$(this).fadeIn();
	}
});

	//tabs hover add class
	$(".wwd-li").hover(function(){
		$(".wwd-li").removeClass("bor");
		$(this).addClass("bor");
		}, function(){
        $(".wwd-li").removeClass("bor");
    });
	
	$('.close-popup').on('click',function(){
		$('.pop-up-container').fadeOut(500);
		$(".pop-up img").css("display","block");
		$(".pop-bg").css("display","none");
	});
	
	
	
	
	
}); /* Main Functino End's here */

function validateEmail($email) {
  var emailReg = /^([\w-\.]+@([\w-]+\.)+[\w-]{2,4})?$/;
  return emailReg.test( $email );
}

function isNumeric(n) {
  return !isNaN(parseFloat(n)) && isFinite(n);
}

/*Invert departure arrival*/
	
function invert() {
	
	var dep = $( "#inputDeparture" ).val();
	var arr = $( "#inputArrival" ).val();
	if(dep.length === 0 && arr.length === 0)
	{ alert("Please Input Both Departure and Arrival First.");}
	else {
	$( "#inputDeparture" ).val(arr);
	$( "#inputArrival" ).val(dep);
	}
}

/*add delete return row function*/
function addret() {
	$('return-date-time').css("display","block");
	$("#datepicker-arrival").prop('disabled', false);
	alert("add-ret-done");
	
}

function remret() {
	$('return-date-time').css("display","none");
	$("#datepicker-arrival").prop('disabled', true);
	alert("add-ret-removed");
	
}




      

$(window).scroll(function() { 
 var scroll = $(window).scrollTop();
 if (scroll >= 20) {
        $("header").addClass("darkHeader");
    } else {
        $("header").removeClass("darkHeader");
    }
});

$(window).on('load', function () {
});

$(window).resize(function() {	
});