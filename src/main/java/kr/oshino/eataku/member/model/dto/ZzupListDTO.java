package kr.oshino.eataku.member.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ZzupListDTO {

    private int listNo;
    private String listName;
    private Long listShare;
    private Long memberNo;
    private String memberName;
    private String memberImg;
}
