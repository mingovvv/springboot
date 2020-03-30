package _10_embeded_type;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class embededMain {

	public static void main(String[] args) {
		// 전체 애플리케이션 실행 중에 단 한번만 실행 되어야 함.
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("devyu");
		
		// 엔티티매니저가 생성될때 영속성 컨텍스트가 같이 생성된다. 생명주기를 함께 한다.
		EntityManager em = emf.createEntityManager();
	
		// 트랜잭션 생성
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		
		// ==== embeded 속성  ====
		try {

			// 임베디드 타입
			Address address1 = new Address("서울", "192-31", "오목로");
			
			Member10 member1 = new Member10();
			member1.setName("devyu");
			member1.setHomeAddress(address1);
			em.persist(member1);
			
			// 방법 1.
			// 입베디드 타입은 공유되기 때문에 새롭게 생성
			// 생성하고 값을 복사하는 방량으로
			Address address2 = new Address(address1.getCity(), address1.getZipcode(), address1.getStreet());
			
			Member10 member2 = new Member10();
			member2.setName("puregyu");
			member2.setHomeAddress(address2);
			em.persist(member2);
			
			// 임베디드 타입의 값은 공유 속성이 있음
			// Side-Effect효과로 인해 member1과 member2 모두 거리명이 변경됨
			member1.getHomeAddress().setStreet("신정로");
			
			// 방법 2. 
			// 원천 차단. Address setter 모두 삭제하고 생성자로 생성되도록 유도
			
			
			// === 값 타입 컬렉션 ===
			Member10 member3 = new Member10();
			member3.setName("민짱");
			member3.setHomeAddress(new Address("서울", "192-31", "마포로"));
			member3.getFavorityFoods().add("피자");
			member3.getFavorityFoods().add("사과");
			member3.getFavorityFoods().add("우유");
			
			member3.getAddressHistory().add(new Address("대구", "192-31", "마포로"));
			member3.getAddressHistory().add(new Address("부산", "192-31", "마포로"));
			member3.getAddressHistory().add(new Address("우산", "192-31", "마포로"));
			
			em.persist(member3);
			
			Member10 member4 = new Member10();
			member4.setName("뉴민짱");
			member4.getFavorityFoods().add("피자");
			member4.getFavorityFoods().add("사과");
			member4.getFavorityFoods().add("우유");
			
			em.persist(member4);
			
			em.flush();
			em.clear();
			
			// 값 컬렉션 지연로딩 확인
			Member10 findMember = em.find(Member10.class, member3.getId());
			List<Address> addressHistory = findMember.getAddressHistory();

			// 값타입의 수정은 immutable을 위한 값 타입 새로 생성
			// 인스턴스 자체를 갈아 끼워야함
			findMember.setHomeAddress(new Address("광주", "123-44", "광주로"));
			
			// 값 타입 컬렉션 수정
			// 사과 -> 바나나
			findMember.getFavorityFoods().remove("사과");
			findMember.getFavorityFoods().add("바나나");
			
			// 우산 -> 부산
			// address hashCode(), equals 생성
//			findMember.getAddressHistory().remove(new Address("우산", "192-31", "마포로"));
//			findMember.getAddressHistory().add(new Address("부산", "192-31", "마포로"));
			
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
		} finally {
			em.close();
		}
		emf.close();
	}

}
