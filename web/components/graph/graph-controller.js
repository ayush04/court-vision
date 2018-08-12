/* global app, google, h337 */

app.controller('GraphController', function(EventsService, GameService, PlayerService, 
    UserService, $rootScope, $scope, $stateParams, $http, $window) {
    $scope.matchId = $stateParams.matchId? $stateParams.matchId: 7;
    $scope.playerStats = {};
    $scope.quarterStats = {};
    var gameStartTime = '12:50';
    var gameEndTime = '14:50';
    $scope.players = [];
    google.charts.load('current', {'packages': ['corechart']});
    
    /* Heatmap */
    var generateHeatMap = function() {
        angular.forEach($scope.players, function(player) {
            EventsService.GetPlayerPosition($scope.matchId, player.userId).then(function (response) {
                if (!response.isErrorPresent) {
                    var data = [];
                    angular.forEach(response.returnData.eventsData, function (event) {
                        var obj = {};
                        obj.x = parseFloat(event.eventValue);
                        obj.y = parseFloat(event.eventValue2);
                        obj.value = 2;
                        data.push(obj);
                    });
                    var heatmap = h337.create({
                        container: document.getElementById('heatmap_' + player.userId),
                        gradient: {.1: 'rgba(0,0,0,0)', 0.25: "rgba(0,0,90, .6)", .6: "blue", .9: "cyan", .95: 'rgba(255,255,255,.4)'},
                        maxOpacity: .6,
                        radius: 10,
                        blur: .90
                    });

                    heatmap.addData(data);
                }
            }); 
        });
    };
    
    var drawChart = function(userId, id, name) {
        EventsService.GetPlayerPosition($scope.matchId, userId).then(function (response) {
            if (!response.isErrorPresent) {
                var data = [['x', 'y']];
                angular.forEach(response.returnData.eventsData, function (event) {
                    data.push([parseFloat(event.eventValue), parseFloat(event.eventValue2)]);
                });
            }
            
            var options = {
                title: 'Player Position - ' + name,
                hAxis: {title: 'x', minValue: 0, maxValue: 860},
                vAxis: {title: 'y', minValue: 0, maxValue: 470},
                legend: 'none',
                backgroundColor: 'none'
            };
            
            var chart = new google.visualization.ScatterChart(document.getElementById(id));
            chart.draw(google.visualization.arrayToDataTable(data), options);
        });
    };
    
    var getMatchAndQuarterStats = function() {
        angular.forEach($scope.players, function(player) {
            google.charts.setOnLoadCallback(drawChart.bind(this, player.userId, 'player' + player.userId + '_chart', player.userName));
            PlayerService.GetPlayerStatsInAGame($scope.matchId, player.userId).then(function (stats) {
                $scope.playerStats[player.userId] = stats.returnData.playerData[0];
            });
            GameService.GetQuarterStatsOfPlayer($scope.matchId, player.userId).then(function (qStats) {
                $scope.quarterStats[player.userId] = qStats.returnData.genericData;
                drawQuarterStats(qStats.returnData.genericData, player.userId);
            });
        });
    };
    
    var drawQuarterStats = function(data, userId) {
        var speedData = new google.visualization.DataTable();
        speedData.addColumn('string', 'Quarter');
        speedData.addColumn('number', 'Speed');
        speedData.addRows(4);
        var distanceData = new google.visualization.DataTable();
        distanceData.addColumn('string', 'Quarter');
        distanceData.addColumn('number', 'Distance');
        distanceData.addRows(4);
        var speedIndex = 1;
        var distanceIndex = 1;
        angular.forEach(data, function(row) {
            if(row.key.indexOf('speed-quarter') > -1) {
                speedData.setCell(speedIndex - 1, 0, 'Quarter ' + speedIndex);
                speedData.setCell(speedIndex - 1, 1, parseFloat(row.value));
                speedIndex++;
            }
            else if(row.key.indexOf('distance-quarter') > -1) {
                distanceData.setCell(distanceIndex - 1, 0, 'Quarter ' + distanceIndex);
                distanceData.setCell(distanceIndex - 1, 1, parseFloat(row.value));
                distanceIndex++;
            }
        });
        
        var speedOptions = {
            title: 'Speed per quarter',
            trendlines: {
                0: {type: 'exponential', lineWidth: 10, opacity: .3}
            },
            hAxis: {
                title: 'Quarter'

            },
            vAxis: {
                title: 'Speed in m/sec'
            },
            legend: { position: 'top', maxLines: 3 }
        };
        var distanceOptions = {
            title: 'Distance per quarter',
            trendlines: {
                0: {type: 'exponential', lineWidth: 10, opacity: .3}
            },
            hAxis: {
                title: 'Quarter'

            },
            vAxis: {
                title: 'Distance in meters'
            },
            legend: { position: 'top', maxLines: 3 }
        };
        var chart = new google.visualization.ColumnChart(document.getElementById('player' + userId + '_speed_quarter'));
        chart.draw(speedData, speedOptions);
        var chart = new google.visualization.ColumnChart(document.getElementById('player' + userId + '_distance_quarter'));
        chart.draw(distanceData, distanceOptions);
    };
    
    var processHeartRate = function (timeSeries) {
        return timeSeries['activities-heart-intraday'].dataset.map(
                function (measurement) {
                    return [
                        measurement.time.split(':').map(
                                function (timeSegment) {
                                    return Number.parseInt(timeSegment);
                                }
                        ),
                        measurement.value
                    ];
                }
        );
    };
    
    var graphHeartRate = function (timeSeries) {
        var data = new google.visualization.DataTable();
        data.addColumn('timeofday', 'Time of Day');
        data.addColumn('number', 'Heart Rate');
        data.addRows(timeSeries);

        var options = google.charts.Line.convertOptions({
            height: 450
        });

        var chart = new google.charts.Line(document.getElementById('heart_rate'));

        chart.draw(data, options);
    };
    
    function drawPlayerSpeed(speedData, isMultiple) {
        var data = [];
        if(!isMultiple) {
            data.push(['Time', 'Speed']);
            angular.forEach(speedData, function (row) {
                data.push([new Date(parseFloat(row.key)), parseFloat(row.value)]);
            });
            data = google.visualization.arrayToDataTable(data);
        }
        else {
            var d = ['Time'];
            angular.forEach($scope.players, function(player) {
                d.push(player.userName);
            });
            data.push(d);
            var temp = {};
            angular.forEach(speedData, function(row) {
                var s = row.value.replace('{', '').replace('}', '').split(', ');
                angular.forEach(s, function(row) {
                    var cells = row.split('=');
                    if(temp[new Date(parseFloat(cells[0]))]) {
                        temp[new Date(parseFloat(cells[0]))].push(parseFloat(cells[1]));
                    }
                    else {
                        temp[new Date(parseFloat(cells[0]))] = [new Date(parseFloat(cells[0])), parseFloat(cells[1])];
                    }
                });
            });
            angular.forEach(temp, function(row) {
                data.push(row);
            });
            data = google.visualization.arrayToDataTable(data);
            //console.log(temp);
        }
        var options = {
            title: 'Player Speed',
            curveType: 'function',
            legend: {position: 'bottom'}
        };

        var chart = new google.visualization.LineChart(document.getElementById('player_speed'));

        chart.draw(data, options);
    };
    
    var drawPlayerSpeedGraph = function() {
        if($rootScope.isCoach) {
            GameService.GetAllPlayersSpeedInGame($scope.matchId).then(function (response) {
                console.log(response.returnData.genericData);
                google.charts.setOnLoadCallback(drawPlayerSpeed.bind(this, response.returnData.genericData, true));
            });
        }
        else {
            GameService.GetPlayerSpeedInGame($rootScope.userId, $scope.matchId).then(function(response) {
                google.charts.setOnLoadCallback(drawPlayerSpeed.bind(this, response.returnData.genericData, false));
            });
        }
    };
    
    /*Fitbit Graphs*/
    var calculateCaloriesInTimeDuration = function (startTime, endTime, data) {
        var calories = 0;
        if (data && data['activities-calories-intraday']) {
            var dataset = data['activities-calories-intraday']['dataset'];
            var inRange = false;
            var count = 0;
            var start = startTime.split(':');
            var end = endTime.split(':');
            angular.forEach(dataset, function (entry) {
                var t = entry.time;
                var time = t.split(':');
                if (!inRange && parseInt(time[0]) === parseInt(start[0]) && parseInt(time[1]) === parseInt(start[1])) {
                    inRange = true;
                }
                if (inRange) {
                    calories += parseFloat(entry.value);
                    count++;
                    if (parseInt(time[0]) > parseInt(end[0]) ||
                            (parseInt(time[0]) === parseInt(end[0]) && parseInt(time[1]) > parseInt(end[1]))) {
                        inRange = false;
                    }
                }
            });
        }
        console.log(count);
        return calories;
    };

    var callFitbitAPI = function (url) {
        return $http({
            method: 'GET',
            url: url,
            headers: {
                'Authorization': 'Bearer ' + $window.localStorage.getItem('fitbitAccessToken'),
                'Accept-Language': 'en_GB'
            },
            mode: 'CORS'
        }).then(function success(response) {
            return response;
        }, function error(response) {
            console.log(response);
        });
    };
    
    var getHeartRate = function() {
        var url = 'https://api.fitbit.com/1/user/-/activities/heart/date/2018-07-28/1d/1sec/time/12:50/14:50.json';
        callFitbitAPI(url).then(function(response) {
            graphHeartRate(processHeartRate(response.data));
        });
    };
    
    var getCalorieCount = function() {
        var url = 'https://api.fitbit.com/1/user/-/activities/calories/date/2018-07-28/1d.json';
        callFitbitAPI(url).then(function(response) {
            $scope.caloriesBurnt = calculateCaloriesInTimeDuration(gameStartTime, gameEndTime, response.data);
        });
    };
    
    if($rootScope.fitbitClientId !== '') {
        getHeartRate();
        getCalorieCount();
    }
    
    var generateGraphs = function () {
        generateHeatMap();
        getMatchAndQuarterStats();
        drawPlayerSpeedGraph();
    };
    if (!angular.isDefined($rootScope.isCoach)) {
        UserService.IsUserCoach($rootScope.userId).then(function (response) {
            $rootScope.isCoach = (response.returnData.genericData[0].value === 'true');
            $window.sessionStorage.setItem('isCoach', $rootScope.isCoach);
            if ($rootScope.isCoach) {
                GameService.getAllPlayersInGame($scope.matchId).then(function (playerResponse) {
                    if (!playerResponse.isErrorPresent) {
                        $scope.players = playerResponse.returnData.userData;
                        generateGraphs();
                    }
                });
            } else {
                $scope.players.push({userId: $rootScope.userId, userName: $window.sessionStorage.getItem('username')});
                generateGraphs();
            }
        });
    } else {
        if ($rootScope.isCoach) {
            GameService.getAllPlayersInGame($scope.matchId).then(function (playerResponse) {
                if (!playerResponse.isErrorPresent) {
                    $scope.players = playerResponse.returnData.userData;
                    generateGraphs();
                }
            });
        } else {
            $scope.players.push({userId: $rootScope.userId, userName: $window.sessionStorage.getItem('username')});
            generateGraphs();
        }
    }
});