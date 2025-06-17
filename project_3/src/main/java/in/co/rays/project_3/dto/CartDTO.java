package in.co.rays.project_3.dto;

import java.util.Date;

public class CartDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;

	private String CustomerName;
	private String productName;
	private Date transactionDate;
	private int productQuantity;

	public String getCustomerName() {
		return CustomerName;
	}

	public void setCustomerName(String customerName) {
		CustomerName = customerName;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	public int getProductQuantity() {
		return productQuantity;
	}

	public void setProductQuantity(int productQuantity) {
		this.productQuantity = productQuantity;
	}

	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		return null;
	}

}