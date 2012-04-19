<%@ page import="service.OrbitStamps" %>
<% String roomID = request.getParameter("room"); %>
<%! int pageID = 0; %>
<%
try
{
	pageID = Integer.parseInt(request.getParameter("page"));	
}
catch(Exception e)
{
	pageID = 0;
}

%>
<%@ include file="includes/header.jsp" %>
<div id="menu">
<ul>
	<li><a href="?page=0">Start</a></li>
	<li><a href="?page=1">Välj sal</a></li>
	<% if(pageID == 2) { %>
	<li><a href="?page=3&amp;room=<%= roomID %>" id="add-room-function">Hantera funktion på sal</a></li>
	<% } %>
	<% if(pageID == 3) { %>
	<li><a href="?page=2&amp;room=<%= roomID %>">Tillbaka till sal</a></li>
	<% } %>
</ul>
<div id="search">Sök<input type="text" name="search" id="search-field" /></div>
</div>
<%

switch(pageID)
{
case 0:
	%><%@include file="includes/start.jsp" %><%
	break;
case 1:
	%><%@include file="includes/start_overview.jsp" %><%
	break;
case 2:
	%><%@include file="includes/room_overview.jsp" %><%
	break;
case 3:
	%><%@include file="includes/room_function.jsp" %><%
	break;
default:
	%><%@include file="includes/service_overview.jsp" %><%
	break;
}
%>
<%@ include file="includes/footer.jsp" %>
