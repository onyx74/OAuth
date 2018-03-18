let totalPages = 0;
let number = 0;
let currentPage = 0;

function getPagebleFriends() {
    let url = '/api/user/current/friends/';
    $.ajax({
        url: url,
        type: 'GET',
        data: {
            pageSize: 5,
            page: 1
            // usernameLike: searchName
        },
        success: function (response) {
            $("#tableFriends tbody").empty();
            let body = $('#tableFriends').find('tbody');
            for (let i = 0; i < response.users.length; ++i) {
                let user = response.users[i];
                var text = '<button id="u' + user.id + '"  onclick=' + '"removeFromFriends(' + user.id + ')"' + 'class="btn btn-danger">' + 'Remove' +
                    '</button>';

                body.append($('<tr>')
                    // .append($('<td>').append('<a onclick=' + '"loadUserProfile(' + user.id + ')">' + user.username + '</a>'))
                        .append($('<td>').append('<a href=' + '"/users/' + user.id + '">' + user.username + '</a>'))
                        .append($('<td>').text(user.firstName))
                        .append($('<td>').text(user.surname))
                        .append($('<td>').text(user.email))
                        .append($('<td>').text(user.phoneNumber === null ? '-' : user.phoneNumber))
                        .append($('<td>').text(user.createdAt.toString()))
                        .append($('<td>').append(text))
                );

            }
            let pager = response.pager;
            let id = 1;
            for (let i = pager.startPage; i <= pager.endPage; ++i) {
                $("#friendsPages" + id).text(i.toString());
                let li = $("#li" + id).classList;
                //todo hidden unavailable pages
                if (li) {
                    li.remove('active');
                }
                if (i === response.number + 1) {
                    number = response.number + 1;
                    $("#li" + id).addClass('active');
                    currentPage = "#friendsPages" + id;
                }
                id++;
            }

            totalPages = response.users.totalPages;
        }
    });
}


function loadNewFriends(pageId) {
    let url = '/api/user/' + userId + '/friends/';
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
            $("#tableFriends tbody").empty();
            let body = $('#tableFriends').find('tbody');
            for (let i = 0; i < response.users.length; ++i) {
                let user = response.users[i];
                var text = '<button id="u' + user.id + '"  onclick=' + '"removeFromFriends(' + user.id + ')"' + 'class="btn btn-danger">' + 'Remove' +
                    '</button>';

                body.append($('<tr>')
                    .append($('<td>').append('<a href=' + '"/users/' + user.id + '">' + user.username + '</a>'))
                    .append($('<td>').text(user.firstName))
                    .append($('<td>').text(user.surname))
                    .append($('<td>').text(user.email))
                    .append($('<td>').text(user.phoneNumber === null ? '-' : user.phoneNumber))
                    .append($('<td>').text(user.createdAt.toString()))
                    .append($('<td>').append(text))
                );
            }
            let pager = response.pager;
            let id = 1;
            for (let i = pager.startPage; i <= pager.endPage; ++i) {
                $("#friendsPages" + id).text(i.toString());
                let li = $("#li" + id)[0].classList;
                if (li) {
                    li.remove('disabled')
                    li.remove('active');
                }
                if (i === response.number + 1) {
                    currentPage = "#friendsPages" + id;
                    number = response.number + 1;
                    $("#li" + id).addClass('active');
                }
                id++;
            }
            for (; id < 6; id++) {
                $("#friendsPages" + id).text(id.toString());
                let li = $("#li" + id)[0].classList;
                if (li) {
                    $("#li" + id).addClass('disabled');
                    li.remove('active');
                }
            }
        }
    });
}

function removeFromFriends(friendId) {
    let url = '/api/user/' + userId + '/friends/' + friendId;
    $.ajax({
        url: url,
        type: 'DELETE',
        success: function (user) {
            loadNewFriends(currentPage);
        }
    });

}