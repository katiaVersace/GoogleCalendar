// --------------------------- //
// -- MERGE MARIO MARCO [0] -- //
// --------------------------- //

var imported = document.createElement('script');
imported.src = 'resources/scripts/openModal.js';
document.head.appendChild(imported);
var imported2 = document.createElement('script');
imported2.src = 'resources/scripts/dropDownMenu.js';
document.head.appendChild(imported2);

hoverCell = function(date, date1, cell) {
    if (cell.date >= date && cell.date <= date1) {
        cell.cssClass = 'clicked-cell';
    }
};

// --------------------------//
// -- ANGULARJS CONTROLLER --//
// --------------------------//

angular.module('mwl.calendar.docs', ['mwl.calendar', 'ngAnimate', 'ui.bootstrap', 'colorpicker.module']);
angular
  .module('mwl.calendar.docs')
  .controller('KitchenSinkCtrl', function($compile, $scope, moment) {     
      
    // Controller
    var vm = this;
        
    // ---------- //
    // -- DATA -- //
    // ---------- //
    
    // Default view
    vm.calendarView = 'month';
    
    // Current date
    vm.viewDate = new Date();

    // Events to be displayed
    vm.events = [];
    
    // Calendars currently associated with the user
    vm.calendarsArray = [];

    // Calendars whose events are currently shown, by id
    vm.shownCalendars = [];
    
    // Calendars currently checked on the sidebar, by id
    // Only used in conjunction with shownCalendars
    vm.checkedCalendars = [];
    
    // Received notifications buffer (move in session)
    vm.notifications = [];
    
    // Received invitations buffer (move in session)
    vm.invitations = [];
    
    // Cell state, used by the view
    vm.cellIsOpen = true;
    
    // List of glyphs shown after entries in a day's event list,
    // with behavior
    var actions = [ {
        label : '<i class=\'glyphicon glyphicon-pencil\'></i>',
        onClick : function(args) {
            vm.clickUpdateEvent(args.calendarEvent);
        }
    }, {
        label : '<i class=\'glyphicon glyphicon-remove\'></i>',
        onClick : function(args) {
            alert("delete");
            alert(args.calendarEvent.id);
            vm.deleteOccurrence(args.calendarEvent.id);
        },
    } ];
        
    // --------------------------- //
    // -- MERGE MARIO MARCO [1] -- //
    // --------------------------- //
    
    vm.vtrCell = [];
    // event for modal
    vm.temp = undefined;
    
    // clicked cell
    vm.lastCellClicked = undefined;
   
    // temp event to update
    vm.tmpMemo = undefined;
    vm.memo = undefined;
    
    // --------------- //
    // -- UTILITIES -- //
    // --------------- //
    
    // Event Constructor
    vm.Event = function (id, calendar, title, description, 
            startsAt, endsAt, primaryColor, secondaryColor) {
        this.id = id;
        this.calendar = calendar;
        this.title = title;
        this.description = description;
        this.startsAt = startsAt;
        this.endsAt = endsAt;
        this.color = {
            primary: primaryColor,
            secondary: secondaryColor,
        };
        this.draggable = false;
        this.resizable = false;
        this.memo = false;
        this.actions = actions;
    };
    
    // Memo Constructor (deceives vm.events into thinking it's a regular event)
    vm.Memo = function (id, title, description, color, dateAdded) {
        var now = new Date();
        
        this.id = id;
        this.calendar = undefined; // FIXME: insert memo calendar (or whatever)
        this.title = title;
        this.description = description;
        this.startsAt = now; // graphical purposes only (renders it on the current day)
        this.endsAt = now; // graphical purposes only (renders it on the current day)
        this.dateAdded = now; // date this memo was added/saved on db
        this.color = {
            primary: color,
            secondary: color,
        };
        this.draggable = false;
        this.resizable = false;
        this.memo = true;
        this.actions = actions;
    };
    
    // 
    vm.getViewDateBoundaries = function () {        
        return {
            start: moment(vm.viewDate).startOf(vm.calendarView).toDate(),
            end: moment(vm.viewDate).endOf(vm.calendarView).toDate(),
        };
    };
    
    // To be called before rendering received messages
    vm.arrangeMessages = function () {
        // BEWARE: shallow copies, remember in case of troubles
        var messages = vm.notifications.concat(vm.invitations);
        
        messages.sort(function (first, second) {
            var ts_first = moment(first.timestamp);
            var ts_second = moment(second.timestamp);
            
            if (ts_first < ts_second) {
                return -1;
            }
            if (ts_first > ts_second) {
                return 1;
            }            
            return 0;
        });
        
        return messages;
    };
    
    // Delete an answered invitation from buffer
    vm.discardInvitation = function (id) {
        vm.invitations.filter(function (item) {
            return item.id != id;
        });
    };
    
    // ------------------- //
    // -- VIEW HANDLING -- //
    // ------------------- //
    
    // Repopulate vm.events accordingly to the data fetched from the DB
    vm.updateEventList = function () {
        vm.events = [];

        var boundaries = vm.getViewDateBoundaries();

        vm.shownCalendars.forEach(function (calendar_id) {
            vm.JSON_getMyEventsInPeriod(calendar_id, boundaries.start, boundaries.end, function (events) {
                JSON.parse(events).forEach(function (blueprint) {
                    vm.events.push(new vm.Event(
                        blueprint.id,
                        blueprint.calendar.id,
                        blueprint.title,
                        blueprint.description,
                        new Date(blueprint.startTime),
                        new Date(blueprint.endTime),
                        blueprint.primaryColor,
                        blueprint.secondaryColor
                    ));
                });
                // Needed for asynchronous update of vm.events
                $scope.$digest();
            });
        });
    };
    
    // Update the list of calendars displayed within the sidebar
    vm.updateCalendarList = function () {
        var viewList = $("#calendarsList");
        vm.calendarsArray = [];
        vm.JSON_getAllMyCalendars(function (calendars) {
            viewList.empty();
            JSON.parse(calendars).forEach(function (calendar) {
                vm.calendarsArray.push(calendar);
                var x = calendar.title;
                var title = x.replace(/'/g,"\\'");;
                viewList.append(
                     $compile(
                          "<li id=\"cal_entry_" + calendar.id + "\">\n"
                        + "  <label>\n"
                        + "    <input\n"
                        + "      type=\"checkbox\"\n"
                        + "      id= \"" + calendar.id + "\"\n"
                        + "      name=\"" + calendar.id + "\"\n"
                        + "      value=\"" + calendar.title + "\"\n"
                        + "      ng-model=\"vm.checkedCalendars['" + calendar.id + "']\"\n"
                        + "      ng-change=\"vm.toggleCalendar('" + calendar.id + "')\"/>\n"
                        + "    <label for=\"" + calendar.id + "\"><span></span>" + calendar.title + "</label>\n"
                        + " </label>\n"
                        + " <label>\n" 
                        + "      <i\n"
                        + "        class=\"glyphicon glyphicon-cog\"\n"
                        + "        ng-click=\"vm.updateCalendarView('"+title+"','"+calendar.id+"')\"\n"
                        + "        style=\"margin-left: 80%;\">\n"
                        + "      </i>\n"
                        + "    </label>\n"
                        + "</li>\n"
                     )($scope)
                );
            });
            vm.updateCalendarListModal();   
        });
     };
    
    
    
    // Update the list of calendars displayed within the Modal
    vm.updateCalendarListModal = function () {
            var viewList = $("#calendarsListModal");
            
            viewList.empty();
            var string =''; 
                string = "<div  class=\"btn-group\">\n"
                +"<button type=\"button\" class=\"btn btn-primary dropdown-toggle\"" 
                +  "data-toggle=\"dropdown\" aria-haspopup=\"true\" aria-expanded=\"false\">"
                +"<i style=\"font-size: 25px; color: white;\"" 
                +  "class=\"glyphicon glyphicon-list-alt\"></i> <span class=\"caret\"></span>"
                +"</button>"
                +"<h4 id=\"ChoiceCalendar\" style=\"color: white; padding-left:120px;\"></h4>"
                +"<ul class=\"dropdown-menu\">";
            
            for(i = 0;i< vm.calendarsArray.length;i++) {
                
                var title = vm.calendarsArray[i].title;
                var id = vm.calendarsArray[i].id;
                
                var x = title;
                title = x.replace(/'/g,"\\'");
                alert(title);
             
                
                string+="<li><a href=\"javascript:void(0)\" onclick=\"setCalendar('"+title+"','"+id+"')\"" +
                    " class = \"calendars\" data-id=\"" + id+ "\">" +vm.calendarsArray[i].title+"</a></li>";
                }
            viewList.append(string);        
    };
    

    
    vm.insertNewCalendarView = function () {
   
       var title = document.getElementById("nameCal").value;
       var description = document.getElementById("descrCalendar").value;
       vm.insertNewCalendar(title, description);
       document.getElementById('modal-wrapper1').style.display = 'none';
       document.getElementById("nameCal").value = '';
       document.getElementById("descrCalendar").value = ''
    };

    
    vm.updateCalendarView = function (a,b) {
            alert(a);
            alert(b);
            // TO DO open modal update Calendar
    };
    
    
    vm.updateUserInformation = function() {
        
        var name = document.getElementById("nameUser").value;
        var oldP = document.getElementById("oldP").value;
        var newP = document.getElementById("newP").value;
        
        // TO DO controllo username empty string
        document.getElementById('usernameHome').innerHTML = name;
        
        vm.updateUser(name,newP);
        
        document.getElementById('modal-wrapper2').style.display = 'none';
        
        
    };
        
    // Hide/Show a calendar's events
    vm.toggleCalendar = function (id) {
        if (vm.checkedCalendars[id]) {
            vm.shownCalendars.push(id);
        } else {
            vm.shownCalendars = vm.shownCalendars.filter(function (element) {
                return element != id;
            });
        }
        vm.updateEventList();
    };
    
    // Update event list when
    vm.viewModifierBehavior = function () {
        vm.cellIsOpen = false;
        vm.updateEventList();
    }
    
    // -------------------------- //
    // -- DATABASE INTERACTION -- //
    // -------------------------- //
    
    /*
     * JSON_getMyEventsInPeriod    
     */
    vm.JSON_getMyEventsInPeriod = function (calendar_id, start, end, callback) {
        $.ajax({
            type: "POST",
            url: "JSON_getMyEventsInPeriod/" + calendar_id,
            data: {
                start: moment(start).format("YYYY-MM-DD HH:mm:ss"),
                end: moment(end).format("YYYY-MM-DD HH:mm:ss"),
            },
            success: function (response) {
                callback(response);
            },
        });
    };
    
    /*
     * JSON_getAllMyCalendars
     */
    vm.JSON_getAllMyCalendars = function (callback) {
        $.ajax({
            type: "POST",
            url: "JSON_getAllMyCalendars",
            success: function (response) {
                callback(response);
            },
        });
    };
    
    /*
     * JSON_getMyAlarms
     * TODO: test me
     */
    vm.JSON_getMyAlarms = function (callback) {
        $.ajax({
            type: "POST",
            url: "JSON_getMyAlarms",
            success: function (response) {
                callback(response);
            },
        });  
    };
    
    /*
     * JSON_getAlarmForAnOccurrence
     * TODO: test me
     */
    vm.JSON_getAlarmForAnOccurrence = function (occurrence_id, callback) {
        $.ajax({
            type: "POST",
            url: "JSON_getAlarmForAnOccurrence/" + occurrence_id,
            success: function (response) {
                callback(resposne);
            },
        });
    };
    
    /*
     * JSON_getMyNotifications
     * TODO: test me
     */
    vm.JSON_getMyNotifications = function (callback) {
        $.ajax({
            type: "POST",
            url: "JSON_getMyNotifications",
            success: function (response) {
                callback(response);
            },
        });
    };
    
    /*
     * JSON_searchEmailInDb
     */
    vm.JSON_searchEmailInDb = function (email,callback) {
        $.ajax({
            type: "POST",
            url: "JSON_searchEmailInDb",
            data: {
               email:email,
            },
            success: function (response) {
            	   callback(response);                
            },
        });
    };
    
    /*
     * JSON_getMyInvitations
     * TODO: test me
     */
    vm.JSON_getMyInvitations = function (callback) {
        $.ajax({
            type: "POST",
            url: "JSON_getMyInvitations",
            success: function (response) {
                callback(response);
            },
        });
    };
    
    /*
     * insertNewEvent
     */
    vm.insertNewEvent = function (calendar_id, title, description, 
            startsAt, endsAt, primaryColor, secondaryColor) {
        $.ajax({
            type: "POST",
            url: "insertNewEvent/" + calendar_id,
            data: {
                title: title,
                description: description,
                startTime: startsAt,
                endTime: endsAt,
                c1: primaryColor,
                c2: secondaryColor,
            },
            success: function (response) {
                if (response != -1) {
                    vm.updateEventList();
                }
            },
        });
    };
    
    /*
     * updateEvent
     */
    vm.updateEvent = function (id, title, description, 
            startsAt, endsAt, primaryColor, secondaryColor) {
        $.ajax({
            type: "POST",
            url: "updateEvent/" + id,
            data: {
                title: title,
                description: description,
                startTime: startsAt,
                endTime: endsAt,
                c1: primaryColor,
                c2: secondaryColor,
            },
            success: function (response) {
                if (response == "YES") {
                    vm.updateEventList();
                }
            },
        });
    };
    
    /*
     * deleteOccurenceId
     */
    vm.deleteOccurrence = function (id) {
        $.ajax({
            type: "POST",
            url: "deleteOccurrence/" + id,
            success: function (response) {
                if (response == "YES") {
                    vm.updateEventList();
                }
            },
        });
    };
    
    /*
     * insertNewMemo
     * TODO: test me
     */
    vm.insertNewMemo = function (title, description, date, color) {
        $.ajax({
            type: "POST",
            url: "insertNewMemo",
            data: {
                title: title,
                description: description,
                data: date,
                c1: color,
            },
            success: function () {
                // TODO
            },
        });
    };
    
    /*
     * updateMemo
     * TODO: test me
     */
    vm.updateMemo = function (memo_id, title, description, date, color) {
        $.ajax({
            type: "POST",
            url: "updateMemo/" + memo_id,
            data: {
                title: title,
                description: description,
                data: date,
                c1: color,
            },
            success: function () {
                // TODO
            },
        });
    };
    
    /*
     * deleteMemoById
     * TODO: test me
     */
    vm.deleteMemoById = function ( /* ... */ ) {
        $.ajax({
            type: "POST",
            url: "deleteMemo/" + memo_id,
            success: function () {
                // TODO
            },
        });
    };
    
    /*
     * insertNewCalendar
     */
    vm.insertNewCalendar = function (title, description) {
        $.ajax({
           type: "POST",
           url: "insertNewCalendar",
           data: {
               title: title,
               description, description,
           },
           success: function (response) {
               if (response != -1) {
                   vm.updateCalendarList();
               }
           },
        });
    };
    
    /*
     * updateCalendar
     */
    vm.updateCalendar = function (calendar_id, title, description) {
        $.ajax({
            type: "POST",
            url: "update/" + calendar_id,
            data: {
                title: title,
                description: description,
            },
            success: function (response) {
                if (response == "YES") {
                    vm.updateCalendarList();
                }
            },
        });
    };
    
    /*
     * disconnectFromCalendar
     */
    vm.disconnectFromCalendar = function (id) {
        $.ajax({
            type: "POST",
            url: "disconnect/" + id,
            success: function (response) {
                if (response == "YES") {
                    vm.updateCalendarList();
                    vm.updateEventList();
                }
            },
        });
    };
    
    /*
     * addAlarm
     * TODO: test me
     */
    vm.addAlarm = function (occurrence_id, minutes) {
        $.ajax({
            type: "POST",
            url: "addAlarm/" + occurrence_id,
            data: {
                minutes: minutes,
            },
            success: function (response) {
                // TODO
            },
        });
    };
    
    /*
     * updateAlarm
     */
    vm.updateAlarm = function (/* ... */) {
        // TODO
    }
    
    /*
     * deleteAlarm
     */
    vm.deleteAlarm = function (/* ... */) {
        // TODO
    }
    
    /*
     * updateUser
     */
    vm.updateUser = function (username, password) {
        $.ajax({
            type: "POST",
            url: "updateUser",
            data: {
                username: username,
                password: password,
            },
            success: function (response) {
                // FIXME: graphical representations of the username
                //        inside the page need to be updated. IndexController
                //        should expose a function for retrieving the current
                //        username, to be used at page initialization and after
                //        a successful call to vm.updateUser
            },
        });
    };
    
    /*
     * sendInvitation
     */
    vm.sendInvitation = function (calendar_id, email, privileges) {
        $.ajax({
            type: "POST",
            url: "sendInvitation/" + calendar_id,
            data: {
                receiver_email: email,
                privilege: privileges,
            },
            success: function (response) {
                if (response == "YES") {
                    // TODO
                }
            },
        });
    };
    
    vm.deleteNotifications = function () {
        $.ajax({
            type: "POST",
            url: "deleteNotifications",
            success: function (response) {
                if (response == "YES") {
                    vm.notifications = [];
                }
            },
        });
    };
    
    vm.answerInvitation = function (id, answer) {
        $.ajax({
            type: "POST",
            url: "answerNotification",
            data: {
                answer: answer ? "accept" : "decline",
            },
            success: function (response) {
                if (response == "accepted") {
                    vm.discardInvitation(id);
                    vm.updateCalendarList();
                } else if (response != "declined") {
                    console.log("vm.answerInvitation unsuccessful");
                    console.log(JSON.stringify(response, null, 4));
                }
            },
        });
    };
    
    // --------- //
    // -- SSE -- //
    // --------- //
    
    vm.SSENotificationSubscription = function () {
        var eventSource = new EventSource("notifications");
        
        eventSource.onmessage = function (event) {
            var received = JSON.parse(event.data);
            
            if (received.length) {
                vm.notifies = vm.notifies.concat(received.slice());
            }
        };
    };
    
    // FIXME: do this on IndexController
    vm.SSEInvitationSubscription = function () {
        var eventSource = new EventSource("invitations");
        
        eventSource.onmessage = function (event) {
            var received = JSON.parse(event.data);
            
            if (received.length) {
                vm.invitations = vm.invitations.concat(received.slice());
            }
        };
    };
    
    // ---------- //
    // -- INIT -- //
    // ---------- //
    
    (function () {
        vm.updateCalendarList();
        vm.SSENotificationSubscription();
        // FIXME: write interface on IndexController first
        // vm.SSEInvitationSubscription();
    })();
    
    // --------------------------- //
    // -- MERGE MARIO MARCO [2] -- //
    // --------------------------- //

    vm.clickUpdateEvent = function(event) {
        if (!event.memo) {
            vm.temp = {
                title : event.title,
                id : event.id,
                memo : false,
                description : event.description,
                clock : event.clock, // TODO: fix clock on DB
                startsAt : event.startsAt,
                endsAt : event.endsAt,
                color : {
                    primary : event.color.primary,
                    secondary : event.color.secondary,
                },
                draggable : false,
                resizable : false,
                actions : actions
            };

            updateClock(vm.temp.clock);
            // document.getElementById("TourId").value =
            // vm.temp.clock;
            modal(6);
        } else {
            var str = event.title;
            var res = str.slice(102);
            vm.memo = {
                title : res,
                id : event.id,
                memo : true,
                description : event.description,
                startsAt : moment(),
                color : {
                    primary : event.color.primary,
                },
                draggable : false,
                resizable : false,
                actions : actions
            };
            vm.tmpMemo = event;
            modal(8);
        }
    };
    
    vm.rangeSelected = function(startDate, endDate) {

        if (vm.lastCellClicked != undefined) {
            vm.lastCellClicked.cssClass = '.clear-cell';
        }

        vm.firstDateClicked = startDate;
        vm.lastDateClicked = endDate;
        vm.temp = {
            title : 'New event',
            id : '',
            memo : false,
            description : '',
            clock : 'none',
            startsAt : startDate,
            endsAt : endDate,
            color : {
                primary : "#123456",
                secondary : "#123458"
            },
            draggable : false,
            resizable : false,
            actions : actions
        };
        vm.openEventModal();

    };

    vm.timespanClicked = function(date, cell) {
        vm.firstDateClicked = date;
        vm.lastDateClicked = date;

        if (vm.lastCellClicked != undefined) {
            vm.lastCellClicked.cssClass = '.clear-cell';
        }

        hoverCell(vm.firstDateClicked, vm.lastDateClicked, cell);
        vm.lastCellClicked = cell;

        vm.temp = {
            id : '',
            title : 'New event',
            description : '',
            memo : false,
            clock : 'none',
            startsAt : vm.firstDateClicked,
            endsAt : vm.lastDateClicked,
            color : {
                primary : "#123456",
                secondary : "#123456",
            },
            draggable : false,
            resizable : false,
            actions : actions
        };
        document.getElementById('btn-add').disabled = false;

        if (vm.calendarView === 'month') {
            if ((vm.cellIsOpen && moment(date).startOf('day')
                    .isSame(moment(vm.viewDate).startOf('day')))
                    || cell.events.length === 0
                    || !cell.inMonth) {
                vm.cellIsOpen = false;
            } else {
                vm.cellIsOpen = true;
                vm.viewDate = date;
            }
        } else if (vm.calendarView === 'year') {
            if ((vm.cellIsOpen && moment(date).startOf('month')
                    .isSame(
                            moment(vm.viewDate)
                                    .startOf('month')))
                    || cell.events.length === 0) {
                vm.cellIsOpen = false;
            } else {
                vm.cellIsOpen = true;
                vm.viewDate = date;
            }
        }
    };
    
    vm.cellModifier = function(cell) {
        vm.vtrCell.push(cell);
        console.log(vm.vtrCell.length);
        console.log(vm.vtrCell[length - 1]);
    };

    // MANAGE EVENT
    vm.openEventModal = function() {
        
        modal(5);
    };

    // MANAGE EVENT
    vm.openEventModal = function() {
        modal(5);
    };


    // add event press button action
    vm.addEventView = function() {

        var idCalendar = document.getElementById("choiceId").value;

        // ///////////// TO DO /////////////////// ADD ATTR in
        // INSERTNEWEVENT
        vm.temp.clock = document.getElementById("TourId").value;

        console.log("event inserted => " + idCalendar + " "
                + vm.temp.title + " " + vm.temp.description
                + " " + vm.temp.startsAt + " " + vm.temp.endsAt
                + " " + vm.temp.color.primary + " "
                + vm.temp.color.secondary);

        if (idCalendar != undefined) {
            vm.insertNewEvent(idCalendar, vm.temp.title,
                    vm.temp.description, vm.temp.startsAt,
                    vm.temp.endsAt, vm.temp.color.primary,
                    vm.temp.color.secondary);

            document.getElementById('btn-add').disabled = true;

            resetClock();
            document.getElementById('modal-wrapper5').style.display = 'none';

        } else {
            alert("please choose a calendar");
        }
    }

    // update event press button action
    vm.updateEventView = function() {

        var idCalendar = document.getElementById("choiceId").value;

        vm.updateEvent(vm.temp.id, vm.temp.title,
                vm.temp.description, vm.temp.startsAt,
                vm.temp.endsAt, vm.temp.color.primary,
                vm.temp.color.secondary);

        resetClock();
        document.getElementById('modal-wrapper6').style.display = 'none';

    }

    // MANAGE MEMO
    vm.openMemoModal = function() {
        modal(7);

        vm.memo = {
            title : 'New Memo',
            id : '',
            startsAt : moment(),
            color : {
                primary : "#123456",
            },
            draggable : false,
            resizable : false,
            memo : true,
            actions : actions
        };

    };

    // add memo press button action
    vm.addMemoView = function() {

        // use tmp.memo variables

        vm.memo.title = '<i class="glyphicon glyphicon-tag" style=" color: #42A5F5; font-size: 20px; margin-right: 10px; "></i>'
                + vm.memo.title;
        // TO DO
        // vm.insertNewMemo(....);

        document.getElementById('modal-wrapper7').style.display = 'none';

    }

    // update memo press button action
    vm.updateMemoView = function() {

        // use tmp.memo variables

        vm.memo.title = '<i class="glyphicon glyphicon-tag" style=" color: #42A5F5; font-size: 20px; margin-right: 10px; "></i>'
                + vm.memo.title;
        // TO DO
        // vm.updateMemo(...);

        document.getElementById('modal-wrapper8').style.display = 'none';
    }
    
    // ---------------------------- //
    // --       WASTELAND        -- //
    // -- enter at your own risk -- //
    // ---------------------------- //
    
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

    // ----------- //
    // -- DEBUG -- //
    // ----------- //
    
    vm.fn = function () { };
});
