<%@ page import="roles.*" %>
<%@ page import="service.Room" %>
<%@ page import="service.Message" %>
<%@ page import="service.CommunicationHistory" %>
<%@ page import="service.Person" %>
<%@ page import="java.util.Map.Entry" %>
<%@ page import="service.OrbitStamps" %>
<% String roomID = request.getParameter("room"); %>
<script type="text/javascript">
$(document).ready(function()
		{
			var historyUpdate = function()
			{
			$.post("includes/functions/getMsgHistory.jsp", {room: <%= roomID %>},
					   function(data) 
					   {
					     $("div#history div.table").html(data);
					     var date = new Date();
					     var updateString = date.getHours() + ":" + date.getMinutes() +":"+ date.getSeconds();
					     $("#history-update").text("Senast uppdaterad " + updateString);
					   });
			};
			$("div.room-container div.row").hover(function()
			{
				var pID = $(this).children("div.cell.personID").text();
				
				$(this).addClass("hovering");
				$("#history div.cell.personID").each(function()
				{
					var that = $(this);
					
					if(that.text()==pID)
						that.parent().addClass("hovering");
				});
				console.log(pID);
			},function(){
				$(this).removeClass("hovering");
				$("#history div.row").removeClass("hovering");
			});
			
			historyUpdate();
			setInterval(historyUpdate,10000);
		});
</script>
<%

if(roomID == null || roomID.isEmpty() || !OrbitStamps.operatingRooms.containsKey(roomID))
{

}
else
{
		Room room = OrbitStamps.operatingRooms.get(roomID);
		%>
		
		<h1>Sal <%= room.roomID %></h1>
		<div class="table-container">
		<div class="room-container" style="width: 50%;">
		<h2>Personal på sal</h2>
		<div class="table">
		
			<div class="header-row">
			<div class="header">HSAID</div>
			<div class="header">Namn</div>
			<div class="header">Roll</div>
			</div>
		<%
		int counter = 0;
		for(Person p : room.getPeople())
		{
		%>
			<div class="row clickable" id="<%= counter %>">
			<div class="cell personID"><%= p.ID %></div>
			<div class="cell"><%= p.name %></div>
			<div class="cell <%= p.role.getRole().replaceAll(" ", "-") %>"><%= p.role.getRole() %></div>
			</div>
		<%
		counter++;
		}
		%>
		</div> <!-- end table -->
		</div> <!-- end room -->
		
		<div id="history" style="width: 48%;">
		<h2>Meddelande historik</h2><span id="history-update"></span>
		<div class="table">
		<!-- history post start -->
		
		<!-- history post end -->
		</div> <!-- end table -->
		</div> <!-- end history -->
		</div> <!-- end table-container -->
		<%
}
		%>