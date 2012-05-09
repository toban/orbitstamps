<%@ page import="service.model.Room" %>
<%@ page import="service.communication.Message" %>
<%@ page import="service.communication.CommunicationHistory" %>
<%@ page import="service.model.Person" %>
<%@ page import="java.util.Map.Entry" %>
<%@ page import="service.OrbitStamps" %>
<%@ page import="java.util.ArrayList" %>

<script>
$(document).ready(function()
{
	$.post("includes/functions/getAllRooms.jsp",
			   function(data) 
			   {
		
			 	 contentContainer = $("#content");
			     var obj = $.parseJSON(data);
			     var rooms = new Object();
			     for(var i = 0; i < obj.rooms.length; i++)
			    	 {
			    	 	
			    	 	var item = obj.rooms[i];
			    	 	if(typeof rooms[item.locationName] == 'undefined')
			    	 		{
			    	 			rooms[item.locationName] = new Array();
			
			    	 		}
			    	 	rooms[item.locationName].push(item);
			    	 }
			     populate(rooms);
			   });
	var populate = function(rooms)
	{
		
	     for (var key in rooms) {
	    	   var obj = rooms[key];
	    	  	contentContainer.append("<h2>"+key+"</h2><br />")
	    	   for (var prop in obj) 
	    	   {
	    		   item = $("<div class='room-overview-container'><a href='?page=2&room="+obj[prop].id+"'>"+obj[prop].name+"</a></div>");
	    		   contentContainer.append(item);
	    	      console.log();
	    	   }
	    	}
		
	}
});
</script>
<div id="content">		
</div>