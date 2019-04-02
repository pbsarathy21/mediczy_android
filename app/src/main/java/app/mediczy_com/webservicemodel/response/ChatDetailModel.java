package app.mediczy_com.webservicemodel.response;

import android.graphics.Bitmap;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * Created by Prithivi Raj on 08-06-2017.
 */

public class ChatDetailModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("msg")
    public String msg;

    @SerializedName("status")
    public String status;

    @SerializedName("message_id")
    public String message_id;

    @SerializedName("doctor_id")
    public String doctor_id;

    @SerializedName("patient_id")
    public String patient_id;

    @SerializedName("time")
    public String time;

    @SerializedName("message")
    public String message;

    @SerializedName("name")
    public String name;

    @SerializedName("image")
    public String image;

    @SerializedName("profile_image")
    public String profile_image;

    @SerializedName("type")
    public String type;

    @SerializedName("file_name")
    public String file_name;

    @SerializedName("file_extension")
    public String file_extension;

    @SerializedName("receiver_id")
    public String receiver_id;

    @SerializedName("sender_id")
    public String sender_id;

    @SerializedName("category_type")
    public String category_type;

    @SerializedName("id")
    public String id;

    @SerializedName("read_status")
    public String read_status;

    @SerializedName("block_type")
    public String block_type;

    @SerializedName("user_type")
    public String user_type;

    @SerializedName("bitmap")
    public Bitmap bitmap;

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getRead_status() {
        return read_status;
    }

    public void setRead_status(String read_status) {
        this.read_status = read_status;
    }

    public String getBlock_type() {
        return block_type;
    }

    public void setBlock_type(String block_type) {
        this.block_type = block_type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory_type() {
        return category_type;
    }

    public void setCategory_type(String category_type) {
        this.category_type = category_type;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(String reciever_id) {
        this.receiver_id = reciever_id;
    }

    public String getFile_extension() {
        return file_extension;
    }

    public void setFile_extension(String file_extension) {
        this.file_extension = file_extension;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(String doctor_id) {
        this.doctor_id = doctor_id;
    }

    public String getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(String patient_id) {
        this.patient_id = patient_id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
