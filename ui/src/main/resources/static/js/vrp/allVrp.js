let totalPages = 0;
let number = 0;
let currentPage = 0;
let name = "";
let startLocation = "";

function uploadMyVrp() {
    $.ajax({
        url: '/api/vrp/current',
        type: 'GET',
        data: {
            pageSize: 5,
            page: 1,
            name: name,
            startLocation: startLocation
        },
        success: function (response) {
            processResponse(response);
        }
    });
}

function uploadMyPagebleVrp(pageId) {
    let url = '/api/vrp/current';
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
            name: name,
            startLocation: startLocation
        },
        success: function (response) {
            processResponse(response);
        }
    });
}


function processResponse(response) {
    $("#tableLoads tbody").empty();
    let body = $('#tableLoads').find('tbody');
    for (let i = 0; i < response.vrps.content.length; ++i) {
        let vrp = response.vrps.content[i];

        body.append($('<tr>')
            .append($('<td>').text(vrp.createDate))
            .append($('<td>').text(vrp.startLocation))
            .append($('<td>').append(vrp.name))
            .append($('<td>').append('<a class="btn btn-success glyphicon glyphicon-search" href=' + '"/vrpMap/' + vrp.vrpId + '"/>'))
        );
    }
    let pager = response.pager;
    let id = 1;
    for (let i = pager.startPage; i <= pager.endPage; ++i) {
        $("#load" + id).text(i.toString());
        let li = $("#li" + id)[0].classList;
        if (li) {
            li.remove('active');
            li.remove('disabled');
        }
        $("#li" + id).addClass('enabled');
        if (i === response.vrps.number + 1) {
            number = response.vrps.number + 1;
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
    totalPages = response.vrps.totalPages;
}


function applayFilters(val) {
    startLocation = $("#startLocation").val();
    name = $("#name").val();
    uploadMyVrp()
}

function resetFilters(val) {
    startLocation = "";
    name = "";
    $("#startLocation").val("");
    $("#name").val("");
    uploadMyVrp()
}