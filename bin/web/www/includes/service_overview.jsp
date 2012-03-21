<%@ page import="service.OrbitStamps" %>
<!-- Service start -->
<div>
<div>
	<h1>Logga till fil</h1>
	<%= OrbitStamps.LOG_TO_FILE %>
</div>
<div>
	<h1>Antal filter</h1>
	<%= OrbitStamps.filterManager.filters.size() %>
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
