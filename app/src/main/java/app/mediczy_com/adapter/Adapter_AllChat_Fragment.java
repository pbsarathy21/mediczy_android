package app.mediczy_com.adapter;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import java.util.ArrayList;

import app.mediczy_com.chat.FragmentChat;
import app.mediczy_com.chat.FragmentChatConversation;
import app.mediczy_com.chat.FragmentChatSpeciality;

/**
 * Created by prithivi on 17-Mar-17.
 */
public class Adapter_AllChat_Fragment extends FragmentStatePagerAdapter
{
    ArrayList<String> tab_title;
    private Context fragment;

    public Adapter_AllChat_Fragment(FragmentManager fm, ArrayList<String> tab_title, Context ctx) {
        super(fm);
        this.tab_title = tab_title;
        this.fragment = ctx;
    }

    @Override
    public Fragment getItem(int index) {
        switch (tab_title.get(index)) {
            case "CHAT":
                Fragment fChat= new FragmentChatConversation();
                Bundle bundle = new Bundle();
                fChat.setArguments(bundle);
                return fChat;
            case "SPECIALITY":
                Fragment fSPECIALITY= new FragmentChatSpeciality();
                Bundle bundlea = new Bundle();
                fSPECIALITY.setArguments(bundlea);
                return fSPECIALITY;
        }
        return null;
    }

    // This method describes to get the page title
    @Override
    public CharSequence getPageTitle(int position) {
        return tab_title.get(position);
    }

    // This method describes to get the page count
    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return tab_title.size();
    }
    @Override
    public Parcelable saveState() {
        return null;
    }
}
