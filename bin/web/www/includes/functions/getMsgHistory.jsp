<%@ page import="service.Room" %>
<%@ page import="service.Message" %>
<%@ page import="service.OrbitStamps" %>
<%@ page import="service.CommunicationHistory" %>
<%@ page import="java.util.Iterator" %>
<%
String roomID = request.getParameter("room");

if(roomID != null && !roomID.isEmpty() && OrbitStamps.operatingRooms.containsKey(roomID))
{
		Room room = OrbitStamps.operatingRooms.get(roomID);
		%>
		<div class="table">
		<div class="header-row">
		<div class="header">Tidpunkt</div>
		<div class="header">Avsändare</div>
		<div class="header" style="display:none;">HSAID</div>
		<div class="header">Nummer</div>
		<div class="header">Meddelande</div>
		<div class="header">Status</div>
		</div>
	<%
	
	for(CommunicationHistory old : room.messageHistory)
	{
	%>
		<div class="row">
		<div class="cell"><%= old.timeSent.getHours() + ":" + old.timeSent.getMinutes() + ":" + old.timeSent.getSeconds() %></div>
		<div class="cell"><%= old.type == CommunicationHistory.HISTORY_TYPE_AUTO ? "Automatisk" : "Manuell" %></div>
		<div class="cell personID" style="display:none;"><%= old.personID %></div>
		<div class="cell"><%= old.recv.number %></div>
		<div class="cell smallText"><%= old.msg.getBody() %></div>
		<div class="cell"><%= old.status %></div>
		</div>
	<%
	}
%></div><%
}
else
{
	out.print("hittar ingen historik för sal " + roomID);
}
	%>
