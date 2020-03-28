package jpa.practice.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
// Order가 DB 예약어로 지정된 데이터베이스가 존재하기 때문에 이름을 약간 변경시켜줌.
@Table(name = "orders")
public class Order extends BaseEntity{

	@Id @GeneratedValue
	@Column(name =  "ORDERS_ID")
	private Long id;
	private LocalDateTime orderDate;
	@Enumerated(EnumType.STRING)
	private OrderStatus status;
	
//	@Column(name = "MEMBER_ID")
//	private Long memberId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="MEMBER_ID")
	private Member member;
	
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	private List<OrderItem> orderItems = new ArrayList<OrderItem>();
	
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "DELIVERY_ID")
	private Delivery delivery;
}
