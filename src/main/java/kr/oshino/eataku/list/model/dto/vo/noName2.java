package kr.oshino.eataku.list.model.dto.vo;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "list_scope") // 임시
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor

public class noName2 {

    //리뷰 별점
    private int scope;
}
