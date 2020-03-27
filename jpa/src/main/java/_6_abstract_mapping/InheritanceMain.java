package _6_abstract_mapping;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Persistence;

public class InheritanceMain {

	public static void main(String[] args) {
		// 전체 애플리케이션 실행 중에 단 한번만 실행 되어야 함.
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("devyu");
		
		// 엔티티매니저가 생성될때 영속성 컨텍스트가 같이 생성된다. 생명주기를 함께 한다.
		EntityManager em = emf.createEntityManager();
	
		// 트랜잭션 생성
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		
		// ==== 고급 테이블-객체 매핑(상속) ====
		try {
			
			// 1. [각각 테이블 전략]
			// 적용방식 : 부모엔티티 클래스 @Inheritance(strategy = InheritanceType.TABLE_PER_CLASS) 추가
			
			// 2. [단일 테이블 전략]
			// 적용방식 : 부모엔티티 클래스 @Inheritance(strategy = InheritanceType.SINGLE_TABLE) 추가
			// JPA 기본전략은 single-table 전략
			// 상속관계를 모두 하나의 테이블에 때려박아 생성함
			
			// 3. [조인전략 ]
			// 적용방식 : 부모엔티티 클래스 @Inheritance(strategy = InheritanceType.JOINED) 추가
			
			// INSERT
			Movie m = new Movie();
			m.setDirector("봉준호");
			m.setActor("송강호");
			m.setName("기생충");
			m.setPrice(12000);
			
			em.persist(m);
			
			
			
			// SELECT
			Movie findMovie = em.find(Movie.class, m.getId());
			
			em.flush();
			em.clear();
			
			Item findItem = em.find(Item.class, m.getId());
			
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
		} finally {
			em.close();
		}
		
		emf.close();
	}

}
