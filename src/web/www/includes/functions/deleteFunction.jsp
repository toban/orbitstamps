<%@ page import="service.OrbitStamps" %>
<%@ page import="service.communication.AscomPagerMessageChannel" %>
<%@ page import="service.communication.PagerReciever" %>
<%@ page import="service.communication.CommunicationHistory" %>
<%@ page import="service.communication.Message" %>
<%@ page import="service.model.Urgency" %>
<%@ page import="service.model.Person" %>
<%@ page import="service.model.Room" %>
<% 

String id = request.getParameter("id");
String roomID = request.getParameter("room");

OrbitStamps.deleteFunctionalPerson(id, roomID);


%>

