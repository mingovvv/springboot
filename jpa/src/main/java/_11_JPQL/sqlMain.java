package _11_JPQL;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class sqlMain {

	public static void main(String[] args) {
		// 전체 애플리케이션 실행 중에 단 한번만 실행 되어야 함.
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("devyu");
		
		// 엔티티매니저가 생성될때 영속성 컨텍스트가 같이 생성된다. 생명주기를 함께 한다.
		EntityManager em = emf.createEntityManager();
	
		// 트랜잭션 생성
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		
		// ==== SQL 기본 ====
		try {

			// [1. JSQL]
			// 쿼리의 Member11은 테이블이 아닌 엔티티를 의미함.
//			List<Member11> resultList = em.createQuery("select m from Member11 m", Member11.class).getResultList();
			
			// [2. Criteria] 
//			CriteriaBuilder cb = em.getCriteriaBuilder();
//			CriteriaQuery<Member11> query = cb.createQuery(Member11.class);
			//루트 클래스 (조회를 시작할 클래스)
//			Root<Member11> m = query.from(Member11.class);
			//쿼리 생성 
//			CriteriaQuery<Member11> cq = query.select(m).where(cb.equal(m.get("username"), "kim")); 
//			List<Member11> resultList2 = em.createQuery(cq).getResultList();

			
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
		} finally {
			em.close();
		}
		emf.close();
	}

}
