/* global app */

app.controller('LoginController', function($scope, LoginService, $window, $state, $rootScope, bsLoadingOverlayService) {
    $scope.showError = false;
    $scope.showSuccess = false;
    $scope.user={'username':'','password':''};
    $scope.appName = 'Court Vision';
    if($window.sessionStorage.getItem('session-id')) {
        $state.go('main.home');
    }
    $scope.login = function() {
        bsLoadingOverlayService.start();
        LoginService.Login($scope.user.username, $scope.user.password).then(function(response) {
            if(!response.isErrorPresent) {
                $rootScope.sessionId = response.returnData.sessionId;
                $rootScope.userId = response.returnData.userData[0].userId;
                $window.sessionStorage.setItem('session-id', response.returnData.sessionId);
                $window.sessionStorage.setItem('user-id', response.returnData.userData[0].userId);
                $window.sessionStorage.setItem('username', $scope.user.username);
                $state.go('main.home');
            }
            bsLoadingOverlayService.stop();
        });
    };
});