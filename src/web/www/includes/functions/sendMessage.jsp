<%@ page import="service.OrbitStamps" %>
<%@ page import="service.communication.AscomPagerMessageChannel" %>
<%@ page import="service.communication.PagerReciever" %>
<%@ page import="service.communication.CommunicationHistory" %>
<%@ page import="service.communication.Message" %>
<%@ page import="service.model.Urgency" %>
<%@ page import="service.model.Person" %>
<%@ page import="service.MsgQueueItem" %>
<%@ page import="service.model.Room" %>
<%@ page import="service.model.Operation" %>

<%
	/*
room = Integer.parseInt(request.getParameter("room"));
pageID = Integer.parseInt(request.getParameter("number"));
message = Integer.parseInt(request.getParameter("page"));
*/
String[] recv = request.getParameterValues("recv[]");
String roomID = request.getParameter("room");
String opID = request.getParameter("operation");
String callback = request.getParameter("callback");
String strMessage = request.getParameter("msg");

Message msg = new Message(strMessage, 
			Urgency.LEVEL_IMPORTANT, 
			AscomPagerMessageChannel.MESSAGE_BEEP_SIGNAL);

Room room = OrbitStamps.operatingRooms.get(roomID);
Operation op = room.operations.get(opID);

for(String id : recv)
{
	Person p = op.getPerson(id);
	
	if(p==null)
		continue;
	else
	{
		PagerReciever pr = (PagerReciever) p.getDeviceCompatibleWith(OrbitStamps.DEFAULT_CHANNEL);
		
		if(pr==null)
		{
			continue;
		}
		else
		{
			OrbitStamps.msgQueue.add(new MsgQueueItem(room, p, msg, CommunicationHistory.HISTORY_TYPE_MANUAL));
		}
		
	}
}
%>

