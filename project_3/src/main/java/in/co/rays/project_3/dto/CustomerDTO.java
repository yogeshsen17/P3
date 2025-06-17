package in.co.rays.project_3.dto;

public class CustomerDTO extends BaseDTO{

	private String clientName;
	private String location;
	private long contactNumber;
	private int importance;

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public long getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(long contactNumber) {
		this.contactNumber = contactNumber;
	}

	public int getImportance() {
		return importance;
	}

	public void setImportance(int importance) {
		this.importance = importance;
	} 
	
    @Override
    public String toString() {
    	// TODO Auto-generated method stub
    	return "Customer {id== "+id+" clientName = "+clientName+ "location== "+location + "contactNumber =="+ contactNumber +" importance = "+ importance+ "}";
    }

	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		return clientName;
	}

}
