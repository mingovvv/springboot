package _1_standard;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class CURDMain {
	public static void main(String[] args) {
		
		// 전체 애플리케이션 실행 중에 단 한번만 실행 되어야 함.
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("devyu");
		
		EntityManager em = emf.createEntityManager();
	
		// 트랜잭션 생성
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		
		// [create]
		
//		
//		try {
//			Member member = new Member();
//			member.setId(1L);
//			member.setName("devyu");
//			
//			em.persist(member);
//			tx.commit();
//		} catch (Exception e) {
//			tx.rollback();
//		} finally {
//			em.close();
//		}
//		
		// [update]
		
//		try {
//
//			// 트랜잭션이 커밋되는 순간에 변경감지를 통해 1차캐시에 존재하는 엔티티의 변경사항을 체크한다.
//			// 변경된 부분은 update SQL을 통해 자동 수정된다.
//			Member findMember = em.find(Member.class, 1L);
//			findMember.setName("puregyu");
//			
//			tx.commit();
//		} catch (Exception e) {
//			tx.rollback();
//		} finally {
//			em.close();
//		}
		
		// [read]
//		try {
//			
//			// 1. 간단한 읽기
//			Member findMember = em.find(Member.class, 1L);
//			
//			// 2. JPQL을 통한 읽기
//			// JPQL은 테이블을 대상으로 쿼리를 날리는 것이 아닌 객체를 대상으로 쿼리를 날리는 방식
//			List<Member> resultList = em.createQuery("select m from Member m", Member.class).getResultList();
//			
//			
//			tx.commit();
//		} catch (Exception e) {
//			tx.rollback();
//		} finally {
//			em.close();
//		}
		
		// [delete]
		try {
			
			Member1 findMember = em.find(Member1.class, 1L);
			em.remove(findMember);
			
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
		} finally {
			em.close();
		}
		
		emf.close();
	}
}
