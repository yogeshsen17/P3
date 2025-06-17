<%@page import="in.co.rays.project_3.controller.ORSView"%>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Welcome Page</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="/resources/demos/style.css">
<style>
body {
	background-image: url('<%=ORSView.APP_CONTEXT%>/img/Background.avif');
	background-size: cover;
	background-repeat: no repeat;
}


.text-cs1 {
	/* font-family: Lucida Calligraphy; */
	font-family: Monotype Corsiva;
}
</style>

</head>
<body class="img-fluid">

	<%@include file="Header.jsp"%>

	<div class="text-cs1" style="text-align: center;">

		<h1 style="padding-top: 15%; color: red; font-size: 50px;">
			<b>Welcome To Online Result System</b>
		</h1>

	</div>

	<%@include file="FooterView.jsp"%>

</body>

</html>