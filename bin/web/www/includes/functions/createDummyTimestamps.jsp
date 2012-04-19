<%@ page import="service.OrbitStamps" %>
<%@ page import="service.communication.AscomPagerMessageChannel" %>
<%@ page import="service.communication.PagerReciever" %>
<%@ page import="service.communication.CommunicationHistory" %>
<%@ page import="service.communication.Message" %>
<%@ page import="service.model.Urgency" %>
<%@ page import="service.model.Timestamp" %>
<%@ page import="java.util.Random" %>
<% 

String roomID = request.getParameter("room");
int numStamps = Integer.parseInt(request.getParameter("numStamps"));
Random rand = new Random();

out.println(numStamps);
out.println(roomID);
out.println("\n------------");
OrbitStamps.createDummyTimestamps(numStamps, roomID, rand);

for(Timestamp ts : OrbitStamps.operatingRooms.get(roomID).newStamps)
{
	out.println("\nstamp = " + ts.stamp);
}

%>

