let totalPages = 0;
let number = 0;
let currentPage = 0;
let subject = "";
let sentTo = "";

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
            page: 1,
            subject: subject,
            to: sentTo
        },
        success: function (response) {
            processResponseSent(response, '/inbox/', false);
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
            page: 1,
            subject: subject,
            from: sentTo
            // usernameLike: searchName
        },
        success: function (response) {
            processResponseSent(response, '/inbox/', true);
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
            page: pageNumber,
            subject: subject,
            from: sentTo
        },
        success: function (response) {
            processResponseSent(response, '/inbox/', true);
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
            page: pageNumber,
            subject: subject,
            to: sentTo
        },
        success: function (response) {
            processResponseSent(response, '/inbox/', false);
        }
    });
}

function processResponseSent(response, string, read) {
    $("#tableMessages tbody").empty();
    let body = $('#tableMessages').find('tbody');
    for (let i = 0; i < response.messages.content.length; ++i) {
        let message = response.messages.content[i];
        if (read) {
            let val = "NO";
            if (message.checked) {
                val = "YES";
            }
            body.append($('<tr>')
                .append($('<td>').append('<a href=' + '"/users/' + message.ownerId + '">' + response.userNames[message.ownerId] + '</a>'))
                .append($('<td>').text(message.subject))
                .append($('<td>').text(message.createdAt))
                .append($('<td>').append(val))
                .append($('<td>').append('<a  class="btn btn-success" href=' + '"' + string + message.id + '">' + 'Show message' + '</a>'))
            );
        } else {
            body.append($('<tr>')
                .append($('<td>').append('<a href=' + '"/users/' + response.userIds[message.sendTo] + '">' + message.sendTo + '</a>'))
                .append($('<td>').text(message.subject))
                .append($('<td>').text(message.createdAt))
                .append($('<td>').append('<a  class="btn btn-success" href=' + '"' + string + message.id + '">' + 'Show message' + '</a>'))
            );
        }
    }
    let pager = response.pager;
    let id = 1;
    for (let i = pager.startPage; i <= pager.endPage; ++i) {
        $("#messagesPages" + id).text(i.toString());
        let li = $("#li" + id)[0].classList;
        //todo hidden unavailable pages
        if (li) {
            li.remove('active');
            li.remove('disabled');
        }
        $("#li" + id).addClass('enabled');
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


