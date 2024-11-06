package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jpabook.jpashop.domain.Member;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository // 스프링 빈으로 리포지토리 등록, 컴포넌트 스캔으로 등록이 됨
public class MemberRepository {
    @PersistenceContext // Jpa가 제공하는 표준 어노테이션으로, 스프링이 엔티티 매니저를 만들어서 걔를 여기에 injection 해줌
    private EntityManager em;

    public  void save(Member member) {
        em.persist(member); // 영속성 컨텍스트에 member 엔티티를 넣어줌, 이후에 트랜잭션이 커밋되는 시점에 DB에 반응이 됨. DB에 INSERT 쿼리가 날아가는 것임
    }
    public Member findOne(Long id) { // 회원 id로 회원 객체 단건 조회
        return em.find(Member.class, id); // jpa의 find 메소드를 사용함, 첫 번째 파라미터에 타입, 두 번째 파라미터에 pk를 넣어주면 됨.
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class) // JPQL을 사용하여 쿼리를 날려줌. SQL과 다른 점은 from의 대상이 테이블이 아니라 엔티티임.
                .getResultList();
    }

    public List<Member> findByName(String name) {
        return em.createQuery("select m from  Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }
}
