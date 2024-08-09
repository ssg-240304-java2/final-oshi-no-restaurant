package kr.oshino.eataku.member.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tbl_member_login_info")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"member"})
public class MemberLoginInfo {

    @Id
    @OneToOne
    @JoinColumn(name = "member_no", referencedColumnName = "member_no")
    private Member member;

    private String account;
    private String password;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MemberLoginInfo that = (MemberLoginInfo) o;

        if (!account.equals(that.account)) return false;
        return password.equals(that.password);
    }

    @Override
    public int hashCode() {
        int result = account.hashCode();
        result = 31 * result + password.hashCode();
        return result;
    }
}
