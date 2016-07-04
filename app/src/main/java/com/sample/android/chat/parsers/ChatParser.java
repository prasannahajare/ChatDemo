package com.sample.android.chat.parsers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.sample.android.chat.ChatApplication;
import com.sample.android.chat.utils.ChatModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by sa on 6/30/16.
 */
public class ChatParser {

    private static ArrayList<ChatModel> chatDataList;
    private static final String TAG = ChatParser.class.getSimpleName();


    public static Cursor parseChatData(Context context,String response) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Cursor cursor = null;

        chatDataList = new ArrayList<ChatModel>();
        if (!response.equals("")) {

            try {
                JSONObject jsonObj = new JSONObject(response);
                JSONArray messageArray = jsonObj.getJSONArray("messages");
                for(int messageIndex = 0; messageIndex < messageArray.length(); messageIndex++){
                    JSONObject messageObj = messageArray.getJSONObject(messageIndex);
                    ChatModel model = new ChatModel();

                    model.setName(messageObj.getString("Name"));
                    model.setUsername(messageObj.getString("username"));
                    model.setBody(messageObj.getString("body"));
                    model.setImage_url(messageObj.getString("image-url"));


                    String time = messageObj.getString("message-time");
                    Date date = sdf.parse(time);

                    model.setTimeStamp(date.getTime());
                    if(model.getUsername().equalsIgnoreCase("john-t")){
                        model.setType("0");
                    }else{
                        model.setType("1");
                    }
                    chatDataList.add(model);

                }

            } catch (ParseException e) {
                e.printStackTrace();
            }catch (JSONException e) {
                e.printStackTrace();
            }
            Collections.sort(chatDataList, new Comparator<ChatModel>() {
                @Override
                public int compare(ChatModel lhs, ChatModel rhs) {
                    return String.valueOf(lhs.getTimeStamp()).compareTo(String.valueOf(rhs.getTimeStamp()));
                }
            });
            cursor = insertDataIntoDB(context, chatDataList);
        }

        return cursor;

    }

    private static Cursor insertDataIntoDB(Context context, ArrayList<ChatModel> chatDataList){

        Cursor cursor = null;
        try {
            SQLiteDatabase database = ChatApplication.getDatabseInstance(context);
            database.beginTransaction();

            SQLiteStatement statement = null;
            for (int index = 0; index < chatDataList.size(); index++) {
                try {
                    ChatModel model = chatDataList.get(index);
                    statement = database.compileStatement("INSERT INTO chat_info (name, username, body, imageurl, timestamp, type) VALUES (?,?,?,?,?,?)");
                    statement.clearBindings();
                    statement.bindString(1, model.getName());
                    statement.bindString(2, model.getUsername());
                    statement.bindString(3, model.getBody());
                    statement.bindString(4, model.getImage_url());
                    statement.bindLong(5, model.getTimeStamp());

                    if(model.getUsername().equalsIgnoreCase("john-t")){
                        statement.bindString(6, "0");
                    }else{
                        statement.bindString(6, "1");
                    }
                    statement.execute();
//                    Log.i(TAG,"insertion: "+index);
                } catch (SQLiteException se) {
                    se.printStackTrace();
                } finally {
                    statement.close();
                }
            }
            database.setTransactionSuccessful();
            database.endTransaction();


            cursor = database.rawQuery("Select * FROM chat_info",null);
            if(cursor != null && cursor.moveToFirst()){
                Log.i(TAG,"Cursor count: "+cursor.getCount());
                return cursor;
            }
        }catch(SQLiteException se){
            se.printStackTrace();
        }

            return  cursor;

    }

}
