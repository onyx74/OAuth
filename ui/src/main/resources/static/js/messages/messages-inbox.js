let currentHref;

function applayFilters(val) {
    var stateObj = {foo: "bar"};
    subject = $("#subject").val();
    sentTo = $("#from").val();
    var z;
    if (currentHref) {
        z = currentHref + "?subject=" + subject + "&sentTo=" + sentTo;
    } else {
        currentHref = window.location.href;
        z = window.location.href + "?subject=" + subject + "&sentTo=" + sentTo;
    }
    history.pushState(stateObj, "page 2", z);
    if (val) {
        getPagebleSentMessages();
    } else {
        getPagebleMessages();
    }
}

function resetFilters(val) {
    subject = "";
    sentTo = "";
    $("#subject").val("");
    $("#from").val("");
    var stateObj = {foo: "bar"};
    history.pushState(stateObj, "page 2", currentHref);
    if (val) {
        getPagebleSentMessages();
    } else {
        getPagebleMessages();
    }
}