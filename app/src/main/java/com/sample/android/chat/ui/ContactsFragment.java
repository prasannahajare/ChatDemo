package com.sample.android.chat.ui;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;

import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.sample.android.chat.ChatApplication;
import com.sample.android.chat.R;
import com.sample.android.chat.adapters.ContactListAdapter;

/**
 * Created by sa on 6/30/16.
 */
public class ContactsFragment  extends Fragment {

    private ContactListAdapter contactListAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView listView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.contact_frag, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout)v.findViewById(R.id.swiperefreshlayout);
        listView = (ListView) v.findViewById(R.id.contact_list_view);


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {
                refreshContent();
            }
        });
        contactListAdapter = new ContactListAdapter(getActivity(), fetchDataFromDB());
        listView.setAdapter(contactListAdapter);


        return v;
    }

    private void refreshContent(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                contactListAdapter = new ContactListAdapter(getActivity(), fetchDataFromDB());
                listView.setAdapter(contactListAdapter);
                swipeRefreshLayout.setRefreshing(false);
            }
        },1000);
    }

    public static ContactsFragment newInstance() {
        ContactsFragment contactsFragment = new ContactsFragment();
        return contactsFragment;
    }

    private Cursor fetchDataFromDB(){
        Cursor cursor = null;
        SQLiteDatabase database = ChatApplication.getDatabseInstance(getActivity());
        try{
            cursor = database.rawQuery("Select * FROM chat_info GROUP BY name",null);
            if(cursor != null && cursor.moveToFirst()){
//                Log.i("ContactsFragment","Cursor Count: "+cursor.getCount());
                return cursor;
            }
        }catch(SQLiteException se){
            se.printStackTrace();
        }
        return cursor;
    }

    //cursor c1 = select * from chat_info where name unique
    // while(c1.hasnext){
    // cursor.getName}


}
