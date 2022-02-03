package hello1.servlet1.web.frontcontroller.v3;

import hello1.servlet1.domain.member.Member;
import hello1.servlet1.domain.member.MemberRepository;
import hello1.servlet1.web.frontcontroller.ModelView;

import java.util.List;
import java.util.Map;

public class MemberListControllerV3 implements ControllerV3{

    private MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    public ModelView process(Map<String, String> paramMap) {
        List<Member> members = memberRepository.findAll();

        ModelView mv = new ModelView("members");
        mv.getModel().put("members", members);
        return mv;
    }
}
