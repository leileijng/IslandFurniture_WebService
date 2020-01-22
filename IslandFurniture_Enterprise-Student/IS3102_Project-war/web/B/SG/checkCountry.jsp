<%
    session = request.getSession();

    String URLprefix = (String) session.getAttribute("URLprefix");
    if (URLprefix == null) { %>
<jsp:forward page="selectCountry.jsp" />
<% }%>