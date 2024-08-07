package kr.oshino.eataku.list.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "List_Info")
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor

public class ListInfo {

    /* 리스트 정보 */
    @Id
    @Column(name = "list_info_no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int listInfoNo;

    /* 리스트 번호*/
    @ManyToOne
    @JoinColumn(name = "list_no")
    private List list;

    /* 매장 번호 */
//    @ManyToOne
//    @JoinColumn(name = "restaurant_no")
//    private Restaurant restaurant;
}
