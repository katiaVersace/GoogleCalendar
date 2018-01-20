(function () {
    var eventSource = new EventSource("notifies");
    eventSource.onmessage = function (msg) {
       console.log("MSG: ");
       console.log(msg);
    };
})();