package kr.oshino.eataku.member.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor

public class Follow {

    @Id
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "from_member")
    private Member member;

    @Column(name = "to_member")
    private String fromMember;
}
