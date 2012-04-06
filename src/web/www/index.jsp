<%@ page import="service.OrbitStamps" %>
<%
int pageID = 0;
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
	<% if(pageID == 2 && true == false) { %>
	<li><a href="#" id="add-room-function">Lägg till funktion på sal</a></li>
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
default:
	%><%@include file="includes/service_overview.jsp" %><%
	break;
}
%>
<%@ include file="includes/footer.jsp" %>
