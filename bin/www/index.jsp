<%@ page import="roles.*" %>
<%@ page import="service.OrbitStamps" %>
<html>
<head>
<title>Systemöversikt</title></head>
<body>
<!-- Service start -->
<div>
<div>
	<h1>Logga till fil</h1>
	<%= OrbitStamps.LOG_TO_FILE %>
</div>
</div>
<!-- Service end -->
<!-- database start -->
<div>
<div>
	<h1>Databas</h1>
	<%= OrbitStamps.poller.serverUserName %>@<%= OrbitStamps.poller.serverIP %>
</div>

<div>
	<h1>Uppdateringsfrekvens:</h1>
	<%= OrbitStamps.poller.pollInterval %>
</div>
</div>
<!-- database end -->
</body>
</html>
