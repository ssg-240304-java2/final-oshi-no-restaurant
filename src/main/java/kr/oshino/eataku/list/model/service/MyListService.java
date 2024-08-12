package kr.oshino.eataku.list.model.service;

import kr.oshino.eataku.list.entity.MyList;
import kr.oshino.eataku.list.model.repository.MyListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MyListService {

    @Autowired
    private MyListRepository myListRepository;
    private String listStatus;

    public void createList(String listName) {
        MyList myList = MyList.builder()
                .listName(listName)
                .listStatus("Public")
                .listShare(0L)
                .build();
        myListRepository.save(myList);
    }

    public List<MyList> getLists() {
        return myListRepository.findAll();
    }

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

        list.setListStatus(listStatus); // 상태 변경
        myListRepository.save(list); // 변경 사항 저장

        // 로그 출력으로 변경된 상태 확인
        System.out.println("New Status: " + list.getListStatus());
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
}
