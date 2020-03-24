package _2_mapping;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class mappingMain {

	public static void main(String[] args) {
		// 전체 애플리케이션 실행 중에 단 한번만 실행 되어야 함.
				EntityManagerFactory emf = Persistence.createEntityManagerFactory("devyu");
				
				// 엔티티매니저가 생성될때 영속성 컨텍스트가 같이 생성된다. 생명주기를 함께 한다.
				EntityManager em = emf.createEntityManager();
			
				// 트랜잭션 생성
				EntityTransaction tx = em.getTransaction();
				tx.begin();
				
				try {
					
					tx.commit();
				} catch (Exception e) {
					tx.rollback();
				} finally {
					em.close();
				}
				
				emf.close();
	}

}
