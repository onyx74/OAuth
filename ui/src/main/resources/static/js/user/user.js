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