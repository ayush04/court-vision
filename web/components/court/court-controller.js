/* global app */

app.controller('CourtController', function(EventsService, $window, $timeout) {
    var move = function(xPosition, yPosition, object, duration) {
        anime({
            targets: '.' + object,
            translateX: function () {
                return xPosition;
            },
            translateY: function () {
                return yPosition;
            },
            duration: duration,
            easing: 'easeInOutSine'
        });
    };
    
    EventsService.GetPlayerPosition(1, $window.sessionStorage.getItem('user-id')).then(function(response) {
        if(!response.isErrorPresent) {
            angular.forEach(response.returnData.eventsData, function(event, index) {
                $timeout(function () {
                    move(event.eventValue, event.eventValue2, 'player1', 2000);
                }, 2000 * index);
            });
        }
    });
    
    EventsService.GetPlayerPosition(1, 3).then(function (response) {
        if (!response.isErrorPresent) {
            angular.forEach(response.returnData.eventsData, function (event, index) {
                $timeout(function () {
                    move(event.eventValue, event.eventValue2, 'player2', 2000);
                }, 2000 * index);
            });
        }
    });
});