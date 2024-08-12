package kr.oshino.eataku.list.model.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class MyListDto {

    private int listNo;

    private int listShare;

    private int memberNo;

    private String listName;

    private String listStatus;

}
