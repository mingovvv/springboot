package _5_relationship_apply_bidirectional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class relationshipMain {

	public static void main(String[] args) {
		// 전체 애플리케이션 실행 중에 단 한번만 실행 되어야 함.
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("devyu");
		
		// 엔티티매니저가 생성될때 영속성 컨텍스트가 같이 생성된다. 생명주기를 함께 한다.
		EntityManager em = emf.createEntityManager();
	
		// 트랜잭션 생성
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		
		// ==== 연관관계를 사용한 테이블-객체 매핑 ====
		try {
			
			Team5 team = new Team5();
			team.setName("IT사업부");
			em.persist(team);
			
			Member5 member = new Member5();
			member.setName("장민규");
			em.persist(member);

			// 연관관계 주입
			member.setTeam(team);
			team.getMembers().add(member);
			
			// 쿼리 날리기
			em.flush();
			// 1차 캐시 초기화
			em.clear();
			
			// 연관관계를 적용해서 간편해진 코드
			// 특정 맴버의 team name을 알고싶다면?
			Member5 findMember = em.find(Member5.class, member.getId());
			
			for (Member5 m : findMember.getTeam().getMembers()) {
				System.out.println(m.getName());
			}
			
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
		} finally {
			em.close();
		}
		
		emf.close();
	}

}
