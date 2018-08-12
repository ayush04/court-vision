/* global app */

app.controller('ProfileController', function(PlayerService, UserService, NotificationService, 
    $window, $scope, $rootScope) {
    var userId = $window.sessionStorage.getItem('user-id');
    $rootScope.profile = {};
    $rootScope.profile['name'] = $window.sessionStorage.getItem('username');
    
    /* Check for user role */
    if(!$window.sessionStorage.getItem('isCoach')) {
        UserService.IsUserCoach(userId).then(function(response) {
            if(!response.isErrorPresent) {
                $scope.isCoach = response.returnData.genericData[0].value;
                $window.sessionStorage.setItem('isCoach', $scope.isCoach);
            }
        });
    }
    else {
        $scope.isCoach = $window.sessionStorage.getItem('isCoach');
    }
    PlayerService.GetPlayerProfile(userId).then(function(response) {
        if(!response.isErrorPresent) {
            var profileData = response.returnData.profileData[0];
            $rootScope.profile['height'] = profileData.height;
            $rootScope.profile['weight'] = profileData.weight;
            $rootScope.profile['height'] = profileData.height;
            $rootScope.profile['fitbitEnabled'] = profileData.fitbitEnabled === 1 ? true: false;
            $rootScope.profile['fitbitClientId'] = profileData.fitbitClientId;
            $rootScope.profile['basketsScored'] = profileData.basketsScored;
        }
    });
    
    $scope.saveProfile = function() {
        var profileObj = $rootScope.profile;
        profileObj['fitbitEnabled'] = $rootScope.profile['fitbitEnabled']? 1: 0;  
        PlayerService.SavePlayerProfile(profileObj).then(function(response) {
            console.log(response);
            $scope.editable = false;
            $rootScope.fitbitClientId = $rootScope.profile['fitbitClientId'];         
        });
    };
    
    $scope.requestAccess = function() {
        NotificationService.requestCoachAccess(userId).then(function(response) {
            console.log(response);
        });
    };
});