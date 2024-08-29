package kr.oshino.eataku.list.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class CopyListDTO {

    private Long listNo;
    private List<Long> restaurantNo;
    private Long fromListNo;
}
