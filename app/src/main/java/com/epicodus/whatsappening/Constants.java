package com.epicodus.whatsappening;

/**
 * Created by Guest on 5/4/16.
 */
public class Constants {

    public static final String FIREBASE_URL = BuildConfig.FIREBASE_ROOT_URL;

    public static final String FIREBASE_LOCATION_FRIENDS = "friends";
    public static final String FIREBASE_PROPERTY_EMAIL = "email";
    public static final String KEY_UID = "UID";
    public static final String FIREBASE_URL_FRIENDS = FIREBASE_URL + "/" + FIREBASE_LOCATION_FRIENDS;

    public static final String FIREBASE_LOCATION_MESSAGES = "messages";
    public static final String FIREBASE_URL_MESSAGES = FIREBASE_URL + "/" + FIREBASE_LOCATION_MESSAGES;
    public static final String KEY_USER_EMAIL = "email";

}
