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

    @Value("${spring.mail.username}")
    private String senderEmail;

    public int sendEmailVerifCode(String email) {

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

    public boolean sendEmailFindId(String email, String account) {

        log.info("🛠️🛠️ MailService Send to email : {}, account : {} 🛠️🛠️", email, account);
        MimeMessage message = mailSender.createMimeMessage();

        try {
            message.setFrom(senderEmail);
            message.setRecipients(MimeMessage.RecipientType.TO, email);
            message.setSubject("아이디 찾기");
            String body = "";
            body += "<h3>" + "고객님이 가입하신 ID는" + "</h3>";
            body += "<h1>" + account + "</h1>";
            body += "<h3>" + "입니다. 감사합니다." + "</h3>";
            message.setText(body, "UTF-8", "html");
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }

        mailSender.send(message);
        return true;
    }

    public boolean sendEmailFindPw(String email, String tempPassword) {

        log.info("🛠️🛠️ MailService Send to email : {}, tempPassword : {} 🛠️🛠️", email, tempPassword);
        MimeMessage message = mailSender.createMimeMessage();

        try {
            message.setFrom(senderEmail);
            message.setRecipients(MimeMessage.RecipientType.TO, email);
            message.setSubject("비밀번호 찾기");
            String body = "";
            body += "<h3>" + "고객님의 임시비밀번호는 " + "</h3>";
            body += "<h1>" + tempPassword + "</h1>";
            body += "<h3>" + "입니다. 감사합니다." + "</h3>";
            message.setText(body, "UTF-8", "html");
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }

        mailSender.send(message);

        return true;
    }
}
