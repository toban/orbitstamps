<%@ page import="service.model.Room" %>
<%@ page import="service.model.Operation" %>
<%@ page import="service.model.Person" %>
<%@ page import="service.communication.MessageReciever" %>
<%@ page import="java.util.Map.Entry" %>
<%@ page import="service.OrbitStamps" %>
<%@ page import="java.text.SimpleDateFormat" %>
{
"operations":  [
<%
boolean isFirst = true;

String roomID = request.getParameter("room");

if(roomID != null && !roomID.isEmpty() && OrbitStamps.operatingRooms.containsKey(roomID))
{
	Room room = OrbitStamps.operatingRooms.get(roomID);
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	
	for(Entry<String, Operation> opEntry : room.operations.entrySet())
	{
		Operation op = opEntry.getValue();
		
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
		"op_desc" : "<%= op.op_desc %>",
		"op_start" : "<%= sdf.format(op.op_start) %>",
		"op_end" : "<%= op.op_end %>",
		"op_id" : "<%= op.op_id %>",
		"opkort_id" : "<%= op.opkort_id %>",
		"op_num_stamps" : "<%= op.stamps.size() %>"
		}
	<%
	}
}
%>
	]
}