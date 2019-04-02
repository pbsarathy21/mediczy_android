package app.mediczy_com.chat.chat_new;

import android.widget.ImageView;

/**
 * Created by Prithivi on 01-07-2017.
 */

public interface ConversationDeleteObserver {

    void onConversationClicked(int position, ImageView iV);

    void onConversationLongClicked(int position);
}
