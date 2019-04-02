package app.mediczy_com.appointment;

import java.util.ArrayList;

import app.mediczy_com.bean.BeanYourAppointment;

/**
 * Created by Prithivi on 30-10-2016.
 */

public interface DoctorAcceptObserver {

    void onDoctorAccept(int position, ArrayList<BeanYourAppointment> arrayList, boolean status);
}
