package lk.hemas.ayubo.model;

import java.io.Serializable;

/**
 * Created by Sabri on 3/12/2018. model for doctor
 */

public class Doctor implements Serializable {

    private int id;
    private String name;
    private String specialty;
    private String imgUrl;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }
}
