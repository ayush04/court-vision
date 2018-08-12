/* global app, config */

app.factory('GameService', function(HttpService) {
    var factory = {};
    
    factory.getAllGames = function() {
        return HttpService.makeHttpCall('GET', 'game');
    };
    
    factory.getCurrentGames = function () {
        return HttpService.makeHttpCall('GET', 'game/current');
    };
    
    factory.getCompletedGames = function () {
        return HttpService.makeHttpCall('GET', 'game/completed');
    };
    
    factory.startGame = function () {
        return HttpService.makeHttpCall('POST', 'game');
    };
    
    factory.endGame = function (gameId) {
        return HttpService.makeHttpCall('POST', 'game/' + gameId);
    };
    
    factory.getGameDetails = function(gameId) {
        return HttpService.makeHttpCall('GET', 'game/' + gameId);
    };
    
    factory.getAllPlayersInGame = function(gameId) {
        return HttpService.makeHttpCall('GET', 'game/' + gameId + '/players');
    };
    
    factory.GetPlayerSpeedInGame = function(userId, gameId) {
        return HttpService.makeHttpCall('GET', 'game/' + gameId + '/player/' + userId + '/speed');
    };
    
    factory.GetAllPlayersSpeedInGame = function (gameId) {
        return HttpService.makeHttpCall('GET', 'game/' + gameId + '/player/speed');
    };
    
    factory.GetGameStats = function(gameId) {
        return HttpService.makeHttpCall('GET', 'game/stats/' + gameId);
    };
    
    factory.GetQuarterStatsOfPlayer = function(gameId, userId) {
        return HttpService.makeHttpCall('GET', 'game/quarter/stats/' + gameId + '/player/' + userId);
    };
    
    factory.GetQuarterStats = function (gameId) {
        return HttpService.makeHttpCall('GET', 'game/quarter/stats/' + gameId);
    };
    
    return factory;
});