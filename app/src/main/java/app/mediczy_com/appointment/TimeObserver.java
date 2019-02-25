package app.mediczy_com.appointment;

import java.util.ArrayList;

import app.mediczy_com.bean.BeanTimeSlot;

/**
 * Created by Prithivi on 30-10-2016.
 */

public interface TimeObserver {

    void onSelectedTime(int position, ArrayList<BeanTimeSlot> arrayList, boolean status);
}
