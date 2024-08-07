package kr.oshino.eataku.restaurant.admin.model;

import com.sun.istack.NotNull;
import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "hashtagList")
@Embeddable
public class HashtagList implements Serializable {      // 해시태그 리스트

    @Id
    @Column(name = "hashTag_no")
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int hashtagNo;      // 해시태그 번호(pk)

    @Column(name = "hashtag")
    private String hashtag;         // 해시태그
}
