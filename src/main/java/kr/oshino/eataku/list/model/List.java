package kr.oshino.eataku.list.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "list")
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor

public class List {

    /* 리스트 번호 */
    @Id
    @Column(name = "list_no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int listNo;

    /* 회원 번호 */
//    @ManyToOne
//    @JoinColumn(name = "member_no")
//    private Member member

    /* 리스트 이름 */
    @Column(name = "list_name")
    private String listName;

    /* 리스트 공개 여부 */
    @Column(name = "list_status")
    private String listStatus;

    /* 리스트 공유 횟수 */
    @Column(name = "list_share")
    private String listShare;



}
