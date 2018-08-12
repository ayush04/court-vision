/* global app */

app.controller('LiveMatchController', function(EventsService, GameService, $scope, 
    $window, $timeout, $stateParams, $rootScope) {
    $scope.username = $window.sessionStorage.getItem('username');
    $scope.progress = false;
    $scope.refreshed = false;
    $scope.showSlider = false;
    var gameId = $stateParams.matchId ? $stateParams.matchId : $rootScope.matchId ? $rootScope.matchId : 7;
    var flag = true;
    var firstPlayerId = '';
    var position = {
        'ball': []
    };
    $scope.currentMatch;
    $scope.range = {
        currentTime: 0,
        maxTime: 5
    };
    
    /* Get all players in the game */
    GameService.getAllPlayersInGame(gameId).then(function(response) {
        if(!response.isErrorPresent) {
            $scope.players = response.returnData.userData;
            var firstPlayer = true;
            angular.forEach($scope.players, function (player) {
                position['player' + player.userId] = [];
                if (firstPlayer) {
                    firstPlayer = false;
                    firstPlayerId = 'player' + player.userId;
                    EventsService.GetPlayerPosition(gameId, player.userId).then(function (response) {
                        if (!response.isErrorPresent) {
                            var cnt = response.returnData.eventsData.length;
                            angular.forEach(response.returnData.eventsData, function (event, index) {
                                if (index < cnt) {
                                    $timeout(function () {
                                        if (!$scope.refreshed) {
                                            if (index + 1 === cnt) {
                                                flag = false;
                                                if ($scope.progress) {
                                                    processDataPointsFromArray();
                                                }
                                            } else {
                                                processDataPoints(event.eventValue, event.eventValue2, 'player' + player.userId, 10000, 1);
                                            }
                                        }
                                    }, 10000 * index);
                                }
                            });
                        }
                    });
                } else {
                    EventsService.GetPlayerPosition(gameId, player.userId).then(function (response) {
                        if (!response.isErrorPresent) {
                            angular.forEach(response.returnData.eventsData, function (event, index) {
                                $timeout(function () {
                                    if (!$scope.refreshed) {
                                        processDataPoints(event.eventValue, event.eventValue2, 'player' + player.userId, 10000, 1);
                                    }
                                }, 10000 * index);
                            });
                        }
                    });
                }
            });
        }
    });
    
    var absoluteStartTime = 0;
    var move = function (xPosition, yPosition, object, duration) {
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
        if(object === firstPlayerId) {
            $scope.range.currentTime += 10000;
        }
    };
    
    var processDataPoints = function(xPosition, yPosition, object, duration, live) {
        if($scope.progress) {
            if(position[object].length === 0 || live === 0) {
                move(xPosition, yPosition, object, duration);
            }
            else {
                var obj = {
                    xPosition: xPosition,
                    yPosition: yPosition,
                    object: object,
                    duration: duration
                };
                position[object].push(obj);
                /* Get first element from position and call move method */
                var dataPoint = position[object].shift();
                move(dataPoint.xPosition, dataPoint.yPosition, dataPoint.object, dataPoint.duration);
            }
        } else {
            var obj = {
                xPosition: xPosition,
                yPosition: yPosition,
                object: object,
                duration: duration
            };
            position[object].push(obj);
        }
    };
    
    var processDataPointsFromArray = function() {
        angular.forEach($scope.players, function(player) {
            angular.forEach(position['player' + player.userId], function (p, index) {
                $timeout(function () {
                    processDataPoints(p.xPosition, p.yPosition, p.object, p.duration, 1);
                }, 10000 * index);
            }); 
        });
    };
    
    EventsService.GetBallPosition(gameId).then(function(response) {
        if (!response.isErrorPresent) {
            angular.forEach(response.returnData.eventsData, function (event, index) {
                $timeout(function () {
                    if (!$scope.refreshed) {
                        processDataPoints(event.eventValue, event.eventValue2, 'ball', 10000, 1);
                    }
                }, 10000 * index);
            });
        }
    });
    
    GameService.getCurrentGames().then(function(response) {
        $scope.currentGames = response.returnData;
    });
    
    /*GameService.getAllGames().then(function(response) {
        if(!response.isErrorPresent) {
            $scope.games = response.returnData.gameData;
        }
    });*/
    
    GameService.getGameDetails(gameId).then(function(response) {
        $scope.range.startTime = 0;
        absoluteStartTime = response.returnData.gameData[0].startTime;
        $scope.range.endTime = response.returnData.gameData[0].endTime - response.returnData.gameData[0].startTime;
        $scope.range.maxTime = $scope.range.endTime;
        $scope.showSlider = true;
    });
    
    $scope.play = function() {
        $scope.progress = true;
        if(!flag) {
            processDataPointsFromArray();
        }
    };
    
    $scope.pause = function() {
        $scope.progress = false;
    };
    
    $scope.refresh = function() {
        $scope.refreshed = true;
        var startTime = $scope.range.currentTime + absoluteStartTime;
        var endTime = $scope.range.maxTime + absoluteStartTime;
        var firstPlayer = true;
        angular.forEach($scope.players, function(player) {
            position['player' + player.userId] = [];
            if(firstPlayer) {
                firstPlayer = false;
                firstPlayerId = 'player' + player.userId;
                EventsService.GetPlayerPositionInTimeDuration(gameId, player.userId,
                        startTime, endTime).then(function (response) {
                    if(!response.isErrorPresent && response.returnData 
                            && response.returnData.eventsData) {
                        $scope.progress = true;
                        var cnt = response.returnData.eventsData.length;
                        angular.forEach(response.returnData.eventsData, function (event, index) {
                            if (index < cnt) {
                                $timeout(function () {
                                    processDataPoints(event.eventValue, event.eventValue2, 'player' + player.userId, 10000, 0);
                                }, 10000 * index);
                            }
                        });
                    }
                });
            }
            else {
                EventsService.GetPlayerPositionInTimeDuration(gameId, player.userId, startTime, endTime).then(function (response) {
                    if (!response.isErrorPresent) {
                        angular.forEach(response.returnData.eventsData, function (event, index) {
                            $timeout(function () {
                                processDataPoints(event.eventValue, event.eventValue2, 'player' + player.userId, 10000, 0);
                            }, 10000 * index);
                        });
                    }
                });
            }
        });
        
        EventsService.GetBallPositionInTimeDuration(gameId, startTime, endTime).then(function(response) {
            if (!response.isErrorPresent) {
                angular.forEach(response.returnData.eventsData, function (event, index) {
                    $timeout(function () {
                        processDataPoints(event.eventValue, event.eventValue2, 'ball', 10000, 0);
                    }, 10000 * index);
                });
            }
        });
    };
});