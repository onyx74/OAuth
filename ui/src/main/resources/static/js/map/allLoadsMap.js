var uluru = {lat: -25.363, lng: 131.044};
var map;
var infoWindow;
var directionsService;
var directionsDisplay;
var markers = [];
var from = "";
var to = "";

function myMap() {
    directionsService = new google.maps.DirectionsService();
    directionsDisplay = new google.maps.DirectionsRenderer();
    var mapProp = {
        center: new google.maps.LatLng(50.4593526, 30.4965838),
        zoom: 5,
    };
    map = new google.maps.Map(document.getElementById("googleMap"), mapProp);
    initAllLoads();
}


function initAllLoads() {
    infoWindow = new google.maps.InfoWindow();
    $.ajax({
        url: '/api/loads/',
        type: 'GET',
        data: {
            pageSize: 200,
            page: 1,
            from: from,
            to: to
        },
        success: function (response) {
            var loads = response.loads.content;
            showMarkers(loads);
        }
    })
}

function createMarker(position, contentString) {
    var marker = new google.maps.Marker({
        position: position,
        map: map
    });
    markers.push(marker);
    google.maps.event.addListener(marker, 'click', function () {
        // including content to the infowindow
        infoWindow.setContent(contentString);
        // opening the infowindow in the current map and at the current marker location
        infoWindow.open(map, marker);
    });


}

function showMarkers(loads) {
    removeAllmarkers();
    for (var i = 0; i < loads.length; ++i) {
        var param = "'" + loads[i].startAddress + "','" + loads[i].finishAddress + "'";
        var position = {lat: loads[i].startLatitude, lng: loads[i].startLongitude};
        var contentString =
            '<br/>' +
            '<h4>Start Address: ' + loads[i].startAddress + ' </h4>' +
            '<h4>Finish Address: ' + loads[i].finishAddress + '</h4>' +
            '<h4>Price: ' + loads[i].price + '$</h4>' +
            '<h4>Distance: ' + loads[i].distance / 100 + ' km</h4>' +
            '<h4>Truck Type: ' + loads[i].truckType + '</h4>' +
            '<div class="row"><div class="col-lg-5"><button class="col-lg-10 btn btn-sm btn-info" onclick="details(' + loads[i].loadId + ')">Details </button></div>' +
            '<div class="col-lg-5"><button class=" col-lg-10 btn btn-sm btn-success"  onclick="showDistance(' + param + ')">Show Road</button></div></div>';


        createMarker(position, contentString);
    }
    google.maps.event.addListener(map, 'click', function () {
        infoWindow.close();
        directionsDisplay.setMap(null);
    });
}

function showDistance(startLocation, finishLocation) {

    directionsService.route({
        origin: startLocation,
        destination: finishLocation,
        travelMode: 'DRIVING'
    }, function (response, status) {
        if (status === 'OK') {
            directionsDisplay.setDirections(response);
            directionsDisplay.setMap(map);
        } else {
            window.alert('Directions request failed due to ' + status);
        }
    });

}


function details(val) {
    window.location.href = '/loads/' + val + '/private';
}

function applayFilters() {
    from = $("#from").val();
    to = $("#to").val();
    initAllLoads();

}

function resetFilters() {
    from = "";
    to = "";
    $("#from").val("");
    $("#to").val("");
    initAllLoads();
}

function removeAllmarkers() {
    for (var i = 0; i < markers.length; ++i) {
        markers[i].setMap(null);
    }
    markers = [];
}