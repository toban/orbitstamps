<%@ page import="service.Room" %>
<%@ page import="service.Message" %>
<%@ page import="service.CommunicationHistory" %>
<%@ page import="service.Person" %>
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