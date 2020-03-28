package _9_cascade;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class cascadeMain {

	public static void main(String[] args) {
		// 전체 애플리케이션 실행 중에 단 한번만 실행 되어야 함.
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("devyu");
		
		// 엔티티매니저가 생성될때 영속성 컨텍스트가 같이 생성된다. 생명주기를 함께 한다.
		EntityManager em = emf.createEntityManager();
	
		// 트랜잭션 생성
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		
		// ==== cascade 속성  ====
		try {

//			1. [cascade = CascadeType.ALL]
			
			// cascade 속성 적용하지 않았을 경우
			Child child1 = new Child();
			Child child2 = new Child();
			
			Parent parent = new Parent();
			parent.addChild(child1);
			parent.addChild(child2);
			
			em.persist(parent);
			// cascade = CascadeType.ALL 속성을 넣어주면 아래 persist가 필요없다
//			em.persist(child1);
//			em.persist(child2);
			
			em.flush();
			em.clear();
			
//			2. [orphanRemoval = true]
			
			// 부모엔티티와 연관관계가 끊긴 자식엔티티를 자동으로 삭제
			// delete 쿼리 작동
			Parent findParent = em.find(Parent.class, parent.getId());
			findParent.getChildList().remove(0);
			
			
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
		} finally {
			em.close();
		}
		
		emf.close();
	}

}
