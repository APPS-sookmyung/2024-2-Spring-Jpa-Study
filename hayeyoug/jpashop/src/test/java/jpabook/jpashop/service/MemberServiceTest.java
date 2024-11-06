package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class) //Junit 실행할때 스프링이랑 같이 엮어서 실행시켜줌
@SpringBootTest // 스프링 컨테이너 안에서 이 테스트를 돌릴 수 있게 해줌
@Transactional // 테스트가 끝나면 롤백 시켜줌 (DB에 이 값들을 넣을 필요가 없으므로, 테스트이므로) 테스트 케이스에서 사용될 때만 기본적으로 롤백
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Test
    public void 회원가입() throws Exception {
        Member member = new Member();
        member.setName("kim");

        Long saveId = memberService.join(member);

        assertEquals(member, memberRepository.findOne(saveId));
    }

    @Test(expected = IllegalStateException.class) // memberService.join(member2) 명령어를 실행하다가 예외가 발생해서 로직 튕겨나가는 걸 여기서 설정해줌 IllegalstateException이 터진다는 걸 알려줌
    public void 중복_회원_예외() throws Exception {
         Member member1 = new Member();
         member1.setName("kim");

        Member member2 = new Member();
        member1.setName("kim");

        memberService.join(member1);
        memberService.join(member1);

        fail("예외가 발생해야 한다."); // assert에서 기본적으로 제공해주는 것으로 위의 명령어에서 예외가 터져서 로직을 나가버려야 하는데 이 fail 문이 실행된다면 잘못 된 것이므로 페일을 실행시켜 준다.
    }
}