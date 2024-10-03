package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) // JPA의 모든 데이터 변경이나 로직들은 가급적이면 다 트랜잭션 안에서 실행되어야함. 그래야 LAZY 이런 것들이 다 작동함.
// 데이터 변경하는 건 트랜잭션이 꼭 필요함
// 지금 대부분의 public 메소드는 읽기만 하면 좋으므로 이렇게 해놓고, 아래 join 메소드만 따로 달아주면 성능을 최적화할 수 있음
@RequiredArgsConstructor // final이 있는 필드만 가지고 생성자를 만들어준다.
public class MemberService {

    // @Autowired // 스프링 빈이 스프링 빈에 등록되어 있는 memberRepository를 주입시켜줌, 필드 인젝션
    // 하지만 위에 붙은 @RequiredArgsConstructor 애노테이션이 더 좋다.
    private final MemberRepository memberRepository;

    // 회원 가입
    @Transactional
    public Long join(Member member) {
        validateDuplicateMember(member); // 중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        // EXCEPTION
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    // 회원 전체 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    // 회원 단건 조회
    public  Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }
}
