package kr.oshino.eataku.member.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_email_verif_code")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailVerifCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String verifCode;
}
