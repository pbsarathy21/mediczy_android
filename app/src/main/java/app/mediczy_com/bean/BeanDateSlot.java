package app.mediczy_com.bean;

/**
 * Created by prithivi on 27-10-2016.
 */
public class BeanDateSlot {

    String weekDay;
    String dateNumeric;
    String month;
    String year;
    boolean isSelected;

    public String getDateNumeric() {
        return dateNumeric;
    }

    public void setDateNumeric(String dateNumeric) {
        this.dateNumeric = dateNumeric;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(String weekDay) {
        this.weekDay = weekDay;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }
}
