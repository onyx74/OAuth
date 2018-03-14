var userId;
function uploadBaseUserInformation() {
    $.ajax({
        url: '/api/user/current',
        type: 'GET',
        success: function (currentUser) {
            $('#header-photo').attr('src', '/api/user/photo/' + currentUser.pathToPhoto);
            $('#header-dropdown-photo').attr('src', '/api/user/photo/' + currentUser.pathToPhoto);
            $('#left-photo').attr('src', '/api/user/photo/' + currentUser.pathToPhoto);
            $('#info-user-name').text(currentUser.firstName + ' ' + currentUser.surname);
            $('#header-user-info').text(currentUser.firstName + ' ' + currentUser.surname);
            $('#headerCreatedAt').text("Member since " + currentUser.createdAt);
            $('#header-span-info').text(currentUser.firstName + ' ' + currentUser.surname);
            userId=currentUser.id;
        }
    });
}
function uploadProfileInformation() {
    $.ajax({
        url: '/api/user/current',
        type: 'GET',
        success: function (currentUser) {
            $('#userPic').attr('src', '/api/user/photo/' + currentUser.pathToPhoto);
            $('#userName').text(currentUser.username);
            $('#firstName').text(currentUser.firstName);
            $('#surname').text(currentUser.surname);
            $('#email').text(currentUser.email);
            $('#phoneNumber').text(currentUser.phoneNumber);
            $('#birthDate').text(currentUser.birthDate);
        }
    });
}
function uploadInformationForEditProfile() {
    $.ajax({
        url: '/api/user/current',
        type: 'GET',
        success: function (currentUser) {
            $('#userPicEdit').attr('src', '/api/user/photo/' + currentUser.pathToPhoto);
            $('#firstNameEdit').val(currentUser.firstName);
            $('#surnameEdit').val(currentUser.surname);
            $('#emailEdit').val(currentUser.email);
            $('#phoneNumberEdit').val(currentUser.phoneNumber);
            $('#birthDateEdit').val(currentUser.birthDate);
        }
    });
}

function deleteCurrentPhoto(){
    var photoUrl = "/api/user/" + userId + "/photo/default";
    $.ajax({
        url: photoUrl,
        type: "POST",
        success: function (res) {
            uploadBaseUserInformation();
            uploadInformationForEditProfile();

        }
    });
}
function editProfile() {

    var formData = $('#form').serialize();
    var photoUrl = "/api/user/" + userId + "/photo";
    $.ajax({
        url: photoUrl,
        type: "POST",
        data: new FormData($('#upload-file-form')[0]),
        enctype: 'multipart/form-data',
        processData: false,
        contentType: false,
        cache: false,
        success: function (res) {
        }
    });
    var url = "/api/user/" + userId;
    $.ajax({
        url: url,
        type: 'POST',
        data: formData,
        success: function (res) {
            window.location.href = '/';
        }

    });
}

function changePassword() {
    var pass=$("#password").val();
    if ($("#password").val().length >= 8) {
        if (ucase.test($("#password").val())) {
            if (lcase.test($("#password").val())) {
                if (num.test($("#password").val())) {
                    if ($("#password").val() == $("#confirmPassword").val()) {
                        var formData = $('#passwordForm').serialize();
                        var url="/api/user/"+userId+"/password";
                        $.ajax({
                            url: url,
                            type: 'POST',
                            data: formData,
                            success: function (res) {
                                window.location.href='/';
                            }

                        });
                    }
                }
            }
        }
    }
}