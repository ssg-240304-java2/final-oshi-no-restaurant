package kr.oshino.eataku.restaurant.admin.model;

import com.sun.istack.NotNull;
import jakarta.persistence.*;

@Entity
@Table(name = "accountInfo")
public class AccountInfo {      // 식당 계정

    @Id
    @Column(name = "id")
    @NotNull
    private String id;      // 아이디(pk)

    @Column(name = "restaurant_no")
    @EmbeddedId
    private Restaurant restqurantNo;        // 식당 고유 번호(fk)

    @Column(name = "password")
    @NotNull
    private String password;        // 비밀번호

    @Column(name = "email")
    @NotNull
    private String email;       // 이메일
}
