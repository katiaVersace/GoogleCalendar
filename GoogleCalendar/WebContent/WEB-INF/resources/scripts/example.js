var edb = {};

var imported = document.createElement('script');
imported.src = 'resources/scripts/openModal.js';
document.head.appendChild(imported);


angular.module('mwl.calendar.docs', ['mwl.calendar', 'ngAnimate', 'ui.bootstrap', 'colorpicker.module']);

angular.module('mwl.calendar.docs').controller('KitchenSinkCtrl', function(moment, calendarConfig) {     
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
    
    vm.temp ;
    
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
    
    // --------
    // - TEST -
    // --------
    
    vm.deleteCalendar = function () {
        var id = "2";
        
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
    };

    vm.cellIsOpen = true;

    
    


    
    vm.eventClicked = function(event) {
       
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

    
    vm.rangeSelected = function(startDate, endDate) {
    
        vm.firstDateClicked = startDate;
        vm.lastDateClicked = endDate;
      };
    
      
   
    vm.timespanClicked = function(date, cell) { 
    	 
//        alert(date);
    	vm.temp = {
    		   title: 'New event',
               startsAt: date,
               endsAt: date,
               color: calendarConfig.colorTypes.important,
               draggable: false,
               resizable: false,
               actions: actions
        			};
    	
        	vm.events.push(vm.temp);
    	
    		 modal(5);
    	
    	
    	    if (vm.calendarView === 'month' ) {
    	    	vm.lastDateClicked = date;
    	    	//modal(5);
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
    
    
    vm.fn = function() {
    	
    		alert("coap");
	
//    		vm.temp = [];
    		
    		//contenuto del modale evento
//    	    var id = document.querySelector('input[name = "rr"]:checked').value;
//    	    var title = document.getElementById('titl').value;
//    	    var colP = document.getElementById('colP').value;
//    	    var colS = document.getElementById('colS').value; 	 
//    	    var dataStart = document.getElementById('dataStart').value;
//    	    var timeStart = document.getElementById('timeStart').value ;
//    	    var dataEnd = document.getElementById('dataEnd').value;
//    	    var timeEnd = document.getElementById('timeEnd').value;
//    	    
//    	    
//    	    
//    		// chiusura modale 
//    	    document.getElementById('modal-wrapper5').style.display='none';
 	    	
    	
	}
    
    

});