package com.balicodes.balinumbers.sections;

import com.balicodes.balinumbers.BaseSection;

/**
 * Created by eka on 7/5/15.
 */
public class FireFighter extends BaseSection {
    public static String TITLE = "Fire Fighters";

    @Override
    public String getJsonFileName() {
        return "fire_fighters.json";
    }

    @Override
    public boolean isDirectCallAction() {
        return true;
    }
}
