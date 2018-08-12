/* configure our routes */
/* global app */

app.config(function ($stateProvider) {
    /* If no specific page is requested then load the default Home Page with the default values defined */
    $stateProvider
        .state('login', {
            templateUrl: 'pages/login/login.html',
            url: '/cv/login'
        })
        .state('signup', {
            templateUrl: 'pages/signup/signup.html',
            url: '/cv/signup'
        })
        .state('main', {
            templateUrl: 'pages/main/main.html'
        })
        .state('main.home', {
            templateUrl: 'pages/home/home.html',
            url: '/cv/home?matchId'
        })
        .state('main.graph', {
            templateUrl: 'pages/graph/graph.html',
            url: '/cv/graph?matchId'
        })
        .state('main.profile', {
            templateUrl: 'pages/profile/profile.html',
            url: '/cv/profile'
        })
        .state('main.myteam', {
            templateUrl: 'pages/my-team/my-team.html',
            url: '/cv/my-team'
        })
        .state('main.calendar', {
            templateUrl: 'pages/calendar/calendar.html',
            url: '/cv/calendar'
        });
});