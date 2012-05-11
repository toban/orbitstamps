<%@ page import="service.model.Room" %>
<%@ page import="service.model.Operation" %>
<%@ page import="service.model.Person" %>
<%@ page import="service.communication.MessageReciever" %>
<%@ page import="java.util.Map.Entry" %>
<%@ page import="service.OrbitStamps" %>
{
"personal":  [
<%
boolean isFirst = true;

String roomID = request.getParameter("room");
String operationID = request.getParameter("opID");

if(roomID != null && operationID != null && !operationID.isEmpty() 
	&& !roomID.isEmpty() && OrbitStamps.operatingRooms.containsKey(roomID) 
	&& OrbitStamps.operatingRooms.get(roomID).operations.containsKey(operationID))
{
	Room room = OrbitStamps.operatingRooms.get(roomID);
	Operation op = room.operations.get(operationID);
	
	for(Person p : op.getPeople())
	{
		if(isFirst)
		{
			isFirst = false;
		}
		else
		{
			%>,<%
		}
		%>
		{
		"namn" : "<%= p.name %>",
		"roll" : "<%= p.role.getRole() %>",
		"id" : "<%= p.ID %>",
		"devices" : [<%
		             boolean isFirstDevice = true;
		             for(MessageReciever mr : p.devices)
		             {
		         		if(isFirstDevice)
		         			{isFirstDevice = false;}
		        		else
		        			{%>,<%}
		         		%>
		         		{ "number": "<%= mr.number %>" }
		         		<%
		             }
		             
		             %>]
		}
	<%
	}
}
%>
	]
}