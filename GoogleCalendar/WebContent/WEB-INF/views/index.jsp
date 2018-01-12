<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=utf8"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>

<html data-ng-app="mwl.calendar.docs">
  <head>
  	<script type="text/javascript" src="resources/opensource/angular.min.js"></script>
  	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://unpkg.com/moment@2.17.1"></script>
    <script src="https://unpkg.com/interactjs@1"></script>
    <script src="https://unpkg.com/angular@1.6.6/angular.js"></script>
    <script src="https://unpkg.com/angular-animate@1.6.6/angular-animate.js"></script>
    <script src="https://unpkg.com/angular-ui-bootstrap@2/dist/ui-bootstrap-tpls.js"></script>
    <script src="https://unpkg.com/rrule@2"></script>
    <script src="https://unpkg.com/angular-bootstrap-colorpicker@3"></script>
    <script src="https://unpkg.com/angular-bootstrap-calendar"></script>
    <link href="https://unpkg.com/bootstrap@3/dist/css/bootstrap.css" rel="stylesheet">
    <link href="https://unpkg.com/angular-bootstrap-colorpicker@3/css/colorpicker.min.css" rel="stylesheet">
    <link href="https://unpkg.com/angular-bootstrap-calendar/dist/css/angular-bootstrap-calendar.min.css" rel="stylesheet">
    <script  src="resources/scripts/example.js"></script>
    <script src="resources/scripts/helpers.js"></script>
    <link rel="stylesheet" type="text/css" href="resources/css/index.css">
   
  
  </head>
  <body>
  
  <div class="barraNotifiche">
 <div class="contenutoBarraNotifiche">
 
 <div class="align-left" id="welcomeMessage">
  <h2 >Welcome <u>${username}</u></h1>
  </div>
  
   <div class="align-left" id="logoutButton">

	
	
	<a href="logout" >
          <span class="glyphicon glyphicon-log-out"></span>
        </a>
  </div>
  
  </div>
  </div>
  
  
  
  <div class="container">
  <div class="row">
 
    <div class="col-sm-2" id="myCalendarsBox">
      
      <form action="">
<fieldset>
 <legend>My Calendars</legend><br>
 
 <c:forEach items="${calendars}" var="el">
 <input type="checkbox" name="${el.id}" value="${el.title}" checked="checked"/> ${el.title}<br /> 
 
 </c:forEach>
 
</fieldset>
</form>
      
    </div>
    
    <div class=col-sm-10>
      <div data-ng-controller="KitchenSinkCtrl as vm">
  <h2 class="text-center">{{ vm.calendarTitle }}</h2>

  <div class="row">

    <div class="col-md-6 text-center">
      <div class="btn-group">

        <button
          class="btn btn-primary"
          mwl-date-modifier
          date="vm.viewDate"
          decrement="vm.calendarView"
          ng-click="vm.cellIsOpen = false">
          Previous
        </button>
        <button
          class="btn btn-default"
          mwl-date-modifier
          date="vm.viewDate"
          set-to-today
          ng-click="vm.cellIsOpen = false">
          Today
        </button>
        <button
          class="btn btn-primary"
          mwl-date-modifier
          date="vm.viewDate"
          increment="vm.calendarView"
          ng-click="vm.cellIsOpen = false">
          Next
        </button>
      </div>
    </div>

    <br class="visible-xs visible-sm">

    <div class="col-md-6 text-center">
      <div class="btn-group">
        <label class="btn btn-primary" ng-model="vm.calendarView" uib-btn-radio="'year'" ng-click="vm.cellIsOpen = false">Year</label>
        <label class="btn btn-primary" ng-model="vm.calendarView" uib-btn-radio="'month'" ng-click="vm.cellIsOpen = false">Month</label>
        <label class="btn btn-primary" ng-model="vm.calendarView" uib-btn-radio="'week'" ng-click="vm.cellIsOpen = false">Week</label>
        <label class="btn btn-primary" ng-model="vm.calendarView" uib-btn-radio="'day'" ng-click="vm.cellIsOpen = false">Day</label>
      </div>
    </div>

  </div>

  <br>

  <mwl-calendar
    events="vm.events"
    view="vm.calendarView"
    view-title="vm.calendarTitle"
    view-date="vm.viewDate"
    on-event-click="vm.eventClicked(calendarEvent)"
    on-event-times-changed="vm.eventTimesChanged(calendarEvent); calendarEvent.startsAt = calendarNewEventStart; calendarEvent.endsAt = calendarNewEventEnd"
    cell-is-open="vm.cellIsOpen"
    day-view-start="06:00"
    day-view-end="22:59"
    day-view-split="30"
    cell-modifier="vm.modifyCell(calendarCell)"
    cell-auto-open-disabled="true"
    on-timespan-click="vm.timespanClicked(calendarDate, calendarCell)">
  </mwl-calendar>

  <br><br><br>

  <h3 id="event-editor">
    Edit events
    <button
      class="btn btn-primary pull-right"
      ng-click="vm.addEvent()">
      Add new
    </button>
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
        <td>
          <input
            type="text"
            class="form-control"
            ng-model="event.title">
        </td>
        <td>
          <input class="form-control" colorpicker type="text" ng-model="event.color.primary">
        </td>
        <td>
          <input class="form-control" colorpicker type="text" ng-model="event.color.secondary">
        </td>
        <td>
          <p class="input-group" style="max-width: 250px">
            <input
              type="text"
              class="form-control"
              readonly
              uib-datepicker-popup="dd MMMM yyyy"
              ng-model="event.startsAt"
              is-open="event.startOpen"
              close-text="Close" >
            <span class="input-group-btn">
              <button
                type="button"
                class="btn btn-default"
                ng-click="vm.toggle($event, 'startOpen', event)">
                <i class="glyphicon glyphicon-calendar"></i>
              </button>
            </span>
          </p>
          <div
            uib-timepicker
            ng-model="event.startsAt"
            hour-step="1"
            minute-step="15"
            show-meridian="true">
          </div>
        </td>
        <td>
          <p class="input-group" style="max-width: 250px">
            <input
              type="text"
              class="form-control"
              readonly
              uib-datepicker-popup="dd MMMM yyyy"
              ng-model="event.endsAt"
              is-open="event.endOpen"
              close-text="Close">
            <span class="input-group-btn">
              <button
                type="button"
                class="btn btn-default"
                ng-click="vm.toggle($event, 'endOpen', event)">
                <i class="glyphicon glyphicon-calendar"></i>
              </button>
            </span>
          </p>
          <div
            uib-timepicker
            ng-model="event.endsAt"
            hour-step="1"
            minute-step="15"
            show-meridian="true">
          </div>
        </td>
        <td>
          <button
            class="btn btn-danger"
            ng-click="vm.events.splice($index, 1)">
            Delete
          </button>
        </td>
      </tr>
    </tbody>

  </table>
</div>
    </div>
  </div>
</div>

  
  
  </body>
</html>