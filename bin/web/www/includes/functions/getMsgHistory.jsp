<%@ page import="service.model.Room" %>
<%@ page import="service.communication.Message" %>
<%@ page import="service.OrbitStamps" %>
<%@ page import="service.communication.CommunicationHistory" %>
<%@ page import="java.util.Iterator" %>
<%
String roomID = request.getParameter("room");

if(roomID != null && !roomID.isEmpty() && OrbitStamps.operatingRooms.containsKey(roomID))
{
	Room room = OrbitStamps.operatingRooms.get(roomID);
	%>
	{
	"history": [
	<%
	int i = 0;
	int length = room.messageHistory.size();
	for(CommunicationHistory old : room.messageHistory)
	{
	%>
	{
	 "uuid": "<%= old.uuid %>",
     "time": "<%= old.timeSent.getHours() + ":" + old.timeSent.getMinutes() + ":" + old.timeSent.getSeconds() %>",
     "type": "<%= old.type == CommunicationHistory.HISTORY_TYPE_AUTO ? "Automatisk" : "Manuell" %>",
     "personID": "<%= old.personID %>",
     "recvNumber": "<%= old.recv.number %>",
     "body": "<%= old.msg.getBody() %>",
     "status": <%= old.status %>
    }
     
	<%
	i++;
	if(i < length)
		out.print(",");
	}
%>
]}
<%
}
else
{

}
	%>
