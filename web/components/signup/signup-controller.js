/* global app */

app.controller('SignupController', function($scope, LoginService, TeamService, UserService) {
    $scope.user = {};
    $scope.authError = null;
    $scope.teamSelected = false;
    $scope.appName = 'Court Vision';
    $scope.coachRegistered = false;
    TeamService.GetAllTeams().then(function(response) {
        $scope.teams = response.returnData.teamData;
    });
    
    $scope.selectTeam = function() {
        if($scope.user.team === null) {
            $scope.teamSelected = false;
        }
        else {
            $scope.teamSelected = true;
            TeamService.GetTeamCoach($scope.user.team).then(function(response) {
                if(!response.isErrorPresent) {
                    $scope.coachRegistered = (response.returnData.userData && 
                            response.returnData.userData.length > 0);
                }
            });
        }
    };
    $scope.signup = function() {
        LoginService.Signup($scope.user.name, $scope.user.password, $scope.user.email).then(function(response) {
            if (!response.isErrorPresent) {
                UserService.RegisterUserToTeam(response.returnData.userData[0].userId, $scope.user.team, $scope.user.isCoach).then(function() {
                    $scope.success = true;
                    $scope.successMesg = 'Signup successful. Please login with your credentials';
                });
            }
        });
    };
});