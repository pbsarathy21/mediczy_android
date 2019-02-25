package app.mediczy_com.bean;

/**
 * Created by Prithivi Raj on 14-12-2015.
 */
public class Bean_Doctor_List {
    String Name;
    String Image;
    String Id;
    String OnLine_Status;
    String Study;
    String nextAvailable;
    String Available;
    String Experience;
    String Doctor_Type;
    String Available_Date;
    String Rate;
    String Number;

    public String getNextAvailable() {
        return nextAvailable;
    }

    public void setNextAvailable(String nextAvailable) {
        this.nextAvailable = nextAvailable;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getOnLine_Status() {
        return OnLine_Status;
    }

    public void setOnLine_Status(String onLine_Status) {
        OnLine_Status = onLine_Status;
    }

    public String getStudy() {
        return Study;
    }

    public void setStudy(String study) {
        Study = study;
    }

    public String getAvailable() {
        return Available;
    }

    public void setAvailable(String available) {
        Available = available;
    }

    public String getExperience() {
        return Experience;
    }

    public void setExperience(String experience) {
        Experience = experience;
    }

    public String getAvailable_Date() {
        return Available_Date;
    }

    public void setAvailable_Date(String available_Date) {
        Available_Date = available_Date;
    }

    public String getRate() {
        return Rate;
    }

    public void setRate(String rate) {
        Rate = rate;
    }

    public String getDoctor_Type() {
        return Doctor_Type;
    }

    public void setDoctor_Type(String doctor_Type) {
        Doctor_Type = doctor_Type;
    }
}
