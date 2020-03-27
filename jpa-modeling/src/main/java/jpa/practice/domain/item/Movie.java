package jpa.practice.domain.item;

import javax.persistence.Entity;

@Entity
public class Movie extends Item{

	private String actor;
	private String director;
}
