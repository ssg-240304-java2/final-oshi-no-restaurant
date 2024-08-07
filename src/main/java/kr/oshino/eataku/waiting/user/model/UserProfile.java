package kr.oshino.eataku.waiting.user.model;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserProfile {

    private int publicListCount;
    private int privateListCount;
    private int reviewCount;
    private String nickname;
    private int followingCount;
    private String followerCount;
    private int joinYear;
    private String introduction;
    private String weight;
    private List<ListItem> publicLists;
    private List<ListItem> privateLists;
}
