package com.balicodes.balinumbers.sections;

import com.balicodes.balinumbers.BaseSection;

/**
 * Created by eka on 7/3/15.
 */
public class Hospitals extends BaseSection {
    public static String TITLE = "Hospitals";

    @Override
    public String getJsonFileName() {
        return "hospitals.json";
    }

    @Override
    public String getHotlineText(String number) {
        return "AMBULANCE - " + number;
    }
}
