package com.balicodes.balinumbers.sections;

import com.balicodes.balinumbers.BaseSection;

/**
 * Created by eka on 7/4/15.
 */
public class Emergency extends BaseSection {
    public static String TITLE = "Emergency";

    @Override
    public String getJsonFileName() {
        return "emergency.json";
    }

    @Override
    public boolean isDirectCallAction() {
        return true;
    }
}
