package com.balicodes.balinumbers.models;

import android.os.Bundle;

import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Map;

public class Section {
    private String id;
    private Long order;
    private String title;
    private String hotline;
    private String hotlineText;
    private Boolean directCallBtn = false;

    public Section() {
    }

    public Section(QueryDocumentSnapshot doc) {
        this.id = doc.getId();
        Map<String, Object> data = doc.getData();

        if (data.containsKey("title")) {
            title = (String) data.get("title");
        }
        if (data.containsKey("order")) {
            order = (Long) data.get("order");
        }
        if (data.containsKey("hotline")) {
            hotline = (String) data.get("hotline");
        }
        if (data.containsKey("hotline_text")) {
            hotlineText = (String) data.get("hotline_text");
        } else {
            hotlineText = "HOTLINE";
        }
        if (data.containsKey("direct_call_btn")) {
            directCallBtn = (Boolean) data.get("direct_call_btn");
        }
    }

    public String getId() {
        return id;
    }

    public Long getOrder() {
        return order;
    }

    public String getTitle() {
        return title;
    }

    public String getHotline() {
        return hotline;
    }

    public String getHotlineText() {
        return hotlineText;
    }

    public Boolean getDirectCallBtn() {
        return directCallBtn;
    }

    public Bundle toBundle() {
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putLong("order", order);
        bundle.putString("title", title);
        bundle.putString("hotline", hotline);
        bundle.putString("hotlineText", hotlineText);
        bundle.putBoolean("directCallBtn", directCallBtn);
        return bundle;
    }
}
