package app.mediczy_com.utility;

import android.app.Activity;
import android.support.v4.app.Fragment;

/**
 * Created by prithivi on 21-Jan-16.
 */

public class FragmentCallBack extends Fragment {
    public FragmentCallBackListener fragmentCallBackListener;

    public interface FragmentCallBackListener {
        public void NextFragment(Fragment fragment, String fragmentValue, String title);
        public void setSubTile(String tag, String subtitle);
/*        public void onRefreshDrawer();
        public void onNearByFragmentHeader(String Title, boolean isFooterBar, int currentFragment);
        public void setActionBarData(String Title, boolean isPropertyResult);
        public void onBackToSearch();
        public void ClaerAllBackStack(Fragment fragment, String fragmentValue, String title);
        public void setScreenName(String ScreenName);
        public void openPanoX();
        public void onHideFragment(Fragment fragment, String fragmentValue, String title);*/
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            fragmentCallBackListener = (FragmentCallBackListener) activity;
        } catch (ClassCastException castException) {
            /** The activity does not implement the listener. */
        }
    }
}
