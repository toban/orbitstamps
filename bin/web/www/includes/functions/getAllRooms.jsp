<%@ page import="service.model.Room" %>
<%@ page import="java.util.Map.Entry" %>
<%@ page import="service.OrbitStamps" %>
{
"rooms":  [
<%
boolean isFirst = true;
for(Entry<String, Room> roomEntry : OrbitStamps.operatingRooms.entrySet())
{
	if(isFirst)
	{
		isFirst = false;
	}
	else
	{
		%>,<%
	}
	Room room = roomEntry.getValue();
	%>
	{
	"name" : "<%= (room.roomName.length() > 0) ? room.roomName : room.roomID %>",
	"id" : "<%= room.roomID %>",
	"locationName" : "<%= room.locationName %>"
	}
<%
}
%>
	]
}