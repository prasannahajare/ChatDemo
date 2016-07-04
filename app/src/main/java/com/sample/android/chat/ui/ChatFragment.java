package com.sample.android.chat.ui;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteStatement;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.sample.android.chat.ChatApplication;
import com.sample.android.chat.R;
import com.sample.android.chat.adapters.ChatListAdapter;
import com.sample.android.chat.network.NetworkManager;
import com.sample.android.chat.parsers.ChatParser;
import com.sample.android.chat.utils.Constants;

/**
 * Created by sa on 6/30/16.
 */
public class ChatFragment extends Fragment{

    private static final String TAG = ChatFragment.class.getSimpleName();
    private ChatListAdapter chatListAdapter;
    private ListView listView;
    private ActionMode mActionMode;
    private int positionValue =0 ;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new fetchDataAsyncTask().execute();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.chat_frag, container, false);
        listView = (ListView) v.findViewById(R.id.chat_list_view);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View view,
                                           int pos, long id) {
                // TODO Auto-generated method stub
                if (mActionMode != null) {
                    return false;
                }
                Log.i(TAG,"Position: "+pos);
                positionValue = pos;
                // Start the CAB using the ActionMode.Callback defined above
                mActionMode = getActivity().startActionMode(mActionModeCallback);
                view.setSelected(true);
                return true;
            }
        });

        return v;
    }

    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        private Menu menu;
        // Called when the action mode is created; startActionMode() was called
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.menu, menu);
            this.menu = menu;
            return true;
        }

        // Called each time the action mode is shown. Always called after onCreateActionMode, but
        // may be called multiple times if the mode is invalidated.
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }

        // Called when the user selects a contextual menu item
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.favorite:
                    menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_favorite_black_24dp));

                    Cursor cursor = (Cursor)chatListAdapter.getItem(positionValue);
                    if(cursor != null && cursor.moveToPosition(positionValue)){
                        updateFavouriteValue(getActivity(),cursor.getLong(cursor.getColumnIndex("timestamp")));
                    }
                    mode.finish(); // Action picked, so close the CAB
                    return true;
                default:
                    return false;
            }
        }

        // Called when the user exits the action mode
        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
        }
    };

    public static ChatFragment newInstance() {
        ChatFragment chatFragment = new ChatFragment();
        return chatFragment;
    }

    private class fetchDataAsyncTask extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Void... voids) {
            String response = NetworkManager.getDataFromNetwork(getActivity(), Constants.chat_url);
            return ChatParser.parseChatData(getActivity(), response);

        }

        @Override
        protected void onPostExecute(Cursor cursor) {

//            Log.i("ChatFragment","Cursor count: "+cursor.getCount());
            chatListAdapter = new ChatListAdapter(getActivity(),cursor);
            listView.setAdapter(chatListAdapter);
//            if(cursor!=null){
//                cursor.close();
//            }

        }
    }

    private void updateFavouriteValue(Context context, long timestamp){

        SQLiteDatabase database = ChatApplication.getDatabseInstance(context);
        SQLiteStatement statement = null;
        try{
            statement = database.compileStatement("UPDATE chat_info SET favourite = 1 WHERE timestamp = '"+timestamp+"'");
            statement.execute();
        }catch(SQLiteException se){
            se.printStackTrace();
        }finally {
            statement.close();
        }
    }

}
