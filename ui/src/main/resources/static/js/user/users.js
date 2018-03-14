let totalPages = 0;
let number = 0;

function getPagebleUsers() {
    $.ajax({
        url: '/api/user/',
        type: 'GET',
        success: function (response) {
            let body = $('#tableUsers').find('tbody');
            for (let i = 0; i < response.users.content.length; ++i) {
                let user = response.users.content[i];
                body.append($('<tr>')
                    .append($('<td>').text(user.username))
                    .append($('<td>').text(user.firstName))
                    .append($('<td>').text(user.surname))
                    .append($('<td>').text(user.email))
                    .append($('<td>').text(user.phoneNumber === null ? '-' : user.phoneNumber))
                    .append($('<td>').text(user.createdAt.toString()))
                    .append($('<td>').text(false))
                );
            }
            let pager = response.pager;
            let id = 1;
            for (let i = pager.startPage; i <= pager.endPage; ++i) {
                $("#userPages" + id).text(i.toString());
                let li = $("#li" + id).classList;

                if (li) {
                    li.remove('active');
                }
                if (i === response.users.number + 1) {
                    number = response.users.number + 1;
                    $("#li" + id).addClass('active');
                }
                id++;
            }

            totalPages = response.users.totalPages;
        }
    });
}

function loadNewUsers(pageId) {
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
    alert(pageNumber);
    $.ajax({
        url: '/api/user',
        type: 'GET',
        data: {
            pageSize: pageSize,
            page: pageNumber
        },
        success: function (response) {
            $("#tableUsers tbody").empty();
            let body = $('#tableUsers').find('tbody');
            for (let i = 0; i < response.users.content.length; ++i) {
                let user = response.users.content[i];
                body.append($('<tr>')
                    .append($('<td>').text(user.username))
                    .append($('<td>').text(user.firstName))
                    .append($('<td>').text(user.surname))
                    .append($('<td>').text(user.email))
                    .append($('<td>').text(user.phoneNumber === null ? '-' : user.phoneNumber))
                    .append($('<td>').text(user.createdAt.toString()))
                    .append($('<td>').text(false))
                );
            }
            let pager = response.pager;
            let id = 1;
            for (let i = pager.startPage; i <= pager.endPage; ++i) {
                $("#userPages" + id).text(i.toString());
                let li = $("#li" + id)[0].classList;
                if (li) {
                    li.remove('active');
                }
                if (i === response.users.number + 1) {
                    number = response.users.number + 1;
                    $("#li" + id).addClass('active');
                }
                id++;
            }
        }
    });
}