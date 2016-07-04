package com.sample.android.chat.adapters;

import android.content.Context;
import android.database.Cursor;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.Date;
import com.sample.android.chat.R;

public class ChatListAdapter extends CursorAdapter {

    private LayoutInflater cursorInflater;
    private Context context;

    public ChatListAdapter(Context context, Cursor cursor) {
        super(context,cursor,false);
        cursorInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {


        View v = null;

        if (cursor.getString(cursor.getColumnIndex("type")).equals("1")) {
            ViewHolder1 holder1 = new ViewHolder1();
            v = cursorInflater.inflate(R.layout.chat_user1_item, parent, false);
            holder1.senderNameView = (TextView) v.findViewById(R.id.sender_name);
            holder1.messageTextView = (TextView) v.findViewById(R.id.textview_message);
            holder1.timeTextView = (TextView) v.findViewById(R.id.textview_time);
            v.setTag(holder1);
        } else if (cursor.getString(cursor.getColumnIndex("type")).equals("0")){
            ViewHolder2 holder2 = new ViewHolder2();
            v = cursorInflater.inflate(R.layout.chat_user2_item, parent, false);
            holder2.messageTextView = (TextView) v.findViewById(R.id.textview_message);
            holder2.timeTextView = (TextView) v.findViewById(R.id.textview_time);
            holder2.messageStatus = (ImageView) v.findViewById(R.id.user_reply_status);
            v.setTag(holder2);
        }


        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        if (cursor.getString(cursor.getColumnIndex("type")).equals("1")) {
            ViewHolder1 holder1 = (ViewHolder1) view.getTag();

            holder1.senderNameView.setText(cursor.getString(cursor.getColumnIndex("name")));
            holder1.messageTextView.setText(cursor.getString(cursor.getColumnIndex("body")));

            long now = new Date().getTime();
            String finalDate = DateUtils.getRelativeTimeSpanString(cursor.getLong(cursor.getColumnIndex("timestamp")),
                    now, DateUtils.SECOND_IN_MILLIS).toString();
            holder1.timeTextView.setText(finalDate);

        }else if (cursor.getString(cursor.getColumnIndex("type")).equals("0")){
            ViewHolder2 holder2 = (ViewHolder2) view.getTag();

            holder2.messageTextView.setText(cursor.getString(cursor.getColumnIndex("body")));
            long now = new Date().getTime();
            String finalDate = DateUtils.getRelativeTimeSpanString(cursor.getLong(cursor.getColumnIndex("timestamp")),
                    now, DateUtils.SECOND_IN_MILLIS).toString();

            holder2.timeTextView.setText(finalDate);
        }


    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    private int getItemViewType(Cursor cursor) {
        return Integer.parseInt(cursor.getString(cursor.getColumnIndex("type")));
    }

    @Override
    public int getItemViewType(int position) {
        Cursor cursor = (Cursor) getItem(position);
        return getItemViewType(cursor);
    }

    private class ViewHolder1 {
        public TextView senderNameView;
        public TextView messageTextView;
        public TextView timeTextView;


    }

    private class ViewHolder2 {
        public ImageView messageStatus;
        public TextView messageTextView;
        public TextView timeTextView;

    }

}
