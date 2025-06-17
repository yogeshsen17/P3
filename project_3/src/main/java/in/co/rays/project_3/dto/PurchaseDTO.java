package in.co.rays.project_3.dto;

import java.util.Date;

public class PurchaseDTO extends BaseDTO {

	private int totalQuantity;
	private int product;
	private Date orderDate;
	private int totalCost;

	public int getTotalQuantity() {
		return totalQuantity;
	}

	public void setTotalQuantity(int totalQuantity) {
		this.totalQuantity = totalQuantity;
	}

	public int getProduct() {
		return product;
	}

	public void setProduct(int product) {
		this.product = product;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public int getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(int totalCost) {
		this.totalCost = totalCost;
	}

	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		return totalQuantity + "";
	}

}
