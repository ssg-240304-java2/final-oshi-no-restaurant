package kr.oshino.eataku.list.controller;

import lombok.Data;

import java.util.List;

@Data
public class DeleteRequest {
    private Integer listNo;
    private List<Integer> restaurantNos;
}
