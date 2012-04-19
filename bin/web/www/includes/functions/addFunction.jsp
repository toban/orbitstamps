<%@ page import="service.Room" %>

<%@ page import="service.OrbitStamps" %>
<%@ page import="service.Role" %>
<%@ page import="service.FunctionalPerson" %>
<%@ page import="service.PagerReciever" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.UUID" %>
<%
String roomID = request.getParameter("room");
String pager = request.getParameter("pager");
String name = request.getParameter("name");
String id = UUID.randomUUID().toString().substring(0,8);

if(roomID != null && !roomID.isEmpty() && OrbitStamps.operatingRooms.containsKey(roomID))
{
	PagerReciever pr = new PagerReciever(pager);
	FunctionalPerson p = new FunctionalPerson(name, new Role(Role.ROLE_FUNKTION), id, roomID);
	p.devices.add(pr);
	Room room = OrbitStamps.operatingRooms.get(roomID);
	room.addPerson(p);
	OrbitStamps.savePersistantData();
	%>
{
"id": "<%= p.ID %>",
"role": "<%= p.role.getRole() %>",
"name": "<%= p.name %>"
}
     
	<%
	}
	%>

