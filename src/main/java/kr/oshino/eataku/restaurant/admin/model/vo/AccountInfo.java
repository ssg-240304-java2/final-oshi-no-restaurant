package kr.oshino.eataku.restaurant.admin.model.vo;

import com.sun.istack.NotNull;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import kr.oshino.eataku.restaurant.admin.entity.Restaurant;
import lombok.*;

@Embeddable
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountInfo {

    private String id;      // 아이디(pk)

    private String password;        // 비밀번호

    private String email;       // 이메일
}
