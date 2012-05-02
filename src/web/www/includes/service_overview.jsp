<%@ page import="service.OrbitStamps" %>
<%@ page import="service.Config" %>
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
	<%= Config.DATABASE_NAME %>@<%= Config.DATABASE_IP %>
</div>

<div>
	<h1>Uppdateringsfrekvens:</h1>
	<%= OrbitStamps.poller.pollInterval %>
</div>
</div>
<!-- database end -->
