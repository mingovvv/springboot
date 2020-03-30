package _10_embeded_type;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

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
	@AttributeOverrides({
        		@AttributeOverride(
        				name="city",
        				column=@Column(name="home_city")),
        		@AttributeOverride(
        				name="street", 
                		column=@Column(name= "home_street")),
        		@AttributeOverride(
        				name="zipcode", 
    					column=@Column(name= "home_zipcode"))
	})
	private Address homeAddress;
	
	@Embedded
	private Address workAddress;

	@ElementCollection
	@CollectionTable(name = "FAVORITY_FOOD",
							  joinColumns = @JoinColumn(name="MEMBER_ID"))
	private Set<String> favorityFoods = new HashSet<String>();
	
	// 값 타입 매핑 방식
	@ElementCollection
	@CollectionTable(name = "ADDRESS",
							  joinColumns = @JoinColumn(name="MEMBER_ID"))
	private List<Address> addressHistory = new ArrayList<Address>();

	// 엔티티로 감싼 방식
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "MEMBER_ID")
	private List<AddressEntity> addressHistory2 = new ArrayList<AddressEntity>();
	
	
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

	public Period getPeriod() {
		return period;
	}

	public void setPeriod(Period period) {
		this.period = period;
	}

	public Address getHomeAddress() {
		return homeAddress;
	}

	public void setHomeAddress(Address homeAddress) {
		this.homeAddress = homeAddress;
	}

	public Address getWorkAddress() {
		return workAddress;
	}

	public void setWorkAddress(Address workAddress) {
		this.workAddress = workAddress;
	}

	public Set<String> getFavorityFoods() {
		return favorityFoods;
	}

	public void setFavorityFoods(Set<String> favorityFoods) {
		this.favorityFoods = favorityFoods;
	}

	public List<Address> getAddressHistory() {
		return addressHistory;
	}

	public void setAddressHistory(List<Address> addressHistory) {
		this.addressHistory = addressHistory;
	}
	

	
}
