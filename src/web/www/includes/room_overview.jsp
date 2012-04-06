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
			var createNewMessage = true;
			var newMsgRecv = new Array();
			
			/* updating the history */
			var historyUpdate = function()
			{
			$.post("includes/functions/getMsgHistory.jsp", {room: <%= roomID %>},
					   function(data) 
					   {
					     $("#history-table-container").html(data);
					     var date = new Date();
					     var updateString = date.getHours() + ":" + date.getMinutes() +":"+ date.getSeconds();
					     $("#history-update").text("Senast uppdaterad " + updateString);
					     
					   });
			};
			/* minify new message */
			$("#new-msg-mini").click(function()
					{
						$("#create-message-container").slideUp();
						createNewMessage = true;
					
					});
			// delete current message, and receievers
			$("#new-msg-close").click(function()
					{
						$("#recv-container").html("");
						newMsgRecv = new Array();
						var newMsg = $("#create-message-container");
						$("div.room-container div").removeClass("msg-selected");
						newMsg.hide();
						createNewMessage = true;
					
					});
			// send message
			$("#new-msg-submit").click(function()
			{
				var message = $("#msg").val();
				if(newMsgRecv.length > 0)
				{
				$.post("includes/functions/sendMessage.jsp", {'recv[]': newMsgRecv,
																room: <%= roomID %>,
																msg: message},
						   function(data) // message sent callback
						   {
							
							$("#msg-template option").first().attr('selected', true); 
							$("#msg").val("");
							updateCharCount();
							
							$("#recv-container").html("");
							newMsgRecv = new Array();
							var newMsg = $("#create-message-container");
							$("div.room-container div").removeClass("msg-selected");
							newMsg.slideUp();
							createNewMessage = true;
							historyUpdate();
						    
						   });
				}
				else
				{
					$("#input-recv-error").fadeIn().delay(1000).fadeOut();
				}
			});
			/*
			ADD NEW RECIEVER TO MESSAGE, CREATE MESSAGE
			*/
			$("div.room-container div.row").click(function()
			{
				var newMsg = $("#create-message-container");	
				
				// initial 
				if(createNewMessage)
				{
					newMsg.slideDown();
					createNewMessage = false;
				}
				
				$(this).addClass("msg-selected");
				var newRecv = $(this).children("div.cell.personID").text();
				var newRecvTitle = $(this).children("div.cell.namn").text(); 
				
				if(newMsgRecv.indexOf(newRecv) === -1)
				{
					newMsgRecv.push(newRecv);
					var currentVal = newMsg.children("input#recv").val();
					console.log(newRecv);
					var item = $("<a class='recv-item' href='#' title='Klicka för att ta bort!' id='recv_"+newRecv+"'>" + newRecvTitle + "</a>");
					item.addClass($(this).children("div.cell.roll").text().replace(" ", "-"));
					//item.addClass()
					$("#recv-container").append(item);
				}
				else // clicked again, already selected
				{
					
					var index = newMsgRecv.indexOf(newRecv);
					console.log(index);
					newMsgRecv.splice(index,1);
					$("#recv_"+newRecv).hide(100, function(){
						$("#recv_"+newRecv).remove();
					});
					$(this).removeClass("msg-selected");
				}
			
			});
			/* eventlistener for reciever */
			$(".recv-item").live("click", function()
					{
						
						console.log($(this).attr("id"))
						var thisVar = $(this).attr("id").split("_")[1];
						var index = newMsgRecv.indexOf(thisVar);
						console.log(index);
						if(index != -1)
						{
							
							newMsgRecv.splice(index,1);
							$(this).hide(100, function(){
								$(this).remove();
								$("#row_"+thisVar).removeClass("msg-selected");
							});
						}
					});
			var updateCharCount = function()
			{
				console.log("asdas");
				if($("#msg").val().length > 120)
				{
					$("#msg").val($("#msg").val().substring(0,120));
				}
				var charLength = 120 - $("#msg").val().length;
				$("#char-counter").text(charLength)
			};
			
			/* msg events */
			$("#msg").keydown(function()
					{
						updateCharCount();
					});
			$("#msg").change(function()
					{
						updateCharCount();
					});
			
			$("#msg-template").change(function()
					{
						var val = $("#msg-template option:selected").val();
						$("#msg").val(val);
						updateCharCount();
					});
			
			/* minify timeline */
			$("#timeline-mini-button").click(function()
					{
						$("div.operation-timeline").slideToggle("slow");
					});
			
			/* personal pa sal hovering */
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
		<div id="timeline-container">
		<div class="operation-timeline">
			<div class="timeline-table">
			<div class="timeline-row">
			<div class="timeline-cell" style="background-color: #F0A;"><a title="tidpunkt" href="#">PREOP</a></div>
			<div class="timeline-cell" style="background-color: #FAA;"><a title="tidpunkt" href="#">ANST</a></div>
			<div class="timeline-cell" style="background-color: #3AA;"><a title="tidpunkt" href="#">ANST</a></div>
			<div class="timeline-cell" style="background-color: #CA0;"><a title="tidpunkt" href="#">ANST</a></div>
			<div class="timeline-cell last-element" style="width: 50%"></div>
			</div>
			</div>
			
		</div>
		<a href="#" title="Dölj/visa operationsflödet" id="timeline-mini-button">&uarr;&darr;</a>
		</div>
		<div id="content">		
		<h1>Sal <%= room.roomID %></h1>
		
		<!-- new message start -->
		<div id="create-message-container">
		<div>
		<h3>Skicka ett meddelande</h3>
		<a href="#" class="window-button" id="new-msg-close">X</a><a href="#" class="window-button" id="new-msg-mini">_</a>
		</div>

		<div class="new-msg-field" style="width: 40%;">
		<div style="display: block;">
		<label for="msg-template">Meddelande-mall</label>
			<select name="msg-template" id="msg-template">
				<option value="" class="msg-template-item">* Nytt meddelande</option>
				<option value="Patienten har anlänt på sal" class="msg-template-item">Patienten har anlänt på sal</option>
				<option value="Patienten har anlänt PREOP" class="msg-template-item">Patienten har anlänt PREOP</option>
			</select>
		
		</div>
		<div>
		<label for="sender">Meddelande</label>
		<textarea name="msg" id="msg" style="width: 100%;" rows="3"></textarea>
		</div>
		<em>Du har <span id="char-counter">120</span> bokstäver kvar.</div></em>
		<div class="new-msg-field" style="width: 40%;">
		<label for="recv-container">Mottagare</label>
		<div id="input-recv-error" style="display: none; color: #F00;">Mottagare saknas!</div>
		<div id="recv-container"></div>
		<!--  <input type="hidden" name="recv" id="recv" />  -->
		</div>

		<div style="float: right; position: absolute; bottom: 10px; right: 10px;">
		<input type="submit" id="new-msg-submit" value="Skicka" />
		</div>
		</div>
		<!--  new mssage stop -->
		
		<div class="table-container">
		<div class="room-container" style="width: 40%;">
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
			<div alt="Klicka för att skicka ett meddelande" title="Klicka för att skicka ett meddelande" class="row clickable" id="row_<%= p.ID %>">
			<div class="cell personID"><%= p.ID %></div>
			<div class="cell namn"><%= p.name %></div>
			<div class="cell roll <%= p.role.getRole().replaceAll(" ", "-") %>"><%= p.role.getRole() %></div>
			</div>
		<%
		counter++;
		}
		%>
		</div> <!-- end table -->
		</div> <!-- end room -->
		
		<div id="history" style="width: 58%;">
		<h2>Meddelande historik</h2><span id="history-update"></span>
		<div id="history-table-container">
		<!-- history post start -->
		
		<!-- history post end -->
		</div>
		</div> <!-- end history -->
		</div> <!-- end table-container -->
		<%
}
		%>
		</div>