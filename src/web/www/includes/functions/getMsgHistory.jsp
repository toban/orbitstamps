<%@ page import="service.Room" %>
<%@ page import="service.Message" %>
<%@ page import="service.OrbitStamps" %>
<%@ page import="service.CommunicationHistory" %>
<%
String roomID = request.getParameter("room");

if(roomID != null && !roomID.isEmpty() && OrbitStamps.operatingRooms.containsKey(roomID))
{
		Room room = OrbitStamps.operatingRooms.get(roomID);
		%>
		<div class="table">
		<div class="header-row">
		<div class="header">Avs�ndare</div>
		<div class="header" style="display:none;">HSAID</div>
		<div class="header">Mottagare</div>
		<div class="header">Meddelande</div>
		</div>
	<% 
	for(CommunicationHistory old : room.messageHistory)
	{
	%>
		<div class="row">
		<div class="cell"><%= old.type == CommunicationHistory.HISTORY_TYPE_AUTO ? "Automatisk" : "Manuell" %></div>
		<div class="cell personID" style="display:none;"><%= old.personID %></div>
		<div class="cell"><%= old.recv.number %></div>
		<div class="cell smallText"><%= old.msg.getBody() %></div>
		</div>
	<%
	}
%></div><%
}
else
{
	out.print("hittar ingen historik f�r sal " + roomID);
}
	%>