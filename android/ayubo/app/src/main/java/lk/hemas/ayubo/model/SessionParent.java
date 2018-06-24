package lk.hemas.ayubo.model;

import java.io.Serializable;
import java.util.HashMap;

public class SessionParent implements Serializable {

    private String doctor_name;
    private int doctor_code;
    private String favourite;
    private String doc_image;
    private String specialization_name;
    private int specialization_id;
    private String special_notes;
    private int hospital_id;
    private String hospital_name;
    private String hospital_image;
    private String[] source;
    private String direct;
    private HashMap<String, Session> sessions;

    public String getDoctor_name() {
        return doctor_name;
    }

    public void setDoctor_name(String doctor_name) {
        this.doctor_name = doctor_name;
    }

    public int getDoctor_code() {
        return doctor_code;
    }

    public void setDoctor_code(int doctor_code) {
        this.doctor_code = doctor_code;
    }

    public String getFavourite() {
        return favourite;
    }

    public void setFavourite(String favourite) {
        this.favourite = favourite;
    }

    public String getDoc_image() {
        return doc_image;
    }

    public void setDoc_image(String doc_image) {
        this.doc_image = doc_image;
    }

    public String getSpecialization_name() {
        return specialization_name;
    }

    public void setSpecialization_name(String specialization_name) {
        this.specialization_name = specialization_name;
    }

    public int getSpecialization_id() {
        return specialization_id;
    }

    public void setSpecialization_id(int specialization_id) {
        this.specialization_id = specialization_id;
    }

    public String getSpecial_notes() {
        return special_notes;
    }

    public void setSpecial_notes(String special_notes) {
        this.special_notes = special_notes;
    }

    public int getHospital_id() {
        return hospital_id;
    }

    public void setHospital_id(int hospital_id) {
        this.hospital_id = hospital_id;
    }

    public String getHospital_name() {
        return hospital_name;
    }

    public void setHospital_name(String hospital_name) {
        this.hospital_name = hospital_name;
    }

    public String getHospital_image() {
        return hospital_image;
    }

    public void setHospital_image(String hospital_image) {
        this.hospital_image = hospital_image;
    }

    public String[] getSource() {
        return source;
    }

    public void setSource(String[] source) {
        this.source = source;
    }

    public String getDirect() {
        return direct;
    }

    public void setDirect(String direct) {
        this.direct = direct;
    }

    public HashMap<String, Session> getSessions() {
        return sessions;
    }

    public void setSessions(HashMap<String, Session> sessions) {
        this.sessions = sessions;
    }
}
