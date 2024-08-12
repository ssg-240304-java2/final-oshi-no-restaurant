//package kr.oshino.eataku.test;
//
//import kr.oshino.eataku.common.enums.AccountAuth;
//import kr.oshino.eataku.list.entity.MyList;
//import kr.oshino.eataku.list.model.repository.MyListRepository;
//import kr.oshino.eataku.member.entity.Member;
//import kr.oshino.eataku.member.model.repository.MemberRepository;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.sql.Date;
//import java.util.Optional;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//
//@SpringBootTest
//public class ListTest {
//
//    @Autowired
//    private MyListRepository myListRepository;
//    @Autowired
//    private MemberRepository memberRepository;
//
//    @Test
//    @DisplayName("리스트 테스트")
//    void test1() {
//
//        Member member = new Member(1L
//                ,"test"
//                ,"pass"
//                ,"test"
//                ,"test"
//                , Date.valueOf("2000-07-28")
//                ,"test"
//                ,"test"
//                ,"test"
//                , AccountAuth.general
//                ,"test"
//                ,"test"
//        );
//        // given
//        MyList myList = MyList.builder()
//                .member(member)
//                .listNo(1)
//                .listName("jisoo")
//                .listShare(0L)
//                .listStatus("pubic")
//                .build();
//
//        memberRepository.save(member);
//        myListRepository.save(myList);
//
//
//        // when
////        MyList foundMyList = myListRepository.findById(myList.getListNo());
//        Optional<MyList> foundMyList = myListRepository.findById(myList.getListNo());
//
//        // then
//        System.out.println("myList = " + myList);
//        System.out.println("foundMyList = " + foundMyList);
//
//        assertThat(foundMyList).isNotNull();
////        assertThat(foundMyList.getListNo()).isEqualTo(myList.getListNo());
//        assertThat(foundMyList.get().getListName()).isEqualTo(myList.getListName());
//        assertThat(foundMyList.get().getListStatus()).isEqualTo(myList.getListStatus());
//        assertThat(foundMyList.get().getListShare()).isEqualTo(myList.getListShare());
//
//    }
//
//}
//
//
//
