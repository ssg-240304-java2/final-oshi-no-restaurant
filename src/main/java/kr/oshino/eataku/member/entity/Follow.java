package kr.oshino.eataku.member.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tbl_follow")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fromMemberNo")
    private Member fromMemberNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "toMemberNo")
    private Member toMemberNo;
}
