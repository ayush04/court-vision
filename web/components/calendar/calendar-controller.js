app.controller('CalendarController', function ($scope, $filter, uiCalendarConfig) {
    var date = new Date();
    var d = date.getDate();
    var m = date.getMonth();
    var y = date.getFullYear();
    
    //new Date(year, monthIndex [, day [, hours [, minutes [, seconds [, milliseconds]]]]]);
    $scope.events = [
        {title: 'All Day Event', start: new Date(y, m, 1), location: 'New York', info: 'This a all day event that will start from 9:00 am to 9:00 pm, have fun!'},
        {title: 'Dance class', start: new Date(y, m, 3), end: new Date(y, m, 4, 9, 30), allDay: false, location: 'London', info: 'Two days dance training class.'},
        {title: 'Game racing', start: new Date(y, m, 6, 16, 0), location: 'Hongkong', info: 'The most big racing of this year.'},
        {title: 'Soccer', start: new Date(y, m, 8, 15, 0), location: 'Rio', info: 'Do not forget to watch.'},
        {title: 'Family', start: new Date(y, m, 9, 19, 30), end: new Date(y, m, 9, 20, 30), info: 'Family party'},
        {title: 'Long Event', start: new Date(y, m, d - 5), end: new Date(y, m, d - 2), location: 'HD City', info: 'It is a long long event'},
        {title: 'Play game', start: new Date(y, m, d - 1, 16, 0), location: 'Tokyo', info: 'Tokyo Game Racing'},
        {title: 'Birthday Party', start: new Date(y, m, d + 1, 19, 0), end: new Date(y, m, d + 1, 22, 30), allDay: false, location: 'New York', info: 'Party all day'},
        {title: 'Repeating Event', start: new Date(y, m, d + 4, 16, 0), alDay: false, location: 'Home Town', info: 'Repeat every day'},
        {title: 'Click for Google', start: new Date(y, m, 28), end: new Date(y, m, 29) },
        {title: 'Feed cat', start: new Date(y, m + 1, 6, 18, 0)}
    ];

    $scope.eventSources = [$scope.events];

    $scope.showNewEvent = function (start, end) {
        $scope.newEvent = true;
        $scope.eventDate = $filter('date')(start.format(), 'fullDate');
    };

    $scope.addNewEvent = function () {
        $scope.events.push({
            title: $scope.eventTitle,
            info: $scope.eventDesc,
            start: $scope.eventStartTime,
            end: $scope.eventEndTime

        });
        $scope.newEvent = false;
        
    };

    $scope.uiConfig = {
        calendar: {
            editable: true,
            selectable: true,
            displayEventTime: false,
            header: {
                left: 'month basicWeek basicDay agendaWeek agendaDay',
                center: 'title',
                right: 'today prev,next'
            },
            select: function (start, end) {
                $scope.showNewEvent(start, end);
            }
        }
    };


    $scope.mytime = new Date();

    $scope.hstep = 1;
    $scope.mstep = 15;

    $scope.options = {
        hstep: [1, 2, 3],
        mstep: [1, 5, 10, 15, 25, 30]
    };

    $scope.ismeridian = true;
    $scope.toggleMode = function () {
        $scope.ismeridian = !$scope.ismeridian;
    };

    $scope.update = function () {
        var d = new Date();
        d.setHours(14);
        d.setMinutes(0);
        $scope.mytime = d;
    };

    $scope.changed = function () {
        //console.log('Time changed to: ' + $scope.mytime);
    };

    $scope.clear = function () {
        $scope.mytime = null;
    };
});