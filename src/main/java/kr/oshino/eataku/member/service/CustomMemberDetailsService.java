package kr.oshino.eataku.member.service;

import kr.oshino.eataku.member.entity.Member;
import kr.oshino.eataku.member.model.dto.CustomMemberDetails;
import kr.oshino.eataku.member.model.repository.MemberRepository;
import kr.oshino.eataku.restaurant.admin.entity.RestaurantInfo;
import kr.oshino.eataku.restaurant.admin.model.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomMemberDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final RestaurantRepository restaurantRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Member memberData = memberRepository.findByMemberLoginInfoAccount(username);
        RestaurantInfo restaurantData = restaurantRepository.findByAccountInfoId(username);

        if (memberData != null) {

            return new CustomMemberDetails(memberData);
        }

        if (restaurantData != null){
            return new CustomMemberDetails(restaurantData);
        }

        return null;
    }
}
