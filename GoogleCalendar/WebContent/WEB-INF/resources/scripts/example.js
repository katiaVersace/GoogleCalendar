var edb = {};

angular.module('mwl.calendar.docs', ['mwl.calendar', 'ngAnimate', 'ui.bootstrap', 'colorpicker.module']);
angular
  .module('mwl.calendar.docs')
  .controller('KitchenSinkCtrl', function(moment, calendarConfig) {     
      
    // AngularJS Controller
    var vm = this;
    
    // Default view
    vm.calendarView = 'month';
    
    // Current date
    vm.viewDate = new Date();

    // List of glyphs shown after entries in a day's event list
    var actions = [{
        label: '<i class=\'glyphicon glyphicon-remove\'></i>',
        onClick: function(args) {
            vm.deleteEvent(args.calendarEvent);
      },
    }];

    // Events to be displayed
    vm.events = [];
    
    // Add an event to a calendar
    // TODO: rewrite entirely
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
    
    // Delete an event from a calendar
    // TODO: no DB update atm
    vm.deleteEvent = function (event) {
        vm.events = vm.events.filter(function (item) {
            return item.id != event.id;
        });
        
        edb[event.calendar].events = edb[event.calendar].events.filter(function (item) {
            return item.id != event.id;
        });
    };
    
    // Hides a calendar (graphically), then updates the edb
    vm.deleteEventsByCalendar = function (id) {
        vm.hideCalendar(id);
        delete edb[id];
    };
    
    // Calendars currently shown, by id
    vm.shownCalendars = [];
    
    // Calendars currently checked on the sidebar, by id
    // (only used in conjunction with shownCalendars, 
    // there may be a better way to do this)
    vm.checkedCalendars = [];
    
    // Makes a calendar's events visible
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
    
    vm.createCalendar = function (userId, title, description) {
        /* DB asks for: user_id, title, description
         * 
         * 1. ajax call to db - retrieve calendar's id
         * 2. add calendar to edb
         * 3. add calendar's title to sidebar 
         */
        $.ajax({
            type: "POST",
            url: "insertNewCalendar/" + userId,
            success: function (result) {
                if (result != -1) {
                    // add new calendar to edb
                    edb[result] = {
                        events: [],
                    }
                    
                    // add new calendar to sidebar
                    var entry = 
                        "<li>\n"
                      + "  <label id=\"cal_entry_" + result + "\">\n"
                      + "    <input\n"
                      + "      type=\"checkbox\"\n"
                      + "      name=\"" + result + "\"\n"
                      + "      value=\"" + title + "\"\n"
                      + "      ng-model=\"vm.checkedCalendars['" + result + "']\"\n"
                      + "      ng-change=\"vm.toggleCalendar('" + result + "')\"\n/>"
                      + "     " + result + "\n"
                      + "  </label>\n"
                      + "</li>\n";
                      
                    $("#calendarsList").append(entry);
                    
                    // DEBUG
                    console.log(JSON.stringify(edb, null, 4));
                    // END DEBUG
                } else {
                    // TODO: server returned -1, handle troubles
                }
            },
            error: function (result) {
                // TODO
            }
        });
    }
    
    vm.deleteCalendar = function (id) {
        // Delete calendar in DB
        $.ajax({
            type: "POST",
            url: "delete/" + id,
            success: function (result) {
                console.log("vm.deleteCalendar: " + result);
            },
            error: function (result) {
                console.log("ERROR ERROR ERROR ERROR ERROR");
            },
        });
        
        // Client-side deletion (vm.events is updated accordingly)
        vm.deleteEventsByCalendar(id);
        
        // Calendar's entry in sidebar is deleted
        $("#cal_entry_" + id).remove();
    };

    // ---------------------------- //
    // --        WASTELAND       -- //
    // -- enter at your own risk -- //
    // ---------------------------- //
    
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