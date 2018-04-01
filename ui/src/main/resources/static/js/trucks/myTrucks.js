let totalPages = 0;
let number = 0;
let currentPage = 0;

function uploadMyPagebleTrucksOnStart() {

    let url = '/api/trucks/current';
    $.ajax({
        url: url,
        type: 'GET',
        data: {
            pageSize: 5,
            page: 1
        },
        success: function (response) {
            processResponse(response);
        }
    });
}

function uploadMyPagebleTrucks(pageId) {
    let url = '/api/trucks/current';
    const pageSize = $('#pageSizeSelect').val();
    let pageNumber;
    if (pageId === "next") {
        pageNumber = number + 1;
        if (pageNumber === totalPages + 1) {
            return;
        }
    }
    else if (pageId === "previous") {
        pageNumber = number - 1;
        if (pageNumber === 0) {
            return;
        }
    } else {
        pageNumber = $(pageId).text();
    }

    $.ajax({
        url: url,
        type: 'GET',
        data: {
            pageSize: pageSize,
            page: pageNumber
        },
        success: function (response) {
            processResponse(response);
        }
    });
}

function deleteTruck(truckId) {
    let url = '/api/trucks/' + truckId;
    $.ajax({
        url: url,
        type: 'DELETE',
        success: function (user) {
            uploadMyPagebleTrucksOnStart();
        }
    });
}

function shoowTrucksOnMap() {

}

function processResponse(response) {
    $("#tableTrucks tbody").empty();
    let body = $('#tableTrucks').find('tbody');
    for (let i = 0; i < response.trucks.content.length; ++i) {
        let truck = response.trucks.content[i];
        let val = "NO";
        if (truck.publicTruck) {
            val = "YES";
        }
        body.append($('<tr>')
            .append($('<td>').append(truck.truckId))
            .append($('<td>').append(truck.ownername))
            .append($('<td>').append(truck.carModel))
            .append($('<td>').append(truck.truckType))
            .append($('<td>').text(truck.createDate))
            .append($('<td>').append(truck.currentPossition))
            .append($('<td>').append(truck.driverName))
            .append($('<td>').append(truck.carrying))
            .append($('<td>').append(truck.pricePerMile))
            .append($('<td>').append(val))
            .append($('<td>').append('<a class="btn btn-success glyphicon glyphicon-search" href=' + '"/trucks/' + truck.truckId + '"/>'))
            .append($('<td>').append('<a class="btn btn-danger glyphicon glyphicon-trash" onclick=' + '"deleteTruck(' + truck.truckId + ')"/>'))
        );
    }
    let pager = response.pager;
    let id = 1;
    for (let i = pager.startPage; i <= pager.endPage; ++i) {
        $("#truck" + id).text(i.toString());
        let li = $("#li" + id)[0].classList;
        //todo hidden unavailable pages
        if (li) {
            li.remove('active');
            li.remove('disabled');
        }
        $("#li" + id).addClass('enabled');
        if (i === response.trucks.number + 1) {
            number = response.trucks.number + 1;
            $("#li" + id).addClass('active');
            currentPage = "#truckPages" + id;
        }
        id++;
    }
    for (; id < 6; id++) {
        $("#truckPages" + id).text(id.toString());
        let li = $("#li" + id)[0].classList;
        if (li) {
            $("#li" + id).addClass('disabled');
            li.remove('active');
        }
    }
    totalPages = response.trucks.totalPages;


}