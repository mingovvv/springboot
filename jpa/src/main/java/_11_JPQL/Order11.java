package _11_JPQL;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Order11 {

	@Id @GeneratedValue
	private Long id;

	private int orderAmount;
	
	@Embedded
	private Address11 address;
	
	@ManyToOne
	@JoinColumn(name="PRODUCT_ID")
	private Product11 product11;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(int orderAmount) {
		this.orderAmount = orderAmount;
	}

	public Address11 getAddress() {
		return address;
	}

	public void setAddress(Address11 address) {
		this.address = address;
	}

	public Product11 getProduct11() {
		return product11;
	}

	public void setProduct11(Product11 product11) {
		this.product11 = product11;
	}
 	
	
}
