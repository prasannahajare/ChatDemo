package com.sample.android.chat.utils;

/**
 * Created by sa on 6/30/16.
 */
public class Constants {

    public static final int CHAT_FRAGMENT_INDEX = 0;
    public static final int CONTACT_FRAGMENT_INDEX = 1;

    public static final String chat_url = "http://haptik.co/android/test_data";

    public static final String CHAT_TABLE_CREATION = "CREATE TABLE IF NOT EXISTS chat_info" +
            " (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT not null, username TEXT not null," +
            " body TEXT not null, imageurl TEXT, timestamp LONG UNIQUE ON CONFLICT REPLACE, favourite INTEGER DEFAULT(0),type TEXT not null);";
}
