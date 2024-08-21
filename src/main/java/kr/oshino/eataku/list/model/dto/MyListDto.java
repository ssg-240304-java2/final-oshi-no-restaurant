package kr.oshino.eataku.list.model.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class MyListDto {

    private Integer listNo;

    private Long listShare;

    private Long memberNo;

    private String listName;

    private String listStatus;

}
