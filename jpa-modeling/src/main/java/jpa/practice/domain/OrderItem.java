package jpa.practice.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class OrderItem {
	@Id @GeneratedValue
	@Column(name =  "ORDER_ITEM_ID")
	private Long id;

//	@Column(name =  "ORDER_ID")
//	private Long orderId;
//	
//	@Column(name =  "ITEM_ID")
//	private Long itemId;
	
	@ManyToOne
	@JoinColumn(name = "ORDER_ID")
	private Order order;
	
	@ManyToOne
	@JoinColumn(name = "ITEM_ID")
	private Item item;
	
	private int orderPirce;
	private int count;
	
}
