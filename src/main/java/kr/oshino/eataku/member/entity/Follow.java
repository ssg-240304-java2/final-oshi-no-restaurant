package kr.oshino.eataku.member.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "a_tbl_follow")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fromMemberId")
    private Member fromMemberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "toMemberId")
    private Member toMemberId;
}
