/* global app */

app.factory('TeamService', function(HttpService) {
    var factory = {};
    
    factory.GetAllTeams = function() {
        return HttpService.makeHttpCall('GET', 'team');
    };
    
    factory.GetTeamDetails = function(teamId) {
        return HttpService.makeHttpCall('GET', 'team/' + teamId);
    };
    
    factory.GetTeamCoach = function(teamId) {
        return HttpService.makeHttpCall('GET', 'team/' + teamId + '/coach');
    };
    
    factory.GetAllTeamMembers = function(teamId) {
        return HttpService.makeHttpCall('GET', 'team/users/' + teamId);
    };
    
    factory.GetMyTeam = function() {
        return HttpService.makeHttpCall('GET', 'team/myteam');
    };
    
    return factory;
});