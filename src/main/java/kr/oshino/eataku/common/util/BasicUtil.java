package kr.oshino.eataku.common.util;

import org.springframework.stereotype.Component;

import java.sql.Time;
import java.time.LocalTime;

@Component
public class BasicUtil {

    /**
     * 현재 영업 중인지 비교
     * @param startTime
     * @param endTime
     * @return
     */
    public boolean isCurrentTimeWithinSchedule(Time startTime, Time endTime) {
        // 현재 시간을 LocalTime으로 가져오기
        LocalTime currentTime = LocalTime.now();

        // java.sql.Time을 java.time.LocalTime으로 변환
        LocalTime start = startTime.toLocalTime();
        LocalTime end = endTime.toLocalTime();

        // 자정을 넘어가는 경우 처리
        if (end.isBefore(start)) {
            // 예: 23:00 ~ 02:00
            return !currentTime.isBefore(start) || !currentTime.isAfter(end);
        }

        // 자정을 넘지 않는 경우 처리
        return !currentTime.isBefore(start) && !currentTime.isAfter(end);
    }
}
