package _1_standard;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class PersistenceContextMain {
	public static void main(String[] args) {
		
		// 전체 애플리케이션 실행 중에 단 한번만 실행 되어야 함.
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("devyu");
		
		// 엔티티매니저가 생성될때 영속성 컨텍스트가 같이 생성된다. 생명주기를 함께 한다.
		EntityManager em = emf.createEntityManager();
	
		// 트랜잭션 생성
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		
		// 영속성 컨텍스트
		try {
			
//			Member member = new Member();
//			member.setId(2L);
//			member.setName("min");
			
			// 영속성 컨텍스트 1차 캐시에 보관
//			em.persist(member);
			
			// @Id:2L 값을 지닌 엔티티 Member는 이미 영속성 컨텍스트에 존재하고 있다.
			// 고로 DB에 select를 날리지 않는다.
			// 1차캐시에 없다면 DB를 조회한다.
//			em.find(Member.class, 2L);
			
			// 영속 엔티티의 동일성 보장 TEST
//			Member find1 = em.find(Member.class, 2L);
//			Member find2 = em.find(Member.class, 2L);
//			System.out.println(find1 == find2);
			
			// 쓰기 지연 SQL 저장소 TEST
			Member member = em.find(Member.class, 1L);
			System.out.println("테스트1");
			em.remove(member);
			System.out.println("테스트2");
			
			// 테스트1, 테스트2가 출력되고 commit할때 DB에 영향을 미침
			// commit 되는 순간 영속성 컨텍스트에 보관된 엔티티가 DB에 SQL을 날림
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
		} finally {
			em.close();
		}
		
		emf.close();
	}
}
