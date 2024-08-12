//package kr.oshino.eataku.test;
//
//import jakarta.transaction.Transactional;
//import kr.oshino.eataku.common.enums.AccountAuth;
//import kr.oshino.eataku.reservation.user.entity.Reservation;
//import kr.oshino.eataku.reservation.user.repository.ReservationRepository;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.annotation.Rollback;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@SpringBootTest
//public class ReservationTest {
//
//    @Autowired
//    public ReservationRepository reservationRepository;
//
//    @AfterEach
//    public void tearDown() {
//        reservationRepository.deleteAll();
//    }
//
//    @Test
//    @DisplayName("예약 테스트")
//    @Transactional
//    @Rollback(false)
//    void test0318() {
//        // Given
////        Reservation reservation = new Reservation();
////        reservation.setReservationNo(1);
////        reservation.setMember(null);
////        reservation.setReservationTime(LocalTime.of(10, 0));
////        reservation.setCreatedTime(LocalDateTime.of(2024, 1, 12, 10, 0));
////        reservation.setPartySize(5);
////        reservation.setReservationDate(LocalDate.of(2024, 1, 12));
////        reservation.setReservationStatus(AccountAuth.ReservationStatus.예약완료);
////        reservation.setUpdatedTime(LocalDateTime.of(2024, 1, 12, 10, 0));
////        reservation.setRestaurantInfo(null);
//
//        reservationRepository.save(reservation);
//
//        // When
//        Optional<Reservation> reservations = reservationRepository.findById(reservation.getReservationNo());
//
//
//        // Then
//        assertThat(reservations.isPresent()).isTrue();
//        assertThat(reservations.get().getMember()).isEqualTo(reservation.getMember());
//        assertThat(reservations.get().getReservationTime()).isEqualTo(LocalTime.of(10, 0));
//        assertThat(reservations.get().getReservationDate()).isEqualTo(LocalDate.of(2024, 1, 12));
//        assertThat(reservations.get().getReservationStatus()).isEqualTo(AccountAuth.ReservationStatus.예약완료);
//        assertThat(reservations.get().getUpdatedTime()).isEqualTo(LocalDateTime.of(2024, 1, 12, 10, 0));
//        assertThat(reservations.get().getRestaurantInfo()).isNull();
//        assertThat(reservations.get().getPartySize()).isEqualTo(5);
//        assertThat(reservations.get().getReservationNo()).isEqualTo(1);
//        assertThat(reservations.get().getCreatedTime()).isEqualTo(LocalDateTime.of(2024,1,12,10,0));
//    }
//}
