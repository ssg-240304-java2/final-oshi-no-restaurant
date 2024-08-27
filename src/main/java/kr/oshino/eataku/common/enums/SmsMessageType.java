package kr.oshino.eataku.common.enums;

public enum SmsMessageType {

    WAITING_ENTRY_MESSAGE("웨이팅 번호 [%d]번 고객님 \n지금 입장해주세요!\n\n매장 이름 : %s\n인원 : %d명"),
    WAITING_REGISTER_MESSAGE("고객님의 웨이팅이 정상적으로 등록되었습니다.\n\n매장 이름 : %s\n웨이팅 번호 : %d\n인원 : %d명"),
    WAITING_NOTIFICATION("안녕하세요. \"%s\" 입니다.\n고객님의 입장까지 3팀 남았습니다. 매장 앞에서 대기해주세요!");

    private final String template;

    SmsMessageType(String template) {
        this.template = template;
    }

    public String format(Object... args) {
        return String.format(template, args);
    }

}
