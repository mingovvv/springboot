package _1_standard;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class FlushMain {
	public static void main(String[] args) {
		
		// 전체 애플리케이션 실행 중에 단 한번만 실행 되어야 함.
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("devyu");
		
		EntityManager em = emf.createEntityManager();
	
		// 트랜잭션 생성
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		
		// flush - 영속성 컨텍스트의 변경 내용을 데이터베이스에 반영
		try {
			
			Member1 member = new Member1();
			member.setId(1L);
			member.setName("min");
			
			em.persist(member);
			// flush를 강제로 호출
			// SQL insert 쿼리가 즉시 날라감
			em.flush();
			
			// 영속상태의 엔티티를 준영속상태로 만드는것
//			em.detach(member); // 1차캐시에서 해당하는 엔티티만 지움
//			em.clear(); // 1차캐시를 통으로 지움
			
			System.out.println(" === 경계 ===");
			
			// commit() 에는 자동 flush() 기능이 함께 포함되어 있음
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
		} finally {
			em.close();
		}
		
		emf.close();
	}
}
