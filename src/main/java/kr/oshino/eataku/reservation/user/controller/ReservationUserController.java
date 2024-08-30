package kr.oshino.eataku.reservation.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import kr.oshino.eataku.member.entity.Member;
import kr.oshino.eataku.member.model.dto.CustomMemberDetails;
import kr.oshino.eataku.member.model.repository.MemberRepository;
import kr.oshino.eataku.reservation.user.model.dto.requestDto.CreateReservationUserRequestDto;
import kr.oshino.eataku.reservation.user.model.dto.responseDto.*;
import kr.oshino.eataku.reservation.user.service.ReservationUserService;
import kr.oshino.eataku.restaurant.admin.entity.RestaurantInfo;
import kr.oshino.eataku.restaurant.admin.model.repository.RestaurantRepository;
import kr.oshino.eataku.ws.entity.ChatMessage;
import kr.oshino.eataku.ws.entity.ChatRoom;
import kr.oshino.eataku.ws.model.dto.ChatMessageDTO;
import kr.oshino.eataku.ws.model.dto.ChatMessageResDTO;
import kr.oshino.eataku.ws.repository.ChatRoomRepository;
import kr.oshino.eataku.ws.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Slf4j
@Controller
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class ReservationUserController {

    private final ReservationUserService reservationUserService;
    private final ChatRoomService chatRoomService;
    private final SimpMessagingTemplate messagingTemplate;
    private final MemberRepository memberRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final RestaurantRepository restaurantRepository;

    /***
     * 예약 등록 페이지 이동 메서드
     */
    @GetMapping("/reservation/{restaurantNo}")
    public String reservation(@PathVariable String restaurantNo, Model model) {

        CustomMemberDetails member = (CustomMemberDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long memberNo = member.getMemberNo();

        model.addAttribute("memberNo", memberNo);
        System.out.println("memberNo= ---------------" + memberNo);
        model.addAttribute("restaurantNo", restaurantNo);
        System.out.println("restaurantNo = " + restaurantNo);

        System.out.println("예약 페이지 접속");
        return "reservation/reservationCalendar";
    }


    /***
     * 특정 식당의 시간을 가져오는 메서드
     * @param restaurantNo
     * @return
     */
    @ResponseBody
    @GetMapping("/reservation/{restaurantNo}/available-times")
    public List<Map<String, Object>> getAvailableTimeSlots(
            @PathVariable("restaurantNo") Long restaurantNo,
            @RequestParam("date") String dateStr,
            @RequestParam("partySize") int partySize) {

        LocalDate date = LocalDate.parse(dateStr);
        System.out.println("partySize = " + partySize);
        System.out.println("restaurantNo = " + restaurantNo);
        System.out.println("Received date: " + date);
        return reservationUserService.getAvailableTimeSlots(date, restaurantNo, partySize);
    }


    /***
     * 예약 등록 메서드
     * @param createReservationUserRequestDto
     * @return
     */
    @PostMapping("/reservation")
    @ResponseBody
    public ResponseEntity<CreateReservationUserResponseDto> registerReservation(@RequestBody CreateReservationUserRequestDto createReservationUserRequestDto) {
        log.info("CreateReservationUserRequestDto: {}", createReservationUserRequestDto);
        System.out.println("createReservationUserRequestDto 확인!!! = " + createReservationUserRequestDto);

        return ResponseEntity.status(HttpStatus.OK)
                .body(reservationUserService.registerReservation(createReservationUserRequestDto));
    }


//    /***
//     * 예약한 인원수 만큼 예약세팅 베이블에서 빼기
//     */
//    @PutMapping("/reservation/{reservationNo}/subtract")
//    public ResponseEntity<Void> subtractPartySize(@PathVariable Long reservationNo,
//                                                  @RequestParam int partySize,
//                                                  @RequestParam String time) {
//        try {
//            LocalTime parsedTime = LocalTime.parse(time);
//            reservationUserService.subtractPartySize(reservationNo, partySize, parsedTime);
//            return ResponseEntity.ok().build();
//        } catch (Exception e) {
//            log.error("인원수 제거 에러남", e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }

//    /***
//     * 모달창에 상세정보
//     */
//    @GetMapping("/reservation/{restaurantNo}/modal")
//    public String modal(@PathVariable String restaurantNo, Model model) {
//        // 모델에 필요한 데이터를 추가
//        model.addAttribute("restaurantNo", restaurantNo);
//        return "reservation/reservationComplete";  // 이 뷰 페이지가 실제로 모달을 포함하고 있어야 함
//    }

    /***
     * 모달에 대한 상세정보 반환
     * @param restaurantNo
     * @return
     */

    @PostMapping("/reservation/{restaurantNo}/modal/data")
    @ResponseBody
    public ResponseEntity<modalDto> getModalDetails(@PathVariable Long restaurantNo) {

        modalDto modalDetails = reservationUserService.getModalDetails(restaurantNo);

        System.out.println("modalDetails = " + modalDetails);
        return ResponseEntity.ok(modalDetails);
    }

    /***
     * 날짜 가져오기
     */
    @PostMapping("/reservation/{restaurantNo}/available-dates")
    @ResponseBody
    public List<LocalDate> getAvailableDates(@PathVariable Long restaurantNo) {

        return reservationUserService.getAvailableDates(restaurantNo);
    }


    /***
     * 방문완료 예약 조회
     */
    @GetMapping("/reservation")
    @ResponseBody
    public ResponseEntity<List<ReadReservationResponseDto>> getMyreservationList(
            ReadReservationResponseDto readReservationResponseDto) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(reservationUserService.getReservationListByMemberNo(readReservationResponseDto));
    }

    /***
     * 예약 취소
     */
    @GetMapping("/reservation/cancel")

    public String cancelReservations() {

//        model.addAttribute("reservationNo",reservationNo);


        return "reservation/updateReservation";
    }

    /***
     * 예약취소
     * @param reservationNo
     * @return
     */

    @PostMapping("/reservation/{reservationNo}/cancel")
    @ResponseBody
    public ResponseEntity<?> cancelReservation(@PathVariable int reservationNo, Model model) {

        boolean isCancelled = reservationUserService.cancelReservation(reservationNo);
        System.out.println("isCancelled = " + isCancelled);
        model.addAttribute("reservationNo", reservationNo);
        System.out.println("reservationNo" + reservationNo);


        if (isCancelled) {
            return ResponseEntity.ok().body(Map.of("success", true, "message", "예약이 성공적으로 취소되었습니다."));
        } else {
            return ResponseEntity.status(200).body(Map.of("success", true, "message", "예약 취소는 24시간 전에만 가능합니다!"));
        }
    }

    /***
     * 식당 상세 페이지
     */

    /**
     * 식당 상세 페이지를 로드
     */
    @GetMapping("/detail")
    public String detail() {
        return "reservation/reservationDetail";
    }

    /**
     * 특정 식당의 상세 정보 가져오기
     */
    @GetMapping("/detail/{restaurantNo}/detailPage")
    public String detailPage(@PathVariable Long restaurantNo, Model model) {

        // 식당 상세 정보
        RestaurantInfoDetails restaurant = reservationUserService.getRestaurantDetailsByReservation(restaurantNo);
        model.addAttribute("restaurant", restaurant);

        // 식당 메뉴 정보
        List<MenuDto> menu = reservationUserService.getMenu(restaurantNo);
        model.addAttribute("menu", menu);

        // 리뷰 상세 정보
        List<ReviewDetails> reviewDetails = reservationUserService.getReviewDetails(restaurantNo);
        model.addAttribute("ReviewDetails", reviewDetails);

        // 태그 횟수 정보
        List<String> tagCount = reservationUserService.getCountTags(restaurantNo);
        Map<String, Integer> tagCountMap = new LinkedHashMap<>();
        System.out.println("tagCountMap = " + tagCountMap);

        // 식당 지도 위치 정보
        List<MapDto> position = reservationUserService.getMapLocation(restaurantNo);

        // 식당 리뷰 사진
        List<ReviewImgDto> reviewImgDto = reservationUserService.getImg(restaurantNo);
        model.addAttribute("reviewImgDto", reviewImgDto);

        for (String tagCounts : tagCount) {
            String[] parts = tagCounts.split(",");
            if (parts.length == 2) {
                String tag = parts[0].trim();  // 태그 부분
                Integer count = Integer.parseInt(parts[1].trim());  // 횟수 부분
                tagCountMap.put(tag, count);
            } else {
                // 태그나 횟수가 올바른 형식이 아닐 경우를 처리
                System.err.println(" 올바른 형식이이 아님 " + tagCount);
            }
        }
        System.out.println("menu = " + menu);

        model.addAttribute("tagCountMap", tagCountMap);
        model.addAttribute("position", position);

        System.out.println("reviewDetails = " + reviewDetails);
        System.out.println("restaurant = " + restaurant);
        System.out.println("tagCountMap = " + tagCountMap);
        System.out.println("tagCountMap = " + tagCountMap);
        System.out.println("position = " + position);
        System.out.println("reviewImgDto = " + reviewImgDto);


        return "reservation/reservationDetail";
    }

    // 유저 채팅
    @GetMapping("/user/chatting/{restaurantNo}")
    public String chattingView(@PathVariable String restaurantNo, Model model) {

        CustomMemberDetails member = (CustomMemberDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long currentUser = member.getMemberNo();

        Member currentMember = memberRepository.findById(currentUser).orElseThrow();

        model.addAttribute("roomId", restaurantNo);
        model.addAttribute("userType", "user");
        model.addAttribute("memberNo", currentMember.getMemberNo());
        model.addAttribute("sender", currentMember.getName());
        log.info("🍎restaurantNo = " + restaurantNo);
        return "ws/user-chat";
    }

    // 채팅방 생성
    @PostMapping("/chat/start")
    public String startChat(@RequestParam Long restaurantNo, HttpServletRequest request) {

        CustomMemberDetails member = (CustomMemberDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long currentUser = member.getMemberNo();

        RestaurantInfo restaurantInfo = restaurantRepository.findById(restaurantNo).orElseThrow(() -> new IllegalArgumentException("Restaurant not found with id: " + restaurantNo));
        Member user = memberRepository.findById(currentUser).orElseThrow(() -> new IllegalArgumentException("Member not found with id: " + currentUser));

        // 기존 채팅방 조회
        ChatRoom existingChatRoom = chatRoomService.findByRestaurantInfoAndMember(restaurantInfo,user);
        if (existingChatRoom != null && existingChatRoom.getMember().getMemberNo().equals(currentUser)) {       // 민규님 dto 확인하기(엔티티 member 추가)
            return "redirect:/user/chatting/"+existingChatRoom.getRoomId();
        }

        // 새 채팅방 생성
        ChatRoom newChatRoom = chatRoomService.createRoom(restaurantInfo, user);

        // 판매자와 현재 사용자 채팅방에 추가

//        chatRoomService.addMember(newChatRoom.getRoomId(), currentUserId, restaurantId);

        return "redirect:/user/chatting/"+newChatRoom.getRoomId();
    }

    @GetMapping("/chat/messages")
    public ResponseEntity<List<ChatMessageResDTO>> getChatMessages(@RequestParam Long roomId){
        List<ChatMessageResDTO> messages = chatRoomService.findMessagesByRoomId(roomId)
                .stream()
                .map(ChatMessageResDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(messages);
    }

    // 채팅 메세지 저장
    @PostMapping("/chat/send")
    public ResponseEntity<String> saveMessage(@RequestBody ChatMessageDTO chatMessageDTO) {
        log.info(" [ ChatSendController ] chatMessageDTO : {} ",chatMessageDTO);

        CustomMemberDetails member = (CustomMemberDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long currentUser = member.getMemberNo();

        chatRoomService.createAndSaveChatMessage(chatMessageDTO, currentUser);
        return ResponseEntity.ok("메시지가 성공적으로 전송되었습니다.");
    }

    @MessageMapping("/chat.sendMessage")
    public ChatMessageResDTO handleMessage(@Payload ChatMessageDTO chatMessageDTO) {
        log.info(" [ ChatSendController ] chatMessageDTO : {} ",chatMessageDTO);
        Long memberNo = chatMessageDTO.getMemberNo();

        ChatMessage chatMessage = chatRoomService.createAndSaveChatMessage(chatMessageDTO, memberNo);
        ChatMessageResDTO responseDTO = ChatMessageResDTO.fromEntity(chatMessage);

        messagingTemplate.convertAndSend("/sub/chat/room/" + chatMessage.getChatRoom().getRoomId(), responseDTO);
        return responseDTO;
    }

    @MessageMapping("/chat.addUser")
    public void addUser(@Payload ChatMessageDTO chatMessageDTO) {
        Long memberNo = chatMessageDTO.getMemberNo();
        Member member = memberRepository.findById(memberNo).orElseThrow(() -> new IllegalArgumentException("Member not found with ID: " + memberNo));

        ChatRoom chatRoom = chatRoomRepository.findById(chatMessageDTO.getRoomId()).orElseThrow(() -> new IllegalArgumentException("Room not found with ID: " + chatMessageDTO.getRoomId()));

        ChatMessage chatMessage = ChatMessage.builder()
                .member(member)
                .chatRoom(chatRoom)
                .message("User " + member.getEmail() + " has joined the chat")      // email??
                .build();

        messagingTemplate.convertAndSend("/sub/chat/room/" + chatRoom.getRoomId(), ChatMessageResDTO.fromEntity(chatMessage));
    }


}


