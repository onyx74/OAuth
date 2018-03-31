function initMap() {

    var input = /** @type {!HTMLInputElement} */(
        document.getElementById('startAddress'));
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
    var input = /** @type {!HTMLInputElement} */(
        document.getElementById('finishAddress'));
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

function createLoad() {
    $("#username").val(currentUserName);
    var formData = $('#form').serialize();
    $.ajax({
        url: '/api/loads',
        type: 'POST',
        data: formData
    });
    window.location.href = "/allLoads";
}
