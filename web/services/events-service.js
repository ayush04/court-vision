/* global app, config */

app.factory('EventsService', function(HttpService) {
    var factory = {};
    
    factory.GetPlayerPosition = function(gameId, userId) {
        return HttpService.makeHttpCall('GET', 'events/game/' + gameId + '/position/' + userId);
    };
    
    factory.GetPlayerPositionInTimeDuration = function(gameId, userId, startTime, endTime) {
        return HttpService.makeHttpCall('GET', 'events/game/' + gameId + '/position/' + userId + '/' + startTime + '/' + endTime);
    };
    
    factory.GetBallPosition = function (gameId) {
        return HttpService.makeHttpCall('GET', 'events/game/' + gameId + '/position/ball');
    };

    factory.GetBallPositionInTimeDuration = function (gameId, startTime, endTime) {
        return HttpService.makeHttpCall('GET', 'events/game/' + gameId + '/position/ball/' + startTime + '/' + endTime);
    };
    return factory;
});