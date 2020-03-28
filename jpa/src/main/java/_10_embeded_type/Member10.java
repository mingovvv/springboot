package _10_embeded_type;

import java.time.LocalDateTime;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Member10 {

	@Id @GeneratedValue
	private Long id;
	
	private String name;
	
	// 기간
//	private LocalDateTime startDate; 
//	private LocalDateTime endDate;

	@Embedded
	private Period period;
	
	// 주소
//	private String city;
//	private String zipcode;
//	private String street;
	@Embedded
	private Adress adress;
}
