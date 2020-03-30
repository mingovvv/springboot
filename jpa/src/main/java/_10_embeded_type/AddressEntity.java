package _10_embeded_type;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class AddressEntity {
	@Id @GeneratedValue
	private Long id;
	
	private Address address;
	
	public AddressEntity() {
		// TODO Auto-generated constructor stub
	}

	public AddressEntity(String city, String street, String zip) {
		this.address = new Address(city, street, zip);
	}
	
}
