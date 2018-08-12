/* global app */

app.controller('MyTeamController', function(TeamService, NotificationService, UserService, 
    $window, $rootScope, $scope) {
    $scope.notificationCreatedBy = [];
    
    var getMyTeam = function() {
        TeamService.GetMyTeam().then(function (response) {
            if (!response.isErrorPresent) {
                var teamId = response.returnData.teamData[0].teamId;
                TeamService.GetAllTeamMembers(teamId).then(function (userResponse) {
                    if (!userResponse.isErrorPresent) {
                        $scope.myTeamMembers = userResponse.returnData.userData;
                    }
                });

                NotificationService.GetCoachRequestNotificationForTeam(teamId).then(function (res) {
                    if (!res.isErrorPresent) {
                        angular.forEach(res.returnData.notificationData, function (notification) {
                            $scope.notificationCreatedBy.push(notification.createdBy);
                        });
                    }
                });
            }
        });
    };
    if (!angular.isDefined($window.sessionStorage.getItem('isCoach')) && $window.sessionStorage.getItem('isCoach') !== null) {
        UserService.IsUserCoach($rootScope.userId).then(function (coachResponse) {
            $rootScope.isCoach = (coachResponse.returnData.genericData[0].value === 'true');
            $window.sessionStorage.setItem('isCoach', $rootScope.isCoach);
            if($rootScope.isCoach) {
                getMyTeam();
            }
        });
    }
    else {
        $rootScope.isCoach = $window.sessionStorage.getItem('isCoach') === 'true';
        if($rootScope.isCoach) {
            getMyTeam();
        }
    }
    
    $scope.showAccessButton = function(userId) {
        return $scope.notificationCreatedBy.indexOf(userId) > -1 && $rootScope.isCoach;
    };
    
    $scope.grantAccess = function(userId) {
        UserService.GrantUserCoachRole(userId, $window.sessionStorage.getItem('user-id'))
                .then(function(response) {
            if(!response.isErrorPresent) {
                $scope.notificationCreatedBy.splice($scope.notificationCreatedBy.indexOf(userId), 1);
                $scope.showAccessButton(userId);
            }
        });
    };
});