package _11_JPQL;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Team11 {

	@Id @GeneratedValue
	private Long id;
	
	private String name;
	
	@OneToMany(mappedBy = "team")
	private List<Member11> memList = new ArrayList<Member11>();
	
	
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

	public List<Member11> getMemList() {
		return memList;
	}

	public void setMemList(List<Member11> memList) {
		this.memList = memList;
	}
	
}
