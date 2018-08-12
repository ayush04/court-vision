/* global app */

app.controller('PlayerStatsController', function(PlayerService, $scope) {
    PlayerService.GetPlayerStatsInAGame(6, 1).then(function(response) {
        $scope.playerData = response.returnData;
    });
});