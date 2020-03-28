package jpa.practice.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import jpa.practice.domain.item.Item;

@Entity
public class Category extends BaseEntity{
	@Id @GeneratedValue
	@Column(name =  "CATERORY_ID")
	private Long id;
	private String name;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PARENT_ID")
	private Category parent;
	
	@OneToMany(mappedBy = "parent")
	private List<Category> child = new ArrayList<Category>();
	
	@ManyToMany
	@JoinTable(name = "CATEGORY_ITEM",
					joinColumns = @JoinColumn(name="CATEGORY_ID"),
					inverseJoinColumns = @JoinColumn(name="ITEM_ID")
	)
	private List<Item> Items = new ArrayList<Item>();
}
