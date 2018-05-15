var uluru = {lat: -25.363, lng: 131.044};
var map;
var infoWindow;
var directionsService;
var directionsDisplay;
var markers = [];
var from = "";
var to = "";
var colourArray = ['navy', 'grey', 'fuchsia', 'black', 'white', 'lime', 'maroon', 'purple', 'aqua', 'red', 'green', 'silver', 'olive', 'blue', 'yellow', 'teal'];

var requestArray = [], renderArray = [];

function myMap() {
    directionsService = new google.maps.DirectionsService();
    directionsDisplay = new google.maps.DirectionsRenderer();
    var mapProp = {
        center: new google.maps.LatLng(51.508742, -0.120850),
        zoom: 5
    };
    map = new google.maps.Map(document.getElementById("googleMap"), mapProp);
    initVrp();
}


function initVrp() {
    infoWindow = new google.maps.InfoWindow();
    let vrpId = window.location.href.split("/")[4];
    let url = '/api/vrp/'+vrpId;
    $.ajax({
        url: url,
        type: 'GET',
        success: function (response) {
            showMarkers(response);
        }
    })
}

function generateRequests(){

    requestArray = [];
    for (var route in jsonArray){
        // This now deals with one of the people / routes

        // Somewhere to store the wayoints
        var waypts = [];

        // 'start' and 'finish' will be the routes origin and destination
        var start, finish

        // lastpoint is used to ensure that duplicate waypoints are stripped
        var lastpoint

        data = jsonArray[route]

        limit = data.length
        for (var waypoint = 0; waypoint < limit; waypoint++) {
            if (data[waypoint] === lastpoint){
                // Duplicate of of the last waypoint - don't bother
                continue;
            }

            // Prepare the lastpoint for the next loop
            lastpoint = data[waypoint]

            // Add this to waypoint to the array for making the request
            waypts.push({
                location: data[waypoint],
                stopover: true
            });
        }

        // Grab the first waypoint for the 'start' location
        start = (waypts.shift()).location;
        // Grab the last waypoint for use as a 'finish' location
        finish = waypts.pop();
        if(finish === undefined){
            // Unless there was no finish location for some reason?
            finish = start;
        } else {
            finish = finish.location;
        }

        // Let's create the Google Maps request object
        var request = {
            origin: start,
            destination: finish,
            waypoints: waypts,
            travelMode: google.maps.TravelMode.DRIVING
        };

        // and save it in our requestArray
        requestArray.push({"route": route, "request": request});
    }

    processRequests();
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

function showMarkers(vrp) {

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
    window.location.href = "/loads/" + val;
}
