package _11_JPQL;

import javax.persistence.Embeddable;

@Embeddable
public class Address11 {
	private String city;
	private String street;
	private String zipcode;
	
	public Address11() {
		// TODO Auto-generated constructor stub
	}
	
	public Address11(String city, String street, String zipcode) {
		this.city = city;
		this.street = street;
		this.zipcode = zipcode;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getZipcode() {
		return zipcode;
	}
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	
	
}
