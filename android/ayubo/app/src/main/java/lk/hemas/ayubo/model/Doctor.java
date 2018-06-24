package lk.hemas.ayubo.model;

import java.io.Serializable;

/**
 * Created by Sabri on 3/12/2018. model for doctor
 */

public class Doctor implements Serializable {

    private int id;
    private String name;
    private String full_name;
    private String ech_code;
    private String doc990_code;
    private String hemas_code;
    private String specialization_id;
    private String hospital_id;
    private String doc_image;

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

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getEch_code() {
        return ech_code;
    }

    public void setEch_code(String ech_code) {
        this.ech_code = ech_code;
    }

    public String getDoc990_code() {
        return doc990_code;
    }

    public void setDoc990_code(String doc990_code) {
        this.doc990_code = doc990_code;
    }

    public String getHemas_code() {
        return hemas_code;
    }

    public void setHemas_code(String hemas_code) {
        this.hemas_code = hemas_code;
    }

    public String getSpecialization_id() {
        return specialization_id;
    }

    public void setSpecialization_id(String specialization_id) {
        this.specialization_id = specialization_id;
    }

    public String getHospital_id() {
        return hospital_id;
    }

    public void setHospital_id(String hospital_id) {
        this.hospital_id = hospital_id;
    }

    public String getDoc_image() {
        return doc_image;
    }

    public void setDoc_image(String doc_image) {
        this.doc_image = doc_image;
    }
}
