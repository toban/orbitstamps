<%@ page import="service.OrbitStamps" %>
<%@ page import="service.AscomPagerMessageChannel" %>
<%@ page import="service.PagerReciever" %>
<%@ page import="service.CommunicationHistory" %>
<%@ page import="service.Message" %>
<%@ page import="service.Urgency" %>
<%@ page import="service.Person" %>
<%@ page import="service.Room" %>
<% 

String id = request.getParameter("id");
String roomID = request.getParameter("room");

OrbitStamps.deleteFunctionalPerson(id, roomID);


%>

