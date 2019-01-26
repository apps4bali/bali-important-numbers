package com.balicodes.balinumbers.sections;

import com.balicodes.balinumbers.BaseSection;

/**
 * Created by eka on 7/5/15.
 */
public class Taxi extends BaseSection {
    public static String TITLE = "Taxi";

    @Override
    public String getJsonFileName() {
        return "taxi.json";
    }

    @Override
    public boolean isDirectCallAction() {
        return true;
    }
}
