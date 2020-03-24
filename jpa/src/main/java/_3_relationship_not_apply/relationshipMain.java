package _3_relationship_not_apply;

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
		
		// ==== 연관관계를 사용하지 않는 테이블-객체 매핑 ====
		try {
			
			Team3 team = new Team3();
			team.setName("IT사업부");
			em.persist(team);
			
			Member3 member = new Member3();
			member.setName("장민규");
			// @GeneratedValue GenerationType.IDENTITY  설정의 경우
			// DB에 맞는 방식으로 id값을 자동 추가해준다.
			// 보통 persist()의 경우 영속성 컨텍스트의 쓰기지연 SQL저장소에 저장된 쿼리가
			// commit()과 동시에 flush()되면서 SQL을 날리지만..
			// 이렇게 id가 미리 필요한 경우가 있는데 이 경우에는
			// persist()와 동시에 쿼리를 날리고 id값을 반환받는다.
			member.setTeamId(team.getId());
			em.persist(member);
			
			// 연관관계를 적용하지 않은 테이블 객체-매핑의 문제점
			// 특정 맴버의 team name을 알고싶다면?
			Member3 findMember = em.find(Member3.class, member.getId());
			Long teamId = findMember.getTeamId();
			Team3 findTeam = em.find(Team3.class, teamId);
			System.out.println("해당 맴버의 팀명은 ? " + findTeam.getName());
			// 굉장히 객체지향스럽지 않다...
			
			
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
		} finally {
			em.close();
		}
		
		emf.close();
	}

}
