package com.apps4bali.nomorpenting.models;

import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Contact {
    private String id;
    private String title;
    private String desc;
    private String address;
    private List<String> numbers = new ArrayList<>();
    private String area;

    public Contact() {
    }

    public Contact(QueryDocumentSnapshot doc) {
        this.id = doc.getId();
        Map<String, Object> data = doc.getData();

        if (data.containsKey("title")) {
            title = (String) data.get("title");
        }
        if (data.containsKey("desc")) {
            desc = (String) data.get("desc");
        }
        if (data.containsKey("address")) {
            address = (String) data.get("address");
        }
        if (data.containsKey("area")) {
            area = (String) data.get("area");
        }
        if (data.containsKey("numbers")) {
            try {
                numbers = (List<String>) data.get("numbers");
            } catch (Exception e) {
                numbers = new ArrayList<>();
            }

        }
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public String getAddress() {
        return address;
    }

    public List<String> getNumbers() {
        return numbers;
    }

    public String getArea() {
        return area;
    }
}
