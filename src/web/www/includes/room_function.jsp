<%@ page import="service.model.Room" %>
<%@ page import="service.communication.Message" %>
<%@ page import="service.communication.CommunicationHistory" %>
<%@ page import="service.model.Person" %>
<%@ page import="service.model.FunctionalPerson" %>
<%@ page import="java.util.Map.Entry" %>
<%@ page import="service.model.Role" %>
<%@ page import="service.OrbitStamps" %>

<script type="text/javascript">
$(document).ready(function()
{
	$("#func-form").validate();
$("td.cell.delete a").live("click", function(e)
		{
		e.preventDefault();
		var row = $(this).parent().parent().parent();
		console.log(row);
		var usrID = row.children("td.cell.personID").first().text();
		$.post("includes/functions/deleteFunction.jsp", {
			room: <%= roomID %>,
			id: usrID},
			function(data) // message sent callback
			{
				console.log(data);
				var parent = row.parent();
				row.remove();
			});
	
		});
$("#add-new-function").click(function(e)
		{
		e.preventDefault();
		if($("#func-form").valid())
			{
		var desc = $("#function-desc").val();
		var num = $("#pager-num").val();;
		
		$.post("includes/functions/addFunction.jsp", {
			room: <%= roomID %>,
			pager: num,
			name: desc
			},
			function(data) 
			{
				var obj = $.parseJSON(data);
				var newFunc = $("<tr class='row'><td class='cell personID'>"+obj.id+"</td><td class='cell namn'>"+obj.name+"</td><td class='cell roll'>"+obj.role+"</td><td class='cell delete'><center><a href='#'></a></center></td></tr>");
				$("#functions-table").append(newFunc);
				console.log(data);
			});
			}
		});
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
		<div id="content">		
		<h1>Sal <%= room.roomID %></h1>
		<div id="form-container">
		<h2>Lägg till en ny funktion på sal</h2>
		<form id="func-form">
		<div style="display: inline-block;">
		<label for=function-dd>Funktion</label>
			<select name="function-dd" class="required" id="function-dd">
				<% for(String s : Role.ROLE_FUNCTION_ARRAY) { %>
				<option value="<%= s %>" class="msg-template-item"><%= s %></option>
				<% } %>
			</select>
		</div>
		<div style="display: inline-block;">
		<label for="pager-num">Sökarnummer</label>
		<input type="text" name="pager-num" class="required" id="pager-num" />
		</div>
		<div style="display: inline-block;">
		<label for="function-desc">Beskrivning / namn</label>
		<input type="text" name="function-desc" class="required" id="function-desc" />
		</div>
		<input type="submit" id="add-new-function" value="Lägg till" />
		</form>
		</div>
		<div class="table-container">
		<div class="room-container" style="width: 100%;">
		<h2>Funktion på sal</h2>
		<table class="table" id="functions-table">
		
			<tr class="header-row">
			<td class="header">ID</td>
			<td class="header">Namn</td>
			<td class="header">Roll</td>
			<td class="header">Ta bort</td>
			</tr>
		<%
		int counter = 0;
		for(Person p : room.getPeople())
		{
			if(p instanceof FunctionalPerson)
			{
		%>
			<tr class="row">
			<td class="cell personID"><%= p.ID %></td>
			<td class="cell namn"><%= p.name %></td>
			<td class="cell roll <%= p.role.getRole().replaceAll(" ", "-") %>"><%= p.role.getRole() %></td>
			<td class="cell delete"><center><a href=""></a></center></td>
			</tr>
		<%
			}
		counter++;
		}
		%>
		</table> <!-- end table -->
		</div> <!-- end room -->
		<%
}
		%>
		</div>
