(function () {
    var eventSource = new EventSource("notifies");
    
    eventSource.onopen = function () {
        console.log("ONOPEN");
    };
    
    eventSource.onmessage = function (msg) {
       console.log("MSG: " + msg); 
    };
})();