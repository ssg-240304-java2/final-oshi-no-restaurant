package kr.oshino.eataku.list.model.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MyListService {

    private final List<String> lists = new ArrayList<>();

    public void createList(String listName) {
        lists.add(listName);
    }

    public List<String> getLists() {
        return new ArrayList<>(lists);
    }

    public void modifyList(int listNo) {
        lists.set(listNo, lists.get(listNo));
    }

    public boolean updateListTitle(int listNo, String listName) {
        return false;
    }
}
