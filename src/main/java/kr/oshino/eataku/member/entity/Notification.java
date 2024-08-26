package kr.oshino.eataku.member.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "tbl_notification")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "to_member")
    private Long toMember;

    private String type;

    @Column(name = "reference_number")
    private Long referenceNumber;

    private String message;

    @Column(name = "review_no")
    private Long reviewNo;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;
}
