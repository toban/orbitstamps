<%@ page import="service.model.Room" %>
<%@ page import="service.communication.Message" %>
<%@ page import="service.communication.CommunicationHistory" %>
<%@ page import="service.model.Person" %>
<%@ page import="java.util.Map.Entry" %>
<%@ page import="service.OrbitStamps" %>

<div id="content">		
<%
for(Entry<String, Room> roomEntry : OrbitStamps.operatingRooms.entrySet())
{
	Room room = roomEntry.getValue();
	%>
	<div class="room-overview-container"><a href="?page=2&amp;room=<%= room.roomID %>">Sal <%= room.roomID %></a></div>
	<%
}
%>
</div>