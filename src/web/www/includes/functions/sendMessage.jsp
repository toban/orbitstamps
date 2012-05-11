<%@ page import="service.OrbitStamps" %>
<%@ page import="service.communication.AscomPagerMessageChannel" %>
<%@ page import="service.communication.PagerReciever" %>
<%@ page import="service.communication.CommunicationHistory" %>
<%@ page import="service.communication.Message" %>
<%@ page import="service.model.Urgency" %>
<%@ page import="service.model.Person" %>
<%@ page import="service.MsgQueueItem" %>
<%@ page import="service.model.Room" %>

<%
	/*
room = Integer.parseInt(request.getParameter("room"));
pageID = Integer.parseInt(request.getParameter("number"));
message = Integer.parseInt(request.getParameter("page"));
*/
String[] recv = request.getParameterValues("recv[]");
String roomID = request.getParameter("room");
String callback = request.getParameter("callback");
String strMessage = request.getParameter("msg");

Message msg = new Message(strMessage, 
			Urgency.LEVEL_IMPORTANT, 
			AscomPagerMessageChannel.MESSAGE_BEEP_SIGNAL);

Room room = OrbitStamps.operatingRooms.get(roomID);

for(String id : recv)
{
	Person p = room.getPersistantPerson(id);
	out.print(p);
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

