package _7_mappedSuperclass;

import java.time.LocalDateTime;

import javax.persistence.MappedSuperclass;

// 엔티티가 아님, 테이블 생성 또한 X
@MappedSuperclass
public abstract class BaseEntity {

	private String createdBy;
	private String modifiedBy;
	private LocalDateTime createdDate;
	private LocalDateTime modifiedDate;
	
}
