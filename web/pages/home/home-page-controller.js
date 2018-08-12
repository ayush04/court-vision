/* global app */

app.controller('HomePageController', function(GameService, $scope, $filter, $state, $rootScope) {
    GameService.getCompletedGames().then(function(response) {
        $scope.completedGames = [];
        angular.forEach(response.returnData.gameData, function(game) {
            var obj = {};
            obj.gameId = game.gameId;
            obj.startTime = $filter('date')(game.startTime, 'medium');
            obj.duration = new Date(game.endTime - game.startTime).toISOString().slice(11, -1);
            $scope.completedGames.push(obj);
        });
    });
    
    $scope.showMatch = function(game) {
        $rootScope.currentMatch = game.gameId;
        $state.go('main.home', {matchId: game.gameId});
    };
});