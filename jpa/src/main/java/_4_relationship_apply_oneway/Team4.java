package _4_relationship_apply_oneway;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author min
 * @설명 단방향 매핑관계를 표현하기 위한 객체
 * 			회원 : 팀 / N : 1 
 */
@Entity
public class Team4 {
	@Id @GeneratedValue
	@Column(name = "TEAN_ID")
	private Long id;
	
	private String name;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
}
