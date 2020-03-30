package _11_JPQL;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Product11 {

	@Id @GeneratedValue
	private Long id;

	private String name;
	private int price;
	private int stockAmount;
	
	
	
}
