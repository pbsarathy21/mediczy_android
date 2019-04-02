package app.mediczy_com.bean;

/**
 * Created by Prithivi Raj on 08-01-2016.
 */
public class Bean_Ambulance {
    String Hospital_Name;
    String Location;
    String Number;
    String Km;
    String Offer;
    String image;
    boolean like;

    public boolean isLike() {
        return like;
    }

    public void setLike(boolean like) {
        this.like = like;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getHospital_Name() {
        return Hospital_Name;
    }

    public void setHospital_Name(String hospital_Name) {
        Hospital_Name = hospital_Name;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public String getKm() {
        return Km;
    }

    public void setKm(String km) {
        Km = km;
    }

    public String getOffer() {
        return Offer;
    }

    public void setOffer(String offer) {
        Offer = offer;
    }
}
