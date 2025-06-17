package in.co.rays.project_3.dto;

public class ColourDTO extends BaseDTO {
	 
	 private String colour ;

	public String getColour() {
		return colour;
	}

	public void setColour(String colour) {
		this.colour = colour;
	}

	@Override
	public String getKey() {
		return colour;
	}

	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		return colour;
	}
	 

}

