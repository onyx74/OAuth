var uluru = {lat: -25.363, lng: 131.044};
var map;
var infoWindow;
var directionsService;
var directionsDisplay;

function myMap() {
    directionsService = new google.maps.DirectionsService();
    directionsDisplay = new google.maps.DirectionsRenderer();
    var mapProp = {
        center: new google.maps.LatLng(51.508742, -0.120850),
        zoom: 5,
    };
    map = new google.maps.Map(document.getElementById("googleMap"), mapProp);
    initAllLoads();
}


function initAllLoads() {
    infoWindow = new google.maps.InfoWindow();
    $.ajax({
        url: '/api/loads/current',
        type: 'GET',
        data: {
            pageSize: 200,
            page: 1
        },
        success: function (response) {
            console.log(response);
            var res = response.loads.content;
            for (var i = 0; i < res.length; ++i) {
                var param = "'" + res[i].startAddress + "','" + res[i].finishAddress + "'";
                var position = {lat: res[i].startLatitude, lng: res[i].startLongitude};
                var contentString =
                    '<br/>' +
                    '<h4>Start Address: ' + res[i].startAddress + ' </h4>' +
                    '<br/>' +
                    '<h4>Finish Address: ' + res[i].finishAddress + '</h4>' +
                    '<br/>' +
                    '<h4>Price: ' + res[i].price + '</h4>' +
                    '<br/>' +
                    '<h4>Truck Type: ' + res[i].truckType + '</h4>' +
                    '<div class="row"><div class="col-lg-5"><button class="col-lg-10 btn btn-sm btn-info" onclick="details(' + res[i].loadId + ')">Details </button></div>' +
                    '<div class="col-lg-5"><button class=" col-lg-10 btn btn-sm btn-success"  onclick="showDistance(' + param + ')">Show Distance</button></div></div>';


                createMarker(position, contentString);
            }
            google.maps.event.addListener(map, 'click', function () {
                infoWindow.close();
                directionsDisplay.setMap(null);
            });
        }
    })
}

function createMarker(position, contentString) {
    var marker = new google.maps.Marker({
        position: position,
        map: map
    });
    google.maps.event.addListener(marker, 'click', function () {


        // including content to the infowindow
        infoWindow.setContent(contentString);

        // opening the infowindow in the current map and at the current marker location
        infoWindow.open(map, marker);
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
