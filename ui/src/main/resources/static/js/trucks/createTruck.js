function initMap() {

    var input = /** @type {!HTMLInputElement} */(
        document.getElementById('currentPossition'));
    var autocomplete = new google.maps.places.Autocomplete(input);
    var infowindow = new google.maps.InfoWindow();
    autocomplete.addListener('place_changed', function () {
        infowindow.close();
        var address = '';

        address = [
            place.address_components[0],
            place.address_components[1],
            place.address_components[2]
        ].join(' ');

        infowindow.setContent('<div><strong>' + place.name + '</strong><br/></div>' + address);
        // infowindow.open(map, marker);
    });

}

function createTruck() {
    $("#ownername").val(currentUserName);
    var formData = $('#form').serialize();
    $.ajax({
        url: '/api/trucks',
        type: 'POST',
        data: formData
    });
    window.location.href = "/myTrucks";
}
