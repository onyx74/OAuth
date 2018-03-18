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


$("#sendMessageForm").submit(function (event) {
    event.preventDefault();
    let formData = $('form').serialize();
    let url = "/api/user/"+userId+"/messages"
    $.ajax({
        url: url,
        type: 'POST',
        data: formData,
        success: function () {
            window.location.href = "/";
        }
    });

});