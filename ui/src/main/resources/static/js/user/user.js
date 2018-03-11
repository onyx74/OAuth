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
        }
    });
}