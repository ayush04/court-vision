app.controller('MatchStatsController', function(GameService, UserService, $stateParams, $scope) {
    var gameId = $stateParams.matchId ? $stateParams.matchId : 7;
    $scope.quarterStats = {};
    /*Get game stats*/
    GameService.GetGameStats(gameId).then(function (response) {
        if (!response.isErrorPresent && response.returnData.gamestatsData) {
            var gameStats = response.returnData.gamestatsData[0];
            console.log(gameStats);
            /* Some calculations */
            $scope.gameDuration = gameStats.endTime - gameStats.startTime;
            $scope.passAccuracy = gameStats.successfulPasses / gameStats.numberOfPasses;
            $scope.baskets = 0;
            angular.forEach(gameStats.gameScorers, function(scorer) {
                $scope.baskets += scorer.score;
            });
            UserService.GetUser(gameStats.topScorer).then(function(userResponse) {
                if(!userResponse.isErrorPresent) {
                    $scope.topScorer = userResponse.returnData.userData[0].userName;
                };
            });
        };
    });
    
    GameService.GetQuarterStats(gameId).then(function (response) {
        if (!response.isErrorPresent) {
            angular.forEach(response.returnData.genericData, function (data) {
                var passes = JSON.parse(data.value);
                var successPercent = (passes[1] / passes[0]) * 100;
                var quarter = parseInt(data.key.substr(data.key.length - 1));
                $scope.quarterStats[quarter] = successPercent;
            });
            console.log($scope.quarterStats);
        }
    });
});