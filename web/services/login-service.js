/* global config, app */

app.factory('LoginService', function (HttpService) {
    var factory = {};

    factory.Login = function (userName, password) {
        var data = {
            'userName': userName,
            'password': password
        };
        return HttpService.makeHttpCall('POST', 'user/login', data);
    };

    factory.Logout = function () {
        return HttpService.makeHttpCall('POST', 'user/logout');
    };

    factory.Signup = function (userName, password, emailAddress) {
        var data = {
            'userName': userName,
            'password': password,
            'emailAddress': emailAddress
        };
        return HttpService.makeHttpCall('POST', 'user/signup', data);
    };

    return factory;
});