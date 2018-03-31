var currentLoadId;
var ownerName;

function loadLoadsDetails() {
    let loadId = window.location.href.split("/")[5];
    let url = '/api/loads/' + loadId;
    $.ajax({
        url: url,
        type: 'GET',
        success: function (load) {
            $('#username').val(load.username);
            $('#startAddress').val(load.startAddress);
            $('#finishAddress').val(load.finishAddress);
            $('#truckType').val(load.truckType);
            $('#loadStatus').val(load.loadStatus);
            $('#weight').val(load.weight);
            $('#price').val(load.price);
            $('#publicLoad').prop('checked', load.publicLoad);
            currentLoadId = load.loadId;
            ownerName = load.username;
            //todo check that is owner
        }
    });
}

function back() {
    window.location.href = "/loads/" + currentLoadId;
}

$("#editLoad").submit(function (event) {
    event.preventDefault();
    let formData = $('form').serialize();
    let url = "/api/loads/" + currentLoadId;
    $.ajax({
        url: url,
        type: 'POST',
        data: formData,
        success: function () {

        }
    });
    window.location.href = "/myLoads";
});