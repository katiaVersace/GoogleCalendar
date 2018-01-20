<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=utf8"
  pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>

<html data-ng-app="mwl.calendar.docs">
<head>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="IE=edge">


<script type="text/javascript" src="resources/opensource/angular.min.js"></script>


<script src="https://unpkg.com/moment@2.17.1"></script>
<script src="https://unpkg.com/interactjs@1"></script>
<script src="https://unpkg.com/angular@1.6.6/angular.js"></script>
<script src="https://unpkg.com/angular-animate@1.6.6/angular-animate.js"></script>
<script src="https://unpkg.com/angular-ui-bootstrap@2/dist/ui-bootstrap-tpls.js"></script>
<script src="https://unpkg.com/rrule@2"></script>
<script src="https://unpkg.com/angular-bootstrap-colorpicker@3"></script>
<script src="https://unpkg.com/angular-bootstrap-calendar"></script>
<link href="https://unpkg.com/bootstrap@3/dist/css/bootstrap.css"
  rel="stylesheet">
<link
  href="https://unpkg.com/angular-bootstrap-colorpicker@3/css/colorpicker.min.css"
  rel="stylesheet">
<link
  href="https://unpkg.com/angular-bootstrap-calendar/dist/css/angular-bootstrap-calendar.min.css"
  rel="stylesheet">

<script src="resources/scripts/example.js"></script>
<script src="resources/scripts/messages.js"></script>

<!-- Bootstrap CSS CDN -->

<link rel="stylesheet" type="text/css"
  href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">

<link rel="stylesheet" type="text/css" href="resources/css/sidebar.css">
<link rel="stylesheet" type="text/css" href="resources/css/popup.css">

</head>
<body data-ng-controller="KitchenSinkCtrl as vm">


  <div class="wrapper">
    <!-- Sidebar Holder -->
    <nav id="sidebar">
      <div class="sidebar-header">
        <h3>ASDE Calendar</h3>
        <strong>AS</strong>
      </div>


      <div class="col-sm-2" id="myCalendarsBox"></div>
      <ul class="list-unstyled components">
        <li class="active"><a href="#homeSubmenu" data-toggle="collapse"
          aria-expanded="false"> <i class="glyphicon glyphicon-calendar"></i>
            My Calendar
        </a>
          <ul class="collapse list-unstyled" id="homeSubmenu">
            <form action="">
              <fieldset id="calendarsList">
              </fieldset>
            </form>
          </ul></li>


        <li><a
          onclick="document.getElementById('modal-wrapper').style.display='block'">
            <i class="glyphicon glyphicon-plus"></i> Add New Calendar
        </a> <a href="#pageSubmenu" data-toggle="collapse" aria-expanded="false">
            <i class="glyphicon glyphicon-calendar"></i> Default Calendar
        </a>
          <ul class="collapse list-unstyled" id="pageSubmenu">
            <li><a href="#">Festività in Italia</a></li>
            <!--  <li><a href="#">Page 2</a></li>
                            <li><a href="#">Page 3</a></li> -->
          </ul></li>

        <li><a
          onclick="document.getElementById('modal-wrapper1').style.display='block'">
            <i class="glyphicon glyphicon-user"></i> Update profile
        </a></li>
        <li><a
          onclick="document.getElementById('modal-wrapper2').style.display='block'">
            <i class="glyphicon glyphicon-briefcase"></i> About us
        </a></li>
        <li></li>
      </ul>

    </nav>

    <!-- Page Content Holder -->
    <div id="content" style="width: 100%;">

      <nav class="navbar navbar-default">
        <div class="container-fluid">

          <div class="navbar-header">
            <button type="button" id="sidebarCollapse"
              class="btn btn-info navbar-btn" style="background: #3c92db;">
              <i class="glyphicon glyphicon-align-left"></i> <span>Toggle
                Sidebar</span>
            </button>
          </div>

          <div class="collapse navbar-collapse"
            id="bs-example-navbar-collapse-1">
            <ul class="nav navbar-nav navbar-right">
              <li><a href="#" style="color: #337ab7; font-size: 20px;">${username}</a></li>
              <li><a href="#"><i class="glyphicon glyphicon-bell"></i></a></li>
              <li>
                <form action="logout">
                  <div class="navbar-header">
                    <button type="submit" id="logout-btn"
                      class="btn btn-info navbar-btn"
                      style="background: #337ab7;">
                      <i class="glyphicon glyphicon-log-out"></i>
                    </button>
                  </div>
                </form>

              </li>
              <!-- <li><a href="#">Page</a></li>-->
            </ul>
          </div>
        </div>
      </nav>


      <div class="line"></div>

      <!-- CALENDAR -->
      <div class="container col-sm-12">
        <div class="row">
          <div>
            <div id="toRedraw">
              <h2 class="text-center">{{ vm.calendarTitle }}</h2>

              <div class="row">
                <div class="col-md-6 text-center">
                  <div class="btn-group">

                    <button class="btn btn-primary" mwl-date-modifier
                      date="vm.viewDate" decrement="vm.calendarView"
                      ng-click="vm.viewModifierBehavior()">Previous</button>
                    <button class="btn btn-default" mwl-date-modifier
                      date="vm.viewDate" set-to-today
                      ng-click="vm.viewModifierBehavior()">Today</button>
                    <button class="btn btn-primary" mwl-date-modifier
                      date="vm.viewDate" increment="vm.calendarView"
                      ng-click="vm.viewModifierBehavior()">Next</button>
                      
                    <!-- DEBUG BUTTON -->
                    <button
                      class="btn btn-primary"
                      ng-click='vm.fn()'>
                      DEBUG_1
                    </button>
                    <!-- /DEBUG BUTTON -->
                    
                  </div>
                </div>

                <br class="visible-xs visible-sm">

                <div class="col-md-6 text-center">
                  <div class="btn-group">
                    <label class="btn btn-primary" ng-model="vm.calendarView"
                      uib-btn-radio="'year'" ng-click="vm.viewModifierBehavior()">Year</label>
                    <label class="btn btn-primary" ng-model="vm.calendarView"
                      uib-btn-radio="'month'" ng-click="vm.viewModifierBehavior()">Month</label>
                    <label class="btn btn-primary" ng-model="vm.calendarView"
                      uib-btn-radio="'week'" ng-click="vm.viewModifierBehavior()">Week</label>
                    <label class="btn btn-primary" ng-model="vm.calendarView"
                      uib-btn-radio="'day'" ng-click="vm.viewModifierBehavior()">Day</label>
                  </div>
                </div>

              </div>

              <br>

              <mwl-calendar events="vm.events" view="vm.calendarView"
                view-title="vm.calendarTitle" view-date="vm.viewDate"
                on-event-click="vm.eventClicked(calendarEvent)"
                on-event-times-changed="vm.eventTimesChanged(calendarEvent); calendarEvent.startsAt = calendarNewEventStart; calendarEvent.endsAt = calendarNewEventEnd"
                cell-is-open="vm.cellIsOpen" day-view-start="06:00"
                day-view-end="22:59" day-view-split="30"
                cell-modifier="vm.modifyCell(calendarCell)"
                cell-auto-open-disabled="true"
                on-timespan-click="vm.timespanClicked(calendarDate, calendarCell)">


              </mwl-calendar>

              <br> <br> <br>

              <h3 id="event-editor">
                Edit events
                <button class="btn btn-primary pull-right"
                  ng-click="vm.addEvent()">Add new</button>
                <div class="clearfix"></div>
              </h3>

              <table class="table table-bordered">

                <thead>
                  <tr>
                    <th>Title</th>
                    <th>Primary color</th>
                    <th>Secondary color</th>
                    <th>Starts at</th>
                    <th>Ends at</th>
                    <th>Remove</th>
                  </tr>
                </thead>

                <tbody>
                  <tr ng-repeat="event in vm.events track by $index">
                    <td><input type="text" class="form-control"
                      ng-model="event.title"></td>
                    <td><input class="form-control" colorpicker type="text"
                      ng-model="event.color.primary"></td>
                    <td><input class="form-control" colorpicker type="text"
                      ng-model="event.color.secondary"></td>
                    <td>
                      <p class="input-group" style="max-width: 250px">
                        <input type="text" class="form-control" readonly
                          uib-datepicker-popup="dd MMMM yyyy"
                          ng-model="event.startsAt" is-open="event.startOpen"
                          close-text="Close"> <span
                          class="input-group-btn">
                          <button type="button" class="btn btn-default"
                            ng-click="vm.toggle($event, 'startOpen', event)">
                            <i class="glyphicon glyphicon-calendar"></i>
                          </button>
                        </span>
                      </p>
                      <div uib-timepicker ng-model="event.startsAt"
                        hour-step="1" minute-step="15" show-meridian="true"></div>
                    </td>
                    <td>
                      <p class="input-group" style="max-width: 250px">
                        <input type="text" class="form-control" readonly
                          uib-datepicker-popup="dd MMMM yyyy"
                          ng-model="event.endsAt" is-open="event.endOpen"
                          close-text="Close"> <span
                          class="input-group-btn">
                          <button type="button" class="btn btn-default"
                            ng-click="vm.toggle($event, 'endOpen', event)">
                            <i class="glyphicon glyphicon-calendar"></i>
                          </button>
                        </span>
                      </p>
                      <div uib-timepicker ng-model="event.endsAt" hour-step="1"
                        minute-step="15" show-meridian="true"></div>
                    </td>
                    <td>
                      <button class="btn btn-danger"
                        ng-click="vm.events.splice($index, 1)">Delete</button>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>

    </div>
  </div>

  <!-- MODAL PAGE  -->

  <!-- ADD NEW CALENDAR-->
  <div id="modal-wrapper" class="modal">
    <div>
      <form class="modal-content animate" action="/action_page.php">
        <div class="imgcontainer">
          <span
            onclick="document.getElementById('modal-wrapper').style.display='none'"
            class="close" title="Close PopUp">&times;</span>
          <h1 style="text-align: center; color: white; padding-top: 10px;">New
            Calendar</h1>
          <a href="#" class="avatar"><i
            style="font-size: 45px; color: white;"
            class="glyphicon glyphicon-calendar"></i></a>
        </div>

        <div class="modal-content">
          <input type="text" placeholder="Name" name="title">
          <input type="text" placeholder="Color" name="psw">
          <textarea rows="4" placeholder="Description" name="description"></textarea>
          <div style="text-align: center;">
            <button type="button" id="newCalendar"
              class="btn btn-info navbar-btn" style="background: #42A5F5">
              <span><strong>Create New Calendar</strong></span>
            </button>
          </div>
        </div>
      </form>
    </div>
  </div>

  <!-- UPDATE PROFILE-->
  <div id="modal-wrapper1" class="modal">
    <div>
      <form class="modal-content animate" action="/action_page.php">
        <div class="imgcontainer">
          <span
            onclick="document.getElementById('modal-wrapper1').style.display='none'"
            class="close" title="Close PopUp">&times;</span>
          <h1 style="text-align: center; color: white; padding-top: 10px;">Update
            Profile</h1>
          <a href="#" class="avatar"><i
            style="font-size: 45px; color: white;"
            class="glyphicon glyphicon-calendar"></i></a>
        </div>

        <div class="modal-content">
          <input type="text" placeholder="Name" name="uname"> <input
            type="password" placeholder="Old Password" name="uname"> <input
            type="password" placeholder="New Password" name="psw">
          <div style="text-align: center;">
            <button type="button" id="newCalendar1"
              class="btn btn-info navbar-btn" style="background: #42A5F5;">
              <span><strong>Update Your Profile</strong></span>
            </button>
          </div>
        </div>
      </form>
    </div>
  </div>

  <!-- ABOUT US-->
  <div id="modal-wrapper2" class="modal">
    <div>
      <form class="modal-content animate" action="/action_page.php">
        <div class="imgcontainer">
          <span
            onclick="document.getElementById('modal-wrapper2').style.display='none'"
            class="close" title="Close PopUp">&times;</span>
          <h1 style="text-align: center; color: white; padding-top: 10px;">Update
            Profile</h1>
          <a href="#" class="avatar"><i
            style="font-size: 45px; color: white;"
            class="glyphicon glyphicon-calendar"></i></a>
        </div>

        <div class="modal-content">
          <p style="color: white; text-align: center; font-size: 16px;">
            <strong>We are five Unical's students and we have developed
              this Web Application for ASDE exam.</strong>
          </p>
          <p></p>
          <p style="color: white; text-align: center;">Marco Amato</p>
          <p style="color: white; text-align: center;">Giuseppe Benvenuto</p>
          <p style="color: white; text-align: center;">Mario Carricato -
            187799</p>
          <p style="color: white; text-align: center;">Fabio Fabiano</p>
          <p style="color: white; text-align: center;">Caterina Versace</p>
        </div>
      </form>
    </div>
  </div>




  <!-- jQuery CDN -->
  <script src="https://code.jquery.com/jquery-1.12.0.min.js"></script>
  <!-- Bootstrap Js CDN -->
  <script
    src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
  <script type="text/javascript">
             $(document).ready(function () {
                 $('#sidebarCollapse').on('click', function () {
                     $('#sidebar').toggleClass('active');
                 });
             });
    </script>






  <!-- <script>

var modal = document.getElementById('modal-wrapper');
window.onclick = function(event) {
    if (event.target == modal) {
        modal.style.display = "none";
    }
}
</script> -->

</body>
</html>
