package app.mediczy_com.bean;

/**
 * Created by prithivi on 27-10-2016.
 */
public class BeanTimeSlot {

    String Time;
    String Available;
    boolean isClicked =false;

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getAvailable() {
        return Available;
    }

    public void setAvailable(String available) {
        Available = available;
    }

    public boolean isClicked() {
        return isClicked;
    }

    public void setClicked(boolean clicked) {
        isClicked = clicked;
    }
}
