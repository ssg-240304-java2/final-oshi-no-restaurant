package kr.oshino.eataku.waiting.user.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Restaurant {

    private String name;
    private double rating;
    private String category;
    private String imageUrl;
    private String address;
}
