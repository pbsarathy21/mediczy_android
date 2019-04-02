package app.mediczy_com.chat;

import android.support.v4.app.Fragment;

/**
 * Created by Prithivi on 17-03-2017.
 */

    public class FragmentChatConversation extends Fragment {

  /*  Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(com.applozic.mobicomkit.uiwidgets.R.layout.quickconversion_activity, container, false);
        context = getActivity();
        init(rootView, savedInstanceState);
        return rootView;
    }

    private void init(View rootView, Bundle savedInstanceState) {
        registerChat();

        Intent i = new Intent();
        i.setClass(rootView.getContext(), ConversationActivity.class);
        rootView.getContext().startActivity(i);
    }

    private void registerChat() {
        String userId = LocalStore.getInstance().getUserID(context);
        String email = LocalStore.getInstance().getFirstName(context);
        String password = LocalStore.getInstance().getUserID(context);
        String displayName = LocalStore.getInstance().getFirstName(context);
        String phoneNumber = LocalStore.getInstance().getPhoneNumber(context);
        User.AuthenticationType authenticationType;
        authenticationType = User.AuthenticationType.APPLOZIC;

        User user = new User();
        user.setUserId(userId);
        user.setEmail(email);
        user.setPassword(password);
        user.setDisplayName(displayName);
        user.setContactNumber(phoneNumber);
        user.setAuthenticationTypeId(authenticationType.getValue());
        ApplozicBridge.registerUserAndLaunchChat(context, user, "");
    }*/
}
