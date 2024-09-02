package kr.oshino.eataku.member.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class HeartDTO {
    private Long restaurant;
    private Long inList;
}
