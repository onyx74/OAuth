let totalPages = 0;
let number = 0;
let currentPage = 0;

function uploadUserToInformation() {
    let userId = window.location.href.split("/")[4];
    let url = '/api/user/' + userId;
    if (userId) {
        $.ajax({
            url: url,
            type: 'GET',
            success: function (user) {
                $('#sendTo').val(user.userDto.username);
            }
        });
    }
}

function uploadMessageToInformation() {
    let messageId = window.location.href.split("/")[4];
    let url = '/api/user/current/messages/' + messageId;
    if (userId) {
        $.ajax({
            url: url,
            type: 'GET',
            success: function (message) {
                $('#sendTo').text(message.sendTo);
                $('#subject').text(message.subject);
                $('#text').text(message.text);
            }
        });
    }
}

function getPagebleSentMessages() {
    let url = '/api/user/current/messagesSent';
    $.ajax({
        url: url,
        type: 'GET',
        data: {
            pageSize: 5,
            page: 1
            // usernameLike: searchName
        },
        success: function (response) {
            processResponseSent(response, '/users/');
        }
    });
}

function readMessage() {
    let messageId = window.location.href.split("/")[4];
    let url = '/api/user/current/message/' + messageId;
    $.ajax({
        url: url,
        type: 'POST',
        success: function (message) {

        }
    });
}

function getPagebleMessages() {
    let url = '/api/user/current/messages';
    $.ajax({
        url: url,
        type: 'GET',
        data: {
            pageSize: 5,
            page: 1
            // usernameLike: searchName
        },
        success: function (response) {
            processResponseSent(response, '/inbox/');
        }
    });
}

function loadNewMessages(pageId) {
    let url = '/api/user/' + userId + '/messages/';
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
            processResponseSent(response, '/inbox/');
        }
    });
}

function loadNewSentMessages(pageId) {
    let url = '/api/user/' + userId + '/messagesSent/';
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
            processResponseSent(response, '/users/');
        }
    });
}

function processResponseSent(response, string) {
    $("#tableMessages tbody").empty();
    let body = $('#tableMessages').find('tbody');
    for (let i = 0; i < response.messages.content.length; ++i) {
        let message = response.messages.content[i];
        // var text = '<button onclick=' + '"showMessageDetails(' + message.id + ')"' + 'class="btn btn-success">' + 'Show Details' +
        //     '</button>';
        body.append($('<tr>')
            // .append($('<td>').append('<a onclick=' + '"loadUserProfile(' + user.id + ')">' + user.username + '</a>'))
                .append($('<td>').append('<a href=' + '"' + string + message.id + '">' + message.sendTo + '</a>'))
                .append($('<td>').text(message.subject))
                .append($('<td>').text(message.createdAt))
                .append($('<td>').append('<a  class="btn btn-success" href=' + '"' + string + message.id + '">' + 'Show message'+ '</a>'))
        );

    }
    let pager = response.pager;
    let id = 1;
    for (let i = pager.startPage; i <= pager.endPage; ++i) {
        $("#messagesPages" + id).text(i.toString());
        let li = $("#li" + id)[0].classList;
        //todo hidden unavailable pages
        if (li) {
            li.remove('active');
        }
        if (i === response.messages.number + 1) {
            number = response.messages.number + 1;
            $("#li" + id).addClass('active');
            currentPage = "#messagesPages" + id;
        }
        id++;
    }
    for (; id < 6; id++) {
        $("#messagesPages" + id).text(id.toString());
        let li = $("#li" + id)[0].classList;
        if (li) {
            $("#li" + id).addClass('disabled');
            li.remove('active');
        }
    }
    totalPages = response.messages.totalPages;
}

//todo make all success funcion in pagination the same;

$("#sendMessageForm").submit(function (event) {
    event.preventDefault();
    let formData = $('form').serialize();
    let url = "/api/user/" + userId + "/messages"
    $.ajax({
        url: url,
        type: 'POST',
        data: formData,
        success: function () {
            window.location.href = "/";
        }
    });

});

function showMessageDetails(messageId) {
    let url = "/message/" + messageId;
    window.location.href = url;

}


