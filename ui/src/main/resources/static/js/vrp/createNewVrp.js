let totalPages = 0;
let number = 0;
let currentPage = 0;
let startPosition = "";
let name = "";
let loadsId = [];

function uploadMyPagebleLoadsOnStart() {

    $.ajax({
        url: '/api/loads/current',
        type: 'GET',
        data: {
            pageSize: 5,
            page: 1,
            from: "",
            to: ""
        },
        success: function (response) {
            processResponse(response);
        }
    });
}

function uploadMyPagebleLoads(pageId) {
    let url = '/api/loads/current';
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
            page: pageNumber,
            from: "",
            to: ""
        },
        success: function (response) {
            processResponse(response);
        }
    });
}

function deleteLoad(loadId) {
    let url = '/api/loads/' + loadId;
    $.ajax({
        url: url,
        type: 'DELETE',
        success: function (user) {
            uploadMyPagebleLoadsOnStart();
        }
    });
}

function showMyLoadsOnMap() {
    window.location.href = "/myLoadsMap";
}

function processResponse(response) {
    $("#tableLoads tbody").empty();
    let body = $('#tableLoads').find('tbody');
    for (let i = 0; i < response.loads.content.length; ++i) {
        let load = response.loads.content[i];
        let val = "NO";
        if (load.publicLoad) {
            val = "YES";
        }
        var checkBox = '<input type="checkbox" onclick=' + '"handleClick(this,' + load.loadId + ')"/>';
        if (loadsId.indexOf(load.loadId) !== -1) {
            checkBox = '<input type="checkbox" checked onclick=' + '"handleClick(this,' + load.loadId + ')"/>';
        }
        body.append($('<tr>')
            .append($('<td>').append(load.loadId))
            .append($('<td>').text(load.createDate))
            .append($('<td>').text(load.loadStatus))
            .append($('<td>').append(load.startAddress))
            .append($('<td>').append(load.finishAddress))
            .append($('<td>').append(load.distance / 1000))
            .append($('<td>').append(load.truckType))
            .append($('<td>').append(load.weight))
            .append($('<td>').append(load.price))
            .append($('<td>').append(val))
            .append($('<td>').append(checkBox))
        );
    }
    let pager = response.pager;
    let id = 1;
    for (let i = pager.startPage; i <= pager.endPage; ++i) {
        $("#load" + id).text(i.toString());
        let li = $("#li" + id)[0].classList;
        //todo hidden unavailable pages
        if (li) {
            li.remove('active');
            li.remove('disabled');
        }
        $("#li" + id).addClass('enabled');
        if (i === response.loads.number + 1) {
            number = response.loads.number + 1;
            $("#li" + id).addClass('active');
            currentPage = "#loadPages" + id;
        }
        id++;
    }
    for (; id < 6; id++) {
        $("#loadPages" + id).text(id.toString());
        let li = $("#li" + id)[0].classList;
        if (li) {
            $("#li" + id).addClass('disabled');
            li.remove('active');
        }
    }
    totalPages = response.loads.totalPages;
}


function applayFilters(val) {
    startPosition = $("#startPosition").val();
    if (!startPosition) {
        $('#error').text("Please input start postion");
        return;
    }
    name = $("#roadName").val();
    if (!name || name === "") {
        $('#error').text("Please input road name");
        return;
    }
    if (loadsId.length <= 2) {
        $('#error').text("Please choose loads");
        return;
    }
    $.ajax({
        url: '/api/vrp/',
        type: 'GET',
        data: {
            loads: loadsId,
            startPosition: startPosition,
            name: name
        },
        success: function (response) {
            window.location.href = "/allVrpRoad";
        }
    });
}


function handleClick(cb, loadId) {
    if (cb.checked) {
        loadsId.push(loadId);
    } else {
        var index = loadsId.indexOf(loadId);
        loadsId.splice(index, 1);
    }
}

function initMap() {

    var input = /** @type {!HTMLInputElement} */(
        document.getElementById('startPosition'));
    var autocomplete = new google.maps.places.Autocomplete(input);
    var infowindow = new google.maps.InfoWindow();
    autocomplete.addListener('place_changed', function () {
        infowindow.close();
        var address = '';

        address = [
            place.address_components[0],
            place.address_components[1],
            place.address_components[2]
        ].join(' ');

        infowindow.setContent('<div><strong>' + place.name + '</strong><br/></div>' + address);
        // infowindow.open(map, marker);
    });
}
