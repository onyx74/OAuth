let totalPages = 0;
let number = 0;


let friends;

function getPagebleUsers() {
    $.ajax({
        url: '/api/user/',
        type: 'GET',
        success: function (response) {
            let body = $('#tableUsers').find('tbody');
            for (let i = 0; i < response.users.content.length; ++i) {
                let user = response.users.content[i];
                friends = response.friends;
                var text;
                if (response.friends[i] != -1) {
                    text = '<button id="u' + user.id + '"   onclick=' + '"changeFriend(' + user.id + ')"' + 'class="btn btn-danger">' + 'Remove' +
                        '</button>';
                } else {
                    text = '<button id="u' + user.id + '"  onclick=' + '"changeFriend(' + user.id + ')"' + 'class="btn btn-success">' + 'Add to friends' +
                        '</button>';
                }
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
            friends = response.friends;
            for (let i = 0; i < response.users.content.length; ++i) {
                let user = response.users.content[i];
                var text;
                if (response.friends[i] != -1) {
                    text = '<button id="u' + user.id + '"   onclick=' + '"changeFriend(' + user.id + ')"' + 'class="btn btn-danger">' + 'Remove' +
                        '</button>';
                } else {
                    text = '<button id="u' + user.id + '"  onclick=' + '"changeFriend(' + user.id + ')"' + 'class="btn btn-success">' + 'Add to friends' +
                        '</button>';
                }
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

function changeFriend(friendId) {
    let url = '/api/user/' + userId + '/friends/' + friendId;
    let btn = $("#u" + friendId);
    var ii = 0;
    for (var i = 0; i < friends.length; ++i) {
        if (friends[i] === friendId) {
            $.ajax({
                url: url,
                type: 'DELETE',
                success: function (user) {
                    btn[0].classList.remove('btn-danger');
                    btn.addClass('btn-success');
                    btn.text('Add to friends');
                }
            });
            friends[i] = -1;
            return;
        } else if (friends[i] === -1) {
            ii = i;
        }
    }
    friends[ii] = friendId;
    $.ajax({
        url: url,
        type: 'POST',
        success: function (user) {
            btn[0].classList.remove('btn-success');
            btn.addClass('btn-danger');
            btn.text('Remove');
        }
    });
}

let isFriend;
let userProfileId;

function uploadUserInformation() {
    let userId = window.location.href.split("/")[4];
    let url = '/api/user/' + userId;
    $.ajax({
        url: url,
        type: 'GET',
        success: function (user) {
            $('#userPic').attr('src', '/api/user/photo/' + user.userDto.pathToPhoto);
            $('#userName').text(user.userDto.username);
            $('#firstName').text(user.userDto.firstName);
            $('#surname').text(user.userDto.surname);
            $('#email').text(user.userDto.email);
            $('#phoneNumber').text(user.userDto.phoneNumber);
            $('#birthDate').text(user.userDto.birthDate);
            if (user.isFriend) {
                $('#isFriend').addClass('btn-danger');
                $('#isFriend').text('Remove from friends');
            } else {
                $('#isFriend').addClass('btn-success');
                $('#isFriend').text('Add to friends');
            }
            isFriend = user.friend;
            let btn = $('#isFriend');
            if (isFriend) {
                btn.addClass('btn-danger');
                btn.text('Remove from friends');
            } else {
                btn.addClass('btn-success');
                btn.text('Add to friends');
            }
            userProfileId = user.userDto.id;
        }
    });
}

function changeUserFriend() {
    let btn = $('#isFriend');
    let url = '/api/user/' + userId + '/friends/' + userProfileId;
    if (isFriend) {
        $.ajax({
            url: url,
            type: 'DELETE',
            success: function (user) {
            }
        });
        btn[0].classList.remove('btn-danger');
        btn.addClass('btn-success');
        btn.text('Add to friends');
    } else {
        $.ajax({
            url: url,
            type: 'POST',
            success: function (user) {
            }
        });
        btn[0].classList.remove('btn-success');
        btn.addClass('btn-danger');
        btn.text('Remove from friends');
    }
    isFriend = !isFriend;
}

