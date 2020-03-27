package _8_fetchType;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class fetchMain {

	public static void main(String[] args) {
		// 전체 애플리케이션 실행 중에 단 한번만 실행 되어야 함.
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("devyu");
		
		// 엔티티매니저가 생성될때 영속성 컨텍스트가 같이 생성된다. 생명주기를 함께 한다.
		EntityManager em = emf.createEntityManager();
	
		// 트랜잭션 생성
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		
		// ==== fetch = FetchType.LAZY or FetchType.EAGER  ====
		try {
			Team8 team = new Team8();
			team.setName("IT연구부");
			
			em.persist(team);
			
			Team8 team2 = new Team8();
			team2.setName("경영관리부");
			
			em.persist(team2);
			
			Member8 member = new Member8();
			member.setName("장민규");
			member.setTeam(team);
			
			em.persist(member);
			
			Member8 member2 = new Member8();
			member2.setName("임수빈");
			member2.setTeam(team2);
			
			em.persist(member2);

			
			// 영속성 컨텍스트에 저장된 쓰기지연 SQL저장소의 쿼리를 commit() 전에 보냄
			em.flush();
			// 영속성 컨텍스트 clear
			em.clear();
			
			// 영속성 컨텍스트가 비워진 상태에서의 select
//			Member8 findMember = em.find(Member8.class, member.getId());

			// fetch = FetchType.LAZY
//			System.out.println("test : "+findMember.getTeam().getName()); // 초기화가 이루어짐, DB 접근
			
			// fetch = FetchType.EAGER
			// JSQL 사용시, 즉시로딩의 경우 N+1의 이슈가 발생
			// JSQL에서 member를 조회할 경우 JSQL은 SQL로 변환되어 "select * from Member8" 이 실행된다.
			List<Member8> resultList = em.createQuery("select m from Member8  m", Member8.class).getResultList(); 
			
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
		} finally {
			em.close();
		}
		
		emf.close();
	}

}
