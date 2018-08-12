/* global app */

app.factory('PlayerService', function(HttpService) {
    var factory = {};
    
    factory.GetPlayerStatsInAGame = function(gameId, userId) {
        return HttpService.makeHttpCall('GET', 'player/game/' + gameId + '/stats/' + userId);
    };
    
    factory.GetPlayerProfile = function(userId) {
        return HttpService.makeHttpCall('GET', 'player/profile/' + userId);
    };
    
    factory.SavePlayerProfile = function(profileObj) {
        return HttpService.makeHttpCall('PUT', 'player/profile/update', profileObj);
    };
    
    return factory;
});