<%@ page import="service.model.Room" %>
<%@ page import="service.model.Operation" %>
<%@ page import="service.model.Timestamp" %>
<%@ page import="java.util.Map.Entry" %>
<%@ page import="service.OrbitStamps" %>
{
"rooms":  [
<%
boolean isFirst = true;

String roomID = request.getParameter("room");
String operationID = request.getParameter("opID");

if(roomID != null && operationID != null && !operationID.isEmpty() && !roomID.isEmpty() && OrbitStamps.operatingRooms.containsKey(roomID) 
	&& OrbitStamps.operatingRooms.get(roomID).operations.containsKey(operationID))
{
	Room room = OrbitStamps.operatingRooms.get(roomID);
	Operation op = room.operations.get(operationID);
	
	for(Timestamp ts : op.stamps)
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
		"order" : "<%= "1" %>",
		"time" : "<%= "2012-05-07 13:22:00"/*ts.time*/ %>"
		}
	<%
	}
}
%>
	]
}