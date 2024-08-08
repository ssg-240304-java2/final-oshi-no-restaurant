package kr.oshino.eataku.restaurant.admin.entity;

import com.sun.istack.NotNull;
import jakarta.persistence.*;

@Entity
@Table(name = "tbl_account_info")
public class AccountInfo {      // 식당 계정

    @Id
    @Column(name = "id")
    @NotNull
    private String id;      // 아이디(pk)

    @Column(name = "restaurant_no")
    @OneToOne
    @JoinColumn(name = "restaurant_no")
    private RestaurantInfo restqurantNo;        // 식당 고유 번호(fk)

    @Column(name = "password")
    @NotNull
    private String password;        // 비밀번호

    @Column(name = "email")
    @NotNull
    private String email;       // 이메일
}
