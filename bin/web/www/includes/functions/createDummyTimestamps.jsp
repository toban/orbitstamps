<%@ page import="service.OrbitStamps" %>
<%@ page import="service.AscomPagerMessageChannel" %>
<%@ page import="service.PagerReciever" %>
<%@ page import="service.CommunicationHistory" %>
<%@ page import="service.Message" %>
<%@ page import="service.Urgency" %>
<%@ page import="service.Timestamp" %>
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

