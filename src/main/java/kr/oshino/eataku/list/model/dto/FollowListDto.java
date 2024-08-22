package kr.oshino.eataku.list.model.dto;

import kr.oshino.eataku.list.entity.MyList;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class FollowListDto {
    private Integer memberNo;
    private String name;
    private String imgUrl;
    private List<MyList> lists;

}
