package _6_super_mapped;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
//@DiscriminatorValue("앨범") // Dtype 칼럼에 들어가는 데이터 값, default는 Entity 명이 들어감
public class Album extends Item{

	private String artist;

	public String getArtist() {
		return artist;
	}
	public void setArtist(String artist) {
		this.artist = artist;
	}
	
}
