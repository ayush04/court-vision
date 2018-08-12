/* global app */

app.controller('MatchListController', function(GameService, TeamService, $scope, $rootScope, 
    $stateParams) {
    $scope.teams = {};
    $rootScope.currentMatchTeams = [];
    $scope.currentMatchId = $stateParams.matchId ? $stateParams.matchId : $rootScope.matchId;
    $scope.currentMatchId = parseInt($scope.currentMatchId);
    GameService.getAllGames().then(function(response) {
        if (!response.isErrorPresent) {
            $scope.games = response.returnData.gameData;
            $scope.games.reverse();
            if(!$scope.currentMatchId) {
                $scope.currentMatchId = $scope.games[0].gameId;
                $rootScope.matchId = $scope.games[0].gameId;
            }
            angular.forEach($scope.games, function(game) {
                game.playedOn = new Date(game.startTime);
                if(!$scope.teams[game.team1]) {
                    TeamService.GetTeamDetails(game.team1).then(function(response) {
                        $scope.teams[game.team1] = response.returnData.teamData[0].teamName;
                        if (game.gameId === $scope.currentMatchId) {
                            $rootScope.currentMatchTeams.push(response.returnData.teamData[0].teamName);
                        }
                    });
                }
                if (!$scope.teams[game.team2]) {
                    TeamService.GetTeamDetails(game.team2).then(function (response) {
                        $scope.teams[game.team2] = response.returnData.teamData[0].teamName;
                        if (game.gameId === $scope.currentMatchId) {
                            $rootScope.currentMatchTeams.push(response.returnData.teamData[0].teamName);
                        }
                    });
                }
            });
        }
    });
});