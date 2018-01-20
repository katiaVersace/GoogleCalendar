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
    
           
    // Calendars whose events are currently shown, by id
    vm.shownCalendars = [];
    
    // Calendars currently checked on the sidebar, by id
    // Only used in conjunction with shownCalendars
    vm.checkedCalendars = [];
    
    vm.calendarsArray = [];
    
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
        this.startsAt = now; // graphical purposes only (renders it on the
								// current day)
        this.endsAt = now; // graphical purposes only (renders it on the
							// current day)
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
    
    vm.getViewDateBoundaries = function () {        
        return {
            start: moment(vm.viewDate).startOf(vm.calendarView).toDate(),
            end: moment(vm.viewDate).endOf(vm.calendarView).toDate(),
        };
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
// console.log(JSON.stringify(blueprint, 4, null));
//                	
// alert(blueprint.)
                    vm.events.push(new vm.Event(
                        blueprint.id,
                        blueprint.calendar.id,
                        blueprint.title,
                        blueprint.description,
                        new Date(blueprint.startTime),
                        new Date(blueprint.endTime),
                        blueprint.primaryColor, // FIXME: substitute with
                                    // blueprint.primaryColor,
                        blueprint.secondaryColor // FIXME: substitute with
                                    // blueprint.secondaryColor,
                    ));
                });
                // Needed for asynchronous update of vm.events
                $scope.$digest();
            });
        });
    };
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    ///////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////
    ///////// 	  SIMULA NOTIFICHE          ///////////////////////
    ///////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////
    
    vm.cazzata = [];
    
    vm.notifica = {
            id : "notifica",
            contenuto : "ciccio ha accettato" 
            };
    vm.notifica2 = {
            id : "notifica2",
            contenuto : "fabio ha trovato il main" };
    vm.notifica3 = {
            id : "notifica3",
            contenuto : "invito a calendario blabla",
            	senderID: "blabla"};
     
    ///////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////
   
    
    
    
    
    
    
    
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
          //  vm.updateCalendarListModal();   
        });
     };
    
     ///////////////////////////////////////////////////////////////
     ///////////////////////////////////////////////////////////////
     ///// 	  INSERT NOTIFICHE IN DROPDOWN         /////////////////
     ///////////////////////////////////////////////////////////////
     ///////////////////////////////////////////////////////////////
     
    //add notifications to dropdown menu
    vm.updateNotifications = function () {
            var viewList = $("#ulNotifications");
            
            alert("ciao");

            
            viewList.empty();
            var string =''; 
 
  
            for(i = vm.cazzata.length-1; i>=0;i--) {
                
                var id = vm.cazzata[i].id;
                var content = vm.cazzata[i].contenuto;
            
                var x = content;
                content = x.replace(/'/g,"\\'");
         
                string+="<li><a href=\"javascript:void(0)\" class = \"calendars\" data-id=\"" + id+ "\">" +content+"</a></li>";
                
                if(!vm.cazzata[i].hasOwnProperty('senderID')) {
                	alert("pop "+content);
                         vm.cazzata.splice(i, 1);
                	
                }
  
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
	 * JSON_getMyAlarms TODO: test me
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
	 * JSON_getAlarmForAnOccurrence TODO: test me
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
	 * JSON_getMyNotifications TODO: test me
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
	 * JSON_getMyInvitations TODO: test me
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
    vm.deleteOccurrence = function (id) {  // done
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
	 * insertNewMemo TODO: test me
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
	 * updateMemo TODO: test me
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
	 * deleteMemoById TODO: test me
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
	 * addAlarm TODO: test me
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
            	
            	if(response.equals("User update successfully") || response.equals("Username changed successfully"))
                    document.getElementById('usernameHome').innerHTML = name;

            	alert(response)
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
                if (reponse == "YES") {
                    // TODO
                }
            },
        });
    };
    
    // ---------- //
    // -- INIT -- //
    // ---------- //
    
    (function () {
        vm.updateCalendarList();        
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

    ///////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////
    //////////     PUSH NOTIFICHE DAL DB         //////////////////
    ///////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////
    
    
    // MANAGE EVENT
    vm.openEventModal = function() {
        modal(5);
    	alert("arrivate le notifiche");
        vm.cazzata.push(vm.notifica);
        vm.cazzata.push(vm.notifica2);
        vm.cazzata.push(vm.notifica3);
        modal(5);
    };

    ///////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////
    

    // add event press button action
    vm.addEventView = function() {
    	


        var idCalendar = document.getElementById("choiceId").value;

        // ///////////// TO DO /////////////////// ADD ATTR in
        // INSERTNEWEVENT
        vm.temp.clock = document.getElementById("TourId").value;

        console.log("event inserted => " + idCalendar + " "
                + vm.temp.title + " ------------------->>>>>>>> DSCR " + vm.temp.description
                + " ----------END DSCR " + vm.temp.startsAt + " " + vm.temp.endsAt
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
    // -- WASTELAND -- //
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
