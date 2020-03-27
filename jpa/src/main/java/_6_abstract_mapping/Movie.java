package _6_abstract_mapping;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
//@DiscriminatorValue("영화") // Dtype 칼럼에 들어가는 데이터 값, default는 Entity 명이 들어감
public class Movie extends Item{

	private String director;
	private String actor;
	
	public String getDirector() {
		return director;
	}
	public void setDirector(String director) {
		this.director = director;
	}
	public String getActor() {
		return actor;
	}
	public void setActor(String actor) {
		this.actor = actor;
	}
}
