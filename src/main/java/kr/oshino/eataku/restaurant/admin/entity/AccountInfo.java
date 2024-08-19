package kr.oshino.eataku.restaurant.admin.entity;

import com.sun.istack.NotNull;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
@Table(name = "tbl_account_info")
@AllArgsConstructor
@Builder

public class AccountInfo {      // 식당 계정

    @Id
    @Column(name = "id")
    @NotNull
    private String id;      // 아이디(pk)

    @OneToOne
    @JoinColumn(name = "restaurant_no", referencedColumnName = "restaurant_no")
    private RestaurantInfo restaurantNo;        // 식당 고유 번호(fk)

    @Column(name = "password")
    @NotNull
    private String password;        // 비밀번호

    @Column(name = "email")
    @NotNull
    private String email;       // 이메일
}
