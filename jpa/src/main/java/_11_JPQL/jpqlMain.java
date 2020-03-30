package _11_JPQL;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import _10_embeded_type.MemberDTO;

public class jpqlMain {

	public static void main(String[] args) {
		// 전체 애플리케이션 실행 중에 단 한번만 실행 되어야 함.
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("devyu");
		
		// 엔티티매니저가 생성될때 영속성 컨텍스트가 같이 생성된다. 생명주기를 함께 한다.
		EntityManager em = emf.createEntityManager();
	
		// 트랜잭션 생성
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		
		// ==== JPQL 기본 문법 ====
		try {

//			Member11 member = new Member11();
//			member.setUsername("민규");
//			member.setAge(10);
//			em.persist(member);
			
			// TypedQuery ? 타입 정보를 받을 수 있을 때
//			TypedQuery<Member11> createQuery = em.createQuery("select m from Member11 m", Member11.class);
//			TypedQuery<Integer> createQuery2 = em.createQuery("select m.age from Member11 m", Integer.class);
			
			// Query ? 타입 정보를 받을 수 없을 때(String과 int)
//			Query createQuery3 = em.createQuery("select m.age, m.username from Member11 m");
//			
			// 바인딩 방식(이름 기준)
//			em.createQuery("select m from Member11 m where m.username=:username")
//			.setParameter("username", "devyu")
//			.getSingleResult();
//			
			// 바인딩 방식(숫자 기준)
//			em.createQuery("select m from Member11 m where m.username=?1")
//			.setParameter(1, "devyu")
//			.getSingleResult();
//			
			// 단순 값 받기 Object[]
//			List<Object[]> resultList = em.createQuery("select m.age, m.username from Member11 m")
//												.getResultList();
//			
//			System.out.println(resultList.get(0)[0]);
//			System.out.println(resultList.get(0)[1]);
			
			// 단순 값 받기 new DTO
//			 List<MemberDTO> resultList2 = em.createQuery("select new _10_embeded_type.MemberDTO(m.age, m.username) from Member11 m", MemberDTO.class)
//					 							.getResultList();
//			 
//			 System.out.println(resultList2.get(0).getUsername());
//			 System.out.println(resultList2.get(0).getAge());
			 
			 // 페이징 방식
//			 List<Member11> resultList3 = em.createQuery("select m from Member11 m order by m.age desc", Member11.class)
//			 .setFirstResult(1)
//			 .setMaxResults(10)
//			 .getResultList();
			 
			 // 조인
			 Team11 team2 = new Team11();
			 team2.setName("IT");
			 em.persist(team2);
			 
			 Member11 member2 = new Member11();
			 member2.setUsername("민규");
			 
			 member2.setTeam(team2);
			 team2.getMemList().add(member2);
			  
			 em.persist(member2);
			 
			 em.flush();
			 em.clear();
			 
			 List<Member11> resultList4 = em.createQuery("select m from Member11 m inner join m.team t",Member11.class)
					 .getResultList();
			 
			 
			 // 서브쿼리
			 
			 
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
		} finally {
			em.close();
		}
		emf.close();
	}

}
