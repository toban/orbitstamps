<%@ page import="service.OrbitStamps" %>
<%@ include file="includes/header.jsp" %>
<%@ include file="includes/menu.jsp" %>
<div id="content">
<%
int pageID = Integer.parseInt(request.getParameter("page"));
switch(pageID)
{
case 0:
	%><%@include file="includes/service_overview.jsp" %><%
	break;
case 1:
	%><%@include file="includes/room_overview.jsp" %><%
	break;
default:
	%><%@include file="includes/service_overview.jsp" %><%
	break;
}
%>
</div>
<%@ include file="includes/footer.jsp" %>
