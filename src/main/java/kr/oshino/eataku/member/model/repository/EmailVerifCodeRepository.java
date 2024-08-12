package kr.oshino.eataku.member.model.repository;

import kr.oshino.eataku.member.entity.EmailVerifCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailVerifCodeRepository extends JpaRepository<EmailVerifCode, Long> {
    boolean existsByEmailAndVerifCode(String email, String reqVerifCode);

    EmailVerifCode findByEmail(String email);
}
