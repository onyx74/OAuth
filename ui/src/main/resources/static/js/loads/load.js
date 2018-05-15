var currentLoadId;
var ownerName;

function loadLoadsDetails() {
    let loadId = window.location.href.split("/")[4];
    let url = '/api/loads/' + loadId;
    $.ajax({
        url: url,
        type: 'GET',
        success: function (load) {
            $('#startAddress').text(load.startAddress);
            $('#finishAddress').text(load.finishAddress);
            $('#truckType').val(load.truckType);
            $('#loadStatus').text(load.loadStatus);
            $('#weight').text(load.weight);
            $('#price').text(load.price);
            $('#publicLoad').prop('checked', load.publicLoad);
            currentLoadId = load.loadId;
            ownerName = load.username;
            //todo check that is owner
        }
    });
}

function deleteLoad() {
    let url = '/api/loads/' + currentLoadId;
    $.ajax({
        url: url,
        type: 'DELETE',
        success: function () {
            window.location.href = "/";
        }
    });
}

function editLoad() {
    window.location.href = "/load/edit/" + currentLoadId;
}

function back() {
    window.location.href = "/allLoads";
}