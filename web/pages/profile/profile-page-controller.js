/* global google, app */

app.controller('ProfilePageController', function ($scope, $window) {
    $scope.username = $window.sessionStorage.getItem('username');
    $scope.isCoach = $window.sessionStorage.getItem('isCoach') === 'true';
});