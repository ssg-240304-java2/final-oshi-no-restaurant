package kr.oshino.eataku.member.service;

import kr.oshino.eataku.member.entity.Member;
import kr.oshino.eataku.member.model.dto.CustomMemberDetails;
import kr.oshino.eataku.member.model.repository.MemberRepository;
import kr.oshino.eataku.restaurant.admin.entity.RestaurantInfo;
import kr.oshino.eataku.restaurant.admin.model.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomMemberDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final RestaurantRepository restaurantRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Member memberData = memberRepository.findByMemberLoginInfoAccount(username);
        RestaurantInfo restaurantData = restaurantRepository.findByAccountInfoId(username);
        log.info("111111111111111");
        if (memberData != null) {

            log.info("22222222222222");
            return new CustomMemberDetails(memberData);
        }

        if (restaurantData != null){

            log.info("333333333333");
            return new CustomMemberDetails(restaurantData);
        }

        log.info("4444444444444444");
        return null;
    }
}
