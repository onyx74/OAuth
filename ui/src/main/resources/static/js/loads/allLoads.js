let totalPages = 0;
let number = 0;
let currentPage = 0;
let from = "";
let to = "";

function uploadMyPagebleLoadsOnStart() {

    $.ajax({
        url: '/api/loads/',
        type: 'GET',
        data: {
            pageSize: 5,
            page: 1,
            from: from,
            to: to
        },
        success: function (response) {
            processResponse(response);
        }
    });
}

function uploadMyPagebleLoads(pageId) {
    let url = '/api/loads/';
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
            from: from,
            to: to
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
    window.location.href = "/allLoadsMap";
}

function processResponse(response) {
    $("#tableLoads tbody").empty();
    let body = $('#tableLoads').find('tbody');
    for (let i = 0; i < response.loads.content.length; ++i) {
        let load = response.loads.content[i];

        body.append($('<tr>')
            .append($('<td>').append(load.loadId))
            .append($('<td>').append(load.username))
            .append($('<td>').text(load.createDate))
            .append($('<td>').text(load.loadStatus))
            .append($('<td>').append(load.startAddress))
            .append($('<td>').append(load.finishAddress))
            .append($('<td>').append(load.distance / 1000))
            .append($('<td>').append(load.truckType))
            .append($('<td>').append(load.weight))
            .append($('<td>').append(load.price))
            .append($('<td>').append('<a class="btn btn-success glyphicon glyphicon-search" href=' + '"/loads/' + load.loadId + '/private"/>'))
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
    var stateObj = {foo: "bar"};
    from = $("#from").val();
    to = $("#to").val();
    uploadMyPagebleLoadsOnStart()
}

function resetFilters(val) {
    from = "";
    to = "";
    $("#from").val("");
    $("#to").val("");
    uploadMyPagebleLoadsOnStart()
}