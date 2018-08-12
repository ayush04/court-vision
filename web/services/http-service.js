/* global app, config */

app.factory('HttpService', function($http, $rootScope, Utils) {
    var factory = {};
    /* Create the global return object */
    var serviceReturnValue = {
        isErrorPresent: false,
        returnData: {},
        errorObject: {}
    };
    
    factory.makeHttpCall = function(method, url, data) {
        var requestObject = {
            method: method,
            url: config.apiPath + url,
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json',
                'sessionId': $rootScope.sessionId
            }
        };
        if((method === 'POST' || method === 'PUT') && data) {
            requestObject.data = Utils.PostParams(data);
        };
        return $http(requestObject).then(function (response) {
            serviceReturnValue.isErrorPresent = false;
            serviceReturnValue.errorObject = {};
            serviceReturnValue.returnData = response.data;
            return serviceReturnValue;
        }, function (response) {
            serviceReturnValue.isErrorPresent = true;
            serviceReturnValue.errorObject = response.data;
            return serviceReturnValue;
        });
    };
    return factory;
});

