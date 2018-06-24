package lk.hemas.ayubo.model;

import java.io.Serializable;

public class Hospital implements Serializable {
    private String doctor_code;
    private String hospital_id;
    private String hospital_name;
    private String specialization_id;
    private String specialization_name;
    private String date;
    private String[] source;
    private String hospital_image;
    private String direct;

    public String getDoctor_code() {
        return doctor_code;
    }

    public void setDoctor_code(String doctor_code) {
        this.doctor_code = doctor_code;
    }

    public String getHospital_id() {
        return hospital_id;
    }

    public void setHospital_id(String hospital_id) {
        this.hospital_id = hospital_id;
    }

    public String getHospital_name() {
        return hospital_name;
    }

    public void setHospital_name(String hospital_name) {
        this.hospital_name = hospital_name;
    }

    public String getSpecialization_id() {
        return specialization_id;
    }

    public void setSpecialization_id(String specialization_id) {
        this.specialization_id = specialization_id;
    }

    public String getSpecialization_name() {
        return specialization_name;
    }

    public void setSpecialization_name(String specialization_name) {
        this.specialization_name = specialization_name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String[] getSource() {
        return source;
    }

    public void setSource(String[] source) {
        this.source = source;
    }

    public String getHospital_image() {
        return hospital_image;
    }

    public void setHospital_image(String hospital_image) {
        this.hospital_image = hospital_image;
    }

    public String getDirect() {
        return direct;
    }

    public void setDirect(String direct) {
        this.direct = direct;
    }
}
