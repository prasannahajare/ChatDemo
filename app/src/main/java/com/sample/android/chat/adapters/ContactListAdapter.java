package com.sample.android.chat.adapters;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.Date;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sample.android.chat.ChatApplication;
import com.sample.android.chat.R;

public class ContactListAdapter extends CursorAdapter {

    private LayoutInflater cursorInflater;
    private Context context;
    static ImageLoader imageLoader;

    public ContactListAdapter(Context context, Cursor cursor) {
        super(context,cursor,false);
        cursorInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        imageLoader = ImageLoader.getInstance();

    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {


        View v = null;
        ViewHolder holder = new ViewHolder();

        v = cursorInflater.inflate(R.layout.contact_user_item, parent, false);

        holder.senderName = (TextView) v.findViewById(R.id.sendername);
        holder.senderUserName = (TextView) v.findViewById(R.id.senderusername);
        holder.messageCount = (TextView) v.findViewById(R.id.messagecount);
        holder.favouriteCount = (TextView) v.findViewById(R.id.favouriteCount);
        holder.imageView = (ImageView) v.findViewById(R.id.imageview);
        v.setTag(holder);
        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
            ViewHolder holder  = (ViewHolder) view.getTag();

            holder.senderName.setText(cursor.getString(cursor.getColumnIndex("name")));
            holder.senderUserName.setText(cursor.getString(cursor.getColumnIndex("username")));

            holder.messageCount.setText("Messages: "+getMessageCount(cursor.getString(cursor.getColumnIndex("name"))));
            holder.favouriteCount.setText(" Favourite: "+getFavouriteCount(cursor.getString(cursor.getColumnIndex("name"))));

            imageLoader.displayImage(cursor.getString(cursor.getColumnIndex("imageurl")),holder.imageView);

    }


    private class ViewHolder{
        public TextView senderName;
        public TextView senderUserName;
        public TextView messageCount;
        public TextView favouriteCount;
        public ImageView imageView;

    }

    private String getMessageCount(String name){
        Cursor cursor = null;
        String count = "0";
        SQLiteDatabase database = ChatApplication.getDatabseInstance(context);
        try{
            cursor = database.rawQuery("Select count(1) FROM chat_info WHERE name = '"+name+"'",null);
            if(cursor != null && cursor.moveToFirst()){
//                Log.i("ContactsFragment","Cursor Count: "+cursor.getCount());
                count = cursor.getString(0);
            }
        }catch(SQLiteException se){
            se.printStackTrace();
        }
        return count;
    }

    private String getFavouriteCount(String name){
        Cursor cursor = null;
        String count = "0";
        SQLiteDatabase database = ChatApplication.getDatabseInstance(context);
        try{
            cursor = database.rawQuery("Select count(1) FROM chat_info WHERE favourite = 1 and name = '"+name+"'",null);
            if(cursor != null && cursor.moveToFirst()){
//                Log.i("ContactsFragment","Cursor Count: "+cursor.getCount());
                count = cursor.getString(0);
            }
        }catch(SQLiteException se){
            se.printStackTrace();
        }
        return count;
    }

}
