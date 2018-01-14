var edb = {};

angular.module('mwl.calendar.docs', ['mwl.calendar', 'ngAnimate', 'ui.bootstrap', 'colorpicker.module']);
angular.module('mwl.calendar.docs') .controller('KitchenSinkCtrl', function(moment, calendarConfig) {     
    var vm = this;
    
    vm.calendarView = 'month';
    vm.viewDate = new Date();

    var actions = [
        {
            label: '<i class=\'glyphicon glyphicon-remove\'></i>',
            onClick: function(args) {
                vm.deleteEvent(args.calendarEvent);
          }
        },
    ];

    vm.events = [];
    
    vm.addEvent = function() {
        vm.events.push({
            title: 'New event',
            startsAt: moment().startOf('day').toDate(),
            endsAt: moment().endOf('day').toDate(),
            color: calendarConfig.colorTypes.important,
            draggable: false,
            resizable: false,
            actions: actions
        });
    };
    
    vm.deleteEvent = function (event) {
        vm.events = vm.events.filter(function (item) {
            return item.id != event.id;
        });
        
        edb[event.calendar].events = edb[event.calendar].events.filter(function (item) {
            return item.id != event.id;
        });
    };
    
    vm.shownCalendars = [];
    vm.checkedCalendars = [];
    
    vm.showCalendar = function (id) {            
        if (edb.hasOwnProperty(id) && !vm.shownCalendars.includes(id)) {
            for (var i = 0; i < edb[id]["events"].length; i++) {
                var event;
                event = edb[id]["events"][i];
                event.calendar = id;
                event.startsAt = new Date(event.startsAt);
                event.endsAt = new Date(event.endsAt);
                event.actions = actions;
                event.draggable = false;
                event.resizable = false;
                
                vm.events.push(event);
            }
            vm.shownCalendars.push(id);
        }
    };
    
    vm.hideCalendar = function (id) {
        if (vm.shownCalendars.includes(id)) {
            var i = vm.events.length;
            while (i--) {
                if (vm.events[i].calendar == id) {
                    vm.events.splice(i, 1);
                }
            }
            
            var index = vm.shownCalendars.indexOf(id);
            if (index !== -1) {
                vm.shownCalendars.splice(index, 1);
            }
        }
    };
    
    vm.toggleCalendar = function (id) {
        if (vm.checkedCalendars[id]) {
            vm.showCalendar(id);
        } else {
            vm.hideCalendar(id);
        }
    };

    vm.cellIsOpen = true;

    vm.eventClicked = function(event) {
        // TODO
    };

    vm.eventEdited = function(event) {
        // TODO
    };

    vm.eventDeleted = function(event) {
        // TODO
    };

    vm.eventTimesChanged = function(event) {
        // TODO
    };

    vm.toggle = function($event, field, event) {
        $event.preventDefault();
        $event.stopPropagation();
        event[field] = !event[field];
    };

    vm.timespanClicked = function(date, cell) { 
        if (vm.calendarView === 'month') {
            if ((vm.cellIsOpen && moment(date).startOf('day').isSame(moment(vm.viewDate).startOf('day'))) || cell.events.length === 0 || !cell.inMonth) {
                vm.cellIsOpen = false;
            } else {
                vm.cellIsOpen = true;
                vm.viewDate = date;
            }
        } else if (vm.calendarView === 'year') {
            if ((vm.cellIsOpen && moment(date).startOf('month').isSame(moment(vm.viewDate).startOf('month'))) || cell.events.length === 0) {
                vm.cellIsOpen = false;
            } else {
                vm.cellIsOpen = true;
                vm.viewDate = date;
            }
        }    
    };
});