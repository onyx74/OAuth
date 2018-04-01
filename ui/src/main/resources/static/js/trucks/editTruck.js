var currentTruckId;
var ownerName;

function loadTrucksDetails() {
    let truckId = window.location.href.split("/")[5];
    let url = '/api/trucks/' + truckId;
    $.ajax({
        url: url,
        type: 'GET',
        success: function (truck) {
            $('#ownername').val(truck.ownername);
            $('#currentPossition').val(truck.currentPossition);
            $('#carModel').val(truck.carModel);
            $('#driverName').val(truck.driverName);
            $('#truckType').val(truck.truckType);
            $('#carrying').val(truck.carrying);
            $('#pricePerMile').val(truck.pricePerMile);
            $('#publicTruck').prop('checked', truck.publicTruck);
            currentTruckId = truck.truckId;
            ownerName = truck.ownername;
            //todo check that is owner

        }
    });
}
//todo add avtocomplite
function back() {
    window.location.href = "/trucks/" + currentTruckId;
}

$("#truckForm").submit(function (event) {
    event.preventDefault();
    let formData = $('form').serialize();
    let url = "/api/trucks/" + currentTruckId;
    $.ajax({
        url: url,
        type: 'POST',
        data: formData,
        success: function () {

        }
    });
    window.location.href = "/myTrucks";
});