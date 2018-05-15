var currentTruckId;
var ownerName;

function loadTrucksDetails() {
    let truckId = window.location.href.split("/")[4];
    let url = '/api/trucks/' + truckId;
    $.ajax({
        url: url,
        type: 'GET',
        success: function (truck) {
            $('#currentPossition').text(truck.currentPossition);
            $('#carModel').text(truck.carModel);
            $('#driverName').text(truck.driverName);
            $('#truckType').val(truck.truckType);
            $('#carrying').text(truck.carrying);
            $('#pricePerMile').text(truck.pricePerMile);
            $('#publicTruck').prop('checked', truck.publicTruck);
            currentTruckId = truck.truckId;
            ownerName = truck.ownername;
            //todo check that is owner

        }
    });
}

function deleteTruck() {
    let url = '/api/trucks/' + currentTruckId;
    $.ajax({
        url: url,
        type: 'DELETE',
        success: function () {
            window.location.href = "/";
        }
    });
}

function editTruck() {
    window.location.href = "/truck/edit/" + currentTruckId;
}

function back() {
    window.location.href = "/allTrucks";
}
