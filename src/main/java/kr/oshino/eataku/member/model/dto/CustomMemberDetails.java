package kr.oshino.eataku.member.model.dto;

import kr.oshino.eataku.member.entity.Member;
import kr.oshino.eataku.restaurant.admin.entity.RestaurantInfo;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@ToString
public class CustomMemberDetails implements UserDetails {

    private final Member member;
    private final RestaurantInfo restaurant;

    public CustomMemberDetails(Member member) {
        this.member = member;
        this.restaurant = new RestaurantInfo();
    }

    public CustomMemberDetails(RestaurantInfo restaurant) {
        this.member = new Member();
        this.restaurant = restaurant;
    }

    public Long getMemberNo(){
        return member.getMemberNo();
    }
    public Long getRestaurantNo() {return restaurant.getRestaurantNo(); }

    @Override
    public String getUsername() {

        if(member.getMemberLoginInfo() != null) {
            return member.getMemberLoginInfo().getAccount();
        }
        else {
            return restaurant.getAccountInfo().getId();
        }
    }

    @Override
    public String getPassword() {
        if(member.getMemberLoginInfo() != null) {
            return member.getMemberLoginInfo().getPassword();
        }
        else {
            return restaurant.getAccountInfo().getPassword();
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> authorities = new ArrayList<>();

        authorities.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {

                if(member.getMemberLoginInfo() != null) {
                    return member.getAuth();
                }
                else  {
                    return "ROLE_MANAGER";
                }
            }
        });

        return authorities;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
}
