package _6_super_mapped;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
//@DiscriminatorValue("책") // Dtype 칼럼에 들어가는 데이터 값, default는 Entity 명이 들어감
public class Book extends Item{

	private String author;
	private String isbn;
	
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getIsbn() {
		return isbn;
	}
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
}
