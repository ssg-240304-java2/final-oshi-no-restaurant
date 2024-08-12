package kr.oshino.eataku.member.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import kr.oshino.eataku.member.entity.EmailVerifCode;
import kr.oshino.eataku.member.model.repository.EmailVerifCodeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {

    private final JavaMailSender mailSender;
    private final EmailVerifCodeRepository emailVerifCodeRepository;

    public int sendEmailVerifCode(String email, @Value("${spring.mail.username}") String senderEmail) {

        int number = (int) (Math.random() * (90000)) + 100000;
        log.info("🛠️🛠️ MailService VerifCode Create : {} 🛠️🛠️", number);
        MimeMessage message = mailSender.createMimeMessage();

        try {
            message.setFrom(senderEmail);
            message.setRecipients(MimeMessage.RecipientType.TO, email);
            message.setSubject("이메일 인증");
            String body = "";
            body += "<h3>" + "요청하신 인증 번호입니다." + "</h3>";
            body += "<h1>" + number + "</h1>";
            body += "<h3>" + "감사합니다." + "</h3>";
            message.setText(body, "UTF-8", "html");
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        mailSender.send(message);
        saveVerifCode(email, String.valueOf(number));
        return number;
    }

    public void saveVerifCode(String email, String verifCode) {

        log.info("🛠️🛠️ MailService VerifCode Email : {} , VerifCode : {} 🛠️🛠️", email, verifCode);

        EmailVerifCode emailVerifCode = emailVerifCodeRepository.findByEmail(email);

        if (emailVerifCode == null) {
            emailVerifCode = EmailVerifCode.builder()
                    .email(email)
                    .verifCode(verifCode)
                    .build();
        }else {
            emailVerifCode.setVerifCode(verifCode);
            emailVerifCode.setEmail(email);
        }

        emailVerifCodeRepository.save(emailVerifCode);
    }

    public boolean checkMailVerifCode(String email, String reqVerifCode) {

        boolean isMatch = emailVerifCodeRepository.existsByEmailAndVerifCode(email, reqVerifCode);

        log.info("🛠️🛠️ MailService Check Verif Email : {} , reqVerifCode : {}, result : {} 🛠️🛠️", email, reqVerifCode, isMatch);
        return isMatch;
    }
}
