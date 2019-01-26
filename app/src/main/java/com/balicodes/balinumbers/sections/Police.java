package com.balicodes.balinumbers.sections;

import com.balicodes.balinumbers.BaseSection;

/**
 * Created by eka on 7/3/15.
 */
public class Police extends BaseSection {
    public static String TITLE = "Police";

    @Override
    public String getJsonFileName() {
        return "police.json";
    }

    @Override
    public boolean isDirectCallAction() {
        return true;
    }
}
