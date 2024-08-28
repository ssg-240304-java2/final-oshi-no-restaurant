package kr.oshino.eataku.common.util;

import jakarta.annotation.PostConstruct;
import kr.oshino.eataku.common.enums.SmsMessageType;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import net.nurigo.sdk.message.model.Message;

@Slf4j
@Component
public class SmsUtil {

    @Value("${coolsms.api.key}")
    private String apiKey;

    @Value("${coolsms.api.secret}")
    private String apiSecretKey;

    @Value("${coolsms.api.number}")
    private String number;

    private DefaultMessageService messageService;

    @PostConstruct
    private void init() {
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecretKey, "https://api.coolsms.co.kr");
    }


    public SingleMessageSentResponse sendWaitingMessage(String to, SmsMessageType messageType, Object... args) {
        Message message = new Message();

        message.setFrom(number);
        message.setTo(to);
        message.setSubject("[EATAKU]");
        String formattedText = messageType.format(args);

        log.info("formattedText: {}", formattedText);

        message.setText(formattedText);

        return this.messageService.sendOne(new SingleMessageSendingRequest(message));
    }
}
