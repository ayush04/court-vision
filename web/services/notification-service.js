/* global app */

app.factory('NotificationService', function(HttpService) {
    var factory = {};
   
    factory.GetAllNotificationsForUser = function (userId) {
        return HttpService.makeHttpCall('GET', 'notification/' + userId);
    };
   
    
    factory.markNotificationAsRead = function (notificationId) {
        return HttpService.makeHttpCall('PUT', 'notification/' + notificationId);
    };
    
    factory.requestCoachAccess = function(userId) {
        return HttpService.makeHttpCall('POST', 'notification/4/user/' + userId);
    };
    
    factory.GetCoachRequestNotificationForTeam = function(teamId) {
        return HttpService.makeHttpCall('GET', 'notification/coach-request/' + teamId);
    };
    return factory;
});