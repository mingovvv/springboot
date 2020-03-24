package _2_mapping;

import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity // JPA가 DB table과 매핑해주는 객체를 의미
@Table(name = "dev_t_Member") // 원하는 테이블 명을 지정할때 사용됨
public class Member2 {

	@Id
	private Long id;
	
	// 원하는 칼럼 명을 지정할때 사용됨, 유니크 제약조건, length 10 설정
	@Column(name = "dev_name", unique = true, length = 10) 
	private String name;
	
	// Null 값 허용 X
	@Column(nullable = false)
	private Integer age;
	
	@Enumerated(EnumType.STRING)
	private RoleType roleType;
	
	// java 1.8 미만 
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	
	// java 1.8 이상
	private LocalDateTime lastModifiedDate;
	
	@Lob
	private String description;
	
	@Transient // db 칼럼과 매핑하지 않는 필드를 의미
	// 메모리상에서만 임시로 값을 지니고 싶을때 사용
	private String etc;
}
