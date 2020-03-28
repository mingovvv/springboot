package _10_embeded_type;

import javax.persistence.Embeddable;

@Embeddable
public class Adress {
	private String city;
	private String zipcode;
	private String street;
	
	public Adress() {

	}

	public Adress(String city, String zipcode, String street) {
		this.city = city;
		this.zipcode = zipcode;
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}
}
