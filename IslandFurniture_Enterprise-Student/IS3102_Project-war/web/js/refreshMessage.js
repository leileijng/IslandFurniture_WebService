function refreshMessages() {
    var address = "../../AccountManagement_LoginServlet";
    var xmlHttp = null;
    xmlHttp = new XMLHttpRequest();
    URL = address;
    xmlHttp.open("GET", URL, false);
    xmlHttp.send();
}
