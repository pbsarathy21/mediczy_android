package app.mediczy_com.bean;

/**
 * Created by Prithivi Raj on 11-12-2015.
 */

public class Home_Type_Bean {

    public static final int TYPE_CATEGORY = 0;
    public static final int TYPE_ADS = 1;

    String Image;
    String Count;
    String TypeName;
    String category_id;
    String hospital_id;
    int type;
    String view_type;
    String TitleAds;
    String SubjectAds;
    String DescAds;
    String DateAds;
    String ImageAds;
    String IdAds;
    String TypeAds;
    String companyLogo;
    String companyName;
    String viewCount;

    public String getView_type() {
        return view_type;
    }

    public void setView_type(String view_type) {
        this.view_type = view_type;
    }

    public String getCompanyLogo() {
        return companyLogo;
    }

    public void setCompanyLogo(String companyLogo) {
        this.companyLogo = companyLogo;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getViewCount() {
        return viewCount;
    }

    public void setViewCount(String viewCount) {
        this.viewCount = viewCount;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getCount() {
        return Count;
    }

    public void setCount(String count) {
        Count = count;
    }

    public String getTypeName() {
        return TypeName;
    }

    public void setTypeName(String typeName) {
        TypeName = typeName;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getHospital_id() {
        return hospital_id;
    }

    public void setHospital_id(String hospital_id) {
        this.hospital_id = hospital_id;
    }

    public String getTitleAds() {
        return TitleAds;
    }

    public void setTitleAds(String titleAds) {
        TitleAds = titleAds;
    }

    public String getSubjectAds() {
        return SubjectAds;
    }

    public void setSubjectAds(String subjectAds) {
        SubjectAds = subjectAds;
    }

    public String getDescAds() {
        return DescAds;
    }

    public void setDescAds(String descAds) {
        DescAds = descAds;
    }

    public String getDateAds() {
        return DateAds;
    }

    public void setDateAds(String dateAds) {
        DateAds = dateAds;
    }

    public String getImageAds() {
        return ImageAds;
    }

    public void setImageAds(String imageAds) {
        ImageAds = imageAds;
    }

    public String getIdAds() {
        return IdAds;
    }

    public void setIdAds(String idAds) {
        IdAds = idAds;
    }

    public String getTypeAds() {
        return TypeAds;
    }

    public void setTypeAds(String typeAds) {
        TypeAds = typeAds;
    }
}
