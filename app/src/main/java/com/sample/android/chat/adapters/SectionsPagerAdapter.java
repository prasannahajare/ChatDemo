package com.sample.android.chat.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.sample.android.chat.ui.ChatFragment;
import com.sample.android.chat.ui.ContactsFragment;
import com.sample.android.chat.utils.Constants;

/**
 * Created by sa on 6/30/16.
 */
/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return specific fragment based on position
        switch (position){
            case Constants.CHAT_FRAGMENT_INDEX:return ChatFragment.newInstance();
            case Constants.CONTACT_FRAGMENT_INDEX: return ContactsFragment.newInstance();
            default: return ChatFragment.newInstance();
        }

    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "CHAT";
            case 1:
                return "CONTACTS";
        }
        return null;
    }
}
