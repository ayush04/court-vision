/* global app */

app.controller('MenuController', function(UserService, PlayerService, LoginService, Utils,
    $rootScope, $scope, $window, $state) {
    $scope.username = $window.sessionStorage.getItem('username');
    var userId = $window.sessionStorage.getItem('user-id');
    /* Check for user role */
    if (!$window.sessionStorage.getItem('isCoach')) {
        UserService.IsUserCoach(userId).then(function (response) {
            if (!response.isErrorPresent) {
                $scope.isCoach = response.returnData.genericData[0].value;
                $window.sessionStorage.setItem('isCoach', $scope.isCoach);
            }
        });
    } else {
        $scope.isCoach = $window.sessionStorage.getItem('isCoach');
    }
    
    if ($rootScope.fitbitClientId === '') {
        PlayerService.GetPlayerProfile($window.sessionStorage.getItem('user-id')).then(function (response) {
            if (!response.isErrorPresent) {
                var profileData = response.returnData.profileData[0];
                $rootScope.fitbitClientId = profileData.fitbitClientId !== null ? profileData.fitbitClientId : '';
            }
        });
    }
    
    $rootScope.$watch(function () {
        return $rootScope.fitbitClientId;
    }, function (value) {
        if (value !== '') {
            fitbitUrl = 'https://www.fitbit.com/oauth2/authorize?response_type=token&client_id=' + value + '&scope=activity%20nutrition%20heartrate%20location%20nutrition%20profile%20settings%20sleep%20social%20weight';
        }
    });
    $scope.logout = function () {
        LoginService.Logout().then(function (response) {
            if (!response.isErrorPresent) {
                Utils.ClearSession();
                $state.go('login');
            }
        });
    };
    
    $scope.launchFitbitLogin = function () {
        if ($rootScope.fitbitClientId !== '') {
            $window.open(fitbitUrl, '_blank');
        }
    };
});