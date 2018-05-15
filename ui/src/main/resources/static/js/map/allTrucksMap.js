var uluru = {lat: -25.363, lng: 131.044};
var map;
var infoWindow;
var directionsService;
var directionsDisplay;
var markers = [];
var carModel = "";
var position = "";

function myMap() {
    directionsService = new google.maps.DirectionsService();
    directionsDisplay = new google.maps.DirectionsRenderer();
    var mapProp = {
        center: new google.maps.LatLng(51.508742, -0.120850),
        zoom: 5
    };
    map = new google.maps.Map(document.getElementById("googleMap"), mapProp);
    initAllTrucks();
}


function initAllTrucks() {
    infoWindow = new google.maps.InfoWindow();
    $.ajax({
        url: '/api/trucks/current',
        type: 'GET',
        data: {
            pageSize: 200,
            page: 1,
            carModel: carModel,
            position: position
        },
        success: function (response) {
            var trucks = response.trucks.content;
            showMarkers(trucks);
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

function showMarkers(trucks) {
    removeAllmarkers();
    for (var i = 0; i < trucks.length; ++i) {
        var position = {lat: trucks[i].latitude, lng: trucks[i].longitude};
        var contentString =
            '<br/>' +
            '<h4>Current Position: ' + trucks[i].currentPossition + ' </h4>' +
            '<h4>Price per mile : ' + trucks[i].pricePerMile + '$</h4>' +
            '<h4>Driver Name: ' + trucks[i].driverName + '</h4>' +
            '<h4>Truck Type: ' + trucks[i].truckType + '</h4>' +
            '<div class="row"><div class="col-lg-5"><button class="col-lg-10 btn btn-sm btn-info" onclick="details(' + trucks[i].truckId + ')">Details </button></div>';


        createMarker(position, contentString);
    }
    google.maps.event.addListener(map, 'click', function () {
        infoWindow.close();
        directionsDisplay.setMap(null);
    });
}


function details(val) {
    window.location.href = "/trucks/" + val + '/private';
}

function applayFilters() {
    position = $("#position").val();
    carModel = $("#carModel").val();
    initAllTrucks();

}

function resetFilters() {
    position = "";
    carModel = "";
    $("#position").val("");
    $("#carModel").val("");
    initAllTrucks();
}

function removeAllmarkers() {
    for (var i = 0; i < markers.length; ++i) {
        markers[i].setMap(null);
    }
    markers = [];
}