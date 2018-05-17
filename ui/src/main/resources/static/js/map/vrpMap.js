var uluru = {lat: -25.363, lng: 131.044};
var map;
var infoWindow;
var directionsService;
var directionsDisplay;
var markers = [];
var from = "";
var to = "";
var colourArray = ['red', 'green', 'black', 'white', 'lime', 'maroon', 'purple', 'aqua', 'silver', 'olive', 'blue', 'yellow', 'teal'];
var colorNumber = 0;
var stepDisplay;


var requestArray = [], renderArray = [];

function myMap() {
    directionsService = new google.maps.DirectionsService();
    directionsDisplay = new google.maps.DirectionsRenderer();
    var mapProp = {
        center: new google.maps.LatLng(50.4593526, 30.4965838),
        zoom: 5
    };
    map = new google.maps.Map(document.getElementById("googleMap"), mapProp);
    stepDisplay = new google.maps.InfoWindow;
    initVrp();
}


function initVrp() {
    infoWindow = new google.maps.InfoWindow();
    let vrpId = window.location.href.split("/")[4];
    let url = '/api/vrp/' + vrpId;
    $.ajax({
        url: url,
        type: 'GET',
        success: function (response) {
            showMarkers(response);
        }
    })
}

var groupBy = function (xs, key) {
    return xs.reduce(function (rv, x) {
        (rv[x[key]] = rv[x[key]] || []).push(x);
        return rv;
    }, {});
};


function showMarkers(vrp) {
    var requestArray = [];
    var roads = groupBy(vrp.vrpItemEntities, "solutionId");
    $('#roadName').text(vrp.name);
    $('#totalDistance').text(vrp.totalDistance / 1000);
    showSteps(roads, vrp);
}

function processRequests(requestArray) {

    // Counter to track request submission and process one at a time;
    var i = 0;

    // Used to submit the request 'i'
    function submitRequest() {
        directionsService.route(requestArray[i].request, directionResults);
    }

    // Used as callback for the above request for current 'i'
    function directionResults(result, status) {
        if (status === google.maps.DirectionsStatus.OK) {

            // Create a unique DirectionsRenderer 'i'
            renderArray[i] = new google.maps.DirectionsRenderer();
            renderArray[i].setMap(map);

            // Some unique options from the colorArray so we can see the routes
            renderArray[i].setOptions({
                preserveViewport: true,
                suppressInfoWindows: true,
                polylineOptions: {
                    strokeWeight: 4,
                    strokeOpacity: 0.8,
                    strokeColor: colourArray[i]
                },
                markerOptions: {
                    icon: {
                        path: google.maps.SymbolPath.BACKWARD_CLOSED_ARROW,
                        scale: 3,
                        strokeColor: colourArray[i + 1]
                    }
                }
            });

            // Use this new renderer with the result
            renderArray[i].setDirections(result);
            // and start the next request
            nextRequest();
        }

    }

    function nextRequest() {
        // Increase the counter
        i++;
        // Make sure we are still waiting for a request
        if (i >= requestArray.length) {
            // No more to do
            return;
        }
        // Submit another request
        submitRequest();
    }

    // This request is just to kick start the whole process
    submitRequest();
}

function showSteps(roads, startLocation) {
    $("#tableVrp tbody").empty();
    let body = $('#tableVrp').find('tbody');
    var marker = new google.maps.Marker({
        position: new google.maps.LatLng(startLocation.startLatitude, startLocation.startLongitude),
        map: map,
        label: "Start"
    });
    attachInstructionText(marker, "Start position  " + startLocation.startLocation, map);



    for (var roadId in roads) {
        var start = new google.maps.LatLng(startLocation.startLatitude, startLocation.startLongitude);
        colorNumber = colorNumber + 1;
        var road = roads[roadId];
        road.sort(function (a, b) {
            return a.position - b.position
        });
        var last;
        var loadsIds = [];
        for (var itemId in road) {
            var item = road[itemId];
            var finish = new google.maps.LatLng(item.startLatitude, item.startLongitude);
            var marker1 = new google.maps.Marker({
                position: finish,
                map: map,
                label: "Load " + (item.position + 1)
            });
            var url = '/loads/' + item.loadId + '/private';
            var param = "'" + item.startLocation + "','" + item.finishLocation + "'";
            attachInstructionText(marker1,
                "<h4>Start position  in: " + item.startLocation + "</h4>" +
                '<br/>' +
                '<div class="row"><div class="col-lg-5"><button class="col-lg-10 btn btn-sm btn-info" onclick="details(' + item.loadId + ')">Details </button></div>' +
                '<div class="col-lg-5"><button class=" col-lg-11 btn btn-sm btn-success"  onclick="showDistance(' + param + ')">Show Road</button></div></div>',
                map);

            drawArrow(start, finish);
            start = finish;
            finish = new google.maps.LatLng(item.finishLatitude, item.finishLongitude);
            // var marker2 = new google.maps.Marker({
            //     position: finish,
            //     map: map,
            //     label: "Finish " + (item.position + 1)
            // });
            drawArrow(start, finish);
            start = finish;
            last = finish;
            loadsIds.push(item.loadId);
        }
        drawArrow(last, new google.maps.LatLng(startLocation.startLatitude, startLocation.startLongitude));

        body.append($('<tr>')
            .append($('<td>').append(colorNumber))
            .append($('<td>').append(loadsIds.join(", ")))
            .append($('<td>').append( colourArray[colorNumber % 12])));
    }


}

function drawArrow(start, finish) {
    var lineSymbol = {
        path: google.maps.SymbolPath.FORWARD_CLOSED_ARROW
    };

    // Create the polyline and add the symbol via the 'icons' property.
    var line = new google.maps.Polyline({
        path: [start, finish],
        icons: [{
            icon: lineSymbol,
            offset: '100%'
        }],
        strokeColor: colourArray[colorNumber % 12],
        map: map
    });
}

function details(val) {
    window.location.href = "/loads/" + val + "/private";
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

function attachInstructionText(marker, text, map) {
    google.maps.event.addListener(marker, 'click', function () {
        // Open an info window when the marker is clicked on, containing the text
        // of the step.
        stepDisplay.setContent(text);
        stepDisplay.open(map, marker);
    });

}


function details(val) {
    window.location.href = "/loads/" + val;
}
