package _7_mappedSuperclass;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class User extends BaseEntity{

	@Id @GeneratedValue
	private Long id;
}
