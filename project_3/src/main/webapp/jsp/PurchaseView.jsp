<%@page import="java.util.List"%>
<%@page import="in.co.rays.project_3.controller.PurchaseCtl"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="in.co.rays.project_3.util.HTMLUtility"%>
<%@page import="in.co.rays.project_3.util.DataUtility"%>
<%@page import="in.co.rays.project_3.util.ServletUtility"%>
<%@page import="in.co.rays.project_3.controller.ORSView"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Purchase view</title>
<script src="<%=ORSView.APP_CONTEXT%>/js/Utility.js"></script>
<script src="<%=ORSView.APP_CONTEXT%>/js/Validate.js"></script>
<meta name="viewport" content="width=device-width, initial-scale=1">
<style type="text/css">
i.css {
	border: 2px solid #8080803b;
	padding-left: 10px;
	padding-bottom: 11px;
	background-color: #ebebe0;
}

.input-group-addon {
	/* box-shadow: 9px 8px 7px #001a33; */
	background-image: linear-gradient(to bottom right, orange, black);
	background-repeat: no repeat;
	background-size: 100%;
	padding-bottom: 11px;
}

.hm {
	background-image: url('<%=ORSView.APP_CONTEXT%>/img/unsplash.jpg');
	background-size: cover;
	padding-top: 6%;
}
</style>

</head>
<body class="hm">
	<div class="header">
		<%@include file="Header.jsp"%>
		<%@include file="calendar.jsp"%>
	</div>
	<div>

		<main>
		<form action="<%=ORSView.PURCHASE_CTL%>" method="post">
			<jsp:useBean id="dto" class="in.co.rays.project_3.dto.PurchaseDTO"
				scope="request"></jsp:useBean>
			<div class="row pt-3">
				<!-- Grid column -->
				<div class="col-md-4 mb-4"></div>
				<div class="col-md-4 mb-4">
					<div class="card input-group-addon">
						<div class="card-body">

							<%
								long id = DataUtility.getLong(request.getParameter("id"));

								if (dto.getId() != null && id > 0) {
							%>


							<h3 class="text-center default-text text-primary">Update
								Purchase</h3>
							<%
								} else {
							%>
							<h3 class="text-center default-text text-primary">Add
								Purchase</h3>
							<%
								}
							%>
							<!--Body-->
							<div>

								<H4 align="center">
									<%
										if (!ServletUtility.getSuccessMessage(request).equals("")) {
									%>
									<div class="alert alert-success alert-dismissible">
										<button type="button" class="close" data-dismiss="alert">&times;</button>
										<%=ServletUtility.getSuccessMessage(request)%>
									</div>
									<%
										}
									%>
								</H4>

								<H4 align="center">
									<%
										if (!ServletUtility.getErrorMessage(request).equals("")) {
									%>
									<div class="alert alert-danger alert-dismissible">
										<button type="button" class="close" data-dismiss="alert">&times;</button>
										<%=ServletUtility.getErrorMessage(request)%>
									</div>
									<%
										}
									%>

								</H4>

								<input type="hidden" name="id" value="<%=dto.getId()%>">

							</div>

							<div class="md-form">

								<span class="pl-sm-5"><b>Total Quantity</b> <span
									style="color: red;">*</span></span> </br>
								<div class="col-sm-12">
									<div class="input-group">
										<div class="input-group-prepend">
											<div class="input-group-text">
												<i class="fa fa-user-alt grey-text" style="font-size: 1rem;"></i>
											</div>
										</div>
										<input type="text" class="form-control" name="totalQuantity"
											placeholder="Total Quantity"
											value="<%=DataUtility.getStringData(dto.getTotalQuantity()).equals("0") ? ""
					: DataUtility.getStringData(dto.getTotalQuantity())%>">
									</div>
								</div>
								<font color="red" class="pl-sm-5"> <%=ServletUtility.getErrorMessage("totalQuantity", request)%></font></br>

								<span class="pl-sm-5"><b>Total Cost</b> <span
									style="color: red;">*</span></span> </br>
								<div class="col-sm-12">
									<div class="input-group">
										<div class="input-group-prepend">
											<div class="input-group-text">
												<i class="fa fa-user-alt grey-text" style="font-size: 1rem;"></i>
											</div>
										</div>
										<input type="text" class="form-control" name="totalCost"
											placeholder="Total Cost"
											value="<%=DataUtility.getStringData(dto.getTotalCost()).equals("0") ? ""
					: DataUtility.getStringData(dto.getTotalCost())%>">
									</div>
								</div>
								<font color="red" class="pl-sm-5"> <%=ServletUtility.getErrorMessage("totalCost", request)%></font></br>


								<span class="pl-sm-5"><b>Order Date</b> <span
									style="color: red;">*</span></span></br>
								<div class="col-sm-12">
									<div class="input-group">
										<div class="input-group-prepend">
											<div class="input-group-text">
												<i class="fa fa-calendar grey-text" style="font-size: 1rem;"></i>
											</div>
										</div>
										<input type="text" id="datepicker2" name="orderDate"
											class="form-control" placeholder="Order Date"
											readonly="readonly"
											value="<%=DataUtility.getDateString(dto.getOrderDate())%>">
									</div>
								</div>
								<font color="red" class="pl-sm-5"> <%=ServletUtility.getErrorMessage("orderDate", request)%></font></br>

								<span class="pl-sm-5"><b>Product</b><span
									style="color: red;">*</span></span> </br>

								<div class="col-sm-12">
									<div class="input-group">
										<div class="input-group-prepend">
											<div class="input-group-text">
												<i class="fa fa-calender grey-text" style="font-size: 1rem;"></i>
											</div>
										</div>

										<%
											Map<Integer, String> map = new HashMap();

											map.put(1, "Mobile");
											map.put(2, "Perfume");
											map.put(3, "Book");

											String hlist = HTMLUtility.getList2("product", DataUtility.getStringData(dto.getProduct()), map);
										%>
										<%=hlist%>

									</div>
									<font color="red" class="pl-sm-5"> <%=ServletUtility.getErrorMessage("product", request)%></font></br>

									<%
										if (dto.getId() != null && id > 0) {
									%>

									<div class="text-center">

										<input type="submit" name="operation"
											class="btn btn-success btn-md" style="font-size: 17px"
											value="<%=PurchaseCtl.OP_UPDATE%>"> <input
											type="submit" name="operation" class="btn btn-warning btn-md"
											style="font-size: 17px" value="<%=PurchaseCtl.OP_CANCEL%>">

									</div>
									<%
										} else {
									%>
									<div class="text-center">

										<input type="submit" name="operation"
											class="btn btn-success btn-md" style="font-size: 17px"
											value="<%=PurchaseCtl.OP_SAVE%>"> <input
											type="submit" name="operation" class="btn btn-warning btn-md"
											style="font-size: 17px" value="<%=PurchaseCtl.OP_RESET%>">
									</div>

								</div>
								<%
									}
								%>
							</div>
						</div>
		</form>
		</main>
		<div class="col-md-4 mb-4"></div>

	</div>

</body>
<%@include file="FooterView.jsp"%>

</body>
</html>