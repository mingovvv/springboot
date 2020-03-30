package _10_embeded_type;

public class MemberDTO {

	private int age;
	private String username;
	
	
	public MemberDTO(int age, String username) {
		this.age = age;
		this.username = username;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	
}
