package kr.oshino.eataku.common.enums;

public enum SmsMessageType {

    WAITING_ENTRY_MESSAGE("웨이팅 번호 [%d]번 고객님 \n지금 입장해주세요!\n\n매장 이름 : %s\n인원 : %d명"),
    WAITING_REGISTER_MESSAGE("고객님의 웨이팅이 정상적으로 등록되었습니다.\n\n매장 이름 : %s\n웨이팅 번호 : %d\n인원 : %d명"),
    RESERVATION_REGISTER_MESSAGE("");

    private final String template;

    SmsMessageType(String template) {
        this.template = template;
    }

    public String format(Object... args) {
        return String.format(template, args);
    }
}
