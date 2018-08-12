
app.directive('homeLink', function($timeout) {
    function link(scope, element) {
        var homeLink = function () {
            var url = '/home';
            if (!element.attr('href')) {
                element.attr('href', url);
            }
        };
        $timeout(homeLink, 0);
    };
    return {
        link: link,
        restrict: 'A'
    };
});

app.directive('signupLink', function ($timeout) {
    function link(scope, element) {
        var signupLink = function () {
            var url = '/signup';
            if (!element.attr('href')) {
                element.attr('href', url);
            }
        };
        $timeout(signupLink, 0);
    }
    ;
    return {
        link: link,
        restrict: 'A'
    };
});

app.directive('loginLink', function ($timeout) {
    function link(scope, element) {
        var loginLink = function () {
            var url = '/login';
            if (!element.attr('href')) {
                element.attr('href', url);
            }
        };
        $timeout(loginLink, 0);
    }
    ;
    return {
        link: link,
        restrict: 'A'
    };
});


app.directive('downloadAttachment', function($window, $location) {
    var apiPath = '';
    function link(scope, element) {
        element.on('click', function() {
            var attachmentUrl = apiPath + 'attachment/' + scope.attachment.attachmentId + '/' + $window.sessionStorage.getItem('session-id');
            $window.location = attachmentUrl;
        });
    };
    return {
        link: link,
        restrict: 'A'
    };
});

app.filter('unsafe', function($sce) { 
    return $sce.trustAsHtml; 
});

app.directive('increaseFont', function() {
    function link(scope, element) {
        element.on('click', function() {
            var fontSize = window.getComputedStyle(document.body, null).getPropertyValue('font-size');
            fontSize = parseFloat(fontSize);
            var incrementor = 1;
            document.body.style["font-size"] = fontSize + incrementor + 'px';
        });
    };
    return {
        link: link,
        restrict: 'A'
    };
});

app.directive('decreaseFont', function() {
    function link(scope, element) {
        element.on('click', function() {
            var fontSize = window.getComputedStyle(document.body, null).getPropertyValue('font-size');
            fontSize = parseFloat(fontSize);
            var decrementor = 1;
            document.body.style["font-size"] = fontSize - decrementor + 'px';
        });
    };
    return {
        link: link,
        restrict: 'A'
    };
});