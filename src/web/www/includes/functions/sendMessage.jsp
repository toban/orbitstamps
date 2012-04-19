<%@ page import="service.OrbitStamps" %>
<%@ page import="service.AscomPagerMessageChannel" %>
<%@ page import="service.PagerReciever" %>
<%@ page import="service.CommunicationHistory" %>
<%@ page import="service.Message" %>
<%@ page import="service.Urgency" %>
<%@ page import="service.Person" %>
<%@ page import="service.Room" %>
<% 
/*
room = Integer.parseInt(request.getParameter("room"));
pageID = Integer.parseInt(request.getParameter("number"));
message = Integer.parseInt(request.getParameter("page"));
*/
String[] recv = request.getParameterValues("recv[]");
String roomID = request.getParameter("room");
String strMessage = request.getParameter("msg");

AscomPagerMessageChannel channel = new AscomPagerMessageChannel();
Message msg = new Message(strMessage, 
					Urgency.LEVEL_IMPORTANT, 
					AscomPagerMessageChannel.MESSAGE_BEEP_SIGNAL);

Room room = OrbitStamps.operatingRooms.get(roomID);

for(String id : recv)
{
	Person p = room.getPerson(id);
	out.print(p);
	if(p==null)
		continue;
	else
	{
		PagerReciever pr = (PagerReciever) p.getDeviceCompatibleWith(channel);
		if(pr==null)
		{
			continue;
		}
		else
		{
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
			room.messageHistory.addFirst(new CommunicationHistory(msg, 
															pr, 
															CommunicationHistory.HISTORY_TYPE_MANUAL,
															id));
		}
		//channel.sendMessage(msg,);
		
	}
}

%>
