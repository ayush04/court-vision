/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cv.utils;

/**
 *
 * @author ayush
 */
public class Constants {
    public static final long MATCH_START_EVENT_TYPE = 1;
    public static final long MATCH_END_EVENT_TYPE = 2;
    public static final long BASKET_SCORED_EVENT_TYPE = 3;
    public static final long PLAYER_POSITION_EVENT_TYPE = 4;
    public static final long BALL_POSITION_EVENT_TYPE = 5;
    public static final long QUARTER_END_EVENT_TYPE = 6;
    public static final long USER_ROLE_CHANGED_TO_COACH = 7;
    
    public static final long COACH_ROLE_ID = 1;
    public static final long PLAYER_ROLE_ID = 2;
    
    public static final int GENERAL_TYPE_NOTIFICATION = 1;
    public static final int EVENT_TYPE_NOTIFICATION = 2;
    public static final int PERSONAL_NOTIFICATION = 3;
    public static final int COACH_ACCESS_NOTIFICATION = 4;
    
    public static final String RASPI_CAMERA_CLIENT = "CV_CAMERA";
    public static final String PUBNUB_PUBLISH_KEY = "";
    public static final String PUBNUB_SUBSCRIBE_KEY = "";
    public static final String[] PUBNUB_CHANNELS = new String[] {"RASPI_CHANNEL"};
}
