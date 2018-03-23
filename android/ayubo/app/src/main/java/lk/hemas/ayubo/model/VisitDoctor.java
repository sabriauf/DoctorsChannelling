package lk.hemas.ayubo.model;

import java.io.Serializable;

/**
 * Created by Sabri on 3/17/2018. Model for Doctors information on visits
 */

public class VisitDoctor implements Serializable{

    private int DocID;
    private String DocCode;
    private String DoctorName;
    private int SpecialiseId;
    private String Specialisation;
    private int HospitalId;
    private String HospitalName;

    public int getDocID() {
        return DocID;
    }

    public void setDocID(int docID) {
        DocID = docID;
    }

    public String getDocCode() {
        return DocCode;
    }

    public void setDocCode(String docCode) {
        DocCode = docCode;
    }

    public String getDoctorName() {
        return DoctorName;
    }

    public void setDoctorName(String doctorName) {
        DoctorName = doctorName;
    }

    public int getSpecialiseId() {
        return SpecialiseId;
    }

    public void setSpecialiseId(int specialiseId) {
        SpecialiseId = specialiseId;
    }

    public String getSpecialisation() {
        return Specialisation;
    }

    public void setSpecialisation(String specialisation) {
        Specialisation = specialisation;
    }

    public int getHospitalId() {
        return HospitalId;
    }

    public void setHospitalId(int hospitalId) {
        HospitalId = hospitalId;
    }

    public String getHospitalName() {
        return HospitalName;
    }

    public void setHospitalName(String hospitalName) {
        HospitalName = hospitalName;
    }
}
