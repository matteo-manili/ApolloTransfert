<script src="https://maps.googleapis.com/maps/api/js?key=${GOOGLE_API_MAP_JS}&libraries=places&language=${pageContext.request.locale.language}"></script>
<script type="text/javascript">
// ------- GOOGLE AUTOCOMPLETE ---------------------------
// vedi: www.stackoverflow.com/questions/499126/jquery-set-cursor-position-in-text-area
function setSelectionRange(input, selectionStart, selectionEnd) {
    if (input.setSelectionRange) {
        input.focus();
        input.setSelectionRange(selectionStart, selectionEnd);
    }
    else if (input.createTextRange) {
        var range = input.createTextRange();
        range.collapse(true);
        range.moveEnd('character', selectionEnd);
        range.moveStart('character', selectionStart);
        range.select();
    }
}

var componentForm = {
    street_number: 'short_name',
    route: 'long_name',
    locality: 'long_name',
    localityS: 'short_name',
    administrative_area_level_1: 'short_name',
    administrative_area_level_2: 'short_name',
    administrative_area_level_3: 'short_name',
    country: 'long_name',
    postal_code: 'short_name'
};
   
$('#idPartenza').on('input',function(e){
		$('#partenzaPlaceID').val('');
});
$('#idArrivo').on('input',function(e){
		$('#arrivoPlaceID').val('');
});

var options = { componentRestrictions: {country: ['${NAZIONI[0]}','${NAZIONI[1]}','${NAZIONI[2]}','${NAZIONI[3]}','${NAZIONI[4]}']} }; // max 5 (italia,francia,svizzera,austria,san marino)
var autocompleteP = new google.maps.places.Autocomplete( $("#idPartenza")[0], options );
var autocompleteA = new google.maps.places.Autocomplete($("#idArrivo")[0], options);
google.maps.event.addListener(autocompleteP, 'place_changed', function() {
	var place = autocompleteP.getPlace();
	$('#partenzaPlaceID').val(place.place_id);
	var streetNumberBoolP = true; var routeP = ''; var streetNumberP = '';
	var localityP = ''; var administAreaLevel_2P = ''; var countryP = '';
	for(var i = 0; i < place.address_components.length; i++) {
         var addressType = place.address_components[i].types[0];
         if(addressType == 'street_number' || jQuery.inArray("point_of_interest", place.types) !== -1){
             streetNumberBoolP = false;
         }
         if(componentForm[addressType]) {
             var val = place.address_components[i][componentForm[addressType]];
             if(addressType == 'route'){
                 routeP = val +", "; $('#routeP').val(val);
             }
             if(addressType == 'street_number'){
                 streetNumberP = val +", "; $('#streetNumberP').val(val);
             }
             if(addressType == 'locality'){
                 localityP = val +", "; $('#localityP').val(val);
             }
             if(addressType == 'administrative_area_level_2'){
                 administAreaLevel_2P = val +", "; $('#administAreaLevel_2P').val(val);
             }
             if(addressType == 'country'){
                 countryP = val +", "; $('#countryP').val(val);
             }
         }
     }
});

google.maps.event.addListener(autocompleteA, 'place_changed', function() {
	var place = autocompleteA.getPlace();
	$('#arrivoPlaceID').val(place.place_id);
	var streetNumberBoolA = true; var routeA = ''; var streetNumberA = '';
	var localityA = ''; var administAreaLevel_2A = ''; var countryA = '';
	for(var i = 0; i < place.address_components.length; i++) {
         var addressType = place.address_components[i].types[0];
         if(addressType == 'street_number' || jQuery.inArray("point_of_interest", place.types) !== -1){
             streetNumberBoolA = false;
         }
         if(componentForm[addressType]) {
             var val = place.address_components[i][componentForm[addressType]];
             if(addressType == 'route'){
                 routeA = val +", "; $('#routeA').val(val);
             }
             if(addressType == 'street_number'){
                 streetNumberA = val +", "; $('#streetNumberA').val(val);
             }
             if(addressType == 'locality'){
                 localityA = val +", "; $('#localityA').val(val);
             }
             if(addressType == 'administrative_area_level_2'){
                 administAreaLevel_2A = val +", "; $('#administAreaLevel_2A').val(val);
             }
             if(addressType == 'country'){
                 countryA = val +", "; $('#countryA').val(val);
             }
         }
     }
});
</script>