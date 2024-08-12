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
        MyList myList = myListRepository.findById(listNo).orElse(null);
        if (myList != null) {
            myList.setListName(newListName);
            myListRepository.save(myList);
            System.out.println("List updated successfully: " + newListName);
        } else {
            System.out.println("List not found for listNo: " + listNo);
        }
    }

}
