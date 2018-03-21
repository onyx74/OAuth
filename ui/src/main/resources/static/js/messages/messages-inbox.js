let currentHref;

function applayFilters() {
    alert(window.location.href);
    var stateObj = {foo: "bar"};
    subject = $("#subject").val();
    sentTo = $("#sentTo").val();
    var z;
    if (currentHref) {
        z = currentHref + "?subject=" + subject + "&sentTo=" + sentTo;
    } else {
        currentHref = window.location.href;
        z = window.location.href + "?subject=" + subject + "&sentTo=" + sentTo;
    }
    history.pushState(stateObj, "page 2", z);
    getPagebleMessages();

}

function resetFilters() {
    subject = "";
    sentTo = "";
    $("#subject").val("");
    $("#sentTo").val("");
    var stateObj = {foo: "bar"};
    history.pushState(stateObj, "page 2", currentHref);
    getPagebleMessages();
}