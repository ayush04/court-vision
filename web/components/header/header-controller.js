/* global app */

app.controller('HeaderController', function($scope, LoginService, PlayerService, NotificationService, 
    $window, $state, $rootScope, UserService, Utils) {
    $scope.username = $window.sessionStorage.getItem('username');
    if(!$rootScope.userId) {
        $rootScope.userId = $window.sessionStorage.getItem('user-id');
    }
    $rootScope.fitbitClientId = '';
    var fitbitUrl = '';
    if(!angular.isDefined($rootScope.isCoach)) {
        UserService.IsUserCoach($rootScope.userId).then(function(response) {
            $rootScope.isCoach = (response.returnData.genericData[0].value === 'true');
        });
    }
    
    if($rootScope.fitbitClientId === '') {
        PlayerService.GetPlayerProfile($window.sessionStorage.getItem('user-id')).then(function (response) {
            if (!response.isErrorPresent) {
                var profileData = response.returnData.profileData[0];
                $rootScope.fitbitClientId = profileData.fitbitClientId !== null? profileData.fitbitClientId: '';
            }
        });
    }
    
    /* Notifications */
    NotificationService.GetAllNotificationsForUser($window.sessionStorage.getItem('user-id')).then(function(response) {
        if(!response.isErrorPresent) {
            $scope.notifications = response.returnData.notificationData;
        };
    });
    
    $scope.markNotificationAsRead = function(notificationId) {
        NotificationService.markNotificationAsRead(notificationId).then(function() {
            NotificationService.GetAllNotificationsForUser($window.sessionStorage.getItem('user-id')).then(function (response) {
                if (!response.isErrorPresent) {
                    $scope.notifications = response.returnData.notificationData;
                };
            });
        });
    };
    
    $rootScope.$watch(function() {
        return $rootScope.fitbitClientId;
    }, function(value) {
        if(value !== '') {
            fitbitUrl = 'https://www.fitbit.com/oauth2/authorize?response_type=token&client_id=' + value + '&scope=activity%20nutrition%20heartrate%20location%20nutrition%20profile%20settings%20sleep%20social%20weight';
        }
    });
    $scope.logout = function() {
        LoginService.Logout().then(function(response) {
            if(!response.isErrorPresent) {
                Utils.ClearSession(); 
                $state.go('login');
            }
        });
    };
    
    $scope.status = {
        isopen: false
    };
    
    $scope.launchFitbitLogin = function() {
        if($rootScope.fitbitClientId !== '') {
            $window.open(fitbitUrl, '_blank');
        }
    };
});