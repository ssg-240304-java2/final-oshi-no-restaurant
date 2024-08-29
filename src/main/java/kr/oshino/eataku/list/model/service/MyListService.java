package kr.oshino.eataku.list.model.service;

import kr.oshino.eataku.list.entity.MyList;
import kr.oshino.eataku.list.model.repository.MyListRepository;
import kr.oshino.eataku.list.model.vo.RestaurantList;
import kr.oshino.eataku.member.entity.Member;
import kr.oshino.eataku.member.model.repository.MemberRepository;
import kr.oshino.eataku.restaurant.admin.entity.RestaurantInfo;
import kr.oshino.eataku.restaurant.admin.model.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MyListService {

    @Autowired
    private MyListRepository myListRepository;
    private String listStatus;
    private String restaurantList;
    @Autowired
    private RestaurantRepository restaurantRepository;
    private final MemberRepository memberRepository;

//    private final FollowingRepository followRepository;

    // 리스트 생성 메소드
    public void createList(String listName, Long loginedMemberNo) {
        Member member = memberRepository.findByMemberNo(loginedMemberNo);
        MyList myList = MyList.builder()
                .listName(listName)
                .listStatus("Public")
                .listShare(0L)
                .member(member)
                .build();
        myListRepository.save(myList);
    }

    // 생성한 리스트 담기 MyList
    public List<MyList> getLists(Long loginedMemberNo) {
        return myListRepository.findByMemberMemberNo(loginedMemberNo);
    }

//    // 생성한 리스트 담기 RestaurantList
//    public List<RestaurantList> getRestaurantLists() {
//        return restaurantList
//    }

    // 리스트 삭제 메소드
    public void deleteList(Integer listNo) {
        myListRepository.deleteById(listNo);
    }

    // 리스트 이름 수정 메소드
    public void modify(Integer listNo, String newListName) {
        MyList list = myListRepository.findById(listNo)
                .orElseThrow(() -> new RuntimeException("List not found"));

        // 로그 출력으로 기존 상태 확인
        System.out.println("Old Status: " + list.getListStatus());

        list.setListName(newListName);
        myListRepository.save(list); // 변경 사항 저장

        // 로그 출력으로 변경된 상태 확인
        System.out.println("New Name: " + list.getListName());
    }


    // 리스트 공개 상태 변경 메소드
    @Transactional
    public void updateStatus(Integer listNo, String listStatus) {
        // listNo로 리스트를 조회하고, 존재하지 않으면 예외 발생
        MyList list = myListRepository.findById(listNo)
                .orElseThrow(() -> new RuntimeException("List not found with id: " + listNo));

        // 상태 변경 전에 기존 상태 로그 출력
        System.out.println("Old Status: " + list.getListStatus());

        // 상태를 업데이트하고 저장
        list.setListStatus(listStatus);

        // 상태 변경 후 새로운 상태 로그 출력
        System.out.println("New Status: " + list.getListStatus());

        myListRepository.save(list); // 이 줄이 실제로 상태를 데이터베이스에 저장함
    }

    // 식당을 리스트에 추가하는 메소드 -------------------나중에 수정
    @Transactional
    public void addRestaurantToList(Integer listNo, Long restaurantNo) {
        MyList myList = myListRepository.findById(listNo)
                .orElseThrow(() -> new IllegalArgumentException("List not found with id: " + listNo));

        List<RestaurantList> restaurantList = myList.getRestaurantList();
        if (!restaurantList.contains(restaurantNo)) {
            RestaurantInfo restaurantInfo = restaurantRepository.findByRestaurantNo(restaurantNo);
            RestaurantList newRestaurant = new RestaurantList();
            newRestaurant.setRestaurantNo(restaurantNo);
            newRestaurant.setRestaurantName(restaurantInfo.getRestaurantName());
            newRestaurant.setRestaurantAddress(restaurantInfo.getRestaurantAddress());
            newRestaurant.setImgUrl(restaurantInfo.getImgUrl());
            newRestaurant.setYCoordinate(restaurantInfo.getYCoordinate());
            newRestaurant.setXCoordinate(restaurantInfo.getXCoordinate());

            restaurantList.add(newRestaurant);
            myListRepository.save(myList);
        } else {
            throw new IllegalArgumentException("Restaurant is already in the list");
        }
    }


    // 마이페이지에서 만든 모든 리스트를 가져오는 메소드
    public List<MyList> getAllMyLists(Long memberNo) {
        return myListRepository.findAllByMemberMemberNo(memberNo);
    }

    // 특정 리스트에 포함된 RestaurantList를 반환하는 메소드
    public List<RestaurantList> getRestaurantListsByListNo(Integer listNo) {
        MyList myList = myListRepository.findById(listNo)
                .orElseThrow(() -> new IllegalArgumentException("List not found with ID: " + listNo));
        log.info("myList: " + myList);
        log.info("restaurantList: " + myList.getRestaurantList());
        return myList.getRestaurantList();
    }

    // 마커 좌표 설정
//    public List<RestaurantList> getAllRestaurantCoordinates() {
//        // 여기에 로직을 추가하여 식당 좌표와 관련 정보를 가져옵니다.
//        List<MyList> myLists = myListRepository.findAll();
//        List<RestaurantList> restaurantCoordinates = new ArrayList<>();
//
//        for (MyList myList : myLists) {
//            // 로그 확인
//            restaurantCoordinates.addAll(myList.getRestaurantList());
//            myList.getRestaurantList().forEach(restaurant ->
//                    log.info("식당 좌표: {}, Coordinates: ({}, {})",
//                            restaurant.getRestaurantName(),
//                            restaurant.getXCoordinate(),
//                            restaurant.getYCoordinate()));
//        }
//
//
//
//        return restaurantCoordinates;
//    }

    // 리스트 삭제 메소드
    @Transactional
    public void deleteRestaurants(Integer listNo, List<Long> restaurantNos) {

        MyList myList = myListRepository.findById(listNo)
                .orElseThrow(() -> new IllegalArgumentException("List not found with ID: " + listNo));

        List<RestaurantList> updatedList = myList.getRestaurantList().stream()
                .filter(restaurant -> !restaurantNos.contains(restaurant.getRestaurantNo()))
                .collect(Collectors.toList());

        myList.setRestaurantList(updatedList);
        myListRepository.save(myList);
    }

    // 내 리스트 가져오기 + 팔로우
    public List<MyList> getFollowingLists(Long loginedMemberNo) {

        return myListRepository.findByfollowMember(loginedMemberNo);
    }


}


