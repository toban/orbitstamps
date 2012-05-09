<%@ page import="service.model.Room" %>
<%@ page import="service.communication.Message" %>
<%@ page import="service.communication.CommunicationHistory" %>
<%@ page import="service.model.Person" %>
<%@ page import="java.util.Map.Entry" %>
<%@ page import="service.OrbitStamps" %>
<%@ page import="service.model.Operation" %>
<%@ page import="service.communication.MessageChannel" %>
<%@ page import="java.util.Map.Entry" %>

<script type="text/javascript">

var statusHTML = function(status)
{
	switch(status)
	{
	case <%= MessageChannel.MSG_STATUS_SENT  %>:
		return '<span class="msg-status" style="background: #0F0;">Skickat</span>';
	case <%= MessageChannel.MSG_STATUS_FAILED  %>:
		return '<span class="msg-status" style="background: #F00;">Misslyckades</span>';
	case <%= MessageChannel.MSG_STATUS_NO_COMPATIBLE_DEVICE  %>:
		return '<span class="msg-status" style="background: #F00;">Ingen kompatibel mottagare</span>';
	case <%= MessageChannel.MSG_STATUS_RECIEVED  %>:
		return '<span class="msg-status" style="background: #0F0;">Mottaget</span>';
	}
	return 'Okänd status';
}

$(document).ready(function()
		{
			var createNewMessage = true;
			var newMsgRecv = new Array();
			var msgHistory = new Array();
			
			var drawTimeOperationOverview = function(operationID)
			{
				$.post("includes/functions/getOperationTimestamps.jsp", {opID: operationID, room: <%= roomID %>},
						   function(data) 
						   {
							console.log(data);
						   });	
			}
			$("#operations").change(function()
					{
						var selectedOperation = $("#operations option:selected").val();
						drawTimeOperationOverview(selectedOperation);
						
					});
			
			/* updating the history */
			var historyUpdate = function()
			{
			$.post("includes/functions/getMsgHistory.jsp", {room: <%= roomID %>},
					   function(data) 
					   {
				
					     
					     var obj = $.parseJSON(data);
						 obj.history = obj.history.reverse();
					     for(var i = 0; i < obj.history.length; i++)
					    	 {
					    	 	var item = obj.history[i];
					    	 	if($.inArray(item.uuid,msgHistory) == -1)
					    	 		{
					    	 			msgHistory.push(item.uuid)
										var htmlItem = $("<tr class='row history'><td class='cell'>"+item.time+"</td><td class='cell'>"+item.type+"</td><td class='cell personID'>"+item.personID+"</td><td class='cell'>"+item.recvNumber+"</td><td class='cell smallText'>"+item.body+"</td><td class='cell'>"+statusHTML(item.status)+"</td></tr>");
					    	 			$("#history-table-container .table .header-row").after(htmlItem);
					    	 			htmlItem.hide();
					    	 			htmlItem.fadeIn();
					    	 		}
					    	 }
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
;
				if(newMsgRecv.length > 0)
				{
				$("#msg-input-container").slideToggle();
				$("#msg-sending-info").show()	
				var callback = $("#msg-callback").val();
				$.post("includes/functions/sendMessage.jsp", {'recv[]': newMsgRecv,
																room: <%= roomID %>,
																callback: callback,
																msg: message},
						   function(data) // message sent callback
						   {
							
							$("#msg-template option").first().attr('selected', true); 
							$("#msg").val("");
							updateCharCount();
							
							$("#recv-container").html("");
							newMsgRecv = new Array();
							var newMsg = $("#create-message-container");
							$("div.room-container tr").removeClass("msg-selected");
							//newMsg.slideUp();
							createNewMessage = true;
							historyUpdate();
							$("#msg-input-container").slideToggle();
							$("#msg-sending-info").hide();
						    
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
			$("div.room-container tr.row").click(function()
			{
				var newMsg = $("#create-message-container");	
				
				// initial 
				if(createNewMessage)
				{
					newMsg.slideDown();
					createNewMessage = false;
				}
				
				$(this).addClass("msg-selected");
				var newRecv = $(this).children("td.cell.personID").text();
				var newRecvTitle = $(this).children("td.cell.namn").text(); 
				
				if($.inArray(newRecv,newMsgRecv) === -1)
				{
					newMsgRecv.push(newRecv);
					var currentVal = newMsg.children("input#recv").val();
					
					var item = $("<a class='recv-item' href='#' title='Klicka för att ta bort!' id='recv_"+newRecv+"'>" + newRecvTitle + "</a>");
					item.addClass($(this).children("td.cell.roll").text().replace(" ", "-"));
					//item.addClass()
					$("#recv-container").append(item);
				}
				else // clicked again, already selected
				{
					
					var index = $.inArray(newRecv,newMsgRecv);
					
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
						
						
						var thisVar = $(this).attr("id").split("_")[1];
						var index = $.inArray(thisVar,newMsgRecv);
					
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
				
				if($("#msg").val().length > 120)
				{
					$("#msg").val($("#msg").val().substring(0,120));
				}
				var charLength = 120 - $("#msg").val().length;
				$("#char-counter").text(charLength)
			};
			
			var updateCallback = function(obj) {
				
				var pos = $.inArray("]", $("#msg").val());
				if(pos != -1)
					$("#msg").val($("#msg").val().substring(pos+2,120));
				
				if($("#msg-callback").val().length > 0)
					var text = "["+$.trim($(obj).val())+"] " + $("#msg").val();
				else
					var text = $("#msg").val();
				
				
				
				$("#msg").val(text)
				updateCharCount();
			}
			/* callback events*/
			$("#msg-callback").change(function(e)
				{
					updateCallback(this);	
				});
			$("#msg-callback").keyup(function(e)
				{
					updateCallback(this);	
				});
			$("#msg-callback").focus(function(e)
					{
						updateCallback(this);	
					});
			/* msg events */
			$("#msg").keydown(function()
					{
						
						updateCharCount();
						updateCallback($("#msg-callback"));	
					});
			$("#msg").change(function()
					{
						updateCharCount();
						updateCallback($("#msg-callback"));	
					});
			
			$("#msg-template").change(function()
					{
						var val = $("#msg-template option:selected").val();
						$("#msg").val(val);
						
						updateCharCount();
						updateCallback($("#msg-callback"));
						
					});
			
			/* minify timeline */
			$("#timeline-mini-button").click(function()
					{
						$("div.operation-timeline").slideToggle("slow");
					});
			
			/* personal pa sal hovering */
			$("div.room-container tr.row").hover(function()
			{
				var pID = $(this).children("td.cell.personID").text();
				
				$(this).addClass("hovering");
				$("#history td.cell.personID").each(function()
				{
					var that = $(this);
					
					if(that.text()==pID)
						that.parent().addClass("second-hovering");
				});
			},function(){
				$(this).removeClass("hovering");
				$("#history tr.row").removeClass("second-hovering");
			});
			/* personal pa sal hovering */
			$("tr.row.history").live("mouseover", function()
			{
				var pID = $(this).children("td.cell.personID").text();
				
				$(this).addClass("hovering");
			
				$(".room-container td.cell.personID").each(function()
				{
					var that = $(this);
					if(that.text()==pID)
						that.parent().addClass("second-hovering");
				});
			});
			$("tr.row.history").live("mouseout",function(){
				$(this).removeClass("hovering");
				$(".room-container tr.row").removeClass("second-hovering");
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
		<div class="operation-timeline" style="display: none;">
			<table class="timeline-table">
			<tr class="timeline-row">
			<td class="timeline-cell" style="background-color: #F0A;"><a title="tidpunkt" href="#">PREOP</a></td>
			<td class="timeline-cell" style="background-color: #FAA;"><a title="tidpunkt" href="#">RINGT AVD</a></td>
			<td class="timeline-cell" style="background-color: #3AA;"><a title="tidpunkt" href="#">ANLÄNT OPSAL</a></td>
			<td class="timeline-cell" style="background-color: #CA0;"><a title="tidpunkt" href="#">PATIENT START</a></td>
			<td class="timeline-cell last-element" style="width: 50%"></td>
			</tr>
			</table>
			
		</div>
		<a href="#" title="Dölj/visa operationsflödet" id="timeline-mini-button">&uarr;&darr;</a>
		</div>
		<div id="content">
		<div>
		<form action="">
		<select name="operations" id="operations">
		<% for(Entry<String, Operation> opEntry : room.operations.entrySet()) { 
		Operation op = opEntry.getValue();
		%>
		<option value="<%= op.op_id %>"><%= op.op_id %></option>
		<% } %>
		</select>
		</form>
		</div>		
		<h1>Sal <%= room.roomName %></h1>
		
		<!-- new message start -->
		<div id="create-message-container">
			<center><div id="msg-sending-info" ><img style="width: 100px; height: 100px;" src="img/loading-gif-animation.gif" /></div></center>
			
			<div id="msg-input-container">
			<div>
			<h3>Skicka ett meddelande</h3>
			<a href="#" class="window-button" id="new-msg-close">X</a><a href="#" class="window-button" id="new-msg-mini">_</a>
			</div>
			<div class="new-msg-field" style="width: 40%;">
			<div style="display: block;">
			<div style="display: inline-block;">
			<label for="msg-template">Meddelande-mall</label>
				<select name="msg-template" id="msg-template">
					<option value="" class="msg-template-item">* Nytt meddelande</option>
					<% for(String s : OrbitStamps.STATIC_TEMPLATE_MESSAGES) { %>
					<option value="<%= s %>" class="msg-template-item"><%= s %></option>
					<% } %>
				</select>
				</div>
				<div style="display: inline-block;">
				<label for="msg-callback">Avsändare</label>
				<input type="text" name="msg-callback" id="msg-callback">
				</div>
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
		</div>
		<!--  new mssage stop -->
		
		<div class="table-container">
		<div class="room-container" style="width: 40%;">
		<h2>Personal på sal</h2>
		<table class="table">
		
			<tr class="header-row">
			<td class="header">HSAID</td>
			<td class="header">Namn</td>
			<td class="header">Roll</td>
			</tr>
		<%
		int counter = 0;
		for(Person p : room.getPeople())
		{
		%>
			<tr alt="Klicka för att skicka ett meddelande" title="Klicka för att skicka ett meddelande" class="row clickable" id="row_<%= p.ID %>">
			<td class="cell personID"><% 
			if(p.ID.length() > 8)
				out.print(p.ID.substring(0,8));
			else
				out.print(p.ID);
			%></td>
			<td class="cell namn"><%= p.name %></td>
			<td class="cell roll <%= p.role.getRole().replaceAll(" ", "-") %>"><%= p.role.getRole() %></td>
			</tr>
		<%
		counter++;
		}
		%>
		</table> <!-- end table -->
		</div> <!-- end room -->
		
		<div id="history" style="width: 58%;">
		<h2>Meddelande historik</h2><span id="history-update"></span>
		<div id="history-table-container">
		<!-- history post start -->
		<table class="table">
		<tr class="header-row">
		<td class="header">Tidpunkt</td>
		<td class="header">Avsändare</td>
		<td class="header">HSAID</td>
		<td class="header">Nummer</td>
		<td class="header">Meddelande</td>
		<td class="header">Status</td>
		</tr>

		</table>
		<!-- history post end -->
		</div>
		</div> <!-- end history -->
		</div> <!-- end table-container -->
		<%
}
		%>
		</div>