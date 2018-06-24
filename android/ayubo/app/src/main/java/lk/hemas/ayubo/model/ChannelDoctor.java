package lk.hemas.ayubo.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sabri on 3/18/2018. model for Channeling doctor
 */

public class ChannelDoctor implements Serializable{

    private int doctor_code;
    private String doctor_name;
    private String doc_image;
    private String favourite;
    private String specialization;
    private int specialization_id;
    private HashMap<String, Hospital> hospitals;

    public int getDoctor_code() {
        return doctor_code;
    }

    public void setDoctor_code(int doctor_code) {
        this.doctor_code = doctor_code;
    }

    public String getDoctor_name() {
        return doctor_name;
    }

    public void setDoctor_name(String doctor_name) {
        this.doctor_name = doctor_name;
    }

    public String getDoc_image() {
        return doc_image;
    }

    public void setDoc_image(String doc_image) {
        this.doc_image = doc_image;
    }

    public String getFavourite() {
        return favourite;
    }

    public void setFavourite(String favourite) {
        this.favourite = favourite;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public int getSpecialization_id() {
        return specialization_id;
    }

    public void setSpecialization_id(int specialization_id) {
        this.specialization_id = specialization_id;
    }

    public HashMap<String, Hospital> getHospitals() {
        return hospitals;
    }

    public void setHospitals(HashMap<String, Hospital> hospitals) {
        this.hospitals = hospitals;
    }
}
