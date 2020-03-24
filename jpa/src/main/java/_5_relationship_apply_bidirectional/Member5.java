package _5_relationship_apply_bidirectional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * @author min
 * @설명 양방향 매핑관계를 표현하기 위한 객체
 * 			회원 : 팀 / N : 1 
 */
@Entity
public class Member5 {
	@Id @GeneratedValue
	@Column(name = "MEMBER_ID")
	private Long id;
	
	private String name;
	
	@ManyToOne
	@JoinColumn(name = "TEAM_ID")
	private Team5 team;

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

	public Team5 getTeam() {
		return team;
	}

	public void setTeam(Team5 team) {
		this.team = team;
	}

	
}
