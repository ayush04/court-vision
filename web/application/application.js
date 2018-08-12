var app = angular.module('app', [
    'ui.router',
    'ui.bootstrap',
    'textAngular',
    'angularFileUpload',
    'ui.select',
    'easypiechart',
    'vr.directives.slider',
    'bsLoadingOverlay',
    'ngAnimate',
    'ui.calendar'
]);

app.config(function ($locationProvider) {
    $locationProvider.html5Mode({
        enabled: true,
        requireBase: false
    });
});
app.run(function ($rootScope, $state, bsLoadingOverlayService) {
    
    bsLoadingOverlayService.setGlobalConfig({
        templateUrl: './components/progressbar/progressbar.html' // Template url for overlay element. If not specified - no overlay element is created.
    });

    if (!$rootScope.sessionId) {
        $rootScope.sessionId = window.sessionStorage.getItem('session-id');
    }
    if (!$rootScope.sessionId) {
        $state.go('login');
    } else {
        $state.go('main.home');
    }
});

app.filter('camelCase', function ()
{
    var camelCaseFilter = function (input)
    {
        var words = input.split(' ');
        for (var i = 0, len = words.length; i < len; i++)
            words[i] = words[i].charAt(0).toUpperCase() + words[i].slice(1);
        return words.join(' ');
    };
    return camelCaseFilter;
});

app.filter('time', function() {
    var convertMilliSecondsToTime = function (milliSeconds) {
        var sec = Math.floor(milliSeconds / 1000);
        var min = Math.floor(sec / 60);
        sec = sec % 60;
        return min + ':' + sec;
    };
    return convertMilliSecondsToTime;
});

app.filter('percentage', function($filter) {
    return function(value, fractionSize) {
        return $filter('number')(value * 100, fractionSize) + '%';
    };
});
