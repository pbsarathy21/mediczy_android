package app.mediczy_com.chat;


import android.support.v4.app.Fragment;
/*import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import com.applozic.mobicomkit.api.account.user.User;

import java.util.ArrayList;

import app.mediczy_com.R;
import app.mediczy_com.adapter.Adapter_AllChat_Fragment;
import app.mediczy_com.chat.app_lozic.ApplozicBridge;
import app.mediczy_com.storage.LocalStore;*/

/**
 * Created by Prithivi on 17-03-2017.
 */

public class FragmentChat extends Fragment {

  /*  Context context;
    private ViewPager mViewPager;
    private TabLayout mSlidingTabLayout;
    private FragmentActivity myContext;

    private Adapter_AllChat_Fragment mAdapter;
    ArrayList<String> arrayTitle = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chat, container, false);
        context = getActivity();
        init(rootView);

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }

    private void init(View rootView) {
        arrayTitle.add("CHAT");
        arrayTitle.add("SPECIALITY");

        FragmentManager fragManager = myContext.getSupportFragmentManager(); //If using fragments from support v4
        mViewPager = (ViewPager) rootView.findViewById(R.id.fragment_chat_view_pager);
        mSlidingTabLayout = (TabLayout) rootView.findViewById(R.id.tabs);

        mAdapter = new Adapter_AllChat_Fragment(fragManager, arrayTitle, context);
        mViewPager.setAdapter(mAdapter);
        mSlidingTabLayout.setupWithViewPager(mViewPager);
    }*/
}
