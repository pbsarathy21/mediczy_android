package app.mediczy_com.webservicemodel.response;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Prithivi on 26-06-2017.
 */

public class ChatResponseModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("msg")
    public String msg;

    @SerializedName("status")
    public String status;

    @SerializedName("id")
    public String id;

    @SerializedName("sender_id")
    public String sender_id;

    @SerializedName("conversation_id")
    public String conversation_id;

    @SerializedName("user_type")
    public String user_type;

    @SerializedName("message_id")
    public String message_id;

    @SerializedName("type")
    public String type;

    @SerializedName("image")
    public String image;

    @SerializedName("image_path")
    public String image_path;

    @SerializedName("created_at")
    public String created_at;

    @SerializedName("message")
    public String message;

    @SerializedName("receiver_id")
    public String receiver_id;

    @SerializedName("block_status")
    public String block_status;

    @SerializedName("messages")
    public List<ConversationListModel> chatListModel;

    @SerializedName("conversations")
    public List<ConversationListModel> conversationListModel;


    public class Message implements Serializable {
        private static final long serialVersionUID = 1L;

        @SerializedName("conversation_id")
        public String conversation_id;

        @SerializedName("sender_id")
        public String sender_id;

        @SerializedName("receiver_id")
        public String receiver_id;

        @SerializedName("type")
        public String type;

        @SerializedName("message")
        public String message;

        @SerializedName("block_status")
        public String block_status;

        @SerializedName("image_path")
        public String image_path;

        @SerializedName("id")
        public String id;
    }

    public class ConversationListModel implements Serializable {
        private static final long serialVersionUID = 1L;

        @SerializedName("conversation_id")
        public String conversation_id;

        @SerializedName("name")
        public String name;

        @SerializedName("user_type")
        public String user_type;

        @SerializedName("message_id")
        public String message_id;

        @SerializedName("type")
        public String type;

        @SerializedName("doctor_id")
        public String doctor_id;

        @SerializedName("patient_id")
        public String patient_id;

        @SerializedName("image")
        public String image;

        @SerializedName("image_path")
        public String image_path;

        @SerializedName("profile_image")
        public String profile_image;

        @SerializedName("created_at")
        public String created_at;

        @SerializedName("receiver_id")
        public String receiver_id;

        @SerializedName("sender_id")
        public String sender_id;

        @SerializedName("message")
        public String message;

        @SerializedName("doctor_category")
        public String doctor_category;

        @SerializedName("message_seen")
        public String message_seen;

        @SerializedName("block_status")
        public String block_status;

        @SerializedName("read_status")
        public String read_status;

        @SerializedName("updated_at")
        public String updated_at;
    }
}
