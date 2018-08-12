/* global app */

app.factory('Utils', function($window, $rootScope) {
    var factory = {};
    
    factory.PostParams = function (obj) {
        var str = [];
        for (var p in obj)
            str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
        return str.join("&");
    };
    
    factory.GetTimeStamp = function() {
        return new Date().getTime();
    };
    
    factory.ClearSession = function() {
        delete $rootScope.sessionId;
        delete $rootScope.isCoach;
        $window.sessionStorage.removeItem('session-id');
        $window.sessionStorage.removeItem('user-id');
        $window.sessionStorage.removeItem('username');
        $window.sessionStorage.removeItem('isCoach');
    };
    return factory;
});