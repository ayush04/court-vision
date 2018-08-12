/* global app, config */

app.factory('UserService', function (HttpService) {
    var factory = {};

    factory.GetUser = function (userId) {
        return HttpService.makeHttpCall('GET', 'user/' + userId);
    };
    
    factory.IsUserCoach = function(userId) {
        return HttpService.makeHttpCall('GET', 'user/coach/' + userId);
    };
    
    factory.RegisterUserToTeam = function(userId, teamId, isCoach) {
        return HttpService.makeHttpCall('POST', 'user/' + userId + '/team/' + teamId, {
            isCoach: isCoach
        });
    };
    
    factory.GrantUserCoachRole = function(userId, coachId) {
        return HttpService.makeHttpCall('POST', 'user/' + userId + '/granted-by/' + coachId);
    };

    return factory;
});